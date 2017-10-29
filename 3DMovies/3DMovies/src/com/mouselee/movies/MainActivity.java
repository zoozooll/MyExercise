package com.mouselee.movies;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends Activity {
	
	private GLSurfaceView glView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        glView = (GLSurfaceView) findViewById(R.id.glview);
    }
    
    private boolean initGLRenderer() {
    	if (detectOpenGLES20()) {
			// Tell the surface view we want to create an OpenGL ES
			// 2.0-compatible
			// context, and set an OpenGL ES 2.0-compatible renderer.
			glView.setEGLContextClientVersion(2);
			glView.setRenderer(mRender);
			/*glView.setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					if (event.getAction() == MotionEvent.ACTION_UP) {
						// added by aaronli at Jun26 2013. fixed #4539
						mRender.handleMouseUpEvent(event.getX(), event.getY());
						// fixed #4539 end
					}
					else if(event.getAction() == MotionEvent.ACTION_MOVE)
					{
//						if(isDropDownButtonHold)
//						{
//							moveDropDownView(event);
//						}
					}
					if (isGlReady) {
						mGuestureDetector.onTouchEvent(event);
						mScaleGuestureDetector.onTouchEvent(event);
					}
					return false;
				}
			});*/
			glView.setLongClickable(true);
		} else {
			Log.e("HelloTriangle", "OpenGL ES 2.0 not supported on device.  Exiting...");
			return false;
		}
    }
    
    private boolean detectOpenGLES20() {
		ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		ConfigurationInfo info = am.getDeviceConfigurationInfo();
		return (info.reqGlEsVersion >= 0x20000);
	}
}
