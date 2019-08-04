package com.oregonscientific.bbq.dao;

import java.io.File;
import java.io.IOException;

import com.oregonscientific.bbq.BuildConfig;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.widget.EditText;
import android.widget.ImageView;

/**
 * 数据库操作助手类
 * 
 * @author amy
 */
public class AndroidSQLiteOpenHelper extends SQLiteOpenHelper {

	// 数据库名称
	public static String DBNAME = "aw133_bbq.db";
	// 数据库版本
	public static final int VERSION = 5;
	// 表名
	//public static final String TABLENAME="myuser";
	/*private ImageView userphoto;
	private EditText userName;
	private EditText gender;
	private EditText dateofbirth;
	private EditText weight;
	private EditText height;*/
	
	static{
		if (BuildConfig.DEBUG) {
			File dbFile = new File(Environment.getExternalStorageDirectory(), DBNAME);
			if (!dbFile.exists())  {
				try {
					dbFile.createNewFile();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			DBNAME = dbFile.getPath();
		}
	}
	
	
	private static final String CREATETABLE_RECORDS = String
			.format("CREATE TABLE %s(_id INTEGER PRIMARY KEY, %s INT8, %s INTEGER, %s INTEGER, %s REAL, %s INTEGER," +
					" %s INTEGER,  %s TEXT, %s TEXT , %s TEXT, %s INTEGER, %s INTEGER, %s REAL, %s INTEGER" +
					", %s REAL, %s REAL, %s REAL, %s REAL, %s REAL)",
					BbqRecordsConfig.TABLENAME, 
					BbqRecordsConfig.COL_FINISHED_DATE        ,
					BbqRecordsConfig.COL_CHANNEL              ,
					BbqRecordsConfig.COL_MODE                 ,
					BbqRecordsConfig.COL_FINISHED_TEMPERATURE ,
					BbqRecordsConfig.COL_COSTD_TIME           ,
					BbqRecordsConfig.COL_COOKING_STATE        ,
					BbqRecordsConfig.COL_GRAPH                ,
					BbqRecordsConfig.COL_TEMPERATURES_FILE,
					BbqRecordsConfig.COL_MEMO                 ,
					BbqRecordsConfig.COL_SET_MEATTYPE         ,
					BbqRecordsConfig.COL_SET_DONENESSLEVEL    ,
					BbqRecordsConfig.COL_SET_TARGE_TEMPERATURE,
					BbqRecordsConfig.COL_SET_TOTALTIME,
					BbqRecordsConfig.COL_SET_DONENESS_R  ,
					BbqRecordsConfig.COL_SET_DONENESS_MR ,
					BbqRecordsConfig.COL_SET_DONENESS_M  ,
					BbqRecordsConfig.COL_SET_DONENESS_MW ,
					BbqRecordsConfig.COL_SET_DONENESS_W  
					);

	public AndroidSQLiteOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);
	}

	// 创建表
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATETABLE_RECORDS);
	}

	// 更新表
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			this.deleteDB(db);
			this.onCreate(db);
		}
	}

	// 删除表
	private void deleteDB(SQLiteDatabase db) {
		db.execSQL("drop table if exists " + BbqRecordsConfig.TABLENAME);
	}
}