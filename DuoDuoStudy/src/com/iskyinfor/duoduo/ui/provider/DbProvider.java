package com.iskyinfor.duoduo.ui.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * ��ݿ�����ṩ��
 * 
 * @author pKF29007
 * 
 */
public class DbProvider extends ContentProvider {

	public static final String TAG = "DbProvider";

	private DbHelper mDbHelper;

	private static final UriMatcher mMatcher;

	static {
		mMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		mMatcher.addURI(DbConstants.AUTHORITY, "item", DbConstants.ITEM);
	}

	@Override
	public boolean onCreate() {
		mDbHelper = new DbHelper(getContext(), DbConstants.DB_NAME, null,
				DbConstants.VERSION);
		return true;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		long rowid = 0;
		Uri itemuri = null;
		if (mMatcher.match(uri) == DbConstants.ITEM) {
			rowid = db.insertOrThrow(DbConstants.TB_NBAME, null, values);
		}
		if (rowid > 0) {
			itemuri = ContentUris
					.withAppendedId(DbConstants.CONTENT_URI, rowid);
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return itemuri;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (mMatcher.match(uri)) {
		case DbConstants.ITEM:
			count = db.delete(DbConstants.TB_NBAME, selection, selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		return count;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		int count;
		switch (mMatcher.match(uri)) {
		case DbConstants.ITEM:
			count = db.update(DbConstants.TB_NBAME, values, selection,
					selectionArgs);
			break;
		default:
			throw new IllegalArgumentException("Unknown URI" + uri);
		}
		if (count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		return count;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		Cursor c = null;
		switch (mMatcher.match(uri)) {
		case DbConstants.ITEM:
			c = db.query(DbConstants.TB_NBAME, projection, selection,
					selectionArgs, null, null, sortOrder);
			break;
		}
		return c;
	}

	@Override
	public String getType(Uri uri) {
		return null;
	}

}
