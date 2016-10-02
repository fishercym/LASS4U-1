package farm.rododo.lass4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.cht.android.KeepScreenOn;
import com.cht.android.Log;
import com.cht.android.StringById;
import com.cht.android.ViewUtils;
import com.cht.android.ViewVisitor;
import com.cht.iot.android.AndroidUtils;
import com.cht.iot.persistence.entity.data.Rawdata;
import com.cht.iot.service.api.OpenMqttClient;
import com.cht.iot.service.api.OpenRESTfulClient;
import com.cht.iot.util.JsonUtils;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.slf4j.Logger;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@KeepScreenOn
public class DashboardActivity extends AppCompatActivity {
    @Log
    Logger LOG; // assign by ViewUtils.bind()

    // assign by ViewVisitor
    Map<String, SensorView> sensorViews = Collections.synchronizedMap(new HashMap<String, SensorView>()); // sensorId -> SensorView

    @StringById(R.string.iot_host)
    String host;

    OpenRESTfulClient restful;
    OpenMqttClient mqtt;

    ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

    protected SharedPreferences getSharedPreferences() {
        return getSharedPreferences("configurations", MODE_PRIVATE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ViewUtils.bind(this);
        ViewUtils.visit(this, new MemberViewVisitor());
    }

    class MemberViewVisitor implements ViewVisitor {
        final Resources resources;

        public MemberViewVisitor() {
            resources = getResources();
        }

        public String nameToSensorId(String name) {
            System.out.println(name);

            if (name.startsWith("mC")) {
                return name.substring(1); // mCH1 -> CH1
            }

            return null;
        }

        @Override
        public boolean visit(View view) {
            try {
                String name = resources.getResourceEntryName(view.getId()); // View's name
                String sensorId = nameToSensorId(name); // View Name -> SensorId
                if (sensorId != null) {
                    SensorView sv = new SensorView(); // HINT - different SensorViews could have the same sensorId
                    sv.sensorId = sensorId;
                    sv.view = view;

                    sensorViews.put(sensorId, sv);
                }
            } catch (Resources.NotFoundException e) {
                // skip
            }

            return true; // continue to visit
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Build connection if you had the configurations saved.
        SharedPreferences preferences = getSharedPreferences();
        final String apiKey = preferences.getString("apiKey", null);
        final String deviceId = preferences.getString("deviceId", null);
        if ((apiKey != null) && (deviceId != null)) {
            executor.execute(new Runnable() {
              @Override
              public void run() {
                  init(apiKey, deviceId);
              }
          });
        }
    }

    @Override
    protected void onDestroy() {
        if (mqtt != null) {
            mqtt.stop();
        }

        super.onDestroy();
    }

    // ======

    protected void onScan(View view) {
        IntentIntegrator ii = new IntentIntegrator(this);
        ii.initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            String contents = result.getContents(); // DK0YZPG9B7779RG53C,816709561
            if (contents != null) {
                int i = contents.indexOf(',');
                if (i > 0) {
                    final String apiKey = contents.substring(0, i);
                    final String deviceId = contents.substring(i + 1);

                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            init(apiKey, deviceId);
                        }
                    });
                } else {
                    Toast.makeText(this, getString(R.string.incorrect_qr_code), Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    // ======

    protected void init(String apiKey, String deviceId) {
        restful = new OpenRESTfulClient(host, 443, apiKey);
        restful.enableTls(true);

        try {
            if (mqtt != null) {
                mqtt.stop();
            }

            mqtt = new OpenMqttClient(host, 8883, apiKey, true);
            mqtt.setListener(new OpenMqttClient.ListenerAdapter() {
                @Override
                public void onRawdata(String topic, Rawdata rawdata) {
                    onRawdataChanged(rawdata);
                }
            });

            for (String sensorId : sensorViews.keySet()) {
                mqtt.subscribe(deviceId, sensorId);
            }

            mqtt.start();

            SharedPreferences preferences = getSharedPreferences();
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("apiKey", apiKey);
            editor.putString("deviceId", deviceId);
            editor.commit();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);

            Toast.makeText(this, getString(R.string.illegal_key) + " - " + e.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
        }
    }

    // ======

    public void onRawdataChanged(final Rawdata rawdata) {
        LOG.info(JsonUtils.toJson(rawdata));

        String sensorId = rawdata.getId();
        final SensorView sv = sensorViews.get(sensorId);
        if (sv != null) {
            String value = rawdata.getValue()[0];
            if (value.startsWith("snapshot://")) {
                AndroidUtils.presentSnapshot(rawdata, restful, executor, this, (ImageView) sv.view);

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        sv.setValue(rawdata.getValue()[0]);
                    }
                });
            }
        }
    }
}
