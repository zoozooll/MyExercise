package com.butterfly.vv.camera.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public interface CameraBaseListElement {
	public int cellType = 0;

	// 获取布局文件的ID
	public int getLayoutId();
	// 获取点击
	public boolean isClickable();
	// 获取每个元素view
	public View getViewForElement(int postion, LayoutInflater layoutInflater,
			Context context, View view);
}
