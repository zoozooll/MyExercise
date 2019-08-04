package com.idt.bw.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import com.idt.bw.activity.BuildConfig;
import com.idt.bw.bean.User;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

public class MyDatabase extends SQLiteOpenHelper {
	
	private Context context;
	static String MyDBname = "bw121.db";
	static final int VERSION = 10;

	// 建表语句，大小写不敏感
	private static final String CREATETABLE_USER = "create table " + User.TABLENAME + 
			"(_id INTEGER PRIMARY KEY,userPhoto string, userName string, userBirth string, userGender string,userPregnancyWeeks string," +
			"userPregnancyDays string,userHeight string,userCategory string,userWeightUnit string,userHeightUnit string)";

	private static final String CREATETABLE_RECORDS = String
			.format("create table %s (_id INTEGER PRIMARY KEY, %s REAL, %s INTEGER1, %s INTEGER, %s REAL, %s TEXT,%s REAL)",
					RecordConfig.TABLE_NAME, 
					RecordConfig.COL_WEIGHT,
					RecordConfig.COL_UNIT, 
					RecordConfig.COL_FOREIGN_KEY,
					RecordConfig.COL_CURRENT_HEIGHT, 
					RecordConfig.COL_DATETIME,
					RecordConfig.COL_TARGE_WEIGHT);
	

	private static final String CREATETABLE_USERSETTINGS = String
			.format("create table %s (_id INTEGER PRIMARY KEY, %s INTEGER, %s REAL, %s REAL,%s INTEGER1,%s INTEGER1,%s INTEGER1, %s INTEGER1, %s TEXT, %s TEXT,%s REAL, %s TEXT, %s INTEGER1)",
					UserSettingsConfig.TABLE_NAME,
					UserSettingsConfig.COL_FOREIGN_KEY_USER,
					UserSettingsConfig.COL_TARGET_WEIGHT,
					UserSettingsConfig.COL_Cloth_WEIGHT,
					UserSettingsConfig.COL_Cloth_ON,
					UserSettingsConfig.COL_Target_ON,
					UserSettingsConfig.COL_General_ON,
					UserSettingsConfig.COL_NOTIFY_ON,
					UserSettingsConfig.COL_NOTIFY_TIME,
					UserSettingsConfig.COL_NOTIFY_LOOP,
					UserSettingsConfig.COL_CURRENT_HEIGHT,
					UserSettingsConfig.COL_NOTIFY_DATE,
					UserSettingsConfig.COL_NOTIFY_FEATURE
					);

	private static final String CREATETABLE_REFERENCT_WEEKLY = "create table reference_data_weekly (_id integer primary key, weeks integer, value real, gender integer, category varchar(25))";
	
	private static final String CREATETABLE_REFERENCT_MONTHLY = "create table reference_data_monthly (_id integer primary key, months integer, value real, gender integer, category varchar(25))";
	
	static {
		if (BuildConfig.DEBUG) {
			File dbFile = new File(Environment.getExternalStorageDirectory(),
					MyDBname);
			if (!dbFile.exists()) {
				try {
					dbFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			MyDBname = dbFile.getPath();
			// Log.e("cdf","------------------------------------"+dbFile.getPath());
		}

	}

	public MyDatabase(Context context) {
		super(context, MyDBname, null, VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATETABLE_USER);
		db.execSQL(CREATETABLE_RECORDS);
		db.execSQL(CREATETABLE_USERSETTINGS);
		db.execSQL(CREATETABLE_REFERENCT_WEEKLY);
		db.execSQL(CREATETABLE_REFERENCT_MONTHLY);
		// added by aaronli at Apr25 2014. get reference data from assets file.
		InputStreamReader inputReader = null;
		BufferedReader bufReader = null;
		 try { 
             inputReader = new InputStreamReader(context.getResources().getAssets().open("reference_data.sql")); 
             bufReader = new BufferedReader(inputReader);
             String line;
             while((line = bufReader.readLine()) != null) {
            	if (line != null && "".equals(line.trim())) {
            		continue;
            	}
            	if (line.startsWith("--")) {
            		continue;
            	}
            	db.execSQL(line);
            }
        } catch (Exception e) { 
            e.printStackTrace(); 
        } finally {
        	if (bufReader != null) {
        		try {
					bufReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        	if (inputReader != null) {
        		try {
					inputReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			this.deleteDB(db);
			this.onCreate(db);
		}
	}

	private void deleteDB(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + User.TABLENAME);
		db.execSQL("drop table if exists " + UserSettingsConfig.TABLE_NAME);
		db.execSQL("drop table if exists " + RecordConfig.TABLE_NAME);
		db.execSQL("drop table if exists reference_data_weekly");
		db.execSQL("drop table if exists reference_data_monthly");
	}
}
