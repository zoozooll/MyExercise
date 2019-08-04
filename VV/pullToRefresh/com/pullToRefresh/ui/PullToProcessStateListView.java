package com.pullToRefresh.ui;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;

/**
 * @ClassName: PullToStateListView
 * @Description: 含有四种状态的listview:加载，超时，成功，空数据
 * @author: yuedong bao
 * @date: 2015-8-19 下午2:13:26
 */
public class PullToProcessStateListView extends FrameLayout {
	/** 过程viewMap，存放listview预加载，超时，空数据时的视图 */
	private Map<ProcessState, View> processViewMap = new HashMap<ProcessState, View>();
	private ProcessState processState;

	public enum ProcessState {
		PreLoad, TimeOut, Succeed, Emptydata;
	}

	public PullToProcessStateListView(Context context) {
		super(context);
		initView(context);
	}
	public PullToProcessStateListView(Context context, AttributeSet attrs,
			int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		initView(context);
	}
	public PullToProcessStateListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	private void initView(Context context) {
		createProcessView(ProcessState.Succeed, new PullToRefreshListView(
				context));
		createProcessView(ProcessState.PreLoad, new SimplePreloadProcessView(
				context));
		createProcessView(ProcessState.Emptydata,
				new SimpleEmptydataProcessView(context));
		createProcessView(ProcessState.TimeOut, new SimpleTimeOutProcessView(
				context));
		addView(processViewMap.get(ProcessState.Succeed), new LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.MATCH_PARENT));
		setProcessState(ProcessState.PreLoad);
		// 默认滑动listview时停止ImageLoader加载
		setOnScrollListener(new PauseOnScrollListener(
				ImageLoader.getInstance(), true, true));
	}
	/**
	 * @Title: createView
	 * @Description:
	 * @param: @param resultType
	 * @return: void
	 * @throws:
	 */
	protected void createProcessView(ProcessState resultType, View view) {
		View prevView = processViewMap.get(resultType);
		// 如果先前存在view并且已经加载在本View中，则将其移除
		if (prevView != null && prevView.getParent() == this) {
			removeView(prevView);
		}
		processViewMap.put(resultType, view);
	}
	/**
	 * @Title: setState
	 * @Description: 设置当前状态
	 * @param: @param state
	 * @return: void
	 * @throws:
	 */
	public void setProcessState(ProcessState state, boolean... noMoreData) {
		//LogUtils.i("nowState:" + state + " prevSate:" + processState);
		View prevView = processViewMap.get(processState);
		View nowView = processViewMap.get(state);
		if (processState != state) {
			if (processState == ProcessState.Succeed) {
				prevView.setVisibility(View.INVISIBLE);
			} else {
				removeView(prevView);
			}
			if (state == ProcessState.Succeed) {
				nowView.setVisibility(View.VISIBLE);
			} else {
				addView(nowView, new LayoutParams(
						android.view.ViewGroup.LayoutParams.MATCH_PARENT,
						android.view.ViewGroup.LayoutParams.MATCH_PARENT));
			}
		}
		getPullToRefreshListView().onPullRefreshComplete(noMoreData);
		processState = state;
	}
	/**
	 * @Title: getPullToRefreshListView
	 * @Description: 获取内部的listview
	 * @param: @return
	 * @return: PullToRefreshListView
	 * @throws:
	 */
	public PullToRefreshListView getPullToRefreshListView() {
		return ((PullToRefreshListView) processViewMap
				.get(ProcessState.Succeed));
	}
	/**
	 * @Title: setPullLoadEnabled
	 * @Description: 设置是否可以上拉加载
	 * @param: @param pullLoadEnabled
	 * @return: void
	 * @throws:
	 */
	public void setPullLoadEnabled(boolean pullLoadEnabled) {
		getPullToRefreshListView().setPullLoadEnabled(pullLoadEnabled);
	}
	/**
	 * @Title: setPullRefreshEnabled
	 * @Description: 设置是否可以下拉加载
	 * @param: @param enable
	 * @return: void
	 * @throws:
	 */
	public void setPullRefreshEnabled(boolean enable) {
		getPullToRefreshListView().setPullRefreshEnabled(enable);
	}
	/**
	 * @Title: getRefreshableView
	 * @Description: 获取内部刷新
	 * @param: @return
	 * @return: ListView
	 * @throws:
	 */
	public ListView getRefreshableView() {
		return getPullToRefreshListView().getRefreshableView();
	}
	/**
	 * @Title: getEmptydataProcessView
	 * @Description: 获取空数据时显示的视图
	 * @param: @return
	 * @return: SimpleEmptydataProcessView
	 * @throws:
	 */
	public SimpleEmptydataProcessView getEmptydataProcessView() {
		return (SimpleEmptydataProcessView) processViewMap
				.get(ProcessState.Emptydata);
	}
	/**
	 * @Title: getTimeoutProcessView
	 * @Description: 获取超时时显示的视图
	 * @param: @return
	 * @return: SimpleTimeOutProcessView
	 * @throws:
	 */
	public SimpleTimeOutProcessView getTimeoutProcessView() {
		return (SimpleTimeOutProcessView) processViewMap
				.get(ProcessState.TimeOut);
	}
	/**
	 * @Title: getPreloadProcessView
	 * @Description:
	 * @param: @return
	 * @return: SimplePreloadProcessView
	 * @throws:
	 */
	public SimplePreloadProcessView getPreloadProcessView() {
		return (SimplePreloadProcessView) processViewMap
				.get(ProcessState.PreLoad);
	}
	/**
	 * @Title: setOnRefreshListener
	 * @Description:设置刷新监听器
	 * @param: @param lis
	 * @return: void
	 * @throws:
	 */
	public void setOnRefreshListener(OnRefreshListener<ListView> lis) {
		getPullToRefreshListView().setOnRefreshListener(lis);
		// 设置超时默认重载
		getTimeoutProcessView().setOnReloadListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setProcessState(ProcessState.PreLoad);
				getPullToRefreshListView().doPullRefreshing(true, 0);
			}
		});
	}
	/**
	 * @Title: setLastUpdatedLabel
	 * @Description: 设置刷新时间
	 * @param: @param label
	 * @return: void
	 * @throws:
	 */
	public void setLastUpdatedLabel(Date label) {
		getPullToRefreshListView().setLastUpdatedLabel(label);
	}
	/**
	 * @Title: setHasMoreData
	 * @Description: 设置是否还有更多数据
	 * @param: @param hasMoreData
	 * @return: void
	 * @throws:
	 */
	public void setHasMoreData(boolean hasMoreData) {
		getPullToRefreshListView().setHasMoreData(hasMoreData);
	}
	/**
	 * @Title: doPullRefreshing
	 * @Description: 开始刷新，通常用于调用者主动刷新，典型的情况是进入界面，开始主动刷新，这个刷新并不是由用户拉动引起的
	 * @param: @param smoothScroll
	 * @param: @param delayMillis
	 * @return: void
	 * @throws:
	 */
	public void doPullRefreshing(final boolean smoothScroll,
			final long delayMillis) {
		setProcessState(ProcessState.Succeed);
		getRefreshableView().setSelection(0);
		getPullToRefreshListView().doPullRefreshing(smoothScroll, delayMillis);
	}
	/**
	 * @Title: setOnScrollListener
	 * @Description: 设置下拉刷新监听器
	 * @param: @param l
	 * @return: void
	 * @throws:
	 */
	public void setOnScrollListener(OnScrollListener l) {
		getPullToRefreshListView().setOnScrollListener(l);
	}
	/**
	 * @Title: setAdapter
	 * @Description: 设置listview的适配器
	 * @param: @param adapter
	 * @return: void
	 * @throws:
	 */
	public void setAdapter(ListAdapter adapter) {
		getPullToRefreshListView().getRefreshableView().setAdapter(adapter);
	}
	/**
	 * @Title: setListViewDivider
	 * @Description: 设置listView分隔符
	 * @param: @param resid
	 * @return: void
	 * @throws:
	 */
	public void setListViewDivider(int... resids) {
		getPullToRefreshListView().setListViewDivider(resids);
	}
}
