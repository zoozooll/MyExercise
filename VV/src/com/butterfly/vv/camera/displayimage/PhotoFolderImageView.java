package com.butterfly.vv.camera.displayimage;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 自定义显示网格相册，2015-03-05
 * @author zhenggen xie com.butterfly.vv.camera.displayimage.PhotoFolderImageView
 */
public class PhotoFolderImageView extends ImageView {
	private OnMeasureListener onMeasureListener;

	public void setOnMeasureListener(OnMeasureListener onMeasureListener) {
		this.onMeasureListener = onMeasureListener;
	}
	public PhotoFolderImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	public PhotoFolderImageView(Context context, AttributeSet attrs,
			int defStyle) {
		super(context, attrs, defStyle);
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		// 将图片测量的大小回调到onMeasureSize()方法中
		if (onMeasureListener != null) {
			onMeasureListener.onMeasureSize(getMeasuredWidth(),
					getMeasuredHeight());
		}
	}

	public interface OnMeasureListener {
		public void onMeasureSize(int width, int height);
	}
}
