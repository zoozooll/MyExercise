package com.oregonscientific.meep.together.library.rest.listener;

import java.util.ArrayList;

import com.oregonscientific.meep.together.bean.Kid;

public interface OnLoadKidListener {
	public void onLoadKidSuccess(Kid[] kids);
	public void onLoadKidFailure();
	public void onLoadKidTimeout();
}
