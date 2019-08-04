package com.mogoo.market.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import com.mogoo.parser.Result;

public class AppUpdatesInfoReq extends NetWorkTaskImpl 
{

	public AppUpdatesInfoReq(Context ctx) {
		super(ctx);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onFail(Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onSuccess(Result result) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isBackgroundTask() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String getActionUrl() {
		// TODO Auto-generated method stub
		return HttpUrls.URL_APP_UPDATES;
	}

	@Override
	public int getRequestType() {
		// TODO Auto-generated method stub
		return NetworkTaskParameter.REQUEST_TYPE_GET;
	}

	@Override
	public Object getResultCallback() {
		// TODO Auto-generated method stub
		return new AppUpdatesInfo.AppUpdatesCallback();
	}
	
	//获取软件详情
    public static void onRequest(Context context, final HashMap<String, String> params, final onRequestCallBack callBack) 
    {
        new AppUpdatesInfoReq(context)
        {
            @Override
            public Map<String, String> getRequestParams(Map<String, String> paramMap) {
                paramMap = params;
                if(paramMap == null){
                    paramMap = new HashMap<String, String>();
                }
                return super.getRequestParams(paramMap);
            }

            @Override
            public void onFail(Result result) {
                if(callBack != null)
                    callBack.onFail(result);
            }

            @Override
            public void onSuccess(Result result) {
                if(callBack != null)
                    callBack.onSuccess(result);
            }
            
        }.onRequestServer();
    }

}
