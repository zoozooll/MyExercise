
package com.iskyinfor.duoduo.downloadManage;

/**
 * 下载组件常理管理
 * 
 * @author pKF29007
 */
public class Constants {

    static final String TAG = "DownloaderConstants";
    public static final String SHARE_DOWNLOAD_COUNT="setting_download_count";
    public static final String SHARE_DOWNLOAD_NAME="download_count";
    
    /**
     *正常启动下载服务的表示
     */
   public static final String ACTION_START_SERVICE = "com.iskyinfor.duoduo.downloader.START_SERVICE";
    
    /**
     * 系统服务重启启动时的任务标示
     */
    static final String ACTION_RESTART_SERVICE = "com.iskyinfor.duoduo.downloader.RESTART_SERVICE";

    /**
     * 网络状态变化引起下载服务唤醒的任务标示
     */
    static final String ACTION_NETCHANGE_WAKEUP_SERVICE = "com.iskyinfor.duoduo.download.netchange.wakeup.service";

    /**
     * 完成状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_FINISH = "com.iskyinfor.duoduo.download.finish";

    /**
     * 暂停状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_PAUSE = "com.iskyinfor.duoduo.download.pause";

    /**
     * 下载异常状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_ERROR = "com.iskyinfor.duoduo.download.error";

    /**
     * 正在下载状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_RUNNING = "com.iskyinfor.duoduo.download.running";

    /**
     * 等待状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_WAIT = "com.iskyinfor.duoduo.download.wait";

    /**
     * 准备下载状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_READYSTART = "com.iskyinfor.duoduo.download.readystart";

    /**
     * 取消状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_CANCEL = "com.iskyinfor.duoduo.download.cancel";

    /**
     * 失败状态下载广播标示
     */
    public static final String ACTION_DOWNLOAD_BROADCAST_STATE_FAIL = "com.iskyinfor.duoduo.download.fail";

    /**
     * 通知刷新广播标示
     */
    public static final String ACTION_NOTIFICATION_REFRESH = "com.iskyinfor.duoduo.download.notification.refresh";

    /**
	 * 下载组件发送广播附加Bundle的获取标示
	 */
    public static final String INTENT_BROADCAST_DOWNLOADTASK_FLAG = "intent_broadcast_downloadtask";

    /**
     * 下载组件中广播Bundle中DownloadBundle对象的获取标示
     */
    public static final String TAG_DOWNLOADBUNDLE = "tag_download_bundle_flag";

  
}
