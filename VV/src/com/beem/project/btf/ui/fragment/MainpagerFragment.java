/**
 * 
 */
package com.beem.project.btf.ui.fragment;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.provider.MediaStore.MediaColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.agimind.widget.SlideHolder;
import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.bbs.view.PreviewPoseView;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.CartoonCameraActivity;
import com.beem.project.btf.ui.CartoonCameraActivity.CameraType;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity.IntentKey;
import com.beem.project.btf.ui.activity.InnerGuideHelper;
import com.beem.project.btf.ui.activity.MySettings;
import com.beem.project.btf.ui.activity.NewsCameraEditorActivity;
import com.beem.project.btf.ui.activity.ShareRankingActivity;
import com.beem.project.btf.ui.activity.base.ActivityController;
import com.beem.project.btf.ui.activity.base.BeemServiceHelper;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.EventBusData.IEventBusAction;
import com.beem.project.btf.ui.loadimages.LoadImageAdapter;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg;
import com.beem.project.btf.ui.views.TimeflyDueRemindView;
import com.beem.project.btf.ui.views.ToastCommon;
import com.beem.project.btf.ui.views.GetPhotoGroupListDlg.OnGetPGListResult;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.beem.project.btf.utils.ThreadUtils;
import com.beem.project.btf.utils.UploadImageUtil;
import com.beem.project.btf.utils.UploadImageUtil.OnUploadProcessListener;
import com.butterfly.piqs.vvcartoon.CartoonLib;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter;
import com.butterfly.vv.adapter.TimeflySliderbarAdapter.YearMapItemListener;
import com.butterfly.vv.adapter.VVTimeFlyTracesAdapter;
import com.butterfly.vv.camera.GalleryActivity;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.ImageFolderNotify;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.model.ImageFolderItem;
import com.butterfly.vv.service.TimeflyService;
import com.butterfly.vv.service.TimeflyService.Valid;
import com.butterfly.vv.view.timeflyView.HolderTwowayView;
import com.butterfly.vv.vv.utils.CToast;
import com.butterfly.vv.vv.utils.JsonParseUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg;
import com.teleca.jamendo.dialog.GetPhotoGroupDetailDlg.onGetPGDetailResult;

import de.greenrobot.event.EventBus;

/**
 * @author hongbo ke
 */
public class MainpagerFragment extends MainpagerAbstractFragment implements
		OnClickListener, IEventBusAction {
	private static final String SCANED_IMAGES = "scaned_images";
	private static final int SCAN_COMPLETE = 1;
	private View rootView;
	private ViewGroup timeflyslider_list;
	private ViewGroup timeflyslider_empty;
	private View CoverImageView_Gallery;
	private TextView ImageCount, DayCount;
	private TimeflySliderbarAdapter timeflyGroupsAdapter;
	private SlideHolder mSlideHolder;
	private View network_invalid_layout;
	private TextView tvw_TimeflyLoginStatus;
	private RelativeLayout loading_images;
	private HolderTwowayView mImageGallery;
	private PullToProcessStateListView xTimeFlyTraceListView;
	private VVTimeFlyTracesAdapter mTimeFlyAdapter;
	private SharedPreferences mSettings;
	private List<String> mScanList = new ArrayList<String>();
	protected Handler mHandler;
	private LoadImageAdapter loadImageadapter;
	private ExpandableListView yearlistview;
	private View beautifyCamera;
	private View newsCamera;
	private View cartoonCamera;
	private ImageView tooglebtn;
	private ImageView settingBtn;
	private boolean isUploading;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSettings = PreferenceManager.getDefaultSharedPreferences(mContext);
		mHandler = new Handler(mContext.getMainLooper()) {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
					case SCAN_COMPLETE: {
						if (mScanList != null && mScanList.size() > 0) {
							loadImageadapter = new LoadImageAdapter(mContext,
									mScanList);
							// 触摸到图片组时，侧拉暂时不响应,解决事件冲突
							mImageGallery.setHolder(mSlideHolder);
							mImageGallery.setItemMargin(2);
							mImageGallery.setAdapter(loadImageadapter);
						} else {
							xTimeFlyTraceListView.getRefreshableView()
									.removeHeaderView(loading_images);
						}
					}
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
					default:
						break;
				}
			}
		};
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != rootView) {
			((ViewGroup) rootView.getParent()).removeView(rootView);
		} else {
			rootView = LayoutInflater.from(mContext).inflate(
					R.layout.time_fly_traces_xlistview, null);
			initViews();
			setupSilderbar();
			setupContentView();
			setupLoadingImages();
			loadImagesFirstTime();
			if (callback.isAutoAuthentificateCompleted()) {
				// 开始拉取数据；先拉取yearMap，即所有图片组的日期以及张数等简略信息。
				if (LoginManager.getInstance().isLogined()) {
					loadingData();
				} else {
					xTimeFlyTraceListView.setProcessState(ProcessState.Emptydata);
				}
			}
			InnerGuideHelper.showTimeflyGuide(mContext);
		}
		showLoginStatus();
		return rootView;
	}
	private void loadingData() {
		String parsedJid = LoginManager.getInstance().getJidParsed();
		new GetPhotoGroupListDlg(mContext, parsedJid, new OnGetPGListResult() {
			@Override
			public void onResult(Map<String, ImageFolder> yearMap,
					boolean isTimeout) {
				
				if (yearMap != null && yearMap.size() > 0) {
					timeflyGroupsAdapter.addDatas(yearMap);
					// 开始获取前个数据的图片详情
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
				timeflyGroupsAdapter.notifyDataSetChanged();
			}
		}).execute();
	}
	private void initViews() {
		timeflyslider_list = (ViewGroup) rootView
				.findViewById(R.id.timeflyslider_list);
		timeflyslider_empty = (ViewGroup) rootView
				.findViewById(R.id.timeflyslider_empty);
		CoverImageView_Gallery = rootView
				.findViewById(R.id.CoverImageView_Gallery);
		ImageCount = (TextView) rootView.findViewById(R.id.ImagesCount);
		DayCount = (TextView) rootView.findViewById(R.id.DayCount);
		mSlideHolder = (SlideHolder) rootView.findViewById(R.id.slideHolder);
		yearlistview = (ExpandableListView) rootView
				.findViewById(R.id.yearlistview);
		network_invalid_layout = rootView
				.findViewById(R.id.network_invalid_layout);
		tvw_TimeflyLoginStatus = (TextView) rootView
				.findViewById(R.id.tvw_TimeflyLoginStatus);
		beautifyCamera = rootView.findViewById(R.id.CoverImageView_Beautify);
		newsCamera = rootView.findViewById(R.id.CoverImageView_NewsCamera);
		cartoonCamera = rootView.findViewById(R.id.CoverImageView_Cartoon);
		tooglebtn = (ImageView) rootView.findViewById(R.id.tooglebtn);
		xTimeFlyTraceListView = (PullToProcessStateListView) rootView
				.findViewById(R.id.myxlistview);
		settingBtn = (ImageView) rootView.findViewById(R.id.settingBtn);
		network_invalid_layout.setOnClickListener(this);
		beautifyCamera.setOnClickListener(this);
		newsCamera.setOnClickListener(this);
		cartoonCamera.setOnClickListener(this);
		CoverImageView_Gallery.setOnClickListener(this);
		tooglebtn.setOnClickListener(this);
		settingBtn.setOnClickListener(this);
	}
	@Override
	public void onClick(View v) {
		if (v == network_invalid_layout) {
			ActivityController.getInstance().gotoLogin();
		} else if (v == beautifyCamera) {
			int isLibtimeout = SharedPrefsUtil.getValue(mContext,
					SettingKey.LibTimeOut, CartoonLib.SUCESS);
			if (isLibtimeout == CartoonLib.SUCESS) {
				CartoonCameraActivity.launch(mContext,
						CameraType.TIME.ordinal());
			} else {
				//				noteLibTimeout();
			}
		} else if (v == newsCamera) {
			NewsCameraEditorActivity.launch(mContext);
		} else if (v == cartoonCamera) {
			int istimeout = CartoonLib.SUCESS;
			if (true) {
				CartoonCameraActivity.launch(mContext,
						CameraType.CARTOON.ordinal());
			} else {
				noteLibTimeout2();
			}
		} else if (v == CoverImageView_Gallery) {
			// 跳转到相册库
			Intent intent = new Intent(mContext, GalleryActivity.class);
			//			intent.setAction("android.intent.action.vv.camera.photo.main");
			//			 intent.putExtra("CameraGalleryType", CameraGalleryType.Time.ordinal());
			int uploadpicMaxNum = Constants.uploadpicMaxNum;
			try {
				ImageFolder todayFolder = ImageFolderItemManager.getInstance()
						.getImageFolderNow(
								LoginManager.getInstance().getJidParsed());
				if (todayFolder != null) {
					uploadpicMaxNum -= Integer.valueOf(todayFolder
							.getPhotoCount());
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			intent.putExtra(GalleryActivity.GALLERY_CHOOSE_MAX, uploadpicMaxNum);
			intent.putExtra(GalleryActivity.GALLERY_FROM_CAMERA, true);
			intent.putExtra(GalleryActivity.GALLERY_CROP, false);
			startActivity(intent);
		} else if (v == tooglebtn) {
			mSlideHolder.toggle();
		} else if (v == settingBtn) {
			Intent intent = new Intent(mContext, MySettings.class);
			startActivity(intent);
		}
	}
	private void setupSilderbar() {
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
				// refresh the images and days count;
				int[] counts = updataImageCount(yearMap);
				ImageCount.setText(mContext.getResources().getString(
						R.string.timefly_imagesCountString, counts[0]));
				DayCount.setText(mContext.getResources().getString(
						R.string.timefly_daysCountString, counts[1]));
			}
		});
		yearlistview.setAdapter(timeflyGroupsAdapter);
	}
	private void setupContentView() {
		xTimeFlyTraceListView.getEmptydataProcessView().setEmptydataImg(
				R.drawable.timefly_user_nopic);
		xTimeFlyTraceListView.getEmptydataProcessView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (BeemServiceHelper.getInstance(mContext.getApplicationContext()).isAuthentificated()) {
					loadingData();
				}
			}
		});
		xTimeFlyTraceListView.getEmptydataProcessView().setloadEmptyBtn("",
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						CoverImageView_Gallery.performClick();
					}
				});
		xTimeFlyTraceListView.getTimeoutProcessView().setOnReloadListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// 如果列表没有取得，
						String parsedJid = LoginManager.getInstance()
								.getJidParsed();
						new GetPhotoGroupListDlg(mContext, parsedJid,
								new OnGetPGListResult() {
									@Override
									public void onResult(
											Map<String, ImageFolder> yearMap,
											boolean isTimeout) {
										if (yearMap != null
												&& yearMap.size() > 0) {
											timeflyGroupsAdapter
													.addDatas(yearMap);
											// 开始获取前个数据的图片详情
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
						// 如果列表已经取得。
						//loadTimeFlyImage(null, 5);
					}
				});
		/*mTimeFlyAdapter = new VVTimeFlyTracesAdapter(mActivity, xTimeFlyTraceListView.getRefreshableView(),
				false);*/
		mTimeFlyAdapter = new VVTimeFlyTracesAdapter(mContext,
				xTimeFlyTraceListView.getRefreshableView(), false);
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
						loadTimeFlyImage(mTimeFlyAdapter.getStartTime(), 5);
					}
					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView,
							PullType pullType) {
						loadTimeFlyImage(mTimeFlyAdapter.getEndTime(), 5);
					}
				});
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
	@Override
	public void onEventMainThread(EventBusData data) {
		switch (data.getAction()) {
			case LOGIN_SUCCESS: {
				showLoginStatus();
				loadingData();
			}
				break;
			case LOGIN_TIMEOUT:
				showLoginStatus();
				break;
			case LOGIN_FAILED:
				mTimeFlyAdapter.removeItems(mTimeFlyAdapter.getItems());
				xTimeFlyTraceListView.setProcessState(ProcessState.Emptydata);
				showLoginStatus();
				break;
			case NETWORK_ACTIVE:
				showLoginStatus();
				/*if (Boolean.TRUE.equals(data.getMsg())) {
					if (LoginManager.getInstance().isLogined()) {
						loadingData();
					}
				}*/
				break;
			case LOGOUT:
				timeflyGroupsAdapter.clearDatas();
				mTimeFlyAdapter.removeItems(mTimeFlyAdapter.getItems());
				xTimeFlyTraceListView.setProcessState(ProcessState.Emptydata);
				showLoginStatus();
				break;
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
					CToast.showToast(mContext, "上传图片中，请稍候再上传！",
							Toast.LENGTH_SHORT);
					break;
				}
				// 上传图片后刷新界面
				ImageFolderItem newFolderItem = (ImageFolderItem) data.getMsg();
				ArrayList<String> listPath = (ArrayList<String>) data
						.getMsgList();
				int ItemsNumber = mTimeFlyAdapter.getItems().size();
				// LogUtils.i("-- ItemsNumber=" + ItemsNumber);
				// LogUtils.i("--newFolderItem image size=" +
				// newFolderItem.getVVImages().size());
				// LogUtils.i("-- getCreateTime=" +
				// newFolderItem.getImageFolder().getCreateTime());
				for (int i = 0; i < ItemsNumber; i++) {
					ImageFolder imagefolder = newFolderItem.getImageFolder();
					if (mTimeFlyAdapter.getItem(i).getImageFolder()
							.getCreateTime()
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
								.getImageFolder().getCreateTime(),
								newFolderItem.getImageFolder());
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
					CToast.showToast(mContext, "上传图片中，请稍候再上传！",
							Toast.LENGTH_SHORT);
					break;
				}
				ImageFolderItem folderItemCreate = (ImageFolderItem) data
						.getMsg();
				ArrayList<String> listPath = (ArrayList<String>) data
						.getMsgList();
				ImageFolder imagefolder = folderItemCreate.getImageFolder();
				for (int i = 0; i < listPath.size(); i++) {
					VVImage vvImage = new VVImage();
					vvImage.setField(DBKey.jid, folderItemCreate
							.getImageFolder().getJid());
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
				timeflyGroupsAdapter.changeData(folderItemCreate
						.getImageFolder().getCreateTime(), folderItemCreate
						.getImageFolder());
				timeflyGroupsAdapter.notifyDataSetChanged();
				uploadImages(listPath, imagefolder);
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
					private String currentTime;

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
						} else {
							int year = (date.getYear() + 1900) * 365;
							int month = (date.getMonth() + 1) * 30;
							int day = date.getDate();
							int a = Integer.valueOf(ymd[0]) * 365
									+ Integer.valueOf(ymd[1]) * 30
									+ Integer.valueOf(ymd[2]);
							currentTime = a - year - month - day + "天";
						}
						notify_time = new StringBuffer()
						.append(ymd[0])
						.append("-")
						.append(BBSUtils.padStrBefore(
								String.valueOf(ymd[1]), '0', 2))
								.append("-")
								.append(BBSUtils.padStrBefore(
										String.valueOf(ymd[2]), '0', 2))
										.append(" 00:00:00").toString();
						Map<String, Object> rstOne = null;
						Map<String, Object> rstTwo = null;
						try {
							rstOne = TimeflyService
									.managePhotogroup(LoginManager.getInstance()
											.getJidParsed(), imageFolderItem
											.getImageFolder().getGid(),
											imageFolderItem.getImageFolder()
											.getCreateTime(), albumSignEdit
											.toString(), authority);
							rstTwo = TimeflyService
									.setPhotoGroupNotify(imageFolderItem
											.getImageFolder().getGid(),
											imageFolderItem.getImageFolder()
											.getCreateTime(), notify_time,
											valid);
						} catch (WSError e) {
							e.printStackTrace();
							setManulaTimeOut(true);
						}
						return JsonParseUtils.getResult(rstOne)
								&& JsonParseUtils.getResult(rstTwo);
					}

					@Override
					protected void onPostExecute(Boolean result) {
						if (result) {
							ToastCommon toastCommon = ToastCommon.createToastConfig();
							toastCommon.ToastShow(mContext, null,
									LoginManager.getInstance().getMyContact()
											.getSuitableName(), currentTime);
							if (valid.ordinal() == 0) {
								mHandler.postDelayed(new Runnable() {
									@Override
									public void run() {
										final BBSCustomerDialog blurDlg = BBSCustomerDialog
												.newInstance(mContext,
														R.style.blurdialog);
										TimeflyDueRemindView remindview = new TimeflyDueRemindView(
												mContext, true);
										remindview.setText(currentTime);
										remindview.setBtnClickListener(new OnClickListener() {
											@Override
											public void onClick(View v) {
												blurDlg.dismiss();
												Intent i = new Intent(mContext, ShareRankingActivity.class);
												i.putExtra("jid", LoginManager.getInstance().getJidParsed());
												i.putExtra("gid", imageFolderItem
														.getImageFolder()
														.getGid());
												i.putExtra("gidCreatTime", imageFolderItem
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
												EventBus.getDefault().post(data);
											}
										});
										blurDlg.setContentView(remindview.getmView());
										blurDlg.getWindow()
												.setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
										blurDlg.setCancelable(true);
										blurDlg.show();
									}
								}, 60 * 1000);
							} else if (valid.ordinal() == 0) {
							}
						}
					}
				}.execute();
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
			default:
				break;
		}
	}
	/**
	 * @param contact
	 * @param params
	 * @func 获得时光界面图片的详情；
	 */
	private void loadTimeFlyImage(String startTime, int num) {
		if (num == 1) {
			for (int i = 0; i < mTimeFlyAdapter.getItems().size(); i++) {
				// 如果列表中已存在，不需要再加载
				if (mTimeFlyAdapter.getItem(i).getImageFolder().getCreateTime()
						.equals(startTime)) {
					xTimeFlyTraceListView.getRefreshableView().setSelection(
							i + xTimeFlyTraceListView
									.getRefreshableView()
									.getHeaderViewsCount());
					return;
				}
			}
		}
		List<ImageFolder> findWheres = timeflyGroupsAdapter.getImageFolders(
				startTime, num);
		GetPhotoGroupDetailDlg timeFlyAlbumLoadingDialog = new GetPhotoGroupDetailDlg(
				mContext, findWheres, onPhotogroupDetailLis, startTime);
		timeFlyAlbumLoadingDialog.execute();
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
	private void setupLoadingImages() {
		// 添加头部视图
		loading_images = (RelativeLayout) LayoutInflater.from(mContext)
				.inflate(R.layout.loading_images, null);
		xTimeFlyTraceListView.getRefreshableView()
				.addHeaderView(loading_images);
	}
	private void loadImagesFirstTime() {
		Button load_btn = (Button) loading_images
				.findViewById(R.id.loading_btn);
		Button cancel_btn = (Button) loading_images
				.findViewById(R.id.cancel_btn);
		mImageGallery = (HolderTwowayView) loading_images
				.findViewById(R.id.loading_images);
		final String mScanedImg = mSettings.getString(SCANED_IMAGES, "");
		load_btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 保存已经上传的图片
				String loadedImages = mScanedImg + mScanList.toString();
				Editor editor = mSettings.edit();
				editor.putString(SCANED_IMAGES, loadedImages);
				editor.commit();
				Toast.makeText(mContext, "上传", Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent(mContext,
						ShareChangeAlbumAuthorityActivity.class);
				intent2.putStringArrayListExtra(IntentKey.LISTIMAGEPATH,
						loadImageadapter.getSelectItems());
				startActivityForResult(intent2, 100);
				xTimeFlyTraceListView.getRefreshableView().removeHeaderView(
						loading_images);
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
						loading_images);
			}
		});
		mImageGallery.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				view.setSelected(!view.isSelected());
//				String changedPath =  mScanList.get(position);
//				if (view.isSelected()) {
				loadImageadapter.setSelectItem(position);
				
				loadImageadapter.notifyDataSetChanged();
			}
		});
		getImages(mScanedImg);
	}
	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中
	 */
	private void getImages(final String mScanedImg) {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			mHandler.sendEmptyMessage(SCAN_COMPLETE);
			return;
		}
		ThreadUtils.executeTask(new Runnable() {
			@Override
			public void run() {
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = mContext
						.getContentResolver();
				// 只查询jpeg和png的图片
				try {
					Cursor mCursor = mContentResolver.query(
							mImageUri,
							new String[] { MediaColumns.DATA, MediaColumns.SIZE },
							" (" +MediaColumns.MIME_TYPE + "=? or "
									+ MediaColumns.MIME_TYPE + "=?)",
							new String[] { "image/jpeg", "image/png" },
							MediaColumns.DATE_MODIFIED + " desc");
					if (mCursor != null && mCursor.getCount() > 0) {
						// //LogUtils.i("---size=" + mScanList.size());
						while (mCursor.moveToNext()) {
							// 获取图片的路径
							String path = mCursor.getString(mCursor
									.getColumnIndex(MediaColumns.DATA));
							// 添加未上传的图片到mScanList里面
							if (!mScanedImg.contains(path))
								mScanList.add(path);
						}
						if (mCursor != null) {
							mCursor.close();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(SCAN_COMPLETE);
			}
		});
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
	public int updateIndex;

	@Override
	public void autoAuthentificateCompleted() {
		// 开始拉取数据；先拉取yearMap，即所有图片组的日期以及张数等简略信息。
		if (LoginManager.getInstance().isLogined()) {
			loadingData();
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
}
