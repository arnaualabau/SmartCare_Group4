package com.example.smartcare_group4.utils;

import static com.example.smartcare_group4.data.constants.Generic.DebugConstants.LEVEL_DEBUG;
import static com.example.smartcare_group4.data.constants.Generic.DebugConstants.LEVEL_ERROR;
import static com.example.smartcare_group4.data.constants.Generic.DebugConstants.LEVEL_INFO;
import static com.example.smartcare_group4.data.constants.Generic.DebugConstants.LEVEL_VERBOSE;

import android.util.Log;

import com.example.smartcare_group4.BuildConfig;

public class PrintLog {

    private static final String DEBUGTAG = "[MET] - ";

    public static void debug(String TAG, String message) {
        if (BuildConfig.DEBUG) {
            Log.i(DEBUGTAG, "[" + TAG + "] --- " + message);
        }
    }

    public static void debugWithLevel(String TAG, int level, String message) {
        if (BuildConfig.DEBUG) {
            String debugMessage = "[" + TAG + "] --- " + message;
            switch (level) {
                case LEVEL_ERROR:
                    Log.e(DEBUGTAG, debugMessage);
                    break;
                case LEVEL_DEBUG:
                    Log.d(DEBUGTAG, debugMessage);
                    break;
                case LEVEL_INFO:
                    Log.i(DEBUGTAG, debugMessage);
                    break;
                default:
                case LEVEL_VERBOSE:
                    Log.v(DEBUGTAG, debugMessage);
                    break;
            }
        }
    }

}
