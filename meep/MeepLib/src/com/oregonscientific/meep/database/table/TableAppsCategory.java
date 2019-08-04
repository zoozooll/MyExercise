package com.oregonscientific.meep.database.table;

public class TableAppsCategory {
	public static final String S_TABLE_NAME = "appsCategory";
	public static final String S_APP_ID = "appId";
	public static final String S_CATEGORY = "category";
	
	private String mAppId;
	private String mCategory;

    public String getAppId() {
        return mAppId;
    }

    public void setAppId(String mAppId) {
        this.mAppId = mAppId;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String mCategory) {
        this.mCategory = mCategory;
    }

    public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_APP_ID); sb.append(" VARCHAR(100) PRIMARY KEY NOT NULL, ");
        sb.append(S_CATEGORY); sb.append(" VARCHAR(50) NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT OR REPLACE INTO "); sb.append(S_TABLE_NAME); sb.append( " (" );
		sb.append( S_APP_ID ); 	sb.append(",");
		sb.append( S_CATEGORY );
		sb.append(") VALUES (");	sb.append("'");
		sb.append( this.getAppId() ); sb.append("','");
		sb.append( this.getCategory() ); sb.append("'");
		sb.append( ")");
		return sb.toString();	
	}
	
	public String getSelectSql()
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
}