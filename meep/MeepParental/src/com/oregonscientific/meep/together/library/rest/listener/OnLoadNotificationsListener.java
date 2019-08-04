package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadNotification;

public interface OnLoadNotificationsListener {
	public void onLoadNotificationsSuccess(ResponseLoadNotification infoNotifications);
	public void onLoadNotificationsFailure(ResponseBasic r);
}
