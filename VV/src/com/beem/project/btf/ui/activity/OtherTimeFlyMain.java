package com.beem.project.btf.ui.activity;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg.OnGetPGListResult;
import com.beem.project.btf.utils.BBSUtils;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter.YearMapItemListener;
import com.butterfly.vv.adapter.VVTimeFlyTracesAdapter;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.ContactService;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg.onGetPGDetailResult;

import de.greenrobot.event.EventBus;

public class OtherTimeFlyMain extends VVBaseFragmentActivity implements
		IEventBusAction {
	private static final String TAG = "OtherTimeFlyMain";
	private PullToProcessStateListView xTimeFlyTraceListView;
	private Contact contactwho;
	private Context mContext;
	private VVTimeFlyTracesAdapter mTimeFlyAdapter;
	private SlideHolder mSlideHolder;
	private LinearLayout friend_profile_llt;
	private ImageView others_infoImage;
	private TextView ageSex_tv, nearBy_tv, timePoint_tv, contactlistmsgperso,
			contactlistpseudo;
	private ViewGroup timeflyslider_list, timeflyslider_empty;
	private TextView ImageCount, DayCount;
	private TimeflySliderbarAdapter timeflyGroupsAdapter;

	// 给外部跳转到此Activity调用
	public static void launch(Context context, Contact contact) {
		Intent intent = new Intent(context, OtherTimeFlyMain.class);
		intent.putExtra("contactwho", contact);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_fly_traces_xlistview_other);
		// 获取intent携带的数据包
		contactwho = (Contact) getIntent().getParcelableExtra("contactwho");
		setupNavigateView();
		new VVBaseLoadingDlg<Contact>(
				new VVBaseLoadingDlgCfg(this).setShowWaitingView(true)) {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 显示好友的数据的布局
				friend_profile_llt = (LinearLayout) findViewById(R.id.friend_profile_llt);
				friend_profile_llt.setVisibility(View.GONE);
				// 隐藏自己的数据布局
			}
			@Override
			protected Contact doInBackground() {
				Contact c;
				if (LoginManager.getInstance().isLogined()) {
					c = ContactService.getInstance().getContact(
							contactwho.getJIDParsed());
				} else {
					c = contactwho;
				}
				return c;
			}
			@Override
			protected void onPostExecute(Contact result) {
				super.onPostExecute(result);
				contactwho = result;
				mContext = OtherTimeFlyMain.this;
				friend_profile_llt.setVisibility(View.VISIBLE);
				// 初始化控件及设置监听器
				initViews();
				// 填充控件数据
				initDatas();
			}
			@Override
			protected void onTimeOut() {
				super.onTimeOut();
			}
		}.execute();
		EventBus.getDefault().register(this);
	}
	@Override
	public void setupNavigateView() {
		// 设置导航条
		CustomTitleBtn spreadBtn = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		spreadBtn.setText("").setImgResource(R.drawable.more_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		spreadBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mSlideHolder.toggle();
			}
		});
		TextView textView = (TextView) findViewById(R.id.topbar_title);
		Log.i("yang", "名称" + contactwho.getSuitableName());
		textView.setText(contactwho.getSuitableName());
	}
	private void initViews() {
		mSlideHolder = (SlideHolder) findViewById(R.id.slideHolder);
		// 获取侧拉控件
		timeflyslider_list = (ViewGroup) findViewById(R.id.timeflyslider_list);
		timeflyslider_empty = (ViewGroup) findViewById(R.id.timeflyslider_empty);
		ImageCount = (TextView) findViewById(R.id.ImagesCount);
		DayCount = (TextView) findViewById(R.id.DayCount);
		// 获取下拉列表控件
		xTimeFlyTraceListView = (PullToProcessStateListView) findViewById(R.id.myxlistview);
		xTimeFlyTraceListView.getEmptydataProcessView().setEmptydataImg(
				R.drawable.timefly_stranger_nopic);
		xTimeFlyTraceListView.getEmptydataProcessView().setloadEmptyText(
				"暂无分享图片和我畅聊吧");
		xTimeFlyTraceListView.getEmptydataProcessView().setloadEmptyBtn("聊天",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						ChatActivity.launch(mContext, contactwho);
					}
				});
		// 获取好友信息控件
		setFriendProfilellt();
	}
	private void setFriendProfilellt() {
		contactlistpseudo = (TextView) findViewById(R.id.contactlistpseudo);
		others_infoImage = (ImageView) findViewById(R.id.others_info);
		others_infoImage.setOnClickListener(new OnClickListener() {
			// 跳转到详情界面
			@Override
			public void onClick(View paramView) {
				ContactInfoActivity.launch(OtherTimeFlyMain.this, contactwho);
			}
		});
		ageSex_tv = (TextView) findViewById(R.id.ageSex_tv);
		nearBy_tv = (TextView) findViewById(R.id.nearBy_tv);
		timePoint_tv = (TextView) findViewById(R.id.timePoint_tv);
		contactlistmsgperso = (TextView) findViewById(R.id.contactlistmsgperso);
	}
	private void initDatas() {
		//名称
		contactlistpseudo.setText(BBSUtils.replaceBlank(contactwho
				.getSuitableName()));
		// 加载默认图片
		contactwho.displayPhoto(others_infoImage);
		// 填充年龄
		ageSex_tv.setText(BBSUtils.getAgeByBithday(contactwho.getBday()));
		ageSex_tv.setSelected(contactwho.getSexInt() == 0);
		// 距离
		nearBy_tv.setText(LoginManager.getInstance().latlon2Distance(
				contactwho.getLat(), contactwho.getLon()));
		// 登陆时间
		timePoint_tv.setText(BBSUtils.getTimeDurationString(contactwho
				.getLogintime()));
		// 签名
		contactlistmsgperso.setText(contactwho.getSignature());
		// 设置列表适配器
		mTimeFlyAdapter = new VVTimeFlyTracesAdapter(mContext,
				xTimeFlyTraceListView.getRefreshableView(), true);
		mTimeFlyAdapter.setSlideHolder(mSlideHolder);
		xTimeFlyTraceListView.setAdapter(mTimeFlyAdapter);
		ExpandableListView yearlistview = (ExpandableListView) findViewById(R.id.yearlistview);
		timeflyGroupsAdapter = new TimeflySliderbarAdapter(mContext);
		timeflyGroupsAdapter.setYearmaplistener(new YearMapItemListener() {
			@Override
			public void updateImage(String dateTime) {
				mSlideHolder.toggle();
				loadTimeFlyImage(dateTime, 1);
			}
			@Override
			public void onDataChange(Map<String, ImageFolder> yearMap) {
				if (yearMap != null && yearMap.size() > 0) {
					timeflyslider_list.setVisibility(View.VISIBLE);
					timeflyslider_empty.setVisibility(View.GONE);
				} else {
					timeflyslider_list.setVisibility(View.GONE);
					timeflyslider_empty.setVisibility(View.VISIBLE);
				}
			}
		});
		yearlistview.setAdapter(timeflyGroupsAdapter);
		mTimeFlyAdapter.notifyDataSetChanged();
		// 实现下拉刷新，上拉加载接口
		xTimeFlyTraceListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						loadTimeFlyImage(mTimeFlyAdapter.getStartTime(), 5);
					}
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						loadTimeFlyImage(mTimeFlyAdapter.getEndTime(), 5);
					}
				});
		new GetPhotoGroupListDlg(OtherTimeFlyMain.this, contactwho.getJid(),
				new OnGetPGListResult() {
					@Override
					public void onResult(Map<String, ImageFolder> yearMap,
							boolean isTimeout) {
						if (yearMap != null && yearMap.size() > 0) {
							timeflyGroupsAdapter.addDatas(yearMap);
							// 刷新用户图片组时间列表
							// timeflyGroupsAdapter.setSlideHolder(mSlideHolder);
							// 获取五条图片详情
							loadTimeFlyImage(null, 5);
							// refresh the images and days count;
							int[] counts = updataImageCount(yearMap);
							ImageCount.setText(getResources().getString(
									R.string.timefly_imagesCountString,
									counts[0]));
							DayCount.setText(getResources()
									.getString(
											R.string.timefly_daysCountString,
											counts[1]));
						} else {
							if (isTimeout) {
								xTimeFlyTraceListView
										.setProcessState(ProcessState.TimeOut);
							} else {
								xTimeFlyTraceListView
										.setProcessState(ProcessState.Emptydata);
							}
							ImageCount.setText(getResources().getString(
									R.string.timefly_imagesCountString, 0));
							DayCount.setText(getResources().getString(
									R.string.timefly_daysCountString, 0));
						}
						timeflyGroupsAdapter.notifyDataSetChanged();
					}
				}).execute();
	}
	private int[] updataImageCount(Map<String, ImageFolder> folderTotalMap) {
		int imagesCount = 0, daysCount = 0;
		// 遍历folderTotalMap获取总天数和总图片数
		daysCount = folderTotalMap.size();
		for (Iterator<ImageFolder> it = folderTotalMap.values().iterator(); it
				.hasNext();) {
			ImageFolder folderOne = it.next();
			imagesCount = imagesCount + folderOne.getPhotoCount();
		}
		return new int[] { imagesCount, daysCount };
	}

	// 刷新界面
	private onGetPGDetailResult onPhotogroupDetailLis = new onGetPGDetailResult() {
		@Override
		public void onResult(List<ImageFolderItem> list, boolean isTimeout,
				String startTime) {
			if (list != null && !list.isEmpty()) {
				mTimeFlyAdapter.addItems(list);
				mTimeFlyAdapter.notifyDataSetChanged();
				// 选中加载的条目
				if (startTime != null) {
					for (int i = 0; i < mTimeFlyAdapter.getItems().size(); i++) {
						if (mTimeFlyAdapter.getItem(i).getImageFolder()
								.getCreateTime().equals(startTime)) {
							xTimeFlyTraceListView.getRefreshableView()
									.setSelection(i);
							break;
						}
					}
				} else {
					xTimeFlyTraceListView.getRefreshableView().setSelection(0);
				}
			} else {
				//LogUtils.e("Error:getPGDetailLis is null~~~~");
			}
			if (mTimeFlyAdapter.isEmpty()) {
				if (isTimeout) {
					xTimeFlyTraceListView.setProcessState(ProcessState.TimeOut);
				} else {
					xTimeFlyTraceListView
							.setProcessState(ProcessState.Emptydata);
				}
			} else {
				boolean noMoreData = (list == null || list.isEmpty());
				xTimeFlyTraceListView.setProcessState(ProcessState.Succeed,
						noMoreData);
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}
	/**
	 * @param contact
	 * @param params
	 * @func 刷新时光界面图片
	 */
	public void loadTimeFlyImage(String startTime, int num) {
		if (num == 1) {
			for (int i = 0; i < mTimeFlyAdapter.getItems().size(); i++) {
				// 如果列表中已存在，不需要再加载
				if (mTimeFlyAdapter.getItem(i).getImageFolder().getCreateTime()
						.equals(startTime)) {
					xTimeFlyTraceListView.getRefreshableView().setSelection(
							i
									+ xTimeFlyTraceListView
											.getRefreshableView()
											.getHeaderViewsCount());
					return;
				}
			}
		}
		List<ImageFolder> findWheres = timeflyGroupsAdapter.getImageFolders(
				startTime, num);
		GetPhotoGroupDetailDlg timeFlyAlbumLoadingDialog = new GetPhotoGroupDetailDlg(
				this, findWheres, onPhotogroupDetailLis, startTime);
		timeFlyAlbumLoadingDialog.execute();
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	public void onEventMainThread(EventBusData data) {
		EventAction action = data.getAction();
		switch (action) {
			case ShareSupportChange: {
				// 点赞数据及状态更新
				PraiseEventBusData pBusData = (PraiseEventBusData) data
						.getMsg();
				mTimeFlyAdapter.updateSupportList(pBusData,
						EventAction.ShareSupportChange);
				break;
			}
			case ShareCommentChange: {
				PraiseEventBusData pBusData = (PraiseEventBusData) data
						.getMsg();
				Log.i(TAG, "~PraiseEventBusData~~~~~~" + pBusData.toString());
				mTimeFlyAdapter.updateSupportList(pBusData,
						EventAction.ShareCommentChange);
				break;
			}
			default:
				break;
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}
