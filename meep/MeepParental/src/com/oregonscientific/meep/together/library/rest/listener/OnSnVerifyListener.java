package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;

public interface OnSnVerifyListener {
	public void onSnVerifySuccess(ResponseBasic r);
	public void onSnVerifyFailure(ResponseBasic r);
	public void onSnVerifyTiemOut();

}
