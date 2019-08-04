package com.tcl.manager.arithmetic.opetate;

import java.util.Collection;
import com.tcl.manager.arithmetic.base.IOperate;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;

/**
 * 得分计算
 * 
 * @author jiaquan.huang
 * 
 */
public class ValueArithmetic implements IOperate<Collection<AppScoreInfo>> {
    @Override
    public void operate(Collection<AppScoreInfo> data) {
        operateTemp(data);
    }

    private void operateTemp(Collection<AppScoreInfo> data) {
        /** 平均数之和 **/
        long totalAvrageUsedTime = 0;
        long totalAvrageBattery = 0;
        long totalAvrageMobileData = 0;
        long totalAPKSize = 0;
        /** 最大百分比 **/
        float maxUseTimePercent = 0;
        float maxBatteryPercent = 0;
        float maxMobileDataPercent = 0;
        float maxAPKSizePercent = 0;
        /** 最大百分比对应最大值的系数 **/
        float useTimeX = 0;
        float batteryX = 0;
        float mobileX = 0;
        float apkSizeX = 0;
        /******************************** 计算平均数 ****************************************/
        for (AppScoreInfo info : data) {
            /** 频率 **/
            if (info.frequencyAvrageNum > 0) {
                info.avrageUsedTime = info.totalUsedTime * 1f / info.frequencyAvrageNum;
                totalAvrageUsedTime += info.avrageUsedTime;
            }
            /** 电量 **/
            if (info.batteryAvrageNum > 0) {
                info.averageBattery = info.totalBattery * 1f / info.batteryAvrageNum;
                totalAvrageBattery += info.averageBattery;
            }
            /** 流量 **/
            if (info.mobileDataAvrageNum > 0) {
                info.averageMoblieData = info.totalMobileData * 1f / info.mobileDataAvrageNum;
                totalAvrageMobileData += info.averageMoblieData;
            }
            /** 应用大小 **/
            {
                totalAPKSize += info.totalAPKSize;
            }
        }
        /******************************** 计算百分比并获取最大百分比 ****************************************/
        for (AppScoreInfo info : data) {
            /** 频率百分比 **/
            if (totalAvrageUsedTime > 0) {
                info.avrageUsedTimePercent = info.avrageUsedTime / totalAvrageUsedTime;
                if (maxUseTimePercent < info.avrageUsedTimePercent) {
                    /** 取最大平均使用时长数 **/
                    maxUseTimePercent = info.avrageUsedTimePercent;
                }
            }
            /** 电量百分比 */
            if (totalAvrageBattery > 0) {
                info.avrageBatteryPercent = info.averageBattery / totalAvrageBattery;
                if (maxBatteryPercent < info.avrageBatteryPercent) {
                    maxBatteryPercent = info.avrageBatteryPercent;
                }
            }
            /** 流量百分比 **/
            if (totalAvrageMobileData > 0) {
                info.avrageMobileDataPercent = info.averageMoblieData / totalAvrageMobileData;
                if (maxMobileDataPercent < info.avrageMobileDataPercent) {
                    maxMobileDataPercent = info.avrageMobileDataPercent;
                }
            }
            /** 应用大小 **/
            if (totalAPKSize > 0) {
                info.apkSizePercent = info.totalAPKSize * 1f / totalAPKSize;
                if (maxAPKSizePercent < info.apkSizePercent) {
                    maxAPKSizePercent = info.apkSizePercent;
                }
            }
        }
        /********************************* 获取系数 ************************************/
        if (maxUseTimePercent != 0) {
            useTimeX = FactorProvider.getFactor(maxUseTimePercent, FactorProvider.MAX_USEDTIME_SCORE);
        }
        if (maxBatteryPercent != 0) {
            batteryX = FactorProvider.getFactor(maxBatteryPercent, FactorProvider.MAX_BATTERY_SCORE);
        }
        if (maxMobileDataPercent != 0) {
            mobileX = FactorProvider.getFactor(maxMobileDataPercent, FactorProvider.MAX_MOBILE_SCORE);
        }
        if (maxAPKSizePercent != 0) {
            apkSizeX = FactorProvider.getFactor(maxAPKSizePercent, FactorProvider.MAX_APKSIZE_SCORE);
        }
        /********************************** 根据系数求对应的值 *****************************/
        for (AppScoreInfo info : data) {
            /** f占比 **/
            info.uesF = FzbProvider.getFZBValue(info.avrageUsedTimePercent);
            /** 频率得分 **/
            info.frequencyScore = info.avrageUsedTimePercent * useTimeX;
            /** 频率等级 **/
            info.frequencyLevel = FrequencyLevelProvider.getUseLevel(info.frequencyScore, info.isRecord);
            /** 记录过的电量得分 **/
            info.batteryEB = FactorProvider.MAX_BATTERY_SCORE - info.avrageBatteryPercent * batteryX * info.uesF;
            /** 记录过的流量得分 **/
            info.mobileDataED = FactorProvider.MAX_MOBILE_SCORE - info.avrageMobileDataPercent * mobileX * info.uesF;
            /** 应用大小得分 **/
            info.apkSizeES = FactorProvider.MAX_APKSIZE_SCORE - info.apkSizePercent * apkSizeX * info.uesF;
            /** app得分 **/
            info.score = info.batteryEB + info.mobileDataED + info.apkSizeES;
        }
    }
}
