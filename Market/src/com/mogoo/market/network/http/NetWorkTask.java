package com.mogoo.market.network.http;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.SocketTimeoutException;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mogoo.market.R;
import com.mogoo.market.network.NetworkManager;
import com.mogoo.market.utils.LogUtils;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.network.http.HttpUtils;

/**
 * 网络任务类
 * @author xjx-motone
 *
 */
public class NetWorkTask extends AsyncTask<Void, Integer, InputStream>{

	private static final int ERROR_NETWORK_ACTIVE = 0;
	private static final int ERROR_SERVICE_RESPONSE = 1;
	private static final int NETWORK_CONNECTION_TIMEOUT=2;
	
	private NetworkTaskParameter mNetworkParams = null;	
	private ProgressDialog dialog = null;
	private Context mContext;
	
	private NetworkTaskListener listener ;
	
	private boolean xmlTest = false;
	
	public NetWorkTask(Context context, NetworkTaskParameter networkParams){
		this.mContext = context;
		this.mNetworkParams = networkParams;
	}
	
	@Override
	protected InputStream doInBackground(Void... params)
	{
		if(xmlTest)
		{
			if(mNetworkParams.getUrl().contains("introduction"))
			{
				return mContext.getClassLoader().getResourceAsStream("app_detail_test.xml");
			}
			else if(mNetworkParams.getUrl().contains("gettopic"))
			{
				return mContext.getClassLoader().getResourceAsStream("topic.xml");
			}
		}
		System.out.println("lcq:"+((mNetworkParams.getUrl()!=null)?mNetworkParams.getUrl():" null"));
		LogUtils.debug(NetWorkTask.class, "url:" + ((mNetworkParams.getUrl()!=null)?mNetworkParams.getUrl():" null"));
		LogUtils.debug(NetWorkTask.class," params:" + ((null != mNetworkParams.getParamsMap())?mNetworkParams.getParamsMap().toString():" null"));
		LogUtils.debug(NetWorkTask.class," request_type:" + ((mNetworkParams.getRequestType()==0)?"post":"get"));
		
		InputStream result = null;
		try {
			NetworkManager networkManager = NetworkManager.getInstance();
			if(!ToolsUtils.isAvailableNetwork(mContext)){//检测网络是否可用
				publishProgress(ERROR_NETWORK_ACTIVE);
				LogUtils.debug(NetWorkTask.class,"network is unable");
				return null;
			}

			if(null == mNetworkParams){
				LogUtils.debug(NetWorkTask.class,"NetworkParams is null");
				return null;
			}
			
			if(null == mNetworkParams.getUrl()){
				LogUtils.debug(NetWorkTask.class,"url is null");
				return null;
			}
			
			switch(mNetworkParams.getRequestType()){
				case NetworkTaskParameter.REQUEST_TYPE_GET:{
					result =  HttpUtils.get(mNetworkParams.getUrl(), mNetworkParams.isGzip());
					Log.d("dlr URL", "dlr ::::URL:::"+mNetworkParams.getUrl());
					break;
				}
				case NetworkTaskParameter.REQUEST_TYPE_POST:{					
					result =  HttpUtils.post(mNetworkParams.getUrl(), mNetworkParams.getParamPair(), mNetworkParams.isGzip());
					Log.d("dlr URL", "dlr ::::URL:::"+mNetworkParams.getUrl());
					break;
				}
				default:{
					LogUtils.debug(NetWorkTask.class,"sorry your request type is inCorrect");
					break;
				}
			}
			
		} 
		catch (SocketTimeoutException e1) {
			LogUtils.error(NetWorkTask.class,"OH sorry you have a SocketTimeoutException",e1);
			if(result==null){
			publishProgress(NETWORK_CONNECTION_TIMEOUT);
			}
			e1.printStackTrace();
		}
		
		catch (Exception e) {
			LogUtils.error(NetWorkTask.class,"OH sorry you have a networkException",e);
			if(result==null){
			//publishProgress(ERROR_SERVICE_RESPONSE);
                        publishProgress(NETWORK_CONNECTION_TIMEOUT);
			}
			e.printStackTrace();
		}
        if(result != null){
            String dataxml = ToolsUtils.streamToString(result);
//            Log.e("test", "result: " +dataxml);
            result = new ByteArrayInputStream(dataxml.getBytes());
        }
		return result;
	}

	@Override
	protected void onCancelled() {
		dismissDialog();
		if(listener != null) {
			listener.onLoadingCancelled();
		}
		super.onCancelled();
	}

	@Override
	protected void onPostExecute(InputStream result) {
		dismissDialog();

		if(listener!=null){
			if(result != null) {
				listener.onLoadingComplete(result);	
			}else {
				listener.onLoadingFailed();
			}
		}
		
		super.onPostExecute(result);
	}

	@Override
	protected void onPreExecute() {
		if(!mNetworkParams.isBackgroundTask()){
			
			//加载进度条
			String tip ;
			if(mNetworkParams.getProgressTip() == -1){
				tip = mContext.getResources().getString(R.string.in_progress);
			}
			else{
				tip = mContext.getResources().getString(mNetworkParams.getProgressTip());
			}
			
			dialog = createProgressDialog(mContext, tip);
			dialog.show();
		}
		if(listener != null) {
			listener.onLoadingStarted();
		}
		super.onPreExecute();
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
			switch(values[0]){
				case ERROR_NETWORK_ACTIVE:{
//					Toast.makeText(mContext, R.string.tip_network_inactive, Toast.LENGTH_SHORT).show();
					
					LayoutInflater inflater=LayoutInflater.from(mContext);
			        View toastview=inflater.inflate(R.layout.network_inactive_layout,null).
			        findViewById(R.id.network_inactive_tip);
			        Toast toast=new Toast(mContext);
			        toast.setDuration(Toast.LENGTH_LONG);
			        toast.setView(toastview);
			        toast.show();
					break;
				}
				case ERROR_SERVICE_RESPONSE:{
//					Toast.makeText(mContext, R.string.tip_server_error, Toast.LENGTH_SHORT).show();
					LayoutInflater inflater=LayoutInflater.from(mContext);
			        View toastview=inflater.inflate(R.layout.server_error_layout,null).
			        findViewById(R.id.server_error_tip);
			        Toast toast=new Toast(mContext);
			        toast.setDuration(Toast.LENGTH_LONG);
			        toast.setView(toastview);
			        toast.show();
					break;
				}
				case NETWORK_CONNECTION_TIMEOUT:{
//					Toast.makeText(mContext, R.string.network_connection_timeout, Toast.LENGTH_SHORT).show();
//					LayoutInflater inflater=LayoutInflater.from(mContext);
//			        View toastview=inflater.inflate(R.layout.network_connect_timeout_layout,null).
//			        findViewById(R.id.network_connection_timeout_tip);
//			        Toast toast=new Toast(mContext);
//			        toast.setDuration(Toast.LENGTH_LONG);
//			        toast.setView(toastview);
//			        toast.show();
					break;
				}
			}
			super.onProgressUpdate(values);

	}
	
	
	private void dismissDialog(){
		if(null != dialog){
			dialog.dismiss();
		}
	}
	
	/**
	 * 进度条
	 * @param context
	 * @param tip
	 * @return
	 */
	private static ProgressDialog createProgressDialog(Context context, String tip){
		ProgressDialog dialog = new ProgressDialog(context);
		dialog.setMessage(tip);
		return dialog;
	}

	public void setListener(NetworkTaskListener listener) {
		this.listener = listener;
	}

	public void setmNetworkParams(NetworkTaskParameter mNetworkParams) {
		this.mNetworkParams = mNetworkParams;
	}
}
