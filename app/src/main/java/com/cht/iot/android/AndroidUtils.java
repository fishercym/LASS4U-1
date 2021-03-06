package com.cht.iot.android;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.widget.ImageView;

import com.cht.iot.persistence.entity.data.Rawdata;
import com.cht.iot.service.api.OpenRESTfulClient;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;

/**
 * Created by rickwang on 2016/07/07.
 */
public class AndroidUtils {
    static final Logger LOG = LoggerFactory.getLogger(AndroidUtils.class);

    static final String SNAPSHOT_PREFIX = "snapshot://";
    static final int SNAPSHOT_PREFIX_LENGTH = SNAPSHOT_PREFIX.length();

    public static final void presentSnapshot(final Rawdata rawdata, final OpenRESTfulClient client, final ExecutorService executor, final Activity activity, final ImageView view, final OnSnapshotListener listener) {
        String[] value = rawdata.getValue();
        if ((value != null) && (value.length > 0)) {
            String sid = value[0];

            if (sid.startsWith(SNAPSHOT_PREFIX)) {
                final String sensorId = rawdata.getId();
                final String imageId = sid.substring(SNAPSHOT_PREFIX_LENGTH); // get the snapshot ID (should be UUID format)

                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            InputStream is = client.getSnapshotBody(rawdata.getDeviceId(), sensorId, imageId); // read the snapshot
                            final Bitmap bmp = BitmapFactory.decodeStream(is);
                            if (bmp != null) { // The image could not be decoded!! Don't set NULL to ImageView.
                                if (listener != null) {
                                    listener.onSnapshot(bmp);
                                }

                                presentSnapshot(bmp, activity, view);
                            }
                        } catch (Exception e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                });
            }
        }
    }

    public static final void presentSnapshot(final Bitmap bmp, final Activity activity, final ImageView view) {
        if (bmp != null) { // The image could not be decoded!! Don't set NULL to ImageView.
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    view.setImageBitmap(bmp);
                    view.invalidate();
                }
            });
        }
    }

    public interface OnSnapshotListener {
        void onSnapshot(Bitmap bmp);
    }
}
