package com.oregonscientific.meep.together.library.rest.listener;

public interface OnRegisterMeeperListener {
	public void onRegisterMeeperSuccess();
	public void onRegisterMeeperFailure(String error);
	public void onRegisterMeeperTimeout();
}
