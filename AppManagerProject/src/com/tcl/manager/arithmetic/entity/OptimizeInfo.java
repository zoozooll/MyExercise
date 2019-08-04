package com.tcl.manager.arithmetic.entity;

import com.tcl.manager.arithmetic.base.IOptimizeArithmectic;


/**
 * 基础日志优化适配
 * 
 * @author jiaquan.huang
 **/
public class OptimizeInfo implements IOptimizeArithmectic {

    AppScoreInfo info;

    public OptimizeInfo(AppScoreInfo info) {
        this.info = info;
    }

    @Override
    public boolean getIsRecord() {
        return info.isRecord;
    }

    @Override
    public void setOpenDataAccess(boolean isOpenDataAccess) {
        info.isOpenDataAccess = isOpenDataAccess;
    }

    @Override
    public void setOptimizedDataED(int optimizedDataED) {
        info.optimizedDataED = optimizedDataED;
    }

    @Override
    public void setIsNeedOptimizeNet(boolean isNeedOptimizedNet) {
        info.needOptimizedNet = isNeedOptimizedNet;
    }

    @Override
    public float getDataED() {
        return info.mobileDataED;
    }

    @Override
    public void setOpenAutoStart(boolean isOpenAutoStart) {
        info.isOpenAutoStart = isOpenAutoStart;
    }

    @Override
    public void setOptimizedBatteryEB(int optimizedBatteryEB) {
        info.optimizedBatteryEB = optimizedBatteryEB;
    }

    @Override
    public void setIsNeedOptimizeBattery(boolean isNeedOptimizeBattery) {
        info.needOptimizedBattery = isNeedOptimizeBattery;
    }

    @Override
    public float getBatteryED() {
        return info.batteryEB;
    }

    @Override
    public void setIsNetRisk(boolean isRisk) {
        info.isNetRisk = isRisk;
    }

    @Override
    public void setIsBatteryRisk(boolean isRisk) {
        info.isBatteryRisk = isRisk;
    }

}
