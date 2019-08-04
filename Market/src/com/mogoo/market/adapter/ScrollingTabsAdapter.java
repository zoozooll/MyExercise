package com.mogoo.market.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import com.mogoo.market.R;
import com.mogoo.market.viewpager.extensions.TabsAdapter;


public class ScrollingTabsAdapter implements TabsAdapter {
	
	private Activity mContext;
	private Resources mRes;
	private String[] mTitles = null;
	
	public ScrollingTabsAdapter(Activity ctx, String[] titles) {
		this.mContext = ctx;
		mRes = mContext.getResources();
		mTitles = titles;
	}
	
	@Override
	public View getView(int position) {
		Button tab;
		
		LayoutInflater inflater = mContext.getLayoutInflater();
		tab = (Button) inflater.inflate(R.layout.tab_scrolling, null);
		tab.setWidth(mRes.getDimensionPixelSize(R.dimen.scrolling_tabs_btn_width));
		tab.setHeight(mRes.getDimensionPixelSize(R.dimen.scrolling_tabs_btn_height));
		
		if (position < mTitles.length) tab.setText(mTitles[position]);
		
		return tab;
	}
	
}
