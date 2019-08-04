package com.oregonscientific.meep.database.table;

import java.util.Date;

import android.text.format.DateFormat;

public class TableChat {
	public static final String S_TABLE_NAME = "chat";
	public static final String S_ID = "id";
	public static final String S_GROUP_ID = "groupId";
	public static final String S_FRIEND_ID = "name";
	public static final String S_IS_MSG_IN = "isMsgIn";
	public static final String S_TIME = "time";
	public static final String S_CONETENT = "content";
	public static final String S_CONTENT_TYPE = "content_type";

	private int mId;
	private int mGroupId;
	private String mFriendId;
	private String mContent;
	private ContentType mContentType;
	private Date mTime;
	private boolean mIsMsgIn;

	public enum ContentType {
		TEXT, URL
	}

	public TableChat() {
		setId(0);
		setGroupId(0);
		setFriendId(null);
		setContent(null);
		setContentType(ContentType.TEXT);
		setTime(null);
		setMsgIn(false);
	}

	public TableChat(int id, int groupId, String friendId, String content, ContentType type,
			Date time, Boolean isMsgIn) {
		setId(id);
		setGroupId(groupId);
		setFriendId(friendId);
		setContent(content);
		setContentType(type);
		setTime(time);
		setMsgIn(isMsgIn);
	}

	public int getId() {
		return mId;
	}

	public void setId(int id) {
		this.mId = id;
	}

	public int getGroupId() {
		return mGroupId;
	}

	public void setGroupId(int groupId) {
		this.mGroupId = groupId;
	}

	public String getFriendId() {
		return mFriendId;
	}

	public void setFriendId(String friendId) {
		this.mFriendId = friendId;
	}

	public String getContent() {
		return mContent;
	}

	public void setContent(String content) {
		this.mContent = content;
	}

	public ContentType getContentType() {
		return mContentType;
	}

	public void setContentType(ContentType contentType) {
		this.mContentType = contentType;
	}

	public Date getTime() {
		return mTime;
	}

	public void setTime(Date time) {
		this.mTime = time;
	}

	public boolean IsMsgIn() {
		return mIsMsgIn;
	}

	public void setMsgIn(boolean isMsgIn) {
		this.mIsMsgIn = isMsgIn;
	}

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(" INTEGER PRIMARY KEY NOT NULL ,");
		sb.append(S_GROUP_ID);
		sb.append(" INTEGER NOT NULL ,");
		sb.append(S_FRIEND_ID);
		sb.append(" VARCHAR NOT NULL ,");
		sb.append(S_CONETENT);
		sb.append(" VARCHAR NOT NULL ,");
		sb.append(S_CONTENT_TYPE);
		sb.append(" INTEGER NOT NULL ,");
		sb.append(S_TIME);
		sb.append(" DATE NOT NULL ,");
		sb.append(S_IS_MSG_IN);
		sb.append(" BIT NOT NULL)");
		return sb.toString();
	}

	public String getInsertSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_GROUP_ID);
		sb.append(", ");
		sb.append(S_FRIEND_ID);
		sb.append(", ");
		sb.append(S_CONETENT);
		sb.append(", ");
		sb.append(S_CONTENT_TYPE);
		sb.append(", ");
		sb.append(S_TIME);
		sb.append(", ");
		sb.append(S_IS_MSG_IN);
		sb.append(") VALUES (");
		sb.append(mGroupId);
		sb.append(", ");
		sb.append("'");
		sb.append(mFriendId);
		sb.append("'");
		sb.append(",");
		sb.append("'");
		sb.append(mContent);
		sb.append("'");
		sb.append(", ");
		sb.append(mContentType == ContentType.TEXT ? 0 : 1);
		sb.append(", ");
		sb.append("datetime()");
		sb.append(", ");
		sb.append(mIsMsgIn ? 1 : 0);
		sb.append(")");

		return sb.toString();
	}

	/**
	 * Returns a INSERT SQL statement
	 * 
	 * @param sender The sender of the message
	 * @param content The content of the message
	 * @return The SQL statement
	 */
	public String getInsertSQL(String sender, String content) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_GROUP_ID);
		sb.append(", ");
		sb.append(S_FRIEND_ID);
		sb.append(", ");
		sb.append(S_CONETENT);
		sb.append(", ");
		sb.append(S_CONTENT_TYPE);
		sb.append(", ");
		sb.append(S_TIME);
		sb.append(", ");
		sb.append(S_IS_MSG_IN);
		sb.append(") VALUES (");
		sb.append(mGroupId);
		sb.append(", ?, ?, ");
		sb.append(mContentType == ContentType.TEXT ? 0 : 1);
		sb.append(", ");
		sb.append("datetime()");
		sb.append(", ");
		sb.append(mIsMsgIn ? 1 : 0);
		sb.append(")");

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

}
