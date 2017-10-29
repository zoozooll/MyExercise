package com.dvr.android.dvr.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * 状�?显示控件，主要以文字提示当前状�?
 * @author hanJ
 *
 */
public class StatusView extends TextView{
	
	/**
	 * 通过该接口来对状态发生变化作出处�?
	 */
	public interface OnStatusChangedListener{
		public void onStatusChanged();
	}
	
	private OnStatusChangedListener mOnStatusChangedListener;
	
	public StatusView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setOnStatusChangedListener(OnStatusChangedListener listener){
		mOnStatusChangedListener = listener;
	}
	
	public void updateStatus(){
		if (mOnStatusChangedListener != null){
			mOnStatusChangedListener.onStatusChanged();
		}
	}
}
