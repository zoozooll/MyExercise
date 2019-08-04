package com.mogoo.market.adapter;

import java.util.HashMap;

import com.mogoo.components.ad.AdViewLayout;
import com.mogoo.components.ad.AdvertiseItem;
import com.mogoo.components.ad.DefalutOnClickListener;
import com.mogoo.components.ad.DoubleRowSlideButtomView;
import com.mogoo.components.ad.MogooLayoutParent;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.manager.AdManager;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.IBEManager;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.ui.AppDetailActivity;
import com.mogoo.market.ui.MainActivity;
import com.mogoo.market.ui.MarketGroupActivity;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.PagerView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.parser.XmlResultCallback;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class HomePagerAdapter extends PagerAdapter {

	private static final int SCREEN_WIDTH_320 = 320;
	private static final int SCREEN_WIDTH_480 = 480;
	private static final int SCREEN_WIDTH_540 = 540;
	private static final int SCREEN_WIDTH_960 = 640;
	private static final int BASE_AD_HEIGHT = 162;

	protected transient Activity mContext;

	private LayoutInflater mInflater;
	
	private LinearLayout adLayout;
	private AdViewLayout adview;
	private DoubleRowSlideButtomView adButtomView;
	private TextView noDateView;
	
	private PaginateListView_1<HotApp> mHotListView;
	private PaginateListView_1<HotApp> mLatestListView;
	private PaginateListView_1<HotApp> mNecessaryListView;
	
	private HotCursorAdapter<HotApp> mHotAdapter;
	private HotCursorAdapter<HotApp> mLatestAdapter;
	private HotCursorAdapter<HotApp> mNecessaryAdapter;

	private CommonEmptyView mHotEmptyView;
	private CommonEmptyView mLatestEmptyView;
	private CommonEmptyView mNecessaryEmptyView;
	
	private int mLength = 0;
	private String[] mUrls;

	public HomePagerAdapter(Activity context) {
		mContext = context;
		mUrls = new String[] {
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_RECOMMEND_APP_LIST),
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_LATEST_APP_LIST),
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_NECESSARY_APP_LIST) };
		mLength = mUrls.length;
		mInflater = mContext.getLayoutInflater();
		findADVise();
		initAdvise(adview);
		initListViewAdapter();
	}

	@Override
	public int getCount() {
		return mLength;
	}

	@Override
	public Object instantiateItem(View container, int position) {

		PagerView pagerView = new PagerView(mContext);
		PaginateListView_1 listview = null;
		ViewGroup parent = null;
		CommonEmptyView emptyView = null;
		
		if (position == MainActivity.SORT_HOT_NUMBER) {
			listview = mHotListView;
			emptyView = mHotEmptyView;
			// pagerView.getBaseHeaderLayout().setVisibility(View.VISIBLE);
			// parent = (ViewGroup) adLayout.getParent();
			// if (parent != null) {
			// parent.removeView(adLayout);
			// }
			// pagerView.setBaseHeaderLayout(adLayout);
		} else if (position == MainActivity.SORT_LATEST_NUMBER) {
			listview = mLatestListView;
			emptyView = mLatestEmptyView;
		} else if (position == MainActivity.SORT_NES_NUMBER) {
			listview = mNecessaryListView;
			emptyView = mNecessaryEmptyView;
		}

		parent = (ViewGroup) listview.getParent();
		if (parent != null) {
			parent.removeView(listview);
		}
		pagerView.setBaseCenterLayout(listview);
		addEmptyView(emptyView, listview);
		((ViewPager) container).addView(pagerView, 0);
		return pagerView;
	}

	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}

	public MogooLayoutParent getADView() {
		return (MogooLayoutParent) adview.getAdView();
	}
	
	public void stop() {
		adview.stopAutoSnap();
		
		if(mHotAdapter != null) {
			mHotAdapter.unRegisterDownloadCompleteListener();
			mHotAdapter.removePackageChangeListener();
			if(mHotAdapter.getCursor() != null && !mHotAdapter.getCursor().isClosed())
				mHotAdapter.getCursor().close();
		}
		if(mLatestAdapter != null) {
			mLatestAdapter.unRegisterDownloadCompleteListener();
			mLatestAdapter.removePackageChangeListener();
			if(mLatestAdapter.getCursor() != null && !mLatestAdapter.getCursor().isClosed())
				mLatestAdapter.getCursor().close();
		}
		if(mNecessaryAdapter != null) {
			mNecessaryAdapter.unRegisterDownloadCompleteListener();
			mNecessaryAdapter.removePackageChangeListener();
			if(mNecessaryAdapter.getCursor() != null && !mNecessaryAdapter.getCursor().isClosed())
				mNecessaryAdapter.getCursor().close();
		}
	}
	
	public void refresh() {
		if(mHotAdapter != null) {
			mHotAdapter.notifyDataSetChanged();
		}
		if(mNecessaryAdapter != null) {
			mNecessaryAdapter.notifyDataSetChanged();
		}
		if(mLatestAdapter != null) {
			mLatestAdapter.notifyDataSetChanged();
		}
	}
	
	/**
	 * initialize the lists
	 * 
	 * @author lcq-motone 2012-6-7
	 */
	private void initListViewAdapter() {
		IBeanDao<HotApp> hotDao = DaoFactory.getHotDao(mContext);
		IBeanDao<HotApp> necessaryDao = DaoFactory.getNecessaryDao(mContext);
		IBeanDao<HotApp> latestDao = DaoFactory.getLatestDao(mContext);

		mHotAdapter = new HotCursorAdapter<HotApp>(mContext,
				hotDao.getAllBean());
		mHotListView = new PaginateListView_1<HotApp>(
				mContext,
				mHotAdapter,
				hotDao,
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_RECOMMEND_APP_LIST));
		mHotListView.setSlideView(getADView());
		
		mNecessaryAdapter = new HotCursorAdapter<HotApp>(mContext,
				necessaryDao.getAllBean());
		mNecessaryListView = new PaginateListView_1<HotApp>(
				mContext,
				mNecessaryAdapter,
				necessaryDao,
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_NECESSARY_APP_LIST));
		
		
		mLatestAdapter = new HotCursorAdapter<HotApp>(mContext,
				latestDao.getAllBean());
		mLatestListView = new PaginateListView_1<HotApp>(mContext,
				mLatestAdapter, latestDao,
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_LATEST_APP_LIST));
		
		
		if(mHotListView.getHeaderViewsCount() == 0) {
			mHotListView.addHeaderView(adLayout);
		}
		initPaginateListView(MainActivity.SORT_HOT_NUMBER, mHotListView,
				mHotAdapter, new HotApp.HotAppListCallback());
		initPaginateListView(MainActivity.SORT_NES_NUMBER, mNecessaryListView,
				mNecessaryAdapter, new HotApp.HotAppListCallback());
		initPaginateListView(MainActivity.SORT_LATEST_NUMBER, mLatestListView,
				mLatestAdapter, new HotApp.HotAppListCallback());
		
		mHotEmptyView = new CommonEmptyView(mContext, mHotListView);
		mNecessaryEmptyView = new CommonEmptyView(mContext, mNecessaryListView);
		mLatestEmptyView = new CommonEmptyView(mContext, mLatestListView);
		// ensureUiListView(mSortMethod);
	}

	@SuppressWarnings("rawtypes")
	private void initPaginateListView(int position,
			PaginateListView_1 listview, BaseAdapter adapter,
			XmlResultCallback xmlResultCallback) {
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
		listview.setXmlResultCallback(xmlResultCallback);
		ListFooterView footerView = new ListFooterView(mContext, adapter);
		listview.addFooterView(footerView);
		listview.setAdapter(adapter);
		if (adapter.getCount() == 0) {
			listview.doFirsetQuery(mUrls[position]);
		}
	}

	@SuppressWarnings("rawtypes")
	private void addEmptyView(CommonEmptyView emptyView, PaginateListView_1 listview) {
		ViewGroup layout = (ViewGroup) emptyView.getParent();
		if(layout != null) {
			layout.removeView(emptyView);
		}
		
		layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}

	/**
	 * initialize the advertise widget初始化广告控件
	 * 
	 * @author lcq-motone 2012-6-7
	 */
	private void findADVise() {
		adLayout = (LinearLayout) mInflater.inflate(
				R.layout.base_header_layout, null);
		adview = (AdViewLayout) adLayout.findViewById(R.id.adView);
		adButtomView = (DoubleRowSlideButtomView) adLayout
				.findViewById(R.id.buttomView);
		Display display = mContext.getWindowManager().getDefaultDisplay();
		int width = display.getWidth();
		double diam = 7;
		if (width >= 640) {
			diam = diam * 2;
		} else if (width >= 480) {
			diam = diam * 1.5;
		}
		adButtomView.setPages(3);
		// adButtomView.setCurrentDotColor(0xff0085c8);//设置圆点颜色
		// adButtomView.setDotDiameter((float)diam);//设置圆点直径
		noDateView = (TextView) adLayout.findViewById(R.id.nodata);
		noDateView.setVisibility(View.GONE);
	}

	/**
	 * 初始化广告
	 * 
	 * @author 张永辉
	 * @date 2012-2-10
	 * @param adView
	 */
	private void initAdvise(AdViewLayout adView) {
		String uid = IBEManager.getInstance().getUid();
		String akey = IBEManager.getInstance().getAKey();
		String aid = IBEManager.getInstance().getAid();
		int appId = IBEManager.getAppId();
		String positionId = "A1,1,1;A1,1,2;A1,2,1;A1,2,2;A2,1,1;A2,1,2;A2,2,1;A2,2,2;A3,1,1;A3,1,2;A3,2,1;A3,2,2";
		String serverUrl = IBEManager.getInstance().getServerAddr();
                //String serverUrl = "http://test.htw.cc:9000";
		// String serverUrl = "http://192.168.10.5:9000";

		adView.setUrl(AdManager.getInstance().getAdPositionUrl(serverUrl, uid,
				akey, aid, appId, positionId, MarketApplication.SELF_VERSION));
		// 注册
		adView.registerMogooInfo(appId + "", uid, akey, aid, serverUrl);
		adView.setAdPading(4);
		int adHeight = getAdHeight();
		adView.setAdHeight(adHeight);
		adView.setAutoSnapTime(5000);// 自动翻页时间
		// adView.setDebug(true);// 打印日志
		adListener();
	}

	/** 添加广告 */
	public void adListener() {
		TextView t = new TextView(mContext);
		t.setText("hello world");

		adview.setAdButtomView(adButtomView);
		adview.showMe();

		adview.setAdButtomView(adButtomView);
		adview.showMe();

		adview.setAdButtomView(adButtomView);
		adview.showMe();

		adview.setAdOnClickListener(new MyAdOnClickListener(mContext));
	}

	/** 广告点击事件 */
	public AdvertiseItem mAdItem;

	public class MyAdOnClickListener extends DefalutOnClickListener {
		private static final String tag = "MyAdOnClickListener";

		public MyAdOnClickListener(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		public void OnClick(AdvertiseItem item) {
			super.OnClick(item);
			mAdItem = item;

			Log.d("OpenType", "dlr  :::openType::" + mAdItem.getOpenType());
			if (mAdItem.getOpenType() == 0) {

				Intent intent_list = new Intent(
						"com.mogoo.market.ui.AdAppActivity");
				Bundle bundle = new Bundle();
				bundle.putString("titleStr", item.getTitle());
				bundle.putString("appUrl", item.getUrl());
				bundle.putString("positionId", item.getpId());
				bundle.putString("adId", item.getAdId());
				intent_list.putExtras(bundle);

				MarketGroupActivity mga = MarketGroupActivity.getInstance();
				intent_list.putExtra(MarketGroupActivity.EXTRA_BACK_INTENT,
						mga.getNowTabIntent());
				mga.setupTabChildView(intent_list);
				mga.setupTabIntent(MarketGroupActivity.getCurrentTab(),
						intent_list);

			} else if (mAdItem.getOpenType() == 1) {
				Intent intentInfo = new Intent(
						"com.mogoo.market.ui.AppDetailActivity");
				Bundle bundleInfo = new Bundle();

				bundleInfo.putString(AppDetailActivity.EXTRA_APP_ID,
						item.getUrl());
				bundleInfo.putString(AppDetailActivity.EXTRA_AD_POSITION_ID,
						item.getpId());
				bundleInfo.putString(AppDetailActivity.EXTRA_AD_ID,
						item.getAdId());

				intentInfo.putExtras(bundleInfo);

				mContext.startActivity(intentInfo);
			}

		}

		/**
		 * ＠author lcq 根据点击位置得到广告请求的url NOTE：只适用于跳转到应用简介的广告
		 * 
		 * @param item
		 *            广告的item
		 * @return 广告请求的url
		 */
		private String getAdUrl(AdvertiseItem item) {
			HashMap<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("appId", IBEManager.getAppId() + "");
			paramMap.put("positionId", item.getpId());
			paramMap.put("adId", item.getAdId());
			String baseUrl = HttpUrls.createBaseUrlWithPairs(item.getUrl());
			String adUrl = HttpUrls.createBaseUrlWithExtendPairs(baseUrl,
					paramMap);
			return adUrl;
		}
	}

	/** 设置没有数据的提示 */
	public void setNoDataView(ListView list) {
		noDateView.setVisibility(View.VISIBLE);

	}

	/**
	 * @return
	 */
	private int getAdHeight() {
		Display display = mContext.getWindowManager().getDefaultDisplay();
		int adHeight = -1;
		if (display.getWidth() >= SCREEN_WIDTH_960) {
			adHeight = BASE_AD_HEIGHT * 2;
		} else if (display.getWidth() >= SCREEN_WIDTH_540) {
			adHeight = 271;
		} else if (display.getWidth() >= SCREEN_WIDTH_480) {
			adHeight = (int) (BASE_AD_HEIGHT * 1.5);
		} else if (display.getWidth() >= SCREEN_WIDTH_320) {
			adHeight = BASE_AD_HEIGHT;
		} else {
			adHeight = (int) (BASE_AD_HEIGHT * 0.75);
		}
		return adHeight;
	}
}
