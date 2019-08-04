package com.oregonscientific.meep.together.adapter;


import java.util.HashMap;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.table.TablePermission;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.Consts;
import com.oregonscientific.meep.together.activity.MeepTogetherMainActivity;
import com.oregonscientific.meep.together.activity.PortalSettingsCustomLevel;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.PermissionNameIndex;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;

public class ListAdapterAppsPermission extends
		ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListAppPermission";
	private int resourceId = 0;
	private LayoutInflater inflater;

	private SettingConfig sc = null;
	private static String meepstore;
	private static String googleplay;
	private static String inapp;
	private static String internet;
	private static String osgdbadword;
	private static String[] permission;
	private Context mContext;

	public ListAdapterAppsPermission(Context context, int resourceId,
			List<HashMap<String, Object>> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		mContext = context;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		String[] appsname = getContext().getResources().getStringArray(R.array.apps_string);
		meepstore = appsname[9];
		inapp = appsname[10];
		googleplay = appsname[11];
		internet = appsname[12];
		osgdbadword = appsname[13];
		permission = getContext().getResources().getStringArray(R.array.permission_string);
		sc = new SettingConfig();
	}

	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		View view;
		HashMap<String, Object> item = getItem(position);

		TextView textName;
		ToggleButton togglebutton;
		final ImageView appsIcon;

		String toggle = (String) item.get("toggle");
		String name = (String) item.get("apps");
		int icon = (Integer) item.get("icon");

		view = inflater.inflate(resourceId, null);
		if (name.equals(meepstore)) {
			view = inflater.inflate(R.layout.item_app, null);
		}

		try {
			textName = (TextView) view.findViewById(R.id.textName);
			appsIcon = (ImageView) view.findViewById(R.id.apps_icon);
			togglebutton = (ToggleButton) view.findViewById(R.id.togglebutton);
		} catch (ClassCastException e) {
			Log.e(TAG, "Wrong resourceId", e);
			throw e;
		}

		// set
		textName.setText(name);
		appsIcon.setImageResource(icon);
		if(!name.equals(meepstore))
		{initToggleButton(togglebutton, view);}

		if (name.equals(meepstore)) {
			TextView textTime;
			TextView textTimeUnit;
			try {
				textTime = (TextView) view.findViewById(R.id.textTime);
				textTimeUnit = (TextView) view.findViewById(R.id.textUnit);
			} catch (ClassCastException e) {
				Log.e(TAG, "Wrong resourceId", e);
				throw e;
			}

			// set
			textTime.setText("");
			if (toggle.equals(SettingConfig.ACCESS_DENY)) {
				textTimeUnit.setText(permission[0]);
			} else if (toggle.equals(SettingConfig.ACCESS_APPROVAL)) {
				textTimeUnit.setText(permission[1]);
			} else if (toggle.equals(SettingConfig.ACCESS_ALLOW)) {
				textTimeUnit.setText(permission[2]);
			}
		} 
		else
		{
			if (toggle.equals(SettingConfig.ACCESS_ALLOW)||toggle.equals(SettingConfig.ACCESS_HIGH)) {
				togglebutton.setChecked(true);
			} else if (toggle.equals(SettingConfig.ACCESS_DENY)||toggle.equals(SettingConfig.ACCESS_MEDIUM)) {
				togglebutton.setChecked(false);
			}

//			if (name.equals(googleplay)) {
//				setGooglePlay(togglebutton);
//			}
			if (name.equals(inapp)) {
				setInApp(togglebutton);
			}
			if (name.equals(osgdbadword)) {
				setOsgdBadword(togglebutton);
			}
			if (name.equals(internet)) {
				setInternet(togglebutton);
			}
		}
		if (position % 2 == 0) {
			view.setBackgroundResource(R.color.item_bkg_one);
		} else {
			view.setBackgroundResource(R.color.item_bkg_two);
		}

		return view;
	}

//	public void setGooglePlay(ToggleButton togglebutton) {
//		togglebutton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				sc = new SettingConfig();
//				ContentValues values = new ContentValues();
//				ToggleButton x = (ToggleButton) v;
//				boolean on = x.isChecked();
//				if (on) {
//					// Enable
//					sc.setAccess(SettingConfig.ACCESS_ALLOW);
//					values.put(TablePermission.S_CAN_ACCESS, 1);
//				} else {
//					// Disable
//					sc.setAccess(SettingConfig.ACCESS_DENY);
//					values.put(TablePermission.S_CAN_ACCESS, 0);
//				}
//				if (UserFunction.isNetworkAvailable(getContext())&&UserFunction.isOnline) {
//					Permission permission = RestClientUsage.infoPermission.getPermission();
//					permission.setGoogleplay(sc);
//					UserFunction.sendSavePermission(permission);
//				} else if (!UserFunction.isOnline) {
//					UserFunction.runUploadService(getContext());
//					UserFunction.updateTimeStamp(getContext());
//					Uri uri = Uri.parse("content://com.oregonscientific.meep.provider/permission");
//					getContext().getContentResolver().update(uri, values, "appName = ?", new String[] { "googleplay" });
//				} else {
//					UserFunction.popupMessage(R.string.no_network, getContext(),null);
//				}
//			}
//		});
//	}

	public void setInApp(ToggleButton togglebutton) {
//		togglebutton.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				sc = new SettingConfig();
//				Permission newPermission = new Permission();
//				ToggleButton x = (ToggleButton) v;
//				boolean on = x.isChecked();
//				if (on) {
//					// Enable
//					sc.setAccess(SettingConfig.ACCESS_ALLOW);
//					newPermission.setAccessLevel(AccessLevels.ALLOW);
//					newPermission.setTimeLimit(0l);
//				} else {
//					// Disable
//					sc.setAccess(SettingConfig.ACCESS_DENY);
//					newPermission.setAccessLevel(AccessLevels.DENY);
//					newPermission.setTimeLimit(0l);
//				}
//				if (UserFunction.isNetworkAvailable(getContext())&&UserFunction.isOnline) {
//					Permissions permission = RestClientUsage.infoPermission.getPermission();
//					permission.setInapp(sc);
////					sendSavePermission(permission);
//					if(mContext instanceof PortalSettingsCustomLevel)
//					{
//						((PortalSettingsCustomLevel)mContext).setAppPos(PermissionNameIndex.INAPP);
//						((PortalSettingsCustomLevel)mContext).settingPermissionOnline(sc);
//					}
//				} else if (!UserFunction.isOnline) {
//					//TODO:update permission for inapp
//					if(mContext instanceof PortalSettingsCustomLevel)
//					{
//						newPermission.setId(UserFunction.getAppId(Consts.INAPP_DISPLAY_NAME));
//						newPermission.setComponent(UserFunction.getAppComponent(Consts.INAPP_DISPLAY_NAME));
//						((PortalSettingsCustomLevel)mContext).settingPermissionOffline(newPermission);
//					}
//				} else {
//					UserFunction.popupMessage(R.string.no_network, getContext(),null);
//				}
//			}
//		});
		setNormal(togglebutton, Consts.INAPP_DISPLAY_NAME, PermissionNameIndex.INAPP);
	}
	
	private void setOsgdBadword(ToggleButton togglebutton)
	{
		setNormal(togglebutton, Consts.OSGD_BADWORD_DISPLAY_NAME, PermissionNameIndex.OSGD_BADWORD);
	}
	
	public void setNormal(ToggleButton togglebutton,final String name,final int index) {
		togglebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sc = new SettingConfig();
				Permission newPermission = new Permission();
				ToggleButton x = (ToggleButton) v;
				boolean on = x.isChecked();
				if (on) {
					// Enable
					sc.setAccess(SettingConfig.ACCESS_ALLOW);
					newPermission.setAccessLevel(AccessLevels.ALLOW);
					newPermission.setTimeLimit(0l);
				} else {
					// Disable
					sc.setAccess(SettingConfig.ACCESS_DENY);
					newPermission.setAccessLevel(AccessLevels.DENY);
					newPermission.setTimeLimit(0l);
				}
				if (UserFunction.isNetworkAvailable(getContext())&&UserFunction.isOnline) {
					Permissions permission = RestClientUsage.infoPermission.getPermission();
					if(PermissionNameIndex.OSGD_BADWORD == index)
					{
						permission.setOsgdbadword(sc);
					}
					else
					{
						permission.setInapp(sc);
					}
//					sendSavePermission(permission);
					if(mContext instanceof PortalSettingsCustomLevel)
					{
						((PortalSettingsCustomLevel)mContext).setAppPos(index);
						((PortalSettingsCustomLevel)mContext).settingPermissionOnline(sc);
					}
				} else if (!UserFunction.isOnline) {
					//TODO:update permission for inapp
					if(mContext instanceof PortalSettingsCustomLevel)
					{
						newPermission.setId(UserFunction.getAppId(name));
						newPermission.setComponent(UserFunction.getAppComponent(name));
						((PortalSettingsCustomLevel)mContext).settingPermissionOffline(newPermission);
					}
				} else {
					UserFunction.popupMessage(R.string.no_network, getContext(),null);
				}
			}
		});
	}

	public void setInternet(ToggleButton togglebutton) {
		togglebutton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				sc = new SettingConfig();
				Permission newPermission = new Permission();
				ToggleButton x = (ToggleButton) v;
				boolean on = x.isChecked();
				if (on) {
					sc.setAccess(SettingConfig.ACCESS_HIGH);
					newPermission.setAccessLevel(AccessLevels.HIGH);
					newPermission.setTimeLimit(0l);
				} else {
					sc.setAccess(SettingConfig.ACCESS_MEDIUM);
					newPermission.setAccessLevel(AccessLevels.MEDIUM);
					newPermission.setTimeLimit(0l);
				}
				if (UserFunction.isNetworkAvailable(getContext())&&UserFunction.isOnline) {
					Permissions permission = RestClientUsage.infoPermission.getPermission();
					permission.setSecuritylevel(sc);
//					sendSavePermission(permission);
					if(getContext() instanceof PortalSettingsCustomLevel)
					{
						((PortalSettingsCustomLevel)mContext).setAppPos(PermissionNameIndex.INTERNET_SECURITY_LEVEL);
						((PortalSettingsCustomLevel)getContext()).settingPermissionOnline(sc);
					}
				} else if (!UserFunction.isOnline) {
					//TODO:update permission for security level
					if(mContext instanceof PortalSettingsCustomLevel)
					{
						newPermission.setId(UserFunction.getAppId(Consts.SECURITYLEVEL_DISPLAY_NAME));
						newPermission.setComponent(UserFunction.getAppComponent(Consts.SECURITYLEVEL_DISPLAY_NAME));
						((PortalSettingsCustomLevel)mContext).settingPermissionOffline(newPermission);
					}
				} else {
					UserFunction.popupMessage(R.string.no_network, getContext(),null);
				}
			}
		});
	}
	
	
	public void initToggleButton(ToggleButton toggleButton,View view)
	{
		final TextView textOn = (TextView) view.findViewById(R.id.textOn);
		final TextView textOff = (TextView) view.findViewById(R.id.textOff);
		toggleButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					textOn.setVisibility(View.VISIBLE);
					textOff.setVisibility(View.INVISIBLE);
				}
				else
				{
					textOff.setVisibility(View.VISIBLE);
					textOn.setVisibility(View.INVISIBLE);
				}
			}
		});
	}
	
//	private void sendSavePermission(Permissions permission)
//	{
//		if(getContext() instanceof PortalSettingsCustomLevel)
//		{
//			((PortalSettingsCustomLevel) getContext()).getLoading().show();
//			UserFunction.sendSavePermission(permission);
//			UserFunction.getRestHelp().setOnUpdatePortalSettingsListener(new OnUpdatePortalSettingsListener() {
//
//				@Override
//				public void onUpdatePortalSettingsTimeout() {
//					((PortalSettingsCustomLevel) getContext()).getLoading().dismiss();
//				}
//
//				@Override
//				public void onUpdatePortalSettingsSuccess(
//						ResponseLoadPermission infoPermission) {
//					((PortalSettingsCustomLevel) getContext()).getLoading().dismiss();
//
//				}
//
//				@Override
//				public void onUpdatePortalSettingsFailure(ResponseBasic r) {
//					((PortalSettingsCustomLevel) getContext()).getLoading().dismiss();
//
//				}
//			});
//		}
//	}
	
	

}
