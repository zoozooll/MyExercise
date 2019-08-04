package com.pullToRefresh.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * @ClassName: RotateImageView
 * @Description:低版本的View没有setRotation方法
 * @author: yuedong bao
 * @date: 2015-10-23 下午1:27:53
 */
public class RotateImageView extends ImageView {
	private float mRotation;

	public RotateImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	@Override
	public void setRotation(float rotation) {
		this.mRotation = rotation;
		Matrix matrix = new Matrix();
		int bitmapW = getDrawable().getIntrinsicWidth();
		int bitmapH = getDrawable().getIntrinsicHeight();
		float centerX = this.getWidth() / 2;
		float centerY = this.getHeight() / 2;
		matrix.preTranslate(centerX, centerY);
		matrix.setScale(getWidth() / (float) bitmapW, getHeight()
				/ (float) bitmapH);
		matrix.postRotate(rotation, centerX, centerY);
		setImageMatrix(matrix);
	}
	@Override
	public float getRotation() {
		return mRotation;
	}
}
