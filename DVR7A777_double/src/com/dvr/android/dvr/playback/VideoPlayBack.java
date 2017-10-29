/**
 * 
 */
package com.dvr.android.dvr.playback;

import java.io.File;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
//import android.widget.MediaController;
import android.widget.VideoView;
import android.media.MediaPlayer;
import com.dvr.android.dvr.R;

/**
 * 视频回放播放�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 23 Feb 2012]
 */
public class VideoPlayBack extends PlayBacker implements MediaPlayer.OnErrorListener ,
    MediaPlayer.OnCompletionListener {

    private static final String TAG = "VideoPlayBack";

    public VideoPlayBack(Context context){
        super(context);
    }

    /**
     * �?��隐藏的控�?视频播放如要隐藏图片
     */
    private int[] hideID = new int[] {R.id.playback_image_id };

    /**
     * 媒体控制�?     */
    //private MediaController mMediaController = null;

    /**
     * 视频播放界面
     */
    private VideoView mVideoView = null;

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#isViewShow(android.view.View)
     */
    @Override
    public boolean isViewShow(View view){
        for (int id : hideID){
            if (id == view.getId()){
                view.setVisibility(View.GONE);
                return false;
            }
        }
        return true;
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#setDisplayView(android.view.View)
     */
    @Override
    public void setDisplayView(View view){
        mVideoView = (VideoView) view;
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#start()
     */
    @Override
    public void start(){
        mVideoView.start();
        chageState(OnPlayBackListener.PLAY);
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#pause()
     */
    @Override
    public void pause(){
        mVideoView.pause();
        chageState(OnPlayBackListener.PAUSE);
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#setSourceFile(java.lang.String)
     */
    @Override
    public void setSourceFile(String filePath){
        if (null == mVideoView){
            Log.e(TAG, "you must call setDisplayView() first!");
            return;
        }

        File file = new File(filePath);
        // 如果文件不存�?直接�?        
        if (!file.exists()){
            isReady = false;
            if (null != mListener){
                mListener.onError(FILE_NOT_EXIST, mContext.getString(R.string.file_not_exist));
            }
            return;
        }
        mVideoView.setVideoURI(Uri.fromFile(file));
        mVideoView.setOnCompletionListener(this);
        mVideoView.setOnErrorListener(this);
        isReady = true;
    }

    public void onCompletion(MediaPlayer mp){
        chageState(OnPlayBackListener.COMPLETE);
    }

    public boolean onError(MediaPlayer mp, int what, int extra){
        chageState(OnPlayBackListener.STOP);
        return false;
    }

    @Override
    public void replay(){
        Log.d(TAG, "replay");
        mVideoView.start();
        // mVideoView.
        chageState(OnPlayBackListener.PLAY);
    }

    @Override
    public void seekTo(int time){
        mVideoView.seekTo(time);
    }

    @Override
    public int currentTime(){
        return mVideoView.getCurrentPosition();
    }

    @Override
    public int getDuration(){
        return mVideoView.getDuration();
    }

    @Override
    public void stop(){
        mVideoView.stopPlayback();
    }
}
