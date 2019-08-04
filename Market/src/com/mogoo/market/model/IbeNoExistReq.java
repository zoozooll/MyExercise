package com.mogoo.market.model;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import com.mogoo.parser.Result;
import com.mogoo.parser.UnderlineResultCallback;

/**
 * 设备不存在IBE通知请求.
 * @author lxr-motone
 */
public abstract class IbeNoExistReq extends NetWorkTaskImpl 
{
    public IbeNoExistReq(Context ctx) {
        super(ctx);
    }

    @Override
    public boolean isBackgroundTask() {
        
        return true;
    }

    @Override
    public String getActionUrl() {
        
        return HttpUrls.URL_IBE_NO_EXIST;
    }

    @Override
    public int getRequestType() {
        
        return NetworkTaskParameter.REQUEST_TYPE_GET;
    }

    @Override
    public Object getResultCallback() {
        
        return new IbeNoExistCallBack();
    }
    
    public static void onInformIbeNoExist(Context context, final onRequestCallBack callBack) {
        new IbeNoExistReq(context) {
            
            @Override
            public Map<String, String> getRequestParams(Map<String, String> paramMap) {
                paramMap = new HashMap<String, String>();
                paramMap.put("package", "com.mogoo.ibe.noexist_2");
                return super.getRequestParams(paramMap);
            }
            
            @Override
            public void onSuccess(Result result) {
                if(callBack != null)
                    callBack.onSuccess(result) ;
            }
            
            @Override
            public void onFail(Result result) {
                if(callBack != null)
                    callBack.onFail(result);
            }
        }.onRequestServer();
        
    }

	private static class IbeNoExistCallBack extends UnderlineResultCallback {

	}
}
