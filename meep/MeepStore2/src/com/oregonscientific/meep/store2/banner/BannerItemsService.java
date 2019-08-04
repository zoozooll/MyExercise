package com.oregonscientific.meep.store2.banner;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.store2.banner.MeepStoreBannerItems.Stub;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.inapp.InAppPurchaseService;

public class BannerItemsService extends Service {

	Stub bannerItemService;

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
	private boolean retrieveBlocked = false;

	public void retriveAccountInformation() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(BannerItemsService.this.getApplication(), ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					MeepStoreLog.LogMsg( "AccountManager from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				Account account = accountManager.getLoggedInAccountBlocking();
				if (account == null) {
					MeepStoreLog.LogMsg( "Account from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					return;
				}
				// store account information to preference
				((MeepStoreApplication)getApplication()).setAccountInformation(account);
			}

		});
	}
	public void retriveAccountInformationBlocking() {
		setRetrieveBlocked(true);
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(BannerItemsService.this.getApplication(), ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					MeepStoreLog.LogMsg( "AccountManager from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					setRetrieveBlocked(false);
					return;
				}
				Account account = accountManager.getLoggedInAccountBlocking();
				if (account == null) {
					MeepStoreLog.LogMsg( "Account from MeepHome is NULL");
					// handler.sendEmptyMessage(TOKEN_NULL_DIALOG);
					account = accountManager.getLastLoggedInAccountBlocking();
					if(account!=null && account.getToken()!=null)
					{
						//skip
					}
					else
					{
						setRetrieveBlocked(false);
						return;
					}
				}
				// store account information to preference
				((MeepStoreApplication) getApplication()).setAccountInformation(account);
				setRetrieveBlocked(false);
			}

		});
		int count = 0;
		while (isRetrieveBlocked()) {
			count++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			if (count > 10) {
				break;
			}
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		bannerItemService = new MeepStoreBannerItemsImpl(this);
		return bannerItemService;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(bannerItemService!=null) ((MeepStoreBannerItemsImpl)bannerItemService).unregisterAccountCallback();
		ServiceManager.unbindServices(this);
	}


	public boolean isRetrieveBlocked() {
		return retrieveBlocked;
	}

	public void setRetrieveBlocked(boolean retrieveBlocked) {
		this.retrieveBlocked = retrieveBlocked;
	}

	
	

}
