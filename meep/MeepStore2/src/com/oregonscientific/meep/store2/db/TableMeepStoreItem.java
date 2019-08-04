package com.oregonscientific.meep.store2.db;

import android.content.ContentValues;

public class TableMeepStoreItem {
	public static final String S_TABLE_NAME = "MeepStoreItem";
	public static final String S_ITEM_ID = "ItemId";
	public static final String S_NAME = "Name";
	public static final String S_ICON = "Icon";
	public static final String S_ITEM_TYPE = "ItemType";
	public static final String S_URL = "Url";
	public static final String S_ITEM_ACTION = "ItemAction";
	public static final String S_LOCAL_PATH = "LocalPath";
	public static final String S_DOWNLOAD_STATE = "DownloadState";
	public static final String S_PRICE = "Price";
	public static final String S_CURRENCY = "Currency";
	public static final String S_PROMOTION_STATUS = "PromotionStatus";
	
	String mItemId;
	String mName;
	ContentValues mIcon;
	String mItemType;
	String mItemAction;
	String mUrl;
	String mLocalPath;
	String mDownloadState;
	int mPrice;
	String mCurrency;
	String mPromotionStatus; 
	
	public TableMeepStoreItem(String itemId, String name, ContentValues icon, String itemType, 
			String itemAction, String url, String localPath, String downloadState, 
			int price, String currency, String promotionStatus){
		mItemId = itemId;
		mName = name;
		mIcon = icon;
		mItemType = itemType;
		mItemAction = itemAction;
		mUrl = url;
		mLocalPath = localPath;
		mDownloadState = downloadState;
		mPrice = price;
		mCurrency = currency;
		mPromotionStatus = promotionStatus;
	}
	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ITEM_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_NAME); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_ICON); sb.append(" BLOB,");
		sb.append( S_ITEM_TYPE ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_ITEM_ACTION ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_URL ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_LOCAL_PATH ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_DOWNLOAD_STATE ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_PRICE ); sb.append(" INTEGER NOT NULL,");
		sb.append( S_CURRENCY ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_PROMOTION_STATUS ); sb.append(" VARCHAR NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public String getInsertSql(String itemId, String name, String itemType, 
			String itemAction, String url, String localPath, String downloadState, 
			int price, String currency, String promotionStatus){

		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ITEM_ID);
		sb.append(",");
		sb.append(S_NAME);
		sb.append(",");
		sb.append(S_ICON);
		sb.append(",");
		sb.append(S_ITEM_TYPE);
		sb.append(",");
		sb.append(S_URL);
		sb.append(",");
		sb.append(S_LOCAL_PATH);
		sb.append(",");
		sb.append(S_DOWNLOAD_STATE);
		sb.append(",");
		sb.append(S_ITEM_TYPE);
		sb.append(",");
		sb.append(S_PRICE);
		sb.append(",");
		sb.append(S_PROMOTION_STATUS);
		sb.append(") VALUES (");
		sb.append("?,?,?,?,?,?,?,?,?,?");
		sb.append(")");
		return sb.toString();
	}
	
	public String getUpdateIconSql(String itemId, ContentValues icon){
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_ICON ); sb.append(" = ?");
		sb.append(" WHERE ");
		sb.append( S_ITEM_ID ); sb.append(" = '"); sb.append( itemId ); sb.append("'");
		return sb.toString();
	}
	
	public String getSelectItemSql(String itemId){
		StringBuilder sb = new StringBuilder();
		sb.append("	SELECT * FROM "); 
		sb.append(S_TABLE_NAME); 
		sb.append(" WHERE ");
		sb.append( S_ITEM_ID ); sb.append(" = '"); sb.append( itemId ); sb.append("'");
		return sb.toString();
	}
	
	
}
