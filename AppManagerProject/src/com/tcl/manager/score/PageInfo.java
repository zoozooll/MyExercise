package com.tcl.manager.score;

import com.tcl.manager.arithmetic.opetate.FrequencyLevel;
import com.tcl.manager.base.BaseInfo;

/**
 * 页面数据
 * 
 * @author jiaquan.huang
 * @date 2014-12-29 下午3:33:44
 * @copyright TCL-MIE
 */
public class PageInfo extends BaseInfo {
    /** 程序名 **/
    public String appName;
    /** app默认得分 **/
    public int score;
    /** 使用频率等级 **/
    public FrequencyLevel frequencyLevel;
    /** 平均使用时间-秒/day **/
    public long averageUseTime;
    /** 平均使用次数-次 /day **/
    public long averageUseCount;
    /** 平均流量大小-Byte/day **/
    public long averageMobileSize;
    /** 今天流量大小-Byte **/
    public long todayMobileSize;
    /** 应用大小-Byte **/
    public long appSize;
    /** 应用数据大小-Byte **/
    public long appDataSize;
    // /** 建议 **/
    // public Suggest suggest;
    /** 自启动开关 **/
    public boolean isOpenAutoStart = true;
    /** 流量开关 **/
    public boolean isOpenDataAccess = true;
    /** 电量百分比 **/
    public int batteryPercent;
    /** 流量百分比 **/
    public int mobileDataPercent;
    /** 是否被统计 **/
    public boolean isRecord;
    /** 电量需要优化 **/
    public boolean needOptimizedBattery;
    /** 流量需要优化 **/
    public boolean needOptimizedNet;
    /** 是否电量风险 **/
    public boolean isBatteryRisk;
    /** 是否流量风险 **/
    public boolean isNetRisk;
    /** 流量减分 **/
    public int dataReducesScore;
    /** 电量减分 **/
    public int batteryReducesScore;
    /** 是否在内存运行 **/
    public boolean isRunning;
    /** 运行内存大小-Byte **/
    public long memorySize;
    /** 释放掉内存能涨的分数 ? 杀死当前进程后的总分 **/
    public float indexScore;

    public float dataED;// 流量得分
    public float batteryEB;// 电量得分
    public float pkgSizeES;// 应用大小得分

    public float optimizedDataED;// 优化后流量得分
    public float optimizedBatteryEB;// 优化后 电量得分
    public float optimizedPkgSizeES;// 优化后应用大小得分
}
