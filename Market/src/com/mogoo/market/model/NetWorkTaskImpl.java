package com.mogoo.market.model;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.mogoo.market.network.http.ErrorCode;
import com.mogoo.market.network.http.NetWorkTask;
import com.mogoo.market.network.http.NetworkTaskListener;
import com.mogoo.market.network.http.NetworkTaskParameter;
import com.mogoo.market.utils.LogUtils;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.parser.Result;
import com.mogoo.parser.UnderlineResultCallback;
import com.mogoo.parser.UnderlineResultParser;
import com.mogoo.parser.XmlResultCallback;
import com.mogoo.parser.XmlResultParser;

public abstract class NetWorkTaskImpl extends BaseNetWork implements onRequestCallBack, NetworkTaskListener {
    
    public NetWorkTaskImpl(Context ctx){
        super(ctx);
    }
    
    /**
     * 发送请求
     */
    public void onRequestServer() {
        NetworkTaskParameter networkParams = new NetworkTaskParameter(); 
        networkParams.setBackgroundTask(isBackgroundTask()) ;
        networkParams.setNetworkParams(getActionUrl(), getRequestType(), getRequestParams(null)) ;
        NetWorkTask netWorkTask = new NetWorkTask(mContext, networkParams);
        netWorkTask.setListener(this);
        netWorkTask.execute();
    }
    
    /**
     * 响应处理
     */
    @Override
    public void onLoadingComplete(InputStream responseData) 
    {
    	if(LogUtils.DEBUG){
            String dataxml = ToolsUtils.streamToString(responseData);
            Log.e("test", "responseData: " +dataxml);
            responseData = new ByteArrayInputStream(dataxml.getBytes());
        }
        
        if(responseData == null){
            onFail(new Result());
            return ;
        }
        
        Result result = null;
        if(getResultCallback() instanceof XmlResultCallback){
            result = XmlResultParser.parser(responseData, (XmlResultCallback)getResultCallback());
        }else {
            result = UnderlineResultParser.parser(responseData, (UnderlineResultCallback)getResultCallback());
        }
        
        
        if(result == null){
            onFail(new Result());
            return ;
        }
        
        if(ErrorCode.isSuccessCode(result.getErrorCode())){
            if(getResultCallback() instanceof UnderlineResultCallback){
                onSuccess(result);//下划线的成功返回
                return;
            }
            if(result.getData()!=null){
                onSuccess(result);
            }else {
                onFail(result);
            }
        }else {
            onFail(result);
        }
    }
    
    @Override
    public void onLoadingFailed() 
    {
    	// TODO Auto-generated method stub
    	onFail(null);
    }
    
    @Override
	public void onLoadingStarted() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onLoadingCancelled() {
		// TODO Auto-generated method stub
		
	}
    
    /*@Override
    public void dowithResponse(InputStream responseData) {
        if(LogUtils.DEBUG){
            String dataxml = ToolsUtils.streamToString(responseData);
            Log.e("test", "responseData: " +dataxml);
            responseData = new ByteArrayInputStream(dataxml.getBytes());
        }
        
        if(responseData == null){
            onFail(new Result());
            return ;
        }
        Result result = null;
        if(getResultCallback() instanceof XmlResultCallback){
            result = XmlResultParser.parser(responseData, (XmlResultCallback)getResultCallback());
        }else {
            result = UnderlineResultParser.parser(responseData, (UnderlineResultCallback)getResultCallback());
        }
        
        
        if(result == null){
            onFail(new Result());
            return ;
        }
        
        if(ErrorCode.isSuccessCode(result.getErrorCode())){
            if(getResultCallback() instanceof UnderlineResultCallback){
                onSuccess(result);//下划线的成功返回
                return;
            }
            if(result.getData()!=null){
                onSuccess(result);
            }else {
                onFail(result);
            }
        }else {
            onFail(result);
        }
    }*/

}
