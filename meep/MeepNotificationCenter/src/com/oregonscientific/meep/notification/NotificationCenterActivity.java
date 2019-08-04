package com.oregonscientific.meep.notification;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

public class NotificationCenterActivity extends Activity {
	
	public static final String ACTION_NOTIFICATION = "notification";
	public static final String ACTION_NEWS = "news";
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		startActivity(new Intent(this, NotificationActivity.class));
//		startActivity(new Intent(this, NewsActivity.class));
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
