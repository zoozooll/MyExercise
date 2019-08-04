package com.beem.project.btf.manager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.beem.project.btf.bbs.bean.DraftInfo;
import com.beem.project.btf.bbs.db.DraftOpenHelper;

public class DraftDao {
	private DraftOpenHelper draftOpenHelper;

	public DraftDao(Context context) {
		draftOpenHelper = new DraftOpenHelper(context, "draft.db", null, 1);
	}
	/**
	 * 根据id,type,content增加内容
	 */
	public int addDraftInfo(String id, String type, String content) {
		SQLiteDatabase sql = null;
		long insert = -1;
		try {
			sql = draftOpenHelper.getWritableDatabase();
			if (sql != null) {
				// 插入到哪个表中
				ContentValues values = new ContentValues();
				values.put("id", id);
				values.put("type", type);
				values.put("content", content);
				insert = sql.insert("info", null, values);
			}
		} finally {
			if (sql != null)
				sql.close();
		}
		return (int) insert;
	}
	/**
	 * 根据id,type删除草稿内容
	 */
	public int deleteDraftContent(String id, String type) {
		SQLiteDatabase sql = null;
		int delete = -1;
		try {
			sql = draftOpenHelper.getWritableDatabase();
			if (sql != null)
				delete = sql.delete("info", "id=? and type=?", new String[] {
						id, type });
		} finally {
			if (sql != null)
				sql.close();
		}
		return delete;
	}
	/**
	 * 删除所有的数据
	 */
	public int deleteAllContent() {
		SQLiteDatabase sql = null;
		int delete = -1;
		try {
			sql = draftOpenHelper.getWritableDatabase();
			if (sql != null)
				delete = sql.delete("info", null, null);
		} finally {
			if (sql != null)
				sql.close();
		}
		return delete;
	}
	/**
	 * 修改已有的数据
	 */
	public int update(String id, String type, String content) {
		SQLiteDatabase sql = null;
		int update = -1;
		try {
			sql = draftOpenHelper.getWritableDatabase();
			if (sql != null) {
				ContentValues contentValues = new ContentValues();
				contentValues.put("content", content);
				update = sql.update("info", contentValues, "id=? and type=?",
						new String[] { id, type });
			}
		} finally {
			if (sql != null) {
				sql.close();
			}
		}
		return update;
	}
	/**
	 * 根据id,type查询一条记录
	 */
	public DraftInfo query(String id, String type) {
		SQLiteDatabase sql = null;
		Cursor cursor = null;
		DraftInfo draftInfo = null;
		try {
			sql = draftOpenHelper.getReadableDatabase();
			if (sql != null) {
				cursor = sql.query("info", null, "id=? and type=?",
						new String[] { id, type }, null, null, null);
				if (cursor != null && cursor.moveToNext()) {
					String id_val = cursor.getString(0);
					String type_val = cursor.getString(1);
					String content_val = cursor.getString(2);
					draftInfo = new DraftInfo(id_val, type_val, content_val);
				}
			}
		} finally {
			if (cursor != null)
				cursor.close();
			if (sql != null)
				sql.close();
		}
		return draftInfo;
	}
}
