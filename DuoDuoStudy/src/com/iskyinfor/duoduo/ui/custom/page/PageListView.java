package com.iskyinfor.duoduo.ui.custom.page;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.iskyinfor.duoduo.R;

public class PageListView extends ListView implements OnScrollListener,
		OnItemClickListener {

	private Context context;

	public static final int INIT_PAGE = 0;
	private int mCacheCount = 0;
	private int mFirstVisibleItem = 0;
	private int mVisibleItemCount = 0;
	private int mTotalItemCount = 0;

	// ===============================

	private PageEventListener pageEvenListener;

	private int totalPage;
	public int getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * 默认页数
	 */
	private int mCurrentPage = INIT_PAGE;
	private String strSearch="";
	private int intGiftType=0;
	public int getmCurrentPage() {
		return mCurrentPage;
	}

	public void setmCurrentPage(int mCurrentPage) {
		this.mCurrentPage = mCurrentPage;
	}

	/**
	 * 数据适配器
	 */
	PageListAdapter pageListAdapter;

	/**
	 * 当页数据是否加载完成
	 * 
	 */
	private boolean isReqScuessData = false;

	/**
	 * 是否加载完所有页
	 */
	private boolean isDataComplete = false;
	/**
	 * 数据加载时异常
	 */
	private boolean isException = false;
	/**
	 * 是否初始化完成
	 */
	private boolean isInitComplete = false;
	/**
	 * 是否开始加载
	 */
	private boolean isStar = false;
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (pageListAdapter != null) {
				pageListAdapter.notifyDataSetChanged();
			}

		}
	};

	public PageListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.pageListView);
		mCacheCount = a.getInteger(R.styleable.pageListView_itemPageCount, 9);
		a.recycle();
		Log.i("peng5", "mCacheCount===:"+mCacheCount);
		initPageinateListView(context);
	}

	public PageListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.pageListView);
		mCacheCount = a.getInteger(R.styleable.pageListView_itemPageCount, 9);
		Log.i("peng5", "mCacheCount===:"+mCacheCount);
		initPageinateListView(context);
	}

	public PageListView(Context context) {
		super(context);
		initPageinateListView(context);
		// TODO Auto-generated constructor stub
		this.setOnScrollListener(this);
	}

	private void initPageinateListView(Context context) {
		isInitComplete = false;
		this.context = context;
		this.setOnScrollListener(this);
		this.setOnItemClickListener(this);
		isInitComplete = true;
	}

	public void setListAdapter(PageListAdapter adapter) {
		this.pageListAdapter = adapter;
		setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		switch (scrollState) {

		case OnScrollListener.SCROLL_STATE_IDLE:
			if (pageEvenListener != null) {
				pageEvenListener.onScrollIdleStateEvent(context, view,
						scrollState, mFirstVisibleItem, mVisibleItemCount,
						mTotalItemCount);
			}
			if (pageListAdapter != null) {
				pageListAdapter.notifyDataSetChanged();
			}
			Log.i("PLJ", "mFirstVisibleItem==>"+mFirstVisibleItem);
			Log.i("PLJ", "mVisibleItemCount==>"+mVisibleItemCount);
			Log.i("PLJ", "mTotalItemCount==>"+mTotalItemCount);
			break;
		case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
			isReqScuessData = true;
			if (pageEvenListener != null) {
				pageEvenListener.onScrollTouchStateEvent(context, view,
						scrollState, mFirstVisibleItem, mVisibleItemCount,
						mTotalItemCount);
			}
			if (pageListAdapter != null) {
				pageListAdapter.notifyDataSetChanged();
			}

			break;
		case OnScrollListener.SCROLL_STATE_FLING:
			if (pageEvenListener != null) {
				pageEvenListener.onScrollFlingStateEvent(context, view,
						scrollState, mFirstVisibleItem, mVisibleItemCount,
						mTotalItemCount);
			}
			break;
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.mFirstVisibleItem = firstVisibleItem;
		this.mVisibleItemCount = visibleItemCount;
		this.mTotalItemCount = totalItemCount;
		
		if (!isInitComplete) {
			return;
		}
		
		if (isException) {
			return;
		}
		
		if (pageEvenListener != null) {
			pageEvenListener.onScrollEvent(context, view, firstVisibleItem,
					visibleItemCount, totalItemCount);
		}
		
		if (mCurrentPage == totalPage) {
			return;
		}
		
		if (!isStar && isReqScuessData) {
			
			if (isLoadNextPage(mCacheCount, firstVisibleItem, visibleItemCount,
					totalItemCount)) {
				// TODO 加载页数
				if(intGiftType!=0)
				{
				   loadPage(mCurrentPage + 1,strSearch,intGiftType);
				}
				else{loadPage(mCurrentPage + 1);}
			}
		}
	}

	public void loadPage(int reqPage) {
		Log.i("liu", "loadPage====:"+reqPage);
		isStar = true;
		mCurrentPage = reqPage;
		PageinateTask pageinateTask = new PageinateTask();
		pageinateTask.execute(mCurrentPage);
	}

	public void loadPage(int reqPage,String strSearch,int intGiftType) {
		isStar = true;
		this.strSearch=strSearch;
		mCurrentPage = reqPage;
		PageinateTask pageinateTask = new PageinateTask();
		pageinateTask.execute(mCurrentPage,strSearch,intGiftType);
	}
	
	public boolean isLoadNextPage(int cacheCount, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		int visibleItemIndex = firstVisibleItem + visibleItemCount - 1; 
		boolean o= (totalItemCount - visibleItemIndex) < cacheCount ? true : false;
		
		return (totalItemCount - visibleItemIndex) < cacheCount ? true : false;
	}

	/**
	 * 
	 * 加载过数据后处理返回结果
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private  synchronized void  disposeLoadedData(PageinateContainer pageinateContainer) {
		
		if (pageinateContainer != null) {
			pageListAdapter.setArrayList(pageinateContainer.responseData);
			mTotalItemCount = pageinateContainer.totalCount;
			totalPage = pageinateContainer.totalPageCount;
			pageListAdapter.notifyDataSetChanged();
			if (pageEvenListener != null) {
				// pageEvenListener.endEveryPageListener(handler, true);
				pageEvenListener.endEveryPageListener(context, handler, true,
						pageinateContainer.responseData);
			}
			isStar = false;
			isReqScuessData = false;
			
		}

	}

	/**
	 * 加载数据的task
	 * 
	 * @author Administrator
	 */
	@SuppressWarnings("rawtypes")
 	class  PageinateTask extends AsyncTask<Object , Void, PageinateContainer> {
		private PageinateContainer pageinateContainer;

		@Override
		protected PageinateContainer doInBackground(Object... params) {
			// TODO Auto-generated method stub
			try {
				if(params.length==1)
				{pageinateContainer = netetworkDataInterface.condition((Integer)params[0]);}
				else
				{
					strSearch=String.valueOf(params[1]);
					intGiftType=(Integer)params[2];
					pageinateContainer = netetworkDataInterface.condition((Integer)params[0],String.valueOf(params[1]),(Integer)params[2]);}
			} catch (Exception e) {
				// 捕获异常状态，停止刷新
				e.printStackTrace();
				isException = true;
				if (pageEvenListener != null) {
					pageEvenListener.exceptionPageListener();
				}

			}

			return pageinateContainer;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected void onPostExecute(PageinateContainer result) {
			Log.i("liu", "result===:"+result);
			if (result != null) {
			
				disposeLoadedData(result);
			}
		}

		@Override
		protected void onProgressUpdate(Void... values) {
			super.onProgressUpdate(values);
		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}
	}

	private NetetworkDataInterface netetworkDataInterface;

	public NetetworkDataInterface getNetetworkDataInterface() {
		return netetworkDataInterface;
	}

	public void setNetetworkDataInterface(
			NetetworkDataInterface netetworkDataInterface) {
		this.netetworkDataInterface = netetworkDataInterface;
	}

	public PageEventListener getPageEvenListener() {
		return pageEvenListener;
	}

	public void setPageEvenListener(PageEventListener pageEvenListener) {
		this.pageEvenListener = pageEvenListener;
	}

	/**
	 * 获得网络数据的接口
	 */
	public interface NetetworkDataInterface {
		public PageinateContainer condition(int reqPage,String strSearch,int intGiftType);
		public PageinateContainer condition(int reqPage);

	}

}
