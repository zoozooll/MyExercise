/**
 * 
 */
package com.beem.project.btf.ui.fragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.CustomViewPagerAdapter;
import com.beem.project.btf.bbs.view.CustomerPageChangeLis;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.ContactListAdapter;
import com.beem.project.btf.ui.activity.AddVVContact;
import com.beem.project.btf.ui.activity.BlacklistActivity;
import com.beem.project.btf.ui.activity.ChatActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.activity.LoadnewFriend;
import com.beem.project.btf.ui.activity.OtherTimeFlyMain;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.NeighborHoodSelPopWindow;
import com.beem.project.btf.ui.views.TitleBtnChangeManger;
import com.beem.project.btf.ui.views.NeighborHoodSelPopWindow.NBSelLT;
import com.beem.project.btf.ui.views.TitleBtnChangeManger.OnTitleBtnChangeListener;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.butterfly.vv.SearchOtherUserUtilsActivity;
import com.butterfly.vv.adapter.NearbyContactsAdapter;
import com.butterfly.vv.adapter.CommonPhotoAdapter.DataStatus;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.vv.utils.CToast;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.PopupWindow.OnDismissListener;

/**
 * @author hongbo ke
 */
public class MainpagerContactFragment extends MainpagerAbstractFragment {
	private static final int LAUNCH_LOADNEWFRIEND = 2;
	private static final int LOAD_FRIEND_COMPLETE = 102;
	private ContactListAdapter contactListAdapter;
	private View rootView;
	private ViewPager viewPager_Friend;
	private CustomTitleBtn friend_friend;
	private CustomTitleBtn friend_nearby;
	private TitleBtnChangeManger headLineViewFriend;
	private CustomTitleBtn friend_nearby_selectrange;
	private TextView contacts_textView2;
	private PullToProcessStateListView distanceRefreshMoreListView;
	private TextView profile_tv_agenew;
	private ImageView head;
	private TextView userName;
	private TextView vnote;
	private ImageView zone;
	private View list;
	private NearbyContactsAdapter nearByAdapter;
	private SharedPreferences mSettings;
	private IXmppFacade mXmppFacade;
	private View network_invalid_layout;
	private TextView tvw_TimeflyLoginStatus;
	private List<Contact> coList;
	private int curPagerIndex;
	private ContactListAdapter friendAdapter;
	private VVBaseBroadCastReceiver friendModleBR = new FriendModelBroadCastReceiver(
			true);

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		regristerBroadReceiver();
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mSettings = PreferenceManager.getDefaultSharedPreferences(activity);
		mXmppFacade = BeemServiceHelper.getInstance(activity).getXmppFacade();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		} else {
			// 好友主界面容器
			rootView = inflater.inflate(R.layout.contaclist_friend_main, null);
			viewPager_Friend = (ViewPager) rootView
					.findViewById(R.id.friendMainViewPager);
			network_invalid_layout = rootView
					.findViewById(R.id.network_invalid_layout);
			tvw_TimeflyLoginStatus = (TextView) rootView
					.findViewById(R.id.tvw_TimeflyLoginStatus);
			// 初始化导航条
			setFriendHeader(rootView);
			// 设置两个页面的适配器
			List<View> childViews = setupFriendView(viewPager_Friend);
			CustomViewPagerAdapter vpAdapter = new CustomViewPagerAdapter(
					childViews, mContext);
			viewPager_Friend.setAdapter(vpAdapter);
			viewPager_Friend.setOnPageChangeListener(friendPageChangeLis);
		}
		int value = SharedPrefsUtil.getValue(mContext,
				SettingKey.CustomViewPagerIndex, 1);
		if (!LoginManager.getInstance().isLogined()) {
			value = 1;
		}
		viewPager_Friend.setCurrentItem(value);
		showLoginStatus();
		if (curPagerIndex == 0) {
			if (BeemServiceHelper.getInstance(mContext).isAuthentificated()) {
				loadFriendData();
			}
		}
		return rootView;
	}
	public void onEventMainThread(final EventBusData data) {
		switch (data.getAction()) {
			case LOGIN_TIMEOUT:
				showLoginStatus();
				break;
			case LOGIN_FAILED:
				showLoginStatus();
				break;
			case NETWORK_ACTIVE:
				showLoginStatus();
				break;
		}
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregristerBroadReceiver();
	}
	private void showLoginStatus() {
		boolean networkOK = BeemApplication.isNetworkOk();
		boolean logined = LoginManager.getInstance().isLogined();
		network_invalid_layout
				.setVisibility((!networkOK || !logined) ? View.VISIBLE
						: View.GONE);
		if (!logined) {
			tvw_TimeflyLoginStatus.setText(R.string.timefly_unlogin);
		} else if (!networkOK) {
			tvw_TimeflyLoginStatus.setText(R.string.timefly_network_failed);
		}
	}
	private void setFriendHeader(View view) {
		ArrayList<CustomTitleBtn> btns = new ArrayList<CustomTitleBtn>();
		// 搜索
		CustomTitleBtn searchbtn = (CustomTitleBtn) view
				.findViewById(R.id.leftbtn1);
		searchbtn.setVisibility(View.VISIBLE);
		searchbtn.setText(getResources().getString(R.string.friend_findperson));
		searchbtn.setImgResource(R.drawable.friend_find_selector);
		searchbtn.setViewPaddingLeft();
		searchbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext,
						SearchOtherUserUtilsActivity.class));
			}
		});
		ColorStateList csl = null;
		try {
			XmlPullParser xrp = getResources().getXml(
					R.color.topbar_textcolor_selector);
			csl = ColorStateList.createFromXml(getResources(), xrp);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 好友
		friend_friend = (CustomTitleBtn) view.findViewById(R.id.rightbtn3);
		friend_friend.setVisibility(View.VISIBLE);
		friend_friend.setText(getResources().getString(R.string.friend_friend));
		friend_friend.setTextColorSelector(R.color.topbar_textcolor_selector);
		friend_friend.setImgResource(R.drawable.friend_friends_selector);
		friend_friend.setViewPaddingRight();
		btns.add(friend_friend);
		// 附近
		friend_nearby = (CustomTitleBtn) view.findViewById(R.id.rightbtn2);
		friend_nearby.setVisibility(View.VISIBLE);
		friend_nearby.setText(getResources().getString(R.string.friend_nearby));
		friend_nearby.setTextColorSelector(R.color.topbar_textcolor_selector);
		friend_nearby.setImgResource(R.drawable.friend_nearby_selector);
		friend_nearby.setViewPaddingRight();
		btns.add(friend_nearby);
		// 附近选择下拉框
		friend_nearby_selectrange = (CustomTitleBtn) view
				.findViewById(R.id.rightbtn1);
		friend_nearby_selectrange.setVisibility(View.VISIBLE);
		friend_nearby_selectrange.setTextViewVisibility(View.GONE);
		friend_nearby_selectrange
				.setImgResource(R.drawable.friend_select_selector);
		FriendListAndDistanceLT friendListAndDistanceLT = new FriendListAndDistanceLT();
		friend_nearby_selectrange.setOnClickListener(friendListAndDistanceLT);
		friend_nearby_selectrange.setViewPaddingRight();
		// 标题
		contacts_textView2 = (TextView) view.findViewById(R.id.topbar_title);
		contacts_textView2.setVisibility(View.GONE);
		// 设置头部监听
		headLineViewFriend = new TitleBtnChangeManger(btns);
		headLineViewFriend.setOnViewChangeListener(friendTabHeadlineLis);
	}
	private List<View> setupFriendView(ViewPager viewPagerParent) {
		List<View> retVal = new ArrayList<View>();
		// 好友
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View friendView = inflater.inflate(
				R.layout.contaclist_friend_item_friend, viewPagerParent, false);
		ListView friendlist = (ListView) friendView
				.findViewById(R.id.friendlist);
		// 好友列表适配器
		View headview = setHeaderViewInRoster(null);
		friendlist.addHeaderView(headview);
		friendAdapter = getContactListAdapter("all friends");
		friendlist.setAdapter(friendAdapter);
		retVal.add(friendView);
		// 附近
		View nearbyView = inflater.inflate(
				R.layout.contaclist_friend_item_nearby, viewPagerParent, false);
		distanceRefreshMoreListView = (PullToProcessStateListView) nearbyView;
		distanceRefreshMoreListView.setListViewDivider();
		distanceRefreshMoreListView.setPullLoadEnabled(true);
		distanceRefreshMoreListView.getPreloadProcessView().setLoadinglayout(
				true);
		retVal.add(nearbyView);
		// 附近adapter
		nearByAdapter = new NearbyContactsAdapter(mContext,
				NeighborHoodType.all,
				distanceRefreshMoreListView.getRefreshableView());
		distanceRefreshMoreListView.setAdapter(nearByAdapter);
		distanceRefreshMoreListView
				.setOnRefreshListener(new OnRefreshListener<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						executeGetNeighborHood(nearByAdapter.getNbType(),
								PullType.PullDown, false);
					}
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						executeGetNeighborHood(nearByAdapter.getNbType(),
								PullType.PullUp, false);
					}
				});
		return retVal;
	}
	private View setHeaderViewInRoster(ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.guide_add_friend, parent, false);
		View mFriendGuideLayout = view.findViewById(R.id.guide_friend_layout);
		View layout_newfriend = view.findViewById(R.id.new_friend);
		View layout_addfriend = view.findViewById(R.id.add_friend);
		View ignore_friend = view.findViewById(R.id.ignore_friend);
//		ImageView status_new = (ImageView) view.findViewById(R.id.status_new);
		
		profile_tv_agenew = (TextView) view
				.findViewById(R.id.profile_tv_agenew);
		head = (ImageView) view.findViewById(R.id.avatar);
		userName = (TextView) view.findViewById(R.id.contactlistpseudo);
		vnote = (TextView) view.findViewById(R.id.contactlistmsgperso);
		zone = (ImageView) view.findViewById(R.id.vvzone);
		list = view.findViewById(R.id.list);
		// 新朋友
		layout_newfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(mContext, LoadnewFriend.class);
				startActivityForResult(intent, LAUNCH_LOADNEWFRIEND);
			}
		});
		// 添加好友
		layout_addfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, AddVVContact.class));
			}
		});
		// 黑名单
		ignore_friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(mContext, BlacklistActivity.class)
						.setAction(Constants.ACTION_BLACKROSTER));
			}
		});
		mFriendGuideLayout.setVisibility(View.VISIBLE);
		loadGendeContact();
		return view;
	}
	
	private void loadGendeContact() {
		new VVBaseLoadingDlg<Contact>(new VVBaseLoadingDlgCfg(mContext)
				.setShowWaitingView(true).setBindXmpp(true)) {
			@Override
			protected Contact doInBackground() {
				Contact contact = ContactService.getInstance().getContact(
						Constants.GENER_NUM, false, true);
				return contact;
			}
			@Override
			protected void onPostExecute(Contact contact) {
				if (contact != null) {
					list.setVisibility(View.VISIBLE);
					bindView(contact);
				} else {
					list.setVisibility(View.GONE);
				}
			}
		}.execute();
	}
	
	private void loadFriendData() {
		new VVBaseLoadingDlg<List<Contact>>(new VVBaseLoadingDlgCfg(mContext)
				.setShowWaitingView(true).setBindXmpp(true)) {
			@Override
			protected List<Contact> doInBackground() {
				coList = ContactService.getInstance().getFriendlist();
				return coList;
			}
			@Override
			protected void onPostExecute(List<Contact> result) {
				if (contactListAdapter != null && coList != null) {
					contactListAdapter.put(coList);
					contactListAdapter.notifyDataSetChanged();
				}
			}
		}.execute();
	}
	/**
	 * Get a {@link ContactListAdapter} for a group. The {@link ContactListAdapter} will be created
	 * if it is not exist.
	 * @param group the group
	 * @return the adapter
	 */
	public ContactListAdapter getContactListAdapter(String group) {
		contactListAdapter = new ContactListAdapter(mContext);
		boolean hideDisconnected = mSettings.getBoolean(
				BeemApplication.SHOW_OFFLINE_CONTACTS_KEY, false);
		contactListAdapter.setOnlineOnly(hideDisconnected);
		return contactListAdapter;
	}
	private void executeGetNeighborHood(final NeighborHoodType nbType,
			final PullType action, boolean isPreloadShow) {
		if (isPreloadShow) {
			distanceRefreshMoreListView.setProcessState(ProcessState.PreLoad);
		}
		if (viewPager_Friend.getCurrentItem() != 1) {
			viewPager_Friend.setCurrentItem(1, true);
		}
		if (nbType != nearByAdapter.getNbType()) {
			nearByAdapter.clearItems();
			nearByAdapter.setNbType(nbType);
			nearByAdapter.getStart().setStart(new Start(null, null));
			distanceRefreshMoreListView.getPullToRefreshListView()
					.setHasMoreData(true);
		}
		if (action == PullType.PullDown) {
			//TODO
			ContactServiceDlg.executeGetNeighborHood(mContext, nbType,
					new Start(null, action), 10, neighborRstLis);
		} else {
			ContactServiceDlg.executeGetNeighborHood(mContext, nbType,
					new Start(nearByAdapter.getStart(), action), 10,
					neighborRstLis);
		}
	}
	private void bindView(final Contact curContact) {
		// 设置年龄
		profile_tv_agenew
				.setText(BBSUtils.getAgeByBithday(curContact.getBday()));
		profile_tv_agenew.setSelected(curContact.getSexInt() == 0);
		/* 头像 */
		head.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ContactInfoActivity.launch(mContext, curContact);
			}
		});
		curContact.displayPhoto(head);
		/* 用户名 */
		userName.setText(curContact.getSuitableNameMix());
		/* 签名 */
		vnote.setText(curContact.getSignature());
		// 看图片组
		zone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				OtherTimeFlyMain.launch(mContext, curContact);
			}
		});
		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChatActivity.launch(mContext, curContact);
			}
		});
	}

	private OnTitleBtnChangeListener friendTabHeadlineLis = new OnTitleBtnChangeListener() {
		@Override
		public boolean onViewChange(int pageIndex) {
			if (pageIndex == 0 && !LoginManager.getInstance().isLogined()) {
				CToast.showToast(mContext, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
				ActivityController.getInstance().gotoLogin();
				return false;
			}
			viewPager_Friend.setCurrentItem(pageIndex, true);
			SharedPrefsUtil.putValue(mContext, SettingKey.CustomViewPagerIndex,
					pageIndex);
			return true;
			
		}
	};
	private OnPageChangeListener friendPageChangeLis = new CustomerPageChangeLis() {
		@Override
		public void onPageSelected(int pageIndex) {
			//LogUtils.i("friend tab switch page:" + pageIndex);
			headLineViewFriend.setSelected(pageIndex);
			if (pageIndex == 1) {
				// 附近
				curPagerIndex = 1;
 				InnerGuideHelper.showAddfriendsGuide(mContext);
				//if (nearByAdapter.getStatus() == DataStatus.empty) {
				int position = SharedPrefsUtil.getValue(mContext,
						SettingKey.neighborPos, NeighborHoodType.all.ordinal());
				executeGetNeighborHood(NeighborHoodType.values()[position],
						PullType.PullDown, true);
				//}
			} else if (pageIndex == 0) {
				curPagerIndex = 0;
				if (LoginManager.getInstance().isLogined()) {
					//if (BeemServiceHelper.getInstance(mContext.getApplicationContext()).isAuthentificated()) {
						loadFriendData();
					//}
				} else {
					CToast.showToast(mContext, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
					ActivityController.getInstance().gotoLogin();
					viewPager_Friend.setCurrentItem(1);
				}
			}
		}
	};

	class FriendListAndDistanceLT implements OnClickListener {
		public FriendListAndDistanceLT() {
		}
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 附近选择界面
				case R.id.rightbtn1: {
					if (viewPager_Friend.getCurrentItem() == 1) {
						float xoff = BBSUtils.toPixel(mContext,
								TypedValue.COMPLEX_UNIT_DIP, 0);
						float yoff = BBSUtils.toPixel(mContext,
								TypedValue.COMPLEX_UNIT_DIP, -6);
						NeighborHoodSelPopWindow popWindow = new NeighborHoodSelPopWindow(
								mContext);
						popWindow.setOnDismissListener(new OnDismissListener() {
							@Override
							public void onDismiss() {
								friend_nearby_selectrange.setSelected(false,
										false);
							}
						});
						popWindow.setNBSelLT(new NBSelLT() {
							@Override
							public void onSelect(final NeighborHoodType nbType) {
								if (nbType != nearByAdapter.getNbType()) {
									executeGetNeighborHood(nbType,
											PullType.PullDown, true);
								}
							}
						});
						popWindow.showAsDropDown(friend_nearby_selectrange,
								(int) xoff, (int) yoff);
						selectrangeIsSelected();
					} else {
						if (LoginManager.getInstance().isLogined()) {
							viewPager_Friend.setCurrentItem(1);
						} else {
							CToast.showToast(mContext, R.string.timefly_unlogin, Toast.LENGTH_SHORT);
							ActivityController.getInstance().gotoLogin();
						}
					}
					break;
				}
				default:
					break;
			}
		}
		public void selectrangeIsSelected() {
			if (friend_nearby_selectrange.isSelected()) {
				friend_nearby_selectrange.setSelected(false, false);
			} else {
				friend_nearby_selectrange.setSelected(true, false);
			}
		}
	}

	private onPacketResult<List<Contact>> neighborRstLis = new onPacketResult<List<Contact>>() {
		@Override
		public void onResult(List<Contact> result, boolean timeout, Start start) {
			if (timeout) {
				if (nearByAdapter.isEmpty()) {
					distanceRefreshMoreListView
							.setProcessState(ProcessState.TimeOut);
				}
			} else {
				if (result != null && !result.isEmpty()) {
					nearByAdapter.addItems(result);
					nearByAdapter.notifyDataSetChanged();
				}
				//LogUtils.i("start.getPullAction():" + start.getPullAction() + " nearByAdapter.getStart():"
				//						+ nearByAdapter.getStart() + " start:" + start);
				if (start.getPullAction() == PullType.PullUp
						|| nearByAdapter.getStart().isStart()) {
					// 只有在上拉或者第一次下拉时才去改start的值
					nearByAdapter.getStart().setStart(start);
				}
				if (start.isEnd()) {
					CToast.showToast(mContext, "没有更多数据", Toast.LENGTH_SHORT);
				}
				distanceRefreshMoreListView
						.setProcessState(ProcessState.Succeed);
			}
			nearByAdapter.setStatus(DataStatus.memory);
		}
	};

	@Override
	public void autoAuthentificateCompleted() {
		if (curPagerIndex == 1) {
			// 附近
			InnerGuideHelper.showAddfriendsGuide(mContext);
			if (nearByAdapter.getStatus() == DataStatus.empty
					&& (BeemServiceHelper.getInstance(mContext).isAuthentificated())) {
				int position = SharedPrefsUtil.getValue(mContext,
						SettingKey.neighborPos,
						NeighborHoodType.all.ordinal());
				executeGetNeighborHood(NeighborHoodType.values()[position],
						PullType.PullDown, true);
			}
		} else if (curPagerIndex == 0) {
			try {
				if (BeemServiceHelper.getInstance(mContext).getXmppFacade()
						.isAuthentificated()) {
					loadFriendData();
				}
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void regristerBroadReceiver() {
		// 好友广播接收器
		IntentFilter friendFilter = new IntentFilter();
		friendFilter.addAction(Constants.ACTION_FRIEND_ADD_BLACKLIST);
		friendFilter.addAction(Constants.ACTION_FRIEND_REMOVE_BLACKLIST);
		friendFilter.addAction(Constants.ACTION_FRIEND_APPROVE_FRIEND);
		friendFilter.addAction(Constants.ACTION_FRIEND_APPROVED_FRIEND);
		friendFilter.addAction(Constants.ACTION_FRIEND_UPDATE_FRIEND);
		friendFilter.addAction(Constants.ACTION_FRIEND_REMOVED_FRIEND);
		friendFilter.addAction(Constants.ACTION_FRIEND_REMOVE_FRIEND);
		friendFilter.addAction(Constants.ACTION_FRIEND_MODIFY_ALIAS);
		mContext.registerReceiver(friendModleBR, friendFilter);
	}
	private void unregristerBroadReceiver() {
		mContext.unregisterReceiver(friendModleBR);
	}
	
	// 好友模块广播接收器
	private class FriendModelBroadCastReceiver extends VVBaseBroadCastReceiver {
		private FriendModelBroadCastReceiver(boolean isLocal) {
			super(isLocal);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			// LogUtils.i("FriendModelBroadCastReceiver_action:" + action);
			// LogUtils.i("FriendModelBroadCastReceiver_isUpdateFrinedList:" +
			// isUpdateFrinedList);
			if (mXmppFacade != null) {
				friendAdapter.clear();
				for (Contact contact : ContactService.getInstance()
						.getFriendlist(true)) {
					friendAdapter.put(contact);
				}
				friendAdapter.notifyDataSetChanged();
			}
		}
	}
}
