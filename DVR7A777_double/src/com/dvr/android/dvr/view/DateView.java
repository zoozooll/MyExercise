package com.dvr.android.dvr.view;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 日期显示控件
 * 
 * @author hanJ
 */
public class DateView extends TextView {

    private Context mContext;

    private ContentObserver mFormatChangeObserver;

    private boolean mLive = true;

    private boolean mAttached;

    private final SimpleDateFormat FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    private final Handler mHandler = new Handler();

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mHandler.post(new Runnable() {
                public void run() {
                    updateDate();
                }
            });
        }
    };

    private class FormatChangeObserver extends ContentObserver {
        public FormatChangeObserver() {
            super(new Handler());
        }

        @Override
        public void onChange(boolean selfChange) {
            updateDate();
        }
    }

    public DateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (mAttached)
            return;
        mAttached = true;

        if (mLive) {
            IntentFilter filter = new IntentFilter();
            //filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);
            filter.addAction(Intent.ACTION_LOCALE_CHANGED);
            mContext.registerReceiver(mIntentReceiver, filter);
        }

        mFormatChangeObserver = new FormatChangeObserver();
        mContext.getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, mFormatChangeObserver);

        updateDate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!mAttached)
            return;
        mAttached = false;

        if (mLive) {
            mContext.unregisterReceiver(mIntentReceiver);
        }
        mContext.getContentResolver().unregisterContentObserver(mFormatChangeObserver);
    }

    private void updateDate() {
        setText(FORMAT.format(new Date()));
    }
}
