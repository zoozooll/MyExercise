package com.yarin.android.Examples_05_11;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

public class Activity01 extends Activity {
	private GameView mGameView = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mGameView = new GameView(this);

		setContentView(mGameView);
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		super.onKeyUp(keyCode, event);
		return true;
	}

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (mGameView == null) {
			return false;
		}
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			this.finish();
			return true;
		}
		return mGameView.onKeyDown(keyCode, event);
	}
}