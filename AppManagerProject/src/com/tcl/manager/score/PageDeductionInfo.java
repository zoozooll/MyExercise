package com.tcl.manager.score;

import com.tcl.manager.arithmetic.base.IDeductionArithmetic;

/**
 * 页面数据参与扣分算法适配
 * 
 * @author jiaquan.huang
 **/
public class PageDeductionInfo implements IDeductionArithmetic {
    PageInfo pageInfo;

    public PageDeductionInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public float getDataED() {
        return pageInfo.dataED;
    }

    @Override
    public float getBatteryEB() {
        return pageInfo.batteryEB;
    }

    @Override
    public void setNeedOptimizeBatteryState(boolean state) {
        pageInfo.needOptimizedBattery = state;
    }

    @Override
    public void setNeedOptimzeNetState(boolean state) {
        pageInfo.needOptimizedNet = state;
    }

    @Override
    public void setDataReducesScore(int reduceScore) {
        pageInfo.dataReducesScore = reduceScore;
    }

    @Override
    public void setBatteryReducesScore(int reduceScore) {
        pageInfo.batteryReducesScore = reduceScore;
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
    public boolean isBatteryRisk() {
        return pageInfo.isBatteryRisk;
    }

    @Override
    public boolean isNetRisk() {
        return pageInfo.isNetRisk;
    }

    @Override
    public void setBatteryRisk(boolean isRisk) {
        pageInfo.isBatteryRisk = isRisk;
    }

    @Override
    public void setNetRisk(boolean isRisk) {
        pageInfo.isNetRisk = isRisk;

    }
}
