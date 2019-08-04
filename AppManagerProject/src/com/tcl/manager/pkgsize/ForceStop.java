package com.tcl.manager.pkgsize;

import java.lang.reflect.Method;
import android.app.ActivityManager;
import android.content.Context;
import com.tcl.framework.log.NLog;
import com.tcl.manager.application.ManagerApplication;

/**
 * 反射调用系统杀进程的方法，本方法需要系统签名
 * 
 * @author jiaquan.huang
 * 
 */
public class ForceStop {
    private static String TAG = ForceStop.class.getName();
    private static Method forceStop;
    static {
        try {
            forceStop = ActivityManager.class.getMethod("forceStopPackage", String.class);
        } catch (Exception e) {
            NLog.e(TAG, e);
        }
    }

    public static boolean forceStopPackage(Context mComtext, String packageName) {
        try {
            ActivityManager mActivityManager = (ActivityManager) mComtext.getSystemService(Context.ACTIVITY_SERVICE);
            forceStop.invoke(mActivityManager, packageName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
