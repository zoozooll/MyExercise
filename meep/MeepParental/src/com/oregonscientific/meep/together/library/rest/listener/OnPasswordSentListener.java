package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;

public interface OnPasswordSentListener {
	public void onPasswordSentSuccess(ResponseBasic r);
	public void onPasswordSentFailure(ResponseBasic r);
	public void onPasswordSentTimeout();

}
