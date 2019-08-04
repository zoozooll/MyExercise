package com.mogoo.market.paginate;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.http.HttpGetURIBuilder;
import com.mogoo.market.http.HttpSendAndRecvTask;
import com.mogoo.market.http.IPageOperator;
import com.mogoo.market.http.IStreamRspProcessor;
import com.mogoo.market.network.http.ErrorCode;
import com.mogoo.market.network.http.ErrorCodeUtils;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.parser.Result;
import com.mogoo.parser.XmlResultCallback;
import com.mogoo.parser.XmlResultParser;

public class InstallPaginateListView<T> extends ListView implements
		IStreamRspProcessor {
	private Context mContext;
	private String mUrl = "";
	private IBeanDao<T> mBeanDao;
	private HttpGetURIBuilder mUriBuilder;
	private XmlResultCallback xmlResultCallback;
	private BaseAdapter mPaginateAdapter;
	private OnScrollListener mOnScrollListener;
	
	private ListFooterView mFootView;
	private CommonEmptyView emptyView = null;
	
	private View mSlideView;
	
	public InstallPaginateListView(Context context){
		super(context);
		setDividerHeight(0);
	}
	public InstallPaginateListView(Context context, AttributeSet attrs){
		super(context, attrs);
	}
	
	public InstallPaginateListView(Context context, ArrayAdapter adapter) {
		super(context);
		mContext = context;
		mPaginateAdapter = adapter;
		initOnScrollListener();
		this.setOnScrollListener(mOnScrollListener);
		setDividerHeight(0);
	}

	public InstallPaginateListView(Context context, CursorAdapter adapter, IBeanDao<T> beanDao, String url) {
		super(context);
		mUrl = url;
		mContext = context;
		mPaginateAdapter = adapter;
		mBeanDao = beanDao;
		initOnScrollListener();
		this.setOnScrollListener(mOnScrollListener);
		setDividerHeight(0);
	}
	
	public InstallPaginateListView init(Context context, CursorAdapter adapter, IBeanDao<T> beanDao, String url){
		mUrl = url;
		mContext = context;
		mPaginateAdapter = adapter;
		mBeanDao = beanDao;
		initOnScrollListener();
		this.setOnScrollListener(mOnScrollListener);
		setDividerHeight(0);
		return this;
	}
	
	private void initOnScrollListener() {
		mOnScrollListener = new OnScrollListener() {
			int lastVisibleItem = 0;
			int totalItemCount = 0;

			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				int j = firstVisibleItem + visibleItemCount - 1;
				this.lastVisibleItem = j;
				this.totalItemCount = totalItemCount;
			}

			// 定义滑动列表监听器
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == SCROLL_STATE_IDLE) {
					int m = this.lastVisibleItem;
					int n = this.totalItemCount - 1;
					if ((m == n) && (!mFootView.isShown())) {
						doQuery();
					}
				}
			}

		};
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		try {
			if (mSlideView != null) {
				int x = (int) event.getX();
				int y = (int) event.getY();
				int[] location = new int[2];
				int width = mSlideView.getWidth();
				int height = mSlideView.getHeight();
				mSlideView.getLocationOnScreen(location);

				if (mSlideView != null && location[0] < x
						&& x < location[0] + width && location[1] < y
						&& y < location[1] + height) {
					return false;
				}
			}
			return super.onInterceptTouchEvent(event);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			return false;
			// android自身bug，在屏幕上进行多点快速滑动的时候会抛出这个异常.
			// ignore it.
		} catch (ArrayIndexOutOfBoundsException ex) {
			ex.printStackTrace();
			return false;
			// android自身bug，在屏幕上进行多点快速滑动的时候会抛出这个异常.
			// ignore it.
		}
	}
	
	public void setSlideView(View slidView) {
		this.mSlideView = slidView;
	}
	
	public void addFooterView(ListFooterView view) {
		view.setVisibility(View.GONE);
		super.addFooterView(view, null, false);
		mFootView = view;
	}
	
	
	@Override
	public void setEmptyView(View emptyView) {
		// TODO Auto-generated method stub
		super.setEmptyView(emptyView);
		if (emptyView instanceof CommonEmptyView) {
			this.emptyView = (CommonEmptyView) emptyView;
			((CommonEmptyView) emptyView)
					.setRefreshListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							InstallPaginateListView.this.emptyView.forceRefresh();
							mPaginateAdapter.notifyDataSetChanged();
							doQuery();
						}
					});
		}
	}

	

	public void setGetUriBuilder(HttpGetURIBuilder uriBuilder) {
		mUriBuilder = uriBuilder;
	}
	
	/**
	 * 第一次查询的时候初始化UriBuilder
	 */
	public void doFirsetQuery(String url){
		mUrl = url;
		mUriBuilder = new GetURIBuilder(url);
		doQuery();
	}
	public void doFirsetQuery(String url,String keyword){
		mUrl = url;
		mUriBuilder = new GetURIBuilder(url,keyword);
		doQuery();
	}
	
	public void doQuery() {
		mFootView.showLoadingView();
		if(mUriBuilder == null && !TextUtils.isEmpty(mUrl)) {
			mUriBuilder = new GetURIBuilder(mUrl);
		}else if(mUriBuilder == null && TextUtils.isEmpty(mUrl)) {
			throw new IllegalArgumentException("before do query, you must set request url.");
		}
		new HttpSendAndRecvTask(false, this, mContext).execute(mUriBuilder);
	}

	public void setList(ArrayList<T> arrayList,String url) {
		mUriBuilder = new GetURIBuilder(url);
		for (T t : arrayList) {
			((ArrayAdapter) mPaginateAdapter).add(t);
		}
		int count = arrayList.size();
		arrayList.clear();
		if (count == 0) {
			//mFootView.showTheLastPageView();
			mPaginateAdapter.notifyDataSetInvalidated();
		} else {
			((IPageOperator) mUriBuilder).toNextPage();
		}
	}
	
	public void onQueryResulted(InputStream response) {
		// TODO Auto-generated method stub
		boolean isCursorAdapter = !(mPaginateAdapter instanceof ArrayAdapter);
		Result result = XmlResultParser.parser(response, xmlResultCallback);
		if (result != null) {
			String errorCode = result.getErrorCode();
			if (ErrorCode.isSuccessCode(errorCode)) {
				if (result.getData() != null) {

					ArrayList<T> arrayList = (ArrayList<T>) result.getData();
					int count = arrayList.size();
					
					if(!isCursorAdapter) {
						for (T t : arrayList) {
							((ArrayAdapter) mPaginateAdapter).add(t);
						}
					}else {
						mBeanDao.addBeans(arrayList);
					}
					
					arrayList.clear();
					
					if (count == 0) {
						//mFootView.showTheLastPageView();
						mPaginateAdapter.notifyDataSetInvalidated();
						if(mPaginateAdapter.getCount() > 1)
							this.setSelection(mPaginateAdapter.getCount()-1);
					} else {
						if(isCursorAdapter) {
							((CursorAdapter) mPaginateAdapter).changeCursor(mBeanDao.getAllBean());
						}
						((IPageOperator) mUriBuilder).toNextPage();
					}
				}else {
					mPaginateAdapter.notifyDataSetInvalidated();
					if(mPaginateAdapter.getCount() > 1)
						this.setSelection(mPaginateAdapter.getCount()-1);
				}

			} else {
				// 显示错误信息
				ErrorCodeUtils.showErrorToast(mContext, errorCode);
			}
		}else {
			onError(new IllegalStateException("解析xml失败，请检查xml格式，是否包含特殊字符"));
//			mPaginateAdapter.notifyDataSetInvalidated();
		}
		if(mPaginateAdapter.getCount() == 0) {
			mPaginateAdapter.notifyDataSetInvalidated();
//			if(mPaginateAdapter.getCount() > 1)
//				this.setSelection(mPaginateAdapter.getCount()-1);
		}
	}

	public void onCancelled() {
		// TODO Auto-generated method stub

	}

	public void onError(Exception e) {
		
		// 先告知发生错误
		e.printStackTrace();
		if(emptyView!=null)
		{
			emptyView.encountError();
		}
		// 尽管没有加载到数据，也通知一下数据变更，让底部的footerView隐藏起来
		mPaginateAdapter.notifyDataSetChanged();

		//MyToast.makeText(getContext(), R.string.tip_no_network, Toast.LENGTH_SHORT).show();
	}

	private class GetURIBuilder extends HttpGetURIBuilder implements
			IPageOperator {
		private Map<String, String> params = new HashMap<String, String>();
		private int pageNo = 1;
		private int pageSize = 10;
		private String myUrl;

		GetURIBuilder() {
			super();
			params.put("pagesize", String.valueOf(pageSize));
			params.put("page", String.valueOf(getPageNo()));
		}
		
		GetURIBuilder(String url) {
			super();
			myUrl= url;
			params.put("pagesize", String.valueOf(pageSize));
			params.put("page", String.valueOf(getPageNo()));
		}
		
		GetURIBuilder(String url,String keyword) {
			super();
			myUrl= url;
			params.put("pagesize", String.valueOf(pageSize));
			params.put("page", String.valueOf(getPageNo()));
			params.put("key", String.valueOf(keyword));
		}
		
		private int getPageNo() {
			int result = mPaginateAdapter.getCount() % pageSize;
			int pageNo = (result != 0) ? mPaginateAdapter.getCount() / pageSize + 2 
					: mPaginateAdapter.getCount() / pageSize +1;
			return pageNo;
		}
		
//		GetURIBuilder(String url, Map<String, String> param) {
//			super();
//			myUrl = url;
//			params.put("pagesize", String.valueOf(pageSize));
//			params.put("page", String.valueOf(pageNo));
//			params.putAll(param);
//		}

		@Override
		public Map<String, String> getParams() {
			return params;
		}

		@Override
		public String getURIString() {
			return myUrl;
		}

		public void toNextPage() {
//			pageNo++;
			int result = mPaginateAdapter.getCount() % pageSize;
			pageNo = (result != 0) ? mPaginateAdapter.getCount() / pageSize + 2 
					: mPaginateAdapter.getCount() / pageSize +1;
			params.put("page", String.valueOf(pageNo));
		}

		public void toPreviousPage() {
//			pageNo--;
//			if (pageNo == 0) {
//				pageNo = 1;
//			}
//			params.put("page", String.valueOf(pageNo));
		}
		
		public void setMyUrl(String url){
			this.myUrl = url;
		}
	}

	public XmlResultCallback getXmlResultCallback() {
		return xmlResultCallback;
	}

	public void setXmlResultCallback(XmlResultCallback xmlResultCallback) {
		this.xmlResultCallback = xmlResultCallback;
	}

}
