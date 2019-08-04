package com.tcl.manager.analyst.provider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import android.content.Context;
import android.text.TextUtils;
import com.tcl.manager.analyst.entity.BatteryInfo;
import com.tcl.manager.analyst.entity.TimeGroupInfo;
import com.tcl.manager.battery.AppInfoBatteryUsage;
import com.tcl.manager.battery.BatteryUsageProvider;
import com.tcl.manager.score.PageFunctionProvider;

/**
 * 电量提供
 * 
 * @author jiaquan.huang
 **/
public class BatteryProvider implements IRead {
    Context mContext;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public BatteryProvider(Context context) {
        mContext = context;
    }

    public long getUpdateTime(String pkgName) {
        return PageFunctionProvider.getUpdateTime(pkgName);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, BatteryInfo> read(long fromTime, long endTime) {
        BatteryUsageProvider provider = new BatteryUsageProvider();
        Date fromDate = new Date(fromTime);
        Date endData = new Date(endTime);
        /** key为yyyyMMdd日期 **/
        Map<String, List<AppInfoBatteryUsage>> batteryData = provider.getAppBatteryUsageHistory(mContext, fromDate, endData);
        HashMap<String, BatteryInfo> data = new HashMap<String, BatteryInfo>();
        if (batteryData == null || batteryData.isEmpty()) {
            return data;
        }
        HashMap<String, TimeGroupInfo> timeGroups = getTimeGroup(data.keySet());
        for (Entry<String, List<AppInfoBatteryUsage>> entity : batteryData.entrySet()) {
            if (entity.getValue() == null || entity.getKey().isEmpty()) {
                continue;
            }
            for (AppInfoBatteryUsage batteryInfo : entity.getValue()) {
                /** 记录时间小于更新时间 ,不参与计算 **/
                long updateTime = getUpdateTime(batteryInfo.pkgName);
                long tempEndTime = 0;
                String key = entity.getKey();
                TimeGroupInfo timeInfo = timeGroups.get(key);
                if (timeInfo != null) {
                    endTime = timeInfo.endTime;
                }
                if (TextUtils.isEmpty(batteryInfo.pkgName) || tempEndTime <= updateTime) {
                    continue;
                }
                BatteryInfo tempBattery = data.get(batteryInfo.pkgName);
                if (tempBattery == null) {
                    tempBattery = new BatteryInfo();
                    tempBattery.pkgName = batteryInfo.pkgName;
                    data.put(tempBattery.pkgName, tempBattery);
                }
                tempBattery.totalBattery += batteryInfo.usage;
                if (tempBattery.batteryAvrageNum == 0) {
                    tempBattery.batteryAvrageNum = AverageNumLogic.getAverageNum(timeGroups.values(), updateTime);
                }
            }
        }
        return data;
    }

    /** 生成一个时间组 ,日期作key **/
    private HashMap<String, TimeGroupInfo> getTimeGroup(Collection<String> date) {
        HashMap<String, TimeGroupInfo> times = new HashMap<String, TimeGroupInfo>();
        for (String tempDate : date) {
            Calendar c = Calendar.getInstance();
            Date dateTime = null;
            try {
                dateTime = df.parse(tempDate);
            } catch (Exception e) {
                continue;
            }
            c.setTime(dateTime);
            c.set(Calendar.HOUR_OF_DAY, 0);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.MILLISECOND, 0);
            long fromTime = c.getTimeInMillis();
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 59);
            c.set(Calendar.MILLISECOND, 59);
            long endTime = c.getTimeInMillis();
            TimeGroupInfo timeGroupInfo = new TimeGroupInfo();
            timeGroupInfo.fromTime = fromTime;
            timeGroupInfo.endTime = endTime;
            times.put(tempDate, timeGroupInfo);
        }
        return times;
    }

}
