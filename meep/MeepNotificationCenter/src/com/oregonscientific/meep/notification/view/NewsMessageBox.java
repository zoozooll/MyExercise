package com.oregonscientific.meep.notification.view;

import android.content.Context;
import android.graphics.RectF;
import android.util.AttributeSet;

public class NewsMessageBox extends MessageBox {

	public NewsMessageBox(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		if (top != null && bottom != null) {
			topRect = new RectF(0, 0, top.getWidth(), top.getHeight());
			contentRect = new RectF(0, topRect.height(), topRect.width(), h - bottom.getHeight());
			bottomRect = new RectF(0, h - bottom.getHeight(), topRect.width(), h);
		}
	}

}
