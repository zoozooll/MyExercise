package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;

public interface OnLoadPortalSettingsListener {
	public void onLoadPortalSettingsSuccess(ResponseLoadPermission infoPermission);
	public void onLoadPortalSettingsFailure(ResponseBasic r);
}
