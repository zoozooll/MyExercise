package com.mogoo.market.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

import com.mogoo.market.R;

public class BaseListActivity extends Activity {
	private Intent backIntent = null;
	protected LinearLayout baseTopLayout;
	protected LinearLayout baseHeaderLayout;
	protected LinearLayout baseCenterLayout;
	protected LinearLayout baseEmpty;
	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.base_listacitivity_layout);
		backIntent = getIntent().getParcelableExtra(
				MarketGroupActivity.EXTRA_BACK_INTENT);
		mInflater = getLayoutInflater();
		findViews();
	}

	public Intent getBackIntent() {
		return backIntent;
	}

	private void findViews() {
		baseTopLayout = (LinearLayout) findViewById(R.id.topLayout);
		baseHeaderLayout = (LinearLayout) findViewById(R.id.header);
		baseCenterLayout = (LinearLayout) findViewById(R.id.centerLayout);
		/* baseEmpty = (LinearLayout) findViewById(R.id.loadableListHolder); */
	}

	// public void empty(int topId){
	// headerLayout.removeAllViews();
	// View topView = mInflater.inflate(topId, null);
	// headerLayout.addView(topView);
	// }

	public void setBaseTopLayout(int topId) {
		baseTopLayout.removeAllViews();
		View topView = mInflater.inflate(topId, null);
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
		View topView = mInflater.inflate(headerId, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		baseHeaderLayout.addView(topView, params);
	}

	public void setBaseHeaderLayout(View header) {
		baseHeaderLayout.removeAllViews();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		baseHeaderLayout.addView(header, params);
	}
	
	public void setBaseCenterLayout(int centerId) {
		baseCenterLayout.removeAllViews();
		View topView = mInflater.inflate(centerId, null);
		baseCenterLayout.addView(topView);

	}

	public void setBaseCenterLayout(View list) {
		baseCenterLayout.removeAllViews();
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		baseCenterLayout.addView(list, params);
	}

	// public void setBaseEmpty(int emptyLayoutId) {
	// }

	public LinearLayout getBaseTopLayout() {
		return baseTopLayout;
	}

	public LinearLayout getBaseHeaderLayout() {
		return baseHeaderLayout;
	}

	public LinearLayout getBaseEmpty() {
		return baseEmpty;
	}

	public LinearLayout getBaseCenterLayout() {
		return baseCenterLayout;
	}

}
