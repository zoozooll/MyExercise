package com.tcl.manager.arithmetic.entity;

import com.tcl.manager.arithmetic.base.IDeductionArithmetic;

/**
 * 基础日志扣分算法适配。
 * 
 * @author jiaquan.huang
 * 
 */
public class DeductionInfo implements IDeductionArithmetic {

    AppScoreInfo appSocreInfo;

    public DeductionInfo(AppScoreInfo appSocreInfo) {
        this.appSocreInfo = appSocreInfo;
    }

    @Override
    public float getDataED() {
        return appSocreInfo.mobileDataED;
    }

    @Override
    public float getBatteryEB() {
        return appSocreInfo.batteryEB;
    }

    @Override
    public void setNeedOptimizeBatteryState(boolean state) {
        appSocreInfo.needOptimizedBattery = state;
    }

    @Override
    public void setNeedOptimzeNetState(boolean state) {
        appSocreInfo.needOptimizedNet = state;
    }

    @Override
    public void setDataReducesScore(int reduceScore) {
        appSocreInfo.dataReducesScore = reduceScore;
    }

    @Override
    public void setBatteryReducesScore(int reduceScore) {
        appSocreInfo.batteryReducesScore = reduceScore;
    }

    boolean isNeedOpreateBattery = true;
    boolean isNeedOpreateNet = true;

    @Override
    public boolean isNeedOpreateBattery() {
        return isNeedOpreateBattery;
    }

    @Override
    public boolean isNeedOpreateNet() {
        return isNeedOpreateNet;
    }

    @Override
    public void setNeedOpreateBattery(boolean state) {
        isNeedOpreateBattery = state;
    }

    @Override
    public void setNeedOpreateNet(boolean state) {
        isNeedOpreateNet = state;
    }

    @Override
    public void setBatteryRisk(boolean isRisk) {
        appSocreInfo.isBatteryRisk = isRisk;

    }

    @Override
    public void setNetRisk(boolean isRisk) {
        appSocreInfo.isNetRisk = isRisk;
    }

    @Override
    public boolean isBatteryRisk() {
        return appSocreInfo.isBatteryRisk;
    }

    @Override
    public boolean isNetRisk() {
        return appSocreInfo.isNetRisk;
    }
}
