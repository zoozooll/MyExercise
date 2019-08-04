package com.oregonscientific.meep.store2.banner;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.account.IAccountServiceCallback;
import com.oregonscientific.meep.store2.ctrl.RestRequest;
import com.oregonscientific.meep.store2.ctrl.RestRequest.PromotionListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.PromotionItem;
import com.oregonscientific.meep.util.NetworkUtils;

public class MeepStoreBannerItemsImpl extends MeepStoreBannerItems.Stub {
	public class UriSerializer implements JsonSerializer<Uri> {

		@Override
		public JsonElement serialize(Uri arg0, java.lang.reflect.Type arg1,
				JsonSerializationContext arg2) {
			return new JsonPrimitive(arg0.toString());
		}
	}

	private Gson mGson;
	private Context mContext;
	private MeepStoreApplication mApp;
	private static ArrayList<PromotionItem> promotions;

	public static ArrayList<PromotionItem> getPromotions() {
		return promotions;
	}

	private boolean blocked = false;

	public MeepStoreBannerItemsImpl(Context context) {
		MeepStoreLog.LogMsg( "create banner service");
		mGson = new GsonBuilder().registerTypeAdapter(Uri.class, new UriSerializer()).create();
		mContext = context;
		mApp = (MeepStoreApplication) context.getApplicationContext();
		RestRequest restRequest = mApp.getRestRequest();
		if (restRequest == null) {
			restRequest = new RestRequest(mContext);
			mApp.setRestRequest(restRequest);
		}
		login();
		registerAccountCallback();
	}

	public void login() {
		if (mApp.isNetworkAvailable(mContext)&& mApp.getUserToken()!="") {
			blocked = true;
			mApp.getRestRequest().setmPromotionListener(new PromotionListener() {

				@Override
				public void onPromotionReceivedFailure() {
					blocked = false;
				}

				@Override
				public void onPromotionReceived(ArrayList<PromotionItem> items) {
					promotions = items;
					blocked = false;
				}
			});
			mApp.getRestRequest().login(mApp.getUserToken());
		}
	}

	public ArrayList<String> getNPromotionItems(int n) {
		MeepStoreLog.LogMsg( "banner");
		ArrayList<PromotionItem> items = null;
		ArrayList<String> jsons = null;
		if (items != null || promotions != null) {
			items = promotions;
			jsons = new ArrayList<String>();
			int size = items.size();
			if (n > items.size()) {
				n = size;
			}
			for (int i = 0; i < n; i++) {
				PromotionItem item = items.get(i);
				MeepStoreLog.LogMsg( "banner:" + item.getPackage_name());
				Banner banner = convertBanner(item);
				String json = mGson.toJson(banner);
				jsons.add(json);
			}
		}
		return jsons;
	}

	public Banner convertBanner(PromotionItem item) {
		Banner banner = new Banner();
		banner.setPackageName(item.getPackage_name());
		banner.setImageUrl(item.getImage());

		// intent
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse("meepstore://details?id="
				+ item.getPackage_name()));
		banner.setIntent(intent);

		return banner;

	}

	@Override
	public Bundle getLatestThreeBannerItems() throws RemoteException {
		precheck();
		if (promotions == null) {
			login();
			precheck();
		}
		Bundle bundle = new Bundle();
		try {
			ArrayList<String> items = getNPromotionItems(3);
			if (items != null) {
				bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_OK);
				bundle.putStringArrayList(Consts.RESPONSE_GET_LIST_BANNER_ITEMS, items);
			} else {
				bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ITEM_UNAVAILABLE);
			}
		} catch (Exception e) {
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ERROR);
		}
		return bundle;
	}

	public void precheck() {
		int count = 0;
		while (blocked) {
			MeepStoreLog.LogMsg("blocked");
			count++;
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				break;
			}
			if (count > 30) {
				break;
			}
		}
	}

	@Override
	public Bundle getLatestBannerItems() throws RemoteException {
		precheck();
		if (promotions == null) {
			login();
			precheck();
		}
		Bundle bundle = new Bundle();
		try {
			ArrayList<String> items = getNPromotionItems(6);
			if (items != null) {
				bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_OK);
				bundle.putStringArrayList(Consts.RESPONSE_GET_LIST_BANNER_ITEMS, items);
			} else {
				bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ITEM_UNAVAILABLE);
			}
		} catch (Exception e) {
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ERROR);
		}
		return bundle;
	}

	public void setBlocked(boolean b) {
		blocked = b;
	}

	@Override
	public void registerCallback(MeepStoreBannerItemsCallback callback)
			throws RemoteException {
		MeepStoreLog.LogMsg("receive register callback");
		if (callback != null)
			mCallbacks.register(callback);

	}

	@Override
	public void unregisterCallback(MeepStoreBannerItemsCallback callback)
			throws RemoteException {
		MeepStoreLog.LogMsg("receive unregister callback");
		if (callback != null)
			mCallbacks.unregister(callback);
	}

	// for callback

	final RemoteCallbackList<MeepStoreBannerItemsCallback> mCallbacks = new RemoteCallbackList<MeepStoreBannerItemsCallback>();

	public void registerAccountCallback() {
		// Start background services and register for callbacks
		final AccountManager accountManager = (AccountManager) ServiceManager.getService(mContext, ServiceManager.ACCOUNT_SERVICE);
		accountManager.registerCallback(accountCallback);
	}

	// The callback of Account Service
	private final IAccountServiceCallback accountCallback = new IAccountServiceCallback.Stub() {

		@Override
		public void onSignIn(boolean success, String errorMessage,
				Account account) throws RemoteException {
			MeepStoreLog.LogMsg(success + account.toJson());
			if (success) {
				try {
					((BannerItemsService) mContext).retriveAccountInformationBlocking();
					final int N = mCallbacks.beginBroadcast();
					for (int i = 0; i < N; i++) {
						try {
							MeepStoreLog.LogMsg("callback run");
							Bundle bundle = getLatestThreeBannerItems();
							MeepStoreLog.LogMsg("callback run"
									+ bundle.getInt(Consts.RESPONSE_CODE));
							mCallbacks.getBroadcastItem(i).onGetBannerItems(bundle);
						} catch (RemoteException e) {
						}
					}
					mCallbacks.finishBroadcast();
				} catch (Exception e) {
					MeepStoreLog.LogMsg("exception");
				}
			}
		}

		@Override
		public void onSignOut(boolean arg0, String arg1, Account arg2)
				throws RemoteException {

		}

		@Override
		public void onUpdateUser(boolean arg0, String arg1, Account arg2)
				throws RemoteException {

		}
	};
	
	public void unregisterAccountCallback()
	{
		AccountManager accountManager = (AccountManager) ServiceManager.getService(mContext, ServiceManager.ACCOUNT_SERVICE);
		accountManager.unregisterCallback(accountCallback);
	}

}
