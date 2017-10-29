package com.iskyinfor.zoom.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class BooksDao {

	private Context context;
	private DBHelper helper;
	private SQLiteDatabase db;

	private static BooksDao dao;

	private BooksDao() {
	}

	/**
	 * 获得数据库连接类Dao唯一方法。本类已实现单例模式
	 * 
	 * @param context
	 * @return
	 * @date 2011-4-3上午09:59:27
	 * @author Lee Chok Kong
	 */
	public static BooksDao getDao(Context context) {
		if (dao == null) {
			dao = new BooksDao();
		}
		dao.context = context;
		return dao;
	}

	/**
	 * 打开数据库并连接 ，本方法已先做判断
	 * 
	 * @throws SQLException
	 * @date 2011-4-3上午10:00:08
	 * @author Lee Chok Kong
	 */
	public void open() throws SQLException {
		helper = DBHelper.getHelper(context);
		if (db == null || !db.isOpen()) {
			db = helper.getWritableDatabase();
		}
	}

	/**
	 * 关闭数据库;
	 * 
	 * @throws SQLException
	 * @date 2011-4-3上午10:00:47
	 * @author Lee Chok Kong
	 */
	public void close() throws SQLException {
		if (helper != null) {
			helper.close();
		}
	}

	/**
	 * 通过执行sql语句进行查询。如果有cursor即返回cursor
	 * 
	 * @param sql
	 *            sql语句字符串
	 * @return
	 * @throws SQLException
	 * @date 2011-4-3上午10:00:59
	 * @author Lee Chok Kong
	 */
	public Cursor exec(String sql) throws SQLException {
		this.open();
		return db.rawQuery(sql, null);
	}

	/**
	 * @deprecated
	 * @param tablename
	 * @param bookname
	 * @date 2011-4-3上午10:02:08
	 * @author Lee Chok Kong
	 */
	public void saveOrUpdate(String tablename, String bookname) {
	}

	/**
	 * 
	 * @param table
	 * @param limit
	 * @return
	 * @throws SQLException
	 * @date 2011-4-3上午10:36:22
	 * @author Lee Chok Kong
	 */
	public Cursor select(String table, String limit) throws SQLException {
		return select(false, table, null, null, null, null, limit);
	}

	/**
	 * 
	 * @param distinct
	 * @param table
	 * @param columns
	 * @param selection
	 * @param selectionArgs
	 * @param orderBy
	 * @param limit
	 * @return
	 * @throws SQLException
	 * @date 2011-4-3上午10:36:28
	 * @author Lee Chok Kong
	 */
	public Cursor select(boolean distinct, String table, String[] columns,
			String selection, String[] selectionArgs, String orderBy,
			String limit) throws SQLException {
		this.open();
		Cursor cursor = db.query(distinct, table, null, selection,
				selectionArgs, null, null, orderBy, limit);
		return cursor;
	}

	/**
	 * 
	 * @param table
	 * @return
	 * @date 2011-4-3上午10:36:33
	 * @author Lee Chok Kong
	 */
	public int count(String table) {
		Cursor cursor = select(false, table, new String[] { "_id" }, null,
				null, null, null);
		int count = cursor.getCount();
		cursor.close();
		return count;
	}

	/**
	 * 
	 * @param table
	 * @param cv
	 * @return
	 * @throws SQLException
	 * @date 2011-4-3上午10:36:37
	 * @author Lee Chok Kong
	 */
	public long insert(String table, ContentValues cv) throws SQLException

	{
		this.open();
		long row = db.insert(table, null, cv);
		return row;

	}

	/**
	 * 
	 * @param table
	 * @param id
	 * @throws SQLException
	 * @date 2011-4-3上午10:36:41
	 * @author Lee Chok Kong
	 */
	public void delete(String table, int id) throws SQLException

	{

		this.open();

		String where = DBHelper.ID + "=?";

		String[] whereValue = { Integer.toString(id) };

		db.delete(table, where, whereValue);

	}
	
	public void delete(String table, String where, String[] whereValue) throws SQLException

	{

		this.open();

		db.delete(table, where, whereValue);

	}

	/**
	 * 
	 * @param table
	 * @throws SQLException
	 * @date 2011-4-3上午10:36:56
	 * @author Lee Chok Kong
	 */
	public void deleteAll(String table) throws SQLException {
		this.open();
		db.delete(table, null, null);
		db.close();
	}

	/**
	 * 
	 * @param table
	 * @param id
	 * @param cv
	 * @throws SQLException
	 * @date 2011-4-3上午10:37:03
	 * @author Lee Chok Kong
	 */
	public void update(String table, int id, ContentValues cv)
			throws SQLException

	{

		this.open();

		String where = DBHelper.ID + "=?";

		String[] whereValue = { Integer.toString(id) };

		db.update(table, cv, where, whereValue);

	}

	/**
	 * 
	 * @param table
	 * @param colName
	 * @param colValue
	 * @param cv
	 * @throws SQLException
	 * @date 2011-4-3上午10:37:11
	 * @author Lee Chok Kong
	 */
	public void update(String table, String colName, String colValue,
			ContentValues cv) throws SQLException

	{

		this.open();

		String where = colName + "=?";

		String[] whereValue = { colValue };

		db.update(table, cv, where, whereValue);

	}

}
