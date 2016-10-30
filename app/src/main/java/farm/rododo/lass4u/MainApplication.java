package farm.rododo.lass4u;

import android.app.Application;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by rickwang on 2016/10/30.
 */

public class MainApplication extends Application {
    public static int WHAT_RAWDATA = 1000;
    public static int WHAT_BITMAP = 1002;

    List<Handler> handlers = Collections.synchronizedList(new ArrayList<Handler>());

    Bitmap snapshot;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public void addHandler(Handler handler) {
        handlers.add(handler);
    }

    public void removeHandler(Handler handler) {
        handlers.remove(handler);
    }

    public void broadcast(Message message) {
        synchronized (handlers) {
            for (Handler handler : handlers) {
                handler.sendMessage(message);
            }
        }
    }

    public Bitmap getSnapshot() {
        return snapshot;
    }

    public void setSnapshot(Bitmap snapshot) {
        this.snapshot = snapshot;
    }
}
