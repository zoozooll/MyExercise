package com.mogoo.market.ui;



import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.adapter.SoftMoveManagerAdapter;
import com.mogoo.market.model.InstalledAppInfo;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.widget.TitleBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Gallery.LayoutParams;

public class SoftMoveActivity extends BaseLoadableListActivityWithViewAndHeader {
	
	public final static int HANDLER_MOVE_LIST_CHANGE=1;
	private SoftMoveManagerAdapter<InstalledAppInfo> mSoftMoveAdapter;   //软件搬家列表适配器
	private ListView mSoftMoveListView;                          //软件搬家列表
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent == null) {
			finish();
		 }
		init();
	}
	
	private Handler mhandler = new Handler() 
	{
        public void handleMessage(Message msg) 
        {
            switch (msg.what) {
               
                case HANDLER_MOVE_LIST_CHANGE:
                	removeMessages(HANDLER_MOVE_LIST_CHANGE);
                	if(MarketApplication.installedAppList.isEmpty()){
                		mSoftMoveAdapter.notifyDataSetInvalidated();
                	}else{
                		mSoftMoveAdapter.notifyDataSetChanged();
                	}
                	break;
            }
        }
    };
    
	private void init(){
		mSoftMoveAdapter=new SoftMoveManagerAdapter<InstalledAppInfo>(this, -1, MarketApplication.installedAppList);
		mSoftMoveListView=new ListView(this);
		initListView(mSoftMoveListView);
		mSoftMoveListView.setAdapter(mSoftMoveAdapter);
		setUpTitleBar();
		setSoftMoveListview();
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
	
	private void setSoftMoveListview() 
	{
		setBaseCenterLayout(mSoftMoveListView);
		addEmptyView(this, mSoftMoveListView ,R.string.no_ram_apk);
		refreshMoveAdapter();
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
    
    private void refreshMoveAdapter(){
	    Message msg = new Message();
		msg.what = HANDLER_MOVE_LIST_CHANGE;
		mhandler.sendMessage(msg);
    }
    
    private void setUpTitleBar() {
		TitleBar titleBar = new TitleBar(this);
		titleBar.setMidText(getResources().getString(R.string.soft_move));
		titleBar.rightBtn.setVisibility(View.INVISIBLE);
		titleBar.leftBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent backIntent = SoftMoveActivity.this.getBackIntent();
				MarketGroupActivity.getInstance().onBackPressed(backIntent);
			}
		});
		setBaseTopLayout(titleBar);
	}
    
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent backIntent = SoftMoveActivity.this.getBackIntent();
			MarketGroupActivity.getInstance().onBackPressed(backIntent);
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
