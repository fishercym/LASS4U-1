package farm.rododo.lass4u;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.StringTokenizer;

/**
 * Created by rickwang on 2016/9/27.
 */

public class MeterView extends RelativeLayout implements Displayer {
    static final int DEFAULT_PADDING = 32;
    static final int[] COLORS = { Color.GRAY, Color.GREEN, Color.YELLOW, Color.RED };

    TextView value;
    TextView unit;
    TinyMeter chart;
    TextView name;

    public MeterView(Context context) {
        super(context);

        init();
    }

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();

        update(context, attrs);
    }

    public MeterView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        init();

        update(context, attrs);
    }

    protected void init() {
        inflate(getContext(), R.layout.view_meter, this);

        value = (TextView) findViewById(R.id.value);
        unit = (TextView) findViewById(R.id.unit);
        chart = (TinyMeter) findViewById(R.id.chart);
        name = (TextView) findViewById(R.id.name);
    }

    protected void update(Context context, AttributeSet attrs) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.MeterView, 0, 0);

        try {
            String s = a.getString(R.styleable.MeterView_name);
            if (s != null) {
                name.setText(s);
            }

            s = a.getString(R.styleable.MeterView_unit);
            if (s != null) {
                unit.setText(s);
            }

            s = a.getString(R.styleable.MeterView_degress);

            if (s != null) {
                StringTokenizer st = new StringTokenizer(s, ",; ");
                int i = 0;
                while (st.hasMoreTokens()) {
                    addDegree(Float.parseFloat(st.nextToken()), COLORS[i++]);
                }
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

    // ======

    public void addDegree(float threshold, @ColorInt int color) {
        chart.addDegree(threshold, color);
    }

    @Override
    public void setValue(float v) {
        value.setText(String.format("%.2f", v));
        chart.setValue(v);

        int w = getWidth();
        int h = getHeight();

        resizeValue(w, h);

//        value.requestLayout();
        requestLayout();
    }
}
