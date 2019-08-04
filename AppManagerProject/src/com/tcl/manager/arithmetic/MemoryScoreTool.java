package com.tcl.manager.arithmetic;

import java.util.Map;
import android.content.Context;

import com.tcl.manager.analyst.AppFilter;
import com.tcl.manager.score.RunningAppInfo;
import com.tcl.manager.util.PkgManagerTool;

/**
 * @Description: 内存得分，MP内存规则
 * @author jiaquan.huang
 * @date 2014-12-19 上午11:16:20
 * @copyright TCL-MIE
 * 
 *             手机剩余内存≥50% —— 15分  50%＞手机剩余内存≥40% —— 11分  40%＞手机剩余内存≥30% —— 8分
 *             30%＞手机剩余内存≥20% —— 5分  20%＞手机剩余内存≥10% —— 1分  手机剩余内存＜10% —— 0 分
 */

public class MemoryScoreTool {

    private final static float PERCENT50 = 0.5f;
    private final static float PERCENT40 = 0.4f;
    private final static float PERCENT30 = 0.3f;
    private final static float PERCENT20 = 0.2f;
    private final static float PERCENT10 = 0.1f;

    private final static int SCORE15 = 15;
    private final static int SCORE11 = 11;
    private final static int SCORE8 = 8;
    private final static int SCORE5 = 5;
    private final static int SCORE4 = 4;
    private final static int SCORE1 = 1;
    private final static int SCORE0 = 0;

    Context context;
    private long availMen = 0;
    private long totalMen = 0;

    public MemoryScoreTool(Context context, long avilMen, long totalMen) {
        this.context = context;
        this.availMen = avilMen;
        this.totalMen = totalMen;
    }

    /** 运行有APP扣4分 **/
    public float getScore() {
        Map<String, RunningAppInfo> running = PkgManagerTool.getRunningAppInfos(context);
        for (RunningAppInfo info : running.values()) {
            if (AppFilter.getInstance().isNeedShow(context, info.pkgName) == false) {
                running.remove(info.pkgName);
            }
        }
        if (running.keySet().size() > 0) {
            return getDefautScore(true);
        } else {
            return getDefautScore(false);
        }
    }

    /** 默认没有运行的进程,不扣分 **/
    public float getDefautScore(boolean hasRunningApp) {
        int score = mpRule();
        if (hasRunningApp) {
            score += SCORE0;
        } else {
            score += SCORE4;
        }
        return score;
    }

    /** MP内存规则 **/
    private int mpRule() {
        if (totalMen == 0) {
            throw new IllegalArgumentException("MemoryScore.totalMen is 0");
        }
        float percent = availMen * 1f / totalMen;
        return getScoreByPercent(percent);
    }

    private int getScoreByPercent(float percent) {
        if (PERCENT50 <= percent) {
            return SCORE15;
        } else if (PERCENT40 <= percent && PERCENT50 > percent) {
            return SCORE11;
        } else if (PERCENT30 <= percent && PERCENT40 > percent) {
            return SCORE8;
        } else if (PERCENT20 <= percent && PERCENT30 > percent) {
            return SCORE5;
        } else if (PERCENT10 <= percent && PERCENT20 > percent) {
            return SCORE1;
        } else if (PERCENT10 > percent) {
            return SCORE0;
        }
        return SCORE15;
    }
}
