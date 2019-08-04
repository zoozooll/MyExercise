package com.mogoo.market.database;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Topic;

public class TopicDaoImpl implements IBeanDao<Topic> {

	private TopicDaoImpl(Context context) {
		mHelper = DatabaseHelper.getInstance(context);
	}
	
	private DatabaseHelper mHelper;
	private static TopicDaoImpl instance = null;
	
	public static TopicDaoImpl getInstance(Context ctx) {
		if(instance == null) {
			instance = new TopicDaoImpl(ctx);
		}
		return instance;
	}
	
	@Override
	public Cursor getAllBean() {
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor =  db.query(TopicSQLTable.TOPIC_TABLE_NAME, null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public void clearAllBean() {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int rows = db.delete(TopicSQLTable.TOPIC_TABLE_NAME, null, null);
	}

	@Override
	public void addBeans(ArrayList<Topic> beans) {
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for(Topic aTopic : beans) {
			ContentValues contentValues = new ContentValues();
			contentValues.put(TopicSQLTable.COLUMN_TOPIC_ID, aTopic.getId());
			contentValues.put(TopicSQLTable.COLUMN_TOPIC_NAME, aTopic.getName());
			contentValues.put(TopicSQLTable.COLUMN_TOPIC_COVER_URL, aTopic.getImgUrl());
			contentValues.put(TopicSQLTable.COLUMN_TOPIC_DESC, aTopic.getDescription());
			contentValues.put(TopicSQLTable.COLUMN_TOPIC_TYPE, aTopic.getType());
			db.insert(TopicSQLTable.TOPIC_TABLE_NAME, null, contentValues);
		}
	}

}
