package com.tcl.manager.analyst.entity;

import java.util.List;

import com.tcl.framework.db.annotation.Column;
import com.tcl.framework.db.annotation.GenerationType;
import com.tcl.framework.db.annotation.Id;
import com.tcl.framework.db.annotation.Table;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;

/** 得分 **/
@Table(version = 1, name = "cache")
public class AppInfoBuffer {
    @Id(strategy = GenerationType.UUID)
    public String dbID;
    /*** 总分 ***/
    @Column
    public int total;
    /** app得分 **/
    @Column
    public int appScore;
    /** 内存得分 **/
    @Column
    public int memoryScore;
    /** 记录时间 年月日 **/
    @Column
    public String time = "";
    /** app */
    @Column
    public List<AppScoreInfo> app;
}
