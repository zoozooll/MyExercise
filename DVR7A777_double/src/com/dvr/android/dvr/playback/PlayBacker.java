/**
 * 
 */
package com.dvr.android.dvr.playback;

import android.content.Context;
import android.view.View;

/**
 * 图片与视频回放的父类<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 23 Feb 2012]
 */
public abstract class PlayBacker {
    
    /**
     * 视频回放,传�?路径key
     */
    public static final String PLAYBACK_KEY_VALUE_FILEPATH = "FILE_PATH";
    
    /**
     * 视频回放,类型，图片或视频 key value
     */
    public static final String PLAYBACK_KEY_VALUE_FILETYPE = "FILE_TYPE";
    
    /**
     * 视频回放的类�?视频
     */
    public static final int TYPE_VIDEO = 0;
    
    /**
     * 视频回放的类�?图片
     */
    public static final int TYPE_IMAGE = 1;
    
    /**
     * 视频回放的类�?图片
     */
    public static final int TYPE_UNDEFINE = -1;
    
    /**
     * 文件不存�?     */
    public static final int FILE_NOT_EXIST = 1;
    
    protected Context mContext = null;
    
    /**
     *播放状�?
     */
    protected int playerState = OnPlayBackListener.STOP;

    /**
     * 是否准备完毕
     */
    protected boolean isReady = false;
    
    /**
     * 错误监听
     */
    protected OnPlayBackListener mListener = null;

    public int getPlayerState(){
        return playerState;
    }

    public void setPlaState(int state){
        playerState= state;
    }

    /**
     * 构�?
     * 
     * @param context
     */
    public PlayBacker(Context context){
        mContext = context;
    }
    
    /**
     * 设置监听
     * @param listener
     */
    public void setListener(OnPlayBackListener listener){
        mListener = listener;
    }

    /**
     * 由子类决定哪些界面该隐藏
     * 
     * @param id
     * @return
     */
    public abstract boolean isViewShow(View view);

    /**
     * 放入显示的VIEW
     * 
     * @param view
     */
    public abstract void setDisplayView(View view);

    /**
     * �?��
     */
    public abstract void start();

    /**
     * 暂停
     */
    public abstract void pause();
    
    /**
     * 跳到指定时间
     * @param time
     */
    public abstract void seekTo(int time);
    
    public abstract int currentTime();
    
    /**
     * 重新�?��播放
     */
    public abstract void replay();
    
    public abstract int getDuration();

    /**
     * 是否准备妥当
     * 
     * @return true准备好了 false 没有准备�?     */
    public boolean prepare()
    {
        return isReady;
    }
    
    public abstract void stop();

    /**
     * 设置播放文件路径
     * 
     * @param filePath 文件路径
     */
    public abstract void setSourceFile(String filePath);
    
    /**
     * 状�?改变
     * @param state
     */
    public void chageState(int state){
        if (null != mListener && playerState != state){
            playerState = state;
            mListener.onStateChange(state);
        }
        else {
            playerState = state;
        }
    }
}
