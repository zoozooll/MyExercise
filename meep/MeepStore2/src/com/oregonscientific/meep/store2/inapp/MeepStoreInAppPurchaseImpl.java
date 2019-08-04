package com.oregonscientific.meep.store2.inapp;

import java.util.ArrayList;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.message.common.MeepLogger;
import com.oregonscientific.meep.permission.Blacklist;
import com.oregonscientific.meep.store2.ctrl.RestRequestForInAppPurchase.GetAvailableSkusListener;
import com.oregonscientific.meep.store2.ctrl.RestRequestForInAppPurchase.GetPurchasedItemsListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.inapp.object.PurchasedItem;
import com.oregonscientific.meep.store2.inapp.object.PurchasedRecord;
import com.oregonscientific.meep.store2.inapp.object.Response;
import com.oregonscientific.meep.store2.inapp.object.ResponseAvaliableSkus;
import com.oregonscientific.meep.store2.inapp.object.ResponseGetPurchasedItem;
import com.oregonscientific.meep.store2.inapp.object.SkuDetails;

public class MeepStoreInAppPurchaseImpl extends MeepStoreInAppPurchase.Stub {
	private Gson mGson;
	private Context mContext;
	private Utils mUtility;
	private MeepStoreApplication mApp;
	

	public MeepStoreInAppPurchaseImpl(Context context) {
		mGson = new Gson();
		mContext = context;
		mApp = (MeepStoreApplication) context.getApplicationContext();
		mUtility = new Utils();
	}
	
//	public void testInsertSkus(String packageName)
//	{
//		Sku sku1 = new Sku();
//		sku1.setProductId("987654321");
//		sku1.setCoins(99);
//		sku1.setConsumable(true);
//		sku1.setPackageName(packageName);
//		sku1.setTitle("XXXX");
//		Sku sku2 = new Sku();
//		sku2.setProductId("123456789");
//		sku1.setPackageName(packageName);
//		sku2.setCoins(199);
//		sku2.setConsumable(false);
//		sku2.setTitle("YYYY");
//		ArrayList<Sku> skuStrings = new ArrayList<Sku>();
//		
//		skuStrings.add(sku1);
//		skuStrings.add(sku2);
//	}

	@Override
	public int isPurchaseSupported(int apiVersion, String packageName)
			throws RemoteException {
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, "isPurchaseSupported : " + apiVersion + "," + packageName);
		// Check whether api version is supported or not
		return mUtility.checkApiVersion(apiVersion);
	}

	@Override
	public Bundle getSkuDetails(int apiVersion, String packageName,
			Bundle skusBundle) throws RemoteException {
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, "getSkuDetails : " + apiVersion + "," + packageName+","+skusBundle);
		final Bundle bundle = new Bundle();
		if (mUtility.checkApiVersion(apiVersion) != Consts.RESULT_OK) {
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ERROR);
		} else {
			mApp.getRestRequestIAP().setmGetAvailableSkusListener(new GetAvailableSkusListener() {
				
				@Override
				public void onGetSkusSuccess(ResponseAvaliableSkus skusResponse,String packageName) {
					MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetAvailableSkus -- "+ skusResponse.getCode()+":"+skusResponse.getStatus());
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_OK);
					ArrayList<SkuDetails> skuList = skusResponse.getResult();
					ArrayList<String> skuArray = new ArrayList<String>();
					for(SkuDetails sku: skuList)
					{
						skuArray.add(mGson.toJson(sku));
					}
					bundle.putStringArray(Consts.RESPONSE_GET_SKU_DETAILS_LIST,skuArray.toArray(new String[]{}));
				}
				
				@Override
				public void onGetSkusFailure(String errorMessage) {
					MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetAvailableSkus -- "+ errorMessage);
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_ITEM_UNAVAILABLE);
					
					//TODO:delete
//					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_OK);
//					bundle.putStringArray(Consts.RESPONSE_GET_SKU_DETAILS_LIST,testData().toArray(new String[]{}));
				}
				
				@Override
				public void onGetSkusFailure(Response response) {
					MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetAvailableSkus -- "+ response.getCode()+":"+response.getStatus());
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_ERROR);
				}
			});
			
		}
		boolean getResultSuccess = sendGetAvailableSkuBlocking(packageName);
		if(!getResultSuccess) 
		{
			MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetAvailableSkus -- timeout");
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_SERVICE_UNAVAILABLE);
		}
		return bundle;
	}
	
	private boolean sendGetAvailableSkuBlocking(String packageName)
	{
		mApp.getRestRequestIAP().getAvailableSkus(packageName, mApp.getAccountToken());
		int count = 0;
		//hold until get results
		while(mApp.getRestRequestIAP().isBlockedGetSku())
		{
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			if(count>5)
			{
				return false;
			}
		}
		return true;
	}
	private boolean sendGetPurchasedItemBlocking(String packageName)
	{
		mApp.getRestRequestIAP().getPurchasedItem(packageName, mApp.getAccountToken());
		int count = 0;
		//hold until get results
		while(mApp.getRestRequestIAP().isBlockedGetPurchased())
		{
			MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"blocked");
			count++;
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			if(count>5)
			{
				return false;
			}
		}
		return true;
	}

	@Override
	public Bundle getPurchases(int apiVersion, String packageName,
			String continuationToken) throws RemoteException {
		
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, "getPurchases : " + apiVersion + "," + packageName+","+continuationToken);
		final Bundle bundle = new Bundle();
		//***********
//		String[] skuStrings = new String[] {};
//		String[] itemStrings = new String[] {};
//		bundle.putStringArray(Consts.RESPONSE_INAPP_PURCHASE_DATA_LIST, skuStrings);
//		bundle.putStringArray(Consts.RESPONSE_INAPP_PURCHASE_ITEM_LIST, itemStrings);
//		bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_OK);
		//***************
		
//		SkuDetails sku1 = new SkuDetails();
//		sku1.setProductId("com.example.app_iap.item1");
//		sku1.setCoins(30);
//		sku1.setConsumable(true);
//		sku1.setName("item1");
//		sku1.setDescription("item1 description");
//
//		PurchasedItem item1 = new PurchasedItem();
//		item1.setOrderId("212121");
//		item1.setPackageName(packageName);
//		item1.setProductId("com.example.app_iap.item1");
//		item1.setPurchaseTime("1345678900000");
//		item1.setPurchaseToken("555554444333221");
//
//		String[] skuStrings = new String[] { mGson.toJson(sku1) };
//		String[] itemStrings = new String[] { mGson.toJson(item1)};

		
		if (mUtility.checkApiVersion(apiVersion) != Consts.RESULT_OK) {
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_ERROR);
		} else {
			mApp.getRestRequestIAP().setmGetPurchasedItemsListener(new GetPurchasedItemsListener() {
				
				@Override
				public void onGetItemSuccess(ResponseGetPurchasedItem purchaseResponse) {
					MeepStoreLog.logcatMessage(MeepStoreLog.IAP_REST_REQUEST, mGson.toJson(purchaseResponse));
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_OK);
					
					ArrayList<PurchasedRecord> list = purchaseResponse.getIap_purchases(); 
					ArrayList<String> skuList = new ArrayList<String>(); 
					ArrayList<String> purchaseItemList = new ArrayList<String>(); 
					
					String packageName = purchaseResponse.getPackage_name();
					for(PurchasedRecord record: list)
					{
						skuList.add(record.generateSkuDetailsJson());
						purchaseItemList.add(record.generatePurchaseItemJson(packageName));
					}
					bundle.putStringArray(Consts.RESPONSE_INAPP_PURCHASE_ITEM_LIST,skuList.toArray(new String[]{}));
					bundle.putStringArray(Consts.RESPONSE_INAPP_PURCHASE_DATA_LIST,purchaseItemList.toArray(new String[]{}));
				}
				
				@Override
				public void onGetItemFailure(String errorMessage) {
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_ITEM_UNAVAILABLE);
					
				}
				
				@Override
				public void onGetItemFailure(Response response) {
					MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetPurchasedItems -- "+ response.getCode()+":"+response.getStatus());
					bundle.putInt(Consts.RESPONSE_CODE,Consts.RESULT_ERROR);
					
				}

				
			});
			
		}
		boolean getResultSuccess = sendGetPurchasedItemBlocking(packageName);
		if(!getResultSuccess) 
		{
			MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE,"GetPurchasedItems -- timeout");
			bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_SERVICE_UNAVAILABLE);
		}
		return bundle;
	}

	@Override
	public Bundle getBuyIntent(int apiVersion, String packageName, String sku,
			String authString) throws RemoteException {
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, "getBuyIntent : " + apiVersion + "," + packageName+","+sku+","+authString);
		Intent intent = new Intent(mContext, PurchaseActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(Consts.KEY_PURCHASE_SKU,sku);
		bundle.putString(Consts.KEY_PURCHASE_AUTH,authString);
		bundle.putString(Consts.KEY_PURCHASE_PACKAGE_NAME,packageName);
		intent.putExtras(bundle);
		
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, sku);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_SERVICE, packageName);
		PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_ONE_SHOT);
		
		bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_OK);
		bundle.putParcelable(Consts.RESPONSE_BUY_INTENT, pendingIntent);

		return bundle;
	}
	
	private ArrayList<String> testData()
	{
		SkuDetails sku1 = new SkuDetails();
		sku1.setProductId("987654321");
		sku1.setCoins(99);
		sku1.setConsumable(true);
		sku1.setName("XXXX");
		SkuDetails sku2 = new SkuDetails();
		sku2.setProductId("123456789");
		sku2.setCoins(199);
		sku2.setConsumable(false);
		sku2.setName("YYYY");
		
		ArrayList<SkuDetails> skuList = new ArrayList<SkuDetails>();
		skuList.add(sku1);
		skuList.add(sku2);
		
		ArrayList<String> skuArray = new ArrayList<String>();
		for(SkuDetails sku: skuList)
		{
			skuArray.add(mGson.toJson(sku));
		}
		return skuArray;
		
	}
	

}
