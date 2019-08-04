/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.account;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

/**
 * Database helper class used to manage the creation and upgrading of accounts
 * database. This class also usually provides the DAOs used by the other
 * classes.
 */
public class AccountDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private final String TAG = getClass().getName();
	
	// Name of the database file
	private static final String DATABASE_NAME = "accounts.db";
	// Any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;
	
	public AccountDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "Creating database tables and insert data...");
			TableUtils.createTable(connectionSource, Account.class);
			
			// Creates a default account
			Account account = new Account(Account.DEFAULT_USER_ID);
			account.setFirstName(Account.DEFAULT_FIRST_NAME);
			account.setMeepTag(Account.DEFAULT_MEEP_TAG);
			account.setNickname(Account.DEFAULT_NICKNAME);
			
			Dao<Account, Long> dao = getDao(Account.class);
			dao.create(account);
		} catch (SQLException ex) {
			Log.e(TAG, "Cannot create database: ", ex);
			throw new RuntimeException(ex);
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
