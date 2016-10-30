package farm.rododo.lass4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import com.cht.android.App;
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

    @App
    MainApplication application;

    Handler handler;

    @ViewById(R.id.snapshot)
    ImageView snapshot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ViewUtils.bind(this);

        Bitmap bmp = application.getSnapshot();
        AndroidUtils.presentSnapshot(bmp, this, snapshot);

        // register a handler to receive the snapshot from DashboardActivity
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                onMessage(msg);
            }
        };
        application.addHandler(handler);
    }

    @Override
    protected void onDestroy() {
        application.removeHandler(handler);

        super.onDestroy();
    }

    protected void onMessage(Message message) {
        if (message.what == MainApplication.WHAT_BITMAP) {
            Bitmap bmp = (Bitmap) message.obj;

            AndroidUtils.presentSnapshot(bmp, this, snapshot);
        }
    }
}
