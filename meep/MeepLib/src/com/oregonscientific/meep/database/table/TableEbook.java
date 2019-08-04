package com.oregonscientific.meep.database.table;

import com.oregonscientific.meep.global.object.EncodingBase64;

public class TableEbook {

	public static final String S_TABLE_NAME = "ebook";
	public static final String S_ID = "id";
	public static final String S_NAME = "name";
	public static final String S_FILE_PATH = "filePath";
	public static final String S_ICON_PATH = "iconAddr";
	
	public String id;
	public String name;
	public String filePath;
	public String iconAddr;
	
	public static String getTableCreateSql(){
		
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ID); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_NAME); sb.append( " VARCHAR NOT NULL ,");
		sb.append( S_FILE_PATH); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_ICON_PATH); sb.append(" VARCHAR NOT NULL ");
		sb.append(" )");
		return sb.toString();
	}
	
	public static String getInsertSql(String id, String name, String filePath, String iconPath){
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(",");
		sb.append(S_NAME);
		sb.append(",");
		sb.append(S_FILE_PATH);
		sb.append(",");
		sb.append(S_ICON_PATH);
		sb.append(") VALUES ('");
		sb.append(id);
		sb.append("','");
		sb.append(EncodingBase64.encode(name));
		sb.append("','");
		sb.append(filePath);
		sb.append("','");
		sb.append(iconPath);
		sb.append("')");
		return sb.toString();
	}
	
	
	public static String getSelectAllEbookSql(){
		return "SELECT * from " + S_TABLE_NAME;
	}
	
	public static String getDeleteSql(String id){
		
			return "DELETE from " + S_TABLE_NAME + " WHERE " + S_ID + " = '"+id+"'";
	}
	
}
