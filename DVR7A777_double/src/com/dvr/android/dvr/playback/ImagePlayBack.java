/**
 * 
 */
package com.dvr.android.dvr.playback;

import java.io.File;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.dvr.android.dvr.R;

/**
 * 图片回放�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 15 Mar 2012]
 */
public class ImagePlayBack extends PlayBacker {
    /**
     * �?��隐藏的控�?视频播放如要隐藏图片
     */
    private int[] hideID = new int[] {R.id.playback_video_id, R.id.playback_seek_id, R.id.playback_play_id,
        R.id.playback_operatorbar };
    private ImageView mImageView = null;

    public ImagePlayBack(Context context){
        super(context);
    }

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
        mImageView = (ImageView) view;
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#start()
     */
    @Override
    public void start(){
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#pause()
     */
    @Override
    public void pause(){
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#seekTo(int)
     */
    @Override
    public void seekTo(int time){
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#currentTime()
     */
    @Override
    public int currentTime(){
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#replay()
     */
    @Override
    public void replay(){
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#getDuration()
     */
    @Override
    public int getDuration(){
        return 0;
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#stop()
     */
    @Override
    public void stop(){
    }

    /*
     * (non-Javadoc)
     * @see com.yecon.android.dvr.playback.PlayBacker#setSourceFile(java.lang.String)
     */
    @Override
    public void setSourceFile(String filePath){
        if (null == mImageView){
            Log.e("TAG", "you must call setDisplayView() first!");
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
        mImageView.setImageURI(Uri.fromFile(file));
        chageState(OnPlayBackListener.ONE_SHOT);
    }
}
