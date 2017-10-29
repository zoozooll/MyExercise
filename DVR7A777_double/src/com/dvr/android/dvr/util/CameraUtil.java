package com.dvr.android.dvr.util;

import android.app.Activity;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.util.Log;
import android.view.Surface;

public class CameraUtil {
	
	private final static String TAG = "CameraUtil";
	
	public static int sDegree = 90;
    public static int sRotation = 1;
	
	public static void setUtilDegree(Activity activity) {
        sDegree = getDisplayRotation(activity);
        sRotation = activity.getWindowManager().getDefaultDisplay().getRotation();
    }

    public static int getDisplayRotation(Activity activity) {
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: return 0;
            case Surface.ROTATION_90: return 90;
            case Surface.ROTATION_180: return 180;
            case Surface.ROTATION_270: return 270;
        }
        return 0;
    }

    public static void setCameraDisplayOrientation(Activity activity,
            int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        
        int degrees;
        int result;

        degrees = sDegree;
        
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        try {
        camera.setDisplayOrientation(result);
        } catch (RuntimeException e) {
        }
    }
    public static void setCameraDisplayOrientation(
            int cameraId, Camera camera) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        
        int degrees;
        int result;

        degrees = sDegree;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        try {
        camera.setDisplayOrientation(result);
        } catch (RuntimeException e) {
        }
    }
    
    public static int getRotation(int cameraId) {
    	CameraInfo info = new CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        
        int degrees;
        int rotation;
        
        degrees = sDegree;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
        	rotation = (info.orientation + degrees) % 360;
        	rotation = (360 - rotation) % 360;   
        } else {  // back-facing
        	rotation = (info.orientation - degrees + 360) % 360;
        }
        return rotation;
    }
}
