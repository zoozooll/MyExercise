package com.mogoo.market.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;
import android.text.TextUtils;

public class DownloadInfoSQLTable implements BaseColumns
{
	public static final String TABLE_DOWNLOAD_INFO = "downloadinfo_table";
	
	public final static String ID = "_id";  //0
	public final static String DOWNLOAD_ID = "downloadinfo_id";   //1
	public final static String APP_ID = "downloadinfo_app_id";   //2
	public final static String DOWNLOAD_URL = "downloadinfo_url";   //3
	public final static String SAVE_PATH = "downloadinfo_save_path";   //4
	public final static String NAME = "downloadinfo_name";    //5
	public final static String SIZE = "downloadinfo_size";   //6
	public final static String ICON_URL = "downloadinfo_icon_url";   //7
	public final static String RATING = "downloadinfo_rating";   //8
	public final static String APP_VERSION_NAME = "downloadinfo_version_name";   //9
	public final static String APP_VERSION_CODE = "downloadinfo_version_code";   //10
	public final static String PACKAGE_NAME = "package_name";   //11
	
	private DownloadInfoSQLTable() {
	}
	
	private static DownloadInfoSQLTable instance;
	
	public static DownloadInfoSQLTable getInstance() {
		if(instance == null) {
			instance = new DownloadInfoSQLTable();
		}
		return instance;
	}
	
	public void creatTable(SQLiteDatabase db, String tableName) 
	{
		if(TextUtils.isEmpty(tableName)) 
		{
			throw new IllegalArgumentException("cannot create table wiht empty name.");
		}
		
		String sql = "create table downloadinfo_table (_id integer primary key, downloadinfo_id text, downloadinfo_app_id text,downloadinfo_url text, " +
				"downloadinfo_save_path text,downloadinfo_name text, downloadinfo_size text, downloadinfo_icon_url text," +
				" downloadinfo_rating text,downloadinfo_version_name text,downloadinfo_version_code integer,package_name text)";
		db.execSQL(sql);
	}
}
