/**
 * 
 */
package com.tcl.manager.battery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.asksven.android.common.privateapiproxies.BatteryStatsProxy;
import com.asksven.android.common.privateapiproxies.BatteryStatsTypes;
import com.asksven.android.common.privateapiproxies.StatElement;
import com.asksven.android.common.utils.SysUtils;
import com.tcl.manager.model.BatteryDBManager;
import com.tcl.manager.model.BatteryHistoryDBManager;
import com.tcl.manager.model.BatteryUsageEntry;
import com.tcl.manager.util.PkgManagerTool;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
//import static com.tcl.manager.battery.BatteryUsageHelper.BatteryUsageContract.BatteryUsageEntry.*;
import android.os.BatteryManager;

/**
 * @author zuokang.li
 *
 */
public class BatteryUsageProvider {

	public List<AppInfoBatteryUsage> getAppBatteryUsage(Context context) {
		List<AppInfoBatteryUsage> result = new ArrayList<AppInfoBatteryUsage>();
		BatteryInfo currentBattery = jumpCurrentBatteryFromSys(context, false);
		if (currentBattery == null) {
			return null;
		}
		Map<String, Long> curMap = currentBattery.batteryUsageComputed;
		if (curMap == null) {
			return null;
		}
		for (Map.Entry<String, Long> entry : curMap.entrySet()) {   
			AppInfoBatteryUsage item = new AppInfoBatteryUsage();
			item.usage = entry.getValue();
			// Modify by zuokang.li. Dec 22,2014.
			// do not add the item which's usage is zero.
			if (item.usage > 0) {
				item.pkgName = entry.getKey();
				result.add(item);
			}
		} 
		return result;
	}
	
	public List<AppInfoBatteryUsage> getAppBatteryUsageHistory(Context context, Date date) {
		BatteryHistoryDBManager historyDBM = BatteryHistoryDBManager.getInstance(context);
		Map<String, Long> curMap =  historyDBM.getUsageofDate(date);
		
		return filterEmptyItems(curMap);
	}

	private List<AppInfoBatteryUsage> filterEmptyItems(Map<String, Long> curMap) {
		if (curMap == null) {
			return null;
		}
		List<AppInfoBatteryUsage> result = new ArrayList<AppInfoBatteryUsage>();
		for (Map.Entry<String, Long> entry : curMap.entrySet()) {   
			AppInfoBatteryUsage item = new AppInfoBatteryUsage();
			item.usage = entry.getValue();
			// Modify by zuokang.li. Dec 22,2014.
			// do not add the item which's usage is zero.
			if (item.usage > 0) {
				item.pkgName = entry.getKey();
				result.add(item);
			}
		} 
		return result;
	}
	
	public Map<String, List<AppInfoBatteryUsage>> getAppBatteryUsageHistory(Context context, Date begin, Date end) {
		BatteryHistoryDBManager historyDBM = BatteryHistoryDBManager.getInstance(context);
		Map<String, Map<String, Long>> curMap =  historyDBM.getUsageBetweenDate(begin, end);
		if (curMap == null) {
			return null;
		}
		Map<String, List<AppInfoBatteryUsage>> result = new HashMap<String, List<AppInfoBatteryUsage>>();
		for (Map.Entry<String, Map<String, Long>> entry : curMap.entrySet()) {
			result.put(entry.getKey(), filterEmptyItems(entry.getValue()));
		}
		return result;
	}
	
	private BatteryInfo getFirstBatteryInfos(Context context, long now) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(now);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long beginOfDay = c.getTimeInMillis();
		/*SQLiteDatabase db = BatteryUsageHelper.getInstance(context)
				.getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME, 
				new String[] {COLUMN_SAVETIME, COLUMN_CONTENT, COLUMN_CONTENT_COUPUTED, COLUMN_COUNT, COLUMN_STATE}
				, COLUMN_SAVETIME
				+ " >= ? and " + COLUMN_SAVETIME + " <= ?", new String[] {
				String.valueOf(beginOfDay), String.valueOf(now) }, null, null,
				COLUMN_SAVETIME, "0,1", null);
		List<BatteryInfo>  result = parseCursor(cursor);
		cursor.close();
		db.close();*/
		// use TCLDatabase and BatterDBManager to manager database model.
		BatteryDBManager db = BatteryDBManager.getInstance(context);
		List<BatteryInfo>  result = parseCursor(db.getFirstToday(beginOfDay, now));
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		} 
		return null;
	}
	
	private BatteryInfo getPreventBatteryInfos(Context context, long time) {
		
		/*SQLiteDatabase db = BatteryUsageHelper.getInstance(context)
				.getReadableDatabase();
		Cursor cursor = db.query(true, TABLE_NAME, 
				new String[] {COLUMN_SAVETIME, COLUMN_CONTENT, COLUMN_CONTENT_COUPUTED, COLUMN_COUNT, COLUMN_STATE}, 
				COLUMN_SAVETIME + " <= ?", 
				new String[] {String.valueOf(time) }, 
				null, null,
				COLUMN_SAVETIME + " desc",
				"0,1", null);
		List<BatteryInfo>  result = parseCursor(cursor);
		cursor.close();
		db.close();*/
		BatteryDBManager db = BatteryDBManager.getInstance(context);
		List<BatteryInfo>  result = parseCursor(db.getPrevInfoToday(time));
		if (result != null && !result.isEmpty()) {
			return result.get(0);
		} 
		return null;
	}
	
	private List<BatteryInfo> parseCursor(BatteryUsageEntry entry) {
		List<BatteryInfo> result = null;
		if(entry != null) {
			if (result == null) {
				result = new ArrayList<BatteryInfo>();
			}
			String content = entry.content;
			String[] strs = content.split(",");
			if (strs == null) {
				return result;
			}
			Map<String, Long> batteryUsageMap = new HashMap<String, Long>();
			Map<String, Long> batteryUsageComputed = new HashMap<String, Long>();
			for (String item : strs) {
				String[] arr = item.split(":");
				if (arr != null && arr.length == 3) {
					batteryUsageMap.put(arr[0], Long.valueOf(arr[1]));
					batteryUsageComputed.put(arr[0], Long.valueOf(arr[2]));
				}
			}
			
			BatteryInfo item = new BatteryInfo(entry.savetime, 
					batteryUsageMap, batteryUsageComputed,
					entry.count,
					entry.state > 0);
			result.add(item);
		}
		return result;
	}
	
/*	private static List<BatteryInfo> parseCursor(Cursor cursor) {
		List<BatteryInfo> result = null;
		for(cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
			if (result == null) {
				result = new ArrayList<BatteryInfo>();
			}
			String content = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT));
			String[] strs = content.split(",");
			Map<String, Long> batteryUsageMap = new HashMap<String, Long>();
			Map<String, Long> batteryUsageComputed = new HashMap<String, Long>();
			for (String item : strs) {
				String[] arr = item.split(":");
				batteryUsageMap.put(arr[0], Long.valueOf(arr[1]));
				batteryUsageComputed.put(arr[0], Long.valueOf(arr[2]));
			}
			
			BatteryInfo item = new BatteryInfo(cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_SAVETIME)), 
					batteryUsageMap, batteryUsageComputed,
					cursor.getLong(cursor.getColumnIndexOrThrow(COLUMN_COUNT)),
					cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_STATE)) == 1);
			result.add(item);
		}
		return result;
	}
	*/
	/**
	 * Jump the application's current battery usage data from system's record.
	 * 
	 * @param context
	 * @return the result is the same as system's record which is the ads of usage.
	 */
	private synchronized BatteryInfo jumpCurrentBatteryFromSys(Context context, boolean state) {
		BatteryInfo result = null;
		if (SysUtils.hasBatteryStatsPermission(context)) 
			BatteryStatsProxy.getInstance(context).invalidate();
		Collection<ApplicationInfo> apps = PkgManagerTool.getInstalledAppFilter(context);
		//print the data from system.
		/*for (ApplicationInfo app : apps){
			NLog.d("aaron", "system getApps uid:    "+app.uid);
		}*/
		//
		long  now = System.currentTimeMillis();
		BatteryStatsProxy mStats = BatteryStatsProxy.getInstance(context);
		
		Map<String, Long> batteryUsageMap = new HashMap<String, Long>();
		ArrayList<StatElement> allProcesses = null;
		try {
			allProcesses = mStats.getProcessStats(context,
					BatteryStatsTypes.STATS_SINCE_UNPLUGGED);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		if (allProcesses == null) {
			return null;
		}
		//print the data from system.
		/*for (StatElement stat : allProcesses){
			NLog.d("aaron", "system getProcessStats:    "+stat.getuid()+"  "+stat);
		}*/
		//
		long count = 0;
		for (ApplicationInfo appInfo : apps) {
			Long l = batteryUsageMap.get(appInfo.packageName);
			if (l == null) {
				batteryUsageMap.put(appInfo.packageName, 0L);
				l = Long.valueOf(0L);
			}
			for (StatElement stat : allProcesses){
				if (stat.getuid() == appInfo.uid) {
					// Modify by zuokang.li . Dec 22, 2014
					// Can not use get Total to get all cpu times. Need to add process.systemTime and process.userTime
					//l += stat.getTotal();
					com.asksven.android.common.privateapiproxies.Process process = (com.asksven.android.common.privateapiproxies.Process) stat;
					l += process.getSystemTime() + process.getUserTime() ;
				}
			}
			batteryUsageMap.put(appInfo.packageName, l);
			count += l;
		}
		// It is not completed,Need to compute the current battery usage
		result = new BatteryInfo(now, batteryUsageMap, state);
		result.count = count;
		computeCurrentBattery(result, context);
		
		return result;
	}
	
	/**
	 * 
	 * @param currentInfo It is not completed info, but it will be completed after this method run.
	 * @param firstInfo
	 * @param prevInfo
	 */
	private void computeCurrentBattery(BatteryInfo currentInfo, Context context) {
		Map<String, Long> batteryUsageMap = new HashMap<String, Long>();
		// if it is charging, the current data from system is zero
		if (!currentInfo.state && isCharging(context)) {
//			NLog.d("aaron", "computeCurrentBattery 0");
			for (String key : currentInfo.batteryUsageMap.keySet()) {
				currentInfo.batteryUsageMap.put(key, 0L); 
	        }
		}
		if (isFirstBatteryRecord(context, currentInfo.saveTime)) {
			// If this state is STATE_FIRST_OF_DAY, the compute usage are all zero.
			for (String key : currentInfo.batteryUsageMap.keySet()) {
//				NLog.d("aaron", "computeCurrentBattery 1 %s : %s", key, currentInfo.batteryUsageMap.get(key));
				batteryUsageMap.put(key, 0L); 
	        }
			currentInfo.total = 0;
			
			BatteryInfo prevInfo = getPreventBatteryInfos(context, currentInfo.saveTime);
			//if (prevInfo != null) {
				saveHistory(context, currentInfo);
			//}
		}  else	{
			getBatteryUsageCommon(currentInfo, context, batteryUsageMap);
		}
		currentInfo.batteryUsageComputed = batteryUsageMap;
	}
	
	private void saveHistory(Context context, BatteryInfo currentInfo) {
		// If it is first data of today, it need to save the last day's usage data to databases.
		Map<String, Long> lastdayBatteryUsageMap = new HashMap<String, Long>();
		getBatteryUsageCommon(currentInfo, context, lastdayBatteryUsageMap);
		BatteryHistoryDBManager historyDB = BatteryHistoryDBManager.getInstance(context);
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(currentInfo.saveTime);
		c.add(Calendar.DAY_OF_YEAR, -1);
		Date date = new Date(c.getTimeInMillis());
		historyDB.deleteUnusefulData(date);
		historyDB.addUsageOfDate(date, lastdayBatteryUsageMap);
//					lastdayBatteryUsageMap.clear();
	}

	private void getBatteryUsageCommon(BatteryInfo currentInfo,
			Context context, Map<String, Long> batteryUsageMap) {
		BatteryInfo prevInfo = getPreventBatteryInfos(context, currentInfo.saveTime);
		if (prevInfo == null) {
			// If the previous info is empty, so the data is zero.
			for (String pkgName : currentInfo.batteryUsageMap.keySet()) {
				batteryUsageMap.put(pkgName, 0L);
			}
			return;
		}
		if (prevInfo.state) {
			// If the prevent info 's state is SHUI_DOWN_SAVED, the current compute usage is this function:
			// 计算电量 = 上一次计算电量 + 当前绝对值
			for (Map.Entry<String, Long> entry : currentInfo.batteryUsageMap.entrySet()) {
//				NLog.d("aaron", "computeCurrentBattery 3 %s : %s", entry.getKey(), entry.getValue());
				String pkgName = entry.getKey();
				Map<String, Long> prevMap = prevInfo.batteryUsageComputed;
				long value = entry.getValue();
				if (prevMap != null) {
					Long l = prevMap.get(pkgName);
					if (l != null) value += l;
				}
				batteryUsageMap.put(pkgName, value);
				currentInfo.total += value;
			} 
		} else if (prevInfo.count > currentInfo.count) {
			// If the prevent info's count is more than current's, the application must be shutdown with any save.
			// 上一次计算电量 + 当前绝对值
			for (Map.Entry<String, Long> entry : currentInfo.batteryUsageMap.entrySet()) {
//				NLog.d("aaron", "computeCurrentBattery 4 %s : %s", entry.getKey(), entry.getValue());
				String pkgName = entry.getKey();
				Map<String, Long> prevMap = prevInfo.batteryUsageComputed;
				long value = entry.getValue();
				if (prevMap != null) {
					Long l = prevMap.get(pkgName);
					if (l != null) value += l;
				}
				batteryUsageMap.put(pkgName, value);
				currentInfo.total += value;
			} 
		} else {
			// 当前绝对值-上次绝对值 +上一次电量
			for (Map.Entry<String, Long> entry : currentInfo.batteryUsageMap
					.entrySet()) {
//				NLog.d("aaron", "computeCurrentBattery 5 %s : %s", entry.getKey(), entry.getValue());
				String pkgName = entry.getKey();
				long value = entry.getValue();
				Map<String, Long> prevMap ;
				if ((prevMap= prevInfo.batteryUsageMap) != null) {
					Long l = prevMap.get(pkgName);
					if (l != null) value -= l;
				}
				if ((prevMap= prevInfo.batteryUsageComputed) != null) {
					Long l = prevMap.get(pkgName);
					if (l != null) value += l;
				} 
				batteryUsageMap.put(pkgName, value);
				currentInfo.total += value;
			}
		}
	}
	
	private boolean isFirstBatteryRecord(Context context, long millis) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(millis);
		c.set(Calendar.HOUR_OF_DAY, 0);
		c.set(Calendar.MINUTE, 0);
		c.set(Calendar.SECOND, 0);
		c.set(Calendar.MILLISECOND, 0);
		long beginOfDay = c.getTimeInMillis();
		BatteryDBManager db = BatteryDBManager.getInstance(context);
		BatteryUsageEntry rs = db.getFirstToday(beginOfDay, millis);
		return rs == null;
	}
	
	public void saveBatteryUsage(Context context, boolean state)  throws IOException {
		BatteryInfo result = jumpCurrentBatteryFromSys(context, state);
		if (result == null) {
			return;
		}
		StringBuilder builder = new StringBuilder();
		for (Map.Entry<String, Long> entry : result.batteryUsageMap.entrySet()) {   
			builder.append(entry.getKey());
			builder.append(':');
			builder.append(entry.getValue());
			builder.append(':');
			Long computed = result.batteryUsageComputed.get(entry.getKey());
			builder.append(computed != null ? computed :0);
			builder.append(',');
        }
		/*OutputStream os = getBatteryUsageSaferStream(context, now);
		os.write(builder.toString().getBytes());
		os.flush();
		os.close();*/
		// save to database. It is parttime.
		/*BatteryUsageHelper helper = BatteryUsageHelper.getInstance(context);
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(COLUMN_SAVETIME, result.saveTime);
		cv.put(COLUMN_CONTENT, builder.toString());
		cv.put(COLUMN_COUNT, result.count);
		db.insertOrThrow(TABLE_NAME, null, cv);
		db.close();*/
		BatteryDBManager db = BatteryDBManager.getInstance(context);
		BatteryUsageEntry values = new BatteryUsageEntry();
		values.savetime = result.saveTime;
		values.content = builder.toString();
		values.state = result.state ? 1 : 0;
		values.count = result.total;
		db.saveItem(values);
	}
	
	private boolean isCharging(Context context){
		IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
		Intent batteryStatus = context.getApplicationContext().registerReceiver(null, ifilter);
		//你可以读到充电状态,如果在充电，可以读到是usb还是交流电
		 
		// 是否在充电
		int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
		return  status == BatteryManager.BATTERY_STATUS_CHARGING ||
		                     status == BatteryManager.BATTERY_STATUS_FULL;
	}
}
