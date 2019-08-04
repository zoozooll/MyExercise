package com.oregonscientific.meep.together.activity;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadPermission;
import com.oregonscientific.meep.together.comparision.PermissionGenerator;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdatePortalSettingsListener;

public class PortalSettingsDefinedLevel extends Activity {
	TextView textTitle;
	TextView textDescription;
	Button btnSelect;
	ImageButton btnBack;
	public static final int TYPE_SCURITY_LEVEL_HIGH = 0;
	public static final int TYPE_SCURITY_LEVEL_LOW = 1;
	public static final String KEY_SCURITY_LEVEL = "level";

	private int type;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.layout_null_to_full_slide, R.anim.layout_full_to_null_slide);
		setContentView(R.layout.layout_main_portal_setting_security_level_description);
		setType(getIntent().getIntExtra(KEY_SCURITY_LEVEL, TYPE_SCURITY_LEVEL_HIGH));
		textTitle = (TextView) findViewById(R.id.textTitle);
		textDescription = (TextView) findViewById(R.id.textDescription);
		textDescription.setMovementMethod(new ScrollingMovementMethod());
		btnSelect = (Button) findViewById(R.id.btnSelect);
		btnBack = (ImageButton) findViewById(R.id.barImageButtonBack);
		loading = UserFunction.initLoading(this);
	}

	private MyProgressDialog loading;

	@Override
	protected void onResume() {
		super.onResume();

		switch (getType()) {
		case TYPE_SCURITY_LEVEL_HIGH:
			textTitle.setText(R.string.high_security_level_title);
			textDescription.setText(R.string.high_security_level_description);
			break;
		case TYPE_SCURITY_LEVEL_LOW:
			textTitle.setText(R.string.low_security_level_title);
			textDescription.setText(R.string.low_security_level_description);
			break;
		}

		btnSelect.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (UserFunction.isNetworkAvailable(PortalSettingsDefinedLevel.this)
						&& UserFunction.isOnline) {
					PermissionGenerator generator = new PermissionGenerator();
					Permissions permissions = null;
					switch (getType()) {
					case TYPE_SCURITY_LEVEL_HIGH:
						permissions = generator.generateHighSecurityLevel();
						break;
					case TYPE_SCURITY_LEVEL_LOW:
						permissions = generator.generateLowSecurityLevel();
						break;
					}
					if (permissions != null) {
						sendUpdatePermission(permissions);
					}
				} else if (!UserFunction.isOnline) {
					setOfflinePermission();
				} else {
					UserFunction.popupMessage(R.string.account_offline, PortalSettingsDefinedLevel.this, null);
					finish();
				}
			}
		});

		btnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private void sendUpdatePermission(Permissions permissions) {
		UserFunction.getRestHelp().setOnUpdatePortalSettingsListener(new OnUpdatePortalSettingsListener() {

			@Override
			public void onUpdatePortalSettingsSuccess(
					ResponseLoadPermission infoPermission) {
				if (loading != null && loading.isShowing()) {
					loading.dismiss();
				}
				setResult(RESULT_OK);
				finish();

			}

			@Override
			public void onUpdatePortalSettingsFailure(ResponseBasic r) {
				UserFunction.popupMessage(R.string.update_settings_failure, PortalSettingsDefinedLevel.this, loading);

			}

			@Override
			public void onUpdatePortalSettingsTimeout() {
				UserFunction.popupMessage(R.string.please_retry, PortalSettingsDefinedLevel.this, loading);
			}

		});

		Utils.printLogcatDebugMessage("start save" + getType());
		UserFunction.getRestHelp().savePermission(UserFunction.currentKid, permissions);
		loading.show();

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	// for offline mode
	PermissionManager myPermissionManager = null;

	public PermissionManager getPermissionManager() {
		if (myPermissionManager == null) {
			myPermissionManager = (PermissionManager) ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
		}
		return myPermissionManager;

	}

	public void setOfflinePermission() {

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				PermissionGenerator generator = new PermissionGenerator();
				List <Permission> list = null;
				list = generator.generateOfflinePermissionList(getType());
				for(Permission p:list)
				{
					getPermissionManager().updatePermission(null, UserFunction.getAccountID(PortalSettingsDefinedLevel.this), p);
				}
				handler.sendEmptyMessage(0);
			}
		});
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			Intent intent = new Intent();
			intent.putExtra(KEY_SCURITY_LEVEL, getType());
			setResult(RESULT_OK, intent);
			finish();
		};
	};
	
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

}
