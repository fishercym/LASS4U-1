package com.cht.android;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by rickwang on 2016/7/6.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ViewById {
    /**
     * Resource ID of the View
     *
     * @return
     */
    int value();
}
