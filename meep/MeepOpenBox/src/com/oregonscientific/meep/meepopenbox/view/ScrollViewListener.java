package com.oregonscientific.meep.meepopenbox.view;

public interface ScrollViewListener {
	
	void onScrollChanged(
			ObservableScrollView scrollView,
			int x,
			int y,
			int oldx,
			int oldy);
	
}
