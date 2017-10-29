package com.iskyinfor.duoduo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskinfor.servicedata.usercenter.serviceimpl.QuerryUserInfor0300020001Impl;
import com.iskyinfor.duoduo.ui.IndexActivity;

public class LoginTask extends AsyncTask<Void, Integer, Object> {
	private static final int TIME_OUT_EXCEPTION = 1;
	private Context mContext = null;
	private String userName, password;
	private boolean flag;
	private int exceptionCode;
	private IQuerryUserInfor0300020001 userInfor;
	private ProgressDialog myLoadingDialog;

	public LoginTask() 
	{
		super();
	}

	public LoginTask(Context con, String userName, String password,ProgressDialog loadingDialog)
	{
		mContext = con;
		this.userName = userName;
		this.password = password;
		myLoadingDialog=loadingDialog;
	}

	@Override
	protected void onPreExecute()
	{
		super.onPreExecute();
		userInfor = new QuerryUserInfor0300020001Impl();
	}


	@Override
	protected Object doInBackground(Void... params)
	{
		try {
			flag = userInfor.Login(userName, password);
		} catch (TimeoutException te) {
			exceptionCode = TIME_OUT_EXCEPTION;
			publishProgress(exceptionCode);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	@Override
	protected void onCancelled()
	{
		super.onCancelled();
	}

	@Override
	protected void onProgressUpdate(Integer... values) 
	{
		
		if (myLoadingDialog != null && myLoadingDialog.isShowing())
		{
			myLoadingDialog.dismiss();
		}

		switch (exceptionCode) {
		case TIME_OUT_EXCEPTION:
			Toast.makeText(
					mContext,
					mContext.getResources().getString(
							R.string.time_out_exception), Toast.LENGTH_SHORT)
					.show();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onPostExecute(Object result) 
	{
		if (myLoadingDialog != null && myLoadingDialog.isShowing())
		{
			myLoadingDialog.dismiss();
		}
		
		if (flag == true) 
		{
			Intent intent = new Intent(mContext, IndexActivity.class);
			mContext.startActivity(intent);
			((Activity) mContext).finish();
		}
		else 
		{
			Toast.makeText(mContext,
					mContext.getResources().getString(R.string.userinfoerror),
					Toast.LENGTH_LONG).show();
		}
	}
}
