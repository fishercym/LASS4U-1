package farm.rododo.lass4u;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cht.android.ViewById;
import com.cht.android.ViewUtils;

/**
 * Created by rickwang on 2016/9/28.
 */

public abstract class Meter extends RelativeLayout {
    static final int DEFAULT_PADDING = 16;

    @ViewById(R.id.value)
    TextView value;
    @ViewById(R.id.unit)
    TextView unit;
    @ViewById(R.id.name)
    TextView name;

    public Meter(Context context) {
        super(context);

        init();
    }

    public TextView getValue() {
        return value;
    }

    public TextView getUnit() {
        return unit;
    }

    public TextView getName() {
        return name;
    }

    public Meter(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
        update(context, attrs);
    }

    public Meter(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);

        init();
        update(context, attrs);
    }

    protected abstract int getInflateResId();

    protected void init() {
        inflate(getContext(), getInflateResId(), this);

        ViewUtils.bind(this);
    }

    protected abstract TypedArray obtainTypedArray(Context context, AttributeSet attrs);

    protected void update(Context context, AttributeSet attrs) {
        TypedArray a = obtainTypedArray(context, attrs);

        try {
            update(a);

        } finally {
            a.recycle();
        }
    }

    protected abstract void update(TypedArray a);

    protected void resizeName() {
        TextPaint paint = name.getPaint();
        Paint.FontMetrics fm = paint.getFontMetrics();

        float ww = name.getMeasuredWidth() - DEFAULT_PADDING;
        float fw = paint.measureText(name.getText().toString());
        float rw = ww / fw;

        float hh = name.getMeasuredHeight() - DEFAULT_PADDING;
        float fh = fm.bottom - fm.top;
        float rh = hh / fh;

        float r = Math.min(rw, rh);

        paint.setTextSize(paint.getTextSize() * r);
    }

    protected void resizeValue() {
        TextPaint vpaint = value.getPaint();
        Paint.FontMetrics vfm = vpaint.getFontMetrics();
        float vfw = vpaint.measureText(value.getText().toString());

        TextPaint upaint = unit.getPaint();
        float ufw = upaint.measureText(unit.getText().toString());

        float ww = getMeasuredWidth() - DEFAULT_PADDING;
        float fw = vfw + ufw;
        float rw = ww / fw;

        float hh = value.getMeasuredHeight() - DEFAULT_PADDING;
        float fh = vfm.bottom - vfm.top; // unit letter's height is lower, skip it
        float rh = hh / fh;

        float r = Math.min(rw, rh);

        vpaint.setTextSize(vpaint.getTextSize() * r);
        upaint.setTextSize(upaint.getTextSize() * r);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        resizeName();
        resizeValue();
    }

    public abstract void setValue(float value);
}
