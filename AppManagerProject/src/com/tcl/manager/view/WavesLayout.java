package com.tcl.manager.view;

import com.tcl.mie.manager.R;

import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 放大，单色
 * 
 * @author jiaquan.huang
 * 
 */
public class WavesLayout extends RelativeLayout {
    private int duration = 550;
    AnimatorListener listener;

    public WavesLayout(Context context) {
        super(context);
        init(context);
    }

    public WavesLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        CircularView view = new CircularView(context);
        view.setBackground(context.getResources().getColor(R.color.main_good));
        this.addView(view, 0);
    }

    public void setAnimatorListener(AnimatorListener listener) {
        this.listener = listener;
    }

    int width = 0;
    int height = 0;

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        width = r - l;
        height = b - t;
        if (this.getChildCount() > 0) {
            this.getChildAt(0).layout(l, b, r, b);
        }
        start(listener);
    }

    private void start(AnimatorListener listener) {
        int childWidth = 0;
        int childHeight = 0;
        if (this.getChildCount() > 0) {
            View child = this.getChildAt(0);
            if (child instanceof CircularView == false) {
                return;
            }
            childWidth = child.getWidth();
            childHeight = child.getHeight();
            if (childWidth == 0) {
                ((CircularView) child).setWidth(1);
                childWidth = 1;
            }
            if (childHeight == 0) {
                ((CircularView) child).setHeight(1);
                childHeight = 1;
            }
            int maxFather = Math.max(width, height);
            int minChile = Math.min(childWidth, childHeight);
            int scaleXY = (int) ((maxFather * 2 + minChile * 2) / (minChile / 1.8f));
            ObjectAnimator animX = ObjectAnimator.ofFloat(child, "scaleX", scaleXY);
            ObjectAnimator animY = ObjectAnimator.ofFloat(child, "scaleY", scaleXY);
            AnimatorSet animSetXY = new AnimatorSet();
            animSetXY.playTogether(animX, animY);
            animSetXY.setDuration(duration);
            if (listener != null) {
                animSetXY.addListener(listener);
            }
            animSetXY.setStartDelay(100);
            animSetXY.start();
        }
    }
}
