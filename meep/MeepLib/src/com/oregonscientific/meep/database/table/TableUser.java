package com.oregonscientific.meep.database.table;

public class TableUser {

	public static final String S_TABLE_NAME = "user";
	public static final String S_ID = "id";
	public static final String S_TOKEN = "token";
	public static final String S_NAME = "name";
	public static final String S_ICON_ADDR = "iconAddr";
	
	private String mId;
	private String mName;
	private String mToken;
	private String mIconAddr;
	

	public String getId() {
		return mId;
	}
	public void setId(String id) {
		this.mId = id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public String getIconAddr() {
		return mIconAddr;
	}
	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}
	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " VARCHAR PRIMARY KEY NOT NULL,");
		sb.append(S_NAME); sb.append(" VARCHAR NOT NULL,");
		sb.append(S_TOKEN); sb.append(" VARCHAR,");
		sb.append(S_ICON_ADDR); sb.append(" VARCHAR");
		sb.append(" )");
		return sb.toString();
	}
	
	public static String getDropTableSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
}
