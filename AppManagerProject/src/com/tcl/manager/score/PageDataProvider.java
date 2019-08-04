package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.content.Context;

import com.tcl.framework.log.NLog;
import com.tcl.manager.analyst.Analyst;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.analyst.IAnalystListener;

/**
 * @Description: 页面数据提供者
 * @author jiaquan.huang
 * @date 2014年12月29日 下午4:42:44
 * @copyright TCL-MIE
 */
public class PageDataProvider {

    private static PageDataProvider instance;
    private volatile HashMap<String, PageInfo> all = new HashMap<String, PageInfo>();
    private Context mContext;
    /** 程序总得分 **/
    public int score;
    public int appScore;
    public int memoryScore;

    /** debug **/
    public List<AppScoreInfo> debug;

    public static PageDataProvider getInstance() {
        synchronized (AppScoreProvider.class) {
            if (instance == null) {
                instance = new PageDataProvider();
            }
        }
        return instance;
    }

    /** 数据加载 **/
    public void init(Context context, List<AppScoreInfo> appScoreInfos) {
        synchronized (all) {
            mContext = context;
            all.clear();
            for (AppScoreInfo appInfo : appScoreInfos) {
                try {
                    /** 会crash暂时保护一下 **/
                    PageInfo pageInfo = putInfo(context, appInfo);
                    all.put(pageInfo.pkgName, pageInfo);
                } catch (Exception e) {
                    NLog.e("debug", e);
                }
            }
        }
        /*** debug **/
        debug = appScoreInfos;
    }

    public void getCacheData() {
        Analyst analyst = new Analyst(ManagerApplication.sApplication, new IAnalystListener() {

            @Override
            public void totalScoreBack(List<AppScoreInfo> list, int totalScore, int appsScore, int memoryScore) {
                init(ManagerApplication.sApplication, list);
            }

            @Override
            public void memoryScoreBack(int memoryScore) {

            }
        });
        analyst.syncAnalysis();
    }

    /** 是否被初始化 **/
    public boolean isInit() {
        if (mContext == null && all.isEmpty()) {
            return false;
        }
        return true;
    }

    /** 重置 **/
    public void reset() {
        synchronized (all) {
            all.clear();
        }
    }

    /** 获取内存 **/
    public List<PageInfo> getMemoryPageData() {
        if (!isInit()) {
            getCacheData();
        }
        List<PageInfo> data = new ArrayList<PageInfo>();
        Map<String, RunningAppInfo> running = PkgManagerTool.getRunningAppInfos(mContext);
        for (Entry<String, RunningAppInfo> entry : running.entrySet()) {
            PageInfo obj = all.get(entry.getKey());
            if (obj != null) {
                obj.memorySize = entry.getValue().getMemsize();
                obj.isRunning = true;
                data.add(obj);
            }
        }
        return data;
    }

    /** 获取全部数据 **/
    public List<PageInfo> getAll() {
        if (!isInit()) {
            getCacheData();
        }
        Map<String, RunningAppInfo> running = PkgManagerTool.getRunningAppInfos(mContext);
        for (Entry<String, RunningAppInfo> entry : running.entrySet()) {
            PageInfo obj = all.get(entry.getKey());
            if (obj != null) {
                obj.memorySize = entry.getValue().getMemsize();
                obj.isRunning = true;
            }
        }
        return new ArrayList<PageInfo>(all.values());
    }

    /** 获取无关正在运行的全部数据 **/
    public List<PageInfo> getStaticAll() {
        if (!isInit()) {
            getCacheData();
        }
        return new ArrayList<PageInfo>(all.values());
    }

    /** 获取无关正在运行的全部数据 **/
    public HashMap<String, PageInfo> getPageInfoMap() {
        if (!isInit()) {
            getCacheData();
        }
        return all;
    }

    /** 拆分数据 **/
    private PageInfo putInfo(Context context, AppScoreInfo appScoreInfo) {
        PageInfo pageInfo = new PageInfo();
        pageInfo.pkgName = appScoreInfo.pkgName;
        pageInfo.appName = PkgManagerTool.getAppName(context, appScoreInfo.pkgName);
        pageInfo.score = (int) appScoreInfo.score;
        pageInfo.frequencyLevel = appScoreInfo.frequencyLevel;
        pageInfo.averageUseTime = (long) appScoreInfo.avrageUsedTime;
        pageInfo.averageUseCount = (long) appScoreInfo.avrageUsedCount;
        pageInfo.averageMobileSize = (long) appScoreInfo.averageMoblieData;
        pageInfo.todayMobileSize = appScoreInfo.todayMobileData;
        pageInfo.appSize = appScoreInfo.pkgSize;
        pageInfo.appDataSize = appScoreInfo.pkgData;
        pageInfo.isOpenAutoStart = appScoreInfo.isOpenAutoStart;
        pageInfo.isOpenDataAccess = appScoreInfo.isOpenDataAccess;
        pageInfo.batteryPercent = (int) (appScoreInfo.avrageBatteryPercent * 100);
        pageInfo.mobileDataPercent = (int) (appScoreInfo.avrageMobileDataPercent * 100);
        pageInfo.isRecord = appScoreInfo.isRecord;
        pageInfo.needOptimizedBattery = appScoreInfo.needOptimizedBattery;
        pageInfo.needOptimizedNet = appScoreInfo.needOptimizedNet;
        pageInfo.dataReducesScore = appScoreInfo.dataReducesScore;
        pageInfo.batteryReducesScore = appScoreInfo.batteryReducesScore;

        pageInfo.isBatteryRisk = appScoreInfo.isBatteryRisk;
        pageInfo.isNetRisk = appScoreInfo.isNetRisk;

        /** 优化得分和各项得分 **/
        pageInfo.dataED = appScoreInfo.mobileDataED;
        pageInfo.batteryEB = appScoreInfo.batteryEB;
        pageInfo.pkgSizeES = appScoreInfo.apkSizeES;
        pageInfo.optimizedDataED = appScoreInfo.optimizedDataED;
        pageInfo.optimizedBatteryEB = appScoreInfo.optimizedBatteryEB;
        pageInfo.optimizedPkgSizeES = appScoreInfo.optimizedPkgSizeES;
        return pageInfo;
    }
}
