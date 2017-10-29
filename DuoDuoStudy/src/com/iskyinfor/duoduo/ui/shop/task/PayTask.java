package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.usercenter.service.IManagerUserInfor0300030001;
import com.iskinfor.servicedata.usercenter.serviceimpl.ManagerUserInfor0300030001Impl;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.BookShelfActivity;

public class PayTask extends StoreBaseAsyncTask<Object, Integer, Void> {
	
	private IManagerUserInfor0300030001 userInfo = null;
	private boolean isSuccess;
	private IOperaterProduct0200030001 operater;
	private ArrayList<Product> checkItems;
	
	public PayTask(Activity mContext) {
		super(mContext);
	}

	public PayTask(Activity mContext, boolean finishContext) {
		super(mContext, finishContext);
	}

	@Override
	protected Void doInBackground(Object... params) {
		checkItems = (ArrayList<Product>) params[0];
		///double money = params[0];
		try {
			if (deleteProduct()) {
				Toast.makeText(mContext, "删除成功!!",Toast.LENGTH_SHORT).show();
			} else {
				Toast.makeText(mContext, "删除失败!!",Toast.LENGTH_SHORT).show();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		//super.onPreExecute();
		userInfo = new ManagerUserInfor0300030001Impl();
		operater = new OperaterProduct020003001Impl();
	}

	@Override
	protected void onPostExecute(Void result) {
		//super.onPostExecute(result);
		
		Intent intent = new Intent();
		intent.setClass(mContext, BookShelfActivity.class);
		startActivity(intent);
	}
	
	
	String [] getCheckItemId () {
		String [] s = new String [checkItems.size()];
		for (int i = 0; i < checkItems.size() ; i++) {
			s[i] = checkItems.get(i).getProId();
		}
		return s;
	}
	
	/**
	 * s删除选择item，
	 * 
	 */
	boolean deleteProduct() {
	
		boolean flag = false;
		try {
			flag = operater.deleteGoodsById(UiHelp.getUserShareID(mContext), getCheckItemId());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return flag;
	}
	
}
