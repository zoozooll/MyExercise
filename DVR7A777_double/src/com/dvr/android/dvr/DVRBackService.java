package com.dvr.android.dvr;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.Size;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.dvr.android.dvr.bean.DrivePosition;
import com.dvr.android.dvr.bean.VideoFile;
import com.dvr.android.dvr.gps.GPSInfoChangedListener;
import com.dvr.android.dvr.gps.GpsProvider;
import com.dvr.android.dvr.msetting.MyImageButton;
import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.mshowplayback.ShowPActivity;
import com.dvr.android.dvr.usbcamera.CameraPreview;
import com.dvr.android.dvr.util.CameraUtil;
import com.dvr.android.dvr.util.HitListener;
import com.dvr.android.dvr.util.ImageManager;
import com.dvr.android.dvr.util.OnHitListener;
import com.dvr.android.dvr.util.PowerUitl;
import com.dvr.android.dvr.util.SDcardManager;
import com.dvr.android.dvr.util.StringUtil;
import com.dvr.android.dvr.util.UsbCameraCallBack;
import com.dvr.android.dvr.util.VideoManager;
import com.dvr.android.dvr.view.StatusView;
import com.dvr.android.dvr.view.StatusView.OnStatusChangedListener;
//import com.lidroid.xutils.DbUtils;
//import com.lidroid.xutils.exception.DbException;
import com.huixin.carnav.service.BroadVoice;
import android.provider.Settings;//liujie add 0828

public class DVRBackService extends Service implements OnHitListener, GPSInfoChangedListener, OnStatusChangedListener
,UsbCameraCallBack.OnMethodCallback{
    private static final String TAG = "DVRBackService";

    private final static int MODE_LOOP_STOP = 203;
    private final static int MODE_START_RECORD = 204;
    private final static int MSG_IMAGE_CAPTURE = 205;
    private final static int SAVE_ON_HIT = 206;
    private final static int MSG_RECORD_STOP = 207;
    private final static int MSG_MEMORY_OUT_DLG_SHOW = 208;
    private final static int MSG_OPEN_CAMERA_FAIL_DLG_SHOW = 209;
    private final static int MSG_HIDE_UNNORMA_USE_BTN = 210;
    private final static int MSG_CLOSE_SETTING_UI = 211;
    private final static int MSG_SDCARD_UNREMOUNT_DLG_SHOW = 212;
    private final static int MSG_EXIT_STOP_RECORD_FIRST = 213;
    private final static int MSG_EXIT_CAPUTER = 214;
    private final static int MSG_PREVIEW_SWITCH = 215;
    private final static int MSG_MICRO_VIDEO = 216;
    private final static int MSG_MICRO_STOP_VIDEO = 217;
    private final static int MSG_ZERO_STOP_MICRO_VIDEO = 218;
    private final static int MSG_ACCDISCONNECT_SLEEP = 219;
    private final static int MSG_SDCARD_UNMOUNTED = 220;
    private final static int MSG_ACCDISCONNECT_SHUTDOWN = 221;
    private final static int MSG_EXIT_STOP_SAVEING = 222;
    
    private final static int MSG_GET_AVIN_STATUS = 223; //liujie add 1009 
    private final static int MSG_START_RECORD = 224; //liujie add 1009
    private final static int MSG_EXIT_CAPUTER_AIDL = 225;
    private final static int MODE_START_RECORD_AIDL = 226;
    private final static int AVIN_TIME_OFFSET = 600; //liujie add 1009
    private final static int MODE_MICRO_VIDEO_PASS = 227;
    private final static int MICROPLAY_NOACTION = 228;
    
    private final static int GET_AVIN_STATUS_MAX_TIMES = 3; //liujie add 1009 
    
    private Camera mCamera;

    private static Handler handler = null;

    private MediaRecorder mMediaRecorder;

    private WindowManager mWindowManager;
    private WindowManager.LayoutParams cameraLayoutParams;
    private WindowManager.LayoutParams usbCameraLayoutParams;

    // 当前状态
    private int mStatus = Config.STATUS_IDLE;
    // bottom bar
    //private StatusView mStatusView;
    // 经纬度及时时控件
    //private TextView mLongitudeView, mLatitudeView, mSpeedView;

    private NumberFormat mNumberFormat;

    // 当前主显示是哪个摄像头
    private boolean isCameraViewSmall = false;
    private Rect smallViewUIRect;
    private CameraPreview usbCameraSurfaceView;
    private CameraSurfaceView cameraSurView;
    private ImageView imageAvin;

    private RelativeLayout serviceRelative;
    private TextView takePictureBtn;
    private ImageView startRecordBtn;
    private TextView stopRecordBtn,settingsBtn,playListBtn,presWitchBtn;

    private MyImageButton btn_Mic = null;
    private TextView btn_Lock = null;
    private View mFloatView,mCameraLayout,mUsbCameraLayout;
    //private Chronometer mChronometer = null;

    private long mLastClickTime = 0; // 按键的上次时间
    private long mCurrentClickTime = 0; // 按键现在的时间

    private RelativeLayout setPreviewLayout;
    //private LinearLayout bottomInfoLayout;

    // 状�?量：预览是否�?��
    private boolean mPreviewing;
    public static boolean mbTakePicture = false;
    private boolean mPreSwitchBool = true;

    private int mCameraId = CameraInfo.CAMERA_FACING_BACK;
    // CAMERA_FACING_FRONT; 前置摄像头
    // CAMERA_FACING_BACK; 后置摄像头
    // 是否在recoder�?1在recoder 0 不在recoder
    private int mState = 0;
    private static final int CURRENT_STATE_UNRECODING = 0;
    private static final int CURRENT_STATE_RECODING = 1;
    private static final int CURRENT_STATE_HIDE = 3;
    private static final int CURRENT_STATE_BACK_UP = 4;

    private boolean mbReadyStartRecord = false;

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyyMMddHHmmss");

    // 后台消息管理
    private NotificationManager mNotificationManager;
    // contentResolver
    private ContentResolver mContentResolver;
    // 文件�?
    private String mFileName,mUsbFileName;
    private String mFileNameBack;
    // 当前Uri
    private long mStartRecordTime = 0; // �?��录制时间
    private long mStopRecordTime = 0; // 结束 录制时间

    // 状�?量：是否是在后台录制
    private static boolean mOnBackground = false;

    private static boolean mRestartService = false;

    private static boolean mIsPowerOnAutorun = false;
    private static boolean mClientLuRuiTong = false;

    // 预览窗口的宽度
    private int nScreenWidth = 0;
    // 预览窗口的高度
    private int nScreenHight = 0;

    private static int mHideAllBtnDelay = 30; // �?

    private boolean mShowunNormalBtn = true;

    private boolean mVideoLock = false; // 手动保存，不允许删除
    private boolean mHitSave = false;
    private boolean mDeviceUnmount = false;

    public static String ACTION_CHANGE_SURFACE = "android.intent.action.CHANGESURFACE";
    public String ACTION_STOP_RECORD = "android.intent.action.STOP_RECORD";
    public String ACTION_START_RECORD = "android.intent.action.START_RECORD";
    public String ACTION_HIDE_ALL_BTN = "android.intent.action.HIDE_ALL_SERVICE_BTN";
    public static String ACTION_NORTICE_BACK_RECORD = "android.intent.action.NORTICE_BACK_RECORD";
    public String ACTION_NORTICE_CLOSE_SETTING = "android.intent.action.NORTICE_CLOSE_SETTING";
    public String ACTION_CANNOT_EXIT_FOR_RECORDING = "android.intent.action.NORTICE_CANNOT_EXIT_FOR_RECORD";
    public String ACTION_STOP_SERVICE = "android.intent.action_STOP_SERVICE";
    public String ACTION_RESTART_SERVICE = "android.intent.action_RESTART_SERVICE";
    public static String ACTION_CANNOT_EXIT_SAVEING = "com.dvr.android.CANNOT_EXIT_FOR_SAVEING";
    public static String ACTION_MICROPLAY_NOACTION = "com.dvr.android.MICROPLAY_NOACTION";
    public ChangeWindowBroadcast mBroadcastReceiver = null;
    public DetectSDCardMount mSDCardMountReceiver = null;
    public BroadcastReceiver mReceiverHome = null;
    public BroadcastReceiver mReceiverBattery = null;

    private SharedPreferences sharePreferences;

	// liujie add begin 0828
	public static final String DVR_RECORD_STATE = "dvr_record_state";
	public static final String DVR_ANTITHEFT_DELICACY = "dvr_antitheft_delicacy"; // liujie add 0926
	// liujie add end
    /**----------------------- 倒车相关 -----------------**/
    //插入AVIN前DVR没有启动，拔出AVIN后发送的消息
    public static final String ACTION_AVIN_OUT_BEFORE_NOSTARTDVR = "android.intent.action.AVINOUT_EXITDVR";
    //插入AVIN前DVR已经启动，插入AVIN发送的消息
    public static final String ACTION_AVIN_PUTIN_BEFORE_STARTDVR = "android.intent.action.AVINPUTIN_HAVESTARDDVR";
    //插入AVIN前DVR已经启动，拔出AVIN后发送的消息
    public static final String ACTION_AVIN_OUT_BEFORE_STARTDVR = "android.intent.action.AVINOUT_HAVESTARDDVR";
    private boolean bAvinPutIn = false;
    private boolean m_bBeforeAvinRecoed = false;
    private CheckAvin checkAvin = null;
    private boolean m_bShowFullScreen = false; //判断是否在回放界面
    /**----------------------- 倒车相关 -----------------**/
    private String ACTION_HIDE_DVR_REC_STATUS = "android.intent.action.DVR_REC_STATUS";
    Intent DvrStatusIntent;

    private final int STATUS_LOOP_RECORDING = Config.STATUS_IDLE + 1;
    private final int STATUS_REGULAR_RECORDING = STATUS_LOOP_RECORDING + 1;
    private final int STATUS_SAVING = STATUS_REGULAR_RECORDING + 1;
    private final int STATUS_IMAGE_CAPTURING = STATUS_SAVING + 1;

    // 当前信息xml文件
    private File mInfoFile;
    // 当前拍照的文件名
    private File mImageFile,mUsbImageFile;
    // 当前正在录制的视频文件
    private File mVideoTmpFile;
    private File mVideoFile;

    // 当前后路正在录制的视频文件
    private File mVideoFileBack, mVideoFileBack1;//penglj_lockfile_20150601
    private boolean isNoCardspace = false;
    private File mMicroVideoFile;//penglj_microfile
    private String mMicroFileName;
    /***********************************************************************************************/
    private boolean mbOpenSettingUi = false;
    private Rect settingUIRect;

    // set valible
    private static final int SETTING_TYPE_RECORD_MODE = 1;
    private static final int SETTING_TYPE_RECORD_QUALITY = 2;
    private static final int SETTING_TYPE_HITDELICACY = 3;
    private static final int SETTING_TYPE_RECORD_BACK = 4;
    private static final int SETTING_TYPE_VIDEO_SET = 5;

    private int nSettingType = SETTING_TYPE_RECORD_MODE;

    // added by ji
    private MyImageButton settingRecordModeBtn = null;
    private MyImageButton settingRecordQualityBtn = null;
    private MyImageButton settingHitDelicacyBtn = null;
    private MyImageButton settingRecordBackBtn = null;
    private MyImageButton settingVideoSetBtn = null;

    // setting里面五项设置layout
    private RelativeLayout recordModeLayout;// 录制模式
    private RelativeLayout recordQualityLayout;// 视频质量
    private RelativeLayout hitDelicacyLayout;// 撞击灵敏度
    private RelativeLayout startingUpBackRecordLayout;// 开机相关
    private RelativeLayout otherSettingLayout;// 其它设置

    // record mode
    private TextView mtextview_videoCycle_value;
    private ImageView btn_videoCycle_left = null;
    private ImageView btn_videoCycle_right = null;
    private String[] mCycleVideoTimes = null;

    // RecordQuality
    private MyImageButton btn_recordQuality_high;
    private MyImageButton btn_recordQuality_mid;
    private MyImageButton btn_recordQuality_lower;
    private MyImageButton btn_playback_show_time;
    
    
    //liujie add 1029
    
    private MyImageButton btn_show_back_window;
    //liujie add 1029

    // hitdelicacy
    private TextView mtextview_power_saving_value;
    //private SeekBar seekbar_delicacy = null;
    private MyImageButton btn_hitdelicacy_auto_saving = null;
    private MyImageButton btn_click_screen = null; // 功能定义为打�?��制声�?
    private ImageView btn_power_save_left = null;
    private ImageView btn_power_save_right = null;
    private String[] mPowerSaveString;

    private MyImageButton btn_hitdelicacy_high;
    private MyImageButton btn_hitdelicacy_middle;
    private MyImageButton btn_hitdelicacy_lower;
    
    //liujie add 0926
    private MyImageButton btn_antitheftdelicacy_high;
    private MyImageButton btn_antitheftdelicacy_middle;
    private MyImageButton btn_antitheftdelicacy_lower;   
    private MyImageButton btn_antitheftdelicacy_off;
    //liujie add end
    
    private ImageView btn_exposure_left = null;
    private ImageView btn_exposure_right = null;
    private TextView mtextview_exposure_text = null;
    private TextView mtextview_exposure_value_text = null;
    protected String[] mExposureValue = null;
    
    // back record
    private MyImageButton btn_record_back = null;
    //private MyImageButton btn_open_back_camera = null;

    // video set
    private TextView resetDefaulBtn,BtnSDFormat;
    private TextView mtextview_version;

    private MyImageButton btn_power_on_auto_run = null;
    private File blsFile1 = null, blsFile2 = null; // penglj_lockfile_20150601
    private UsbCameraCallBack mUsbCameraCallPrev;
    private BroadVoice broadVioce;
    int sleep,shutdown;
    private boolean isDvrStart = false;
    private boolean isInitDvr = false;
    private boolean isInitDvrRec = false;
    
    private boolean bUsbCameraInit1 = false; //liujie add 0922
    private boolean bInitFinished = true; //liujie add 1009
    private int  nGetAVINStatusTimes= 0; //liujie add 1009
    private boolean bIsSettingChanged = false; //liujie add 1013
    private ProgressDialog mProgressDialog;
    private boolean isLoopRec = false;
    private int bCameraSwitch = 0;
    /***********************************************************************************************/
    
    private boolean isRecording() {
        return mState == 1;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message paramMessage) {
            switch (paramMessage.what) {
            case MODE_LOOP_STOP:
                ServiceStopRecord(true);
                // stopRecord(true);
                break;
            case MODE_START_RECORD:
                startRecord();
                break;
            case MODE_START_RECORD_AIDL:
            	ServiceStartRecordAIDL();//startRecordAIDL();
                break;
            case MSG_IMAGE_CAPTURE:
                setControlBarVisibility(false);
                
                String capturepath = capture();
                if (capturepath.contains(",")) {
                    setControlBarVisibility(true);
                }
                /*if (isRecording()) {
                    new Thread() {
                        @Override
                        public void run() {
                            mHandler.sendEmptyMessage(MSG_RECORD_STOP);
                            while (isRecording()) {
                                SystemClock.sleep(50);
                            }

                            String capturepath = capture();
                            if (!capturepath.contains(",")) {
                                setControlBarVisibility(true);
                            }
                        }
                    }.start();
                } else {
                    new Thread() {
                        @Override
                        public void run() {
                        	String capturepath = capture();
                            if (!capturepath.contains(",")) {
                                setControlBarVisibility(true);
                            }
                        }
                    }.start();
                }*/
                break;
            case SAVE_ON_HIT: // hanJ add
                /*if (isRecording()) {
                    mHitSave = true;
                    stopRecord();
                    mHitSave = false;
                    mHandler.removeMessages(MODE_LOOP_STOP);
                    mHandler.sendEmptyMessage(MODE_START_RECORD);
                }*/
            	if (isRecording()) {
					if (mState == 1) {
			            //mHandler.removeMessages(MODE_START_RECORD);
			            //mHandler.removeMessages(MODE_LOOP_STOP);
			            
			            mHandler.post(new Runnable() {
				            public void run() {
				            	btn_Lock.setBackgroundResource(R.drawable.btn_main_lock_pressed);
				            }
				        });
			            //stopRecord(0);
			            mVideoLock = true;
			            //mHandler.removeMessages(MODE_START_RECORD);
			    		//mHandler.sendEmptyMessage(MODE_START_RECORD);
			            //updateStatus(Config.STATUS_IDLE);
			        }
                }
                break;
            case MSG_RECORD_STOP:
                ServiceStopRecord(false);
                break;
            case MSG_MEMORY_OUT_DLG_SHOW:
            	playVoice(getString(R.string.sdcard_no_space));//showErrorPopupDialog(DVRBackService.this, mFloatView, MSG_MEMORY_OUT_DLG_SHOW, 0, R.string.sdcard_no_space, R.string.clean_sdcard);
                break;
            case MSG_OPEN_CAMERA_FAIL_DLG_SHOW:
            	playVoice(getString(R.string.open_camera_error));//showErrorPopupDialog(DVRBackService.this, mFloatView, MSG_OPEN_CAMERA_FAIL_DLG_SHOW, 0,R.string.open_camera_error, 0);
                break;
            case MSG_SDCARD_UNREMOUNT_DLG_SHOW:
            	playVoice(getString(R.string.sdcard_not_mounted));//showErrorPopupDialog(DVRBackService.this, mFloatView, MSG_SDCARD_UNREMOUNT_DLG_SHOW, 0,R.string.sdcard_not_mounted, 0);
                break;
            case MSG_EXIT_STOP_RECORD_FIRST:
            	playVoice(getString(R.string.exit_stop_record));//showErrorPopupDialog(DVRBackService.this, mFloatView, MSG_EXIT_STOP_RECORD_FIRST, 0, R.string.exit_stop_record, 0);
                break;
            case MSG_HIDE_UNNORMA_USE_BTN:
                if (!mbOpenSettingUi && !mOnBackground) {
                    setunNormalBtnVisibility(View.INVISIBLE);
                }
                break;
            case MSG_CLOSE_SETTING_UI:
                bCameraSwitch = 0;
                Config.gbOpenSetting = false;
                mbOpenSettingUi = false;
                initSetActivity(); // close
                showMicStatus();
                saveme();
                break;
            case MSG_EXIT_CAPUTER:
            	capture();
            	mHandler.sendEmptyMessageDelayed(MODE_START_RECORD, 1500);
                break;
            case MSG_EXIT_CAPUTER_AIDL:
            	capture();
            	mHandler.sendEmptyMessageDelayed(MODE_START_RECORD_AIDL, 1500);
                break;
            case MSG_PREVIEW_SWITCH:
	            usbCameraSurfaceView.BackCameraStopPrev();
	    		mPreSwitchBool = false;
	            break;
            case MSG_MICRO_VIDEO:
            	boolean bool = paramMessage.getData().getBoolean("micro2", false);
            	usbCameraSurfaceView.BackCameraStopRec("",0);
            	
            	Message message=new Message();  
                Bundle bundle=new Bundle();  
                bundle.putBoolean("micro2", bool);
                message.setData(bundle);
                message.what=MSG_MICRO_STOP_VIDEO;
                mHandler.sendMessageDelayed(message, 1000 * 5);
            	//mHandler.sendEmptyMessageDelayed(MSG_MICRO_STOP_VIDEO, 1000 * 5);
            	break;
            case MSG_MICRO_STOP_VIDEO:
            	boolean bool2 = paramMessage.getData().getBoolean("micro2", false);
            	usbCameraSurfaceView.surfaceDestroyed(usbCameraSurfaceView.getHolder());//usbCameraSurfaceView.BackCameraClose();//
            	usbCameraSurfaceView.usbCameraInit(0);
            	
            	if(bool2){
            		mHandler.sendEmptyMessageDelayed(MODE_START_RECORD_AIDL/*MODE_START_RECORD*/, 1500);
            		mHandler.sendEmptyMessageDelayed(MODE_MICRO_VIDEO_PASS, 5000);
            	}else{
            		mHandler.sendEmptyMessageDelayed(MODE_MICRO_VIDEO_PASS, 2000);
            	}
            	break;
            case MSG_ZERO_STOP_MICRO_VIDEO:
            	MicroVideoFun2(paramMessage.getData().getString("micro"));
            	break;
            case MSG_ACCDISCONNECT_SLEEP:
	            if(libucamera.native_getclosestate() == 1){
	                Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING);
	            	if (usbCameraSurfaceView != null) {
	                    usbCameraSurfaceView.surfaceDestroyed(usbCameraSurfaceView.getHolder());
	                }
	        		DVRBackService.this.stopSelf();
	        		if(DVRActivity.instance!=null){
                		DVRActivity.instance.finish();
                	}
	    		    android.os.Process.killProcess(android.os.Process.myPid());
	            }else{
	                sleep++;
	                if(sleep>10){
	                	mHandler.removeMessages(MSG_ACCDISCONNECT_SLEEP);
	                }else {
	                	mHandler.sendEmptyMessageDelayed(MSG_ACCDISCONNECT_SLEEP, 1000 * 3);
					}
	            }
            	break;
            case MSG_ACCDISCONNECT_SHUTDOWN:
	            if(libucamera.native_getclosestate() == 1){
	                sendBroadcast(new Intent("android.intent.action.ACTION_SYSTEM_SHUTDOWN"));
	            
	            }else{
	                shutdown++;
	                if(shutdown>10){
	                	mHandler.removeMessages(MSG_ACCDISCONNECT_SHUTDOWN);
	                }else {
	                	mHandler.sendEmptyMessageDelayed(MSG_ACCDISCONNECT_SHUTDOWN, 1000 * 3);
					}
	            }
            	break;
            case MSG_SDCARD_UNMOUNTED:
            	if (!SDcardManager.checkDVRSDCard()) {
            		if (mState == 1) {
                        mDeviceUnmount = true;
                        mHandler.sendEmptyMessage(MSG_RECORD_STOP);
                        mHandler.sendEmptyMessage(MSG_SDCARD_UNREMOUNT_DLG_SHOW);
                    }
                }
            	break;
            case MSG_EXIT_STOP_SAVEING:
            	playVoice(getString(R.string.exit_saveing));//showErrorPopupDialog(DVRBackService.this, mFloatView, MSG_EXIT_STOP_SAVEING, 0, R.string.exit_saveing, 0);
                break;
            case MSG_GET_AVIN_STATUS:
				/*liujie remove 1029
            	boolean backCameraExist = checkAvin.AVINstaus() == 0 ? true:false;
            	nGetAVINStatusTimes ++;

            	if(backCameraExist){
				    mHandler.removeMessages(MSG_GET_AVIN_STATUS);
            		Config.bBackCameraExist = true ;
	            	mCameraLayout.setVisibility(View.VISIBLE);
	            	startRecordForAutoRunSetting();
            	}else if(nGetAVINStatusTimes < GET_AVIN_STATUS_MAX_TIMES ){
            		mHandler.sendEmptyMessageDelayed(MSG_GET_AVIN_STATUS, 100); 
            	}else if(nGetAVINStatusTimes >= GET_AVIN_STATUS_MAX_TIMES ){
            		Config.bBackCameraExist = false ;
            		mCameraLayout.setVisibility(View.GONE);
            		startRecordForAutoRunSetting();
					mHandler.removeMessages(MSG_GET_AVIN_STATUS);
            	}*/
				    startRecordForAutoRunSetting();
					mHandler.removeMessages(MSG_GET_AVIN_STATUS);

            	if(Config.isBoot){
	            	//Toast.makeText(DVRBackService.this, checkAvin.BACKstatus()+"  "+checkAvin.ACCstatus()+"  "+checkAvin.AVINstaus(), Toast.LENGTH_LONG).show();
	            	if(checkAvin.BACKstatus()==0){
	            		sendBroadcast(new Intent("android.intent.action.F9_BTN_CLICK"));
	            	}
	                if(checkAvin.ACCstatus()==1){
	                	sendBroadcast(new Intent("android.intent.action.F11_BTN_CLICK"));
	            	}
	                Config.isBoot = false;
            	}
            	break;
            case MSG_START_RECORD://liujie add 1009
            	ServiceStartRecord();
            	break;
            case MODE_MICRO_VIDEO_PASS:
            	Config.isMicroVideoPass = 0;
            	break;
            case MICROPLAY_NOACTION:
            	playVoice(getString(R.string.microplay_noaction));
            	break;
            }
        }
    };
    
    //liujie add begin 1009
    private void startRecordForAutoRunSetting(){
        /*if (mIsPowerOnAutorun) {
            if (SettingBean.mEnableAutoRun) {
            	mHandler.sendEmptyMessageDelayed(MSG_START_RECORD, 200);
            }
        }*/
    	isInitDvr = true;
        if (mIsPowerOnAutorun) {
            if (SettingBean.mEnableAutoRun) {
                 mHandler.sendEmptyMessageDelayed(MSG_START_RECORD, 200); //ServiceStartRecord();
            } else {
                this.stopSelf();
                return;
            }
        }
    }
    //liujie add begin 1009
    
    private void playVoice(String str){
    	try {
			broadVioce.playVoice(str);
		} catch (RemoteException e) {}
    }

    private Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(mCameraId);
        } catch (Exception e) {
            Log.e(TAG, "getCameraInstance Open camera fail ");
            e = null;
        }
        return c;
    }
    
    private Camera getCameraInstance11() {
        Camera c = null;
	    if(mCamera==null){
	        try {
	            c = Camera.open(mCameraId);
	        } catch (Exception e) {
	        	c = null;
	            Log.i("PLJ", "getCameraInstance Open camera fail:"+c);
	        }
        }else{
        	c = mCamera;
        }
        
        Camera c1 = null;
        for(int i=0; i<4; i++){
        	SystemClock.sleep(100);
        	boolean backCameraExist = checkAvin.AVINstaus() == 0 ? true:false;
			if(backCameraExist){
				c1 = c;
			}else{
				c1 = null;
				
			}
        }
        if(c1 == null){
        	SettingBean.bShowBackWindow = false;
			savemebWin();
		}
        return c1;
    }
    
    private Camera getCameraBwin() {
        Camera c = null;
	    if(mCamera==null){
	        try {
	            c = Camera.open(mCameraId);
	        } catch (Exception e) {
	        	c = null;
	            Log.i("PLJ", "getCameraInstance Open camera fail:"+c);
	        }
        }else{
        	c = mCamera;
        }
        
        return c;
    }
    
    private Camera getCameraBwinSub(Camera c) {
        Camera c1 = null;
        for(int i=0; i<4; i++){
        	SystemClock.sleep(100);
        	boolean backCameraExist = checkAvin.AVINstaus() == 0 ? true:false;
			if(backCameraExist){
				c1 = c;
			}else{
				c1 = null;
			}
        }
        
        return c1;
    }

    private void closeCamera() {
        if (mCamera != null) {
            
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    private Parameters getParameters() {
        Parameters params = mCamera.getParameters();

        List<Integer> frameRates = params.getSupportedPreviewFrameRates();
        if (frameRates != null) {
            Integer max = Collections.max(frameRates);
            params.setPreviewFrameRate(max);
        } else {
            params.setPreviewFrameRate(15);
        }

        if (mbTakePicture) {
            params.setPreviewSize(640, 480); //1280, 720  16:9
        }

        params.setPictureFormat(PixelFormat.JPEG);
        params.setPictureSize(1280, 720);
        params.setJpegQuality(90);
        // 设置帧率
        params.setRecordingHint(true); 
        return params;
    }

    private void setMediaRecorder() {
        if (mMediaRecorder != null) {
            if (SettingBean.isRecordSound) {
                mMediaRecorder.setAudioEncodingBitRate(2*1024*1024);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][0]
            }
            mMediaRecorder.setVideoEncodingBitRate(2*1024*1024);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][7]
            mMediaRecorder.setVideoSize(640,480);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][11], Config.VIDEO_PROFILES[SettingBean.mVideoQuality][9]

            // 录像帧率
            try {   Log.i("PLJ", "DVRBackService---->setMediaRecorder:"+Config.VIDEO_PROFILES[SettingBean.mVideoQuality][10]+"   "+SettingBean.mVideoQuality);
                // mediaRecorder.setCaptureRate(videoFrameRate*2);
                mMediaRecorder.setVideoFrameRate(30/*Config.VIDEO_PROFILES[SettingBean.mVideoQuality][10]*/);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void setMediaRecorderForMicro() { //liujie add 0924
        if (mMediaRecorder != null) {
            if (SettingBean.isRecordSound) {
                mMediaRecorder.setAudioEncodingBitRate(512*1024);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][0]
            }
            mMediaRecorder.setVideoEncodingBitRate(512*1024);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][7]
            mMediaRecorder.setVideoSize(320,240);//Config.VIDEO_PROFILES[SettingBean.mVideoQuality][11], Config.VIDEO_PROFILES[SettingBean.mVideoQuality][9]

            // 录像帧率
            try {
                // mediaRecorder.setCaptureRate(videoFrameRate*2);
                mMediaRecorder.setVideoFrameRate(10);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void setupDirectorys() {
        try {
            File path = new File(Config.SAVE_PATH + "/");
            if (!path.exists()) {
                path.mkdirs();
            }

            path = new File(Config.SAVE_RECORD_PATH + "/");
            if (!path.exists()) {
                path.mkdirs();
            }
        } catch (Exception e) {
        }
    }

    private boolean checkSDCard(boolean bDealVideo) {
        if (!SDcardManager.checkSDCardMount()) {
            mHandler.sendEmptyMessage(MSG_SDCARD_UNREMOUNT_DLG_SHOW);
            return false;
        }
        if (!SDcardManager.checkSDCardAvailableSize(this, bDealVideo)) {
            mHandler.sendEmptyMessage(MSG_MEMORY_OUT_DLG_SHOW);
            return false;
        }
        return true;
    }

    private synchronized void updateStatus(final int status) {
        // 更新状�?控件
        mStatus = status;
        /*mHandler.post(new Runnable() {
            public void run() {
                if (mStatusView != null) {
                    mStatusView.updateStatus();
                }
            }
        });*/
    }

    private void saveDataToFile(byte[] data, File file) {
        FileOutputStream fos = null;
        if (file != null && file.exists()) {
            file.delete();
        } else {
            try {
                fos = new FileOutputStream(file);
                fos.write(data);
                fos.flush();
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fos = null;
                }
            }
        }
    }

    /** 设置摄像头的焦距 **/
    void setCameraZoom(int m_nzoomValue) {
    	if(mCamera == null || !SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 
    	     return; 
    	}

        Parameters params = mCamera.getParameters();
        try {
            params.setZoom(m_nzoomValue);
            mCamera.setParameters(params);
        } catch (Exception e) {
            e.printStackTrace();
        }
        params = null;
    }

    private PictureCallback mJpegPictureCallback = new PictureCallback() {
        public void onPictureTaken(byte[] data, Camera camera) {
            Log.e(TAG, "mJpegPictureCallback");
            if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){//liujie add 1013
	            saveDataToFile(data, mImageFile);
	            // 将照片保存至数据库中
	            ImageManager.addImage(mContentResolver, mFileName, System.currentTimeMillis(), null, mImageFile);
            }
            
            new Thread() {
                @Override
                public void run() {
                    startPreview();
                    updateStatus(Config.STATUS_IDLE);
                    setControlBarVisibility(true);
                }
            }.start();
        }
    };

    private String capture() {
        /*if (mCamera == null || isRecording()) {
            return false;
        }*/
        if (!checkSDCard(false))
            return "";
        mFileName = Config.BACK_CAMERA_FILE_SUFFIX + FORMAT.format(new Date());
        mUsbFileName = Config.FRONT_CAMERA_FILE_SUFFIX + FORMAT.format(new Date());

        String datePath = mFileName.substring(1, 5) + "-" + mFileName.substring(5, 7) + "-" + mFileName.substring(7, 9)/*+ Config.BACK_CAMERA_SUFFIX*/;//FRONT_CAMERA_SUFFIX
        File path = new File(Config.SAVE_CAPTURE_PATH + datePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        mImageFile = new File(path, mFileName + Config.IMAGE_SUFFIX);
        mUsbImageFile = new File(path, mUsbFileName + Config.IMAGE_SUFFIX);
        mInfoFile = new File(path, mFileName + Config.INFO_SUFFIX);
        libucamera.native_ucameracmd(libucamera.JCMD_TAKEPICTURE,  mUsbImageFile.getAbsolutePath() , 0, 0, 0);
        
        /*try {
        	if (mCamera != null) {
                mCamera.reconnect();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        while (!mPreviewing) {
            SystemClock.sleep(50);
        }
        updateStatus(STATUS_IMAGE_CAPTURING);
        Parameters params = mCamera.getParameters();
        params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
        int rotation = CameraUtil.getRotation(mCameraId);
        params.setRotation(rotation);
        // 只支持这种分辨率的图�?如果是其它机型需要再适配
        params.setPictureSize(640,480);//(1600, 1200)
        mCamera.setParameters(params);
        mCamera.takePicture(null, null, null, mJpegPictureCallback);*/

        updateStatus(STATUS_IMAGE_CAPTURING);
        // 记录拍照时的Gps数据
        GpsProvider.getGpsProvider(this).capture(Config.SAVE_CAPTURE_PATH + "/" + datePath + "/" + mFileName);
        
        ImageManager.addImage(mContentResolver, mUsbFileName, System.currentTimeMillis(), null, mUsbImageFile);//liujie add 1104
       
        if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){//liujie add 1009
        	return mUsbImageFile.getAbsolutePath()+","+mImageFile.getAbsolutePath();
        }else{
        	return mUsbImageFile.getAbsolutePath()+",";
        }
    }
    
    private String doubleCapture() {
        /*if (mCamera == null || isRecording()) {
            return false;
        }*/
        if (!checkSDCard(false))
            return "";
        mFileName = Config.BACK_CAMERA_FILE_SUFFIX + FORMAT.format(new Date());
        mUsbFileName = Config.FRONT_CAMERA_FILE_SUFFIX + FORMAT.format(new Date());

        String datePath = mFileName.substring(1, 5) + "-" + mFileName.substring(5, 7) + "-" + mFileName.substring(7, 9) /*+ Config.BACK_CAMERA_SUFFIX*/;//FRONT_CAMERA_SUFFIX
        File path = new File(Config.SAVE_CAPTURE_PATH + datePath);
        if (!path.exists()) {
            path.mkdirs();
        }

        mImageFile = new File(path, mFileName + Config.IMAGE_SUFFIX);
        mUsbImageFile = new File(path, mUsbFileName + Config.IMAGE_SUFFIX);
        mInfoFile = new File(path, mFileName + Config.INFO_SUFFIX);
        libucamera.native_ucameracmd(libucamera.JCMD_TAKEPICTURE,  mUsbImageFile.getAbsolutePath() , 0, 0, 0);
        
        if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){
	        try {
	        	if (mCamera != null) {
	                mCamera.reconnect();
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	        while (!mPreviewing) {
	            SystemClock.sleep(50);
	        }
	        updateStatus(STATUS_IMAGE_CAPTURING);
	        Parameters params = mCamera.getParameters();
	        params.setFocusMode(Parameters.FOCUS_MODE_AUTO);
	        //int rotation = CameraUtil.getRotation(mCameraId);
	        //params.setRotation(rotation);
	        // 只支持这种分辨率的图�?如果是其它机型需要再适配
	        params.setPictureSize(640,480);//(1600, 1200)
	        mCamera.setParameters(params);
	        mCamera.takePicture(null, null, null, mJpegPictureCallback);
        }
        
        ImageManager.addImage(mContentResolver, mUsbFileName, System.currentTimeMillis(), null, mUsbImageFile);
        // 记录拍照时的Gps数据
        GpsProvider.getGpsProvider(this).capture(Config.SAVE_CAPTURE_PATH + "/" + datePath + "/" + mFileName);
        if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){//liujie add 1009
        	return mUsbImageFile.getAbsolutePath()+","+mImageFile.getAbsolutePath();
        }else{
        	return mUsbImageFile.getAbsolutePath()+",";
        }
    }

    /**
     * 开始录制
     * */
    private void recordVideo() { Log.i("PLJ", "DVRBackService---->recordVideo:111");
        if (mMediaRecorder == null) {
            mMediaRecorder = new MediaRecorder();
        }
        if (mCamera != null) {
            mCamera.unlock();
            mMediaRecorder.setCamera(mCamera);
        }
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        if (SettingBean.isRecordSound) {
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        }

        // 录像保存格式 ,默认为mp4
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// THREE_GPP);//
        // 设置录像视频编码方式 ，默认为MPEG_4_SP
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// H263);//
        // 录像帧率
        //mMediaRecorder.setVideoFrameRate(15);
        // 设置音频编码方式,默认为AMR_NB
        if (SettingBean.isRecordSound) {
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.HE_AAC);
        }
        // 设置录像画面质量:audioBitRate、videoBitRate、videoFrameHeight、videoFrameWidth
        if(!bUsbCameraInit1){ //liujie add 0924
        	setMediaRecorder();
        }else{
        	setMediaRecorderForMicro();
        }
        
        mFileName = FORMAT.format(new Date());
        String filePathFormat = mFileName.substring(0, 4) + "-" + mFileName.substring(4, 6) + "-" + mFileName.substring(6, 8);
        String recordPath = null;
        if(bUsbCameraInit1){ //liujie modify 1020
        	recordPath = Config.SAVE_MICRO_PATH /*+ filePathFormat + Config.SAVE_LOOP_PATH*/;
        }else{
            recordPath = Config.SAVE_RECORD_PATH /*+ filePathFormat + Config.SAVE_LOOP_PATH*/;
        }
        File path = new File(recordPath);
        if (!path.exists()) {
            try {
                path.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        if (SettingBean.mOpenBackCamera) {
        	/*mFileNameBack = FORMAT.format(new Date()); // penglj_lockfile_20150601
            String recordPath_back = Config.SAVE_RECORD_PATH + fileNameFormat + Config.BACK_CAMERA_SUFFIX + Config.SAVE_LOOP_PATH;
            // String recordPath_back = "/mnt/sdcard2/DVR/video/" + datapath_back + Config.SAVE_LOOP_PATH;
            File path_back = new File(recordPath_back);
            if (!path_back.exists()) {
                try {
                	path_back.mkdirs();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }*/
            //mVideoFileBack = new File(recordPath_back, mFileNameBack + Config.VIDEO_SUFFIX_BACK);
        	mFileNameBack = mFileName + Config.FRONT_CAMERA_SUFFIX;//liujie ad 1104
            mVideoFileBack = new File(recordPath, mFileName + Config.FRONT_CAMERA_SUFFIX + Config.VIDEO_SUFFIX); //VIDEO_SUFFIX_BACK BACK_CAMERA_SUFFIX
			mVideoFileBack1= new File(recordPath, mFileName + Config.FRONT_CAMERA_SUFFIX + Config.VIDEO_SUFFIX);//penglj_lockfile_20150601 //BACK_CAMERA_SUFFIX
        }

        mVideoTmpFile = new File(recordPath, "b" + Config.VIDEO_SUFFIX + ".tmp");
        mVideoFile = new File(recordPath, mFileName + Config.BACK_CAMERA_SUFFIX + Config.VIDEO_SUFFIX);//FRONT_CAMERA_SUFFIX
        mInfoFile = new File(recordPath, mFileName + Config.INFO_SUFFIX);
        
        if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
        	mMediaRecorder.setOutputFile(mVideoTmpFile.getAbsolutePath());
        }
        // 设置预览
        if (cameraSurView != null) {
            mMediaRecorder.setPreviewDisplay(cameraSurView.getHolder().getSurface());
        }
        try {
        	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
	            mMediaRecorder.prepare(); // 预期准备
	            mMediaRecorder.start();// 开始录制
        	}
            usbCameraSurfaceView.BackCameraRecord(mVideoFileBack.getAbsolutePath(),SettingBean.bBeforeSound?0:1);
            mStartRecordTime =  SystemClock.uptimeMillis()/*System.currentTimeMillis()*/;//liujie 1024
            if(!bUsbCameraInit1){ //liujie modify 1020
            	GpsProvider.getGpsProvider(this).startRecoder(recordPath + mFileName + Config.BACK_CAMERA_SUFFIX);// 同时�?��记录 //FRONT_CAMERA_SUFFIX 
            }
            //mState = CURRENT_STATE_RECODING;
            Config.gbRecordStatus = true;
            mHandler.sendEmptyMessageDelayed(MODE_LOOP_STOP, SettingBean.CirVideoArray[SettingBean.cirVideoLong] * 1000 * 60);
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //updateStatus(STATUS_LOOP_RECORDING);

        //updateUI();
    }

    private File createLockFile(File initfile) {
        String fileName = initfile.getName();
        String path = initfile.getParent();
        String newfileAllpath;
        File lockfile;

        mFileName = fileName.substring(0, fileName.length() - 4) + Config.LOCK_FILE_SUFFIX; // 去掉后缀，加上锁标志
        newfileAllpath = path + "/" + mFileName + Config.VIDEO_SUFFIX;
        lockfile = new File(newfileAllpath);

        return lockfile;
    }

    private File createLockFileBack(File initfile) {
        String fileName = initfile.getName();
        String path = initfile.getParent();
        String newfileAllpath;
        File lockfile;

        mFileNameBack = fileName.substring(0, fileName.length() - 4) + Config.LOCK_FILE_SUFFIX; // 去掉后缀，加上锁标志
        newfileAllpath = path + "/" + mFileNameBack + Config.VIDEO_SUFFIX;
        lockfile = new File(newfileAllpath);

        return lockfile;
    }

    private void stopRecord(int p1) {Log.i("PLJ", "DVRBackService---->stopRecord:000:"+(mMediaRecorder == null)+"   "+mVideoLock+"   "+p1);
        if (mMediaRecorder == null) {
            return;
        }
        mState = 0;
        updateStatus(STATUS_SAVING);
        boolean bCanDelete;
        if (mVideoLock || mHitSave) {
            bCanDelete = false;
            if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
            	mVideoFile = createLockFile(mVideoFile);
            }
            blsFile1 = mVideoFileBack1;
            mVideoFileBack1 = blsFile2 = createLockFileBack(mVideoFileBack1);
        } else {
            bCanDelete = true;
        }
        
        try {
            //mMediaRecorder.setOnErrorListener(null);//liujie add 1013
        	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ ////liujie add 1009
        		mMediaRecorder.stop();
        	}
            mMediaRecorder.reset();
            if (mVideoLock){ usbCameraSurfaceView.BackCameraStopRec(mVideoFileBack1.getAbsolutePath(),p1); 
            }else{ usbCameraSurfaceView.BackCameraStopRec("",p1); }
            mStopRecordTime = SystemClock.uptimeMillis();/*System.currentTimeMillis()*/;//liujie 1024
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        if (mCamera != null)
            mCamera.lock();
        
        // SD卡是否移除
        if (!mDeviceUnmount) {
        	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
        		mVideoTmpFile.renameTo(mVideoFile);
        		saveVideo(bCanDelete);
        	}
        	
            mVideoFileBack.renameTo(mVideoFileBack1);
            //new StopFileOption().execute(bCanDelete);
            saveVideo_back(bCanDelete, mVideoFileBack1);
            if(!bUsbCameraInit1){ //liujie modify 1020
            	GpsProvider.getGpsProvider(DVRBackService.this).stopRecoder();
            }
        }
        
        Config.gbRecordStatus = false;
        mDeviceUnmount = false;
        if (mVideoLock){
        	mVideoLock = false;
        	btn_Lock.setBackgroundResource(R.drawable.btn_main_lock_normal);
        }
    }

    class StopFileOption extends AsyncTask<Object, Object, Object> {

        @Override
        protected String doInBackground(Object... arg0) {
            boolean bool = ((Boolean)arg0[0]).booleanValue();
	    	saveVideo(bool);
	        saveVideo_back(bool, mVideoFileBack1);
	        GpsProvider.getGpsProvider(DVRBackService.this).stopRecoder();
            /*try {
                usbCameraSurfaceView.BackCameraStopRec();
                mMediaRecorder.stop();
                mMediaRecorder.reset();
                mStopRecordTime = System.currentTimeMillis();
            } catch (RuntimeException e) {
                e.printStackTrace();
            }
            if (mCamera != null)
                mCamera.lock();
        	
            boolean bCanDelete;
            File mFile = null;
            if (mVideoLock || mHitSave) {//penglj_lockfile_20150601 mod start
                bCanDelete = false;
                mVideoFile = createLockFile(mVideoFile);
                blsFile1 = mVideoFileBack1;
                mVideoFileBack1 = blsFile2 = createLockFileBack(mVideoFileBack1);
            } else {
                bCanDelete = true;
            }

            // SD卡是否移除
            if (!mDeviceUnmount) {
            	mVideoTmpFile.renameTo(mVideoFile);
                mVideoFileBack.renameTo(mVideoFileBack1);
                saveVideo(bCanDelete);
                saveVideo_back(bCanDelete, mVideoFileBack1);
                GpsProvider.getGpsProvider(DVRBackService.this).stopRecoder();
            }
            
            Config.gbRecordStatus = false;
            mDeviceUnmount = false;
            if (mVideoLock){
            	Message message = new Message();                                            
                mpHandler.sendMessageDelayed(message, 2000);
            }
            mVideoLock = false;*/

            return null;
        }
       
    }//penglj_lockfile_20150601 add end

    /**
     * 将视频保存至数据库中
     */
    private void saveVideo(boolean canDelete) {
    	
    	if(bUsbCameraInit1){ //liujie modify 1020
    		return;
    	}
    	
        String title = mFileName;
        long dateTaken = System.currentTimeMillis();
        File file = mVideoFile;// mVideoFile;
        long duration = 0;
        if (mStopRecordTime > mStartRecordTime)
            duration = mStopRecordTime - mStartRecordTime;
        VideoManager.addVideo(mContentResolver, file.getName(), dateTaken, duration, file, canDelete);
    }

    private void saveVideo_back(boolean canDelete, File videofile) {
    	
    	if(bUsbCameraInit1){ //liujie modify 1020
    		return;
    	}
    	
        long duration = 0;
        File file = mVideoFileBack1;//penglj_lockfile_20150601
        if (mStopRecordTime > mStartRecordTime)
            duration = mStopRecordTime - mStartRecordTime;
        VideoManager.addBackVideo(mContentResolver, mFileNameBack, System.currentTimeMillis(), duration, file, canDelete);//penglj_lockfile_20150601
    }

    private void showMicStatus() {
        if (btn_Mic != null) {
            if (SettingBean.bBeforeSound/*SettingBean.isRecordSound*/) {
                btn_Mic.setImageDrawable(getResources().getDrawable(R.drawable.btn_main_mic_pressed));
            } else {
                btn_Mic.setImageDrawable(getResources().getDrawable(R.drawable.btn_main_mic_normal));
            }
        }
    }

    private void createView() {
        // 加载布局文件
        if (mFloatView == null) {
            mFloatView = LayoutInflater.from(this).inflate(R.layout.service_recorder, null);
        }
        if (mCamera != null) {
        	mCameraLayout = LayoutInflater.from(this).inflate(R.layout.camerasurfacelayout, null);
        	cameraSurView = (CameraSurfaceView)mCameraLayout.findViewById(R.id.cameraprevView); //cameraSurView = new CameraSurfaceView(this, mCamera); 
        	cameraSurView.setCameraSurfaceView(this, mCamera);
        	imageAvin = (ImageView)mCameraLayout.findViewById(R.id.imageavin);
        } Log.i("PLJ", "DVRBackService---->createView:111:"+(usbCameraSurfaceView == null));
        //usbCameraSurfaceView = new CameraPreview(this);
        if (usbCameraSurfaceView == null) {
        	mUsbCameraLayout = LayoutInflater.from(this).inflate(R.layout.usbcameralayout, null);
        	usbCameraSurfaceView = (CameraPreview)mUsbCameraLayout.findViewById(R.id.usbcameraview);
        	usbCameraSurfaceView.setCameraPreview(this);
        }
        /*
        if(mCameraLayout!=null){
        	mCameraLayout.setVisibility(Config.bBackCameraExist ?View.VISIBLE: View.INVISIBLE); //liujie removed 1029
        }*/
        Log.i("PLJ", "DVRBackService---->createView:222:"+this.nScreenWidth+"    "+this.nScreenHight);
        createUsbCamreaWindow();
        if (usbCameraSurfaceView != null) {
            mWindowManager.addView(mUsbCameraLayout/*usbCameraSurfaceView*/, usbCameraLayoutParams);
        }

        createCamreaWindow();
        if (mCameraLayout != null) {//cameraSurView
            mWindowManager.addView(mCameraLayout, cameraLayoutParams);//cameraSurView
        }
        
        mWindowManager.addView(mFloatView, usbCameraLayoutParams);

        initLayout();

        mPowerSaveString = getResources().getStringArray(R.array.setting_power_saving_str);

        showOrHideBackCameraWindow();//liujie add 1029
        //startPreview();
        
        
        mHandler.sendEmptyMessageDelayed(MSG_GET_AVIN_STATUS, AVIN_TIME_OFFSET); //liujie add 1009
        
    }

    /**
     * 创建主显示区域 
     * */
    private void createCamreaWindow() {
        /* 为View设置参数 */
        cameraLayoutParams = new WindowManager.LayoutParams();
        // 设置View默认的摆放位�?
        cameraLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        // 设置window type
        cameraLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        // 设置背景为�?�?
        cameraLayoutParams.format = PixelFormat.RGBA_8888;
        // 注意该属性的设置很重要，FLAG_NOT_FOCUSABLE使浮动窗口不获取焦点,若不设置该属性，屏幕的其它位置点击无效，应为它们无法获取焦点
        cameraLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置视图的显示位置，通过WindowManager更新视图的位置其实就是改�?x,y)的�?
        cameraLayoutParams.x = 45;
        cameraLayoutParams.y = 45;
        // 设置视图的宽、高
        if (this.nScreenHight == 480) {
            cameraLayoutParams.width = 200;
            cameraLayoutParams.height = 200;
        }else if (this.nScreenHight == 540) {
        	cameraLayoutParams.width = 200;
            cameraLayoutParams.height = 200;
        } else {
            cameraLayoutParams.width = 1;
            cameraLayoutParams.height = 1;
        }
    }

    private void createUsbCamreaWindow() {
        usbCameraLayoutParams = new WindowManager.LayoutParams();
        usbCameraLayoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        usbCameraLayoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        usbCameraLayoutParams.format = PixelFormat.RGBA_8888;
        usbCameraLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        usbCameraLayoutParams.x = 1;
        usbCameraLayoutParams.y = 1;
        /*usbCameraLayoutParams.width = 200;
        usbCameraLayoutParams.height = 200;*/
     // 设置视图的宽、高
        if (this.nScreenHight == 480) {
        	usbCameraLayoutParams.width = LayoutParams.MATCH_PARENT;//854;
        	usbCameraLayoutParams.height = LayoutParams.MATCH_PARENT;//480;
        }else if (this.nScreenHight == 540) {
        	usbCameraLayoutParams.width = LayoutParams.MATCH_PARENT;//854;
        	usbCameraLayoutParams.height = LayoutParams.MATCH_PARENT;//480;
        }else {
        	usbCameraLayoutParams.width = 1;
        	usbCameraLayoutParams.height = 1;
        }
    }

    public void onStatusChanged() {
        /*if (mStatus == STATUS_LOOP_RECORDING) {
            mStatusView.setText(R.string.status_loop_recording);
            mStatusView.setTextColor(Color.GREEN);
        } else if (mStatus == STATUS_REGULAR_RECORDING) {
            mStatusView.setText(R.string.status_regular_recording);
            mStatusView.setTextColor(Color.GREEN);
        } else if (mStatus == STATUS_SAVING) {
            mStatusView.setText(R.string.status_saving);
            mStatusView.setTextColor(Color.YELLOW);
        } else if (mStatus == STATUS_IMAGE_CAPTURING) {
            mStatusView.setText(R.string.status_taking_picture);
            mStatusView.setTextColor(Color.GREEN);
        } else {
            mStatusView.setText("");
        }*/
    }

    public void onGPSInfoChanged(DrivePosition position) {
        final DrivePosition pos = position;
        String gps = position.mLongitude+","+position.mLatitude+","+position.mSpeed;
        if(usbCameraSurfaceView != null){ //liujie add 1013
	        usbCameraSurfaceView.BackCameraSetTimeInfo("");
	        usbCameraSurfaceView.BackCameraSetGpsInfo(gps);
        }
        /*mHandler.post(new Runnable() {
            public void run() {
                setInfo(pos);
            }
        });*/
    }

    private void setInfo(DrivePosition position) {
        /*boolean valid = position.mValid;
        if (valid) {
            mLongitudeView.setText(StringUtil.loactionToString(position.mLongitude, true));
            mLatitudeView.setText(StringUtil.loactionToString(position.mLatitude, false));
            mSpeedView.setText(mNumberFormat.format(position.mSpeed) + " km/h");
        } else {
            mLongitudeView.setText(R.string.ellipsis_text);
            mLatitudeView.setText(R.string.ellipsis_text);
            mSpeedView.setText(R.string.ellipsis_text);
        }*/
    }
	
	public static void PerviewSurfave(int id, byte[]frame, int size){
		Log.e(TAG, "***id = " + id + ", frame = " + frame + ", size=" + size + "***");
		//File fp = new File("/mnt/sdcard/1.jpg");
		try{
			BitmapFactory.decodeByteArray(frame, 0, frame.length) ;
		}catch (Exception e) {
			e.printStackTrace();
		}
    }

    private void initLayout() {
        smallViewUIRect = new Rect(45, 45, 200, 200);
        
        serviceRelative = (RelativeLayout)mFloatView.findViewById(R.id.service_relative);
        takePictureBtn = (TextView) mFloatView.findViewById(R.id.btn_takepicture);
        startRecordBtn = (ImageView) mFloatView.findViewById(R.id.startrecord);
        stopRecordBtn = (TextView) mFloatView.findViewById(R.id.stoprecord);
        settingsBtn = (TextView) mFloatView.findViewById(R.id.set);
        playListBtn = (TextView) mFloatView.findViewById(R.id.btn_playback);
        presWitchBtn = (TextView) mFloatView.findViewById(R.id.preswitch);
        //mChronometer = (Chronometer) mFloatView.findViewById(R.id.recordTime_run);
        setPreviewLayout = (RelativeLayout) mFloatView.findViewById(R.id.m_setting);
        //bottomInfoLayout = (LinearLayout) mFloatView.findViewById(R.id.info_bar);

        settingUIRect = new Rect();

        btn_Mic = ((MyImageButton) mFloatView.findViewById(R.id.btn_mic));
        btn_Mic.setOnClickListener(BtnOnClickListener);
        showMicStatus();

        btn_Lock = ((TextView) mFloatView.findViewById(R.id.btn_lock_video));
        btn_Lock.setOnClickListener(BtnOnClickListener);

        /*mStatusView = (StatusView) mFloatView.findViewById(R.id.info_status);
        mStatusView.setOnStatusChangedListener(this);
        mLongitudeView = (TextView) mFloatView.findViewById(R.id.info_longitude);
        mLatitudeView = (TextView) mFloatView.findViewById(R.id.info_latitude);
        mSpeedView = (TextView) mFloatView.findViewById(R.id.info_speed);*/

        mNumberFormat = NumberFormat.getInstance();
        mNumberFormat.setMinimumFractionDigits(2);
        mNumberFormat.setMaximumFractionDigits(2);

        setPreviewLayout.setVisibility(View.INVISIBLE);
        takePictureBtn.setOnClickListener(BtnOnClickListener);
        startRecordBtn.setOnClickListener(BtnOnClickListener);
        stopRecordBtn.setOnClickListener(BtnOnClickListener);
        settingsBtn.setOnClickListener(BtnOnClickListener);
        playListBtn.setOnClickListener(BtnOnClickListener);
        presWitchBtn.setOnClickListener(BtnOnClickListener);

        mHandler.sendEmptyMessageDelayed(MSG_HIDE_UNNORMA_USE_BTN, 1000 * mHideAllBtnDelay);

        mFloatView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                PowerUitl.getPowerUitl().onDisplayOut();
                PowerUitl.getPowerUitl().onDisplayIn();
                if (!mbOpenSettingUi && !mOnBackground) {
                    if (!mShowunNormalBtn && !bAvinPutIn) {
                        setunNormalBtnVisibility(View.VISIBLE);
                        mHandler.sendEmptyMessageDelayed(MSG_HIDE_UNNORMA_USE_BTN, 1000 * mHideAllBtnDelay);
                    } else {
                        mHandler.removeMessages(MSG_HIDE_UNNORMA_USE_BTN);
                        mHandler.sendEmptyMessage(MSG_HIDE_UNNORMA_USE_BTN);
                    }
                }
            }
        });

        mFloatView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View arg0, MotionEvent event) {
                if (bAvinPutIn) {
                    return false;
                }
                int x = (int) event.getX();
                int y = (int) event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (isCameraViewSmall || smallViewUIRect.contains(x, y)) {
                        //checkCameraWindow();
                        if (isCameraViewSmall){ 
                            mWindowManager.updateViewLayout(mCameraLayout, cameraLayoutParams);
                            usbCameraSurfaceView.setZOrderOnTop(true);
                            usbCameraSurfaceView.setZOrderMediaOverlay(true);
                            isCameraViewSmall = false;
                        } else {
                            mWindowManager.updateViewLayout(mCameraLayout, usbCameraLayoutParams);
                            cameraSurView.setZOrderOnTop(true);
                            cameraSurView.setZOrderMediaOverlay(true);
                            isCameraViewSmall = true;
                        }
                    }
                }

                if (mbOpenSettingUi) {
                    if (!settingUIRect.contains(x, y)) {
                        mHandler.sendEmptyMessage(MSG_CLOSE_SETTING_UI);
                        mShowunNormalBtn = false; // 点击设置外的区域，设置消失，同时2个图标也隐藏了�?
                    }
                }
                return false;
            }
        });
    }

    /**
     * 切换窗口
     * */
    private void checkCameraWindow() {
        //  如果主摄像头是在小窗口位置
        if (isCameraViewSmall){
        	imageAvin.setVisibility(View.GONE);
        	Config.AvinHomeBack = true;
            mWindowManager.updateViewLayout(mCameraLayout, cameraLayoutParams);//cameraSurView
            usbCameraSurfaceView.setZOrderOnTop(true);
            usbCameraSurfaceView.setZOrderMediaOverlay(true);
            isCameraViewSmall = false;
        } else {
        	imageAvin.setVisibility(View.VISIBLE);
        	Config.AvinHomeBack = false;
            mWindowManager.updateViewLayout(mCameraLayout, usbCameraLayoutParams);//cameraSurView
            cameraSurView.setZOrderOnTop(true);
            cameraSurView.setZOrderMediaOverlay(true);
            isCameraViewSmall = true;
        }
    }

    private void cancleRecord() {
        //mChronometer.stop();
        //mChronometer.setBase(SystemClock.elapsedRealtime());
		// liujie modify begin 0828
		/*
		 * DvrStatusIntent.putExtra("dvrstatus", 2);
		 * sendBroadcast(DvrStatusIntent);
		 */
		//try {
			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING);
		//} catch (Exception localException) {}
		// liujie modify end
        mState = CURRENT_STATE_UNRECODING;
        updateUI();
    }

    private void saveRecordSound() {
        SharedPreferences uiState = getSharedPreferences("xgx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uiState.edit();
        editor.putBoolean("bBeforeSound", SettingBean.bBeforeSound);//editor.putBoolean("isRecordSound", SettingBean.isRecordSound);
        editor.commit();
    }

    private void saveClientType() {
        SharedPreferences uiState = getSharedPreferences("xgx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uiState.edit();
        editor.putBoolean("mLuRuiTong", SettingBean.misLuRuiTongClient);
        editor.commit();
    }

    private void ServiceStopRecord(boolean isLoop) {
    	isLoopRec = isLoop;
        if (mState == 1) {
            mHandler.removeMessages(MODE_START_RECORD);
            mHandler.removeMessages(MODE_LOOP_STOP);

            //stopRecord();
            if (!isLoop) {
            	stopRecord(0);
                cancleRecord();
            } else {
            	stopRecord(1);
                //startRecord();
            }
            //updateStatus(Config.STATUS_IDLE);
        }
    }

    private void ServiceStartRecord() {
        /*if (!SDcardManager.checkSDCardMount()) {
            mHandler.sendEmptyMessage(MSG_SDCARD_UNREMOUNT_DLG_SHOW);
            return;
        }*/
        if (mState == CURRENT_STATE_UNRECODING) {
        	//mChronometer.setBase(SystemClock.elapsedRealtime());
            //mChronometer.start(); 
			// liujie modify begin 0828
			/*
			 * DvrStatusIntent.putExtra("dvrstatus", 3);
			 * sendBroadcast(DvrStatusIntent);
			 */

			// liujie modify end
            mState = CURRENT_STATE_RECODING;
            updateUI();
            startRecord();
        }
    }

    private void updateUI() {
        switch (mState) {
        case CURRENT_STATE_UNRECODING:
            startRecordBtn.setVisibility(View.VISIBLE);
            stopRecordBtn.setVisibility(View.INVISIBLE);
            break;
        case CURRENT_STATE_RECODING:
            startRecordBtn.setVisibility(View.INVISIBLE);
            stopRecordBtn.setVisibility(View.VISIBLE);
            break;
        case CURRENT_STATE_HIDE:
            stopRecordBtn.setVisibility(View.INVISIBLE);
            startRecordBtn.setVisibility(View.INVISIBLE);
            break;
        }
    }

    private void startRecord() { Log.i("PLJ", "DVRBackService---->startRecord:");
        mbReadyStartRecord = true;
        if (!checkSDCard(true)) {
            // 重新录制的时候就没有空间�?015.01.08
            mHandler.removeMessages(MODE_START_RECORD);
            mHandler.removeMessages(MODE_LOOP_STOP);

            //liujie add begin 0906 
            mState = CURRENT_STATE_UNRECODING; 
		    //try { 
		    Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING); 
		    //} catch (Exception localException) { } 
		    //liujie add end
            updateUI();
            cancleRecord();
            updateStatus(Config.STATUS_IDLE);
            mbReadyStartRecord = false;
            return;
        }
        //liujie add begin 0910
		//try {
			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_RECODING);
		//} catch (Exception localException) {}
        //mState = CURRENT_STATE_RECODING;
        //updateUI();
        
        if (mIsPowerOnAutorun) {
            //showBackgroudNotification(getString(R.string.background_recording));
            mOnBackground = true;
        }
        new startRecordTask().execute(); //recordVideo(); 
        mbReadyStartRecord = false;
    }
    
    //penglj_startRecord
    class startRecordTask extends AsyncTask<Object, Object, Object> {
    	//onPreExecute方法用于在执行后台任务前做一些UI操作
    	//doInBackground方法内部执行后台任务,不可在此方法内修改UI  
        @Override
        protected String doInBackground(Object... arg0) { Log.i("PLJ", "DVRBackService---->startRecordTask:");
        	recordVideo();
            return null;
        }
        //onProgressUpdate方法用于更新进度信息  
        //onPostExecute方法用于在执行完后台任务后更新UI,显示结果
        //onCancelled方法用于在取消执行中的任务时更改UI  
		@Override
		protected void onPostExecute(Object result) {
			//super.onPostExecute(result);
			mState = CURRENT_STATE_RECODING;
			updateStatus(STATUS_LOOP_RECORDING);
			mbTakePicture = false;
			isDvrStart = false;
			isInitDvrRec = true;
		}
    }

    private void setControlBarVisibility(final boolean bEnable) {
        mHandler.post(new Runnable() {
            public void run() {
                takePictureBtn.setEnabled(bEnable);
                startRecordBtn.setEnabled(bEnable);
                stopRecordBtn.setEnabled(bEnable);
                btn_Lock.setEnabled(bEnable);
                btn_Mic.setEnabled(bEnable);
                playListBtn.setEnabled(bEnable);
            }
        });
    }

    private void setMainViewBtnVisibility(final int nViewable) {
    	serviceRelative.setVisibility(nViewable);
        /*mHandler.post(new Runnable() {
            public void run() {
                takePictureBtn.setVisibility(nViewable);
                if (isRecording()) {
                    startRecordBtn.setVisibility(View.INVISIBLE);
                    stopRecordBtn.setVisibility(nViewable);
                } else {
                    startRecordBtn.setVisibility(nViewable);
                    stopRecordBtn.setVisibility(View.INVISIBLE);
                }
                btn_Lock.setVisibility(nViewable);
                btn_Mic.setVisibility(nViewable);
                playListBtn.setVisibility(nViewable);
                settingsBtn.setVisibility(nViewable);
                bottomInfoLayout.setVisibility(nViewable);
                mChronometer.setVisibility(nViewable);
                ((TextView) mFloatView.findViewById(R.id.recordTime)).setText(getResources().getString(
                        R.string.total_record_time));
                ((TextView) mFloatView.findViewById(R.id.recordTime)).setVisibility(nViewable);
            }
        });*/
    }
    
    public void SurCameraVisibility(final int ViewVisibility){
    	usbCameraSurfaceView.setVisibility(ViewVisibility);
    	if (mCameraLayout != null) {//liujie add 1029
    		mCameraLayout.setVisibility(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/ ? ViewVisibility :View.GONE);//cameraSurView liujie add 1009
    	}
        mFloatView.setVisibility(ViewVisibility);
    }

    /**
     * 设置按钮与播放按钮隐藏
     * */
    private void setunNormalBtnVisibility(final int nViewable) {
        mHandler.post(new Runnable() {
            public void run() {
                if (nViewable == View.VISIBLE) {
                    mShowunNormalBtn = true;
                } else {
                    mShowunNormalBtn = false;
                }
                playListBtn.setVisibility(nViewable);
                settingsBtn.setVisibility(nViewable);
            }
        });
    }

    /**
     * 显示错误对话框
     * */
    private void showErrorPopupDialog(Context context, View parent, final int type, int titleTextId, int messageTextId, int btnTextId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow = inflater.inflate(R.layout.error_popup_dialog, null, false);
        final PopupWindow pw = new PopupWindow(vPopupWindow, 336, 196, true);

        TextView titleTv = (TextView) vPopupWindow.findViewById(R.id.message_title_text);
        TextView messageTv = (TextView) vPopupWindow.findViewById(R.id.message_text);
        Button btnOK = (Button) vPopupWindow.findViewById(R.id.BtnOK);
        if (messageTextId != 0) {
            messageTv.setText(messageTextId);
        }
        if (titleTextId != 0) {
            titleTv.setText(titleTextId);
        }
        if (btnTextId != 0) {
            btnOK.setText(btnTextId);
        }
        btnOK.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();// 关闭
                switch (type) {
                case MSG_MEMORY_OUT_DLG_SHOW:
                    updateFloatView(0, 0, 1, 1);
                    Intent intent = new Intent(DVRBackService.this, ShowPActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    break;
                case MSG_SDCARD_UNREMOUNT_DLG_SHOW:
                    if (mOnBackground) {
                        // 后台录制，出现拔卡或者连接电脑，把复位�?出去
                        DVRBackService.this.stopSelf();
                    }
                    break;
                case MSG_OPEN_CAMERA_FAIL_DLG_SHOW:
                case MSG_EXIT_STOP_RECORD_FIRST:
                    break;
                }
            }
        });

        // 显示popupWindow对话框
        if (parent != null) {
            if (type == MSG_EXIT_STOP_RECORD_FIRST) {
                pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
            } else {
                pw.showAtLocation(parent, Gravity.NO_GRAVITY, (nScreenWidth - 336) / 2, (nScreenHight - 196) / 2);
            }
        }
    }

    private void dealWithBtnSetOrPlayback(int btnId, int userInput) {
        if (userInput == 1) {
            ServiceStopRecord(false);
            if (R.id.btn_playback == btnId) {
                updateFloatView(0, 0, 1, 1);
                Intent intent = new Intent(DVRBackService.this, ShowPActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else if (R.id.btn_takepicture == btnId) {
            	mHandler.removeMessages(MSG_IMAGE_CAPTURE);
                mHandler.sendEmptyMessageDelayed(MSG_IMAGE_CAPTURE, 500);
            }
        }
    }

    private void showPopupWindow(Context context, View parent, int btnId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow = inflater.inflate(R.layout.pop_up_dlg, null, false);
        final PopupWindow pw = new PopupWindow(vPopupWindow, 336, 196, true);
        final int id = btnId;

        // OK按钮及其处理事件
        Button btnOK = (Button) vPopupWindow.findViewById(R.id.BtnOK);
        btnOK.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                dealWithBtnSetOrPlayback(id, 1);
                pw.dismiss();// 关闭
            }
        });

        // Cancel按钮及其处理事件
        Button btnCancel = (Button) vPopupWindow.findViewById(R.id.BtnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();// 关闭
            }
        });

        // 显示popupWindow对话�?
        if (parent != null) {
            pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
    }

    private OnClickListener BtnOnClickListener = new OnClickListener() {
        public void onClick(View v) {
            PowerUitl.getPowerUitl().onDisplayOut();
            PowerUitl.getPowerUitl().onDisplayIn();
            mHandler.removeMessages(MSG_HIDE_UNNORMA_USE_BTN);

            mCurrentClickTime = System.currentTimeMillis();
            if (mCurrentClickTime - mLastClickTime < 800)
                return;

            mLastClickTime = mCurrentClickTime;

            switch (v.getId()) {
            case R.id.startrecord:
            	isDvrStart = true;
                ServiceStartRecord();
                break;
            case R.id.stoprecord:
            	if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 0){
            		if(!isDvrStart){
                        ServiceStopRecord(false);}
            	}else if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 1){
            		playVoice(getString(R.string.microplay_noaction));
            	}
                break;
            case R.id.set:
                mbOpenSettingUi = !mbOpenSettingUi;
                Config.gbOpenSetting = mbOpenSettingUi;
                initSetActivity();
                break;
            case R.id.btn_mic:
            	if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
	                //SettingBean.isRecordSound = !SettingBean.isRecordSound;
	                //saveRecordSound();
	            	SettingBean.bBeforeSound = !SettingBean.bBeforeSound;
	            	saveRecordSound();
	                showMicStatus();
                }
                break;
            case R.id.btn_lock_video:
                if (isRecording() && !mVideoLock) {
                	/*mHandler.removeMessages(MODE_LOOP_STOP);
                    mHandler.sendEmptyMessage(MODE_LOOP_STOP);*/
					
					if (mState == 1) {
			            //mHandler.removeMessages(MODE_START_RECORD);
			            //mHandler.removeMessages(MODE_LOOP_STOP);
			            
			            mHandler.post(new Runnable() {
				            public void run() {
				            	btn_Lock.setBackgroundResource(R.drawable.btn_main_lock_pressed);
				            }
				        });
			            //stopRecord(0);
			            mVideoLock = true;
			            //mHandler.removeMessages(MODE_START_RECORD);
			    		//mHandler.sendEmptyMessage(MODE_START_RECORD);
			            //updateStatus(Config.STATUS_IDLE);
			        }
                }
                break;
            case R.id.btn_playback:
                if (isRecording() || mbTakePicture) {
                	playVoice(getString(R.string.if_playback_show)); //showPopupWindow(DVRBackService.this, v, R.id.btn_playback);
                } else {
                    dealWithBtnSetOrPlayback(R.id.btn_playback, 1);
                }
                break;
            case R.id.btn_takepicture:
                /*if (isRecording()) {
                	if(!mbTakePicture){
	                	mbTakePicture = true;
	                    mHandler.removeMessages(MODE_START_RECORD);
	                    mHandler.removeMessages(MODE_LOOP_STOP);
	                    stopRecord(0);
	                    mHandler.sendEmptyMessageDelayed(MSG_EXIT_CAPUTER, 1500);
                	}
                } else {
                	if(!mbTakePicture){
                		dealWithBtnSetOrPlayback(R.id.btn_takepicture, 1);
                	}
                }*/
            	mHandler.removeMessages(MSG_IMAGE_CAPTURE);
                mHandler.sendEmptyMessageDelayed(MSG_IMAGE_CAPTURE, 500);
                break;
            case R.id.preswitch:
            	if(mPreSwitchBool){
            		usbCameraSurfaceView.setPreSwitch(false);
            		//usbCameraSurfaceView.BackCameraStopPrev();
            		mPreSwitchBool = false;
            	}else{
            		usbCameraSurfaceView.setPreSwitch(true);
            		//usbCameraSurfaceView.usbCameraPreView();
            		mPreSwitchBool = true;
            		//mHandler.sendEmptyMessageDelayed(MSG_PREVIEW_SWITCH, 1000 * 15);
            	}
                break;
            default:
                break;
            }
            mHandler.sendEmptyMessageDelayed(MSG_HIDE_UNNORMA_USE_BTN, 1000 * mHideAllBtnDelay);
        }
    };

    /*该方法用来更新视图的位置，其实就是改�?LayoutParams.x,LayoutParams.y)的�?*/
    private void updateFloatView(int mParamsX, int mParamsY, int nWidth, int nHeight) {
        /*cameraLayoutParams.x = mParamsX;
        cameraLayoutParams.y = mParamsY;
        cameraLayoutParams.width = nWidth;
        cameraLayoutParams.height = nHeight;
        mWindowManager.updateViewLayout(mFloatView, cameraLayoutParams);

        if (cameraSurView != null) {
            mWindowManager.updateViewLayout(cameraSurView, cameraLayoutParams);
        }
        
        if (usbCameraSurfaceView != null) {
            if (nWidth == 1 || nWidth == 0) {
                usbCameraLayoutParams.x = mParamsX;
                usbCameraLayoutParams.y = mParamsY;
                usbCameraLayoutParams.width = nWidth;
                usbCameraLayoutParams.height = nHeight;
            } else {
                usbCameraLayoutParams.x = 45;
                usbCameraLayoutParams.y = 45;
                usbCameraLayoutParams.width = 200;
                usbCameraLayoutParams.height = 200;
            }
            mWindowManager.updateViewLayout(usbCameraSurfaceView, usbCameraLayoutParams);
        }*/
    	if(nWidth == 0 || nWidth == 1){
        	mUsbCameraLayout.setVisibility(View.INVISIBLE);
        }else{
        	mUsbCameraLayout.setVisibility(View.VISIBLE);
        }
        /////////////////////////////////////////////////////
        usbCameraLayoutParams.x = mParamsX;
        usbCameraLayoutParams.y = mParamsY;
        usbCameraLayoutParams.width = nWidth;
        usbCameraLayoutParams.height = nHeight;
        mWindowManager.updateViewLayout(mFloatView, usbCameraLayoutParams);
        if(usbCameraSurfaceView!=null){
           mWindowManager.updateViewLayout(mUsbCameraLayout/*usbCameraSurfaceView*/, usbCameraLayoutParams);
        }
        if (mCameraLayout != null) {//cameraSurView
            if (nWidth == 1 || nWidth == 0|| nWidth == 100) {
            	cameraLayoutParams.x = 45;
            	cameraLayoutParams.y = 45;
            	cameraLayoutParams.width = nWidth;
            	cameraLayoutParams.height = nHeight;
            } else {
            	cameraLayoutParams.x = 45;
            	cameraLayoutParams.y = 45;
            	cameraLayoutParams.width = 200;
            	cameraLayoutParams.height = 200;
            }
            mWindowManager.updateViewLayout(mCameraLayout, cameraLayoutParams);//cameraSurView
        }
        //penglj add 20151119 start
        if(nWidth == 0 || nWidth == 1){
        	LayoutParams lp = usbCameraSurfaceView.getLayoutParams();
        	lp.height = 0; lp.width = 0;
        	usbCameraSurfaceView.setLayoutParams(lp);
        	mFloatView.setVisibility(View.INVISIBLE);
        	/*takePictureBtn.setVisibility(View.INVISIBLE);
        	startRecordBtn.setVisibility(View.INVISIBLE);
        	stopRecordBtn.setVisibility(View.INVISIBLE);
        	settingsBtn.setVisibility(View.INVISIBLE);
        	playListBtn.setVisibility(View.INVISIBLE);
        	presWitchBtn.setVisibility(View.INVISIBLE);*/
        }else{
        	LayoutParams lp = usbCameraSurfaceView.getLayoutParams();
        	lp.height = LayoutParams.MATCH_PARENT; lp.width = LayoutParams.MATCH_PARENT;
        	usbCameraSurfaceView.setLayoutParams(lp);
        	mFloatView.setVisibility(View.VISIBLE);
        	/*takePictureBtn.setVisibility(View.VISIBLE);
        	startRecordBtn.setVisibility(View.VISIBLE);
        	stopRecordBtn.setVisibility(View.VISIBLE);
        	settingsBtn.setVisibility(View.VISIBLE);
        	playListBtn.setVisibility(View.VISIBLE);
        	presWitchBtn.setVisibility(View.VISIBLE);*/
        }//penglj add 20151119 end
    }

    private void saveme() {
        SharedPreferences uiState = getSharedPreferences("xgx", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = uiState.edit();
        editor.putBoolean("isHitAutosave", SettingBean.isHitAutosave);
        editor.putBoolean("isClickSave", SettingBean.isClickSave);
        editor.putBoolean("isHasvoice", SettingBean.isHasvoice);
        editor.putBoolean("isRecordSound", SettingBean.isRecordSound);
        editor.putBoolean("mRecordMode", SettingBean.mRecordMode);
        editor.putBoolean("mBGRecord", SettingBean.mBGRecord);
        editor.putBoolean("mBGAnim", SettingBean.mBGAnim);
        editor.putInt("cirVideoLong", SettingBean.cirVideoLong);
        editor.putInt("mVideoQuality", SettingBean.mVideoQuality);
        editor.putInt("mHitDelicacy", SettingBean.mHitDelicacy);
        editor.putInt("powersaving_time", SettingBean.powersaving_time);
        editor.putInt("mPathItem", SettingBean.mPathItem);
        editor.putBoolean("isBackCamera", SettingBean.isBackCamera);
        editor.putBoolean("mEnableAutoRun", SettingBean.mEnableAutoRun);
        editor.putBoolean("mOpenBackCamera", SettingBean.mOpenBackCamera);
        //liujie add 0926
        editor.putInt("mHitDelicacyType", SettingBean.mHitDelicacyType);
        editor.putBoolean("bPlayBackShowRecordTime", SettingBean.bPlayBackShowRecordTime);
        editor.putInt("mExposureValue", SettingBean.mExposureValue);
        editor.putInt("mAntitheftDelicacyType", SettingBean.mAntitheftDelicacyType);
        //liujie add end
        
        //liujie add 1029
        editor.putBoolean("bShowBackWindow", SettingBean.bShowBackWindow);
        //liujie add end
        editor.putBoolean("bBeforeSound", SettingBean.bBeforeSound);
        editor.commit();
    }
    
    private void savemebWin() {
    	SharedPreferences.Editor editor = getSharedPreferences("xgx", Context.MODE_PRIVATE).edit();
        editor.putBoolean("bShowBackWindow", SettingBean.bShowBackWindow);
        editor.commit();
    }

    public void RestartServiceForError() {
        mRestartService = true;
        stopSelf();
    }

    // 改变窗口广播
    public class ChangeWindowBroadcast extends BroadcastReceiver {
        public String TAG = "ChangeWindowBroadcast";

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (ACTION_CHANGE_SURFACE.equals(action)) {
                int mParamsX = intent.getIntExtra("mProgramX", 0);
                int mParamsY = intent.getIntExtra("mProgramY", 0);
                int mParamsWidth = intent.getIntExtra("nScreenWidth", 0);
                int mParamsHeight = intent.getIntExtra("nScreenHeight", 0);

                mClientLuRuiTong = intent.getBooleanExtra("bLuRuiTong", false);
                if (mClientLuRuiTong) {
                    if (!SettingBean.misLuRuiTongClient) {
                        SettingBean.misLuRuiTongClient = true;
                        saveClientType();
                    }
                }

                if (DVRBackService.mIsPowerOnAutorun) {
                    if (isRecording()) {
                        updateStatus(STATUS_LOOP_RECORDING);
                    }
                }
                DVRBackService.mIsPowerOnAutorun = intent.getBooleanExtra("PowerOnAutorun", false);

                // 设置视图的宽、高
                if (mParamsWidth == 1024) {
                    mParamsHeight = 560;
                } else if (mParamsWidth == 800) {
                    mParamsHeight = 480;
                }
                updateFloatView(mParamsX, mParamsY, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);//updateFloatView(mParamsX, mParamsY, mParamsWidth, mParamsHeight);
                // mNotificationManager.cancel(R.string.app_name);
            } else if (ACTION_STOP_RECORD.equals(action)) {
                stopRecord(0);
                updateStatus(Config.STATUS_IDLE);
            } else if (ACTION_START_RECORD.equals(action)) {
                if (!(isRecording() || mbReadyStartRecord)) {
                    ServiceStartRecord();
                }
            } else if (ACTION_HIDE_ALL_BTN.equals(action)) {
                mState = CURRENT_STATE_HIDE;
                updateUI();
            } else if (ACTION_NORTICE_BACK_RECORD.equals(action)) {
                int nOpenBackRecord = intent.getIntExtra("backrecord", 0);
                
                if (nOpenBackRecord != 1) {
                    mNotificationManager.cancel(R.string.app_name);
                    mOnBackground = false;
                    isNoCardspace = false;
                    setMainViewBtnVisibility(View.VISIBLE);
                    SurCameraVisibility(View.VISIBLE);
                    mHandler.sendEmptyMessageDelayed(MSG_HIDE_UNNORMA_USE_BTN, 1000 * mHideAllBtnDelay);
                }
            } else if (ACTION_NORTICE_CLOSE_SETTING.equals(action)) {
                mHandler.sendEmptyMessage(MSG_CLOSE_SETTING_UI);
            } else if (ACTION_CANNOT_EXIT_FOR_RECORDING.equals(action)) {
            	DVRBack(); //mHandler.sendEmptyMessage(MSG_EXIT_STOP_RECORD_FIRST);//
            } else if (ACTION_STOP_SERVICE.equals(action)) {
                if (mState == 1) {
                    ServiceStopRecord(false);
                    mOnBackground = false;
                }
                stopSelf();
            } else if (ACTION_RESTART_SERVICE.equals(action)) {
                mRestartService = true;
                stopSelf();
            } else if (ACTION_AVIN_OUT_BEFORE_NOSTARTDVR.equals(action)) {
                bAvinPutIn = false;
                Intent noticeRestartServiceIntent = new Intent("com.dvr.android.dvr.RECEIVER");
                noticeRestartServiceIntent.putExtra("msgtype", Config.MSG_ACTIVITY_BROADCASE_STOP_ACTIVITY);
                sendBroadcast(noticeRestartServiceIntent);
            } else if (ACTION_AVIN_PUTIN_BEFORE_STARTDVR.equals(action)) {
                // 开始倒车
                bAvinPutIn = true;

                if (mbOpenSettingUi) {
                    mHandler.sendEmptyMessage(MSG_CLOSE_SETTING_UI);
                }
                updateFloatView(0, 0, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                setMainViewBtnVisibility(View.INVISIBLE);
                isCameraViewSmall = false;
                checkCameraWindow();
            } else if (ACTION_AVIN_OUT_BEFORE_STARTDVR.equals(action)) {
                bAvinPutIn = false;
                
                if (mOnBackground) {
                	imageAvin.setVisibility(View.GONE);
                	Config.AvinHomeBack = true;
                    // 拨出恢复后台录
                    updateFloatView(0, 0, 1, 1);
                } else if (m_bShowFullScreen) {
                	isCameraViewSmall = true;
                    checkCameraWindow();
                    // 是否在回放界面
                    m_bShowFullScreen = false;
                    updateFloatView(0, 0, 1, 1);
                } else {
                	isCameraViewSmall = true;
                    checkCameraWindow();
                    
                    setMainViewBtnVisibility(View.VISIBLE);
                    SurCameraVisibility(View.VISIBLE);
                }
            } else if (ACTION_CANNOT_EXIT_SAVEING.equals(action)) {
                mHandler.sendEmptyMessage(MSG_EXIT_STOP_SAVEING);
            } else if (ACTION_MICROPLAY_NOACTION.equals(action)) {
            	mHandler.sendEmptyMessage(MICROPLAY_NOACTION);
            }
            //usbCameraSurfaceView.setBack(bAvinPutIn);
        }
    }

    // 监听SD卡移除广播
    public class DetectSDCardMount extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO SD卡监听广播
            if ((Intent.ACTION_MEDIA_UNMOUNTED).equals(intent.getAction())) {
            	mHandler.sendEmptyMessageDelayed(MSG_SDCARD_UNMOUNTED, 1500);
            }
        }
    }

    /**
     * 开始预览
     * */
    private void startPreview() {
        try {
            mCamera.setPreviewDisplay(cameraSurView.getHolder());
            mCamera.setParameters(getParameters());
            mCamera.startPreview();
            mPreviewing = true;
        } catch (Exception e) {
            mPreviewing = false;
        }
    }

    private void showBackgroudNotification(String paramString) {
        // 设置通知的事件消息
        Intent localIntent = new Intent(this, DVRActivity.class);
        localIntent.setAction("android.intent.action.MAIN");
        localIntent.addCategory("android.intent.category.LAUNCHER");
        PendingIntent localPendingIntent = PendingIntent.getActivity(this, 0, localIntent, 0);
        Notification localNotification = new Notification(R.drawable.not_icon, paramString, System.currentTimeMillis());
        localNotification.flags |= Notification.FLAG_ONGOING_EVENT;
        localNotification.setLatestEventInfo(this, getText(R.string.app_name), paramString, localPendingIntent);
        mNotificationManager.notify(R.string.app_name, localNotification);
    }

    @Override
    public void onCreate() {
    	Log.i("PLJ", "DVRBackService---->onCreate");
        HitListener.getInstance(this).setListener(this);

        mContentResolver = getContentResolver();
        mNotificationManager = ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE));

        GpsProvider.getGpsProvider(this).setGPSListener(this);

        setupDirectorys();
        mWindowManager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

    @Override
    public void onDestroy() { Log.i("PLJ", "DVRBackService---->onDestroy()000:");
        if (isNoCardspace) {
            Toast.makeText(this, getString(R.string.sdcard_no_space), Toast.LENGTH_LONG).show();
            isNoCardspace = false;
        }
        mNotificationManager.cancel(R.string.app_name);
        if (this.mWindowManager != null) {
        	if(mFloatView!=null){
               this.mWindowManager.removeView(this.mFloatView);
        	}
            if (mCameraLayout != null) {//cameraSurView
                this.mWindowManager.removeView(this.mCameraLayout);//cameraSurView
            }
            if (usbCameraSurfaceView != null) {
                usbCameraSurfaceView.surfaceDestroyed(usbCameraSurfaceView.getHolder());
                this.mWindowManager.removeView(this.usbCameraSurfaceView);
            }
        }
        mHandler.removeMessages(MODE_START_RECORD);
        mHandler.removeMessages(MODE_LOOP_STOP);
        mHandler.removeMessages(MSG_EXIT_CAPUTER);
        mHandler.removeMessages(MSG_GET_AVIN_STATUS);
        mHandler.removeMessages(MSG_START_RECORD);
        
        mOnBackground = false;
        //stopRecord();
        closeCamera();
        mMediaRecorder = null;
        GpsProvider.getGpsProvider(this).stopGPS();
        if (mBroadcastReceiver != null) {
            unregisterReceiver(mBroadcastReceiver);
        }
        if (mSDCardMountReceiver != null) {
            unregisterReceiver(mSDCardMountReceiver);
        }

        if (mReceiverHome != null) {
            unregisterReceiver(mReceiverHome);
            mReceiverHome = null;
        }
        
        if (mReceiverBattery != null) {
        	unregisterReceiver(mReceiverBattery);
        }
        
        //liujie add begin 
        if (mReceiverAccDorant != null) {
        	unregisterReceiver(mReceiverAccDorant);
        }
        //liujie add end
        
        if (mRestartService) {
            Intent noticeRestartServiceIntent = new Intent("com.dvr.android.dvr.RECEIVER");
            noticeRestartServiceIntent.putExtra("msgtype", Config.MSG_ACTIVITY_BROADCASE_RESTART_SERVICE);
            sendBroadcast(noticeRestartServiceIntent);

            mRestartService = false;
        }
        if(broadVioce!=null){// 1013
        	unbindService(mConnection);
        }
        Config.gbRecordStatus = false;
        //try {  
        	Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE,  CURRENT_STATE_UNRECODING);  
        	//} catch (Exception localExcepton) {}
        
        super.onDestroy();
    }

    @Override
    public boolean onUnbind(Intent intent) {
    	Log.i("PLJ", "DVRBackService---->onUnbind:");
        return super.onUnbind(intent);
    }

    @Override
    public void onStart(Intent intent, int startid) { Log.i("PLJ", "DVRBackService---->onStart:111:");
        boolean bActivityStart = false; isInitDvr = false; isInitDvrRec = false; bCameraSwitch = 0;
        // ---pz---add---
        if (intent != null) {
            this.nScreenWidth = intent.getIntExtra("nScreenWidth", this.nScreenWidth);
            this.nScreenHight = intent.getIntExtra("nScreenHeight", this.nScreenHight);
            DVRBackService.mIsPowerOnAutorun = intent.getBooleanExtra("PowerOnAutorun", false);
            bActivityStart = intent.getBooleanExtra("ActivityStart", false);
            mClientLuRuiTong = SettingBean.misLuRuiTongClient;
            showMicStatus();
        }

        mBroadcastReceiver = new ChangeWindowBroadcast();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_CHANGE_SURFACE);
        filter.addAction(ACTION_STOP_RECORD);
        filter.addAction(ACTION_HIDE_ALL_BTN);
        filter.addAction(ACTION_NORTICE_BACK_RECORD);
        filter.addAction(ACTION_NORTICE_CLOSE_SETTING);
        filter.addAction(ACTION_CANNOT_EXIT_FOR_RECORDING);
        filter.addAction(ACTION_STOP_SERVICE);
        filter.addAction(ACTION_START_RECORD);
        filter.addAction(ACTION_AVIN_OUT_BEFORE_NOSTARTDVR);
        filter.addAction(ACTION_AVIN_PUTIN_BEFORE_STARTDVR);
        filter.addAction(ACTION_AVIN_OUT_BEFORE_STARTDVR);
        filter.addAction(ACTION_CANNOT_EXIT_SAVEING);
        filter.addAction(ACTION_MICROPLAY_NOACTION);
        this.registerReceiver(mBroadcastReceiver, filter);

        // 监听SD卡插拨
        mSDCardMountReceiver = new DetectSDCardMount();
        IntentFilter filterSDCardMount = new IntentFilter();
        filterSDCardMount.addAction(Intent.ACTION_MEDIA_MOUNTED);
        filterSDCardMount.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        filterSDCardMount.addDataScheme("file");
        this.registerReceiver(mSDCardMountReceiver, filterSDCardMount);

        registerHomeReveiver();
        //registerBatteryReveiver();
        SDcardManager.DeleteScratchFile();
        mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
        mUsbCameraCallPrev.setOnMethodCallback(this);
        DvrStatusIntent = new Intent(ACTION_HIDE_DVR_REC_STATUS);

        handler = new Handler();
        checkAvin = new CheckAvin();
        mCamera = getCameraInstance();
		bindService(new Intent(BroadVoice.class.getName()), mConnection, Context.BIND_AUTO_CREATE);
        createView();

        if (mCamera == null) { 
            if (bActivityStart || mIsPowerOnAutorun) {
            	if(SettingBean.bShowBackWindow){//liujie add 1004
            		mHandler.sendEmptyMessage(MSG_OPEN_CAMERA_FAIL_DLG_SHOW);
            	}
            } else {
                // 服务崩溃会自动重启，但是摄像头就无法恢复，不如把复位关掉
                //this.stopSelf();
            }
        }

        sharePreferences = this.getSharedPreferences("xgx", Context.MODE_PRIVATE);
        bAvinPutIn = sharePreferences.getBoolean("isAvinInput", false);
        Log.i("PLJ", "DVRBackService---->onStart:222:"+(usbCameraSurfaceView != null));
        if (usbCameraSurfaceView != null) {
        	usbCameraSurfaceView.usbCameraInit(0);
            if (bAvinPutIn) {
                usbCameraSurfaceView.setBack(bAvinPutIn);
                setMainViewBtnVisibility(View.INVISIBLE);
                isCameraViewSmall = false;
                checkCameraWindow();
            }
        }

        /*if (mIsPowerOnAutorun) {
            if (SettingBean.mEnableAutoRun) {
                handler.postDelayed(new Runnable() {
                    public void run() {
                        ServiceStartRecord();
                    }
                }, AVIN_TIME_OFFSET);//liujie add 1009
            } else {
                this.stopSelf();
                return;
            }
        }*/
		// liujie add begin 0828
		else {
			//try {
				Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING);
			//} catch (Exception localException) {}
		}
        
        GpsProvider.getGpsProvider(this).startGps();
        registerAccDormantDVRReveiver();
    }

    private String ACTION_SYSTEM_ACCDORMANT = "android.intent.action.SYSTEM_ACCDORMANT";
    private String ACTION_SYSTEM_ACCSHUTDOWN = "android.intent.action.SYSTEM_ACCSHUTDOWN";
    BroadcastReceiver mReceiverAccDorant;
    private void registerAccDormantDVRReveiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_SYSTEM_ACCDORMANT);
        filter.addAction(ACTION_SYSTEM_ACCSHUTDOWN);
        mReceiverAccDorant = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	String str = intent.getAction();
            	if(str.equals(ACTION_SYSTEM_ACCDORMANT)){
            		if(isRecording()){
            			mHandler.removeMessages(MODE_START_RECORD);
                        mHandler.removeMessages(MODE_LOOP_STOP);
                        stopRecord(0);
                        
                        sleep=0;
                        mHandler.sendEmptyMessageDelayed(MSG_ACCDISCONNECT_SLEEP, 1000 * 3);
            		}else{
            			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING);
    	            	if (usbCameraSurfaceView != null) {
    	                    usbCameraSurfaceView.surfaceDestroyed(usbCameraSurfaceView.getHolder());
    	                }
    	        		DVRBackService.this.stopSelf();
    	        		if(DVRActivity.instance!=null){
                    		DVRActivity.instance.finish();	
                    	}
    	    		    android.os.Process.killProcess(android.os.Process.myPid());	
            		}
            	}else if(str.equals(ACTION_SYSTEM_ACCSHUTDOWN)){
            		if(isRecording()){
            			mHandler.removeMessages(MODE_START_RECORD);
                        mHandler.removeMessages(MODE_LOOP_STOP);
                        stopRecord(0);
                        
                        shutdown=0;
                        mHandler.sendEmptyMessageDelayed(MSG_ACCDISCONNECT_SHUTDOWN, 1000 * 3);
            		}
            	}
            }
        };
        registerReceiver(mReceiverAccDorant, filter);
    }
    
    ServiceConnection mConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName className, IBinder service) {
			broadVioce = BroadVoice.Stub.asInterface(service);
		}

		@Override
		public void onServiceDisconnected(ComponentName className) {
			broadVioce = null;
		}
	};
    
    /**
     * 注册Home键的监听
     * */
    protected void registerHomeReveiver() {
        IntentFilter filterHome = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        filterHome.addAction("android.intent.action.GPS_ClICK");
        mReceiverHome = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
            	String str = intent.getAction();
                if (str.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                    String reason = intent.getStringExtra("reason");
                    if (reason != null && reason.equals("lock")) {
                        if (!(mState == 1 || Config.gbRecordStatus || mbReadyStartRecord)) {
                            DVRBackService.this.stopSelf();
                        }
                    } else if (reason != null && (reason.equals("homekey") || reason.equals("recentapps"))) {
	                    if(Config.AvinHomeBack){
	                        DVRBack();	
	                    }
                    }
                }else if(str.equals("android.intent.action.GPS_ClICK")){
                	DVRBack();
                	if(DVRActivity.instance!=null){
                		DVRActivity.instance.finish();	
                	}
                }
            }
        };
        registerReceiver(mReceiverHome, filterHome);
    }
    
    private void DVRBack(){
    	mOnBackground = true; Toast.makeText(DVRBackService.this, ""+(mState == 1) +"   "+ Config.gbRecordStatus +"   "+ mbReadyStartRecord +"   "+ SettingBean.mBGRecord, 1).show();
        // 测试发现，有�?��录制的过程中按HOME键退出，再进去停止录像了，很奇怪2015.01.24
        if (mState == 1 || Config.gbRecordStatus || mbReadyStartRecord) {
            if (SettingBean.mBGRecord) {
                //SurCameraVisibility(View.INVISIBLE);
                updateFloatView(0, 0, 1, 1);
                //showBackgroudNotification(getString(R.string.background_recording));
                //setMainViewBtnVisibility(View.INVISIBLE);
            } else {
                ServiceStopRecord(false);
                if (!mClientLuRuiTong) {
                    DVRBackService.this.stopSelf();
                }
            }
        } else {
            if (!mClientLuRuiTong) {
            	//setMainViewBtnVisibility(View.INVISIBLE);
            	updateFloatView(0, 0, 1, 1);//penglj
                //DVRBackService.this.stopSelf();
            }
        }
    }
    
    protected void registerBatteryReveiver() {
        IntentFilter filterBattery = new IntentFilter("android.intent.action.BATTERY_CHANGED");
        mReceiverBattery = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);//当前剩余电量
            	
            	int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                switch (status) {
                case BatteryManager.BATTERY_STATUS_CHARGING://正在充电
                	 
                	 libucamera.native_ucameracmd(libucamera.JCMD_CHECKDEVICE, "", 0, 0, 0);
                    break;
                case BatteryManager.BATTERY_STATUS_FULL://充满
                    break;
                case BatteryManager.BATTERY_STATUS_NOT_CHARGING://没有充电
                	 
                	 libucamera.native_ucameracmd(libucamera.JCMD_CHECKDEVICE, "", 0, 0, 0);
                    break;
                case BatteryManager.BATTERY_STATUS_UNKNOWN://未知状态
                    break;
                default:
                    break;
                }
                
            }
        };
        registerReceiver(mReceiverBattery, filterBattery);
    }

    public void OnHit() {
        // hanJ add
        if (SettingBean.IsHitAutosave && mState == 1) {
            mHandler.sendEmptyMessage(SAVE_ON_HIT);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
    	bUsbCameraInit1 = intent.getBooleanExtra("usbCameraInit1", false);//liujie add 0922
        return new DVRBinder();
    }

    //TODO 设置相关
    private void initSetActivity() {
        if (mbOpenSettingUi) {
            setControlBarVisibility(false);
            setPreviewLayout.setVisibility(View.VISIBLE);

            // 设置五个选项
            settingRecordModeBtn = ((MyImageButton) mFloatView.findViewById(R.id.recordmode_button_id));
            recordModeLayout = (RelativeLayout) mFloatView.findViewById(R.id.record_mode_layout);
            settingRecordQualityBtn = ((MyImageButton) mFloatView.findViewById(R.id.recordquality_button_id));
            recordQualityLayout = (RelativeLayout) mFloatView.findViewById(R.id.record_quality_layout);
            settingHitDelicacyBtn = ((MyImageButton) mFloatView.findViewById(R.id.hitdelicacy_button_id));
            hitDelicacyLayout = (RelativeLayout) mFloatView.findViewById(R.id.hit_delicacy_layout);
            settingRecordBackBtn = ((MyImageButton) mFloatView.findViewById(R.id.recordback_button_id));
            otherSettingLayout = (RelativeLayout) mFloatView.findViewById(R.id.other_setting_layout);
            settingVideoSetBtn = ((MyImageButton) mFloatView.findViewById(R.id.videoset_button_id));
            startingUpBackRecordLayout = (RelativeLayout) mFloatView.findViewById(R.id.starting_up_back_record_layout);
            
            setting_draw_type_content();
            initButtonAction();

            setPreviewLayout.getGlobalVisibleRect(settingUIRect);
        } else {
        	CameraSetExposureCompensation(SettingBean.mExposureValue);
            setControlBarVisibility(true);
            setPreviewLayout.setVisibility(View.INVISIBLE);
            mHandler.sendEmptyMessageDelayed(MSG_HIDE_UNNORMA_USE_BTN, 1000 * mHideAllBtnDelay);
        }
    }

    private void showVideoAutoRunButton() {
        if (SettingBean.mEnableAutoRun) {
            btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_power_on_auto_run.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showvideo_setUI(boolean bShow) {
        btn_power_on_auto_run = ((MyImageButton) mFloatView.findViewById(R.id.btn_power_on_auto_run_id));
        btn_record_back = ((MyImageButton) mFloatView.findViewById(R.id.btnthree_bgrecord));

    	//liujie add 0926
    	btn_antitheftdelicacy_high = ((MyImageButton) mFloatView.findViewById(R.id.anti_theft_delicacy_high_button_id));
    	btn_antitheftdelicacy_middle = ((MyImageButton) mFloatView.findViewById(R.id.anti_theft_delicacy_middle_button_id));
    	btn_antitheftdelicacy_lower = ((MyImageButton) mFloatView.findViewById(R.id.anti_theft_delicacy_lower_button_id));
    	btn_antitheftdelicacy_off = ((MyImageButton) mFloatView.findViewById(R.id.anti_theft_delicacy_off_button_id));
    	//liujie add 0926
        
        if (bShow) {
            showVideoAutoRunButton();
            showBackRecordButton();

            startingUpBackRecordLayout.setVisibility(View.VISIBLE);

            //liujie add 0926
            btn_antitheftdelicacy_high.setText(getResources().getString(R.string.msetting_hit_high));
            btn_antitheftdelicacy_high.setColor(Color.WHITE);
            btn_antitheftdelicacy_high.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_high_y));
            btn_antitheftdelicacy_high.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_high_x));
            btn_antitheftdelicacy_middle.setText(getResources().getString(R.string.msetting_hit_middle));
            btn_antitheftdelicacy_middle.setColor(Color.WHITE);
            btn_antitheftdelicacy_middle.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_middle_y));
            btn_antitheftdelicacy_middle.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_middle_x)); 
            btn_antitheftdelicacy_lower.setText(getResources().getString(R.string.msetting_hit_lower));
            btn_antitheftdelicacy_lower.setColor(Color.WHITE);
            btn_antitheftdelicacy_lower.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_lower_y));
        	btn_antitheftdelicacy_lower.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_lower_x)); 
        	btn_antitheftdelicacy_off.setText(getResources().getString(R.string.msetting_closed));
        	btn_antitheftdelicacy_off.setColor(Color.WHITE);
        	btn_antitheftdelicacy_off.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_lower_y));
        	btn_antitheftdelicacy_off.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_lower_x));         	
        	
        	showAntitheftDelicacyButtonS();
        	btn_antitheftdelicacy_high.setVisibility(View.VISIBLE);
        	btn_antitheftdelicacy_middle.setVisibility(View.VISIBLE);
        	btn_antitheftdelicacy_lower.setVisibility(View.VISIBLE);
        	btn_antitheftdelicacy_off.setVisibility(View.VISIBLE);
            //liujie add 0926
            
        } else {
            //liujie add 0926
            btn_antitheftdelicacy_high.setVisibility(View.INVISIBLE);
            btn_antitheftdelicacy_middle.setVisibility(View.INVISIBLE);
            btn_antitheftdelicacy_lower.setVisibility(View.INVISIBLE);
            btn_antitheftdelicacy_off.setVisibility(View.INVISIBLE);
            //liujie add 0926
            startingUpBackRecordLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showBackRecordButton() {
        if (SettingBean.mBGRecord) {
            btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_record_back.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showOpenBackCameraButton() {
        if (SettingBean.mOpenBackCamera) {
            //btn_open_back_camera.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            //btn_open_back_camera.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }
    
    private void showExposureValue()
    {
    	mExposureValue = getResources().getStringArray(R.array.setting_ev_value);
		mtextview_exposure_value_text.setText(mExposureValue[SettingBean.mExposureValue]);
		//CameraSetExposureCompensation(SettingBean.mExposureValue);
    }

    private void CameraSetExposureCompensation(int nCompensation)
	{
	    if (mCamera == null || !SettingBean.bShowBackWindow/*Config.bBackCameraExist*/ || !bIsSettingChanged ) {//liujie add
	        return;
	    }
		Parameters params = null;
		Log.e(TAG,  "***CameraSetExposureCompensation ***");
		params = mCamera.getParameters();
		switch (nCompensation) {
		case 0:
			params.setExposureCompensation(-3);
			break;
		case 1:
			params.setExposureCompensation(-2);
			break;
		case 2:
			params.setExposureCompensation(-1);
			break;
		case 3:
			params.setExposureCompensation(0);
			break;
		case 4:
			params.setExposureCompensation(1);
			break;
		case 5:
			params.setExposureCompensation(2);
			break;
		case 6:
			params.setExposureCompensation(3);
			break;
		default:
			params.setExposureCompensation(0);
			break;
		}
		mCamera.setParameters(params);
		bIsSettingChanged = false;//liujie add 1013
	}
    
    private void setPlayBackShowTime(){
    	if(mCamera == null || !SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 
    	     return; 
    	 }
    	
		Parameters params = null;
		params = mCamera.getParameters();
    	try {
			String enableShowTime = "enable-osd";
			if(SettingBean.bPlayBackShowRecordTime){
				params.set(enableShowTime, 1);
			}else{
				params.set(enableShowTime, 0);
			}
			mCamera.setParameters(params);
		} catch (Exception e) {
			//SDcardManager.WriteErrorLogToFile("***enableShowTime fail ***");
			e.printStackTrace();
		}
		params = null;
	}
    
    //liujie add 0926
    private void showAntitheftDelicacyButtonS()
    {
    	if(SettingBean.mAntitheftDelicacyType == 30)//0
    	{
    		btn_antitheftdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed));
    		btn_antitheftdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal)); 
    		btn_antitheftdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_off.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    	}
    	else if(SettingBean.mAntitheftDelicacyType == 20)//1
    	{
    		btn_antitheftdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed)); 
    		btn_antitheftdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_off.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    	}
    	else if(SettingBean.mAntitheftDelicacyType == 10)//2
    	{
    		btn_antitheftdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal)); 
    		btn_antitheftdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed));
    		btn_antitheftdelicacy_off.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		
    	}else if(SettingBean.mAntitheftDelicacyType == -1)//-1
    	{
    		btn_antitheftdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal)); 
    		btn_antitheftdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_antitheftdelicacy_off.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed));
    	}
    }    
    //liujie add 0926
    
    private void showrecord_backUI(boolean bShow) {
        resetDefaulBtn = ((TextView) mFloatView.findViewById(R.id.btnfour_defealt));
        BtnSDFormat = ((TextView) mFloatView.findViewById(R.id.sd_format));
        //btn_open_back_camera = ((MyImageButton) mFloatView.findViewById(R.id.btn_open_back_camera_id));
        btn_exposure_left = ((ImageView)mFloatView.findViewById(R.id.set_exposure_left_button_id));
    	btn_exposure_right = ((ImageView)mFloatView.findViewById(R.id.set_exposure_right_button_id));
    	mtextview_exposure_text = ((TextView)mFloatView.findViewById(R.id.set_exposure_text_id));
    	mtextview_exposure_value_text = ((TextView)mFloatView.findViewById(R.id.set_exposure_value_text_id));
        if (bShow) {
            showOpenBackCameraButton();
            otherSettingLayout.setVisibility(View.VISIBLE);
            showExposureValue();
            /*btn_exposure_left.setVisibility(View.VISIBLE);
    		btn_exposure_right.setVisibility(View.VISIBLE);
    		mtextview_exposure_text.setVisibility(View.VISIBLE);
    		mtextview_exposure_value_text.setVisibility(View.VISIBLE);*/
        } else {
            otherSettingLayout.setVisibility(View.INVISIBLE);
            /*btn_exposure_left.setVisibility(View.INVISIBLE);
    		btn_exposure_right.setVisibility(View.INVISIBLE);
    		mtextview_exposure_text.setVisibility(View.INVISIBLE);
    		mtextview_exposure_value_text.setVisibility(View.INVISIBLE);*/
        }
    }

    private void showhitdelicacyButton() {
        if (SettingBean.isHitAutosave) {
            btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_hitdelicacy_auto_saving.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showPlayBackShowTimeButton(){
    	if(SettingBean.bPlayBackShowRecordTime){
    		btn_playback_show_time.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}else{
    		btn_playback_show_time.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }
    
    //liujie add 1029
    private void showBackWindowButton(){
    	if(SettingBean.bShowBackWindow){
    		btn_show_back_window.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
    	}else{
    		btn_show_back_window.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
    	}
    }   
    //liujie add end
    
    private void showClickSaveButton() {
        if (SettingBean.bBeforeSound/*SettingBean.isRecordSound*/) {
            btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_open));
        } else {
            btn_click_screen.setImageDrawable(getResources().getDrawable(R.drawable.btn_control_close));
        }
    }

    private void showPowerSaveString(boolean bInit) {
        int[] values = getResources().getIntArray(R.array.setting_power_saving_value);
        if (bInit) {
            for (int i = 0; i < values.length; i++) {
                if (SettingBean.powersaving_time == values[i]) {
                    SettingBean.powerSaveIndex = i;
                    break;
                }
            }
        } else {
            SettingBean.powersaving_time = values[SettingBean.powerSaveIndex];
            PowerUitl.getPowerUitl().setValue(SettingBean.powersaving_time);
        }
        mtextview_power_saving_value.setText(mPowerSaveString[SettingBean.powerSaveIndex]);
    }

    private void showhitdelicacyUI(boolean bShow, boolean bInit) {
        btn_hitdelicacy_auto_saving = ((MyImageButton) mFloatView.findViewById(R.id.hit_auto_saving_button_id));
        btn_click_screen = ((MyImageButton) mFloatView.findViewById(R.id.click_screen_saving_button_id));
        //seekbar_delicacy = (SeekBar) mFloatView.findViewById(R.id.seekbar_delicacy);
        btn_hitdelicacy_high = ((MyImageButton) mFloatView.findViewById(R.id.hit_delicacy_high_button_id));
    	btn_hitdelicacy_middle = ((MyImageButton) mFloatView.findViewById(R.id.hit_delicacy_middle_button_id));
    	btn_hitdelicacy_lower = ((MyImageButton) mFloatView.findViewById(R.id.hit_delicacy_lower_button_id));
        if (bShow) {
            // init
            showhitdelicacyButton();
            showClickSaveButton();
            btn_hitdelicacy_high.setText(getResources().getString(R.string.msetting_hit_high));
        	btn_hitdelicacy_high.setColor(Color.WHITE);
            btn_hitdelicacy_high.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_high_y));
        	btn_hitdelicacy_high.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_high_x));
        	btn_hitdelicacy_middle.setText(getResources().getString(R.string.msetting_hit_middle));
        	btn_hitdelicacy_middle.setColor(Color.WHITE);
        	btn_hitdelicacy_middle.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_middle_y));
        	btn_hitdelicacy_middle.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_middle_x)); 
        	btn_hitdelicacy_lower.setText(getResources().getString(R.string.msetting_hit_lower));
        	btn_hitdelicacy_lower.setColor(Color.WHITE);
        	btn_hitdelicacy_lower.setOffsetY(getResources().getDimension(R.dimen.set_hitdelicacy_lower_y));
        	btn_hitdelicacy_lower.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_lower_x)); 
        	showHitDelicacyButtonS();
        	
            hitDelicacyLayout.setVisibility(View.VISIBLE);
            btn_hitdelicacy_high.setVisibility(View.VISIBLE);
    		btn_hitdelicacy_middle.setVisibility(View.VISIBLE);
    		btn_hitdelicacy_lower.setVisibility(View.VISIBLE);
        } else {
            hitDelicacyLayout.setVisibility(View.INVISIBLE);
            btn_hitdelicacy_high.setVisibility(View.INVISIBLE);
    		btn_hitdelicacy_middle.setVisibility(View.INVISIBLE);
    		btn_hitdelicacy_lower.setVisibility(View.INVISIBLE);
        }
    }

    private void showRecordQualityButton()
    {
    	if(SettingBean.mVideoQuality == 1)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_pressed));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal));
    	}
    	else if(SettingBean.mVideoQuality == 2)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_pressed)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal));
    	}
    	else if(SettingBean.mVideoQuality == 3)
    	{
    		btn_recordQuality_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal));
    		btn_recordQuality_mid.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_normal)); 
    		btn_recordQuality_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_record_selected_pressed));
    	}
    }

    private void showHitDelicacyButtonS()
    {
    	if(SettingBean.mHitDelicacyType == 0)
    	{
    		btn_hitdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed));
    		btn_hitdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal)); 
    		btn_hitdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    	}
    	else if(SettingBean.mHitDelicacyType == 1)
    	{
    		btn_hitdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_hitdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed)); 
    		btn_hitdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    	}
    	else if(SettingBean.mHitDelicacyType == 2)
    	{
    		btn_hitdelicacy_lower.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal));
    		btn_hitdelicacy_middle.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_normal)); 
    		btn_hitdelicacy_high.setImageDrawable(getResources().getDrawable(R.drawable.btn_selected_pressed));
    	}
    }
    
    private void showRecordQualityUI(boolean bShow) {
        btn_recordQuality_high = ((MyImageButton) mFloatView.findViewById(R.id.recordquality_high_button_id));
        btn_recordQuality_mid = ((MyImageButton) mFloatView.findViewById(R.id.recordquality_middle_button_id));
        btn_recordQuality_lower = ((MyImageButton) mFloatView.findViewById(R.id.recordquality_lower_button_id));
        btn_playback_show_time = ((MyImageButton) mFloatView.findViewById(R.id.playback_show_time_button_id));
        btn_show_back_window = ((MyImageButton) mFloatView.findViewById(R.id.show_back_window_saving_button_id));//liujie add 1029
        if (bShow) {
            recordQualityLayout.setVisibility(View.VISIBLE);
            
            btn_recordQuality_high.setText(getResources().getString(R.string.msetting_gao));
    		btn_recordQuality_high.setColor(Color.WHITE);
    		btn_recordQuality_high.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_high_y));
    		btn_recordQuality_high.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_high_xx));//set_record_quality_high_x
    		btn_recordQuality_mid.setText(getResources().getString(R.string.msetting_zhong));
    		btn_recordQuality_mid.setColor(Color.WHITE);
    		btn_recordQuality_mid.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_mid_y));
    		btn_recordQuality_mid.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_high_xx));
    		btn_recordQuality_lower.setText(getResources().getString(R.string.msetting_di));
    		btn_recordQuality_lower.setColor(Color.WHITE);
    		btn_recordQuality_lower.setOffsetY(getResources().getDimension(R.dimen.set_record_quality_lower_y));
    		btn_recordQuality_lower.setOffsetX(getResources().getDimension(R.dimen.set_hitdelicacy_high_xx));
    		showRecordQualityButton();
    		showPlayBackShowTimeButton();
    		showBackWindowButton();	//liujie add 1029
        } else {
            recordQualityLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void showCycleVideoTime() {
        mCycleVideoTimes = getResources().getStringArray(R.array.setting_record_time_str);
        mtextview_videoCycle_value.setText(mCycleVideoTimes[SettingBean.cirVideoLong]);
    }

    /**
     * 设置-录制模式
     * */
    private void showRecordModeUI(boolean bShow) {
        mtextview_videoCycle_value = ((TextView) mFloatView.findViewById(R.id.video_time_value_text_id));
        btn_videoCycle_left = ((ImageView) mFloatView.findViewById(R.id.video_time_left_button_id));
        btn_videoCycle_right = ((ImageView) mFloatView.findViewById(R.id.video_time_right_button_id));

        mtextview_power_saving_value = ((TextView) mFloatView.findViewById(R.id.power_save_value_text_id));
        btn_power_save_left = ((ImageView) mFloatView.findViewById(R.id.power_save_left_button_id));
        btn_power_save_right = ((ImageView) mFloatView.findViewById(R.id.power_save_right_button_id));
        if (bShow) {
    		//init
            showPowerSaveString(true);
            showCycleVideoTime();
            recordModeLayout.setVisibility(View.VISIBLE);
        } else {
            recordModeLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void setting_draw_type_content() {
        if (nSettingType == SETTING_TYPE_RECORD_MODE) {
            showRecordModeUI(true);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_RECORD_QUALITY) {
            showRecordModeUI(false);
            showRecordQualityUI(true);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_HITDELICACY) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(true, true);
            showrecord_backUI(false);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_RECORD_BACK) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(true);
            showvideo_setUI(false);
        } else if (nSettingType == SETTING_TYPE_VIDEO_SET) {
            showRecordModeUI(false);
            showRecordQualityUI(false);
            showhitdelicacyUI(false, true);
            showrecord_backUI(false);
            showvideo_setUI(true);
        }
    }

    private void initButtonAction() {
        // 设置对话框五项事件监听
        settingRecordModeBtn.setOnTouchListener(onSettingTouchListener);
        settingRecordQualityBtn.setOnTouchListener(onSettingTouchListener);
        settingHitDelicacyBtn.setOnTouchListener(onSettingTouchListener);
        settingRecordBackBtn.setOnTouchListener(onSettingTouchListener);
        settingVideoSetBtn.setOnTouchListener(onSettingTouchListener);

        // 循环录制时长控制
        btn_videoCycle_left.setOnClickListener(setClickListener);
        btn_videoCycle_right.setOnClickListener(setClickListener);

        // RecordQuality
        btn_recordQuality_high.setOnClickListener(setClickListener);
        btn_recordQuality_mid.setOnClickListener(setClickListener);
        btn_recordQuality_lower.setOnClickListener(setClickListener);
        btn_playback_show_time.setOnClickListener(setClickListener);
        btn_show_back_window.setOnClickListener(setClickListener); //liujie add 1029
        // hitdelicacy
        /*seekbar_delicacy.setMax(30);
        seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);
        seekbar_delicacy.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar arg0, int arg1, boolean arg2) {
                if (arg2) {
                    SettingBean.mHitDelicacy = arg1;
                    SettingBean.onChage(SettingBean.HIT_CHANGE);
                }
            }

            public void onStartTrackingTouch(SeekBar arg0) {
            }

            public void onStopTrackingTouch(SeekBar arg0) {
            }
        });*/
        btn_hitdelicacy_auto_saving.setOnClickListener(setClickListener);
        btn_click_screen.setOnClickListener(setClickListener);
        btn_power_save_left.setOnClickListener(setClickListener);
        btn_power_save_right.setOnClickListener(setClickListener);

        btn_hitdelicacy_high.setOnClickListener(setClickListener);
        btn_hitdelicacy_middle.setOnClickListener(setClickListener);
        btn_hitdelicacy_lower.setOnClickListener(setClickListener);
        btn_exposure_left.setOnClickListener(setClickListener);
        btn_exposure_right.setOnClickListener(setClickListener);
        
        //liujie add 0926
        btn_antitheftdelicacy_high.setOnClickListener(setClickListener);
        btn_antitheftdelicacy_middle.setOnClickListener(setClickListener);
        btn_antitheftdelicacy_lower.setOnClickListener(setClickListener);
        btn_antitheftdelicacy_off.setOnClickListener(setClickListener);
        //liujie add
        
        // back record
        btn_record_back.setOnClickListener(setClickListener);
        //btn_open_back_camera.setOnClickListener(setClickListener);

        // video set
        resetDefaulBtn.setOnClickListener(setClickListener);
        resetDefaulBtn.setOnLongClickListener(new View.OnLongClickListener() {

            public boolean onLongClick(View v) {
                mtextview_version = ((TextView) mFloatView.findViewById(R.id.apk_version_text_id));
                if (!mtextview_version.isShown()) {
                    String strVersion;
                    try {
                        strVersion = getPackageManager().getPackageInfo("com.dvr.android.dvr", 0).versionName;
                        mtextview_version.setText("Version: " + strVersion);
                    } catch (NameNotFoundException e) {
                        e.printStackTrace();
                    }
                    mtextview_version.setVisibility(View.VISIBLE);
                } else {
                    mtextview_version.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        });
        BtnSDFormat.setOnClickListener(setClickListener);
        btn_power_on_auto_run.setOnClickListener(setClickListener);
    }

    private OnClickListener setClickListener = new OnClickListener() {

        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.btnthree_bgrecord:
                SettingBean.mBGRecord = !SettingBean.mBGRecord;
                showBackRecordButton();
                break;
            case R.id.btn_power_on_auto_run_id:
                SettingBean.mEnableAutoRun = !SettingBean.mEnableAutoRun;
                showVideoAutoRunButton();
                break;
            case R.id.btnfour_defealt:
                if (isRecording()) { //liujie add 1029
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
                	setdefealt();
                }
                break;
            case R.id.video_time_left_button_id:
                if (SettingBean.cirVideoLong > 0) {
                    SettingBean.cirVideoLong--;
                    showCycleVideoTime();
                }
                break;
            case R.id.video_time_right_button_id:
                if (SettingBean.cirVideoLong < (mCycleVideoTimes.length - 1)) {
                    SettingBean.cirVideoLong++;
                    showCycleVideoTime();
                }
                break;
            case R.id.recordquality_high_button_id:
            	if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
                	usbCameraSurfaceView.FrontCameraSetResolution(1920, 1080, 30);
                	SettingBean.mVideoQuality = 1;
                    showRecordQualityButton();
                }
                
                break;
            case R.id.recordquality_middle_button_id:
            	if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
                	usbCameraSurfaceView.FrontCameraSetResolution(1280, 720, 25);
                	SettingBean.mVideoQuality = 2;
                    showRecordQualityButton();
                }
                
                break;
            case R.id.recordquality_lower_button_id:
                SettingBean.mVideoQuality = 3;
                showRecordQualityButton();
                break;
            case R.id.hit_auto_saving_button_id:
                SettingBean.isHitAutosave = !SettingBean.isHitAutosave;
                showhitdelicacyButton();
                break;
            case R.id.click_screen_saving_button_id:
            	if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
	                //SettingBean.isRecordSound = !SettingBean.isRecordSound;
	            	SettingBean.bBeforeSound = !SettingBean.bBeforeSound;
	                showClickSaveButton();
                }
                break;
            case R.id.btn_open_back_camera_id:
                SettingBean.mOpenBackCamera = !SettingBean.mOpenBackCamera;
                showOpenBackCameraButton();
                break;
            case R.id.power_save_left_button_id:
                if (SettingBean.powerSaveIndex > 0) {
                    SettingBean.powerSaveIndex--;
                    showPowerSaveString(false);
                }
                break;
            case R.id.power_save_right_button_id:
                if (SettingBean.powerSaveIndex < (mPowerSaveString.length - 1)) {
                    SettingBean.powerSaveIndex++;
                    showPowerSaveString(false);
                }
                break;
            case  R.id.sd_format:
            	showPopupSDCard(DVRBackService.this, v);
                break;
            //liujie add begin 0927
            case R.id.anti_theft_delicacy_high_button_id:
            	SettingBean.mAntitheftDelicacyType = 10;//2
            	showAntitheftDelicacyButtonS();
            	Settings.System.putInt(getContentResolver(), DVR_ANTITHEFT_DELICACY, SettingBean.mAntitheftDelicacyType);
            	break;
            case R.id.anti_theft_delicacy_middle_button_id:
            	SettingBean.mAntitheftDelicacyType = 20;//1
            	showAntitheftDelicacyButtonS();
            	Settings.System.putInt(getContentResolver(), DVR_ANTITHEFT_DELICACY, SettingBean.mAntitheftDelicacyType);
            	break;
            case R.id.anti_theft_delicacy_lower_button_id:
            	SettingBean.mAntitheftDelicacyType = 30;//0
            	showAntitheftDelicacyButtonS();
            	Settings.System.putInt(getContentResolver(), DVR_ANTITHEFT_DELICACY, SettingBean.mAntitheftDelicacyType);
            	break; 
            case R.id.anti_theft_delicacy_off_button_id:
            	SettingBean.mAntitheftDelicacyType = -1;//-1
            	showAntitheftDelicacyButtonS();
            	Settings.System.putInt(getContentResolver(), DVR_ANTITHEFT_DELICACY, SettingBean.mAntitheftDelicacyType);
            	break;  	
            //liujie add end    
            case R.id.hit_delicacy_high_button_id:
            	SettingBean.mHitDelicacyType = 2;
            	SettingBean.mHitDelicacy = SettingBean.HitDelicacyArray[SettingBean.mHitDelicacyType];
            	showHitDelicacyButtonS();
            	SettingBean.onChage(SettingBean.HIT_CHANGE);
            	break;
            case R.id.hit_delicacy_middle_button_id:
            	SettingBean.mHitDelicacyType = 1;
            	SettingBean.mHitDelicacy = SettingBean.HitDelicacyArray[SettingBean.mHitDelicacyType];
            	showHitDelicacyButtonS();
            	SettingBean.onChage(SettingBean.HIT_CHANGE);
            	break;
            case R.id.hit_delicacy_lower_button_id:
            	SettingBean.mHitDelicacyType = 0;
            	SettingBean.mHitDelicacy = SettingBean.HitDelicacyArray[SettingBean.mHitDelicacyType];
            	showHitDelicacyButtonS();
            	SettingBean.onChage(SettingBean.HIT_CHANGE);
            	break; 
            case R.id.set_exposure_left_button_id:
            	if(SettingBean.mExposureValue > 0)
        		{
            		bIsSettingChanged = true; //liujie add 1013
        			SettingBean.mExposureValue--;
        			showExposureValue();
        		}
            	break;
            case R.id.set_exposure_right_button_id:
            	if(SettingBean.mExposureValue < (mExposureValue.length - 1))
            	{
            		bIsSettingChanged = true; //liujie add 1013
            		SettingBean.mExposureValue++;
            		showExposureValue();
            	}
            	break;
            case R.id.playback_show_time_button_id:
            	SettingBean.bPlayBackShowRecordTime = !SettingBean.bPlayBackShowRecordTime;
            	setPlayBackShowTime();
            	showPlayBackShowTimeButton();
            	break;
            case R.id.show_back_window_saving_button_id: //liujie add 1029
                /*if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
                	mCamera = getCameraBwin();
                	Camera c2 = getCameraBwinSub(mCamera);
                	if(c2 != null){
                		if(cameraSurView==null){
                			playVoice(getString(R.string.rebootdvr));
                			return;
                		}
	            		SettingBean.bShowBackWindow = !SettingBean.bShowBackWindow;
	            		showBackWindowButton();
	            		Config.bBackCameraExist = SettingBean.bShowBackWindow;
	            		 Log.i("PLJ", "DVRBackService---->showOrHideBackCameraWindow:111:"+SettingBean.bShowBackWindow+"    "+(mCamera == null));
	            		if(SettingBean.bShowBackWindow){
	                        if(mCamera==null){
	                        	mCamera = c2;
	                        }
	                        if(mCamera!=null){
	                        	cameraSurView.setCamera(mCamera);
	                        }
	            		}
	            		showOrHideBackCameraWindow();
                	}else{
                		if(bCameraSwitch > 0){
                			playVoice(getString(R.string.backwin_nftry));
                		}else{
                			playVoice(getString(R.string.open_camera_error));
                		}
                	}
            	}*/
            	if (isRecording()) {
                	playVoice(getString(R.string.if_show_back_window_show)); 
                }else{
            		SettingBean.bShowBackWindow = !SettingBean.bShowBackWindow;
            		showBackWindowButton();
            		Config.bBackCameraExist = SettingBean.bShowBackWindow;
            		if(SettingBean.bShowBackWindow){
                        if(mCamera==null){
                        	mCamera = getCameraInstance();
                            cameraSurView.setCamera(mCamera); 
                        }
            		}
            		showOrHideBackCameraWindow();
            	}
            	
            	break;
            }
        }
    };

    protected void setdefealt() {
        SettingBean.isHitAutosave = SettingBean.IsHitAutosave;
        SettingBean.isClickSave = SettingBean.IsClickSave;
        SettingBean.isHasvoice = SettingBean.IsHasvoice;
        SettingBean.isRecordSound = SettingBean.IsRecordSound;
        SettingBean.cirVideoLong = SettingBean.CirVideoLong;
        SettingBean.mVideoQuality = SettingBean.MVideoQuality;
        SettingBean.mHitDelicacy = SettingBean.mHitDelicacy;
        SettingBean.mRecordMode = SettingBean.MRecordMode;
        SettingBean.mBGRecord = SettingBean.MBGRecord;
        SettingBean.mOpenBackCamera = SettingBean.MOpenBackCamera;
        SettingBean.mBGAnim = SettingBean.MBGAnim;
        SettingBean.powersaving_time = SettingBean.PowerSaving_time;
        SettingBean.mPathItem = SettingBean.MPathItem;
        SettingBean.isBackCamera = SettingBean.IsBackCamera;
        SettingBean.mEnableAutoRun = SettingBean.MEnableAutoRun;
        
        //liujie add 0924
        SettingBean.mExposureValue = SettingBean.MExposureValue;
        showExposureValue();
        
        SettingBean.mHitDelicacyType = SettingBean.MHitDelicacyType;
        SettingBean.mHitDelicacy = SettingBean.HitDelicacyArray[SettingBean.mHitDelicacyType];
        SettingBean.onChage(SettingBean.HIT_CHANGE);
        
        SettingBean.bPlayBackShowRecordTime = SettingBean.BPlayBackShowRecordTime;
        setPlayBackShowTime();
        
        
        SettingBean.mAntitheftDelicacyType = SettingBean.MAntitheftDelicacyType;
        Settings.System.putInt(getContentResolver(), DVR_ANTITHEFT_DELICACY, SettingBean.MAntitheftDelicacyType);
        //liujie add end
        
        //liujie add 1029
        SettingBean.bShowBackWindow = SettingBean.BShowBackWindow;
        Config.bBackCameraExist = SettingBean.bShowBackWindow;
        if(SettingBean.bShowBackWindow){
            if(mCamera==null){
            	mCamera = getCameraInstance();
                cameraSurView.setCamera(mCamera); 
            } 
		}
        showOrHideBackCameraWindow();
        //liujie add end
        SettingBean.bBeforeSound = SettingBean.BBeforeSound;
        SettingBean.mVideoQuality = SettingBean.MVideoQuality;
        //seekbar_delicacy.setProgress(SettingBean.mHitDelicacy);

        int[] values = getResources().getIntArray(R.array.setting_power_saving_value);
        for (int i = 0; i < values.length; i++) {
            if (SettingBean.powersaving_time == values[i]) {
                SettingBean.powerSaveIndex = i;
                break;
            }
        }
    }

    private OnTouchListener onSettingTouchListener = new OnTouchListener() {
        public boolean onTouch(View view, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                switch (view.getId()) {
                case R.id.recordmode_button_id:
                    nSettingType = SETTING_TYPE_RECORD_MODE;
                    settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_pressed));
                    settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                    settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                    settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                    settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                    break;
                case R.id.recordquality_button_id:
                    nSettingType = SETTING_TYPE_RECORD_QUALITY;
                    settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                    settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_pressed));
                    settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                    settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                    settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                    break;
                case R.id.hitdelicacy_button_id:
                    nSettingType = SETTING_TYPE_HITDELICACY;
                    settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                    settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                    settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_pressed));
                    settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                    settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                    break;
                case R.id.recordback_button_id:
                    nSettingType = SETTING_TYPE_RECORD_BACK;
                    settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                    settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                    settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                    settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_pressed));
                    settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_normal));
                    break;
                case R.id.videoset_button_id:
                    nSettingType = SETTING_TYPE_VIDEO_SET;
                    settingRecordModeBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_recordmode_bg_normal));
                    settingRecordQualityBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoquality_bg_normal));
                    settingHitDelicacyBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_hit_bg_normal));
                    settingRecordBackBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_back_bg_normal));
                    settingVideoSetBtn.setImageDrawable(getResources().getDrawable(R.drawable.setting_btn_videoset_bg_pressed));
                    break;
                }
                setting_draw_type_content();
            }
            return false;
        }
    };
    
	public void doMethod(boolean bool) {
		if(bool){
			usbCameraSurfaceView.usbCameraPreView();
	    }else{
	    	usbCameraSurfaceView.BackCameraClose();
	    	Message message = new Message();                                            
	    	UsbCameraHandler.sendMessageDelayed(message, 3000);
	    }
	}
	Handler UsbCameraHandler = new Handler() {            
	public void handleMessage(Message msg) {
		super.handleMessage(msg);
			usbCameraSurfaceView.usbCameraInit(0);
	   }
	};
	public void doBatteryMethod(boolean bool) {
		if(!bool){
			usbCameraSurfaceView.BackCameraClose();
	    	Message message = new Message();                                            
	    	UsbCameraHandler.sendMessageDelayed(message, 3000);
	    }
	}

	@Override
	public void doRecStartMethod(boolean bool)
	{   Log.i("PLJ", "DVRBackService---->doRecStartMethod:"+isLoopRec);
		if(isLoopRec){
			startRecord();
			isLoopRec = false;
		}
	}
	
	//penglj_add_20150622_sdformat
	private void showPopupSDCard(Context context, View parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View vPopupWindow = inflater.inflate(R.layout.popup_sdcard, null, false);
        final PopupWindow pw = new PopupWindow(vPopupWindow, 336, 196, true);

        // OK按钮及其处理事件
        Button btnOK = (Button) vPopupWindow.findViewById(R.id.BtnOK);
        btnOK.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
            	pw.dismiss();
            	mProgressDialog=new ProgressDialog(v.getContext());
			    mProgressDialog.setMessage(getString(R.string.sdcard_formatinfo));
			    mProgressDialog.setCancelable(false);
			    mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
			    mProgressDialog.show();
				new Thread(new Runnable() {
    				public void run() {
    					boolean bool = wipingSdcard();
    					if(bool){
    						//((DRVApp) DRVApp.getApplication()).showToast(getString(R.string.sdcard_forma_sucess)); //Toast.makeText(DRVApp.getApplication(), R.string.sdcard_forma_sucess, Toast.LENGTH_SHORT).show();
    						Message message = new Message();   
    						message.what =1;
    						SDCardHandler.sendMessage(message);
    					}else{
    						//((DRVApp) DRVApp.getApplication()).showToast(getString(R.string.sdcard_forma_fail)); //Toast.makeText(DRVApp.getApplication(), R.string.sdcard_forma_fail, Toast.LENGTH_SHORT).show();
    						Message message = new Message();   
    						message.what =2;
    						SDCardHandler.sendMessage(message);
    					}
    				}
    			}).start();
                pw.dismiss();// 关闭
            }
        });

        // Cancel按钮及其处理事件
        Button btnCancel = (Button) vPopupWindow.findViewById(R.id.BtnCancel);
        btnCancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                pw.dismiss();// 关闭
            }
        });

        // 显示popupWindow对话�?
        if (parent != null) {
            pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
        }
    }
	
	Handler SDCardHandler = new Handler() {            
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
				switch (msg.what) {
					case 1:
						mProgressDialog.dismiss();
						((DRVApp) DRVApp.getApplication()).showToast(getString(R.string.sdcard_forma_sucess));
					  break;
					case 2:
						mProgressDialog.dismiss();
						((DRVApp) DRVApp.getApplication()).showToast(getString(R.string.sdcard_forma_fail));
					  break;
					 
					default:
					  break;
				}

		   }
		};
	
	protected void dialog() {
		  AlertDialog.Builder builder = new Builder(getApplicationContext());
		  builder.setMessage(R.string.sdcard_format_ok);
		  builder.setTitle(R.string.tip);
		  builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{   
				dialog.dismiss();
				new Thread(new Runnable() {
    				public void run() {
    					boolean bool = wipingSdcard();
    					if(bool){
    						Toast.makeText(DVRBackService.this, R.string.sdcard_forma_sucess, Toast.LENGTH_SHORT).show();
    					}else{
    						Toast.makeText(DVRBackService.this, R.string.sdcard_forma_fail, Toast.LENGTH_SHORT).show();
    					}
    				}
    			}).start();
			}
		  });
		  builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		  });
		  builder.create().show();
		 }
	
	private boolean wipingSdcard() {
		boolean bool = true;
        File deleteMatchingFile = new File(Config.EXCARD_PATH);
        try {
            File[] filenames = deleteMatchingFile.listFiles();
            if (filenames != null && filenames.length > 0) {
                for (File tempFile : filenames) {
                    if (tempFile.isDirectory()) {
                    	bool = wipeDirectory(tempFile.toString());
                        tempFile.delete();
                    } else {
                        tempFile.delete();
                    }
                }
            } else {
                deleteMatchingFile.delete();
            }
        } catch (Exception e) {
            e.printStackTrace();
            bool = false;
        }
        return bool;
    }

    private boolean wipeDirectory(String name) {
    	boolean bool = true;
        File directoryFile = new File(name);
        File[] filenames = directoryFile.listFiles();
        try {
			if (filenames != null && filenames.length > 0) {
			    for (File tempFile : filenames) {
			        if (tempFile.isDirectory()) {
			            wipeDirectory(tempFile.toString());
			            tempFile.delete();
			        } else {
			            tempFile.delete();
			        }
			    }
			} else {
			    directoryFile.delete();
			}
		} catch (Exception e) {
			e.printStackTrace();
			bool = false;
		}
        return bool;
    }
	/*add by zengpeng 20121108*/
    String retMicro = "0";
    private class DVRBinder extends DVRRemote.Stub{

		@Override
		public int getDVRStatus() throws RemoteException
		{
			int ret = 0;
			if(mFloatView!=null){
			   if(isRecording()){
				   ret = 2;
			   }else{
				   ret = 0;
			   }
			}else{
			   ret = 1;
			}
			return ret;
		}

		@Override
		public String DVRTakePicture() throws RemoteException
		{
			String ret = "0";
			if(mFloatView!=null){
				   if(!checkSDCard(true)){
					   ret = "2";
				   }else if (isRecording()) {
					   if(isInitDvrRec){
						   if(!mbTakePicture){
				            	mbTakePicture = true;
				                mHandler.removeMessages(MODE_START_RECORD);
				                mHandler.removeMessages(MODE_LOOP_STOP);
				                stopRecordAIDL(false);//stopRecord(0);
				                //mHandler.sendEmptyMessageDelayed(MSG_EXIT_CAPUTER_AIDL, 1500);
				                String capturepath = doubleCapture();//capture();
				                mHandler.sendEmptyMessageDelayed(MODE_START_RECORD_AIDL, 1500);//mHandler.sendEmptyMessageDelayed(MODE_START_RECORD, 1500);
				                ret = capturepath;
						   }
					   }else{
						   ret = "-1";
					   }
	               } else {
	            	   if(isInitDvr){
		            	   if(!mbTakePicture){
		            		  String capturepath = doubleCapture();//capture();//dealWithBtnSetOrPlayback(R.id.btn_takepicture, 1);
		            		  ret = capturepath;
		            	   }
	            	   }else{
	            		   ret = "-1";
	            	   }
	               }
			}else{
				ret = "1";
			}
			return ret;
		}

		@Override
		public int DVRStartRecord() throws RemoteException
		{
			int ret = 0;
			if(mFloatView!=null){
			   if(!checkSDCard(true)){
				   ret = 2;
			   }else{
				   if(!bUsbCameraInit1){
					   if(isInitDvr){
						   if(Config.isMicroVideoPass == 0){
							    ServiceStartRecordAIDL(); ret = 0;
						   }else if(Config.isMicroVideoPass == 1){
			            		playVoice(getString(R.string.microplay_noaction)); ret = 3;
			               }
					   }else{
						 ret = -1;
					   }
				   }else{
					    if (usbCameraSurfaceView != null) {
				    		int microsta = usbCameraSurfaceView.BackCameraClose();
				    		if(microsta != -3){
				    			usbCameraSurfaceView.usbCameraInit(1);
				    			bUsbCameraInit1 = true;
				    			ServiceStartRecordAIDL(); ret = 0;
				    		}
				    	}
				   }
			   }
			}else{
				ret = 1;
			}
			return ret;
		}

		@Override
		public int DVRStopRecord() throws RemoteException
		{
			int ret = 0;
			if(mFloatView!=null){
            	if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 0){
            		if (isRecording()){
            			if(isInitDvrRec){
    					   ServiceStopRecordAIDL(false); ret = 3;
            			}else{
   						 ret = -1;
 					   }
    				}else{
    					ret = 4;
    				}
            	}else if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 1){
            		playVoice(getString(R.string.microplay_noaction)); ret = 2;
            	}
			}else{
				ret = 1;
			}
			return ret;
		}

		@Override
		public int DVRLockVideo() throws RemoteException
		{
			int ret = 0;
			if(mFloatView!=null){
				if (isRecording() && !mVideoLock) {
					if(isInitDvrRec){
			            mHandler.post(new Runnable() {
				            public void run() {
				            	btn_Lock.setBackgroundResource(R.drawable.btn_main_lock_pressed);
				            }
				        });
			            mVideoLock = true;
		                ret = 0;
	                }else{
  						ret = -1;
					}
	            }else{
	            	if(isInitDvr){
		            	if(mVideoLock){
		            		ret = 3;
		            	}else{
		            		ret = 2;
		            	}
	            	}else{
  						ret = -1;
					}
	            }
			}else{
				ret = 1;
			}
			return ret;
		}

		@Override
		public String DVRMicroVideo() throws RemoteException
		{
			String ret = "0";
			if(mFloatView!=null){
        		if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 0){
					if (isRecording()) {
						if(isInitDvrRec){
							mHandler.removeMessages(MODE_START_RECORD);
		                    mHandler.removeMessages(MODE_LOOP_STOP);
		                    stopRecordAIDL(false);// stopRecord(0);
		                    
		                    mMicroFileName = FORMAT.format(new Date());
		                    mMicroFileName = mMicroFileName.substring(0, 4)+"-"+mMicroFileName.substring(4, 6)+"-"+mMicroFileName.substring(6, 8)+"_"+mMicroFileName.substring(8, 10)+"-"+mMicroFileName.substring(10, 12)+"-"+mMicroFileName.substring(12, 14);   
		                    String recordPath = Config.SAVE_MICRO_PATH;
		                    File path = new File(recordPath);
		                    if (!path.exists()) {
		                        try {
		                            path.mkdirs();
		                        } catch (Exception e) { e.printStackTrace(); }
		                    }
		                    if (SettingBean.mOpenBackCamera) {
		                    	mMicroVideoFile = new File(recordPath, mMicroFileName /*+ Config.FRONT_MICRO_SUFFIX*/ + Config.VIDEO_SUFFIX); //BACK_CAMERA_SUFFIX
		                    }
		                    ret = MicroVideoFun2(mMicroVideoFile.getAbsolutePath());
						}else{
	  						ret = "-1";
						}
		            }else{
		            	if(isInitDvr){
			            	if(retMicro.equals("-4")){
			                    mMicroFileName = FORMAT.format(new Date());
			                    mMicroFileName = mMicroFileName.substring(0, 4)+"-"+mMicroFileName.substring(4, 6)+"-"+mMicroFileName.substring(6, 8)+"_"+mMicroFileName.substring(8, 10)+"-"+mMicroFileName.substring(10, 12)+"-"+mMicroFileName.substring(12, 14);   
			                    String recordPath = Config.SAVE_MICRO_PATH;
			                    File path = new File(recordPath);
			                    if (!path.exists()) {
			                        try {
			                            path.mkdirs();
			                        } catch (Exception e) { e.printStackTrace(); }
			                    }
			                    if (SettingBean.mOpenBackCamera) {
			                    	mMicroVideoFile = new File(recordPath, mMicroFileName /*+ Config.FRONT_MICRO_SUFFIX*/ + Config.VIDEO_SUFFIX); //BACK_CAMERA_SUFFIX
			                    }
			                    retMicro = "0";
			                    ret = MicroVideoFun2(mMicroVideoFile.getAbsolutePath());
			            	}else{
			            		ret = MicroVideoFun1();
			            	}
		            	}else{
	  						ret = "-1";
						}
		            }
	        	}else if(Config.isMicroVideoPass /*libucamera.native_getRecordIsLV()*/ == 1){
	        		playVoice(getString(R.string.microplay_noaction)); ret = "2";
	        	}
			}else{
				ret = "1";
			}
			return ret;
		}

		@Override
		public void sendPhotoAndVideoPath() throws RemoteException
		{
			try {
				if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1009
					broadVioce.setPhotoPath(mUsbImageFile.getAbsolutePath() + "," + mImageFile.getAbsolutePath());
					broadVioce.setVideoPath(mVideoFileBack1.getAbsolutePath() + "," + mVideoFile.getAbsolutePath());
				}else{
					broadVioce.setPhotoPath(mUsbImageFile.getAbsolutePath() + ",");
					broadVioce.setVideoPath(mVideoFileBack1.getAbsolutePath() + ",");
				}

			} catch (RemoteException e) {}
		}

    }
    
    public String MicroVideoFun1(){
    	String ret = "2";
    	if (usbCameraSurfaceView != null) {
    		int microsta = usbCameraSurfaceView.BackCameraClose();
    		if(microsta != -3){
	            mMicroFileName = FORMAT.format(new Date());
	            mMicroFileName = mMicroFileName.substring(0, 4)+"-"+mMicroFileName.substring(4, 6)+"-"+mMicroFileName.substring(6, 8)+"_"+mMicroFileName.substring(8, 10)+"-"+mMicroFileName.substring(10, 12)+"-"+mMicroFileName.substring(12, 14);   
	            String recordPath = Config.SAVE_MICRO_PATH;
	            File path = new File(recordPath);
	            if (!path.exists()) {
	                try {
	                    path.mkdirs();
	                } catch (Exception e) { e.printStackTrace(); }
	            }
	            if (SettingBean.mOpenBackCamera) {
	            	mMicroVideoFile = new File(recordPath, mMicroFileName /*+ Config.FRONT_MICRO_SUFFIX*/ + Config.VIDEO_SUFFIX); //BACK_CAMERA_SUFFIX
	            }
	            
	            usbCameraSurfaceView.usbCameraInit(1);//usbCameraInit  usbCameraMicroInit
	            Config.isMicroVideoPass = 1;
	            
            	ret = mMicroVideoFile.getAbsolutePath();//"2"
	            usbCameraSurfaceView.BackCameraRecord(mMicroVideoFile.getAbsolutePath(),SettingBean.bBeforeSound?0:1);
	            mHandler.sendEmptyMessageDelayed(MSG_MICRO_VIDEO, 1000 * 14);
            }else{
            	ret = "-3";
            }
        }
    	return ret;
    }
    
    public String MicroVideoFun2(String micropath){
    	String ret = "2";
    	if (usbCameraSurfaceView != null) {
    		if(libucamera.native_getclosestate() == 1){
    			int microsta = usbCameraSurfaceView.BackCameraClose();

	            usbCameraSurfaceView.usbCameraInit(1);//1微视频0正常视频 usbCameraInit  usbCameraMicroInit
	            Config.isMicroVideoPass = 1;
	            
	            if(microsta != -3){
	            	ret = micropath;
	            	usbCameraSurfaceView.BackCameraRecord(micropath,SettingBean.bBeforeSound?0:1);
		            Message message=new Message();  
		            Bundle bundle=new Bundle();  
		            bundle.putBoolean("micro2", true);//putString("micro", mMicroVideoFile.getAbsolutePath());  
		            message.setData(bundle);
		            message.what=MSG_MICRO_VIDEO;
		            mHandler.sendMessageDelayed(message, 1000 * 14);
		            //mHandler.sendEmptyMessageDelayed(MSG_MICRO_VIDEO, 1000 * 10);
	            }else{
	            	ret = "-3";
	            }
	            
    		}else{
    			ret = "-4";
    			retMicro = "-4";
    		}
        }
    	return ret;
    }
    
    //ServiceStartRecordAIDL start
    private void ServiceStartRecordAIDL() {
	    /*if (!SDcardManager.checkSDCardMount()) {
	        mHandler.sendEmptyMessage(MSG_SDCARD_UNREMOUNT_DLG_SHOW);
	        return;
	    }*/
	    if (mState == CURRENT_STATE_UNRECODING) {
		    /*mHandler.post(new Runnable() {
	            public void run() {
	            	mChronometer.setBase(SystemClock.elapsedRealtime());
	    	        mChronometer.start(); 
	            }
	        });*/
			// liujie ,modify begin 0828
			/*
			 * DvrStatusIntent.putExtra("dvrstatus", 3);
			 * sendBroadcast(DvrStatusIntent);
			 */
			
			mState = CURRENT_STATE_RECODING;
			// liujie,modify end
	        startRecordAIDL();//startRecord();
	    }
	}
    private void startRecordAIDL() {
	    mbReadyStartRecord = true;
	    if (!checkSDCard(true)) {
	        // 重新录制的时候就没有空间�?015.01.08
	        mHandler.removeMessages(MODE_START_RECORD);
	        mHandler.removeMessages(MODE_LOOP_STOP);
	      //liujie add begin 0906 
	        mState = CURRENT_STATE_UNRECODING; 
			//try { 
			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING); 
			//} catch (Exception localException) { } 
			//liujie add end
	        updateUIAIDL();
	        cancleRecord();
	        updateStatus(Config.STATUS_IDLE);
	        mbReadyStartRecord = false;
	        return;
	    }
        //liujie add begin 0910
		//try {
			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_RECODING);
		//} catch (Exception localException) {}
		//liujie add end 0910
	    mState = CURRENT_STATE_RECODING;
	    updateUIAIDL();
	    
	    if (mIsPowerOnAutorun) {
	        //showBackgroudNotification(getString(R.string.background_recording));
	        mOnBackground = true;
	    }
	    new startRecordTask().execute(); //recordVideo(); 
	    mbReadyStartRecord = false;
	}
    private void updateUIAIDL() {
	    switch (mState) {
	    case CURRENT_STATE_UNRECODING:
	    	mHandler.post(new Runnable() {
	            public void run() {
	            	startRecordBtn.setVisibility(View.VISIBLE);
	    	        stopRecordBtn.setVisibility(View.INVISIBLE);
	            }
	        });
	        break;
	    case CURRENT_STATE_RECODING:
	    	mHandler.post(new Runnable() {
	            public void run() {
	            	startRecordBtn.setVisibility(View.INVISIBLE);
	    	        stopRecordBtn.setVisibility(View.VISIBLE);
	            }
	        });
	        break;
	    case CURRENT_STATE_HIDE:
	    	mHandler.post(new Runnable() {
	            public void run() {
	            	stopRecordBtn.setVisibility(View.INVISIBLE);
	    	        startRecordBtn.setVisibility(View.INVISIBLE);
	            }
	        });
	        break;
	    }
	}
    //ServiceStartRecordAIDL end
    
    //ServiceStopRecordAIDL start
    private void ServiceStopRecordAIDL(boolean isLoop) {
	    if (mState == 1) {
	        mHandler.removeMessages(MODE_START_RECORD);
	        mHandler.removeMessages(MODE_LOOP_STOP);
	
	        stopRecordAIDL(true);
	        if (!isLoop) {
	        	cancleRecordAIDL();//cancleRecord();
	        } else {
	            startRecord();
	        }
	        updateStatus(Config.STATUS_IDLE);
	    }
	}

      private void stopRecordAIDL(boolean isUpdateUi) {
		    if (mMediaRecorder == null) {
		        return;
		    }
		    mState = 0;        
		    updateStatus(STATUS_SAVING);
		    boolean bCanDelete;
		    if (mVideoLock || mHitSave) {
		        bCanDelete = false;
		        if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1013
		        mVideoFile = createLockFile(mVideoFile);}
		        blsFile1 = mVideoFileBack1;
		        mVideoFileBack1 = blsFile2 = createLockFileBack(mVideoFileBack1);
		    } else {
		        bCanDelete = true;
		    }
		    
		    try {
		    	//mMediaRecorder.setOnErrorListener(null);//liujie add 1013
		    	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1013
		        mMediaRecorder.stop();}
		        mMediaRecorder.reset();
		        if (mVideoLock){ usbCameraSurfaceView.BackCameraStopRec(mVideoFileBack1.getAbsolutePath(),0); 
		        }else{ usbCameraSurfaceView.BackCameraStopRec("",0); }
		        mStopRecordTime = SystemClock.uptimeMillis()/*System.currentTimeMillis()*/;//liujie 1024
		    } catch (RuntimeException e) {
		        e.printStackTrace();
		    }
		    if (mCamera != null)
		        mCamera.lock();
		    
		    // SD卡是否移除
		    if (!mDeviceUnmount) {
		    	if(SettingBean.bShowBackWindow/*Config.bBackCameraExist*/){ //liujie add 1013
		    	mVideoTmpFile.renameTo(mVideoFile);
		    	saveVideo(bCanDelete);
		        }
		        mVideoFileBack.renameTo(mVideoFileBack1);
		        //new StopFileOption().execute(bCanDelete);
		        saveVideo_back(bCanDelete, mVideoFileBack1);
		        if(!bUsbCameraInit1){ //liujie modify 1020
		        	GpsProvider.getGpsProvider(DVRBackService.this).stopRecoder();
		        }
		    }
		    
		    Config.gbRecordStatus = false;
		    mDeviceUnmount = false;
		    if (mVideoLock){
		    	if(isUpdateUi){
			    	mVideoLock = false;
		            mHandler.post(new Runnable() {
			            public void run() {
			            	btn_Lock.setBackgroundResource(R.drawable.btn_main_lock_normal);
			            }
			        });
		    	}
		    }
		    if(isUpdateUi){
		       updateUIAIDL();
		    }
		}
	  private void cancleRecordAIDL() {
		  /*mHandler.post(new Runnable() {
	            public void run() {
	            	mChronometer.stop();
	    	        mChronometer.setBase(SystemClock.elapsedRealtime());
	            }
	        });*/
		// liujie modify begin 0828
		/*
		 * DvrStatusIntent.putExtra("dvrstatus", 2);
		 * sendBroadcast(DvrStatusIntent);
		 */
		//try {
			Settings.System.putInt(getContentResolver(), DVR_RECORD_STATE, CURRENT_STATE_UNRECODING);
		//} catch (Exception localException) {}
		mState = CURRENT_STATE_UNRECODING;
		// liujie modify end
	  }
      //ServiceStopRecordAIDL end

	  /**
	   * liujie add 1029
	   */
	private void showOrHideBackCameraWindow() {
		if (mCameraLayout != null) {
			if(!SettingBean.bShowBackWindow){
				cameraSurView.setCamera(null);
				closeCamera();
			}else{
				startPreview();
				//cameraSurView.setCamera(mCamera);
			}
			bCameraSwitch++;
			mCameraLayout.setVisibility(SettingBean.bShowBackWindow ?  View.VISIBLE :View.INVISIBLE);
		}
	}
}