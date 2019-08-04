package com.mogoo.market.ui;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.Gallery.LayoutParams;

import com.mogoo.market.R;

import com.mogoo.market.adapter.StorageDeleteAdapter;

import com.mogoo.market.model.SdcardInstalledAppInfo;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ManagerClearAllView;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.ScanSdCardUtils;
import com.mogoo.market.widget.TitleBar;

public class SdcardInstallDeleteActivity extends BaseLoadableListActivityWithViewAndHeader {
	
	public final static int HANDLER_SCAN_SDCARD=1;
	private StorageDeleteAdapter<SdcardInstalledAppInfo> mSdcardDeleteAdapter; //从sdcard删除apk列表适配器
	private ListView mSdcardDeleteListView;                          //SDcard删除应用列表
	private Button mScanSdcardBotton = null;		     //扫描sdcard按钮
	private ScanSdCardUtils ssu;                                 //扫描sdcard工具
	private ManagerClearAllView mScanSdcardView = null;  //重新扫描sdcard的apk布局，用于列表addHeaderView
    private String sdcardPath;                           //sdcard路径
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent == null) {
			finish();
		 }
		init();
	}
	
	@SuppressWarnings("static-access")
	@Override
	protected void onRestart(){
		super.onRestart();
		if(!AppUtils.isSdcardExist()){	
			sendMessageScanSdcard();
		}else{
			MyToast.makeText(SdcardInstallDeleteActivity.this,
    				getResources().getText(R.string.scan_sdcard_tip),
    				2).show();
			mSdcardDeleteAdapter.clear();
			ssu.isStartScan=true;
			scanSdcard();
		}

	}
	
	@SuppressWarnings("static-access")
	@Override
	protected void onStop(){
		super.onStop();
		ssu.isStartScan=false;
	}
	
	private Handler mhandler = new Handler() 
	{
        @SuppressWarnings({ "unchecked", "rawtypes", "static-access"})
		public void handleMessage(Message msg) 
        {
            switch (msg.what) {
            case HANDLER_SCAN_SDCARD:
            	removeMessages(HANDLER_SCAN_SDCARD);
            	 if(ssu.myFiles.isEmpty())
         	       {
           	    	 HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSdcardDeleteListView.getAdapter();
          	    	 mSdcardDeleteAdapter=(StorageDeleteAdapter) listAdapter.getWrappedAdapter();
            		 mSdcardDeleteAdapter.notifyDataSetInvalidated();
  			    	
          	       }
          	    else
          	       {
          	    	HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSdcardDeleteListView.getAdapter();
          	    	mSdcardDeleteAdapter=(StorageDeleteAdapter) listAdapter.getWrappedAdapter();
          	    	mSdcardDeleteAdapter.notifyDataSetChanged();
       			  
          	        }
  			    break;
            }
        }
    };
    
	@SuppressWarnings("static-access")
	private void init(){
		mScanSdcardView = new ManagerClearAllView(this, getResources().getString(R.string.scan_sdcard));
		mScanSdcardBotton=(Button) mScanSdcardView.findViewById(R.id.btn_clear_all);
		mScanSdcardBotton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!AppUtils.isSDCardReady()){
					String text = getResources().getString(R.string.no_sdcard_tip);
					MyToast.makeText(SdcardInstallDeleteActivity.this, text, Toast.LENGTH_SHORT).show();
				}else{
					MyToast.makeText(SdcardInstallDeleteActivity.this,
		    				getResources().getText(R.string.scan_sdcard_tip),
		    				2).show();
				}
				mSdcardDeleteAdapter.clear();
				scanSdcard();
//				sendMessageScanSdcard();
			}
		});
		sdcardPath=AppUtils.getSDKpath();
		ssu=new ScanSdCardUtils(this);
		if(AppUtils.isSdcardExist()){
        	MyToast.makeText(SdcardInstallDeleteActivity.this,
    				getResources().getText(R.string.scan_sdcard_tip),
    				2).show();
        	}
//		scanSdcardThread.start();
//		sendMessageScanSdcard();
		ssu.isStartScan=true;
		scanSdcard();
		mSdcardDeleteAdapter=new StorageDeleteAdapter<SdcardInstalledAppInfo>(this, -1,ssu.myFiles);
		mSdcardDeleteListView=new ListView(this);
		initListView(mSdcardDeleteListView);
	
		mSdcardDeleteListView.addHeaderView(mScanSdcardView);
		mSdcardDeleteListView.setAdapter(mSdcardDeleteAdapter);
		setUpTitleBar();
		setSdcardDeleteListview();
	}
	
	/**
	 * 初始化列表
	 * @param listview
	 */
	private void initListView(ListView listview) 
	{
		listview.setDivider(this.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
	}
	
	private void setSdcardDeleteListview() 
	{
		setBaseCenterLayout(mSdcardDeleteListView);
		addEmptyView(this, mSdcardDeleteListView ,R.string.no_sd_apk);
//		sendMessageScanSdcard();
	}
	
	   /**
     * 添加列表数据为空时的界面
     */
    private void addEmptyView(Context context, ListView listview, int emptyTextId) 
    {
		CommonEmptyView emptyView = new CommonEmptyView(this, listview);
		emptyView.setEmptyText(context.getResources().getString(emptyTextId));

		ViewGroup layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
    
    private void sendMessageScanSdcard(){
    	Message msg = new Message();
		msg.what = HANDLER_SCAN_SDCARD;
		mhandler.sendMessage(msg);
    }
    
    private void setUpTitleBar() {
		TitleBar titleBar = new TitleBar(this);
		titleBar.setMidText(getResources().getString(R.string.delete_install_package));
		titleBar.rightBtn.setVisibility(View.INVISIBLE);
		titleBar.leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent backIntent = SdcardInstallDeleteActivity.this.getBackIntent();
				MarketGroupActivity.getInstance().onBackPressed(backIntent);
			}
		});
		setBaseTopLayout(titleBar);
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent backIntent = SdcardInstallDeleteActivity.this.getBackIntent();
			MarketGroupActivity.getInstance().onBackPressed(backIntent);
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
	private void scanSdcard(){
			new Thread(){
				@Override
				public void run(){
					ssu.myFiles.clear();
					ssu.FindAllAPKFile(new File(sdcardPath));
					sendMessageScanSdcard();

				}
			}.start();
			
		}
}
