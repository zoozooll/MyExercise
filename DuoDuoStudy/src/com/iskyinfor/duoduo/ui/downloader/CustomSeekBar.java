package com.iskyinfor.duoduo.ui.downloader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.SeekBar;
import com.iskyinfor.duoduo.R;

public class CustomSeekBar extends SeekBar {

	private Drawable mThumb;
	private int width = 0;

	public CustomSeekBar(Context context, AttributeSet attrs, int defStyle) 
	{
		super(context, attrs, defStyle);
	}

	public CustomSeekBar(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public CustomSeekBar(Context context)
	{
		super(context);
	}

	@Override
	protected synchronized void onDraw(Canvas canvas)
	{
		super.onDraw(canvas);
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		paint.setTextSize(30);
		int coordinateX = 0;
		canvas.drawText("1", coordinateX, 30, paint);
		coordinateX = coordinateX + width / 4;
		canvas.drawText("2", coordinateX, 30, paint);
		coordinateX = coordinateX + width / 4;
		canvas.drawText("3", coordinateX - 5, 30, paint);
		coordinateX = coordinateX + width / 4;
		canvas.drawText("4", coordinateX - 10, 30, paint);
		coordinateX = coordinateX + width / 4;
		canvas.drawText("5", coordinateX - 20, 30, paint);
	}

	@Override
	public void setThumb(Drawable thumb) 
	{
		mThumb = getResources().getDrawable(R.drawable.drag);
		super.setThumb(mThumb);
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,int bottom)
	{
		super.onLayout(changed, left, top, right, bottom);
		width = right - left;
//		Log.i("liu", "onLayout====:" + changed);
//		Log.i("liu", "left====:" + left);
//		Log.i("liu", "top====:" + top);
//		Log.i("liu", "right====:" + right);
//		Log.i("liu", "bottom====:" + bottom);
//		Log.i("liu", "bottom-top====:" + (bottom - top));
//		Log.i("liu", "right-left====:" + (right - left));
	}
}