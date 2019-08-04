package com.mogoo.market.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.HotApp;

public class ApkListDaoImpl implements IBeanDao<HotApp> {
	private String mTableName;
	
	private ApkListDaoImpl(Context context, String tableName) {
		mHelper = DatabaseHelper.getInstance(context);
		mTableName = tableName;
	}
	
	private DatabaseHelper mHelper;
	private static ApkListDaoImpl instance = null;
	
	public static ApkListDaoImpl getInstance(Context ctx, String tableName) {
		instance = new ApkListDaoImpl(ctx, tableName);
		return instance;
	}
	
	@Override
	public Cursor getAllBean() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor =  db.query(mTableName, null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public void clearAllBean() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int rows = db.delete(mTableName, null, null);
	}

	@Override
	public void addBeans(ArrayList<HotApp> beans) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for(HotApp apk : beans) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(ApkListSQLTable.COLUMN_APK_OPEN_TYPE, apk.getOpenType());
			contentValues.put(ApkListSQLTable.COLUMN_APK_ID, apk.getApkId());
			contentValues.put(ApkListSQLTable.COLUMN_APK_NAME, apk.getName());
			contentValues.put(ApkListSQLTable.COLUMN_APK_VERSION_CODE, apk.getVersionCode());
			contentValues.put(ApkListSQLTable.COLUMN_APK_VERSION_STRING, apk.getVersionStr());
			contentValues.put(ApkListSQLTable.COLUMN_APK_SIZE, apk.getApkSize());
			contentValues.put(ApkListSQLTable.COLUMN_APK_VSCORE, apk.getvScore());
			contentValues.put(ApkListSQLTable.COLUMN_APK_RSCORE, apk.getrScore());
			contentValues.put(ApkListSQLTable.COLUMN_APK_PRICE, apk.getPrice());
			contentValues.put(ApkListSQLTable.COLUMN_APK_AUTHOR, apk.getAuthor());
			contentValues.put(ApkListSQLTable.COLUMN_APK_ADDRESS, apk.getApkAddress());
			contentValues.put(ApkListSQLTable.COLUMN_APK_ICONURL, apk.getIconUrl());
			contentValues.put(ApkListSQLTable.COLUMN_APK_PACKAGENAME, apk.getPackageName());
			db.insert(mTableName, null, contentValues);
		}
	}

}
