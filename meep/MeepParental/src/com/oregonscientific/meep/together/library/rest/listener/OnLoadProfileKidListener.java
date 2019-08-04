package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseProfileKid;

public interface OnLoadProfileKidListener {
	public void onLoadProfileSuccess(ResponseProfileKid profileKid);
	public void onLoadProfileFailure(ResponseBasic r);
}
