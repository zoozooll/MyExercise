package com.oregonscientific.meep.store2.ctrl;

import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.inapp.Consts;
import com.oregonscientific.meep.store2.inapp.object.ContentPurchase;
import com.oregonscientific.meep.store2.inapp.object.ContentVerify;
import com.oregonscientific.meep.store2.inapp.object.Response;
import com.oregonscientific.meep.store2.inapp.object.ResponseAvaliableSkus;
import com.oregonscientific.meep.store2.inapp.object.ResponseGetPurchasedItem;
import com.oregonscientific.meep.store2.inapp.object.ResponsePurchase;
import com.oregonscientific.meep.store2.inapp.object.ResponseVerify;

public class RestRequestForInAppPurchase {

	public interface GetAvailableSkusListener {
		void onGetSkusSuccess(ResponseAvaliableSkus skusResponse,
				String packageName);

		void onGetSkusFailure(Response response);

		void onGetSkusFailure(String errorMessage);
	}

	public interface ProcessPurchaseListener {
		void onPurchaseSuccess(ResponsePurchase purchaseResponse);

		void onPurchaseFailure(Response response);

		void onPurchaseFailure(String errorMessage);
	}

	public interface GetPurchasedItemsListener {
		void onGetItemSuccess(ResponseGetPurchasedItem purchaseResponse);

		void onGetItemFailure(Response response);

		void onGetItemFailure(String errorMessage);
	}

	public interface VerifyPurchaseListener {
		void onVerifySuccess(ResponseVerify verifyResponse);

		void onVerifyFailure(Response response);

		void onVerifyFailure(String errorMessage);
	}

	private boolean isBlockedGetSku = false;
	private boolean isBlockedGetPurchased = false;
	private boolean isBlockedPurchase = false;

	private static RestRequestClient mClient;
	private Gson mGson;
	private Context mContext;
	private GetAvailableSkusListener mGetAvailableSkusListener;
	private GetPurchasedItemsListener mGetPurchasedItemsListener;
	private ProcessPurchaseListener mProcessPurchaseListener;
	private VerifyPurchaseListener mVerifyPurchaseListener;

	public RestRequestForInAppPurchase(Context context) {
		mClient = new RestRequestClient();
		mGson = new Gson();
		mContext = context;
	}

	public void getAvailableSkus(final String packageName, String token) {
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, "getAvailableSkus -- packageName:"
				+ packageName);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, "getAvailableSkus -- token:"
				+ token);
		setBlockedGetSku(true);
		mClient.initVersion(1);
		mClient.getJsonAuth(Consts.URL_IAP_AVAILABLE_SKUS + packageName, token, new JsonHttpResponseHandler() {
			String pkgName = packageName;

			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, content);
				if (mGetAvailableSkusListener != null)
					mGetAvailableSkusListener.onGetSkusFailure(content);
				setBlockedGetSku(false);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, arg1.toString());
				if (mGetAvailableSkusListener != null)
					mGetAvailableSkusListener.onGetSkusFailure(mGson.toJson(arg1, Response.class));
				setBlockedGetSku(false);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, json.toString());
				if (mGetAvailableSkusListener != null)
					mGetAvailableSkusListener.onGetSkusSuccess(mGson.fromJson(json.toString(), ResponseAvaliableSkus.class), pkgName);
				setBlockedGetSku(false);
			}
		});
	}

	public void getPurchasedItem(final String packageName, String token) {
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, "lock");
		setBlockedGetPurchased(true);
		mClient.initVersion(1);
		mClient.getJsonAuth(Consts.URL_IAP_PURCHASED_ITEM_PREFIX + packageName
				+ Consts.URL_IAP_PURCHASED_ITEM_SUFFIX, token, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, content);
				if (mGetPurchasedItemsListener != null)
					mGetPurchasedItemsListener.onGetItemFailure(content);
				setBlockedGetPurchased(false);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, arg1.toString());
				if (mGetPurchasedItemsListener != null)
					mGetPurchasedItemsListener.onGetItemFailure(mGson.toJson(arg1, Response.class));
				setBlockedGetPurchased(false);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, json.toString());
				if (mGetPurchasedItemsListener != null)
					mGetPurchasedItemsListener.onGetItemSuccess(mGson.fromJson(json.toString(), ResponseGetPurchasedItem.class));
				setBlockedGetPurchased(false);

			}
		});
	}

	public void purchaseInAppItem(ContentPurchase content, String token) {
		setBlockedPurchase(true);
		mClient.initVersion(1);
		mClient.postJsonAuth(mContext, Consts.URL_IAP_PURCHASE, mGson.toJson(content), token, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, content);
				if (mProcessPurchaseListener != null)
					mProcessPurchaseListener.onPurchaseFailure(content);
				setBlockedPurchase(false);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, arg1.toString());
				if (mProcessPurchaseListener != null)
					mProcessPurchaseListener.onPurchaseFailure(mGson.fromJson(arg1.toString(), Response.class));
				setBlockedPurchase(false);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, json.toString());
				if (mProcessPurchaseListener != null)
					mProcessPurchaseListener.onPurchaseSuccess(mGson.fromJson(json.toString(), ResponsePurchase.class));
				setBlockedPurchase(false);
			}
		});
	}

	public void verifyPurchase(ContentVerify content, String token) {
		mClient.initVersion(1);
		mClient.postJsonAuth(mContext, Consts.URL_IAP_VERIFY, mGson.toJson(content), token, new JsonHttpResponseHandler() {
			@Override
			public void onFailure(Throwable error, String content) {
				super.onFailure(error, content);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, content);
				if (mVerifyPurchaseListener != null)
					mVerifyPurchaseListener.onVerifyFailure(content);
			}

			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, arg1.toString());
				if (mVerifyPurchaseListener != null)
					mVerifyPurchaseListener.onVerifyFailure(mGson.fromJson(arg1.toString(), Response.class));
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, json.toString());
				if (mVerifyPurchaseListener != null)
					mVerifyPurchaseListener.onVerifySuccess(mGson.fromJson(json.toString(), ResponseVerify.class));
			}
		});
	}

	public GetAvailableSkusListener getmGetAvailableSkusListener() {
		return mGetAvailableSkusListener;
	}

	public void setmGetAvailableSkusListener(
			GetAvailableSkusListener mGetAvailableSkusListener) {
		this.mGetAvailableSkusListener = mGetAvailableSkusListener;
	}

	public ProcessPurchaseListener getmProcessPurchaseListener() {
		return mProcessPurchaseListener;
	}

	public void setmProcessPurchaseListener(
			ProcessPurchaseListener mProcessPurchaseListener) {
		this.mProcessPurchaseListener = mProcessPurchaseListener;
	}

	public GetPurchasedItemsListener getmGetPurchasedItemsListener() {
		return mGetPurchasedItemsListener;
	}

	public void setmGetPurchasedItemsListener(
			GetPurchasedItemsListener mGetPurchasedItemsListener) {
		this.mGetPurchasedItemsListener = mGetPurchasedItemsListener;
	}

	public VerifyPurchaseListener getmVerifyPurchaseListener() {
		return mVerifyPurchaseListener;
	}

	public void setmVerifyPurchaseListener(
			VerifyPurchaseListener mVerifyPurchaseListener) {
		this.mVerifyPurchaseListener = mVerifyPurchaseListener;
	}

	public boolean isBlockedGetSku() {
		return isBlockedGetSku;
	}

	public void setBlockedGetSku(boolean isBlockedGetSku) {
		this.isBlockedGetSku = isBlockedGetSku;
	}

	public boolean isBlockedPurchase() {
		return isBlockedPurchase;
	}

	public void setBlockedPurchase(boolean isBlockedPurchase) {
		this.isBlockedPurchase = isBlockedPurchase;
	}

	public boolean isBlockedGetPurchased() {
		return isBlockedGetPurchased;
	}

	public void setBlockedGetPurchased(boolean isBlockedGetPurchased) {
		this.isBlockedGetPurchased = isBlockedGetPurchased;
	}

}
