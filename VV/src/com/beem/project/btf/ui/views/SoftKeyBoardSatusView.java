package com.beem.project.btf.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

/**
 * 该监听在类容不能滚动的情况下无效
 * @author liao
 */
public class SoftKeyBoardSatusView extends View {
	private final int CHANGE_SIZE = 100;

	public SoftKeyBoardSatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}
	public SoftKeyBoardSatusView(Context context) {
		super(context);
		init();
	}
	private void init() {
	}
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		if (oldw == 0 || oldh == 0)
			return;
		if (boardListener != null) {
			boardListener.keyBoardStatus(w, h, oldw, oldh);
			if (oldw != 0 && h - oldh < -CHANGE_SIZE) {
				boardListener.keyBoardVisable(oldh - h);
			}
			if (oldw != 0 && h - oldh > CHANGE_SIZE) {
				boardListener.keyBoardInvisable(oldh - h);
			}
		}
	}

	public interface SoftkeyBoardListener {
		public void keyBoardStatus(int w, int h, int oldw, int oldh);
		public void keyBoardVisable(int move);
		public void keyBoardInvisable(int move);
	}

	SoftkeyBoardListener boardListener;

	public void setSoftKeyBoardListener(SoftkeyBoardListener boardListener) {
		this.boardListener = boardListener;
	}
}
