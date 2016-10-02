package farm.rododo.lass4u;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.support.constraint.ConstraintLayout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.util.TypedValue.COMPLEX_UNIT_PT;
import static android.util.TypedValue.COMPLEX_UNIT_PX;

/**
 * Created by rickwang on 2016/9/27.
 */

public class GaugeView extends RelativeLayout implements Displayer {
    static final int DEFAULT_PADDING = 32;

    TextView value;
    TextView unit;
    TinyChart chart;
    TextView name;

    public GaugeView(Context context) {
        super(context);

        init();
    }

    public GaugeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        update(context, attrs);
    }

    public GaugeView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        init();
        update(context, attrs);
    }

    protected void init() {
        inflate(getContext(), R.layout.view_gauge, this);

        value = (TextView) findViewById(R.id.value);
        unit = (TextView) findViewById(R.id.unit);
        chart = (TinyChart) findViewById(R.id.chart);
        name = (TextView) findViewById(R.id.name);
    }

    protected void update(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.GaugeView, 0, 0);

        try {
            String s = a.getString(R.styleable.GaugeView_name);
            if (s != null) {
                name.setText(s);
            }

            s = a.getString(R.styleable.GaugeView_unit);
            if (s != null) {
                unit.setText(s);
            }

        } finally {
            a.recycle();
        }
    }

    protected void resizeName(int w, int h) {
        TextPaint paint = name.getPaint();
        Paint.FontMetrics fm = paint.getFontMetrics();

        float ww = w - DEFAULT_PADDING;
        float fw = paint.measureText(name.getText().toString());
        float rw = ww / fw;

        float hh = (h - chart.getHeight()) / 2 - DEFAULT_PADDING;
        float fh = fm.bottom - fm.top;
        float rh = hh / fh;

        float r = Math.min(rw, rh);

        paint.setTextSize(paint.getTextSize() * r);
    }

    protected void resizeValue(int w, int h) {
        TextPaint vpaint = value.getPaint();
        Paint.FontMetrics vfm = vpaint.getFontMetrics();
        float vfw = vpaint.measureText(value.getText().toString());

        TextPaint upaint = unit.getPaint();
        float ufw = upaint.measureText(unit.getText().toString());

        float ww = w - DEFAULT_PADDING;
        float fw = vfw + ufw;
        float rw = ww / fw;

        float hh = (h - chart.getHeight()) / 2 - DEFAULT_PADDING;
        float fh = vfm.bottom - vfm.top; // unit letter's height is lower, skip it
        float rh = hh / fh;

        float r = Math.min(rw, rh);

        vpaint.setTextSize(vpaint.getTextSize() * r);
        upaint.setTextSize(upaint.getTextSize() * r);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        resizeName(w, h);
        resizeValue(w, h);
    }

    @Override
    public void setValue(float v) {
        value.setText(String.format("%.2f", v));
        chart.add(v);

        int w = getWidth();
        int h = getHeight();

        resizeValue(w, h);

        value.requestLayout();
    }
}
