package com.tcl.manager.analyst;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.tcl.manager.analyst.entity.AppInfoBuffer;
import com.tcl.manager.util.SharedStorekeeper;
import com.tcl.manager.util.TimeUtil;

/**
 * 分数保存
 * 
 * @author jiaquan.huang
 * 
 */
public class ScoreSaver {
    public final static String SCORE_KEY = "APP_SCORE_KEY";// 记录上次计算的分数
    volatile static ScoreSaver scoreSaver;

    public static ScoreSaver getInstance() {
        synchronized (ScoreSaver.class) {
            if (scoreSaver == null) {
                scoreSaver = new ScoreSaver();
            }
            return scoreSaver;
        }
    }

    /** 保存计算的分数 **/
    public void setSaveScore(AppInfoBuffer info) {
        synchronized (this) {
            if (info != null) {
                CacheDBHelper.getInstance().saveOrUpdate(info);
            }
        }
    }

    /** 获取存储的分数 **/
    public AppInfoBuffer getAppInfoBuffer() {
        synchronized (this) {
            return CacheDBHelper.getInstance().getCache();
        }
    }
}
