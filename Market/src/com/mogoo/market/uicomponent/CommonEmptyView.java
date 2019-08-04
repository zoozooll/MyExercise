package com.mogoo.market.uicomponent;

import android.content.Context;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mogoo.market.R;

/**
 * 一般的空记录界面 在加载的时候如果没有数据在Adapter中就显示loading的界面， 否则显示没有记录的界面
 * 
 * @author fdl
 */
public class CommonEmptyView extends LinearLayout {
	private AdapterView<?> adapterView;

	/** 整个XML代表的view */
	private View view;

	/** 空记录界面 */
	private ViewGroup noRecordsFrame;

	/** 显示空记录的文字 */
	private TextView emptyText;
	
	/** 显示正在加载的界面 */
	private FrameLayout loadingFrame;

	/** 默认的无数据提示字符串 */
	private CharSequence defaultTipNoRecord;

	private boolean mForceRefresh = false;
	
	private DataSetObserver observer = new DataSetObserver() {
		/**
		 * This method is called when the entire data set has changed, most
		 * likely through a call to {@link Cursor#requery()} on a {@link Cursor}
		 * .
		 */
		public void onChanged() {
			if (!mEncountError) {
				// 加载失败后仅当强制刷新才能进行下一次加载.
				if(noRecordsFrame.getVisibility() == View.VISIBLE && !mForceRefresh) {
					return;
				}
				// 显示正在加载
				noRecordsFrame.setVisibility(View.GONE);
				loadingFrame.setVisibility(View.VISIBLE);
			} else {
				mEncountError = false;
				// 显示没有查询到数据的界面
				noRecordsFrame.setVisibility(View.VISIBLE);
				loadingFrame.setVisibility(View.GONE);
			}
			mForceRefresh = false;
		}

		/**
		 * This method is called when the entire data becomes invalid, most
		 * likely through a call to {@link Cursor#deactivate()} or
		 * {@link Cursor#close()} on a {@link Cursor}.
		 */
		public void onInvalidated() {
			// 显示没有查询到数据的界面
			noRecordsFrame.setVisibility(View.VISIBLE);
			loadingFrame.setVisibility(View.GONE);
		}
	};

	private boolean mEncountError;

	/**
	 * note:传入的adapterView必须是已经有adapter的了
	 * 
	 * @param paramContext
	 *            当前的Activity
	 * @param host
	 *            参照的view， 使本控件的可见性与hostView一致，传入null则应该自行设置本控件的可见性
	 * @param adapterView
	 *            设置此emptyView的View
	 */
	public CommonEmptyView(Context paramContext, AdapterView<?> adapterView) {
		super(paramContext);
		initView();
		this.adapterView = adapterView;
		// 注册数据监听器，当数据改变时显示空记录界面，当数据不可用时候显示正在加载中界面
		if (adapterView.getAdapter() == null) {
			throw new IllegalArgumentException(
					"adapter should set adapter first");
		}
		this.adapterView.getAdapter().registerDataSetObserver(observer);
	}

	/**
	 * @param paramContext
	 *            当前的Activity
	 * @param host
	 *            参照的view 使本控件的可见性与hostView一致，传入null则应该自行设置本控件的可见性
	 * @param adapterView
	 *            设置此emptyView的View
	 * @param AttributeSet
	 *            布局参数
	 */
	public CommonEmptyView(Context paramContext, AdapterView<?> adapterView,
			AttributeSet paramAttributeSet) {
		super(paramContext, paramAttributeSet);
		initView();
		this.adapterView = adapterView;
	}

	private void initView() {
		this.view = LayoutInflater.from(getContext()).inflate(
				R.layout.common_empty_view, null);
		this.noRecordsFrame = (RelativeLayout) this.view
				.findViewById(R.id.no_records_view);
		this.loadingFrame = (FrameLayout) this.view
				.findViewById(R.id.loading_view);
		this.emptyText = (TextView) this.view.findViewById(R.id.empty_text);
		defaultTipNoRecord = this.emptyText.getText();
		addView(this.view);
	}

	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (this.isShown()
				|| this.noRecordsFrame.getVisibility() == View.VISIBLE) {
			super.onLayout(changed, l, t, r, b);
		} else {
			setEmptyText(defaultTipNoRecord);
		}
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

	public void setEmptyText(CharSequence text) {
		emptyText.setText(text);
	}

	/**
	 * 设置显示空记录界面的时候需要显示的内容 <font color='red'>注意：此方法会使 {@code #setEmptyText}
	 * 失效</font>
	 * 
	 * @param v
	 */
	public void setEmptyShowView(View v) {
		noRecordsFrame.removeAllViews();
		noRecordsFrame.addView(v);
	}

	/**
	 * 遇到网络错误的时候告知一下这个方法
	 */
	public void encountError() {
		setEmptyText(this.getContext().getString(R.string.tip_to_refresh));
		mEncountError = true;
	}

	/**
	 * 遇到网络错误的时候告知一下这个方法
	 */
	public void encountError(CharSequence tip) {
		setEmptyText(tip);
		mEncountError = true;
	}

	public void forceRefresh() {
		mForceRefresh = true;
	}
	
	public void setRefreshListener(View.OnClickListener listener) {
		emptyText.setOnClickListener(listener);
	}
	
	/**
	 *当数据为空时的显示界面，不通过invalidate回调，因为invalidate是使listview翻滚到第一个
	 
	public void setInvalidated(){
		noRecordsFrame.setVisibility(View.VISIBLE);
		loadingFrame.setVisibility(View.GONE);
	}*/
}