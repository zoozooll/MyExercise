package com.mogoo.ping.model;

import android.provider.BaseColumns;

public class DataBaseConfig {

	public static final String DATABASE_NAME = "mogooping.db";
	public static final int DATABASE_VERSION = 2;
	public static final String CRATE_SQL_FORMAT = "CREATE TABLE %s" +
			"(%s INTEGER NOT NULL PRIMARY KEY," +
			"%s INTEGER," +
			"%s TEXT NOT NULL," +
			"%s INTEGER," +
			"%s TEXT," +
			"%s TEXT," +
			"%s TEXT," +
			"%s TEXT," +
			"%s TEXT," +
			"%s TEXT," +
			"%s INTEGER," +
			"%s BIGINT )";
	
	
	public static class ApkListTable implements BaseColumns {
		
		public static final String COLUMN_ID = "apk_id";
		public static final String COLUMN_NAME = "apk_name";
		public static final String COLUMN_VERSION_CODE = "apk_version_code";
		public static final String COLUMN_VERSION_STRING = "apk_version_string";
		public static final String COLUMN_ICONURL_REMOTE = "apk_icon_remote_url";
		public static final String COLUMN_ICONURL_LOCAL = "apk_icon_local_url";
		public static final String COLUMN_PACKAGE_NAME = "apk_package_name";
		public static final String COLUMN_ADDRESS_REMOTE = "apk_remote_url";
		public static final String COLUMN_ADDRESS_LOCAL = "apk_local_url";
		public static final String COLUMN_APK_TYPE = "apk_type";
		public static final String COLUMN_APK_DOWNLOADID = "apk_local_downloadid";
	}
	
	public static class ApplicationsLastedTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_applications_lasted";
		static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT, TABLE_NAME, _ID, COLUMN_ID, COLUMN_NAME, COLUMN_VERSION_CODE,
					COLUMN_VERSION_STRING, COLUMN_ICONURL_REMOTE, COLUMN_ICONURL_LOCAL, COLUMN_PACKAGE_NAME,
					COLUMN_ADDRESS_REMOTE, COLUMN_ADDRESS_LOCAL, COLUMN_APK_TYPE, COLUMN_APK_DOWNLOADID);
	
	};
	
	public static class ApplicationsRecomendTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_applications_recomend";
		static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT,TABLE_NAME, _ID,  COLUMN_ID, COLUMN_NAME, COLUMN_VERSION_CODE,
				COLUMN_VERSION_STRING, COLUMN_ICONURL_REMOTE, COLUMN_ICONURL_LOCAL, COLUMN_PACKAGE_NAME,
				COLUMN_ADDRESS_REMOTE, COLUMN_ADDRESS_LOCAL, COLUMN_APK_TYPE, COLUMN_APK_DOWNLOADID);
	};
	
	public static class GamesLastedTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_games_lasted";
		static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT,TABLE_NAME, _ID,  COLUMN_ID, COLUMN_NAME, COLUMN_VERSION_CODE,
				COLUMN_VERSION_STRING, COLUMN_ICONURL_REMOTE, COLUMN_ICONURL_LOCAL, COLUMN_PACKAGE_NAME,
				COLUMN_ADDRESS_REMOTE, COLUMN_ADDRESS_LOCAL, COLUMN_APK_TYPE, COLUMN_APK_DOWNLOADID);
	};
	
	public static class GamesRecomendTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_games_recomend";
		static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT,TABLE_NAME, _ID,  COLUMN_ID, COLUMN_NAME, COLUMN_VERSION_CODE,
				COLUMN_VERSION_STRING, COLUMN_ICONURL_REMOTE, COLUMN_ICONURL_LOCAL, COLUMN_PACKAGE_NAME,
				COLUMN_ADDRESS_REMOTE, COLUMN_ADDRESS_LOCAL, COLUMN_APK_TYPE, COLUMN_APK_DOWNLOADID);
	};
	
	public static class ApplicationsUsedTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_applications_used";
		public static final String COLUMN_LASTUSED = "apk_lastused";
		public static final String COLUMN_COMPANDNAME = "apk_classname";
		private static final String CRATE_SQL_FORMAT = "CREATE TABLE %s" +
				"(%s INTEGER NOT NULL PRIMARY KEY," +
				"%s TEXT NOT NULL," +
				"%s TEXT," +
				"%s TEXT" +
				")";
		/*static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT,TABLE_NAME, _ID,  COLUMN_ID, COLUMN_NAME, COLUMN_VERSION_CODE,
				COLUMN_VERSION_STRING, COLUMN_ICONURL_REMOTE, COLUMN_ICONURL_LOCAL, COLUMN_PACKAGE_NAME,
				COLUMN_ADDRESS_REMOTE, COLUMN_ADDRESS_LOCAL, COLUMN_APK_TYPE, COLUMN_APK_DOWNLOADID);*/
		static final String CREATE_TABLE_SQL = String.format(CRATE_SQL_FORMAT, TABLE_NAME, _ID, COLUMN_NAME, COLUMN_PACKAGE_NAME, COLUMN_COMPANDNAME);
	};
	
	public static class GamesUsedTable extends ApkListTable {
		public static final String TABLE_NAME = "ping_games_used";
		public static final String COLUMN_LASTUSED = "apk_lastused";
		static final String CREATE_TABLE_SQL = "";
		
	};
}
