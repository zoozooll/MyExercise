package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;

public interface RequestRespondedListener
{
	public void onRespondSuccess(ResponseBasic r);
	public void onRespondFailure(ResponseBasic r);
	public void onRespondTimeout();
}
