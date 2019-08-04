package com.oregonscientific.meep.database.table;

import java.util.ArrayList;
import java.util.List;

public class TableRecommendation {
	public static final String S_TABLE_NAME = "recommendations";
	public static final String S_ID = "id";
	public static final String S_LIST_TYPE = "listType";
	public static final String S_THUMBNAIL = "thumbnail";
	public static final String S_LIST_ENTRY = "listEntry";
	public static final String S_YOUTUBE_TYPE_OS = "os-youtube";
	public static final String S_YOUTUBE_TYPE_PARENT = "youtube";

	private int mId;
	private String mListType;
	private String mThumbnail;
	private String[] mListEntry;

	public int getId() {
		return mId;
	}

	public void setId(int mId) {
		this.mId = mId;
	}

	public String getListType() {
		return mListType;
	}

	public void setListType(String mListType) {
		this.mListType = mListType;
	}

	public String[] getListEntry() {
		return mListEntry;
	}

	public void setListEntry(String[] mListEntry) {
		this.mListEntry = mListEntry;
	}

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(" INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
		sb.append(S_LIST_TYPE);
		sb.append(" VARCHAR(50) NOT NULL, ");
		sb.append(S_THUMBNAIL);
		sb.append(" VARCHAR(255) NOT NULL, ");
		sb.append(S_LIST_ENTRY);
		sb.append(" VARCHAR(50) NOT NULL");
		sb.append(" )");
		return sb.toString();
	}

	public String getDeleteSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(S_LIST_TYPE);
		sb.append("='");
		sb.append(this.getListType());
		sb.append("'");
		return sb.toString();
	}

	public List<String> getInsertSql() {

		List<String> sql = new ArrayList<String>();

		for (String entry : this.getListEntry()) {
			StringBuilder sb = new StringBuilder();
			sb.append("INSERT INTO ");
			sb.append(S_TABLE_NAME);
			sb.append(" (");
			sb.append(S_LIST_TYPE);
			sb.append(",");
			sb.append(S_THUMBNAIL);
			sb.append(",");
			sb.append(S_LIST_ENTRY);
			sb.append(") VALUES (");
			sb.append("'");
			sb.append(this.getListType());
			sb.append("','");
			sb.append(this.getThumbnail());
			sb.append("','");
			sb.append(entry);
			sb.append("'");
			sb.append(")");
			sql.add(sb.toString());
		}

		return sql;
	}

	/**
	 * Returns the SQL statement that inserts an entry into the recommendation
	 * table
	 * 
	 * @param type
	 *          The type of the recommendation
	 * @param thumbnail
	 *          The thumbnail of the recommendation
	 * @param entry
	 *          The recommendation entry
	 * @return The SQL statement
	 */
	public static String getInsertSql(String type, String thumbnail, String entry) {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_LIST_TYPE);
		sb.append(",");
		sb.append(S_THUMBNAIL);
		sb.append(",");
		sb.append(S_LIST_ENTRY);
		sb.append(") VALUES (?, ?, ?)");
		return sb.toString();
	}

	public String getSelectSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);
		return sb.toString();
	}

	public String getSelectYoutubeSql(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(S_LIST_TYPE);
		sb.append("= '");
		sb.append(type);
		sb.append("'");
		return sb.toString();
	}
	
	public static String getSelectSql(String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);
		
		if (type != null) {
			sb.append(" WHERE ");
			sb.append(S_LIST_TYPE);
			sb.append(" = ?");
		}
		
		return sb.toString();
	}

	public static String getDropTableSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE ");
		sb.append(S_TABLE_NAME);
		return sb.toString();
	}

	public String getThumbnail() {
		return mThumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.mThumbnail = thumbnail;
	}

	// public String getSelectItemByEntry(String entry)
	// {
	// StringBuilder sb = new StringBuilder();
	// sb.append("SELECT * FROM "); sb.append(S_TABLE_NAME);
	//
	// return sb.toString();
	// }
}