package com.tcl.manager.adapter;

import android.view.View;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2014-12-30 上午10:57:33
 * @copyright TCL-MIE
 */
public interface OnAdapterItemListener<T>
{
	public void onItemLongClick(View view, T t, int position);
	public void onItemChange(Object ...args);
}
