package com.tcl.manager.score;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;

import com.tcl.manager.analyst.ScoreSaver;
import com.tcl.manager.analyst.entity.AppInfoBuffer;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.MemoryScoreTool;
import com.tcl.manager.arithmetic.base.IDeductionArithmetic;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.arithmetic.opetate.DeductionArithmetic;
import com.tcl.manager.arithmetic.opetate.OptimizeArithmetic;

import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.util.TimeUtil;

/**
 * 页面功能提供
 * 
 * @author jiaquan.huang
 * @date 2014年12月26日 下午4:42:44
 * @copyright TCL-MIE
 * **/
public class PageFunctionProvider {
    /**
     * 开关自启动
     * 
     * @param pkgName
     *            包名
     * @param state
     * 
     * @return PageInfo
     */
    public static PageInfo setAutoStart(String pkgName, boolean isOpenAutoStart) {
        return setBatteryState(pkgName, isOpenAutoStart);
    }

    /**
     * 开关自启动
     * 
     * @param pkgName
     *            包名
     * @param state
     * 
     * @return 修改后APP得分
     */
    public static PageInfo setDataAccess(String pkgName, boolean isOpenAutoStart) {
        return setDataAccessState(pkgName, isOpenAutoStart);
    }

    /** 关闭自启动 **/
    private static PageInfo setBatteryState(String pkgName, boolean isOpenAutoStart) {
        PageInfo pageInfo = getPageInfo(pkgName);
        if (pageInfo == null) {
            return null;
        }
        OptimizeArithmetic.optimizeBattery(new PageOptimizeInfo(pageInfo), isOpenAutoStart);
        startBatteryRun(isOpenAutoStart, pkgName);
        return pageInfo;

    }

    /**
     * 开关数据流量
     * 
     * @param pkgName
     *            包名
     * @param state
     *            开关状态 false:关闭 true:打开
     */
    private static PageInfo setDataAccessState(String pkgName, boolean isOpenDataAccess) {
        PageInfo pageInfo = getPageInfo(pkgName);
        if (pageInfo == null) {
            return null;
        }
        pageInfo.isOpenDataAccess = isOpenDataAccess;
        OptimizeArithmetic.optimizeNet(new PageOptimizeInfo(pageInfo), isOpenDataAccess);
        startAccessRun(isOpenDataAccess, pkgName);
        return pageInfo;
    }

    private static void startAccessRun(final boolean isOpenDataAccess, final String pkgName) {
        /** 流量黑名单操作 **/
        Runnable run = new Runnable() {

            @Override
            public void run() {
                if (isOpenDataAccess) {
                    FirewallManager.getSingleInstance().unBlockPackages(pkgName);
                } else {
                    FirewallManager.getSingleInstance().setBlockPackages(pkgName);
                }
            }
        };
        singleThreadExecutor.execute(run);
    }

    private static void startBatteryRun(final boolean isOpenAutoStart, final String pkgName) {
        /** 黑名单操作 **/
        Runnable run = new Runnable() {

            @Override
            public void run() {
                /** 黑名单操作 **/
                if (isOpenAutoStart) {
                    AutoStartBlackList.getInstance().removeFromBlackList(pkgName);
                } else {
                    AutoStartBlackList.getInstance().addToBlacklist(pkgName);
                }
            }
        };
        singleThreadExecutor.execute(run);
    }

    /**
     * 关闭应用
     * 
     * @param pkgName
     *            包名
     * **/
    public static boolean stop(String pkgName) {
        try {
            AdbCmdTool.forceStop(ManagerApplication.sApplication, pkgName);
            /** 如果程序有缓存数据则修改相应状态，如果没有则不用修改 **/
            PageInfo pageInfo = getPageInfo(pkgName);
            if (pageInfo == null) {
                return true;// 默认关闭成功
            }
            pageInfo.isRunning = false;
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 调用系统卸载程序
     * 
     * @Description: TODO
     * @author Pengcheng.Zhang
     * @param context
     * @param packageName
     */
    public static void unistallApp(Context context, String packageName) {
        try {
            Uri uri = Uri.parse("package:" + packageName);
            Intent it = new Intent(Intent.ACTION_DELETE, uri);
            context.startActivity(it);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
    }

    /** 卸载成功 **/
    public static void uninstalledSuccess(String pkgName) {
        PageDataProvider.getInstance().getPageInfoMap().remove(pkgName);
    }

    /** 通过包名获取指定实体 **/
    public static PageInfo getPageInfo(String pkgName) {
        return PageDataProvider.getInstance().getPageInfoMap().get(pkgName);
    }

    /** 杀进程前的运算操作 **/
    public static List<PageInfo> killOperation(Context context) {
        opreateAppScore();
        final int newAppScore = PageDataProvider.getInstance().appScore;
        List<PageInfo> running = PageDataProvider.getInstance().getMemoryPageData();
        final float newMemoryScore = oprateMemoryScore(context, running, newAppScore);
        /** 重新计算内存得分, **/
        PageDataProvider.getInstance().memoryScore = (int) newMemoryScore;
        return running;
    }

    public static void save() {
        AppInfoBuffer scoreInfo = new AppInfoBuffer();
        scoreInfo.appScore = PageDataProvider.getInstance().appScore;
        scoreInfo.memoryScore = PageDataProvider.getInstance().memoryScore;
        scoreInfo.total = (int) (scoreInfo.appScore + scoreInfo.memoryScore);
        scoreInfo.time = TimeUtil.getTodayCalendar();
        scoreInfo.app = pageInfoToAppInfo(PageDataProvider.getInstance().getAll());
        ScoreSaver.getInstance().setSaveScore(scoreInfo);
    }

    /** 获取内存里面的APP **/
    public static List<PageInfo> getMemoryPageData(Context mContext) {
        List<PageInfo> data = new ArrayList<PageInfo>();
        Map<String, RunningAppInfo> running = PkgManagerTool.getRunningAppInfos(mContext);
        for (Entry<String, RunningAppInfo> entry : running.entrySet()) {
            PageInfo obj = getPageInfo(entry.getKey());
            if (obj == null) {
                continue;
            }
            obj.pkgName = entry.getKey();
            obj.memorySize = entry.getValue().getMemsize();
            obj.isRunning = true;
            data.add(obj);
        }
        return data;
    }

    /** 计算内存得分 **/
    public static float oprateMemoryScore(Context context, List<PageInfo> running, int appScore) {
        long releaseTotalMemory = 0;
        boolean hasRunningApp = false;
        if (running.size() > 0) {
            hasRunningApp = true;
        }
        for (PageInfo info : running) {
            releaseTotalMemory += info.memorySize;
        }
        MemoryInfoProvider memoryInfo = MemoryInfoProvider.getInstance(context);
        long avilMem = memoryInfo.availMem;
        long totalMem = memoryInfo.totalMem;
        long totalAvilMen = avilMem + releaseTotalMemory;
        MemoryScoreTool oldTool = new MemoryScoreTool(context, avilMem, totalMem);
        MemoryScoreTool newTool = new MemoryScoreTool(context, totalAvilMen, totalMem);
        float oldScore = oldTool.getDefautScore(hasRunningApp);
        float newScore = newTool.getDefautScore(false);
        float needAddScore = newScore - oldScore;
        if (needAddScore < 0 || releaseTotalMemory == 0) {
            return oldScore;
        }
        float currOldScore = oldScore;
        for (PageInfo info : running) {
            currOldScore += (info.memorySize * 1f / releaseTotalMemory) * needAddScore;
            info.indexScore = currOldScore + appScore;// (info.memorySize
        }
        return newScore;
    }

    /** MiniApp杀进程前的操作 **/
    public static List<PageInfo> killOperationInMiniApp(Context context, int appScore) {
        List<PageInfo> pageInfos = getMemoryPageData(context);
        oprateMemoryScore(context, pageInfos, appScore);
        return pageInfos;
    }

    /** 计算APP优化得分 和APP总分 **/
    public static void opreateAppScore() {
        PageDataProvider instance = PageDataProvider.getInstance();
        List<PageInfo> all = instance.getStaticAll();
        List<IDeductionArithmetic> optimize = new ArrayList<IDeductionArithmetic>();
        for (PageInfo pageInfo : all) {
            PageDeductionInfo oprimizeInfo = new PageDeductionInfo(pageInfo);
            /** 需要优化电量的需要重新参与计算扣分 ***/
            if (pageInfo.needOptimizedBattery && pageInfo.isRecord == true) {
                oprimizeInfo.setNeedOpreateBattery(true);
            } else {
                oprimizeInfo.setNeedOpreateBattery(false);
            }
            /** 需要优化流量的需要重新计算扣分 **/
            if (pageInfo.needOptimizedNet && pageInfo.isRecord == true) {
                oprimizeInfo.setNeedOpreateNet(true);
            } else {
                oprimizeInfo.setNeedOpreateNet(false);
            }
            optimize.add(oprimizeInfo);
        }
        DeductionArithmetic deduction = new DeductionArithmetic();
        deduction.operate(optimize);
        instance.appScore = (int) deduction.getScore();
    }

    /** 获取首页总分 **/
    public static int getIndexViewScore() {
        PageDataProvider provider = PageDataProvider.getInstance();
        return provider.appScore + provider.memoryScore;
    }

    /** 首页加分后，设置优化后的TotalScore **/
    public static void setAppScore(int total) {
        singleThreadExecutor.execute(getRunnable(total));
    }

    static ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();

    /** 生成一个得分保存线程 **/
    private static Runnable getRunnable(final int total) {
        Runnable run = new Runnable() {

            @Override
            public void run() {
                AppInfoBuffer scoreInfo = new AppInfoBuffer();
                scoreInfo.total = total;
                scoreInfo.appScore = total - PageDataProvider.getInstance().memoryScore;
                scoreInfo.memoryScore = PageDataProvider.getInstance().memoryScore;
                scoreInfo.app = pageInfoToAppInfo(PageDataProvider.getInstance().getAll());
                scoreInfo.time = TimeUtil.getTodayCalendar();
                ScoreSaver.getInstance().setSaveScore(scoreInfo);
            }
        };
        return run;
    }

    /** 数据结构转换,Wight只需要了解优化项 **/
    public static List<AppScoreInfo> pageInfoToAppInfo(List<PageInfo> app) {
        ArrayList<AppScoreInfo> appScoreInfos = new ArrayList<AppScoreInfo>();
        for (PageInfo page : app) {
            AppScoreInfo appScoreInfo = new AppScoreInfo();
            appScoreInfo.pkgName = page.pkgName;
            appScoreInfo.needOptimizedBattery = page.needOptimizedBattery;
            appScoreInfo.needOptimizedNet = page.needOptimizedNet;
            appScoreInfos.add(appScoreInfo);
        }
        return appScoreInfos;
    }

    /** 获取App更新时间 **/
    public static long getUpdateTime(String pkgName) {
        PackageInfo info;
        try {
            info = InstalledAppProvider.getInstance().getPackageInfo(pkgName);
            return info.lastUpdateTime;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /** 获取优化逻辑后单个App的得分 **/
    public static int getPageInfoScore(PageInfo pageInfo) {
        /** 不参与统计的直接返回默认分 **/
        if (!pageInfo.isRecord) {
            return pageInfo.score;
        }
        float score = 0;
        /** 电量开关关着，且它是个风险项，给予优化分 **/
        if (pageInfo.isOpenAutoStart == false && pageInfo.isBatteryRisk == true) {
            score += pageInfo.optimizedBatteryEB;
        } else {
            score += pageInfo.batteryEB;
        }
        /** 流量开关关着，且它是个风险项，给予优化分 **/
        if (pageInfo.isOpenDataAccess == false && pageInfo.isNetRisk == true) {
            score += pageInfo.optimizedDataED;
        } else {
            score += pageInfo.dataED;
        }
        score += pageInfo.pkgSizeES;
        return (int) score;
    }
}
