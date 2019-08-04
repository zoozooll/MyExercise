package com.mogoo.market.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Apps;

public class AppsCateDaoImpl implements IBeanDao<Apps> {

	private AppsCateDaoImpl(Context context) {
		mHelper = DatabaseHelper.getInstance(context);
	}
	
	private DatabaseHelper mHelper;
	private static AppsCateDaoImpl instance = null;
	
	public static AppsCateDaoImpl getInstance(Context ctx) {
		if(instance == null) {
			instance = new AppsCateDaoImpl(ctx);
		}
		return instance;
	}
	
	@Override
	public Cursor getAllBean() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor =  db.query(AppsCateSQLTable.APPS_CATE_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public void clearAllBean() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int rows = db.delete(AppsCateSQLTable.APPS_CATE_TABLE_NAME, null, null);
	}

	@Override
	public void addBeans(ArrayList<Apps> beans) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for(Apps aApps : beans) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(AppsCateSQLTable.COLUMN_APPS_CATE_ID, aApps.getId());
			contentValues.put(AppsCateSQLTable.COLUMN_APPS_CATE_NAME, aApps.getName());
			contentValues.put(AppsCateSQLTable.COLUMN_APPS_CATE_COVER_URL, aApps.getImgUrl());
			contentValues.put(AppsCateSQLTable.COLUMN_APPS_CATE_DESC, aApps.getDescription());
			contentValues.put(AppsCateSQLTable.COLUMN_APPS_CATE_COUNT, aApps.getCount());
			db.insert(AppsCateSQLTable.APPS_CATE_TABLE_NAME, null, contentValues);
		}
	}

}
