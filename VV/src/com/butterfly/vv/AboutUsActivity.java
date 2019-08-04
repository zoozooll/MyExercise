package com.butterfly.vv;

import org.json.JSONException;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.UpdateManager;
import com.beem.project.btf.manager.UpdateMessage;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.AppFileDownUtils;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.IntroductionActivity;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;

/**
 * 关于时光机界面
 */
public class AboutUsActivity extends VVBaseFragmentActivity implements
		OnClickListener, IEventBusAction {
	protected static final String tag = AboutUsActivity.class.getSimpleName();
	private Context mContext;
	private Button about_btn_introduction;
	private Button about_btn_checknewversion;
	private String URL;
	private String info;
	private TextView versionname;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			URL = (String) msg.obj;
			switch (msg.what) {
				case Constants.SHOW_CHOOSEUPDATE:
					UpdateManager.showUpdateDialog(mContext, URL,
							Constants.SHOW_CHOOSEUPDATE, info);
					break;
				case Constants.SHOW_FORCEUPDATE:
					UpdateManager.showUpdateDialog(mContext, URL,
							Constants.SHOW_FORCEUPDATE, info);
					break;
				case Constants.SHOW_NOINFO:
					CToast.showToast(mContext, "没有发现新版本", Toast.LENGTH_SHORT);
					break;
				case Constants.SHOW_ISDOWNING:
					CToast.showToast(mContext, "正在下载中...", Toast.LENGTH_SHORT);
					break;
				case Constants.SHOW_WSError:
					CToast.showToast(mContext, "网络错误,请重试", Toast.LENGTH_SHORT);
					break;
				case Constants.SHOW_JSONException:
					CToast.showToast(mContext, "JSON解析错误", Toast.LENGTH_SHORT);
					break;
				default:
					break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_aboutvv);
		mContext = this;
		CustomTitleBtn back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		TextView contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setText(R.string.login_register_aboutvv);// 关于时光机
		CustomTitleBtn timeflyProtocol = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		timeflyProtocol.setText(R.string.login_register_vvprotocol)
				.setImgVisibility(View.VISIBLE).setViewPaddingRight()
				.setVisibility(View.VISIBLE);// 用户协议
		back.setOnClickListener(this);
		timeflyProtocol.setOnClickListener(this);
		versionname = (TextView) findViewById(R.id.about_tv_versionname);
		versionname.setText(BBSUtils.getVersionName() + "版");
		about_btn_introduction = (Button) findViewById(R.id.about_btn_introduction);
		about_btn_checknewversion = (Button) findViewById(R.id.about_btn_checknewversion);
		about_btn_introduction.setOnClickListener(this);
		about_btn_checknewversion.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 返回
			case R.id.leftbtn1: {
				finish();
				break;
			}
			// 用户协议
			case R.id.rightbtn1: {
				Intent intent = new Intent(mContext, VVprotocolActivity.class);
				startActivity(intent);
				break;
			}
			// 功能介绍
			case R.id.about_btn_introduction: {
				Intent intent = new Intent(mContext, IntroductionActivity.class);
				startActivity(intent);
				break;
			}
			// 软件更新
			case R.id.about_btn_checknewversion: {
				update(AppProperty.getInstance().UPDATE_URL);
				break;
			}
			default:
				break;
		}
	}
	private void update(final String url) {
		new Thread() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					UpdateMessage udp = UpdateManager.update(url);
					String version = udp.getVersion();
					String url = udp.getUrl();
					info = udp.getInfo();
					String versionName = BBSUtils.getVersionName();
					boolean isDowningState = SharedPrefsUtil.getValue(mContext,
							SettingKey.IsDowningState, false);
					if (version != null) {
						if (version.compareTo(versionName) > 0) {
							if (!isDowningState) {
								if (!versionName.substring(0, 1).equals(
										version.substring(0, 1))) {
									msg.what = Constants.SHOW_FORCEUPDATE;
								} else {
									msg.what = Constants.SHOW_CHOOSEUPDATE;
								}
								msg.obj = url;
							} else {
								msg.what = Constants.SHOW_ISDOWNING;
							}
						} else {
							msg.what = Constants.SHOW_NOINFO;
						}
					} else {
						msg.what = Constants.SHOW_NOINFO;
					}
				} catch (WSError e) {
					msg.what = Constants.SHOW_WSError;
				} catch (JSONException e) {
					msg.what = Constants.SHOW_JSONException;
				} finally {
					mHandler.sendMessage(msg);
				}
			};
		}.start();
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		if (Integer.valueOf(AppFileDownUtils.MSG_DOWNING).equals(data.getMsg())) {
			SharedPrefsUtil.putValue(mContext, SettingKey.IsDowningState, true);
		} else if (Integer.valueOf(AppFileDownUtils.MSG_FINISH).equals(
				data.getMsg())) {
			SharedPrefsUtil
					.putValue(mContext, SettingKey.IsDowningState, false);
		} else if (Integer.valueOf(AppFileDownUtils.MSG_FAILURE).equals(
				data.getMsg())) {
			SharedPrefsUtil
					.putValue(mContext, SettingKey.IsDowningState, false);
		}
	}
}
