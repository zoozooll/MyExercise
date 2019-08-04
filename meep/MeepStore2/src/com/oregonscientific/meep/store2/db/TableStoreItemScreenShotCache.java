package com.oregonscientific.meep.store2.db;


public class TableStoreItemScreenShotCache {
	public static final String S_TABLE_NAME = "StoreItemScreenShotCache";
	public static final String S_ITEM_ID = "ItemId";
	public static final String S_SCREENSHOT = "ScreenShot";
	public static final String S_ORDER_NUM = "OrderNum"; 
	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ITEM_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_ORDER_NUM); sb.append( " INTEGER NOT NULL ,");
		sb.append( S_SCREENSHOT); sb.append(" BLOB");
		sb.append(" )");
		return sb.toString();
	}
	
	
	public static String getInsertSql(String itemId, int orderNum){

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ITEM_ID);
		sb.append(",");
		sb.append(S_ORDER_NUM);
		sb.append(",");
		sb.append(S_SCREENSHOT);
		sb.append(") VALUES (");
		sb.append("'");sb.append(itemId); sb.append("',");
		sb.append(itemId);
		sb.append(",?");
		sb.append(")");
		return sb.toString();
	}
	
	public static String getSelectItemSql(String itemId){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT ");
		sb.append(S_SCREENSHOT);
		sb.append(" FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ITEM_ID ); sb.append(" = '"); sb.append( itemId ); sb.append("'");
		sb.append(" ORDER BY "); sb.append(S_ORDER_NUM);
		return sb.toString();
	}
}
