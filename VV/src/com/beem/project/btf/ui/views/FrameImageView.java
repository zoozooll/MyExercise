package com.beem.project.btf.ui.views;

import com.beem.project.btf.utils.DimenUtils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ImageView;

/** 带边框的imageview */
public class FrameImageView extends ImageView {
	private int strokewidth;

	public FrameImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		strokewidth = DimenUtils.dip2px(context, 2);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		Rect rec = canvas.getClipBounds();
		rec.bottom--;
		rec.right--;
		Paint paint = new Paint();
		paint.setColor(Color.parseColor("#19ADF1"));
		paint.setStrokeWidth(strokewidth);
		paint.setStyle(Paint.Style.STROKE);
		canvas.drawRect(rec, paint);
	}
}
