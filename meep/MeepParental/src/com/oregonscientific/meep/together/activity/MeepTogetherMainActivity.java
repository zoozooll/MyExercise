package com.oregonscientific.meep.together.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.security.PermissionCollection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.Manifest.permission;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.CustomAlertDialog.OnOkListener;
import com.oregonscientific.meep.together.activity.PortalSettingsCustomLevel.GetOfflinePermission;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsPermission;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsTime;
import com.oregonscientific.meep.together.adapter.ListAdapterRequests;
import com.oregonscientific.meep.together.adapter.ListAdapterSecurityLevel;
import com.oregonscientific.meep.together.bean.Kid;
import com.oregonscientific.meep.together.bean.LoginUser;
import com.oregonscientific.meep.together.bean.Notification;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadNotification;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.comparision.PermissionComparision;
import com.oregonscientific.meep.together.comparision.PermissionData;
import com.oregonscientific.meep.together.comparision.PermissionGenerator;
import com.oregonscientific.meep.together.library.database.table.AuthInfo;
import com.oregonscientific.meep.together.library.database.table.TableUser;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnGetOwnerIdListerner;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadKidListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadNotificationsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadPortalSettingsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnLoginListener;
import com.oregonscientific.meep.together.library.rest.listener.OnSnVerifyListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MeepTogetherMainActivity extends SlidingFragmentActivity {

	public static SlidingMenu menu;
	private MenuFragment menuFragment;
	// COMMON
	// flag
	private boolean isPermissionUpdated = true;
	public static String currentKidName;
	private Context context;
	private static TableUser tableuser;

	// animation
	Animation rightToLeftAnimation;
	Animation leftToRightAnimation;
	// layout
	RelativeLayout mainContentLayout;
	RelativeLayout mainMenuLayout;
	ViewFlipper flipper;
	FrameLayout FrameClick;
	View LayoutNotification;
	View LayoutPortal;

	// text
	TextView username;
	TextView usercoin;
	// image
	ImageButton icon;
	// Button btnTerms;

	// MAINPAGE -- level 1
	private ArrayList<HashMap<String, Object>> arraylist_Kids;
	public final static int PARENTAL_SETTINGS = 0;
	public final static int NOTIFICATIONS = 1;
	// ====Layout Notification====
	// button
	ImageButton barLeftNotificationMenu;
	// listview
	ListView listNotification;
	private ListAdapterRequests notificationAdapter;
	private ArrayList<HashMap<String, Object>> arraylist_Notification;
	// text
	private TextView KidNameNoti;
	TextView noti_showMore;
	ProgressBar noti_bar;

	// // ====Layout Portal====
	// // button
	ImageButton barLeftPortalMenu;

	MyProgressDialog loading;

	// private static boolean UserFunction.isOnline = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		initLeftMenu();

		retriveAccountInformation();
		context = getApplicationContext();
		// Titles = context.getResources().getStringArray(R.array.apps_string);
		// TimeString =
		// context.getResources().getStringArray(R.array.time_string);
		noti_lastId = null;
		noti_currentDate = null;
		// notiNum = 0;

		// is not Online(network is not ready)
		initUI();
		initListeners();
		if (!getIntent().getBooleanExtra("online", false)) {
			tableuser = UserFunction.getUserInfo();
			// init
			initUserInformation();
			onUpdateBasic();
			// start get offline permission
			// TODO retrieve offline permission
			// GetOfflinePermission task = new GetOfflinePermission();
			// task.execute();

			UserFunction.isOnline = false;
			// TODO:delete
			// new FinishActivityDialog(this,
			// "Parental Control OFFLINE mode\nComming soon.").show();
		} else {
			loading.show();
			// init AsynTask
			setRestListeners();
			// init
			initUserInformation();
			UserFunction.isOnline = true;
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(UserFunction.isOnline)
		{
			UserFunction.getRestHelp().setOnLoadPortalSettingsListener(new OnLoadPortalSettingsListener() {
				
				@Override
				public void onLoadPortalSettingsSuccess(
						ResponseLoadPermission infoPermission) {
					
					onPortalSettingsUpdated(infoPermission.getPermission().getLimit());
				}
				
				@Override
				public void onLoadPortalSettingsFailure(ResponseBasic r) {
					onPopupResponse(R.string.load_setting_failure);
				}
			});
		}
		else
		{
			if(isPermissionUpdated)
			{
				refreshOfflinePermissionList();
				isPermissionUpdated = false;
			}
		}
	}

	protected void onPortalSettingsUpdated(ArrayList<SettingConfig> list) {
		int type = getTypeOfSecurityLevel(list);
		for (int i = 0; i < 3; i++) {
			if (type == i) {
				arraylistSecurityLevel.set(i, true);
			} else {
				arraylistSecurityLevel.set(i, false);
			}
		}
		securityLevelAdapter.notifyDataSetChanged();
	}
	protected void onPortalSettingsUpdated(int type) {
		for (int i = 0; i < 3; i++) {
			if (type == i) {
				arraylistSecurityLevel.set(i, true);
			} else {
				arraylistSecurityLevel.set(i, false);
			}
		}
		securityLevelAdapter.notifyDataSetChanged();
	}

	public void initLeftMenu() {
		// set right menu
		setSlidingActionBarEnabled(false);
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.LEFT);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setBehindScrollScale(1.0f);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.5f);
		// menu.setSecondaryMenu(R.layout.properties);
		// menu.setSecondaryShadowDrawable(R.drawable.shadow);
		setTitle("Sliding Bar");

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		menuFragment = new MenuFragment();
		t.replace(R.id.menu_frame, menuFragment);
		t.commit();
	}

	public void setRestListeners() {

		UserFunction.getRestHelp().setOnLoadKidListener(new OnLoadKidListener() {

			@Override
			public void onLoadKidSuccess(Kid[] kids) {
				// onLoadKids(infoKid.getKids());
				getDeviceOwnerId(kids);
			}

			@Override
			public void onLoadKidFailure() {
				onPopupFinishDialog(R.string.load_kid_failure);
			}

			@Override
			public void onLoadKidTimeout() {
				onPopupFinishDialog(R.string.load_kid_failure);

			}
		});
		UserFunction.getRestHelp().setOnLoadNotificationsListener(new OnLoadNotificationsListener() {

			@Override
			public void onLoadNotificationsSuccess(
					ResponseLoadNotification infoNotifications) {
				onUpdateNotification();
			}

			@Override
			public void onLoadNotificationsFailure(ResponseBasic r) {
				onPopupResponse(R.string.load_notification_failure);
			}
		});
		// UserFunction.getRestHelp().setOnLoadPortalSettingsListener(new
		// OnLoadPortalSettingsListener() {
		//
		// @Override
		// public void onLoadPortalSettingsSuccess(
		// ResponseLoadPermission infoPermission) {
		// onUpdatePortalSetting(infoPermission.getPermission());
		//
		// }
		//
		// @Override
		// public void onLoadPortalSettingsFailure(ResponseBasic r) {
		// onPopupResponse(R.string.load_setting_failure);
		// }
		// });
	}

	public void onPopupFinishDialog(int resId) {
		if (loading != null)
			loading.dismiss();
		FinishActivityDialog dialog = new FinishActivityDialog(MeepTogetherMainActivity.this, resId);
		dialog.show();
	}

	public void getDeviceOwnerId(final Kid[] kids) {
		UserFunction.getRestHelp().setOnGetOwnerIdListener(new OnGetOwnerIdListerner() {

			@Override
			public void onGetSuccess(String ownerId) {
				onUpdateKidsMaps(kids);
				if (!containsOwnerIdInList(ownerId, kids)) {
					checkSerialNumberValidation(kids.length);
				} else {
					setttingCurrentOwner(ownerId);
				}

			}

			@Override
			public void onGetFailure() {
				onUpdateKidsMaps(kids);
				checkSerialNumberValidation(kids.length);
			}

			@Override
			public void onGetTimeout() {
				onPopupFinishDialog(R.string.please_retry);

			}
		});
		UserFunction.getRestHelp().getChildUserId(UserFunction.getSerialNumber(), getOwnerToken());
	}

	public void checkSerialNumberValidation(final int length) {
		final String sn = UserFunction.getSerialNumber();
		UserFunction.getRestHelp().serialVerify(sn);
		UserFunction.getRestHelp().setOnSnVerifyListener(new OnSnVerifyListener() {

			@Override
			public void onSnVerifySuccess(ResponseBasic r) {
				// // Account has kids
				// if (length > 0) {
				// // migrateAccount(UserFunction.getSerialNumber());
				// toSelectChild(sn);
				// }
				// // no kid
				// else {
				// createNewChildAccount(null);
				// }
				createNewChildAccount(sn);
			}

			@Override
			public void onSnVerifyFailure(ResponseBasic r) {
				onPopupFinishDialog(R.string.verfiy_sn_failure);
			}

			@Override
			public void onSnVerifyTiemOut() {
				onPopupFinishDialog(R.string.please_retry);

			}
		});

	}

	/**
	 * 
	 * @param sn
	 */
	public void createNewChildAccount(String sn) {
		Intent start = new Intent(context, MeepTogetherStartUsingActivity.class);
		if (sn != null) {
			start.putExtra("sn", sn);
		}
		startActivityForResult(start, CREATE_CHILD_ACCOUNT);
	}

	public boolean containsOwnerIdInList(String id, Kid[] kids) {
		for (Kid k : kids) {
			if (id.equals(k.getUserId())) {
				return true;
			}
		}
		return false;
	}

	public void setttingCurrentOwner(String id) {
		UserFunction.currentKid = id;
		onUpdateBasic();
		onUpdateKids();
		UserFunction.getRestHelp().refreshPermission(UserFunction.currentKid);
	}

	private String getOwnerToken() {
		String token = UserFunction.getAccountToken(MeepTogetherMainActivity.this);
		return token;
	}

	public void retriveAccountInformation() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(MeepTogetherMainActivity.this, ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					return;
				}
				Account account = accountManager.getLoggedInAccountBlocking();
				if (account == null) {
					return;
				}
				// store account information to preference
				UserFunction.setAccountInformation(MeepTogetherMainActivity.this, account);
				Utils.printLogcatDebugMessage(account.toJson());
			}

		});
	}

	public void reSignInAccount() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				try {

					AccountManager accountManager = (AccountManager) ServiceManager.getService(MeepTogetherMainActivity.this, ServiceManager.ACCOUNT_SERVICE);
					if (accountManager == null) {
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					accountManager.signIn();
				} catch (NullPointerException e) {
					return;
				}
			}

		});
	}

	LoginUser loginUser;
	private ListView listSecurityLevel;
	private ArrayList<Boolean> arraylistSecurityLevel;
	private ListAdapterSecurityLevel securityLevelAdapter;

	public void initUI() {
		getAnimationInfo();
		loading = UserFunction.initLoading(this);
		mainContentLayout = (RelativeLayout) findViewById(R.id.mainContentLayout);
		mainMenuLayout = (RelativeLayout) findViewById(R.id.mainMenuLayout);
		FrameClick = (FrameLayout) findViewById(R.id.FrameClick);

		// menu
		// menuNotification = (Button) findViewById(R.id.menuNotification);
		// menuAccount = (Button) findViewById(R.id.menuAccount);
		// menuParental = (Button) findViewById(R.id.menuParental);
		// menuGooglePlay = (Button) findViewById(R.id.menuGooglePlay);
		// menuLogout = (Button) findViewById(R.id.menuLogout);
		username = (TextView) findViewById(R.id.user_name);
		usercoin = (TextView) findViewById(R.id.user_coins);
		icon = (ImageButton) findViewById(R.id.user_icon);
		// notificationNum = (TextView)findViewById(R.id.notificationNum);
		// btnTerms = (Button) findViewById(R.id.btnTerms);
		// image = (ImageButton) findViewById(R.id.user_icon);

		// flipper
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		// in ViewFlipper
		LayoutNotification = findViewById(R.id.LayoutNotification);
		// TODO: update layout
		LayoutPortal = findViewById(R.id.LayoutPortalSetting);

		// mainPage
		// ===notification===
		barLeftNotificationMenu = (ImageButton) LayoutNotification.findViewById(R.id.mainImageButtonMenu);
		listNotification = (ListView) LayoutNotification.findViewById(R.id.listResquests);
		KidNameNoti = (TextView) LayoutNotification.findViewById(R.id.KidName);

		// // ===portal setting===
		listSecurityLevel = (ListView) LayoutPortal.findViewById(R.id.listSecurityLevel);
		barLeftPortalMenu = (ImageButton) LayoutPortal.findViewById(R.id.mainImageButtonBack);
		// listAppTime = (ListView)
		// LayoutPortal.findViewById(R.id.listAppsTime);
		// listPurchase = (ListView)
		// LayoutPortal.findViewById(R.id.listPurchase);
		// listInternet = (ListView)
		// LayoutPortal.findViewById(R.id.listInternet);
		// resetTimer = (Button) LayoutPortal.findViewById(R.id.clearTimer);

	}

	public void initListeners() {
		// FrameClick.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (isMenuShown)
		// translateMainContent();
		// }
		// });
		// menuNotification.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (!UserFunction.isNetworkAvailable(MeepTogetherMainActivity.this))
		// {
		// onPopupResponse(R.string.no_network);
		// } else if (!UserFunction.isOnline) {
		// onPopupResponse(R.string.account_offline);
		// } else {
		// if (menu.isShown()) {
		// // show page
		// flipper.setDisplayedChild(NOTIFICATIONS);
		// // send Request Notification
		// if (arraylist_Notification.size() == 0) {
		// UserFunction.getRestHelp().refreshNotification(currentKid,
		// noti_lastId);
		// }
		// // translateMainContent();
		// menu.showContent();
		// }
		// }
		// }
		// });
		// menuAccount.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (!UserFunction.isNetworkAvailable(MeepTogetherMainActivity.this))
		// {
		// onPopupResponse(R.string.no_network);
		// } else if (!UserFunction.isOnline) {
		// onPopupResponse(R.string.account_offline);
		// } else {
		// if (menu.isShown()) {
		// Intent account = new Intent(MeepTogetherMainActivity.this,
		// AccountEditInfoKid.class);
		// Bundle bundle = new Bundle();
		// Gson gson = new Gson();
		// String kidString = gson.toJson(kidMap.get(currentKid));
		// bundle.putString("kid", kidString);
		// account.putExtra("account", bundle);
		// startActivityForResult(account, ACCOUNT_MANAGE);
		// }
		// }
		// }
		// });
		// menuParental.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// if (menu.isShown()) {
		// flipper.setDisplayedChild(PARENTAL_SETTINGS);
		// if (arraylist_AppTime.size() == 0
		// || arraylist_Purchase.size() == 0
		// || arraylist_Internet.size() == 0) {
		// if (UserFunction.isNetworkAvailable(getApplicationContext())
		// && UserFunction.isOnline) {
		// UserFunction.getRestHelp().refreshPermission(currentKid);
		// } else {
		// // start get offline permission
		// GetOfflinePermission task = new GetOfflinePermission();
		// task.execute();
		// }
		// }
		// // translateMainContent();
		// menu.showContent();
		// }
		// }
		// });
		// menuGooglePlay.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// CustomAlertDialog dialog = new
		// CustomAlertDialog(MeepTogetherMainActivity.this,
		// R.string.google_play_confirmation);
		// dialog.setOnOkListener(new OnOkListener() {
		// @Override
		// public void onOk() {
		// toGooglePlay();
		// }
		// });
		// dialog.show();
		// }
		// });
		// menuLogout.setOnClickListener(new View.OnClickListener() {
		//
		// @Override
		// public void onClick(View arg0) {
		// finish();
		// }
		// });
		//
		// ======Notification=====
		barLeftNotificationMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (!isMenuShown)
				// translateMainContent();
				menu.showMenu();
			}
		});

		// ======Portal Setting=====
		barLeftPortalMenu.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// if (!isMenuShown)
				// translateMainContent();
				menu.showMenu();
			}
		});

	}

	public void relocateLayout(int left, int top, int right, int bottom) {
		int[] pos = { mainContentLayout.getLeft() + left,
				mainContentLayout.getTop() + top,
				mainContentLayout.getRight() + right,
				mainContentLayout.getBottom() + bottom };
		mainContentLayout.layout(pos[0], pos[1], pos[2], pos[3]);
	}

	public void getAnimationInfo() {
		rightToLeftAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_right_to_left_slide);
		leftToRightAnimation = AnimationUtils.loadAnimation(this, R.anim.layout_left_to_right_slide);
	}

	public void initUserInformation() {
		// portal setting
		initPortalSettingListItem();
		// kids
		initKidsListItem();
		// notifications
		initNotificationListItem();

		tableuser = UserFunction.getDataHelp().getUserInfoFromDB();
		UserFunction.getRestHelp().reloadAllKids();
	}

	private void initPortalSettingListItem() {
		// TODO retrieve permission
		// high,low or custom
		arraylistSecurityLevel = new ArrayList<Boolean>();
		arraylistSecurityLevel.add(false);
		arraylistSecurityLevel.add(false);
		arraylistSecurityLevel.add(false);
		securityLevelAdapter = new ListAdapterSecurityLevel(MeepTogetherMainActivity.this, R.layout.item_security_level, arraylistSecurityLevel);
		listSecurityLevel.setAdapter(securityLevelAdapter);
		listSecurityLevel.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 < 2) {
					toSetSecurityLevel(arg2);
				}
				else
				{
					toSetCustomSecurityLevel();
				}
			}

			
		});
		UserFunction.setListViewHeightBasedOnChildren(listSecurityLevel);
	}

	/**
	 * KID
	 */
	public void initKidsListItem() {
		arraylist_Kids = new ArrayList<HashMap<String, Object>>();
	}

	// ImageButton image;

	public void onUpdateBasic() {
		username = (TextView) findViewById(R.id.user_name);
		usercoin = (TextView) findViewById(R.id.user_coins);
		// username,avatar
		username.setText(tableuser.getFirstName() + " "
				+ tableuser.getLastName());

		usercoin.setText(tableuser.getCoins());

		loadImage(tableuser.getIconAddr());
		// ***image***
	}

	private void loadImage(String url) {
		// TODO Auto-generated method stub
		menuFragment.loadImage(url);
	}

	private ArrayList<String> kidIdMap = new ArrayList<String>();
	private HashMap<String, Kid> kidMap = new HashMap<String, Kid>();

	public void onUpdateKidsMaps(Kid[] kids) {
		arraylist_Kids.clear();
		kidIdMap.clear();
		kidMap.clear();
		for (Kid k : kids) {
			kidMap.put(k.getUserId(), k);
			HashMap<String, Object> map = new HashMap<String, Object>();
			String kidString = new Gson().toJson(k);
			map.put("kid", kidString);
			arraylist_Kids.add(map);
			kidIdMap.add(k.getUserId());

		}
	}

	public void onUpdateKids() {
		Kid kid = kidMap.get(UserFunction.currentKid);
		// set current Kid Name
		String name = kid.getName();
		currentKidName = name;
		KidNameNoti.setText(String.format(getResources().getString(R.string.main_page_notification_after), name));
		// set unread notification
		int number = kid.getUnread_notifications();
		TextView notificationNum = (TextView) findViewById(R.id.notificationNum);
		if (number > 0) {
			notificationNum.setVisibility(View.VISIBLE);
			notificationNum.setText(Integer.toString(number));
		} else {
			notificationNum.setVisibility(View.INVISIBLE);
		}
		loading.dismiss();
	}

	/**
	 * NOTIFICATION
	 */
	public void initNotificationListItem() {
		arraylist_Notification = new ArrayList<HashMap<String, Object>>();
		notificationAdapter = new ListAdapterRequests(MeepTogetherMainActivity.this, R.layout.item_notification, arraylist_Notification);
		View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.footer, null, false);
		noti_bar = (ProgressBar) footerView.findViewById(R.id.progressBar);
		noti_showMore = (TextView) footerView.findViewById(R.id.showMore);
		// generate underline text
		SpannableString content = new SpannableString(getResources().getString(R.string.main_page_notifications_show_more));
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		noti_showMore.setText(content);
		noti_showMore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				noti_showMore.setVisibility(View.GONE);
				noti_bar.setVisibility(View.VISIBLE);
				if (UserFunction.currentKid != null)
					UserFunction.getRestHelp().refreshNotification(UserFunction.currentKid, noti_lastId);
			}
		});
		listNotification.addFooterView(footerView);
		listNotification.setAdapter(notificationAdapter);
		listNotification.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				try {
					// notiItemPos = arg2;
					Kid kid = kidMap.get(UserFunction.currentKid);
					Notification notification = (Notification) arg1.getTag();

					if (context.getResources().getString(R.string.main_text_type_coins).equals(((TextView) arg1.findViewById(R.id.textLogsType)).getText().toString())) {
						// start activity to coin allocation
						int index = kidIdMap.indexOf(UserFunction.currentKid);
						toCoinAllocation(index, kid);
					} else if (context.getResources().getString(R.string.main_text_type_google).equals(((TextView) arg1.findViewById(R.id.textLogsType)).getText().toString())) {
						// google play item
						String details = ((Notification) arraylist_Notification.get(arg2).get("notification")).getDetails();
						toBrowserRequest(details);
					} else {
						toNotificationProcess(arg2, kid, notification);
					}
				} catch (NullPointerException e) {
					// nothing
				}
			}
		});

	}

	public String noti_lastId;
	public String noti_currentDate;
	public String noti_newDate;

	public void onUpdateNotification() {
		for (Notification x : RestClientUsage.infoNotification.getNotifi()) {
			noti_lastId = x.getId();

			// notification date
			Date dts = new Date();
			long date = (long) ((Double.parseDouble(x.getEvent_time()) - (dts.getTimezoneOffset() * 60)) * 1000);
			date = date - TimeZone.getDefault().getRawOffset();
			dts = new Date(date);

			SimpleDateFormat fy = new SimpleDateFormat("yyyy-MM-dd");
			SimpleDateFormat fd = new SimpleDateFormat("HH:mm");

			// if(Notification.S_APPROVAL_PENDING.equals(x.getApproval()))
			// {
			noti_newDate = fy.format(dts);
			if (noti_currentDate == null
					|| !noti_newDate.equals(noti_currentDate)) {
				HashMap<String, Object> section = new HashMap<String, Object>();
				section.put("isSection", 1);
				section.put("date", noti_newDate);
				arraylist_Notification.add(section);
				noti_currentDate = noti_newDate;
			}
			// put into ListView
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("time", fd.format(dts));
			map.put("notification", x);
			arraylist_Notification.add(map);
			// notiNum++;
			// }
		}
		noti_bar.setVisibility(View.GONE);
		if (RestClientUsage.infoNotification.getNotifi().length == 0) {
			noti_showMore.setVisibility(View.GONE);
		} else {
			noti_showMore.setVisibility(View.VISIBLE);
		}
		notificationAdapter.notifyDataSetChanged();
		UserFunction.setListViewHeightBasedOnChildren(listNotification);
	}

	String meeper;
	// protected static final int SET_PERMISSION = 0;
	// protected static final int SET_TIMELIMIT = 3;
	protected static final int NOTIFICATION_PROCESS = 4;
	protected static final int ACCOUNT_MANAGE = 5;
	protected static final int COIN_ALLOCATION = 6;
	private static final int SELECT_CHILD = 1;
	private static final int CREATE_CHILD_ACCOUNT = 2;
	protected static final int SET_SECURITY_LEVEL = 7;
	protected static final int SET_CUSTOM_SECURITY_LEVEL = 8;

	protected void onActivityResult(int requestCode, int resultCode,
			Intent returnIntent) {
		super.onActivityResult(requestCode, resultCode, returnIntent);
		switch (requestCode) {
		case CREATE_CHILD_ACCOUNT:
			if (resultCode == RESULT_CANCELED) {
				UserFunction.getDataHelp().deleteAll();
			}
			finish();
			break;
		case SELECT_CHILD:
			if (resultCode == RESULT_OK) {
				// // init
				// initUserInformation();
				// UserFunction.isOnline = true;
				// Utils.printLogcatDebugMessage( "migrate success");
				onPopupFinishDialog(R.string.migrate_success);
			} else if (resultCode == RESULT_CANCELED) {
				UserFunction.getDataHelp().deleteAll();
				finish();
			}
			break;
		// case SET_PERMISSION:
		// if (resultCode == RESULT_OK) {
		// settingPurchasePermission(returnIntent.getIntExtra("index_permission",
		// 0));
		// }
		// break;
		// case SET_TIMELIMIT:
		// if (resultCode == RESULT_OK) {
		// settingTimeLimit(returnIntent.getIntExtra("index_time", 0));
		// }
		// break;
		case SET_SECURITY_LEVEL:
			if(resultCode == RESULT_OK)
			{
				if(UserFunction.isNetworkAvailable(this)&&UserFunction.isOnline)
				{
					onPortalSettingsUpdated(RestClientUsage.infoPermission.getPermission().getLimit());
				}
				else if(!UserFunction.isOnline)
				{
//					refreshOfflinePermissionList();
//					isPermissionUpdated = true;
					onPortalSettingsUpdated(returnIntent.getIntExtra(PortalSettingsDefinedLevel.KEY_SCURITY_LEVEL,0));
				}
				else{
					onPopupResponse(R.string.account_offline);
				}
			}
			break;
		case SET_CUSTOM_SECURITY_LEVEL:
			if(UserFunction.isOnline)
			{
				onPortalSettingsUpdated(RestClientUsage.infoPermission.getPermission().getLimit());
			}
			else
			{
				isPermissionUpdated = true;
//				refreshOfflinePermissionList();
			}
			break;
		case NOTIFICATION_PROCESS:
			if (resultCode == RESULT_OK) {
				int arg = returnIntent.getIntExtra("index", 0);
				String notificationString = returnIntent.getBundleExtra("result").getString("update-notification");
				Gson gson = new Gson();
				Notification n = gson.fromJson(notificationString, Notification.class);
				arraylist_Notification.get(arg).put("notification", n);
				notificationAdapter.notifyDataSetChanged();
			}
			break;
		case ACCOUNT_MANAGE:
			if (resultCode == RESULT_OK) {
				try {
					// int arg = returnIntent.getIntExtra("index", -1);
					// if(arg>=0)
					// {
					// String kidString =
					// returnIntent.getBundleExtra("result").getString("update-kid");
					// arraylist_Kids.get(arg).put("kid", kidString);
					// accountAdapter.notifyDataSetChanged();
					// loading.show();
					// UserFunction.getRestHelp().addNewKidOrUpdate();
					// }

					// repalce with login and signin again
					int parent = returnIntent.getIntExtra("parent", -1);
					if (parent == 0) {
						//method 1:update by returned data
						String parentString = returnIntent.getBundleExtra("result").getString("update-parent");
						Gson gson = new Gson();
						tableuser = gson.fromJson(parentString, TableUser.class);
						onUpdateBasic();
						
						//method 2:login again
						reLogin();

					} else if (parent == -1) {
						//method 1:
						String kidString = returnIntent.getBundleExtra("result").getString("update-kid");
						Gson gson = new Gson();
						Kid k = gson.fromJson(kidString, Kid.class);
						kidMap.put(UserFunction.currentKid, k);
						onUpdateKids();
						notificationAdapter.refresh();
						
						//method 2 : sign in again
						UserFunction.getRestHelp().reloadAllKids();
						reSignInAccount();
					}
					new CustomDialog(MeepTogetherMainActivity.this, R.string.profile_update_success).show();
				} catch (NullPointerException e) {
					e.printStackTrace();
				}
			}
			break;
		case COIN_ALLOCATION:
			if (resultCode == RESULT_OK) {
				long parentCoins = returnIntent.getBundleExtra("result").getLong("update-coin");
				tableuser.setCoins(Long.toString(parentCoins));
				onUpdateBasic();
			}
			break;
		}
	}

	public void reLogin() {
		UserFunction.getRestHelp().setOnLoginListener(new OnLoginListener() {

			@Override
			public void onLoginTimeOut() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onLoginSuccess(ResponseLogin lr) {
				UserFunction.getDataHelp().deleteUser();
				tableuser = UserFunction.generateTableUser(lr);
				UserFunction.getDataHelp().insertUser(tableuser.getInsertSql());
				onUpdateBasic();
				UserFunction.isOnline = true;
			}

			@Override
			public void onLoginFailure(ResponseBasic r) {
				// TODO Auto-generated method stub

			}
		});
		UserFunction.getRestHelp().reAuthentication(context);
	}


	// /**
	// * SUB PAGE
	// */
	// /**
	// * Apps Time Limit
	// */
	private Integer[] timelimit = { 0, 15, 30, 60, 120, 240, 480, 720, 1440 };

	public void onPopupResponse(int resId) {
		UserFunction.popupMessage(resId, MeepTogetherMainActivity.this, loading);
	}

	public void onPopupResponse(String status) {
		UserFunction.popupResponse(status, MeepTogetherMainActivity.this, loading);
	}

	// /**
	// * toast
	// */
	// public void onUpdatePermission(int timePos) {
	// onPopupResponse(R.string.permission_save);
	// barLeftPortalMenu.setEnabled(true);
	// if (appPos >= 0) {
	// // modify portal setting page
	// View appView = (View) listAppTime.getChildAt(appPos);
	// TextView t1 = (TextView) appView.findViewById(R.id.textTime);
	// t1.setText(TimeString[timePos]);
	// TextView t2 = (TextView) appView.findViewById(R.id.textUnit);
	// t2.setText("");
	// appPos = -1;
	// }
	// }

	public Bitmap loadBitmap(String url) {
		Bitmap mIcon = null;
		try {
			URL newurl = new URL(url);
			mIcon = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mIcon;
	}

	@Override
	public void onBackPressed() {

		if (menu.isMenuShowing()) {
			// translateMainContent();
			menu.showContent();
		} else {
			finish();
		}

	}

	/**
	 * SKIP calling onCreate() when orientation is changed
	 */
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {

		} else if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
		}
	}

	// private void toSetPermission() {
	// Intent intent = new Intent();
	// intent.setClass(this, PortalSettingsPermission.class);
	// intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	// startActivityForResult(intent, SET_PERMISSION);
	// }
	//
	// private void toSetTimeLimit(String title, int index) {
	// Intent intent = new Intent();
	// intent.setClass(this, PortalSettingsTimeLimit.class);
	// intent.putExtra("title", title);
	// intent.putExtra("index", index);
	// startActivityForResult(intent, SET_TIMELIMIT);
	// }
	private void toSetSecurityLevel(int index) {
		Intent intent = new Intent();
		intent.setClass(this, PortalSettingsDefinedLevel.class);
		intent.putExtra(PortalSettingsDefinedLevel.KEY_SCURITY_LEVEL, index);
		startActivityForResult(intent, SET_SECURITY_LEVEL);
	}
	private void toSetCustomSecurityLevel() {
		Intent intent = new Intent();
		intent.setClass(this, PortalSettingsCustomLevel.class);
		startActivityForResult(intent, SET_CUSTOM_SECURITY_LEVEL);
	}

	private void toNotificationProcess(int index, Kid kid,
			Notification notification) {
		Gson gson = new Gson();
		String kidString = gson.toJson(kid);
		String notificationString = gson.toJson(notification);

		Intent process = new Intent(MeepTogetherMainActivity.this, NotificationProcess.class);
		Bundle extras = new Bundle();
		extras.putString("kid", kidString);
		extras.putString("notification", notificationString);
		process.putExtra("request", extras);
		process.putExtra("index", index);
		startActivityForResult(process, NOTIFICATION_PROCESS);
	}

	private void toSelectChild(String sn) {
		Intent intent = new Intent();
		intent.setClass(this, SelectChild.class);
		intent.putExtra("list_kids", arraylist_Kids);
		intent.putExtra("sn", sn);
		startActivityForResult(intent, SELECT_CHILD);
	}

	private void toCoinAllocation(int index, Kid kid) {
		Gson gson = new Gson();
		String kidString = gson.toJson(kid);
		String parentString = gson.toJson(tableuser);

		Intent process = new Intent(MeepTogetherMainActivity.this, CoinAllocation.class);
		Bundle extras = new Bundle();
		extras.putString("kid", kidString);
		extras.putString("parent", parentString);
		process.putExtra("account", extras);
		process.putExtra("index", index);
		startActivityForResult(process, COIN_ALLOCATION);
	}

	private void toBrowserRequest(String details) {
		// Intent browserIntent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse("https://portal.meeptablet.com/1/store/details/"
		// + notificationId));
		// browserIntent.setComponent(new
		// ComponentName("com.oregonscientific.meep.browser",
		// "com.oregonscientific.meep.browser.WebBrowserActivity"));
		// startActivity(browserIntent);

		// create eanble_google_play.txt
		setLaunchAppEnable();

		// // method 1
		// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(details));
		// intent.putExtra("MEEP_LAUNCH", true);
		// methods 2
		// String pkgname = details.substring(details.lastIndexOf('?') + 1);
		// Log.w("test", pkgname);
		// Log.w("test", details);;
		// Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(details));
		// Intent intent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://market.android.com/details?"
		// + pkgname));

		String packageName = "com.android.vending/com.android.vending.AssetBrowserActivity";
		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(details));
		intent.setComponent(ComponentName.unflattenFromString(packageName));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.putExtra("MEEP_LAUNCH", true);

		// start
		startActivity(intent);
	}

	public void toGooglePlay() {
		setLaunchAppEnable();
		// Intent intent = new Intent(Intent.ACTION_VIEW,
		// Uri.parse("http://market.android.com/"));
		// intent.putExtra("MEEP_LAUNCH", true);
		// //start
		// startActivity(intent);

		// String path =
		// "com.android.vending/com.android.vending.AssetBrowserActivity";
		// Intent intent = new Intent();
		// // intent.setComponent(ComponentName.unflattenFromString(path));
		// intent.setComponent(new ComponentName("com.android.vending",
		// "com.android.vending.AssetBrowserActivity"));
		// intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY).addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		// intent.addCategory("android.intent.category.LAUNCHER");
		// intent.putExtra("MEEP_LAUNCH", true);
		// startActivity(intent);

		String packageName = "com.android.vending/com.android.vending.AssetBrowserActivity";
		Intent intent = new Intent();
		intent.setComponent(ComponentName.unflattenFromString(packageName));
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.addCategory(Intent.CATEGORY_LAUNCHER);
		intent.putExtra("MEEP_LAUNCH", true);
		startActivity(intent);

		setLaunchAppEnable();

	}

	public void toTerms() {
		Intent terms = new Intent(getApplicationContext(), MeepTogetherTermsOfServiceActivity.class);
		startActivity(terms);
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		ServiceManager.unbindServices(MeepTogetherMainActivity.this);
		//
	}

	// public void clearTimer() {
	// // clear history
	// PermissionManager permissionManager = getPermissionManager();
	// permissionManager.clearRunHistories(UserFunction.getAccountIdentity(this),
	// UserFunction.getAccountID(this));
	// }

	public static void setLaunchAppEnable() {
		final String PATH_ENABLE_APP_LAUNCH_FLAG = "/mnt/private/enable_google_play.txt";
		final String TEXT_ENABLE_APP_LAUNCH = "enable_googleplay=1";
		File file = new File(PATH_ENABLE_APP_LAUNCH_FLAG);
		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (IOException e) {
				Log.e("AppLaunchCtrl", "error create file:" + e.toString());
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(file, true));
			buf.write(TEXT_ENABLE_APP_LAUNCH);
			buf.newLine();
			buf.close();
		} catch (IOException e) {
			Log.e("AppLaunchCtrl", "error write file:" + e.toString());
		}
	}

	// for 3.0 UI
	public void clickMenuNotification() {
		if (!UserFunction.isNetworkAvailable(MeepTogetherMainActivity.this)) {
			onPopupResponse(R.string.no_network);
		} else if (!UserFunction.isOnline) {
			onPopupResponse(R.string.account_offline);
		} else {
			if (menu.isShown()) {
				// show page
				flipper.setDisplayedChild(NOTIFICATIONS);
				// send Request Notification
				if (arraylist_Notification.size() == 0) {
					UserFunction.getRestHelp().refreshNotification(UserFunction.currentKid, noti_lastId);
				}
				// translateMainContent();
				menu.showContent();
			}
		}
	}

	public void clickMenuAccount() {
		if (!UserFunction.isNetworkAvailable(MeepTogetherMainActivity.this)) {
			onPopupResponse(R.string.no_network);
		} else if (!UserFunction.isOnline) {
			onPopupResponse(R.string.account_offline);
		} else {
			if (menu.isShown()) {
				Intent account = new Intent(MeepTogetherMainActivity.this, AccountEditInfoKid.class);
				Bundle bundle = new Bundle();
				Gson gson = new Gson();
				String kidString = gson.toJson(kidMap.get(UserFunction.currentKid));
				bundle.putString("kid", kidString);
				account.putExtra("account", bundle);
				startActivityForResult(account, ACCOUNT_MANAGE);
			}
		}
	}

	public void clickMenuParental() {
		if (menu.isMenuShowing()) {
			flipper.setDisplayedChild(PARENTAL_SETTINGS);
			// TODO:refresh parental
//			refreshOfflinePermissionList();
			menu.showContent();
		}
	}
	public void clickMenuCoins(){
		Kid kid = kidMap.get(UserFunction.currentKid);
		int index = kidIdMap.indexOf(UserFunction.currentKid);
		toCoinAllocation(index, kid);
	}

	public void clickMenuGooglePlay() {
		CustomAlertDialog dialog = new CustomAlertDialog(MeepTogetherMainActivity.this, R.string.google_play_confirmation);
		dialog.setOnOkListener(new OnOkListener() {
			@Override
			public void onOk() {
				toGooglePlay();
			}
		});
		dialog.show();
	}

	public void clickMenuLogout() {
		finish();
	}

	public void clickMenuTerm() {
		toTerms();
	}

	public void clickUserIcon() {
		if (!UserFunction.isNetworkAvailable(MeepTogetherMainActivity.this)) {
			onPopupResponse(R.string.no_network);
		} else if (!UserFunction.isOnline) {
			onPopupResponse(R.string.account_offline);
		} else {
			Intent account = new Intent(MeepTogetherMainActivity.this, AccountEditInfoParent.class);
			Bundle bundle = new Bundle();
			Gson gson = new Gson();
			String parentString = gson.toJson(tableuser);
			bundle.putString("parent", parentString);
			account.putExtra("account", bundle);
			startActivityForResult(account, ACCOUNT_MANAGE);
		}
	}
	
	public int getTypeOfSecurityLevel(List<SettingConfig> list)
	{
		PermissionComparision compareHandler =  new PermissionComparision();
		if(compareHandler.isInternetHighSecurityLevel(list))
		{
			if(compareHandler.equalsToHighLevel(list))
			{
				return PermissionData.TYPE_HIGH;
			}
			else
			{
				return PermissionData.TYPE_CUSTOM;
			}
		}
		else
		{
			if(compareHandler.equalsToLowLevel(list))
			{
				return PermissionData.TYPE_LOW;
			}
			else
			{
				return PermissionData.TYPE_CUSTOM;
			}
		}
	}
	
	
	//for offline mode
	PermissionManager myPermissionManager = null;

	public PermissionManager getPermissionManager() {
		if (myPermissionManager == null) {
			myPermissionManager = (PermissionManager) ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
		}
		return myPermissionManager;

	}
	
	Permissions offlinePermission;
	class GetOfflinePermission extends AsyncTask<Void, Void, Permissions> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// start get permission from service
			if (loading != null && !loading.isShowing())
				loading.show();
			getPermissionList();
		}

		@Override
		protected Permissions doInBackground(Void... params) {
			Permissions list = null;
			Utils.printLogcatDebugMessage("start return loop");
			for (int i = 0; i < 10; i++) {
				// sleep every one minute
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				list = offlinePermission;
				if (list != null) {
					list.setLimit();
					Utils.printLogcatDebugMessage("break return loop");
					break;
				}

			}
			return list;
		}

		@Override
		protected void onPostExecute(Permissions result) {
			super.onPostExecute(result);
			Utils.printLogcatDebugMessage("end return loop "
					+ new Gson().toJson(result));
			if (loading != null)
				loading.dismiss();
			if (result != null) {
				result.setLimit();
				onPortalSettingsUpdated(result.getLimit());
			} else {
				new FinishActivityDialog(MeepTogetherMainActivity.this, R.string.Requested_information_not_found).show();
			}
		}

	}
	
	public void getPermissionList() {
		// reset list
		offlinePermission = null;
		// get list of permission
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionManager permissionManager = getPermissionManager();
				List<Permission> list = permissionManager.getAccessScheduleBlocking(UserFunction.getAccountIdentity(MeepTogetherMainActivity.this), UserFunction.getAccountID(MeepTogetherMainActivity.this));
				if (list != null) {
					Permissions permissions = new Permissions();
					for (Permission p : list) {
						// generate Permissions Object
						Utils.printLogcatDebugMessage("from home:" + p.toJson());
						if (p.getComponent() != null) {
							String appName = p.getComponent().getDisplayName();
							long id = p.getId();
							// record this appid
							UserFunction.recordAppId(appName, id);
							UserFunction.recordAppComponent(appName, p.getComponent());
							// setting permissions object
							SettingConfig config = SettingConfig.getSettingConfigByPermission(p);
							permissions.settingPermissionsObjectByName(appName, config);

						}
					}
					Utils.printLogcatDebugMessage("retrive permission stopped");
					offlinePermission = permissions;
				}
			}
		});
	}

	public void refreshOfflinePermissionList() {
		// start get offline permission
		GetOfflinePermission task = new GetOfflinePermission();
		task.execute();
	}
	
}
