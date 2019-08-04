package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;


public interface OnUpdateProfileParentListener {
	public void onUpdateProfileParentSuccess(ResponseBasic r);
	public void onUpdateProfileParentFailure(ResponseBasic r);
	public void onUpdateProfileParentTimeout();
}
