package com.mogoo.market.widget;

import com.mogoo.market.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Button;

public class IrregularButton extends Button {
    private Context mContext;
    private int mRangeLeft;
    private int mRangeTop;
    private int mRangeRight;
    private int mRangeBottom;
    
	public IrregularButton(Context context) {
		super(context);
        mContext = context;
	}
	
	public IrregularButton(Context context, AttributeSet attrs) {
		super(context, attrs);
        mContext = context;
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.IrregularButton, 0, 0);
        mRangeLeft = a.getDimensionPixelSize(R.styleable.IrregularButton_clickRangeLeft, 0);
        mRangeTop = a.getDimensionPixelSize(R.styleable.IrregularButton_clickRangeTop, 0);
        mRangeRight = a.getDimensionPixelSize(R.styleable.IrregularButton_clickRangeRight, 0);
        mRangeBottom = a.getDimensionPixelSize(R.styleable.IrregularButton_clickRangeBottom, 0);
        a.recycle();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int x;
		int y;
		if(event.getAction() == MotionEvent.ACTION_DOWN) {
			x = (int) event.getX();
			y = (int) event.getY();

			if(!(mRangeLeft < x && x < mRangeRight && mRangeTop < y && y < mRangeBottom)) {
				return false;
			}
		}
		return super.onTouchEvent(event);
	}
}
