package com.beem.project.btf.ui.activity;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.service.CommonService;
import com.butterfly.vv.service.CommonService.Verify_Type;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

public class ForgetPassWordNextStepActivity extends VVBaseActivity implements
		OnClickListener {
	protected static final String tag = ForgetPassWordNextStepActivity.class
			.getSimpleName();
	private String identifyCode;
	private EditText input_pwd, re_input_pwd;
	private Button change_pwd_btn;
	private String num;
	private String oldPassword;
	private String token;
	private static final int MODIFSUCCESS = 0;
	private static final int JSONException = -1;
	private static final int WSError = -2;
	private static final int MODIFFAIL = -3;
	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MODIFSUCCESS:
					Toast.makeText(mContext, "修改密码成功", Toast.LENGTH_SHORT)
							.show();
					skipAccountConfigureFragment(ForgetPassWordNextStepActivity.this);
					break;
				case JSONException:
					Toast.makeText(mContext, "数据错误", Toast.LENGTH_SHORT)
							.show();
					break;
				case WSError:
					Toast.makeText(mContext, "网络错误,请重试", Toast.LENGTH_SHORT)
							.show();
					break;
				case MODIFFAIL:
					Toast.makeText(mContext, "修改密码失败", Toast.LENGTH_SHORT)
							.show();
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.forget_password_nextstep_layout);
		identifyCode = getIntent().getStringExtra(
				ForgetPassWordActivity.IDENTIFYCODE);
		num = getIntent().getStringExtra(ForgetPassWordActivity.NUM);
		if (oldPassword == null) {
			token = identifyCode;
		} else {
			token = oldPassword;
		}
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		input_pwd = (EditText) findViewById(R.id.input_pwd);
		re_input_pwd = (EditText) findViewById(R.id.re_input_pwd);
		change_pwd_btn = (Button) findViewById(R.id.change_pwd_btn);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setVisibility(View.GONE);
		btBack.setTextAndImgRes("找回密码 2/2", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		change_pwd_btn.setOnClickListener(this);
	}
	private void skipAccountConfigureFragment(Context context) {
		Intent intent = new Intent(context, AccountConfigureFragment.class);
		startActivity(intent);
		EventBus.getDefault().post(
				new EventBusData(EventAction.CloseFrontActivity, null));
		finish();
	}
	@Override
	public void onClick(View v) {
		if (v == change_pwd_btn) {
			if (input_pwd.getText().toString().isEmpty()) {
				input_pwd.setError("密码不能为空！");
				return;
			}
			if (!input_pwd.getText().toString()
					.equals(re_input_pwd.getText().toString())) {
				re_input_pwd.setError("两次密码不一致！");
				return;
			}
			new Thread() {
				@Override
				public void run() {
					String pass_md5 = BBSUtils.Md5(input_pwd.getText()
							.toString().trim());
					JSONObject jsonObject = null;
					String rstInt = null;
					Message message = Message.obtain();
					try {
						String ret = CommonService.modifyPassword(num,
								Verify_Type.identifyCode, "", pass_md5);
						jsonObject = new JSONObject(ret);
						rstInt = jsonObject.getString("result");
						if ("0".equals(rstInt)) {
							BBSUtils.closeWindowKeyBoard(mContext);
							message.what = MODIFSUCCESS;
						} else if ("-1".equals(rstInt)) {
							message.what = MODIFFAIL;
						}
					} catch (JSONException e) {
						message.what = JSONException;
					} catch (WSError e) {
						message.what = WSError;
					} finally {
						mHandler.sendMessage(message);
					}
				};
			}.start();
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	//点击屏幕关闭软键盘
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			if (getCurrentFocus() != null
					&& getCurrentFocus().getWindowToken() != null) {
				im.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
						InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}
		return super.onTouchEvent(event);
	}
}
