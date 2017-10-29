package com.iskyinfor.duoduo.downloadManage;


/**
 * 下载处理过程事件监听接口
 * @author pKF29007
 *
 */
public interface OnDownloadEventListener {
	
//    /**
//     * 下载任务中进入准备下载状态
//     * @param item
//     */
//	public void downloadPreStart(DownloadTask item );

	  /**
     * 下载任务中进入正在下载状态
     * @param item
     */
	public void downloadRunning(DownloadTask item);

	  /**
     * 下载任务中进入完成下载状态
     * @param item
     */
	public void downloadFinish(DownloadTask item);

	  /**
     * 下载任务中进入异常下载状态
     * @param item
     */
	public void downloadError(DownloadTask item);	

	  /**
     * 下载任务中进入取消下载状态
     * @param item
     */
	public void downloadCancel(DownloadTask item);
	
	  /**
     * 下载任务中进入异常下载状态
     * @param item
     */
	public void downloadFail(DownloadTask item);	
	
	/**
	 * 下载任务中进入暂停下载状态
	 * @param item
	 */
	public void downloadPause(DownloadTask item);	
}
