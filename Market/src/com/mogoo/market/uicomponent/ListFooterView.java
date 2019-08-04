package com.mogoo.market.uicomponent;

import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogoo.market.R;

/**
 * 显示“正在加载数据”的页脚
 * 
 * @author fdl
 */
public class ListFooterView extends LinearLayout {

	private View view;
	private RelativeLayout mProgressLayout;
	private TextView mNoneDataTextView;
	private boolean mShowNodataText = true;

	private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			setVisibility(View.GONE);
		}

		@Override
		public void onInvalidated() {
			setVisibility(View.VISIBLE);
			mProgressLayout.setVisibility(View.GONE);
			if(mShowNodataText){
				mNoneDataTextView.setVisibility(View.VISIBLE);
			}
		}
	};

	public ListFooterView(Context context, BaseAdapter adapter) {
		super(context);
		initView();
		adapter.registerDataSetObserver(observer);
	}

	public ListFooterView(Context context, BaseAdapter adapter,
			AttributeSet attrs) {
		super(context, attrs);
		initView();
		adapter.registerDataSetObserver(observer);
	}

	private void initView() {
		this.view = LayoutInflater.from(getContext()).inflate(
				R.layout.list_footer, null);
		LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);

		this.view.setLayoutParams(localLayoutParams);
		this.mProgressLayout = (RelativeLayout) this.view
				.findViewById(R.id.loading_footer_rl);
		this.mNoneDataTextView = (TextView) this.view
				.findViewById(R.id.none_data_footer);
		addView(this.view);
	}

	public void setBackgroundColor(int paramInt) {
		this.view.setBackgroundColor(paramInt);
	}

	public void setBackgroundDrawable(Drawable paramDrawable) {
		this.view.setBackgroundDrawable(paramDrawable);
	}

	public void setBackgroundResource(int paramInt) {
		this.view.setBackgroundResource(paramInt);
	}

	public void showLoadingView() {
		mProgressLayout.setVisibility(View.VISIBLE);
		mNoneDataTextView.setVisibility(View.GONE);
		setVisibility(View.VISIBLE);
	}

	public void setShowNodataText(boolean isShown){
		this.mShowNodataText = isShown;
	}
//	public void showTheLastPageView() {
//		mProgressLayout.setVisibility(View.GONE);
//		mNoneDataTextView.setVisibility(View.VISIBLE);
//		setVisibility(View.VISIBLE);
//	}

}
