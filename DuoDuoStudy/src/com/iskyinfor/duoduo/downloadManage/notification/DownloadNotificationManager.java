package com.iskyinfor.duoduo.downloadManage.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.DownloadTask;
import com.iskyinfor.duoduo.downloadManage.DownloadUtil;

public class DownloadNotificationManager {
	public static final String TAG = "DownloadNotificationManager";

	// public static final String COMEFROM_NOTIC = "comeFromNotic";
	public static final String APP_ID = "appId";
	private Context context;

	public DownloadNotificationManager(Context ctx) {
		context = ctx;
	}

	/**
	 * ����Զ����ͼ
	 * 
	 * @param item
	 * @return
	 */
	private RemoteViews createRemoteView(DownloadTask item) {
		RemoteViews rv = new RemoteViews(this.context.getPackageName(),
				R.layout.download_notification_down_info);
		if (item != null) {
			rv.setTextViewText(R.id.down_name, item.name);
			rv
					.setProgressBar(R.id.down_progress_bar, 1000,
							item.progress, true);
		}
		return rv;
	}

	/**
	 * ����������ɣ������쳣������ʧ�ܵ�Զ����ͼ
	 * 
	 * @param item
	 * @return
	 */

	private RemoteViews createRemoteCompleteView(DownloadTask item) {
		RemoteViews rv = new RemoteViews(this.context.getPackageName(),
				R.layout.download_notification_down_info);
		if (item != null) {
			rv.setTextViewText(R.id.down_name, item.name);
		}
		return rv;
	}

	/**
	 * ��������֪ͨ
	 * 
	 * @param item
	 * @return
	 */
	public void refrushNotification(RemoteViews rv, DownloadTask item,
			int iconId, String currStr, boolean indeterminate) {
		if ((item.silentMode & DownloadTask.SilentMode.NOTIFICATION) != 0) {
			rv.setTextViewText(R.id.down_state, currStr);
			rv.setProgressBar(R.id.down_progress_bar, 1000, item.progress,
					indeterminate);
			// ֪ͨ�ı�־λ
			int notifTag = item.notifyTag;
			// Intent ��ת��ͼ
			Intent intent = new Intent();
			// intent.setAction(COMEFROM_NOTIC);
			intent.putExtra(APP_ID, item.resourceId);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			int notifFlag = Notification.FLAG_AUTO_CANCEL;
			showNotification(context, notifTag, notifFlag, rv, iconId, intent);
		}

	}

	/**
	 * ����������֪ͨ
	 * 
	 * @param rv
	 * @param item
	 * @param iconId
	 * @param currStr
	 */
	public void refrushNotification(RemoteViews rv, DownloadTask item,
			int iconId, String currStr) {
		if ((item.silentMode & DownloadTask.SilentMode.NOTIFICATION) != 0) {
			rv.setTextViewText(R.id.down_state, currStr);
			// ֪ͨ�ı�־λ
			int notifTag = item.notifyTag;
			// FIXME ���Inetent�����ã����õ����ֹ���¼� �������� ���� �������ؽ��� ; ������� ���� �����ļ� ���Ǵ��ļ�
			// ������;
			Intent intent = new Intent();
			intent.putExtra(APP_ID, item.resourceId);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			int notifFlag = Notification.FLAG_AUTO_CANCEL;
			showNotification(context, notifTag, notifFlag, rv, iconId, intent);
		}
	}



	/**
	 * ���͵ȴ����ص�֪ͨ
	 * 
	 * @param item
	 * @return
	 */
	public void sendWaitStartNotification(DownloadTask item) {
		RemoteViews rv = createRemoteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_wait_tickertext);
		int iconId = R.drawable.notify_downloading;
		refrushNotification(rv, item, iconId, currStr, true);
	}

	/**
	 * �����������ص�֪ͨ
	 */
	public void sendRunningNotification(DownloadTask item) {
		RemoteViews rv = createRemoteView(item);
		String currStr = DownloadUtil.setCurrentSizeStyle(item.currentSize,
				item.totalSize);
		int iconId = R.drawable.notify_downloading;
		refrushNotification(rv, item, iconId, currStr, false);
	}

	/**
	 * ���������쳣��֪ͨ
	 */
	public void sendErrorNotification(DownloadTask item) {
		RemoteViews rv = createRemoteCompleteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_error_tickertext);
		int iconId = R.drawable.notify_download_fail;

		switch (item.errorCode) {
		case DownloadTask.ErrorCode.DownloadProcessError:
			currStr = context.getResources().getString(
					R.string.notif_downloadprocesserror_tickertext);
			break;
		case DownloadTask.ErrorCode.NetWorkStateError:
			currStr = context.getResources().getString(
					R.string.notif_networkstateerror_tickertext);
			break;
		case DownloadTask.ErrorCode.SdCardCanNotUseError:
			context.getResources().getString(
					R.string.notif_sdcardcannotuseerror_tickertext);
			break;
		case DownloadTask.ErrorCode.NotEnoughAvailableBlocksError:
			context.getResources().getString(
					R.string.notif_notenoughavailableblockserror_tickertext);
			break;
		case DownloadTask.ErrorCode.UrlNotValidError:
			context.getResources().getString(
					R.string.notif_urlnotvaliderror_tickertext);
			break;
		}
		refrushNotification(rv, item, iconId, currStr);
	}

	/**
	 * ����������ɵ�֪ͨ
	 * 
	 * @param item
	 */
	public void sendFinishNotification(DownloadTask item) {
		RemoteViews rv = createRemoteCompleteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_finish_tickertext);
		int iconId = R.drawable.notify_download_complete;

		refrushNotification(rv, item, iconId, currStr);

	}

	/**
	 * ����������ֹ��֪ͨ
	 * 
	 * @param item
	 */
	public void sendCancelNotification(DownloadTask item) {
		RemoteViews rv = createRemoteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_cancel_tickertext);
		int iconId = R.drawable.notify_download_fail;
		refrushNotification(rv, item, iconId, currStr);
	}

	/**
	 * ����������ͣ��֪ͨ
	 * 
	 * @param item
	 */
	public void sendPauseNotification(DownloadTask item) {
		RemoteViews rv = createRemoteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_pause_tickertext);
		int iconId = R.drawable.notify_download_fail;
		refrushNotification(rv, item, iconId, currStr);
	}

	/**
	 * ��������ʧ�ܵĹ㲥��ˢ��֪ͨ
	 * 
	 * @param item
	 * @throws InterruptedException
	 */
	public void sendFailNotification(DownloadTask item) {
		RemoteViews rv = createRemoteView(item);
		String currStr = context.getResources().getString(
				R.string.notif_fail_tickertext);
		int iconId = R.drawable.notify_download_fail;
		refrushNotification(rv, item, iconId, currStr);
	}

	/**
	 * 
	 * @param context
	 * @param notifTag
	 *            ֪ͨ�ı�ʶλ
	 * @param notifFlag
	 *            ֪ͨ��flag
	 * @param rv
	 *            ֪ͨ�������ͼ
	 * @param iconId
	 *            ֪ͨ��icon
	 * @param tickerText
	 * @param intent
	 *            ֪ͨ�󶨵�intent����ת����ͼ��
	 */
	private void showNotification(Context context, int notifTag, int notifFlag,
			RemoteViews rv, int iconId, Intent intent) {
		NotificationManager nManager = null;
		nManager = ((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE));
		Notification notification = new Notification();

		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				intent, notifTag);

		notification.icon = iconId;
		String tickerText = "";
		notification.tickerText = tickerText;
		notification.flags = notifFlag;
		notification.setLatestEventInfo(context, tickerText, tickerText,
				contentIntent);
		if (rv != null) {
			notification.contentView = rv;
		}
		nManager.notify(notifTag, notification);
	}

	/**
	 * 
	 * @param context
	 * @param notifTag
	 *            ֪ͨ�ı�ʶλ
	 * @param notifFlag
	 *            ֪ͨ��flag
	 * @param rv
	 *            ֪ͨ�������ͼ
	 * @param iconId
	 *            ֪ͨ��icon
	 * @param tickerText
	 * @param intent
	 *            ֪ͨ�󶨵�intent������Ӧ�õ���ͼ��
	 */
	public static void showInstallScuesssfulNotification(Context context,
			Intent notifyIntent, int notifTag, RemoteViews rv, int iconId) {
		NotificationManager nManager = null;

		nManager = ((NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE));
		Notification notification = new Notification();
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notifyIntent, notifTag);
		notification.icon = iconId;
		String tickerText = "";
		notification.tickerText = tickerText;
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, tickerText, tickerText,
				contentIntent);
		if (rv != null) {
			notification.contentView = rv;
		}
		nManager.notify(notifTag, notification);
	}

	/**
	 * ���֪ͨ����
	 * 
	 * @param context
	 *            �����Ķ���
	 * @param notifTag
	 *            Ӧ�����б��е�λ�ã���Ϊ֪ͨ��ID
	 */

	public void clearNotifcation(Context context, int notifTag) {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancel(notifTag);
	}

	/**
	 * ������е�֪ͨ
	 * 
	 * @param context
	 *            �����Ķ���
	 */
	public void clearAllNotfication(Context context) {
		NotificationManager mNotificationManager = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.cancelAll();
	}
}
