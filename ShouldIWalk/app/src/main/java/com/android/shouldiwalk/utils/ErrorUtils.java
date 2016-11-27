package com.android.shouldiwalk.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class ErrorUtils {
    public static void logException(String tag, Exception e) {
        Log.e(tag, getStackTraceAsString(e));
    }

    public static String getStackTraceAsString(Exception e) {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        e.printStackTrace(new PrintStream(bos));
        return bos.toString();
    }
}
