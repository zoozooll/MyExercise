package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.ui.UiHelp;

public class PutShelfTask extends AsyncTask<ArrayList<Product>, Void, Void> {
	private IOperaterProduct0200030001 opreater;
	private Context mContext;
	boolean flag;
	public PutShelfTask(Context mContext) {
		super();
		this.mContext = mContext;
	}

	@Override
	protected Void doInBackground(ArrayList<Product>... params) {
		try {
			flag =  opreater.putBuyedProducetToShelf(UiHelp.getUserShareID(mContext), params[0]);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		opreater = new OperaterProduct020003001Impl();
		BuilderProgress.loadProgress("正在获取数据，请稍等...", mContext);
		
	}

	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		if(BuilderProgress.mDialog!=null&&BuilderProgress.mDialog.isShowing()){
			BuilderProgress.mDialog.dismiss();
		}
		if (flag) {
			Toast.makeText(mContext, "加入书架成功！！", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(mContext, "加入书架失败！！", Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);
		if(BuilderProgress.mDialog!=null&&BuilderProgress.mDialog.isShowing()){
			BuilderProgress.mDialog.dismiss();
		}
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}
	
}
