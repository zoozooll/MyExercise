package com.iskyinfor.duoduo.downloadManage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

import org.apache.commons.pool.KeyedObjectPool;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.dcfs.esb.client.config.Config;
import com.iskyinfor.duoduo.downloadManage.broadcast.DownloadBroadcastManager;
import com.iskyinfor.duoduo.downloadManage.pool.HttpURLConnectionPool;
import com.iskyinfor.duoduo.downloadManage.provider.ProviderInterface;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.downloadManage.utils.NetworkUtil;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.downloadManage.utils.UrlAnalysisTool;

/**
 * 下载任务执行线程
 * 
 * @author pKF29007
 */
public class DownloadThread extends Thread {

	private Context mContext = null;

	private boolean isRunning = false;

	private DownloadTask mItem = null;

	private KeyedObjectPool mPool = null;

	private ProviderInterface mDataFace = null;

	private OnDownloadEventListener mListener = null;

	private DownloadBroadcastManager mBoradcastManager = null;

	public DownloadThread(Context cxt, DownloadTask queneItem,
			OnDownloadEventListener eventListener) {

		Log.i("pengjun", "DownloadThread builded" + "");
		mContext = cxt;
		mItem = queneItem;
		mPool = HttpURLConnectionPool.getInstance();
		mDataFace = new ProviderInterface(cxt);
		mBoradcastManager = new DownloadBroadcastManager(cxt);
		this.mListener = eventListener;
	}

	public DownloadThread(Context cxt, DownloadTask downItem) {
		this(cxt, downItem, null);
	}

	@Override
	public void run() {

		Log.i("peng", "DownloadThread run mItem url" + mItem.url);
		Log.i("peng", "DownloadThread run" + "");

		isRunning = true;

		int errorCode = preCheck();
		Log.i("peng", "errorCode=====:" + errorCode);
		switch (errorCode) {
		case DownloadTask.ErrorCode.EmptyError:
			doDownload();
			break;

		case DownloadTask.ErrorCode.NetWorkStateError:
		case DownloadTask.ErrorCode.DownloadProcessError:
			doDownloadingError();
			break;

		case DownloadTask.ErrorCode.UrlNotValidError:
		case DownloadTask.ErrorCode.NotEnoughAvailableBlocksError:
		case DownloadTask.ErrorCode.SdCardCanNotUseError:

			doStartDownloadFail();
			if (mListener != null) {
				mListener.downloadFail(mItem);
			}
			break;

		default:
			break;
		}

		mItem.hasActiveThread = false;

	}

	/**
	 * <pre>
	 * 下载任务正式启动前的前置检查方法。用于放置下载条件不符合带来不必要的操作。 
	 * TODO 1. 需要检查URL是否为合法地址 
	 * 
	 * 下载的必要条件 
	 * 1. URL 不可少 
	 * 2. TotalSize 必须获得
	 * 
	 * 
	 * 
	 * </pre>
	 * 
	 * @return
	 */
	private int preCheck() {
		mItem.errorCode = checkSavePath();
		
		if (mItem.errorCode == DownloadTask.ErrorCode.EmptyError) {
		
			mItem.errorCode = checkUrl();
			Log.i("liu", "mItem.errorCode===："+mItem.errorCode);
			if (mItem.errorCode == DownloadTask.ErrorCode.EmptyError) {

				setNotifyTag();
				mDataFace.updateDownloadTask(mItem);

			}
		}
		Log.i("peng", "preCheck errorCode=====:" + mItem.errorCode);
		return mItem.errorCode;
	}

	/**
	 * @return
	 */
	private int checkSavePath() {
		int errorCode = DownloadTask.ErrorCode.EmptyError;
		// 校验SD卡状态
		if (mItem.downType == DownloadTask.DownType.FILE_CACHE
				&& mItem.downType == DownloadTask.DownType.SYSTEM_CACHE) {
			if (!SdcardUtil.isAvailableBlocks(mContext.getFilesDir(),
					mItem.totalSize)) {
				errorCode = DownloadTask.ErrorCode.NotEnoughAvailableBlocksError;
			} else {

			}
		} else if (mItem.downType == DownloadTask.DownType.SDCARD
				&& mItem.downType == DownloadTask.DownType.CUSTOM) {
			if (!SdcardUtil.checkSdcardExist()) {
				errorCode = DownloadTask.ErrorCode.SdCardCanNotUseError;
			}
			if (!SdcardUtil.isAvailableBlocks(Environment
					.getExternalStorageDirectory(), mItem.totalSize)) {
				errorCode = DownloadTask.ErrorCode.NotEnoughAvailableBlocksError;
			}
		}

		if (errorCode == DownloadTask.ErrorCode.EmptyError) {
			// setFileSavePath(mContext);
			SdcardUtil.mkdirByPath(mItem.filePath);
			setFileInfo();
		}
		Log.i("peng", "checkSavePath()=====:" + errorCode);
		return errorCode;
	}

	/**
	 * 设置NotifyTag
	 */
	private void setNotifyTag() {
		mItem.notifyTag = (mItem.notifyTag == 0) ? mItem.id : mItem.notifyTag;
	}

	/**
	 * 检查URL地址
	 */
	private int checkUrl() {
		mItem.totalSize = DownloadUtil.getTotalSize(mItem.url);
		// 添加URL连接性校验 Url的非空在task初始化时已经校验过
		Log.i("liu", "mItem.url=====:"+mItem.url);
		if (!UrlAnalysisTool.urlSniffer(mItem.url) || mItem.totalSize <= 0L) {
			Log.i("liu", "UrlNotValidError=====:");
			return DownloadTask.ErrorCode.UrlNotValidError;
		} else {
			return DownloadTask.ErrorCode.EmptyError;
		}

	}

	/**
	 * 下载过程处理
	 */
	private void doDownload() {
		Log.i("liu", "start doDownload=====:");
		// 设置辅助参数
		boolean isFirst = true;
		Long refushTime = System.currentTimeMillis();
		int refushSize = 0;

		// 初始化参数
		File tempFile = mItem.file;

		InputStream ins = null;
		FileOutputStream fos = null;
		HttpURLConnection conn = null;

		try {

			mItem.taskState = DownloadTask.State.RUNNING;
			mItem.errorCode = DownloadTask.ErrorCode.EmptyError;
			mItem.failCount = 0;
			mItem.refreshSelfProgress();
			mDataFace.updateDownloadTask(mItem);

			conn = (HttpURLConnection) mPool.borrowObject(mItem.url);
			
			conn.setRequestProperty("uid", Config.getLogProperty(Config.UID));
			//conn.setRequestProperty("series", strDes);
			conn.setRequestProperty("workDay", null);
			conn.setRequestProperty("flag", "A");
			
			
			
			
			
			
//			conn.setRequestProperty("User-Agent", "space");

			Log.i("peng5", "currentSize : " + mItem.currentSize);
			conn
					.setRequestProperty("RANGE", "bytes=" + mItem.currentSize
							+ "-");
			conn.connect();
			Log.i("peng5", "conn.getContentLength:"
					+ conn.getContentLength());
			Log.i("peng5", "conn.getCurrentSize:" + mItem.currentSize);
			Log.i("peng5", "conn.getTotalSize:" + mItem.totalSize);

			// 不支持断点续传
			if (mItem.currentSize > 0
					&& (conn.getContentLength() + mItem.currentSize) > mItem.totalSize) {
				mItem.currentSize = 0;
				SdcardUtil.deleteFile(mItem.file);
			}

			ins = conn.getInputStream();

			if (mItem.downType == DownloadTask.DownType.FILE_CACHE) {
				fos = mContext.openFileOutput(tempFile.getName(),
						Context.MODE_APPEND | Context.MODE_PRIVATE
								| Context.MODE_WORLD_READABLE
								| Context.MODE_WORLD_WRITEABLE);
			} else {
				fos = new FileOutputStream(mItem.file.getPath(), true);
			}

			byte[] b = new byte[SettingUtils.DOWNLOAD_CACHE_SIZE];

			int read = 0;
			int readTotal = 0;
			Log.i("liu", "byte size===:"+b.length);
			while ((read = ins.read(b, 0, SettingUtils.DOWNLOAD_CACHE_SIZE)) > 0) {

				Thread.sleep(20);
				Log.i("liu", "read size===:"+read);
				if (!this.isCancelled()) {
					fos.write(b, 0, read);
					refushSize += read;
					readTotal += read;
					long now = System.currentTimeMillis();
					if (isFirst == true
							|| refushSize > SettingUtils.MIN_REFRUSH_SIZE
							|| (now - refushTime) > SettingUtils.MIN_REFRUSH_TIME) {

						mItem.refreshSelfProgress();
						mDataFace.updateCurrentSizeById(mItem.resourceId,
								mItem.currentSize);
						// mDataFace.updateDownloadTask(mItem);

						mBoradcastManager
								.sendDownloadBroadcast(
										mContext,
										Constants.ACTION_DOWNLOAD_BROADCAST_STATE_RUNNING,
										mItem);

						if (mListener != null) {
							mListener.downloadRunning(mItem);
						}

						isFirst = false;
						refushSize = 0;
						refushTime = now;

					}
				} else {

					if (mItem.taskState == DownloadTask.State.CANCEL) {
						if (mListener != null) {
							mListener.downloadCancel(mItem);
							SdcardUtil.deleteFile(mItem.file);
						}
					}
					if (mItem.taskState == DownloadTask.State.PAUSE) {
						if (mListener != null) {
							mListener.downloadPause(mItem);
						}
					}

					mItem.refreshSelfProgress();
					mDataFace.updateCurrentSizeById(mItem.resourceId,
							mItem.currentSize);

					break;
				}
			}

			// 下载完成
			fos.flush();
			mPool.returnObject(null, conn);
			mItem.refreshSelfProgress();

			// 确保是正常下载完成
			if (mItem.taskState != DownloadTask.State.PAUSE
					&& mItem.taskState != DownloadTask.State.CANCEL) {

				if (tempFile.length() == mItem.totalSize) {
					doDownloadFinish(tempFile);
				} else {
					doDownloadingError();
				}

			}

			return;
		} catch (Exception e) {
			e.printStackTrace();
			doDownloadingError();
		} finally {
			try {
				mPool.returnObject(null, conn);
				if (fos != null) {
					fos.close();
				}
				if (ins != null) {
					ins.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @param tempFile
	 * @throws Exception
	 */
	private void doDownloadFinish(File tempFile) throws Exception {
		mItem.refreshSelfProgress();
		mItem.refreshSelfState(DownloadTask.State.SUCCESS);

		// FIXME 在这里处理 文件覆盖
		File file = new File(tempFile.getPath() + File.separator
				+ mItem.fileName);
		if (file.exists()) {
			file.delete();
		}

		tempFile = SdcardUtil.renameFile(tempFile, mItem.fileName);

		mItem.file = tempFile;
		mItem.filePath = tempFile.getParent();

		mDataFace.updateDownloadTask(mItem);

		mBoradcastManager.sendDownloadBroadcast(mContext,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FINISH, mItem);

		if (mListener != null) {
			mListener.downloadFinish(mItem);
		}

	}

	/**
	 * 处理下载过程过程中出现的异常
	 */
	private void doDownloadingError() {

		// 如果下载文件失败，则删除下载的临时文件
		if (mItem.file.length() > mItem.totalSize) {
			SdcardUtil.deleteFile(mItem.file);
		}

		if (NetworkUtil.hasActiveNetwork(mContext)) {
			mItem.errorCode = DownloadTask.ErrorCode.DownloadProcessError;
		} else {
			mItem.errorCode = DownloadTask.ErrorCode.NetWorkStateError;
		}
		mItem.failCount = (mItem.failCount + 1);

		mItem.retryTime = nextAction(mItem.failCount);
		mItem.refreshSelfState(DownloadTask.State.ERROR);
		mDataFace.updateDownloadTask(mItem);
		mBoradcastManager.sendDownloadBroadcast(mContext,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_ERROR, mItem);

		if (mListener != null) {
			mListener.downloadError(mItem);
		}
	}

	/**
	 * 是否取消下载
	 * 
	 * @return
	 */
	public boolean isCancelled() {
		return !isRunning;
	}

	/**
	 * 取消下载
	 */
	public void cancel() {
		isRunning = false;
	}

	/**
	 * 设定任务的下一次启动时间
	 * 
	 * @param failCount
	 * @return
	 */
	private long nextAction(int failCount) {
		int wakeUp = SettingUtils.BASE_RETRY_TIME + failCount
				* randomRetryTime();
		Long now = System.currentTimeMillis();
		return now + wakeUp;
	}

	/**
	 * 
	 * 调整重试的时间基数
	 * 
	 * @return 调整时间
	 */
	private int randomRetryTime() {
		return (int) ((Math.random() * SettingUtils.BASE_RETRY_TIME * 100) / 100) * 1000;
	}

	/**
	 * 构建临时文件
	 * 
	 * @return
	 */
	private void setFileInfo() {
if(mItem.fileName!=null&&mItem.fileName.contains(".")){
	String temp=mItem.fileName.substring(0,mItem.fileName.indexOf("."));
	
	String tempName = mItem.filePath + File.separator + temp
			+ SettingUtils.TEMP_FIlE_SUFFIX;
	Log.i("peng5", "tempName===:"+tempName);
	File tempFile = new File(tempName);
	if (!tempFile.exists()) {
		try {
			tempFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	mItem.file = tempFile;
	mItem.currentSize = DownloadUtil.getCurrentSize(tempFile);
	mItem.progress = DownloadUtil.getProgress(mItem.totalSize,
			mItem.currentSize);
}
	}

	/**
	 * 启动下载失败
	 * 
	 * @param errorCode
	 */
	private void doStartDownloadFail() {
		mItem.taskState = DownloadTask.State.FAIL;
		mItem.failCount = mItem.failCount + 1;
		mDataFace.updateDownloadTask(mItem);

		mBoradcastManager.sendDownloadBroadcast(mContext,
				Constants.ACTION_DOWNLOAD_BROADCAST_STATE_ERROR, mItem);
	}
}
