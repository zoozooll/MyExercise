package com.oregonscientific.meep.database.table;

import java.util.Date;

import android.database.Cursor;
import android.util.Log;

import com.oregonscientific.meep.global.Global.AppType;

public class TableDownloadLog {
	private static final String TAG = TableDownloadLog.class.getSimpleName();
	
	public static final int STATUS_PENDING = 0;
	public static final int STATUS_DOWNLOADING = 1;
	public static final int STATUS_COMPLETED = 2;

	public static final String S_TABLE_NAME = "downloadLog";
	public static final String S_TASK = "downloadTask";
	public static final String S_URL = "url";
	public static final String S_STORE_PATH = "storePath";
	public static final String S_STATUS = "status"; // 0 not start, 1 downloading, 2 completed
	public static final String S_CREATE_TIME = "createTime";
	public static final String S_COMPLETE_TIME = "completeTime";
	public static final String S_TYPE = "type";

	public String Task;
	public String Url;
	public String StorePath;
	public int Status;
	public long Size;
	public Date CreateTime;
	public Date CompletdTime;
	public String Type;

	public TableDownloadLog(String type, String task, String url, String storePath, int status,
			int size) {
		this.Type = type;
		this.Task = task;
		this.Url = url;
		this.StorePath = storePath;
		this.Status = status;
		this.Size = size;
	}
	
	public int getStatus() {
		return Status;
	}

	public static String getCreateSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("CREATE TABLE IF NOT EXISTS ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_TYPE);
		sb.append(" VARCHAR NOT NULL ,");
		sb.append(S_TASK);
		sb.append(" VARCHAR NOT NULL ,");
		sb.append(S_URL);
		sb.append(" VARCHAR NOT NULL,");
		sb.append(S_STORE_PATH);
		sb.append(" VARCHAR NOT NULL,");
		sb.append(S_STATUS);
		sb.append(" INTEGER NOT NULL,");
		sb.append(S_CREATE_TIME);
		sb.append(" DATETIME NOT NULL,");
		sb.append(S_COMPLETE_TIME);
		sb.append(" DATETIME");
		sb.append(" )");
		return sb.toString();
	}

	public String getInsertSql() {
		StringBuilder sb = new StringBuilder();
		sb.append("INSERT INTO ");
		sb.append(S_TABLE_NAME);
		sb.append(" (");
		sb.append(S_TYPE);
		sb.append(",");
		sb.append(S_TASK);
		sb.append(",");
		sb.append(S_URL);
		sb.append(",");
		sb.append(S_STORE_PATH);
		sb.append(",");
		sb.append(S_STATUS);
		sb.append(",");
		sb.append(S_CREATE_TIME);
		sb.append(") VALUES ('");
		sb.append(this.Type);
		sb.append("','");
		sb.append(this.Task);
		sb.append("','");
		sb.append(this.Url);
		sb.append("','");
		sb.append(this.StorePath);
		sb.append("',");
		sb.append(this.Status);
		sb.append(",datetime()");
		sb.append(")");
		return sb.toString();
	}

	public static String getUpdateDownloadStatusSql(String task, int status) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_STATUS);
		sb.append(" = ");
		sb.append(status);
		sb.append(" WHERE ");
		sb.append(S_TASK);
		sb.append(" = '");
		sb.append(task);
		sb.append("'");
		return sb.toString();
	}

	public static String getUpdateImageSql(String task, String type, String imagePath) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_STORE_PATH);
		sb.append(" = '");
		sb.append(imagePath);
		sb.append("'");
		sb.append(" WHERE ");
		sb.append(S_TASK);
		sb.append(" = '");
		sb.append(task);
		sb.append("' AND ");
		sb.append(S_TYPE);
		sb.append(" = '");
		sb.append(type);
		sb.append("'");
		return sb.toString();
	}

	public static String getUpdateDownloadCompletedSql(String task, String type) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_STATUS);
		sb.append(" = ");
		sb.append(2);
		sb.append(",");
		sb.append(S_COMPLETE_TIME);
		sb.append(" = ");
		sb.append("datetime()");
		sb.append(" WHERE ");
		sb.append(S_TASK);
		sb.append(" = '");
		sb.append(task);
		sb.append("' AND ");
		sb.append(S_TYPE);
		sb.append(" = '");
		sb.append(type);
		sb.append("'");
		return sb.toString();
	}
	
	/**
	 * Returns a TableDownloadLog object by translating the cursor 
	 * 
	 * @param cursor The cursor to translate
	 * @return The translated TableDownloadLog object
	 */
	public static TableDownloadLog fromCursor(Cursor cursor) {
		// Quick return if there is nothing to process
		if (cursor == null) {
			return null;
		}

		TableDownloadLog result = null;
		try {
			result = new TableDownloadLog(
					cursor.getString(cursor.getColumnIndex(S_TYPE)), 
					cursor.getString(cursor.getColumnIndex(S_TASK)),
					cursor.getString(cursor.getColumnIndex(S_URL)),
					cursor.getString(cursor.getColumnIndex(S_STORE_PATH)),
					cursor.getInt(cursor.getColumnIndex(S_STATUS)),
					0);
		} catch (Exception ex) {
			// Ignore
			Log.e(TAG, "Cannot translate cursor into Java object");
		}
		
		return result;
	}
	
	/**
	 * Returns values of this object as SQL arguments 
	 * 
	 * @return An array of String as bind arguments in a SQL statement
	 */
	public String[] toSqlArguments() {
		return (new String[] { Type, Task, Url, StorePath, Integer.toString(Status) });
	}
	
	/**
	 * Returns an UPDATE SQL statement that updates the URL and destination of a download
	 *  
	 * @return The SQL statement
	 */
	public static String getUpdateSourceAndDestinationSql() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_URL);
		sb.append(" = ?, ");
		sb.append(S_STORE_PATH);
		sb.append(" = ?");
		sb.append(" WHERE ");
		sb.append(S_TYPE);
		sb.append(" = ?");
		sb.append(" AND ");
		sb.append(S_TASK);
		sb.append(" = ?");
		sb.append(" AND ");
		sb.append(S_URL);
		sb.append(" = ?");
		sb.append(" AND ");
		sb.append(S_STORE_PATH);
		sb.append(" = ?");
		sb.append(" AND ");
		sb.append(S_STATUS);
		sb.append(" = ?");
		
		return sb.toString();
	}
	
	/**
	 * Returns an UPDATE SQL statement that updates status of a download 
	 * 
	 * @param url The URL of the download
	 * @param status The current status of the download
	 * @return The SQL statement
	 */
	public static String getUpdateStatusSql(String url, int status) {
		StringBuilder sb = new StringBuilder();
		sb.append("UPDATE ");
		sb.append(S_TABLE_NAME);
		sb.append(" SET ");
		sb.append(S_STATUS);
		sb.append(" = ?, ");
		sb.append(S_CREATE_TIME);
		sb.append(" = DATETIME()");
		sb.append(" WHERE ");
		sb.append(S_STATUS);
		sb.append(" = ");
		sb.append(Integer.toString(status));

		if (url != null) {
			sb.append(" AND ");
			sb.append(S_URL);
			sb.append(" = ?");
		}

		return sb.toString();
	}

	public static String getSelectSql(String url) {
		return getSelectSql(url, null);
	}

	/**
	 * Retrieves a SELECT SQL statement with the given parameters
	 * 
	 * @param url The URL of the remote resource
	 * @param status The status of the download
	 * @return The SQL statement
	 */
	public static String getSelectSql(String url, int... status) {
		StringBuilder sb = new StringBuilder();
		sb.append("SELECT * FROM ");
		sb.append(S_TABLE_NAME);

		if (url != null) {
			sb.append(" WHERE ");
			sb.append(S_URL);
			sb.append(" = ?");
		}

		if (status != null) {
			if (url != null) {
				sb.append(" AND (");
			} else {
				sb.append(" WHERE (");
			}

			for (int i = 0; i < status.length; i++) {
				if (i > 0) {
					sb.append(" OR ");
				}
				sb.append(S_STATUS);
				sb.append(" = ");
				sb.append(Integer.toString(status[i]));
			}
			sb.append(")");
		}

		return sb.toString();
	}

	/**
	 * Deletes entries in the table with the given url(s)
	 * 
	 * @param status
	 *          The status of the download
	 * @param urls
	 *          An array of URLs to delete
	 * @return The result SQL statement
	 */
	public static String getDeleteSql(int status, String... urls) {
		StringBuilder sb = new StringBuilder();
		sb.append("DELETE FROM ");
		sb.append(S_TABLE_NAME);
		sb.append(" WHERE ");
		sb.append(S_STATUS);
		sb.append(" = ");
		sb.append(Integer.toString(status));

		if (urls != null) {
			sb.append(" AND (");
			for (int i = 0; i < urls.length; i++) {
				if (i > 0) {
					sb.append(" OR ");
				}
				sb.append(S_URL);
				sb.append(" = ?");
			}
			sb.append(")");
		}

		return sb.toString();
	}

	public static String getDeleteOTASql() {
		return "DELETE from " + S_TABLE_NAME + " WHERE " + S_TYPE + " = 'ota'";
	}

	public static String getSelectOTASql(String url) {
		return "SELECT * from " + S_TABLE_NAME + " WHERE " + S_TYPE + " = 'ota' AND " + S_URL + " == '"
				+ url + "'";
	}

}
