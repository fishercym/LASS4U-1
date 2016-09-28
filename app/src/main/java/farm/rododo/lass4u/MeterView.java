package farm.rododo.lass4u;

import android.content.Context;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by rickwang on 2016/9/27.
 */

public class MeterView extends RelativeLayout {
    TextView value;
    TextView unit;
    TinyMeter meter;
    TextView name;

    public MeterView(Context context) {
        super(context);

        init();
    }

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public MeterView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        init();
    }

    protected void init() {
        inflate(getContext(), R.layout.view_meter, this);

        value = (TextView) findViewById(R.id.value);
        unit = (TextView) findViewById(R.id.unit);
        meter = (TinyMeter) findViewById(R.id.meter);
        name = (TextView) findViewById(R.id.name);
    }

    public void addDegree(float threshold, @ColorInt int color) {
        meter.addDegree(threshold, color);
    }

    public void setValue(float v) {
        value.setText(String.format("%.2f", v));
        meter.setValue(v);
    }
}
