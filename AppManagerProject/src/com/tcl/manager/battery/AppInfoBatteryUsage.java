/**
 * 
 */
package com.tcl.manager.battery;

import com.tcl.manager.base.BaseInfo;

/**
 * @author zuokang.li
 * 
 */
public class AppInfoBatteryUsage extends BaseInfo {
    public long usage;

	@Override
	public String toString() {
		return "AppInfoBatteryUsage [usage=" + usage + ", pkgName=" + pkgName
				+ "]";
	}
}
