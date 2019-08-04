package com.beem.project.btf.bbs.view;

import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class TimeCameraPagerAdapter extends PagerAdapter {
	private List<? extends View> datas;
	private Context mContext;

	public TimeCameraPagerAdapter(List<? extends View> datas, Context context) {
		super();
		this.datas = datas;
		this.mContext = context;
	}
	@Override
	public int getCount() {
		return datas.size();
	}
	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == arg1;
	}
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView(datas.get(position));
	}
	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		if (datas.get(position).getParent() == null) {
			container.addView(datas.get(position), LayoutParams.FILL_PARENT,
					LayoutParams.FILL_PARENT);
		}
		return datas.get(position);
	}
	public List<? extends View> getDatas() {
		return datas;
	}
	public void setDatas(List<? extends View> datas) {
		this.datas = datas;
	}
}
