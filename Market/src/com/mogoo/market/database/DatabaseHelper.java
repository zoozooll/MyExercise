package com.mogoo.market.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String TAG = "DatabaseHelper";
	
	private static final String DB_NAME = "market.db";
	private static final int DB_VERSION = 2;
	private Context mContext;
	
	DatabaseHelper(Context context) {
		super(context, DB_NAME , null, DB_VERSION);
		this.mContext = context;
	}
	
	private static DatabaseHelper instance = null;
	
	public static synchronized DatabaseHelper getInstance(Context context) {
		if(instance == null) {
			instance = new DatabaseHelper(context);
		}
		return instance;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		bootstrapDB(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(TAG, "Upgrading DB from version " + oldVersion + " to "
				+ newVersion);
		try {
            if (oldVersion == 1) {
                upgradeToVersion2(db); // From 1 or 2
            }
		}catch (SQLiteException e) {
			Log.e(TAG, "onUpgrade: SQLiteException, recreating db. " + e);
            dropTables(db);
            bootstrapDB(db);
            return; // this was lossy
		}
	}
	
    /**
     * 数据库版本从1更新到2.
     * @param db
     */
    private void upgradeToVersion2(SQLiteDatabase db) {
        Log.w(TAG, "Upgrading DeletedEvents table");
        
        dropCacheTables(db);
        bootstrapCacheDB(db);
    }
    
    /**
     * 删除商城所有数据表.
     * @param db
     */
    private void dropTables(SQLiteDatabase db) {
    	dropCacheTables(db);
    	db.execSQL("DROP TABLE IF EXISTS downloadinfo_table;");
    }
    
    /**
     * 创建商城所有数据表.
     * @param db
     */
    private void bootstrapDB(SQLiteDatabase db) {
    	bootstrapCacheDB(db);
		DownloadInfoSQLTable.getInstance().creatTable(db, DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO);
    }
    
    /**
     * 删除商城列表缓存数据表.
     * @param db
     */
    private void dropCacheTables(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS topic_data;");
        db.execSQL("DROP TABLE IF EXISTS apps_cate_data;");
        db.execSQL("DROP TABLE IF EXISTS game_cate_data;");
        db.execSQL("DROP TABLE IF EXISTS hot_data;");
        db.execSQL("DROP TABLE IF EXISTS latest_data;");
        db.execSQL("DROP TABLE IF EXISTS necessary_data;");
        db.execSQL("DROP TABLE IF EXISTS top_ranking_data;");
        db.execSQL("DROP TABLE IF EXISTS apps_ranking_data;");
        db.execSQL("DROP TABLE IF EXISTS game_ranking_data;");
        db.execSQL("DROP TABLE IF EXISTS child_cate_data;");
        db.execSQL("DROP TABLE IF EXISTS quality_apps_data;");
        db.execSQL("DROP TABLE IF EXISTS newest_apps_data;");
        db.execSQL("DROP TABLE IF EXISTS quality_game_data;");
        db.execSQL("DROP TABLE IF EXISTS newest_game_data;");
        db.execSQL("DROP TABLE IF EXISTS installous_data;");
    }
    
    /**
     * 创建商城列表缓存数据表.
     * @param db
     */
    private void bootstrapCacheDB(SQLiteDatabase db) {
		TopicSQLTable.getInstance().creatTable(db);
		AppsCateSQLTable.getInstance().creatTable(db);
		GameCateSQLTable.getInstance().creatTable(db);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_HOT);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_LATEST);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_NECESSARY);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_TOP_RANKING);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_APPS_RANKING);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_GAME_RANKING);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_CHILD_CATE);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_QUALITY_APPS);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_NEWEST_APPS);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_QUALITY_GAME);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_NEWEST_GAME);
		ApkListSQLTable.getInstance().creatTable(db, ApkListSQLTable.TABLE_INSTALLOUS);
    }
}
