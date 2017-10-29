package com.iskyinfor.duoduo.downloadManage.broadcast;

import java.io.Serializable;

import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.downloadManage.utils.DownloadLog;

/**
 * �����������㲥����
 * 
 * @author pKF29007
 */
public class DownloadBundle implements Serializable {

	private static final long serialVersionUID = -1613593714195967678L;

	/**
	 * ��ԴID
	 */
	private String resId = "";

	/**
	 * ����״̬��DownloadInfo.State����״̬һ��
	 */
	private int taskState = DownloadTask.State.WAIT;

	/**
	 * ���ؽ��
	 */
	private int progress = 0;

	/**
	 * ��ǰ���ش�С
	 */
	private long currentSize = 0;

	/**
	 * �ļ�·���������ļ�
	 */
	private String filePath = "";

	/**
	 * ���������ֶΣ�App��ʾӦ�ð���ͼ���ʾ��ͼƬ��Ӧ��MIME����
	 */
	private String packageName = "";
	/**
	 * ���������ֶ� ͼ���ʾ��ͼƬ��Ӧ��MIME����
	 */
	private String metaType = "";

	/**
	 * ��������Ĵ������ͣ���ӦDownloadInfo.ErrorCode ����ʼֵΪDownload.EMPTY_ERRORCODE
	 */
	private int errorCode = DownloadTask.ErrorCode.EmptyError;

	/**
	 * ����������ļ�����,��ӦDownloadInfo.FileType,��ʼֵΪDownload.EMPTY_FILETYPE
	 */
	private int fileType = DownloadTask.FileType.OHTER;

	/**
	 * �ļ���С
	 */
	private long totalSize = 0;

	/**
	 * �ļ��������ͣ���Ӧ��DownloadInfo.DownType
	 */
	private int downType = DownloadTask.DownType.SDCARD;

	/**
	 * ���������Ӧ��֪ͨ���ʾ����ͬResId�������֪ͨ���ʾһ��
	 */
	private int notifyTag = -1;

	/**
	 * �����ļ���չ��
	 */
	private String extendName = "";
	/**
	 * ������ļ���
	 */
	private String name = "";

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getResId() {
		return resId;
	}

	public void setResId(String resId) {
		this.resId = resId;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getMetaType() {
		return metaType;
	}

	public void setMetaType(String metaType) {
		this.metaType = metaType;
	}

	public int getTaskState() {
		return taskState;
	}

	public void setTaskState(int taskState) {
		this.taskState = taskState;
	}

	public int getProgress() {
		return progress;
	}

	public void setProgress(int progress) {
		this.progress = progress;
	}

	public long getCurrentSize() {
		return currentSize;
	}

	public void setCurrentSize(long currentSize) {
		this.currentSize = currentSize;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public int getFileType() {
		return fileType;
	}

	public void setFileType(int fileType) {
		this.fileType = fileType;
	}

	public long getTotalSize() {
		return totalSize;
	}

	public void setTotalSize(long totalSize) {
		this.totalSize = totalSize;
	}

	public int getDownType() {
		return downType;
	}

	public void setDownType(int downType) {
		this.downType = downType;
	}

	public int getNotifyTag() {
		return notifyTag;
	}

	public void setNotifyTag(int notifyTag) {
		this.notifyTag = notifyTag;
	}

	public String getExtendName() {
		return extendName;
	}

	public void setExtendName(String extendName) {
		this.extendName = extendName;
	}

	public void logSelf(String flag) {
		DownloadLog.d("send broadcast info ");
		DownloadLog.d("flag��:��" + flag);
		DownloadLog.d("Constants.TAG_PROGRESS: " + this.progress);
		DownloadLog.d("Constants.DownType: " + this.downType);
		DownloadLog.d("Constants.TAG_TASKSTATE : " + this.taskState);
		DownloadLog.d("Constants.TAG_RESOURCEID : " + this.resId);
		DownloadLog.d("Constants.TAG_CURRENTSIZE : " + this.currentSize);
		DownloadLog.d("Constants.TAG_TOTALSIZE : " + this.totalSize);
		DownloadLog.d("Constants.TAG_NOTIFYTAG : " + this.notifyTag);
		DownloadLog.d("Constants.TAG_FILEPATH : " + this.filePath);
		DownloadLog.d("Constants.TAG_FILETYPE : " + this.fileType);
		DownloadLog.d("Constants.TAG_SPECIALSIGN : " + this.packageName);
		DownloadLog.d("Constants.TAG_ERRORCODE : " + this.errorCode);
	}
	
}
