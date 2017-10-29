package com.iskyinfor.duoduo.downloadManage;

import java.io.File;
import java.io.Serializable;

import android.content.Context;
import android.os.Environment;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.iskyinfor.duoduo.downloadManage.mime.MimeUtils;
import com.iskyinfor.duoduo.downloadManage.notification.DefaultDownloadEventListenerImpl;
import com.iskyinfor.duoduo.downloadManage.setting.SettingUtils;
import com.iskyinfor.duoduo.downloadManage.utils.LangUtil;
import com.iskyinfor.duoduo.downloadManage.utils.MD5;
import com.iskyinfor.duoduo.downloadManage.utils.NetworkUtil;

public class DownloadTask implements Parcelable, Serializable {

	private static final long serialVersionUID = -1853067772203482885L;

	/**
	 * 应用下载文件类型
	 */
	public static final class FileType {
		public static final int APPLICATION = 1;
		public static final int IMAGE = 2;
		public static final int AUDIO = 3;
		public static final int VIDEO = 4;
		public static final int TEXT = 5;
		public static final int OHTER = 6;
		public static final int UNKOWN = -1;
	}

	/**
	 * 应用保存路径类型
	 */
	public static final class DownType {
		public static final int FILE_CACHE = 1;
		public static final int SDCARD = 2;
		public static final int SYSTEM_CACHE = 3;
		public static final int CUSTOM = 4;
	}

	/**
	 * <pre>
	 * 定义任务的运行状态
	 * WAIT : 等待下载
	 * RUNNING ： 正在下载
	 * FINISH ：完成下载
	 * FAIL ： 下载失败
	 * PAUSE ： 暂停下载
	 * ERROR ： 执行过程错误
	 * CANCEL : 取消下载任务
	 * CANNOTADD :  添加任务失败
	 * </pre>
	 * 
	 */
	public static final class State {
		public static final int WAIT = 5;
		public static final int RUNNING = 1;
		public static final int SUCCESS = 3;
		public static final int FAIL = 6;
		public static final int PAUSE = 2;
		public static final int ERROR = 4;
		public static final int CANCEL = 7;
		public static final int CANNOTADD = -1;
	}

	/**
	 * 下载异常类型
	 */
	public static final class ErrorCode {
		public final static int EmptyError = -1;
		public final static int SdCardCanNotUseError = 1;
		public final static int DownloadProcessError = 2;
		public final static int NotEnoughAvailableBlocksError = 3;
		public final static int NetWorkStateError = 4;
		public final static int UrlNotValidError = 5;
	}

	/**
	 * 文件下载完成状态
	 */
	public static final class FinishState {
		public static final int TASK_FINISH_STATE = 1;
		public static final int TASK_UNFINISH_STATE = 0;
	}

	/**
	 * 文件下载的网络状态
	 */
	public static final class NetState {
		public static final int MOBILE = 1;
		public static final int WIFI = 2;
	}

	/**
	 * 文件下载的静默模式
	 */
	public static final class SilentMode {
		public static final int NOTIFICATION = 1;
		public static final int BROADCAST = 2;
	}

	/**
	 * 历史记录状态
	 */
	public static final class HistoryState {
		public static final int NEW_TASK = 0; // : 第一次下载的任务，可以直接添加下载地址 ;
		public static final int DOWNLOADING_TASK = 1;// : 正在下载的任务，取消任务后可重新下载
		public static final int DOWNLOADED_TASK = 2;// ：已下载的网络任务，添加下载会覆盖相关记录
	}

	/**
	 * 下载任务的主键ID
	 */
	public int id;

	/**
	 * 下载进度 (MAX_PROGRESS = 1000)
	 */
	public int progress = 0;

	/**
	 * 下载任务状态
	 */
	public int taskState = State.WAIT;

	/**
	 * 　资源编号 "url+当前时间"
	 */
	public String resourceId;

	/**
	 * 当前文件大小
	 */
	public long currentSize = 0L;

	/**
	 * 总文件大小
	 */
	public long totalSize = 0L;

	/**
	 * 通知标示
	 */
	public int notifyTag;

	/**
	 * 文件保存路径： 只包括文件路径，不包括文件的名称
	 */
	public String filePath = "";

	/**
	 * 文件类型 用于辅助决定文件的位置 FileType为空的时候，通过文件的MetaType帮助确定
	 */
	public int fileType = FileType.UNKOWN;

	/**
	 * 错误类型 下载过程中发生错误的类型
	 */
	public int errorCode = ErrorCode.EmptyError;

	/**
	 * 文件名称： 用于文件
	 */
	public String name = "";

	/**
	 * 文件下载路径
	 */
	public String url = null;

	/**
	 * 文件下载保存名称
	 */
	public String fileName = null;

	/**
	 * 文件下载类型 用于决定文件的保存的根路径
	 */
	public int downType = DownType.SDCARD;

	/**
	 * 文件扩展名
	 */
	public String extendName = "";

	/**
	 * 文件MetaType 通过文件扩展名获取
	 */
	public String metaType = "";

	/**
	 * 包名，只针对Application对象
	 */
	public String packageName = "";

	/**
	 * 任务的添加时间
	 */
	public long createTime = System.currentTimeMillis();

	/**
	 * TODO 新增 是否开启安静模式
	 */
	public int silentMode = SilentMode.BROADCAST | SilentMode.NOTIFICATION;

	/**
	 * 重试次数时间
	 */
	public long retryTime = -1L;

	/**
	 * 失败次数，连续三次尝试下载失败后下载任务失败
	 */
	public int failCount = 0;

	/**
	 * 文件是否完成的标示
	 */
	public int isFinish = FinishState.TASK_UNFINISH_STATE;

	/**
	 * 最后修改时间
	 */
	public long lastmod = System.currentTimeMillis();

	/**
	 * 标示任务是否已同步,每次检查前同步标示会被统一设置为False，随着同步进程分别设置每个任务的标示
	 */
	boolean isSync = false;

	/**
	 * 下载文件
	 */
	public File file = null;

	/**
	 * 下载线程
	 */
	private DownloadThread task = null;

	/**
	 * 是否已启动下载线程
	 */
	boolean hasActiveThread = false;

	/**
	 * 允许执行该下载任务的网络状态
	 */
	public int netState = NetState.MOBILE | NetState.WIFI;

	private Context mContext;

	public DownloadTask(Context context) {
		mContext = context;
	}

	public void setDefaultValueIfNeed() {

		// FIXME 校验失败后的异常处理
		if (LangUtil.isNull(this.url)) {
			throw new IllegalArgumentException(
					"DownloadInfo:url can not be Null");
		}

		if (LangUtil.isNull(resourceId)) {
			resourceId = buildResourceId(url);
		}

		if (LangUtil.isNull(fileName)) {
			fileName = NetworkUtil.getFileName(url);
		}

		if (LangUtil.isNull(extendName)) {
			extendName = NetworkUtil.getExtension(url);

		}

		if (!LangUtil.isNull(extendName)) {
			metaType = MimeUtils.getMimeTypes(mContext).getMimeTypeByExtension(
					extendName);
			fileType = DownloadTask.getFileType(metaType);
		}

		if (LangUtil.isNull(name)) {
			name = fileName;
		}

		if (downType == DownType.CUSTOM && !LangUtil.isNull(filePath)) {
			File file = new File(filePath);
			if (!(file.exists() && file.isDirectory())) {
				file.delete();
				if (!file.mkdirs()) {
					throw new IllegalArgumentException(
							"DownloadInfo:filePath is wrong files address");
				}
			}
		} else {
			downType = DownType.SDCARD;
			filePath = "";
			setFilePath(mContext);
		}

		if (!checkSilentMode()) {
			silentMode = SilentMode.BROADCAST | SilentMode.NOTIFICATION;
		}

		if (!checkNetState()) {
			netState = NetState.MOBILE | NetState.WIFI;
		}

	}

	/**
	 * 检查静默模式的值是否为指定值
	 * 
	 * @return
	 */
	private boolean checkSilentMode() {

		boolean isbro = ((silentMode & SilentMode.BROADCAST) != 0);
		boolean isnot = ((silentMode & SilentMode.NOTIFICATION) != 0);

		return (isnot || isbro);

	}

	/**
	 * 检查网络状态的值是否为指定值
	 * 
	 * @return
	 */
	private boolean checkNetState() {
		return ((netState & NetState.MOBILE) != 0)
				|| ((netState & NetState.WIFI) != 0);
	}

	private DownloadTask(Parcel in) {
		readFromParcel(in);
	}

	public static final Parcelable.Creator<DownloadTask> CREATOR = new Parcelable.Creator<DownloadTask>() {
		public DownloadTask createFromParcel(Parcel in) {
			return new DownloadTask(in);
		}

		public DownloadTask[] newArray(int size) {
			return new DownloadTask[size];
		}
	};

	private void readFromParcel(Parcel in) {

		id = in.readInt();
		progress = in.readInt();
		taskState = in.readInt();
		resourceId = in.readString();
		currentSize = in.readLong();
		totalSize = in.readLong();
		notifyTag = in.readInt();
		filePath = in.readString();
		fileType = in.readInt();
		errorCode = in.readInt();
		name = in.readString();
		url = in.readString();
		fileName = in.readString();
		downType = in.readInt();
		extendName = in.readString();
		metaType = in.readString();

		retryTime = in.readLong();
		failCount = in.readInt();
		isFinish = in.readInt();
		lastmod = in.readLong();

		createTime = in.readLong();
		silentMode = in.readInt();
		netState = in.readInt();

	}

	@Override
	public void writeToParcel(Parcel out, int arg1) {
		out.writeInt(id);
		out.writeInt(progress);
		out.writeInt(taskState);
		out.writeString(resourceId);
		out.writeLong(currentSize);
		out.writeLong(totalSize);
		out.writeInt(notifyTag);
		out.writeString(filePath);
		out.writeInt(fileType);

		out.writeInt(errorCode);
		out.writeString(name);
		out.writeString(url);
		out.writeString(fileName);

		out.writeInt(downType);
		out.writeString(extendName);

		out.writeLong(retryTime);
		out.writeInt(failCount);
		out.writeInt(isFinish);
		out.writeLong(lastmod);

		out.writeInt(silentMode);
		out.writeInt(netState);
		out.writeLong(createTime);
		out.writeString(metaType);

	}

	@Override
	public int describeContents() {
		return 0;
	}

	/**
	 * 刷新设置状态.
	 * 
	 * @param taskState
	 *            任务进度
	 */
	public void refreshSelfState(int taskState) {
		this.taskState = taskState;
	}

	/**
	 * 刷新当前大小和进度条
	 */
	public void refreshSelfProgress() {

		this.currentSize = DownloadUtil.getCurrentSize(this.file);
		this.progress = DownloadUtil.getProgress(this.totalSize, currentSize);

	}

	@Override
	public int hashCode() {
		return 0;
	}

	@Override
	public boolean equals(Object obj) {
		boolean isEqual = false;
		if (!(obj instanceof DownloadTask)) {
			return isEqual;
		} else {
			DownloadTask item = (DownloadTask) obj;
			if (this.resourceId.equals(item.resourceId)) {
				isEqual = true;
			}
		}
		return isEqual;
	}

	/**
	 * 通过meta-type获取文件类型
	 * 
	 * @param metaType
	 * @return 文件类型
	 */
	public static int getFileType(String metaType) {
		int fileType = FileType.OHTER;
		if (!LangUtil.isNull(metaType)) {
			if (metaType.startsWith("image")) {
				fileType = FileType.IMAGE;
			} else if (metaType.startsWith("audio")) {
				fileType = FileType.AUDIO;
			} else if (metaType.startsWith("video")) {
				fileType = FileType.VIDEO;
			} else if (metaType.startsWith("application")) {
				fileType = FileType.APPLICATION;
			} else if (metaType.startsWith("text")) {
				fileType = FileType.TEXT;
			} else {
				fileType = FileType.OHTER;
			}
		}
		return fileType;
	}

	public void logSelf(String tag, String flag) {
		Log.i(tag, flag);
		Log.i(tag, "id:" + id);
		Log.i(tag, "progress:" + progress);
		Log.i(tag, "taskState:" + taskState);
		Log.i(tag, "resourceId:" + resourceId);
		Log.i(tag, "currentSize:" + currentSize);
		Log.i(tag, "totalSize:" + totalSize);
		Log.i(tag, "notifyTag:" + notifyTag);
		Log.i(tag, "filePath:" + filePath);
		Log.i(tag, "fileType:" + fileType);

		Log.i(tag, "errorCode:" + errorCode);
		Log.i(tag, "name:" + name);
		Log.i(tag, "url:" + url);
		Log.i(tag, "fileName:" + fileName);

		Log.i(tag, "downType:" + downType);
		Log.i(tag, "extendName:" + extendName);
		Log.i(tag, "metaType:" + metaType);
		Log.i(tag, "packageName:" + packageName);
		Log.i(tag, "createTime:" + createTime);

		Log.i(tag, "silentMode:" + silentMode);
		Log.i(tag, "retryTime:" + retryTime);
		Log.i(tag, "failCount:" + failCount);
		Log.i(tag, "isFinish:" + isFinish);

		Log.i(tag, "lastmod:" + lastmod);
		Log.i(tag, "silentMode:" + silentMode);
		Log.i(tag, "netState:" + netState);
		Log.i(tag, "createTime:" + createTime);
		Log.i(tag, "isSync:" + isSync);

		Log.i(tag, "file:" + (file != null ? file.getPath() : ""));
		Log.i(tag, "task:" + (task != null ? task.getName() : ""));

		Log.i(tag, "hasActiveThread:" + hasActiveThread);

	}

	/**
	 * 启动下载任务
	 * 
	 * @param cxt
	 * @param item
	 */
	public void startTask(Context cxt) {

		if (task != null && task.isCancelled()) {
			task.start();
		} else {
			DefaultDownloadEventListenerImpl listener = new DefaultDownloadEventListenerImpl(
					cxt);
			task = new DownloadThread(cxt, this, listener);
			task.start();
		}
		this.hasActiveThread = true;
	}

	/**
	 * 停止下载任务
	 * 
	 * @param item
	 */
	public void stopTask(int taskState) {
		if (task != null && !task.isCancelled()) {
			task.cancel();
		}
		this.taskState = taskState;
		this.task = null;
		this.hasActiveThread = false;
	}

	/**
	 * 生成资源文件 resourceId
	 * 
	 * @param sign
	 * @return
	 */
	static String buildResourceId(String sign) {
		MD5 m = new MD5();
		String md5Str = m.getMD5ofStr(sign);
		return md5Str;
	}

	/**
	 * 获取文件保存路径
	 * 
	 * @param context
	 * @return
	 */
	private void setFilePath(Context context) {
		if (LangUtil.isNull(filePath)) {

			if (downType == DownloadTask.DownType.FILE_CACHE) {
				filePath = context.getFilesDir().getPath();

			} else if (downType == DownloadTask.DownType.SYSTEM_CACHE) {
				filePath = context.getCacheDir().getPath();

			} else if (downType == DownloadTask.DownType.SDCARD) {
				filePath = Environment.getExternalStorageDirectory()
						+ File.separator + SettingUtils.DOWNLOAD_SAVE_BASE_DIR;

				switch (fileType) {
				case DownloadTask.FileType.APPLICATION:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				case DownloadTask.FileType.AUDIO:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				case DownloadTask.FileType.IMAGE:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				case DownloadTask.FileType.VIDEO:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				case DownloadTask.FileType.TEXT:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				case DownloadTask.FileType.OHTER:
					filePath = filePath + File.separator
							+ SettingUtils.SAVE_APPLICATION_DIR;
					break;

				default:
					break;
				}
			}
		}
	}

}
