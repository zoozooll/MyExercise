package com.beem.project.btf.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ListView;
import android.widget.TextView;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.ImageGalleryActivity;
import com.beem.project.btf.ui.adapter.ThumbnailAdapter;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.fragment.TalkFragement.FragmentType;
import com.beem.project.btf.ui.fragment.TalkFragement.SendmsgListenter;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.adapter.CommentAdapter;
import com.butterfly.vv.adapter.CommentAdapter.ItemChangedListener;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder.onLocationLis;
import com.butterfly.vv.model.Comment;
import com.butterfly.vv.model.CommentItem;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.view.timeflyView.HorizontalListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.pullToRefresh.ui.PullToRefreshListView;

import de.greenrobot.event.EventBus;

/**
 * @category app中有两处引用了评论展示，故封装成单独的一个模块
 */
public class ShareRankingFragment extends Fragment implements IEventBusAction {
	private Context mContext;
	private View view;
	private PullToRefreshListView replyLv;
	private View replyLvHeadView;
	private ViewPager bigPicPager;
	private TextView praise_tv, replyNum_tv, comment_nodata;
	private TextView locPoint_tv;
	private TextView pager_current;
	private ImageFolderItem folderItem;
	private picPagerAdapter picPagerAdapter;
	private CommentAdapter replyListViewAdapter;
	private int count = 0;
	private String nextAct;
	private ArrayList<ImageView> bigPicIvList = new ArrayList<ImageView>();
	public static final String SR_FOLDERITEM = "sr_folderItem";
	public static final String SR_NEXT = "sr_next";
	public static final String SR_ISTHUMBUPED = "sr_isthumbuped";
	private static final String TAG = "ShareRankingFragment";
	private HorizontalListView thumbnail_list;
	private ThumbnailAdapter thumbnailAdapter;
	private DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
			.cacheInMemory(true).cacheOnDisk(true).build();

	public ShareRankingFragment() {
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 如果启动该fragment时包含了SHARERANKING_ID参数,folderItem为数据包
		if (getArguments().containsKey(SR_FOLDERITEM)
				&& getArguments().containsKey(SR_NEXT)) {
			folderItem = getArguments().getParcelable(SR_FOLDERITEM);
			nextAct = getArguments().getString(SR_NEXT);
		}
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
			return view;
		}
		view = inflater.inflate(R.layout.share_ranking_fragment, container,
				false);
		replyLv = (PullToRefreshListView) view.findViewById(R.id.app_list);
		replyLv.setPullRefreshEnabled(false);
		replyLv.setVisibility(View.VISIBLE);
		// 头部
		replyLvHeadView = inflater.inflate(
				R.layout.share_ranking_detail_headview, null);
		bigPicPager = (ViewPager) replyLvHeadView.findViewById(R.id.picPager);
		replyLv.getRefreshableView().addHeaderView(replyLvHeadView);
		setHeaderView(view);
		initViewState();
		return view;
	}
	private void setHeaderView(View view) {
		ImageView pic_btn = (ImageView) view.findViewById(R.id.pic_btn);
		TextView timePoint_tv = (TextView) view.findViewById(R.id.timePoint_tv);
		TextView ageSex_tv = (TextView) view.findViewById(R.id.ageSex_tv);
		TextView name = (TextView) view.findViewById(R.id.name);
		TextView picNum = (TextView) view.findViewById(R.id.picNum);
		praise_tv = (TextView) view.findViewById(R.id.praise_tv);
		praise_tv.setSelected(folderItem.getImageFolder().isThumbup());
		locPoint_tv = (TextView) view.findViewById(R.id.locPoint_tv);
		pager_current = (TextView) view.findViewById(R.id.pager_current);
		replyNum_tv = (TextView) view.findViewById(R.id.replyNum_tv);
		comment_nodata = (TextView) view.findViewById(R.id.comment_nodata);
		TextView signText = (TextView) view.findViewById(R.id.tags_content);
		ageSex_tv.setSelected(folderItem.getContact().getSexInt() == 0 ? true
				: false);
		ageSex_tv.setText(BBSUtils.getAgeByBithday(folderItem.getContact()
				.getBday()));
		name.setText(BBSUtils.replaceBlank(folderItem.getContact()
				.getNickName()));
		picNum.setText("(" + folderItem.getVVImages().size() + "张)");
		// 根据性别加载头像
		ImageLoader.getInstance()
				.displayImage(
						folderItem.getContact().getPhoto(),
						pic_btn,
						ImageLoaderConfigers.sexOpt[folderItem.getContact()
								.getSexInt()]);
		pic_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				ContactInfoActivity.launch(mContext, folderItem.getContact());
			}
		});
		timePoint_tv.setText(BBSUtils.getTimeDurationString(folderItem
				.getImageFolder().getCreateTime()));
		int currentPraisecount = folderItem.getImageFolder().getThumbupCount();
		int currentCommentcount = folderItem.getImageFolder().getCommentCount();
		praise_tv.setText("赞  " + currentPraisecount);
		replyNum_tv.setText("评论  " + currentCommentcount);
		signText.setText(folderItem.getImageFolder().getSignature());
		folderItem.getImageFolder().showLocation(new onLocationLis() {
			@Override
			public void onResult(String location) {
				locPoint_tv.setText(location);
			}
		});
		replyNum_tv.setOnClickListener(mainClickLis);
		praise_tv.setOnClickListener(mainClickLis);
		thumbnail_list = (HorizontalListView) view
				.findViewById(R.id.thumbnail_list);
		thumbnail_list.setNeedResetLocate(false);
		thumbnailAdapter = new ThumbnailAdapter(mContext,
				folderItem.getVVImages());
		thumbnail_list.setAdapter(thumbnailAdapter);
		thumbnail_list.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				bigPicPager.setCurrentItem(position);
				thumbnailAdapter.setSelectedPosition(position);
				thumbnailAdapter.notifyDataSetChanged();
				thumbnail_list.setSelection(position);
			}
		});
	}
	private void initViewState() {
		// 回复列表适配器
		replyListViewAdapter = new CommentAdapter(mContext);
		//设置监听评论数据的监听器
		replyListViewAdapter.setItemChangedListener(new ItemChangedListener() {
			@Override
			public void ItemChanged(int count) {
				if (count == 0) {
					// 数据条目为空显示空数据布局
					Log.i(TAG, "count1~~" + count);
					comment_nodata.setVisibility(View.VISIBLE);
				} else {
					comment_nodata.setVisibility(View.GONE);
					Log.i(TAG, "count2~~" + count);
				}
			}
		});
		replyLv.getRefreshableView().setAdapter(replyListViewAdapter);
		replyListViewAdapter.addItems(folderItem.getComments());
		replyListViewAdapter.notifyDataSetChanged();
		boolean noMoreData = replyListViewAdapter.getCount() >= folderItem
				.getImageFolder().getCommentCount();
		replyLv.onPullRefreshComplete(noMoreData);
		replyLv.setOnRefreshListener(commentRefreshLis);
		setUpDots();
		// 头部图片适配器
		picPagerAdapter = new picPagerAdapter(mContext, bigPicIvList);
		bigPicPager.setAdapter(picPagerAdapter);
		bigPicPager.setOnPageChangeListener(picPagerLis);
	}
	// 初始化页数并装载图片集合
	private void setUpDots() {
		count = folderItem.getVVImages().size();
		setPagerCount(0, count);
		for (int i = 0; i < count; i++) {
			ImageView picImageView = new ImageView(mContext);
			final int index = i;
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			picImageView.setLayoutParams(lp);
			picImageView.setScaleType(ScaleType.CENTER_CROP);
			picImageView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// 跳转到图片浏览器
					ImageGalleryActivity.launch(mContext, index, folderItem);
				}
			});
			bigPicIvList.add(picImageView);
		}
	}
	// 设置页数
	private void setPagerCount(int pageIndex, int pageTotal) {
		pager_current.setText(pageIndex + 1 + "/" + pageTotal);
	}

	// 页面切换监听器
	private OnPageChangeListener picPagerLis = new OnPageChangeListener() {
		@Override
		public void onPageSelected(int pos) {
			setPagerCount(pos, count);
			ImageLoader.getInstance().displayImage(
					folderItem.getVVImages().get(pos).getPath(),
					bigPicIvList.get(pos), defaultOptions);
			thumbnailAdapter.setSelectedPosition(pos);
			thumbnailAdapter.notifyDataSetChanged();
			thumbnail_list.setSelection(pos);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	};
	// 单击事件监听器
	private OnClickListener mainClickLis = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
				case R.id.replyNum_tv: {
					TalkFragement talkFragement = TalkFragement.newFragment(
							FragmentType.dialog);
					talkFragement.show(((FragmentActivity) mContext)
							.getSupportFragmentManager(), "");
					String id = folderItem.getContact().getJid()
							+ folderItem.getImageFolder().getGid()
							+ folderItem.getImageFolder().getCreateTime();
					id = id.trim().replace("-", "");
					talkFragement.setTypeTDialog(id);
					talkFragement.setCommentedSuitableName(folderItem
							.getContact().getSuitableName());
					// 发送评论
					talkFragement.setSendmsgListenter(new SendmsgListenter() {
						@Override
						public void sendmsg(TalkFragement fragment, String str) {
							TimeflyService
									.executeCommentPG(mContext, folderItem
											.getContact().getJid(), folderItem
											.getImageFolder().getGid(),
											folderItem.getImageFolder()
													.getCreateTime(), str,
											Comment.cid_first, folderItem
													.getContact(), null);
						}
					});
					break;
				}
				case R.id.praise_tv: {
					final ImageFolder folder = folderItem.getImageFolder();
					TimeflyService.executeThumbUp(mContext, folder,
							new onPacketResult<Map<String, Object>>() {
								@Override
								public void onResult(
										Map<String, Object> result,
										boolean timeout, Start start) {
									folder.setThumbupCount(folder
											.getThumbupCount() + 1);
									folderItem.getImageFolder()
											.setThumbup(true);
									praise_tv.setSelected(true);
									praise_tv.setText("赞  "
											+ folder.getThumbupCount());
									PraiseEventBusData praisedata = new PraiseEventBusData(
											folderItem.getImageFolder()
													.getJid(), folderItem
													.getImageFolder().getGid(),
											folderItem.getImageFolder()
													.getCreateTime(), true);
									EventBus.getDefault()
											.post(new EventBusData(
													EventAction.ShareSupportChange,
													praisedata));
								}
							});
				}
			}
		}
	};

	private class picPagerAdapter extends PagerAdapter {
		private List<ImageView> dataList;

		public picPagerAdapter(Context mContext, List<ImageView> bigPicIvList) {
			super();
			this.dataList = bigPicIvList;
		}
		public void setDataList(List<ImageView> dataList) {
			this.dataList = dataList;
		}
		@Override
		public int getCount() {
			return dataList.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(View container, int position) {
			final ImageView imgView = dataList.get(position);
			((ViewPager) container).addView(dataList.get(position), 0);
			if (position == 0) {
				ImageLoader.getInstance().displayImage(
						folderItem.getVVImages().get(position).getPathThumb(),
						imgView, defaultOptions,
						new SimpleImageLoadingListener() {
							@Override
							public void onLoadingComplete(String imageUri,
									View view, Bitmap loadedImage) {
								// TODO Auto-generated method stub
								super.onLoadingComplete(imageUri, view,
										loadedImage);
								ImageLoader.getInstance().displayImage(
										folderItem.getVVImages().get(0)
												.getPath(), imgView,
										defaultOptions);
							}
						});
			} else {
				ImageLoader.getInstance().displayImage(
						folderItem.getVVImages().get(position).getPathThumb(),
						imgView, defaultOptions);
			}
			return dataList.get(position);
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(dataList.get(position));
		}
	}

	// 联网获取数据，从一个activity跳转进来，先启动界面再加载数据，在oncreate()方法里就启动此任
	public static class DownTask extends VVBaseLoadingDlg<List<CommentItem>> {
		private final IGetCommentRst rstLis;
		private final ImageFolderItem imageFolderItem;
		private Start start;

		public DownTask(Context context, ImageFolderItem folderItem,
				String next, IGetCommentRst rstLis, boolean... isShowWaitDlg) {
			super(
					new VVBaseLoadingDlgCfg(context)
							.setShowWaitingView(isShowWaitDlg.length > 0 ? isShowWaitDlg[0]
									: false));
			this.rstLis = rstLis;
			this.imageFolderItem = folderItem;
			this.start = new Start(next);
		}
		@Override
		// 执行联网的耗时操作
		protected List<CommentItem> doInBackground() {
			if (imageFolderItem != null) {
				List<CommentItem> comments = TimeflyService
						.getPhotoGroupComments(imageFolderItem.getContact()
								.getJid(), imageFolderItem.getImageFolder()
								.getGid(), imageFolderItem.getImageFolder()
								.getCreateTime(), start, 10, !BeemApplication
								.isNetworkOk());
				return comments;
			}
			return null;
		}
		@Override
		protected void onPostExecute(List<CommentItem> comment) {
			if (rstLis != null) {
				rstLis.onResult(comment, start, false);
			}
		}
		@Override
		protected void onTimeOut() {
			super.onTimeOut();
			if (rstLis != null) {
				rstLis.onResult(null, start, true);
			}
		}
	}

	public interface IGetCommentRst {
		void onResult(List<CommentItem> comments, Start start, boolean isTimeout);
	}

	private OnRefreshListener<ListView> commentRefreshLis = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<ListView> refreshView, PullType pullType) {
			downDask(false);
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView,
				PullType pullType) {
			downDask(true);
		}
	};

	private void downDask(final boolean isLoadMore) {
		new DownTask(mContext, folderItem, nextAct, new IGetCommentRst() {
			@Override
			public void onResult(List<CommentItem> comments, Start next,
					boolean isTimeout) {
				if (comments != null && !comments.isEmpty()) {
					for (CommentItem commentItem2 : comments) {
						replyListViewAdapter.addItem(commentItem2);
					}
					replyListViewAdapter.notifyDataSetChanged();
				}
				if (!next.isEnd()) {
					if (isLoadMore) {
						nextAct = next.getVal();
					} else {
						nextAct = null;
					}
				}
				boolean noMoreData = replyListViewAdapter.getCount() >= folderItem
						.getImageFolder().getCommentCount();
				replyLv.onPullRefreshComplete(noMoreData);
			}
		}).execute();
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		EventAction action = data.getAction();
		switch (action) {
			case UploadTimeflyPhotoAdd: {
				folderItem = (ImageFolderItem) data.getMsg();
				picPagerAdapter.notifyDataSetChanged();
				break;
			}
			case TimeflyImageDelete: {
				String[] imageid = (String[]) data.getMsg();
				Log.i(TAG, "~imageid[1]~" + imageid[1]);
				bigPicIvList.remove(Integer.parseInt(imageid[1]));
				folderItem.getVVImages().remove(Integer.parseInt(imageid[1]));
				count = folderItem.getVVImages().size();
				setPagerCount(0, count);
				picPagerAdapter.notifyDataSetChanged();
				bigPicPager.setCurrentItem(0);
				break;
			}
			case ShareCommentChange: {
				PraiseEventBusData pBusData = (PraiseEventBusData) data
						.getMsg();
				CommentItem commentItem = pBusData.getComment();
				replyListViewAdapter.addItem(commentItem);
				replyListViewAdapter.notifyDataSetChanged();
				folderItem.getImageFolder().setCommentCount(
						folderItem.getImageFolder().getCommentCount() + 1);
				replyNum_tv.setText("评论  "
						+ (folderItem.getImageFolder().getCommentCount()));
				break;
			}
			default:
				break;
		}
	}
}
