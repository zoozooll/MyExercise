package com.beem.project.btf.ui.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.util.AttributeSet;
import android.view.View;

/**
 * 头像裁剪View
 * @ClassName: ClipView
 * @Description: TODO
 * @author: zhenggen xie
 * @date: 2015年3月10日 下午4:00:30
 */
public class ClipView extends View {
	private Paint paint = new Paint();
	private Paint borderPaint = new Paint();
	/** 自定义顶部栏高度，如不是自定义，则默认为0即可 */
	private int customTopBarHeight = 0;
	/** 裁剪框长宽比，默认4：3 */
	private double clipRatio = 1.0;
	/** 裁剪框宽度 */
	private int clipWidth = -1;
	/** 裁剪框高度 */
	private int clipHeight = -1;
	/** 裁剪框左边空留宽度 */
	private int clipLeftMargin = 0;
	/** 裁剪框上边空留宽度 */
	private int clipTopMargin = 0;
	/** 裁剪框边框宽度 */
	private int clipBorderWidth = 0;
	private boolean isSetMargin = false;
	private OnDrawListenerComplete listenerComplete;
	private boolean isDrawComplete;

	public ClipView(Context context) {
		super(context);
	}
	public ClipView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public ClipView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		int width = this.getWidth();
		int height = this.getHeight();
		// 如没有显示设置裁剪框高度和宽度，取默认值
		if (clipWidth == -1 || clipHeight == -1) {
			clipWidth = width;
			clipHeight = (int) (width * clipRatio);
			// 横屏
			if (width > height) {
				clipHeight = height;
				clipWidth = (int) (clipHeight / clipRatio);
			}
		}
		// 如没有显示设置裁剪框左和上预留宽度，取默认值
		if (!isSetMargin) {
			clipLeftMargin = (width - clipWidth) / 2;
			clipTopMargin = (height - customTopBarHeight - clipHeight) / 2;
		}
		// 画阴影
		paint.setAlpha(175);
		// top
		canvas.drawRect(0, 0, width, clipTopMargin, paint);
		// left
		canvas.drawRect(0, clipTopMargin, clipLeftMargin, clipTopMargin
				+ clipHeight, paint);
		// right
		canvas.drawRect(clipLeftMargin + clipWidth, clipTopMargin, width,
				clipTopMargin + clipHeight, paint);
		// bottom
		canvas.drawRect(0, clipTopMargin + clipHeight, width, height
				- customTopBarHeight, paint);
		// 画边框
		borderPaint.setStyle(Style.STROKE);
		// borderPaint.setColor(Color.WHITE);
		borderPaint.setStrokeWidth(clipBorderWidth);
		canvas.drawRect(clipLeftMargin, clipTopMargin, clipLeftMargin
				+ clipWidth, clipTopMargin + clipHeight, borderPaint);
		isDrawComplete = true;
		if (listenerComplete != null) {
			listenerComplete.onDrawCompelete();
		}
	}
	public int getCustomTopBarHeight() {
		return customTopBarHeight;
	}
	public void setCustomTopBarHeight(int customTopBarHeight) {
		this.customTopBarHeight = customTopBarHeight;
	}
	public double getClipRatio() {
		return clipRatio;
	}
	public void setClipRatio(double clipRatio) {
		this.clipRatio = clipRatio;
	}
	public int getClipWidth() {
		// 减clipBorderWidth原因：截图时去除边框白线
		return clipWidth - clipBorderWidth;
	}
	public void setClipWidth(int clipWidth) {
		this.clipWidth = clipWidth;
	}
	public int getClipHeight() {
		return clipHeight - clipBorderWidth;
	}
	public void setClipHeight(int clipHeight) {
		this.clipHeight = clipHeight;
	}
	public int getClipLeftMargin() {
		return clipLeftMargin + clipBorderWidth;
	}
	public void setClipLeftMargin(int clipLeftMargin) {
		this.clipLeftMargin = clipLeftMargin;
		isSetMargin = true;
	}
	public int getClipTopMargin() {
		return clipTopMargin + clipBorderWidth;
	}
	public void setClipTopMargin(int clipTopMargin) {
		this.clipTopMargin = clipTopMargin;
		isSetMargin = true;
	}
	public void addOnDrawCompleteListener(OnDrawListenerComplete listener) {
		this.listenerComplete = listener;
	}
	public void removeOnDrawCompleteListener() {
		this.listenerComplete = null;
	}

	/**
	 * 裁剪区域画完时调用接口
	 * @author Cow
	 */
	public interface OnDrawListenerComplete {
		public void onDrawCompelete();
	}

	public boolean isDrawComplete() {
		return isDrawComplete;
	}
	public void setDrawComplete(boolean isDrawComplete) {
		this.isDrawComplete = isDrawComplete;
	}
}
