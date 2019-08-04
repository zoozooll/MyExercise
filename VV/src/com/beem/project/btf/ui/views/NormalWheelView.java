package com.beem.project.btf.ui.views;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.views.wheelviewWidget.OnWheelChangedListener;
import com.beem.project.btf.ui.views.wheelviewWidget.WheelView;
import com.beem.project.btf.ui.views.wheelviewWidget.adapters.ArrayWheelAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class NormalWheelView {
	private View mView;
	private Context mContext;
	private WheelView view1, view2, view3;
	private String[] strs;
	private String currentValues;
	private NormalWheelViewsCount viewcount = NormalWheelViewsCount.one;

	public enum NormalWheelViewsCount {
		one, two, three
	}

	public NormalWheelView(Context context, String[] strs) {
		// TODO Auto-generated constructor stub
		mContext = context;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.setting_birthday, null);
		this.strs = strs;
		view1 = (WheelView) mView.findViewById(R.id.year);
		view2 = (WheelView) mView.findViewById(R.id.month);
		view2.setVisibility(View.GONE);
		view3 = (WheelView) mView.findViewById(R.id.day);
		view3.setVisibility(View.GONE);
		initView1();
	}
	private void initView1() {
		currentValues = strs[0];
		view1.setViewAdapter(new ArrayWheelAdapter(mContext, strs));
		view1.setCyclic(false);
		view1.setCurrentItem(0);// 默认显示当前年
		view1.setVisibleItems(9);
		view1.addChangingListener(new OnWheelChangedListener() {
			@Override
			public void onChanged(WheelView wheel, int oldValue, int newValue) {
				// TODO Auto-generated method stub
				currentValues = strs[newValue];
			}
		});
	}
	// 从外部设置滚轮渐变色
	public void setShadowColor(int start, int middle, int end) {
		switch (viewcount) {
			case one: {
				view1.setShadowColor(start, middle, end);
				break;
			}
			case two: {
				view1.setShadowColor(start, middle, end);
				view2.setShadowColor(start, middle, end);
				break;
			}
			case three: {
				view1.setShadowColor(start, middle, end);
				view2.setShadowColor(start, middle, end);
				view3.setShadowColor(start, middle, end);
			}
		}
	}
	// 返回布局对象
	public View getmView() {
		return mView;
	}
	// 返回选中的值
	public String getcurrentData() {
		return currentValues;
	}
}
