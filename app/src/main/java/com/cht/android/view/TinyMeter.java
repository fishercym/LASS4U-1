package com.cht.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.Shader;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Created by rickwang on 2016/9/27.
 */

public class TinyMeter extends View {
    Rect size;

    Bitmap bitmap;
    Canvas canvas;

    Paint paint = new Paint();

    float horizontalSidePadding = 0f;

    float barWidth = 24f;
    @ColorInt int barColor = Color.BLACK;
    float value;

    float degreeWidth = 48f;
    List<Degree> degrees = new ArrayList<>();

    public TinyMeter(Context context) {
        super(context);
    }

    public TinyMeter(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TinyMeter(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    public void setHorizontalSidePadding(float horizontalSidePadding) {
        this.horizontalSidePadding = horizontalSidePadding;
    }

    public void setBarWidth(float barWidth) {
        this.barWidth = barWidth;
    }

    public void setBarColor(int barColor) {
        this.barColor = barColor;
    }

    public void setDegreeWidth(float degreeWidth) {
        this.degreeWidth = degreeWidth;
    }

    public void addDegree(float threshold, @ColorInt int color) {
        degrees.add(new Degree(threshold, color));

        Collections.sort(degrees);
    }

    public void setValue(float value) {
        this.value = value;

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (w < 16) w = 16;
        if (h < 16) h = 16;

        size = new Rect(0, 0, w, h);

        bitmap = Bitmap.createBitmap(size.width(), size.height(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);
    }

    @Override
    protected void onDraw(Canvas c) {
        if (isInEditMode()) {
            return;
        }

        drawBackground(canvas);

        drawThem(canvas);

        c.drawBitmap(bitmap, 0f, 0f, paint);
    }

    protected void drawBackground(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    protected void drawThem(Canvas canvas) {
        if (degrees.isEmpty()) {
            return;
        }

        int w = size.width();
        int h = size.height();

        int s = degrees.size();
        Degree max = degrees.get(s - 1); // last degree is the maximum one
        float ratio = (w - horizontalSidePadding - horizontalSidePadding) / max.threshold;

        float left = horizontalSidePadding;
        float top = (h - degreeWidth) / 2.0f;
        float bottom = top + degreeWidth;

        Iterator<Degree> it = degrees.iterator();
        if (it.hasNext()) {
            Degree degree = it.next();
            do {
                Degree next = null;
                if (it.hasNext()) {
                    next = it.next();
                }

                int color;
                
                float right = horizontalSidePadding;
                if (next != null) {
                    right += ((degree.threshold + next.threshold) / 2) * ratio;
                    color = next.color;

                } else {
                    right += (degree.threshold * ratio);
                    color = Color.DKGRAY;
                }

                Paint paint = new Paint();
                Shader shader = new LinearGradient(left, top, right, top, degree.color, color, Shader.TileMode.CLAMP);
                paint.setShader(shader);

                canvas.drawRect(left, top, right, bottom, paint);

                left = right;
                degree = next;

            } while (degree != null);
        }

        left = horizontalSidePadding;

        top = (h - barWidth) / 2.0f;
        bottom = top + barWidth;
        float right = horizontalSidePadding + (value * ratio);

        paint.setColor(barColor);
        canvas.drawRect(left, top, right, bottom, paint);
    }

    static class Degree implements Comparable<Degree> {
        float threshold;
        @ColorInt int color;

        public Degree(float threshold, @ColorInt int color) {
            this.threshold = threshold;
            this.color = color;
        }

        @Override
        public int compareTo(Degree o) {
            return ((threshold - o.threshold) > 0)? 1 : -1;
        }
    }
}
