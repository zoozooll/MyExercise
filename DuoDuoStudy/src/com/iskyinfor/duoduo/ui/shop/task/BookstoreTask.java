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
import com.iskinfor.servicedata.bookshopdataservice.IQurryProductInfor0200020002;
import com.iskinfor.servicedata.bookshopdataservice.IShowProduct0200010001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.QurryProductInfor0200020002Impl;
import com.iskinfor.servicedata.bookshopdataserviceimpl.ShowProduct0200010001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

public class BookstoreTask extends StoreBaseAsyncTask<Integer, Integer, Map<String, Object>> {
	
	private IShowProduct0200010001 bookShopData;
	private IQurryProductInfor0200020002 queryInfo;
	
	public BookstoreTask(Activity mContext) {
		super(mContext);
	}
	
	public BookstoreTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected void onPreExecute() {
		bookShopData = new ShowProduct0200010001Impl();
		queryInfo = new QurryProductInfor0200020002Impl();
		super.onPreExecute();
	}

	@Override
	protected void onPostExecute(Map<String, Object> result) {
		super.onPostExecute(result);
		int flag = (Integer) result.get("resultFlag");
		ArrayList<Product> productes = (ArrayList<Product>)result.get(DataConstant.LIST);
		BookstoreActivity activity = (BookstoreActivity) mContext;
		if (flag==1){
			activity.notifyDataSetChanged(productes);
		} else {
			int total = 0;
			try {
				
				total = Integer.valueOf((String)result.get(DataConstant.TOTAL_NUM));
			} catch (Exception e) {
			}
			activity.getData( total, productes);
		}
		
	}

	@Override
	protected Map<String, Object> doInBackground(Integer... params) {
		int type = params[0];
		//int len = params.length;
		int page = params[1];
		int flag = 0;
		try {
			flag = params[2];
		} catch (Exception e) {
		}
		
		Map<String, Object> resultData = null ;
		try {
			switch (type) {
			//查询所有
			case StaticData.ALL:
				resultData = bookShopData.getAllProduct(page);
				break;
				//查询书籍
			case StaticData.BOOKS:
				resultData = bookShopData.getAllBook(page);
				break;
				//查询练习
			case StaticData.COURSEWARE:
				resultData = bookShopData.getAllCourseware(page);
				break;
				//查询习题
			case StaticData.EXAM:
				resultData = bookShopData.getAllExam(page);
				break;
				//查询考卷
			case StaticData.EXAMPAPER:
				resultData = bookShopData.getAllExercise(page);
				break;
				//团购
			case StaticData.GROUPSHOP:
				resultData = bookShopData.getAllProduct(page);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		resultData.put("resultFlag", flag);
		return resultData;
	}

}
