package com.oregonscientific.meep.database;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.oregonscientific.meep.communicator.ContentType;
import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.User;
import com.oregonscientific.meep.communicator.UserFriend;

/**
 * Database helper class used to manage the creation and upgrading of
 * communicator database. This class also usually provides the DAOs used by the
 * other classes.
 */
public class CommunicatorDatabaseHelper extends OrmLiteSqliteOpenHelper {
	
	private final String LOG_TAG = getClass().getSimpleName();
	
	// name of the database file
	private static final String DATABASE_NAME = "communicator.db";
	// any time you make changes to your database objects, you may have to
	// increase the database version
	private static final int DATABASE_VERSION = 1;
	
	public CommunicatorDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	/**
	 * This is called when the database is first created. Usually you should call
	 * createTable statements here to create the tables that will store your data.
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(LOG_TAG, "onCreate");
			TableUtils.createTable(connectionSource, User.class);
			TableUtils.createTable(connectionSource, ContentType.class);
			TableUtils.createTable(connectionSource, Friend.class);
			TableUtils.createTable(connectionSource, UserFriend.class);
			TableUtils.createTable(connectionSource, Conversation.class);
			TableUtils.createTable(connectionSource, ConversationMessage.class);
		} catch (SQLException e) {
			Log.e(LOG_TAG, "Can't create database: ", e);
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
		Log.i(LOG_TAG, "onUpgrade");
	}
	
}
