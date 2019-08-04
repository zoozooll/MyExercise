package com.oregonscientific.meep.youtube.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oregonscientific.meep.youtube.R;

/**
 * Database helper which creates and upgrades the database and provides the DAOs.
 * 
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {



	private static final String DATABASE_NAME = "browser.db";
	private static final int DATABASE_VERSION = 1;

	private Dao<Bookmark, Integer> bookmarkDao;
	private Dao<History, Integer> historyDao;

	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
	}

	@Override
	public void onCreate(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource) {
		try {
			TableUtils.createTable(connectionSource, Bookmark.class);
			TableUtils.createTable(connectionSource, History.class);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to create datbases", e);
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase sqliteDatabase, ConnectionSource connectionSource, int oldVer, int newVer) {
		try {
			TableUtils.dropTable(connectionSource, Bookmark.class, true);
			TableUtils.dropTable(connectionSource, History.class, true);
			onCreate(sqliteDatabase, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Unable to upgrade database from version " + oldVer + " to new "
					+ newVer, e);
		}
	}

	public Dao<Bookmark, Integer> getBookmarkDao() throws SQLException {
		if (bookmarkDao == null) {
			bookmarkDao = getDao(Bookmark.class);
		}
		return bookmarkDao;
	}
	public Dao<History, Integer> getHistoryDao() throws SQLException {
		if (historyDao == null) {
			historyDao = getDao(History.class);
		}
		return historyDao;
	}
}
