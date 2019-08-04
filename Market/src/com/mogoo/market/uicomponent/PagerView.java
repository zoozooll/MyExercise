package com.mogoo.market.uicomponent;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.mogoo.market.R;

public class PagerView extends LinearLayout {
	protected LinearLayout baseTopLayout;
	protected LinearLayout baseHeaderLayout;
	protected LinearLayout baseCenterLayout;
	
	public PagerView(Context context) {
		super(context);
		initView();
	}
	
	public PagerView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}


	private void initView() {
		View view = LayoutInflater.from(getContext()).inflate(
				R.layout.pager_view_layout, null);
		baseTopLayout = (LinearLayout) view.findViewById(R.id.ly_top_module);
		baseTopLayout.setVisibility(View.GONE);
		baseHeaderLayout = (LinearLayout) view.findViewById(R.id.ly_header_module);
		baseHeaderLayout.setVisibility(View.GONE);
		baseCenterLayout = (LinearLayout) view.findViewById(R.id.ly_center_module);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		view.setLayoutParams(localLayoutParams);
		addView(view);
	}
	
	public void setBaseTopLayout(int topId) {
		baseTopLayout.removeAllViews();
		View topView = LayoutInflater.from(getContext()).inflate(topId, null);
		baseTopLayout.addView(topView);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) topView.getLayoutParams();
        params.height = LayoutParams.FILL_PARENT;
        params.width = LayoutParams.FILL_PARENT;
        topView.setLayoutParams(params);
	}

	public void setBaseTopLayout(View view) {
		baseTopLayout.removeAllViews();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		baseTopLayout.addView(view, params);
	}
	
	public void setBaseHeaderLayout(int headerId) {
		baseHeaderLayout.removeAllViews();
		View topView = LayoutInflater.from(getContext()).inflate(headerId, null);
		baseHeaderLayout.addView(topView);
	}

	public void setBaseHeaderLayout(View header) {
		baseHeaderLayout.removeAllViews();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		baseHeaderLayout.addView(header, params);
	}
	
	public void setBaseCenterLayout(int centerId) {
		baseCenterLayout.removeAllViews();
		View topView = LayoutInflater.from(getContext()).inflate(centerId, null);
		baseCenterLayout.addView(topView);

	}

	public void setBaseCenterLayout(View list) {
		baseCenterLayout.removeAllViews();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT);
		baseCenterLayout.addView(list, params);
	}
	
	public LinearLayout getBaseTopLayout() {
		return baseTopLayout;
	}

	public LinearLayout getBaseHeaderLayout() {
		return baseHeaderLayout;
	}

	public LinearLayout getBaseCenterLayout() {
		return baseCenterLayout;
	}
}
