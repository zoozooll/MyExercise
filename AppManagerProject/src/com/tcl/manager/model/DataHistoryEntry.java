package com.tcl.manager.model;

import java.util.Date;

import com.tcl.framework.db.annotation.Column;
import com.tcl.framework.db.annotation.GenerationType;
import com.tcl.framework.db.annotation.Id;
import com.tcl.framework.db.annotation.Table;

@Table(version = 1, name = "battery_history")
public class DataHistoryEntry {

	/**
     * 用@Id表示主键
     */
    @Id(strategy = GenerationType.UUID)
    public String syncId;

    @Column(unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	public Date saveTime;
	
    @Column
	public String mobileData;
    
    @Column
   	public String wifiData;
    
    @Column
    public long totalWifi;
    
    @Column
    public long totalMobileData;
	
}
