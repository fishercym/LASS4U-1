package farm.rododo.lass4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.cht.android.Log;
import com.cht.android.StringById;
import com.cht.android.ViewById;
import com.cht.android.ViewUtils;
import com.cht.iot.android.AndroidUtils;
import com.cht.iot.persistence.entity.data.Rawdata;
import com.cht.iot.service.api.OpenMqttClient;
import com.cht.iot.service.api.OpenRESTfulClient;
import com.cht.iot.util.JsonUtils;

import org.slf4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VideoActivity extends AppCompatActivity {
    @Log
    Logger LOG; // assign by ViewUtils.bind()

    @StringById(R.string.iot_host)
    String host;

    String apiKey;
    String deviceId;
    String sensorId = "image";

    OpenRESTfulClient restful;
    OpenMqttClient mqtt;

    @ViewById(R.id.snapshot)
    ImageView snapshot;

    ExecutorService executor = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ViewUtils.bind(this);

        Intent intent = getIntent();

        apiKey = intent.getStringExtra("apiKey");
        deviceId = intent.getStringExtra("deviceId");

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

            mqtt.subscribe(deviceId, sensorId);

            mqtt.start();

        } catch (Exception e) {
            LOG.error(e.getMessage(), e);

            Toast.makeText(this, getString(R.string.illegal_key) + " - " + e.getClass().getSimpleName(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        mqtt.stop();

        super.onDestroy();
    }

    public void onRawdataChanged(final Rawdata rawdata) {
        LOG.info(JsonUtils.toJson(rawdata));

        AndroidUtils.presentSnapshot(rawdata, restful, executor, this, snapshot);
    }
}
