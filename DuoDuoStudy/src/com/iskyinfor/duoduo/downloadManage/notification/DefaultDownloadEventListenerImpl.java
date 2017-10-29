package com.iskyinfor.duoduo.downloadManage.notification;

import android.content.Context;

import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.downloadManage.OnDownloadEventListener;

/**
 * Ĭ��DownloadEventListenerʵ��
 * TODO ȡ�����Ĭ��ʵ�� ��Ϊ����������
 * TODO ֧��֪ͨ��ʽ���Զ���ģʽ
 * 
 * @author pKF29007
 * 
 */
public class DefaultDownloadEventListenerImpl implements
		OnDownloadEventListener {

	private DownloadNotificationManager notificationManager = null;

	public DefaultDownloadEventListenerImpl(Context cxt) {
		notificationManager = new DownloadNotificationManager(cxt);
	}

	@Override
	public void downloadError(DownloadTask item) {
		notificationManager.sendErrorNotification(item);
	}

	@Override
	public void downloadFinish(DownloadTask item) {
		notificationManager.sendFinishNotification(item);
	}

	@Override
	public void downloadCancel(DownloadTask item) {
		notificationManager.sendCancelNotification(item);
	}

	@Override
	public void downloadRunning(DownloadTask item) {
		notificationManager.sendRunningNotification(item);
	}

//	@Override
//	public void downloadPreStart(DownloadTask item) {
//		notificationManager.sendReadyStartNotification(item);
//	}

	@Override
	public void downloadFail(DownloadTask item) {
		notificationManager.sendFailNotification(item);
		
	}

	@Override
	public void downloadPause(DownloadTask item) {
		notificationManager.sendPauseNotification(item);
		
	}

}
