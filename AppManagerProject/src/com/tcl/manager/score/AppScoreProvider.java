package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.mie.manager.R;

/**
 * @Description: app分数提供者
 * @author wenchao.zhang
 * @date 2014年12月26日 下午4:42:44
 * @copyright TCL-MIE
 */

public class AppScoreProvider {

    private List<AppScoreInfo> recordedApps = new ArrayList<AppScoreInfo>();// 统计到的数据集合
    private List<AppScoreInfo> unrecordedApps = new ArrayList<AppScoreInfo>();// 未统计到的数据集合

    private PackageManager mPackageManager;
    /** 所有app的分数 */
    private int appsScore;

    public List<AppScoreInfo> getRecordedApps() {
        return recordedApps;
    }

    public void setRecordedApps(List<AppScoreInfo> recordedApps) {
        this.recordedApps = recordedApps;
    }

    public List<AppScoreInfo> getUnrecordedApps() {
        return unrecordedApps;
    }

    public void setUnrecordedApps(List<AppScoreInfo> unrecordedApps) {
        this.unrecordedApps = unrecordedApps;
    }

    public int getAppsScore() {
        return appsScore;
    }

    public void setAppsScore(int appsScore) {
        this.appsScore = appsScore;
    }

    /**
     * 筛选建议优化电池的所有app
     * 
     * @return
     */
    public List<OptimizeChildItem> filterBatteryOptimizeItems(Context mContext) {
        // Resources res = ManagerApplication.sApplication.getResources();
        List<OptimizeChildItem> items = new ArrayList<OptimizeChildItem>();
        for (PageInfo info : PageDataProvider.getInstance().getAll()) {
            if (info.isRecord && info.needOptimizedBattery) {
                OptimizeChildItem item = new OptimizeChildItem();
                item.setAppName(info.appName);
                item.setAppDesc(ScoreLevel.resolveToFrequencyLevelString(info.frequencyLevel));
                item.setAppIcon(getIconByPkgName(info.pkgName));
                item.setData(info.batteryPercent + mContext.getResources().getString(R.string.app_sign_percent));
                item.setSelected(true);
                item.setPgkName(info.pkgName);
                item.setNeedAddScore(info.batteryReducesScore);
                item.setType(OptimizeChildItem.TYPE_BATTERY);
                items.add(item);
            }
        }
        return items;
    }

    /**
     * 筛选建议优化数据流量
     * 
     * @return
     */
    public List<OptimizeChildItem> filterDataTrafficOptimizeItems(Context mContext) {
        List<OptimizeChildItem> items = new ArrayList<OptimizeChildItem>();
        for (PageInfo info : PageDataProvider.getInstance().getAll()) {
            if (info.isRecord && info.needOptimizedNet) {
                OptimizeChildItem item = new OptimizeChildItem();
                item.setAppName(info.appName);
                item.setAppDesc(ScoreLevel.resolveToFrequencyLevelString(info.frequencyLevel));
                item.setAppIcon(getIconByPkgName(info.pkgName));
                item.setData(info.mobileDataPercent + mContext.getResources().getString(R.string.app_sign_percent));
                item.setSelected(true);
                item.setPgkName(info.pkgName);
                item.setNeedAddScore(info.dataReducesScore);
                item.setType(OptimizeChildItem.TYPE_DATA);
                items.add(item);
            }
        }
        return items;
    }

    /**
     * 根据包名获取应用程序信息
     * 
     * @param pkgName
     * @return 包名找不到则为null
     */
    public ApplicationInfo getAppInfoByPkgName(String pkgName) {
        try {
            ApplicationInfo info = PkgManagerTool.getApplicationInfo(ManagerApplication.sApplication, pkgName);
            return info;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 根据应用程序信息获取应用程序图标
     * 
     * @param app
     * @return
     */
    public Drawable getIconByPkgName(String pkgName) {
        try {
            return mPackageManager.getApplicationInfo(pkgName, 0).loadIcon(mPackageManager);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过包名获取分值
     * 
     * @param pkgName
     * @return
     */
    public int getAppScoreByPkgName(String pkgName) {
        return 0;
    }

    /**
     * 开始杀进程
     * 
     * @param context
     * @param handler
     */
    public void startKillProcess(Context context, Handler handler) {
        new TaskKiller(context, handler).start();
    }

    public void startKillProcessForMiniapp(Context context, Handler handler, int appScore) {
        new TaskKillerMiniApp(context, handler, appScore).start();
    }

    /**
     * 加入黑名单
     * 
     * @param pkgName
     * @return
     */
    public void shutDownAutoStart(String pkgName) {
        PageFunctionProvider.setAutoStart(pkgName, false);
    }

    private static volatile AppScoreProvider instance;

    private AppScoreProvider() {
        mPackageManager = ManagerApplication.sApplication.getPackageManager();
    }

    public static AppScoreProvider getInstance() {
        synchronized (AppScoreProvider.class) {
            if (instance == null) {
                instance = new AppScoreProvider();
            }
        }
        return instance;
    }
}
