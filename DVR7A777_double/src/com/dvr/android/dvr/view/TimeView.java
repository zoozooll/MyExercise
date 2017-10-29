package com.dvr.android.dvr.view;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.widget.TextView;

import java.util.Calendar;

/**
 * 时间显示控件
 * @author hanJ
 */
public class TimeView extends TextView{
    
	private final static String m12 = "h:mm:ss";
    private final static String m24 = "k:mm:ss";
    
    private FormatChangeObserver mFormatChangeObserver;

    Calendar mCalendar;
    
    private Runnable mTicker;
    
    private Handler mHandler;

    String mFormat;

    private boolean mTickerStopped = false;

    public TimeView(Context context) {
        super(context);
        initClock(context);
    }

    public TimeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initClock(context);
    }

    private void initClock(Context context) {	
    	if (mCalendar == null) {
            mCalendar = Calendar.getInstance();
        }
    	mFormatChangeObserver = new FormatChangeObserver();
        getContext().getContentResolver().registerContentObserver(
                Settings.System.CONTENT_URI, true, mFormatChangeObserver);
        setFormat();
    }

    @Override
    protected void onAttachedToWindow() {
        mTickerStopped = false;
        super.onAttachedToWindow();
        mHandler = new Handler();

        mTicker = new Runnable() {
                public void run() {
                    if (mTickerStopped) return;
                    mCalendar.setTimeInMillis(System.currentTimeMillis());
                    //setText(DateFormat.format(mFormat, mCalendar));
                    String timeStr = (String) DateFormat.format(mFormat, mCalendar);
                    setText(timeStr.substring(0, timeStr.length()-3));
                    //invalidate();
                    mHandler.postDelayed(mTicker, 1000*60);
                    /*long now = SystemClock.uptimeMillis();
                    long next = now + (1000 - now % 1000);
                    mHandler.postAtTime(mTicker, next);*/
                }
            };
        mTicker.run();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mTickerStopped = true;
    }

    /**
     * 获取系统是否�?4小时�?
     */
    private boolean get24HourMode() {
        return android.text.format.DateFormat.is24HourFormat(getContext());
    }

    private void setFormat() {
        if (get24HourMode()) {
            mFormat = m24;
        } else {
            mFormat = m12;
        }
    }

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            setFormat();
        }
    }
}
