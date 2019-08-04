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

import com.tcl.manager.analyst.entity.MobileDataInfo;
import com.tcl.manager.analyst.entity.TimeGroupInfo;
import com.tcl.manager.datausage.AppNetstatInfo;
import com.tcl.manager.datausage.DatausageHistoryProvider;
import com.tcl.manager.score.PageFunctionProvider;

/**
 * 流量获取
 * 
 * @author jiaquan.huang
 * 
 */
public class MobileDataProvider implements IRead {

    Context mContext;
    SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());

    public MobileDataProvider(Context context) {
        mContext = context.getApplicationContext();
    }

    public long getUpdateTime(String pkgName) {
        return PageFunctionProvider.getUpdateTime(pkgName);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, MobileDataInfo> read(long fromTime, long endTime) {
        DatausageHistoryProvider provider = new DatausageHistoryProvider();
        Date fromDate = new Date(fromTime);
        Date endData = new Date(endTime);
        /** key为yyyyMMdd日期 **/
        Map<String, List<AppNetstatInfo>> mobileData = provider.getUsageApps(mContext, fromDate, endData);
        HashMap<String, MobileDataInfo> data = new HashMap<String, MobileDataInfo>();
        if (mobileData == null || mobileData.isEmpty()) {
            return data;
        }
        HashMap<String, TimeGroupInfo> timeGroups = getTimeGroup(data.keySet());
        for (Entry<String, List<AppNetstatInfo>> entity : mobileData.entrySet()) {
            if (entity.getValue() == null || entity.getKey().isEmpty()) {
                continue;
            }
            for (AppNetstatInfo netInfo : entity.getValue()) {
                /** 记录时间小于更新时间 ,不参与计算 **/
                String key = entity.getKey();
                long updateTime = getUpdateTime(netInfo.pkgName);
                long saveTime = 0;
                try {
                    saveTime = df.parse(key).getTime();
                } catch (Exception e) {
                    continue;
                }
                if (TextUtils.isEmpty(netInfo.pkgName) || saveTime < updateTime) {
                    continue;
                }
                MobileDataInfo tempMobileInfo = data.get(netInfo.pkgName);
                if (tempMobileInfo == null) {
                    tempMobileInfo = new MobileDataInfo();
                    tempMobileInfo.pkgName = netInfo.pkgName;
                    data.put(tempMobileInfo.pkgName, tempMobileInfo);
                }
                tempMobileInfo.totalMobiledata += netInfo.mobiledataBytes;
                if (tempMobileInfo.mobileDataAvrageNum == 0) {
                    tempMobileInfo.totalMobiledata = AverageNumLogic.getAverageNum(timeGroups.values(), updateTime);
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
