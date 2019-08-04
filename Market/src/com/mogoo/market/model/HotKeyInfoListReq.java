package com.mogoo.market.model;

import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.network.http.NetworkTaskParameter;
import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import com.mogoo.parser.Result;

public abstract class HotKeyInfoListReq extends NetWorkTaskImpl {

    private int pageSize = 36;
    public HotKeyInfoListReq(Context ctx) {
        super(ctx);
    }
    
    @Override
    public Map<String, String> getRequestParams(Map<String, String> paramMap) {
        paramMap = new HashMap<String, String>();
        paramMap.put("pageSize", pageSize+"");
        return super.getRequestParams(paramMap);
    }
    
    @Override
    public boolean isBackgroundTask() {
        
        return true;
    }

    @Override
    public String getActionUrl() {
        
        return HttpUrls.URL_HOT_KEY_LIST;
    }

    @Override
    public int getRequestType() {
        
        return NetworkTaskParameter.REQUEST_TYPE_GET;
    }

    @Override
    public Object getResultCallback() {
        
        return new HotKeyInfo.HotKeyInfoListCallback();
    }
    
    /**
     * 获取热关键词列表
     * @param context：上下文
     * @param callBack： 回调函数
     */
    public static void onGetHotKey(Context context, final onRequestCallBack callBack) {
        new HotKeyInfoListReq(context) {

            @Override
            public void onSuccess(Result result) {
                if(callBack != null)
                    callBack.onSuccess(result);
            }
            
            @Override
            public void onFail(Result result) {
                if(callBack != null)
                    callBack.onFail(result);
            }

//			@Override
//			public void onLoadingComplete(InputStream responseData) {
//				// TODO Auto-generated method stub
//				
//			}

//			@Override
//			public void onLoadingStarted() {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onLoadingFailed() {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onLoadingCancelled() {
//				// TODO Auto-generated method stub
//				
//			}
        }.onRequestServer();
        Log.d("$$$$$","tamage");
    }

}
