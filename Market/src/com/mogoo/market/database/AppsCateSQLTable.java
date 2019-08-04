package com.mogoo.market.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class AppsCateSQLTable implements BaseColumns {

	private AppsCateSQLTable() {
		
	}
	
	public static final String APPS_CATE_TABLE_NAME = "apps_cate_data";
	public static final String COLUMN_APPS_CATE_ID = "apps_cate_id";
	public static final String COLUMN_APPS_CATE_NAME = "apps_cate_name";
	public static final String COLUMN_APPS_CATE_COVER_URL = "apps_cate_cover_url";
	public static final String COLUMN_APPS_CATE_DESC = "apps_cate_desc";
	public static final String COLUMN_APPS_CATE_COUNT = "apps_cate_count";
	
	public static final int NUMBER_APPS_CATE_ID = 1;
	public static final int NUMBER_APPS_CATE_NAME = 2;
	public static final int NUMBER_APPS_CATE_COVER_URL = 3;
	public static final int NUMBER_APPS_CATE_DESC = 4;
	public static final int NUMBER_APPS_CATE_COUNT = 5;
	
	public String getTableName() {
		return APPS_CATE_TABLE_NAME;
	}
	
	private static AppsCateSQLTable instance = new AppsCateSQLTable();
	
	public static AppsCateSQLTable getInstance() {
		return instance;
	}
	
	public void creatTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + APPS_CATE_TABLE_NAME + "("
				+ _ID
				+ " INTEGER NOT NULL PRIMARY KEY,"
				+ COLUMN_APPS_CATE_ID + " TEXT ,"
				+ COLUMN_APPS_CATE_NAME + " TEXT ,"
				+ COLUMN_APPS_CATE_COVER_URL + " TEXT ,"
				+ COLUMN_APPS_CATE_DESC + " TEXT ,"
				+ COLUMN_APPS_CATE_COUNT + " TEXT "
				+ ");");
	}
}
