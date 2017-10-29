package com.iskyinfor.duoduo.downloadManage.provider;

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

		sql.append(DbConstants.TaskTbField.RESID);
		sql.append(" TEXT , ");

		sql.append(DbConstants.TaskTbField.NAME);
		sql.append(" TEXT , ");

		sql.append(DbConstants.TaskTbField.FILENAME);
		sql.append(" TEXT , ");

		sql.append(DbConstants.TaskTbField.EXTENDNAME);
		sql.append(" TEXT , ");

		sql.append(DbConstants.TaskTbField.URL);
		sql.append("  TEXT, ");

		sql.append(DbConstants.TaskTbField.FILEPATH);
		sql.append("  TEXT , ");

		sql.append(DbConstants.TaskTbField.TOTALSIZE);
		sql.append("  TEXT ,");

		sql.append(DbConstants.TaskTbField.CURRENTSIZE);
		sql.append("  TEXT ,");

		sql.append(DbConstants.TaskTbField.TASKSTATE);
		sql.append("  TEXT , ");

		sql.append(DbConstants.TaskTbField.FILETYPE);
		sql.append("  TEXT , ");

		sql.append(DbConstants.TaskTbField.DOWNTYPE);
		sql.append("  TEXT , ");

		sql.append(DbConstants.TaskTbField.ERRORCODE);
		sql.append("  TEXT , ");

		sql.append(DbConstants.TaskTbField.LASTMOD);
		sql.append("  REAL DEFAULT CURRENT_TIMESTAMP, ");

		sql.append(DbConstants.TaskTbField.FAILCOUNT);
		sql.append("  INTEGER , ");

		sql.append(DbConstants.TaskTbField.NOTIFYTAG);
		sql.append("  TEXT ,  ");

		sql.append(DbConstants.TaskTbField.ISFINISH);
		sql.append("  INTEGER  DEFAULT 0 ,");

		sql.append(DbConstants.TaskTbField.RETRYTIME);
		sql.append("  REAL  , ");

		sql.append(DbConstants.TaskTbField.METATYPE);
		sql.append("  TEXT  , ");

		sql.append(DbConstants.TaskTbField.PACKAGENAME);
		sql.append("  TEXT  , ");

		sql.append(DbConstants.TaskTbField.CREATETIME);
		sql.append("  REAL DEFAULT CURRENT_TIMESTAMP ,   ");

		sql.append(DbConstants.TaskTbField.SILENTMODE);
		sql.append("  TEXT  ,");

		sql.append(DbConstants.TaskTbField.NETSTATE);
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
