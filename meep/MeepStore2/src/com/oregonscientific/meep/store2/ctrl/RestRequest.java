package com.oregonscientific.meep.store2.ctrl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.ContentRecoveryFeedback;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItemDetailsFeedback;
import com.oregonscientific.meep.store2.object.MeepStoreLoginInfo;
import com.oregonscientific.meep.store2.object.MyAppItems;
import com.oregonscientific.meep.store2.object.OtaUpdateFeedback;
import com.oregonscientific.meep.store2.object.PromotionItem;
import com.oregonscientific.meep.store2.object.PurchaseFeedback;
import com.oregonscientific.meep.store2.object.RedeemCoinsFeedback;
import com.oregonscientific.meep.store2.object.RedeemConfirmFeedback;
import com.oregonscientific.meep.store2.object.RedeemFeedback;
import com.oregonscientific.meep.store2.object.StoreFeedback;


public class RestRequest {

	private final String TAG = "RestRequest";
	
	private final String URL_APP = "https://portal.meeptablet.com/1/store/items/";
	private final String URL_PURCHASE = "https://portal.meeptablet.com/2/store/purchase/";
	private final String URL_CONFIRM_REDEEM = "https://portal.meeptablet.com/2/store/coupon/confirmredeem/";
	private final String URL_REDEEM = "https://portal.meeptablet.com/1/store/coupon/redeem/";
	private final String URL_LOGIN = "https://portal.meeptablet.com/2/store/welcome";
	private final String URL_COIN_REQUEST = "https://portal.meeptablet.com/1/store/coinrequest";
	private final String URL_GET_ITEM = "https://portal.meeptablet.com/1/store/item/";
	private final String URL_GET_PURCHASED_ITEM = "https://portal.meeptablet.com/1/store/items/purchased";
	private final String URL_SEARCH_ITEM = "https://portal.meeptablet.com/1/store/search/";
	private final String URL_PRELOAD_RECOVERY = "https://portal.meeptablet.com/1/store/contentrecovery";
	private final String URL_OTA_UPDATE = "https://portal.meeptablet.com/1/store/ota";
	
	public static final String CAT_ALL = "all";
	public static final String CAT_BOOKS = "Books";
	public static final String CAT_ARCADE_ACTION = "Arcade & action";
	public static final String CAT_EDUCATION = "Education";
	public static final String CAT_VIDEO = "Video";
	public static final String CAT_MUSIC = "Music & audio";
	public static final String CAT_ENTERTAINMENT = "Entertainment";
	public static final String CAT_PUZZLE = "Brain & Puzzle";
	public static final String CAT_TOOLS = "Tools";
	public static final String CAT_CARDS = "Cards";
	public static final String CAT_CASUAL = "Casual";
	public static final String CAT_RACING = "Racing";
	public static final String CAT_SPORT = "Sports";
	public static final String CAT_PHOTOGRAPHY = "Photography";
	
	public static final String PRICE_ALL = "all";
	public static final String PRICE_FREE = "free";
	public static final String PRICE_PAID= "paid";
	public static final String PRICE_GOOGLDPLAY = "googleplay";
	
	public static final int HTTP_DEFAULT_TIMEOUT = 30000;
	
	private static AsyncHttpClient mHttpClient;
	private Context mContext;
	private SearchItemListener mSearchItemListener;
	private PurchaseItemListener mPurchaseItemListener;
	private RedeemListener mRedeemListener;
	private LoginListener mLoginListener;
	private PromotionListener mPromotionListener;
	private RequestCoinsListener mRequestCoinsListener;
	private GetStoreItemListener mGetStoreItemListener;
	private GetPurchasedItemListener mGetPurchasedListener;
	private RecoveryContentListener mRecoveryContentListener;
	private OtaUpdateListener mOtaUpdateListener;
	
	

	public interface SearchItemListener{
		public abstract void onSearchItemReceived(int code, String msg, ArrayList<MeepStoreItem> itemList, int total);
	}
	
	public interface PurchaseItemListener{
		public abstract void onPurchaseReceived(PurchaseFeedback feedback);
	}
	
	public interface RedeemListener{
		public abstract void onRedeemReceived(RedeemFeedback feedback);
		//public abstract void onRedeenCoinsReceived(RedeemCoinsFeedback feedback);
		public abstract void onConfirmRedeemReceived(RedeemConfirmFeedback redeemConfirmFeedbacks);
	}
	
	public interface LoginListener{
		public abstract void onLoginReceived(MeepStoreLoginInfo loginInfo);
	}
	
	public interface PromotionListener{
		public abstract void onPromotionReceived(ArrayList<PromotionItem> items);
		public abstract void onPromotionReceivedFailure();
	}
	
	public interface GetStoreItemListener{
		public abstract void onStoreItemReceived(MeepStoreItem feedback);
	}
	
	public interface RequestCoinsListener{
		public abstract void onRequestCoinsReceived(StoreFeedback feedback);
	}
	
	public interface GetPurchasedItemListener{
		public abstract void onGetPurchasedItemListener(MyAppItems myAppItems);
	}
	
	public interface RecoveryContentListener{
		public abstract void onContentRecovery(ContentRecoveryFeedback feedback);
	}
	public interface OtaUpdateListener{
		public abstract void onUpdateReceived(OtaUpdateFeedback otaUpdateFeedback);
	}
	
	
	
	public void setSearchItemListener(SearchItemListener listener) {
		mSearchItemListener = listener;
	}
	public void setPurchaseItemListener(PurchaseItemListener listener){
		mPurchaseItemListener = listener;
	}
	public void setRedeemListener(RedeemListener listener){
		mRedeemListener = listener;
	}
	public void setLoginListener(LoginListener listener){
		mLoginListener = listener;
	}
	public void setRequestCoinsListener(RequestCoinsListener listener){
		mRequestCoinsListener = listener;
	}
	
	public void setGetStoreItemListener(GetStoreItemListener listener){
		mGetStoreItemListener = listener;
	}
	
	public void setGetPurchasedItemListener(GetPurchasedItemListener listener){
		mGetPurchasedListener = listener;
	}
	
	public GetPurchasedItemListener getPurchasedItemListener(){
		return mGetPurchasedListener;
	}
	public void setRecoveryContentListener(RecoveryContentListener listener){
		mRecoveryContentListener = listener;
	}
	public OtaUpdateListener getOtaUpdateListener() {
		return mOtaUpdateListener;
	}
	public void setOtaUpdateListener(OtaUpdateListener mOtaUpdateListener) {
		this.mOtaUpdateListener = mOtaUpdateListener;
	}
	
	
	public RestRequest(Context context){
		mContext = context;
	}
	
	///******get store item *************
	
	public void searchItem(String type, int page, String token, String category, String paidType, int itemPerPage){
		
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		if(category==null)category= CAT_ALL;
		if(paidType== null) paidType = PRICE_ALL;
		String url = URL_APP + type + "/" + category + "/" + paidType + "/" + page + "/" + itemPerPage;
		
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		

		final long time = System.currentTimeMillis();
		MeepStoreLog.logcatMessage("restrequest", "start get item request...");
		mHttpClient.get(url, new JsonHttpResponseHandler()
		{
			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "get_store_item FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(999,"timeout", null, 0);
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
		        super.onFailure(arg0, arg1);
				if (arg1 == null) {
					// show time out message
				} else {
					MeepStoreLog.logcatMessage("StoreRecevied", arg1.toString());
					if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(999, arg1.toString(), null, 0);
				}
			}
			

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				MeepStoreLog.logcatMessage("restrequest", "get item time:" + (System.currentTimeMillis()  - time)/1000f + "s");
				MeepStoreLog.logcatMessage("StoreRecevied", "search item - "+ json.toString());
				int code;
				try {
					code = json.getInt("code");
					if(code == 200){
						ArrayList<MeepStoreItem> itemList = decodeStoreItems(json);
						int total = json.getInt("total");
						if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(200, "ok", itemList, total);
					}
				} catch (JSONException e) {
					Log.w(TAG, "searchItemError:" + e.toString());
					e.printStackTrace();
				}
				
			}
		});
	}
	public void otaUpdate(String otaId,String token){
		
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		String url = URL_OTA_UPDATE;
		
		MeepStoreLog.logcatMessage("restrequest", "start ota_update request...");
		mHttpClient.get(url+"/"+otaId, new JsonHttpResponseHandler()
		{
			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "ota update FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				//TODO:tiemout
				MeepStoreLog.logcatMessage(TAG,content);
				OtaUpdateFeedback feedback = new OtaUpdateFeedback(999, "time out");
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onUpdateReceived(feedback);
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				super.onFailure(arg0, arg1);
				//TODO:fail get
				MeepStoreLog.logcatMessage(TAG,arg1.toString());
				Gson gson = new Gson();
				OtaUpdateFeedback feedback = gson.fromJson(arg1.toString(),OtaUpdateFeedback.class);
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onUpdateReceived(feedback);
			}
			
			
			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				//TODO:start ota
				MeepStoreLog.logcatMessage(TAG,json.toString());
				Gson gson = new Gson();
				OtaUpdateFeedback feedback = gson.fromJson(json.toString(),OtaUpdateFeedback.class);
				if(mOtaUpdateListener!=null)
					mOtaUpdateListener.onUpdateReceived(feedback);
			}
		});
	}
	
	
	public void searchItem(String keyword, int page, int itemPerPage, String userToken, String paidType ){
		
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT);
		if(paidType == null)paidType=PRICE_ALL;
		String url = null;
		
		try {
			url = URL_SEARCH_ITEM + URLEncoder.encode(keyword,"utf8") + "/"  + paidType + "/"+ page + "/" + itemPerPage;
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		if (url != null) {
			mHttpClient.addHeader("Content-Type", "application/json");
			mHttpClient.addHeader("Authorization", "OST "+ userToken);
			//mHttpClient.addHeader("Accept-Language", getLanguage());
			mHttpClient.get(url, new JsonHttpResponseHandler(){
				
				@Override
				public void onFailure(Throwable error, String content) {
					MeepStoreLog.logcatMessage("onFailure", "search_item FAILED FAILED FAILED FAILED FAILED FAILED");
					super.onFailure(error, content);
					if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(999,"timout", null, 0);
				}
				
				@Override
				public void onFailure(Throwable arg0, JSONObject arg1) {
					MeepStoreLog.logcatMessage("StoreRecevied", arg1.toString());
					super.onFailure(arg0, arg1);
					if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(999, arg1.toString(), null, 0);
				}
	
				@Override
				public void onSuccess(JSONObject json) {
					MeepStoreLog.logcatMessage("StoreRecevied", "search item - "+ json.toString());
					super.onSuccess(json);
					int code;
					try {
						code = json.getInt("code");
						if(code == 200){
							ArrayList<MeepStoreItem> itemList = decodeStoreItems(json);
							int total = json.getInt("total");
							if(mSearchItemListener!= null) mSearchItemListener.onSearchItemReceived(200, "ok", itemList, total);
						}
					} catch (JSONException e) {
						Log.w(TAG, "searchItemError:" + e.toString());
						e.printStackTrace();
					}
					
				}
				
			});
		}
	}
	
	
	
	
	private ArrayList<MeepStoreItem> decodeStoreItems(JSONObject json) {
		ArrayList<MeepStoreItem> storeItemList = new ArrayList<MeepStoreItem>();
		try {
			JSONArray result = json.getJSONArray("results");
			for (int i = 0; i < result.length(); i++) {
				// get item info
				String iconUrl = result.getJSONObject(i).getString("icon");
				String category = result.getJSONObject(i).getString("category");
				Log.w("category", category);
				String id = result.getJSONObject(i).getString("_id");
				String purchase_status = result.getJSONObject(i).getString("purchase_status");
				int coins = result.getJSONObject(i).getInt("coins");
				String recommends = result.getJSONObject(i).getString("recommends");
				JSONArray screenshots = result.getJSONObject(i).getJSONArray("screenshots");
				ArrayList<String> screenShotUrls = new ArrayList<String>();
				for (int j = 0; j < screenshots.length(); j++) {
					screenShotUrls.add(screenshots.getString(j));
				}
				String developer = result.getJSONObject(i).getString("developer");
				String description = result.getJSONObject(i).getString("description");
				String name = result.getJSONObject(i).getString("name");
				String badge = result.getJSONObject(i).getString("badge");
				String type = result.getJSONObject(i).getString("type");
				double size = result.getJSONObject(i).getDouble("size");
			
				// package item info
				MeepStoreItem item = new MeepStoreItem();
				item.setItemId(id);
				item.setCategory(category);
				item.setBadge(badge);
				item.setDeveloper(developer);
				MeepStoreLog.logcatMessage("test","purchase_status:"+purchase_status);
//				if (purchase_status.equals(MeepStoreItem.ACTION_NORMAL)) {
//					if (coins == -2) {
//						item.setItemAction(MeepStoreItem.ACTION_COMING_SOON);
//					} else if (coins == -1) {
//						item.setItemAction(MeepStoreItem.ACTION_GET_IT);
//					} else if (coins == 0) {
//						item.setItemAction(MeepStoreItem.ACTION_FREE);
//					} else {
//						item.setItemAction(MeepStoreItem.ACTION_NORMAL);
//					}
//				} else {
//					// PENDING, BLOCKED, PURCHASED
//					item.setItemAction(purchase_status); 
//				}
				item.setItemType(type);
				
				item.setLocalPath("/data/home/" + type + "/data/");
				item.setName(name);
				item.setPrice(coins);
				item.setRecommends(recommends);
				item.setScreenShotUrls(screenShotUrls.toArray(new String[0]));
				item.setSize(size);
				item.setIconUrl(iconUrl);
				item.setDescription(description);
				String pName = null;
				if(type.equals("app") || type.equals("game")){
				    if (result.getJSONObject(i).has("package_name")) {
    					pName = result.getJSONObject(i).getString("package_name");
    					item.setPackageName(pName);
    					
				    } else {
				        item.setPackageName("");
				    }
				}
				item.setItemAction(getAction(coins, purchase_status, type, id, pName,name));

				storeItemList.add(item);
			}
			MeepStoreLog.logcatMessage(TAG, "decode json:" + storeItemList.size() + " records");
			return storeItemList;
		} catch (JSONException ex) {
			Log.e(TAG, "decode json error: " + ex.toString());
			return null;
		}
	}
	
	private String getAction(int coins, String purchase_status, String type, String id, String packageName,String name){
		MeepStoreLog.logcatMessage("getaction", coins + "   " +   purchase_status + " " + type + "   " + id  +  "   " + packageName);
		MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
		
		if (purchase_status.equals(MeepStoreItem.ACTION_NORMAL)) {
			if (coins == -2) {
				return MeepStoreItem.ACTION_COMING_SOON;
			} else if (coins == -1) {
				return MeepStoreItem.ACTION_GET_IT;
			} else if (coins == 0) {
				//*****
				if(app.isItemDownloading(id)){
					return MeepStoreItem.ACTION_DOWNLOADING;
				}else if (app.isItemWaitingToDownload(id)){
					return MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD;
				} else {
					if (type.equals(MeepStoreItem.TYPE_APP) || type.equals(MeepStoreItem.TYPE_GAME)) {
						if (app.isPackageInstalled(mContext, packageName)) {
							return MeepStoreItem.ACTION_INSTALLED;
						}
					}
					return MeepStoreItem.ACTION_FREE;
				}
				//*****
				
			} else {
				return MeepStoreItem.ACTION_NORMAL;
			}
		}else if (purchase_status.equals(MeepStoreItem.ACTION_PURCHASED)){
			if(app.isItemDownloading(id)){
				return MeepStoreItem.ACTION_DOWNLOADING;
			}else if (app.isItemWaitingToDownload(id)){
				return MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD;
			} else {
				if (type.equals(MeepStoreItem.TYPE_APP) || type.equals(MeepStoreItem.TYPE_GAME)) {
					if (app.isPackageInstalled(mContext, packageName)) {
						return MeepStoreItem.ACTION_INSTALLED;
					}
				} else {
//					if (app.isEbookInstalled(id)) {
//						return MeepStoreItem.ACTION_EBOOK_DOWNLOADED;
//					}
					if(EbookCtrl.isEbookInstalled(name)){
						return MeepStoreItem.ACTION_EBOOK_DOWNLOADED;
					}
				}
				return purchase_status;
			}
		}
		else {
			// PENDING, BLOCKED
			return purchase_status; 
		}
	}
	
	///********** purchase *********
	public void purchaseStoreItem(String id, String token){
		
		MeepStoreLog.logcatMessage("storedownload", "purchse item:" + id);
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		String url = URL_PURCHASE + id;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.get(url, new JsonHttpResponseHandler(){

			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "purchase_store_item FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				if (mPurchaseItemListener != null)
					mPurchaseItemListener.onPurchaseReceived(null);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				super.onFailure(arg0, json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "purchase: " + json.toString());
				Gson gson = new Gson();
				PurchaseFeedback purchaseFeedback = gson.fromJson(jsonStr, PurchaseFeedback.class);
				if (mPurchaseItemListener != null)
					mPurchaseItemListener.onPurchaseReceived(purchaseFeedback);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "purchase: " + json.toString());
				Gson gson = new Gson();
				PurchaseFeedback purchaseFeedback = gson.fromJson(jsonStr, PurchaseFeedback.class);

				if (mPurchaseItemListener != null)
					mPurchaseItemListener.onPurchaseReceived(purchaseFeedback);

			}
			
		});
	}
	
	///*************redeem**************
	public void confirmRedeemStoreItem(String couponCode, String token){
		String code = formatCouponCode(couponCode);
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT);
		String url = URL_CONFIRM_REDEEM + code;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.get(url, new JsonHttpResponseHandler(){

			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "confirm_redeem FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				RedeemConfirmFeedback redeem = new RedeemConfirmFeedback(999, "timeout", -1, null, 0,null);
				mRedeemListener.onConfirmRedeemReceived(redeem);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				super.onFailure(arg0, json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", " confirm redeem: " + json.toString());
				Gson gson = new Gson();
				RedeemConfirmFeedback redeem = gson.fromJson(jsonStr, RedeemConfirmFeedback.class);
				mRedeemListener.onConfirmRedeemReceived(redeem);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "confirm redeem: " + json.toString());
				Gson gson = new Gson();
				RedeemConfirmFeedback redeem = gson.fromJson(jsonStr, RedeemConfirmFeedback.class);
				mRedeemListener.onConfirmRedeemReceived(redeem);
			}
			
		});
	}
	
	public void getRedeemStoreItem(String couponCode, String token){
		String code = formatCouponCode(couponCode);
		if(code.length() != 19){
			RedeemFeedback redeem = new RedeemFeedback(888, "incorrect coupon code", 0, null, null);
			if (mRedeemListener!= null) mRedeemListener.onRedeemReceived(redeem);
		}

		else
		{
			mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
			String url = URL_REDEEM + code;
			mHttpClient.addHeader("Content-Type", "application/json");
			mHttpClient.addHeader("Authorization", "OST "+ token);
			mHttpClient.get(url, new JsonHttpResponseHandler(){
				
				public void onFailure(Throwable error, String content) {
					MeepStoreLog.logcatMessage("onFailure", "get_redeem_item FAILED FAILED FAILED FAILED FAILED FAILED");
					super.onFailure(error, content);
					RedeemFeedback redeem = new RedeemFeedback(999, "timeout", -1, null, null);
					mRedeemListener.onRedeemReceived(redeem);
				}
				
				@Override
				public void onFailure(Throwable arg0, JSONObject json) {
					super.onFailure(arg0, json);
					String jsonStr = json.toString();
					MeepStoreLog.logcatMessage("StoreRecevied", "redeem: " + json.toString());
					Gson gson = new Gson();
					RedeemFeedback redeem = gson.fromJson(jsonStr, RedeemFeedback.class);
					mRedeemListener.onRedeemReceived(redeem);
				}
				
				@Override
				public void onSuccess(JSONObject json) {
					super.onSuccess(json);
					String jsonStr = json.toString();
					Gson gson = new Gson();
					MeepStoreLog.logcatMessage("StoreRecevied", "redeem: " + json.toString());
					try {
						String type = json.getString("type");
						if(type.equals("coins")){
							RedeemCoinsFeedback redeemCoins = gson.fromJson(jsonStr, RedeemCoinsFeedback.class);
							RedeemFeedback redeem = new RedeemFeedback(redeemCoins.getCode(), redeemCoins.getStatus(), redeemCoins.getRemaining(), null, redeemCoins.getType());
							redeem.setCoins(redeemCoins.getResults());
							mRedeemListener.onRedeemReceived(redeem);
							//mRedeemListener.onRedeenCoinsReceived(redeemCoins);
						}else{
							RedeemFeedback redeem = gson.fromJson(jsonStr, RedeemFeedback.class);
							mRedeemListener.onRedeemReceived(redeem);
						}
						
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
				}		
			});
		}
		
	}
	
	private String formatCouponCode(String couponCode){
		String code = couponCode.toUpperCase();
		if(code.length() == 16){
			String str1 = code.substring(0, 4);
			String str2 = code.substring(4, 8);
			String str3 = code.substring(8,12);
			String str4 = code.substring(12, 16);
			return str1 + "-" + str2+ "-"+ str3 + "-" + str4;
		}else
		{
			return couponCode;
		}
		
		
	}
	
	private String getLanguage(){
		String lang = Locale.getDefault().getLanguage();
		MeepStoreLog.logcatMessage("language", lang);
		return lang;
	}
	
	///*************login**************
	public void login(String token){
		final String token2 = token;
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		String url = URL_LOGIN;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		
		try {
			final long time = System.currentTimeMillis();
			MeepStoreLog.logcatMessage("restrequest", "start login request...");
			mHttpClient.get(url, new JsonHttpResponseHandler() {

				@Override
				public void onFailure(Throwable error, String content) {
					MeepStoreLog.logcatMessage("onFailure", "FAILED FAILED FAILED FAILED FAILED FAILED FAILED"+content);
					super.onFailure(error, content);
					if(mLoginListener!= null) {
						mLoginListener.onLoginReceived(null);
					}
					if(mPromotionListener !=null){
						mPromotionListener.onPromotionReceivedFailure();
					}
				}
				
				@Override
				public void onFailure(Throwable arg0, JSONObject json) {
					super.onFailure(arg0, json);
					String jsonStr = json.toString();
					MeepStoreLog.logcatMessage("StoreRecevied", "login2: " + json.toString());
					Gson gson = new Gson();
					MeepStoreLoginInfo loginInfo = gson.fromJson(jsonStr, MeepStoreLoginInfo.class);
					if(mLoginListener!= null)  {
						mLoginListener.onLoginReceived(loginInfo);
					}
					if(mPromotionListener !=null){
						mPromotionListener.onPromotionReceivedFailure();
					}
				}
				
				@Override
				public void onSuccess(JSONObject json) {
					super.onSuccess(json);
					MeepStoreLog.logcatMessage("restrequest", "login time:" + (System.currentTimeMillis()  - time)/1000f + "s");
					
					String jsonStr = json.toString();
					MeepStoreLog.logcatMessage("StoreRecevied", "login: " + json.toString());
					Gson gson = new Gson();
					MeepStoreLoginInfo loginInfo = gson.fromJson(jsonStr, MeepStoreLoginInfo.class);
					if(mLoginListener!= null) {
						mLoginListener.onLoginReceived(loginInfo);
					}
					if(mPromotionListener !=null&&loginInfo!=null)
					{
						mPromotionListener.onPromotionReceived(loginInfo.getPromotion());
					}
					
				}
			});

		} catch (Exception ex) {
			MeepStoreLog.logcatMessage("timeout", ex.toString());
		}
		
		
	}
	
	//***********coins request************
	public void requestCoins(String token,long coins){
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		String url = URL_COIN_REQUEST;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.get(url, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "FAILED FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				StoreFeedback requestCoin = new StoreFeedback(999, "timeout",0);
				mRequestCoinsListener.onRequestCoinsReceived(requestCoin);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				super.onFailure(arg0, json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "request coins: " + json.toString());
				Gson gson = new Gson();
				StoreFeedback requestCoin = gson.fromJson(jsonStr, StoreFeedback.class);
				mRequestCoinsListener.onRequestCoinsReceived(requestCoin);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "request coins: " + json.toString());
				Gson gson = new Gson();
				StoreFeedback requestCoin = gson.fromJson(jsonStr, StoreFeedback.class);
				mRequestCoinsListener.onRequestCoinsReceived(requestCoin);
			}
		});
		
	}

	public void contentRecovery(String token){
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		String url = URL_PRELOAD_RECOVERY;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.post(url, new JsonHttpResponseHandler(){

			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "content_recovery FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				if(mRecoveryContentListener!= null)
					mRecoveryContentListener.onContentRecovery(new ContentRecoveryFeedback(999, "timeout"));
			}
			@Override
			public void onFailure(Throwable arg0, JSONObject json) {
				super.onFailure(arg0, json);
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "content recovery: " + json.toString());
				Gson gson = new Gson();
				ContentRecoveryFeedback feedback = gson.fromJson(jsonStr, ContentRecoveryFeedback.class);
				mRecoveryContentListener.onContentRecovery(feedback);
			}

			@Override
			public void onSuccess(JSONObject json) {
				super.onSuccess(json);
				String jsonStr = json.toString();
			
				MeepStoreLog.logcatMessage("StoreRecevied", "content recovery: " + json.toString());
				Gson gson = new Gson();
				ContentRecoveryFeedback feedback = gson.fromJson(jsonStr, ContentRecoveryFeedback.class);
				mRecoveryContentListener.onContentRecovery(feedback);
			}
		});
	}
		
	
	
	//****************get single item *************
	public void getStoreItem(String itemId, String token){
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT);
		String url = URL_GET_ITEM + itemId;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.get(url, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "get_single_item FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				mGetStoreItemListener.onStoreItemReceived(null);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				MeepStoreLog.logcatMessage("StoreRecevied", arg1.toString());
				super.onFailure(arg0, arg1);
				//if(mLoginListener!= null) mLoginListener.onLoginReceived(null);
			}

			@Override
			public void onSuccess(JSONObject json) {
				
				String jsonStr = json.toString();
				MeepStoreLog.logcatMessage("StoreRecevied", "single item: " + json.toString());
				
				Gson gson = new Gson();
				MeepStoreItemDetailsFeedback itemdetails = gson.fromJson(jsonStr, MeepStoreItemDetailsFeedback.class);
				
				MeepStoreItem item =new MeepStoreItem();
				item.setBadge(itemdetails.getResult().getBadge());
				item.setCategory((itemdetails.getResult().getCategory()));
				item.setDescription(itemdetails.getResult().getDescription());
				item.setDeveloper(itemdetails.getResult().getDeveloper());
				item.setIconUrl(itemdetails.getResult().getIcon());
				item.setItemId(itemdetails.getResult().getId());
				item.setItemType(itemdetails.getResult().getType());
				item.setName(itemdetails.getResult().getName());
				item.setPackageName(itemdetails.getResult().getPackage_name());
				item.setPrice(Integer.parseInt(itemdetails.getResult().getCoins()));
				item.setRecommends(itemdetails.getResult().getRecommends());
				item.setScreenShotUrls(itemdetails.getResult().getScreenshots().toArray(new String[]{""}));
				item.setSize(itemdetails.getResult().getSize());
				item.setRecommends(itemdetails.getResult().getRecommends());
				item.setItemAction(itemdetails.getResult().getPurchaseStatus());
				item.setItemAction(getAction(item.getPrice(), item.getItemAction(), item.getItemType(), item.getItemId(), item.getPackageName(),item.getName()));
				
				mGetStoreItemListener.onStoreItemReceived(item);
			}
		});
	}
	
	//**********my app ***********
	public void getPurchaseItem(String token){
		Log.i("restrequest", "get purchase items");
		mHttpClient = new AsyncHttpClient(); 
		mHttpClient.setTimeout(HTTP_DEFAULT_TIMEOUT); 
		String url = URL_GET_PURCHASED_ITEM;
		mHttpClient.addHeader("Content-Type", "application/json");
		mHttpClient.addHeader("Authorization", "OST "+ token);
		//mHttpClient.addHeader("Accept-Language", getLanguage());
		mHttpClient.get(url, new JsonHttpResponseHandler(){
			
			@Override
			public void onFailure(Throwable error, String content) {
				MeepStoreLog.logcatMessage("onFailure", "get_purchase_item FAILED FAILED FAILED FAILED FAILED FAILED");
				super.onFailure(error, content);
				mGetPurchasedListener.onGetPurchasedItemListener(null);
			}
			
			@Override
			public void onFailure(Throwable arg0, JSONObject arg1) {
				MeepStoreLog.logcatMessage("StoreRecevied", arg1.toString());
				super.onFailure(arg0, arg1);
				mGetPurchasedListener.onGetPurchasedItemListener(null);
			}

			@Override
			public void onSuccess(JSONObject json) {
				MeepStoreLog.logcatMessage("StoreRecevied", "get purchased item: " + json.toString());
				MyAppItems myAppItems = decodePurchasedItems(json);
				mGetPurchasedListener.onGetPurchasedItemListener(myAppItems);
			}
		});
	}

	private MyAppItems decodePurchasedItems(JSONObject json) {
		MyAppItems myAppItems = new MyAppItems();
		ArrayList<MeepStoreItem> storeItemList = new ArrayList<MeepStoreItem>();
		ArrayList<MeepStoreItem> preloadedItemList = new ArrayList<MeepStoreItem>();
		Gson mGson = new Gson();
		try {
			JSONArray result = json.getJSONArray("purchased");
			for (int i = 0; i < result.length(); i++) {
				// get item info
				MeepStoreItem item = mGson.fromJson(result.getJSONObject(i).toString(), MeepStoreItem.class);
				// package item info
				item.setLocalPath("/data/home/" + item.getItemType() + "/data/");
				item.setItemAction(getAction(item.getPrice(), MeepStoreItem.ACTION_PURCHASED, item.getItemType(), item.getItemId(), item.getPackageName(),item.getName()));
				item.setRecovery(false);
				storeItemList.add(item);
			}
			MeepStoreLog.logcatMessage(TAG, "decode json:" + storeItemList.size() + " records");
			myAppItems.setPurchasedItems(storeItemList);
			
			//preloaded item
			result = json.getJSONArray("preloaded");
			for (int i = 0; i < result.length(); i++) {
				// get item info
				MeepStoreItem item = mGson.fromJson(result.getJSONObject(i).toString(), MeepStoreItem.class);
				// package item info
				item.setLocalPath("/data/home/" + item.getItemType() + "/data/");
				item.setItemAction(getAction(item.getPrice(), MeepStoreItem.ACTION_PURCHASED, item.getItemType(), item.getItemId(), item.getPackageName(),item.getName()));
				item.setRecovery(true);
				preloadedItemList.add(item);
			}
			MeepStoreLog.logcatMessage(TAG, "decode json:" + storeItemList.size() + " records");
			myAppItems.setPreloadedItems(preloadedItemList);
			
			
			return myAppItems;
		} catch (JSONException ex) {
			Log.e(TAG, "decode json error: " + ex.toString());
			return null;
		}
	}
	public PromotionListener getmPromotionListener() {
		return mPromotionListener;
	}
	public void setmPromotionListener(PromotionListener mPromotionListener) {
		this.mPromotionListener = mPromotionListener;
	}
	
}
