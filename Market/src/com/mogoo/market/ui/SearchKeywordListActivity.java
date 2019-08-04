package com.mogoo.market.ui;

import java.util.ArrayList;

import com.mogoo.market.R;
import com.mogoo.market.adapter.HotAdapter;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.widget.TitleBar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Gallery.LayoutParams;

public class SearchKeywordListActivity extends BaseLoadableListActivityWithViewAndHeader{
    private PaginateListView_1<HotApp> mSearchList;
    private HotAdapter<HotApp> mSearchAdapter;
    private String mKeyword;
    private ListFooterView mFooterView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent intent = getIntent();
		if(intent == null) {
			finish();
		 }
		mKeyword=intent.getStringExtra("keyword");
		init();
	}
	
	private void init(){
		mSearchAdapter = new HotAdapter(this ,new ArrayList<HotApp>());
		mSearchList = new PaginateListView_1<HotApp>(this,mSearchAdapter);
		initListView(mSearchList);
		mFooterView = new ListFooterView(this, mSearchAdapter);
		mSearchList.addFooterView(mFooterView);
		mSearchList.setAdapter(mSearchAdapter);	
		mSearchList.doFirsetQuery(HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_SEARCH_APP_LIST),mKeyword);
		mSearchList.setXmlResultCallback(new HotApp.HotAppListCallback());
		setUpTitleBar();
		setSearchListview();
	}
	
	/**
	 * 初始化列表
	 * @param listview
	 */
	private void initListView(ListView listview) 
	{
		// listview.setDivider(this.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
	}
	
	 private void setUpTitleBar() {
			TitleBar titleBar = new TitleBar(this);
			titleBar.setMidText(getResources().getString(R.string.tv_sear_result));
			titleBar.rightBtn.setVisibility(View.INVISIBLE);
			titleBar.leftBtn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent backIntent = SearchKeywordListActivity.this.getBackIntent();
					MarketGroupActivity.getInstance().onBackPressed(backIntent);
				}
			});
			setBaseTopLayout(titleBar);
		}
   
	   private void setSearchListview(){
		   setBaseCenterLayout(mSearchList);
		   addEmptyView(this, mSearchList ,R.string.search_empty_tip);
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
	    
		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			if(keyCode == KeyEvent.KEYCODE_BACK) {
				Intent backIntent = SearchKeywordListActivity.this.getBackIntent();
				MarketGroupActivity.getInstance().onBackPressed(backIntent);
				return true;
			}else {
				return super.onKeyDown(keyCode, event);
			}
		}
}
