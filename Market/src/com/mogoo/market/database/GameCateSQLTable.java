package com.mogoo.market.database;

import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

public class GameCateSQLTable implements BaseColumns {

	private GameCateSQLTable() {
		
	}
	
	public static final String GAME_CATE_TABLE_NAME = "game_cate_data";
	public static final String COLUMN_GAME_CATE_ID = "game_cate_id";
	public static final String COLUMN_GAME_CATE_NAME = "game_cate_name";
	public static final String COLUMN_GAME_CATE_COVER_URL = "game_cate_cover_url";
	public static final String COLUMN_GAME_CATE_DESC = "game_cate_desc";
	public static final String COLUMN_GAME_CATE_COUNT = "game_cate_count";
	
	public static final int NUMBER_GAME_CATE_ID = 1;
	public static final int NUMBER_GAME_CATE_NAME = 2;
	public static final int NUMBER_GAME_CATE_COVER_URL = 3;
	public static final int NUMBER_GAME_CATE_DESC = 4;
	public static final int NUMBER_GAME_CATE_COUNT = 5;
	
	public String getTableName() {
		return GAME_CATE_TABLE_NAME;
	}
	
	private static GameCateSQLTable instance = new GameCateSQLTable();
	
	public static GameCateSQLTable getInstance() {
		return instance;
	}
	
	public void creatTable(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + GAME_CATE_TABLE_NAME + "("
				+ _ID
				+ " INTEGER NOT NULL PRIMARY KEY,"
				+ COLUMN_GAME_CATE_ID + " TEXT ,"
				+ COLUMN_GAME_CATE_NAME + " TEXT ,"
				+ COLUMN_GAME_CATE_COVER_URL + " TEXT ,"
				+ COLUMN_GAME_CATE_DESC + " TEXT ,"
				+ COLUMN_GAME_CATE_COUNT + " TEXT "
				+ ");");
	}
}
