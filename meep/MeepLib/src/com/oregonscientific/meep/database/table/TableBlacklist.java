package com.oregonscientific.meep.database.table;

import java.util.ArrayList;
import java.util.List;

public class TableBlacklist {
	public static final String S_TABLE_NAME = "blacklist";
	public static final String S_ID = "id";
	public static final String S_LIST_TYPE = "listType";
	public static final String S_LIST_ENTRY = "listEntry";
	
	private int mId;
	private String mListType;
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

    public static String getCreateSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS "); sb.append(S_TABLE_NAME); sb.append(" (");
		sb.append(S_ID); sb.append( " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, ");
		sb.append(S_LIST_TYPE); sb.append(" VARCHAR(50) NOT NULL, ");
        sb.append(S_LIST_ENTRY); sb.append(" VARCHAR(50) NOT NULL");
		sb.append(" )");
		return sb.toString();
	}
	
	public String getDeleteSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM "); sb.append(S_TABLE_NAME); sb.append( " WHERE " );
		sb.append( S_LIST_TYPE ); sb.append("='");
        sb.append( this.getListType() ); sb.append("'");
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
			sb.append(S_LIST_ENTRY);
			sb.append(") VALUES (");
			sb.append("'");
			sb.append(this.getListType());
			sb.append("','");
			sb.append(entry);
			sb.append("'");
			sb.append(")");
			sql.add(sb.toString());
		}

		return sql;
	}
	
	/**
	 * Returns the SQL statement that creates an entry in the blacklist
	 * 
	 * @param type The type of the entry
	 * @param entry The content of the entry
	 * @return The SQL statement
	 */
	public static String getCreateSql(String type, String entry) {
		if (type == null || entry == null) {
			return null;
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_LIST_TYPE);
		sb.append(", ");
		sb.append(S_LIST_ENTRY);
		sb.append(") VALUES (?, ?)");
		
		return sb.toString();
	}

	public static String getDropTableSql()
	{
		StringBuilder sb = new StringBuilder();
		sb.append("DROP TABLE "); sb.append(S_TABLE_NAME);
		return sb.toString();
	}
}