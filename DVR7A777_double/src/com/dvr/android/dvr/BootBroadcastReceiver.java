package com.dvr.android.dvr;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.dvr.android.dvr.msetting.SettingBean;

public class BootBroadcastReceiver extends BroadcastReceiver {

    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    static String TAG = "BootBroadcastReceiver";
    private static final String service_name = "com.dvr.android.dvr.DVRBackService";
    static final String ACTION_AVIN_OUTPUT = "android.intent.action.F10_BTN_CLICK";
    static final String ACTION_AVIN_HAVESINGLE = "android.intent.action.F9_BTN_CLICK";
    static final String ACTION_ACCSCREENON_STARTDVR = "android.intent.action.ACTION_ACCSCREENON_STARTDVR";

    public static Intent localIntent;
    public static Context mContext;
    SharedPreferences share;
    Editor editor;
    public static boolean isServiceRuning = false;

    @Override
    public void onReceive(Context context, Intent intent) {
    	String str = intent.getAction();
    	Log.i("PLJ", "BootBroadcastReceiver---->onReceive:"+str);
        mContext = context;
        share = context.getSharedPreferences("xgx", Context.MODE_PRIVATE);
        editor = share.edit();
        if (str.equals(ACTION)) {
        	Config.isBoot = true;
            SettingBean.getdatafromShareP(context);
            if (isServiceRunning(service_name)) {
                Intent updateSurfaceIntent = new Intent("android.intent.action.START_RECORD");
                mContext.sendBroadcast(updateSurfaceIntent);
            } else {
                if (SettingBean.mEnableAutoRun) {
                    // 如果不是�?��自动录的话，进入服务，服务自己�?出去，不进入服务，休眠会电流掉不下去
                    localIntent = new Intent(context, DVRBackService.class);
                    localIntent.putExtra("PowerOnAutorun", true);
                    context.startService(localIntent);
                } else {
                    /*localIntent = new Intent(context, DVRActivity.class);
                    localIntent.putExtra("PowerOnAutorun", true);
                    localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(localIntent);*/
                }
            }
            
            /*if(share.getBoolean("isAvinInput", false)){
            	AVINHandler.removeMessages(MODE_AVIN_STSRT);
                AVINHandler.sendEmptyMessageDelayed(MODE_AVIN_STSRT, 3000);
            }*/
        } else if (str.equals(ACTION_AVIN_OUTPUT)) {
        	AVINHandler.removeMessages(MODE_AVIN_STOP);
        	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
        		AVINHandler.sendEmptyMessageDelayed(MODE_AVIN_STOP, 100);
        	}
        } else if (str.equals(ACTION_AVIN_HAVESINGLE)) {
        	AVINHandler.removeMessages(MODE_AVIN_STSRT);
        	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
        		AVINHandler.sendEmptyMessageDelayed(MODE_AVIN_STSRT, 100);
        	}
        } else if (str.equals(ACTION_ACCSCREENON_STARTDVR)) {
        	Log.i("PLJ", "BootBroadcastReceiver---->ACTION_ACCSCREENON_STARTDVR:111:"+intent.getStringExtra("startdvr")+"  "+intent.getStringExtra("startdvr").equals("recdvr"));
        	if(intent.getStringExtra("startdvr").equals("startdvr")){
        		Intent mIntent = new Intent(context, DVRBackService.class);
                context.startService(mIntent);
        	}else if(intent.getStringExtra("startdvr").equals("recdvr")){
        		Log.i("PLJ", "BootBroadcastReceiver---->ACTION_ACCSCREENON_STARTDVR:222:");
        		Intent mIntent = new Intent(context, DVRBackService.class);
        		mIntent.putExtra("PowerOnAutorun", true);
                context.startService(mIntent);
        	}
        }
    }

    private final static int MODE_AVIN_STSRT = 1;
    private final static int MODE_AVIN_STOP = 2;
    Handler AVINHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
            case MODE_AVIN_STSRT:
                startAvin();
                break;
            case MODE_AVIN_STOP:
                stopAvin();
                break;
            default:
                break;
            }
        }
    };

    private void startAvin() {
    	// 开始倒车
        editor.putBoolean("isAvinInput", true);
        editor.commit();
        isServiceRuning = isServiceRunning(service_name); Log.i("PLJ", "BootBroadcastReceiver---->startAvin:"+isServiceRuning);
        if (isServiceRuning) {
            // 如果插入AVIN前DvrService已经启动则发送广播到Service
            Intent startActivityIntent = new Intent(DVRBackService.ACTION_AVIN_PUTIN_BEFORE_STARTDVR);
            mContext.sendBroadcast(startActivityIntent);
        } else {
            localIntent = new Intent(mContext, DVRActivity.class);
            localIntent.putExtra("ReadAvinState", false);
            localIntent.putExtra("PowerOnAutorun", false);
            localIntent.putExtra("NoShowBtn", true);
            localIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(localIntent);
        }
    }

    private void stopAvin() {
    	// 离开倒车
        editor.putBoolean("isAvinInput", false);
        editor.commit();
        isServiceRuning = isServiceRunning(service_name);  Log.i("PLJ", "BootBroadcastReceiver---->stopAvin:"+isServiceRuning);
        if (isServiceRuning) {
            // 判断service有没有开启，有开启发送广播
            Intent stopActivityIntent = new Intent(DVRBackService.ACTION_AVIN_OUT_BEFORE_STARTDVR);
            mContext.sendBroadcast(stopActivityIntent);
        } else {
            // 退出Activity,如果插入AVIN前DVR没有启动
            Intent stopActivityIntent = new Intent(DVRBackService.ACTION_AVIN_OUT_BEFORE_NOSTARTDVR);
            mContext.sendBroadcast(stopActivityIntent);
        }
    }
    
    private boolean isServiceRunning(String serviceName) {
        ActivityManager myManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }
}