package com.aaron.fallsnow;

import android.app.Activity;
import android.os.Bundle;
import android.renderscript.RSSurfaceView;
import android.util.Log;
import android.view.View;

public class FallingSnow extends Activity {
	
	private RSSurfaceView mRootView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.grid_outside);
		mRootView = (RSSurfaceView) findViewById(R.id.view_fallingsnow);
	}
	
	@Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mRootView.resume();
    }

    @Override
    protected void onPause() {

        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mRootView.pause();
        Runtime.getRuntime().exit(0);
    }

}
