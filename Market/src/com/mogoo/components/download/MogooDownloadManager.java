package com.mogoo.components.download;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import cn.imogoo.providers.downloads.Downloads;

public class MogooDownloadManager extends MogooDownloadBase {
	private static final String tag = "MogooDownloadManager";
	private Context mContext;
	private ContentResolver mResolver;
	private String mPackageName;

	// private Uri mBaseUri = Downloads.Impl.CONTENT_URI;

	/**
	 * @author gaolong 设置软件包名称，在调用startDownLoad()前
	 * @param mPackageName
	 */
	public void setmPackageName(String mPackageName) {
		this.mPackageName = mPackageName;
	}

	public MogooDownloadManager(Context context, ContentResolver resolver,
			String packageName) {
		super();
		this.mContext = context;
		this.mResolver = resolver;
		this.mPackageName = packageName;
	}

	//
	// public MogooDownloadManager(Context context, ContentResolver resolver) {
	// super();
	// this.mContext = context;
	// this.mResolver = resolver;
	// }

	public MogooDownloadManager(Context context) {
		super();
		this.mContext = context;
		this.mResolver = context.getContentResolver();
	}

	@Override
	public long startDownload(String url) {
		if (url == null) {
			return -1;
		}
		
//		deleteDownload(url);

		String status = Environment.getExternalStorageState();
		if (!status.equals(Environment.MEDIA_MOUNTED)) {
			String title;
			String msg;

			if (status.equals(Environment.MEDIA_SHARED)) {
				msg = "USB 存储设备正忙。要允许下载，请在通知中选择 关闭 USB 存储设备";
				title = "SD卡不可用";
			} else {

				msg = "必须装载USB存储设备才能下载";
				title = "不存在 USB 存储设备";
			}

			new AlertDialog.Builder(mContext).setTitle(title)
					.setIcon(android.R.drawable.ic_dialog_alert)
					.setMessage(msg).setPositiveButton("确定", null).show();
			Log.e(tag, "SD卡有问题");
			return -1;
		}

		setDownloadUrl(url);
		//
		// if (mResolver != null) {
		//
		// Log.e(tag, "mResolver.toString():" + mResolver.toString());
		// } else {
		// Log.e(tag, "mResolver==null!!!!!!!!!!");
		// }

		Log.e(tag, "mPackageName:" + mPackageName);
		ContentValues values = toContentValues(mPackageName);
		Uri downloadUri = mContext.getContentResolver().insert(
				Downloads.Impl.CONTENT_URI, values);
		long id = Long.parseLong(downloadUri.getLastPathSegment());

//		new MogooDownLayoutManager(mContext, id);
		return id;
	}
	
	/**
	 * 更改数据库将 downloadId 对应的下载任务状态改为暂停.
	 * 
	 * @param downloadId
	 */
	public void pauseDownload(long... downloadId){
	    
	    mContext.getContentResolver().update(Downloads.Impl.CONTENT_URI, getPauseContentValues(mPackageName), getWhereClauseForIds(downloadId),
                getWhereArgsForIds(downloadId));
	}
	
	/**
	 * 更改数据库将 downloadId 对应的下载任务状态改为继续下载.
	 * 
	 * @param downloadId
	 */
	public void restartDownload(long... downloadId){
	    mContext.getContentResolver().update(Downloads.Impl.CONTENT_URI, getRestartContentValues(mPackageName), getWhereClauseForIds(downloadId),
                getWhereArgsForIds(downloadId));
	}

	/**
	 * 取消下载 删除downloadId 对应的下载任务.
	 */
	@Override
	public long cancelDownload(long... downloadId) {
		if (downloadId == null || downloadId.length == 0) {
			throw new IllegalArgumentException(
					"input param 'ids' can't be null");
		}
		return mResolver.delete(Downloads.Impl.CONTENT_URI,
				getWhereClauseForIds(downloadId),
				getWhereArgsForIds(downloadId));
	}
	
	private void deleteDownload(String url){
	    if (url == null || "".equals(url)) {
	        return;
        }
	    String whereClause = Downloads.Impl.COLUMN_URI + " = ?";
        mResolver.delete(Downloads.Impl.CONTENT_URI,
                whereClause,new String[]{url});
	}

//	@Override
//	public void restartDownload(long... downloadId) {
//		Cursor cursor = query(new Query().setFilterById(ids));
//		try {
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				int status = cursor
//						.getInt(cursor.getColumnIndex(COLUMN_STATUS));
//				if (status != STATUS_SUCCESSFUL && status != STATUS_FAILED) {
//					throw new IllegalArgumentException(
//							"Cannot restart incomplete download: "
//									+ cursor.getLong(cursor
//											.getColumnIndex(COLUMN_ID)));
//				}
//			}
//		} finally {
//			cursor.close();
//		}
//
//		ContentValues values = new ContentValues();
//		values.put(Downloads.Impl.COLUMN_CURRENT_BYTES, 0);
//		values.put(Downloads.Impl.COLUMN_TOTAL_BYTES, -1);
//		values.putNull(Downloads.Impl._DATA);
//		values.put(Downloads.Impl.COLUMN_STATUS, Downloads.Impl.STATUS_PENDING);
//		mResolver.update(mBaseUri, values, getWhereClauseForIds(ids),
//				getWhereArgsForIds(ids));
//	}

	private static String getWhereClauseForIds(long[] ids) {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("(");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				whereClause.append("OR ");
			}
			whereClause.append(Downloads.Impl._ID);
			whereClause.append(" = ? ");
		}
		whereClause.append(")");
		return whereClause.toString();
	}

	private static String[] getWhereArgsForIds(long[] ids) {
		String[] whereArgs = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			whereArgs[i] = Long.toString(ids[i]);
		}
		return whereArgs;
	}
	
	/**
	 * 获取downloadId的下载地址
	 */
	public MogooDownloadInfo getDownloadInfo(String downloadId)
	{
		ContentResolver cr = mContext.getContentResolver();
        String[] projection = new String[]
        		{
        			Downloads.Impl.COLUMN_URI,
        			Downloads.Impl._DATA,
        			Downloads.Impl.COLUMN_STATUS,
        			Downloads.Impl.COLUMN_NOTIFICATION_PACKAGE,
        			Downloads.Impl.COLUMN_TOTAL_BYTES,
        	        Downloads.Impl.COLUMN_CURRENT_BYTES,
        	        Downloads.Impl.COLUMN_TITLE,
        	        Downloads.Impl.COLUMN_CONTROL
        		};
        String selection = Downloads.Impl._ID + "=?";
        
        Cursor c = null;
        try 
        {
        	c = cr.query(Downloads.Impl.CONTENT_URI, projection, selection, new String[] {downloadId}, null);
            if(c!=null && c.getCount()>0)
            {
            	c.moveToFirst();
            	return new MogooDownloadInfo(c.getString(0), c.getString(1),
        				c.getInt(2), c.getString(3),
        				c.getLong(4), c.getLong(5),
        				c.getString(6), c.getInt(7));
            }
		}
        catch (Exception e) 
        {
			// TODO: handle exception
		}
        finally
        {
        	if(c!=null)
        	{
        		c.close();
        	}
        }
        return null;
	}
	
	
}
