package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.WindowManager;

public class BaseActivity extends Activity{
	
	@Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.getWindow().setType(WindowManager.LayoutParams.TYPE_KEYGUARD);           
    }
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {     

        if(keyCode == KeyEvent.KEYCODE_HOME)
        {
           Intent first = new Intent(getApplicationContext(),MeepTogetherSplashScreen.class);
           startActivity(first);
        }
        return false;
    }
	
	@Override
	protected void onPause() {
		super.onPause();
		super.onDetachedFromWindow();
	}
	
	@Override
	protected void onRestart() {
		super.onRestart();
		super.onAttachedToWindow();
	}
}
