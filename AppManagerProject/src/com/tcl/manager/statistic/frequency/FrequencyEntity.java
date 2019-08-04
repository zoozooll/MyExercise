package com.tcl.manager.statistic.frequency;

import com.tcl.framework.db.annotation.Column;
import com.tcl.framework.db.annotation.GenerationType;
import com.tcl.framework.db.annotation.Id;
import com.tcl.manager.base.BaseInfo;

/**
 * 频率实体
 * 
 * @author jiaquan.huang
 */
public class FrequencyEntity extends BaseInfo {
    /** Id **/
    @Id(strategy = GenerationType.UUID)
    public String dbID;
    @Column
    /** 记录起始时间 **/
    public long fromTime;
    @Column
    /**记录结束时间 **/
    public long endTime;
    /** 使用时长 **/
    public long usedTime;
    /** 使用次数 **/
    public long usedCount;
}
