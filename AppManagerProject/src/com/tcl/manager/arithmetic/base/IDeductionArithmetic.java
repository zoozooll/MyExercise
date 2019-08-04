package com.tcl.manager.arithmetic.base;

/** 扣分算法，对象转换器 **/
public interface IDeductionArithmetic {
    public float getDataED();

    public float getBatteryEB();

    public boolean isBatteryRisk();

    public boolean isNetRisk();

    public void setNeedOptimizeBatteryState(boolean state);

    public void setNeedOptimzeNetState(boolean state);

    public void setDataReducesScore(int reduceScore);

    public void setBatteryReducesScore(int reduceScore);

    public boolean isNeedOpreateBattery();

    public boolean isNeedOpreateNet();

    public void setNeedOpreateBattery(boolean state);

    public void setNeedOpreateNet(boolean state);

    public void setBatteryRisk(boolean isRisk);

    public void setNetRisk(boolean isRisk);
}
