package com.oregonscientific.meep.meepphoto;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

	
public class DeactivableViewPager extends ViewPager {

    private boolean activated = true;

    public DeactivableViewPager(Context context) {
        super(context);
    }

    public DeactivableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void activate() {
        activated = true;
    }

    public void deactivate() {
        activated = false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
    	if (activated) {
    		try {
                return super.onInterceptTouchEvent(event);
            } catch (Exception e) {
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
//    	Log.e("onTouchEvent","the f"+event.toString());
        try {
            return super.onTouchEvent(event);
        } catch (Exception e) {
            return true;
        }
    }
}