package com.tcl.manager.arithmetic.opetate;

/**
 * 算法系数提供者
 * 
 * @author jiaquan.huang
 * 
 */
public class FactorProvider {
    /** 时长满分 **/
    public final static int MAX_USEDTIME_SCORE = 100;
    /** 电量满分 **/
    public final static int MAX_BATTERY_SCORE = 45;
    /** 流量满分 **/
    public final static int MAX_MOBILE_SCORE = 45;
    /** 应用大小满分 **/
    public final static int MAX_APKSIZE_SCORE = 10;

    /** 获取最大百分比对应最大值的对应系数 **/
    public static float getFactor(float percent, int maxTarget) {
        if (percent == 0 || maxTarget == 0) {
            return 0;
        }
        return maxTarget / percent;
    }

}
