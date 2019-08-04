package com.oregonscientific.meep.together.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.ViewSwitcher;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.CustomDialog.OnClickOkButtonListener;
import com.oregonscientific.meep.together.bean.RegisterMeeper;
import com.oregonscientific.meep.together.library.rest.listener.OnRegisterMeeperListener;

public class MeepTogetherStartUsingActivity extends Activity implements
		ViewSwitcher.ViewFactory {

	Context context;
	ViewFlipper flipper;
	Button next;
	ImageButton barImageButtonBack;

	View LayoutCreate;
	EditText textFirstName;
	EditText textLastName;
	// EditText id;
	EditText textYear;
	EditText textMonth;
	EditText textDay;

	MyProgressDialog loading;
	private String sn = "INITIALUSER";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		context = getApplicationContext();
		initUI();
		initListeners();
		if (getIntent().getStringExtra("sn") != null) {
			sn = getIntent().getStringExtra("sn");
		}
	}

	public void initUI() {
		setContentView(R.layout.layout_start_using);
		loading = UserFunction.initLoading(this);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		next = (Button) findViewById(R.id.btnNext);

		barImageButtonBack = (ImageButton) findViewById(R.id.barImageButtonBack);

		Animation in = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
		Animation out = AnimationUtils.loadAnimation(this, android.R.anim.fade_out);
		flipper.setInAnimation(in);
		flipper.setOutAnimation(out);

		LayoutCreate = findViewById(R.id.LayoutCreate);
		textFirstName = (EditText) LayoutCreate.findViewById(R.id.textFirstName);
		textLastName = (EditText) LayoutCreate.findViewById(R.id.textLastName);
		// id = (EditText) LayoutCreate.findViewById(R.id.textMeeptag);
		textYear = (EditText) LayoutCreate.findViewById(R.id.textYear);
		textMonth = (EditText) LayoutCreate.findViewById(R.id.textMonth);
		textDay = (EditText) LayoutCreate.findViewById(R.id.textDay);

	}

	public void initListeners() {
		next.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (flipper.getDisplayedChild()) {
				case 0:
					checkRegister();
					hideKeyboard(v);
					// flipper.showNext();
					break;
				case 1:
					// toMain();
					setResult(RESULT_OK);
					CustomDialog dialog = new CustomDialog(MeepTogetherStartUsingActivity.this, R.string.create_child_account_success);
					dialog.setOnClickOkButtonListener(new OnClickOkButtonListener() {

						@Override
						public void onClickOk() {
							finish();
						}
					});
					dialog.setCancelable(false);
					dialog.show();
					break;
				}
			}
		});
		barImageButtonBack.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (flipper.getDisplayedChild()) {
				case 0:
					setResult(RESULT_CANCELED);
					finish();
					break;
				case 1:
					// flipper.setDisplayedChild(0);
					UserFunction.popupMessage(R.string.have_register, MeepTogetherStartUsingActivity.this, loading);
					break;
				}
			}
		});
		UserFunction.getRestHelp().setOnRegisterMeeperListener(new OnRegisterMeeperListener() {

			@Override
			public void onRegisterMeeperSuccess() {
				resigninAccount();
				onCreateSuccess();

			}

			@Override
			public void onRegisterMeeperFailure(String error) {
				UserFunction.popupResponse(error, MeepTogetherStartUsingActivity.this, loading);
			}

			@Override
			public void onRegisterMeeperTimeout() {
				UserFunction.popupMessage(R.string.please_retry, MeepTogetherStartUsingActivity.this, loading);
			}
		});
	}

	public void checkRegister() {
		String firstName = textFirstName.getText().toString().trim();
		String lastName = textLastName.getText().toString().trim();
		String day = textDay.getText().toString().trim();
		String month = textMonth.getText().toString().trim();
		String year = textYear.getText().toString().trim();
		if (firstName == null || firstName.equals("") || lastName == null
				|| lastName.equals("")) {
			UserFunction.popupMessage(R.string.register_null_name, MeepTogetherStartUsingActivity.this, loading);
		} else if (day == null || day.equals("") || month == null
				|| month.equals("") || year == null || year.equals("")) {
			UserFunction.popupMessage(R.string.register_null_birthday, MeepTogetherStartUsingActivity.this, loading);
		} else if (!UserFunction.checkBirthday(year, month, day)) {
			UserFunction.popupMessage(R.string.register_wrong_birthday, MeepTogetherStartUsingActivity.this, loading);
		} else {
			loading.show();
			// others
			RegisterMeeper user = new RegisterMeeper();
			user.setFirst_name(firstName);
			user.setLast_name(lastName);
			// user.setMeeptag(id.getText().toString());
			user.setDob(year + "-" + month + "-" + day);
			user.setSerial_no(sn);
			if (UserFunction.isNetworkAvailable(this)) {
				UserFunction.getRestHelp().registerMeeper(user);
			} else {
				UserFunction.popupMessage(R.string.no_network, this, loading);
			}
		}
	}

	public void onCreateSuccess() {
		loading.dismiss();
		flipper.setDisplayedChild(1);

	}

	// public void toMain() {
	// // Launch Main Screen
	// Intent main = new Intent(getApplicationContext(),
	// MeepTogetherMainActivity.class);
	// // Close all views before launching Main
	// main.putExtra("online", true);
	// startActivity(main);
	// // Close start Screen
	// finish();
	// }

	@Override
	public View makeView() {
		TextView tv = new TextView(this);
		tv.setTextSize(32);
		tv.setTextColor(Color.WHITE);
		return tv;

	}

	public void onBackPressed() {
		super.onBackPressed();
		loading.dismiss();
	}

	public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	public void resigninAccount() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(MeepTogetherStartUsingActivity.this, ServiceManager.ACCOUNT_SERVICE);
				accountManager.signIn();
				Account account;
				try {
					Thread.sleep(3000);

					account = accountManager.getLoggedInAccount();
					for (int i = 0; i < 5; i++) {
						Utils.printLogcatDebugMessage(account.objectToString());
						if (account.getNickname().equals("Guest")) {
							Thread.sleep(1000);
							account = accountManager.getLoggedInAccount();
						} else {
							break;
						}
					}

				} catch (Exception e) {

				}
				account = accountManager.getLoggedInAccountBlocking();
				accountManager.signIn(account);

			}

		});
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		ServiceManager.unbindServices(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		if (loading != null) {
			loading.dismiss();
		}
	}

}
