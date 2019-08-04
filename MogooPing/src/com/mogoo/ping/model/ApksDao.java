package com.mogoo.ping.model;

import java.util.List;

import com.mogoo.ping.utils.Mogoo_Tools;
import com.mogoo.ping.vo.ApkItem;
import com.mogoo.ping.vo.UsedActivityItem;

import static com.mogoo.ping.model.DataBaseConfig.*;

import android.content.ContentValues;
import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.provider.SyncStateContract.Columns;

public class ApksDao {
	
	private static ApksDao dao;
	
	private DatabaseHelper helper;
	private SQLiteDatabase db;
	
	private ApksDao(Context context){
		helper = DatabaseHelper.getInstance(context);
	}
	
	public static ApksDao getInstance(Context context) {
		if (dao == null) {
			dao = new ApksDao(context);
		}
		return dao;
	}
	
	public void openDatabase() {
		try {
			if (db == null || !db.isOpen()) {
				db = helper.getWritableDatabase();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public Cursor queryAllCursorTable(String tableName) {
		Cursor cursor = null;
		try {
			openDatabase();
			cursor = db.query(tableName, null, null, null, null, null, null);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
		return cursor;
	}
	
	public Cursor querySingleItems(String table, String[] columns, String where, String[] args) {
		Cursor cursor = null;
		try {
			openDatabase();
			cursor = db.query(table, columns, where, args,null, null, null, "0, 1");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return cursor;
	}
	
	public void clearData(String tableName) {
		try {
			openDatabase();
			db.delete(tableName, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void addItems(String table, List<ApkItem> items) {
		openDatabase();
		ContentValues values = new ContentValues();
		for (ApkItem apk : items) {
			values.put(ApkListTable.COLUMN_ID, apk.getApkId());
			values.put(ApkListTable.COLUMN_NAME, apk.getApkName());
			values.put(ApkListTable.COLUMN_VERSION_CODE, apk.getVersionCode());
			values.put(ApkListTable.COLUMN_VERSION_STRING, apk.getVersionStr());
			values.put(ApkListTable.COLUMN_ICONURL_REMOTE, apk.getIconRemote());
			values.put(ApkListTable.COLUMN_ICONURL_LOCAL, apk.getIconLocal());
			values.put(ApkListTable.COLUMN_PACKAGE_NAME, apk.getPackageName());
			values.put(ApkListTable.COLUMN_ADDRESS_REMOTE, apk.getApkAddressRemote());
			values.put(ApkListTable.COLUMN_ADDRESS_LOCAL, apk.getApkAddressLocal());
			values.put(ApkListTable.COLUMN_APK_TYPE, apk.getType());
			db.insertOrThrow(table, null, values);
			values.clear();
		}
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * Add the items by id desc
	 * @param table
	 * @param items
	 * @Date 上午9:22:39  2012-10-25
	 */
	public void addItemsForUsed(String table, List<UsedActivityItem> items) {
		openDatabase();
		ContentValues values = new ContentValues();
		if (items == null) {
			return ;
		}
		for (int i = items.size()-1; i >= 0; i --) {
			//mogoo add xuzhenqin 20121011 begin
			UsedActivityItem apk = items.get(i);
			isExiteCompent(table,apk.getPackageName(), apk.getClassName());
			//mogoo add xuzhenqin 20121015 begin
			//apk.getLaunchIntent().getComponent().getClassName();
			//String component = Mogoo_Tools.castrateIntent(apk.getLaunchIntent().toUri(0));
			String component = apk.getLaunchIntent().getComponent().getClassName();
			String packageName = apk.getPackageName();
			//mogoo end
			values.put(ApkListTable.COLUMN_NAME, packageName+"/"+component);
			values.put(ApkListTable.COLUMN_PACKAGE_NAME, packageName);
			values.put(ApplicationsUsedTable.COLUMN_COMPANDNAME, component);
			db.insertOrThrow(table, null, values);
			values.clear();
			//mogoo end
		}
	}
	
	/**
	 * 
	 * @Author lizuokang
	 * ��ӵ���Ӧ����
	 * @param table
	 * @param apk
	 * @Date ����10:01:19  2012-9-21
	 */
	public void addSingleItem(String table, ApkItem apk) {
		ContentValues values = new ContentValues();
		openDatabase();
		try {
			values.put(ApkListTable.COLUMN_ID, apk.getApkId());
			values.put(ApkListTable.COLUMN_NAME, apk.getApkName());
			values.put(ApkListTable.COLUMN_VERSION_CODE, apk.getVersionCode());
			values.put(ApkListTable.COLUMN_VERSION_STRING, apk.getVersionStr());
			values.put(ApkListTable.COLUMN_ICONURL_REMOTE, apk.getIconRemote());
			values.put(ApkListTable.COLUMN_ICONURL_LOCAL, apk.getIconLocal());
			values.put(ApkListTable.COLUMN_PACKAGE_NAME, apk.getPackageName());
			values.put(ApkListTable.COLUMN_ADDRESS_REMOTE, apk.getApkId());
			values.put(ApkListTable.COLUMN_ADDRESS_LOCAL, apk.getApkId());
			values.put(ApkListTable.COLUMN_APK_TYPE, apk.getType());
			db.insertOrThrow(table, null, values);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		values.clear();
	}

	
	public void updateSingleItem(String table, ContentValues values, String apkId) {
		try {
			openDatabase();
			db.update(table, values, ApkListTable.COLUMN_ID + " = ? ", new String[]{apkId});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		values.clear();
	}
	

	//mogoo add xuzhenqin 20121011 begin
	public Cursor queryUsedApks(String table, int num) {
		try {
			openDatabase();
			return db.query(table, null, null, null, null, null, Columns._ID +" desc ", null);
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void isExiteCompent(String table,String packageName, String className){
		Cursor c = null;
		try {
			c  = db.query(table, new String[]{ApkListTable._ID}, ApkListTable.COLUMN_PACKAGE_NAME +" =? and " + ApplicationsUsedTable.COLUMN_COMPANDNAME + " =?", new String[]{packageName, className}, null, null, null, null);       
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				db.delete(table, ApkListTable._ID +"=? ", new String[] {String.valueOf(c.getInt(0))});
			}
		} finally {
			if (c != null)
				c.close();
		}
		return;
	}
	
	public void removeSinglePackage(String table, String packageName) {
		try {
			openDatabase();
			db.delete(table, ApkListTable.COLUMN_PACKAGE_NAME +" =?", new String[]{packageName});
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	//mogoo end
	public void closeDB() {
		if (db != null && db.isOpen()) {
			db.close();
		}
	}
	
}
