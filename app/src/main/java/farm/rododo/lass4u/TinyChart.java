package farm.rododo.lass4u;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Created by rickwang on 2016/9/25.
 */

public class TinyChart extends View {
    Rect size;

    Bitmap bitmap;
    Canvas canvas;

    Paint paint = new Paint();

    float horizontalSidePadding = 4f;
    float verticalSidePadding = 8f;
    float barPadding = 2f;
    float barWidth = 8f;
    float fillRatio = 1.0f;

    int maxCount = 8;
    LinkedList<Float> values = new LinkedList<>();

    public TinyChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TinyChart(Context context) {
        super(context);
    }

    // ======

    public void setColor(int color) {
        paint.setColor(color);
    }

    public void setHorizontalSidePadding(float horizontalSidePadding) {
        this.horizontalSidePadding = horizontalSidePadding;

        reconfigure();
    }

    public void setVerticalSidePadding(float verticalSidePadding) {
        this.verticalSidePadding = verticalSidePadding;
    }

    public void setBarPadding(float barPadding) {
        this.barPadding = barPadding;

        reconfigure();
    }

    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;

        reconfigure();
    }

    public void setFillRatio(float fillRatio) {
        this.fillRatio = fillRatio;
    }

    public int getMaxCount() {
        return maxCount;
    }

    // ======

    public void clear() {
        values.clear();
    }

    public void add(float value) {
        values.add(value);

        sweap();

        invalidate();
    }

    // ======

    protected void reconfigure() {
        maxCount = (int) ((size.width() - horizontalSidePadding - horizontalSidePadding - barPadding) / (barWidth + barPadding));

        sweap();
    }

    protected void sweap() {
        if (isInEditMode()) {
            return;
        }

        while (values.size() > maxCount) {
            values.removeFirst();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = MeasureSpec.getSize(widthMeasureSpec);
        if (w > 640) {
            w = 640;
        }

        int h = MeasureSpec.getSize(heightMeasureSpec);
        if (h > 480) {
            h = 480;
        }

        setMeasuredDimension(w, h);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        size = new Rect(0, 0, w, h);

        reconfigure();

        System.out.printf("%d, %d", size.width(), size.height());

        bitmap = Bitmap.createBitmap(size.width(), size.height(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas c) {
        drawBackground(canvas);

        drawValues(canvas);

        c.drawBitmap(bitmap, 0f, 0f, paint);
    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    protected void drawValues(Canvas canvas) {
        if (values.isEmpty()) {
            return;
        }

        int h = size.height();

        float max = Collections.max(values);
        float ratio = fillRatio * (h - verticalSidePadding - verticalSidePadding) / max;

        float bottom = h - verticalSidePadding;

        float leftPadding = horizontalSidePadding + barPadding;

        int i = 0;
        for (float value : values) {
            float left = leftPadding + ((barPadding + barWidth) * i);
            float right = left + barWidth;
            float top = bottom - (value * ratio);

            canvas.drawRect(left, top, right, bottom, paint);

            i++;
        }
    }
}
