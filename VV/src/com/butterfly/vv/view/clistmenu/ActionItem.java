package com.butterfly.vv.view.clistmenu;

import android.content.Context;
import android.graphics.drawable.Drawable;

public class ActionItem {
	protected Drawable icon;
	protected Context mContext;

	public ActionItem(Context context) {
		mContext = context;
	}
	public void setIcon(int resId) {
		setIcon(mContext.getResources().getDrawable(resId));
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public Drawable getIcon() {
		return this.icon;
	}
}
