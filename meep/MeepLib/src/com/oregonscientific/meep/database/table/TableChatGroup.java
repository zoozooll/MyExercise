package com.oregonscientific.meep.database.table;

public class TableChatGroup {

	public static final String S_TABLE_NAME = "chatGroup";
	public static final String S_ID = "id";
	public static final String S_NAME = "name";
	public static final String S_ICON_ADDR = "iconAddr";

	private String mId;
	private String mName = null;
	private String mIconAddr = null;
	
	public TableChatGroup()
	{
		
	}
			
	

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

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(" VARCHAR PRIMARY KEY NOT NULL ,");
		sb.append(S_NAME);
		sb.append(" VARCHAR NOT NULL,");
		sb.append(S_ICON_ADDR);
		sb.append(" VARCHAR)");
		return sb.toString();
	}

	public String getInsertSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(",");
		sb.append(S_NAME);
		sb.append(",");
		sb.append(S_ICON_ADDR);
		sb.append(") VALUES ('");
		sb.append(getId());
		sb.append("','");
		sb.append(getName());
		sb.append("','");
		sb.append(getIconAddr());
		sb.append("')");
		return sb.toString();
	}

	public String getUpdateSql() {
		StringBuilder sb = new StringBuilder();

		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_NAME);
		sb.append(" = '");
		sb.append(getName());
		sb.append("',");
		sb.append(S_ICON_ADDR);
		sb.append(" = '");
		sb.append(getIconAddr());
		sb.append("',");
		sb.append(" WHERE");
		sb.append(S_ID);
		sb.append(" = ");
		sb.append(getId());

		return sb.toString();
	}

	public String getSelectSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);
		return sb.toString();
	}

	public static String getDropTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		sb.append(S_TABLE_NAME);
		return sb.toString();
	}

	public static String getDefaultGroupSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append('(');
		sb.append(S_NAME);
		sb.append(',');
		sb.append(S_ICON_ADDR);
		sb.append(')');
		sb.append(" VALUES ");
		sb.append("DEFAULT");
		sb.append(',');
		sb.append("/mnt/sdcard/profile/icon/default.png");

		return sb.toString();
	}

}
