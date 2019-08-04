package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SizeImageView extends ImageView {
	public SizeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}
	public SizeImageView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int width = MeasureSpec.getSize(widthMeasureSpec);
		int height = Math.round(width * 3 / 4);
		setMeasuredDimension(width, height);
	}
}
