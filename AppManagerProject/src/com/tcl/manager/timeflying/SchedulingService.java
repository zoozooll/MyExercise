package com.tcl.manager.timeflying;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import com.tcl.base.http.IProviderCallback;
import com.tcl.manager.analyst.Analyst;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.battery.BatteryUsageProvider;
import com.tcl.manager.battery.BatteryUsageTask;
import com.tcl.manager.datausage.DatausageProvider;
import com.tcl.manager.miniapp.MiniAppWindowManager;
import com.tcl.manager.model.DataHistoryDBManager;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.protocol.PhoneInfoProtocol;
import com.tcl.manager.statistic.frequency.LogReport;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.SharedStorekeeper;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code SampleAlarmReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SchedulingService extends Service {

    public static final String TAG = "SchedulingService";
    // 从客户端传来的INTENT的键名。
    public static final String REPEAT_TAG = "SchedulingService_repeat_tag";
    /*
     * This value of REPEAT_TAG from intent means beginning to run repeat. If
     * the alarm's clock is not running, it will run onCreate() 开始循环的。如果传送的是
     * 此值，而没有启动循环alarm, 那么调用onCreate()来启动。，
     */
    public static final int TAG_REPEAT_START = 1;

    /*
     * 如果传送的是 此值，说明是从循环机制AlarmReceiver传过来的值。将在onStartCommand方法中处理有关逻辑
     */
    public static final int TAG_REPEAT_LOOP = 2;
    
    //为上传数据而定
    public static final int TAG_REPEAT_UPLOAD = 3;

    private Handler mHandler = new MyHandler();
    private static final int START_SHCEDULING = 0;
    private static final int BATTERY_THREAD_END = 1;
    private static final int SAVE_THREAD_END = 2;
    private static final int KILL_END = 3;

    @Override
    public void onCreate() {
        super.onCreate();
        AlarmReceiver.setAlarmRepeat(this.getApplicationContext());
        new BatteryUsageTask().execute(getApplicationContext(), false);
        AlarmReceiverForLogupload.setAlarm(getApplicationContext());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // modify by zuokang.li. Dec 23,2014
        // for intent is null and catch NullPointerException
        if (intent != null) {
            int tag = intent.getIntExtra(REPEAT_TAG, 0);
            // NLog.i(TAG, "REPEAT_TAG "+tag);
            switch (tag) {
            case TAG_REPEAT_START:

                break;
            case TAG_REPEAT_LOOP:
                mHandler.sendEmptyMessage(START_SHCEDULING);
                break;
            case TAG_REPEAT_UPLOAD:
                doReport();
                break;
            default:
                break;
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void doReport() {
		// Report and upload the device messages
    	boolean startableDeviceMsg = false, startableLog = false;
    	long lastDeviceMsgReport = SharedStorekeeper.getLong(SharedStorekeeper.DEVEICE_LAST_REPORT_TIME);
    	
    	String lastreportk = SharedStorekeeper.get(SharedStorekeeper.LOG_LAST_REPORT_TIME);
    	SimpleDateFormat format = new SimpleDateFormat("yyyy:MM:dd");
    	final String current = format.format(new Date());
    	if (AndroidUtil.isWifiConnect(getApplicationContext())) {
    		if (lastDeviceMsgReport == 0) {
    			startableDeviceMsg = true;
    		}
    		startableLog = !current.equals(lastreportk);
		} else if (AndroidUtil.isNetConnect(getApplicationContext())) {
			
			if ((System.currentTimeMillis() - lastDeviceMsgReport) > (1000 * 3600 * 24 * 7)) {
    			startableDeviceMsg = true;
    		}
			long lastUploadTime = 0;
			try {
				
				lastUploadTime = format.parse(lastreportk).getTime();
			} catch (java.text.ParseException  e) {
				e.printStackTrace();
			}
			startableLog = ((System.currentTimeMillis() - lastUploadTime) > (1000 * 3600 * 24 * 7));
		}
    	
    	if (startableDeviceMsg) {
			Thread t = new Thread(new Runnable() {
				@Override
				public void run() {
					new PhoneInfoProtocol(new IProviderCallback<Boolean>() {
						
						@Override
						public void onSuccess(Boolean obj) {
							SharedStorekeeper.save(SharedStorekeeper.DEVEICE_LAST_REPORT_TIME, System.currentTimeMillis());
						}
						
						@Override
						public void onFailed(int code, String msg, Object obj) {
						}
						
						@Override
						public void onCancel() {
						}
					}).send();
				}
			});
			t.start();
		}
		// Report and upload the log data
    		if (startableLog) {
    			Thread t = new Thread(new Runnable() {
    				@Override
    				public void run() {
    					LogReport report = new LogReport(ManagerApplication.sApplication);
    					report.report();
    					SharedStorekeeper.save(SharedStorekeeper.LOG_LAST_REPORT_TIME, current);
    				}
    			});
    			t.start();
    	}
		AlarmReceiverForLogupload.setNextAlarm(getApplicationContext());
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    private class BatteryThread implements Runnable {

        /*
         * (non-Javadoc)
         * 
         * @see java.lang.Runnable#run()
         */
        @Override
        public void run() {
            try {
                new BatteryUsageProvider().saveBatteryUsage(getApplicationContext(), false);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mHandler.sendEmptyMessage(BATTERY_THREAD_END);
        }

    }

    private class SaveTread implements Runnable {

        @Override
        public void run() {
            BackgroudTask.save(getApplicationContext());
            mHandler.sendEmptyMessage(SAVE_THREAD_END);
        }

    }
    
    private class DataHistoryThread implements Runnable {

		@Override
		public void run() {
			Calendar c = Calendar.getInstance();
			DataHistoryDBManager db = DataHistoryDBManager.getInstance(getApplicationContext());
			c.add(Calendar.DAY_OF_YEAR, -1);
			Date date = c.getTime();
			if (db.getUsageofDate(date) == null) {
				DatausageProvider p = new DatausageProvider();
				p.saveDatausage(date, getApplicationContext());
			}
		}
    	
    }

    /** 开启杀后台 */
    private void startKillThread() {
        Thread kill = new Thread(new Runnable() {

            @Override
            public void run() {
                AutoStartBlackList.getInstance().killBackground();
                mHandler.sendEmptyMessage(KILL_END);
            }
        });
        kill.start();
    }
    

    private Analyst mAppScoreAnaly;

    private class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case START_SHCEDULING:
                new Thread(new BatteryThread(), "Battery Threas").start();
                startDatausegeHistory() ;
                startCheckScore();
                break;
            case BATTERY_THREAD_END:
                new Thread(new SaveTread(), "Save Threas").start();
                break;
            case SAVE_THREAD_END:
                startKillThread();
                break;

            case KILL_END:
//                startCheckScore();
                break;

            default:
                break;
            }
        }

    }

    private void startCheckScore() {
    	MiniAppWindowManager.getInstance(this).checkScore();
//        // 开始检测分数
//        if (mAppScoreAnaly == null) {
//            mAppScoreAnaly = new Analyst(SchedulingService.this, new IAnalystListener() {
//
//                @Override
//                public void opreateOver(List<AppScoreInfo> list, int totalScore, int appsScore, int memoryScore) {
//                    // 更新总分
//                    MiniAppWindowManager.getInstance(SchedulingService.this).updateAppWidget2(true, true, totalScore);
//                }
//            });
//        }
//        mAppScoreAnaly.startAnalysisPart();
    }
    
    private void startDatausegeHistory() {
    	Calendar c = Calendar.getInstance();
		if (c.get(Calendar.HOUR_OF_DAY) > 3) {
			// If it is more than 3 hours of today, start to check if the history data of previous day is saved to database;
			new Thread(new DataHistoryThread(), "data histrory thread").start();
		}
    }

}
