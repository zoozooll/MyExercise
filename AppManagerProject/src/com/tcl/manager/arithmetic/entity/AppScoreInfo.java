package com.tcl.manager.arithmetic.entity;

import com.tcl.manager.arithmetic.opetate.FrequencyLevel;
import com.tcl.manager.base.BaseInfo;

/**
 * @Description: 需要算分的app的信息
 * @author jiaquan.huang
 * @date 2014-12-18 下午2:00:14
 * @copyright TCL-MIE
 */

public class AppScoreInfo extends BaseInfo {
    private static final long serialVersionUID = -2868150514010570094L;
    public long pkgCache;
    public long pkgData;
    public long pkgSize;

    public long totalMobileData;// 统计到的移动数据总流量
    public long totalBattery;// 统计到的电量
    public long totalAPKSize;// 总大小
    public long totalUsedTime;// 统计到的使用的时间
    public long totalUsedCount;// 统计到的使用的次数

    public int frequencyAvrageNum;// 平均数
    public int batteryAvrageNum;// 平均数
    public int mobileDataAvrageNum;// 平均数

    public float averageBattery;// 平均每天的流量
    public float averageMoblieData;// 平均每天的流量
    public float averagePkgSize;// 平均每天的APK大小
    public float avrageUsedTime;// 平均每天的使用时间
    public float avrageUsedCount;// 平均每天的使用次数

    public float avrageUsedTimePercent;// 平均使用时长百分比
    public float avrageUsedCountPercent;// 平均使用次数百分比
    public float avrageBatteryPercent;// 平均电量百分比
    public float avrageMobileDataPercent;// 平均流量百分比
    public float apkSizePercent;// 应用大小百分比

    public float useTimeScore;// 时长得分
    public float useCountScore;// 次数得分
    public float frequencyScore;// 频率得分
    public FrequencyLevel frequencyLevel;// 使用频率的等级

    public float score;// 总得分
    public float mobileDataED;// 流量得分
    public float batteryEB;// 电量得分
    public float apkSizeES;// 应用大小得分

    public float uesF;// 频率f占比
    public long lastlyRecordTimes;// 最近时间
    public int allDays;// 需要计算的天数
    public int recordDays;// 统计到的天数
    public boolean isRecord;// 是否被统计

    /** 附加信息 **/
    public long todayMobileData;// 今天的流量
    public boolean isOpenAutoStart = true;// 默认开自启动
    public boolean isOpenDataAccess = true;// 默认开流量开关

    /** 流量需要优化 **/
    public boolean needOptimizedNet = false;
    /** 电量需要优化 **/
    public boolean needOptimizedBattery = false;
    /** 是否电量风险 **/
    public boolean isBatteryRisk;
    /** 是否流量风险 **/
    public boolean isNetRisk;
    /** 优化后流量得分 **/
    public float optimizedDataED;
    /** 优化后 电量得分 **/
    public float optimizedBatteryEB;
    /** 优化后应用大小得分 **/
    public float optimizedPkgSizeES;
    /** 流量风险所扣分 **/
    public int dataReducesScore;
    /** 电量风险所扣分 **/
    public int batteryReducesScore;

}
