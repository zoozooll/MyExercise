package com.oregonscientific.meep.together.activity;

import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.library.database.DatabaseHelper;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnSnVerifyListener;

@SuppressLint("HandlerLeak")
public class MeepTogetherSplashScreen extends Activity {

//	// Splash Screen Timer
//	Timer timer;
//	protected long startTime;
//	protected int splashTime = 3000;

//	private int which;
	// Active or not
//	protected boolean isActive = true;

//	// Time task
//	private final TimerTask task = new TimerTask() {
//		@Override
//		public void run() {
//			if (task.scheduledExecutionTime() - startTime >= splashTime
//					|| !isActive) {
//				Message message = new Message();
//				message.what = which;
////				timerHandler.sendMessage(message);
//				timer.cancel();
//				this.cancel();
//			}
//		}
//	};

//	// handler
//	private final Handler timerHandler = new Handler() {
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case 0:
//				// ask Exist account
//				toNEXT(MeepTogetherExistAccountActivity.class);
//				break;
//			case 1:
//				// toLogin
//				toNEXT(MeepTogetherLoginActivity.class);
//				break;
//			default:
//				break;
//			}
//		}
//	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_splash);
		

//		// set splash timer
//		timer = new Timer(true);
//		startTime = System.currentTimeMillis();
//		timer.schedule(task, 0, 1);
	}
//
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		if (event.getAction() == MotionEvent.ACTION_DOWN) {
//			isActive = false;
//		}
//		return true;
//	}

	public void toNEXT(Class<?> cls) {
		// Launch Login Screen
		Intent next = new Intent(getApplicationContext(), cls);
		// Close all views before launching Login
		startActivity(next);
		// Close Login Screen
	}
	@Override
	protected void onPause() {
		super.onPause();
	} 
	@Override
	protected void onResume() {
		super.onResume();
		if (RestClientUsage.isInUse) {
			
			// show popup
			// Message:  session timeout, please login again
			new ExitAppDialog(MeepTogetherSplashScreen.this, R.string.session_timeout).show();
		} else {
			RestClientUsage.isInUse = true;
			//init
			// UserFunction.getDataHelp().dropAll();
			UserFunction.getDataHelp().createAll();
			
			if (UserFunction.getDataHelp().queryIsLogin() != null) {
				toNEXT(MeepTogetherLoginActivity.class);
			}else{
				if (!UserFunction.isNetworkAvailable(MeepTogetherSplashScreen.this)) {
					FinishActivityDialog dialog = new FinishActivityDialog(MeepTogetherSplashScreen.this, R.string.no_network);
					dialog.show();
				} else {
					UserFunction.getRestHelp().serialVerify(UserFunction.getSerialNumber());
					UserFunction.getRestHelp().setOnSnVerifyListener(new OnSnVerifyListener() {
						
						@Override
						public void onSnVerifySuccess(ResponseBasic r) {
							// TODO Auto-generated method stub
							toNEXT(MeepTogetherExistAccountActivity.class);
						}
						
						@Override
						public void onSnVerifyFailure(ResponseBasic r) {
							toNEXT(MeepTogetherLoginActivity.class);
						}
						
						@Override
						public void onSnVerifyTiemOut() {
							FinishActivityDialog dialog = new FinishActivityDialog(MeepTogetherSplashScreen.this, R.string.please_retry);
							dialog.show();
						}
					});
				}
			}
		}

	}

}
