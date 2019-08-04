package com.tcl.manager.view;

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
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.tcl.manager.score.ScoreLevel;
import com.tcl.mie.manager.R;

/**
 * @Description: miniapp分数圈
 * @author wenchao.zhang
 * @date 2014年12月25日 下午6:55:50
 * @copyright TCL-MIE
 */

public class MiniAppScoreCircleView extends View {
	// 防止连续点击
	private static final long CLICK_DIDIVER = 2 * 1000;
	// 尺寸(默认)
	private int layout_height = 0;
	private int layout_width = 0;
	private int barWidth = 0;
	private int rimWidth = 0;
	private int descTextSize = 0;
	private int scoreTextSize = 0;
	private float fullRadius = 0;
	// padding(默认)
	private int paddingTop = 0;
	private int paddingBottom = 0;
	private int paddingLeft = 0;
	private int paddingRight = 0;
	// 颜色(默认)
	private int bgColorNormal;
	private int bgColorPress;
	private int textColor;
	// 画笔
	private Paint barPaint = new Paint();
	private Paint circlePaint = new Paint();
	private Paint scoreTextPaint = new Paint();
	private Paint descTextPaint = new Paint();

	// 矩形坐标
	private RectF circleBounds = new RectF();
	private RectF barBounds = new RectF();
	// 渐变色
	private int[] mRectColors;// = new int[] { Color.TRANSPARENT, Color.WHITE
								// };//
								// {0xFFFFFFFF,0xCCFFFFFF,0xAAFFFFFF,0x88FFFFFF,0x66FFFFFF,0x44FFFFFF,0x22FFFFFF,0x00FFFFFF};

	// 分数文本
	private int currentScore = 99;
	// 描述文本
	private String descText = "Healthy Status";

	private OnClickListener onClickListener;

	// Animation
	// The amount of pixels to move the bar by on each draw
	private int spinSpeed = 5;
	// The number of milliseconds to wait inbetween each draw
	private int delayMillis = 10;
	// 0-360
	private int progress = 0;
	boolean isSpinning = false;

	/** 用户是否按下 */
	private boolean isPressed = false;

	private Handler spinHandler = new Handler() {
		/**
		 * This is the code that will increment the progress variable and so
		 * spin the wheel
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				invalidate();
				if (isSpinning) {
					progress += spinSpeed;
					if (progress > 360) {
						progress = 0;
					}
					spinHandler.sendEmptyMessageDelayed(0, delayMillis);
				}
				break;
			default:
				break;
			}

			// super.handleMessage(msg);
		}
	};

	public MiniAppScoreCircleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setFocusable(true);
		setDefaultValue();
	}

	private void setDefaultValue() {
		Resources r = getResources();
		barWidth = (int) r.getDimension(R.dimen.default_miniapp_bar_width);
		rimWidth = (int) r.getDimension(R.dimen.default_miniapp_rim_width);
		descTextSize = (int) r
				.getDimension(R.dimen.default_miniapp_desc_textsize);
		scoreTextSize = (int) r
				.getDimension(R.dimen.default_miniapp_score_textsize);
		textColor = Color.WHITE;
		bgColorPress = r.getColor(R.color.main_score_circle_pressed);
		bgColorNormal = r.getColor(R.color.main_good);

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
		mRectColors = new int[] { Color.TRANSPARENT, bgColorNormal };
		Shader s = new SweepGradient(circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop, mRectColors, null);
		Matrix matrix = new Matrix();
		matrix.setRotate(-90, circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop);
		s.setLocalMatrix(matrix);
		barPaint.setShader(s);
		barPaint.setAntiAlias(true);
		barPaint.setStyle(Style.STROKE);
		barPaint.setStrokeWidth(barWidth);

		circlePaint.setColor(bgColorNormal);
		circlePaint.setAntiAlias(true);
		circlePaint.setStyle(Style.FILL);

		scoreTextPaint.setColor(textColor);
		scoreTextPaint.setStyle(Style.FILL);
		scoreTextPaint.setAntiAlias(true);
		scoreTextPaint.setTextSize(scoreTextSize);

		descTextPaint.setColor(textColor);
		descTextPaint.setStyle(Style.FILL);
		descTextPaint.setAntiAlias(true);
		descTextPaint.setTextSize(descTextSize);

	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		if (isPressed) {
			circlePaint.setColor(bgColorPress);
		} else {
			circlePaint.setColor(bgColorNormal);
		}

		canvas.drawCircle(circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop, fullRadius - rimWidth
						- barWidth, circlePaint);
		// 画圈
		// canvas.drawCircle(circleBounds.width() / 2 + paddingLeft,
		// circleBounds.height() / 2 + paddingTop, fullRadius-barWidth/2,
		// barPaint);
		mRectColors = new int[] { Color.TRANSPARENT, bgColorNormal };
		Shader s = new SweepGradient(circleBounds.width() / 2 + paddingLeft,
				circleBounds.height() / 2 + paddingTop, mRectColors, null);
		Matrix matrix = new Matrix();
		matrix.setRotate(-90 + progress,
				circleBounds.width() / 2 + paddingLeft, circleBounds.height()
						/ 2 + paddingTop);
		s.setLocalMatrix(matrix);
		barPaint.setShader(s);
		canvas.drawArc(barBounds, 0, 360, false, barPaint); // 根据进度画圆弧
		// 画分数文本
		float scoreOffset = scoreTextPaint.measureText(String
				.valueOf(currentScore)) / 2;
		canvas.drawText(String.valueOf(currentScore), this.getWidth() / 2
				- scoreOffset, this.getHeight() / 2, scoreTextPaint);
		// 画描述文本
		float descOffset = descTextPaint.measureText(descText) / 2;
		canvas.drawText(descText, this.getWidth() / 2 - descOffset,
				this.getHeight() / 2 + scoreTextSize / 2, descTextPaint);

	}

	private boolean isInCircleDown = false;
	// 上次点击时间
	private long lastClickTime;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (isInCircle(event.getX(), event.getY()) && this.isClickable()) {
				isInCircleDown = true;
				isPressed = true;
				invalidate();
				return true;
			}
			break;
		case MotionEvent.ACTION_MOVE:
			break;
		case MotionEvent.ACTION_UP:
			if (isInCircleDown) {
				if (isInCircle(event.getX(), event.getY())) {
					// isInCircleUp onClick
					if (this.onClickListener != null && this.isClickable()) {
						long currTime = System.currentTimeMillis();
						if (currTime - lastClickTime >= CLICK_DIDIVER) {
							this.onClickListener.onClick(this);
							lastClickTime = currTime;
						}
//						lastClickTime = currTime;

					}
				}
				isInCircleDown = false;
			}
			isPressed = false;
			invalidate();
			break;
		default:
			break;
		}
		return super.onTouchEvent(event);
	}

	private boolean isInCircle(float x, float y) {
		float centerX = circleBounds.width() / 2 + paddingLeft;
		float centerY = circleBounds.height() / 2 + paddingTop;
		double dis = Math.sqrt(Math.abs(centerX - x) * Math.abs(centerX - x)
				+ Math.abs(centerY - y) * Math.abs(centerY - y));
		return fullRadius <= dis ? false : true;

	}

	/**
	 * 设置点击监听器
	 */
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;
	}

	private void setDescText(String text) {
		this.descText = text;
	}

	/**
	 * 设置分数
	 * 
	 * @param score
	 */
	public void setScore(int score) {
		this.currentScore = score;
		this.bgColorNormal = ScoreLevel.resolveToColor(score);
		setDescText(ScoreLevel.resolveToString(score));
		invalidate();
	}

	/**
	 * Turn off spin mode
	 */
	public void stopSpinning() {
		isSpinning = false;
		progress = 0;
		spinHandler.removeMessages(0);
	}

	/**
	 * Puts the view on spin mode
	 */
	public void spin() {
		stopSpinning();
		isSpinning = true;
		spinHandler.sendEmptyMessage(0);
	}

	/**
	 * Increment the progress by 1 (of 360)
	 */
	public void incrementProgress() {
		isSpinning = false;
		progress++;
		spinHandler.sendEmptyMessage(0);
	}
}
