package com.butterfly.vv.camera.renew;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.camera.CameraActivity;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.activity.ClipPictureActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.butterfly.vv.camera.Utils;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;
import de.greenrobot.event.EventBus;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 图片美化，上传界面
 * @ClassName: RenewDetailBaseActivity
 * @Description: TODO
 * @author: zhenggen xie
 * @date: 2015年3月16日 上午10:32:56
 */
public class RenewDetailBaseActivity extends Activity {
	private static final String TAG = "RenewDetailBaseActivity";
	//	private boolean mIsShowTitleAndBtmBar = false;
	//	private ImageView mTitleLeftImg; // 标题栏左边按钮
	//	private ImageView mTitleRightImg; // 标题栏右边按钮
	private ImageInfoHolder mImageInfoHolder;
	private String mNormalTitleString;
	private String mTitleString;
	public static String IMAGE_VIEW_HOLDER_EXTRA = "image_view_holder_extra";
	public static String IMAGE_DETAIL_TITLE_STRING = "image_detail_title_string";
	// 最新详情页面
	private RelativeLayout mDetailMainLayout;
	private PhotoView mDetailImage;
	//	private Bitmap mTempBm;
	private RelativeLayout mBottomBtnLayout;
	//	private ImageView upload;
	//	private ImageView beautiful;
	private TextView tvw_detail_rotate;
	private CheckBox check_detail_selected;
	// 详细信息
	//	private RelativeLayout mDetailInfoLayout;
	private boolean mIsDetailInfoScreen = false;
	public static final int CLICK_DISTANCE = 10;
	private static List<ImageInfoHolder> mBigImageList;
	private GalleryNavigation mDetailMainTitleBar;
	private ViewPager mBigImageViewPager;
	private BigImageViewPagerAdapter mBigImagePagerAdapter;
	//	private List<View> mBigPagerViews;
	private PhotosChooseManager chooser;
	//	public static HashMap<Integer, Bitmap> mBmMap = new HashMap<Integer, Bitmap>();
	//	public static List<Boolean> mBmChecked = new ArrayList<Boolean>();
	//	private static boolean mIsStopThread = false;
	boolean dopick = false;
	private int mCurrImageCount = 0;
	private int mAllImageCount = 0;
	private int mCurrPageIndex = 0;
	private int mCurrBigImageType = -1;
	public static final int NEW_LIST_BIG_IMAGE_TYPE = 0;
	public static final int DATE_GRID_BIG_IMAGE_TYPE = 1;
	public static final int PHOTO_GRID_BIG_IMAGE_TYPE = 2;
	private Uri photoUri;

	/*class ImageDetailHolder {
		LinearLayout layoutShootTime;
		LinearLayout layoutModifyTime;
		LinearLayout layoutShootPlace;
		LinearLayout layoutFileName;
		LinearLayout layoutFileMetrics;
		LinearLayout layoutFileSize;
		LinearLayout layoutDeviceComp;
		LinearLayout layoutDeviceName;
		LinearLayout layoutFlashState;
		LinearLayout layoutFocusDistance;
		LinearLayout layoutWhiteBanlance;
		LinearLayout layoutPhoteAperture;
		LinearLayout layoutExporeTime;
		LinearLayout layoutPhoteIso;
		LinearLayout layoutImagePath;
		TextView tvShootTime;
		TextView tvModifyTime;
		TextView tvShootPlace;
		TextView tvFileName;
		TextView tvFileMetrics;
		TextView tvFileSize;
		TextView tvDeviceComp;
		TextView tvDeviceName;
		TextView tvFlashState;
		TextView tvFocusDistance;
		TextView tvWhiteBanlance;
		TextView tvPhoteAperture;
		TextView tvExporeTime;
		TextView tvPhoteIso;
		TextView tvImagePath;
	}*/
	//	private ImageDetailHolder mDetailHolder;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (mBigImageList == null) {
			finish();
			return;
		}
		chooser = PhotosChooseManager.getInstance();
		// mTempBm = BitmapFactory.decodeResource(getResources(), R.drawable.xc_photo_temp_img);
		// 最新详情页面
		initExtras();
		mCurrPageIndex = (mCurrImageCount == 0) ? 0 : (mCurrImageCount - 1);
		if (mBigImageList != null && mBigImageList.size() > 0) {
			mImageInfoHolder = mBigImageList.get(mCurrPageIndex);
		}
		mTitleString = mNormalTitleString + "(" + mCurrImageCount + "/" + mAllImageCount + ")";
		mDetailMainLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.xc_renew_detail_layout, null);
		setContentView(mDetailMainLayout);
		//		InitBmMapAndCheckedData();
		//		startGetImageListThread();
		initNavigateView();
		initViewPager();
		//		setDetailView();
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		if (mBigImageList != null) {
			if (chooser.isChooseMode()) {
				String choiseText;
				if (mCurrBigImageType == -1) {
					choiseText = getResources().getString(R.string.str_had_selected, chooser.getChoosedCount(),
							chooser.getChooseCountMax());
				} else {
					choiseText = getString(R.string.login_ok);
				}
				mDetailMainTitleBar.setChoiseMode(choiseText);
			} else {
				mDetailMainTitleBar.cancelChoiseMode();
			}
			if (mCurrPageIndex < mBigImageList.size())
				check_detail_selected.setChecked(chooser.isChoosed(mBigImageList.get(mCurrPageIndex)));
		}
	}
	private void initExtras() {
		Intent intent = getIntent();
		mImageInfoHolder = (ImageInfoHolder) intent.getSerializableExtra(IMAGE_VIEW_HOLDER_EXTRA);
		mNormalTitleString = intent.getStringExtra(IMAGE_DETAIL_TITLE_STRING);
		mCurrBigImageType = intent.getIntExtra(CameraActivity.CAMERA_GALLERY_TYPE, -1);
		mCurrImageCount = intent.getIntExtra(CameraActivity.IMAGE_DETAIL_CURR_INDEX, -1);
		if (mCurrImageCount == -1) {
			for (int i = 0, size = mBigImageList.size(); i < size; i++) {
				if (mImageInfoHolder.equals(mBigImageList.get(i))) {
					mCurrImageCount = i + 1;
					break;
				}
			}
		}
		mAllImageCount = mBigImageList.size();
	}
	private void initNavigateView() {
		setDetailView();
		tvw_detail_rotate = (TextView) mDetailMainLayout.findViewById(R.id.tvw_detail_rotate);
		check_detail_selected = (CheckBox) mDetailMainLayout.findViewById(R.id.check_detail_selected);
		check_detail_selected.setChecked(chooser.isChoosed(mBigImageList.get(mCurrPageIndex)));
		tvw_detail_rotate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				View itemView = mBigImageViewPager.findViewById(mCurrPageIndex);
				PhotoView ivt = (PhotoView) itemView.findViewById(R.id.detail_image);
				Drawable drawable = ivt.getDrawable();
				if ((drawable instanceof BitmapDrawable)) {
					Bitmap bmTemp = ((BitmapDrawable) drawable).getBitmap();
					if (bmTemp == null) {
						return;
					}
					Bitmap bmDest = PictureUtil.rotaingImageView(90, bmTemp);
					ivt.setImageBitmap(bmDest);
					PictureUtil.saveBitmapFile(bmDest, mBigImageList.get(mCurrPageIndex).mImagePath);
					PictureUtil.galleryAddPic(RenewDetailBaseActivity.this,
							mBigImageList.get(mCurrPageIndex).mImagePath);
					ThumbImageFetcher.getInstance(RenewDetailBaseActivity.this).clearCache();
					bmTemp.recycle();
				}
			}
		});
		check_detail_selected.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (((CheckBox) v).isChecked()) {
					if (!chooser.choosePhoto(mBigImageList.get(mCurrPageIndex))) {
						((CheckBox) v).setChecked(false);
						Toast.makeText(RenewDetailBaseActivity.this, R.string.showimage_uploadCountFull,
								Toast.LENGTH_SHORT).show();
					}
				} else {
					chooser.unChoose(mBigImageList.get(mCurrPageIndex));
				}
				if (chooser.isChooseMode()) {
					String choiseText;
					if (mCurrBigImageType == -1) {
						choiseText = getResources().getString(R.string.str_had_selected, chooser.getChoosedCount(),
								chooser.getChooseCountMax());
					} else {
						choiseText = getString(R.string.login_ok);
					}
					mDetailMainTitleBar.setChoiseMode(choiseText);
				} else {
					mDetailMainTitleBar.cancelChoiseMode();
				}
			}
		});
	}
	public static void setBigImageInfoList(List<ImageInfoHolder> bigImageList) {
		mBigImageList = bigImageList;
		/*
		 * if (bigImageList != null && bigImageList.size() > 0) { for (int i = 0; i <
		 * bigImageList.size(); i++) { mBigImageList.add(bigImageList.get(i)); } }
		 */
	}
	public void updateTitleString() {
		mCurrImageCount = mCurrPageIndex + 1;
		mTitleString = "(" + mCurrImageCount + "/" + mAllImageCount + ")";
		/*if (mTitleText != null) {
			mTitleText.setText(mTitleString);
		}*/
		mDetailMainTitleBar.setStringTitle(mNormalTitleString, mTitleString);
		check_detail_selected.setChecked(chooser.isChoosed(mBigImageList.get(mCurrPageIndex)));
		if (chooser.isChooseMode()) {
			String choiseText;
			if (mCurrBigImageType == -1) {
				choiseText = getResources().getString(R.string.str_had_selected, chooser.getChoosedCount(),
						chooser.getChooseCountMax());
			} else {
				choiseText = getString(R.string.login_ok);
			}
			mDetailMainTitleBar.setChoiseMode(choiseText);
		} else {
			mDetailMainTitleBar.cancelChoiseMode();
		}
	}
	public void setDetailView() {
		mDetailMainTitleBar = (GalleryNavigation) mDetailMainLayout.findViewById(R.id.new_detail_titlebar);
		mDetailMainTitleBar.setCameraBtnHide(true);
		mDetailMainTitleBar.setStringTitle(mTitleString, null);
		mDetailMainTitleBar.setBtnLeftListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		mBottomBtnLayout = (RelativeLayout) mDetailMainLayout.findViewById(R.id.upload_beauti_layout);
		// 上传
		mDetailMainTitleBar.setBtnUploadListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				switch (mCurrBigImageType) {
					case CameraActivity.GALLERY_TYPE_CONTACTCARD: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							ClipPictureActivity.launch(RenewDetailBaseActivity.this, path, 20);
							setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
					}
						break;
					case CameraActivity.GALLERY_TYPE_NEWSTV:
					case CameraActivity.GALLERY_TYPE_NEWSTOPTITLE: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							//							String path = checkListimagespath.iterator().next().mImagePath;
							//							photoUri=Uri.fromFile(new File(path));
							//							PictureUtil.photoClip(RenewDetailBaseActivity.this,photoUri,900,602);
							//							PictureUtil.photoClipInner(RenewDetailBaseActivity.this,path, 0.6666667f);
							setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
						break;
					}
					case CameraActivity.GALLERY_TYPE_TIME: {
						setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
						finish();
					}
						break;
					case CameraActivity.GALLERY_TYPE_REGISTER: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String path = checkListimagespath.iterator().next().mImagePath;
							ClipPictureActivity.launch(RenewDetailBaseActivity.this, path, 20, true);
							setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
							finish();
						} else {
							Toast.makeText(getApplicationContext(), "选择图片文件不正确", Toast.LENGTH_SHORT).show();
						}
					}
						break;
					case CameraActivity.GALLERY_TYPE_CHAT: {
						Collection<ImageInfoHolder> checkListimagespath = chooser.getChoosedSet();
						if (checkListimagespath != null) {
							String imgepath = checkListimagespath.iterator().next().mImagePath;
							EventBus.getDefault().post(new EventBusData(EventAction.SendPathTChat, imgepath));
							setResult(CameraActivity.RESULTCODE_ENTER_CONFIRM);
							finish();
						}
					}
						break;
					default: {
						Intent intent2 = new Intent(RenewDetailBaseActivity.this,
								ShareChangeAlbumAuthorityActivity.class);
						startActivity(intent2);
					}
						break;
				}
			}
		});
	}
	public void initViewPager() {
		mBigImageViewPager = (ViewPager) mDetailMainLayout.findViewById(R.id.big_image_pager);
		/*mBigPagerViews = new ArrayList<View>();
		//for (int i = 0; i < mBigImageList.size(); i++) {
		for (int i = 0; i < 5; i++) {
			RelativeLayout viewItem = generalPagerviewItem();
			mBigPagerViews.add(viewItem);
		}*/
		// ViewPager设置适配器
		//		mBigImagePagerAdapter = new BigImageViewPagerAdapter(mBigPagerViews);
		mBigImagePagerAdapter = new BigImageViewPagerAdapter();
		mBigImageViewPager.setAdapter(mBigImagePagerAdapter);
		// ViewPager设置当前条目
		mBigImageViewPager.setCurrentItem(mCurrPageIndex);
		// ViewPager设置页面监听事件
		mBigImageViewPager.setOnPageChangeListener(new BigImagePageChangeListener());
		updateViewPager();
		updateTitleString();
	}
	private RelativeLayout generalPagerviewItem() {
		RelativeLayout viewItem = (RelativeLayout) getLayoutInflater().inflate(R.layout.xc_big_image_itemview, null);
		PhotoView ivt = (PhotoView) viewItem.findViewById(R.id.detail_image);
		ivt.setImageResource(R.drawable.xc_photo_temp_img);
		ivt.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				finish();
			}
		});
		return viewItem;
	}
	public void updateViewPager() {
		if (mBigImageList != null && mBigImageList.size() > 0) {
			mImageInfoHolder = mBigImageList.get(mCurrPageIndex);
		}
		/*	if (mBigImagePagerAdapter != null) {
				List<View> bigPagerViews = mBigImagePagerAdapter.getViews();
				// System.out.println(" bigPagerViews.size() ="+bigPagerViews.size());
				for (int i = 0; i < bigPagerViews.size(); i++) {
					RelativeLayout viewItem = (RelativeLayout) bigPagerViews.get(i);
					PhotoView ivt = (PhotoView) viewItem.getChildAt(0);
					if (i >= mCurrPageIndex - 2 && i <= mCurrPageIndex + 2) {
						Bitmap bm1 = getItemBitmap(i);
						if (bm1 != null) {
							if (bm1.isRecycled() == false) {
								ivt.setImageBitmap(bm1);
							} else {
								Log.d(TAG, "updateViewPager, bm1 is recycled, mCurrPageIndex = " + mCurrPageIndex);
							}
						}
						if (i == mCurrPageIndex) {
							mDetailImage = ivt;
						}
					} else {
						// ivt.setImageDrawable(new BitmapDrawable(mTempBm));
						ivt.setImageResource(R.drawable.xc_photo_temp_img);
					}
				}
				for (int begin = mCurrPageIndex - 2, end = mCurrPageIndex + 2; begin <= end; begin++) {
					if (begin >= 0 && begin < mBigImageList.size()) {
						int curViewIndex = begin % bigPagerViews.size();
						// set view at position == mCurrentPageIndex
						RelativeLayout viewItem = (RelativeLayout) bigPagerViews.get(curViewIndex);
						String viewPath = (String) viewItem.getTag();
						String curPath = mBigImageList.get(begin).mImagePath;
						if (viewPath == null || !viewPath.equals(curPath)) {
							PhotoView ivt = (PhotoView) viewItem.getChildAt(0);
							Bitmap bm1 = getItemBitmap(begin);
							ivt.setImageBitmap(bm1);
							viewItem.setTag(curPath);
						}
					}
				}
				// set view at position
				mBigImagePagerAdapter.notifyDataSetChanged();
			}*/
	}
	/*public void InitBmMapAndCheckedData() {
		if (mBmChecked != null && mBmChecked.size() > 0) {
			mBmChecked.clear();
		}
		if (mBigImageList != null) {
			for (int i = 0; i < mBigImageList.size(); i++) {
				mBmChecked.add(false);
			}
		}
		if (mBmMap != null && mBmMap.size() > 0) {
			Iterator<Integer> it = mBmMap.keySet().iterator();
			while (it.hasNext()) {
				Bitmap bm = mBmMap.get(it.next());
				if (bm != null && bm.isRecycled() == false) {
					bm.recycle();
					bm = null;
				}
			}
			mBmMap.clear();
		}
	}*/
	/*public void startGetImageListThread() {
		mIsStopThread = false;
		new Thread(new Runnable() {
			@Override
			public void run() {
				Looper.prepare();
				try {
					Thread.sleep(1000);
					int count = mBigImageList.size();
					// System.out.println(" mBmMap = "+mBmMap.size());
					for (int i = 0; i < count; i++) {
						if (mIsStopThread) {
							return;
						}
						if (i >= mCurrPageIndex - 2 && i <= mCurrPageIndex + 2) {
							if (mBmChecked.get(i).booleanValue() == false) {
								mBmMap.put(i, getDetailImage(i));
								mBmChecked.set(i, true);
							}
						} else {
							if (mBmChecked.get(i).booleanValue()) {
								mBmChecked.set(i, false);
								Bitmap bmTemp = mBmMap.remove("" + i);
								if (bmTemp != null && bmTemp.isRecycled() == false) {
	//									Log.d(TAG, "tempThread=, recycle bitmap, i = " + i + ", mCurrPageIndex = "
	//											+ mCurrPageIndex);
									bmTemp.recycle();
									bmTemp = null;
								}
							}
						}
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
	//					Log.d(TAG, "tempThread InterruptedException");
				} catch (Exception e) {
	//					Log.d(TAG, "tempThread Exception");
				}
				Looper.loop();
			}
		}).start();
	}*/
	private Bitmap getDetailImage(int pageIndex) {
		Bitmap bmTemp = null;
		if (mBigImageList != null && mBigImageList.size() > pageIndex) {
			// System.out.println(" getDetailImage count ="+(count++));
			ImageInfoHolder holder = mBigImageList.get(pageIndex);
			//			int sampleSize = Utils.getBmSampleSizeForBigImage(holder.mImageSizeKB);
			//bmTemp = Utils.getBitmapFormFile(holder.mImagePath, sampleSize);
			int[] wh = BBSUtils.getScreenWH(this);
			bmTemp = Utils.getBitmapFormFile(holder.mImagePath, wh[0], wh[1]);
			// 20150626 zhenfeng wu 针对三星拍照旋转90度处理
			//			int angle = PictureUtil.readPictureDegree(holder.mImagePath);
			//			if (angle != 0) {
			//				bmTemp = PictureUtil.rotaingImageView(angle, bmTemp);
			//			}
		}
		return bmTemp;
	}
	/*public Bitmap getItemBitmap(int pageIndex) {
		Bitmap bm = null;
		if (mBmMap != null && mBmMap.size() > 0) {
			boolean isChecked = mBmChecked.get(pageIndex).booleanValue();
			Log.d(TAG, "getItemBitmap, pageIndex = " + pageIndex + ", isChecked = " + isChecked);
			if (isChecked) {
				bm = mBmMap.get(pageIndex);
			}
		}
		Log.d(TAG, "getItemBitmap , bm is null? " + (bm == null));
		if (bm == null) {
			bm = getDetailImage(pageIndex);
		}
		return bm;
	}*/
	/*private void setDetailImageInfo() {
		mDetailHolder = new ImageDetailHolder();
		mDetailHolder.layoutShootTime = (LinearLayout) mDetailInfoLayout.findViewById(R.id.shoot_time_layout);
		mDetailHolder.layoutModifyTime = (LinearLayout) mDetailInfoLayout.findViewById(R.id.modify_time_layout);
		mDetailHolder.layoutShootPlace = (LinearLayout) mDetailInfoLayout.findViewById(R.id.shoot_place_layout);
		mDetailHolder.layoutFileName = (LinearLayout) mDetailInfoLayout.findViewById(R.id.file_name_layout);
		mDetailHolder.layoutFileMetrics = (LinearLayout) mDetailInfoLayout.findViewById(R.id.file_metrics_layout);
		mDetailHolder.layoutFileSize = (LinearLayout) mDetailInfoLayout.findViewById(R.id.file_size_layout);
		mDetailHolder.layoutDeviceComp = (LinearLayout) mDetailInfoLayout.findViewById(R.id.device_comp_layout);
		mDetailHolder.layoutFlashState = (LinearLayout) mDetailInfoLayout.findViewById(R.id.flash_state_layout);
		mDetailHolder.layoutDeviceName = (LinearLayout) mDetailInfoLayout.findViewById(R.id.device_name_layout);
		mDetailHolder.layoutFocusDistance = (LinearLayout) mDetailInfoLayout.findViewById(R.id.focus_distance_layout);
		mDetailHolder.layoutWhiteBanlance = (LinearLayout) mDetailInfoLayout.findViewById(R.id.white_banlance_layout);
		mDetailHolder.layoutPhoteAperture = (LinearLayout) mDetailInfoLayout.findViewById(R.id.photo_aperture_layout);
		mDetailHolder.layoutExporeTime = (LinearLayout) mDetailInfoLayout.findViewById(R.id.explore_time_layout);
		mDetailHolder.layoutPhoteIso = (LinearLayout) mDetailInfoLayout.findViewById(R.id.photo_iso_layout);
		mDetailHolder.layoutImagePath = (LinearLayout) mDetailInfoLayout.findViewById(R.id.image_path_layout);
		mDetailHolder.tvShootTime = (TextView) mDetailInfoLayout.findViewById(R.id.shoot_time);
		mDetailHolder.tvModifyTime = (TextView) mDetailInfoLayout.findViewById(R.id.modify_time);
		mDetailHolder.tvShootPlace = (TextView) mDetailInfoLayout.findViewById(R.id.shoot_place);
		mDetailHolder.tvFileName = (TextView) mDetailInfoLayout.findViewById(R.id.file_name);
		mDetailHolder.tvFileMetrics = (TextView) mDetailInfoLayout.findViewById(R.id.file_metrics);
		mDetailHolder.tvFileSize = (TextView) mDetailInfoLayout.findViewById(R.id.file_size);
		mDetailHolder.tvDeviceComp = (TextView) mDetailInfoLayout.findViewById(R.id.device_comp);
		mDetailHolder.tvFlashState = (TextView) mDetailInfoLayout.findViewById(R.id.flash_state);
		mDetailHolder.tvDeviceName = (TextView) mDetailInfoLayout.findViewById(R.id.device_name);
		mDetailHolder.tvFocusDistance = (TextView) mDetailInfoLayout.findViewById(R.id.focus_distance);
		mDetailHolder.tvWhiteBanlance = (TextView) mDetailInfoLayout.findViewById(R.id.white_banlance);
		mDetailHolder.tvPhoteAperture = (TextView) mDetailInfoLayout.findViewById(R.id.photo_aperture);
		mDetailHolder.tvExporeTime = (TextView) mDetailInfoLayout.findViewById(R.id.explore_time);
		mDetailHolder.tvPhoteIso = (TextView) mDetailInfoLayout.findViewById(R.id.photo_iso);
		mDetailHolder.tvImagePath = (TextView) mDetailInfoLayout.findViewById(R.id.image_path);
	}*/
	/*private void updateImageHolderInfo() {
		com.butterfly.vv.camera.base.GetImageFileUtil.getImageExif(mImageInfoHolder);
		mDetailHolder.layoutShootTime.setVisibility(View.VISIBLE);
		mDetailHolder.layoutModifyTime.setVisibility(View.VISIBLE);
		mDetailHolder.layoutShootPlace.setVisibility(View.VISIBLE);
		mDetailHolder.layoutFileName.setVisibility(View.VISIBLE);
		mDetailHolder.layoutFileMetrics.setVisibility(View.VISIBLE);
		mDetailHolder.layoutFileSize.setVisibility(View.VISIBLE);
		mDetailHolder.layoutDeviceComp.setVisibility(View.VISIBLE);
		mDetailHolder.layoutDeviceName.setVisibility(View.VISIBLE);
		mDetailHolder.layoutFlashState.setVisibility(View.VISIBLE);
		mDetailHolder.layoutFocusDistance.setVisibility(View.VISIBLE);
		mDetailHolder.layoutWhiteBanlance.setVisibility(View.VISIBLE);
		mDetailHolder.layoutPhoteAperture.setVisibility(View.VISIBLE);
		mDetailHolder.layoutExporeTime.setVisibility(View.VISIBLE);
		mDetailHolder.layoutPhoteIso.setVisibility(View.VISIBLE);
		mDetailHolder.layoutImagePath.setVisibility(View.VISIBLE);
		String unknown = getResources().getString(R.string.str_detail_unknown);
		int lastIndex = mImageInfoHolder.mImagePath.lastIndexOf("/");
		mImageInfoHolder.mImageFileName = mImageInfoHolder.mImagePath.substring(lastIndex + 1);
		if (mImageInfoHolder.mImageDateTime == null
				|| (mImageInfoHolder.mImageDateTime != null && mImageInfoHolder.mImageDateTime.length() < ImageInfoHolder.BASE_DATE
						.length())) {
			mImageInfoHolder.mImageDateTime = unknown;
			mDetailHolder.layoutShootTime.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mImageModifiedTime == null) {
			// 修改时间暂时获取不到
			mImageInfoHolder.mImageModifiedTime = unknown;
			mDetailHolder.layoutModifyTime.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mDeviceMake == null) {
			mImageInfoHolder.mDeviceMake = unknown;
			mDetailHolder.layoutDeviceComp.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mExposureTime == null) {
			mImageInfoHolder.mExposureTime = unknown;
			mDetailHolder.layoutExporeTime.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mFocalLength == null) {
			mImageInfoHolder.mFocalLength = unknown;
			mDetailHolder.layoutFocusDistance.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mImageDeviceModel == null) {
			mImageInfoHolder.mImageDeviceModel = unknown;
			mDetailHolder.layoutDeviceName.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mImagePath == null) {
			mImageInfoHolder.mImagePath = unknown;
			mDetailHolder.layoutImagePath.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mImageSize == null) {
			mImageInfoHolder.mImageSize = unknown;
			mDetailHolder.layoutFileSize.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mPhotoAperture == null) {
			mImageInfoHolder.mPhotoAperture = unknown;
			mDetailHolder.layoutPhoteAperture.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mPhotoFlash == null) {
			mImageInfoHolder.mPhotoFlash = unknown;
			mDetailHolder.layoutFlashState.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mPhotoIso == null) {
			mImageInfoHolder.mPhotoIso = unknown;
			mDetailHolder.layoutPhoteIso.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mWhiteBalance == null) {
			mImageInfoHolder.mWhiteBalance = unknown;
			mDetailHolder.layoutWhiteBanlance.setVisibility(View.GONE);
		}
		if (mImageInfoHolder.mImageHeight == null || mImageInfoHolder.mImageWidth == null) {
			mImageInfoHolder.mImageMetrics = unknown;
			mDetailHolder.layoutFileMetrics.setVisibility(View.GONE);
		} else {
			if (mImageInfoHolder.mImageWidth.equals("0") || mImageInfoHolder.mImageHeight.equals("0")) {
				mImageInfoHolder.mImageMetrics = unknown;
				mDetailHolder.layoutFileMetrics.setVisibility(View.GONE);
			} else {
				mImageInfoHolder.mImageMetrics = mImageInfoHolder.mImageWidth + "x" + mImageInfoHolder.mImageHeight;
			}
		}
		if (mImageInfoHolder.mLongitude == null || mImageInfoHolder.mLatitude == null) {
			mImageInfoHolder.mPhotoCity = unknown;
			mDetailHolder.layoutShootPlace.setVisibility(View.GONE);
		} else {
			LocationUtil locUtil = new LocationUtil(this);
			mImageInfoHolder.mPhotoCity = locUtil.getLocationCityByNormalLatLon(mImageInfoHolder.mLatitude,
					mImageInfoHolder.mLongitude);
		}
		mDetailHolder.tvShootTime.setText(mImageInfoHolder.mImageDateTime);
		mDetailHolder.tvModifyTime.setText(mImageInfoHolder.mImageModifiedTime);
		mDetailHolder.tvShootPlace.setText(mImageInfoHolder.mPhotoCity);
		mDetailHolder.tvFileName.setText(mImageInfoHolder.mImageFileName);
		mDetailHolder.tvFileMetrics.setText(mImageInfoHolder.mImageMetrics);
		mDetailHolder.tvFileSize.setText(mImageInfoHolder.mImageSize);
		mDetailHolder.tvDeviceComp.setText(mImageInfoHolder.mDeviceMake);
		mDetailHolder.tvDeviceName.setText(mImageInfoHolder.mImageDeviceModel);
		if (mImageInfoHolder.mPhotoFlash.equals("0")) {
			mDetailHolder.tvFlashState.setText("未使用闪光灯");
		} else {
			mDetailHolder.tvFlashState.setText(mImageInfoHolder.mPhotoFlash);
		}
		mDetailHolder.tvFocusDistance.setText(mImageInfoHolder.mFocalLength);
		if (mImageInfoHolder.mWhiteBalance.equals("0")) {
			mDetailHolder.tvWhiteBanlance.setText("自动");
		} else {
			mDetailHolder.tvWhiteBanlance.setText(mImageInfoHolder.mWhiteBalance);
		}
		mDetailHolder.tvPhoteAperture.setText(mImageInfoHolder.mPhotoAperture);
		mDetailHolder.tvExporeTime.setText(mImageInfoHolder.mExposureTime);
		mDetailHolder.tvPhoteIso.setText(mImageInfoHolder.mPhotoIso);
		mDetailHolder.tvImagePath.setText(mImageInfoHolder.mImagePath);
	}*/
	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mIsDetailInfoScreen) {
				mIsDetailInfoScreen = false;
				//				mDetailMainLayout.removeView(mDetailInfoLayout);
			} else {
				finish();
			}
			return true;
		}
		return super.onKeyUp(keyCode, event);
	}
	@Override
	protected void onPause() {
		super.onPause();
	}

	private int mStartY;
	private int mStartX;
	private int mSpace;
	private boolean needRefresh = false;

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		onTouchEvent2(event);
		int pointCount = event.getPointerCount();
		int action = event.getAction();
		if (action == MotionEvent.ACTION_DOWN) {
			needRefresh = true;
		}
		if (pointCount > 1 || mSpace > CLICK_DISTANCE) {
			needRefresh = false;
		}
		if (mIsDetailInfoScreen) {
			// 上层详情界面覆盖时，不往下层的view传递touch事件
			return super.dispatchTouchEvent(event);
		}
		if (event.getAction() == MotionEvent.ACTION_UP && needRefresh) {
			// Log.d(TAG, "mDetailImage onTouch!");
			needRefresh = false;
		}
		return super.dispatchTouchEvent(event);
	}
	/*
	 * @Override public boolean onTouchEvent(MotionEvent event) { Log.d(TAG, "onTouchEvent.");
	 * return super.onTouchEvent(event); }
	 */
	public void onTouchEvent2(MotionEvent event) {
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:
				mSpace = 0;
				mStartX = (int) event.getX();
				mStartY = (int) event.getY();
				break;
			case MotionEvent.ACTION_MOVE:
				whenMove(event);
				break;
		}
	}
	private void whenMove(MotionEvent event) {
		int touchCount = event.getPointerCount();
		if (touchCount == 1) {
			int tmpX = (int) event.getX();
			int tmpY = (int) event.getY();
			int spaceX = Math.abs(tmpX - mStartX);
			int spaceY = Math.abs(tmpY - mStartY);
			mSpace = spaceX > spaceY ? spaceX : spaceY;
		}
	}

	private View.OnTouchListener mMainLayoutTouchListener = new View.OnTouchListener() {
		private boolean needRefresh = false;

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			/*
			 * int pointCount = event.getPointerCount(); int action = event.getAction(); if (action
			 * == MotionEvent.ACTION_DOWN) { needRefresh = true; } if (pointCount > 1 || mSpace >
			 * CLICK_DISTANCE) { needRefresh = false; } if (v == mDetailInfoLayout) { //
			 * 上层详情界面覆盖时，不往下层的view传递touch事件 return true; } if (v.getId() ==
			 * R.id.big_image_view_container && event.getAction() == MotionEvent.ACTION_UP &&
			 * needRefresh) { // Log.d(TAG, "mDetailImage onTouch!"); needRefresh = false;
			 * mIsShowTitleAndBtmBar = !mIsShowTitleAndBtmBar; refreshTitleAndBtmBar(); }
			 */
			return true;
		}
	};

	// ViewPager适配器
	public class BigImageViewPagerAdapter extends PagerAdapter {
		private Queue<View> views = new ArrayDeque<View>();

		public BigImageViewPagerAdapter() {
			super();
			//			this.views = pViews;
		}
		/*public void setViews(List<View> pViews) {
			this.views = pViews;
		}
		public List<View> getViews() {
			return views;
		}*/
		// 销毁条目
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			//			Log.d(TAG, "destroyItem "+position);
			container.removeView((View) object);
			//			views.offer((View) object);
		}
		@Override
		public int getCount() {
			if (mBigImageList != null) {
				return mBigImageList.size();
			}
			return 0;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			//			Log.d(TAG, "viewpager instantiateItem "+position);
			View view = views.poll();
			if (view == null) {
				view = generalPagerviewItem();
			}
			view.setId(position);
			container.addView(view, 0);
			PhotoView ivt = (PhotoView) view.findViewById(R.id.detail_image);
			ImageLoader.getInstance().displayImage(Scheme.FILE.wrap(mBigImageList.get(position).mImagePath), ivt);
			return view;
		}
	}

	// ViewPager监听事件
	public class BigImagePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
			//			 Log.d(TAG,"onPageScrollStateChanged, arg0 = " + arg0);
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
			//			 Log.d(TAG,"onPageScrolled, arg0 = " + arg0 + ", arg1 = " + arg1 +
			// ", arg2 = " +
			// arg2);
		}
		@Override
		public void onPageSelected(int arg0) {
			Log.d(TAG, "viewpager onPageSelected, arg0 = " + arg0);
			mCurrPageIndex = arg0;
			updateViewPager();
			updateTitleString();
			//						updateImageHolderInfo();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//		mIsStopThread = true;
		//		mBigImageList = null;
		//		InitBmMapAndCheckedData();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == Constants.CLIPPHOTO) {
			//调用系统剪切功能的返回接收
			EventBus.getDefault().post(new EventBusData(EventAction.SendClipPhoto, photoUri));
			EventBus.getDefault().post(new EventBusData(EventAction.FinishActivity, null));
			finish();
		}
	}
}
