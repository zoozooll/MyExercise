package com.tcl.manager.analyst;


import java.util.List;

import com.tcl.manager.arithmetic.entity.AppScoreInfo;

/**
 * 运算回调
 * 
 * @author jiaquan.huang
 **/
public interface IAnalystListener {
    /** 返回扣除了内存分的总分 **/
    public void memoryScoreBack(int memoryScore);

    /** 返回所有信息 **/
    public void totalScoreBack(List<AppScoreInfo> apps, int totalScore, int appsScore, int memoryScore);

}
