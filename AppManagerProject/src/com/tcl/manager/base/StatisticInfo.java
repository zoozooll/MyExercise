/**
 * 
 */
package com.tcl.manager.base;

import java.text.Collator;
import java.util.Locale;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.opetate.FrequencyLevel;

import android.graphics.drawable.Drawable;

/**
 * @author zuokang.li
 *
 */
public class StatisticInfo extends BaseInfo implements Comparable<StatisticInfo> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2012998198500403959L;

	public Drawable icon;
	
	public String appName;
	
	public FrequencyLevel level;
	
	public int frequencyTimes;
	
	public long frequencySeconds;
	
	public float score;
	
	public boolean isRunning;
	
	public float memory;
	
	public float batteryPercent;
	
	public long dataToday;
	
	public long dataPerDay;
	
	public boolean showBottom;


	@Override
	public int compareTo(StatisticInfo another) {
		int flag = (int) (another.score - this.score); 
		if (flag == 0 && appName != null && another.appName != null) {
			Collator c = Collator.getInstance(ManagerApplication.sApplication. getResources().getConfiguration().locale);
			return c.compare(this.appName, another.appName);
		}
		return flag;
	}
}
