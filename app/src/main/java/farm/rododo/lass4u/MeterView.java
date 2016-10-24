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

import com.cht.android.ViewById;
import com.cht.android.view.TinyMeter;

import java.util.StringTokenizer;

/**
 * Created by rickwang on 2016/9/27.
 */

public class MeterView extends Meter {
    static final int[] COLORS = { Color.GREEN, Color.YELLOW, Color.RED, Color.MAGENTA, Color.DKGRAY };

    @ViewById(R.id.chart)
    TinyMeter chart;

    public MeterView(Context context) {
        super(context);
    }

    public MeterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MeterView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    @Override
    protected int getInflateResId() {
        return R.layout.view_meter;
    }

    @Override
    protected TypedArray obtainTypedArray(Context context, AttributeSet attrs) {
        return context.getTheme().obtainStyledAttributes(attrs, R.styleable.MeterView, 0, 0);
    }

    protected void update(TypedArray a) {
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
    }

    // ======

    public void addDegree(float threshold, @ColorInt int color) {
        chart.addDegree(threshold, color);
    }

    @Override
    public void setValue(float v) {
        value.setText(String.format("%d", (int) v));
        chart.setValue(v);

        resizeValue();

        value.requestLayout();
    }
}
