package com.tcl.manager.miniapp;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.framework.log.NLog;
import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.analyst.Analyst;
import com.tcl.manager.analyst.IAnalystListener;
import com.tcl.manager.score.AppScoreProvider;
import com.tcl.manager.score.PackageChangeEvent;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.PkgManagerTool;

/**
 * 负责mini app的逻辑与界面显示
 * 
 * @author difei.zou
 * @date 2014-12-13 下午3:04:22
 * @copyright
 */

public class MiniAppService extends Service implements Subscriber<PackageChangeEvent> {

    public static final String SCORE_ICON_CLICK = "SCORE_ICON_CLICK";
    public static final String MORE_DETAIL_CLICK = "MORE_DEATIL_CLICK";

    public static final String EVENT_UPDATE = "EVENT_UPDATE";
    public static final String EVENT_ENABLE = "EVENT_ENABLE";
    public static final String EVENT_DISABLE = "EVENT_DISABLE";

    private static final int MSG_CHECK_FINISH = 101;
    private static final int MSG_UPDATE_SCOREVIEW = 102;
    private static final int MSG_ANIM_FINISH = 103;

    // private static final int MSG_OPTIMIZE_FINISH = 2;

    private MiniAppWindowManager miniManager;
    /** 当前分数 */
    private int currentScore = 99;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        miniManager = MiniAppWindowManager.getInstance(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {

            String action = intent.getAction();
            if (SCORE_ICON_CLICK.equals(action)) {
                // showMiniWindow(intent.getSourceBounds());
                startOptimize(intent.getSourceBounds());
            } else if (MORE_DETAIL_CLICK.equals(action)) {
                // Intent launch =
                // getPackageManager().getLaunchIntentForPackage(
                // getPackageName());
                // //
                // launch.addFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY);
                // //
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                // intent.setAction(Intent.ACTION_MAIN);
                // intent.addCategory(Intent.CATEGORY_LAUNCHER);
                // startActivity(launch);
                lauch();

            } else if (EVENT_UPDATE.equals(action)) {
                // miniManager.startCircleAnim(currentScore);
                checkScore();
            } else if (EVENT_ENABLE.equals(action)) {
                // if (!PageDataProvider.getInstance().isInit()) {
                // miniManager.startCircleAnim(currentScore);
                checkScore();
                // } else {
                // mHandler.sendEmptyMessage(MSG_CHECK_FINISH);
                // }
            } else if (EVENT_DISABLE.equals(action)) {
                stopSelf();
            }

        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startOptimize(Rect rect) {
        // miniManager.startCircleAnim(currentScore);
        // showMiniWindow(rect);
        AppScoreProvider.getInstance().startKillProcessForMiniapp(this, mHandler, mAppScore);
        miniManager.startOptimizeAnim();
    }

    private void showMiniWindow(Rect rect) {
        miniManager.showPopupWindow(rect);
    }

    private Analyst mAppScoreAnaly;

    // 安装的app数量
    private int mInstalledApps;
    // 运行的app的个数
    private int mRunnableApps;
    /** 可优化风险项个数 */
    private int mOptimizableItemCount;
    /** appscore 提供给优化 */
    private int mAppScore;

    private boolean checking = false;

    private void checkScore() {
        if (checking) {
            return;
        }
        // 开始检测分数
        if (mAppScoreAnaly == null) {
            mAppScoreAnaly = new Analyst(this, new IAnalystListener() {

                @Override
                public void totalScoreBack(List<AppScoreInfo> list, int totalScore, int appsScore, int memoryScore) {
                    if (list == null) {
                        NLog.i("wenchao", "miniapp list 为空");
                        return;
                    }
                    mOptimizableItemCount = getOptimizeCount(list);
                    mInstalledApps = list.size();
                    mRunnableApps = PkgManagerTool.getRunningAppInfos(getApplicationContext()).size();
                    mAppScore = appsScore;
                    Message msg = new Message();
                    msg.arg1 = totalScore;
                    msg.what = MSG_CHECK_FINISH;
                    mHandler.sendMessage(msg);

                    checking = false;
                }

                @Override
                public void memoryScoreBack(int memoryScore) {

                }
            });
        }
        /** 迷你App只需要 总分 **/
        mAppScoreAnaly.startAnalysisPart();

        checking = true;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_CHECK_FINISH:
                // update(true, true, currentScore);
                new AnimRunnable(currentScore, msg.arg1, 500).start();
                currentScore = msg.arg1;
                break;
            case MainActivity.MSG_OPTIMIZE_SINGLE_PROCESS_FINISH:
                // 单个进程干掉
                PageInfo runningAppInfo = (PageInfo) msg.obj;
                // optimizeSingleProcessCompleted(runningAppInfo);
                miniManager.updateAppWidget2(false, false, (int) runningAppInfo.indexScore);
                currentScore = (int) runningAppInfo.indexScore;
//                System.out.println();
                break;
            case MainActivity.MSG_OPTIMIZE_PROCESS_FINISH:
                // currentScore = msg.arg1;
                // int needOptimizeItemCount = msg.arg2;
                miniManager.stopOptimizeAnim(currentScore, mInstalledApps, mRunnableApps, mOptimizableItemCount);

                break;
            case MSG_UPDATE_SCOREVIEW:
                // miniManager.updateAppWidget2(false, false, (Integer)msg.obj);
                // int preScore = msg.arg1;
                // if (preScore == ScoreLevel.DANGER
                // || preScore == ScoreLevel.NORMAL
                // || preScore == ScoreLevel.WELL) {
                // miniManager.startCircleAnim((Integer) msg.obj);
                // } else {
                miniManager.updateCircleAnim((Integer) msg.obj);
                // }

                break;
            case MSG_ANIM_FINISH:
                miniManager.stopCircleAnim(currentScore, mInstalledApps, mRunnableApps, mOptimizableItemCount);
                miniManager.updateAppWidget2(true, true, currentScore);
                break;
            default:
                break;
            }
        };
    };

    private class AnimRunnable implements Runnable {

        private int dstScore;
        private int currScore;
        private int duration;

        public AnimRunnable(int currScore, int dstScore, int duration) {
            super();
            this.dstScore = dstScore;
            this.currScore = currScore;
            this.duration = duration;
        }

        public void start() {
            new Thread(this).start();
        }

        @Override
        public void run() {
            while (true) {
                if (currScore == dstScore) {
                    mHandler.sendMessage(mHandler.obtainMessage(MSG_ANIM_FINISH, currScore));
                    break;
                }

                int deyTime = duration / Math.abs(currScore - dstScore);

                int preScore = currScore;
                if (currScore > dstScore) {
                    currScore--;
                } else {
                    currScore++;
                }
                mHandler.sendMessage(mHandler.obtainMessage(MSG_UPDATE_SCOREVIEW, preScore, preScore, currScore));

                try {
                    Thread.sleep(deyTime);
                } catch (InterruptedException e) {
                }
            }
        }

    }

    /**
     * 获取流量和电量风险总个数
     * 
     * @param list
     * @return
     */
    private int getOptimizeCount(List<AppScoreInfo> list) {
        int totalCount = 0;
        for (AppScoreInfo appScoreInfo : list) {
            if (appScoreInfo.needOptimizedBattery) {
                totalCount++;
            }
            if (appScoreInfo.needOptimizedNet) {
                totalCount++;
            }
        }
        return totalCount;
    }

    private void lauch() {
        // //获取ActivityManager
        // ActivityManager mAm = (ActivityManager)
        // getSystemService(ACTIVITY_SERVICE);
        // //获得当前运行的task
        // List<ActivityManager.RunningTaskInfo> taskList =
        // mAm.getRunningTasks(100);
        // for (ActivityManager.RunningTaskInfo rti : taskList) {
        // //找到当前应用的task，并启动task的栈顶activity，达到程序切换到前台
        // if(rti.baseActivity.getPackageName().equals(getPackageName())) {
        // mAm.moveTaskToFront(rti.id,0);
        // return;
        // }
        // }
        // //若没有找到运行的task，用户结束了task或被系统释放，则重新启动mainactivity
        // Intent resultIntent = new Intent(this, MainActivity.class);
        // resultIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        // startActivity(resultIntent);

        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(ACTIVITY_SERVICE);
        String packageName = getPackageName();
        List<RunningAppProcessInfo> appProcess = activityManager.getRunningAppProcesses();

        if (appProcess != null) {
            for (RunningAppProcessInfo runningAppProcessInfo : appProcess) {
                if (runningAppProcessInfo.processName.equals(packageName) && runningAppProcessInfo.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    if (MainActivity.sMainActivity != null && MainActivity.sMainActivity.getTaskId() != -1) {
                        activityManager.moveTaskToFront(MainActivity.sMainActivity.getTaskId(), 0);
                        return;
                    }

                }

            }
        }

        Intent resultIntent = new Intent(this, MainActivity.class);
        resultIntent.setAction(Intent.ACTION_MAIN);
        resultIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(resultIntent);

        NLog.i("wenchao", "重新启动一个");
    }

    @Override
    public void onEvent(PackageChangeEvent arg0) {
        if (PackageChangeEvent.REMOVE == arg0.stats) {
            // 卸载应用
            checkScore();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationCenter.defaultCenter().unsubscribe(PackageChangeEvent.class, this);
    }
}
