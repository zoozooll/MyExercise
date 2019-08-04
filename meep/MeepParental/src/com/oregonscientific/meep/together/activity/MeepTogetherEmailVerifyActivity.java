package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.library.rest.listener.ResendEmailListener;

public class MeepTogetherEmailVerifyActivity extends Activity {

	Button back;
	ImageButton barLeft;
	Button btnResend;
	MyProgressDialog loading;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		initUI();
		initListeners();
	}

	public void initUI() {
		setContentView(R.layout.layout_email_verify);
		loading = UserFunction.initLoading(this);
		back = (Button) findViewById(R.id.btnBack);
		barLeft = (ImageButton) findViewById(R.id.barImageButtonBack);
		btnResend = (Button) findViewById(R.id.btnResend);
	}

	public void initListeners() {
		barLeft.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		back.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btnResend.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				loading.show();
				resedConfirmationEmail(getIntent().getExtras().getString("email"));
			}
		});
	}

	public void resedConfirmationEmail(String email) {
		UserFunction.getRestHelp().resendConfirmEmail(email);
		UserFunction.getRestHelp().setResendEmailListener(
				new ResendEmailListener() {

					@Override
					public void onResendSuccess(ResponseBasic r) {
//						UserFunction.popupMessage(R.string.confirm_email_send_success,
//								MeepTogetherEmailVerifyActivity.this,loading);
				    	UserFunction.popupResponse(r.getStatus(), MeepTogetherEmailVerifyActivity.this, loading);
					}

					@Override
					public void onResendFailure(ResponseBasic r) {
//						UserFunction.popupMessage(R.string.confirm_email_send_failure,
//								MeepTogetherEmailVerifyActivity.this,loading);
						UserFunction.popupResponse(r.getStatus(), MeepTogetherEmailVerifyActivity.this, loading);
					}

					@Override
					public void onResendTimeout() {
						UserFunction.popupMessage(R.string.please_retry,
								MeepTogetherEmailVerifyActivity.this,loading);
					}
				});
	}

}
