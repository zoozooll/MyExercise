package com.beem.project.btf.ui.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences.Editor;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabContentFactory;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.LogService;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.CustomViewPagerAdapter;
import com.beem.project.btf.bbs.view.CustomerPageChangeLis;
import com.beem.project.btf.bbs.view.PreviewPoseView;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.manager.UpdateManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.service.MessageManager;
import com.beem.project.btf.service.PacketResult;
import com.beem.project.btf.service.XmppFacade;
import com.beem.project.btf.service.aidl.IXmppFacade;
import com.beem.project.btf.ui.CartoonCameraActivity;
import com.beem.project.btf.ui.CartoonCameraActivity.CameraType;
import com.beem.project.btf.ui.ContactListAdapter;
import com.beem.project.btf.ui.ContactListAdapter.ContactListType;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity.IntentKey;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper.IBeemServiceConnection;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.IShareRstLis;
import com.beem.project.btf.ui.dialog.ShareRankingFootPrintLoadingDialog.ShareType;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.entity.PraiseEventBusData;
import com.beem.project.btf.ui.loadimages.LoadImageAdapter;
import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg.OnGetPGListResult;
import com.beem.project.btf.ui.views.NeighborHoodSelPopWindow;
import com.beem.project.btf.ui.views.NeighborHoodSelPopWindow.NBSelLT;
import com.beem.project.btf.ui.views.SessionHeanLineHandler;
import com.beem.project.btf.ui.views.SimpleDilaogView;
import com.beem.project.btf.ui.views.TimeflyDueRemindView;
import com.beem.project.btf.ui.views.TitleBtnChangeManger;
import com.beem.project.btf.ui.views.TitleBtnChangeManger.OnTitleBtnChangeListener;
import com.beem.project.btf.ui.views.ToastCommon;
import com.beem.project.btf.utils.AppFileDownUtils;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DataCleanManager;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.UploadImageUtil;
import com.beem.project.btf.utils.UploadImageUtil.OnUploadProcessListener;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.NeighborHoodPacket.NeighborHoodType;
import com.btf.push.UserInfoPacket;
import com.btf.push.UserInfoPacket.UserInfoKey;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.ImageArrangementModeActivity;
import com.butterfly.vv.SearchOtherUserUtilsActivity;
import com.butterfly.vv.adapter.CommonPhotoAdapter.DataStatus;
import com.butterfly.vv.adapter.NearbyContactsAdapter;
import com.butterfly.vv.adapter.SessionsImAdapter;
import com.butterfly.vv.adapter.SessionsImAdapter.ItemChangedListener;
import com.butterfly.vv.adapter.ShareTranceCommentsAdapter;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter.YearMapItemListener;
import com.butterfly.vv.adapter.VVTimeFlyTracesAdapter;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact;
import com.butterfly.vv.db.ormhelper.bean.PhoneContact.PhoneNumWhere;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.service.ContactService.onPacketResult;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.service.dialog.ContactServiceDlg;
import com.butterfly.vv.view.timeflyView.HolderTwowayView;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg.onGetPGDetailResult;
import com.vv.image.gallery.viewer.ScrollingViewPager;

import de.greenrobot.event.EventBus;

/**
 * 所有主页面 The contact list activity displays the roster of the user.
 */
public class ContactList extends VVBaseFragmentActivity implements
		IBeemServiceConnection, IEventBusAction {
	private static final String TAG = ContactList.class.getSimpleName();
	private final List<String> mListGroup = new ArrayList<String>();
	private final VVBaseBroadCastReceiver friendModleBR = new FriendModelBroadCastReceiver(
			true);
	private final Map<String, ContactListAdapter> contactListAdapters = new HashMap<String, ContactListAdapter>();
	private VVTimeFlyTracesAdapter mTimeFlyAdapter;
	private View layout_newfriend;  
	private View layout_addfriend, ignore_friend;
	private View mFriendGuideLayout;
	// 好友adapter
	private PullToProcessStateListView messageListView;
	private PullToProcessStateListView distanceRefreshMoreListView;
	// 时光
	private TimeflySliderbarAdapter timeflyGroupsAdapter;
	private PullToProcessStateListView xTimeFlyTraceListView;
	private PullToProcessStateListView[] shareListViews;
	private View network_invalid_layout;
	// 好友附近Adapter
	private ContactListAdapter friendAdapter;
	private NearbyContactsAdapter nearByAdapter;
	// 分享Adapter数据，第一个是脚印，第二个是分享
	private ShareTranceCommentsAdapter[] shareAdapterArr;
	// 消息Adapter
	private SessionsImAdapter sessionsImAdapter;
	private Activity mActivity;
	private static final String STATE_POSITION = "STATE_POSITION";
	// 好友，附近，附近选择下拉框
	private CustomTitleBtn friend_friend, friend_nearby,
			friend_nearby_selectrange;
	private CustomTitleBtn save_info_button;
	private FriendListAndDistanceLT friendListAndDistanceLT;
	private ViewPager viewPager_Friend;
	private ImageView status_new;
	// 短消息数量
	private TextView msgBadgeViewNumIcon;
	private TabHost mTabHost;
	private ListView friendlist;
	private SlideHolder mSlideHolder;
	private XmppFacade mXmppFacade;
	private BeemServiceHelper xmppHelper;
	// 脚印排名的ViewPager
	private ScrollingViewPager footprintRankingViewPager;
	private TitleBtnChangeManger headLineViewFriend;
	private TitleBtnChangeManger headLineViewShare;
	// 默认显示的Tab
	private TabName curTabName;
	private TextView contacts_textView2;
	// pjunjun add for 上传图片
	private LoadImageAdapter loadImageadapter;
	private List<String> mScanList = new ArrayList<String>();
	private Button load_btn;
	private Button cancel_btn;
	private HolderTwowayView mImageGallery;
	// 需要上传的数据
	private String mScanedImg;
	private static final String SCANED_IMAGES = "scaned_images";
	private RelativeLayout linearlayout;
	// 上传图片的对应位置
	private int updateIndex = 0;
	// pjunjun add end
	private TextView profile_tv_agenew;
	private ImageView head;
	private TextView userName;
	private TextView vnote;
	private ImageView zone;
	private View list;
	private static final int LAUNCH_LOADNEWFRIEND = 2;
	protected static final String tag = ContactList.class.getSimpleName();
	private String info;
	public boolean isDowningAPK;
	private boolean isUploading = false;
	private ToastCommon toastCommon;
	private boolean isLocal;
	private String currentTime;
	private GalleryNavigation mMyNavigationView;
	private enum TabName {
		Friend, TimeFly, Share, Session;
	}

	// xmpp协议通知界面
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Constants.SHOW_CHOOSEUPDATE:
				String URL = (String) msg.obj;
				UpdateManager.showUpdateDialog(mActivity, mHandler, URL,
						Constants.SHOW_CHOOSEUPDATE, info);
				break;
			case Constants.SHOW_FORCEUPDATE:
				String url = (String) msg.obj;
				UpdateManager.showUpdateDialog(mActivity, mHandler, url,
						Constants.SHOW_FORCEUPDATE, info);
				break;
			case Constants.SHOW_NOINFO:
				break;
			case Constants.SHOW_WSError:
				CToast.showToast(mContext, "网络错误,请重试", Toast.LENGTH_SHORT);
				break;
			case Constants.SHOW_JSONException:
				CToast.showToast(mContext, "JSON解析错误", Toast.LENGTH_SHORT);
				break;
			case Constants.SCAN_COMPLETE:
				if (mScanList != null && mScanList.size() > 0) {
					loadImageadapter = new LoadImageAdapter(mContext, mScanList);
					// 触摸到图片组时，侧拉暂时不响应,解决事件冲突
					mImageGallery.setHolder(mSlideHolder);
					mImageGallery.setItemMargin(2);
					mImageGallery.setAdapter(loadImageadapter);
				} else {
					xTimeFlyTraceListView.getRefreshableView()
							.removeHeaderView(linearlayout);
				}
				// LogUtils.i("scan local pitures complete!");
				break;
			case Constants.UPLOAD_INIT_PROCESS:
				CToast.showToast(mContext, "图片不存在，请重试！", Toast.LENGTH_SHORT);
				isUploading = false;
				break;
			case Constants.UPLOAD_IN_PROCESS:
				// pos:当前正上传的图片index
				int reponsecode = msg.arg1;
				String responseMessage = (String) msg.obj;
				// LogUtils.i("----UPLOAD_INIT_PROCESS ---responseMessage=" +
				// responseMessage + "--reponsecode="
				// + reponsecode);
				if (reponsecode == Constants.UPLOAD_SUCCESS_CODE) {
					saveLoadImageInfo(responseMessage);
				} else if (reponsecode == Constants.UPLOAD_SERVER_ERROR_CODE) {
					// int index = Integer.parseInt(responseMessage);
					// removeUnloadedImages(index);
					// mTimeFlyAdapter.notifyDataSetChanged();
				}
				break;
			case Constants.UPLOAD_SERVER_ERROR_CODE:
				int count = msg.arg1;
				// LogUtils.i("----UPLOAD_SERVER_ERROR_CODE - count=" + count);
				ImageFolderItem folderItem = mTimeFlyAdapter
						.getItem(updateIndex);
				ImageFolder imagefolder = folderItem.getImageFolder();
				// LogUtils.e("--jj FolderCount=" +
				// imagefolder.getField(DBKey.photoCount));
				if (count == 0) {
					// 删除图片组
					imagefolder.setField(DBKey.photoCount, 0);
					mTimeFlyAdapter.removeItem(updateIndex);
				} else {
					String jid = imagefolder.getJid();
					String gid = imagefolder.getGid();
					String createTime = imagefolder.getCreateTime();
					imagefolder.setField(DBKey.jid, jid);
					imagefolder.setField(DBKey.gid, gid);
					imagefolder.setField(DBKey.createTime, createTime);
					imagefolder.setField(DBKey.photoCount, count);
					imagefolder.saveToDatabaseAsync();
					// LogUtils.e("--jj mTimeFlyAdapter jid=" + jid + "-- gid="
					// + gid + "--createTime=" + createTime);
				}
				mTimeFlyAdapter.notifyDataSetChanged();
				timeflyGroupsAdapter.changeData(folderItem.getImageFolder()
						.getCreateTime(), folderItem.getImageFolder());
				timeflyGroupsAdapter.notifyDataSetChanged();
				isUploading = false;
				break;
			case Constants.UPLOAD_SUCCESS_CODE:
				// LogUtils.i("----UPLOAD_SUCCESS_CODE --=");
				mTimeFlyAdapter.notifyDataSetChanged();
				isUploading = false;
				break;
			case AppFileDownUtils.MSG_DOWNING:
				CToast.showToast(mContext, "正在下载...", Toast.LENGTH_SHORT);
				break;
			case AppFileDownUtils.MSG_FINISH:
				CToast.showToast(mContext, "下载完成", Toast.LENGTH_SHORT);
				break;
			case AppFileDownUtils.MSG_FAILURE:
				CToast.showToast(mContext, "下载失败,请重试", Toast.LENGTH_SHORT);
				break;
			default:
				Object[] data = (Object[]) msg.obj;
				final ImageFolder folder = (ImageFolder) data[0];
				final String time = (String) data[1];
				final BBSCustomerDialog blurDlg = BBSCustomerDialog
						.newInstance(mContext, R.style.blurdialog);
				TimeflyDueRemindView remindview = new TimeflyDueRemindView(
						mContext, true);
				remindview.setText(time);
				remindview.setBtnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						blurDlg.dismiss();
						ShareRankingActivity.launch(mContext, LoginManager
								.getInstance().getJidParsed(), folder.getGid(),
								folder.getCreateTime());
						ImageFolderNotify notifyDB = new ImageFolderNotify();
						notifyDB.setField(DBKey.jid, LoginManager.getInstance()
								.getJidParsed());
						notifyDB.setField(DBKey.gid, folder.getGid());
						notifyDB.setField(DBKey.createTime,
								folder.getCreateTime());
						notifyDB.setField(DBKey.notify_valid, Valid.close.val);
						notifyDB.saveToDatabaseAsync();
						// 更新界面数据
						EventBusData data = new EventBusData(
								EventAction.CheckTimeflyNotify,
								new Object[] { folder.getCreateTime(),
										Valid.close.val });
						EventBus.getDefault().post(data);
					}
				});
				blurDlg.setContentView(remindview.getmView());
				blurDlg.getWindow().setType(
						WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
				blurDlg.setCancelable(true);
				blurDlg.show();
				break;
			}
		}
	};

	private class RemoveFolderItemTask extends
			AsyncTask<Void, Integer, Boolean> {
		private String responseMsg;

		public RemoveFolderItemTask(String responseMsg) {
			super();
			this.responseMsg = responseMsg;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			// LogUtils.e("--jj mTimeFlyAdapter00 size=" +
			// mTimeFlyAdapter.getItems().size());
			ImageFolderItem folderItem = mTimeFlyAdapter.getItem(updateIndex);
			ArrayList<VVImage> vvimages = folderItem.getVVImages();
			String[] listIndex = responseMsg.split(",");
			int size = listIndex.length;
			for (int i = 0; i < size; i++) {
				int index = Integer.parseInt(listIndex[i]);
				// 清除图片缓存
				ImageLoader.getInstance().getMemoryCache()
						.remove(vvimages.get(index - 1).getPathThumb());
				ImageLoader.getInstance().getDiskCache()
						.remove(vvimages.get(index - 1).getPathThumb());
				vvimages.get(index - 1).setImageisLoading(false);
				vvimages.remove(index - 1);
			}
			int newFolderCount = vvimages.size();
			Message msg = Message.obtain();
			msg.what = Constants.UPLOAD_SERVER_ERROR_CODE;
			msg.arg1 = newFolderCount;
			mHandler.sendMessage(msg);
			// LogUtils.e("--jj RemoveFolderItemTask doinbackground exit");
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CToast.showToast(mContext, "连接服务器失败，请重试！", Toast.LENGTH_SHORT);
			removeTempImages();
		}
	}

	private class SaveFolderItemTask extends AsyncTask<Void, Integer, Boolean> {
		private String responseMsg;

		public SaveFolderItemTask(String responseMsg) {
			super();
			this.responseMsg = responseMsg;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			saveLoadImageInfo(responseMsg);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			ImageFolder imagefolder = mTimeFlyAdapter.getItem(updateIndex)
					.getImageFolder();
			ArrayList<VVImage> vvimages = mTimeFlyAdapter.getItem(updateIndex)
					.getVVImages();
			String[] responseQueen = responseMsg.split(",");
			imagefolder.setField(DBKey.gid, responseQueen[1]);
			imagefolder.setField(DBKey.photoCount, vvimages.size());
			imagefolder.saveToDatabaseAsync();
		}
	}

	private class UploadFolderItemTask extends
			AsyncTask<Void, Integer, Boolean> {
		private String responseMsg;

		public UploadFolderItemTask(String responseMsg) {
			super();
			this.responseMsg = responseMsg;
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			saveLoadImageInfo(responseMsg);
			mHandler.sendEmptyMessage(Constants.UPLOAD_SUCCESS_CODE);
			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			CToast.showToast(mContext, "上传成功", Toast.LENGTH_SHORT);
			// 移除缓存文件
			removeTempImages();
		}
	}

	private void saveLoadImageInfo(String info) {
		String responseMessage = info;
		String[] responseQueen = responseMessage.split(",");
		int index = Integer.parseInt(responseQueen[0]);
		ArrayList<VVImage> vvimages = mTimeFlyAdapter.getItem(updateIndex)
				.getVVImages();
		ImageFolder imagefolder = mTimeFlyAdapter.getItem(updateIndex)
				.getImageFolder();
		imagefolder.setField(DBKey.album_url, responseQueen[6]);
		VVImage vvImage = vvimages.get(index);
		ImageLoader.getInstance().getMemoryCache()
				.remove(vvImage.getPathThumb());
		ImageLoader.getInstance().getDiskCache().remove(vvImage.getPathThumb());
		// 保存上传的图片信息
		vvImage.setField(DBKey.jid, imagefolder.getJid());
		vvImage.setField(DBKey.gid, responseQueen[1]);
		vvImage.setField(DBKey.pid, responseQueen[2]);
		vvImage.setField(DBKey.path, responseQueen[3]);
		vvImage.setField(DBKey.pathThumb, responseQueen[4]);
		vvImage.setField(DBKey.createTime, responseQueen[5]);
		vvImage.setField(DBKey.isLoading, false);
		vvImage.saveToDatabaseAsync();
	}

	private void removeTempImages() {
		if (!android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment
				.getExternalStorageState())) {
			return;
		}
		File updateDir = BBSUtils.getAppCacheDir(BeemApplication.getContext(),
				"uploadTempFile");
		if (updateDir.exists()) {
			if (updateDir.isFile()) {
				updateDir.delete();
			} else if (updateDir.isDirectory()) {
				File files[] = updateDir.listFiles();
				for (int i = 0; i < files.length; i++) {
					files[i].delete();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle saveBundle) {
		super.onCreate(saveBundle);
		xmppHelper = new BeemServiceHelper(this, this);
		mActivity = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.custom_tabs_contact_list);
		setupTabHost(saveBundle);
		update(AppProperty.getInstance().UPDATE_URL);
		Intent intent = getIntent();
		Bundle extras = intent.getExtras();
		if (extras != null) {
			String[] path = (String[]) extras
					.getCharSequenceArray("VCard_path");
			String big_path = path[0];
			String small_path = path[1];
			UserInfoPacket modifyMap = new UserInfoPacket();
			modifyMap.setField(UserInfoKey.big, big_path);
			modifyMap.setField(UserInfoKey.small, small_path);
			modifyContactInfo(modifyMap);
			EventBus.getDefault().post(
					new EventBusData(EventAction.CloseFrontActivity, null));
		}
		EventBus.getDefault().register(this);
		// 每次进入Contactlist时进行清除缓存一次动作
		DataCleanManager.clearCache();
	}

	private void update(final String url) {
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				Message msg = Message.obtain();
				try {
					String[] udp = UpdateManager.update(url);
					String version = udp[0];
					String url = udp[1];
					Log.i(TAG, "url" + url);
					info = udp[2];
					String versionName = BBSUtils.getVersionName();
					boolean isDowningState = SharedPrefsUtil.getValue(mContext,
							SettingKey.IsDowningState, false);
					if (version != null) {
						if (version.compareTo(versionName) > 0) {
							if (!isDowningState) {
								if (!versionName.substring(0, 1).equals(
										version.substring(0, 1))) {
									msg.what = Constants.SHOW_FORCEUPDATE;
								} else {
									msg.what = Constants.SHOW_CHOOSEUPDATE;
								}
								msg.obj = url;
							} else {
								msg.what = Constants.SHOW_ISDOWNING;
							}
						} else {
							msg.what = Constants.SHOW_NOINFO;
						}
					} else {
						msg.what = Constants.SHOW_NOINFO;
					}
				} catch (WSError e) {
					msg.what = Constants.SHOW_WSError;
				} catch (JSONException e) {
					msg.what = Constants.SHOW_JSONException;
				} finally {
					mHandler.sendMessage(msg);
				}
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		xmppHelper.bindBeemService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mXmppFacade != null && mXmppFacade.getMessageManager() != null) {
			mXmppFacade.getMessageManager().checkTimeflyNotifyDlg(
					ContactList.this);
		}
	}

	private void setupTabHost(Bundle saveBundle) {
		mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup();
		final List<String> tabNames = Arrays.asList(
				getResources().getString(R.string.friends), getResources()
						.getString(R.string.time_machine), getResources()
						.getString(R.string.vvim_share), getResources()
						.getString(R.string.information));
		mTabHost.setOnTabChangedListener(new OnTabChangeListener() {
			@Override
			public void onTabChanged(String tabId) {
				curTabName = TabName.values()[tabNames.indexOf(tabId)];
				if (curTabName == TabName.Session) {
					msgBadgeViewNumIcon.setVisibility(View.GONE);
					if (mXmppFacade != null
							&& mXmppFacade.getMessageManager() != null) {
						mXmppFacade.getMessageManager().checkMessages();
					}
				} else if (curTabName == TabName.Friend) {
				} else if (curTabName == TabName.TimeFly) {
					InnerGuideHelper.showTimeflyGuide(ContactList.this);
				} else if (curTabName == TabName.Share) {
					InnerGuideHelper.showBBSGuide(ContactList.this);
				}
				if (curTabName != TabName.Session) {
					// 切换模块时退出消息界面的选择
					if (sessionsImAdapter != null
							&& sessionsImAdapter.isDelState()) {
						sessionsImAdapter.Cancel();
					}
				}
			}
		});
		// 好友
		setupFriendTab(new TextView(this), tabNames.get(0));
		// 时光
		setupTimeFlyTracesTab(new TextView(this), tabNames.get(1));
		// 分享
		setupShareTab(new TextView(this), tabNames.get(2));
		// 消息
		setupSessionsTab(new TextView(this), tabNames.get(3));
		curTabName = saveBundle != null ? TabName.values()[saveBundle.getInt(
				STATE_POSITION, TabName.TimeFly.ordinal())] : TabName.TimeFly;
		mTabHost.setCurrentTab(curTabName.ordinal());
	}

	// 消息
	private void setupSessionsTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		ImageView imageViewttt = (ImageView) tabview
				.findViewById(R.id.imageViewttt);
		// 设置消息图标
		imageViewttt.setImageResource(R.drawable.news_selector);
		// 有消息的提醒标示
		msgBadgeViewNumIcon = (TextView) tabview
				.findViewById(R.id.main_tab_new_tv);
		msgBadgeViewNumIcon.setVisibility(View.GONE);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						View view = LayoutInflater
								.from(getApplicationContext()).inflate(
										R.layout.gps_search_grid, null);
						messageListView = (PullToProcessStateListView) view
								.findViewById(R.id.sessionlist);
						messageListView.setPullRefreshEnabled(false);
						messageListView.setPullLoadEnabled(false);
						messageListView.setListViewDivider();
						messageListView.getEmptydataProcessView()
								.setEmptydataImg(
										R.drawable.timefly_session_nopic);
						messageListView.getEmptydataProcessView()
								.setloadEmptyBtn("附近人", new OnClickListener() {
									@Override
									public void onClick(View v) {
										// 切换到附近的人
										mTabHost.setCurrentTab(TabName.Friend
												.ordinal());
										headLineViewFriend.performClick(1);
									}
								});
						messageListView.getEmptydataProcessView()
								.setloadEmptyText("你还没有任何消息记录,看看附近的人吧...");
						sessionsImAdapter = new SessionsImAdapter(mContext,
								messageListView.getRefreshableView());
						messageListView.setAdapter(sessionsImAdapter);
						if (mXmppFacade != null
								&& mXmppFacade.getMessageManager() != null) {
							sessionsImAdapter.addItems(mXmppFacade
									.getMessageManager().getAllMessages());
						}
						sessionsImAdapter
								.setItemChangedListener(new ItemChangedListener() {
									@Override
									public void ItemChanged(int count) {
										if (count == 0) {
											// 数据条目为空显示空数据布局
											messageListView
													.setProcessState(ProcessState.Emptydata);
										} else {
											messageListView
													.setProcessState(ProcessState.Succeed);
										}
									}
								});
						sessionsImAdapter.notifyDataSetChanged();
						// 添加头部及头部监听器
						new SessionHeanLineHandler(mActivity, view,
								sessionsImAdapter);
						return view;
					}
				});
		mTabHost.addTab(setContent);
	}

	// 分享
	private void setupShareTab(final View view, final String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		ImageView imageViewttt = (ImageView) tabview
				.findViewById(R.id.imageViewttt);
		// 设置分享图标
		imageViewttt.setImageResource(R.drawable.rss_selector);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						View view = LayoutInflater
								.from(getApplicationContext()).inflate(
										R.layout.comments_photo_traces2, null);
						mMyNavigationView = (GalleryNavigation) view.findViewById(R.id.share_navigation_view);
						mMyNavigationView.setBtnLeftHide();
						mMyNavigationView
								.setBtnRightIcon(R.drawable.shared_refresh_selector);
						mMyNavigationView
								.setCameraListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										int currentItem = footprintRankingViewPager
												.getCurrentItem();
										shareListViews[currentItem]
												.doPullRefreshing(true, 200);
									}
								});
						mMyNavigationView
								.setBtnLeftIcon(R.drawable.share_activity_close_selector);
						mMyNavigationView.inflateCenter(headLineLis);
						mMyNavigationView.setCenterText("分享榜","周边");
						mMyNavigationView.setTopbarTab(0);
						// 内容部分
						footprintRankingViewPager = (ScrollingViewPager) view
								.findViewById(R.id.scrollLyt);
						LayoutInflater inflater = (LayoutInflater) mActivity
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						ArrayList<View> shareContentView = new ArrayList<View>();
						for (int i = 0; i < 2; i++) {
							View footprintView = inflater
									.inflate(
											R.layout.comments_photo_traces2_viewpageritem,
											null);
							shareContentView.add(footprintView);
						}
						CustomViewPagerAdapter mScrollAdapter = new CustomViewPagerAdapter(
								shareContentView, mActivity);
						footprintRankingViewPager.setAdapter(mScrollAdapter);
						footprintRankingViewPager
								.setOnPageChangeListener(footprintRankingPagerLis);
						// TODO
						shareListViews = new PullToProcessStateListView[shareContentView
								.size()];
						shareAdapterArr = new ShareTranceCommentsAdapter[shareContentView
								.size()];
						for (int i = 0; i < shareContentView.size(); i++) {
							View contentView = shareContentView.get(i);
							shareListViews[i] = (PullToProcessStateListView) contentView
									.findViewById(R.id.myxlistview);
							shareListViews[i].setListViewDivider();
							shareAdapterArr[i] = new ShareTranceCommentsAdapter(
									mActivity, ShareType.values()[i],
									shareListViews[i].getRefreshableView());
							shareAdapterArr[i]
									.setScollingViewPager(footprintRankingViewPager);
							shareListViews[i].setAdapter(shareAdapterArr[i]);
							shareListViews[i]
									.setOnScrollListener(new PauseOnScrollListener(
											ImageLoader.getInstance(), true,
											true));
							shareListViews[i].getEmptydataProcessView()
									.setEmptydataImg(
											R.drawable.timefly_user_nopic);
							// 列表刷新
							final int index = i;
							shareListViews[i]
									.setOnRefreshListener(new OnRefreshListener<ListView>() {
										@Override
										public void onPullDownToRefresh(
												PullToRefreshBase<ListView> refreshView,
												PullType pullType) {
											new ShareRankingFootPrintLoadingDialog(
													mContext, ShareType
															.values()[index],
													new Start(null),
													shareResultLis,
													PullType.PullDown)
													.execute();
										}

										@Override
										public void onPullUpToRefresh(
												PullToRefreshBase<ListView> refreshView,
												PullType pullType) {
											Start start = new Start(
													shareAdapterArr[index]
															.getStart()
															.getVal());
											new ShareRankingFootPrintLoadingDialog(
													mContext, ShareType
															.values()[index],
													start, shareResultLis,
													PullType.PullUp).execute();
										}
									});
						}
						// 脚印排名列表
						new ShareRankingFootPrintLoadingDialog(mContext,
								ShareType.TopN, shareAdapterArr[0].getStart(),
								shareResultLis, PullType.PullDown).execute();
						return view;
					}
				});
		mTabHost.addTab(setContent);
	}

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
			// Log.v(TAG, "~~~~~footprintRankingPagerLis~~~~onPageSelected");
			ShareSwitchPage(pageIndex);
			mMyNavigationView.setTopbarTab(pageIndex);
		}
	};
	// 分享界面的广播接收器
	private VVBaseBroadCastReceiver shareModelReceiver = new VVBaseBroadCastReceiver(
			true) {
		@Override
		public void onReceive(Context context, Intent intent) {
			// 这里处理点赞广播
		}
	};
	// 消息模块接收器
	private VVBaseBroadCastReceiver sessionModelReceiver = new VVBaseBroadCastReceiver(
			true) {
		@Override
		public void onReceive(Context context, Intent intent) {
			Item item = intent.getParcelableExtra("item");
			// LogUtils.i("received message:" + item.getMessage() + " jid:" +
			// item.getJidParsed() + " unReadMsgCount:"
			// + item.getUnReadMsgCount());
			if (curTabName != TabName.Session && !item.isLocal()) {
				msgBadgeViewNumIcon.setVisibility(View.VISIBLE);
			} else {
				mXmppFacade.getMessageManager().checkMessages();
			}
			if (sessionsImAdapter != null) {
				sessionsImAdapter.addItem(item);
				sessionsImAdapter.notifyDataSetChanged();
			}
		}
	};

	// 时光模块接收器
	private void setupTimeFlyTracesTab(View view, String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		// 获取单个标签页
		ImageView imageViewttt = (ImageView) tabview
				.findViewById(R.id.imageViewttt);
		// 设置时光图标
		imageViewttt.setImageResource(R.drawable.time_selector);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					private View view = LayoutInflater.from(mContext).inflate(
							R.layout.time_fly_traces_xlistview, null);
					private ViewGroup timeflyslider_list = (ViewGroup) view
							.findViewById(R.id.timeflyslider_list);
					private ViewGroup timeflyslider_empty = (ViewGroup) view
							.findViewById(R.id.timeflyslider_empty);
					private View CoverImageView_Gallery = view
							.findViewById(R.id.CoverImageView_Gallery);
					// 图片张数和天数
					private TextView ImageCount, DayCount;

					@Override
					public View createTabContent(String tag) {
						ImageCount = (TextView) view
								.findViewById(R.id.ImagesCount);
						DayCount = (TextView) view.findViewById(R.id.DayCount);
						ExpandableListView yearlistview = (ExpandableListView) view
								.findViewById(R.id.yearlistview);
						timeflyGroupsAdapter = new TimeflySliderbarAdapter(
								ContactList.this);
						timeflyGroupsAdapter
								.setYearmaplistener(new YearMapItemListener() {
									@Override
									public void updateImage(String dateTime) {
										mSlideHolder.toggle();
										loadTimeFlyImage(dateTime, 1);
									}

									@Override
									public void onDataChange(
											Map<String, ImageFolder> yearMap) {
										if (yearMap != null
												&& yearMap.size() > 0) {
											timeflyslider_list
													.setVisibility(View.VISIBLE);
											timeflyslider_empty
													.setVisibility(View.GONE);
										} else {
											timeflyslider_list
													.setVisibility(View.GONE);
											timeflyslider_empty
													.setVisibility(View.VISIBLE);
										}
										// refresh the images and days count;
										int[] counts = updataImageCount(yearMap);
										ImageCount
												.setText(getResources()
														.getString(
																R.string.timefly_imagesCountString,
																counts[0]));
										DayCount.setText(getResources()
												.getString(
														R.string.timefly_daysCountString,
														counts[1]));
									}
								});
						yearlistview.setAdapter(timeflyGroupsAdapter);
						// 网络状态栏
						network_invalid_layout = view
								.findViewById(R.id.network_invalid_layout);
						network_invalid_layout
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										Intent intent = new Intent(
												android.provider.Settings.ACTION_WIFI_SETTINGS);
										startActivity(intent);
									}
								});
						/*
						 * network_invalid_layout.getViewTreeObserver().
						 * addOnGlobalLayoutListener(new
						 * OnGlobalLayoutListener() {
						 * 
						 * @Override public void onGlobalLayout() {
						 * network_invalid_layout
						 * .getViewTreeObserver().removeGlobalOnLayoutListener
						 * (this); LinearLayout.LayoutParams params =
						 * (LayoutParams)
						 * network_invalid_layout.getLayoutParams();
						 * params.bottomMargin = BeemApplication.isOff() ? 0 :
						 * -network_invalid_layout.getHeight(); } });
						 */
						network_invalid_layout.setVisibility(BeemApplication
								.isOff() ? View.VISIBLE : View.GONE);
						// 时光相机
						View beautifyCamera = view
								.findViewById(R.id.CoverImageView_Beautify);
						beautifyCamera
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										int isLibtimeout = SharedPrefsUtil
												.getValue(mContext,
														SettingKey.LibTimeOut,
														CartoonLib.SUCESS);
										if (isLibtimeout == CartoonLib.SUCESS) {
											CartoonCameraActivity.launch(
													mContext,
													CameraType.TIME.ordinal());
										} else {
											noteLibTimeout();
										}
									}
								});
						// 娱乐相机
						View newsCamera = view
								.findViewById(R.id.CoverImageView_NewsCamera);
						newsCamera.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								// TODO Auto-generated method stub
								NewsCameraEditorActivity.launch(mContext);
							}
						});
						// 手绘相机
						View cartoonCamera = view
								.findViewById(R.id.CoverImageView_Cartoon);
						cartoonCamera.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								int istimeout = CartoonLib.SUCESS;
								if (true) {
									CartoonCameraActivity.launch(mContext,
											CameraType.CARTOON.ordinal());
								} else {
									noteLibTimeout2();
								}
							}
						});
						// 相册库
						mSlideHolder = (SlideHolder) view
								.findViewById(R.id.slideHolder);
						// 加载图片
						loadImagesFirstTime();
						CoverImageView_Gallery
								.setOnClickListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										// 跳转到相册库
										Intent intent = new Intent(
												ContactList.this,
												GalleryActivity.class);
										// intent.setAction("android.intent.action.vv.camera.photo.main");
										// intent.putExtra("CameraGalleryType",
										// CameraGalleryType.Time.ordinal());
										int uploadpicMaxNum = Constants.uploadpicMaxNum;
										try {
											ImageFolder todayFolder = ImageFolderItemManager
													.getInstance()
													.getImageFolderNow(
															LoginManager
																	.getInstance()
																	.getJidParsed());
											if (todayFolder != null) {
												uploadpicMaxNum -= Integer.valueOf(todayFolder
														.getPhotoCount());
											}
										} catch (Exception e) {
											e.printStackTrace();
										}
										intent.putExtra(
												GalleryActivity.GALLERY_CHOOSE_MAX,
												uploadpicMaxNum);
										intent.putExtra(
												GalleryActivity.GALLERY_FROM_CAMERA,
												true);
										intent.putExtra(
												GalleryActivity.GALLERY_CROP,
												false);
										startActivity(intent);
									}
								});
						// 设置导航条颜色等值
						CustomTitleBtn tooglebtn = (CustomTitleBtn) view
								.findViewById(R.id.leftbtn1);
						tooglebtn.setVisibility(View.VISIBLE);
						tooglebtn.setViewPaddingLeft();
						tooglebtn.setTextViewVisibility(View.GONE);
						// 侧拉按钮图片
						tooglebtn.setImgResource(R.drawable.more_selector);
						tooglebtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								mSlideHolder.toggle();
							}
						});
						// 设置按钮图片
						CustomTitleBtn settingBtn = (CustomTitleBtn) view
								.findViewById(R.id.rightbtn1);
						settingBtn.setVisibility(View.VISIBLE);
						settingBtn.setTextViewVisibility(View.GONE);
						settingBtn.setViewPaddingRight();
						settingBtn.setImgResource(R.drawable.setting_selector);
						settingBtn.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(mTabHost
										.getContext(), MySettings.class);
								startActivity(intent);
							}
						});
						TextView textView = (TextView) view
								.findViewById(R.id.topbar_title);
						textView.setVisibility(View.VISIBLE);
						textView.setText(getString(R.string.time_machine));
						xTimeFlyTraceListView = (PullToProcessStateListView) view
								.findViewById(R.id.myxlistview);
						// 添加头部视图
						xTimeFlyTraceListView.getRefreshableView()
								.addHeaderView(linearlayout);
						xTimeFlyTraceListView.getEmptydataProcessView()
								.setEmptydataImg(R.drawable.timefly_user_nopic);
						xTimeFlyTraceListView.getEmptydataProcessView()
								.setloadEmptyBtn("", new OnClickListener() {
									@Override
									public void onClick(View v) {
										// 打开相册库
										CoverImageView_Gallery.performClick();
									}
								});
						xTimeFlyTraceListView.getTimeoutProcessView()
								.setOnReloadListener(new OnClickListener() {
									@Override
									public void onClick(View v) {
										loadTimeFlyImage(null, 5);
									}
								});
						// TODO
						/*
						 * mTimeFlyAdapter = new
						 * VVTimeFlyTracesAdapter(mActivity,
						 * xTimeFlyTraceListView.getRefreshableView(), false);
						 */
						mTimeFlyAdapter = new VVTimeFlyTracesAdapter(mActivity,
								xTimeFlyTraceListView.getRefreshableView(),
								false, mHandler);
						// 触摸到图片组时，侧拉暂时不响应,解决事件冲突
						mTimeFlyAdapter.setSlideHolder(mSlideHolder);
						xTimeFlyTraceListView.setAdapter(mTimeFlyAdapter);
						// 列表刷新
						xTimeFlyTraceListView
								.setOnRefreshListener(new OnRefreshListener<ListView>() {
									@Override
									public void onPullDownToRefresh(
											PullToRefreshBase<ListView> refreshView,
											PullType pullType) {
										loadTimeFlyImage(
												mTimeFlyAdapter.getStartTime(),
												5);
									}

									@Override
									public void onPullUpToRefresh(
											PullToRefreshBase<ListView> refreshView,
											PullType pullType) {
										loadTimeFlyImage(
												mTimeFlyAdapter.getEndTime(), 5);
									}
								});
						// 侧拉菜单
						new GetPhotoGroupListDlg(mActivity, LoginManager
								.getInstance().getJidParsed(),
								new OnGetPGListResult() {
									@Override
									public void onResult(
											Map<String, ImageFolder> yearMap,
											boolean isTimeout) {
										if (yearMap != null
												&& yearMap.size() > 0) {
											timeflyGroupsAdapter
													.addDatas(yearMap);
											// 获取五条图片详情
											loadTimeFlyImage(null, 5);
										} else {
											if (isTimeout) {
												xTimeFlyTraceListView
														.setProcessState(ProcessState.TimeOut);
											} else {
												xTimeFlyTraceListView
														.setProcessState(ProcessState.Emptydata);
											}
										}
										timeflyGroupsAdapter
												.notifyDataSetChanged();
									}
								}).execute();
						return view;
					}
				});
		mTabHost.addTab(setContent);
	}

	private void noteLibTimeout() {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				mContext, R.style.blurdialog);
		SimpleDilaogView simpleDilaogView = new SimpleDilaogView(mContext);
		// 设置标题
		simpleDilaogView.setTitle("核心处理库已过期请下载新版本!");
		simpleDilaogView.setPositiveButton("确定", new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(simpleDilaogView.getmView());
		blurDlg.show();
	}

	private void noteLibTimeout2() {
		final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
				mContext, R.style.blurdialog);
		PreviewPoseView pview = new PreviewPoseView(mContext,
				new String[] { "" });
		// 设置标题
		pview.setCloseListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				blurDlg.dismiss();
			}
		});
		blurDlg.setContentView(pview.getView());
		blurDlg.show();
	}

	private int[] updataImageCount(Map<String, ImageFolder> folderTotalMap) {
		if (folderTotalMap == null) {
			return new int[] { 0, 0 };
		}
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

	private void loadImagesFirstTime() {
		linearlayout = (RelativeLayout) LayoutInflater.from(mContext).inflate(
				R.layout.loading_images, null);
		load_btn = (Button) linearlayout.findViewById(R.id.loading_btn);
		cancel_btn = (Button) linearlayout.findViewById(R.id.cancel_btn);
		mImageGallery = (HolderTwowayView) linearlayout
				.findViewById(R.id.loading_images);
		mScanedImg = mSettings.getString(SCANED_IMAGES, "");
		load_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存已经上传的图片
				ArrayList<String> listimagePath = (ArrayList<String>) loadImageadapter
						.getListImagePath();
				String loadedImages = mScanedImg + mScanList.toString();
				Editor editor = mSettings.edit();
				editor.putString(SCANED_IMAGES, loadedImages);
				editor.commit();
				Toast.makeText(ContactList.this, "上传", Toast.LENGTH_SHORT)
						.show();
				Intent intent2 = new Intent(ContactList.this,
						ShareChangeAlbumAuthorityActivity.class);
				intent2.putStringArrayListExtra(IntentKey.LISTIMAGEPATH,
						listimagePath);
				startActivityForResult(intent2, 100);
				xTimeFlyTraceListView.getRefreshableView().removeHeaderView(
						linearlayout);
			}
		});
		cancel_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存取消上传的图片
				String loadedImages = mScanedImg + mScanList.toString();
				Editor editor = mSettings.edit();
				editor.putString(SCANED_IMAGES, loadedImages);
				editor.commit();
				xTimeFlyTraceListView.getRefreshableView().removeHeaderView(
						linearlayout);
			}
		});
		getImages();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// LogUtils.i("暂无外部存储,不能扫描手机图片");
			return;
		}
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext
						.getContentResolver();
				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri,
						new String[] { MediaColumns.DATA, MediaColumns.SIZE },
						MediaColumns.MIME_TYPE + "=? or "
								+ MediaColumns.MIME_TYPE + "=?", new String[] {
								"image/jpeg", "image/png" },
						MediaColumns.DATE_MODIFIED);
				if (mCursor != null && mCursor.getCount() > 0) {
					// //LogUtils.i("---size=" + mScanList.size());
					while (mCursor.moveToNext()) {
						// 获取图片的路径
						String path = mCursor.getString(mCursor
								.getColumnIndex(MediaColumns.DATA));
						// 添加未上传的图片到mScanList里面
						long ImageSize = mCursor.getInt(mCursor
								.getColumnIndex(MediaColumns.SIZE));
						if (ImageSize > (200 * 1024)
								&& (mScanedImg == null || !mScanedImg
										.contains(path))) {
							mScanList.add(path);
						}
					}
					Collections.reverse(mScanList);
					if (mCursor != null) {
						mCursor.close();
					}
				}
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(Constants.SCAN_COMPLETE);
			}
		});
	}

	// 判断图片大小，大于200kb的图片才显示
	/*
	 * private int getImageSize(String path) { File file = new File(path); if
	 * (file.exists()) { return (int) (file.length() / (200 * 1024)); } return
	 * 0; }
	 */
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
				// LogUtils.e("Error:getPGDetailLis is null~~~~");
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

	// 由 startActivityForResult 跳转到这里
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == Constants.ISACCEPT) {
				// 是否接受请求的处理
				Item item = (Item) data.getParcelableExtra("choice");
				sessionsImAdapter.addItem(item);
				sessionsImAdapter.notifyDataSetChanged();
			}
		}
		if (resultCode == 0 && requestCode == LAUNCH_LOADNEWFRIEND) {
			status_new.setVisibility(View.INVISIBLE);
		}
	}

	// 好友
	private void setupFriendTab(View view, String tag) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		ImageView imageViewttt = (ImageView) tabview
				.findViewById(R.id.imageViewttt);
		// 好友图标
		imageViewttt.setImageResource(R.drawable.friend_selector);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(new TabContentFactory() {
					@Override
					public View createTabContent(String tag) {
						// //LogUtils.i("createTabcontent:" + tag);
						LayoutInflater inflater = (LayoutInflater) mActivity
								.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
						// 好友主界面容器
						View view = inflater.inflate(
								R.layout.contaclist_friend_main, null);
						viewPager_Friend = (ViewPager) view
								.findViewById(R.id.friendMainViewPager);
						// 初始化导航条
						setFriendHeader(view);
						// 设置两个页面的适配器
						List<View> childViews = setupFriendView(viewPager_Friend);
						CustomViewPagerAdapter vpAdapter = new CustomViewPagerAdapter(
								childViews, mContext);
						viewPager_Friend.setAdapter(vpAdapter);
						viewPager_Friend
								.setOnPageChangeListener(friendPageChangeLis);
						int value = SharedPrefsUtil.getValue(mContext,
								SettingKey.CustomViewPagerIndex, 0);
						viewPager_Friend.setCurrentItem(value);
						return view;
					}
				});
		mTabHost.addTab(setContent);
	}

	private OnTitleBtnChangeListener friendTabHeadlineLis = new OnTitleBtnChangeListener() {
		@Override
		public void onViewChange(int pageIndex) {
			viewPager_Friend.setCurrentItem(pageIndex, true);
			SharedPrefsUtil.putValue(mContext, SettingKey.CustomViewPagerIndex,
					pageIndex);
		}
	};
	private OnPageChangeListener friendPageChangeLis = new CustomerPageChangeLis() {
		@Override
		public void onPageSelected(int pageIndex) {
			// LogUtils.i("friend tab switch page:" + pageIndex);
			headLineViewFriend.setSelected(pageIndex);
			if (pageIndex == 1) {
				// 附近
				InnerGuideHelper.showAddfriendsGuide(ContactList.this);
				if (nearByAdapter.getStatus() == DataStatus.empty) {
					int position = SharedPrefsUtil.getValue(mContext,
							SettingKey.neighborPos,
							NeighborHoodType.all.ordinal());
					executeGetNeighborHood(NeighborHoodType.values()[position],
							PullType.PullDown, true);
				}
			}
		}
	};

	private void setFriendHeader(View view) {
		ArrayList<CustomTitleBtn> btns = new ArrayList<CustomTitleBtn>();
		// 搜索
		CustomTitleBtn searchbtn = (CustomTitleBtn) view
				.findViewById(R.id.leftbtn1);
		searchbtn.setVisibility(View.VISIBLE);
		searchbtn.setText(mActivity.getResources().getString(
				R.string.friend_findperson));
		searchbtn.setImgResource(R.drawable.friend_find_selector);
		searchbtn.setViewPaddingLeft();
		searchbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
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
		friend_friend.setText(mActivity.getResources().getString(
				R.string.friend_friend));
		friend_friend.setTextColorSelector(R.color.topbar_textcolor_selector);
		friend_friend.setImgResource(R.drawable.friend_friends_selector);
		friend_friend.setViewPaddingRight();
		btns.add(friend_friend);
		// 附近
		friend_nearby = (CustomTitleBtn) view.findViewById(R.id.rightbtn2);
		friend_nearby.setVisibility(View.VISIBLE);
		friend_nearby.setText(mActivity.getResources().getString(
				R.string.friend_nearby));
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
		friendListAndDistanceLT = new FriendListAndDistanceLT();
		friend_nearby_selectrange.setOnClickListener(friendListAndDistanceLT);
		friend_nearby_selectrange.setViewPaddingRight();
		// 标题
		contacts_textView2 = (TextView) view.findViewById(R.id.topbar_title);
		contacts_textView2.setVisibility(View.GONE);
		// 设置头部监听
		headLineViewFriend = new TitleBtnChangeManger(btns);
		headLineViewFriend.setOnViewChangeListener(friendTabHeadlineLis);
	}

	class FriendListAndDistanceLT implements OnClickListener {
		public FriendListAndDistanceLT() {
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			// 附近选择界面
			case R.id.rightbtn1: {
				if (viewPager_Friend.getCurrentItem() == 1) {
					float xoff = BBSUtils.toPixel(mActivity,
							TypedValue.COMPLEX_UNIT_DIP, 0);
					float yoff = BBSUtils.toPixel(mActivity,
							TypedValue.COMPLEX_UNIT_DIP, -6);
					NeighborHoodSelPopWindow popWindow = new NeighborHoodSelPopWindow(
							mActivity);
					popWindow.setOnDismissListener(new OnDismissListener() {
						@Override
						public void onDismiss() {
							friend_nearby_selectrange.setSelected(false, false);
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
					viewPager_Friend.setCurrentItem(1);
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
				// LogUtils.i("start.getPullAction():" + start.getPullAction() +
				// " nearByAdapter.getStart():"
				// + nearByAdapter.getStart() + " start:" + start);
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
			// TODO
			ContactServiceDlg.executeGetNeighborHood(mContext, nbType,
					new Start(null, action), 10, neighborRstLis);
		} else {
			ContactServiceDlg.executeGetNeighborHood(mContext, nbType,
					new Start(nearByAdapter.getStart(), action), 10,
					neighborRstLis);
		}
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context)
				.inflate(R.layout.tabs_bg, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	@Override
	public void registerVVBroadCastReceivers() {
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
		registerVVBroadCastReceiver(friendModleBR, friendFilter);
		registerVVBroadCastReceiver(shareModelReceiver, new IntentFilter(
				"shareTab"));
		// 消息数量接收器
		IntentFilter filter = new IntentFilter();
		filter.addAction(MsgType.chat.toString());
		filter.addAction(MsgType.friend_require.toString());
		filter.addAction(MsgType.comment.toString());
		registerVVBroadCastReceiver(sessionModelReceiver, filter);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putInt(STATE_POSITION, mTabHost.getCurrentTab());
	}

	private List<View> setupFriendView(ViewPager viewPagerParent) {
		List<View> retVal = new ArrayList<View>();
		// 好友
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View friendView = inflater.inflate(
				R.layout.contaclist_friend_item_friend, viewPagerParent, false);
		friendlist = (ListView) friendView.findViewById(R.id.friendlist);
		mListGroup.add(getString(R.string.contact_list_all_contact));
		// 好友列表适配器
		View headview = setHeaderViewInRoster(null);
		friendlist.addHeaderView(headview);
		friendAdapter = getContactListAdapter(mListGroup
				.get(mListGroup.size() - 1));
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
		nearByAdapter = new NearbyContactsAdapter(mActivity,
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
		LayoutInflater inflater = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = inflater.inflate(R.layout.guide_add_friend, parent, false);
		mFriendGuideLayout = view.findViewById(R.id.guide_friend_layout);
		layout_newfriend = view.findViewById(R.id.new_friend);
		layout_addfriend = view.findViewById(R.id.add_friend);
		ignore_friend = view.findViewById(R.id.ignore_friend);
		status_new = (ImageView) view.findViewById(R.id.status_new);
		Contact contact = ContactService.getInstance().getContact(
				Constants.GENER_NUM);
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
				Intent intent = new Intent(getApplicationContext(),
						LoadnewFriend.class);
				startActivityForResult(intent, LAUNCH_LOADNEWFRIEND);
			}
		});
		// 添加好友
		layout_addfriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						AddVVContact.class));
			}
		});
		// 黑名单
		ignore_friend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getApplicationContext(),
						BlacklistActivity.class)
						.setAction(Constants.ACTION_BLACKROSTER));
			}
		});
		mFriendGuideLayout.setVisibility(View.VISIBLE);
		if (contact != null) {
			bindView(contact);
		}
		return view;
	}

	/**
	 * @param contact
	 * @param params
	 * @func 刷新时光界面图片
	 */
	private void loadTimeFlyImage(String startTime, int num) {
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

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(tag, "ContactList onDestroy");
		EventBus.getDefault().unregister(this);
		xmppHelper.unBindBeemSerivice();
		if (AppProperty.getInstance().logServiceEnable)
			stopService(new Intent(this, LogService.class));
	}

	/**
	 * Get a {@link ContactListAdapter} for a group. The
	 * {@link ContactListAdapter} will be created if it is not exist.
	 * 
	 * @param group
	 *            the group
	 * @return the adapter
	 */
	public ContactListAdapter getContactListAdapter(String group) {
		synchronized (contactListAdapters) {
			ContactListAdapter contactListAdapter = contactListAdapters
					.get(group);
			if (contactListAdapter == null) {
				if (BeemApplication.isOff()) {
					// 读取离线好友
					List<Contact> coList = ContactService.getInstance()
							.getFriendlist();
					contactListAdapter = new ContactListAdapter(
							ContactList.this);
					for (Contact c : coList) {
						contactListAdapter.put(c);
					}
				} else {
					contactListAdapter = new ContactListAdapter(
							ContactList.this, mXmppFacade);
				}
				if (group.equals("new friends")) {
					contactListAdapter.setListType(ContactListType.NEW_FRIEND);
				}
				if (mXmppFacade != null && !BeemApplication.isOff()) {
					try {
						// LogUtils.i(contactListAdapter + "-" + mXmppFacade);
						contactListAdapter.setmUserInfo(mXmppFacade
								.getUserInfo());
					} catch (RemoteException e) {
						e.printStackTrace();
					}
				}
				contactListAdapters.put(group, contactListAdapter);
				List<String> realGroups = mListGroup.subList(0,
						mListGroup.size() - 1);
				if (!mListGroup.contains(group)) {
					boolean added = false;
					// insert group in sorted list
					for (ListIterator<String> iterator = realGroups
							.listIterator(); iterator.hasNext();) {
						String currentGroup = iterator.next();
						if (currentGroup.compareTo(group) > 0) {
							iterator.previous();
							iterator.add(group);
							added = true;
							break;
						}
					}
					if (!added)
						realGroups.add(group);
				}
			}
			boolean hideDisconnected = mSettings.getBoolean(
					BeemApplication.SHOW_OFFLINE_CONTACTS_KEY, false);
			contactListAdapter.setOnlineOnly(hideDisconnected);
			return contactListAdapter;
		}
	}

	/**
	 * Add a contact to the special list No Group and All contacts. The contact
	 * will be added if the list is not the current list otherwise the list must
	 * be modified in a Handler.
	 * 
	 * @param contact
	 *            the contact to add.
	 */
	private void addToSpecialList(Contact contact) {
		List<String> groups = contact.getGroups();
		// System.out.println("addToSpecialList " + contact.getJid());
		ContactListAdapter adapter = getContactListAdapter(getString(R.string.contact_list_all_contact));
		adapter.put(contact);
	}

	// 好友模块广播接收器
	private class FriendModelBroadCastReceiver extends VVBaseBroadCastReceiver {
		private FriendModelBroadCastReceiver(boolean isLocal) {
			super(isLocal);
		}

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			boolean isUpdateFrinedList = intent.getBooleanExtra(
					Constants.EXTRA_FRIEND_ISUPDATE_FRIENDS, false);
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

	@Override
	public void onBackPressed() {
		if (sessionsImAdapter != null && sessionsImAdapter.isDelState()) {
			sessionsImAdapter.Cancel();
			return;
		}
		super.onBackPressed();
	}

	/**
	 * Assign the differents contact to their groups. This methods will fill the
	 * mContactOnGroup map.
	 * 
	 * @param contacts
	 *            list of contacts
	 * @param groupNames
	 *            list of existing groups
	 */
	private void assignContactToGroups(List<Contact> contacts,
			List<String> groupNames) {
		if (contacts == null) {
			return;
		}
		for (Contact c : contacts) {
			addToSpecialList(c);
			List<String> groups = c.getGroups();
			if (groups == null) {
				continue;
			}
			for (String currentGroup : groups) {
				ContactListAdapter cl = getContactListAdapter(currentGroup);
				cl.put(c);
			}
		}
	}

	@Override
	public void onServiceConnectAct(IXmppFacade xmppFacade, ComponentName name,
			IBinder service) {
		mXmppFacade = (XmppFacade) xmppFacade;
		// LogUtils.i("~~onServiceConnected~~");
		checkOfflineMsg();
		new VVBaseLoadingDlg<Boolean>(new VVBaseLoadingDlgCfg(mContext)) {
			private List<Contact> contactList = new ArrayList<Contact>();

			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 登录时上报下经纬度
				ContactService.getInstance().synGeoInfo();
			}

			@Override
			protected Boolean doInBackground() {
				contactList
						.addAll(ContactService.getInstance().getFriendlist());
				return true;
			}

			@Override
			protected void onPostExecute(Boolean result) {
				// LogUtils.i("~~onPostExecute~~");
				if (mXmppFacade != null
						&& mXmppFacade.getMessageManager() != null) {
					if (!mXmppFacade.getMessageManager().isMessageChecked()) {
						if (curTabName == TabName.Session) {
							msgBadgeViewNumIcon.setVisibility(View.GONE);
						} else {
							msgBadgeViewNumIcon.setVisibility(View.VISIBLE);
						}
					}
					mXmppFacade.getMessageManager().checkTimeflyNotifyDlg(
							ContactList.this);
					assignContactToGroups(contactList, null);
					friendAdapter.notifyDataSetChanged();
				}
				// 上传手机联系人
				ThreadUtils.executeTask(new Runnable() {
					@Override
					public void run() {
						Map<String, PhoneContact> localPhoneContactMap = new HashMap<String, PhoneContact>();
						BBSUtils.getPhoneContact(PhoneNumWhere.all, mContext,
								localPhoneContactMap);
						ContactService.getInstance().uploadPhoneContacts(
								mXmppFacade, localPhoneContactMap, 20);
					}
				});
			}
		}.execute();
	}

	@Override
	public void onServiceDisconnectAct(IXmppFacade xmppFacade,
			ComponentName name) {
		mXmppFacade = (XmppFacade) xmppFacade;
		Log.i(TAG, "~~~onServiceDisconnected~~~");
		mListGroup.clear();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		if (intent != null && intent.getAction() != null
				&& intent.getAction().equals(MessageManager.CHECK_MESSAGE)) {
			if (curTabName == TabName.Session) {
				mXmppFacade.getMessageManager().checkMessages();
			} else {
				mTabHost.setCurrentTab(TabName.Session.ordinal());
			}
		}
	}

	// 如果退出主界面存在离线消息，进入activity则跳到消息界面
	public void checkOfflineMsg() {
		if (SharedPrefsUtil.getValue(BeemApplication.getContext(),
				SettingKey.offline_msg, false)) {
			SharedPrefsUtil.putValue(BeemApplication.getContext(),
					SettingKey.offline_msg, false);
			if (curTabName == TabName.Session) {
				mXmppFacade.getMessageManager().checkMessages();
			} else {
				mTabHost.setCurrentTab(TabName.Session.ordinal());
			}
		}
	}

	@Override
	public void onEventMainThread(EventBusData data) {
		// LogUtils.i("data_action:" + data.getAction());
		switch (data.getAction()) {
		case TimeflyImageDelete: {
			// 图片组数据删除
			String[] imageid = (String[]) data.getMsg();
			List<ImageFolderItem> itmes = mTimeFlyAdapter.getItems();
			Iterator<ImageFolderItem> it = itmes.iterator();
			ImageFolder folder = null;
			while (it.hasNext()) {
				ImageFolderItem imgFolderItem = it.next();
				folder = imgFolderItem.getImageFolder();
				if (folder.getCreateTime().equals(imageid[0])) {
					imgFolderItem.getVVImages().remove(
							Integer.parseInt(imageid[1]));
					folder.setField(DBKey.photoCount, imgFolderItem
							.getVVImages().size());
					if (imgFolderItem.getVVImages().size() == 0) {
						// 删除右侧图片列表中的文件夹信息
						it.remove();
					}
					timeflyGroupsAdapter.changeData(folder.getCreateTime(),
							folder);
					timeflyGroupsAdapter.notifyDataSetChanged();
					mTimeFlyAdapter.notifyDataSetChanged();
					break;
				}
			}
			break;
		}
		case UploadTimeflyPhotoAdd: {
			if (isUploading == true) {
				CToast.showToast(mContext, "上传图片中，请稍候再上传！", Toast.LENGTH_SHORT);
				break;
			}
			// 上传图片后刷新界面
			ImageFolderItem newFolderItem = (ImageFolderItem) data.getMsg();
			ArrayList<String> listPath = (ArrayList<String>) data.getMsgList();
			int ItemsNumber = mTimeFlyAdapter.getItems().size();
			// LogUtils.i("-- ItemsNumber=" + ItemsNumber);
			// LogUtils.i("--newFolderItem image size=" +
			// newFolderItem.getVVImages().size());
			// LogUtils.i("-- getCreateTime=" +
			// newFolderItem.getImageFolder().getCreateTime());
			for (int i = 0; i < ItemsNumber; i++) {
				ImageFolder imagefolder = newFolderItem.getImageFolder();
				if (mTimeFlyAdapter.getItem(i).getImageFolder().getCreateTime()
						.equals(imagefolder.getCreateTime())) {
					for (int j = 0; j < listPath.size(); j++) {
						VVImage vvImage = new VVImage();
						vvImage.setField(DBKey.jid, imagefolder.getJid());
						vvImage.setField(DBKey.createTime,
								imagefolder.getCreateTime());
						vvImage.setField(DBKey.isLoading, true);
						vvImage.setField(DBKey.path,
								Scheme.FILE.wrap(listPath.get(j)));
						vvImage.setField(DBKey.pathThumb,
								Scheme.FILE.wrap(listPath.get(j)));
						vvImage.setImageisLoading(true);
						vvImage.setDiskPath(listPath.get(j));
						// 添加上传的图片到起始位置；
						// LogUtils.i("-- j=" + j + ", i =" + i);
						newFolderItem.getVVImages().add(j, vvImage);
					}
					updateIndex = i;
					// LogUtils.i("--newFolderItem image size2222=" +
					// newFolderItem.getVVImages().size());
					// 更新数据
					mTimeFlyAdapter.setItem(i, newFolderItem);
					mTimeFlyAdapter.notifyDataSetChanged();
					timeflyGroupsAdapter.changeData(newFolderItem
							.getImageFolder().getCreateTime(), newFolderItem
							.getImageFolder());
					timeflyGroupsAdapter.notifyDataSetChanged();
					// 上传图片
					uploadImages(listPath, imagefolder);
					break;
				}
			}
			break;
		}
		case UploadTimeflyPhotoCreate: {
			if (isUploading == true) {
				CToast.showToast(mContext, "上传图片中，请稍候再上传！", Toast.LENGTH_SHORT);
				break;
			}
			ImageFolderItem folderItemCreate = (ImageFolderItem) data.getMsg();
			ArrayList<String> listPath = (ArrayList<String>) data.getMsgList();
			ImageFolder imagefolder = folderItemCreate.getImageFolder();
			for (int i = 0; i < listPath.size(); i++) {
				VVImage vvImage = new VVImage();
				vvImage.setField(DBKey.jid, folderItemCreate.getImageFolder()
						.getJid());
				vvImage.setField(DBKey.isLoading, true);
				vvImage.setField(DBKey.pathThumb,
						Scheme.FILE.wrap(listPath.get(i)));
				vvImage.setImageisLoading(true);
				vvImage.setDiskPath(listPath.get(i));
				// vvImage.saveToDatabase();
				// 添加上传的图片到起始位置；
				folderItemCreate.getVVImages().add(i, vvImage);
			}
			updateIndex = 0;// 更新图片的选项
			// 更新数据
			xTimeFlyTraceListView.setProcessState(ProcessState.Succeed);
			mTimeFlyAdapter.addItem(folderItemCreate);
			mTimeFlyAdapter.notifyDataSetChanged();
			timeflyGroupsAdapter.changeData(folderItemCreate.getImageFolder()
					.getCreateTime(), folderItemCreate.getImageFolder());
			timeflyGroupsAdapter.notifyDataSetChanged();
			uploadImages(listPath, imagefolder);
			break;
		}
		case ShareSupportChange: {
			// 点赞数据及状态更新
			PraiseEventBusData pBusData = (PraiseEventBusData) data.getMsg();
			for (int i = 0; i < shareAdapterArr.length; i++) {
				shareAdapterArr[i]
						.updateSupportList(pBusData, data.getAction());
			}
			break;
		}
		case ShareCommentChange: {
			PraiseEventBusData pBusData = (PraiseEventBusData) data.getMsg();
			for (int i = 0; i < shareAdapterArr.length; i++) {
				shareAdapterArr[i].updateSupportList(pBusData,
						EventAction.ShareCommentChange);
			}
			break;
		}
		case DOWNLOADSUCCESS:
			// UpdateManager.install(mActivity, (String) data.getMsg());
			break;
		case TimeflyNotify: {
			// 时光提醒
			if (mXmppFacade != null && mXmppFacade.getMessageManager() != null) {
				mXmppFacade.getMessageManager().checkTimeflyNotifyDlg(
						ContactList.this);
			}
			break;
		}
		case CheckTimeflyNotify: {
			Object[] extra_data = (Object[]) data.getMsg();
			String create_time = (String) extra_data[0];
			String notify_valid = (String) extra_data[1];
			for (int i = 0; i < mTimeFlyAdapter.getItems().size(); i++) {
				if (mTimeFlyAdapter.getItem(i).getImageFolder().getCreateTime()
						.equals(create_time)) {
					mTimeFlyAdapter.getItem(i).getImageFolder()
							.setNotify_valid(notify_valid);
					break;
				}
			}
			break;
		}
		case LoadImageNumberChanged: {
			int msg = (Integer) data.getMsg();
			if (msg == 0) {
				load_btn.setText(getString(R.string.str_load));
				load_btn.setWidth((int) (75 * density));
				load_btn.setTextColor(0xFFFFFFFF);
				load_btn.setBackgroundResource(R.drawable.button_selected_style);
			} else if (msg == -1) {
				CToast.showToast(mContext, R.string.showimage_uploadCountFull,
						Toast.LENGTH_SHORT);
			} else {
				// ArrayList<String> listimagePath = (ArrayList<String>)
				// loadImageadapter.getListImagePath();
				String titleText = "( " + msg + " )";
				load_btn.setText(getString(R.string.str_load) + titleText);
				load_btn.setWidth((int) (110 * density));
				load_btn.setTextColor(0xFFFFFFFF);
				load_btn.setBackgroundResource(R.drawable.button_selected_style);
				// LogUtils.i("---handleMessage titleText=" + titleText);
			}
			break;
		}
		case MessageChatOpen: {
			// 收到打开chat的
			if (sessionsImAdapter != null)
				sessionsImAdapter.readChatItemAndRefresh((Item) data.getMsg());
			break;
		}
		case XmppConnectState: {
			/*
			 * int animType = BeemApplication.isOff() ?
			 * ExpandCollapseAnimation.EXPAND :
			 * ExpandCollapseAnimation.COLLAPSE; Animation anim = new
			 * ExpandCollapseAnimation(network_invalid_layout, animType);
			 * anim.setDuration(200);
			 * network_invalid_layout.startAnimation(anim);
			 */
			network_invalid_layout
					.setVisibility(BeemApplication.isOff() ? View.VISIBLE
							: View.GONE);
			break;
		}
		case TimeflyAlert: {
			final Object[] obj = (Object[]) data.getMsg();
			final ImageFolderItem imageFolderItem = (ImageFolderItem) obj[0];
			final ToggleButton remind_toggle = (ToggleButton) obj[1];
			final String albumSignEdit = (String) obj[2];
			final String authority = (String) obj[3];
			final String[] time = (String[]) obj[4];
			new VVBaseLoadingDlg<Boolean>(
					new VVBaseLoadingDlgCfg(mContext).setShowWaitingView(true)) {
				private Valid valid;
				private String notify_time;

				@SuppressWarnings("deprecation")
				@Override
				protected Boolean doInBackground() {
					String[] ymd = time;
					Date date = new Date();
					valid = remind_toggle.isChecked() ? Valid.open
							: Valid.close;
					if (date.getYear() + 1900 == Integer.valueOf(ymd[0])
							&& date.getMonth() + 1 == Integer.valueOf(ymd[1])
							&& date.getDate() == Integer.valueOf(ymd[2])) {
						currentTime = "1分钟";
						isLocal = true;
						return true;
					} else {
						isLocal = false;
						int year = (date.getYear() + 1900) * 365;
						int month = (date.getMonth() + 1) * 30;
						int day = date.getDate();
						int a = Integer.valueOf(ymd[0]) * 365
								+ Integer.valueOf(ymd[1]) * 30
								+ Integer.valueOf(ymd[2]);
						currentTime = a - year - month - day + "天";
						notify_time = new StringBuffer()
								.append(ymd[0])
								.append("-")
								.append(BBSUtils.padStrBefore(
										String.valueOf(ymd[1]), '0', 2))
								.append("-")
								.append(BBSUtils.padStrBefore(
										String.valueOf(ymd[2]), '0', 2))
								.append(" 00:00:00").toString();
						Map<String, Object> rstOne = TimeflyService
								.managePhotogroup(LoginManager.getInstance()
										.getJidParsed(), imageFolderItem
										.getImageFolder().getGid(),
										imageFolderItem.getImageFolder()
												.getCreateTime(), albumSignEdit
												.toString(), authority);
						Map<String, Object> rstTwo = TimeflyService
								.setPhotoGroupNotify(imageFolderItem
										.getImageFolder().getGid(),
										imageFolderItem.getImageFolder()
												.getCreateTime(), notify_time,
										valid);
						return JsonParseUtils.getResult(rstOne)
								&& JsonParseUtils.getResult(rstTwo);
					}
				}

				@Override
				protected void onPostExecute(Boolean result) {
					if (result) {
						toastCommon = ToastCommon.createToastConfig();
						toastCommon.ToastShow(mContext, null,
								LoginManager.getInstance().getMyContact()
										.getSuitableName(), currentTime);
						if (isLocal && valid.ordinal() == 0) {
							mHandler.postDelayed(new Runnable() {
								@Override
								public void run() {
									final BBSCustomerDialog blurDlg = BBSCustomerDialog
											.newInstance(mContext,
													R.style.blurdialog);
									TimeflyDueRemindView remindview = new TimeflyDueRemindView(
											mContext, true);
									remindview.setText(currentTime);
									remindview
											.setBtnClickListener(new OnClickListener() {
												@Override
												public void onClick(View v) {
													blurDlg.dismiss();
													ShareRankingActivity
															.launch(mContext,
																	LoginManager
																			.getInstance()
																			.getJidParsed(),
																	imageFolderItem
																			.getImageFolder()
																			.getGid(),
																	imageFolderItem
																			.getImageFolder()
																			.getCreateTime());
													ImageFolderNotify notifyDB = new ImageFolderNotify();
													notifyDB.setField(
															DBKey.jid,
															LoginManager
																	.getInstance()
																	.getJidParsed());
													notifyDB.setField(
															DBKey.gid,
															imageFolderItem
																	.getImageFolder()
																	.getGid());
													notifyDB.setField(
															DBKey.createTime,
															imageFolderItem
																	.getImageFolder()
																	.getCreateTime());
													notifyDB.setField(
															DBKey.notify_valid,
															Valid.close.val);
													notifyDB.saveToDatabaseAsync();
													// 更新界面数据
													EventBusData data = new EventBusData(
															EventAction.CheckTimeflyNotify,
															new Object[] {
																	imageFolderItem
																			.getImageFolder()
																			.getCreateTime(),
																	Valid.close.val });
													EventBus.getDefault().post(
															data);
												}
											});
									blurDlg.setContentView(remindview
											.getmView());
									blurDlg.getWindow()
											.setType(
													WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
									blurDlg.setCancelable(true);
									blurDlg.show();
								}
							}, 60 * 1000);
						} else if (!isLocal && valid.ordinal() == 0) {
							/*
							 * if (lis != null) {
							 * lis.onChangePhoto(albumSignEdit, authority);
							 * lis.onChangeNotify(valid.val, notify_time); }
							 */
						}
					} else {
						// CToast.showToast(mContext, "修改失败",
						// Toast.LENGTH_SHORT);
					}
				}
			}.execute();
			break;
		}
		default:
			break;
		}
	}

	private void uploadImages(ArrayList<String> listPath,
			ImageFolder imagefolder) {
		// 上传图片的参数
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("authority", imagefolder.getAuthority());
		params.put("lon", String.valueOf(imagefolder.getLon()));
		params.put("lat", String.valueOf(imagefolder.getLat()));
		params.put("gid", imagefolder.getGid());
		params.put("create_time", imagefolder.getCreateTime());
		UploadImageUtil uploadUtil = UploadImageUtil.getInstance();
		uploadUtil.setOnUploadProcessListener(new OnUploadProcessListener() {
			@Override
			public void onUploadProcess(int responseCode, String message) {
				new SaveFolderItemTask(message).execute();
			}

			@Override
			public void onUploadDone(int responseCode, String message) {
				// LogUtils.i("onUploadDone message=" + message +
				// ",responseCode=" + responseCode);
				if (responseCode == Constants.UPLOAD_SERVER_ERROR_CODE) {
					new RemoveFolderItemTask(message).execute();
				} else if (responseCode == Constants.UPLOAD_SUCCESS_CODE) {
					new UploadFolderItemTask(message).execute();
				}
			}

			@Override
			public void initUploadError() {
				mHandler.sendEmptyMessage(Constants.UPLOAD_INIT_PROCESS);
			}
		}); // 设置监听器监听上传状态
		isUploading = true;
		uploadUtil.uploadFile(listPath, params);
	}

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

	public void modifyContactInfo(final UserInfoPacket modiInfoPacket) {
		final Map<UserInfoKey, String> modifyMaps = modiInfoPacket
				.cloneFieldMaps();
		if (modifyMaps.isEmpty()) {
			Log.i(TAG, "modifyMaps.isEmpty()");
			return;
		}
		new VVBaseLoadingDlg<PacketResult>(new VVBaseLoadingDlgCfg(
				ContactList.this).setShowWaitingView(true)) {
			@Override
			protected PacketResult doInBackground() {
				PacketResult result = ContactService.getInstance()
						.modifyContactInfo(modifyMaps);
				return result;
			}

			@Override
			protected void onPostExecute(PacketResult result) {
				Log.i(TAG, "result" + result.isOk());
				if (result.isOk()) {
					modiInfoPacket.clearFileds();
					Contact myContact = LoginManager.getInstance()
							.getMyContact();
					myContact.saveData(modifyMaps);
					EventBus.getDefault().post(
							new EventBusData(EventAction.ModifyContactInfo,
									modifyMaps));
					CToast.showToast(mContext, "保存头像成功", Toast.LENGTH_SHORT);
				} else {
					CToast.showToast(mContext, "保存头像失败", Toast.LENGTH_SHORT);
				}
			}
		}.execute();
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
				ContactInfoActivity.launch(mActivity, curContact);
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
				OtherTimeFlyMain.launch(mActivity, curContact);
			}
		});
		list.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ChatActivity.launch(mActivity, curContact);
			}
		});
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		// 此处在内存溢出回复Contactlist时重新连接服务器
	}
}
