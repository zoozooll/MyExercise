package com.oregonscientific.meep.database.table;


public class TableFriend {
	public static final String S_TABLE_NAME = "friend";
	public static final String S_ID = "id";
	public static final String S_GROUP_ID = "groupId";
	public static final String S_NAME = "name";
	public static final String S_ICON_ADDR = "iconAddr";
	public static final String S_ONLINE = "online";
	
	private String mId;
	private int mGroupId;
	private String mName;
	private String mIconAddr;
	private boolean mOnline;
	
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
	
	public int getGroupId() {
		return mGroupId;
	}

	public void setGroupId(int groupId) {
		this.mGroupId = groupId;
	}

	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ID); sb.append( " VARCHAR PRIMARY KEY NOT NULL ,");
		sb.append( S_GROUP_ID); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_NAME ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_ICON_ADDR ); sb.append(" VARCHAR,");
		sb.append( S_ONLINE ); sb.append(" INTEGER NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(TableFriend.S_TABLE_NAME); sb.append( " (" );
		sb.append( S_ID ); 			sb.append(",");
		sb.append( S_GROUP_ID ); 	sb.append(",");
		sb.append( S_NAME ); 		sb.append(",");
		sb.append( S_ICON_ADDR ); sb.append(",");
		sb.append( S_ONLINE );
		sb.append(") VALUES (");	sb.append("'");
		sb.append( getId() );		sb.append("',");
		sb.append( getGroupId() ); 	sb.append(",'");
		sb.append( getName() ); 	sb.append("','");
		sb.append( getIconAddr() ); sb.append("',");
		if (isOnline()) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		sb.append( ")");
		return sb.toString();	
	}
	
	public String getUpdateSql()
	{
		StringBuilder sb = new StringBuilder();
		int online = 0;
		if(isOnline())
		{
			online = 1;
		}
		
		sb.append("UPDATE "); sb.append(TableFriend.S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( TableFriend.S_GROUP_ID ); sb.append(" = "); sb.append( getGroupId() ); sb.append(",");
		sb.append( TableFriend.S_NAME ); sb.append(" = '"); sb.append( getName() ); sb.append("',");
		sb.append( TableFriend.S_ICON_ADDR ); sb.append(" = '"); sb.append( getIconAddr()); sb.append("', ");
		sb.append( TableFriend.S_ONLINE ); sb.append(" = ");sb.append(online);
		sb.append(" WHERE ");
		sb.append( TableFriend.S_ID ); sb.append(" = '"); sb.append( getId() );sb.append("' ");

		return sb.toString();	
	}
	
	public static String getUpdateOnlineStatusSql(String userId, boolean online)
	{
		StringBuilder sb = new StringBuilder();
		int onlineStatus = 0;
		if (online) {
			onlineStatus = 1;
		}
		sb.append("UPDATE "); sb.append(TableFriend.S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( TableFriend.S_ONLINE ); sb.append(" = ");sb.append(onlineStatus);
		sb.append(" WHERE ");
		sb.append( TableFriend.S_ID ); sb.append(" = '"); sb.append( userId );sb.append("' ");
		
		return sb.toString();
	}
	
	public String getAssignGroupSql()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE "); sb.append(TableFriend.S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( TableFriend.S_GROUP_ID ); sb.append(" = "); sb.append( getGroupId() );
		sb.append(" WHERE ");
		sb.append( TableFriend.S_ID ); sb.append(" = '"); sb.append( getId() );sb.append("' ");

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

	public String getDeleteSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME);
		sb.append(" WHERE "); sb.append(S_ID);
		sb.append("='");sb.append(getId());sb.append("'");
		return sb.toString();
	}
	public boolean isOnline() {
		return mOnline;
	}
	public void setOnline(boolean mOnline) {
		this.mOnline = mOnline;
	}

	
	public String getAssignGroupSQL(String groupId, String id)
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE "); sb.append(TableFriend.S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( TableFriend.S_GROUP_ID ); sb.append(" = ? ");
		sb.append(" WHERE ");
		sb.append( TableFriend.S_ID ); sb.append(" = ? ");

		return sb.toString();	
	}
	
	
	public String getDeleteSQL(String userId)
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME);
		sb.append(" WHERE "); sb.append(S_ID);
		sb.append("= ? ");
		return sb.toString();
	}
	
	public String getInsertSQL(String id, String groupId, String name, String iconAddress) {
	
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(TableFriend.S_TABLE_NAME); sb.append( " (" );
		sb.append( S_ID ); 			sb.append(",");
		sb.append( S_GROUP_ID ); 	sb.append(",");
		sb.append( S_NAME ); 		sb.append(",");
		sb.append( S_ICON_ADDR ); sb.append(",");
		sb.append( S_ONLINE );
		sb.append(") VALUES ( ?, ?, ? ,?,");
		
		if (isOnline()) {
			sb.append(1);
		} else {
			sb.append(0);
		}
		sb.append( ")");
		return sb.toString();	
	}
	
			
	public static String getUpdateOnlineStatusSQL(String userId, String online)
	{
		StringBuilder sb = new StringBuilder();

		sb.append("UPDATE "); sb.append(TableFriend.S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( TableFriend.S_ONLINE ); sb.append(" = ? ");
		sb.append(" WHERE ");
		sb.append( TableFriend.S_ID ); sb.append(" = ? ");
		
		return sb.toString();
	}
	
}