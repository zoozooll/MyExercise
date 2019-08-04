package com.oregonscientific.meep.database.table;

public class TableChatGroupMember {
	public static final String S_TABLE_NAME = "chatGroupMember";
	public static final String S_FRIEND_ID = "friendId";
	public static final String S_GROUP_ID = "groupId";

	private String mFriendId = null;
	private String mGroupId = null;


	public String getFriendId() {
		return mFriendId;
	}

	public void setName(String friendId) {
		this.mFriendId = friendId;
	}

	public String getGroupId() {
		return mGroupId;
	}

	public void setGroupId(String groupId) {
		this.mGroupId = groupId;
	}

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_FRIEND_ID);
		sb.append(" VARCHAR NOT NULL ,");
		sb.append(S_GROUP_ID);
		sb.append(" VARCHAR NOT NULL)");
		return sb.toString();
	}

//	public String getInsertSql() {
//		StringBuilder sb = new StringBuilder();
//		sb.append("INSERT INTO ");
//		sb.append(S_TABLE_NAME);
//		sb.append(" (");
//		sb.append(S_ID);
//		sb.append(",");
//		sb.append(S_NAME);
//		sb.append(",");
//		sb.append(S_ICON_ADDR);
//		sb.append(") VALUES ('");
//		sb.append(getId());
//		sb.append("','");
//		sb.append(getName());
//		sb.append("','");
//		sb.append(getIconAddr());
//		sb.append("')");
//		return sb.toString();
//	}

//	public String getUpdateSql() {
//		StringBuilder sb = new StringBuilder();
//
//		sb.append("UPDATE ");
//		sb.append(S_TABLE_NAME);
//		sb.append(" SET ");
//		sb.append(S_NAME);
//		sb.append(" = '");
//		sb.append(getName());
//		sb.append("',");
//		sb.append(S_ICON_ADDR);
//		sb.append(" = '");
//		sb.append(getIconAddr());
//		sb.append("',");
//		sb.append(" WHERE");
//		sb.append(S_ID);
//		sb.append(" = ");
//		sb.append(getId());
//
//		return sb.toString();
//	}

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

}
