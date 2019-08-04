package com.tcl.manager.analyst.entity;

import com.tcl.manager.base.BaseInfo;

/**
 * 流量信息
 * 
 * @author jiaquan.huang
 * **/
public class MobileDataInfo extends BaseInfo {
    private static final long serialVersionUID = -2277213926919313928L;
    /** 移动数据流量 **/
    public long totalMobiledata;
    /** Wifi流量 **/
    public long totalWifidata;
    /** 流量平均数 **/
    public int mobileDataAvrageNum;
}
