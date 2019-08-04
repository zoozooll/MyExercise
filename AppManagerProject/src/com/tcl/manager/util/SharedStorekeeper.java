package com.tcl.manager.util;

import com.tcl.framework.log.NLog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @Description:SharedPreferences
 * @author jiaquan.huang
 * @date 2014-12-12 上午9:40:18
 * @copyright TCL-MIE
 */

public class SharedStorekeeper {
    final static String STOREKEEPERNAME = "xlm_data";
    public static final String LOG_LAST_REPORT_TIME = "LAST_REPORT_TIME";
    public static final String DEVEICE_LAST_REPORT_TIME = "LAST_DEVICE_TIME";
    public static final String FIRST_TIME_RUNNING = "FIRST_RUNNING";
    public static final String WHITELIST_LAST_REPORTED_TIME = "whitelist_reported";
    static SharedPreferences storeKeeper;
    private static String TAG = "SharedStorekeeper";

    public static void init(Context context) {
        storeKeeper = context.getSharedPreferences(STOREKEEPERNAME, Context.MODE_PRIVATE);
    }

    public static boolean save(String key, String value) {
        if (storeKeeper == null) {
            NLog.w(TAG, "storeKeeper is null");
            return false;
        }
        Editor edit = storeKeeper.edit();
        edit.putString(key, value);
        edit.commit();
        return true;
    }

    public static String get(String key) {
        if (storeKeeper == null) {
            NLog.w(TAG, "storeKeeper is null");
            return "";
        }
        return storeKeeper.getString(key, "");
    }

    public static boolean save(String key, long value) {
        if (storeKeeper == null) {
            NLog.w(TAG, "storeKeeper is null");
            return false;
        }
        Editor edit = storeKeeper.edit();
        edit.putLong(key, value);
        edit.commit();
        return true;
    }

    public static long getLong(String key) {
        if (storeKeeper == null) {
            NLog.w(TAG, "storeKeeper is null");
            return 0;
        }
        return storeKeeper.getLong(key, 0);
    }

    public static void clean(String key) {
        if (storeKeeper == null) {
            NLog.w(TAG, "storeKeeper is null");
            return;
        }
        Editor edit = storeKeeper.edit();
        edit.remove(key);
        edit.commit();
    }

}
