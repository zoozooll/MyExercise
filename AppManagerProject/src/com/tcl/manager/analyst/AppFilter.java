package com.tcl.manager.analyst;

import android.content.Context;
import com.tcl.manager.blackwhitelist.WhiteListSharedManager;
import com.tcl.manager.util.PkgManagerTool;

/**
 * 过滤不需要展示的App
 * 
 * @author jiaquan.huang
 * 
 */
public class AppFilter {
    private java.util.Collection<String> packageName;
    private static AppFilter instance;

    private AppFilter() {
        load();
    }

    public static AppFilter getInstance() {
        synchronized (AppFilter.class) {
            if (instance == null) {
                instance = new AppFilter();
            }
        }
        return instance;
    }

    public void load() {
        packageName = WhiteListSharedManager.getSingleInstance().getAllWhiteList();
    }

    /** 是否本程序，需要展示这个APP **/
    public boolean isNeedShow(Context mContext, String pkgName) {
        boolean isInWhiteList = false;
        if (packageName == null) {
            isInWhiteList = false;
        } else {
            isInWhiteList = packageName.contains(pkgName);
        }
        boolean isUserApp = PkgManagerTool.isDefaultUserApp(mContext, pkgName);
        if (isUserApp == true && isInWhiteList == false) {
            return true;
        } else {
            return false;
        }

    }
}
