package com.oregonscientific.meep.store2.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TableStoreImageCache {
	public static final String S_TABLE_NAME = "StoreImageCache";
	public static final String S_ITEM_ID = "ItemId";
	public static final String S_ICON = "Icon";
	public static final String S_CREATE_DATE = "CreateDate";
	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ITEM_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_ICON); sb.append(" BLOB ,");
		sb.append(S_CREATE_DATE);sb.append(" DATETIME NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public static String getInsertSql(String itemId){

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ITEM_ID);
		sb.append(",");
		sb.append(S_CREATE_DATE);
		sb.append(",");
		sb.append(S_ICON);
		sb.append(") VALUES (");
		sb.append("'");sb.append(itemId);sb.append("',");
		sb.append("datetime()");
		sb.append(",?");
		sb.append(")");
		return sb.toString();
	}
	
	public static String getSelectItemSql(String itemId){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT ");
		sb.append(S_ICON);
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ITEM_ID ); sb.append(" = '"); sb.append( itemId ); sb.append("'");
		return sb.toString();
	}
	
	public static String getDelete30DayCacheSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("	DELETE ");
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_CREATE_DATE ); sb.append(" < date('now', '-30 days');"); 
		return sb.toString();
	}
	
	public static String getSelect30DayCacheSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT  ");
		sb.append( " * ");
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_CREATE_DATE ); sb.append(" < date('now', '-30 days');"); 
		return sb.toString();
	}
	public static String getSelect1DayCacheSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT  ");
		sb.append( " * ");
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_CREATE_DATE ); sb.append(" < date('now', '-1 days');"); 
		return sb.toString();
	}
	public static String getDelete1DayCacheSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("	DELETE ");
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_CREATE_DATE ); sb.append(" < date('now', '-1 days');"); 
		return sb.toString();
	}
	
	public static String getDeleteFirst10RowSql(){
		String sql ="delete from " + S_TABLE_NAME +
			     " where "+S_ITEM_ID+" in (select "+ S_ITEM_ID +" from "+ S_TABLE_NAME+ " LIMIT 10)";
		return sql;
	}
	public static String getDeleteFirst20RowSql(){
		String sql ="delete from " + S_TABLE_NAME +
				" where "+S_ITEM_ID+" in (select "+ S_ITEM_ID +" from "+ S_TABLE_NAME+ " LIMIT 20)";
		return sql;
	}
	public static String getDeleteFirst50RowSql(){
		String sql ="delete from " + S_TABLE_NAME +
				" where "+S_ITEM_ID+" in (select "+ S_ITEM_ID +" from "+ S_TABLE_NAME+ " LIMIT 50)";
		return sql;
	}
	public static String getDeleteFirstRowSql(){
		String sql ="delete from " + S_TABLE_NAME +
				" where "+S_ITEM_ID+" in (select "+ S_ITEM_ID +" from "+ S_TABLE_NAME+ " LIMIT 1)";
		return sql;
	}
	
	
	public static void clearRecords(SQLiteDatabase db){
		String sql = "SELECT Count(*) as count FROM " + S_TABLE_NAME;
		Cursor c = db.rawQuery(sql, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			int count = c.getInt(c.getColumnIndex("count"));
			Log.d("clearCacheRecord", "get total record:" + count);
			if (count > 300) {
				sql = getDeleteFirst50RowSql();
				db.execSQL(sql);
			}
		}
		
	}
	
	
	public static String getDelete1dayFirst5RowSql()
	{
		//delete from StoreImageCache where CreateDate in (select CreateDate from StoreImageCache order by CreateDate limit 2);
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(S_CREATE_DATE);
		sb.append(" IN (SELECT ");
		sb.append(S_CREATE_DATE);
		sb.append(" FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" ORDER BY ");
		sb.append(S_CREATE_DATE);
		sb.append(" LIMIT 5);");
		return sb.toString();
	}
	
}
