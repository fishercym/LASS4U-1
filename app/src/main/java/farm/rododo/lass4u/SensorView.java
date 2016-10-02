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
        if (view instanceof Meter) {
            Meter meter = (Meter) view;
            meter.setValue(Float.parseFloat(value));

        } else if (view instanceof TextView) {
            TextView text = (TextView) view;
            text.setText(String.format("%s %s", value, (unit != null)? unit : ""));
        }
    }
}
