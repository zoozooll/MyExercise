package com.tcl.manager.miniapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.tcl.mie.manager.R;

/** 
 * @Description: 白色小三角形，针对miniapp目标指向作用
 * @author wenchao.zhang 
 * @date 2014年12月18日 下午1:33:08 
 * @copyright TCL-MIE
 */

public class TrigonView extends View {

	private int layout_width = 0;
	private int layout_height = 0;

	public TrigonView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		layout_width = w;
		layout_height = h;
		invalidate();
	}
	

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		canvas.drawColor(Color.TRANSPARENT);
		Paint trigonPaint = new Paint();
		//去锯齿
		trigonPaint.setAntiAlias(true);
		trigonPaint.setColor(getResources().getColor(R.color.miniapp_bg_dialog));
		Path trigonPath = new Path();
		trigonPath.moveTo(0, layout_height);
		trigonPath.lineTo(layout_width, layout_height);
		trigonPath.lineTo(layout_width/2.0f, 0);
		trigonPath.close();
		canvas.drawPath(trigonPath, trigonPaint);
	}
}
