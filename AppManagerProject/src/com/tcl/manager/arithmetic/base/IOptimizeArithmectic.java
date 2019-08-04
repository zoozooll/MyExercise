package com.tcl.manager.arithmetic.base;

/**
 * 优化（开关电量流量视为优化操作）
 * 
 * @author jiaquan.huang
 * 
 */
public interface IOptimizeArithmectic {

    /** 是否被日志统计 **/
    public boolean getIsRecord();

    /** 流量相关操作 **/
    public void setOpenDataAccess(boolean isOpenDataAccess);

    public void setOptimizedDataED(int optimizedDataED);

    public void setIsNeedOptimizeNet(boolean isNeedOptimizedNet);

    public void setIsNetRisk(boolean isRisk);

    public float getDataED();

    /** 电量相关操作 **/
    public void setOpenAutoStart(boolean isOpenAutoStart);

    public void setOptimizedBatteryEB(int optimizedBatterEB);

    public void setIsNeedOptimizeBattery(boolean isNeedOptimizeBattery);

    public void setIsBatteryRisk(boolean isRisk);

    public float getBatteryED();
}
