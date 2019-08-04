package com.oregonscientific.meep.home.view;

import java.util.List;

import com.oregonscientific.meep.notification.Notification;


public interface OnMenuRefreshListener {
	public List<Notification> onRefresh();
}
