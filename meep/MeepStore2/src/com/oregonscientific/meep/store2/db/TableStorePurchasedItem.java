package com.oregonscientific.meep.store2.db;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


public class TableStorePurchasedItem {
	public static final String S_TABLE_NAME = "StorePurchasedItem";
	public static final String S_ICON_URL = "iconUrl";
	public static final String S_NAME = "name";
	public static final String S_TYPE = "type";
	public static final String S_ID	= "id";

	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_NAME); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_TYPE); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_ICON_URL); sb.append("  VARCHAR NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public static String getInsertSql(String id, String name,  String itemType, String iconUrl){

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(",");
		sb.append(S_NAME);
		sb.append(",");
		sb.append(S_TYPE);
		sb.append(",");
		sb.append(S_ICON_URL);
		sb.append(") VALUES (");
		sb.append("'");  sb.append(id); sb.append("',");
		sb.append("'");  sb.append(name); sb.append("',");
		sb.append("'");  sb.append(itemType); sb.append("',");
		sb.append("'");  sb.append(iconUrl); sb.append("'");
		
		sb.append(")");
		return sb.toString();
	}
	
	public static String getSelectItemSql(){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT * FROM "); 
		sb.append(S_TABLE_NAME); 
		return sb.toString();
	}
	
	public static String getDeleteAllSql(){
		return "DELETE FROM " + S_TABLE_NAME;
	}
	
	public static boolean isCheckSumColumnExisted(SQLiteDatabase db){
		String sql = "SELECT " + S_ID + " FROM " + S_TABLE_NAME;
		try{
			db.rawQuery(sql, null);
			return true;
		}catch(Exception ex){
			return false;
		}
	}
	
	public static void dropTable(SQLiteDatabase db){
		String sql = "DROP TABLE " + S_TABLE_NAME;
		try{
			db.execSQL(sql);
		}catch(Exception e){
			Log.e(S_TABLE_NAME, e.toString());
		}
		
	}
}
