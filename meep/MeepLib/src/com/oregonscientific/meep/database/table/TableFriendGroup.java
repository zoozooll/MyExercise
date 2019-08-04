package com.oregonscientific.meep.database.table;

import java.util.ArrayList;
import java.util.List;

import com.oregonscientific.meep.global.Global;

import android.R;
import android.os.Environment;

public class TableFriendGroup {
	public static final String S_TABLE_NAME = "friendGroup";
	public static final String S_ID = "id";
	public static final String S_NAME = "name";
	public static final String S_ICON_ADDR = "iconAddr";
	//extra field
	public static final String S_CHILD_COUNT = "count";

	private int mId;
	private String mName = null;
	private String mIconAddr = null;
	private int mChildCount;

	/**
	 * An enumeration of possible group names
	 */
	public enum FriendGroupName {
	
		GROUP_PARENTS ("parents"),
		GROUP_BUDDY ("buddy");
	
		
		private String groupName = null;
		
		FriendGroupName (String groupName) {
			
			this.groupName = groupName;
			
		}
		
		public String toString() {
			
			return groupName;
			
		}
		
	}
	
	
	public TableFriendGroup() {

	}

	public TableFriendGroup(int id, String name, String iconAddr, int childCount) {
		setId(id);
		setName(name);
		setIconAddr(iconAddr);
		setChildCount(childCount);
	}

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

	public String getIconAddr() {
		return mIconAddr;
	}

	public void setIconAddr(String iconAddr) {
		this.mIconAddr = iconAddr;
	}
	
	public int getChildCount() {
		return mChildCount;
	}

	public void setChildCount(int childCount) {
		this.mChildCount = childCount;
	}

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_ID);
		sb.append(" INTEGER PRIMARY KEY,");
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
		sb.append(S_NAME);
		sb.append(",");
		sb.append(S_ICON_ADDR);
		sb.append(") VALUES ('");
		sb.append(getName());
		sb.append("','");
		sb.append(getIconAddr());
		sb.append("')");
		return sb.toString();
	}
	
	/**
	 * Returns a INSERT SQL statement
	 * 
	 * @param groupName The name of the group to insert
	 * @param iconAddress The address of the icon file
	 * @return The SQL statement
	 */
	public static String getInsertSQL(String name, String iconAddress) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_NAME);
		sb.append(", ");
		sb.append(S_ICON_ADDR);
		sb.append(") VALUES (?, ?)");

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
	
	/**
	 * Returns a SELECT SQL statement
	 * 
	 * @param groupName The name of the group to select
	 * @return The SQL statement
	 */
	public static String getSelectSQL(String name) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);
		
		if (name != null) {
			sb.append(" WHERE ");
			sb.append(TableFriend.S_NAME);
			sb.append(" = ?");
		}
		
		return sb.toString();
	}
	
	public String getDeleteSql() {
		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(TableFriend.S_NAME);
		sb.append(" = '");
		sb.append(getName());
		sb.append("' ");

		return sb.toString();
	}
	
	public static String getDeleteSql(String groupName) {
		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(TableFriend.S_NAME);
		sb.append(" = '");
		sb.append(groupName);
		sb.append("' ");

		return sb.toString();
	}
	
	/**
	 * Returns a DELETE SQL statement
	 * 
	 * @param groupName The name of the group to delete
	 * @return The SQL statement
	 */
	public static String getDeleteSQL(String groupName) {
		StringBuilder sb = new StringBuilder();

		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		
		if (groupName != null) {
			sb.append(" WHERE ");
			sb.append(TableFriend.S_NAME);
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

	public static List<String> getInsertDefaultGroupSql(String school, String  buddies , String family, String addGroup, String addFriend) {
		List<String> sqls = new ArrayList<String>();
		
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append('(');
		sb.append(S_NAME);
		sb.append(',');
		sb.append(S_ICON_ADDR);
		sb.append(')');
		sb.append(" VALUES ");
		sb.append('(');
		sb.append("'");
		sb.append(school);
		sb.append("'");
		sb.append(',');
		sb.append("'");
		sb.append(Global.GROUP_CLASSMATE);
		sb.append("'");
		sb.append(')');
		sqls.add(sb.toString());

//		sb = new StringBuilder();
//		sb.append("INSERT INTO ");
//		sb.append(S_TABLE_NAME);
//		sb.append('(');
//		sb.append(S_NAME);
//		sb.append(',');
//		sb.append(S_ICON_ADDR);
//		sb.append(')');
//		sb.append(" VALUES ");
//		sb.append('(');
//		sb.append("'");
//		sb.append(buddies);
//		sb.append("'");
//		sb.append(',');
//		sb.append("'");
//		sb.append(Global.GROUP_BUDDY);
//		sb.append("'");
//		sb.append(')');
//		sqls.add(sb.toString());

//		sb = new StringBuilder();
//		sb.append("INSERT INTO ");
//		sb.append(S_TABLE_NAME);
//		sb.append('(');
//		sb.append(S_NAME);
//		sb.append(',');
//		sb.append(S_ICON_ADDR);
//		sb.append(')');
//		sb.append(" VALUES ");
//		sb.append('(');
//		sb.append("'");
//		sb.append(family);
//		sb.append("'");
//		sb.append(',');
//		sb.append("'");
//		sb.append(Global.GROUP_FAMILY);
//		sb.append("'");
//		sb.append(')');
//		sqls.add(sb.toString());

		sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append('(');
		sb.append(S_NAME);
		sb.append(',');
		sb.append(S_ICON_ADDR);
		sb.append(')');
		sb.append(" VALUES ");
		sb.append('(');
		sb.append("'");
		sb.append(addGroup);
		sb.append("'");
		sb.append(',');
		sb.append("'");
		sb.append(Global.GROUP_Add);
		sb.append("'");
		sb.append(')');
		sqls.add(sb.toString());
		
		sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append('(');
		sb.append(S_NAME);
		sb.append(',');
		sb.append(S_ICON_ADDR);
		sb.append(')');
		sb.append(" VALUES ");
		sb.append('(');
		sb.append("'");
		sb.append(addFriend);
		sb.append("'");
		sb.append(',');
		sb.append("'");
		sb.append(Global.GROUP_ADD_FRIEND);
		sb.append("'");
		sb.append(')');
		sqls.add(sb.toString());

		return sqls;
	}



}
