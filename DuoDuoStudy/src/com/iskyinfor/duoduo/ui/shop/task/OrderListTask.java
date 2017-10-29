package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Order;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.OrderListActivity;

public class OrderListTask extends
		StoreBaseAsyncTask<Void, Integer, Map<String, Object>> {

	private IOperaterRecordQuerry0200020001 operaterRecord;
	private Map<String, Object> resultData;

	public OrderListTask(Activity mContext) {
		super(mContext);
	}
	

	public OrderListTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected Map<String, Object> doInBackground(Void... params) {
		try {
			resultData = (Map<String, Object>) operaterRecord.querryOrderInfor(UiHelp.getUserShareID(mContext), null, null, 1, String.valueOf(15), null);
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
		/*Intent intent = new Intent(mContext, OrderListActivity.class);

		ArrayList<Order> orderes = (ArrayList<Order>) result
				.get(DataConstant.ORDER_KEY);
		intent.putExtra(StaticData.STORE_KEY_ORDERES, orderes);
		intent.putExtra(StaticData.STORE_KEY_TOTAL, (Integer) result.get(StaticData.STORE_KEY_TOTAL));
		startActivity(intent);*/
		int total = 0;
		ArrayList<Order> orderes = null;
		try {
			orderes = (ArrayList<Order>) result
				.get(DataConstant.ORDER_KEY);
			OrderListActivity acitivity = (OrderListActivity) mContext;
			total = (Integer) result.get(DataConstant.TOTAL_NUM);
		} catch (Exception e) {
			// TODO: handle exception
		}
		 orderes = (ArrayList<Order>) result
			.get(DataConstant.ORDER_KEY);
		OrderListActivity acitivity = (OrderListActivity) mContext;
		Object o = result.get(DataConstant.TOTAL_NUM);
		acitivity.getData(orderes, (Integer) o);
	}

}
