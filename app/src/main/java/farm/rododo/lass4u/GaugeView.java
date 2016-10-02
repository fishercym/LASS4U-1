package farm.rododo.lass4u;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.cht.android.ViewById;
import com.cht.android.view.TinyChart;

/**
 * Created by rickwang on 2016/9/27.
 */

public class GaugeView extends Meter {

    @ViewById(R.id.chart)
    TinyChart chart;

    public GaugeView(Context context) {
        super(context);
    }

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GaugeView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    protected int getInflateResId() {
        return R.layout.view_gauge;
    }

    @Override
    protected TypedArray obtainTypedArray(Context context, AttributeSet attrs) {
        return context.getTheme().obtainStyledAttributes(attrs, R.styleable.GaugeView, 0, 0);
    }

    protected void update(TypedArray a) {
        String s = a.getString(R.styleable.GaugeView_name);
        if (s != null) {
            name.setText(s);
        }

        s = a.getString(R.styleable.GaugeView_unit);
        if (s != null) {
            unit.setText(s);
        }
    }

    @Override
    public void setValue(float v) {
        value.setText(String.format("%.2f", v));
        chart.add(v);

        resizeValue();

        value.requestLayout();
    }
}
