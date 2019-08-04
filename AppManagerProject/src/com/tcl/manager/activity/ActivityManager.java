package com.tcl.manager.activity;

import android.app.Activity;

/**
 * @author difei.zou
 * @Description:
 * @date 2014/9/29 15:10
 * @copyright TCL-MIE
 */

public class ActivityManager {


    private static Activity topActivity;

    public static Activity getTopActivity() {
        return topActivity;
    }

    public static void setTopActivity(Activity topActivity) {
        ActivityManager.topActivity = topActivity;
    }


    private static class AcitvityHolder {
        public final static ActivityManager INSTANCE = new ActivityManager();
    }

    public ActivityManager getInstance() {
        return AcitvityHolder.INSTANCE;
    }
}
