package com.mogoo.components.download;

import java.io.File;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import cn.imogoo.providers.downloads.Downloads;

abstract class MogooDownloadBase {

	/**
	 * 使用移动网络进行下载
	 */
	public static final int NETWORK_MOBILE = 0;
	/**
	 * 使用WIFI网络进行下载
	 */
	public static final int NETWORK_WIFI = 1;
	// 使用的网络方式，缺省使用全部的网路链接进行下载
	private int mAllowedNetworkTypes = ~0;
	// 需要下载的文件的uri
	private Uri mUri;
	private Uri mDestinationUri;
	// 在状态栏显示的标题
	private CharSequence mTitle;
	private CharSequence mDescription;
	// 是否在状态栏显示消息
	private boolean mShowNotification = true;
	// private String mMimeType;
	// 是否允许在漫游的情况下进行下载，默认为允许
	private boolean mRoamingAllowed = true;
	// 是否显示下载管理器
	private boolean mIsVisibleInDownloadsUi = false;

	/**
	 * 下载完毕后发出的广播(不一定是下载成功)
	 */
	public final static String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";

	/**
	 * 用来打开下载管理器的 action 备用
	 */
	public final static String ACTION_VIEW_DOWNLOADS = "android.intent.action.VIEW_DOWNLOADS";

	/**
	 * 从intent里获取下载完成的任务ID需要用到的KEY
	 */
	public static final String EXTRA_DOWNLOAD_ID = "extra_download_id";

	/**
	 * 开始进行下载
	 * 
	 * @param url
	 *            需要下载的文件的具体地址
	 * @return 对应此下载任务的一个ID，是独一无二，自动产生，将来操作此任务或查询此下载任务时都要用到；<br>
	 *         下载失败的话返回-1
	 */
	public abstract long startDownload(String url);

	/**
	 * 取消某个下载任务
	 * 
	 * @param downloadId
	 *            启动startDownload方法时返回的值
	 * @return 失败时返回-1，成功的话返回大于0的数
	 */
	public abstract long cancelDownload(long... downloadId);

//	public abstract void restartDownload(long... downloadId);

	protected void setDownloadUrl(String url) {
		Uri uri = Uri.parse(url);
		if (uri == null) {
			throw new NullPointerException();
		}
		String scheme = uri.getScheme();
		if (scheme == null || !scheme.equals("http")) {
			throw new IllegalArgumentException("Can only download HTTP URIs: "
					+ uri);
		}
		mUri = uri;
	}

	// public void setDestinationUri(Uri uri)
	// {
	// mDestinationUri = uri;
	// }

	/**
	 * 设置保存到SD卡的路径
	 * 
	 * @param context
	 * @param dirType
	 *            比如要保存到SD卡的"mogoo"目录下，那设置为"mogoo"即可
	 * @param subPath
	 *            可以看做是保存的文件名，如"hello.mp3"
	 */
	public void setDestinationInExternalFilesDir(Context context,
			String dirType, String subPath) {
		setDestinationFromBase(context.getExternalFilesDir(dirType), subPath);
	}

	/**
	 * 
	 * 设置保存到SD卡的路径
	 * 
	 * @param dirType
	 *            比如要保存到SD卡的"mogoo"目录下，那设置为"mogoo"即可
	 * @param subPath
	 *            可以看做是保存的文件名，如"hello.mp3"
	 */
	public void setDestinationInExternalPublicDir(String dirType, String subPath) {
		setDestinationFromBase(
				Environment.getExternalStoragePublicDirectory(dirType), subPath);
	}

	private void setDestinationFromBase(File base, String subPath) {
		if (subPath == null) {
			throw new NullPointerException("subPath cannot be null");
		}
		mDestinationUri = Uri.withAppendedPath(Uri.fromFile(base), subPath);
	}

	public void setTitle(CharSequence title) {
		mTitle = title;
	}

	public void setDescription(CharSequence description) {
		mDescription = description;
	}

	//
	// public void setMimeType(String mimeType)
	// {
	// mMimeType = mimeType;
	// }

	public void setShowRunningNotification(boolean show) {
		mShowNotification = show;
	}

	/**
	 * 使用的网络方式，缺省使用全部的网路链接进行下载
	 * 
	 * @param flags
	 *            NETWORK_MOBILE 或 NETWORK_WIFI
	 */
	public void setAllowedNetworkTypes(int flags) {
		mAllowedNetworkTypes = flags;
	}

	public void setAllowedOverRoaming(boolean allowed) {
		mRoamingAllowed = allowed;
	}

	// public void setVisibleInDownloadsUi(boolean isVisible)
	// {
	// mIsVisibleInDownloadsUi = isVisible;
	// }
	
	
	protected ContentValues getPauseContentValues(String packageName){
	    ContentValues values = new ContentValues();
        assert mUri != null;
        values.put(Downloads.Impl.COLUMN_CONTROL, Downloads.Impl.CONTROL_PAUSED);
        return values;
        
	}
	
	/**
	 * 获取更改继续下载字段值
	 * 
	 * @param packageName
	 * @return
	 */
	protected ContentValues getRestartContentValues(String packageName){
        ContentValues values = new ContentValues();
        assert mUri != null;
        values.put(Downloads.Impl.COLUMN_CONTROL, Downloads.Impl.CONTROL_RUN);
        values.put(Downloads.Impl.COLUMN_STATUS, Downloads.Impl.STATUS_RUNNING);
        
        return values;
        
    }
	
	/**
	 * 用一个ContentValues对象返回所有设置好的网络参数
	 * 
	 * @param packageName
	 * @return
	 */
	protected ContentValues toContentValues(String packageName) {
		ContentValues values = new ContentValues();
		assert mUri != null;
		values.put(Downloads.Impl.COLUMN_URI, mUri.toString());
		values.put(Downloads.Impl.COLUMN_IS_PUBLIC_API, true);
		values.put(Downloads.Impl.COLUMN_NOTIFICATION_PACKAGE, packageName);

		if (mDestinationUri != null) {
			values.put(Downloads.Impl.COLUMN_DESTINATION,
					Downloads.Impl.DESTINATION_FILE_URI);
			values.put(Downloads.Impl.COLUMN_FILE_NAME_HINT,
					mDestinationUri.toString());
		} else {
			values.put(Downloads.Impl.COLUMN_DESTINATION,
					Downloads.Impl.DESTINATION_CACHE_PARTITION_PURGEABLE);
		}

		putIfNonNull(values, Downloads.Impl.COLUMN_TITLE, mTitle);
		putIfNonNull(values, Downloads.Impl.COLUMN_DESCRIPTION, mDescription);
		// putIfNonNull(values, Downloads.COLUMN_MIME_TYPE, mMimeType);

		values.put(Downloads.Impl.COLUMN_VISIBILITY,
				mShowNotification ? Downloads.Impl.VISIBILITY_VISIBLE
						: Downloads.Impl.VISIBILITY_HIDDEN);

		values.put(Downloads.Impl.COLUMN_ALLOWED_NETWORK_TYPES,
				mAllowedNetworkTypes);
		values.put(Downloads.Impl.COLUMN_ALLOW_ROAMING, mRoamingAllowed);
		// values.put(Downloads.Impl.COLUMN_IS_VISIBLE_IN_DOWNLOADS_UI,
		// mIsVisibleInDownloadsUi);

		return values;
	}

	private void putIfNonNull(ContentValues contentValues, String key,
			Object value) {
		if (value != null) {
			contentValues.put(key, value.toString());
		}
	}
}
