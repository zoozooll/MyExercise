package com.mogoo.market.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;

import com.mogoo.market.R;
import com.mogoo.market.viewpager.extensions.TabsAdapter;
import com.mogoo.market.viewpager.extensions.ViewPagerTabButton;

public class FixedTabsAdapter implements TabsAdapter {
	
	private Activity mContext;
	private Resources mRes;
	private String[] mTitles = null;
	
	public FixedTabsAdapter(Activity ctx, String[] titles) {
		this.mContext = ctx;
		mRes = mContext.getResources();
		mTitles = titles;
	}
	
	@Override
	public View getView(int position) {
		ViewPagerTabButton tab;
		
		LayoutInflater inflater = mContext.getLayoutInflater();
		tab = (ViewPagerTabButton) inflater.inflate(R.layout.tab_fixed, null);
		tab.setWidth(mRes.getDimensionPixelSize(R.dimen.fixed_tabs_btn_width));
		tab.setHeight(mRes.getDimensionPixelSize(R.dimen.fixed_tabs_btn_height));
		
		if (position < mTitles.length) tab.setText(mTitles[position]);
		
		return tab;
	}
	
}
