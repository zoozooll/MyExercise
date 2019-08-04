package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLogin;

public interface OnLoginListener {
	public void onLoginSuccess(ResponseLogin lr);
	public void onLoginFailure(ResponseBasic r);
	public void onLoginTimeOut();
}
