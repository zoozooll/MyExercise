package com.butterfly.vv.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
	private SQLiteDatabase sqLiteDatabase;
	private String[] allCityColumns = { Constants.COLUMN_ID,
			Constants.COLUMN_CPCODE, Constants.COLUMN_CNAME,
			Constants.COLUMN_CCODE };
	private String[] allOwnerColumns = { Constants.COLUMN_MYOWNER_OID,
			Constants.COLUMN_MYOWNER_NAME };

	public DBService(Context context) {
		// TODO Auto-generated constructor stub
		// vvimsqLiteHelper = new VVIMSQLiteHelper(context);
		// context.op
	}
	public void openDB() {
		// TODO Auto-generated method stub
		// vvimsqLiteHelper.close();
		// sqLiteDatabase = vvimsqLiteHelper.getWritableDatabase();
	}
	public void closeDB() {
		// TODO Auto-generated method stub
		// vvimsqLiteHelper.close();
	}
}
