/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.ui.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.util.StringUtils;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.LoginFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.ui.activity.RegisterActivity.RegisterFrgmtStatus;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.update.UploadUtil;
import com.beem.project.btf.update.UploadUtil.UpdateTaskCallback;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.UIHelper;
import com.btf.push.UserInfoPacket;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.VVXMPPUtils;

import de.greenrobot.event.EventBus;

/**
 * Fragment to enter the information required in order to configure a XMPP account.
 */
public class AccountConfigureFragment extends VVBaseActivity implements
		OnClickListener, IEventBusAction {
	private Button mLoginButton, registryButton;
	private TextView login_forget_btn;
	private EditText mAccountJID;
	private EditText mAccountPassword;
	private final PasswordTextWatcher mPasswordTextWatcher = new PasswordTextWatcher();
	private boolean mValidJid;
	private boolean mValidPassword;
	private boolean useSystemAccount;
	private BeemApplication mBeemApplication;
	private static final int LOGIN_REQUEST_CODE = 1;
	private BBSCustomerDialog blurDlg;
	private View layout_photo_logo;
	private ImageView login_photo_logo, login_none_logo;
	private List<Contact> allContacts;
	private ScrollView scrollView;
	//	private SoftKeyBoardSatusView softKeyStatus;
	private LinearLayout ConfigureWraper;
	private String savePhoto;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vv_wizard_account_configure);
		PreferenceManager.setDefaultValues(this, R.xml.vvpreferences, false);
		Application app = getApplication();
		if (app instanceof BeemApplication)
			mBeemApplication = (BeemApplication) app;
		/*if (mSettings.getBoolean("auto_login_vv", false)) {
			if (mBeemApplication.isAccountConfigured() && !BeemApplication.isOff()) {
				// 在线需要重新验证账号密码
				LoginAnim.launch(this, SharedPrefsUtil.getValue(this, SettingKey.account_username, ""),
						SharedPrefsUtil.getValue(this, SettingKey.account_password, ""), LOGIN_REQUEST_CODE);
			}
		}*/
		ConfigureWraper = (LinearLayout) findViewById(R.id.ConfigureWraper);
		ConfigureWraper.setOnClickListener(this);
		registryButton = (Button) findViewById(R.id.regist_btn);
		registryButton.setOnClickListener(this);
		login_forget_btn = (TextView) findViewById(R.id.login_forget_btn);
		login_forget_btn.setText(Html.fromHtml("<u>忘记密码</u>"));
		login_forget_btn.setOnClickListener(this);
		mLoginButton = (Button) findViewById(R.id.login);
		mLoginButton.setOnClickListener(this);
		mAccountJID = (EditText) findViewById(R.id.account_username);
		InputFilter[] orgFilters = mAccountJID.getFilters();
		InputFilter[] newFilters = new InputFilter[orgFilters.length + 1];
		System.arraycopy(orgFilters, 0, newFilters, 0, orgFilters.length);
		newFilters[newFilters.length - 1] = new LoginFilter.UsernameFilterGeneric();
		mAccountJID.setFilters(newFilters);
		mAccountPassword = (EditText) findViewById(R.id.account_password);
		mAccountPassword.addTextChangedListener(mPasswordTextWatcher);
		layout_photo_logo = findViewById(R.id.layout_photo_logo);
		login_photo_logo = (ImageView) findViewById(R.id.login_photo_logo);
		login_none_logo = (ImageView) findViewById(R.id.login_none_logo);
		//softKeyStatus = (SoftKeyBoardSatusView) findViewById(R.id.softkeyStatus);
		//		softKeyStatus.setSoftKeyBoardListener(this);
		scrollView = (ScrollView) findViewById(R.id.scroll);
		mAccountJID.addTextChangedListener(userNameWatcher);
		EventBus.getDefault().register(this);
		loadPhoto(SharedPrefsUtil.getValue(this, SettingKey.account_username,
				""));
		useSystemAccount = mSettings.getBoolean(
				BeemApplication.USE_SYSTEM_ACCOUNT_KEY, false);
		String jidText = SharedPrefsUtil.getValue(this,
				SettingKey.account_token, "");
		mAccountJID.setText(jidText);
		if (useSystemAccount) {
			mAccountPassword.setText("*******"); // dummy password
			mAccountJID.setText(StringUtils.parseName(SharedPrefsUtil.getValue(
					this, SettingKey.account_token, "")));
			mAccountPassword.setEnabled(false);
			mLoginButton.setEnabled(true);
		}
		mAccountJID.setSelection(mAccountJID.getText().toString().length());
		if (getIntent().hasExtra("ErrorMessage")) {
			String error = getIntent().getStringExtra("ErrorMessage");
			onAccountConnectionFailed(error);
		}
		final Button switch_version = (Button) findViewById(R.id.switch_version);
		if (BeemApplication.sDebuggerEnabled) {
			switch_version.setVisibility(View.VISIBLE);
		} else {
			switch_version.setVisibility(View.GONE);
		}
		switch_version.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				AppProperty.getInstance().switchEditVersion(
						!AppProperty.getInstance().isInlineVer);
				switch_version.setText(AppProperty.getInstance().isInlineVer ? "内网版本"
						: "外网版本");
			}
		});
		switch_version.setText(AppProperty.getInstance().isInlineVer ? "内网版本"
				: "外网版本");
	}
	@Override
	public void onStart() {
		super.onStart();
	}
	@Override
	public void onClick(View v) {
		if (v == mLoginButton) {
			if (!mValidJid) {
				CToast.showToast(mContext, "请输入有效的时光号", Toast.LENGTH_SHORT);
//				mAccountJID.requestFocus();
				mAccountJID.setSelection(mAccountJID.getText().length());
				return;
			}
			if (!mValidPassword) {
				CToast.showToast(mContext, "请输入有效的密码", Toast.LENGTH_SHORT);
//				mAccountPassword.requestFocus();  
				mAccountPassword.setSelection(mAccountPassword.getText()
						.length());
				return;
			}
			BBSUtils.closeWindowKeyBoard(this);
			String jid = VVXMPPUtils.makeJidParsed(mAccountJID.getText()
					.toString());
			String password = BBSUtils.Md5(mAccountPassword.getText()
					.toString());
			gotoLogin(jid, password);
		} else if (v == registryButton) {
			RegisterActivity
					.launch(mContext, RegisterFrgmtStatus.CheckRegister);
		} else if (v == login_forget_btn) {
			//LogUtils.v("v == login_forget_btn");
			Intent i = new Intent(this, ForgetPassWordActivity.class);
			startActivity(i);
		} else if (v == ConfigureWraper) {
			// 关闭键盘
			BBSUtils.closeWindowKeyBoard(this);
		}
	}
	private void gotoLogin(String jid, String password) {
		if (!BeemApplication.isNetworkOk()) {
			onAccountConnectionFailed("网络异常");
		} else {
			BeemServiceHelper.getInstance(mBeemApplication).xmppLoginAsync(jid,
					password);
			UIHelper.showDialogForLoading(this, "正在登录", false);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data); 
		/*if (requestCode == LOGIN_REQUEST_CODE) {
			if (resultCode == Activity.RESULT_OK) {
				Intent myintent = new Intent(this, MainpagerActivity.class);
				startActivity(myintent);
				finish();
			} else if (resultCode == Activity.RESULT_CANCELED) {
				if (data != null && data.hasExtra("ErrorMessage")) {
					String errorMessage = data.getStringExtra("ErrorMessage");
					onAccountConnectionFailed(errorMessage);
				}
			}
		} else {
			super.onActivityResult(requestCode, resultCode, data);
		}*/
	}
	/**
	 * Callback called when the account connection failed.
	 */
	private void onAccountConnectionFailed(String errorMessage) {
		// 清空密码框
		mAccountPassword.setText("");
		// 登陆失败提醒框
		if (blurDlg != null && blurDlg.isShowing()) {
			blurDlg.dismiss();
		}
		blurDlg = BBSCustomerDialog.newInstance(mContext, R.style.blurdialog);
		SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
		// 设置标题
		simpleDilaogView.setTitle(errorMessage);
		// 分别设置按钮及定义操作
		simpleDilaogView.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(simpleDilaogView.getmView());
		blurDlg.show();
	}
	/**
	 * Check that the username is really a JID.
	 * @param username the username to check.
	 */
	private void checkUsername(String username) {
		String name = StringUtils.parseName(username);
		if (TextUtils.isEmpty(name)) {
			mValidJid = false;
		} else {
			mValidJid = true;
		}
	}
	/**
	 * Check password.
	 * @param password the password to check.
	 */
	private void checkPassword(String password) {
		if (password.length() > 0)
			mValidPassword = true;
		else
			mValidPassword = false;
	}

	/**
	 * Text watcher to test the existence of a password.
	 */
	private class PasswordTextWatcher implements TextWatcher {
		/**
		 * Constructor.
		 */
		public PasswordTextWatcher() {
		}
		@Override
		public void afterTextChanged(Editable s) {
			checkPassword(s.toString());
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
	}

	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	public void loadPhoto(String jid) {
		if (allContacts == null) {
			new WorkerThread().start();
		} else {
			onEventMainThread(allContacts);
		}
	}
	
	private boolean modifyContactInfo(final UserInfoPacket modiInfoPacket) {
		final Map<UserInfoKey, String> modifyMaps = modiInfoPacket
				.cloneFieldMaps();
		if (modifyMaps.isEmpty()) {
			return false;
		}
		PacketResult result = ContactService.getInstance()
				.modifyContactInfo(modifyMaps);

		if (result.isOk()) {
			modiInfoPacket.clearFileds();
			Contact myContact = LoginManager.getInstance()
					.getMyContact();
			myContact.saveData(modifyMaps);
			EventBus.getDefault().post(
					new EventBusData(EventAction.ModifyContactInfo,
							modifyMaps));
			return true;
		} else {
			return false;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	public void onEventMainThread(List<Contact> map) {
		this.allContacts = map;
		String jid = mAccountJID.getText().toString();
		Contact contact = null;
		//手机号码：
		for (Contact c : allContacts) {
			if (jid.length() == 11) {
				if (jid.equals(c.getPhoneNum())) {
					contact = c;
					break;
				}
			} else {
				if (jid.equals(c.getJIDParsed())) {
					contact = c;
					break;
				}
			}
		}
		if (contact != null) {
			layout_photo_logo.setVisibility(View.VISIBLE);
			login_none_logo.setVisibility(View.GONE);
			contact.displayRoundPhoto(login_photo_logo);
		} else {
			layout_photo_logo.setVisibility(View.GONE);
			login_none_logo.setVisibility(View.VISIBLE);
		}
	}

	/** 加载列表的工作线程 */
	private class WorkerThread extends Thread {
		@Override
		public void run() {
			List<Contact> checkContacts = new ArrayList<Contact>();
			List<Contact> contacts = Contact.queryAll();
			if (contacts != null) {
				checkContacts.addAll(contacts);
			}
			EventBus.getDefault().post(checkContacts);
		}
	}

	private TextWatcher userNameWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			loadPhoto(mAccountJID.getText().toString());
			checkUsername(s.toString());
		}
	};

	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (data.getAction() == null) {
			return;
		}
		switch (data.getAction()) {
			case RegisterSuccess: {
				Bundle bundle = (Bundle) data.getMsg();
				String jid = (String) bundle.get("jid");
				String pass = (String) bundle.get("pass");
				savePhoto = (String) bundle.get("savePhoto");
				SharedPrefsUtil.putValue(mContext, SettingKey.savePhoto,
						savePhoto);
				gotoLogin(jid, pass);
			}
				break;
			case CloseFrontActivity: {
				finish();
				break;
			}
			case LOGIN_TIMEOUT:
				UIHelper.hideDialogForLoading();
				CToast.showToast(this, R.string.remote_server_timeout,
						Toast.LENGTH_SHORT);
				finish();
				break;
			case LOGIN_FAILED:
				UIHelper.hideDialogForLoading();
				CToast.showToast(this, R.string.error_login_authentication,
						Toast.LENGTH_SHORT);
				finish();
				break;
			case LOGIN_SUCCESS:
				if (!TextUtils.isEmpty(savePhoto)) {
					String url = AppProperty.getInstance().VVAPI
							+ AppProperty.getInstance().UPLOAD_PORTRAIT;
					UploadUtil.updateAccountIcon(savePhoto, url, new UpdateTaskCallback() {
						@Override
						public void preExecute() {
						}
						@Override
						public void postExecute(String[] uploadUrl) {
							if (uploadUrl != null) {
								// 删除拍照图片
								new File(savePhoto).delete();
								CToast.showToast(mContext,
										R.string.image_uploaded_successfully,
										Toast.LENGTH_SHORT);
							} else {
								CToast.showToast(mContext,
										R.string.image_upload_failed,
										Toast.LENGTH_SHORT);
							}
							UIHelper.hideDialogForLoading();
							finish();
						}
						@Override
						public void cancelExecute() {
						}
						@Override
						public void onUploading(String[] path) {
							String big_path = path[0];
							String small_path = path[1];
							UserInfoPacket modifyMap = new UserInfoPacket();
							modifyMap.setField(UserInfoKey.big, big_path);
							modifyMap.setField(UserInfoKey.small, small_path);
							modifyContactInfo(modifyMap);
						}
					});
				} else {
					UIHelper.hideDialogForLoading();
					finish();
				}
				break;
			default:
				break;
		}
	}
	
	
}
