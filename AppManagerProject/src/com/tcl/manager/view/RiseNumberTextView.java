package com.tcl.manager.view;

import java.text.DecimalFormat;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * @Description: 自动增长分数
 * @author wenchao.zhang
 * @date 2015年01月09日 下午3:35:25
 * @copyright TCL-MIE
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RiseNumberTextView extends TextView {

    private static final int STOPPED = 0;

    private static final int RUNNING = 1;

    private int mPlayingState = STOPPED;

    private float number;

    private float fromNumber;

    private long duration = 1500;
    /**
     * 1.int 2.float
     */
    private int numberType = 2;

    private DecimalFormat fnum;

    private EndListener mEndListener = null;

    final static int[] sizeTable = { 9, 99, 999, 9999, 99999, 999999, 9999999, 99999999, 999999999, Integer.MAX_VALUE };

    public RiseNumberTextView(Context context) {
        super(context);
    }

    public RiseNumberTextView(Context context, AttributeSet attr) {
        super(context, attr);
    }

    public RiseNumberTextView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
    }

    public interface EndListener {
        public void onEndFinish();
    }

    public boolean isRunning() {
        return (mPlayingState == RUNNING);
    }

    private void runFloat() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(fromNumber, number);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(fnum.format(Float.parseFloat(valueAnimator.getAnimatedValue().toString())));
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED;
                    if (mEndListener != null)
                        mEndListener.onEndFinish();
                }
            }

        });
        valueAnimator.start();
    }

    private int partDuration = 300;

    private void runInt() {
        int tempNum = (int) Math.abs(number - fromNumber);
        long tempDuration = tempNum * partDuration;
        if (Math.abs(number - fromNumber) < 1) {
            mPlayingState = STOPPED;
            if (mEndListener != null)
                mEndListener.onEndFinish();
        }
        if (tempDuration > duration) {
            tempDuration = duration;
        }
        ValueAnimator valueAnimator = ValueAnimator.ofInt((int) fromNumber, (int) number);
        valueAnimator.setDuration(tempDuration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @SuppressLint("NewApi")
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                setText(valueAnimator.getAnimatedValue().toString());
                if (valueAnimator.getAnimatedFraction() >= 1) {
                    mPlayingState = STOPPED;
                    if (mEndListener != null)
                        mEndListener.onEndFinish();
                }
            }
        });
        valueAnimator.start();
    }

    static int sizeOfInt(int x) {
        for (int i = 0;; i++)
            if (x <= sizeTable[i])
                return i + 1;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        fnum = new DecimalFormat("##0.00");
    }

    public void start() {

        if (!isRunning()) {
            mPlayingState = RUNNING;
            if (numberType == 1)
                runInt();
            else
                runFloat();
        }
    }

    public RiseNumberTextView withNumber(float number) {

        this.number = number;
        numberType = 2;
        if (number > 1000) {
            fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
        } else {
            fromNumber = number / 2;
        }

        return this;
    }

    public RiseNumberTextView withNumber(int number) {
        this.number = number;
        numberType = 1;
        if (number > 1000) {
            fromNumber = number - (float) Math.pow(10, sizeOfInt((int) number) - 2);
        } else {
            try {
                fromNumber = Integer.parseInt(getText().toString());
            } catch (NumberFormatException e) {
                fromNumber = number / 2;
            }

        }

        return this;

    }

    public RiseNumberTextView setDuration(long duration) {
        this.duration = duration;
        return this;
    }

    public void setOnEnd(EndListener callback) {
        mEndListener = callback;
    }

}
