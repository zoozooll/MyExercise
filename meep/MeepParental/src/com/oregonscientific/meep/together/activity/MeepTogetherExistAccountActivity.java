package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.library.database.DatabaseHelper;

public class MeepTogetherExistAccountActivity extends Activity {

	Button btnToLogin;
	Button btnLinkToRegister;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initUI();
		initListeners();
	}

	public void initUI() {
		setContentView(R.layout.activity_exist_account);

		// layout
		// button
		btnToLogin = (Button) findViewById(R.id.btnToLogin);
		btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
	}

	public void initListeners() {
		btnToLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				toOtherActivity(MeepTogetherLoginActivity.class);
			}
		});
		// Link to Register Screen
		btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {
				toOtherActivity(MeepTogetherRegistrationActivity.class);
			}
		});

	}

	public void toOtherActivity(Class<?> cls) {
		// Launch Main Screen
		Intent other = new Intent(getApplicationContext(), cls);
		// Close all views before launching Main
		startActivity(other);
		// Close Login Screen
		finish();
	}
}
