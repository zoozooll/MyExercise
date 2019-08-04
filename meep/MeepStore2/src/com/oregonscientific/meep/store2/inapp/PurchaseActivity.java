package com.oregonscientific.meep.store2.inapp;

import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.store2.FinishActivityDialog;
import com.oregonscientific.meep.store2.R;
import com.oregonscientific.meep.store2.ctrl.RestRequestForInAppPurchase.ProcessPurchaseListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.inapp.object.ContentPurchase;
import com.oregonscientific.meep.store2.inapp.object.Order;
import com.oregonscientific.meep.store2.inapp.object.PurchasedItem;
import com.oregonscientific.meep.store2.inapp.object.Response;
import com.oregonscientific.meep.store2.inapp.object.ResponsePurchase;
import com.oregonscientific.meep.store2.inapp.object.SkuDetails;

public class PurchaseActivity extends Activity {

	private MeepStoreApplication mApp;
	private Gson mGson;
	private ContentPurchase content;
	Button btnOk;
	Button btnCancel;
	ProgressBar progressBar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_in_app_purchase);
		mGson = new Gson();
		btnOk = (Button) findViewById(R.id.ok);
		btnCancel = (Button) findViewById(R.id.cancel);
		progressBar = (ProgressBar) findViewById(R.id.progressBar);

	}

	public void initListeners() {
		mApp = (MeepStoreApplication) getApplication().getApplicationContext();
		// listener
		mApp.getRestRequestIAP().setmProcessPurchaseListener(new ProcessPurchaseListener() {

			@Override
			public void onPurchaseSuccess(ResponsePurchase purchaseResponse) {
				// generate return json data
				String json = generatePurchaseItem(purchaseResponse);
				// finish and return success
				returnSuccess(json);
			}

			@Override
			public void onPurchaseFailure(String errorMessage) {
				errorPopUp(R.string.please_try_later);

				// TODO:delete test return
				// String json = generatePurchaseItem(testData());
				// returnSuccess(json);
			}

			@Override
			public void onPurchaseFailure(Response response) {
				int code = response.getCode();
				int message = R.string.please_try_later;
				switch (code) {
				case 403:
					message = R.string.not_allowed_purchase_inapp_item;
				case 404:
					message = R.string.item_not_found;
				case 412:
					message = R.string.have_insufficient_coins;
				}
				errorPopUp(message);
			}
		});
		
		btnOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				btnOk.setEnabled(false);
				btnCancel.setEnabled(false);
				progressBar.setVisibility(View.VISIBLE);
				mApp.getRestRequestIAP().purchaseInAppItem(content, mApp.getAccountToken());
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				returnCancel();
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		initListeners();
		try {
			Bundle bundle = getIntent().getExtras();
			String skuJson = bundle.getString(Consts.KEY_PURCHASE_SKU);
			String authString = bundle.getString(Consts.KEY_PURCHASE_AUTH);
			String packageName = bundle.getString(Consts.KEY_PURCHASE_PACKAGE_NAME);
			MeepStoreLog.logcatMessage(MeepStoreLog.IAP_PURCHASE_ACTIVITY, skuJson);
			// Order
			SkuDetails sku = mGson.fromJson(skuJson, SkuDetails.class);
			Order order = new Order();
			order.setProductId(sku.getProductId());
			order.setQuantity(1);
			// get json of order object
			String order_json = mGson.toJson(order);

			// signature
			String signature = generateSignature(order_json, mApp.getAccountToken(), authString);

			// Content
			content = new ContentPurchase();
			content.setPackage_name(packageName);
			content.setOrder_json(order_json);
			content.setSignature("SHA1 " + signature);

			initInformation(sku);
		} catch (Exception e) {
			MeepStoreLog.logcatMessage(MeepStoreLog.IAP_PURCHASE_ACTIVITY, e.getLocalizedMessage());
			returnCancel();
		}
	}

	private void initInformation(SkuDetails sku) {
		// find view in layout
		TextView name = (TextView) findViewById(R.id.name);
		TextView coins = (TextView) findViewById(R.id.coins);
		TextView description = (TextView) findViewById(R.id.description);

		// put information into textview
		name.setText(sku.getName());
		coins.setText(sku.getCoins() + "");
		description.setText(sku.getDescription());
	}

	private String generatePurchaseItem(ResponsePurchase purchaseResponse) {
		PurchasedItem item2 = new PurchasedItem();
		item2.setOrderId(purchaseResponse.getTransaction_id());
		item2.setPackageName(getIntent().getStringExtra(Consts.KEY_PURCHASE_PACKAGE_NAME));
		item2.setProductId(purchaseResponse.getProductId());
		return mGson.toJson(item2);
	}

	private void returnSuccess(String json) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putInt(Consts.RESPONSE_CODE, Consts.RESULT_OK);
		bundle.putString(Consts.RESPONSE_INAPP_PURCHASE_DATA, json);
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_PURCHASE_ACTIVITY, json);
		finish();
	}

	public void returnCancel() {
		setResult(RESULT_CANCELED);
		finish();
	}

	private String generateSignature(String order_json, String token,
			String authString) {

		// generate original String
		String orignalString = order_json + token + authString;

		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_PURCHASE_ACTIVITY, "original: "
				+ orignalString);
		// encrypt string by SHA1
		String encryptedString = Utils.Encode("SHA-1", orignalString);

		MeepStoreLog.logcatMessage(MeepStoreLog.IAP_PURCHASE_ACTIVITY, "encoded: "
				+ encryptedString);
		return encryptedString;

	}

	public ResponsePurchase testData() {
		ResponsePurchase purchaseResponse = new ResponsePurchase();
		purchaseResponse.setCode(999);
		purchaseResponse.setStatus("test success");
		purchaseResponse.setProductId("11223344");
		purchaseResponse.setQuantity(2);
		purchaseResponse.setTransaction_id("x1.111");
		return purchaseResponse;
	}

	public void errorPopUp(int errorMessage) {
		new FinishActivityDialog(PurchaseActivity.this, errorMessage).show();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

}
