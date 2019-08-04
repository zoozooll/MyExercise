package com.mogoo.market.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class TopicSQLTable implements BaseColumns {

	private TopicSQLTable() {
		
	}
	
	public static final String TOPIC_TABLE_NAME = "topic_data";
	public static final String COLUMN_TOPIC_ID = "topic_id";
	public static final String COLUMN_TOPIC_NAME = "topic_name";
	public static final String COLUMN_TOPIC_COVER_URL = "topic_cover_url";
	public static final String COLUMN_TOPIC_DESC = "topic_desc";
	public static final String COLUMN_TOPIC_TYPE = "topic_type";
	
	public static final int NUMBER_TOPIC_ID = 1;
	public static final int NUMBER_TOPIC_NAME = 2;
	public static final int NUMBER_TOPIC_COVER_URL = 3;
	public static final int NUMBER_TOPIC_DESC = 4;
	public static final int NUMBER_TOPIC_TYPE = 5;
	
	
	public String getTableName() {
		return TOPIC_TABLE_NAME;
	}
	
	private static TopicSQLTable instance = new TopicSQLTable();
	
	public static TopicSQLTable getInstance() {
		return instance;
	}
	
	public void creatTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TOPIC_TABLE_NAME + "("
				+ _ID
				+ " INTEGER NOT NULL PRIMARY KEY,"
				+ COLUMN_TOPIC_ID + " TEXT ,"
				+ COLUMN_TOPIC_NAME + " TEXT ,"
				+ COLUMN_TOPIC_COVER_URL + " TEXT ,"
				+ COLUMN_TOPIC_DESC + " TEXT ,"
				+ COLUMN_TOPIC_TYPE + " TEXT "
				+ ");");
	}
}
