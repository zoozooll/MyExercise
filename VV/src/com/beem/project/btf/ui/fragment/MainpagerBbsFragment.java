/**
 * 
 */
package com.beem.project.btf.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CustomViewPagerAdapter;
import com.beem.project.btf.bbs.view.CustomerPageChangeLis;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.IShareRstLis;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.TitleBtnChangeManger;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.adapter.ShareTranceCommentsAdapter;
import com.butterfly.vv.adapter.CommonPhotoAdapter.DataStatus;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.vv.image.gallery.viewer.ScrollingViewPager;

import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ListView;
import android.widget.Toast;

/**
 * @author hongbo ke
 */
public class MainpagerBbsFragment extends MainpagerAbstractFragment {
	private View view;
	private ScrollingViewPager footprintRankingViewPager;
	private CustomTitleBtn save_info_button;
	private PullToProcessStateListView[] shareListViews;
	private ShareTranceCommentsAdapter[] shareAdapterArr;
	private TitleBtnChangeManger headLineViewShare;
	private GalleryNavigation mMyNavigationView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.comments_photo_traces2, null);
			mMyNavigationView = (GalleryNavigation) view
					.findViewById(R.id.share_navigation_view);
			mMyNavigationView.setBtnLeftHide();
			mMyNavigationView.setBtnRightIcon(R.drawable.shared_refresh_selector);
			mMyNavigationView.setCameraListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					int currentItem = footprintRankingViewPager.getCurrentItem();
					shareListViews[currentItem].doPullRefreshing(true, 200);
				}
			});
			mMyNavigationView
			.setBtnLeftIcon(R.drawable.share_activity_close_selector);
			mMyNavigationView.inflateCenter(headLineLis);
			mMyNavigationView.setCenterText("分享榜", "周边");
			mMyNavigationView.setTopbarTab(0);
			// 内容部分
			footprintRankingViewPager = (ScrollingViewPager) view
					.findViewById(R.id.scrollLyt);
			ArrayList<View> shareContentView = new ArrayList<View>();
			for (int i = 0; i < 2; i++) {
				View footprintView = inflater.inflate(
						R.layout.comments_photo_traces2_viewpageritem, null);
				shareContentView.add(footprintView);
			}
			CustomViewPagerAdapter mScrollAdapter = new CustomViewPagerAdapter(
					shareContentView, mContext);
			footprintRankingViewPager.setAdapter(mScrollAdapter);
			footprintRankingViewPager
			.setOnPageChangeListener(footprintRankingPagerLis);
			// TODO
			shareListViews = new PullToProcessStateListView[shareContentView.size()];
			shareAdapterArr = new ShareTranceCommentsAdapter[shareContentView
			                                                 .size()];
			for (int i = 0; i < shareContentView.size(); i++) {
				View contentView = shareContentView.get(i);
				shareListViews[i] = (PullToProcessStateListView) contentView
						.findViewById(R.id.myxlistview);
				shareListViews[i].setListViewDivider();
				shareAdapterArr[i] = new ShareTranceCommentsAdapter(mContext,
						shareListViews[i].getRefreshableView());
				shareAdapterArr[i].setScollingViewPager(footprintRankingViewPager);
				shareListViews[i].setAdapter(shareAdapterArr[i]);
				shareListViews[i].setOnScrollListener(new PauseOnScrollListener(
						ImageLoader.getInstance(), true, true));
				shareListViews[i].getEmptydataProcessView().setEmptydataImg(
						R.drawable.timefly_user_nopic);
				// 列表刷新
				final int index = i;
				shareListViews[i]
						.setOnRefreshListener(new OnRefreshListener<ListView>() {
							@Override
							public void onPullDownToRefresh(
									PullToRefreshBase<ListView> refreshView,
									PullType pullType) {
								new ShareRankingFootPrintLoadingDialog(mContext,
										ShareType.values()[index], new Start(null),
										shareResultLis, PullType.PullDown)
								.execute();
							}
							@Override
							public void onPullUpToRefresh(
									PullToRefreshBase<ListView> refreshView,
									PullType pullType) {
								Start start = new Start(shareAdapterArr[index]
										.getStart().getVal());
								new ShareRankingFootPrintLoadingDialog(mContext,
										ShareType.values()[index], start,
										shareResultLis, PullType.PullUp).execute();
							}
						});
			}
			// 脚印排名列表
			new ShareRankingFootPrintLoadingDialog(mContext, ShareType.TopN,
					shareAdapterArr[0].getStart(), shareResultLis,
					PullType.PullDown).execute();
			InnerGuideHelper.showBBSGuide(mContext);
		}
		return view;
	}
	public void onEventMainThread(final EventBusData data) {
	}
	private void ShareSwitchPage(int pageIndex) {
		if (shareAdapterArr[pageIndex].getStatus() == DataStatus.empty) {
			new ShareRankingFootPrintLoadingDialog(mContext,
					ShareType.values()[pageIndex],
					shareAdapterArr[pageIndex].getStart(), shareResultLis,
					PullType.PullDown).execute();
		}
	}

	private OnPageChangeListener footprintRankingPagerLis = new CustomerPageChangeLis() {
		@Override
		public void onPageSelected(int pageIndex) {
			ShareSwitchPage(pageIndex);
			mMyNavigationView.setTopbarTab(pageIndex);
		}
	};
	private OnClickListener headLineLis = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
				case R.id.tvw_gallery_topbar_tab0:
					footprintRankingViewPager.setCurrentItem(0, true);
					ShareSwitchPage(0);
					mMyNavigationView.setTopbarTab(0);
					break;
				case R.id.tvw_gallery_topbar_tab1:
					footprintRankingViewPager.setCurrentItem(1, true);
					mMyNavigationView.setTopbarTab(1);
					ShareSwitchPage(1);
					break;
			}
		}
	};
	private IShareRstLis shareResultLis = new IShareRstLis() {
		@Override
		public void onShareResult(List<ImageFolderItem> items,
				ShareType shareType, Start start, PullType pullType) {
			shareAdapterArr[shareType.ordinal()].setStatus(DataStatus.memory);
			if (items == null || items.isEmpty()) {
				if (shareAdapterArr[shareType.ordinal()].isEmpty()) {
					shareListViews[shareType.ordinal()]
							.setProcessState(ProcessState.Emptydata);
				} else {
					CToast.showToast(BeemApplication.getContext(), "没有更多数据",
							Toast.LENGTH_SHORT);
					shareListViews[shareType.ordinal()]
							.setProcessState(ProcessState.Succeed);
				}
			} else {
				if (pullType == PullType.PullDown) {
					shareAdapterArr[shareType.ordinal()].clearItems();
				}
				shareAdapterArr[shareType.ordinal()].addItems(items);
				shareAdapterArr[shareType.ordinal()].notifyDataSetChanged();
				shareListViews[shareType.ordinal()]
						.setProcessState(ProcessState.Succeed);
				if (pullType == PullType.PullUp) {
					shareAdapterArr[shareType.ordinal()].getStart().setStart(
							start);
				} else {
					// 下拉第一次赋值start
					if (shareAdapterArr[shareType.ordinal()] == null) {
						shareAdapterArr[shareType.ordinal()].getStart()
								.setStart(start);
					}
				}
			}
		}
		@Override
		public void onTimeOut(ShareType shareType) {
			if (shareAdapterArr[shareType.ordinal()].isEmpty()) {
				shareListViews[shareType.ordinal()]
						.setProcessState(ProcessState.TimeOut);
			} else {
				shareListViews[shareType.ordinal()]
						.setProcessState(ProcessState.Succeed);
			}
		}
	};

	@Override
	public void autoAuthentificateCompleted() {
		// TODO Auto-generated method stub
	}
}
