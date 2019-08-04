/**
 * 
 */
package com.tcl.manager.battery;

import java.util.Map;

/**
 * @author zuokang.li
 *
 */
class BatteryInfo {

	/** The saved time. */
	long saveTime;
	
	/** The data is from system */
	Map<String, Long> batteryUsageMap;
	
	/**The result data was computed by us*/
	Map<String, Long> batteryUsageComputed;
	
	/**The data count with system' data*/
	long count;
	
	/**If the data counting is cut down like reboot or plug in, it is true */
	boolean state;
	
	/**The data count with system' data*/
	long total;
	
	
	public BatteryInfo() {
		super();
	}
	
	public BatteryInfo(long saveTime, Map<String, Long> batteryUsageMap,
			boolean state) {
		super();
		this.saveTime = saveTime;
		this.batteryUsageMap = batteryUsageMap;
		this.state = state;
	}

	public BatteryInfo(long saveTime, Map<String, Long> batteryUsageMap,
			Map<String, Long> batteryUsageComputed, long count, boolean state) {
		super();
		this.saveTime = saveTime;
		this.batteryUsageMap = batteryUsageMap;
		this.batteryUsageComputed = batteryUsageComputed;
		this.count = count;
		this.state = state;
	}
	
	
}
