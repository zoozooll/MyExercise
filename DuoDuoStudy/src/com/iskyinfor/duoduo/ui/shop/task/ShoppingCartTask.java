package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.pojo.ShopCar;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.ShoppingCartActivity;

public class ShoppingCartTask extends
		StoreBaseAsyncTask<Void, Integer, Map<String, Object>> {

	private IOperaterRecordQuerry0200020001 record;
	private IOperaterProduct0200030001 opreater;
	private Map<String, Object> resultData;

	public ShoppingCartTask(Activity mContext) {
		super(mContext);
	}

	public ShoppingCartTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		try {
			resultData = (Map<String, Object>) record.querryShopingCat(UiHelp
					.getUserShareID(mContext));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		record = new OperaterRecordQuerry0200020001Impl();
		opreater = new OperaterProduct020003001Impl();
	}

	@Override
	protected void onPostExecute(Map<String, Object> result) {

		super.onPostExecute(result);
		/*Intent intent = new Intent(mContext, ShoppingCartActivity.class);
		ShopCar shopCar = (ShopCar) result.get(DataConstant.SHOPCAR_KEY);
		ArrayList<Product> productes = shopCar.getProductList();
		intent.putExtra(StaticData.STORE_KEY_PRODUCTES, productes);
		startActivity(intent);*/
		ShopCar shopCar = (ShopCar) result.get(DataConstant.SHOPCAR_KEY);
		ShoppingCartActivity activity = (ShoppingCartActivity) mContext;
		activity.getData(shopCar.getProductList());
	}

}
