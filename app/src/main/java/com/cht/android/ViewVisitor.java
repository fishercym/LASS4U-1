package com.cht.android;

import android.view.View;

/**
 * Created by rickwang on 2016/07/07.
 */
public interface ViewVisitor {

    /**
     *
     * @param view
     *
     * @return  continue or not
     */
    boolean visit(View view);
}
