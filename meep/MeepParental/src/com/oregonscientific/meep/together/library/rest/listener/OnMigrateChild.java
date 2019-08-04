package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;


public interface OnMigrateChild {
	public void onMigrateSuccess();
	public void onMigrateFailure(ResponseBasic r);
	public void onMigrateTimeout();
}
