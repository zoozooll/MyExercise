package com.dvr.android.dvr;

import android.media.MediaRecorder;
import android.util.Log;

import com.dvr.android.dvr.msetting.SettingBean;

/**
 * @author tony
 * @version 创建时间：2015-6-3 上午9:55:40 录制工具类
 */
public class MediaRecorderUtils {

    private static final String TAG = "MediaRecorderUtils";

    private static MediaRecorderUtils mediaRecorderUtils;
    private MediaRecorder mediaRecorder;

    private MediaRecorderUtils() {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        setMediaRecorderArg();
    }

    public static synchronized MediaRecorderUtils getMediaRecorderUtilsInstance() {
        if (mediaRecorderUtils == null) {
            mediaRecorderUtils = new MediaRecorderUtils();
        }
        return mediaRecorderUtils;
    }

    // 设置MediaRecorder参数
    private void setMediaRecorderArg() {
        mediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
        // 录像保存格式 ,默认为mp4
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// THREE_GPP);
        // 设置录像视频编码方式 ，默认为MPEG_4_SP
        mediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);// H263);
        // 录像帧率
        mediaRecorder.setVideoFrameRate(15);
        // 设置音频编码方式,默认为AMR_NB
        if (SettingBean.isRecordSound) {
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_WB);
            mediaRecorder.setAudioEncodingBitRate(Config.VIDEO_PROFILES[SettingBean.mVideoQuality][0]);
        }
        // 设置录像画面质量:audioBitRate、videoBitRate、videoFrameHeight、videoFrameWidth
        mediaRecorder.setVideoEncodingBitRate(Config.VIDEO_PROFILES[SettingBean.mVideoQuality][7]);
        mediaRecorder.setVideoSize(Config.VIDEO_PROFILES[SettingBean.mVideoQuality][11],
                Config.VIDEO_PROFILES[SettingBean.mVideoQuality][9]);

        // 录像帧率
        try {
            // mediaRecorder.setCaptureRate(videoFrameRate*2);
            mediaRecorder.setVideoFrameRate(Config.VIDEO_PROFILES[SettingBean.mVideoQuality][10]);
            Log.e(TAG, "***setVideoFrameRate success ***");
        } catch (Exception e) {
            Log.e(TAG, "***setVideoFrameRate faile ***");
            e.printStackTrace();
        }
    }

    /**
     * 开始录制
     * */
    public void startRecorder(String path) {
        if (mediaRecorder == null) {
            mediaRecorder = new MediaRecorder();
        }
        // if (mCamera != null) {
        // mCamera.unlock();
        // mMediaRecorder.setCamera(mCamera);
        // }
        mediaRecorder.setOutputFile(path);
        try {
            mediaRecorder.prepare(); // 预期准备
            mediaRecorder.start();// 开始录制
            // mState = CURRENT_STATE_RECODING;
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "mMediaRecorder start fail!");
            e.printStackTrace();
        }
    }

    /**
     * 停止录制
     * */
    public void stopRecorder() {
        if (mediaRecorder == null) {
            return;
        }

        try {
            mediaRecorder.stop();
            mediaRecorder.reset();
            // usbCameraSurfaceView.BackCameraStopRec();
        } catch (RuntimeException e) {
            Log.e(TAG, "***mMediaRecorder.stop error.***");
            e.printStackTrace();
        }
        // new StopFileOption().execute("");
    }

//    class StopFileOption extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... arg0) {
//            boolean bCanDelete;
//            File mFile = null;
//            if (mVideoLock || mHitSave) {
//                bCanDelete = false;
//                mVideoFile1 = createLockFile(mVideoFile1);
//                if (SettingBean.mOpenBackCamera) {
//                    mFile = createLockFileBack(mVideoFileBack);
//                }
//            } else {
//                bCanDelete = true;
//            }
//
//            // SD卡是否移除
//            if (!mDeviceUnmount) {
//                File tempFile = mVideoFile;
//                tempFile.renameTo(mVideoFile1);
//                saveVideo(bCanDelete);
//                GpsProvider.getGpsProvider(DVRBackService.this).stopRecoder();
//
//                DbUtils db = DbUtils.create(DVRBackService.this, "dvr.db");
//                db.configAllowTransaction(true);
//                db.configDebug(true);
//                try {
//                    VideoFile file = new VideoFile();
//                    file.setName(mFileName + ".mp4");
//                    file.setPath(mVideoFile1.getAbsolutePath());
//                    db.save(file);
//                    file = new VideoFile();
//                    file.setName(mFileName + ".mp4");
//                    file.setPath(mVideoFileBack.getAbsolutePath());
//                    db.save(file);
//                } catch (DbException e) {
//                    e.printStackTrace();
//                }
//                if (SettingBean.mOpenBackCamera) {
//                    if (mFile != null) {
//                        Log.e("tag_tony",
//                                ">>>-2--" + mVideoFileBack.getAbsoluteFile() + "---" + mFile.getAbsolutePath());
//                        mVideoFileBack.renameTo(mFile);
//                        saveVideo_back(bCanDelete, mFile);
//                    } else {
//                        saveVideo_back(bCanDelete, mVideoFileBack);
//                    }
//                }
//            }
//            return null;
//        }
//    }

}
