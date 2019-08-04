package com.mogoo.market.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class ApkListSQLTable implements BaseColumns {
	public static final String TABLE_HOT = "hot_data";
	public static final String TABLE_LATEST = "latest_data";
	public static final String TABLE_NECESSARY = "necessary_data";
	public static final String TABLE_TOP_RANKING = "top_ranking_data";
	public static final String TABLE_APPS_RANKING = "apps_ranking_data";
	public static final String TABLE_GAME_RANKING = "game_ranking_data";
	public static final String TABLE_CHILD_CATE = "child_cate_data";
	public static final String TABLE_QUALITY_APPS = "quality_apps_data";
	public static final String TABLE_NEWEST_APPS = "newest_apps_data";
	public static final String TABLE_QUALITY_GAME = "quality_game_data";
	public static final String TABLE_NEWEST_GAME = "newest_game_data";
	public static final String TABLE_INSTALLOUS = "installous_data";
	
	public static final String COLUMN_APK_OPEN_TYPE = "apk_open_type";
	public static final String COLUMN_APK_ID = "apk_id";
	public static final String COLUMN_APK_NAME = "apk_name";
	public static final String COLUMN_APK_VERSION_CODE = "apk_version_code";
	public static final String COLUMN_APK_VERSION_STRING = "apk_version_str";
	public static final String COLUMN_APK_SIZE = "apk_szie";
	public static final String COLUMN_APK_VSCORE = "apk_vscore";
	public static final String COLUMN_APK_RSCORE = "apk_rscore";
	public static final String COLUMN_APK_PRICE = "apk_price";
	public static final String COLUMN_APK_AUTHOR = "apk_author";
	public static final String COLUMN_APK_ADDRESS= "apk_address";
	public static final String COLUMN_APK_ICONURL = "apk_icon_url";
	public static final String COLUMN_APK_PACKAGENAME = "apk_package_name";
	
	public static final int NUMBER_APK_OPEN_TYPE = 1;
	public static final int NUMBER_APK_ID = 2;
	public static final int NUMBER_APK_NAME = 3;
	public static final int NUMBER_APK_VERSION_CODE = 4;
	public static final int NUMBER_APK_VERSION_int = 5;
	public static final int NUMBER_APK_SIZE = 6;
	public static final int NUMBER_APK_VSCORE = 7;
	public static final int NUMBER_APK_RSCORE = 8;
	public static final int NUMBER_APK_PRICE = 9;
	public static final int NUMBER_APK_AUTHOR = 10;
	public static final int NUMBER_APK_ADDRESS= 11;
	public static final int NUMBER_APK_ICONURL = 12;
	public static final int NUMBER_APK_PACKAGENAME = 13;
	
	private ApkListSQLTable() {
	}
	
	private static ApkListSQLTable instance;
	
	public static ApkListSQLTable getInstance() {
		if(instance == null) {
			instance = new ApkListSQLTable();
		}
		return instance;
	}
	
	public void creatTable(SQLiteDatabase db, String tableName) {
		if(TextUtils.isEmpty(tableName)) {
			throw new IllegalArgumentException("cannot create table wiht empty name.");
		}
		
		db.execSQL("CREATE TABLE " + tableName + "("
				+ _ID
				+ " INTEGER NOT NULL PRIMARY KEY,"
				+ COLUMN_APK_OPEN_TYPE + " TEXT ,"
				+ COLUMN_APK_ID + " TEXT ,"
				+ COLUMN_APK_NAME + " TEXT ,"
				+ COLUMN_APK_VERSION_CODE + " INTEGER ,"
				+ COLUMN_APK_VERSION_STRING + " TEXT ,"
				+ COLUMN_APK_SIZE + " INTEGER ,"
				+ COLUMN_APK_VSCORE + " TEXT ,"
				+ COLUMN_APK_RSCORE + " TEXT ,"
				+ COLUMN_APK_PRICE + " TEXT ,"
				+ COLUMN_APK_AUTHOR + " TEXT ,"
				+ COLUMN_APK_ADDRESS + " TEXT ,"
				+ COLUMN_APK_ICONURL + " TEXT ,"
				+ COLUMN_APK_PACKAGENAME + " TEXT "
				+ ");");
	}
}
