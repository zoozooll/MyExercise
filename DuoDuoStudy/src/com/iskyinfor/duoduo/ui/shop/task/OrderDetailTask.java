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
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.OrderDetailActivity;

public class OrderDetailTask extends StoreBaseAsyncTask<Integer, Integer, Map<String, Object>> {
	
	private IOperaterRecordQuerry0200020001 operaterRecord;
	private Map<String, Object> resultData ;
	
	public OrderDetailTask(Activity mContext) {
		super(mContext);
	}
	
	public OrderDetailTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected Map<String, Object> doInBackground(Integer... params) {
		try {
			resultData =  (Map<String, Object>) operaterRecord.querryOrderProduct(UiHelp.getUserShareID(mContext), String.valueOf(params[0]), null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		operaterRecord = new OperaterRecordQuerry0200020001Impl();
	}
	
	@Override
	protected void onPostExecute(Map<String, Object> result) {
		super.onPostExecute(result);
		Intent intent = new Intent(mContext, OrderDetailActivity.class);
		ArrayList<Product> productes = (ArrayList<Product>) result.get(DataConstant.LIST);
		intent.putExtra(StaticData.STORE_KEY_PRODUCTES, productes);
		startActivity(intent);
	}
	
	public static String getRealState(String ordeState, String tranState){
		if ("00".equals(ordeState)){	//订单无效
			return "无效";
		} else if ("01".equals(ordeState) ) {//订单有效
			if ("00".equals(tranState)){//未支付
				return "未支付";
			}else if ("01".equals(tranState)) {
				return "完成";
			} else {
				return "错误订单";
			}
		} else {
			return "无效";
		}
		
	}
		
}
