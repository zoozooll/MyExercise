package com.oregonscientific.meep.together.library.rest.listener;


public interface OnGetOwnerIdListerner {
	public void onGetSuccess(String ownerId);
	public void onGetFailure();
	public void onGetTimeout();
}
