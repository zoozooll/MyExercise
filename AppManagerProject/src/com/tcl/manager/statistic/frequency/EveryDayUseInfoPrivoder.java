package com.tcl.manager.statistic.frequency;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import com.tcl.framework.util.AndroidNewApi;
import com.tcl.manager.statistic.frequency.UsageStats.PkgUsageStatsExtended;

import android.content.Context;

/**
 * @Description:获取当天使用情况，使用时间已经转换成秒
 * @author jiaquan.huang
 * @date 2014-12-20 下午1:18:19
 * @copyright TCL-MIE
 */
public class EveryDayUseInfoPrivoder {
    /** 5.0以前的取一天频率时长数据的老方法 **/
    private static List<FrequencyEntity> provideInfo(Context context) {
        List<FrequencyEntity> usageInfos = new ArrayList<FrequencyEntity>();
        Calendar calendar = Calendar.getInstance();
        long endTime = calendar.getTimeInMillis();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long fromTime = calendar.getTimeInMillis();
        UsageReader reader = UsageReader.getInstance(context);
        Map<String, PkgUsageStatsExtended> info = reader.readByTime(endTime);
        for (Entry<String, PkgUsageStatsExtended> entry : info.entrySet()) {
            FrequencyEntity entity = new FrequencyEntity();
            entity.pkgName = entry.getKey();
            entity.fromTime = fromTime;
            entity.endTime = endTime;
            PkgUsageStatsExtended u = entry.getValue();
            entity.usedCount = u.mLaunchCount;
            entity.usedTime = u.mUsageTime / 1000;// 转换成秒
            usageInfos.add(entity);
        }
        return usageInfos;
    }

    public static List<FrequencyEntity> provide(Context context) {
        if (AndroidNewApi.IsSDKLevelAbove(21)) {
            return provideInfoV5(context);
        } else {
            return provideInfo(context);
        }
    }

    /** 5.0取“相对一天”数据的取频率和时长 新方法 **/
    private static List<FrequencyEntity> provideInfoV5(Context context) {
        /** 开始时间0：00 **/
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 0);
        start.set(Calendar.MINUTE, 0);
        start.set(Calendar.MILLISECOND, 0);
        start.set(Calendar.SECOND, 0);
        /** 结束时间当前时间 **/
        long startTime = start.getTimeInMillis();
        Calendar end = Calendar.getInstance();
        long endTime = end.getTimeInMillis();
        V5UsageReader reader = new V5UsageReader();
        List<FrequencyEntity> usageInfos = reader.getUsageInfo(context, startTime, endTime);
        return usageInfos;
    }

}
