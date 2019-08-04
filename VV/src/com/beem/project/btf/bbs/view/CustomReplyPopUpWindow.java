package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.PopupWindow;

public class CustomReplyPopUpWindow extends PopupWindow {
	private Activity mAct;
	private PopupWindow popupWindow;

	public CustomReplyPopUpWindow(Activity mAct, View popupWindowView) {
		super(mAct);
		this.mAct = mAct;
		popupWindow = this;
		popupWindow = new PopupWindow(popupWindowView,
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT, true);
		popupWindow.setBackgroundDrawable(new BitmapDrawable());
		// 设置PopupWindow的弹出和消失效果
		popupWindow.setAnimationStyle(R.style.popupAnimation);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow
				.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
						| WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED);
		popupWindow.setOutsideTouchable(true);
	}
	/**
	 * @func 显示，在Activity的底部显示
	 */
	public void show() {
		int bottom = mAct.getWindow().getDecorView().getBottom();
		popupWindow.getContentView().measure(MeasureSpec.AT_MOST,
				MeasureSpec.AT_MOST);
		int height = popupWindow.getContentView().getMeasuredHeight();
		popupWindow.showAtLocation(mAct.getWindow().getDecorView(),
				Gravity.TOP, 0, bottom - height);
		//		//LogUtils.i("TAG" + popupWindow.getContentView().getMeasuredWidth() + "===>"
		//				+ popupWindow.getContentView().getMeasuredHeight() + "==>");
	}
}
