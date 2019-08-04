package com.tcl.manager.activity.adapter;

import java.util.List;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
/**
 * scan adapter//
 * @author wenchao.zhang
 *
 */
public class ScanAdapter extends PagerAdapter {

	private List<View> mListViews;
	public ScanAdapter(List<View> listViews){
		mListViews = listViews;
	}
	
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
//		container.removeView(mListViews.get(position));
		container.removeView(mListViews.get(position % mListViews.size()));  
	}


	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		// TODO Auto-generated method stub
		container.addView(mListViews.get(position % mListViews.size()));
//		return mListViews.get(position);
		return mListViews.get(position % mListViews.size());  
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == arg1;
	}

}
