/**
 * 
 */
package com.tcl.manager.datausage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.pm.ApplicationInfo;

import com.tcl.manager.model.DataHistoryDBManager;
import com.tcl.manager.model.DataHistoryEntry;
import com.tcl.manager.util.PkgManagerTool;

/**
 * @author zuokang.li
 *
 */
public class DatausageHistoryProvider {

	public List<AppNetstatInfo> getUsageApps(Context context, Date d) {
		Collection<ApplicationInfo> apps = PkgManagerTool.getInstalledAppFilter(context);
		//DatausageProvider provider = new DatausageProvider(startMillis, endMillis);
		DataHistoryDBManager db = DataHistoryDBManager.getInstance(context);
		DataHistoryEntry entry = db.getUsageofDate(d);
		if (entry == null)  return null;
		Map<Integer,Long> statsCellular = DataHistoryDBManager.stringToMap(entry.mobileData) ;
		Map<Integer,Long> statsWifi = DataHistoryDBManager.stringToMap(entry.wifiData) ;
		return parseUsageMap(apps, statsCellular, statsWifi);
	}
	
	public Map<String, List<AppNetstatInfo>> getUsageApps(Context context, Date begin, Date end) {
		Collection<ApplicationInfo> apps = PkgManagerTool.getInstalledAppFilter(context);
		//DatausageProvider provider = new DatausageProvider(startMillis, endMillis);
		DataHistoryDBManager db = DataHistoryDBManager.getInstance(context);
		List<DataHistoryEntry> list = db.getUsageBetweenDate(begin, end);
		if (list == null) return null;
		Map<String, List<AppNetstatInfo>> map = new HashMap<String, List<AppNetstatInfo>>();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd");
		for (DataHistoryEntry entry : list) {
			Map<Integer,Long> statsCellular = DataHistoryDBManager.stringToMap(entry.mobileData) ;
			Map<Integer,Long> statsWifi = DataHistoryDBManager.stringToMap(entry.wifiData) ;
			map.put(df.format(entry.saveTime), parseUsageMap(apps, statsCellular, statsWifi));
		}
		return map;
	}
	

	private List<AppNetstatInfo> parseUsageMap(
			Collection<ApplicationInfo> apps,
			Map<Integer,Long> statsCellular,
			Map<Integer,Long> statsWifi) {
		List<AppNetstatInfo> result = new ArrayList<AppNetstatInfo>();
		AppNetstatInfo info = null;
		for (ApplicationInfo app : apps) {
			long totalBytes;
			if (statsCellular != null && statsCellular.containsKey(app.uid) && statsCellular.get(app.uid) > 0) {
				if (info == null) {
					info = new AppNetstatInfo();
					info.uid = app.uid;
					info.pkgName = app.packageName;
				}
				info.mobiledataBytes = statsCellular.get(app.uid);
			}
			if (statsWifi != null && statsWifi.containsKey(app.uid) && statsWifi.get(app.uid) > 0) {
				if (info == null) {
					info = new AppNetstatInfo();
					info.uid = app.uid;
					info.pkgName = app.packageName;
				}
				info.wlanBytes = statsWifi.get(app.uid);
			}
			
			if (info != null) {
				result.add(info);
				info = null;
			}
		}
		return result;
	}
}
