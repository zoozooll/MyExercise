package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;

public interface ResendEmailListener
{
	public void onResendSuccess(ResponseBasic r);
	public void onResendFailure(ResponseBasic r);
	public void onResendTimeout();
}
