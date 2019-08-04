/**
 * 
 */
package com.beem.project.btf.ui.activity;

import java.util.HashMap;
import java.util.Map;
import org.json.JSONException;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.manager.UpdateManager;
import com.beem.project.btf.manager.UpdateMessage;
import com.beem.project.btf.network.BDLocator;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.TimeflyLocalNotify;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.fragment.MainpagerAbstractFragment;
import com.beem.project.btf.ui.fragment.MainpagerAbstractFragment.OnMainpagerFramentCallback;
import com.beem.project.btf.ui.fragment.MainpagerAbstractFragment.TabName;
import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;
import com.beem.project.btf.ui.views.TimeflyDueRemindView;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.vv.utils.CToast;
import com.teleca.jamendo.api.WSError;
//import com.umeng.analytics.MobclickAgent;

import de.greenrobot.event.EventBus;

/**
 * @author hongbo ke
 */
public class MainpagerActivity extends VVBaseFragmentActivity implements
		OnClickListener, OnMainpagerFramentCallback, IEventBusAction {
	private static final String TAG = "MainpagerActivity";
	public static final String MSG_UPDATE_CHECKED = "MSG_UPDATE_CHECKED";
	private static final int MSG_AUTOAUTHENTIFICATE_COMPLETED = 0x60901;
	private FragmentManager fm;
	private View layout_content;
	private View btn_tagitem0;
	private View btn_tagitem1;
	private View btn_tagitem2;
	private View btn_tagitem3;
	private ViewGroup layout_tags;
	private Map<String, MainpagerAbstractFragment> fragments = new HashMap<String, MainpagerAbstractFragment>(
			4);
	private Handler mHandler;
	private String curFragmentTag;
	private boolean autoAuthentificateCompleted;
	protected View msgBadgeViewNumIcon;
//	private BroadcastReceiver mScreenReceiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initAccountStatus();
		BDLocator.getInstance().requestLatlon();
//		MobclickAgent.openActivityDurationTrack(false);
		setContentView(R.layout.activity_mainpager);
		initViews();
		fm = getSupportFragmentManager();
		EventBus.getDefault().register(this);
		update(AppProperty.getInstance().UPDATE_URL);
		mHandler = new Handler(getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case Constants.SHOW_CHOOSEUPDATE:
						UpdateMessage updateMsg = (UpdateMessage) msg.obj;
						String url = updateMsg.getUrl();
						String force = updateMsg.getForce();
						String info = updateMsg.getInfo();
						if ("1".equals(force)) {
							UpdateManager.showUpdateDialog(
									MainpagerActivity.this, url,
									Constants.SHOW_FORCEUPDATE, info);
						} else {
							UpdateManager.showUpdateDialog(
									MainpagerActivity.this, url,
									Constants.SHOW_CHOOSEUPDATE, info);
						}
						break;
					case MSG_AUTOAUTHENTIFICATE_COMPLETED: {
						MainpagerAbstractFragment curFragment = fragments
								.get(curFragmentTag);
						if (curFragment != null) {
							curFragment.autoAuthentificateCompleted();
						}
					}
						break;
					default: {
						Object[] data = (Object[]) msg.obj;
						final ImageFolder folder = (ImageFolder) data[0];
						final String time = (String) data[1];
						final BBSCustomerDialog blurDlg = BBSCustomerDialog
								.newInstance(mContext, R.style.blurdialog);
						TimeflyDueRemindView remindview = new TimeflyDueRemindView(
								mContext, true);
						remindview.setText(time);
						remindview.setBtnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								blurDlg.dismiss();
								Intent i = new Intent(MainpagerActivity.this, ShareRankingActivity.class);
								i.putExtra("jid", folder.getJid());
								i.putExtra("gid", folder.getGid());
								i.putExtra("gidCreatTime", folder.getCreateTime());
								startActivity(i);
								ImageFolderNotify notifyDB = new ImageFolderNotify();
								notifyDB.setField(DBKey.jid, LoginManager.getInstance()
										.getJidParsed());
								notifyDB.setField(DBKey.gid, folder.getGid());
								notifyDB.setField(DBKey.createTime,
										folder.getCreateTime());
								notifyDB.setField(DBKey.notify_valid, Valid.close.val);
								notifyDB.saveToDatabaseAsync();
								// 更新界面数据
								EventBusData data = new EventBusData(
										EventAction.CheckTimeflyNotify,
										new Object[] { folder.getCreateTime(),
												Valid.close.val });
								EventBus.getDefault().post(data);
							}
						});
						blurDlg.setContentView(remindview.getmView());
						blurDlg.getWindow().setType(
								WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
						blurDlg.setCancelable(true);
						blurDlg.show();
					}
						break;
				}
			}
		};
		switchFragments(MainpagerAbstractFragment.TabName.TIMEFLY);
	}
	private void initViews() {
		layout_content = findViewById(R.id.layout_content);
		layout_tags = (ViewGroup) findViewById(R.id.layout_tags);
		btn_tagitem0 = findViewById(R.id.btn_tagitem0);
		btn_tagitem1 = findViewById(R.id.btn_tagitem1);
		btn_tagitem2 = findViewById(R.id.btn_tagitem2);
		btn_tagitem3 = findViewById(R.id.btn_tagitem3);
		msgBadgeViewNumIcon = findViewById(R.id.main_tab_new_tv);
		btn_tagitem0.setOnClickListener(this);
		btn_tagitem1.setOnClickListener(this);
		btn_tagitem2.setOnClickListener(this);
		btn_tagitem3.setOnClickListener(this);
	}
	private void switchFragments(TabName tag) {
		if (tag == TabName.SESSION && !LoginManager.getInstance().isLogined()) {
			CToast.showToast(this, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
			ActivityController.getInstance().gotoLogin();
			return;
		}
		MainpagerAbstractFragment f = fragments.get(tag.toString());
		if (f == null) {
			f = MainpagerAbstractFragment.newInstance(tag.toString());
			if (f == null) {
				return;
			}
			fragments.put(tag.toString(), f);
		}
		if (tag == TabName.SESSION) {
			msgBadgeViewNumIcon.setVisibility(View.GONE);
		}
		curFragmentTag = tag.toString();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.replace(R.id.layout_content, f);
		transaction.commit();
		for (int i = 0, size = layout_tags.getChildCount(); i < size; i++) {
			if (tag.ordinal() == i) {
				layout_tags.getChildAt(i).setSelected(true);
			} else {
				layout_tags.getChildAt(i).setSelected(false);
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		mHandler.removeCallbacksAndMessages(null);
//		unregisterReceiver(mScreenReceiver);  
	}
	@Override
	public void registerVVBroadCastReceivers() {
		// 消息数量接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgType.chat.toString());
		filter.addAction(MsgType.friend_require.toString());
		filter.addAction(MsgType.comment.toString());
		filter.addAction(MsgType.like.toString());
		registerVVBroadCastReceiver(sessionModelReceiver, filter);
		/*mScreenReceiver = new ScreenBroadcastReceiver();
		filter = new IntentFilter();  
        filter.addAction(Intent.ACTION_SCREEN_ON);  
        filter.addAction(Intent.ACTION_SCREEN_OFF);  
        registerReceiver(mScreenReceiver, filter);*/
	}
	@Override
	public void onSwitchFragment(TabName tag) {
		switchFragments(tag);
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		switch (data.getAction()) {
			case LOGIN_SUCCESS:
				break;
			case NETWORK_ACTIVE:
				if (Boolean.TRUE.equals(data.getMsg())) {
					if (LoginManager.getInstance().isLogined()) {
						initAccountStatus();
					}
				}
				break;
			case TimeflyAlertLocal:
				TimeflyLocalNotify tln = (TimeflyLocalNotify) data;
				ImageFolder  folder = tln.folder;
				String time = tln.time;
				int what = Integer.parseInt(BBSUtils.getShortGidCreatTime(folder.getCreateTime()).replaceAll("-", ""));
				Message message = mHandler.obtainMessage(what);
				if (message == null) {
					message = new Message();
					message.what = what;
				}
				message.obj = new Object[] { folder, time };
				mHandler.removeMessages(what);
				if (tln.valid == Valid.open) mHandler.sendMessageDelayed(message, 60 * 1000);
			default:
				break;
		}
	}
	@Override
	public void onClick(View v) {
		if (v == btn_tagitem0) {
			switchFragments(MainpagerAbstractFragment.TabName.FRIEND);
		} else if (v == btn_tagitem1) {
			switchFragments(MainpagerAbstractFragment.TabName.TIMEFLY);
		} else if (v == btn_tagitem2) {
			switchFragments(MainpagerAbstractFragment.TabName.SHARE);
		} else if (v == btn_tagitem3) {
			switchFragments(MainpagerAbstractFragment.TabName.SESSION);
		}
	}
	private void initAccountStatus() {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				boolean result = BeemServiceHelper.getInstance(
						getApplicationContext()).xmppLoginSilently();
				autoAuthentificateCompleted = true;
				mHandler.sendEmptyMessage(MSG_AUTOAUTHENTIFICATE_COMPLETED);
			}
		});
	}
	private void update(final String url) {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				try {
					UpdateMessage udp = UpdateManager.update(url);
					String version = udp.getVersion();
					String versionName = BBSUtils.getVersionName();
					/*if (!versionName.substring(0, 1).equals(
							version.substring(0, 1))) {
						udp.setForce("1");
					} else {
						udp.setForce("0");
					}*/
					boolean isDowningState = SharedPrefsUtil.getValue(mContext,
							SettingKey.IsDowningState, false);
					if (version != null) {
						if (version.compareTo(versionName) > 0) {
							if (!isDowningState) {
								Message msg = mHandler
										.obtainMessage(Constants.SHOW_CHOOSEUPDATE);
								if (msg == null) {
									msg = Message.obtain();
									msg.what = Constants.SHOW_CHOOSEUPDATE;
								}
								msg.obj = udp;
								mHandler.sendMessage(msg);
							}
						} else {
						}
					}
				} catch (WSError e) {
				} catch (JSONException e) {
				} finally {
				}
			}
		});
	}
	@Override
	public boolean isAutoAuthentificateCompleted() {
		return autoAuthentificateCompleted;
	}

	private VVBaseBroadCastReceiver sessionModelReceiver = new VVBaseBroadCastReceiver(
			true) {
		@Override
		public void onReceive(Context context, Intent intent) {
			Item item = intent.getParcelableExtra("item");
			// LogUtils.i("received message:" + item.getMessage() + " jid:" +
			// item.getJidParsed() + " unReadMsgCount:"
			// + item.getUnReadMsgCount());
			if (curFragmentTag != TabName.SESSION.name() && !item.isLocal()) {
				msgBadgeViewNumIcon.setVisibility(View.VISIBLE);
			}
		}
	};
	
	/*private class ScreenBroadcastReceiver extends BroadcastReceiver {
		private String action = null;

		@Override
		public void onReceive(Context context, Intent intent) {
			action = intent.getAction();
			if (Intent.ACTION_SCREEN_ON.equals(action)) {
				if (LoginManager.getInstance().isLogined()) {
					initAccountStatus();
				}
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
			}
		}
	}*/
}
