package com.mogoo.market.ui;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.mogoo.ibetask.sales.SalesStatistics;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.IbeNoExistReq;
import com.mogoo.market.model.onRequestCallBack;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.utils.LoadingUtils;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.parser.Result;

/*加载首页 lzg*/
public class LoadingFirstPage extends Activity {
	private static final boolean DEBUG = false;
	
	private static final String PREFERENCE_INFORM_IBE_NOEXIST = "ibe_noexist";
	
	private TextView mLoadingText;
	private SharedPreferences mPrefs;
	
	private void loaddingSoftManager(){
		new Thread(){
			@Override
			public void run(){
				try {
					Thread.sleep(2000);
					Intent intent=new Intent();
					intent.setClass(LoadingFirstPage.this,MarketGroupActivity.class);
					intent.putExtra("tabnum", MarketGroupActivity.TAB_MANAGER);
					startActivity(intent);
					finish();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				}
			}.start();
	}
		
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loading_first_page);
		mLoadingText=(TextView)findViewById(R.id.loading_text);
		mLoadingText.setVisibility(View.GONE);
		IntentFilter filter = new IntentFilter(); 
	    filter.addAction("com.android.loadingpagesuccess");
	    filter.addAction("com.android.loadingpagefail");
	    registerReceiver(myReceiver, filter); 
    	// 清空所有数据库中列表的缓存.
    	DaoFactory.clearDataBase(this);
		loadingData();
		mPrefs = PreferenceManager.getDefaultSharedPreferences(this);
		// add by fdl:不是IBE的环境下，自己上传销量信息
		if (!IBEManager.isIBEExist())
		{
			new Thread(){
				public void run()
				{
					SalesStatistics.getInstance(getApplicationContext()).uploadSalesStatistics();
				}
			}.start();
			Log.e("SALES", "This is a OS enviroment that has no ibe , so upload sales statistics in market's code");
			
			// mogoo lxr add 2012.08.14
			// 设备不存在IBE且未上传服务器进行通知.
			if(!mPrefs.getBoolean(PREFERENCE_INFORM_IBE_NOEXIST, false)) {
				if (DEBUG)
					Log.d("InformIbeNoExist",
							"never inform ibe no exist, do it.");
				informIbeNoExist();
			}else {
				if (DEBUG)
					Log.d("InformIbeNoExist", "already inform ibe no exist.");
			}
		}
	}

        @Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(myReceiver); 
	}
	
	/**
	 * 通知IBE不存在请求.
	 */
    private void informIbeNoExist() {
    	IbeNoExistReq.onInformIbeNoExist(this,  new onRequestCallBack() {

			@Override
			public void onFail(Result result) {
				if (DEBUG)
					Log.d("InformIbeNoExist", "informe ibe no exist failed.");
				Editor editor = mPrefs.edit();
				editor.putBoolean(PREFERENCE_INFORM_IBE_NOEXIST, false);
				editor.commit();
			}

			@Override
			public void onSuccess(Result result) {
				if (DEBUG)
					Log.d("InformIbeNoExist", "informe ibe no exist successed.");
				Editor editor = mPrefs.edit();
				editor.putBoolean(PREFERENCE_INFORM_IBE_NOEXIST, true);
				editor.commit();
			}
			
		});
    }
        
	/*加载数据*/
	private void loadingData(){
		boolean networkFlag=isAvailableNetwork();
		if(networkFlag){
			availableNetworkDo();
		}
		else{
			notAvailableNetworkDo();
		}
	}
	
	/*网络有效时做的事情*/
	private void availableNetworkDo(){
		/*拉第一页数据传给首页*/
		mLoadingText.setText(R.string.loading_first_page_data);
		mLoadingText.setVisibility(View.VISIBLE);
		new LoadingUtils(LoadingFirstPage.this).requestData();
//		loaddingFirstPageThread.start();
		
	}
	
	/*网络无效时做的事情*/
	private void notAvailableNetworkDo(){
		/*通过MarketGroupActivity启动管理界面*/
		mLoadingText.setText(R.string.loading_first_page_no_network);
		mLoadingText.setVisibility(View.VISIBLE);
		loaddingSoftManager();
	}
	
	/*检测网络*/
	public boolean isAvailableNetwork(){
		if (ToolsUtils.isAvailableNetwork(this)){
			return true;
		}
		else{
			return false;
		}
	}
	
	private BroadcastReceiver myReceiver =new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
		         String intentString=intent.getAction();
		         if(intentString.equals("com.android.loadingpagesuccess")){
		        	 finish();
		         }
		         else if(intentString.equals("com.android.loadingpagefail")){
		        	 notAvailableNetworkDo();
		         }
		         
		              
		}
	};
    
}
