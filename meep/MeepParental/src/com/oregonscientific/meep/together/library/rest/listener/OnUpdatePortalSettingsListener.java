package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;

public interface OnUpdatePortalSettingsListener {
	public void onUpdatePortalSettingsSuccess(ResponseLoadPermission infoPermission);
	public void onUpdatePortalSettingsFailure(ResponseBasic r);
	public void onUpdatePortalSettingsTimeout();
}
