package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseProfileParent;

public interface OnLoadProfileParentListener {
	public void onLoadProfileSuccess(ResponseProfileParent profileParent);
	public void onLoadProfileFailure(ResponseBasic r);
}
