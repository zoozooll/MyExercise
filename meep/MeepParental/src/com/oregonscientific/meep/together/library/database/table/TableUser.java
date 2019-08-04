package com.oregonscientific.meep.together.library.database.table;

public class TableUser {

	public static final String S_TABLE_NAME = "user";
	public static final String S_ID = "id";
	public static final String S_FIRST_NAME = "firstName";
	public static final String S_LAST_NAME = "lastName";
	public static final String S_TOKEN = "token";
	public static final String S_ICON_ADDR = "iconAddr";
	public static final String S_COINS = "coins";
	public static final String S_LAST_KID = "lastKid";
	
	private String mId;
	private String mFirstName;
	private String mLastName;
	private String mToken;
	private String mIconAddr;
	private String mCoins;
	private String mLastKid;
	

	public String getLastKid() {
		return mLastKid;
	}
	public void setmLastKid(String lastKid) {
		this.mLastKid = lastKid;
	}
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		this.mId = id;
	}
	public String getFirstName() {
		return mFirstName;
	}
	public void setFirstName(String firstName) {
		this.mFirstName = firstName;
	}
	public String getLastName() {
		return mLastName;
	}
	public void setLastName(String lastName) {
		this.mLastName = lastName;
	}
	public String getToken() {
		return mToken;
	}
	public void setToken(String token) {
		this.mToken = token;
	}
	public String getIconAddr() {
		return mIconAddr;
	}
	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}
	public String getCoins() {
		return mCoins;
	}
	public void setCoins(String coins) {
		this.mCoins = coins;
	}
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " INTEGER PRIMARY KEY,");
		sb.append(S_FIRST_NAME); sb.append(" VARCHAR,");
		sb.append(S_LAST_NAME); sb.append(" VARCHAR,");
		sb.append(S_TOKEN); sb.append(" VARCHAR,");
		sb.append(S_ICON_ADDR); sb.append(" VARCHAR,");
		sb.append(S_COINS); sb.append(" VARCHAR,");
		sb.append(S_LAST_KID); sb.append(" VARCHAR");
		sb.append(" )");
		return sb.toString();
	}
	
	public static String getDropTableSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
	
	public static String getDeleteSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME);
		return sb.toString();
		
	}
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(S_TABLE_NAME); sb.append( " (" );
		sb.append( S_FIRST_NAME );   sb.append(",");
		sb.append( S_LAST_NAME);   sb.append(",");
		sb.append( S_TOKEN );   sb.append(",");
		sb.append( S_ICON_ADDR );sb.append(",");
		sb.append( S_COINS );
		sb.append(") VALUES (");    sb.append("'");
		sb.append( this.getFirstName() ); sb.append("','");
		sb.append( this.getLastName()); sb.append("','");
		sb.append( this.getToken()); sb.append("','");
		sb.append( this.getIconAddr()); sb.append("','");
		sb.append( this.getCoins()); sb.append("'");
		sb.append( ")");
		
		return sb.toString(); 
	}
	    
    public static String getSelectSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
}
