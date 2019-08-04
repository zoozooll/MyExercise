package com.oregonscientific.meep.store2.db;

import com.oregonscientific.meep.database.table.TableDownloadLog;
import com.oregonscientific.meep.store2.global.MeepStoreLog;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbAdapter {

	// database
	private SQLiteDatabase mDatabase;
	private String DB_NAME = "meepStoreDb";
	private int DB_VERSION = 1;
	private SQLiteOpenHelper mDbHelper;
	//private Context mContext;

	
	public DbAdapter(Context context) {
		//mContext = context;
		initDataBase(context);
	}
	
	private void initDataBase(Context context) {
		mDbHelper = new SQLiteOpenHelper(context, DB_NAME, null, DB_VERSION) {
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				MeepStoreLog.logcatMessage("database", "on database update");
			}

			@Override
			public void onCreate(SQLiteDatabase db) {
				MeepStoreLog.logcatMessage("database", "on database create");
				CreateTable(db );
			}
		};
	}
	
	public DbAdapter open() throws SQLException
	{
		mDatabase = mDbHelper.getWritableDatabase();
		if(mDatabase!= null) updateDbTable(mDatabase);
		return this;
	}
	
	public SQLiteDatabase getDatabase(){
		return mDatabase;
	}

	
	public void close()
	{
		mDbHelper.close();
	}
	
	public void updateDbTable(SQLiteDatabase db){
		db.execSQL(TableStorePurchasedItem.getCreateSql());
		//check downloadQueue table exists
		if(!TableMeepStoreDownloadQueue.isItemIdColumnExisted(db)){
			MeepStoreLog.logcatMessage("database", "create downloaditem table");
			db.execSQL(TableMeepStoreDownloadQueue.getCreateSql());
		}
		//add checksum column into table
		if(!TableMeepStoreDownloadQueue.isCheckSumColumnExisted(db)){
			MeepStoreLog.logcatMessage("database", "replace store downloaditem table");
			TableMeepStoreDownloadQueue.dropTable(db);
			db.execSQL(TableMeepStoreDownloadQueue.getCreateSql());
		}
		if(!TableMeepStoreDownloadQueue.isPackageNameColumnExisted(db)){
			MeepStoreLog.logcatMessage("database", "replace store downloaditem table");
			TableMeepStoreDownloadQueue.dropTable(db);
			db.execSQL(TableMeepStoreDownloadQueue.getCreateSql());
		}
		
		if(!TableStorePurchasedItem.isCheckSumColumnExisted(db)){
			MeepStoreLog.logcatMessage("database", "replace purchased item table");
			TableStorePurchasedItem.dropTable(db);
			db.execSQL(TableStorePurchasedItem.getCreateSql());
		}
	}
	

	
	private void CreateTable(SQLiteDatabase db)
	{
		if (db != null) {
			db.execSQL(TableDownloadLog.getCreateSql());
			db.execSQL(TableStoreImageCache.getCreateSql());
			db.execSQL(TableMeepStoreDownloadQueue.getCreateSql());
			db.execSQL(TableStorePurchasedItem.getCreateSql());
		}
	}
	


}
