package com.android.shouldiwalk.utils;

import android.content.Context;
import android.os.Build;

public class Util {

    public static boolean isRoboUnitTest() {
        return "robolectric".equals(Build.FINGERPRINT);
    }
}
