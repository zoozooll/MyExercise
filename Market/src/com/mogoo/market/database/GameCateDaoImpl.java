package com.mogoo.market.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Game;

public class GameCateDaoImpl implements IBeanDao<Game> {

	private GameCateDaoImpl(Context context) {
		mHelper = DatabaseHelper.getInstance(context);
	}
	
	private DatabaseHelper mHelper;
	private static GameCateDaoImpl instance = null;
	
	public static GameCateDaoImpl getInstance(Context ctx) {
		if(instance == null) {
			instance = new GameCateDaoImpl(ctx);
		}
		return instance;
	}
	
	@Override
	public Cursor getAllBean() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor =  db.query(GameCateSQLTable.GAME_CATE_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public void clearAllBean() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int rows = db.delete(GameCateSQLTable.GAME_CATE_TABLE_NAME, null, null);
	}

	@Override
	public void addBeans(ArrayList<Game> beans) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for(Game aGame : beans) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(GameCateSQLTable.COLUMN_GAME_CATE_ID, aGame.getId());
			contentValues.put(GameCateSQLTable.COLUMN_GAME_CATE_NAME, aGame.getName());
			contentValues.put(GameCateSQLTable.COLUMN_GAME_CATE_COVER_URL, aGame.getImgUrl());
			contentValues.put(GameCateSQLTable.COLUMN_GAME_CATE_DESC, aGame.getDescription());
			contentValues.put(GameCateSQLTable.COLUMN_GAME_CATE_COUNT, aGame.getCount());
			db.insert(GameCateSQLTable.GAME_CATE_TABLE_NAME, null, contentValues);
		}
	}

}
