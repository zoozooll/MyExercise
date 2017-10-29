package com.iskyinfor.duoduo.downloadManage.provider;

import static android.provider.BaseColumns._ID;

import java.util.ArrayList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.downloadManage.DownloadUtil;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.downloadManage.utils.LangUtil;

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
	public void insertDownloadTask(DownloadTask item) {

		ContentValues values = new ContentValues();
		values.put(DbConstants.TaskTbField.RESID, item.resourceId);
		values.put(DbConstants.TaskTbField.NAME, item.name);
		values.put(DbConstants.TaskTbField.FILENAME, item.fileName);
		values.put(DbConstants.TaskTbField.URL, item.url);
		values.put(DbConstants.TaskTbField.NOTIFYTAG, 0);
		values.put(DbConstants.TaskTbField.TASKSTATE, item.taskState);
		values.put(DbConstants.TaskTbField.LASTMOD, item.lastmod);
		values.put(DbConstants.TaskTbField.FILETYPE, item.fileType);
		values.put(DbConstants.TaskTbField.DOWNTYPE, item.downType);
		values.put(DbConstants.TaskTbField.ERRORCODE, item.errorCode);

		values.put(DbConstants.TaskTbField.FILEPATH, item.filePath);
		values.put(DbConstants.TaskTbField.TOTALSIZE, item.totalSize);
		values.put(DbConstants.TaskTbField.CURRENTSIZE, item.currentSize);
		values.put(DbConstants.TaskTbField.EXTENDNAME, item.extendName);

		values.put(DbConstants.TaskTbField.METATYPE, item.metaType);
		values.put(DbConstants.TaskTbField.PACKAGENAME, item.packageName);
		values.put(DbConstants.TaskTbField.CREATETIME, item.createTime);
		values.put(DbConstants.TaskTbField.SILENTMODE, item.silentMode);
		values.put(DbConstants.TaskTbField.NETSTATE, item.netState);

		values.put(DbConstants.TaskTbField.FAILCOUNT, 0);
		values.put(DbConstants.TaskTbField.ISFINISH,
				DownloadTask.FinishState.TASK_UNFINISH_STATE);

		contentResolver.insert(DbConstants.CONTENT_URI, values);

	}

	/**
	 * ͨ��DownloadInfo�е���� �޸���ݿ���
	 * 
	 * @param mContext
	 * @param item
	 */
	public void updateDownloadTask(DownloadTask item) {
		ContentValues values = new ContentValues();
		values.put(DbConstants.TaskTbField.RESID, item.resourceId);
		values.put(DbConstants.TaskTbField.NAME, item.name);
		values.put(DbConstants.TaskTbField.FILENAME, item.fileName);
		values.put(DbConstants.TaskTbField.EXTENDNAME, item.extendName);

		values.put(DbConstants.TaskTbField.URL, item.url);
		values.put(DbConstants.TaskTbField.FILEPATH, item.filePath);

		values.put(DbConstants.TaskTbField.TOTALSIZE, item.totalSize);
		values.put(DbConstants.TaskTbField.CURRENTSIZE, item.currentSize);

		values.put(DbConstants.TaskTbField.TASKSTATE, item.taskState);
		values.put(DbConstants.TaskTbField.FILETYPE, item.fileType);
		values.put(DbConstants.TaskTbField.DOWNTYPE, item.downType);
		values.put(DbConstants.TaskTbField.ERRORCODE, item.errorCode);

		values.put(DbConstants.TaskTbField.LASTMOD, item.lastmod);
		values.put(DbConstants.TaskTbField.FAILCOUNT, item.failCount);
		values.put(DbConstants.TaskTbField.NOTIFYTAG, item.notifyTag);
		values.put(DbConstants.TaskTbField.ISFINISH,
				DownloadTask.FinishState.TASK_UNFINISH_STATE);
		values.put(DbConstants.TaskTbField.CREATETIME, item.createTime);
		values.put(DbConstants.TaskTbField.SILENTMODE, item.silentMode);
		values.put(DbConstants.TaskTbField.NETSTATE, item.netState);
		values.put(DbConstants.TaskTbField.RETRYTIME, item.retryTime);

		contentResolver.update(DbConstants.CONTENT_URI, values,
				DbConstants.TaskTbField.RESID + " = ? ",
				new String[] { item.resourceId });

	}

	/**
	 * ������ݿ�TaskState״̬
	 * 
	 * @param mContext
	 * @param state
	 */
	public void updateTaskStateByResId(String resId, int taskState) {
		ContentValues values = new ContentValues();
		values
				.put(DbConstants.TaskTbField.TASKSTATE, String
						.valueOf(taskState));
		values.put(DbConstants.TaskTbField.LASTMOD, System.currentTimeMillis());
		contentResolver.update(DbConstants.CONTENT_URI, values,
				DbConstants.TaskTbField.RESID + "=?", new String[] { resId });
	}

	/**
	 * ͨ��ID�ж���������ݿ����Ƿ����,���ڷ�������ID
	 */
	public Integer isExistTaskByResId(String resId) {
		Integer id = null;
		Cursor c = contentResolver.query(DbConstants.CONTENT_URI,
				new String[] { _ID }, DbConstants.TaskTbField.RESID + "=?",
				new String[] { resId }, null);
		if (c.moveToFirst()) {
			id = c.getInt(c.getColumnIndex(_ID));
		}
		closeCursor(c);
		return id;
	}

	/**
	 * ͨ��ID�ж���������ݿ����Ƿ����,���ڷ�������ID
	 */
	public Integer isFinishById(int id) {
		Integer isFinish = null;
		Cursor c = contentResolver.query(DbConstants.CONTENT_URI,
				new String[] { DbConstants.TaskTbField.ISFINISH },
				DbConstants.TaskTbField._ID + "=?", new String[] { String
						.valueOf(id) }, null);
		if (c.moveToFirst()) {
			isFinish = c.getInt(c
					.getColumnIndex(DbConstants.TaskTbField.ISFINISH));
		}
		closeCursor(c);
		return isFinish;
	}

	/**
	 * <pre>
	 * ��������¼ʱ���жϣ�����¼��Ϊ����
	 *  NEW_TASK = 0 :  ��һ�����ص����񣬿���ֱ��������ص�ַ ;
	 *  DOWNLOADING_TASK = 1 : �������ص�����ȡ����������������
	 *  DOWNLOADED_TASK = 2 �������ص���������������ػḲ����ؼ�¼
	 * </pre>
	 */
	public int getHistoryStateByResid(String resId) {
		int result = DownloadTask.HistoryState.NEW_TASK;
		Integer id = isExistTaskByResId(resId);
		if (id != null) {
			Integer isFinish = isFinishById(id);
			if (isFinish != null) {
				result = (isFinish == DownloadTask.FinishState.TASK_FINISH_STATE) ? DownloadTask.HistoryState.DOWNLOADED_TASK
						: DownloadTask.HistoryState.DOWNLOADING_TASK;
			}

		}
		return result;
	}

	/**
	 * ��ѯ����δ������ɵļ�¼
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> queryTaskByUnFinishState() {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? ",
							new String[] { String
									.valueOf(DownloadTask.FinishState.TASK_UNFINISH_STATE) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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

	/**
	 * ��ѯ�����״̬�ļ�¼��
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> queryTaskByFinishState() {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? ",
							new String[] { String
									.valueOf(DownloadTask.FinishState.TASK_FINISH_STATE) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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

	/**
	 * ��ѯ�����سɹ�״̬�ļ�¼��
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> querySuccessTask() {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? and "
									+ DbConstants.TaskTbField.TASKSTATE + "=? ",
							new String[] {
									String
											.valueOf(DownloadTask.FinishState.TASK_FINISH_STATE),
									String.valueOf(DownloadTask.State.SUCCESS) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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

	/**
	 * ��ѯ������δ�ɹ�״̬�ļ�¼��
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> queryUnSuccessTask() {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? and "
									+ DbConstants.TaskTbField.TASKSTATE + "!=? ",
							new String[] {
									String
											.valueOf(DownloadTask.FinishState.TASK_FINISH_STATE),
									String.valueOf(DownloadTask.State.SUCCESS) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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
	
	/**
	 * ��ѯ�������״̬�ĸ�����״̬������
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> queryTaskIfFinish(int tastState) {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? and "
									+ DbConstants.TaskTbField.TASKSTATE
									+ " = ? ",
							new String[] {
									String
											.valueOf(DownloadTask.FinishState.TASK_FINISH_STATE),
									String.valueOf(tastState) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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

	/**
	 * ��ѯδ���״̬�ĸ�����״̬������
	 * 
	 * @return
	 */
	public ArrayList<DownloadTask> queryTaskIfUnfinish(int tastState) {
		ArrayList<DownloadTask> list = new ArrayList<DownloadTask>();

		Cursor c = null;
		try {
			c = contentResolver
					.query(
							DbConstants.CONTENT_URI,
							new String[] { "*" },
							DbConstants.TaskTbField.ISFINISH + " = ? and "
									+ DbConstants.TaskTbField.TASKSTATE
									+ " = ? ",
							new String[] {
									String
											.valueOf(DownloadTask.FinishState.TASK_UNFINISH_STATE),
									String.valueOf(tastState) },
							DbConstants.TaskTbField._ID);
			while (c.moveToNext()) {
				DownloadTask item = transformDownloadTask(c);
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

	/**
	 * ������������������������Ѵ��ڣ��򸲸Ǹ������¼
	 * 
	 * @param context
	 * @param downloadInfo
	 *            ������Ҫ����Ϣ
	 */
	public void addDownloadTask(Context context, DownloadTask downloadInfo) {
		ProviderInterface downFace = new ProviderInterface(context);

		Integer id = isExistTaskByResId(downloadInfo.resourceId);

		if (id == null) {
			downFace.insertDownloadTask(downloadInfo);
		} else {
			downloadInfo.notifyTag = id;
			downFace.updateDownloadTask(downloadInfo);
		}
	}

	/**
	 * ͨ��resourceId����DownloadInfo
	 * 
	 * @param context
	 * @param resId
	 * @return DownloadInfo
	 */
	public DownloadTask queryTaskByResId(String resId) {
		Cursor c = null;
		DownloadTask downLoadItem = null;
		try {
			c = context.getContentResolver().query(DbConstants.CONTENT_URI,
					new String[] { "*" },
					DbConstants.TaskTbField.RESID + " = ? ",
					new String[] { resId }, null);
			while (c.moveToNext()) {
				downLoadItem = transformDownloadTask(c);
			}
			closeCursor(c);
		} catch (Exception e) {
			e.printStackTrace();
			closeCursor(c);
		} finally {
			closeCursor(c);
		}
		return downLoadItem;
	}

	/**
	 * ��ݰ����������Ƿ����
	 * 
	 * @param context
	 * @param packageName
	 * @return isFinish =-1��û���������� isFinish
	 *         =1����DownloadInfo.TASK_FINISH_STATE��������� isFinish
	 *         =0����DownloadInfo.TASK_UNFINISH_STATE����������
	 */
	public int getTaskStateByPackageName(Context context, String packageName) {
		Cursor c = null;
		int isFinish = -1;
		try {
			c = context.getContentResolver().query(DbConstants.CONTENT_URI,
					new String[] { DbConstants.TaskTbField.ISFINISH },
					DbConstants.TaskTbField.PACKAGENAME + " = ? ",
					new String[] { packageName }, null);
			while (c.moveToNext()) {
				isFinish = c.getInt(c
						.getColumnIndex(DbConstants.TaskTbField.ISFINISH));
			}
			closeCursor(c);
		} catch (Exception e) {
			e.printStackTrace();
			closeCursor(c);
		} finally {
			closeCursor(c);
		}
		return isFinish;
	}

	/**
	 * ͨ��RESID,����FinishState״̬
	 * 
	 * @param resid
	 * @param finishState
	 */
	public void updateFinishStateByResid(String resId, int finishState) {
		ContentValues values = new ContentValues();
		values.put(DbConstants.TaskTbField.ISFINISH, finishState);
		// ���²���ʱ��
		values.put(DbConstants.TaskTbField.LASTMOD, System.currentTimeMillis());
		contentResolver.update(DbConstants.CONTENT_URI, values,
				DbConstants.TaskTbField.RESID + " = ? ", new String[] { String
						.valueOf(resId) });
	}

	/**
	 * ���µ�ǰ�ļ������ش�С
	 * 
	 * @param resId
	 * @param currentSize
	 */
	public void updateCurrentSizeById(String resId, Long currentSize) {
		ContentValues values = new ContentValues();
		values.put(DbConstants.TaskTbField.CURRENTSIZE, String
				.valueOf(currentSize));
		values.put(DbConstants.TaskTbField.LASTMOD, System.currentTimeMillis());
		// һ���������أ�����������
		values.put(DbConstants.TaskTbField.FAILCOUNT, 0);
		contentResolver
				.update(DbConstants.CONTENT_URI, values,
						DbConstants.TaskTbField.RESID + " = ? ",
						new String[] { resId });
	}

	/**
	 * ���ת���ķ�������Cursorת����DownloadTask����
	 * 
	 * @param cursor
	 * @return
	 */
	private DownloadTask transformDownloadTask(Cursor cursor) {
		DownloadTask info = new DownloadTask(context);

		Integer id = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField._ID));
		info.id = id;
		String resId = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.RESID));
		info.resourceId = resId;
		info.currentSize = cursor.getLong(cursor
				.getColumnIndex(DbConstants.TaskTbField.CURRENTSIZE));

		info.filePath = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.FILEPATH));
		info.name = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.NAME));
		info.fileName = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.FILENAME));
		info.extendName = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.EXTENDNAME));
		info.taskState = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.TASKSTATE));

		Integer notifyTag = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.NOTIFYTAG));

		notifyTag = (notifyTag == 0) ? id : notifyTag;

		info.notifyTag = notifyTag;
		Long totalSize = cursor.getLong(cursor
				.getColumnIndex(DbConstants.TaskTbField.TOTALSIZE));
		info.totalSize = totalSize;
		info.url = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.URL));
		info.failCount = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.FAILCOUNT));
		info.retryTime = cursor.getLong(cursor
				.getColumnIndex(DbConstants.TaskTbField.RETRYTIME));
		info.fileType = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.FILETYPE));
		info.downType = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.DOWNTYPE));
		info.errorCode = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.ERRORCODE));
		info.isFinish = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.ISFINISH));

		info.metaType = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.METATYPE));
		info.packageName = cursor.getString(cursor
				.getColumnIndex(DbConstants.TaskTbField.PACKAGENAME));

		info.silentMode = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.SILENTMODE));
		info.netState = cursor.getInt(cursor
				.getColumnIndex(DbConstants.TaskTbField.NETSTATE));
		info.createTime = cursor.getLong(cursor
				.getColumnIndex(DbConstants.TaskTbField.CREATETIME));
		info.lastmod = cursor.getLong(cursor
				.getColumnIndex(DbConstants.TaskTbField.LASTMOD));

		if (!LangUtil.isNull(info.filePath)) {
			Long currentSize = info.currentSize;
			int progress = DownloadUtil.getProgress(totalSize, currentSize);
			info.progress = progress;
		}
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
	 * ������ݿ��¼
	 */
	public void trimDatabase() {
		Cursor cursor = context.getContentResolver().query(
				DbConstants.CONTENT_URI,
				new String[] { DbConstants._ID },
				DbConstants.TaskTbField.TASKSTATE + "= '"
						+ DownloadTask.State.SUCCESS + "'", null,
				DbConstants.TaskTbField._ID + " asc ");
		if (cursor.moveToFirst()) {
			int numDelete = cursor.getCount() - SettingUtils.MAX_DOWNLOADS;
			int columnIndex = cursor.getColumnIndexOrThrow(DbConstants._ID);
			while (numDelete > 0) {
				int columnId = cursor.getInt(columnIndex);
				context.getContentResolver().delete(DbConstants.CONTENT_URI,
						_ID + " = ? ",
						new String[] { String.valueOf(columnId) });
				if (!cursor.moveToNext()) {
					break;
				}
				numDelete--;
			}
		}
		cursor.close();
	}

	/**
	 * ͨ��ResIdɾ����������
	 * 
	 */
	public int deleteTaskByResId(String resId) {
		int count = contentResolver
				.delete(DbConstants.CONTENT_URI, DbConstants.TaskTbField.RESID
						+ " = ? ", new String[] { resId });
		return count;
	}

	/**
	 * ͨ�����DownloadInfo
	 * 
	 * @param context
	 * @param packageName
	 * @return DownloadInfo ����������Ϣ�Ķ���
	 */
	public DownloadTask queryTaskByPackageName(Context context,
			String packageName) {
		Cursor c = null;
		DownloadTask downLoadItem = null;
		try {
			c = context.getContentResolver().query(DbConstants.CONTENT_URI,
					new String[] { "*" },
					DbConstants.TaskTbField.PACKAGENAME + " = ? ",
					new String[] { packageName }, null);
			while (c.moveToNext()) {
				downLoadItem = transformDownloadTask(c);
			}
			closeCursor(c);
		} catch (Exception e) {
			e.printStackTrace();
			closeCursor(c);
		} finally {
			closeCursor(c);
		}
		return downLoadItem;
	}

}
