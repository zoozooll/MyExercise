/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.recommendation;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of
 * recommendation database. This class also usually provides the DAOs used by
 * the other classes.
 */
public class RecommendationDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private final String TAG = getClass().getName();
	
	// name of the database file
	private static final String DATABASE_NAME = "recommendations.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;
	
	public RecommendationDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * This is called when the database is first created. Usually you should call
	 * createTable statements here to create the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Creating database tables and insert data...");
			TableUtils.createTable(connectionSource, $User.class);
			TableUtils.createTable(connectionSource, Recommendation.class);
		} catch (SQLException e) {
			Log.e(TAG, "Cannot create database: ", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * This is called when your application is upgraded and it has a higher
	 * version number. This allows you to adjust the various data to match the new
	 * version number.
	 */
	@Override
	public void onUpgrade(
			SQLiteDatabase db,
			ConnectionSource connectionSource,
			int oldVersion,
			int newVersion) {
		// TODO: Alter appropriate database tables
		Log.i(TAG, "Upgrading database tables...");
	}
	
}
