package com.beem.project.btf.ui.views;

import java.io.File;

import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

/**
 * 录音线程
 * @author zhenggen xie
 */
public class MediaRecordHandler implements Runnable {
	private String savePath = null;
	private MediaRecorder mRecorder = null;
	private long recordTime = 0;
	private long startTime = 0;
	private long endTime = 0;
	private long maxVolumeStart = 0;
	private long maxVolumeEnd = 0;
	private volatile boolean isRecording;
	private Handler mhandler;
	private String fileName;

	public MediaRecordHandler(String fileName, String savePath, Handler mHandler) {
		super();
		this.fileName = fileName;
		this.savePath = savePath;
		this.mhandler = mHandler;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		start(savePath);
		recordTime = 0;
		startTime = System.currentTimeMillis();
		maxVolumeStart = System.currentTimeMillis();
		while (this.isRecording) {
			endTime = System.currentTimeMillis();
			recordTime = ((endTime - startTime) / 1000);
			if (recordTime >= HandlerConstant.MAX_SOUND_RECORD_TIME) {
				// 释放资源
				stop();
				// 时间超时发送信息
				sendAudioMessage(HandlerConstant.RECORD_AUDIO_TOO_LONG);
				break;
			}
			maxVolumeEnd = System.currentTimeMillis();
			getAmplitude();
		}
	}
	// 开始录音函数
	private void start(String name) {
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)
				&& !new File(name).exists()) {
			return;
		}
		if (mRecorder == null) {
			mRecorder = new MediaRecorder();
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			mRecorder.setOutputFile(name);
			try {
				mRecorder.prepare();
				mRecorder.start();
			} catch (Exception e) {
				Log.i("MediaRecordHandler",
						e.getStackTrace() + "" + e.getMessage());
			}
		}
	}
	public void setRecording(boolean isRec) {
		this.isRecording = isRec;
	}
	// 停止录音
	public void stop() {
		if (mRecorder != null) {
			try {
				mRecorder.setOnErrorListener(null);
				mRecorder.setPreviewDisplay(null);
				mRecorder.stop();
			} catch (RuntimeException e) {
				File myRecAudioFile = new File(savePath);
				if (myRecAudioFile.exists()) {
					// you must delete the outputfile when the recorder stop
					// failed.
					myRecAudioFile.delete();
					//LogUtils.e("record failed,del the record file:" + savePath);
				}
				e.printStackTrace();
			} finally {
				if (mRecorder != null) {
					mRecorder.release();
				}
				mRecorder = null;
			}
		}
	}
	// 暂停录音
	public void pause() {
		if (mRecorder != null) {
			mRecorder.stop();
		}
	}
	public void start() {
		if (mRecorder != null) {
			mRecorder.start();
		}
	}
	// 获取录音时间
	public long getRecordTime() {
		return recordTime;
	}
	// 获取音量值
	public void getAmplitude() {
		if (mRecorder != null) {
			if (maxVolumeEnd - maxVolumeStart < 100) {
				return;
			}
			maxVolumeStart = maxVolumeEnd;
			float max = mRecorder.getMaxAmplitude() / 650.0f;// 分为50个区段getMaxAmplitude返回值最大为32767
			// Log.i("MediaRecordHandler", "max" + max + "");
			Message Msg = new Message();
			Msg.what = HandlerConstant.RECEIVE_MAX_VOLUME;
			float[] msg = { max, getRecordTime() };
			Msg.obj = msg;
			mhandler.sendMessage(Msg);
		}
	}
	public void sendAudioMessage(int sendType) {
		Message msg = mhandler.obtainMessage();
		msg.what = sendType;
		Bundle bundle = new Bundle();
		bundle.putString("savePath", savePath);
		bundle.putString("fileName", fileName);
		bundle.putInt("recordTime", (int) getRecordTime());
		msg.setData(bundle);
		mhandler.sendMessage(msg);
	}
}
