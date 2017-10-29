package com.iskyinfor.duoduo.ui.provider;

import static android.provider.BaseColumns._ID;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;

/**
 * ��ݿ��ʼ��������
 * 
 * @author pKF29007
 * 
 */
public class DbHelper extends SQLiteOpenHelper {

	public static final String TAG = "DbHelper";

	public DbHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		this.getWritableDatabase();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		StringBuffer sql = createTaskTable();
		db.execSQL(sql.toString());
	}

	/**
	 * �����������
	 * 
	 * @return
	 */
	private StringBuffer createTaskTable() {
		StringBuffer sql = new StringBuffer();
		sql.append("create table ");
		sql.append(DbConstants.TB_NBAME);
		sql.append(" ( ");
		sql.append(_ID);
		sql.append("  integer primary key autoincrement, ");

		sql.append(DbConstants.BookTBField.RESID);
		sql.append(" TEXT , ");

		sql.append(DbConstants.BookTBField.BOOK_AUTHOR);
		sql.append(" TEXT , ");

		sql.append(DbConstants.BookTBField.BOOK_COMMENT_PAINT);
		sql.append(" TEXT , ");

		sql.append(DbConstants.BookTBField.BOOK_FILE_PATH);
		sql.append(" TEXT , ");

		sql.append(DbConstants.BookTBField.BOOK_LOGO_PATH);
		sql.append("  TEXT, ");

		sql.append(DbConstants.BookTBField.BOOK_READ_REMMBER_TIME);
		sql.append("  TEXT , ");

		sql.append(DbConstants.BookTBField.URL);

		sql.append("  TEXT  ) ");

		return sql;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		StringBuffer sql = new StringBuffer();
		sql.append("drop table if exists ");
		sql.append(DbConstants.TB_NBAME);
		db.execSQL(sql.toString());
		onCreate(db);
	}

}
