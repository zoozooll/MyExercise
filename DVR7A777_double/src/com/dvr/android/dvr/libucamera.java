package com.dvr.android.dvr;

import java.nio.ByteBuffer;

import com.dvr.android.dvr.util.UsbCameraCallBack;

import android.R.bool;
import android.graphics.Bitmap;
import android.util.Log;


public class libucamera {
	
	public static String TAG = "libucamera";
	public final static int JCMD_INIT = 200;
	public final static int JCMD_CLOSE = 201;
	public final static int JCMD_REC = 202;
	public final static int JCMD_STOPREC = 203;
	public final static int JCMD_PREVIEW = 204;
	public final static int JCMD_STOPPREVIEW = 205;
	public final static int JCMD_SETFRAMEBUF = 206;
	public final static int JCMD_READFRAME = 207;
	public final static int JCMD_GETVER = 208;
	public final static int JCMD_CHECKDEVICE = 209;
	public final static int JCMD_TAKEPICTURE = 210;
	public final static int JCMD_SETTIMEINFO = 211;
	public final static int JCMD_SETGPSINFO = 212;
	public final static int JCMD_SETRESOLUTION = 213;

	public final static int JEVT_INIT = 100;
	public final static int JEVT_REC = 101;
	public final static int JEVT_GETVER = 102;
	public final static int JEVT_CHECKDEVICE = 103;
	public final static int JEVT_CLOSEFILE_SUCCESS = 104;
	public final static int JEVT_BACK_REC_NAME = 105;
	public final static int JEVT_STOPREC = 106;

	
	private static UsbCameraCallBack mUsbCameraCallPrev;

	static{
		Log.i("PLJ", "***libucamera***");
		System.loadLibrary("jucamera");
	}
	
	public static native int native_ucameracmd(int cmd, String p0, int p1, int p2, int p3);
	public static native int native_ucameracmd2(int cmd, String p0, int p1, int p2, int p3, ByteBuffer p4);
	public static native int native_getclosestate();//1成功 0失败
	public static native int native_getRecordIsLV();//1微视频 0正常
	public static native int native_recordvideo();
	public static native int native_setbmp(Bitmap bitmap); 
	public static native int native_initBmp(Bitmap bitmap);
	
	private static boolean isRecStatus = false;
	
	static public void playVFrame(int id, byte[]frame, int size){
		Log.e(TAG, "***id = " + id + ", frame = " + frame + ", size=" + size + "***");
		//DVRBackService.PerviewSurfave(id, frame,size);
	}
	
	public static void oncameraevt(short event, int param0, int param1, int param2, int param3, String param4){
		if(event == JEVT_INIT){
			if(param0 < 0){
				Log.e("PLJ", "libucamera---->***JEVT_INIT failed***");
				/*mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
				mUsbCameraCallPrev.invokeMethod(false);*/
			}else{
				Log.e("PLJ", "libucamera---->***JEVT_INIT Success***");
				/*mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
				mUsbCameraCallPrev.invokeMethod(true);*/
			}
		}else if(event == JEVT_REC){
			if(param0 < 0){
				Log.e("PLJ", "***JEVT_REC failed***");
				isRecStatus = true;
			}else{
				Log.e("PLJ", "***JEVT_REC Success***");
			}
		}else if(event == JEVT_STOPREC)
		{
			if(param0 < 0){
				Log.e("PLJ", "***JEVT_STOPREC failed***");
				
			}else{
				Log.e("PLJ", "***JEVT_STOPREC Success***");
				//if(isRecStatus){ Log.i("PLJ", "libucamera---->doRecStartMethod:111:");
					mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
					mUsbCameraCallPrev.doRecStartMethod(true);
					isRecStatus = false;
				//}
			}
		}else if(event == JEVT_CHECKDEVICE){
			if(param0 < 0){
				Log.e("PLJ", "***JEVT_CHECKDEVICE failed***");
				mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
				mUsbCameraCallPrev.BatteryMethod(false);
			}else{
				Log.e("PLJ", "***JEVT_CHECKDEVICE Success***");
				mUsbCameraCallPrev = UsbCameraCallBack.getInstance();
				mUsbCameraCallPrev.BatteryMethod(true);
			}
		}
		
		return;
	}
}