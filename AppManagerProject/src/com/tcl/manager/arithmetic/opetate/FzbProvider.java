package com.tcl.manager.arithmetic.opetate;

/**
 * f占比提供者
 * 
 * @author jiaquan.huang
 * 
 */
public class FzbProvider {
    /** 百分比 **/
    private static final float PERCENT0 = 0.0f;
    private static final float PERCENT1 = 0.01f;
    private static final float PERCENT10 = 0.1f;
    private static final float PERCENT20 = 0.2f;
    private static final float PERCENT30 = 0.3f;
    private static final float PERCENT40 = 0.4f;
    private static final float PERCENT50 = 0.5f;
    private static final float PERCENT60 = 0.6f;
    private static final float PERCENT70 = 0.7f;
    private static final float PERCENT80 = 0.8f;
    private static final float PERCENT90 = 0.9f;
    private static final float PERCENT95 = 0.95f;
    private static final float PERCENT100 = 1f;
    /** 百分比 对应的值 **/
    private static final float VALUE0_0 = 1;
    private static final float VALUE1_10 = 1;
    private static final float VALUE10_20 = 0.9f;
    private static final float VALUE20_30 = 0.8f;
    private static final float VALUE30_40 = 0.7f;
    private static final float VALUE40_50 = 0.6f;
    private static final float VALUE50_60 = 0.5f;
    private static final float VALUE60_70 = 0.4f;
    private static final float VALUE70_80 = 0.3f;
    private static final float VALUE80_90 = 0.2f;
    private static final float VALUE90_95 = 0.1f;
    private static final float VALUE95_100 = 0.01f;

    public static float getFZBValue(float percent) {
        if (PERCENT0 <= percent && percent < PERCENT1) {
            return VALUE0_0;
        } else if (PERCENT1 < percent && percent < PERCENT10) {
            return VALUE1_10;
        } else if (PERCENT10 <= percent && percent < PERCENT20) {
            return VALUE10_20;
        } else if (PERCENT20 <= percent && percent < PERCENT30) {
            return VALUE20_30;
        } else if (PERCENT30 <= percent && percent < PERCENT40) {
            return VALUE30_40;
        } else if (PERCENT40 <= percent && percent < PERCENT50) {
            return VALUE40_50;
        } else if (PERCENT50 <= percent && percent < PERCENT60) {
            return VALUE50_60;
        } else if (PERCENT60 <= percent && percent < PERCENT70) {
            return VALUE60_70;
        } else if (PERCENT70 <= percent && percent < PERCENT80) {
            return VALUE70_80;
        } else if (PERCENT80 <= percent && percent < PERCENT90) {
            return VALUE80_90;
        } else if (PERCENT90 <= percent && percent < PERCENT95) {
            return VALUE90_95;
        } else if (PERCENT95 <= percent && percent <= PERCENT100) {
            return VALUE95_100;
        }
        return 0;
    }

}
