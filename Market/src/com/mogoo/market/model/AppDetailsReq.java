package com.mogoo.market.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.mogoo.market.network.IBEManager;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import com.mogoo.parser.Result;

public abstract class AppDetailsReq extends NetWorkTaskImpl {

    public AppDetailsReq(Context ctx) {
        super(ctx);
        // TODO Auto-generated constructor stub
    }

    @Override
    public Map<String, String> getRequestParams(Map<String, String> paramMap) {
    	// mogoo lxr add 2012.08.14
    	// 广告接口特殊必须传appId,其他接口为appid, 否则获取不了数据.
		if (paramMap == null)
			paramMap = new HashMap<String, String>();
		paramMap.put("appId", IBEManager.getAppId() + "");
		return paramMap;
		// mogoo lxr add end 2012.08.14
        // return super.getRequestParams(paramMap);
    }

    @Override
    public boolean isBackgroundTask() {
        
        return true;
    }

    @Override
    public String getActionUrl() {
        
    	return HttpUrls.URL_APP_DETAILS;
    }

    @Override
    public int getRequestType() {
        
        return NetworkTaskParameter.REQUEST_TYPE_GET;
    }
    
    @Override
    public Object getResultCallback() {
        
        return new AppDetails.AppDetailsCallback();
    }
    

    //获取软件详情
    public static void onRequest(Context context, final HashMap<String, String> params, final onRequestCallBack callBack) {
        new AppDetailsReq(context){

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
    
    //获取广告点击，单个应用软件详情
    public static void onRequest(Context context, final String url, final String positionId, final String adId, final onRequestCallBack callBack) 
    {
        new AppDetailsReq(context)
        {
            @Override
            public String getActionUrl() {
                
                return url;
            }
            
            @Override
            public Object getResultCallback() 
            {
                return new AdAppDetails.AdAppDetailsCallback();
            }
            
            @Override
            public Map<String, String> getRequestParams(Map<String, String> paramMap) 
            {
            	paramMap = new HashMap<String, String>();
                paramMap.put("positionId", positionId);
                paramMap.put("adId", adId);
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
