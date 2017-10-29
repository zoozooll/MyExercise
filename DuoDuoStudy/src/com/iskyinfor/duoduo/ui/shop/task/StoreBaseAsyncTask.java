package com.iskyinfor.duoduo.ui.shop.task;

import java.util.ArrayList;
import java.util.Map;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;

public abstract class StoreBaseAsyncTask<Params, Progress, Result> extends
		AsyncTask<Params, Progress, Result>{
	
	protected ProgressDialog mDialog;
	protected Activity mContext;
	protected boolean finishContext;
	
	public StoreBaseAsyncTask(Activity mContext){
		this.mContext = mContext;
	}
	
	
	public StoreBaseAsyncTask(Activity mContext, boolean finishContext) {
		super();
		this.mContext = mContext;
		this.finishContext = finishContext;
	}


	public void setFinishContext(boolean finishContext) {
		this.finishContext = finishContext;
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadProgress();
	}
	
	@Override
	protected void onPostExecute(Result result) {
		super.onPostExecute(result);
		if(result!=null){
			if(mDialog!=null&&mDialog.isShowing()){
				mDialog.cancel();
			}
		}
	}
	
	@Override
	protected void onCancelled() {
		super.onCancelled();
		if(mDialog!=null&&mDialog.isShowing()){
			mDialog.cancel();
		}
	}
	
	protected void loadProgress() {
		mDialog = new ProgressDialog(mContext);
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.setMessage(mContext.getResources().getString(R.string.storeWaitting));
		mDialog.setIndeterminate(false);
		mDialog.setCancelable(true);
		mDialog.show();
	}
	
	protected void startActivity(Intent intent){
		mContext.startActivity(intent);
		if (finishContext){
			mContext.finish();
		}
	}
}
