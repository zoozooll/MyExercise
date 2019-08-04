package com.tcl.manager.application;

import com.tcl.framework.log.Logger;
import com.tcl.framework.log.NLog;
import com.tcl.manager.blackwhitelist.WhiteListObserver;
import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.optimize.AutoStartBlackList;

import com.tcl.manager.score.InstalledAppProvider;

import com.tcl.manager.statics.OALogger;
import com.tcl.manager.timeflying.SchedulingService;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.manager.util.Constant;
import com.tcl.manager.util.LogSetting;
import com.tcl.manager.util.SharedStorekeeper;
import com.tcl.manager.util.TUncaughtExceptionHandler;

import android.app.Application;
import android.content.Intent;

/**
 * @Description: Application
 * @author jiaquan.huang
 * @date 2014-12-17 上午10:04:12
 * @copyright TCL-MIE
 */
public class ManagerApplication extends Application {

    public static Application sApplication = null;
    
    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this);
        AMDirectorManager.initManager(this);
        initNlog();
        initOALogger();
        initConfig();
        // modify by zuolang.li
        startTimeRepeat();
        initializeFirewall();
        AutoStartBlackList.getInstance().init(this);
        InstalledAppProvider.getInstance().init(sApplication);
        setFirsttimeRunning();
        AdbCmdTool.init(this);
        WhiteListObserver.getInstance().register();
    }

    private void initNlog() {
        AMDirectorManager manager = AMDirectorManager.getInstance();
        // // 抓取崩溃日志
        // AMUncaughtExceptionHandler handler = new
        // AMUncaughtExceptionHandler(getApplicationContext(),
        // manager.getDirectoryPath(AMDirType.crash));
        // handler.registerForExceptionHandler();

        if (!Constant.DEBUG) {
            NLog.setDebug(false, Logger.VERBOSE);
        } else {
            NLog.setDebug(true, Logger.VERBOSE);
            // 测试日志
            NLog.trace(Logger.TRACE_ALL, manager.getDirectoryPath(AMDirType.log));
        }
    }

    private void initOALogger() {
        String loggerPath = AMDirectorManager.getInstance().getDirectoryPath(AMDirType.log);
        OALogger.initLogger(getApplicationContext(), loggerPath);
    }

    private void initConfig() {
        initLog();
        SharedStorekeeper.init(this);
    }

    /** 初始化系统日志和crash日志 **/
    private void initLog() {
        LogSetting.initLog();
        TUncaughtExceptionHandler.catchUncaughtException(this, AMDirectorManager.getInstance().getDirectoryPath(AMDirType.crash));
    }

    /* start running repeat per hour */
    private void startTimeRepeat() {
        Intent service = new Intent(this, SchedulingService.class);
        startService(service);
    }

    // start a thread to run firewall
    private void initializeFirewall() {
        FirewallManager.getSingleInstance().initFirewall();
    }

    private void setFirsttimeRunning() {
        if (SharedStorekeeper.getLong(SharedStorekeeper.FIRST_TIME_RUNNING) <= 0) {
            SharedStorekeeper.save(SharedStorekeeper.FIRST_TIME_RUNNING, System.currentTimeMillis());
        }
    }
    
    private static void setApplication (Application a) {
    	sApplication = a;
    }

}
