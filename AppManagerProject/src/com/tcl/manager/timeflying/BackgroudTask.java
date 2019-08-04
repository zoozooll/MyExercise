package com.tcl.manager.timeflying;

import android.content.Context;

import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.statistic.frequency.FrequencySaver;

/**
 * @Description:日志任务
 * @author jiaquan.huang
 * @date 2014-12-20 上午10:31:56
 * @copyright TCL-MIE
 */
public class BackgroudTask {
    public static String LOG_SAVE_TASK = "log_save_save";
    public static Context mContext = ManagerApplication.sApplication;

    /** 收集基础数据 1个小时调一次 **/
    public static void save(Context context) {
        FrequencySaver.getInstance(context).saveFrequency();
        FrequencySaver.getInstance(context).clean();
    }

    /** 开启后台查杀 1个小时调一次 **/
    public static void autoStartKiller() {
        AutoStartBlackList.getInstance().startKillThread();
    }
}
