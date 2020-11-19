package com.alex_podolian.npuzzle.utils;

import android.util.Log;

import com.alex_podolian.npuzzle.BuildConfig;

public class RLogs {
    private static final String DEFAULT_TAG = "<-- N-Puzzle -->";

    public static void d(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(DEFAULT_TAG, message);
        }
    }

    public static void e(String message) {
        if (BuildConfig.DEBUG) {
            Log.e(DEFAULT_TAG, message);
        }
    }

    public static void w(String message) {
        if (BuildConfig.DEBUG) {
            Log.w(DEFAULT_TAG, message);
        }
    }

    public static void i(String message) {
        if (BuildConfig.DEBUG) {
            Log.i(DEFAULT_TAG, message);
        }
    }

    public static void e(Throwable throwable) {
        if (BuildConfig.DEBUG) {
            throwable.printStackTrace();
            Log.e(DEFAULT_TAG, throwable.getMessage(), throwable);
        }
    }
}
