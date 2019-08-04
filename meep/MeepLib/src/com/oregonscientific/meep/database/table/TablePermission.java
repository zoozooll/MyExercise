package com.oregonscientific.meep.database.table;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class TablePermission {
	public static final String S_TABLE_NAME = "permission";
	public static final String S_ID = "id";
	public static final String S_APP_NAME = "appName";
	public static final String S_CAN_ACCESS = "canAccess";
	public static final String S_TIME_LIMIT = "timeLimit";
	public static final String S_TIME_USED = "timeUsed";
	public static final String S_LAST_OPEN = "lastOpen";
	public static final String S_TIME_STAMP = "timeStamp";
	
	private int mId;
	private String mAppName;
	private int mCanAccess;
	private int mTimeLimit;
	private int mTimeUsed;
	private long mLastOpen;
	private long mTimeStamp;
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	
	public String getAppName() {
		return mAppName;
	}
	public void setAppName(String appName) {
		this.mAppName = appName;
	}
	
	public int getCanAccess() {
		return mCanAccess;
	}
	public void setCanAccess(int canAccess) {
		this.mCanAccess = canAccess;
	}
	
	public int getTimeLimit() {
		return mTimeLimit;
	}

	public void setTimeLimit(int timeLimit) {
		this.mTimeLimit = timeLimit;
	}
	
	public int getTimeUsed() {
		return mTimeUsed;
	}

	public void setTimeUsed(int timeUsed) {
		this.mTimeUsed = timeUsed;
	}
	
	public long getLastOpen() {
		return mLastOpen;
	}

	public void setLastOpen(long lastOpen) {
		this.mLastOpen = lastOpen;
	}

	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
		sb.append(S_APP_NAME); sb.append(" VARCHAR(50) NOT NULL, ");
		sb.append(S_CAN_ACCESS); sb.append(" INTEGER NOT NULL, ");
		sb.append(S_TIME_LIMIT); sb.append(" INTEGER NOT NULL, ");
		sb.append(S_TIME_USED); sb.append(" INTEGER NOT NULL, ");
		sb.append(S_LAST_OPEN); sb.append(" UNSIGNED BIG INT NOT NULL,");
		sb.append(S_TIME_STAMP); sb.append(" VARCHAR(50)");
		sb.append(" )");
		return sb.toString();
	}
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(S_TABLE_NAME); sb.append( " (" );
		sb.append( S_APP_NAME ); 	sb.append(",");
		sb.append( S_CAN_ACCESS ); 	sb.append(",");
		sb.append( S_TIME_LIMIT );	sb.append(",");
		sb.append( S_TIME_USED );	sb.append(",");
		sb.append( S_LAST_OPEN );
		sb.append(") VALUES (");	sb.append("'");
		sb.append( getAppName() ); 	sb.append("','");
		sb.append( getCanAccess() ); sb.append("','");
		sb.append( getTimeLimit() ); sb.append("','");
		sb.append( getTimeUsed() ); sb.append("','");
		sb.append( getLastOpen() ); sb.append("'");
		sb.append( ")");
		return sb.toString();	
	}
	
	public String getUpdateSql()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_APP_NAME ); sb.append(" = '"); sb.append( getAppName() ); sb.append("',");
		sb.append( S_CAN_ACCESS ); sb.append(" = '"); sb.append( getCanAccess() ); sb.append("',");
		sb.append( S_TIME_LIMIT ); sb.append(" = '"); sb.append( getTimeLimit() ); sb.append("',");
		sb.append( S_TIME_USED ); sb.append(" = '"); sb.append( getTimeUsed() ); sb.append("',");
		sb.append( S_LAST_OPEN ); sb.append(" = '"); sb.append( getLastOpen() ); sb.append("'");
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = "); sb.append( getId() );

		return sb.toString();	
	}
	
	public String getUpdateTimeUsedSql()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_TIME_USED ); sb.append(" = '"); sb.append( getTimeUsed() ); sb.append("',");
		sb.append( S_LAST_OPEN ); sb.append(" = '"); sb.append( getLastOpen() ); sb.append("'");
		sb.append(" WHERE ");
		sb.append( S_ID ); sb.append(" = "); sb.append( getId() );

		return sb.toString();	
	}
	
	public static String getSelectSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
	
	public static String getDropTableSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
	public static String addColumnTimeStamp()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("ALTER TABLE "); sb.append(S_TABLE_NAME);
		sb.append(" ADD COLUMN "); sb.append(S_TIME_STAMP);
		sb.append(" VARCHAR(50)");
		return sb.toString();
	}
	
	public static boolean isTimeStampColumnExisted(SQLiteDatabase db){
		String sql = "SELECT " + S_TIME_STAMP + " FROM " + S_TABLE_NAME;
		try{
			db.rawQuery(sql, null);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	public static String getTimeStampSql(){
		String sql = "SELECT " + S_TIME_STAMP + " FROM " + S_TABLE_NAME;
		return sql;
	}

			
}