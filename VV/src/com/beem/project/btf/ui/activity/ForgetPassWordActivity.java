package com.beem.project.btf.ui.activity;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.ClearEditText;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.providers.SMSContentObserver;
import com.beem.project.btf.providers.SMSContentObserver.ContentObserverType;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.CountTimer;
import com.butterfly.vv.service.CommonService;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

/**
 * 找回密码界面
 */
public class ForgetPassWordActivity extends VVBaseActivity implements
		IEventBusAction {
	private Button tv_post, next_btn;
	private ClearEditText promptIdentifyCode;
	private ClearEditText tmnum;
	private TextView notepass;
	public static final String IDENTIFYCODE = "IDENTIFYCODE";
	public static final String NUM = "num";
	private String inputNum;
	private SMSContentObserver contentObserver;
	private CountTimer countTimer;
	private static final int CHECKSUCCESS = 0;
	private static final int GETNUMERROR = -1;
	private static final int JSONException = -2;
	private static final int UNBINDEXCEPTION = -3;
	private static final int WSError = -4;
	private static final int OUTOFLIMIT = -5;
	private static final int SENDSUCCESS = -6;
	private static final int SENDFAIL = -7;
	private static final int LOCKED = -8;
	private boolean isPhoneOk;
	private String sendNum;
	private static final String tag = ForgetPassWordActivity.class
			.getSimpleName();
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constants.SMSCONTENT_OBSERVER:
					Bundle data = msg.getData();
					String content = data.getString("messagecode");
					setContent(content);
					break;
				case CHECKSUCCESS:
					countTimer.start();
					tv_post.setEnabled(true);
					setSendNum(sendNum);
					break;
				case GETNUMERROR:
					Toast.makeText(getApplicationContext(), "获取验证码失败,请重试",
							Toast.LENGTH_SHORT).show();
					break;
				case JSONException:
					Toast.makeText(getApplicationContext(), "JSON解析错误",
							Toast.LENGTH_SHORT).show();
					break;
				case UNBINDEXCEPTION:
					Toast.makeText(getApplicationContext(), "此号码没有注册",
							Toast.LENGTH_SHORT).show();
					break;
				case WSError:
					Toast.makeText(getApplicationContext(), "网络错误,请重试",
							Toast.LENGTH_SHORT).show();
					break;
				case OUTOFLIMIT:
					Toast.makeText(getApplicationContext(), "两个星期内超过短信接收次数",
							Toast.LENGTH_SHORT).show();
					break;
				case SENDSUCCESS:
					setSendNum(sendNum);
					skipNetStepActivity();
					break;
				case SENDFAIL:
					Toast.makeText(mContext, "验证码错误,请重新输入", Toast.LENGTH_SHORT)
							.show();
					break;
				case LOCKED:
					tv_post.setEnabled(false);
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
		setContentView(R.layout.forget_password_layout);
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		btBack.setImgResource(R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setText("");
		btBack.setText("找回密码 1/2");
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tmnum = (ClearEditText) findViewById(R.id.tmnum);
		promptIdentifyCode = (ClearEditText) findViewById(R.id.promptIdentifyCode);
		tv_post = (Button) findViewById(R.id.tv_post);
		tv_post.setOnClickListener(new MOnClickListener());
		next_btn = (Button) findViewById(R.id.next_btn);
		notepass = (TextView) findViewById(R.id.notepass);
		next_btn.setOnClickListener(new MOnClickListener());
		tmnum.addTextChangedListener(new TextWatcher() {
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
				if (tmnum.getText().toString().trim().length() > 0) {
					checkPhoneNum(tmnum.getText().toString().trim().length());
					if (isPhoneOk) {
						tv_post.setEnabled(true);
					} else {
						tv_post.setEnabled(false);
					}
				} else {
					tv_post.setEnabled(false);
				}
			}
		});
		promptIdentifyCode.addTextChangedListener(new TextWatcher() {
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
				if (promptIdentifyCode.getText().toString().length() == 6
						&& tmnum.getText().toString().length() > 0) {
					// 输入框输入完才能点击下一步
					next_btn.setEnabled(true);
				} else {
					next_btn.setEnabled(false);
				}
			}
		});
		countTimer = new CountTimer(tv_post, R.string.register_finish);
		contentObserver = new SMSContentObserver(getApplicationContext(),
				mHandler, ContentObserverType.ForgetPasswordType);
		getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, contentObserver);
		EventBus.getDefault().register(this);
	}
	private boolean checkPhoneNum(int length) {
		String regext = null;
		String phoneNum = tmnum.getText().toString().trim();
		if (length == 6) {
			regext = "\\d{6,10}";
		} else if (length == 11) {
			regext = "^1[3-57-9]\\d{9}$";
		}
		if (regext != null) {
			boolean matches = Pattern.matches(regext, phoneNum);
			if (!matches) {
				isPhoneOk = false;
				return isPhoneOk;
			} else {
				isPhoneOk = true;
				return isPhoneOk;
			}
		}
		return false;
	}

	private class MOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.tv_post:
					//只能点一次
					if (!checkPhoneNum(tmnum.getText().toString().trim()
							.length())) {
						return;
					}
					Log.i(tag, "tv_post click");
					if (isPhoneOk) {
						inputNum = tmnum.getText().toString().trim();
						new Thread() {
							@Override
							public void run() {
								JSONObject jsonObject = null;
								Message message = Message.obtain();
								try {
									String retVal = CommonService
											.forgetPassword(inputNum);
									jsonObject = new JSONObject(retVal);
									String result = jsonObject
											.getString("result");
									message.what = LOCKED;
									if (String.valueOf(Constants.RESULT_OK)
											.equals(result)) {
										BBSUtils.closeWindowKeyBoard(mContext);
										message.what = CHECKSUCCESS;
										if (inputNum.length() != 11) {
											Log.i(tag,
													"mobile"
															+ jsonObject
																	.getString("mobile"));
											sendNum = jsonObject
													.getString("mobile");
										}
									} else if ("-1".equals(result)) {
										message.what = GETNUMERROR;
									} else if ("-2".equals(result)) {
										message.what = OUTOFLIMIT;
									} else if ("-5".equals(result)) {
										message.what = UNBINDEXCEPTION;
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
					} else {
						Toast.makeText(mContext, "请输入合法的时光号/手机号码",
								Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.next_btn:
					new Thread() {
						@Override
						public void run() {
							Message msg = Message.obtain();
							final String verfify_code = promptIdentifyCode
									.getText().toString().trim();
							JSONObject jsonObject = null;
							boolean result = false;
							try {
								String retVal = CommonService.verifyVerifyCode(
										inputNum, verfify_code);
								jsonObject = new JSONObject(retVal);
								result = String.valueOf(Constants.RESULT_OK)
										.equals(jsonObject.getString("result"));
								if (result) {
									msg.what = SENDSUCCESS;
								} else {
									msg.what = SENDFAIL;
								}
							} catch (JSONException e) {
								msg.what = JSONException;
							} catch (WSError e) {
								msg.what = WSError;
							} finally {
								mHandler.sendMessage(msg);
							}
						};
					}.start();
					break;
				default:
					break;
			}
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		// 注销短信监听广播
		getContentResolver().unregisterContentObserver(contentObserver);
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	public void setContent(String content) {
		String dynamicPassword = CountTimer.getDynamicPassword(content);
		promptIdentifyCode.setText(dynamicPassword);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (data.getAction() == EventAction.CloseFrontActivity) {
			finish();
		}
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
	public void setSendNum(String sendNum) {
		if (inputNum.length() != 11) {
			notepass.setVisibility(View.VISIBLE);
			notepass.setText("已将验证码发送到" + sendNum + "的手机号,请注意查收");
		} else {
			StringBuffer buffer = new StringBuffer();
			buffer.append(inputNum.substring(0, 3));
			buffer.append("****");
			buffer.append(inputNum.substring(7, 11));
			notepass.setVisibility(View.VISIBLE);
			notepass.setText("已将验证码发送到" + buffer.toString() + "的手机号,请注意查收");
		}
	}
	protected void skipNetStepActivity() {
		Intent intent = new Intent(mContext,
				ForgetPassWordNextStepActivity.class);
		intent.putExtra(IDENTIFYCODE, promptIdentifyCode.getText().toString()
				.trim());
		intent.putExtra(NUM, inputNum);
		startActivity(intent);
		next_btn.setEnabled(false);
	}
}
