package com.tcl.manager.view;

import com.tcl.framework.log.NLog;
import com.tcl.mie.manager.R;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.tcl.mie.manager.R;

/**
 * @Description:白色渐变圆环
 * @author wenchao.zhang
 * @date 2014年12月18日 下午6:55:50
 * @copyright TCL-MIE
 */

public class WhiteShadeAnnulus extends View {
	// 尺寸(默认)
	private int layout_height = 0;
	private int layout_width = 0;
	private int barWidth = 0;
	private float fullRadius = 0;
	// padding(默认)
	private int paddingTop = 0;
	private int paddingBottom = 0;
	private int paddingLeft = 0;
	private int paddingRight = 0;
	private int rotate;
	// 画笔
	private Paint barPaint = new Paint();
	private Matrix matrix = new Matrix();
	private Shader s;
	private ValueAnimator anim;

    // 矩形坐标
    private RectF circleBounds = new RectF();
    private RectF barBounds = new RectF();
    // 渐变色
    private int[] mRectColors = new int[] { Color.TRANSPARENT, Color.WHITE };// {0xFFFFFFFF,0xCCFFFFFF,0xAAFFFFFF,0x88FFFFFF,0x66FFFFFF,0x44FFFFFF,0x22FFFFFF,0x00FFFFFF};


	

	public WhiteShadeAnnulus(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setDefaultValue();
		iniatilizedAnim();
	}
	
	private void iniatilizedAnim() {
		anim = ValueAnimator.ofInt(0, 360);
		anim.setDuration(2000);
		anim.setRepeatCount(ValueAnimator.INFINITE);
		anim.setRepeatMode(ValueAnimator.RESTART);
		anim.setInterpolator(new LinearInterpolator());
		anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int r = (Integer) animation.getAnimatedValue();
				setRotate(r);
			}
		});;
		anim.start();
	}
	
	

	@Override
	protected void onVisibilityChanged(View changedView, int visibility) {
		if (anim != null) {
			if (visibility == View.VISIBLE) {
				anim.start();
			} else {
				anim.end();
			}
		}
		super.onVisibilityChanged(changedView, visibility);
	}

	private void setDefaultValue() {
		Resources r = getResources();
		barWidth = (int) r.getDimension(R.dimen.default_main_bar_width);

    }

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		layout_width = w;
		layout_height = h;

		setupBounds();
		setupPaints();
		invalidate();
	}

	/**
	 * Set the bounds of the component
	 */
	private void setupBounds() {
		int minValue = Math.min(layout_width, layout_height);

		int xOffset = layout_width - minValue;
		int yOffset = layout_height - minValue;

		paddingTop = getPaddingTop() + (yOffset / 2);
		paddingBottom = getPaddingBottom() + (yOffset / 2);
		paddingLeft = getPaddingLeft() + (xOffset / 2);
		paddingRight = getPaddingRight() + (xOffset / 2);

		// arcBounds = new RectF(paddingLeft + rimWidth+barWidth, paddingTop +
		// rimWidth+barWidth,
		// layout_width - paddingRight - rimWidth-barWidth,
		// layout_height - paddingBottom - rimWidth-barWidth);

		circleBounds = new RectF(paddingLeft, paddingTop, layout_width
				- paddingRight, layout_height - paddingBottom);

		barBounds = new RectF(paddingLeft + barWidth / 2, paddingTop + barWidth
				/ 2, layout_width - paddingRight - barWidth / 2, layout_height
				- paddingBottom - barWidth / 2);

		fullRadius = circleBounds.width() / 2;
	}

	
	private void setupPaints() {
		s = new SweepGradient(circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop, mRectColors, null);
		matrix.setRotate(rotate, circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop);
		s.setLocalMatrix(matrix);

		barPaint.setShader(s);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth);


	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		canvas.drawArc(barBounds, 0, 360, false, barPaint); // 根据进度画圆�?

	}

	public void setRotate(int rotate) {
		if (matrix != null && s != null) {
			
			matrix.setRotate(rotate, circleBounds.width() / 2 + paddingLeft,
					circleBounds.height() / 2 + paddingTop);
			s.setLocalMatrix(matrix);
			invalidate();
		}
	}



	

}
