package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;

public interface OnRegisterListener {
	public void onRegisterSuccess();
	public void onRegisterFailure(ResponseBasic r);
	public void onRegisterTimeout();
}
