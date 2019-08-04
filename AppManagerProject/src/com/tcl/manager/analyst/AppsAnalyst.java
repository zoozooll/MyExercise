package com.tcl.manager.analyst;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import com.tcl.manager.analyst.entity.BatteryInfo;
import com.tcl.manager.analyst.entity.FrequencyInfo;
import com.tcl.manager.analyst.entity.MobileDataInfo;
import com.tcl.manager.analyst.provider.BatteryProvider;
import com.tcl.manager.analyst.provider.FrequencyProvider;
import com.tcl.manager.analyst.provider.MobileDataProvider;
import com.tcl.manager.arithmetic.MemoryScoreTool;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.arithmetic.opetate.ArithmeitcOperator;
import com.tcl.manager.arithmetic.opetate.DeductionArithmetic;
import com.tcl.manager.datausage.AppNetstatInfo;
import com.tcl.manager.datausage.DatausageProvider;
import com.tcl.manager.pkgsize.PkgSizeInfo;
import com.tcl.manager.pkgsize.StorageProvider;
import com.tcl.manager.pkgsize.StorageProvider.IDataBack;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.util.PkgManagerTool;

/**
 * @Description:日志数据分析
 * @author jiaquan.huang
 * @date 2014-12-19 下午4:25:59
 * @copyright TCL-MIE
 */
public class AppsAnalyst {
    public final static int defaultScore = 85;// 默认未统计的分数
    Context context;
    IAnalystListener listenner;
    int dayX = 29;// 分析的天数
    long recordTime = -1;
    AnalystType type;// 分析的方式
    public long fromTime = 0;// 分析日志起始时间
    public long endTime = 0;// 分析日志结束时间
    //public boolean isDebug = false;
    //public String debug = "debug";

    public AppsAnalyst(Context context, IAnalystListener listenner) {
        this.context = context.getApplicationContext();
        this.listenner = listenner;
        initTime();
    }

    /** 开始分析日志数据 **/
    public void startAnalysis() {
        Thread analysis = new Thread(analysisThread);
        analysis.start();
    };

    /** 初始化默认时间 **/
    private void initTime() {
        /** 获取当前时间和30天以前的时刻点 **/
        Calendar time = Calendar.getInstance();
        time.set(Calendar.HOUR_OF_DAY, 23);
        time.set(Calendar.MILLISECOND, 59);
        time.set(Calendar.MINUTE, 59);
        time.add(Calendar.DAY_OF_YEAR, -1);// 不记录当天
        endTime = time.getTimeInMillis();// 结束时间
        time.add(Calendar.DAY_OF_YEAR, -dayX);
        time.set(Calendar.HOUR_OF_DAY, 0);
        time.set(Calendar.MILLISECOND, 0);
        time.set(Calendar.MINUTE, 0);
        fromTime = time.getTimeInMillis();// 起始时间
    }

    public void setAnalyzeTime(long fromTime, long endTime) {
        this.fromTime = fromTime;
        this.endTime = endTime;
    }

    Runnable analysisThread = new Runnable() {

        @Override
        public void run() {
            /** 内存得分 **/
            MemoryInfoProvider memoryProvider = MemoryInfoProvider.getInstance(context);
            MemoryScoreTool memoryScoreTool = new MemoryScoreTool(context, memoryProvider.availMem, memoryProvider.totalMem);
            int memoryScore = (int) memoryScoreTool.getScore();
            listenner.memoryScoreBack(memoryScore + DeductionArithmetic.MAX_APP_SOCRE);
            /** 初始化值 **/
            HashMap<String, AppScoreInfo> allApp = initData();
            /** 进行计算 **/
            ArithmeitcOperator operator = new ArithmeitcOperator();
            Collection<AppScoreInfo> data = allApp.values();
            operator.operate(data);
            int score = operator.getTotalScore();
            int totalScore = score + memoryScore;
            listenner.totalScoreBack(new ArrayList<AppScoreInfo>(data), totalScore, score, memoryScore);
        }
    };

    /*********************************************** 调试 *************************************************/
//    private HashMap<String, AppScoreInfo> debuginitData(boolean isDebug) {
//        HashMap<String, AppScoreInfo> dataMap = initData();
//        if (isDebug) {
//            String path = LogTools.getPath(context) + "/debug.txt";
//            String data = LogReader.readTextFromFile(new File(path));
//            if (TextUtils.isEmpty(data)) {
//                DebugEntity entity = new DebugEntity();
//                entity.app = dataMap;
//                Gson gson = new Gson();
//                String gsonString = gson.toJson(entity);
//                LogWriter.getInstance(context).writeLogToFile("debug.txt", gsonString);
//                return dataMap;
//            }
//            Gson gson = new Gson();
//            DebugEntity entity = gson.fromJson(data, DebugEntity.class);
//            return entity.app;
//        }
//        return dataMap;
//    }
//
//    private class DebugEntity {
//        HashMap<String, AppScoreInfo> app;
//    }
    /*********************************************** 调试 *************************************************/

    /** 初始化值 **/
    private HashMap<String, AppScoreInfo> initData() {
        /** 频率获取 **/
        FrequencyProvider frequency = new FrequencyProvider();
        HashMap<String, FrequencyInfo> frequencyData = frequency.read(fromTime, endTime);
        /** 电量获取 **/
        BatteryProvider batteryProvider = new BatteryProvider(context);
        HashMap<String, BatteryInfo> batteryData = batteryProvider.read(fromTime, endTime);
        /** 流量获取 **/
        MobileDataProvider mobileProvider = new MobileDataProvider(context);
        HashMap<String, MobileDataInfo> mobileData = mobileProvider.read(fromTime, endTime);
        /** 取所有App(不含过滤掉的App) **/
        HashMap<String, AppScoreInfo> allApp = getAllApp();
        /** 赋值频率 **/
        for (Entry<String, FrequencyInfo> entity : frequencyData.entrySet()) {
            String pkgName = entity.getKey();
            FrequencyInfo frequencyInfo = entity.getValue();
            AppScoreInfo appScoreInfo = allApp.get(pkgName);
            if (appScoreInfo != null) {
                appScoreInfo.totalUsedCount = frequencyInfo.totalUsedCount;
                appScoreInfo.totalUsedTime = frequencyInfo.totalUsedTime;
                appScoreInfo.frequencyAvrageNum = frequencyInfo.frequencyAvrageNum;
                appScoreInfo.isRecord = true;
            }
        }
        /** 电量赋值 **/
        for (Entry<String, BatteryInfo> entity : batteryData.entrySet()) {
            String pkgName = entity.getKey();
            BatteryInfo batteryInfo = entity.getValue();
            AppScoreInfo appScoreInfo = allApp.get(pkgName);
            if (appScoreInfo != null) {
                appScoreInfo.totalBattery = batteryInfo.totalBattery;
                appScoreInfo.batteryAvrageNum = batteryInfo.batteryAvrageNum;
                appScoreInfo.isRecord = true;
            }
        }
        /** 流量赋值 **/
        for (Entry<String, MobileDataInfo> entity : mobileData.entrySet()) {
            String pkgName = entity.getKey();
            MobileDataInfo mobileDataInfo = entity.getValue();
            AppScoreInfo appScoreInfo = allApp.get(pkgName);
            if (appScoreInfo != null) {
                appScoreInfo.totalMobileData = mobileDataInfo.totalMobiledata;
                appScoreInfo.mobileDataAvrageNum = mobileDataInfo.mobileDataAvrageNum;
                appScoreInfo.isRecord = true;
            }
        }
        /** 初始化应用大小 **/
        setPkgSize(allApp);
        /** 初始化当天的3G流量 **/
        setTodayMobileData(allApp);
        return allApp;
    }

    /** 获取手机上所有的APP，并赋予默认值 **/
    private HashMap<String, AppScoreInfo> getAllApp() {
        Collection<ApplicationInfo> apps = PkgManagerTool.getInstalledApp(context);
        HashMap<String, AppScoreInfo> installedApps = new HashMap<String, AppScoreInfo>();
        for (ApplicationInfo app : apps) {
            if (!AppFilter.getInstance().isNeedShow(context, app.packageName)) {
                continue;
            }
            AppScoreInfo info = new AppScoreInfo();
            info.pkgName = app.packageName;
            installedApps.put(info.pkgName, info);
        }
        return installedApps;
    }

    /** 获取当天的流量 **/
    private void setTodayMobileData(HashMap<String, AppScoreInfo> appScoreInfo) {
        DatausageProvider p = new DatausageProvider();
        List<AppNetstatInfo> todayMobile = p.getUsageApps(context);
        if (todayMobile == null) {
            return;
        }
        for (AppNetstatInfo net : todayMobile) {
            AppScoreInfo tempObj = appScoreInfo.get(net.pkgName);
            if (tempObj != null) {
                tempObj.todayMobileData = net.mobiledataBytes;
            }
        }
    }

    /** 获取应用大小 **/
    private void setPkgSize(final HashMap<String, AppScoreInfo> appLog) {
        StorageProvider sp = new StorageProvider();
        List<PkgSizeInfo> temp = new ArrayList<PkgSizeInfo>();
        for (AppScoreInfo logInfo : new ArrayList<AppScoreInfo>(appLog.values())) {
            PkgSizeInfo info = new PkgSizeInfo();
            info.pkgName = logInfo.pkgName;
            temp.add(info);
        }
        sp.loadingData(context, temp, new IDataBack() {

            @Override
            public void dataBack(HashMap<String, PkgSizeInfo> data) {
                for (PkgSizeInfo info : data.values()) {
                    AppScoreInfo appLogInfo = appLog.get(info.pkgName);
                    if (appLogInfo == null) {
                        continue;
                    }
                    appLogInfo.pkgCache = info.cacheSize;
                    appLogInfo.pkgData = info.dataSize;
                    appLogInfo.pkgSize = info.codeSize;
                    appLogInfo.totalAPKSize = info.cacheSize + info.dataSize + info.codeSize;
                }
            }
        });
    }
}
