package com.example.liujianglong.testapp;

import android.content.res.Resources;

/**
 * Created by liujianglong on 2017/9/11.
 */

public class DisplayUtil {

    public static float dip2px(float dpValue) {
        float scale = Resources.getSystem().getDisplayMetrics().density;
        return scale * dpValue + 0.5f;
    }
}
