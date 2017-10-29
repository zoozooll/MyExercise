package com.iskyinfor.duoduo.ui.provider;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class ProviderInterface {

	private ContentResolver contentResolver = null;

	Context context = null;

	public ProviderInterface(Context cxt) {
		this.context = cxt;
		contentResolver = context.getContentResolver();
	}

	/**
	 * ͨ��DownloadTask�е���� ������ݿ���
	 * 
	 * @param mContext
	 * @param info
	 */
	public void insertBookInfor(Book item) {

		ContentValues values = new ContentValues();
		values.put(DbConstants.BookTBField.RESID, item.resourceId);
		values.put(DbConstants.BookTBField.BOOK_AUTHOR, item.author);
		values.put(DbConstants.BookTBField.BOOK_COMMENT_PAINT, item.paint);
		values.put(DbConstants.BookTBField.URL, item.url);
		values.put(DbConstants.BookTBField.BOOK_FILE_PATH, item.filePath);
		values.put(DbConstants.BookTBField.BOOK_LOGO_PATH, item.logoUrl);
		values.put(DbConstants.BookTBField.BOOK_READ_REMMBER_TIME, item.remmberTime);

		contentResolver.insert(DbConstants.CONTENT_URI, values);

	}

	/**
	 * ͨ��DownloadInfo�е���� �޸���ݿ���
	 * 
	 * @param mContext
	 * @param item
	 */
	public void updateDownloadTask(Book item) {
		ContentValues values = new ContentValues();
		values.put(DbConstants.BookTBField.RESID, item.resourceId);
		values.put(DbConstants.BookTBField.BOOK_AUTHOR, item.author);
		values.put(DbConstants.BookTBField.BOOK_COMMENT_PAINT, item.paint);
		values.put(DbConstants.BookTBField.URL, item.url);
		values.put(DbConstants.BookTBField.BOOK_FILE_PATH, item.filePath);
		values.put(DbConstants.BookTBField.BOOK_LOGO_PATH, item.logoUrl);
		values.put(DbConstants.BookTBField.BOOK_READ_REMMBER_TIME, item.remmberTime);
		contentResolver.update(DbConstants.CONTENT_URI, values,
				DbConstants.BookTBField.RESID + " = ? ",
				new String[] { item.resourceId });

	}

	/**
	 * 判断书籍是否存在
	 */
	public Integer isExistBookByResId(String resId) {
		Integer id = null;
		Cursor c = contentResolver.query(DbConstants.CONTENT_URI,
				new String[] { _ID }, DbConstants.BookTBField.RESID + "=?",
				new String[] { resId }, null);
		if (c.moveToFirst()) {
			id = c.getInt(c.getColumnIndex(_ID));
		}
		closeCursor(c);
		return id;
	}



	/**
	 * ��ѯ����δ������ɵļ�¼
	 * 
	 * @return
	 */
	public ArrayList<Book> queryTaskByUnFinishState() {
		ArrayList<Book> list = new ArrayList<Book>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							null,
							null,
							DbConstants.BookTBField._ID);
			while (c.moveToNext()) {
				Book item = transformCursor(c);
				list.add(item);
			}
			closeCursor(c);
		} catch (Exception e) {
			e.printStackTrace();
			closeCursor(c);
		} finally {
			closeCursor(c);
		}
		return list;
	}




	public Book queryTaskByResId(String resId) {
		Cursor c = null;
		Book book = null;
		try {
			c = context.getContentResolver().query(DbConstants.CONTENT_URI,
					new String[] { "*" },
					DbConstants.BookTBField.RESID + " = ? ",
					new String[] { resId }, null);
			while (c.moveToNext()) {
				book = transformCursor(c);
			}
			closeCursor(c);
		} catch (Exception e) {
			e.printStackTrace();
			closeCursor(c);
		} finally {
			closeCursor(c);
		}
		return book;
	}


	/**
	 * ���ת���ķ�������Cursorת����DownloadTask����
	 * 
	 * @param cursor
	 * @return
	 */
	private Book transformCursor(Cursor cursor) {
		Book info = new Book();

		Integer id = cursor.getInt(cursor
				.getColumnIndex(DbConstants.BookTBField._ID));
		String resId = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.RESID));
		info.resourceId = resId;
		info.author = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.BOOK_AUTHOR));

		info.filePath = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.BOOK_FILE_PATH));
		info.logoUrl = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.BOOK_LOGO_PATH));
		info.paint = cursor.getLong(cursor
				.getColumnIndex(DbConstants.BookTBField.BOOK_COMMENT_PAINT));
		info.remmberTime = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.BOOK_READ_REMMBER_TIME));
		info.url = cursor.getString(cursor
				.getColumnIndex(DbConstants.BookTBField.URL));
		return info;
	}

	/**
	 * �ر��α�
	 * 
	 * @param cursor
	 */
	private void closeCursor(Cursor cursor) {
		if (cursor != null && !cursor.isClosed()) {
			cursor.close();
		}
	}


	/**
	 * ͨ��ResIdɾ����������
	 * 
	 */
	public int deleteBookByResId(String resId) {
		int count = contentResolver
				.delete(DbConstants.CONTENT_URI, DbConstants.BookTBField.RESID
						+ " = ? ", new String[] { resId });
		return count;
	}

}
