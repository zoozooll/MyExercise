package com.tcl.manager.statistic.frequency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import android.content.Context;
import android.util.Log;

import com.tcl.base.http.IProviderCallback;
import com.tcl.framework.log.NLog;
import com.tcl.manager.analyst.Analyst;
import com.tcl.manager.analyst.IAnalystListener;
import com.tcl.manager.analyst.entity.BatteryInfo;
import com.tcl.manager.analyst.entity.FrequencyInfo;
import com.tcl.manager.analyst.entity.MobileDataInfo;
import com.tcl.manager.analyst.provider.BatteryProvider;
import com.tcl.manager.analyst.provider.FrequencyProvider;
import com.tcl.manager.analyst.provider.MobileDataProvider;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.pkgsize.PkgSizeInfo;
import com.tcl.manager.pkgsize.StorageProvider;
import com.tcl.manager.pkgsize.StorageProvider.IDataBack;
import com.tcl.manager.protocol.LogReportProtocol;
import com.tcl.manager.protocol.data.LogInfo;
import com.tcl.manager.protocol.data.LogInfo.AppInfo;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.MemoryInfoProvider;

/**
 * 日志上报（上报昨天统计到的数据和当前的手机情况）
 * 
 * @author jiaquan.huang
 * 
 */
public class LogReport {

    Context mContext;

    public LogReport(Context context) {
        mContext = context;
    }

    public HashMap<String, AppInfo> getByTime(long fromTime, long endTime) {
        /** 频率获取 **/
        FrequencyProvider frequency = new FrequencyProvider();
        HashMap<String, FrequencyInfo> frequencyData = frequency.getLastedFrequency(fromTime, endTime);
        /** 电量获取 **/
        BatteryProvider batteryProvider = new BatteryProvider(mContext);
        HashMap<String, BatteryInfo> batteryData = batteryProvider.read(fromTime, endTime);
        /** 流量获取 **/
        MobileDataProvider mobileProvider = new MobileDataProvider(mContext);
        HashMap<String, MobileDataInfo> mobileData = mobileProvider.read(fromTime, endTime);

        HashMap<String, AppInfo> logs = new HashMap<String, LogInfo.AppInfo>();

        for (FrequencyInfo info : frequencyData.values()) {
            AppInfo tempInfo;
            tempInfo = logs.get(info.pkgName);
            if (tempInfo == null) {
                tempInfo = new AppInfo();
                logs.put(info.pkgName, tempInfo);
            }
            tempInfo.package_name = info.pkgName;
            // 掉精没关系，使用次数达不到int最大值
            tempInfo.usage_count = (int) info.totalUsedCount;
            tempInfo.usage_time = info.totalUsedTime;
        }

        for (BatteryInfo info : batteryData.values()) {
            AppInfo tempInfo;
            tempInfo = logs.get(info.pkgName);
            if (tempInfo == null) {
                tempInfo = new AppInfo();
                logs.put(info.pkgName, tempInfo);
            }
            tempInfo.battery_consume = String.valueOf(info.totalBattery);
        }
        for (MobileDataInfo info : mobileData.values()) {
            AppInfo tempInfo;
            tempInfo = logs.get(info.pkgName);
            if (tempInfo == null) {
                tempInfo = new AppInfo();
                logs.put(info.pkgName, tempInfo);
            }
            tempInfo.wifi_data = info.totalWifidata;
            tempInfo.mobile_data = info.totalMobiledata;
        }
        setPkgSize(logs);
        return logs;
    }

    public void report() {
        /* 手机基本信息 */
        final LogInfo logInfo = new LogInfo();
        MemoryInfoProvider provider = MemoryInfoProvider.getInstance(mContext);

        logInfo.imei = AndroidUtil.getImei(mContext);
        logInfo.user_account = "";
        logInfo.write_time = System.currentTimeMillis();
        logInfo.write_timezone = TimeZone.getDefault().getID();
        logInfo.total_ram = provider.totalMem;

        /* 手机APP信息 */
        /** 昨天时段 **/
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long fromTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.MILLISECOND, 59);
        long endTime = calendar.getTimeInMillis();

        final HashMap<String, AppInfo> logs = getByTime(fromTime, endTime);
        /** 计算出30天的分数 ***/
        Analyst analyst = new Analyst(mContext, new IAnalystListener() {

            @Override
            public void totalScoreBack(List<AppScoreInfo> list, int totalScore, int appsScore, int memoryScore) {
                for (AppScoreInfo appInfo : list) {
                    AppInfo tempObj = logs.get(appInfo.pkgName);
                    if (tempObj != null) {
                        tempObj.apk_score = (int) appInfo.score;
                    }
                }

                logInfo.list = new ArrayList<LogInfo.AppInfo>(logs.values());
                /** 给整个程序打上总分 **/
                logInfo.total_score = totalScore;

                /** 收集数据结束上报 ***/
                new LogReportProtocol(logInfo, new IProviderCallback<Boolean>() {

                    @Override
                    public void onSuccess(Boolean obj) {
                        NLog.i("LogReport", "onSuccess" + obj);
                    }

                    @Override
                    public void onFailed(int code, String msg, Object obj) {
                    	NLog.w("LogReport", "onFailed" + obj);
                    }

                    @Override
                    public void onCancel() {
                    	NLog.w("LogReport", "onCancel");
                    }
                }).send();
            }

            @Override
            public void memoryScoreBack(int memoryScore) {

            }

        });
        analyst.startAnalysisComplete();

    }

    /** 获取应用大小 **/
    private void setPkgSize(final HashMap<String, AppInfo> sizeData) {
        StorageProvider sp = new StorageProvider();
        List<PkgSizeInfo> temp = new ArrayList<PkgSizeInfo>();
        for (AppInfo logInfo : sizeData.values()) {
            PkgSizeInfo info = new PkgSizeInfo();
            info.pkgName = logInfo.package_name;
            temp.add(info);
        }
        sp.loadingData(mContext, temp, new IDataBack() {

            @Override
            public void dataBack(HashMap<String, PkgSizeInfo> data) {
                for (PkgSizeInfo info : data.values()) {
                    AppInfo app = sizeData.get(info.pkgName);
                    if (app != null) {
                        app.apk_cache = info.cacheSize;
                        app.apk_data = info.dataSize;
                        app.apk_size = info.codeSize;
                    }
                }
            }
        });
    }
}
