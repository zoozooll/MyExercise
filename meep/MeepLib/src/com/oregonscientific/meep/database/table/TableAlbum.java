package com.oregonscientific.meep.database.table;

public class TableAlbum {

	public static final String S_TABLE_NAME = "album";
	public static final String S_ID = "id";
	public static final String S_CLIENT_ID = "clientId";
	public static final String S_NAME = "name";
	public static final String S_TYPE = "type";
	public static final String S_MUTABLE = "mutable";
	public static final String S_ICON_ADDR = "iconAddr";	
	public static final String S_RIGHT = "right";
	
	private int mId;
	private int mCLientId;
	private String mName;
	private String mType;
	private boolean mIsMutable;
	private String mIconAddr;
	private int mRight;
	
	public int getId() {
		return mId;
	}
	public void setId(int id) {
		this.mId = id;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		this.mName = name;
	}
	public int getRight() {
		return mRight;
	}
	public void setRight(int mRight) {
		this.mRight = mRight;
	}
	public String getType() {
		return mType;
	}
	public void setType(String type) {
		this.mType = type;
	}
	public int getCLientId() {
		return mCLientId;
	}
	public void setCLientId(int mCLientId) {
		this.mCLientId = mCLientId;
	}
	public boolean IsMutable() {
		return mIsMutable;
	}
	public void setMutable(Boolean mutable) {
		this.mIsMutable = mutable;
	}
	public String getIconAddr() {
		return mIconAddr;
	}
	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}

	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ID); sb.append( " INTEGER PRIMARY KEY AUTOINCREMENT  NOT NULL ,");
		sb.append( S_CLIENT_ID); sb.append(" INTEGER NOT NULL,");
		sb.append( S_NAME ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_TYPE ); sb.append(" INTEGER NOT NULL DEFAULT 0," );
		sb.append( S_MUTABLE ); sb.append(" BIT NOT NULL DEFAULT 0," );
		sb.append( S_ICON_ADDR ); sb.append(" VARCHAR,");
		sb.append( S_RIGHT); sb.append(" INTEGER )");
		return sb.toString();
	}
	
	public String getInsertSql()
	{
		int mutable = IsMutable()?1:0;
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(S_TABLE_NAME); sb.append( " (" );
		sb.append( S_CLIENT_ID ); sb.append(",");
		sb.append( S_NAME ); sb.append(",");
		sb.append( S_TYPE ); sb.append(",");
		sb.append( S_MUTABLE ); sb.append(",");
		sb.append( S_ICON_ADDR ); sb.append(",");
		sb.append( S_RIGHT ); sb.append(") VALUES (");
		sb.append( getCLientId() ); sb.append(",'");
		sb.append( getName() ); sb.append("','");
		sb.append( getType() ); sb.append("',");
		sb.append( mutable ); sb.append(",'");
		sb.append( getIconAddr() ); sb.append("',");
		sb.append( getRight()  ); sb.append( ")");
		return sb.toString();	
	}
	
	public String getUpdateSql()
	{
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
		sb.append( " SET " );
		sb.append( S_CLIENT_ID ); sb.append(" = "); sb.append( getCLientId() ); sb.append(",");
		sb.append( S_NAME ); sb.append(" = '"); sb.append( getName() ); sb.append("','");
		sb.append( S_TYPE ); sb.append(" = "); sb.append(getType() ); sb.append("',");
		sb.append( S_MUTABLE ); sb.append(" = '"); sb.append( IsMutable() ); sb.append("',");
		sb.append( S_ICON_ADDR ); sb.append(" = '"); sb.append( getIconAddr() ); sb.append("',");
		sb.append( S_RIGHT ); sb.append(" = "); sb.append( getRight() ); 
		sb.append(" WHERE");
		sb.append( S_ID ); sb.append(" = "); sb.append( getId() );

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
