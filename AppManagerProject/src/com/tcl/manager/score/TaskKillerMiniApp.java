package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.mie.manager.R;

/**
 * @Description: 进程杀手 for miniapp
 * @author wenchao.zhang
 * @date 2014年12月26日 下午3:00:30
 * @copyright TCL-MIE
 */

public class TaskKillerMiniApp implements Runnable {

    /** 限制风险项最大65个 */
    private static final int MOST_OPTIMIE_ITEM = 65;

    private Context mContext;
    private Handler mHandler;
    private int appScore;

    public TaskKillerMiniApp(Context context, Handler handler, int appScore) {
        this.mContext = context;
        this.mHandler = handler;
        this.appScore = appScore;
    }

    /**
     * 开始运行
     */
    public void start() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        // 获取所有进程
        // Map<String, RunningAppInfo> appInfos =
        // PkgManagerTool.getRunningAppInfos(mContext);
        // int releasedMemory = 0;
        // 轮询杀死
        // for (String key : appInfos.keySet()) {
        // RunningAppInfo app = appInfos.get(key);
        // // boolean isSuccess =
        // AdbCmdTool.forceStop(app.pkgName);
        // releasedMemory += app.getMemsize();
        // mHandler.sendMessage(mHandler.obtainMessage(MainActivity.MSG_OPTIMIZE_SINGLE_PROCESS_FINISH,app));
        // }

        List<PageInfo> pageInfoList = PageFunctionProvider.killOperationInMiniApp(mContext, appScore);

        for (PageInfo info : pageInfoList) {
            AdbCmdTool.forceStop(mContext.getApplicationContext(), info.pkgName);
            // PageFunctionProvider.stop(info.pkgName);
            mHandler.sendMessage(mHandler.obtainMessage(MainActivity.MSG_OPTIMIZE_SINGLE_PROCESS_FINISH, info));
        }

        Message msg = mHandler.obtainMessage(MainActivity.MSG_OPTIMIZE_PROCESS_FINISH);

        mHandler.sendMessage(msg);
    }

}
