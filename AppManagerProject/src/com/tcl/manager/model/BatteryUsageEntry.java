/**
 * 
 */
package com.tcl.manager.model;

import com.tcl.framework.db.annotation.Column;
import com.tcl.framework.db.annotation.GenerationType;
import com.tcl.framework.db.annotation.Id;
import com.tcl.framework.db.annotation.Table;
/**
 * @author zuokang.li
 *
 */
@Table(version = 1, name = "battery_usage")
public class BatteryUsageEntry {

	/**
     * 用@Id表示主键
     */
    @Id(strategy = GenerationType.UUID)
    public String syncId;
    
    @Column
    public long savetime;
    
    @Column
    public String content;
    
    @Column
    public int state;
    
    @Column
    public long count;
}
