package com.oregonscientific.meep.together.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.AccountService;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.RegisterUser;
import com.oregonscientific.meep.together.bean.RegisterUserCombined;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.library.rest.CryptoText;
import com.oregonscientific.meep.together.library.rest.listener.OnRegisterListener;
import com.oregonscientific.meep.together.library.rest.listener.OnSnVerifyListener;
import com.oregonscientific.meep.together.library.rest.listener.ResendEmailListener;

public class MeepTogetherRegistrationActivity extends Activity {

	Context context;
	MyProgressDialog loading;

	// Layout
	View registerLegalInfo;
	View registerCreate;
	View registerVerify;
	ViewFlipper flipper;
	int whichStore = 7;
	CheckBox term;
	CheckBox promotion;

	EditText textFirstName;
	EditText textLastName;
	EditText textEmail;
	EditText textConfirmEmail;
	EditText textSetPassword;
	EditText textConfirmPassword;
	EditText textChildFirstName;
	EditText textChildLastName;
//	EditText textChildMeeptag;
	TextView textMale;
	TextView textFemale;
	EditText textChildDay;
	EditText textChildMonth;
	EditText textChildYear;
//	EditText textTel;
//	EditText textAddress;

	ToggleButton togglebutton;

	// Animation
	Animation nullToFullAnimation;
	Animation fullToNullAnimation;
	Animation reverNullToFullAnimation;
	Animation reverFullToNullAnimation;

	// private static final String[]
	// country={"Australia","Brazil","France","Germany","Italy","Spain","United Kingdom","United States","None of the Above"};
	private static String[] country;
	private static final String[] countryCode = { "au", "br", "fr", "de", "it",
			"es", "uk", "us", "jp", "kr", "zz" };
	private Spinner spinner;
	private ArrayAdapter<String> adapter;
	private CryptoText c = new CryptoText();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration);
		context = this.getApplicationContext();
		country = this.getApplicationContext().getResources().getStringArray(R.array.country_string);
		getAnimationInfo();
		initUI();
		initListeners();

		spinner = (Spinner) findViewById(R.id.spinnerLocation);
		adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, country);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(new SpinnerSelectedListener());
		spinner.setSelection(whichStore);

	}

	public void initUI() {
		flipper = (ViewFlipper) findViewById(R.id.flipper);

		// flipper.setInAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		// android.R.anim.slide_in_left));
		// flipper.setOutAnimation(AnimationUtils.loadAnimation(getApplicationContext(),
		// android.R.anim.slide_out_right));

		registerLegalInfo = (View) findViewById(R.id.registerLayout_legal);
		registerCreate = (View) findViewById(R.id.registerLayout_create);
		registerVerify = (View) findViewById(R.id.registerLayout_verify);

		term = (CheckBox) registerLegalInfo.findViewById(R.id.term_agree);
		promotion = (CheckBox) registerCreate.findViewById(R.id.promotion);

		// load privacy Policy
		final WebView webview = (WebView) registerLegalInfo.findViewById(R.id.webview);
		//2013-03-25 - raymond - change priacy url to terms
		String baseUrl = "https://meeptablet-static.commondatastorage.googleapis.com/terms.html";
		// String baseUrl = "file:///android_asset/PrivacyPolicy.html";
		webview.loadUrl(baseUrl);
		webview.setBackgroundColor(0x00000000);
		WebSettings ws = webview.getSettings();
		ws.setTextSize(WebSettings.TextSize.LARGER);

		textFirstName = (EditText) registerCreate.findViewById(R.id.textFirstName);
		textLastName = (EditText) registerCreate.findViewById(R.id.textLastName);
		textEmail = (EditText) registerCreate.findViewById(R.id.textEmail);
		textConfirmEmail = (EditText) registerCreate.findViewById(R.id.textConfirmEmail);
		textSetPassword = (EditText) registerCreate.findViewById(R.id.textSetPassword);
		textConfirmPassword = (EditText) registerCreate.findViewById(R.id.textConfirmPassword);
//		textDay = (EditText) registerCreate.findViewById(R.id.textDay);
//		textMonth = (EditText) registerCreate.findViewById(R.id.textMonth);
//		textYear = (EditText) registerCreate.findViewById(R.id.textYear);
//		textTel = (EditText) registerCreate.findViewById(R.id.textTelMobile);
//		textAddress = (EditText) registerCreate.findViewById(R.id.textAddress);
		togglebutton = (ToggleButton) registerCreate.findViewById(R.id.togglebutton);
		
		textChildFirstName = (EditText) registerCreate.findViewById(R.id.textChildFirstName);
		textChildLastName = (EditText) registerCreate.findViewById(R.id.textChildLastName);
//		textChildMeeptag = (EditText) registerCreate.findViewById(R.id.textChildMeeptag);
		textMale = (TextView) registerCreate.findViewById(R.id.textMale);
		textFemale = (TextView) registerCreate.findViewById(R.id.textFemale);
		
		textChildYear = (EditText) registerCreate.findViewById(R.id.textChildYear);
		textChildMonth = (EditText) registerCreate.findViewById(R.id.textChildMonth);
		textChildDay = (EditText) registerCreate.findViewById(R.id.textChildDay);

		
		
		loading = new MyProgressDialog(MeepTogetherRegistrationActivity.this);
		loading.setMessage(context.getResources().getString(R.string.loading_text));

	}

	public void initListeners() {
		UserFunction.getRestHelp().setOnSnVerifyListener(new OnSnVerifyListener() {

			@Override
			public void onSnVerifySuccess(ResponseBasic r) {
				onVerifySuccess();
			}

			@Override
			public void onSnVerifyFailure(ResponseBasic r) {
				UserFunction.popupResponse(r.getStatus(), MeepTogetherRegistrationActivity.this, loading);

			}

			@Override
			public void onSnVerifyTiemOut() {
				UserFunction.popupMessage(R.string.please_retry, MeepTogetherRegistrationActivity.this, loading);
				
			}
		});
		UserFunction.getRestHelp().setOnRegisterListener(new OnRegisterListener() {

			@Override
			public void onRegisterSuccess() {
				resigninAccount();
				onRegistrationSuccess();
			}

			@Override
			public void onRegisterFailure(ResponseBasic r) {
				UserFunction.popupResponse(r.getStatus(), MeepTogetherRegistrationActivity.this, loading);

			}

			@Override
			public void onRegisterTimeout() {
				UserFunction.popupMessage(R.string.please_retry, MeepTogetherRegistrationActivity.this, loading);
				
			}
		});
		
		togglebutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					setTextOnToggleButton(textMale, R.color.text_white, R.color.text_black, true);
					setTextOnToggleButton(textFemale, R.color.text_gray, R.color.text_white, false);
				}
				else
				{
					setTextOnToggleButton(textMale, R.color.text_gray, R.color.text_white, false);
					setTextOnToggleButton(textFemale, R.color.text_white, R.color.text_black, true);
				}
				
			}
		});
	}
	
	public void setTextOnToggleButton(TextView textview,int textColor,int shadowColor,boolean clickable)
	{
		textview.setTextColor(getResources().getColor(textColor));
		textview.setShadowLayer(0.1f, 0, -1, getResources().getColor(shadowColor));
		textview.setClickable(clickable);
	}
	
	public void getAnimationInfo() {
		nullToFullAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_null_to_full_slide);
		fullToNullAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_full_to_null_slide);
		reverNullToFullAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_rever_null_to_full_slide);
		reverFullToNullAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_rever_full_to_null_slide);
	}

	public void stepOneCheck() {
		loading.show();
		if (term.isChecked()) {
			stepTwoCheck();
		} else {
			UserFunction.popupMessage(R.string.registration_legal_agree_toast, this, loading);
		}
	}

	private String serialno;

	// Auto check serial number
	public void stepTwoCheck() {
		//get serial number
		serialno = UserFunction.getSerialNumber();
		//check serial nuber
		if (serialno == null || serialno.equals("")) {
			UserFunction.popupMessage(R.string.registration_serial_toast, this, loading);
		} else {
			loading.show();
			UserFunction.getRestHelp().serialVerify(serialno);

		}
	}

	public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void stepThreeCheck() {
		loading.show();
		String firstName = textFirstName.getText().toString().trim();
		String lastName = textLastName.getText().toString().trim();
		String email = textEmail.getText().toString().trim();
		String emailcon = textConfirmEmail.getText().toString().trim();
		String password = textSetPassword.getText().toString().trim();
		String passwordcon = textConfirmPassword.getText().toString().trim();
		String childfirstName = textChildFirstName.getText().toString().trim();
		String childlastName = textChildLastName.getText().toString().trim();
		String childday = textChildDay.getText().toString();
		String childmonth = textChildMonth.getText().toString();
		String childyear = textChildYear.getText().toString();

		RegisterUserCombined user = new RegisterUserCombined();

		if (firstName == null || firstName.equals("") || lastName == null
				|| lastName.equals("")) {
			UserFunction.popupMessage(R.string.register_null_name, this, loading);
		} else if (email == null || email.equals("")||emailcon == null || emailcon.equals("")) {
			UserFunction.popupMessage(R.string.register_null_email, this, loading);
		} else if (!Utils.isValidEmailAddress(email)) {
			UserFunction.popupMessage(R.string.The_email_is_incorrect, this, loading);
		} else if (password == null || password.equals("")
				|| passwordcon == null || passwordcon.equals("")) {
			UserFunction.popupMessage(R.string.register_null_password, this, loading);
		} else if (childfirstName == null || childfirstName.equals("") || childlastName == null
				|| childlastName.equals("")) {
			UserFunction.popupMessage(R.string.register_null_name, this, loading);
		} else if (childday == null || childday.equals("") || childmonth == null
			|| childmonth.equals("") || childyear == null || childyear.equals("")) {
			UserFunction.popupMessage(R.string.register_null_birthday, this, loading);
		} else if (!UserFunction.checkBirthday(childyear, childmonth, childday)) {
			UserFunction.popupMessage(R.string.register_wrong_birthday, this, loading);
		}
		
		else {
			if (!email.equals(emailcon)) {
				UserFunction.popupMessage(R.string.email_different, this, loading);
			} else if (!password.equals(passwordcon)) {
				UserFunction.popupMessage(R.string.password_different, this, loading);

			}
			else {
				loading.show();
				// gender
				if (togglebutton.isChecked()) {
					user.setGender(RegisterUser.MALE);
				} else {
					user.setGender(RegisterUser.FEMALE);
				}
				// promotion&terms
				user.setPromotion_optin(promotion.isChecked());
				user.setTns_acceptance(term.isChecked());

				// others
				user.setFirst_name(firstName);
				user.setLast_name(lastName);
				user.setEmail(email);
				String crypto = c.regiterPss(email, password);
				user.setPassword(crypto);
				user.setSerial_no(serialno);
				user.setStore_country(countryCode[whichStore]);
				//new
				user.setKid_first_name(childfirstName);
				user.setKid_last_name(childlastName);
				user.setKid_dob(childyear + "-" + childmonth + "-" + childday);
//				user.setMeeptag(childmeeptag);
				
				// send register
				UserFunction.getRestHelp().registration(serialno, user);
			}

		}
	}

	public void onVerifySuccess() {
		flipper.setInAnimation(nullToFullAnimation);
		flipper.setOutAnimation(fullToNullAnimation);
		flipper.setDisplayedChild(1);
		loading.dismiss();
	}

	public void onRegistrationSuccess() {
		flipper.setInAnimation(nullToFullAnimation);
		flipper.setOutAnimation(fullToNullAnimation);
		flipper.setDisplayedChild(2);
		loading.dismiss();
	}
	public void resigninAccount() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(MeepTogetherRegistrationActivity.this, ServiceManager.ACCOUNT_SERVICE);
				accountManager.signIn();
			}
					
		});
	}


	class SpinnerSelectedListener implements OnItemSelectedListener {

		public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			// Set Country
			whichStore = arg2;
		}

		public void onNothingSelected(AdapterView<?> arg0) {
		}
	}

	public void onClickButton(View view) {
		switch (view.getId()) {
		case R.id.btnAgree:
			stepOneCheck();
			// flipper.showNext();
			break;
		case R.id.btnNext:
			hideKeyboard(view);
			stepThreeCheck();
			// flipper.showNext();
			break;
		case R.id.btnResend:
			resedConfirmationEmail(textEmail.getText().toString());
			break;
		case R.id.btnBack:
			finish();
			break;
		default:
			break;
		}

	}
	public void onClickBack(View view) {
		if (registerLegalInfo.isShown()) {
			finish();
		} else {
			flipper.setInAnimation(reverFullToNullAnimation);
			flipper.setOutAnimation(reverNullToFullAnimation);
			flipper.showPrevious();
			hideKeyboard(view);
		}

	}
	
	public void resedConfirmationEmail(String email) {
		UserFunction.getRestHelp().resendConfirmEmail(email);
		UserFunction.getRestHelp().setResendEmailListener(new ResendEmailListener() {

			@Override
			public void onResendSuccess(ResponseBasic r) {
//				UserFunction.popupMessage(R.string.confirm_email_send_success, MeepTogetherRegistrationActivity.this, loading);
				UserFunction.popupResponse(r.getStatus(), MeepTogetherRegistrationActivity.this, loading);
			}

			@Override
			public void onResendFailure(ResponseBasic r) {
//				UserFunction.popupMessage(R.string.confirm_email_send_failure, MeepTogetherRegistrationActivity.this, loading);
				UserFunction.popupResponse(r.getStatus(), MeepTogetherRegistrationActivity.this, loading);
			}

			@Override
			public void onResendTimeout() {
				UserFunction.popupMessage(R.string.please_retry, MeepTogetherRegistrationActivity.this, loading);
			}
		});
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServiceManager.unbindServices(MeepTogetherRegistrationActivity.this);
	}
	
}
