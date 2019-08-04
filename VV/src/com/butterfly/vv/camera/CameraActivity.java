package com.butterfly.vv.camera;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.manager.ImageFolderItemManager;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity.IntentKey;
import com.beem.project.btf.ui.activity.ClipPictureActivity;
import com.beem.project.btf.ui.activity.ContactInfoActivity.ContactFrgmtStatus;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.FileUtil;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.UIHelper;
import com.btf.push.UserInfoPacket;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.camera.base.CameraBaseListElement;
import com.butterfly.vv.camera.base.DateImageHolder;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotoChoiceChangeListener;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.butterfly.vv.camera.date.DateGridCellGetData;
import com.butterfly.vv.camera.date.GalleryDateListAdapter;
import com.butterfly.vv.camera.date.HalfRoundProgressBar;
import com.butterfly.vv.camera.date.HalfRoundProgressBar.OnSeekChangeListener;
import com.butterfly.vv.camera.photo.PhotoFolderGetData;
import com.butterfly.vv.camera.renew.GroupAdapter;
import com.butterfly.vv.db.ormhelper.bean.ImageFolder;
import com.butterfly.vv.db.ormhelper.bean.VVImage;
import com.butterfly.vv.view.grid.ImageGridAdapter;
import com.vv.image.gallery.viewer.ScrollingViewPager;

import de.greenrobot.event.EventBus;

public class CameraActivity extends VVBaseActivity implements OnClickListener {
	private static final String TAG = CameraActivity.class.getSimpleName();
	private ScrollingViewPager viewPager; // 页卡内容
	private ImageView mCursorImageView; // 动画图片
	// 标题文本 textView1,
	private static final String tag = CameraActivity.class.getSimpleName();
	private TextView textView2, textView3;
	private List<View> views; // Tab页面列表
	private int mPageTitleWidth = 0;
	private int mCurrPageIndex = 0; // 当前页卡编号
	// private View view1, view2, view3, view4; // 各个页卡
	private View view2, view4; // 各个页卡
	public static final String CAMERA_GALLERY_TYPE = "cameraType";
	public static final String GALLERY_CHOOSE_MAX = "GALLERY_CHOOSE_MAX";
	public static final int GALLERY_TYPE_CONTACTCARD = 0;
	public static final int GALLERY_TYPE_TIME = 1;
	public static final int GALLERY_TYPE_REGISTER = 2;
	public static final int GALLERY_TYPE_CHAT = 3;
	public static final int GALLERY_TYPE_NEWSTV = 4;
	public static final int GALLERY_TYPE_NEWSTOPTITLE = 5;
	// request codes.临时方案，日后图库的所有功能将在一个activity完成；
	/**
	 * 向图库深一层层次的请求数据，用来发送数据， 例如：从日期界面进入到图片详情； 从相册界面进入相册详情； 从相册详情进入到图片详情。
	 * 传递参数的同时也有传递CAMERA_GALLERY_TYPE参数
	 */
	public static final int REQUESTCODE_ENTER = 0x1000;
	/**
	 * 从上一层相册返回的数据，此参数为用户选择到正确的图片且确认后的返回。通常情况下会关闭本activity
	 */
	public static final int RESULTCODE_ENTER_CONFIRM = 0x1001;
	public static final String SELECTED_RESULT_KEY = "SELECTED_RESULT_KEY";
	//Time, Register, Chat;
	public static final String CHOOSE_IMAGE_PATH = "CHOOSE_IMAGE_PATH";
	public static final int LIST_REFRESH = 0;
	public static final int LIST_UPDATE_DISPLAY = 1;
	public static final int MSG_SET_VIEWPAGER_ADAPTER = 2;
	public static final int MSG_SET_DATA_POHOH_ADAPTER = 15;
	public static final String IMAGE_VIEW_HOLDER_EXTRA = "image_view_holder_extra";
	public static final String IMAGE_DETAIL_TITLE_STRING = "image_detail_title_string";
	public static final String IMAGE_DETAIL_TYPE = "image_detail_type";
	public static final String IMAGE_DETAIL_CURR_INDEX = "image_detail_curr_index";
	public static final String IMAGE_DETAIL_COUNT = "image_detail_count";
	public static final String IMAGE_DETAIL_IMAGE_LIST = "image_detail_image_list";
	public static final String PREF_NAME_HIDE_PHOTO_FOLDER = "pref_name_hide_photo_folder";
	public static final String PREF_ITEM_FOLDER = "pref_item_folder";
	public static final int TO_HIDE_ACT_REQUEST_CODE = 62;
	public static final int TO_SYSTEM_CAMERA_REQUST_CODE = 63;
	public static final int TO_HIDE_ACT_RESULT_CODE = 82;
	public static final String ContactCardUpload = "1";
	public static final String TimeFlyUpload = "2";
	private static final int CLIP_PICTURE_A = 20;
	private static final int SCAN_OK = 3;
	private static final int CROP_PICTURE = 5;
	// 数组用于放置0-9数字
	private static final int[] DAY_PHOTO_RESIDS = new int[] { R.drawable.pop_0, R.drawable.pop_1, R.drawable.pop_2,
			R.drawable.pop_3, R.drawable.pop_4, R.drawable.pop_5, R.drawable.pop_6, R.drawable.pop_7, R.drawable.pop_8,
			R.drawable.pop_9 };
	/**
	 * 相册适配器
	 */
	private GroupAdapter cameroAdapter;
	/**
	 * 相册的GridView
	 */
	private GridView mGroupGridViewCamero;
	// 日期ListView
	public ListView dateListView;
	// 日期 适配器
	public GalleryDateListAdapter dateListAdapter;
	private List<DateImageHolder> mDateImageList;
	// 相册ListView适配器
	private boolean mIsPhotoDisplaySmallImage = false;
	public List<ImageFolderInfo> mFoldInfoList = new ArrayList<ImageFolderInfo>();
	public LinearLayout mLoadingLayout;
	// 导航栏控键
	private GalleryNavigation mMyNavigationView;
	private MyNewCheckListener mNewCheckListener;
	private MyPhotoCheckListener mPhotoCheckListener;
	// 弹出窗口
	private Button mPopSeekButton; // Date遮罩Button
	private PopupWindow mPopupWindow;
	private View mPopWindowView;
	private HalfRoundProgressBar mRoundSeekBar;
	private RoundSeekBarChangeListener mRoundSeekBarListener;
	private static int mCurrPopviewPos = 0;
	private static int mCurrRoundProgress = 0;
	public static int mDateGvState = 0;
	public static final int DATE_GV_UP = 0;
	public static final int DATE_GV_DOWN = 1;
	private int mDateGvFirstVisibleItem = 0;
	private int mDateGvScrollState = 0;
	private ArrayList<VVImage> tempVvImages = new ArrayList<VVImage>();
	private ImageGridAdapter mAlbumGridAdapter;
	private UserInfoPacket modifyPacket = new UserInfoPacket();
	public int cameraType;
	private DateRoundHolder mDrh = null;
	private static final int MSG_NEW = 0;
	private static final int MSG_DATE = 1;
	private static final int MSG_PHOTO = 2;
	private static final int MSG_FOLDERS = 3;
	private List<CameraBaseListElement> mNewListData;
	private List<ImageInfoHolder> mNewImageInfo = new ArrayList<ImageInfoHolder>();
	private List<CameraBaseListElement> mPhotoListData = new ArrayList<CameraBaseListElement>();
	private List<ImageFolderInfo> listfolder;
	private Intent intent;
	private DateGridCellGetData calendarDataGetter;
	private PhotoFolderGetData folderAlbumDataGetter;
	private PhotosChooseManager chooser;
	private Uri photoUri;
	/** 从Intent获取图片路径的KEY */
	public static final String KEY_PHOTO_PATH = "photo_path";
	// 主线程更新数据到UI
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
				case LIST_REFRESH:
					if (msg.arg1 == MSG_DATE) {
						if (mDateImageList != null) {
							//							dateListView.onRefreshComplete();
							dateListAdapter.setData(mDateImageList);
							dateListAdapter.notifyDataSetInvalidated();
						}
					}
					if (msg.arg1 == MSG_FOLDERS) {
						if (mDateImageList != null) {
							//							dateListView.onRefreshComplete();
							cameroAdapter.setDataList(listfolder);
							cameroAdapter.notifyDataSetChanged();
						}
					}
					updateTitleTextImage();
					break;
				case LIST_UPDATE_DISPLAY:
					if (msg.arg1 == MSG_NEW) {
					} else if (msg.arg1 == MSG_DATE) {
						if (mDateImageList != null) {
							handler.sendEmptyMessage(SCAN_OK);
							dateListAdapter.setData(mDateImageList);
							dateListAdapter.notifyDataSetChanged();
						}
						// 通知Handler扫描图片完成
						// dateListView.onRefreshComplete();
					} else if (msg.arg1 == MSG_FOLDERS) {
						if (listfolder != null) {
							cameroAdapter.setDataList(listfolder);
							cameroAdapter.notifyDataSetChanged();
						}
					}
					break;
				case MSG_SET_VIEWPAGER_ADAPTER:
					// ViewPager设置适配器
					viewPager.setAdapter(new MyViewPagerAdapter(views));
					// ViewPager设置当前条目
					viewPager.setCurrentItem(mCurrPageIndex);
					// ViewPager设置页面监听事件
					viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
					break;
				case SCAN_OK:
					// 关闭进度条
					UIHelper.hideDialogForLoading();
					break;
				case CROP_PICTURE:
					//进入剪切界面
					String path = (String) msg.obj;
					Uri cropPhotoUri = Uri.fromFile(new File(path));
					PictureUtil.photoClip(CameraActivity.this, cropPhotoUri, 900, 602);
					break;
				default:
					break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.xc_main_page);
		ImageLoaderConfigers.initThumbImageFetcher(this);
		// 显示进度条
		UIHelper.showDialogForLoading(mContext, getString(R.string.xlistview_header_hint_loading), false);
		intent = getIntent();
		cameraType = intent.getIntExtra(CAMERA_GALLERY_TYPE, -1);
		mNewCheckListener = new MyNewCheckListener();
		mPhotoCheckListener = new MyPhotoCheckListener();
		mAlbumGridAdapter = new ImageGridAdapter((Activity) mContext, tempVvImages, ContactFrgmtStatus.EditContactInfo);
		initPopupWindow();
		calendarDataGetter = DateGridCellGetData.getInstance(this);
		folderAlbumDataGetter = PhotoFolderGetData.getInstance();
		initNavigateView();
		initViewPager();
		loadDateImagesData();
		loadFolderImageData();
		initImagesChooser();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	private void initImagesChooser() {
		chooser = PhotosChooseManager.getInstance();
		int uploadpicMaxNum = getIntent().getIntExtra(GALLERY_CHOOSE_MAX, 0);
		if (uploadpicMaxNum == 0) {
			switch (cameraType) {
				case GALLERY_TYPE_CONTACTCARD: {
					uploadpicMaxNum = 1;
				}
					break;
				case GALLERY_TYPE_TIME: {
					uploadpicMaxNum = 1;
				}
					break;
				case GALLERY_TYPE_REGISTER:
					uploadpicMaxNum = 1;
					break;
				case GALLERY_TYPE_CHAT:
					uploadpicMaxNum = 1;
					break;
				case GALLERY_TYPE_NEWSTV:
					uploadpicMaxNum = 1;
					break;
				case GALLERY_TYPE_NEWSTOPTITLE:
					uploadpicMaxNum = 1;
					break;
				default: {
					uploadpicMaxNum = Constants.uploadpicMaxNum;
					try {
						ImageFolder todayFolder = ImageFolderItemManager.getInstance().getImageFolderNow(
								LoginManager.getInstance().getJidParsed());
						if (todayFolder != null) {
							uploadpicMaxNum -= Integer.valueOf(todayFolder.getPhotoCount());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
					break;
			}
		}
		chooser.resetChooseCountMax(uploadpicMaxNum);
	}
	private void initNavigateView() {
		mMyNavigationView = (GalleryNavigation) findViewById(R.id.main_navigation_view);
		mMyNavigationView.setBtnLeftListener(this);
		mMyNavigationView.setBtnUploadListener(this);
		mMyNavigationView.setCameraListener(this);
		mMyNavigationView.inflateCenter(this);
		mMyNavigationView.setStringTitle(getString(R.string.tabtitle_photo), null);
		mMyNavigationView.setTopbarTab(0);
		if (cameraType == GALLERY_TYPE_TIME) {
			mMyNavigationView.setCameraBtnHide(true);
		}
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.TAKEPHOTO && resultCode == Activity.RESULT_OK) { // return from this app's camera; 
//			String videoPath = BBSUtils.getTakePhotoPath(this, photoUri, data);
//			Log.d(TAG, "onCameraResult "+ data.getDataString());
			String videoPath = FileUtil.getCameraPhotoUri(this).getPath();
			if (videoPath != null) {
				switch (cameraType) {
					case GALLERY_TYPE_CONTACTCARD:
						ClipPictureActivity.launch(CameraActivity.this, videoPath, CLIP_PICTURE_A);
						finish();
						break;
					case GALLERY_TYPE_NEWSTV: {
						PictureUtil.photoClip(this, photoUri, 900, 602);
						//						PictureUtil.photoClipInner(this, videoPath, 0.6666667f);
						//finish();
						break;
					}
					case GALLERY_TYPE_NEWSTOPTITLE: {
						EventBus.getDefault().post(new EventBusData(EventAction.SendUnClipPhoto, videoPath));
						finish();
						break;
					}
					case GALLERY_TYPE_TIME: {
						Intent result = new Intent();
						ArrayList<String> list = new ArrayList<String>();
						for (ImageInfoHolder item : chooser.getChoosedSet()) {
							list.add(item.mImagePath);
						}
						result.putStringArrayListExtra(SELECTED_RESULT_KEY, list);
						setResult(Activity.RESULT_OK, result);
						finish();
					}
						break;
					case GALLERY_TYPE_REGISTER:
						ClipPictureActivity.launch(CameraActivity.this, videoPath, CLIP_PICTURE_A, true);
						finish();
						break;
					case GALLERY_TYPE_CHAT:
						EventBus.getDefault().post(new EventBusData(EventAction.SendPathTChat, videoPath));
						finish();
						break;
					default: {
						ArrayList<String> listimagePath = new ArrayList<String>();
						listimagePath.add(videoPath);
						Intent intent2 = new Intent(CameraActivity.this, ShareChangeAlbumAuthorityActivity.class);
						intent2.putStringArrayListExtra(IntentKey.LISTIMAGEPATH, listimagePath);
						startActivity(intent2);
						finish();
					}
						break;
				}
			}
		} else if (requestCode == REQUESTCODE_ENTER) {
			// 从更深层图库界面，例如相册详情，照片详情，返回的信息。
			// 当返回数据的为确认图片状态时，退出本界面，且返回一个数据给调用者
			if (resultCode == RESULTCODE_ENTER_CONFIRM) {
				switch (cameraType) {
					case GALLERY_TYPE_TIME: {
						Intent result = new Intent();
						ArrayList<String> list = new ArrayList<String>();
						for (ImageInfoHolder item : chooser.getChoosedSet()) {
							list.add(item.mImagePath);
						}
						result.putStringArrayListExtra(SELECTED_RESULT_KEY, list);
						setResult(Activity.RESULT_OK, result);
						finish();
					}
						break;
					case GALLERY_TYPE_NEWSTV: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null && !checkListimagespath.isEmpty()) {
							String imgepath = checkListimagespath.iterator().next().mImagePath;
							Message msg = handler.obtainMessage(CROP_PICTURE);
							if (msg == null) {
								msg = new Message();
								msg.what = CROP_PICTURE;
							}
							msg.obj = imgepath;
							handler.sendMessage(msg);
						}
						break;
					}
					case GALLERY_TYPE_NEWSTOPTITLE: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							EventBus.getDefault().post(new EventBusData(EventAction.SendUnClipPhoto, path));
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
						finish();
					}
						break;
					default:
						finish();
						break;
				}
			}
		} else if (requestCode == Constants.CLIPPHOTO) {
			//调用系统剪切功能的返回接收
			if (resultCode == RESULT_OK) {
				EventBus.getDefault().post(new EventBusData(EventAction.SendClipPhoto, photoUri));
			}
			setResult(RESULT_OK);
			finish();
		}
	}
	private void loadDateImagesData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				getDateListData();
				displayListViewAfterGetData(MSG_DATE);
				//				updatePhotoScreen();
			}
		}, "loadDateImagesData").start();
	}
	private void loadFolderImageData() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				listfolder = folderAlbumDataGetter.getData(CameraActivity.this, true);
				displayListViewAfterGetData(MSG_FOLDERS);
				//				updatePhotoScreen();
			}
		}, "loadFolderImageData").start();
	}
	private void displayListViewAfterGetData(int page) {
		Message msg = handler.obtainMessage();
		msg.what = LIST_UPDATE_DISPLAY;
		msg.arg1 = page;
		handler.sendMessage(msg);
	}
	/**
	 * 日期测试数据 日期数据集合展示
	 * @return
	 */
	private void getDateListData() {
		mDateImageList = calendarDataGetter.getPathAllDateImageInfo2();
	}
	// TODO
	@Override
	protected void onRestart() {
		super.onRestart();
		updateTitleTextImage();
		if (mCurrPageIndex == 0) {
			dateListAdapter.checkAndSetMarkStatus();
			dateListAdapter.notifyDataSetChanged();
		}
	}
	private void initPopupWindow() {
		mDrh = new DateRoundHolder();
		LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mPopWindowView = inflater.inflate(R.layout.xc_popwindow, null);
		mRoundSeekBar = (HalfRoundProgressBar) mPopWindowView.findViewById(R.id.half_round_seek_bar);
		mRoundSeekBarListener = new RoundSeekBarChangeListener();
		mRoundSeekBar.setSeekBarChangeListener(mRoundSeekBarListener);
		mDrh.ten = (ImageView) mPopWindowView.findViewById(R.id.ten); // 初始化十位
		mDrh.single = (ImageView) mPopWindowView.findViewById(R.id.single); // 初始化个位
		mDrh.monyearText = (TextView) mPopWindowView.findViewById(R.id.showmonthyear);
		mDrh.timeDuration = (TextView) mPopWindowView.findViewById(R.id.date_duration);
		mDrh.photoCount = (TextView) mPopWindowView.findViewById(R.id.image_count_tv);
		mDrh.hourImage = (ImageView) mPopWindowView.findViewById(R.id.left_date_hour);
		mDrh.minImage = (ImageView) mPopWindowView.findViewById(R.id.left_date_min);
		mPopWindowView.setFocusable(true);
		mPopWindowView.setFocusableInTouchMode(true);
		mPopWindowView.setOnKeyListener(new OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if (keyCode == KeyEvent.KEYCODE_BACK) {
					dismisssPopupWindow();
					return true;
				}
				return false;
			}
		});
		mPopWindowView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_UP) {
					if (isTouchNotInPopupArea(event)) {
						dismisssPopupWindow();
						return true;
					}
				}
				return false;
			}
		});
	}
	/** 展示弹出窗口 */
	private void showPopWindow(Context context, View parent) {
		/*
		 * mDateGvIndicator.setVisibility(View.GONE); mDateGv.setVisibility(View.GONE);
		 */
		if (mDateImageList == null && mDateImageList.isEmpty()) {
			return;
		}
		int currPos = 0;
		int firstPos = dateListView.getFirstVisiblePosition();
		int lastPos = dateListView.getLastVisiblePosition();
		int dateCount = mDateImageList.size();
		// 防止除数为0的情况. Prevent dividing by zero
		if (dateCount == 0) {
			return;
		}
		if (mCurrPopviewPos >= firstPos && mCurrPopviewPos <= lastPos) {
			currPos = mCurrPopviewPos;
		} else {
			currPos = firstPos;
		}
		if (currPos >= mDateImageList.size()) {
			currPos = mDateImageList.size() - 1;
		}
		int seekProgress = 0;
		if (mRoundSeekBar != null) {
			int maxProgress = 0;
			if (dateCount > 100) {
				maxProgress = 1000;
			} else {
				maxProgress = 100;
			}
			mRoundSeekBar.setMaxProgress(maxProgress);
			seekProgress = (currPos * maxProgress) / dateCount;
			mRoundSeekBar.setProgress(seekProgress);
		}
		mCurrPopviewPos = currPos;
		DateImageHolder currDateHolder = mDateImageList.get(currPos);
		setDatePopupView(currDateHolder);
		DisplayMetrics dm = getResources().getDisplayMetrics();
		int paddingTopBtm = (int) getResources().getDimension(R.dimen.round_seekbar_padding_top_bottom);
		int popWidth = dm.widthPixels;
		int popHeight = popWidth / 2 + paddingTopBtm;
		mPopupWindow = new PopupWindow(mPopWindowView, popWidth, popHeight, true);
		mPopupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);
		mPopupWindow.setFocusable(false);
		mPopupWindow.setOutsideTouchable(true);
	}
	private void updatePopupView(int newPos) {
		if (mDateImageList != null && mCurrPopviewPos != newPos) {
			mCurrPopviewPos = newPos;
			DateImageHolder currDateHolder = mDateImageList.get(newPos);
			setDatePopupView(currDateHolder);
		}
	}
	private void updatePopupClockView(int newProgress) {
		if (mCurrRoundProgress == newProgress) {
			return;
		}
		mCurrRoundProgress = newProgress;
		float minDegree = -(mCurrRoundProgress % 4) * 90; // 负数表示逆时针旋转，时光倒流
		float hourDegree = -(mCurrRoundProgress % 6) * 60;
		int hourWidth = mDrh.hourImage.getWidth();
		int hourHeight = mDrh.hourImage.getHeight();
		Matrix hMatrix = new Matrix();
		hMatrix.setRotate(hourDegree, hourWidth / 2, hourHeight / 2);
		mDrh.hourImage.setImageMatrix(hMatrix);
		int minWidth = mDrh.minImage.getWidth();
		int minHeight = mDrh.minImage.getHeight();
		Matrix mMatrix = new Matrix();
		mMatrix.setRotate(minDegree, minWidth / 2, minHeight / 2);
		mDrh.minImage.setImageMatrix(mMatrix);
		// mDrh.hourImage.setRotation(hourDegree); //2.3上没有这两个方法
		// mDrh.minImage.setRotation(minDegree);
	}
	private void setDatePopupView(DateImageHolder holder) {
		if (holder.mDateString.length() < ImageInfoHolder.BASE_DATE_LENGTH) {
			mDrh.year = -1;
			mDrh.month = -1;
			mDrh.date = -1;
			changIntToImage(mDrh.date); // 更改天数为ImageView
			mDrh.monyearText.setText("00" + getResources().getString(R.string.str_month) + "0000"
					+ getResources().getString(R.string.str_year));
			mDrh.photoCount.setText(holder.mDateImageList.size() + getResources().getString(R.string.str_img_piece));
			mDrh.timeDuration.setText(R.string.str_detail_unknown);
		} else {
			Date dt = null;
			try {
				dt = MyDateFormat.getDateByString(holder.mDateString, "yyyy:MM:dd");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			int year, month, date, hour, min, second;
			long passMills = 0;
			if (dt != null) {
				year = dt.getYear();
				month = dt.getMonth();
				mDrh.date = dt.getDate();
				mDrh.year = year + 1900;
				mDrh.month = month + 1;
				changIntToImage(mDrh.date);
				mDrh.monyearText.setText(mDrh.month + getResources().getString(R.string.str_month) + mDrh.year
						+ getResources().getString(R.string.str_year));
				mDrh.photoCount
						.setText(holder.mDateImageList.size() + getResources().getString(R.string.str_img_piece));
				Date dt2 = new Date(System.currentTimeMillis());
				Date dt3 = new Date(dt2.getYear(), dt2.getMonth(), dt2.getDate(), 23, 59, 59);
				passMills = dt3.getTime() - dt.getTime();
				String durationString = MyDateFormat.getIntervalDayFromMillis(passMills);
				mDrh.timeDuration.setText(durationString);
			}
		}
	}
	private boolean isTouchNotInPopupArea(MotionEvent event) {
		// 这里好像Y小于0是点击popup window区域之外
		if (event.getY() < 0) {
			return true;
		} else {
			return false;
		}
	}
	/*
	 * function changIntToImage 将数字天数转换成图片
	 */
	private void changIntToImage(int dayOfMonth) {
		switch (dayOfMonth) {
			case -1:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 1:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 2:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 3:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 4:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 5:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 6:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 7:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 8:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 9:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[0]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 10:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 11:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 12:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 13:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 14:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 15:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 16:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 17:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 18:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 19:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[1]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 20:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 21:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			case 22:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[2]);
				break;
			case 23:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[3]);
				break;
			case 24:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[4]);
				break;
			case 25:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[5]);
				break;
			case 26:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[6]);
				break;
			case 27:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[7]);
				break;
			case 28:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[8]);
				break;
			case 29:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[2]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[9]);
				break;
			case 30:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[3]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[0]);
				break;
			case 31:
				mDrh.ten.setImageResource(DAY_PHOTO_RESIDS[3]);
				mDrh.single.setImageResource(DAY_PHOTO_RESIDS[1]);
				break;
			default:
				break;
		}
	}
	private void updateTitleTextColor() {
		int titleSelectColor = getResources().getColor(R.color.new_title_bg);
		int titleNormalColor = getResources().getColor(R.color.tab_scr_text_color);
		switch (mCurrPageIndex) {
			case 2:
				// textView1.setTextColor(titleSelectColor); // 文本框选中变色
				textView2.setTextColor(titleNormalColor); // 未选中文本框还原
				textView3.setTextColor(titleNormalColor); // 未选中文本框还原
				break;
			case 0:
				textView2.setTextColor(titleSelectColor); // 文本框选中变色
				// textView1.setTextColor(titleNormalColor); // 未选中文本还原
				textView3.setTextColor(titleNormalColor); // 未选中文本还原
				break;
			case 1:
				textView3.setTextColor(titleSelectColor); // 文本框选中变色
				// textView1.setTextColor(titleNormalColor); // 未选中文本还原
				textView2.setTextColor(titleNormalColor); // 未选中文本还原
				break;
			default:
				break;
		}
	}

	/* 头标点击监听 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		// 文本框点击事件
		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	// 初始化ViewPager
	private void initViewPager() {
		views = new ArrayList<View>();
		LayoutInflater inflater = getLayoutInflater();
		viewPager = (ScrollingViewPager) findViewById(R.id.viewPager);
		// 隐藏最新
		// views.add(view1);
		// 初始化日期页面
		view2 = inflater.inflate(R.layout.xc_lay2, null);
		/** 分页ListView */
		dateListView = (ListView) view2.findViewById(R.id.autolist1);
		dateListView.setDivider(null);
		dateListView.setDividerHeight(0);
		dateListView.setEmptyView(view2.findViewById(android.R.id.empty));
		dateListAdapter = new GalleryDateListAdapter(CameraActivity.this);
		dateListAdapter.setPhotoCheckedListener(mDatePhotoCheckedListener);
		dateListView.setAdapter(dateListAdapter);
		// 初始化遮罩按钮
		mPopSeekButton = (Button) view2.findViewById(R.id.popup_seek_button);
		// 初始化GridView
		// if (mDateImageList != null) {
		// mDateGridAdapter = new DateGridFrameAdapter(CameraActivity.this, mDateImageList);
		// mDateGv.setAdapter(mDateGridAdapter);
		// }
		// DateGridScrollListener listener = new DateGridScrollListener();
		// mDateGv.setOnScrollListener(listener);
		views.add(view2);
		view4 = inflater.inflate(R.layout.xc_lay4, null);
		mGroupGridViewCamero = (GridView) view4.findViewById(R.id.main_grid);
		mGroupGridViewCamero.setEmptyView(view4.findViewById(android.R.id.empty));
		cameroAdapter = new GroupAdapter(CameraActivity.this, listfolder);
		mGroupGridViewCamero.setAdapter(cameroAdapter);
		mGroupGridViewCamero.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				//				List<String> childList = mGruopMapFolderInfo.get(listfolder.get(position).getmFolderName());
				// Intent mIntent = new
				// Intent(CameraActivity.this,
				// PhotoGridViewActivity.class);
				Intent mIntent = new Intent(getApplicationContext(), ShowImageActivity.class);
				//				mIntent.putStringArrayListExtra("data", listfolder.get(position).getmFolderName());
				mIntent.putExtra("cameraType", cameraType);
				mIntent.putExtra("transfer_photo_folder_string", listfolder.get(position));
				mIntent.putExtra("dopick", getIntent().getBooleanExtra("dopick", false));
				startActivityForResult(mIntent, REQUESTCODE_ENTER);
			}
		});
		views.add(view4);
		// 初始化相册页面这段代码暂时隐藏，以后要有升级以作它用。
		if (mIsPhotoDisplaySmallImage) {
			// mPhotoDisplaySmallIb.setImageResource(R.drawable.xc_not_display_small);
			// PhotoCell.setExceptSmallImageSize(0);
		} else {
			// mPhotoDisplaySmallIb.setImageResource(R.drawable.xc_display_small);
			// PhotoCell.setExceptSmallImageSize(PhotoCell.EXCEPT_SMALL_IMG_SIZE_KB);
		}
		// ViewPager设置适配器
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		// ViewPager设置当前条目
		viewPager.setCurrentItem(mCurrPageIndex);
		// ViewPager设置页面监听事件
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 日期刷新加载
		mPopSeekButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				showPopWindow(CameraActivity.this, view2);
			}
		});
	}
	// 关闭PopupWindow，判断是否存在，存在就把它dismiss掉
	private void dismisssPopupWindow() {
		if (mPopupWindow != null) {
			mPopupWindow.dismiss();
			mPopupWindow = null;
		}
	}
	// 20150619 activity 不主动销毁，压入task。主动跳转到主页
	private void backHome() {
		if (cameraType == -1) {
			finish();
		} else {
			finish();
			chooser.exitChooseMode();
		}
	}
	@Override
	public void onBackPressed() {
		onBack();
	}
	private boolean onBack() {
		if (mCurrPageIndex == 0) {
			// 相册界面特殊处理
			if (mPopupWindow != null && mPopupWindow.isShowing()) {
				dismisssPopupWindow();
				return true;
			}
		}
		if (chooser.isChooseMode()) {
			chooser.exitChooseMode();
			dateListAdapter.resetMarkModeStatus();
			updateTitleTextImage();
			return true;
		}
		finish();
		return true;
	}
	@Override
	protected void onPause() {
		dismisssPopupWindow();
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		chooser.exitChooseMode();
		ThumbImageFetcher.destroy();
		handler.removeCallbacksAndMessages(null);
	}

	// ViewPager适配器
	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> views;

		public MyViewPagerAdapter(List<View> pViews) {
			super();
			this.views = pViews;
		}
		// 销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(views.get(position));
		}
		@Override
		public int getCount() {
			return views.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			if (views.get(position).getParent() == null) {
				container.addView(views.get(position));
			}
			return views.get(position);
		}
	}

	// ViewPager监听事件
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
			// 虽然这个比较简洁，只有一行代码
			//			Animation animation = new TranslateAnimation(mPageTitleWidth * mCurrPageIndex, mPageTitleWidth * arg0, 0, 0);
			mCurrPageIndex = arg0;
			//			updateTitleTextColor();
			switch (arg0) {
				case 0:
					viewPager.setCurrentItem(mCurrPageIndex);// 显示第一页
					break;
				case 1:
					//dateListView.setCurrentViewPage(mCurrPageIndex);
					break;
				case 2:
					break;
				default:
					break;
			}
			updateTitleTextImage();
			//animation.setFillAfter(true); // True图片停在动画结束位置
			//animation.setDuration(300);
			//mCursorImageView.startAnimation(ani;mation);
			mMyNavigationView.setTopbarTab(arg0);
		}
	}

	@TargetApi(11)
	private void updateDatePhotoListView(int newPos) {
		if (mDateImageList == null) {
			return;
		}
		int dateCount = mDateImageList.size();
		if (newPos >= dateCount) {
			newPos = dateCount - 1;
		}
		int pos = dateListAdapter.getTopIndexOfDate(newPos);
		if (dateListView != null) {
			dateListView.setSelected(true);
			if (Utils.getAndroidSDKVersion() >= 11) {
				dateListView.smoothScrollToPositionFromTop(pos, 5);
			} else {
				dateListView.smoothScrollToPosition(pos);
			}
			//dateListView.setSelection(newPos);
		}
	}

	public class RoundSeekBarChangeListener implements OnSeekChangeListener {
		@Override
		public void onProgressChange(HalfRoundProgressBar view, int newProgress) {
			if (mDateImageList == null) {
				return;
			}
			int maxProgress = view.getMaxProgress();
			int dateCount = mDateImageList.size();
			int newPos = (newProgress * dateCount) / maxProgress;
			if (newPos >= dateCount) {
				newPos = dateCount - 1;
			}
			updatePopupView(newPos);
			updateDatePhotoListView(newPos);
			updatePopupClockView(newProgress);
		}
	}

	private class MyNewCheckListener implements PhotoChoiceChangeListener {
		@Override
		public void onPhotoChoiceChange() {
			updateTitleTextImage();
		}
	}

	PhotoChoiceChangeListener mDatePhotoCheckedListener = new PhotoChoiceChangeListener() {
		@Override
		public void onPhotoChoiceChange() {
			updateTitleTextImage();
		}
	};

	private void updateTitleTextImage() {
		if (chooser.isChooseMode()) {
			String choiseText;
			if (cameraType == -1) {
				choiseText = getResources().getString(R.string.str_had_selected, chooser.getChoosedCount(),
						chooser.getChooseCountMax());
			} else {
				choiseText = getString(R.string.login_ok);
			}
			mMyNavigationView.setChoiseMode(choiseText);
			viewPager.setScanScroll(false);
		} else {
			mMyNavigationView.cancelChoiseMode();
			viewPager.setScanScroll(true);
		}
	}
	public MyPhotoCheckListener getMyPhotoCheckListener() {
		return mPhotoCheckListener;
	}

	public class MyPhotoCheckListener implements PhotoChoiceChangeListener {
		@Override
		public void onPhotoChoiceChange() {
			updateTitleTextImage();
		}
	}

	public void newListCheckGoBack() {
		int selectedCount = dateListAdapter.getMarkedImageCount();
		if (selectedCount == 0) {
			// finish();
			backHome();
		} else {
			dateListAdapter.resetMarkModeStatus();
			updateTitleTextImage();
		}
	}
	public void dateListCheckGoBack() {
		boolean isMarkMode = dateListAdapter.getMarkModeStatus();
		if (isMarkMode == false) {
			backHome();
		} else {
			dateListAdapter.resetMarkModeStatus();
			updateTitleTextImage();
			dateListAdapter.notifyDataSetChanged();
		}
	}

	class DateGridScrollListener implements OnScrollListener {
		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			mDateGvFirstVisibleItem = firstVisibleItem;
		}
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			int oldState = mDateGvScrollState;
			mDateGvScrollState = scrollState;
			// 日期相册跟随Date List滑动
			if ((oldState == SCROLL_STATE_FLING && mDateGvScrollState == SCROLL_STATE_IDLE)
					|| (oldState == SCROLL_STATE_TOUCH_SCROLL && mDateGvScrollState == SCROLL_STATE_IDLE)) {
				int pos = mDateGvFirstVisibleItem + 1;
				updateDatePhotoListView(pos);
			}
		}
	}

	private boolean checkIsMarkedMode() {
		boolean mode = false;
		if (mCurrPageIndex == 0) {
			if (dateListAdapter != null) {
				mode = dateListAdapter.getMarkModeStatus();
			}
		}
		return mode;
	}

	private static boolean mIsStopMaintainThread = false;
	public static int mMTFirstVisibleItem = 0;
	private static List<ImageFolderInfo> mTopImageFolderList = new ArrayList<ImageFolderInfo>();
	private static List<ImageFolderInfo> mAllImageFolderList = new ArrayList<ImageFolderInfo>();
	private static String mDcimPath = null;
	private static String mCameraPath = null;
	private static String mVVPath = null;
	private static String mLatestImagePath = null;
	private List<String> listPath;

	// 环形日期显示容器
	private class DateRoundHolder {
		private TextView monyearText; // 显示年月
		private int year; // 年
		private int month; // 月
		private int date; // 日
		private ImageView ten;// 日期十位数
		private ImageView single;// 日期个位数
		private TextView timeDuration;
		private TextView photoCount;
		private ImageView hourImage;
		private ImageView minImage;
	}

	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvw_topbar_title: {
				onBack();
			}
				break;
			case R.id.btn_topbar_upload: {
				//if (mCurrPageIndex == 0) {
				switch (cameraType) {
					case GALLERY_TYPE_CONTACTCARD: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							ClipPictureActivity.launch(CameraActivity.this, path, CLIP_PICTURE_A);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
					}
						break;
					case GALLERY_TYPE_NEWSTV: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							Message msg = handler.obtainMessage(CROP_PICTURE);
							if (msg == null) {
								msg = new Message();
								msg.what = CROP_PICTURE;
							}
							msg.obj = path;
							handler.sendMessage(msg);
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
					}
						break;
					case GALLERY_TYPE_NEWSTOPTITLE: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							EventBus.getDefault().post(new EventBusData(EventAction.SendUnClipPhoto, path));
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
						finish();
						break;
					}
					case GALLERY_TYPE_TIME: {
						Intent result = new Intent();
						ArrayList<String> list = new ArrayList<String>();
						for (ImageInfoHolder item : chooser.getChoosedSet()) {
							list.add(item.mImagePath);
						}
						result.putStringArrayListExtra(SELECTED_RESULT_KEY, list);
						setResult(Activity.RESULT_OK, result);
						finish();
					}
						break;
					case GALLERY_TYPE_REGISTER: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							ClipPictureActivity.launch(CameraActivity.this, path, CLIP_PICTURE_A, true);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
					}
						break;
					case GALLERY_TYPE_CHAT: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String imgepath = checkListimagespath.iterator().next().mImagePath;
							EventBus.getDefault().post(new EventBusData(EventAction.SendPathTChat, imgepath));
							finish();
						}
					}
						break;
					default: {
						Intent intent2 = new Intent(CameraActivity.this, ShareChangeAlbumAuthorityActivity.class);
						startActivity(intent2);
					}
						break;
				}
			}
				break;
			case R.id.btn_topbar_camera: {
				photoUri = BBSUtils.takePhoto(CameraActivity.this, Constants.TAKEPHOTO);
			}
				break;
			case R.id.tvw_gallery_topbar_tab0:
				if (!PhotosChooseManager.getInstance().isChooseMode()) {
					viewPager.setCurrentItem(0);
				}
				break;
			case R.id.tvw_gallery_topbar_tab1:
				if (!PhotosChooseManager.getInstance().isChooseMode()) {
					viewPager.setCurrentItem(1);
				}
				break;
			default:
				break;
		}
	}
}
