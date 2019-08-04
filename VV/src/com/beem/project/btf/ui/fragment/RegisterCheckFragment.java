package com.beem.project.btf.ui.fragment;

import java.util.regex.Pattern;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.providers.SMSContentObserver;
import com.beem.project.btf.providers.SMSContentObserver.ContentObserverType;
import com.beem.project.btf.ui.activity.RegisterActivity;
import com.beem.project.btf.ui.activity.RegisterActivity.IFragmentTransaction;
import com.beem.project.btf.ui.activity.RegisterActivity.RegisterFrgmtStatus;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.CountTimer;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

public class RegisterCheckFragment extends Fragment implements
		IFragmentTransaction, OnClickListener {
	private View view;
	protected static final int TESTSUCCESS = 3;
	protected static final int TESTFAIL = 4;
	private static final int CHECKSUCCESS = 5;
	private static final int CHECKFAIL = 6;
	private static final int OUTOFSECOND = 7;
	private static final int REGISTERED = 8;
	private static final int NETWORKERROR = 9;
	private static final int LOCKED = 10;
	protected static final String tag = RegisterCheckFragment.class
			.getSimpleName();
	private EditText testnum;
	private EditText phonenum;
	private Context mContext;
	private Button register_btn, get_num;
	private boolean isPhoneOk, isTestNumOk;
	private SMSContentObserver contentObserver;
	private RegisterFrgmtStatus status;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case Constants.SMSCONTENT_OBSERVER:
					Bundle data = msg.getData();
					String content = data.getString("messagecode");
					if (content != null) {
						setContent(content);
					}
					break;
				case LOCKED:
					testnum.setEnabled(false);
					break;
				case TESTSUCCESS:
					testnum.setEnabled(true);
					CountTimer myCountTimer = new CountTimer(get_num,
							R.string.register_finish, mContext);
					myCountTimer.start();
					break;
				case TESTFAIL:
					Toast.makeText(mContext, "获取验证码失败", Toast.LENGTH_SHORT)
							.show();
					get_num.setEnabled(true);
					break;
				case CHECKSUCCESS:
					RegisterActivity activity = (RegisterActivity) getActivity();
					activity.onSendBundle(RegisterFrgmtStatus.EditRegisterInfo,
							getValidContent());
					break;
				case CHECKFAIL:
					Toast.makeText(mContext, "验证码错误,请重新输入", Toast.LENGTH_SHORT)
							.show();
					get_num.setEnabled(true);
					break;
				case OUTOFSECOND:
					Toast.makeText(mContext, "两个星期内超过短信接收次数",
							Toast.LENGTH_SHORT).show();
					get_num.setEnabled(true);
					break;
				case REGISTERED:
					Toast.makeText(mContext, "此手机号已注册", Toast.LENGTH_SHORT)
							.show();
					get_num.setEnabled(true);
					break;
				case NETWORKERROR:
					Toast.makeText(mContext, "网络异常,请重试", Toast.LENGTH_SHORT)
							.show();
					get_num.setEnabled(true);
					break;
				case Constants.SHOW_JSONException:
					Toast.makeText(mContext, "Json解析错误", Toast.LENGTH_SHORT)
							.show();
					get_num.setEnabled(true);
					break;
				default:
					break;
			}
		}
	};

	public RegisterCheckFragment(RegisterFrgmtStatus status) {
		this.status = status;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		mContext = getActivity();
		view = inflater.inflate(R.layout.regist_test, container, false);
		// 下一步按钮
		register_btn = (Button) view.findViewById(R.id.register_btn);
		register_btn.setOnClickListener(this);
		// 发送验证码
		get_num = (Button) view.findViewById(R.id.get_num);
		get_num.setOnClickListener(this);
		// 验证码输入框
		testnum = (EditText) view.findViewById(R.id.testnum);
		phonenum = (EditText) view.findViewById(R.id.phonenum);
		TelephonyManager mTelephonyMgr = (TelephonyManager) mContext
				.getSystemService(Context.TELEPHONY_SERVICE);
		try {
			String line1Number = mTelephonyMgr.getLine1Number();
			phonenum.setText(line1Number);
		} catch (Exception e) {
			CToast.showToast(mContext, "禁止了读取号码状态的权限,无法获取号码",
					Toast.LENGTH_SHORT);
		}
		phonenum.addTextChangedListener(new TextWatcher() {
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
				if (phonenum.getText().toString().trim().length() > 0) {
					checkTestNum();
					if (isPhoneOk && isTestNumOk) {
						register_btn.setEnabled(true);
					} else {
						register_btn.setEnabled(false);
					}
				}
			}
		});
		testnum.addTextChangedListener(new TextWatcher() {
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
				int index = phonenum.getSelectionStart();// 获取光标位置
				phonenum.setSelection(index);// 重新设置光标位置
				checkUserPhoNo();
				checkTestNum();
				if (isPhoneOk && isTestNumOk) {
					register_btn.setEnabled(true);
				} else {
					register_btn.setEnabled(false);
				}
			}
		});
		return view;
	}
	@Override
	public boolean onBackPressed() {
		FragmentManager frgmtManager = getActivity()
				.getSupportFragmentManager();
		FragmentTransaction frgmtTransaction = frgmtManager.beginTransaction();
		frgmtTransaction.remove(this);
		getActivity().finish();
		return true;
	}
	@Override
	public void onClick(View v) {
		if (v == register_btn) {
			if (isPhoneOk && isTestNumOk) {
				final String phoneNum = getValidContent();
				final String testNum = testnum.getText().toString();
				final String url = AppProperty.getInstance().VVAPI
						+ AppProperty.getInstance().REGIST_SEND;
				new Thread() {
					@Override
					public void run() {
						Message message = Message.obtain();
						String doPost;
						JSONObject jsonObject = null;
						String respCode = null;
						try {
							doPost = Caller.doPost(url, new String[] {
									"mobile", "verify_code", "flag" },
									new String[] { phoneNum, testNum, "0" });
							jsonObject = new JSONObject(doPost);
							respCode = jsonObject.getString("result");
							if (String.valueOf(Constants.RESULT_OK).equals(
									respCode)) {
								BBSUtils.closeWindowKeyBoard(mContext);
								message.what = CHECKSUCCESS;
							} else if ("-4".equals(respCode)) {
								message.what = CHECKFAIL;
							}
						} catch (WSError e) {
							message.what = NETWORKERROR;
						} catch (JSONException e) {
							message.what = Constants.SHOW_JSONException;
						} finally {
							mHandler.sendMessage(message);
						}
					};
				}.start();
			}
		} else if (v == get_num) {
			if (get_num.isEnabled()) {
				get_num.setEnabled(false);
				checkUserPhoNo2();
				if (isPhoneOk) {
					String phone = getValidContent();
					final String url = AppProperty.getInstance().VVAPI
							+ AppProperty.getInstance().GET_CHECKNUM + phone;
					new Thread() {
						@Override
						public void run() {
							String doGet = null;
							JSONObject jsonObject = null;
							String resp_code = null;
							Message msg = Message.obtain();
							try {
								doGet = Caller.doGet(url);
								jsonObject = new JSONObject(doGet);
								resp_code = jsonObject.getString("result");
								msg.what = LOCKED;
								if ("0".equals(resp_code)) {
									msg.what = TESTSUCCESS;
								} else if ("-1".equals(resp_code)) {
									msg.what = TESTFAIL;
								} else if ("-2".equals(resp_code)) {
									msg.what = OUTOFSECOND;
								} else if ("-3".equals(resp_code)) {
									msg.what = REGISTERED;
								}
							} catch (WSError e) {
								msg.what = NETWORKERROR;
							} catch (JSONException e) {
								msg.what = Constants.SHOW_JSONException;
							} finally {
								mHandler.sendMessage(msg);
							}
						}
					}.start();
				}
			} else {
				return;
			}
		}
	}
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		contentObserver = new SMSContentObserver(getActivity(), mHandler,
				ContentObserverType.RegisterType);
		getActivity().getContentResolver().registerContentObserver(
				Uri.parse("content://sms"), true, contentObserver);
	}
	private void checkTestNum() {
		String regext = "^\\d{6}$";
		String testNum = testnum.getText().toString().trim();
		boolean matches = Pattern.matches(regext, testNum);
		if (!matches) {
			isTestNumOk = false;
		} else {
			isTestNumOk = true;
		}
	}
	private void checkUserPhoNo() {
		String regex = "^1[3-57-9]\\d{9}$";
		String validContent = getValidContent();
		if (validContent == null) {
			Toast.makeText(mContext, "手机号码为空,请重试	", Toast.LENGTH_SHORT).show();
			isPhoneOk = false;
		} else {
			if (Pattern.matches(regex, validContent)) {
				isPhoneOk = true;
			} else {
				Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT)
						.show();
				isPhoneOk = false;
			}
		}
	}
	private void checkUserPhoNo2() {
		String regex = "^1[3-57-9]\\d{9}$";
		String validContent = getValidContent();
		if (validContent == null) {
			Toast.makeText(mContext, "手机号码为空,请重试	", Toast.LENGTH_SHORT).show();
			isPhoneOk = false;
			get_num.setEnabled(true);
		} else {
			if (Pattern.matches(regex, validContent)) {
				isPhoneOk = true;
			} else {
				Toast.makeText(mContext, "请输入正确的电话号码", Toast.LENGTH_SHORT)
						.show();
				isPhoneOk = false;
				get_num.setEnabled(true);
			}
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		// 注销短信监听广播
		getActivity().getContentResolver().unregisterContentObserver(
				contentObserver);
	}
	public void setContent(String content) {
		String dynamicPassword = CountTimer.getDynamicPassword(content);
		testnum.setText(dynamicPassword);
	}

	public interface IFragment2ActivityTransaction {
		public void onSendBundle(RegisterFrgmtStatus status, String phone);
	}

	private String getValidContent() {
		String phone = phonenum.getText().toString().trim();
		if (TextUtils.isEmpty(phone)) {
			return null;
		} else {
			if (phone.startsWith("86")) {
				phone = phone.substring(2, 13);
			} else if (phone.startsWith("+86")) {
				phone = phone.substring(3, 14);
			}
			return phone;
		}
	}
}
