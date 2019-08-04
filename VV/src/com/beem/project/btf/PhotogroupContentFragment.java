package com.beem.project.btf;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.builder.CompareToBuilder;

import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.IShareRstLis;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.utils.SortedList;
import com.butterfly.vv.adapter.ShareTranceCommentsAdapter;
import com.butterfly.vv.adapter.CommonPhotoAdapter.DataStatus;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.vv.utils.CToast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.vv.image.gallery.viewer.ScrollingViewPager;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass. Activities that contain this fragment must implement the
 * {@link PhotogroupContentFragment.OnFragmentInteractionListener} interface to handle interaction
 * events. Use the {@link PhotogroupContentFragment#newInstance} factory method to create an
 * instance of this fragment.
 */
public class PhotogroupContentFragment extends Fragment {
	// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
	private static final String ARG_PARAM1 = "param1";
	// TODO: Rename and change types of parameters
	private String mParam1;
	private OnFragmentInteractionListener mListener;
	private View rootView;
	private ScrollingViewPager scrollLyt;
	private int curIndex;
	private SparseArray<PullToProcessStateListView> contentViews = new SparseArray<PullToProcessStateListView>();
	private SparseArray<List<ImageFolderItem>> itemArray = new SparseArray<List<ImageFolderItem>>();
	private ShareResultLis lis = new ShareResultLis();

	private enum ContentTags {
		ONE
	};

	/**
	 * Use this factory method to create a new instance of this fragment using the provided
	 * parameters.
	 * @param param1 Parameter 1.
	 * @return A new instance of fragment PhotogroupContent.
	 */
	public static PhotogroupContentFragment newInstance(String param1) {
		PhotogroupContentFragment fragment = new PhotogroupContentFragment();
		Bundle args = new Bundle();
		args.putString(ARG_PARAM1, param1);
		fragment.setArguments(args);
		return fragment;
	}
	public PhotogroupContentFragment() {
		// Required empty public constructor
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments() != null) {
			mParam1 = getArguments().getString(ARG_PARAM1);
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (rootView != null) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		} else {
			rootView = inflater.inflate(R.layout.fragment_commen_test,
					container, false);
			scrollLyt = (ScrollingViewPager) rootView
					.findViewById(R.id.scrollLyt);
			scrollLyt.setAdapter(new MyPagerAdapter());
			scrollLyt.setOnPageChangeListener(new OnPageChangeListener() {
				@Override
				public void onPageSelected(int position) {
					setPosition(position);
				}
				@Override
				public void onPageScrolled(int position, float positionOffset,
						int positionOffsetPixels) {
					// TODO Auto-generated method stub
				}
				@Override
				public void onPageScrollStateChanged(int state) {
					// TODO Auto-generated method stub
				}
			});
		}
		setPosition(0);
		return rootView;
	}
	public void setPosition(int position) {
		curIndex = position;
		scrollLyt.setCurrentItem(position);
		loadNewData(position, new Start(), PullType.PullDown);
	}
	// TODO: Rename method, update argument and hook method into UI event
	public void onButtonPressed(Uri uri) {
		if (mListener != null) {
			mListener.onFragmentInteraction(uri);
		}
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try {
			mListener = (OnFragmentInteractionListener) activity;
		} catch (ClassCastException e) {
			throw new ClassCastException(activity.toString()
					+ " must implement OnFragmentInteractionListener");
		}
	}
	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}
	private void loadNewData(int index, Start start, PullType pullType) {
		new ShareRankingFootPrintLoadingDialog(getActivity(),
				ShareType.values()[index], start, lis, pullType)
				.execute();
	}

	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public int getCount() {
			return ShareType.values().length;
		}
		@Override
		public boolean isViewFromObject(View view, Object object) {
			return object == view;
		}
		@Override
		public Object instantiateItem(ViewGroup container, final int position) {
			PullToProcessStateListView lv = contentViews.get(position);
			if (lv == null) {
				lv = new PullToProcessStateListView(getActivity());
				lv.setListViewDivider();
				final ShareTranceCommentsAdapter adapter = new ShareTranceCommentsAdapter(
						getActivity(), lv.getRefreshableView());
				List<ImageFolderItem> data = itemArray.get(position);
				if (data != null) {
					adapter.addItems(data);
				}
				lv.setAdapter(adapter);
				lv.setOnScrollListener(new PauseOnScrollListener(ImageLoader
						.getInstance(), true, true));
				lv.getEmptydataProcessView().setEmptydataImg(
						R.drawable.timefly_user_nopic);
				// 列表刷新
				lv.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						loadNewData(position, new Start(null), PullType.PullDown);
					}
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						Start start = new Start(adapter.getStart().getVal());
						loadNewData(position, start, PullType.PullUp);
					}
				});
				contentViews.put(position, lv);
				container.addView(lv);
			}
			return lv;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			if (object instanceof View) {
				container.removeView((View) object);
			}
		}
	}
	
	private class ShareResultLis implements IShareRstLis {
		@Override
		public void onShareResult(List<ImageFolderItem> items,
				ShareType shareType, Start start, PullType pullType) {
			
			List<ImageFolderItem> data = itemArray.get(shareType.ordinal());
			if (data == null) {
				data = new ArrayList<ImageFolderItem>(items);
			} else {
				data.clear();
				data.addAll(items);
			}
			// 整合数据；
			PullToProcessStateListView shareListViews = contentViews
					.get(shareType.ordinal());
			if (shareListViews != null) {
				ShareTranceCommentsAdapter shareAdapter = (ShareTranceCommentsAdapter) ((HeaderViewListAdapter) (shareListViews
						.getPullToRefreshListView().getRefreshableView()
						.getAdapter())).getWrappedAdapter();
				shareAdapter.setStatus(DataStatus.memory);
				if (items == null || items.isEmpty()) {
					if (shareAdapter.isEmpty()) {
						shareListViews.setProcessState(ProcessState.Emptydata);
					} else {
						CToast.showToast(BeemApplication.getContext(), "没有更多数据",
								Toast.LENGTH_SHORT);
						shareListViews.setProcessState(ProcessState.Succeed);
					}
				} else {
					if (pullType == PullType.PullDown) {
						shareAdapter.clearItems();
					}
					shareAdapter.addItems(items);
					shareAdapter.notifyDataSetChanged();
					shareListViews.setProcessState(ProcessState.Succeed);
					if (pullType == PullType.PullUp) {
						shareAdapter.getStart().setStart(start);
					} else {
						// 下拉第一次赋值start
						if (shareAdapter == null) {
							shareAdapter.getStart().setStart(start);
						}
					}
				}
			}
		}
		@Override
		public void onTimeOut(ShareType shareType) {
			PullToProcessStateListView shareListViews = contentViews
					.get(shareType.ordinal());
			ShareTranceCommentsAdapter shareAdapter = (ShareTranceCommentsAdapter) ((HeaderViewListAdapter) (shareListViews
					.getPullToRefreshListView().getRefreshableView()
					.getAdapter())).getWrappedAdapter();
			if (shareAdapter.isEmpty()) {
				shareListViews.setProcessState(ProcessState.TimeOut);
			} else {
				shareListViews.setProcessState(ProcessState.Succeed);
			}
		}
	};

	/**
	 * This interface must be implemented by activities that contain this fragment to allow an
	 * interaction in this fragment to be communicated to the activity and potentially other
	 * fragments contained in that activity.
	 * <p>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html" >Communicating
	 * with Other Fragments</a> for more information.
	 */
	public interface OnFragmentInteractionListener {
		// TODO: Update argument type and name
		public void onFragmentInteraction(Uri uri);
	}
}
