package com.mogoo.market.database;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.mogoo.market.MarketApplication;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.DownloadInfo;

/**
 * 下载信息数据库管理
 */
public class DownloadInfoDaoImpl implements IBeanDao<DownloadInfo> 
{
	private String mTableName;
	private DatabaseHelper mHelper;
	private static DownloadInfoDaoImpl instance = null;
	
	private DownloadInfoDaoImpl(Context context, String tableName) 
	{
		mHelper = DatabaseHelper.getInstance(context);
		mTableName = tableName;
	}
	
	public static DownloadInfoDaoImpl getInstance(Context ctx, String tableName) 
	{
		instance = new DownloadInfoDaoImpl(ctx, tableName);
		return instance;
	}
	
	@Override
	public Cursor getAllBean() 
	{
		SQLiteDatabase db = mHelper.getReadableDatabase();
		Cursor cursor =  db.query(mTableName, null, null, null, null, null, null);
		return cursor;
	}

	@Override
	public synchronized void clearAllBean() 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		int rows = db.delete(mTableName, null, null);
		//更新MarketApplication中的下载软件列表
		MarketApplication.getInstance().clearAllMyDownloadAppList();
	}

	@Override
	public synchronized void addBeans(ArrayList<DownloadInfo> beans) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		for(DownloadInfo info : beans)
		{
			ContentValues cv = new ContentValues();
			cv.put(DownloadInfoSQLTable.DOWNLOAD_ID, info.getDownload_id());
			cv.put(DownloadInfoSQLTable.APP_ID, info.getApp_id());
			cv.put(DownloadInfoSQLTable.DOWNLOAD_URL, info.getDownload_url());
			cv.put(DownloadInfoSQLTable.SAVE_PATH, info.getSave_path());
			cv.put(DownloadInfoSQLTable.NAME, info.getName());
			cv.put(DownloadInfoSQLTable.SIZE, info.getSize());
			cv.put(DownloadInfoSQLTable.ICON_URL, info.getIcon_url());
			cv.put(DownloadInfoSQLTable.RATING, info.getRating());
			cv.put(DownloadInfoSQLTable.APP_VERSION_NAME, info.getVersionName());
			cv.put(DownloadInfoSQLTable.APP_VERSION_CODE, info.getVersionCode());
			cv.put(DownloadInfoSQLTable.PACKAGE_NAME, info.getPackageName());
			long row = db.insert(DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO, null, cv);
			//更新MarketApplication中的下载软件列表
			MarketApplication.getInstance().addToMyDownloadAppList(info);
		}
	}
	
	public synchronized long addBean(DownloadInfo info) 
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		ContentValues cv = new ContentValues();
		cv.put(DownloadInfoSQLTable.DOWNLOAD_ID, info.getDownload_id());
		cv.put(DownloadInfoSQLTable.APP_ID, info.getApp_id());
		cv.put(DownloadInfoSQLTable.DOWNLOAD_URL, info.getDownload_url());
		cv.put(DownloadInfoSQLTable.SAVE_PATH, info.getSave_path());
		cv.put(DownloadInfoSQLTable.NAME, info.getName());
		cv.put(DownloadInfoSQLTable.SIZE, info.getSize());
		cv.put(DownloadInfoSQLTable.ICON_URL, info.getIcon_url());
		cv.put(DownloadInfoSQLTable.RATING, info.getRating());
		cv.put(DownloadInfoSQLTable.APP_VERSION_NAME, info.getVersionName());
		cv.put(DownloadInfoSQLTable.APP_VERSION_CODE, info.getVersionCode());
		cv.put(DownloadInfoSQLTable.PACKAGE_NAME, info.getPackageName());
		long row = db.insert(DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO, null, cv);
		//更新MarketApplication中的下载软件列表
		MarketApplication.getInstance().addToMyDownloadAppList(info);
		return row;
	}
	
	/**
	 * 返回下载管理数据库中所有已下载的任务
	 * @param context 
	 * @return all the downloaded task(key is the app_Id,value is the download id)
	 */
	public synchronized HashMap<String,String> getAllDownloadId(Context context) 
	{
		HashMap<String, String> resultList = new HashMap<String, String>();
		
		SQLiteDatabase db = mHelper.getReadableDatabase();
		final String sql = "select downloadinfo_app_id , downloadinfo_id from downloadinfo_table ";
		Cursor cursor = db.rawQuery(sql, null);
		
		if (cursor != null && cursor.getCount() != 0) 
		{
			for (cursor.moveToFirst() ; !cursor.isAfterLast() ; cursor.moveToNext()) 
			{				
				resultList.put( cursor.getString(0), cursor.getString(1));
			}
		}
		
		if(cursor != null)
		{
			cursor.close();
		}
		return resultList;
	}
	
	/**
	 * 插入数据
	 * @param download_id
	 * @param download_name
	 * @param download_size
	 * @param download_icon_url
	 * @return
	 */
	public void update(String download_id,ContentValues cv)
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		String where = DownloadInfoSQLTable.DOWNLOAD_ID+"="+download_id;
		db.update(DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO, cv, where, null);
	}
	
	/**
	 * 删除数据
	 * @param download_id
	 */
	public void delete(String download_id)
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		db.delete(DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO, DownloadInfoSQLTable.DOWNLOAD_ID+"=?", new String[]{download_id});
		//更新MarketApplication中的下载软件列表
		MarketApplication.getInstance().deleteMyDownloadAppList(download_id);
	}
	
	/**
	 * 删除数据
	 * @param download_id
	 */
	public void delete(ArrayList<String> downloadIds)
	{
		SQLiteDatabase db = mHelper.getWritableDatabase();
		StringBuilder sb = new StringBuilder();
		sb.append(DownloadInfoSQLTable.DOWNLOAD_ID+" IN (");
		for(String id : downloadIds)
		{
			sb.append("'"+id+"',");
		}
		sb.deleteCharAt(sb.lastIndexOf(","));
		sb.append(")");
		String where = sb.toString();
		db.delete(DownloadInfoSQLTable.TABLE_DOWNLOAD_INFO, where ,null);
		
		//更新MarketApplication中的下载软件列表
		MarketApplication.getInstance().updateMyDownloadAppList();
	}
	
	/**
	 * motone lcq add 2012-5-15
	 * @param apkId 
	 * @return downloadId if no result,return error,else return the downloadId
	 */
	public String getDownloadId (String apkId)
	{
		String result = "error";
		SQLiteDatabase db = null;
		try 
		{
			db = mHelper.getWritableDatabase();
		} 
		catch (SQLiteException e) 
		{
			db = mHelper.getReadableDatabase();
		}
		final String sql = "select downloadinfo_id from downloadinfo_table where downloadinfo_app_id = '" +apkId+"' ";
		Cursor cursor = db.rawQuery(sql, null);
		if (cursor != null && cursor.getCount() != 0) {
			cursor.moveToFirst();
			result = cursor.getString(0); 
		}
		
		if(cursor != null)
		{
			cursor.close();
		}
		return result;
	}

}
