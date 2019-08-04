package com.tcl.manager.analyst.provider;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import android.content.Context;
import android.text.TextUtils;

import com.tcl.manager.analyst.AppFilter;
import com.tcl.manager.analyst.entity.FrequencyInfo;
import com.tcl.manager.analyst.entity.TimeGroupInfo;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.statistic.frequency.FrequencyColumn;
import com.tcl.manager.statistic.frequency.FrequencyDBHelper;
import com.tcl.manager.statistic.frequency.FrequencyEntity;
import com.tcl.manager.util.PkgManagerTool;

/**
 * 频率获取
 * 
 * @time 2015-2-3 19:17
 * @author jiaquan.huang
 */
public class FrequencyProvider implements IRead {

    public FrequencyProvider() {
    }

    public long getUpdateTime(String pkgName) {
        return PageFunctionProvider.getUpdateTime(pkgName);
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, FrequencyInfo> read(long fromTime, long endTime) {
        List<FrequencyColumn> data = FrequencyDBHelper.getInstance().select(fromTime, endTime);
        return parseData(data);
    }

    /** 解析频率 **/
    private HashMap<String, FrequencyInfo> parseData(List<FrequencyColumn> data) {
        HashMap<String, FrequencyInfo> frequencyInfos = new HashMap<String, FrequencyInfo>();
        HashMap<String, TimeGroupInfo> times = getTimeGroup(data);
        for (FrequencyColumn column : data) {
            if (column.frequencyEntity == null) {
                continue;
            }
            for (FrequencyEntity entity : column.frequencyEntity) {
                /** 记录时间小于等于更新时间 ,不参与计算 **/
                long updateTime = getUpdateTime(entity.pkgName);
                if (updateTime >= entity.endTime || TextUtils.isEmpty(entity.pkgName)) {
                    continue;
                }
                /** 赋值 **/
                FrequencyInfo tempInfo;
                if (frequencyInfos.containsKey(entity.pkgName)) {
                    tempInfo = frequencyInfos.get(entity.pkgName);
                } else {
                    tempInfo = new FrequencyInfo();
                    frequencyInfos.put(entity.pkgName, tempInfo);
                }
                tempInfo.pkgName = entity.pkgName;
                tempInfo.totalUsedCount += entity.usedCount;
                tempInfo.totalUsedTime += entity.usedTime;
                if (tempInfo.frequencyAvrageNum == 0) {
                    tempInfo.frequencyAvrageNum = AverageNumLogic.getAverageNum(times.values(), updateTime);
                }
            }
        }

        return frequencyInfos;
    }

    /** 生成一个时间组 **/
    private HashMap<String, TimeGroupInfo> getTimeGroup(Collection<FrequencyColumn> data) {
        HashMap<String, TimeGroupInfo> times = new HashMap<String, TimeGroupInfo>();
        for (FrequencyColumn column : data) {
            TimeGroupInfo timeGroupInfo = new TimeGroupInfo();
            timeGroupInfo.fromTime = column.fromTime;
            timeGroupInfo.endTime = column.endTime;
            times.put(String.valueOf(column.endTime), timeGroupInfo);
        }
        return times;
    }

    /** 获取某一时段中最后一个时段的频率数据 **/
    public HashMap<String, FrequencyInfo> getLastedFrequency(long fromTime, long endTime) {
        FrequencyColumn column = FrequencyDBHelper.getInstance().getFirst(fromTime, endTime);
        HashMap<String, FrequencyInfo> frequencyInfos = new HashMap<String, FrequencyInfo>();
        if (column == null || column.frequencyEntity == null || column.frequencyEntity.isEmpty()) {
            return frequencyInfos;
        }
        for (FrequencyEntity entity : column.frequencyEntity) {
            if (TextUtils.isEmpty(entity.pkgName)) {
                continue;
            }
            /** 附加 **/
            FrequencyInfo tempInfo;
            if (frequencyInfos.containsKey(entity.pkgName)) {
                tempInfo = frequencyInfos.get(entity.pkgName);
            } else {
                tempInfo = new FrequencyInfo();
                frequencyInfos.put(entity.pkgName, tempInfo);
            }
            tempInfo.pkgName = entity.pkgName;
            tempInfo.totalUsedCount += entity.usedCount;
            tempInfo.totalUsedTime += entity.usedTime;
            tempInfo.frequencyAvrageNum++;
        }
        return frequencyInfos;
    }
}
