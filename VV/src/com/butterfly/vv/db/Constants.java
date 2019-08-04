package com.butterfly.vv.db;

public class Constants {
	public static final String DB_NAME = "vvim.db";
	public static final int DB_VERSION = 1;
	public static final String TABLE_PROVICE = "provice";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_PCODE = "pcode";
	public static final String COLUMN_PNAME = "pname";
	public static final String TABLE_PROVICE_CREATE = "create table "
			+ TABLE_PROVICE + "(" + COLUMN_ID + " integer(10) DEFAULT 1, "
			+ COLUMN_PCODE + "  text primary key ," + COLUMN_PNAME
			+ "  text );";
	public static final String TABLE_CITY = "city";
	public static final String COLUMN_CCODE = "ccode";
	public static final String COLUMN_CPCODE = "cpcode";
	public static final String COLUMN_CNAME = "cname";
	public static final String TABLE_CITY_CREATE = "create table " + TABLE_CITY
			+ "(" + COLUMN_ID + " integer(10) DEFAULT 1, " + COLUMN_CCODE
			+ " text primary key ," + COLUMN_CPCODE + " text," + COLUMN_CNAME
			+ " text );";
	public static final String TABLE_UNIVERSITY = "university";
	public static final String COLUMN_UCODE = "ucode";
	public static final String COLUMN_UNAME = "uname";
	public static final String TABLE_UNIVERSITY_CREATE = "create table "
			+ TABLE_UNIVERSITY + "(" + COLUMN_ID + " integer(10) DEFAULT 1, "
			+ COLUMN_UCODE + " text primary key ," + COLUMN_UNAME + " text );";
	public static final String TABLE_SESSION = "imsession";
	public static final String COLUMN_SESSIONID = "_id";// session ID
	public static final String COLUMN_SESSIONNAME = "s_name";
	public static final String COLUMN_SESSIONDATE = "s_date";
	public static final String COLUMN_SESSIONWHOSEND = "s_whosend";
	public static final String COLUMN_SESSIONMSGBODY = "s_msgbody";
	public static final String COLUMN_SESSIONMSG_SID = "s_id";
	public static final String COLUMN_SESSIONMSG_TYPE = "s_type";
	public static final String TABLE_SESSION_CREATE = "create table "
			+ TABLE_SESSION + "(" + COLUMN_SESSIONID
			+ " integer primary key  autoincrement," + COLUMN_SESSIONNAME
			+ " text ," + COLUMN_SESSIONDATE + " text," + COLUMN_SESSIONWHOSEND
			+ " text," + COLUMN_SESSIONMSGBODY + " text,"
			+ COLUMN_SESSIONMSG_TYPE + " integer ," + COLUMN_SESSIONMSG_SID
			+ " integer "// foreign key(s_id) references
							// session(s_id)"
			+ ");";
	public static final String TABLE_MYSESSION = "session";
	public static final String COLUMN_MYSESSIONID = "s_id";// session ID
	public static final String COLUMN_MYSESSION_CHATWITH = "s_chatwith";
	public static final String COLUMN_MYSESSION_OID = "o_id";
	public static final String TABLE_MYSESSION_CREATE = "create table "
			+ TABLE_MYSESSION + "(" + COLUMN_MYSESSIONID
			+ " integer primary key  autoincrement,"
			+ COLUMN_MYSESSION_CHATWITH + " text ," + COLUMN_MYSESSION_OID
			+ " integer " // +
							// " foreign key(o_id) references owner(o_id)"
			+ ");";
	public static final String TABLE_MYOWNER = "owner";
	public static final String COLUMN_MYOWNER_NAME = "o_name";
	public static final String COLUMN_MYOWNER_OID = "o_id";
	public static final String TABLE_MYOWNER_CREATE = "create table "
			+ TABLE_MYOWNER + "(" + COLUMN_MYOWNER_OID
			+ " integer primary key  autoincrement," + COLUMN_MYOWNER_NAME
			+ " text " + ");";
	public static final int MSG_TYPE = 1;
}
