package com.tcl.manager.model;

import java.util.List;

import android.content.Context;
import android.database.Cursor;

import com.tcl.framework.db.EntityManager;
import com.tcl.framework.db.EntityManagerFactory;
import com.tcl.framework.db.sqlite.Selector;
import com.tcl.framework.db.sqlite.WhereBuilder;

/**
 * @author difei.zou
 * @Description:数据库操作示例
 * @date 2014/10/20 20:57
 * @copyright TCL-MIE
 */

public class BatteryDBManager {

	private volatile static BatteryDBManager instance;
    private static final int DB_VERSION = 1;
	protected EntityManager<BatteryUsageEntry> dbMananger;
   
	public static BatteryDBManager getInstance(Context context) {
		if (instance == null) {
			synchronized (BatteryDBManager.class) {
				if (instance == null) {
					instance = new BatteryDBManager(context);
				}
			}
		}
		return instance;

	}
	
	private BatteryDBManager(Context context) {
		dbMananger = EntityManagerFactory.getInstance(
				context.getApplicationContext(), DB_VERSION, "appManager.db",
				null, null).getEntityManager(BatteryUsageEntry.class,
				"battery_usage");
	}
	
	public BatteryUsageEntry getFirstToday(long beginOfDay, long now) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("savetime", ">=", beginOfDay);
		where.and("savetime", "<", now);
		selector.where(where);
		selector.limit(1);
		selector.orderBy("savetime", false);
		return dbMananger.findFirst(selector);
	}

	public BatteryUsageEntry getPrevInfoToday(long time) {
		Selector selector = Selector.create();
		WhereBuilder where = WhereBuilder.create("savetime", "<", time);
		selector.where(where);
		selector.limit(1);
		selector.orderBy("savetime", true);
		return dbMananger.findFirst(selector);
	}
	
	public void saveAll(List<BatteryUsageEntry> values) {
		dbMananger.saveOrUpdateAll(values);
	}
	
	public void saveItem(BatteryUsageEntry values) {
		dbMananger.saveOrUpdate(values);
	}

}
