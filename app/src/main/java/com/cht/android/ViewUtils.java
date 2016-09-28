package com.cht.android;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

import com.cht.GreenException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;

/**
 * Created by rickwang on 2016/7/6.
 */
public class ViewUtils {
    static final Logger LOG = LoggerFactory.getLogger(ViewUtils.class);

    public static final void bind(View view) {
        try {
            Class<?> clazz = view.getClass();

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Log.class)) {
                    field.setAccessible(true);
                    field.set(view, LoggerFactory.getLogger(clazz));
                }

                ViewById vid = field.getAnnotation(ViewById.class);
                if (vid != null) {
                    int id = vid.value();
                    field.setAccessible(true);
                    field.set(view, view.findViewById(id));
                }
            }
        } catch (Exception e) {
            throw new GreenException(e.getMessage(), e);
        }
    }

    public static final void bind(Activity activity) {
        try {
            Class<?> clazz = activity.getClass();

            if (clazz.isAnnotationPresent(KeepScreenOn.class)) {
                activity.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            }

            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(Log.class)) {
                    field.setAccessible(true);
                    field.set(activity, LoggerFactory.getLogger(clazz));
                }

                if (field.isAnnotationPresent(App.class)) {
                    field.setAccessible(true);
                    field.set(activity, activity.getApplication());
                }

                ViewById vid = field.getAnnotation(ViewById.class);
                if (vid != null) {
                    int id = vid.value();
                    field.setAccessible(true);
                    field.set(activity, activity.findViewById(id));
                }

                StringById sid = field.getAnnotation(StringById.class);
                if (sid != null) {
                    int id = sid.value();
                    field.setAccessible(true);
                    field.set(activity, activity.getString(id));
                }
            }
        } catch (Exception e) {
            throw new GreenException(e.getMessage(), e);
        }
    }

    public static final void visit(ViewGroup vg, ViewVisitor visitor) {
        int s = vg.getChildCount();
        for (int i = 0;i < s;i++) {
            View v = vg.getChildAt(i);

            if (!visitor.visit(v)) {
                break;
            }

            if (v instanceof ViewGroup) {
                visit((ViewGroup) v, visitor);
            }
        }
    }

    public static final void visit(Activity activity, ViewVisitor visitor) {
        ViewGroup vg = (ViewGroup) activity.findViewById(android.R.id.content);
        vg = (ViewGroup) vg.getChildAt(0);

        visit(vg, visitor);
    }
}
