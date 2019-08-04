package com.tcl.manager.arithmetic.opetate;

import java.util.Collection;
import java.util.List;

import com.tcl.manager.arithmetic.base.IOperate;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;

/**
 * 补分算法：记录的最高分不到90分，加权到90，未记录的给予默认分
 * 
 * @author jiaquan.huang
 * 
 */
public class ChangeScoreArithmetic implements IOperate<Collection<AppScoreInfo>> {
    /** 最高记录不到的目标分 **/
    public final static int DEFAULT_SCORE = 90;
    /** 未记录电量默认分 **/
    public final static int NO_RECORED_BATTERY_EB = 42;
    /** 未记录流量默认分 **/
    public final static int NO_RECORED_MOBILE_ED = 42;

    @Override
    public void operate(Collection<AppScoreInfo> appScoreInfos) {
        operateTotalScore(appScoreInfos);
    }

    private void operateTotalScore(Collection<AppScoreInfo> appScoreInfos) {
        float maxScore = 0;
        boolean needOperate = false;
        for (AppScoreInfo appScoreInfo : appScoreInfos) {
            /** 未统计的记录 重新计算的得分 */
            if (appScoreInfo.isRecord == false) {
                appScoreInfo.batteryEB = NO_RECORED_BATTERY_EB;
                appScoreInfo.mobileDataED = NO_RECORED_MOBILE_ED;
                appScoreInfo.score = appScoreInfo.batteryEB //
                        + appScoreInfo.mobileDataED //
                        + appScoreInfo.apkSizeES;//
                continue;
            }
            needOperate = true;
            if (maxScore < appScoreInfo.score) {
                maxScore = appScoreInfo.score;
            }
        }
        /** 记录到的最高分都不到目标分，加权到目标分 **/
        if (maxScore > 0 && maxScore < DEFAULT_SCORE && needOperate) {
            float percentX = DEFAULT_SCORE / maxScore;
            for (AppScoreInfo appScoreInfo : appScoreInfos) {
                if (appScoreInfo.isRecord == false) {
                    continue;
                }
                appScoreInfo.score = appScoreInfo.score * percentX;
            }
        }
    }
}
