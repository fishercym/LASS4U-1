package com.cht.android.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.SystemClock;
import android.support.annotation.RawRes;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by rickwang on 2016/9/26.
 */

public class GifView extends View {
    static final int DEFAULT_DURATION = 1000;

    Movie movie;
    int duration = DEFAULT_DURATION;

    Bitmap bitmap; // draw by Movie
    Canvas canvas;

    Rect src; // Movie's size
    Rect dst; // View's size

    Paint paint = new Paint();

    Runnable redrawing = new Runnable() {
        @Override
        public void run() {
            invalidate();
        }
    };

    public GifView(Context context) {
        super(context);
    }

    public GifView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GifView(Context context, AttributeSet attrs, int style) {
        super(context, attrs, style);
    }

    public void setImageRawResource(@RawRes int rawResId) {
        movie = Movie.decodeStream(getContext().getResources().openRawResource(rawResId));

        src = new Rect(0, 0, movie.width(), movie.height());

        duration = movie.duration();
        if (duration == 0) {
            duration = DEFAULT_DURATION;
        }

        bitmap = Bitmap.createBitmap(src.width(), src.height(), Bitmap.Config.ARGB_8888);
        canvas = new Canvas(bitmap);

        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        dst = new Rect(0, 0, w, h);
    }

    @Override
    protected void onDraw(Canvas c) {
        if (movie == null) {
            return;
        }

        int time = (int) (SystemClock.uptimeMillis() % duration);
        movie.setTime(time);

        movie.draw(canvas, 0, 0);

        c.drawBitmap(bitmap, src, dst, paint); // resize

        postDelayed(redrawing, 0L);
    }
}
