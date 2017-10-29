package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.BookFavoriteActivity;

public class BookFavoriteTask extends StoreBaseAsyncTask<Void, Integer, Map<String, Object>> {
	private IOperaterRecordQuerry0200020001 record;
	private Map<String, Object> resultData ;
	
	public BookFavoriteTask(Activity mContext) {
		super(mContext);
	}
	
	public BookFavoriteTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		try {
			resultData =  (Map<String, Object>) record.querryStoreInfo(UiHelp.getUserShareID(mContext) , "00");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		record = new OperaterRecordQuerry0200020001Impl();
	}

	@Override
	protected void onPostExecute(Map<String, Object> result) {
		super.onPostExecute(result);
		/*Intent intent = new Intent(mContext, BookFavoriteActivity.class);
		ArrayList<Product> productes = (ArrayList<Product>)result.get(DataConstant.LIST);
		intent.putExtra(StaticData.STORE_KEY_PRODUCTES, productes);
		startActivity(intent);*/
		ArrayList<Product> productes = (ArrayList<Product>)result.get(DataConstant.LIST);
		BookFavoriteActivity activity = (BookFavoriteActivity) mContext;
		activity.getData(productes);
	}

}
