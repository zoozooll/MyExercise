package com.oregonscientific.meep.communicator.view.conversation;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.oregonscientific.meep.communicator.R;

/**
 * A class for Message Box of style used by current user
 */
public class MessageBoxStyle2 extends MessageBox {
	
	private Drawable topDrawable, middleDrawable, bottomDrawable;
	private RectF topRect, contentRect, bottomRect;
	
	public MessageBoxStyle2(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MessageBox);
		topDrawable = typedArray.getDrawable(R.styleable.MessageBox_topBackground);
		middleDrawable = typedArray.getDrawable(R.styleable.MessageBox_contentBackground);
		bottomDrawable = typedArray.getDrawable(R.styleable.MessageBox_bottomBackground);
		typedArray.recycle();
		
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
		setWillNotDraw(false);
		if (topDrawable != null) {
			top = ((BitmapDrawable) topDrawable).getBitmap();
			topDrawable = null;
		}
		
		if (middleDrawable != null) {
			middle = ((BitmapDrawable) middleDrawable).getBitmap();
			middleDrawable = null;
		}
		
		if (bottomDrawable != null) {
			bottom = ((BitmapDrawable) bottomDrawable).getBitmap();
			bottomDrawable = null;
		}
	}
	
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawBackground(canvas);
	}
	
	@Override
	protected void drawBackground(Canvas canvas) {
		if (top != null && middle != null && bottom != null) {
			
			int width = getWidth();
			int height = getHeight();
			
			// draw top area of message box
			topRect = new RectF(0, 0, width, top.getHeight());
			canvas.drawBitmap(top, null, topRect, null);
			
			// draw the content area
			contentRect = new RectF(13, top.getHeight(), width - 45, height - bottom.getHeight());
			canvas.drawBitmap(middle, null, contentRect, null);
			
			// draw the bottom area
			bottomRect = new RectF(10, height - bottom.getHeight(), width - 42, height);
			canvas.drawBitmap(bottom, null, bottomRect, null);
		}
	}
	
}
