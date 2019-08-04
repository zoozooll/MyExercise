package com.oregonscientific.meep.together.library.database.table;

public class AuthInfo {

	public static final String S_TABLE_NAME = "authinfo";
	public static final String S_ID = "id";
	public static final String S_EMAIL = "email";
	public static final String S_PASSWORD = "password";
	
	private String mId;
	private String email;
	private String password;
	
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getId() {
		return mId;
	}
	public void setId(String id) {
		this.mId = id;
	}
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " INTEGER PRIMARY KEY,");
		sb.append(S_EMAIL); sb.append(" VARCHAR,");
		sb.append(S_PASSWORD); sb.append(" VARCHAR");
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
		sb.append( S_EMAIL );   sb.append(",");
		sb.append( S_PASSWORD );
		sb.append(") VALUES (");    sb.append("'");
		sb.append( this.getEmail()); sb.append("','");
		sb.append( this.getPassword()); sb.append("'");
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
