package com.oregonscientific.meep.together.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.Identity;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.LoginUser;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.library.database.DatabaseHelper;
import com.oregonscientific.meep.together.library.database.table.AuthInfo;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnLoginListener;
import com.oregonscientific.meep.together.library.rest.listener.OnPasswordSentListener;

public class MeepTogetherLoginActivity extends Activity {

	RelativeLayout loginMainLayout;
	LinearLayout choice;
	LinearLayout loginedit;
	FrameLayout forgetLayout;
	Context context;
	MyProgressDialog loading;

	Button btnLogin;
	Button btnToLogin;
	Button btnLinkToRegister;
	Button btnLinkToForget;
	Button btnReset;
	ImageButton barImageButtonBack;
	ImageButton MainTitleImage;

	EditText forgetPassEmail;

	// animation
	Animation topToBottomAnimation;
	Animation bottomToTopAnimation;

	EditText loginEmail;
	EditText loginPassword;

	private boolean isForget = false;
	private static LoginUser loginuser;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		context = this.getApplicationContext();
		initUI();
		initListeners();

	}

	public void initUI() {
		setContentView(R.layout.activity_login);

		getAnimationInfo();

		// layout
		loginMainLayout = (RelativeLayout) findViewById(R.id.loginMainLayout);
		choice = (LinearLayout) findViewById(R.id.layout_login_choose);
		loginedit = (LinearLayout) findViewById(R.id.layout_login_edit);
		forgetLayout = (FrameLayout) findViewById(R.id.forgetLayout);
		// button
		btnLogin = (Button) findViewById(R.id.btnLogin);
		btnToLogin = (Button) findViewById(R.id.btnToLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
		btnLinkToForget = (Button) findViewById(R.id.btnLinkToForgetPassword);
		// edittext
		loginEmail = (EditText) findViewById(R.id.loginEmail);
		loginPassword = (EditText) findViewById(R.id.loginPassword);

		loading = new MyProgressDialog(MeepTogetherLoginActivity.this);
		loading.setMessage(context.getResources().getString(R.string.loading_text));
		loading.getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	}

	public void initListeners() {
		btnToLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				choice.setVisibility(View.GONE);
				loginedit.setVisibility(View.VISIBLE);
			}
		});
		// Login button Click Event
		btnLogin.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				String email = loginEmail.getText().toString().trim();
				String password = loginPassword.getText().toString().trim();
				loading.show();
				if (email.equals("")) {
					UserFunction.popupMessage(R.string.email_empty, MeepTogetherLoginActivity.this, loading);
				} else if (!Utils.isValidEmailAddress(email)) {
					UserFunction.popupMessage(R.string.The_email_is_incorrect, MeepTogetherLoginActivity.this, loading);
				} else if (password.equals("")) {
					UserFunction.popupMessage(R.string.password_empty, MeepTogetherLoginActivity.this, loading);
				} else if (!email.equals("") && !password.equals("")
						&& email != null && password != null) {
					email = email.toLowerCase();
					loginuser = new LoginUser(email, password);
					if (UserFunction.isNetworkAvailable(context)) {
						view.setClickable(false);
						UserFunction.getRestHelp().authentication(loginuser, context);
					} else {
						AuthInfo auth = UserFunction.getDataHelp().queryIsLogin();
						if (auth == null) {
							UserFunction.popupMessage(R.string.no_network, MeepTogetherLoginActivity.this, loading);
						} else {
							UserFunction.getRestHelp().authenticationLocally(loginuser, auth);
						}

					}

				}
			}

		});

		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				Intent i = new Intent(getApplicationContext(), MeepTogetherRegistrationActivity.class);
				startActivity(i);
			}
		});

		// Link to Forget PassWord
		btnLinkToForget.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				initForgetPassPage();
			}
		});
		UserFunction.getRestHelp().setOnPasswordSentListener(new OnPasswordSentListener() {

			@Override
			public void onPasswordSentSuccess(ResponseBasic r) {
				// onPassSent();
				UserFunction.popupResponse(r.getStatus(), MeepTogetherLoginActivity.this, loading);
			}

			@Override
			public void onPasswordSentFailure(ResponseBasic r) {
				// onPassSentFailure();
				UserFunction.popupResponse(r.getStatus(), MeepTogetherLoginActivity.this, loading);
			}

			@Override
			public void onPasswordSentTimeout() {
				UserFunction.popupMessage(R.string.please_retry, MeepTogetherLoginActivity.this, loading);

			}
		});

		UserFunction.getRestHelp().setOnLoginListener(new OnLoginListener() {

			@Override
			public void onLoginSuccess(ResponseLogin lr) {
				onLoginUserSuccess(lr);
				btnLogin.setClickable(true);
			}

			@Override
			public void onLoginFailure(ResponseBasic r) {
				// onLoginUserFailure();
				if (r != null) {
					UserFunction.popupResponse(r.getStatus(), MeepTogetherLoginActivity.this, loading);
				} else {
					UserFunction.popupMessage(R.string.login_failure, MeepTogetherLoginActivity.this, loading);
				}
				btnLogin.setClickable(true);
			}

			@Override
			public void onLoginTimeOut() {
				onLoginUserTimeOut();
				btnLogin.setClickable(true);

			}
		});
		
		loginEmail.setOnEditorActionListener(new OnEditorActionListener() {
	    	
	    	@Override
	    	public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
	    		//hide keyboard when click done/
	    		if (actionId == EditorInfo.IME_ACTION_NEXT) {
	    			loginPassword.requestFocus();
	    		}
	    		return false;
	    	}
	    });
	    loginPassword.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//hide keyboard when click done/
				if (actionId == EditorInfo.IME_ACTION_DONE) {
					UserFunction.hideKeyboard(v, MeepTogetherLoginActivity.this);
				}
				return false;
			}
		});

	}

	public void toMain(boolean isOnline) {
		// Launch Main Screen
		Intent main = new Intent(getApplicationContext(), MeepTogetherMainActivity.class);
		// Close all views before launching Main
		main.putExtra("online", isOnline);
		startActivity(main);
		// Close Login Screen
		finish();
	}

	public void getAnimationInfo() {
		bottomToTopAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_bottom_to_top_slide);
		topToBottomAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_top_to_bottom_slide);

		topToBottomAnimation.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationStart(Animation arg0) {
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
			}

			@Override
			public void onAnimationEnd(Animation arg0) {
				clearForgetPassPage();
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (isForget) {
			translateForgetPassContent();
		} else {
			if (!choice.isShown()) {
				choice.setVisibility(View.VISIBLE);
				loginedit.setVisibility(View.GONE);

			} else {
				super.onBackPressed();
			}
		}

	}

	public void initForgetPassPage() {
		// forget page
		View view = forgetLayout.findViewById(R.id.forgetPassLayout);
		barImageButtonBack = (ImageButton) view.findViewById(R.id.barImageButtonBack);
		forgetPassEmail = (EditText) view.findViewById(R.id.forgetPassEmail);
		btnReset = (Button) view.findViewById(R.id.btnReset);
		// forgetpass page back button
		barImageButtonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				translateForgetPassContent();
				// hide keyboard
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			}
		});
		btnReset.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				loading.show();
				String email = forgetPassEmail.getText().toString().trim();
				if (email == null || email.equals("")) {
					UserFunction.popupMessage(R.string.email_empty, MeepTogetherLoginActivity.this, loading);
				} else if (!Utils.isValidEmailAddress(email)) {
					UserFunction.popupMessage(R.string.The_email_is_incorrect, MeepTogetherLoginActivity.this, loading);
				} else {
					if (UserFunction.isNetworkAvailable(context)) {
						email = forgetPassEmail.getText().toString();
						Utils.printLogcatDebugMessage("forget email:"+email);
						UserFunction.getRestHelp().forgotPassword(email);
					} else {
						UserFunction.popupMessage(R.string.no_network, MeepTogetherLoginActivity.this, loading);
					}

				}
			}
		});

		translateForgetPassContent();
	}

	public void clearForgetPassPage() {
		View view = forgetLayout.findViewById(R.id.forgetPassLayout);
		view.setVisibility(View.GONE);

		// reset
		btnLinkToForget.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				initForgetPassPage();
			}
		});

	}

	public void showForgetPassPage() {
		View view = forgetLayout.findViewById(R.id.forgetPassLayout);
		view.setVisibility(View.VISIBLE);
		btnLinkToForget.setOnClickListener(null);

	}

	// Move Mainlayout
	public void translateForgetPassContent() {
		forgetLayout.clearAnimation();

		if (isForget) {
			forgetLayout.startAnimation(topToBottomAnimation);
			isForget = false;
		} else {
			forgetLayout.startAnimation(bottomToTopAnimation);
			forgetLayout.setVisibility(View.VISIBLE);
			showForgetPassPage();
			isForget = true;
		}

	}

	// public void onPassSent() {
	// UserFunction.popupMessage(R.string.reset_pwd_success, this,loading);
	// }
	//
	// public void onPassSentFailure() {
	// UserFunction.popupMessage(R.string.reset_pwd_failure, this,loading);
	// }
	//
	// public void onLoginUserFailure() {
	// UserFunction.popupMessage(R.string.login_failure, this,loading);
	// }
	public void onLoginUserTimeOut() {
		UserFunction.popupMessage(R.string.please_retry, this, loading);
	}

	public void onLoginUserSuccess(ResponseLogin lr) {
		Utils.printLogcatDebugMessage("login really Stage -- "+ lr.getStage());
		boolean isFirst = false;
		boolean isVerify = false;
		boolean isCreditVerify = false;
		boolean isNormal = false;
		boolean isOffline = false;

		if (lr.getStage().equals(ResponseLogin.STAGE_CREATE_ACCOUNT))
			isFirst = true;
		if (lr.getStage().equals(ResponseLogin.STAGE_VERIFY_EMAIL))
			isVerify = true;
		if (lr.getStage().equals(ResponseLogin.STAGE_VERIFY_CRIDITCARD))
			isCreditVerify = true;
		if (lr.getStage().equals(ResponseLogin.STAGE_NORMAL))
			isNormal = true;
		if (lr.getStage().equals(ResponseLogin.STAGE_OFFLINE))
			UserFunction.isCreditCardVerified(context);
			isOffline = true;
		if (isFirst) {
			// Launch StratUsing Screen
			Intent start = new Intent(context, MeepTogetherStartUsingActivity.class);
			startActivity(start);
			finish();
		} else if (isVerify) {
			// Launch Verify Screen
			Intent verif = new Intent(context, MeepTogetherEmailVerifyActivity.class);
			verif.putExtra("email", loginEmail.getText().toString());
			startActivity(verif);
			if (loading.isShowing()) {
				loading.dismiss();
			}
		} else if (isCreditVerify) {
			UserFunction.setCreditCardVerfied(this,false);
			UserFunction.getRestHelp().getKidNumber();
		} else if (isNormal) {

			UserFunction.storeUserInDatabase(lr, loginuser);
			toMain(true);

		} else if (isOffline) {
			toMain(false);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (loading.isShowing()) {
			loading.dismiss();
		}
	}

	public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
