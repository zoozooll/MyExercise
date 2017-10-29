package com.dvr.android.dvr;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.Toast;

import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.util.CameraUtil;
import com.dvr.android.dvr.util.LightListener;
import com.dvr.android.dvr.util.PowerUitl;
import com.dvr.android.dvr.util.SDcardManager;

public class DVRActivity extends Activity implements LightListener {

    private final String TAG = "DVRActivity";

    private int nScreenHeight = 0;
    private int nScreenWidth = 0;
    public static DVRActivity instance = null;

    /* 后台相关变量 */
    private static final String service_name = "com.dvr.android.dvr.DVRBackService";

    private class MsgReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            int msg = intent.getIntExtra("msgtype", 0);
            if (msg == Config.MSG_ACTIVITY_BROADCASE_RESTART_SERVICE) {
                ShowService();
            }else if (msg == Config.MSG_ACTIVITY_BROADCASE_STOP_ACTIVITY) {
                if (isServiceRunning(service_name)) {
                    disconnectRecorderService();
                }
                DVRActivity.this.finish();
            }
        }
    }

    private MsgReceiver msgReceiver;
    private boolean mAutoRunWhenPowerOn = false;
    private boolean mDeviceSleep = false;

    @Override
    public void onCreate(Bundle savedInstanceState) { Log.i("PLJ", "DVRActivity---->onCreate:");
        super.onCreate(savedInstanceState);
        this.mAutoRunWhenPowerOn = this.getIntent().getBooleanExtra("PowerOnAutorun", false);
        SettingBean.getdatafromShareP(this);

        // 动态注册广播接收器
        msgReceiver = new MsgReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.dvr.android.dvr.RECEIVER");
        registerReceiver(msgReceiver, intentFilter);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        CameraUtil.setUtilDegree(this);

        PowerUitl.getPowerUitl().setListener(this);
        instance = this;
    }

    private void getWindWidth() {
        DisplayMetrics dm = new DisplayMetrics();
        // 取得窗口属性
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        // 窗口的宽度
        nScreenWidth = dm.widthPixels;
        // 窗口高度
        nScreenHeight = dm.heightPixels;
    }

    private void ShowService() {
        getWindWidth(); Log.i("PLJ", "DVRActivity---->ShowService:"+isServiceRunning(service_name));
        if (isServiceRunning(service_name)) {
            // isgoonrecord = true;
            // disconnectRecorderService();
            Intent updateSurfaceIntent = new Intent(DVRBackService.ACTION_CHANGE_SURFACE);
            updateSurfaceIntent.putExtra("mProgramX", 0);
            updateSurfaceIntent.putExtra("mProgramY", 0);
            updateSurfaceIntent.putExtra("nScreenWidth", nScreenWidth);
            updateSurfaceIntent.putExtra("nScreenHeight", nScreenHeight);
            updateSurfaceIntent.putExtra("PowerOnAutorun", false);
            sendBroadcast(updateSurfaceIntent);
        } else {
            Intent intent = new Intent(DVRActivity.this, DVRBackService.class);
            intent.putExtra("nScreenWidth", nScreenWidth);
            intent.putExtra("nScreenHeight", nScreenHeight);
            if (mAutoRunWhenPowerOn) {
                intent.putExtra("PowerOnAutorun", true);
            } else {
                intent.putExtra("PowerOnAutorun", false);
            }
            mAutoRunWhenPowerOn = false;
            intent.putExtra("ActivityStart", true);
            startService(intent);
        }
    }

    private boolean isServiceRunning(String serviceName) {
    	boolean bool = false;
        ActivityManager myManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(100);
        for (int i = 0; i < runningService.size(); i++) {
            if (runningService.get(i).service.getClassName().toString().equals(serviceName)) {
            	bool = true;
            	break;
            }
        }
        return bool;
    }

    private void disconnectRecorderService() {
        this.stopService(new Intent(this, DVRBackService.class));
    }

    @Override
    protected void onStop() { Log.i("PLJ", "DVRActivity---->onStop:");
        // 离开此页面就让屏幕亮起来
        if (mDeviceSleep == false) {
//            Intent updateSurfaceIntent = new Intent("android.intent.action.CHANGESURFACE");
//            updateSurfaceIntent.putExtra("mProgramX", 0);
//            updateSurfaceIntent.putExtra("mProgramY", 0);
//            updateSurfaceIntent.putExtra("nScreenWidth", 1);
//            updateSurfaceIntent.putExtra("nScreenHeight", 1);
//            sendBroadcast(updateSurfaceIntent);
            PowerUitl.getPowerUitl().onDisplayOut();
        }
        Config.bSecondActivityCanBack = true;
        super.onStop();
    }

    @Override
    protected void onStart() { Log.i("PLJ", "DVRActivity---->onStart:");
        super.onStart();
        mDeviceSleep = false;
        //ShowService();
        Config.bSecondActivityCanBack = false;
        PowerUitl.getPowerUitl().onDisplayIn();
    }

    @Override
    public void onResume() { Log.i("PLJ", "DVRActivity---->onResume:");
        super.onResume();
        ShowService();
        if (isServiceRunning(service_name)) {
            Intent updateSurfaceIntent = new Intent(DVRBackService.ACTION_NORTICE_BACK_RECORD);
            updateSurfaceIntent.putExtra("backrecord", 0);
            sendBroadcast(updateSurfaceIntent);
        } else {
            //ShowService();
        }
    }

    @Override
    public void onDestroy() { Log.i("PLJ", "DVRActivity---->onDestroy:");
        super.onDestroy();
        if (msgReceiver != null) {
            unregisterReceiver(msgReceiver);
        }
        Config.bSecondActivityCanBack = true;
        instance = null; //liujie add 0922
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        SharedPreferences sharePreferences = this.getSharedPreferences("xgx", Context.MODE_PRIVATE);
        if (keyCode == KeyEvent.KEYCODE_BACK) {Log.i("PLJ", "DVRActivity---->onKeyDown:"+Config.AvinHomeBack+"  "+Config.gbOpenSetting+"   "+Config.gbRecordStatus+"   "+libucamera.native_getclosestate()+"  "+libucamera.native_getRecordIsLV());
            if(Config.AvinHomeBack){
	            if (Config.gbOpenSetting) {
	                sendBroadcast(new Intent("android.intent.action.NORTICE_CLOSE_SETTING"));
	                return true;
	            } else if (Config.gbRecordStatus || DVRBackService.mbTakePicture) {
	                sendBroadcast(new Intent("android.intent.action.NORTICE_CANNOT_EXIT_FOR_RECORD"));
	                this.finish();
	                return true;
	            } else {
	            	if (libucamera.native_getclosestate() == 1) {
	            		if(Config.isMicroVideoPass == 0){
	            			disconnectRecorderService();
		                    finish();
	                	}else if(Config.isMicroVideoPass == 1){
	                		sendBroadcast(new Intent(DVRBackService.ACTION_MICROPLAY_NOACTION));
	                		return true;
	                	}
	            		
	                } else if (libucamera.native_getclosestate() == 0){
	                	if(Config.isMicroVideoPass == 0){
	                		sendBroadcast(new Intent(DVRBackService.ACTION_CANNOT_EXIT_SAVEING));
	                	}else if(Config.isMicroVideoPass == 1){
	                		sendBroadcast(new Intent(DVRBackService.ACTION_MICROPLAY_NOACTION));
	                	}
	                    return true;
	                }
	            }
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    public void executeDown() {
        Log.d("TAG", "do power down");
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.screenBrightness = Float.valueOf(10) * (1f / 255f);
        this.getWindow().setAttributes(lp);
    }

    public void executeUp() {
        ContentResolver resolver = this.getContentResolver();
        try {
            int restoreBrightness = android.provider.Settings.System
                    .getInt(resolver, Settings.System.SCREEN_BRIGHTNESS);
            WindowManager.LayoutParams lp = this.getWindow().getAttributes();
            lp.screenBrightness = Float.valueOf(restoreBrightness) * (1f / 255f);
            this.getWindow().setAttributes(lp);
        } catch (SettingNotFoundException e) {
            e.printStackTrace();
        }
    }
}
