package com.tcl.manager.view;

import com.tcl.mie.manager.R;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * 波动视图
 * 
 * @author jiaquan.huang
 * 
 */
public class RadarWaveView extends FrameLayout {
    Context mContext;
    /** 波动宽度 **/
    public int radarWidth = 100;
    /** 3个View波动间隙 **/
    int gapTime = 300;
    /** 动画间隙时间 **/
    int handerDelayTime = 3500;
    int what = 0;
    ImageView wave1;
    ImageView wave2;
    ImageView wave3;
    boolean isStart = false;
    /** 两段动画最少间隙 **/
    long animationTime = 1000;
    long previousTime = 0;

    Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            if (isStart) {
                long currentTime = System.currentTimeMillis();
                boolean timeOKToAnim = currentTime - previousTime > animationTime ? true : false;
                if (isLayout && timeOKToAnim == true) {
                    startAnimation();
                }
                handler.removeMessages(what);
                handler.sendEmptyMessageDelayed(what, handerDelayTime);
                isLayout = true;
            }
        };
    };

    public void setRadarWidth(int radarWidth) {
        this.radarWidth = radarWidth;
    }

    /**
     * @param context
     * @param attrs
     */
    public RadarWaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    /**
     * @param context
     */
    public RadarWaveView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    private void init() {
        wave1 = new ImageView(mContext);
        wave2 = new ImageView(mContext);
        wave3 = new ImageView(mContext);
        wave1.setImageResource(R.drawable.shape_score_circle_outside);
        wave2.setImageResource(R.drawable.shape_score_circle_outside);
        wave3.setImageResource(R.drawable.shape_score_circle_outside);
        this.addView(wave1);
        this.addView(wave2);
        this.addView(wave3);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childLeft = radarWidth / 2;
        int childTop = radarWidth / 2;
        int childRight = right - radarWidth / 2 - left;
        int childBottom = bottom - radarWidth / 2 - top;
        int childCount = getChildCount();
        f = (childRight - childLeft + radarWidth) * 1f / (childRight - childLeft);
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView != null) {
                childView.layout(childLeft, childTop, childRight, childBottom);
            }
        }
        isLayout = true;
    }

    /** 放大系数 **/
    float f = 0;

    public void startAnimation() {
        if (isStart == false) {
            return;
        }
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            reset(childView);
            if (childView != null) {
                setAnimation(childView, i * gapTime);
            }
        }
        previousTime = System.currentTimeMillis();
    }

    private void reset(View v) {
        if (v.getTag() != null) {
            ((ValueAnimator) v.getTag()).cancel();
        }
        v.setScaleX(0);
        v.setScaleY(0);
        v.setAlpha(0);
        v.setVisibility(View.VISIBLE);
    }

    private void setAnimation(final View v, long startDelay) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, f);
        valueAnimator.addUpdateListener(new AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fValue = (Float) animation.getAnimatedValue();
                float percent = animation.getAnimatedFraction();
                v.setScaleX(fValue);
                v.setScaleY(fValue);
                v.setAlpha(1f - percent);
            }
        });
        valueAnimator.setDuration(1200);
        valueAnimator.setTarget(v);
        v.setTag(valueAnimator);
        valueAnimator.setStartDelay(startDelay);
        valueAnimator.start();
    }

    public void start() {
        if (isStart) {
            return;
        }
        isStart = true;
        handler.sendEmptyMessage(what);
    };

    boolean isLayout = false;

    public void callLayout() {
        isLayout = false;
    }

    public void stop() {
        wave1.setVisibility(View.GONE);
        wave2.setVisibility(View.GONE);
        wave3.setVisibility(View.GONE);
        isStart = false;
    }
}
