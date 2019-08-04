package com.tcl.manager.blackwhitelist;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.analyst.AppFilter;
import com.tcl.manager.score.PageFunctionProvider;

/**
 * @author jiaquan.huang
 */
public class WhiteListObserver implements Subscriber<WhiteListRefreshEvent> {
    volatile static WhiteListObserver observer;

    private WhiteListObserver() {
    }

    public static WhiteListObserver getInstance() {
        synchronized (WhiteListObserver.class) {
            if (observer == null) {
                observer = new WhiteListObserver();
            }
        }
        return observer;
    }

    public void register() {
        NotificationCenter.defaultCenter().subscriber(WhiteListRefreshEvent.class, this);
    }

    @Override
    public void onEvent(WhiteListRefreshEvent event) {
        AppFilter.getInstance().load();
        for (String pkg : event.pkgName) {
            PageFunctionProvider.setAutoStart(pkg, true);
            PageFunctionProvider.setDataAccess(pkg, true);
        }
    }
}
