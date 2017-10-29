package com.dvr.android.dvr.msetting;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class SettingBean
{
    
    /**
     * 撞击值改�?
     */
    public static final int HIT_CHANGE = 0;
    
    private static ArrayList<OnSettingChangeListener> settingListeners = new ArrayList<OnSettingChangeListener>();
    
    public static final boolean IsHitAutosave =true;
    //10�?5�?0�?5�?0，�?。�?�?0
    
    public static final int CirVideoLong = 0;    // old is 1
    //分为 0 1 2 3 代表精细 �?�?�?
    public static final int MVideoQuality = 2;
    //1~20 (5)
    public static final int MHitDelicacy = 5;    
    public static  final int MHitDelicacyType = 1; //liujie add 0924
    //1~4 (5)
    public static final int PowerSaveIndex = 3;
  //曝光默认值 0表示-3；1表示-2；2表示-1；3表示0；4表示1；5表示2；6表示3；
    public static int ExposureDefaultValue = 3;
    
    // 录制模式 0-循环录制  1-正常录制
    public static final boolean MRecordMode = false;
    // 是否后台录制 0-不支持后台录�? 1-支持后台录制
    public static final boolean MBGRecord = true;
    // 后台录制动画 0-不支持后台录制动�? 1-支持后台录制动画
    public static final boolean MBGAnim = false;   //true  

    // 是否打开后置摄像�?0-不打�?��置摄像头  1-打开后置摄像�?
    public static boolean MOpenBackCamera = true;     
    
    // �?��自动运行录像 0-不支�? 1-支持
    public static final boolean MEnableAutoRun = true;
    /*//max=100
    
    public static final int MScreenBright = 50;*/
  //默认回放时显示录制 时间 false-不显示 true-显示
    public static boolean MPlayBackShowRecordTime = true;
    
    public static final boolean IsClickSave =true;
    
    public static final boolean IsHasvoice =true;
    
    public static final boolean IsRecordSound =false;     // old is true
    
    //默认是后摄像�?
    public static final boolean IsBackCamera = true;  // true  后置    // false 前置

    
    
    public static final int PowerSaving_time = -1;
    public static final int MPathItem = 0;
    //public static final int CirVideoArray[] = {1, 3, 5, 10};
    public static final int CirVideoArray[] = {2, 3, 5};

    public static final boolean IsLuRuiTongClient = false; 
    
    public static  boolean misLuRuiTongClient = false; 
    
    public static  boolean isHitAutosave =true;
  //摄像头设�? true为后摄像�? false为前摄像�?
    public static  boolean isBackCamera = true;   //true  后置   // false 前置
    //10---60(0----10)
    public static  int cirVideoLong = 0;     //old is 1
    //分为 1 2 3 4 代表精细 �?�?�?
    public static  int mVideoQuality = 2;
    //max=20
    public static  int mHitDelicacy = 1;
    //撞击灵敏度  0为低，1为中，2为高
    public static  int mHitDelicacyType = 1;
    public static final int HitDelicacyArray[] = {0, 2, 5};
    
    //liujie add 0926
    public static  final int MAntitheftDelicacyType = 10;
    public static  int mAntitheftDelicacyType = 10;
    
    public static  int powerSaveIndex = 3;
    
  //曝光默认值 0表示-3；1表示-2；2表示-1；3表示0；4表示1；5表示2；6表示3；
    public static final int MExposureValue = 3; //liujie add 0924
    public static int mExposureValue = 3;
    
    // 录制模式 0-循环录制  1-正常录制
    public static boolean mRecordMode = false;
    // 是否后台录制 0-不支持后台录�? 1-支持后台录制
    public static boolean mBGRecord = true;     //
    // 是否打开后置摄像�?0-不打�?��置摄像头  1-打开后置摄像�?
    public static boolean mOpenBackCamera = true;     
    // 后台录制动画 0-不支持后台录制动�? 1-支持后台录制动画
    public static boolean mBGAnim = false;  //true

    // �?��自动运行录像 0-不支�? 1-支持
    public static boolean mEnableAutoRun = true;   //true
    //记录保存目录
    public static int mPathItem = 0;
    
  //默认回放时显示录制 时间
    public static final boolean BPlayBackShowRecordTime = true;
    public static boolean bPlayBackShowRecordTime = true;
  /*  //max=100
    
    public static  int mScreenBright = 50;*/
    
    //liujie add 1029
    public static final boolean BShowBackWindow = true;
    public static boolean bShowBackWindow = true;
    
    
    public static  boolean isClickSave =false;
    
    public static  boolean isHasvoice =true;
    
    public static  boolean isRecordSound =false;    // default is true
     
    public static int powersaving_time = -1;
    
    private static boolean mIsFirst = true; 
    
    // add 1107
    public static final boolean BBeforeSound = true; //0有声音  1无声音
    public static boolean bBeforeSound = true;

    /**
     * 设置监听
     * @param listener
     */
    public static void setSettingListener(OnSettingChangeListener listener)
    {
        if(null != listener)
        {
            settingListeners.add(listener);
        }
    } 
    
    /**
     * 移除监听
     * @param listener
     */
    public static void removeListener(OnSettingChangeListener listener)
    {
        settingListeners.remove(listener);
    }
    
    public static void onChage(int type)
    {
        for(int i=0 ; i < settingListeners.size(); i++)
        {
            OnSettingChangeListener listener  = settingListeners.get(i);
            if(null != listener)
            {   Log.i("PLJ", "SettingBean---->onChage:"+type);
                listener.onSettingChange(type);
            }
            else
            {
                settingListeners.remove(i);
                i --;
            }
        }
    }
    
    public static void getdatafromShareP(Context context) {
        if (mIsFirst) {
        	System.out.println("getdatafromShareP");
            mIsFirst = false;
          
        	SharedPreferences share = context.getSharedPreferences("xgx", Context.MODE_PRIVATE);
            isHitAutosave = share.getBoolean("isHitAutosave", IsHitAutosave);
            isClickSave = share.getBoolean("isClickSave", IsClickSave);
            isHasvoice = share.getBoolean("isHasvoice", IsHasvoice);
            isRecordSound = share.getBoolean("isRecordSound", IsRecordSound);
            // add by zw
            mRecordMode = share.getBoolean("mRecordMode", MRecordMode);
            // add by zw
            mBGRecord = share.getBoolean("mBGRecord", MBGRecord);
            // add by zw
            mBGAnim = share.getBoolean("mBGAnim", MBGAnim);
            mPathItem = share.getInt("mPathItem", MPathItem);
            powersaving_time = share.getInt("powersaving_time", PowerSaving_time);
            cirVideoLong = share.getInt("cirVideoLong", CirVideoLong);
            mVideoQuality = share.getInt("mVideoQuality", MVideoQuality);
            mHitDelicacyType = share.getInt("mHitDelicacyType", MHitDelicacyType);
            mHitDelicacy = HitDelicacyArray[mHitDelicacyType];// = share.getInt("mHitDelicacy", mHitDelicacy);
            bPlayBackShowRecordTime = share.getBoolean("bPlayBackShowRecordTime", MPlayBackShowRecordTime);
            mExposureValue = share.getInt("mExposureValue", ExposureDefaultValue);
            
            //liujie add 0926
            mAntitheftDelicacyType = share.getInt("mAntitheftDelicacyType", MAntitheftDelicacyType);
            android.provider.Settings.System.putInt(context.getContentResolver(), "dvr_antitheft_delicacy", mAntitheftDelicacyType); //liujie 1019
            //liujie add end
            
            //liujie add 1029
            bShowBackWindow = share.getBoolean("bShowBackWindow", BShowBackWindow);
            //liujie add end
            bBeforeSound = share.getBoolean("bBeforeSound", BBeforeSound);
            try{
                isBackCamera = share.getBoolean("isBackCamera", IsBackCamera);
            } catch(Exception e) {
            	isBackCamera = true;
            }

            mEnableAutoRun = share.getBoolean("mEnableAutoRun", MEnableAutoRun);
            misLuRuiTongClient = share.getBoolean("mLuRuiTong", IsLuRuiTongClient);
            mOpenBackCamera = share.getBoolean("mOpenBackCamera", MOpenBackCamera);
       //     Log.e("isBackCamera","isBackCamera"+isBackCamera);
            /*SettingBean.mScreenBright = share.getInt("mScreenBright", 50);*/
        }
    }

}
