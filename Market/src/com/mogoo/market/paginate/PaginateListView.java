package com.mogoo.market.paginate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mogoo.market.R;
import com.mogoo.market.http.IStreamRspProcessor;
import com.mogoo.market.network.http.ErrorCode;
import com.mogoo.market.network.http.ErrorCodeUtils;
import com.mogoo.market.network.http.NetWorkTask;
import com.mogoo.market.network.http.NetworkTaskListener;
import com.mogoo.parser.Result;
import com.mogoo.parser.XmlResultCallback;
import com.mogoo.parser.XmlResultParser;

public class PaginateListView<T> extends ListView implements
		AbsListView.OnScrollListener, IStreamRspProcessor {
	/** 分页条件 */
	private PaginateCondition mPaginateCondition;

	/** 分页适配器 */
	private PaginateAdapter<T> mPaginateAdapter;

	/** 数据解析器 */
	private XmlResultCallback xmlResultCallback;
	/**
	 * 网络任务
	 */
	private NetWorkTask networkTask;

	/** 正在加载的视图 */
	private View mLoading;

	/** 是否正在加载 */
	private boolean isLoading = false;

	/** 是否已全部加载完数据 */
	private boolean isWholeLoaded = false;

	/** 是否还在滚动 */
	// private int scrolling = 0;

	/** 是否已加载第一页数据 */
	private boolean isStart = false;

	private Context mContext;

	private OnExternalLoadingListener mOnExternalLoadingListener;

	public interface OnExternalLoadingListener {
		void doLoadingStarted();

		void doLoadingFailed();

		void doLoadingComplete();

		void doLoadingCancelled();
	}

	/**
	 * 处理返回的结果
	 */
	private NetworkTaskListener listener = new NetworkTaskListener() {

		public void onLoadingComplete(InputStream responseData) {

			Result result = XmlResultParser.parser(responseData,
					xmlResultCallback);
			if (result != null) {
				String errorCode = result.getErrorCode();
				if (ErrorCode.isSuccessCode(errorCode)) {
					if (result.getData() != null) {

						ArrayList<T> arrayList = (ArrayList<T>) result
								.getData();
						int count = arrayList.size();

						// 保存数据--检查是否已全部加载
						if (count < mPaginateCondition.getPageSize()) {
							isWholeLoaded = true;
						}

						mPaginateAdapter.add(arrayList);
						arrayList.clear();

						// 更新界面
						mPaginateAdapter.notifyDataSetChanged();
						mPaginateCondition.currentPage++;

					} else {

						if (mPaginateCondition.currentPage == 0) {
							mPaginateAdapter.clear();
						}
						mPaginateAdapter.notifyDataSetChanged();
					}
				} else {
					// 显示错误信息
					ErrorCodeUtils.showErrorToast(mContext, errorCode);
				}
			}

			changeState();
			if (mOnExternalLoadingListener != null) {
				if (mPaginateAdapter.getCount() == 0) {
					mOnExternalLoadingListener.doLoadingFailed();
				} else {
					mOnExternalLoadingListener.doLoadingComplete();
				}
			}
		}

		public void onLoadingStarted() {
			if (mOnExternalLoadingListener != null) {
				mOnExternalLoadingListener.doLoadingStarted();
			}
		}

		public void onLoadingFailed() {
			if (mOnExternalLoadingListener != null) {
				mOnExternalLoadingListener.doLoadingFailed();
			}
		}

		public void onLoadingCancelled() {
			if (mOnExternalLoadingListener != null) {
				mOnExternalLoadingListener.doLoadingCancelled();
			}
		}
	};

	public PaginateListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		mContext = context;
		initPaginateListView();
	}

	public PaginateListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initPaginateListView();
	}

	public PaginateListView(Context context) {
		super(context);
		mContext = context;
		initPaginateListView();
	}

	public void setOnExternalLoadingListener(OnExternalLoadingListener listener) {
		mOnExternalLoadingListener = listener;
	}

	public void initPaginateListView() {
		this.setOnScrollListener(this);
	}

	/**
	 * 开始加载数据
	 */
	public void start() {
		if (mPaginateCondition == null) {
			// LogUtils.debug(PaginateListView.class,
			// "start--startmPaginateCondition is null");
			return;
		}

		this.reset();

		this.loadNextPageData();
	}

	/**
	 * 重置
	 */
	private void reset() {
		if (networkTask != null && !networkTask.isCancelled()) {
			networkTask.cancel(true);
		}

		if (mPaginateAdapter != null) {
			mPaginateAdapter.clear();
		}

		if (this.getFooterViewsCount() > 0) {
			this.removeFooterView();
		}

		if (mPaginateCondition != null) {
			mPaginateCondition.currentPage = 0;
		}

		isWholeLoaded = false;
		isLoading = false;
	}

	/** 加载下一页数据 */
	public void loadNextPageData() {
		// 为什么移动位置就会出现问题呢
		isLoading = true;

		addFooterView();

		if (mPaginateCondition == null) {
			// LogUtils.debug(PaginateListView.class,
			// "mPaginateCondition is null");
			return;
		}

		if (null == mPaginateCondition.getParamsMap()) {
			mPaginateCondition.setParamsMap(new HashMap<String, String>());
		}

		/**
		 * 启动网络任务
		 */
		mPaginateCondition.getParamsMap().put(
				PaginateCondition.PARAM_CURRENT_PAGE,
				String.valueOf(mPaginateCondition.currentPage + 1));
		networkTask = new NetWorkTask(mContext, mPaginateCondition);
		networkTask.setListener(listener);
		networkTask.execute();
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setAdapter(ListAdapter adapter) {
		this.mPaginateAdapter = (PaginateAdapter<T>) adapter;
		super.setAdapter(adapter);
	}

	@Override
	public ListAdapter getAdapter() {
		return this.mPaginateAdapter;
	}

	public void addFooterView() {
		if (mLoading == null) {
			mLoading = LayoutInflater.from(mContext).inflate(
					R.layout.list_footer_loading, null);
		}

		this.addFooterView(mLoading);
	}

	public void removeFooterView() {
		if (mLoading != null) {
			this.removeFooterView(mLoading);
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {
		case SCROLL_STATE_IDLE: {
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {
				enterLoad();
			}
			break;
		}
		case SCROLL_STATE_TOUCH_SCROLL: {
			break;
		}
		case SCROLL_STATE_FLING: {
			if (view.getLastVisiblePosition() == (view.getCount() - 1)) {//
				enterLoad();
			}
			break;
		}
		}

	}

	public void enterLoad() {
		if (!isLoading && !isWholeLoaded) {
			loadNextPageData();
		}
	}

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		if (isStart == false && !isLoading) {
			loadNextPageData();
			isStart = true;
		}
	}

	private void changeState() {
		try {
			isLoading = false;
			if (mLoading != null) {
				removeFooterView();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void destroy() {
		if (networkTask != null
				&& networkTask.getStatus() == AsyncTask.Status.RUNNING) {
			networkTask.cancel(true);
		}
	}

	public PaginateCondition getmPaginateCondition() {
		return mPaginateCondition;
	}

	public void setmPaginateCondition(PaginateCondition mPaginateCondition) {
		this.mPaginateCondition = mPaginateCondition;
	}

	public boolean isLoading() {
		return isLoading;
	}

	public void setLoading(boolean isLoading) {
		this.isLoading = isLoading;
	}

	public boolean isWholeLoaded() {
		return isWholeLoaded;
	}

	public void setWholeLoaded(boolean isWholeLoaded) {
		this.isWholeLoaded = isWholeLoaded;
	}

	public XmlResultCallback getXmlResultCallback() {
		return xmlResultCallback;
	}

	public void setXmlResultCallback(XmlResultCallback xmlResultCallback) {
		this.xmlResultCallback = xmlResultCallback;
	}

	/**
	 * 重新加载
	 * 
	 * @author 张永辉
	 * @date 2012-2-11
	 */
	public void reLoad() {
		mPaginateCondition.currentPage = 0;
		start();
	}

	public void onQueryResulted(InputStream response) {
		// TODO Auto-generated method stub

		Result result = XmlResultParser.parser(response, xmlResultCallback);
		if (result != null) {
			String errorCode = result.getErrorCode();
			if (ErrorCode.isSuccessCode(errorCode)) {
				if (result.getData() != null) {

					ArrayList<T> arrayList = (ArrayList<T>) result.getData();
					int count = arrayList.size();

					// 保存数据--检查是否已全部加载
					if (count < mPaginateCondition.getPageSize()) {
						isWholeLoaded = true;
					}

					mPaginateAdapter.add(arrayList);
					arrayList.clear();

					// 更新界面
					mPaginateAdapter.notifyDataSetChanged();
					mPaginateCondition.currentPage++;

				} else {

					if (mPaginateCondition.currentPage == 0) {
						mPaginateAdapter.clear();
					}
					mPaginateAdapter.notifyDataSetChanged();
				}
			} else {
				// 显示错误信息
				ErrorCodeUtils.showErrorToast(mContext, errorCode);
			}
		}

		changeState();
		if (mOnExternalLoadingListener != null) {
			if (mPaginateAdapter.getCount() == 0) {
				mOnExternalLoadingListener.doLoadingFailed();
			} else {
				mOnExternalLoadingListener.doLoadingComplete();
			}
		}

	}

	public void onCancelled() {
		// TODO Auto-generated method stub

	}

	public void onError(Exception e) {
		// TODO Auto-generated method stub

	}

}
