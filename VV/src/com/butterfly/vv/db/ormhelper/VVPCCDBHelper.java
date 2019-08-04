package com.butterfly.vv.db.ormhelper;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.butterfly.vv.db.Province;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public class VVPCCDBHelper extends OrmLiteSqliteOpenHelper {
	private static final String DATABASE_NAME = "ProvinceCityeCollege.db";
	private static final int DATABASE_VERSION = 1;
	private Dao<Province, Integer> proviceDao = null;
	private static VVPCCDBHelper pccHelper = null;

	public VVPCCDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	@Override
	public void onCreate(SQLiteDatabase sqLiteDatabase,
			ConnectionSource connectionSource) {
	}
	@Override
	public void onUpgrade(SQLiteDatabase arg0,
			ConnectionSource connectionSource, int arg2, int arg3) {
	}
	public Dao<Province, Integer> getProviceDao() throws SQLException {
		if (proviceDao == null) {
			proviceDao = getDao(Province.class);
		}
		return proviceDao;
	}
	@Override
	public void close() {
		super.close();
		proviceDao = null;
	}
}
