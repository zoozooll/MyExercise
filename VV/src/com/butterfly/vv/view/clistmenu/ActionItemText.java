package com.butterfly.vv.view.clistmenu;

import android.content.Context;

public class ActionItemText extends ActionItem {
	public ActionItemText(Context context, int title, int icon) {
		super(context);
		setTitle(title);
		setIcon(icon);
	}
	public ActionItemText(Context context, String title, int icon) {
		super(context);
		setTitle(title);
		setIcon(icon);
	}

	private String title;

	public void setTitle(int resId) {
		setTitle(mContext.getString(resId));
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return this.title;
	}
}
