package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;

import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.mie.manager.R;

/**
 * @Description: 进程杀手
 * @author wenchao.zhang
 * @date 2014年12月26日 下午3:00:30
 * @copyright TCL-MIE
 */

public class TaskKiller implements Runnable {

    /** 限制风险项最大65个 */
    private static final int MOST_OPTIMIE_ITEM = 65;

    private Context mContext;
    private Handler mHandler;

    public TaskKiller(Context context, Handler handler) {
        this.mContext = context;
        this.mHandler = handler;
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

        List<PageInfo> pageInfoList = PageFunctionProvider.killOperation(mContext);

        for (PageInfo info : pageInfoList) {
            // AdbCmdTool.forceStop(info.pkgName);
            mHandler.sendMessage(mHandler.obtainMessage(MainActivity.MSG_OPTIMIZE_SINGLE_PROCESS_FINISH, info));
            AdbCmdTool.forceStop(mContext, info.pkgName);
            info.isRunning = false;
        }

        Resources res = mContext.getResources();
        List<ExpandableListItem> list = new ArrayList<ExpandableListItem>();
        // list.add(new ExpandableListItem(R.drawable.ic_check,
        // res.getString(R.string.main_list_item_label_01), appInfos.size() +
        // res.getString(R.string.main_list_item_label_03)));
        // String memoryStr = MemoryInfoProvider.byteToMB(mContext,
        // releasedMemory);
        // list.add(new ExpandableListItem(R.drawable.ic_check,
        // res.getString(R.string.main_list_item_label_02),
        // String.valueOf(memoryStr)));

        // 加入子项
        List<OptimizeChildItem> childItems = AppScoreProvider.getInstance().filterBatteryOptimizeItems(mContext);

        if (childItems.size() > 0) {// 若列表中有,则加入该优化项

            // 加入电池优化项
            ExpandableListItem batteryItem = new ExpandableListItem(R.drawable.ic_tanhao, res.getString(R.string.main_list_item_label_05), res.getString(R.string.main_list_item_label_06));
            batteryItem.setCanExpand(true);

            if (childItems.size() > MOST_OPTIMIE_ITEM) {
                // 超过目标数目，则只要目标数目
                List<OptimizeChildItem> childItemsFilter = new ArrayList<OptimizeChildItem>();
                for (int i = 0; i < MOST_OPTIMIE_ITEM; i++) {
                    childItemsFilter.add(childItems.get(i));
                }
                batteryItem.getChildren().addAll(childItemsFilter);
            } else {
                batteryItem.getChildren().addAll(childItems);
            }
            list.add(batteryItem);

        }
        List<OptimizeChildItem> dataChildItems = AppScoreProvider.getInstance().filterDataTrafficOptimizeItems(mContext);
        if (dataChildItems.size() > 0) {// 若列表中有,则加入该优化项
            // 加入电池优化项
            ExpandableListItem batteryItem = new ExpandableListItem(R.drawable.ic_tanhao, res.getString(R.string.main_list_item_label_07), res.getString(R.string.main_list_item_label_08));
            batteryItem.setCanExpand(true);

            // 剩余个数
            int surplusCount = MOST_OPTIMIE_ITEM - childItems.size();
            if (surplusCount > 0) {
                // 名额不足
                if (surplusCount < dataChildItems.size()) {
                    List<OptimizeChildItem> childItemsFilter = new ArrayList<OptimizeChildItem>();
                    for (int i = 0; i < surplusCount; i++) {
                        childItemsFilter.add(dataChildItems.get(i));
                    }
                    batteryItem.getChildren().addAll(childItemsFilter);
                } else {
                    batteryItem.getChildren().addAll(dataChildItems);
                }
            }

            list.add(batteryItem);
        }
        // 杀死进程后的总分
        int killScore = PageFunctionProvider.getIndexViewScore();
        // 需要优化项目的个数
        int needOptimizeItemCount = childItems.size() + dataChildItems.size();

        Message msg = mHandler.obtainMessage(MainActivity.MSG_OPTIMIZE_PROCESS_FINISH, list);
        msg.arg1 = killScore;
        msg.arg2 = needOptimizeItemCount;
        mHandler.sendMessage(msg);
        PageFunctionProvider.save();
    }

}
