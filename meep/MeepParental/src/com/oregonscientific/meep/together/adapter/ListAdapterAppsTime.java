package com.oregonscientific.meep.together.adapter;


import java.util.HashMap;
import java.util.List;
import java.util.TooManyListenersException;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.table.TablePermission;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.Consts;
import com.oregonscientific.meep.together.activity.CustomAlertDialog;
import com.oregonscientific.meep.together.activity.MeepTogetherMainActivity;
import com.oregonscientific.meep.together.activity.PortalSettingsCustomLevel;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.activity.CustomAlertDialog.OnOkListener;
import com.oregonscientific.meep.together.bean.PermissionNameIndex;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.bean.ResponseLogin;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;


public class ListAdapterAppsTime extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "ListNotification";
	private int resourceId = 0;
	private Context mContext;
	private LayoutInflater inflater;
	private String[] units;
	private String communicator;

	public ListAdapterAppsTime(Context context, int resourceId, List<HashMap<String, Object>> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		communicator = getContext().getResources().getString(R.string.main_portal_setting_label_communicator);
		units = context.getResources().getStringArray(R.array.unit_string);
		mContext = context;
	}
	
	public void refresh()
	{
		notifyDataSetChanged();  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(position == 0)
		{
			return convertView = new View(mContext);
		}
	    View view = null;
	    HashMap<String, Object> item = getItem(position);
                
    	
	    if(position< 8)
    	{
    		TextView textName;
    		TextView textTime;
    		TextView textTimeUnit;
    		final ImageView appsIcon;
    		
    		view = inflater.inflate(resourceId,null);
    		
    		try {
    			textName = (TextView)view.findViewById(R.id.textName);
    			textTime = (TextView)view.findViewById(R.id.textTime);
    			textTimeUnit = (TextView)view.findViewById(R.id.textUnit);
    			appsIcon = (ImageView)view.findViewById(R.id.apps_icon);
    			
    			
    		} catch( ClassCastException e ) {
    			Log.e(TAG, "Wrong resourceId", e);
    			throw e;
    		}
    		int t = (Integer) item.get("time");
    		String time = "";
    		String unit = "";
    		
    		switch(t)
    		{
    		case 1440: 
    			//        		time = "Unlimited"; 
    			time = units[0];
    			break;
    		case 720:
    		case 480:
    		case 240:
    		case 120:
    			time = Integer.toString(t/60); unit=" "+units[1];
    			break;
    		case 60: 
    			//        		time = Integer.toString(t/60); unit=" Hours"; 
    			time = Integer.toString(t/60); unit=" "+units[2];
    			break;
    		case 30:
    		case 15:
    			//        		time = Integer.toString(t); unit=" Minutes";
    			time = Integer.toString(t); unit=" "+units[3];
    			break;
    		case 0:
    			//        		time="Blocked"; 
    			time=units[4];
    			break;
    		}
    		
    		
    		//set
    		textName.setText((String) item.get("apps"));
    		textTime.setText(time);
    		textTimeUnit.setText(unit);
    		appsIcon.setImageResource((Integer)item.get("icon"));
    		
    	}
    	//communicator
    	else
    	{
    		TextView textName;
    		ToggleButton togglebutton;
    		final ImageView appsIcon;
    		view = ((LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_permission, null);
        	
        	try {
        		textName = (TextView)view.findViewById(R.id.textName);
        		appsIcon = (ImageView)view.findViewById(R.id.apps_icon);
        		togglebutton = (ToggleButton)view.findViewById(R.id.togglebutton);
        		initToggleButton(togglebutton, view);
        	} catch( ClassCastException e ) {
        		Log.e(TAG, "Wrong resourceId", e);
        		throw e;
        	}
        	
        	//set
        	textName.setText((String) item.get("apps"));
        	appsIcon.setImageResource((Integer)item.get("icon"));
        	String toggle = (String)item.get("toggle");
        	
        	if(toggle.equals(SettingConfig.ACCESS_ALLOW))
        	{
        		togglebutton.setChecked(true);
        	}
        	else if(toggle.equals(SettingConfig.ACCESS_DENY))
        	{
        		togglebutton.setChecked(false);
        	}
    		togglebutton.setOnClickListener(new View.OnClickListener()
        	{
				@Override
				public void onClick(View v) 
				{
					if(!UserFunction.isCreditCardVerified(mContext))
					{
						((CompoundButton) v).setChecked(false);
						UserFunction.popupMessage(R.string.need_verify_creditcard, mContext, null);
					}
					else
					{
						settingCommunicator(v);
					}
					
				}
    		});
    	}
    	
    	if(position%2 == 0)
    	{
    		view.setBackgroundResource(R.color.item_bkg_one);
    	}
    	else
    	{
    		view.setBackgroundResource(R.color.item_bkg_two);
    	}
    
    	return view;
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
	
	public void settingCommunicator(View v)
	{
		SettingConfig sc = new SettingConfig();
		ToggleButton x =(ToggleButton)v;
		Permission newPermission = new Permission();
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
		if(UserFunction.isNetworkAvailable(getContext())&&UserFunction.isOnline)
		{
			Permissions permission = RestClientUsage.infoPermission.getPermission();
			permission.setCommunicator(sc);
//			sendSavePermission(permission);
			if(mContext instanceof PortalSettingsCustomLevel)
			{
				((PortalSettingsCustomLevel)mContext).setAppPos(PermissionNameIndex.COMMUNICATOR);
				((PortalSettingsCustomLevel)mContext).settingPermissionOnline(sc);
			}
		}
		else if(!UserFunction.isOnline)
		{
			//TODO:update permission for communicator
			if(mContext instanceof PortalSettingsCustomLevel)
			{
				newPermission.setId(UserFunction.getAppId(Consts.COMMUNICATOR_DISPLAY_NAME));
				newPermission.setComponent(UserFunction.getAppComponent(Consts.COMMUNICATOR_DISPLAY_NAME));
				((PortalSettingsCustomLevel)mContext).settingPermissionOffline(newPermission);
			}
		}
		else
		{
			UserFunction.popupMessage(R.string.no_network, getContext(),null);
		}
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
