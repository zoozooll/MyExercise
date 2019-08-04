package com.tcl.manager.score;

import com.tcl.manager.arithmetic.base.IOptimizeArithmectic;

/**
 * 页面数据参与优化算法适配
 * 
 * @author jiaquan.huang
 * 
 */
public class PageOptimizeInfo implements IOptimizeArithmectic {

    PageInfo pageInfo;

    public PageOptimizeInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public boolean getIsRecord() {
        return pageInfo.isRecord;
    }

    @Override
    public void setOpenDataAccess(boolean isOpenDataAccess) {
        pageInfo.isOpenDataAccess = isOpenDataAccess;
    }

    @Override
    public void setOptimizedDataED(int optimizedDataED) {
        pageInfo.optimizedDataED = optimizedDataED;
    }

    @Override
    public void setIsNeedOptimizeNet(boolean isNeedOptimizedNet) {
        pageInfo.needOptimizedNet = isNeedOptimizedNet;
    }

    @Override
    public void setIsNetRisk(boolean isRisk) {
        pageInfo.isNetRisk = isRisk;
    }

    @Override
    public float getDataED() {
        return pageInfo.dataED;
    }

    @Override
    public void setOpenAutoStart(boolean isOpenAutoStart) {
        pageInfo.isOpenAutoStart = isOpenAutoStart;
    }

    @Override
    public void setOptimizedBatteryEB(int optimizedBatterEB) {
        pageInfo.optimizedBatteryEB = optimizedBatterEB;
    }

    @Override
    public void setIsNeedOptimizeBattery(boolean isNeedOptimizeBattery) {
        pageInfo.needOptimizedBattery = isNeedOptimizeBattery;
    }

    @Override
    public void setIsBatteryRisk(boolean isRisk) {
        pageInfo.isBatteryRisk = isRisk;
    }

    @Override
    public float getBatteryED() {
        return pageInfo.batteryEB;
    }

}
