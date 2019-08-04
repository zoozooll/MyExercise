package com.tcl.manager.arithmetic.opetate;

import com.tcl.manager.arithmetic.entity.AppScoreInfo;

/**
 * 频率等级划分
 * 
 * @author jiaquan.huang
 * 
 */
public class FrequencyLevelProvider {

    static int SCORE_80 = 80;
    static int SCORE_40 = 40;

    /** 划定使用频率的等级 **/
    public static FrequencyLevel getUseLevel(float frequencyScore, boolean isRecord) {
        if (isRecord == false) {
            return FrequencyLevel.NO_RECORD;
        }
        if (frequencyScore >= SCORE_80) {
            return FrequencyLevel.OFTEN;
        } else if (SCORE_40 <= frequencyScore && frequencyScore < SCORE_80) {
            return FrequencyLevel.GENERAL;
        } else if (frequencyScore > 0 && frequencyScore < SCORE_40) {
            return FrequencyLevel.NOT_OFTEN;
        } else {
            return FrequencyLevel.NEVER_UESE;
        }
    }
}
