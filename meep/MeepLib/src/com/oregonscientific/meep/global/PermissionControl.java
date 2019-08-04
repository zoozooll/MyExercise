package com.oregonscientific.meep.global;

import java.util.Calendar;
import java.util.Locale;

import android.app.Service;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.oregonscientific.meep.database.table.TablePermission;

public class PermissionControl {
	private Locale mLocale = null;
	private String mAppName = null;
	
	public PermissionControl(String appName, Locale locale) {
		mAppName = appName;
		mLocale = locale;
	}
	
	public void setAppName(String appName) {
		mAppName = appName;
	}
	
	public String getPermissionSql() {
		if (mAppName != null) {
			return "SELECT * FROM " + TablePermission.S_TABLE_NAME + " WHERE " + TablePermission.S_APP_NAME + "=" + "'" + mAppName + "'";
		}
		
		return null;
	}
	
	public String getUpdateUsedTimeSql(int id, int canAccess, int timeLimit, int timeUsed, long lastOpen) {
		String sql = null;
		
		boolean allowOpen = checkPermission(canAccess, timeLimit, timeUsed, lastOpen);
		
		if (allowOpen) {
			Calendar currentCalendar = Calendar.getInstance(mLocale);
			Calendar lastCalendar = Calendar.getInstance(mLocale);
			
			lastCalendar.setTimeInMillis(lastOpen);
			long currentTime = currentCalendar.getTimeInMillis();
			boolean isSameDay = compareDate(currentCalendar, lastCalendar);
			
			// Update record
			sql = "UPDATE " + TablePermission.S_TABLE_NAME + " WHERE " + TablePermission.S_APP_NAME + "=" + "'" + mAppName + "'";
			
			TablePermission tablePermission = new TablePermission();
			tablePermission.setId(id);
			tablePermission.setLastOpen(currentTime);
			
			if (isSameDay) {
				tablePermission.setTimeUsed(timeUsed+1);
			} else {
				tablePermission.setTimeUsed(0);
			}
			
			sql = tablePermission.getUpdateTimeUsedSql();
		}
		
		return sql;
	}
	
	public boolean checkPermission(int canAccess, int timeLimit, int timeUsed, long lastOpen) {
		boolean allowOpen = false;
		
		if (mAppName != null) {
			if (canAccess == 1) {
				Calendar currentCalendar = Calendar.getInstance(mLocale);
				Calendar lastCalendar = Calendar.getInstance(mLocale);
				
				lastCalendar.setTimeInMillis(lastOpen);
				long currentTime = currentCalendar.getTimeInMillis();
				
				if (lastOpen == 0) {
					allowOpen = true;
				}else if (timeLimit>=1440){
					allowOpen = true;
				}
				else if (currentTime >= lastOpen) {
					// Normal case, use didn't set a future time
					// Get date
					boolean isSameDay = compareDate(currentCalendar, lastCalendar);
					
					if (isSameDay) {
						// Same day, check the time limit
						if (timeUsed < timeLimit) {
							allowOpen = true;
						}
					} else {
						// Different day
						Log.e("Parental Control", "Diff Day");
						
						if (timeLimit > 0) {
							allowOpen = true;
						} else {
							Log.e("Parental Control", "Reason:: Diff Day && timeLimit = 0.");
						}
					}
				} else {
					Log.e("Parental Control", "Reason:: future time.");
				}
				
				if (!allowOpen) {
					Log.e("Parental Control", "Reason:: Time is up.");
				}
			} else {
				Log.e("Parental Control", "Reason:: Deny");
			}
		} else {
			Log.e("Parental Control", "Reason:: App name is Null");
		}
		
		return allowOpen;
	}
	
	public boolean compareDate(Calendar currentTime, Calendar lastTime) {
		int currentDay = currentTime.get(Calendar.DATE);
		int currentMonth = currentTime.get(Calendar.MONTH);
		int currentYear = currentTime.get(Calendar.YEAR);
		
		int lastDay = lastTime.get(Calendar.DATE);
		int lastMonth = lastTime.get(Calendar.MONTH);
		int lastYear = lastTime.get(Calendar.YEAR);
		
		if (currentDay == lastDay && currentMonth == lastMonth && currentYear == lastYear) {
			return true;
		}
		
		return false;
	}
}

