package com.android.shouldiwalk.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {
    public static void showToast(Context context, int length, String message) {
        Toast.makeText(context, message, length).show();
    }
}
