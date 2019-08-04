package com.oregonscientific.meep.database.table;


public class TableMovie {
	public static final String S_TABLE_NAME = "movie";
	public static final String S_ID = "id";
	public static final String S_NAME = "name";
	public static final String S_DESCRIPTION = "desc";
	public static final String S_TYPE = "type";
	public static final String S_ICON_ADDR = "iconAddr";
	public static final String S_DATA_ADDR = "dataAddr";
	public static final String S_METADATA_ADDR = "metaDataAddr";
	public static final String S_RIGHT = "right";
	
	private String mId;
	private String mName;
	private String mDesc;
	private String mType;
	private String mIconAddr;
	private String mDataAddr;
	private String mMetaDataAddr;
	private int mRight;
	
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
	public String getDesciption() {
		return mDesc;
	}
	public void setDesciption(String desc) {
		this.mDesc = desc;
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
	public String getDataAddr() {
		return mDataAddr;
	}
	public void setDataAddr(String dataAddr) {
		this.mDataAddr = dataAddr;
	}
	public String getMetaDataAddr() {
		return mMetaDataAddr;
	}
	public void setThumbnailAddr(String metaDataAddr) {
		this.mMetaDataAddr = metaDataAddr;
	}

	
	public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append( "CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append( S_ID); sb.append( " VARCHAR PRIMARY KEY NOT NULL ,");
		sb.append( S_NAME ); sb.append(" VARCHAR NOT NULL,");
		sb.append( S_TYPE ); sb.append(" INTEGER NOT NULL DEFAULT 0," );
		sb.append( S_DESCRIPTION); sb.append(" VARCHAR,");
		sb.append( S_ICON_ADDR ); sb.append(" VARCHAR,");
		sb.append( S_DATA_ADDR ); sb.append(" VARCHAR,");
		sb.append( S_METADATA_ADDR ); sb.append(" VARCHAR,");
		sb.append( S_RIGHT); sb.append(" INTEGER )");
		return sb.toString();
	}
	
	public String getInsertSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO "); sb.append(S_TABLE_NAME); sb.append( " (" );
		sb.append( S_NAME ); sb.append(",");
		sb.append( S_TYPE ); sb.append(",");
		sb.append( S_DESCRIPTION ); sb.append(",");
		sb.append( S_ICON_ADDR ); sb.append(",");
		sb.append( S_DATA_ADDR); 
		sb.append(") VALUES (");
		sb.append( getName() ); sb.append("','");
		sb.append( getType() ); sb.append("','");
		sb.append( getDesciption()  ); sb.append("','");
		sb.append( getIconAddr() ); sb.append("','");
		sb.append( getDataAddr() ); sb.append("')");
		return sb.toString();	
	}
	
//	public String getUpdateSql()
//	{
//		StringBuilder sb = new StringBuilder();
//		
//		sb.append("UPDATE "); sb.append(S_TABLE_NAME); 
//		sb.append( " SET " );
//		sb.append( S_ALBUM_ID ); sb.append(" = "); sb.append( getAlbumId() ); sb.append(",");
//		sb.append( S_NAME ); sb.append(" = '"); sb.append( getName() ); sb.append("',");
//		sb.append( S_TYPE ); sb.append(" = "); sb.append(getType() ); sb.append(",");
//		sb.append( S_TOKEN ); sb.append(" = '"); sb.append( getToken() ); sb.append("',");
//		sb.append( S_DESCRIPTION ); sb.append(" = '"); sb.append( getDesciption() ); sb.append("',");
//		sb.append( S_ICON_ADDR ); sb.append(" = '"); sb.append( getIconAddr() ); sb.append("',");
//		sb.append( S_DATA_ADDR ); sb.append(" = '"); sb.append( getDataAddr() ); sb.append("',");
//		sb.append( S_THUMBNAIL_ADDR ); sb.append(" = '"); sb.append( getThumbnailAddr() ); sb.append("',");
//		sb.append( S_EXTRA_CONTENT ); sb.append(" = '"); sb.append( getExtraContent() ); sb.append("',");
//		sb.append( S_RIGHT ); sb.append(" = "); sb.append( getRight() ); 
//		sb.append(" WHERE");
//		sb.append( S_ID ); sb.append(" = "); sb.append( getId() );
//
//		return sb.toString();	
//	}
	
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
