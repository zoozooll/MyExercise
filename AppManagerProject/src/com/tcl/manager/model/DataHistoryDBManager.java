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
import com.tcl.manager.datausage.NetworkStatsHistory;

public class DataHistoryDBManager {

	private volatile static DataHistoryDBManager instance;
    private static final int DB_VERSION = 1;
	protected EntityManager<DataHistoryEntry> dbMananger;
   
	public static DataHistoryDBManager getInstance(Context context) {
		if (instance == null) {
			synchronized (BatteryDBManager.class) {
				if (instance == null) {
					instance = new DataHistoryDBManager(context);
				}
			}
		}
		return instance;

	}
	
	private DataHistoryDBManager(Context context) {
		dbMananger = EntityManagerFactory.getInstance(
				context.getApplicationContext(), DB_VERSION, "appManager.db",
				null, null).getEntityManager(DataHistoryEntry.class,
				"datausage_history");
	}
	
	public DataHistoryEntry getUsageofDate(Date date) {
		Date d = new Date(date.getYear(), date.getMonth(), date.getDate());
		return getRealUsageOfDate(d);
	}
	
	/**
	 * Get the battery history usage data between begin date to end date,
	 * @param begin
	 * @param end
	 * @return
	 */
	public List<DataHistoryEntry> getUsageBetweenDate(Date begin, Date end) {
		Date beginDate = new Date(begin.getYear(), begin.getMonth(), begin.getDate());
		Date endDate = new Date(end.getYear(), end.getMonth(), end.getDate());
		return  getRealUsageBetweenDate(beginDate, endDate);
	}
	
	public void addUsageOfDate(Date date,
			HashMap<Integer, NetworkStatsHistory> wifiMap,
			HashMap<Integer, NetworkStatsHistory> mobiledataMap,
			long totalWifi, long totalMobile) {
		Date d = new Date(date.getYear(), date.getMonth(), date.getDate());
		DataHistoryEntry entry = new DataHistoryEntry();
		entry.saveTime = d;
		entry.wifiData = mapToString(wifiMap);
		entry.mobileData = mapToString(mobiledataMap);
		entry.totalWifi = totalWifi;
		entry.totalMobileData = totalMobile;
		dbMananger.saveOrUpdate(entry);
	}
	
	public void deleteUnusefulData(Date today) {
		WhereBuilder where = WhereBuilder.create("savetIME", ">=", today);
		dbMananger.delete(where);
	}
	
	private List<DataHistoryEntry> getRealUsageBetweenDate(Date begin, Date end) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("saveTime", ">", begin);
		where.and("saveTime", "<=", end);
		selector.where(where);
		selector.orderBy("saveTime", false);
		List<DataHistoryEntry> list = dbMananger.findAll(selector);
		/*if (list == null) {
			return null;
		}
		Map<String, Map<String, Long>> map = new HashMap<String, Map<String,Long>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		for (DataHistoryEntry entry :list) {
			Map<String, Long> item =  stringToMap(entry.batteryData);
			if (item != null) map.put(sdf.format(entry.saveTime), item);
		}*/
		return list;
	}
	
	private DataHistoryEntry getRealUsageOfDate(Date date) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("saveTime", "=", date);
		selector.where(where);
		selector.limit(1);
		selector.orderBy("saveTime", false);
		DataHistoryEntry entry = dbMananger.findFirst(selector);
		return entry;
	}
	
	private static String mapToString(HashMap<Integer, NetworkStatsHistory> map) {
		if (map == null) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		for (Map.Entry<Integer, NetworkStatsHistory> entry : map.entrySet()) {
			NetworkStatsHistory value = entry.getValue();
			if (value == null) continue;
			if (value.getTotalBytes() > 0) {
				sb.append(entry.getKey());
				sb.append(':');
				sb.append(value.getTotalBytes());
				sb.append(',');
			}
		}
		return sb.toString();
	}
	
	public static Map<Integer, Long> stringToMap(String str) {
   		if (str == null || str.length() <= 1) {
			return null;
		}
		str = str.substring(0, str.length() -1);
		String[] array = str.split(",");
		if (array == null) {
			return null;
		}
		Map<Integer, Long> map = new HashMap<Integer, Long>();
		try {
			for (String s : array) {
				int index = s.indexOf(":");
				if (index == -1) {
					continue;
				}
				map.put(Integer.valueOf(s.substring(0, index)), Long.valueOf(s.substring(index + 1, s.length()).trim()));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return map;
	}
}
