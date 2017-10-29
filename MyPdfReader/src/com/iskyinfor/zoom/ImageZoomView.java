/*
 * Copyright (c) 2010, Sony Ericsson Mobile Communication AB. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 *    * Redistributions of source code must retain the above copyright notice, this 
 *      list of conditions and the following disclaimer.
 *    * Redistributions in binary form must reproduce the above copyright notice,
 *      this list of conditions and the following disclaimer in the documentation
 *      and/or other materials provided with the distribution.
 *    * Neither the name of the Sony Ericsson Mobile Communication AB nor the names
 *      of its contributors may be used to endorse or promote products derived from
 *      this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED 
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 * INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING,
 * BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 * OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.iskyinfor.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Observable;
import java.util.Observer;

/**
 * View capable of drawing an image at different zoom state levels
 */
public class ImageZoomView extends View implements Observer {

    /** Paint object used when drawing bitmap. */
    private final Paint mPaint = new Paint();

    /** Rectangle used (and re-used) for cropping source image. */
    private final Rect mRectSrc = new Rect();

    /** Rectangle used (and re-used) for specifying drawing area on canvas. */
    private final Rect mRectDst = new Rect();

    /** The bitmap that we're zooming in, and drawing on the screen. */
    private Bitmap mBitmap;

    /** Pre-calculated aspect quotient. */
    private float mAspectQuotient;

    /** State of the zoom. */
    private ZoomState mState;

    // Public methods

    /*
     * Constructor
     */
    
    
    public ImageZoomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ImageZoomView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public ImageZoomView(Context context) {
		super(context);
	}

	/**
     * Set image bitmap
     * 
     * @param bitmap The bitmap to view and zoom into
     */
    public void setImage(Bitmap bitmap) {
        mBitmap = bitmap;

        calculateAspectQuotient();

        invalidate();
    }

    /**
     * Set object holding the zoom state that should be used
     * 
     * @param state The zoom state
     */
    public void setZoomState(ZoomState state) {
        if (mState != null) {
            mState.deleteObserver(this);
        }

        mState = state;
        mState.addObserver(this);

        invalidate();
    }

    // Private methods

    private void calculateAspectQuotient() {
        if (mBitmap != null) {
            mAspectQuotient = (((float)mBitmap.getWidth()) / mBitmap.getHeight())
                    / (((float)getWidth()) / getHeight());
        }
    }

    // Superclass overrides

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mState != null) {
            final int viewWidth = getWidth();
            final int viewHeight = getHeight();
            final int bitmapWidth = mBitmap.getWidth();
            final int bitmapHeight = mBitmap.getHeight();
            //Log.i("aaron", "view:"+viewWidth+","+viewHeight);
            //Log.i("aaron", "bitmap:"+bitmapWidth +","+bitmapHeight);

            final float panX = mState.getPanX();
            final float panY = mState.getPanY();
            final float zoomX = mState.getZoomX(mAspectQuotient) * viewWidth / bitmapWidth;
            final float zoomY = mState.getZoomY(mAspectQuotient) * viewHeight / bitmapHeight;
            
           // Log.i("aaron", "pan:( "+panX+","+panY+")");
           // Log.i("aaron", "zoom:( "+zoomX+","+zoomY +")");

            // Setup source and destination rectangles
            mRectSrc.left = (int)(panX * bitmapWidth - viewWidth / (zoomX * 2));
            mRectSrc.top = (int)(panY * bitmapHeight - viewHeight / (zoomY * 2));
            mRectSrc.right = (int)(mRectSrc.left + viewWidth / zoomX);
            mRectSrc.bottom = (int)(mRectSrc.top + viewHeight / zoomY);
            mRectDst.left = getLeft();
            mRectDst.top = getTop();
            mRectDst.right = getRight();
            mRectDst.bottom = getBottom();
            
            //Log.i("aaron", "mRectSrc:( "+mRectSrc.left+","+mRectSrc.top+","+mRectSrc.right+","+mRectSrc.bottom+")");
           // Log.i("aaron", "mRectDst:( "+mRectDst.left+","+mRectDst.top+","+mRectDst.right+","+mRectDst.bottom+")");

            // Adjust source rectangle so that it fits within the source image.
            if (mRectSrc.left < 0) {
                mRectDst.left += -mRectSrc.left * zoomX;
                mRectSrc.left = 0;
            }
            if (mRectSrc.right > bitmapWidth) {
                mRectDst.right -= (mRectSrc.right - bitmapWidth) * zoomX;
                mRectSrc.right = bitmapWidth;
            }
            if (mRectSrc.top < 0) {
                mRectDst.top += -mRectSrc.top * zoomY;
                mRectSrc.top = 0;
            }
            if (mRectSrc.bottom > bitmapHeight) {
                mRectDst.bottom -= (mRectSrc.bottom - bitmapHeight) * zoomY;
                mRectSrc.bottom = bitmapHeight;
            }
            
           // Log.i("aaron", "mRectSrc2:( "+mRectSrc.left+","+mRectSrc.top+","+mRectSrc.right+","+mRectSrc.bottom+")");
            //Log.i("aaron", "mRectDst2:( "+mRectDst.left+","+mRectDst.top+","+mRectDst.right+","+mRectDst.bottom+")");
            //Log.i("aaron", "");
            canvas.drawBitmap(mBitmap, mRectSrc, mRectDst, mPaint);
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        calculateAspectQuotient();
    }

    // implements Observer
    public void update(Observable observable, Object data) {
        invalidate();
    }
    
    /**
     * 释放图片内存;
     * 
     * @Author Aaron Lee
     * @date 2011-7-21 下午04:13:14
     */
    public void clear(){
    	mBitmap.recycle();
    }

}
