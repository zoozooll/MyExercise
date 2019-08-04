package com.tcl.manager.analyst;

import java.util.Iterator;
import java.util.List;

import com.tcl.manager.analyst.entity.AppInfoBuffer;
import com.tcl.manager.arithmetic.MemoryScoreTool;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.arithmetic.opetate.DeductionArithmetic;
import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.util.TimeUtil;
import android.content.Context;

/**
 * 数据分析
 * 
 * @author jiaquan.huang
 **/
public class Analyst {
    Context context;
    IAnalystListener listener;
    AnalystType type = null;// 分析的方式

    public Analyst(Context context, IAnalystListener listener) {
        this.context = context.getApplicationContext();
        this.listener = listener;
    }

    /** 彻底的分析日志数据 **/
    public void startAnalysisComplete() {
        type = AnalystType.APPSCORE_MEMORY;
        Thread analysis = new Thread(analysisThread);
        analysis.start();
    }

    /** 取存储的旧AppScore **/
    public void startAnalysisPart() {
        type = AnalystType.MEMORY;
        Thread analysis = new Thread(analysisThread);
        analysis.start();
    }

    /** 同步获取数据 **/
    public void syncAnalysis() {
        type = AnalystType.MEMORY;
        Thread analysis = new Thread(analysisThread);
        analysis.start();
        try {
            analysis.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /** 保存计算的分数 **/
    public void setSaveScore(AppInfoBuffer info) {
        ScoreSaver.getInstance().setSaveScore(info);
    }

    Runnable analysisThread = new Runnable() {

        @Override
        public void run() {
            if (type == AnalystType.APPSCORE_MEMORY) {
                /** 彻底重新计算 **/
                AppsAnalyst analyst = new AppsAnalyst(context, new IAnalystListener() {

                    @Override
                    public void totalScoreBack(List<AppScoreInfo> apps, int totalScore, int appsScore, int memoryScore) {
                        listener.totalScoreBack(apps, totalScore, appsScore, memoryScore);
                        /** 保存 **/
                        AppInfoBuffer scoreInfo = new AppInfoBuffer();
                        scoreInfo.total = totalScore;
                        scoreInfo.appScore = appsScore;
                        scoreInfo.memoryScore = memoryScore;
                        scoreInfo.time = TimeUtil.getTodayCalendar();
                        scoreInfo.app = apps;
                        setSaveScore(scoreInfo);
                    }

                    @Override
                    public void memoryScoreBack(int memoryScore) {
                        listener.memoryScoreBack(memoryScore);
                    }
                });
                analyst.startAnalysis();
            } else {
                AppInfoBuffer scoreInfo = ScoreSaver.getInstance().getAppInfoBuffer();
                /** 1:查看缓存，有直接返回，无则重新计算 **/
                if (scoreInfo == null || scoreInfo.app == null) {
                    /** 彻底重新计算 **/
                    AppsAnalyst analyst = new AppsAnalyst(context, new IAnalystListener() {

                        @Override
                        public void totalScoreBack(List<AppScoreInfo> apps, int totalScore, int appsScore, int memoryScore) {
                            listener.totalScoreBack(apps, totalScore, appsScore, memoryScore);
                            /** 保存 **/
                            AppInfoBuffer scoreInfo = new AppInfoBuffer();
                            scoreInfo.total = totalScore;
                            scoreInfo.appScore = appsScore;
                            scoreInfo.memoryScore = memoryScore;
                            scoreInfo.time = TimeUtil.getTodayCalendar();
                            scoreInfo.app = apps;
                            setSaveScore(scoreInfo);
                        }

                        @Override
                        public void memoryScoreBack(int memoryScore) {
                            listener.memoryScoreBack(memoryScore);
                        }
                    });
                    analyst.startAnalysis();
                } else {
                    /** 2:有缓存 **/
                    MemoryInfoProvider memoryProvider = MemoryInfoProvider.getInstance(context);
                    MemoryScoreTool memoryScoreTool = new MemoryScoreTool(context, memoryProvider.availMem, memoryProvider.totalMem);
                    int memoryScore = (int) memoryScoreTool.getScore();
                    if (scoreInfo.app != null) {
                        Iterator<AppScoreInfo> iterator = scoreInfo.app.iterator();
                        while (iterator.hasNext()) {
                            AppScoreInfo appScoreInfo = iterator.next();
                            if (!InstalledAppProvider.getInstance().isInstalled(appScoreInfo.pkgName)) {
                                iterator.remove();
                            }
                        }
                        listener.memoryScoreBack(memoryScore + DeductionArithmetic.MAX_APP_SOCRE);
                        listener.totalScoreBack(scoreInfo.app, scoreInfo.total, scoreInfo.appScore, memoryScore);
                    }
                }
            }
        }
    };

}
