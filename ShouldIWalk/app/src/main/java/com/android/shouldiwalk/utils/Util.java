package com.android.shouldiwalk.utils;

import android.content.Context;

public class Util {

    public static float dpFromPx(final Context context, final float px) {
        return px / context.getResources().getDisplayMetrics().density;
    }
}
