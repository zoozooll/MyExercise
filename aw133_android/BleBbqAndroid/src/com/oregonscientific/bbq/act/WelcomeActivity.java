/**
 * 
 */
package com.oregonscientific.bbq.act;


import com.oregonscientific.bbq.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

/**
 * @author aaronli
 *
 */
public class WelcomeActivity extends Activity {

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		
		new Handler().postDelayed(new Runnable(){
			@Override
			public void run() {
			    Intent intent = new Intent(WelcomeActivity.this, OperationActivity.class);
				//Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
			    startActivity(intent);
			    finish();
			}
		}, 1500);
	}

}
