package com.tcl.manager.util;

import com.tcl.framework.log.Logger;
import com.tcl.framework.log.NLog;
import com.tcl.manager.application.AMDirType;
import com.tcl.manager.application.AMDirectorManager;

/**
 * 日志设置
 * 
 * @author jiaquan.huang
 * 
 */
public class LogSetting {

    /**
     * 日志标示
     */
    public final static String DEBUG_TAG = "tcl";

    public static void initLog() {
        NLog.setDebug(false, Logger.VERBOSE);
       // NLog.trace(Logger.TRACE_ALL, AMDirectorManager.getInstance().getDirectoryPath(AMDirType.log));
    }
}
