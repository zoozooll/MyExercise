package com.tcl.manager.analyst.entity;

import com.tcl.manager.base.BaseInfo;

/**
 * 频率记录的信息
 * 
 * @author jiaquan.huang
 * 
 */
public class FrequencyInfo extends BaseInfo {
    private static final long serialVersionUID = 2154053430765393656L;
    /** 总时长 **/
    public long totalUsedTime;
    /** 总次数 **/
    public long totalUsedCount;
    /** 频率平均数 **/
    public int frequencyAvrageNum;

}
