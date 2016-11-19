package farm.rododo.lass4u;

import android.view.View;
import android.widget.TextView;

/**
 * Created by rickwang on 2016/09/14.
 */
public class SensorView {
    String sensorId;
    String unit;

    View view;

    public void setValue(String value) {
        float v = 0;
        try {
            v = Float.parseFloat(value);

        } catch(Exception e) {
        }

        if (view instanceof Meter) {
            Meter meter = (Meter) view;
            meter.setValue(v);

        } else if (view instanceof TextView) {
            TextView text = (TextView) view;
            text.setText(String.format("%.0f %s", v, (unit != null)? unit : ""));
        }
    }
}
