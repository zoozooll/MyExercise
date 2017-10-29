package com.iskyinfor.duoduo.ui.book;

import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;
import com.iskinfor.servicedata.study.serviceimpl.ManagerStudyOperater010003000Impl;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;

public class GiftSendTask extends AsyncTask<Void, Integer, Object> {
    
	private IManagerStudyOperater0100030001 managerstudy;
	private Activity mContext;
	private static final int TIME_OUT_EXCEPTION = 1;
	private int exceptionCode;
	private ProgressDialog myLoadingDialog;
	private String mactivityName;
	private String reinfo;
	/**调用函数参数*/
	private String userId;
	private String proId;
	private String operateType;
	private String reason;
	private String[] object;
	private String ispublic;
	
	public GiftSendTask(Activity context,ProgressDialog loadingDialog,String activityName,String userId,String proId,String operateType,String reason,String[] object,String ispublic)
	{
		super();
		mContext=context;	
		myLoadingDialog=loadingDialog;
		mactivityName= activityName;
		/**接受函数参数*/
		this.userId=userId;
		this.proId=proId;
		this.operateType=operateType;
		this.reason=reason;
		this.object=object;
		this.ispublic=ispublic;
	}
	
	@Override
	protected Object doInBackground(Void... params) {
		try {
			if(mactivityName.equals("gift"))
			{
		     reinfo= managerstudy.giveBookToOther(userId, proId,operateType, reason, object, ispublic);
			}
			else if(mactivityName.equals("recommend"))
			{
		     reinfo = managerstudy.recommProduct(userId, proId,operateType, reason, object, ispublic);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reinfo;
	}

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		super.onPreExecute();
		managerstudy=new ManagerStudyOperater010003000Impl();
	}

	@Override
	protected void onPostExecute(Object result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
		
		if (myLoadingDialog != null && myLoadingDialog.isShowing())
		{
			myLoadingDialog.dismiss();
		}
		
		if(reinfo!=null)
		{
			if(mactivityName.equals("gift"))
			{
			  ((BookShelfGiftEditActivity)mContext).setGiftSendInfo(reinfo);
			}
			else if(mactivityName.equals("recommend"))
			{
				((BookShelfRecommendEditActivity)mContext).setRecommendSendInfo(reinfo);
			}
		}
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		// TODO Auto-generated method stub
		super.onProgressUpdate(values);
	}

}
