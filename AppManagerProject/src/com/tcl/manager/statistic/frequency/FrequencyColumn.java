package com.tcl.manager.statistic.frequency;

import java.util.List;
import com.tcl.framework.db.annotation.Column;
import com.tcl.framework.db.annotation.GenerationType;
import com.tcl.framework.db.annotation.Id;
import com.tcl.framework.db.annotation.Table;

@Table(version = 1, name = "frequncy")
public class FrequencyColumn {

    /** Id **/
    @Id(strategy = GenerationType.UUID)
    public String dbID;
    @Column
    /** 记录起始时间 **/
    public long fromTime;
    @Column
    /**记录结束时间 **/
    public long endTime;
    @Column
    /**APP频率集合内容**/
    public List<FrequencyEntity> frequencyEntity;
}
