package com.tcl.manager.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 雷达波位置控制布局
 * 
 * @author jiaquan.huang
 * 
 */
public class RadarViewLayout extends ViewGroup {
    Context mContext;

    public RadarViewLayout(Context context) {
        super(context);
        mContext = context;
    }

    public RadarViewLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init(mContext);
    }

    RadarWaveView radarView;

    private void init(Context context) {
        radarView = new RadarWaveView(context);
        this.addView(radarView);
    }

    int left = 0;
    int top = 0;
    int right = 0;
    int bottom = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount > 0) {
            View child = getChildAt(0);
            int tempLeft = left - l;
            int tempTop = top - t;
            int tempRight = right - l;
            int tempBottom = bottom - t;
            if (child instanceof RadarWaveView) {
                RadarWaveView radarWaveView = (RadarWaveView) child;
                radarWaveView.layout(tempLeft - radarWaveView.radarWidth / 2, tempTop - radarWaveView.radarWidth / 2, tempRight + radarWaveView.radarWidth / 2, tempBottom + radarWaveView.radarWidth
                        / 2);
            }
        }
    }

    public void setLocation(int l, int t, int r, int b, int radarSize) {
        left = l;
        top = t;
        right = r;
        bottom = b;
        radarView.radarWidth = radarSize;
        radarView.callLayout();
        this.requestLayout();
    }

    public void start() {
        radarView.start();
    }

    public void end() {
        radarView.stop();
    }
}
