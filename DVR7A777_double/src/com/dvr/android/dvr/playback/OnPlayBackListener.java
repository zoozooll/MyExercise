/**
 * 
 */
package com.dvr.android.dvr.playback;

/**
 * 回放错误回调<BR>
 * [功能详细描述]
 * @author sunshine
 * @version [yecon Android_Platform, 23 Feb 2012] 
 */
public interface OnPlayBackListener
{
    public static final int PLAY = 1;
    public static final int PAUSE = 2;
    public static final int STOP = 3;
    public static final int COMPLETE = 4;
    public static final int ONE_SHOT = 5;
    public void onError(int code , String info);
    public void onStateChange(int state);
}
