package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;


public interface OnUpdateProfileKidListener {
	public void onUpdateProfileKidSuccess(ResponseBasic r);
	public void onUpdateProfileKidFailure(ResponseBasic r);
	public void onUpdateProfileKidTimeout();
}
