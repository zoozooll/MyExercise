package com.beem.project.btf.ui.adapter;

import com.beem.project.btf.ui.fragment.NewsTopTitleEditorFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

/** 实现可动态添加view的adpater */
public class NewsCameraViewPagerAdapter extends PagerAdapter {
	private static final String TAG = "NewsCameraViewPagerAdapter";
	private NewsTopTitleEditorFragment fragment;
	private int count;
	private int mChildCount;

	public NewsCameraViewPagerAdapter(NewsTopTitleEditorFragment fragment,
			int count) {// 构造函数  
		this.fragment = fragment;
		this.count = count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	@Override
	public int getCount() {// 返回数量  
		return count;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		((ViewPager) container).removeView((View) object);
		//LogUtils.v("destroyItem_position:" + position);
	}
	@Override
	public void finishUpdate(View arg0) {
		//LogUtils.v("finishUpdate:");
	}
	@Override
	public Object instantiateItem(ViewGroup arg0, int arg1) {// 返回view对象  
		//LogUtils.d("instantiateItem position:" + arg1);
		View view = fragment.generalNewsToptitleView();
		view.setId(arg1);
		arg0.addView(view, 0, new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		return view;
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public int getItemPosition(Object object) {
		if (mChildCount > 0) {
			mChildCount--;
			return POSITION_NONE;
		}
		return super.getItemPosition(object);
	}
	@Override
	public void notifyDataSetChanged() {
		mChildCount = getCount();
		Log.i(TAG, "~mChildCount~" + mChildCount);
		super.notifyDataSetChanged();
	}
}
