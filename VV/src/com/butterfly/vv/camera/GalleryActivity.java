/**
 * 
 */
package com.butterfly.vv.camera;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import com.beem.project.btf.R;
import com.beem.project.btf.constant.Constants;
import com.beem.project.btf.imagefilter.crop.CropActivity;
import com.beem.project.btf.imagefilter.crop.CropExtras;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity;
import com.beem.project.btf.ui.ShareChangeAlbumAuthorityActivity.IntentKey;
import com.beem.project.btf.ui.loadimages.ImageLoaderConfigers;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.FileUtil;
import com.butterfly.vv.GalleryNavigation;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.butterfly.vv.camera.base.PhotosChooseManager;
import com.vv.image.gallery.viewer.ScrollingViewPager;

/**
 * 图库activity，统一采用的activity
 * @author Aaron Lee Created at 上午11:41:19 2016-1-12
 */
public class GalleryActivity extends FragmentActivity implements
		OnClickListener, GalleryBaseFragment.OnGalleryFramentCallback,
		OnBackStackChangedListener {
	private static final String TAG_DATELIST = "TAG_DateList";
	private static final String TAG_FOLDERLIST = "TAG_FolderList";
	private static final String TAG_ALBUM = "TAG_ALBUM";
	private static final String TAG_DETAIL = "TAG_DETAIL";
	/** 是否可以选择 */
	public static final String GALLERY_PICKABLE = "GALLERY_PICKABLE";
	/** 能够选择的最大张数，默认值为1 */
	public static final String GALLERY_CHOOSE_MAX = "GALLERY_CHOOSE_MAX";
	/**
	 * 是否能够从拍照返回的图片，传入参数为boolean类型。此时返回的张数为1，且立即返回。 如果没有指定返回的{@link GalleryActivity.GALLERY_OUTPUT}
	 * ,那么将会返回data，即一个完整的{@link Bitmap}类型
	 */
	public static final String GALLERY_FROM_CAMERA = "GALLERY_FROM_CAMERA";
	/**
	 * 是否需要剪裁,如果带有此参数，最大张数也变为1，在确定选择后立即跳到 {@link CropActivity}处理， 如果没有指定返回的
	 * {@link GalleryActivity.GALLERY_OUTPUT},那么将会返回data，即一个完整的{@link Bitmap}类型
	 */
	public static final String GALLERY_CROP = "GALLERY_CROP";
	/**
	 * 指定返回的Uri，此模式尽在设定 {$link GALLERY_FROM_CAMERA} 或者{$link GALLERY_CROP} 为true时候有效，
	 * 否则直接返回选择了图片的路径或者Uri
	 */
	public static final String GALLERY_OUTPUT = MediaStore.EXTRA_OUTPUT;
	/**
	 * 指定返回的是Uri格式，或者全路径格式返回，此模式尽在设定 {$link GALLERY_FROM_CAMERA} 以及{$link GALLERY_CROP} 均为false时候有效，
	 * 即只选择图库中的图片时候有效 .如果选用了多张图片模式，那么必定返回的是路径
	 */
	public static final String GALLERY_OUTOUT_FULLPATH = "GALLERY_OUTOUT_FULLPATH";
	/** 返回参数，当返回类型为{@link Bitmap}所用的参数 */
	public static final String GALLERY_RESULT_DATA = CropExtras.KEY_RETURN_DATA;
	/** 返回参数，当返回类型为完整路径所用的参数 */
	public static final String GALLERY_RESULT_FULLPATH = "GALLERY_RESULT_FULLPATH";
	public static final String IMAGE_DETAIL_CURR_INDEX = "image_detail_curr_index";
	private GalleryNavigation main_navigation_view;
	private ScrollingViewPager viewPager;
	private FragmentManager fm;
	private PhotosChooseManager chooser;
	private Map<String, GalleryBaseFragment> fragments = new HashMap<String, GalleryBaseFragment>();
	private String curFragmentTag;
	private GalleryDataService cameraPhotoService;
	private int mCurrPageIndex;
//	private GalleryBaseFragment albumFragment;
//	private GalleryBaseFragment detailFragment;
	private View layuout_frameContent;
	private boolean crop;
	private boolean pickFromCamera;
	private boolean pickable;
	private Uri output;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		getExtras();
		ImageLoaderConfigers.initThumbImageFetcher(this);
		setContentView(R.layout.activity_gallery);
		fm = getSupportFragmentManager();
		fm.addOnBackStackChangedListener(this);
		cameraPhotoService = new GalleryDataService();
		initViews();
		initImagesChooser();
	}
	private void getExtras() {
		Intent i = getIntent();
		crop = i.getBooleanExtra(GALLERY_CROP, false);
		pickFromCamera = i.getBooleanExtra(GALLERY_FROM_CAMERA, false);
		output = (Uri) i.getParcelableExtra(MediaStore.EXTRA_OUTPUT);
		pickable = i.getBooleanExtra(GALLERY_PICKABLE, false);
	}
	private void initViews() {
		main_navigation_view = (GalleryNavigation) findViewById(R.id.main_navigation_view);
		viewPager = (ScrollingViewPager) findViewById(R.id.viewPager);
		layuout_frameContent = findViewById(R.id.layuout_frameContent);
		initNavigateView();
		initViewPager();
	}
	private void initNavigateView() {
		main_navigation_view.setBtnLeftListener(this);
		main_navigation_view.setBtnUploadListener(this);
		main_navigation_view.setCameraListener(this);
		main_navigation_view.inflateCenter(this);
		main_navigation_view.setTopbarTab(0);
		setListNavigate();
	}
	private void setListNavigate() {
		main_navigation_view.setStringTitle(getString(R.string.tabtitle_photo),
				null);
		main_navigation_view.setBackgroundColor(getResources().getColor(
				R.color.new_title_bg));
		main_navigation_view.setCameraBtnHide(false);
		main_navigation_view.hindTopbarCenter(main_navigation_view.isChoiceMode());
		main_navigation_view.setCenterInflated(true);
	}
	private void setAlbumNavigate() {
		ImageFolderInfo imageFolderInfo = cameraPhotoService.getCurrentAlbum();
		main_navigation_view.setStringTitle(imageFolderInfo.getmFolderName(),
				"(" + imageFolderInfo.getmFolderImgCount()
						+ getString(R.string.str_img_piece) + ")");
		main_navigation_view.setBackgroundColor(getResources().getColor(
				R.color.new_title_bg));
		main_navigation_view.setCameraBtnHide(true);
		main_navigation_view.hindTopbarCenter(true);
		main_navigation_view.setCenterInflated(false);
	}
	private void setDetailNavigate(int position) {
		ImageFolderInfo imageFolderInfo = cameraPhotoService.getCurrentAlbum();
		String title0;
		if (imageFolderInfo != null) {
			title0 = imageFolderInfo.getmFolderName();
		} else {
			title0 = getResources().getString(R.string.tabtitle_photo);
		}
		main_navigation_view.setStringTitle(title0,
				"(" + (position + 1) + "/"
						+ cameraPhotoService.getmBigImageList().size() + ")");
		main_navigation_view
				.setBackgroundResource(R.drawable.xc_top_infobar_bg);
		main_navigation_view.setCameraBtnHide(true);
		main_navigation_view.hindTopbarCenter(true);
		main_navigation_view.setCenterInflated(false);
	}
	private void initViewPager() {
		GalleryPagerAdapter adapter = new GalleryPagerAdapter(
				getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		// ViewPager设置当前条目
		viewPager.setCurrentItem(mCurrPageIndex);
		// ViewPager设置页面监听事件
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
	}
	private void initImagesChooser() {
		chooser = PhotosChooseManager.getInstance();
		int uploadpicMaxNum = getIntent().getIntExtra(GALLERY_CHOOSE_MAX, 1);
		chooser.resetChooseCountMax(uploadpicMaxNum);
	}
	private void updateTitleTextImage() {
		if (chooser.isChooseMode()) {
			String choiseText;
			choiseText = getResources().getString(R.string.str_had_selected,
					chooser.getChoosedCount(), chooser.getChooseCountMax());
			main_navigation_view.setChoiseMode(choiseText);
			viewPager.setScanScroll(false);
		} else {
			main_navigation_view.cancelChoiseMode();
			viewPager.setScanScroll(true);
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		fm.removeOnBackStackChangedListener(this);
		ImageLoaderConfigers.destroyThumbImageFetcher();
	}
	@Override
	protected void onStart() {
		super.onStart();
	}
	@Override
	protected void onStop() {
		super.onStop();
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.TAKEPHOTO) { // 从拍照返回；
			if (resultCode == RESULT_OK) {
				Uri returnUri;
				if (output != null && !crop) {
					returnUri = output;
				} else {
					returnUri = FileUtil.getCameraPhotoUri(this);
				}
				if (crop) {
					// 如果需要剪裁，调用剪裁
					gotoCrop(returnUri);
				} else {
					// 如果不需要剪裁，则直接返回uri.
					if (pickable) {
						Intent result = new Intent();
						result.setData(returnUri);
						setResult(RESULT_OK, result);
						finish();
					} else {
						ArrayList<String> listimagePath = new ArrayList<String>();
						listimagePath.add(returnUri.getPath());
						Intent intent2 = new Intent(this,
								ShareChangeAlbumAuthorityActivity.class);
						intent2.putStringArrayListExtra(
								IntentKey.LISTIMAGEPATH, listimagePath);
						startActivity(intent2);
					}
				}
			}
		} else if (requestCode == Constants.CLIPPHOTO) {
			if (resultCode == RESULT_OK) {
				if (pickable) {
					if (output != null) {
						Intent result = new Intent();
						result.setData(output);
						setResult(RESULT_OK, result);
						finish();
					} else {
						ArrayList<String> listimagePath = new ArrayList<String>();
						listimagePath.add(output.getPath());
						Intent intent2 = new Intent(this,
								ShareChangeAlbumAuthorityActivity.class);
						intent2.putStringArrayListExtra(
								IntentKey.LISTIMAGEPATH, listimagePath);
						startActivity(intent2);
					}
				}
			}
		}
	}
	private void returnResult() {
		if (pickable) {
			Intent result = new Intent();
			// 如果需要剪切，那么调用剪切的activity;
			if (crop) {
				ImageInfoHolder image = chooser.getChoosedSet().iterator()
						.next();
				String path = image.mImagePath;
				Uri cropPhotoUri = Uri.fromFile(new File(path));
				gotoCrop(cropPhotoUri);
			} else if (chooser.getChooseCountMax() > 1) {
				ArrayList<String> list = new ArrayList<String>();
				for (ImageInfoHolder item : chooser.getChoosedSet()) {
					list.add(item.mImagePath);
				}
				result.putStringArrayListExtra(GALLERY_RESULT_FULLPATH, list);
				setResult(Activity.RESULT_OK, result);
				finish();
			} else if (chooser.getChooseCountMax() == 1) {
				String resultPath = chooser.getChoosedSet().iterator().next().mImagePath;
				result.setData(Uri.fromFile(new File(resultPath)));
				result.putExtra(GALLERY_RESULT_DATA, resultPath);
				setResult(Activity.RESULT_OK, result);
				finish();
			}
		} else {
			Intent intent2 = new Intent(this,
					ShareChangeAlbumAuthorityActivity.class);
			startActivity(intent2);
		}
	}
	private void gotoCrop(Uri uri) {
		Intent localIntent = new Intent(this, CropActivity.class);
		localIntent.setDataAndType(uri, "image/*");
		localIntent.putExtra("crop", "true");
		localIntent.putExtra(CropExtras.KEY_OUTPUT_X,
				getIntent().getIntExtra(CropExtras.KEY_OUTPUT_X, 0));
		localIntent.putExtra(CropExtras.KEY_OUTPUT_Y,
				getIntent().getIntExtra(CropExtras.KEY_OUTPUT_Y, 0));
		localIntent.putExtra(CropExtras.KEY_SCALE,
				getIntent().getBooleanExtra(CropExtras.KEY_SCALE, true));
		localIntent.putExtra(CropExtras.KEY_SCALE_UP_IF_NEEDED, getIntent()
				.getBooleanExtra(CropExtras.KEY_SCALE_UP_IF_NEEDED, false));
		localIntent.putExtra(CropExtras.KEY_ASPECT_X,
				getIntent().getIntExtra(CropExtras.KEY_ASPECT_X, 0));
		localIntent.putExtra(CropExtras.KEY_ASPECT_Y,
				getIntent().getIntExtra(CropExtras.KEY_ASPECT_Y, 0));
		localIntent.putExtra(CropExtras.KEY_SET_AS_WALLPAPER, getIntent()
				.getBooleanExtra(CropExtras.KEY_SET_AS_WALLPAPER, false));
		localIntent.putExtra(CropExtras.KEY_RETURN_DATA, getIntent()
				.getBooleanExtra(CropExtras.KEY_RETURN_DATA, false));
		localIntent.putExtra(MediaStore.EXTRA_OUTPUT, getIntent()
				.getParcelableExtra(MediaStore.EXTRA_OUTPUT));
		localIntent.putExtra(CropExtras.KEY_OUTPUT_FORMAT, getIntent()
				.getStringExtra(CropExtras.KEY_OUTPUT_FORMAT));
		localIntent.putExtra(CropExtras.KEY_SHOW_WHEN_LOCKED, getIntent()
				.getBooleanExtra(CropExtras.KEY_SHOW_WHEN_LOCKED, false));
		localIntent.putExtra(CropExtras.KEY_SPOTLIGHT_X, getIntent()
				.getFloatExtra(CropExtras.KEY_SPOTLIGHT_X, 0.0f));
		localIntent.putExtra(CropExtras.KEY_SPOTLIGHT_Y, getIntent()
				.getFloatExtra(CropExtras.KEY_SPOTLIGHT_Y, 0.0f));
		startActivityForResult(localIntent, Constants.CLIPPHOTO);
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.tvw_topbar_title: {
				onBackPressed();
			}
				break;
			case R.id.btn_topbar_upload: {
				// 当按下确定时候的操作。
				returnResult();
			}
				break;
			case R.id.btn_topbar_camera: {
				if (output != null && !crop) {
					BBSUtils.takePhoto(this, Constants.TAKEPHOTO, output);
				} else {
					BBSUtils.takePhoto(this, Constants.TAKEPHOTO);
				}
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

	private class GalleryPagerAdapter extends FragmentPagerAdapter {
		public GalleryPagerAdapter(FragmentManager fm) {
			super(fm);
		}
		@Override
		public Fragment getItem(int arg0) {
			switch (arg0) {
				case 1: {
					Fragment f = fragments.get(TAG_FOLDERLIST);
					if (f == null) {
						f = GalleryFoldersFragment.newInstance(TAG_FOLDERLIST);
						fragments.put(TAG_FOLDERLIST, (GalleryBaseFragment) f);
					}
					return f;
				}
				case 0:
				default: {
					Fragment f = fragments.get(TAG_DATELIST);
					if (f == null) {
						f = GalleryDateListFragment.newInstance(TAG_DATELIST);
						fragments.put(TAG_DATELIST, (GalleryBaseFragment) f);
					}
					return f;
				}
			}
		}
		@Override
		public int getCount() {
			return 2;
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
			updateTitleTextImage();
			//animation.setFillAfter(true); // True图片停在动画结束位置
			//animation.setDuration(300);
			//mCursorImageView.startAnimation(ani;mation);
			switch (arg0) {
				case 0:
					curFragmentTag = TAG_DATELIST;
					break;
				case 1:
					curFragmentTag = TAG_FOLDERLIST;
					break;
				default:
					break;
			} 
			mCurrPageIndex = arg0;
			main_navigation_view.setTopbarTab(arg0);
		}
	}

	@Override
	public void onPhotoChoiceChange() {
		if (chooser.isChooseMode()) {
			String choiseText;
			if (chooser.getChooseCountMax() == 1) {
				choiseText = getString(R.string.login_ok);
			} else {
				choiseText = getResources().getString(
						R.string.str_had_selected, chooser.getChoosedCount(),
						chooser.getChooseCountMax());
			}
			main_navigation_view.setChoiseMode(choiseText);
			viewPager.setScanScroll(false);
		} else {
			main_navigation_view.cancelChoiseMode();
			viewPager.setScanScroll(true);
		}
	}
	@Override
	public void onDataLoading() {
		// TODO Auto-generated method stub
	}
	@Override
	public void onDateLoadedComplete() {
		// TODO Auto-generated method stub
	}
	
	private void notifyAlbumData() {
		GalleryBaseFragment f =  fragments.get(curFragmentTag);
		if (f != null) {
			f.notifyDataUpdate();
		}
	}
	private void notifyListData() {
		if (mCurrPageIndex == 0) {
			curFragmentTag = TAG_DATELIST;
		} else if (mCurrPageIndex == 1) {
			curFragmentTag = TAG_FOLDERLIST;
		}
		GalleryBaseFragment f =  fragments.get(curFragmentTag);
		if (f != null) {
			f.notifyDataUpdate();
		}
	}
	@Override
	public void showPhotoDetail(int position,
			List<ImageInfoHolder> mBigImageList, ImageInfoHolder currentPhoto) {
		Fragment detailFragment = fragments.get(TAG_DETAIL);
		if (detailFragment == null) {
			detailFragment = GalleryDetailFragment.newInstance(TAG_DETAIL);
			fragments.put(TAG_DETAIL, (GalleryBaseFragment) detailFragment);
		}
		curFragmentTag = TAG_DETAIL;
		cameraPhotoService.setDetailData(mBigImageList, position, currentPhoto);
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.layuout_frameDetail, detailFragment);
		transaction.addToBackStack(TAG_DETAIL);
		transaction.commit();
	}
	@Override
	public void showPhotoAlbum(ImageFolderInfo imageFolderInfo) {
		Fragment albumFragment = fragments.get(TAG_ALBUM);
		if (albumFragment == null) {
			albumFragment = GalleryAlbumFragment.newInstance(TAG_ALBUM);
			fragments.put(TAG_ALBUM, (GalleryBaseFragment) albumFragment);
		}
		curFragmentTag = TAG_ALBUM;
		/*Bundle args = new Bundle();
		args.putSerializable(GalleryAlbumFragment.TRANSFER_PHOTO_FOLDER_STRING, imageFolderInfo);
		albumFragment.setArguments(args);*/
		cameraPhotoService.setCurrentAlbum(imageFolderInfo);
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.add(R.id.layuout_frameContent, albumFragment);
		transaction.addToBackStack(TAG_ALBUM);
		transaction.commit();
	}
	@Override
	public void setTitleString(String title0, String title1) {
		main_navigation_view.setStringTitle(title0, title1);
	}
	@Override
	public void onFragmentExit(Fragment f) {
		/*FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		transaction.detach(f);*/
	}
	@Override
	public GalleryDataService getDataService() {
		return cameraPhotoService;
	}
	@Override
	public PhotosChooseManager getChooser() {
		return chooser;
	}
	@Override
	public void onBackStackChanged() {
		//		LogUtils.d("onBackStackChanged");
		int count = fm.getBackStackEntryCount();
		//		for (int i = 0; i < count; i ++) {
		//			LogUtils.d("back stack "+i+ ":" + fm.getBackStackEntryAt(i));
		//		}
		if (count == 0) {
			// Count is 0 : It changes to datelist mode or folder mode;
			setListNavigate();
			notifyListData();
		} else {
			String name = fm.getBackStackEntryAt(count - 1).getName();
			if (TAG_ALBUM.equals(name)) {
				curFragmentTag = TAG_ALBUM;
				setAlbumNavigate();
				notifyAlbumData();
			} else if (TAG_DETAIL.equals(name)) {
				curFragmentTag = TAG_DETAIL;
				setDetailNavigate(cameraPhotoService.getCurrentPosition());
			}
		}
	}
	
}
