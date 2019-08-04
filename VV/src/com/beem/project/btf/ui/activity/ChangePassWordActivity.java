package com.beem.project.btf.ui.activity;

import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CutomerMyEditText;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.service.CommonService;
import com.butterfly.vv.service.CommonService.Verify_Type;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.butterfly.vv.vv.utils.JsonParseUtils.FindParam;
import com.teleca.jamendo.api.WSError;

public class ChangePassWordActivity extends VVBaseActivity implements
		OnClickListener {
	CutomerMyEditText old_pwd, new_pwd, new_pwd_again;
	Button change_pwd_btn;
	CheckBox checkBox;
	private static final int MODIFYPASSWORDFAIL = -1;
	private static final int MODIFYPASSWORDSUCCESS = 0;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case MODIFYPASSWORDSUCCESS:
					CToast.showToast(mContext, "修改密码成功,请重新登录",
							Toast.LENGTH_SHORT);
					ActivityController.getInstance().relogin();
					break;
				case MODIFYPASSWORDFAIL:
//					if (msg.obj != null) {
//						CToast.showToast(mContext,
//								"修改密码失败:" + (String) msg.obj,
//								Toast.LENGTH_SHORT);
//					} else {
						CToast.showToast(mContext, "修改密码失败",
								Toast.LENGTH_SHORT);
//					}
					break;
				case Constants.SHOW_WSError:
					CToast.showToast(mContext, "网络异常,请重试:", Toast.LENGTH_SHORT);
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
		setContentView(R.layout.change_password_layout);
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		CustomTitleBtn btright = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		btright.setText("忘记密码？");
		btright.setImgVisibility(View.GONE);
		btright.setViewPaddingRight().setVisibility(View.VISIBLE);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setText("");
		btBack.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(ChangePassWordActivity.this,
						ForgetPassWordActivity.class);
				startActivity(intent);
			}
		});
		// 获取两个输入框
		old_pwd = (CutomerMyEditText) findViewById(R.id.old_pwd);
		new_pwd = (CutomerMyEditText) findViewById(R.id.new_pwd);
		new_pwd_again = (CutomerMyEditText) findViewById(R.id.new_pwd_again);
		// 获取修改密码按钮
		change_pwd_btn = (Button) findViewById(R.id.change_pwd_btn);
		change_pwd_btn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == change_pwd_btn) {
			String pwd = new_pwd.getText().toString().trim();
			String pwd_confrim = new_pwd_again.getText().toString().trim();
			if (!TextUtils.isEmpty(pwd)
					&& !TextUtils.isEmpty(old_pwd.getText().toString().trim())) {
				if (pwd.equals(old_pwd.getText().toString().trim())) {
					CToast.showToast(mContext, "新密码与旧密码相同,请重新输入",
							Toast.LENGTH_SHORT);
					return;
				}
			}
			if (!TextUtils.isEmpty(pwd) && !TextUtils.isEmpty(pwd_confrim)) {
				if (pwd.equals(pwd_confrim)) {
					// 修改密码
					new Thread() {
						@Override
						public void run() {
							Message msg = Message.obtain();
							try {
								Map<String, Object> rstMap = CommonService
										.modifyPassword(LoginManager
												.getInstance().getJidParsed(),
												BBSUtils.Md5(old_pwd.getText()
														.toString().trim()),
												BBSUtils.Md5(new_pwd.getText()
														.toString().trim()),
												Verify_Type.oldPw);
								if (rstMap != null) {
									String rstInt = JsonParseUtils
											.getParseValue(rstMap,
													String.class,
													new FindParam("result"));
									if (String.valueOf(Constants.RESULT_OK)
											.equals(rstInt)) {
										msg.what = MODIFYPASSWORDSUCCESS;
									} else {
										String error = JsonParseUtils
												.getParseValue(rstMap,
														String.class,
														new FindParam(
																"description"));
										msg.what = MODIFYPASSWORDFAIL;
										//										msg.obj = error;
									}
								} else {
									msg.what = MODIFYPASSWORDFAIL;
								}
							} catch (WSError e) {
								msg.what = Constants.SHOW_WSError;
							} finally {
								handler.sendMessage(msg);
							}
						};
					}.start();
				} else if (!pwd.equals(pwd_confrim)) {
					CToast.showToast(mContext, "新密码与确认密码不同,请重新输入",
							Toast.LENGTH_SHORT);
				}
			} else {
				CToast.showToast(mContext, "新密码为空,请重新输入", Toast.LENGTH_SHORT);
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
}
