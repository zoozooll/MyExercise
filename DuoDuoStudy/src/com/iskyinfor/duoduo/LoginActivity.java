package com.iskyinfor.duoduo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.NumberKeyListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;

import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskyinfor.duoduo.ui.UIPublicConstant;

public class LoginActivity extends Activity {
	private ImageButton loginBtn;
	private EditText accountEdit, pswdEdit;
	private CheckBox mindPswd, autoLogin;
	private ProgressDialog myLoadingDialog;
	IQuerryUserInfor0300020001 userInfor = null;
	SharedPreferences settings = null;
	private LoginTask loginTask = null;
	private boolean flag;
	char inputInfo[] = UIPublicConstant.inputInfo;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.login_activity);
		initView();
	}

	private void initView() {
		accountEdit = (EditText) findViewById(R.id.user_edit_account);
		pswdEdit = (EditText) findViewById(R.id.user_edit_password);

		accountEdit.setText("0002");
		pswdEdit.setText("1234");

		settings = getSharedPreferences(UIPublicConstant.UserInfo,
				Context.MODE_WORLD_WRITEABLE | Context.MODE_WORLD_READABLE);

		// 防止特殊字符输入
		accountEdit.setKeyListener(new NumberKeyListener() {

			@Override
			public int getInputType() {
				return inputInfo.length;
			}

			@Override
			protected char[] getAcceptedChars() {
				return inputInfo;
			}
		});

		pswdEdit.setKeyListener(new NumberKeyListener() {

			@Override
			public int getInputType() {
				return inputInfo.length;
			}

			@Override
			protected char[] getAcceptedChars() {
				return inputInfo;
			}
		});

		// 登录
		loginBtn = (ImageButton) findViewById(R.id.loginbtn);
		loginBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				settings.edit()
						.putString("account", accountEdit.getText().toString())
						.commit();
				settings.edit()
						.putString("password", pswdEdit.getText().toString())
						.commit();
				loadDataProgress();
				loginTask = new LoginTask(LoginActivity.this, accountEdit
						.getText().toString(), pswdEdit.getText().toString(),myLoadingDialog);
				loginTask.execute();
			}
		});

		// 记住密码
		mindPswd = (CheckBox) findViewById(R.id.check1);
		mindPswd.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				settings.edit().putBoolean("mindCheck", isChecked).commit();
			}
		});

		mindPasswordMethod(); // 判断记住密码的状态

		// 自动登录
		autoLogin = (CheckBox) findViewById(R.id.check2);
		autoLogin.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				settings.edit().putBoolean("autoLogin", isChecked).commit();
			}
		});

		autoLoginMethod(); // 判断自动登录的状态

	}

	// 是否记住密码
	protected void mindPasswordMethod() {
		flag = settings.getBoolean("mindCheck", false);
		mindPswd.setChecked(flag);

		if (flag) {
			accountEdit.setText(settings.getString("account", "").toString());
			pswdEdit.setText(settings.getString("password", ""));
		}
	}

	// 是否自动登录
	private void autoLoginMethod() {
		flag = settings.getBoolean("autoLogin", false);
		autoLogin.setChecked(flag);

		if (flag) {
			loadDataProgress();
			loginTask = new LoginTask(LoginActivity.this, accountEdit.getText()
					.toString(), pswdEdit.getText().toString(),myLoadingDialog);
			loginTask.execute();
		}
	}
	private void loadDataProgress(){
		myLoadingDialog = new ProgressDialog(this);
		myLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		myLoadingDialog.setTitle("Login");
		myLoadingDialog.setMessage("登录中，请稍候片刻。。。");
		myLoadingDialog.setIcon(R.drawable.person);
		myLoadingDialog.setIndeterminate(false);
		myLoadingDialog.setCancelable(true);
		myLoadingDialog.show();
	}
	private void cancelDialog(){
		if(myLoadingDialog!=null){
			myLoadingDialog.dismiss();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.onDestroy();
	}
}