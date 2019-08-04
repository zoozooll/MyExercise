/**
 * 
 */
package com.oregonscientific.bbq.act;

import com.oregonscientific.bbq.bean.BBQDataSet.CookingStatus;
import com.oregonscientific.bbq.bean.BBQDataSet.DonenessLevel;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;


/**
 * @author aaronli
 *
 */
public class NotificationController {
	private String flag;
	private boolean notifiStart;
	private static NotificationController instance;
	
	private Context mContext;
	private Notification.Builder mBuilder;
	private NotificationManager mNotificationManager;
	private PowerManager.WakeLock m_wakeLockObj;
	
	private CookingStatus preStatusCh1;
	private CookingStatus preStatusCh2;
	
	private NotificationController(Context context) {
		mContext = context;
		init();
	}

	public static NotificationController getInstance(Context context) {
		if (instance == null) {
			synchronized (NotificationController.class) {
				if (instance == null) {
					instance = new NotificationController(context);
				}
			}
		}
		return instance;

	}
	
	private void init() {
		mBuilder =
		        new Notification.Builder(mContext)
		        .setSmallIcon(android.R.drawable.btn_star)
		        .setContentTitle("AW3312 Cooking")
		        .setContentText("Hello World!");
		mBuilder.setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		// Creates an explicit intent for an Activity in your app
		Intent resultIntent = new Intent(mContext, OperationActivity.class);

		// The stack builder object will contain an artificial back stack for the
		// started Activity.
		// This ensures that navigating backward from the Activity leads out of
		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.create(mContext);
		// Adds the back stack for the Intent (but not the Intent itself)
		//stackBuilder.addParentStack(OperationActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(resultIntent);
		/*PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_CANCEL_CURRENT
		        );*/
		/*PendingIntent resultPendingIntent =PendingIntent.getActivity(mContext, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);  
		mBuilder.setContentIntent(resultPendingIntent);*/
		mNotificationManager =
		    (NotificationManager)mContext.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		
	}
	
	public void showingNotification() {
		mNotificationManager.notify(100, mBuilder.build());
		acquireWakeLock();
		releaseWakeLock();
	}
	
	public void acquireWakeLock() {
		if (m_wakeLockObj == null) {
			PowerManager pm = (PowerManager) mContext.getSystemService(Context.POWER_SERVICE);
			m_wakeLockObj = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
				| PowerManager.ACQUIRE_CAUSES_WAKEUP
				| PowerManager.ON_AFTER_RELEASE, "MainActivity");
		}
		m_wakeLockObj.acquire();
	}
	
	public void releaseWakeLock() {
		if (m_wakeLockObj != null && m_wakeLockObj.isHeld()) {
			m_wakeLockObj.release();
		}
	}

	public void showDonenessLevelNotify(int channel, CookingStatus curStatus) {
		
		if (channel == 1) {
			if (!curStatus.equals(preStatusCh1)) {
				notifyCookingChanged(curStatus, channel);
				preStatusCh1 = curStatus;
			}
		} else if (channel == 2) {
			if (!curStatus.equals(preStatusCh2)) {
				notifyCookingChanged(curStatus, channel);
				preStatusCh2 = curStatus;
			}
		}
		
	}
	
	public void forceStopNotify() {
		notifiStart = false;
		preStatusCh1 = CookingStatus.STOPED;
		preStatusCh2 = CookingStatus.STOPED;
	}

	/**
	 * @param curStatus
	 */
	public void notifyCookingChanged(CookingStatus curStatus, int channel) {
		
		flag = "";
		if (channel == 1) {
			flag = "Ch1";
		} else if (channel == 2) {
			flag = "Ch2";
		}
		switch (curStatus) {
		case ALMOST:
			//TODO 修改这里，当进入almost状态
			mBuilder.setContentTitle(flag +" is almost")
				.setContentText(flag +" is almost now");
			mNotificationManager.notify(100, mBuilder.build());
			break;
		case READY:
			//TODO 修改这里，当进入ready状态，每8秒提醒一次
			notifiStart = true;
			new Thread(){
				@Override
				public void run() {
					while(notifiStart){
						try {
							Thread.sleep(4500);
							mBuilder.setContentTitle(flag +" is READY")
					    	.setContentText(flag +" is READY now, please be care");
							mNotificationManager.notify(100, mBuilder.build());;
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}					
				}
			}.start();
			
			break;
		case OVERCOOK:
			//TODO 修改这里，当进入烧焦状态，每5秒一次
			notifiStart = true;
			new Thread(){
				@Override
				public void run() {
					while(notifiStart){
						try {
							Thread.sleep(2500);
							mBuilder.setContentTitle(flag +" is OVERCOOK")
					    	.setContentText(flag +" is OVERCOOK now, Hurry up!");
							mNotificationManager.notify(100, mBuilder.build());
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					
				}
			}.start();
			
			break;
		case STOPED:
			notifiStart = false;
			break;
			
		default:
			break;
		}
	}
}
