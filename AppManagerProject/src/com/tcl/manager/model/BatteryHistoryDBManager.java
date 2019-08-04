package com.tcl.manager.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;

import com.tcl.framework.db.EntityManager;
import com.tcl.framework.db.EntityManagerFactory;
import com.tcl.framework.db.sqlite.Selector;
import com.tcl.framework.db.sqlite.WhereBuilder;

public class BatteryHistoryDBManager {

	private volatile static BatteryHistoryDBManager instance;
    private static final int DB_VERSION = 1;
	protected EntityManager<BatteryHistoryEntry> dbMananger;
   
	public static BatteryHistoryDBManager getInstance(Context context) {
		if (instance == null) {
			synchronized (BatteryDBManager.class) {
				if (instance == null) {
					instance = new BatteryHistoryDBManager(context);
				}
			}
		}
		return instance;

	}
	
	private BatteryHistoryDBManager(Context context) {
		dbMananger = EntityManagerFactory.getInstance(
				context.getApplicationContext(), DB_VERSION, "appManager.db",
				null, null).getEntityManager(BatteryHistoryEntry.class,
				"battery_history");
	}
	
	public Map<String, Long> getUsageofDate(Date date) {
		Date d = new Date(date.getYear(), date.getMonth(), date.getDate());
		return getRealUsageOfDate(d);
	}
	
	/**
	 * Get the battery history usage data between begin date to end date,
	 * @param begin
	 * @param end
	 * @return
	 */
	public Map<String, Map<String, Long>> getUsageBetweenDate(Date begin, Date end) {
		Date beginDate = new Date(begin.getYear(), begin.getMonth(), begin.getDate());
		Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate());
		return  getRealUsageBetweenDate(beginDate, endDate);
	}
	
	public void addUsageOfDate(Date date,  Map<String, Long> map) {
		Date d = new Date(date.getYear(), date.getMonth(), date.getDate());
		String content = mapToString(map);
		BatteryHistoryEntry entry = new BatteryHistoryEntry();
		entry.saveTime = d;
		entry.batteryData = content;
		dbMananger.saveOrUpdate(entry);
	}
	
	public void deleteUnusefulData(Date today) {
		WhereBuilder where = WhereBuilder.create("savetIME", ">=", today);
		dbMananger.delete(where);
	}
	
	private Map<String, Map<String, Long>> getRealUsageBetweenDate(Date begin, Date end) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("saveTime", ">", begin);
		where.and("saveTime", "<=", end);
		selector.where(where);
		selector.orderBy("saveTime", false);
		List<BatteryHistoryEntry> list = dbMananger.findAll(selector);
		if (list == null) {
			return null;
		}
		Map<String, Map<String, Long>> map = new HashMap<String, Map<String,Long>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (BatteryHistoryEntry entry :list) {
			Map<String, Long> item =  stringToMap(entry.batteryData);
			if (item != null) map.put(sdf.format(entry.saveTime), item);
		}
		return map;
	}
	
	private Map<String, Long> getRealUsageOfDate(Date date) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("saveTime", "=", date);
		selector.where(where);
		selector.limit(1);
		selector.orderBy("saveTime", false);
		BatteryHistoryEntry entry = dbMananger.findFirst(selector);
		return stringToMap(entry.batteryData);
	}
	
	private static String mapToString(Map<String, Long> map) {
		if (map == null) {
			return null;
		}
		return map.toString();
	}
	
	private static Map<String, Long> stringToMap(String str) {
   		if (str == null) {
			return null;
		}
		int start = str.indexOf('{');
  		if (start == -1) {
			return null;
		}
		int end = str.lastIndexOf('}');
		if (end == -1 || end <= start) {
			return null;
		}
		str = str.substring(start + 1, end);
		String[] array = str.split(",");
		if (array == null) {
			return null;
		}
		Map<String, Long> map = new HashMap<String, Long>();
		for (String s : array) {
			int index = s.indexOf("=");
			if (index == -1) {
				continue;
			}
			map.put(s.substring(0, index).trim(), Long.valueOf(s.substring(index + 1, s.length()).trim()));
		}
		return map;
	}
}
