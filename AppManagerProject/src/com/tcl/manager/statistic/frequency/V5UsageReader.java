package com.tcl.manager.statistic.frequency;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;

/**
 * android5.0获取使用时长和次数
 * 
 * @author jiaquan.huang
 * 
 */
public class V5UsageReader {

    /**
     * 获取使用次数和时长
     * 
     * @param startTime
     *            起始时间
     * @param endTime
     *            结束时间
     * @return
     * **/
    public List<FrequencyEntity> getUsageInfo(Context context, long startTime, long endTime) {
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getSystemService("usagestats");// Context.USAGE_STATS_SERVICE);
        List<UsageStats> list = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, startTime, endTime);
        List<FrequencyEntity> usageInfos = new ArrayList<FrequencyEntity>();
        for (UsageStats usage : list) {
            long usedTime = usage.getTotalTimeInForeground();
            long useCount = getLaunchCount(usage);
            long firstTime = usage.getFirstTimeStamp();
            long lastTime = usage.getLastTimeStamp();
            FrequencyEntity entity = new FrequencyEntity();
            entity.pkgName = usage.getPackageName();
            entity.usedTime = usedTime / 1000;// 毫秒转秒
            entity.usedCount = useCount;
            entity.fromTime = firstTime;
            entity.endTime = lastTime;
            usageInfos.add(entity);
        }
        return usageInfos;
    }

    /** 反射使用次数 **/
    public long getLaunchCount(UsageStats usageStats) {
        try {
            Field fMLaunchCount = UsageStats.class.getField("mLaunchCount");
            fMLaunchCount.setAccessible(true);
            long mLaunchCount = fMLaunchCount.getLong(usageStats);
            return mLaunchCount;
        } catch (Exception e) {
            return 0;
        }
    }

    public void checkPermission() {
    }

}
