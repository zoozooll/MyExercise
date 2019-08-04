package com.mogoo.market.ui;

import com.mogoo.market.network.IBEManager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ReturnIbeParamsActivity extends Activity {
	public static final String ACTION_BROADCAST_PARAMS = "com.mogoo.market.action.BROADCAST_PARAMS";
	
	public static final String EXTRA_AKEY = "akey";
	public static final String EXTRA_UID = "uid";
	public static final String EXTRA_AID = "aid";
	public static final String EXTRA_MAS_SERVER = "mas_server";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		returnIbeParams();
	}
	
	private void returnIbeParams() {
		Intent resultIntent = new Intent();
		resultIntent.setAction(ACTION_BROADCAST_PARAMS);
		Bundle bundle = new Bundle();
		bundle.putString(EXTRA_AID, IBEManager.getInstance().getAid()); 
		bundle.putString(EXTRA_AKEY, IBEManager.getInstance().getAKey()); 
		bundle.putString(EXTRA_UID, IBEManager.getInstance().getUid()); 
		bundle.putString(EXTRA_MAS_SERVER, IBEManager.getInstance().getMasServer()); 
		resultIntent.putExtras(bundle);
		sendBroadcast(resultIntent);
		finish();
	}
}
