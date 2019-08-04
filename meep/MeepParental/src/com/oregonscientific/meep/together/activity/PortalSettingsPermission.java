package com.oregonscientific.meep.together.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.adapter.ListAdapterAppsPermissionConfig;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;

public class PortalSettingsPermission extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		initUI();
	}

	public void initUI() {
		setContentView(R.layout.layout_main_portal_setting_time);

		// set title
		((TextView) findViewById(R.id.dialogBarTitle)).setText(this
				.getResources().getStringArray(R.array.apps_string)[9]);

		// get listview
		ListView permission = (ListView) findViewById(R.id.listTimeLimit);
		LayoutParams lp = permission.getLayoutParams();
		lp.height = 170;
		permission.setLayoutParams(lp);

		// define an array for permission-string storage
		ArrayList<String> items = new ArrayList<String>();

		ListAdapterAppsPermissionConfig adapter = new ListAdapterAppsPermissionConfig(
				this, R.layout.item_permission_config, items);

		// set adapter for listview
		permission.setAdapter(adapter);

		// add string into array
		for (String s : this.getResources().getStringArray(
				R.array.permission_string)) {
			items.add(s);
		}

		// notify change
		adapter.notifyDataSetChanged();

		// final String permi[] = new
		// String[]{SettingConfig.ACCESS_DENY,SettingConfig.ACCESS_APPROVAL,SettingConfig.ACCESS_ALLOW};

		permission.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					final int arg2, long arg3) {
				// SettingConfig sc = new SettingConfig();
				// switch(arg2)
				// {
				// case 0: sc.setAccess(SettingConfig.ACCESS_DENY); break;
				// case 1: sc.setAccess(SettingConfig.ACCESS_APPROVAL); break;
				// case 2: sc.setAccess(SettingConfig.ACCESS_ALLOW); break;
				// }
				// Permission permission =
				// RestClientUsage.infoPermission.getPermission();
				// permission.setPurchase(sc);

//				if(timeTick != null)
//				{
//					timeTick.setVisibility(View.GONE);
//				}
//				timeTick = (ImageView) arg1.findViewById(R.id.isTick);
//				timeTick.setVisibility(View.VISIBLE);
				
				Intent intent = getIntent();
				intent.putExtra("index_permission", arg2);
				setResult(RESULT_OK, intent);
				finish();
//				if (UserFunction.isNetworkAvailable(getApplicationContext())
//						&& MeepTogetherMainActivity.isOnline()) {
//					Intent i = new Intent();
//					i.putExtra("index_permission", arg2);
//					PortalSettingsPermission.this.setResult(RESULT_OK, i);
//					PortalSettingsPermission.this.finish();
//				} else if (!MeepTogetherMainActivity.isOnline()) {
//					UserFunction.popupMessage(R.string.account_offline,
//							PortalSettingsPermission.this);
//				} else {
//					UserFunction.popupMessage(R.string.no_network,
//							PortalSettingsPermission.this);
//				}

			}
		});

		((ImageButton) findViewById(R.id.mainImageButtonBack))
				.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						finish();
					}
				});
	}

	public void sendUpdatedPermission(Permissions permission) {
		UserFunction.sendSavePermission(permission);
		UserFunction.getRestHelp().setOnUpdatePortalSettingsListener(
				new OnUpdatePortalSettingsListener() {

					@Override
					public void onUpdatePortalSettingsSuccess(
							ResponseLoadPermission infoPermission) {
						Intent i = new Intent();
						// i.putExtra("permission",p);
						PortalSettingsPermission.this.setResult(RESULT_OK, i);
						PortalSettingsPermission.this.finish();
					}

					@Override
					public void onUpdatePortalSettingsFailure(ResponseBasic r) {
						UserFunction.popupResponse(r.getStatus(), PortalSettingsPermission.this, null);
					}

					@Override
					public void onUpdatePortalSettingsTimeout() {
						// TODO Auto-generated method stub
						UserFunction.popupMessage(R.string.please_retry, PortalSettingsPermission.this, null);
						
					}
				});
	}

}
