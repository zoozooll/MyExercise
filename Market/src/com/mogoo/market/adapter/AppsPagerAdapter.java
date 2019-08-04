package com.mogoo.market.adapter;

import java.util.ArrayList;

import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.model.Apps;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.uicomponent.PagerView;
import com.mogoo.parser.XmlResultCallback;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;


public class AppsPagerAdapter extends PagerAdapter {
	private static final int INDEX_SUBCLASS = 0;
	private static final int INDEX_QUALITY = 1;
	private static final int INDEX_NEWEST = 2;
	
	protected transient Activity mContext;
	
	private PaginateListView_1<Apps> mSubClassListView;
	private PaginateListView_1<HotApp> mQualityListView;
	private PaginateListView_1<HotApp> mNewestListView;
	
	private CursorAdapter mSubClassAdapter;
	private HotCursorAdapter<HotApp> mQualityAdapter;
	private HotCursorAdapter<HotApp> mNewestAdapter;
	
	private CommonEmptyView mSubClassEmptyView;
	private CommonEmptyView mQualityEmptyView;
	private CommonEmptyView mNewestEmptyView;
	
	private ImageDownloader mImageLoader;
	
	private int mLength = 0;
	private String[] mUrls;
	
	ArrayList<String> mArrayList = new ArrayList<String>();
	
	public AppsPagerAdapter(Activity context) {
		mContext = context;
		mUrls = new String[] {
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_APPS_LIST),
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_TOP_APPS_LIST),
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_NEWEST_APPS_LIST) };
		mLength = mUrls.length;
		mImageLoader = ImageDownloader.getInstance(mContext);
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
		
		if (position == INDEX_SUBCLASS) {
			listview = mSubClassListView;
			emptyView = mSubClassEmptyView;
		} else if (position == INDEX_QUALITY) {
			listview = mQualityListView;
			emptyView = mQualityEmptyView;
		} else if (position == INDEX_NEWEST) {
			listview = mNewestListView;
			emptyView = mNewestEmptyView;
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
	public void restoreState(Parcelable state, ClassLoader loader) {}
	
	@Override
	public Parcelable saveState() {
		return null;
	}
	
	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}
	
	public void stop() {
		
		if(mSubClassAdapter != null) {
			// mSubClassAdapter.unRegisterDownloadCompleteListener();
			// mSubClassAdapter.removePackageChangeListener();
			if(mSubClassAdapter.getCursor() != null && !mSubClassAdapter.getCursor().isClosed())
				mSubClassAdapter.getCursor().close();
		}
		if(mQualityAdapter != null) {
			 mQualityAdapter.unRegisterDownloadCompleteListener();
			 mQualityAdapter.removePackageChangeListener();
			if(mQualityAdapter.getCursor() != null && !mQualityAdapter.getCursor().isClosed())
				mQualityAdapter.getCursor().close();
		}
		if(mNewestAdapter != null) {
			 mNewestAdapter.unRegisterDownloadCompleteListener();
			 mNewestAdapter.removePackageChangeListener();
			if(mNewestAdapter.getCursor() != null && !mNewestAdapter.getCursor().isClosed())
				mNewestAdapter.getCursor().close();
		}
	}
	
	public void refresh() {
		if(mSubClassAdapter != null) {
			mSubClassAdapter.notifyDataSetChanged();
		}
		if(mQualityAdapter != null) {
			mQualityAdapter.notifyDataSetChanged();
		}
		if(mNewestAdapter != null) {
			mNewestAdapter.notifyDataSetChanged();
		}
	}
	
	private void initListViewAdapter() {
		IBeanDao<Apps> subClassDao = DaoFactory.getAppsCateDao(mContext);
		IBeanDao<HotApp> qualityDao = DaoFactory.getQualityAppsDao(mContext);
		IBeanDao<HotApp> newestDao = DaoFactory.getNewestAppsDao(mContext);
		
		mSubClassAdapter = new AppsAdapter<Apps> (mContext, subClassDao.getAllBean(), mImageLoader);
		mSubClassListView = new PaginateListView_1<Apps>(mContext, mSubClassAdapter, subClassDao, mUrls[INDEX_SUBCLASS]);
		
		mQualityAdapter = new HotCursorAdapter<HotApp> (mContext, qualityDao.getAllBean());
		mQualityListView = new PaginateListView_1<HotApp>(mContext, mQualityAdapter, qualityDao, mUrls[INDEX_QUALITY]);
		
		mNewestAdapter = new HotCursorAdapter<HotApp> (mContext, newestDao.getAllBean());
		mNewestListView = new PaginateListView_1<HotApp>(mContext, mNewestAdapter, newestDao, mUrls[INDEX_NEWEST]);
		
		initPaginateListView(INDEX_SUBCLASS, mSubClassListView, mSubClassAdapter, new Apps.AppsCallback());
		initPaginateListView(INDEX_QUALITY, mQualityListView, mQualityAdapter, new HotApp.HotAppListCallback());
		initPaginateListView(INDEX_NEWEST, mNewestListView, mNewestAdapter, new HotApp.HotAppListCallback());
		
		mSubClassEmptyView = new CommonEmptyView(mContext, mSubClassListView);
		mQualityEmptyView = new CommonEmptyView(mContext, mQualityListView);
		mNewestEmptyView = new CommonEmptyView(mContext, mNewestListView);
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
}
