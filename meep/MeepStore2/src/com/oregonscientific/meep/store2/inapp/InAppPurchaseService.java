package com.oregonscientific.meep.store2.inapp;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.store2.MainActivity;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.inapp.MeepStoreInAppPurchase;
import com.oregonscientific.meep.store2.inapp.MeepStoreInAppPurchase.Stub;

public class InAppPurchaseService extends Service {

	Stub inAppPurchase;

	@Override
	public void onCreate() {
		super.onCreate();
		retriveAccountInformation();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		retriveAccountInformation();
		return super.onStartCommand(intent, flags, startId);
	}

	public void retriveAccountInformation() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(InAppPurchaseService.this, ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					Log.d("test", "AccountManager from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				Account account = accountManager.getLoggedInAccountBlocking();
				if (account == null) {
					Log.d("test", "Account from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				// store account information to preference
				((MeepStoreApplication)getApplication()).setAccountInformation(account);
			}

		});
	}
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		inAppPurchase = new MeepStoreInAppPurchaseImpl(this);
		return inAppPurchase;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		ServiceManager.unbindServices(this);
	}

//	private final BroadcastReceiver receiver = new BroadcastReceiver() {
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			String action = intent.getAction();
//			if (action.equals("com.oregonscientific.meep.store2.inapp.buy")) {
//				test();
//			}
//		}
//	};
//	
//	public void test()
//	{
//		Log.v("test", "buy");
//	}

}
