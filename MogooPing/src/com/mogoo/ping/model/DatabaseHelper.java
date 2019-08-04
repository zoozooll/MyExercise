package com.mogoo.ping.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static com.mogoo.ping.model.DataBaseConfig.*;

class DatabaseHelper extends SQLiteOpenHelper {
	
	private static DatabaseHelper self;

	private DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	public static DatabaseHelper getInstance(Context context) {
		if (self == null) {
			self = new DatabaseHelper(context);
		}
		return self;
	}
	
	public static void recyleInstance() {
		self = null;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(ApplicationsLastedTable.CREATE_TABLE_SQL);
		db.execSQL(GamesLastedTable.CREATE_TABLE_SQL);
		db.execSQL(ApplicationsRecomendTable.CREATE_TABLE_SQL);
		db.execSQL(GamesRecomendTable.CREATE_TABLE_SQL);
		db.execSQL(ApplicationsUsedTable.CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (newVersion > oldVersion) {
			db.execSQL("DROP TABLE "+ApplicationsLastedTable.TABLE_NAME);
			db.execSQL("DROP TABLE "+GamesLastedTable.TABLE_NAME);
			db.execSQL("DROP TABLE "+ApplicationsRecomendTable.TABLE_NAME);
			db.execSQL("DROP TABLE "+GamesRecomendTable.TABLE_NAME);
			db.execSQL("DROP TABLE "+ApplicationsUsedTable.TABLE_NAME);
			onCreate(db);
		}
	}

}
