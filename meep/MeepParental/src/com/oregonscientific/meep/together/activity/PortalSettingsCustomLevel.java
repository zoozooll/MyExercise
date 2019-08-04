package com.oregonscientific.meep.together.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.CustomAlertDialog.OnOkListener;
import com.oregonscientific.meep.together.activity.PortalSettingsCustomLevel.GetOfflinePermission;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsPermission;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsTime;
import com.oregonscientific.meep.together.bean.Notification;
import com.oregonscientific.meep.together.bean.PermissionNameIndex;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnLoadPortalSettingsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class PortalSettingsCustomLevel extends Activity {

	private Button resetTimer;
	MyProgressDialog loading;
	// ====Layout Portal====
	// button
	ImageButton barLeftPortalMenu;
	// listview
	ListView listAppTime;
	ListView listPurchase;
	ListView listInternet;
	private ListAdapterAppsTime appTimeAdapter;
	private ListAdapterAppsPermission purchaseAdapter;
	private ListAdapterAppsPermission internetAdapter;
	private ArrayList<HashMap<String, Object>> arraylist_AppTime;
	private ArrayList<HashMap<String, Object>> arraylist_Purchase;
	private ArrayList<HashMap<String, Object>> arraylist_Internet;
	private Permissions offlinePermission = null;

	protected static final int SET_PERMISSION = 0;
	protected static final int SET_TIMELIMIT = 3;
	private String[] Titles;
	private String[] TimeString;
	private Integer[] images = { R.drawable.portal_apps,
			R.drawable.portal_game, R.drawable.portal_movie,
			R.drawable.portal_music, R.drawable.portal_ebook,
			R.drawable.portal_picture, R.drawable.portal_browser,
			R.drawable.portal_youtube, R.drawable.portal_livechat,
			R.drawable.portal_meepstore_icon, R.drawable.portal_purchase_icon,
			R.drawable.portal_google_play_store_icon,
			R.drawable.portal_security_icon,R.drawable.portal_badword };
	private Integer[] timelimit = { 0, 15, 30, 60, 120, 240, 480, 720, 1440 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		setContentView(R.layout.layout_main_portal_setting_custom_security_level);

		Titles = this.getResources().getStringArray(R.array.apps_string);
		TimeString = this.getResources().getStringArray(R.array.time_string);
		// ===portal setting===
		barLeftPortalMenu = (ImageButton) findViewById(R.id.mainImageButtonBack);
		listAppTime = (ListView) findViewById(R.id.listAppsTime);
		listPurchase = (ListView) findViewById(R.id.listPurchase);
		listInternet = (ListView) findViewById(R.id.listInternet);
		resetTimer = (Button) findViewById(R.id.clearTimer);
		loading = UserFunction.initLoading(this);
		resetTimer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				CustomAlertDialog dialog = new CustomAlertDialog(PortalSettingsCustomLevel.this, R.string.clear_timer);
				dialog.setOnOkListener(new OnOkListener() {
					@Override
					public void onOk() {
						clearTimer();
					}
				});
				dialog.show();
			}
		});
		// back button
		barLeftPortalMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		initPortalSettingListItem();
	}

	@Override
	protected void onResume() {
		super.onResume();

		UserFunction.getRestHelp().setOnLoadPortalSettingsListener(new OnLoadPortalSettingsListener() {

			@Override
			public void onLoadPortalSettingsSuccess(
					ResponseLoadPermission infoPermission) {
				onUpdatePortalSetting(infoPermission.getPermission());

			}

			@Override
			public void onLoadPortalSettingsFailure(ResponseBasic r) {
				onPopupResponse(R.string.load_setting_failure);
			}
		});

		// if (arraylist_AppTime.size() == 0 || arraylist_Purchase.size() == 0
		// || arraylist_Internet.size() == 0) {
		if (UserFunction.isNetworkAvailable(getApplicationContext())
				&& UserFunction.isOnline) {
			UserFunction.getRestHelp().refreshPermission(UserFunction.currentKid);
		} else if (!UserFunction.isOnline) {
			// start get offline permission
			GetOfflinePermission task = new GetOfflinePermission();
			task.execute();
		} else {
			onPopupResponse(R.string.account_offline);
			setResult(RESULT_CANCELED);
			finish();
		}
		// }
	}

	public void settingTimeLimit(int position) {
		SettingConfig newSc = new SettingConfig();
		Permission newPermission = new Permission();
		if (position == 0) {
			newSc.setAccess(SettingConfig.ACCESS_DENY);
			newPermission.setAccessLevel(AccessLevels.DENY);
		} else {
			newSc.setAccess(SettingConfig.ACCESS_ALLOW);
			newPermission.setAccessLevel(AccessLevels.ALLOW);
		}
		newSc.setTimelimit(timelimit[position]);
		newPermission.setTimeLimit(timelimit[position] * Consts.ONE_MINUTE);
		// setting permission info
		if (UserFunction.isNetworkAvailable(getApplicationContext())
				&& UserFunction.isOnline) {
			settingPermissionOnline(newSc);
		} else if (!UserFunction.isOnline) {
			String appName = PermissionNameIndex.LIST_NAME[appPos];
			long id = UserFunction.getAppId(appName);
			Component component = UserFunction.getAppComponent(appName);
			newPermission.setId(id);
			newPermission.setComponent(component);
			Utils.printLogcatDebugMessage("appname:" + appName + " appid:" + id
					+ " componentName:" + component.getDisplayName()
					+ " componentId:" + component.getId());
			settingPermissionOffline(newPermission);
		} else {
			onPopupResponse(R.string.no_network);
		}
	}

	public void settingPermissionOnline(SettingConfig newSc) {
		if (loading != null && !loading.isShowing()) {
			loading.show();
		}
		Permissions permission = getUpdatedPermissionObject(RestClientUsage.infoPermission.getPermission(), newSc);
		// RestClientUsage.infoPermission.setPermission(permission);
		UserFunction.getRestHelp().setOnUpdatePortalSettingsListener(new OnUpdatePortalSettingsListener() {

			@Override
			public void onUpdatePortalSettingsSuccess(
					ResponseLoadPermission infoPermission) {
				onUpdatePortalSetting(infoPermission.getPermission());
				loading.dismiss();
			}

			@Override
			public void onUpdatePortalSettingsFailure(ResponseBasic r) {
				onPopupResponse(R.string.load_setting_failure);
				loading.dismiss();
			}

			@Override
			public void onUpdatePortalSettingsTimeout() {
				onPopupResponse(R.string.please_retry);
				loading.dismiss();

			}
		});
		UserFunction.getRestHelp().savePermission(UserFunction.currentKid, permission);
	}

	PermissionManager myPermissionManager = null;

	public PermissionManager getPermissionManager() {
		if (myPermissionManager == null) {
			myPermissionManager = (PermissionManager) ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
		}
		return myPermissionManager;

	}

	public void settingPermissionOffline(Permission newPermission) {
		// update permission
		PermissionManager permissionManager = getPermissionManager();
		permissionManager.updatePermission(UserFunction.getAccountIdentity(this), UserFunction.getAccountID(this), newPermission);
		refreshOfflinePermissionList();
	}

	public Permissions getUpdatedPermissionObject(Permissions permission,
			SettingConfig newSc) {
		switch (appPos) {
		case 0:
			permission.setApps(newSc);
			permission.setGame(newSc);
			break;
		case 1:
			permission.setGame(newSc);
			permission.setApps(newSc);
			break;
		case 2:
			permission.setMovie(newSc);
			break;
		case 3:
			permission.setMusic(newSc);
			break;
		case 4:
			permission.setEbook(newSc);
			break;
		case 5:
			permission.setPicture(newSc);
			break;
		case 6:
			permission.setBrowser(newSc);
			break;
		case 7:
			permission.setYoutube(newSc);
			break;
		case PermissionNameIndex.PURCHASE:
			permission.setPurchase(newSc);
			break;
		case PermissionNameIndex.COMMUNICATOR:
			permission.setCommunicator(newSc);
			break;
		case PermissionNameIndex.INAPP:
			permission.setInapp(newSc);
			break;
		case PermissionNameIndex.INTERNET_SECURITY_LEVEL:
			permission.setSecuritylevel(newSc);
			break;
		case PermissionNameIndex.OSGD_BADWORD:
			permission.setOsgdbadword(newSc);
			break;
		default:
			break;
		}
		return permission;
	}

	/**
	 * Portal Setting
	 */
	private int appPos = -1;

	public void initPortalSettingListItem() {
		// apps time limit
		arraylist_AppTime = new ArrayList<HashMap<String, Object>>();
		appTimeAdapter = new ListAdapterAppsTime(PortalSettingsCustomLevel.this, R.layout.item_app, arraylist_AppTime);
		listAppTime.setAdapter(appTimeAdapter);
		listAppTime.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 < 8) {
					String title = Titles[arg2];
					// for(int i=0;i<timelimit.length;i++)
					// {
					// if(limit.get(arg2).getTimelimit() == timelimit[i])
					// {
					// timePos = i;
					// }
					// }
					appPos = arg2;
					int index = findTimeLimitIndex((Integer) arraylist_AppTime.get(arg2).get("time"));
					toSetTimeLimit(title, index);
				} else {

				}
			}
		});
		// purchase
		arraylist_Purchase = new ArrayList<HashMap<String, Object>>();
		purchaseAdapter = new ListAdapterAppsPermission(PortalSettingsCustomLevel.this, R.layout.item_permission, arraylist_Purchase);
		listPurchase.setAdapter(purchaseAdapter);
		listPurchase.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				if (arg2 == 0) {
					toSetPermission();
					appPos = 9;
				}
			}
		});

		// internet
		arraylist_Internet = new ArrayList<HashMap<String, Object>>();
		internetAdapter = new ListAdapterAppsPermission(PortalSettingsCustomLevel.this, R.layout.item_permission, arraylist_Internet);
		listInternet.setAdapter(internetAdapter);

	}

	protected void onActivityResult(int requestCode, int resultCode,
			Intent returnIntent) {
		super.onActivityResult(requestCode, resultCode, returnIntent);
		switch (requestCode) {
		case SET_PERMISSION:
			if (resultCode == RESULT_OK) {
				settingPurchasePermission(returnIntent.getIntExtra("index_permission", 0));
			}
			break;
		case SET_TIMELIMIT:
			if (resultCode == RESULT_OK) {
				settingTimeLimit(returnIntent.getIntExtra("index_time", 0));
			}
			break;
		}
	}

	public void settingPurchasePermission(int position) {
		SettingConfig newSc = new SettingConfig();
		Permission newPermission = new Permission();
		switch (position) {
		case 0:
			newSc.setAccess(SettingConfig.ACCESS_DENY);
			newPermission.setAccessLevel(AccessLevels.DENY);
			break;
		case 1:
			newSc.setAccess(SettingConfig.ACCESS_APPROVAL);
			newPermission.setAccessLevel(AccessLevels.APPROVAL);
			break;
		case 2:
			newSc.setAccess(SettingConfig.ACCESS_ALLOW);
			newPermission.setAccessLevel(AccessLevels.ALLOW);
			break;
		}
		newSc.setTimelimit(0);
		newPermission.setTimeLimit(1440 * Consts.ONE_MINUTE);

		if (UserFunction.isNetworkAvailable(getApplicationContext())
				&& UserFunction.isOnline) {
			settingPermissionOnline(newSc);
		} else if (!UserFunction.isOnline) {
			newPermission.setId(UserFunction.getAppId(Consts.PURCHASE_DISPLAY_NAME));
			newPermission.setComponent(UserFunction.getAppComponent(Consts.PURCHASE_DISPLAY_NAME));
			settingPermissionOffline(newPermission);
		} else {
			onPopupResponse(R.string.no_network);
		}
		// String p = newSc.getAccess();
		// HashMap<String, Object> item = arraylist_Purchase.get(0);
		// item.put("toggle", p);
		// arraylist_Purchase.set(0, item);
		// purchaseAdapter.notifyDataSetChanged();
	}

	/**
	 * toast
	 */
	public void onUpdatePermission(int timePos) {
		onPopupResponse(R.string.permission_save);
		barLeftPortalMenu.setEnabled(true);
		if (appPos >= 0) {
			// modify portal setting page
			View appView = (View) listAppTime.getChildAt(appPos);
			TextView t1 = (TextView) appView.findViewById(R.id.textTime);
			t1.setText(TimeString[timePos]);
			TextView t2 = (TextView) appView.findViewById(R.id.textUnit);
			t2.setText("");
			appPos = -1;
		}
	}

	private void toSetPermission() {
		Intent intent = new Intent();
		intent.setClass(this, PortalSettingsPermission.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		startActivityForResult(intent, SET_PERMISSION);
	}

	private void toSetTimeLimit(String title, int index) {
		Intent intent = new Intent();
		intent.setClass(this, PortalSettingsTimeLimit.class);
		intent.putExtra("title", title);
		intent.putExtra("index", index);
		startActivityForResult(intent, SET_TIMELIMIT);
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
				List<Permission> list = permissionManager.getAccessScheduleBlocking(UserFunction.getAccountIdentity(PortalSettingsCustomLevel.this), UserFunction.getAccountID(PortalSettingsCustomLevel.this));
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

	private Permissions settingPermissionsObjectById(Permissions permissions,
			int id, SettingConfig config) {
		switch (id) {
		case 1:// picture
			permissions.setPicture(config);
			break;
		case 2:// youtube
			permissions.setYoutube(config);
			break;
		case 3:// ebook
			permissions.setEbook(config);
			break;
		case 4:// movie
			permissions.setMovie(config);
			break;
		case 5:// apps
			permissions.setApps(config);
			break;
		case 6:// game
			permissions.setGame(config);
			break;
		case 7:// music
			permissions.setMusic(config);
			break;
		case 8:// browser
			permissions.setBrowser(config);
			break;
		case 9:// communicator
			permissions.setCommunicator(config);
			break;
		case 10:// inapp
			permissions.setInapp(config);
			break;
		case 11:// googleplay
			permissions.setGoogleplay(config);
			break;
		case 12:// securitylevel
			permissions.setSecuritylevel(config);
			break;
		case 13:// purchase
			permissions.setPurchase(config);
			break;
		case 14:// purchase
			permissions.setOsgdbadword(config);
			break;
		default:
			break;
		}
		return permissions;
	}

	public void clearTimer() {
		// clear history
		PermissionManager permissionManager = getPermissionManager();
		permissionManager.clearRunHistories(UserFunction.getAccountIdentity(this), UserFunction.getAccountID(this));
	}

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
				onUpdatePortalSetting(result);
			} else {
				new FinishActivityDialog(PortalSettingsCustomLevel.this, R.string.Requested_information_not_found).show();
			}
		}

	}

	public void refreshOfflinePermissionList() {
		// start get offline permission
		GetOfflinePermission task = new GetOfflinePermission();
		task.execute();
	}

	public void onUpdatePortalSetting(Permissions p) {
		clearPermissionList();
		int i = 0;
		for (SettingConfig x : p.getLimit()) {
			// put into ListView
			if (i <= 7) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("apps", Titles[i]);
				map.put("time", x.getTimelimit());
				map.put("icon", images[i]);
				arraylist_AppTime.add(map);
			} else if (i <= 13) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("apps", Titles[i]);
				map.put("icon", images[i]);
				map.put("toggle", x.getAccess());
				if (i == 8) {
					arraylist_AppTime.add(map);
				} else if (i == 12 || i == 13) {
					arraylist_Internet.add(map);
				} else if (i == 11) {
					// skip googleplay
				} else {
					arraylist_Purchase.add(map);
				}

			}
			i++;
		}
		appTimeAdapter.notifyDataSetChanged();
		purchaseAdapter.notifyDataSetChanged();
		internetAdapter.notifyDataSetChanged();

		UserFunction.setListViewHeightBasedOnChildren(listAppTime);
		UserFunction.setListViewHeightBasedOnChildren(listPurchase);
		UserFunction.setListViewHeightBasedOnChildren(listInternet);
	}

	public void clearPermissionList() {
		arraylist_AppTime.clear();
		arraylist_Internet.clear();
		arraylist_Purchase.clear();
	}

	public int findTimeLimitIndex(int time) {
		for (int i = 0; i < timelimit.length; i++) {
			if (time == timelimit[i]) {
				return i;
			}
		}
		return -1;
	}

	public void onPopupResponse(int resId) {
		UserFunction.popupMessage(resId, PortalSettingsCustomLevel.this, loading);
	}

	public void onPopupResponse(String status) {
		UserFunction.popupResponse(status, PortalSettingsCustomLevel.this, loading);
	}

	@Override
	protected void onStop() {
		super.onStop();
		ServiceManager.unbindServices(this);
	}

	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.layout_rever_full_to_null_slide, R.anim.layout_rever_null_to_full_slide);
	}
	
	public MyProgressDialog getLoading() {
		return loading;
	}

	public void setAppPos(int position) {
		this.appPos = position;
	}

}
