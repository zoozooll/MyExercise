package com.dvr.android.dvr;

import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.Size;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
	private SurfaceHolder mHolder;
	private Camera mCamera = null;
	private static final String TAG = "CameraSurfaceView";
	Context mContext;

	public CameraSurfaceView(Context context, AttributeSet attrs){
	    super(context,attrs);
	}

	public void setCameraSurfaceView(Context context, Camera camera) {
		//super(context);
		mContext = context;
		mCamera = camera;
		// 安装�?��Surfaceholder.Callback
		// 如需创建和销毁底层的Surface时可以获得�?�?

		// 操作surface的holder
		mHolder = getHolder();
		// 创建SurfaceHolder.Callback对象
		mHolder.addCallback(this);
		// 设置Push缓冲类型，说明surface数据由其他来源提价，而不是用自己的Canvas来绘图，
		// 在这里由摄像头来提供数据
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void setCamera(Camera camera) {
	    this.mCamera = camera;
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// 获得相机参数对象
		if(mCamera == null) {
			Log.e(TAG, "surfaceChanged mCamera null");
			return;
		}
		mHolder = holder;
		Camera.Parameters parameters = null;		
		try {
			parameters = mCamera.getParameters();	
		} catch (Exception e) {
			Log.e(TAG, "surfaceChanged getParameters fail!");
			e.printStackTrace();
		}

		if(parameters == null) {
			//经常出现内存不足的情况下，录制文件已经出现了0B的情况下，进入DVR就出现这样的错误，黑屏了
			Log.e(TAG, "surfaceChanged parameters null");
			((DVRBackService)mContext).RestartServiceForError();
			return;
		}
		// 设置格式
		parameters.setPictureFormat(PixelFormat.JPEG);
		// 设置自动对焦
		parameters.setFocusMode("auto");
		// 设置图片保存时的分辨率大小
		parameters.getSupportedPictureSizes();
		// parameters.setPictureSize(2592, 1456);
		ArrayList<Size> list = (ArrayList<Size>) parameters.getSupportedPictureSizes();
		/*for (int i = 0; i < list.size(); i++) {
			Log.e(TAG,
					"height = " + list.get(i).height + " width = "
							+ list.get(i).width);
		}*/
		// 设置预览大小
		// parameters.setPreviewSize(width, height);
		parameters.setPreviewSize(list.get(3).width, list.get(3).height);
		// 设置图片分辩率
		parameters.setPictureSize(list.get(3).width, list.get(3).height);

		// 给相机对象设置刚才设定的参数
		mCamera.setParameters(parameters);
		// �?��预览
		mCamera.startPreview();
		// ReturnSurfaceChangeHolde(holder);
	}

	public SurfaceHolder ReturnSurfaceChangeHolde() {
		SurfaceHolder holder = null;
		holder = mHolder;
		return holder;
	}

	public void surfaceCreated(SurfaceHolder holder) {
		if (mCamera == null) {
			mCamera = Camera.open();
		}
		try {
			mCamera.setPreviewDisplay(holder);
		} catch (IOException e) {
			mCamera.release();
			mCamera = null;
			e.printStackTrace();
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// 释放资源
		/** 如果系统是4.2的话，要打开下面 **/
		boolean m_bAndroid42 = true;
		if (m_bAndroid42) {
			if (mCamera != null) {
				mCamera.release();
				mCamera = null;
			}
		}
	}
}
