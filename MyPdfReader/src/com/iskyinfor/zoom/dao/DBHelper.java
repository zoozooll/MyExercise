package com.iskyinfor.zoom.dao;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

public class DBHelper extends SQLiteOpenHelper {

	private static DBHelper helper;

	public static final String DATABASE_NAME = "books.db";
	public static final int DATEBASE_VERSION = 1;
	
	public static final String ID = "_id";

	public static final String TABLE_BOOK_MARK = "duoduo_bookmark";

	public static final String BOOK_ID = "book_id";

	public static final String PAGE = "page_Index";

	public static final String MARK_DATE = "mark_dae";
	
	private DBHelper(Context context, int versionCode ) {
		super(context, DATABASE_NAME, null, versionCode);
	}

	public static DBHelper getHelper(Context context) {
		if (helper == null) {
			/*-	数据库版本跟软件版本统一，无需每次都卸载再安装
			 * 
			 * */
			
			PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = null;
			try {
				pi = pm.getPackageInfo(context.getPackageName(), 0);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}  
	        int versionCode = pi.versionCode ; 
			helper = new DBHelper(context, versionCode);
		}
		return helper;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String createTableBooks = "CREATE TABLE "+TABLE_BOOK_MARK
			+" ( "+ID+" integer primary key AUTOINCREMENT , "
			+BOOK_ID+" integer NOT NULL, "
			+PAGE+" integer DEFAULT NULL, "
			+MARK_DATE+" numeric DEFAULT NULL) ";
		db.execSQL(createTableBooks);
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("drop table if exists "+TABLE_BOOK_MARK);
		onCreate(db);
	}

}
