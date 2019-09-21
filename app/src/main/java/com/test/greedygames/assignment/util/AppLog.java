package com.test.greedygames.assignment.util;

public class AppLog {

    public final static String TAG = "RECIPE";

    public final static boolean LOG_ENABLED = true;

    public static void i(String message) {
        if (LOG_ENABLED)
            android.util.Log.i(TAG, message);
    }

    public static void i(String tag, String message) {
        if (LOG_ENABLED)
            android.util.Log.i(tag, message);
    }
    public static void v(String message) {
        if (LOG_ENABLED)
            android.util.Log.v(TAG, message);
    }

    public static void v(String tag, String message) {
        if (LOG_ENABLED)
            android.util.Log.v(tag, message);
    }

    public static void d(String message) {
        if (LOG_ENABLED)
            android.util.Log.d(TAG, message);
    }

    public static void d(String tag, String message) {
        if (LOG_ENABLED)
            android.util.Log.d(tag, message);
    }

    public static void e(String message) {
        if (LOG_ENABLED)
            android.util.Log.e(TAG, message);
    }

    public static void e(String tag, String message) {
        if (LOG_ENABLED)
            android.util.Log.e(tag, message);
    }

    public static void w(String message) {
        if (LOG_ENABLED)
            android.util.Log.w(TAG, message);
    }

    public static void w(String tag, String message) {
        if (LOG_ENABLED)
            android.util.Log.w(tag, message);
    }
}
