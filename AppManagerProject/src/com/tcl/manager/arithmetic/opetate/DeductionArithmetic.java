package com.tcl.manager.arithmetic.opetate;

import java.util.Collection;

import com.tcl.manager.arithmetic.base.IDeductionArithmetic;
import com.tcl.manager.arithmetic.base.IOperate;

/**
 * 优化扣分算法规则
 * 
 * @author jiaquan.huang
 * 
 */
public class DeductionArithmetic implements IOperate<Collection<IDeductionArithmetic>> {
    public static final int MAX_APP_SOCRE = 80;// 总分
    public static final int MAX_APP_DEC_SOCRE = 65;// 扣分最大分
    public static final int LIMIT = 10;// 减分标准
    private int totalDeductionScore = 0;// 减分数总数
    public int singleDeductionScore = 0;// 单个减分数
    private int deductionCount = 0;// 减分个数
    private int netScore = 10;// 流量扣分数
    private int batteryScore = 10;// 电量扣分数
    private int score = 0;// 总分

    public void operate(Collection<IDeductionArithmetic> apps) {
        for (IDeductionArithmetic app : apps) {
            if (app.getDataED() < LIMIT && app.isNeedOpreateNet()) {
                totalDeductionScore += netScore;
                deductionCount++;
                app.setNetRisk(true);
            }
            if (app.getBatteryEB() < LIMIT && app.isNeedOpreateBattery()) {
                totalDeductionScore += batteryScore;
                deductionCount++;
                app.setBatteryRisk(true);
            }
        }
        operateScore();
        for (IDeductionArithmetic app : apps) {
            if (app.isNetRisk() && app.isNeedOpreateNet()) {
                app.setDataReducesScore(singleDeductionScore);
                app.setNeedOptimzeNetState(true);
            } else {
                app.setDataReducesScore(0);
            }
            if (app.isBatteryRisk() && app.isNeedOpreateBattery()) {
                app.setNeedOptimizeBatteryState(true);
                app.setBatteryReducesScore(singleDeductionScore);
            } else {
                app.setBatteryReducesScore(0);
            }
        }
    }

    /** 计算每一项的扣分数 **/
    private void operateScore() {
        if (totalDeductionScore > MAX_APP_DEC_SOCRE) {
            totalDeductionScore = MAX_APP_DEC_SOCRE;
        }
        if (deductionCount != 0) {
            if (deductionCount >= 65) {
                /** 超过65个危险项，每个扣一分 **/
                singleDeductionScore = 1;
            } else if (deductionCount > 0) {
                /** 单个危险项扣分取整 **/
                singleDeductionScore = totalDeductionScore / deductionCount;
            }
        }
        if (deductionCount > 5) {
            score = MAX_APP_SOCRE - MAX_APP_DEC_SOCRE;// 大于5个全扣
        } else {
            score = MAX_APP_SOCRE - singleDeductionScore * deductionCount;
        }
    }

    public int getScore() {
        return score;
    }
}
