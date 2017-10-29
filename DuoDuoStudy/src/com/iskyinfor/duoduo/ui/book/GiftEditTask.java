package com.iskyinfor.duoduo.ui.book;

import java.util.Map;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

public class GiftEditTask extends AsyncTask<Void, Integer, Object> {
	private IQuerryUserInfor0300020001 queryuserinfo;
	private Map<String, Object> CLASSNAMTESmap;
	private Activity mContext;
	
	private static final int TIME_OUT_EXCEPTION = 1;
	private int exceptionCode;
	private ProgressDialog myLoadingDialog;
	private String mactivityName;
	
	public GiftEditTask(Activity context,ProgressDialog loadingDialog,String activityName)
	{
		super();
		mContext=context;	
		myLoadingDialog=loadingDialog;
		mactivityName= activityName;
	}
	
	@Override
	protected Object doInBackground(Void... params) {
		try {
			CLASSNAMTESmap=queryuserinfo.groupQuery("07",UiHelp.getUserShareID(mContext));
		} catch (TimeoutException e) {
			// TODO Auto-generated catch block
			exceptionCode = TIME_OUT_EXCEPTION;
			publishProgress(exceptionCode);
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CLASSNAMTESmap;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		queryuserinfo = new QuerryUserInfor0300020001Impl();

	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if (myLoadingDialog != null && myLoadingDialog.isShowing())
		{
			myLoadingDialog.dismiss();
		}
		
		if(CLASSNAMTESmap!=null)
		{
			if(mactivityName.equals("BookShelfGiftEditActivity"))
			{
			  ((BookShelfGiftEditActivity)mContext).setGiftFriendMap(CLASSNAMTESmap);
			}
			else if(mactivityName.equals("BookShelfRecommendEditActivity"))
			{
				((BookShelfRecommendEditActivity)mContext).setRecommendFriendMap(CLASSNAMTESmap);
			}
		}
		
	}

	@Override
	protected void onCancelled() {
		// TODO Auto-generated method stub
		super.onCancelled();
	}

}
