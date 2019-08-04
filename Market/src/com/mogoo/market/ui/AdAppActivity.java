package com.mogoo.market.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.R;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Gallery.LayoutParams;

import com.mogoo.market.adapter.AdAppAdapter;
import com.mogoo.market.model.AdApp;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.widget.TitleBar;

/**
 * @author dengliren
 * @date:2012-1-4
 * @description
 */
public class AdAppActivity extends BaseLoadableListActivityWithViewAndHeader {
	public static final String EXTRA_PARENT_LIST_INDEX = "parent_list_index";

	private int mParentListIndex;
	private String mTitleStr;
	private String mAppUrl;
	private Bundle mExtraBundle;
	private String mPositionId;
	private String mAdId;

	private ListFooterView mFooterView;
	private PaginateListView_1<AdApp> adAppList;
	private AdAppAdapter<AdApp> adAppAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mExtraBundle = this.getIntent().getExtras();
		mTitleStr = mExtraBundle.getString("titleStr");
		mAppUrl = mExtraBundle.getString("appUrl");
		mPositionId = mExtraBundle.getString("positionId");
		mAdId = mExtraBundle.getString("adId");
		Map<String, String> params = new HashMap<String, String>();
		params.put("positionId", mPositionId);
		params.put("adId", mAdId);
		params.put("appId", IBEManager.getAppId()+"");

		Intent intent = getIntent();
		if (intent == null) {
			finish();
		}
		mParentListIndex = intent.getIntExtra(EXTRA_PARENT_LIST_INDEX, 0);
		setUpTitleBar();

		initListViewAdapter();
		initFooterViewAndEmptyView(adAppList, adAppAdapter);
		adAppList.doFirsetQuery(HttpUrls
				.createBaseUrlWithBaseParamAndExtendPairs(mAppUrl, params));
		adAppList.setXmlResultCallback(new AdApp.AdAppCallback());

	}

	private void setUpTitleBar() {
		TitleBar titleBar = new TitleBar(this);
		titleBar.setMidText(mTitleStr);
		titleBar.rightBtn.setVisibility(View.INVISIBLE);
		titleBar.leftBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent backIntent = AdAppActivity.this.getBackIntent();
				backIntent.putExtra(EXTRA_PARENT_LIST_INDEX, mParentListIndex);
				MarketGroupActivity.getInstance().onBackPressed(backIntent);
			}
		});
		setBaseTopLayout(titleBar);
	}

	private void initListViewAdapter() {
		adAppAdapter = new AdAppAdapter<AdApp>(this, new ArrayList<AdApp>());
		adAppList = new PaginateListView_1<AdApp>(this, adAppAdapter);
		adAppList.setCacheColorHint(Color.WHITE);

	}

	private void initFooterViewAndEmptyView(PaginateListView_1 listView,
			ArrayAdapter adapter) {

		mFooterView = new ListFooterView(this, adapter);
		listView.addFooterView(mFooterView);
		listView.setAdapter(adapter);
		super.setBaseCenterLayout(listView);

		CommonEmptyView emptyView = new CommonEmptyView(this, listView);
		ViewGroup layout = (ViewGroup) listView.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		listView.setEmptyView(emptyView);
		listView.setVerticalScrollBarEnabled(false);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
//		if(adAppAdapter.get)
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			Intent backIntent = AdAppActivity.this.getBackIntent();
			MarketGroupActivity.getInstance().onBackPressed(backIntent);
			return true;
		}else {
			return super.onKeyDown(keyCode, event);
		}
	}

}
