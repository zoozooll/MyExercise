package com.beem.project.btf.ui.camera;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Face;
import android.hardware.Camera.FaceDetectionListener;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.SystemClock;
import android.util.Log;

import com.beem.project.btf.utils.CamParaUtil;
import com.beem.project.btf.utils.DimenUtils;

public class CameraController {
	private static final String TAG = "CameraController";
	private Camera camera;
	private long beginFaceDetectedTime;

	/**
	 * @param camera
	 */
	public CameraController(Camera camera) {
		super();
		this.camera = camera;
	}
	public void setCameraParameters(Context context) {
		if (camera != null) {
			Camera.Parameters parameters = camera.getParameters();
			/*
			 * 取得相机所支持多少图片大小的个数,不这么干会报setParameters failed错误 不能随意设置预览图像大小和所获图片大小
			 */
			// 获取屏幕宽高比,用于适配屏幕分辨率
			Point p = DimenUtils.getScreenMetrics(context);
			float previewRate = DimenUtils.getScreenRate(context);
			Size previewSize = CamParaUtil.getInstance().getPropPreviewSize(
					parameters.getSupportedPreviewSizes(), previewRate,
					DimenUtils.getScreenMetrics(context).x);
			// 设置预览尺寸
			parameters.setPreviewSize(previewSize.width, previewSize.height);
			Size pictureSize = CamParaUtil.getInstance().getPropPictureSize(
					parameters.getSupportedPictureSizes(), previewRate,
					DimenUtils.getScreenMetrics(context).x);
			// 设置图片尺寸
			parameters.setPictureSize(pictureSize.width, pictureSize.height);
			parameters.setPictureFormat(PixelFormat.JPEG); // 指定图片为JPEG图片
			parameters.set("jpeg-quality", 90); // 设置图片的质量
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				// 设置对焦模式
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			}
			//			parameters.setExposureCompensation(Parameters.);
			parameters.setWhiteBalance(Parameters.WHITE_BALANCE_AUTO);
			//4.0以上的才支持人脸检测
			if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
				//设定脸部识别
				camera.setFaceDetectionListener(new FaceDetectionListener() {
					@Override
					public void onFaceDetection(Face[] faces, Camera camera) {
						long now = SystemClock.elapsedRealtime();
						if (beginFaceDetectedTime == 0) {
							beginFaceDetectedTime = now;
						}
						if (beginFaceDetectedTime - now > 3000) {
							// 超过3秒才才是做定焦操作。
							beginFocusByFaces(faces);
						}
					}
				});
			}
			camera.setParameters(parameters); // 重新设置相机参数
			/*Camera.Parameters mParams = camera.getParameters(); // 重新get一次
			Log.i(TAG,
					"最终设置:PreviewSize--With = " + mParams.getPreviewSize().width + "Height = "
							+ mParams.getPreviewSize().height);
			Log.i(TAG,
					"最终设置:PictureSize--With = " + mParams.getPictureSize().width + "Height = "
							+ mParams.getPictureSize().height);*/
		}
	}
	public boolean setFocusAndMeteringArea(List<Camera.Area> camera_areas) {
		Camera.Parameters parameters = camera.getParameters();
		String focus_mode = parameters.getFocusMode();
		// getFocusMode() is documented as never returning null, however I've had null pointer exceptions reported in Google Play
		if (parameters.getMaxNumFocusAreas() != 0
				&& focus_mode != null
				&& (focus_mode.equals(Camera.Parameters.FOCUS_MODE_AUTO)
						|| focus_mode
								.equals(Camera.Parameters.FOCUS_MODE_MACRO)
						|| focus_mode
								.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE) || focus_mode
							.equals(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO))) {
			parameters.setFocusAreas(camera_areas);
			// also set metering areas
			if (parameters.getMaxNumMeteringAreas() == 0) {
				Log.w(TAG, "metering areas not supported");
			} else {
				parameters.setMeteringAreas(camera_areas);
			}
			camera.setParameters(parameters);
			return true;
		} else if (parameters.getMaxNumMeteringAreas() != 0) {
			parameters.setMeteringAreas(camera_areas);
			camera.setParameters(parameters);
		}
		return false;
	}
	private void beginFocusByFaces(final Face[] faces) {
		new Thread("beginFocusByFaces") {
			@Override
			public void run() {
				List<Camera.Area> camera_areas = getAreasFromFaces(faces);
				if (camera_areas != null)
					setFocusAndMeteringArea(camera_areas);
				beginFaceDetectedTime = SystemClock.currentThreadTimeMillis();
			}
		}.start();
	}
	private List<Camera.Area> getAreasFromFaces(Face[] faces) {
		List<Camera.Area> camera_areas = new ArrayList<Camera.Area>();
		if (faces == null) {
			return camera_areas;
		}
		for (Face f : faces) {
			camera_areas.add(faceToArea(f));
		}
		return camera_areas;
	}
	private Camera.Area faceToArea(Face face) {
		return new Camera.Area(face.rect, 1000);
	}
}
