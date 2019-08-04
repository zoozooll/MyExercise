/**
 * 
 */
package com.butterfly.vv.camera;

import java.util.ArrayDeque;
import java.util.List;
import java.util.Queue;

import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.loadimages.ThumbImageFetcher;
import com.beem.project.btf.utils.PictureUtil;
import com.butterfly.vv.camera.base.ImageFolderInfo;
import com.butterfly.vv.camera.base.ImageInfoHolder;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.download.ImageDownloader.Scheme;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Aaron Lee Created at 下午3:31:23 2015-12-28
 */
public class GalleryDetailFragment extends GalleryBaseFragment implements
		OnClickListener {
	private View view;
	private TextView tvw_detail_rotate;
	private CheckBox check_detail_selected;
	private ViewPager mBigImageViewPager;
	private BigImageViewPagerAdapter mBigImagePagerAdapter;
	private int mCurrPageIndex;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mCurrPageIndex = dataService.getCurrentPosition();
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		if (null != view) {
			((ViewGroup) view.getParent()).removeView(view);
		} else {
			view = inflater.inflate(R.layout.fragment_gallery_detail, container,
					false);
			tvw_detail_rotate = (TextView) view
					.findViewById(R.id.tvw_detail_rotate);
			check_detail_selected = (CheckBox) view
					.findViewById(R.id.check_detail_selected);
			tvw_detail_rotate.setOnClickListener(this);
			check_detail_selected.setOnClickListener(this);
			mBigImageViewPager = (ViewPager) view
					.findViewById(R.id.big_image_pager);
			mBigImagePagerAdapter = new BigImageViewPagerAdapter();
			mBigImageViewPager.setAdapter(mBigImagePagerAdapter);
			// ViewPager设置当前条目
			// ViewPager设置页面监听事件
			mBigImageViewPager
				.setOnPageChangeListener(new BigImagePageChangeListener());
			
		}
		
		updateViewPager();
		return view;
	}
	@Override
	public void onStart() {
		super.onStart();
		mBigImageViewPager.setCurrentItem(mCurrPageIndex);
	}
	private void updateViewPager() {
		ImageFolderInfo imageFolderInfo = dataService.getCurrentAlbum();
		String title0;
		if (imageFolderInfo != null) {
			title0 = imageFolderInfo.getmFolderName();
		} else {
			title0 = getResources().getString(R.string.tabtitle_photo);
		}
		callback.setTitleString(title0, "(" + (mCurrPageIndex + 1) + "/"
				+ dataService.getmBigImageList().size() + ")");
		List<ImageInfoHolder> mBigImageList = dataService.getmBigImageList();
		check_detail_selected.setChecked(chooser.isChoosed(mBigImageList.get(mCurrPageIndex)));
	}
	@Override
	public void refreshImagesData() {
		mBigImagePagerAdapter.notifyDataSetChanged();
	}
	private RelativeLayout generalPagerviewItem() {
		RelativeLayout viewItem = (RelativeLayout) getActivity()
				.getLayoutInflater().inflate(R.layout.xc_big_image_itemview,
						null);
		PhotoView ivt = (PhotoView) viewItem.findViewById(R.id.detail_image);
		ivt.setImageResource(R.drawable.xc_photo_temp_img);
		ivt.setOnPhotoTapListener(new OnPhotoTapListener() {
			@Override
			public void onPhotoTap(View view, float x, float y) {
				if (callback != null) {
					callback.onFragmentExit(GalleryDetailFragment.this);
				}
			}
		});
		return viewItem;
	}
	@Override
	public void onClick(View v) {
		List<ImageInfoHolder> mBigImageList = dataService.getmBigImageList();
		if (v == tvw_detail_rotate) {
			View itemView = mBigImageViewPager.findViewById(mCurrPageIndex);
			PhotoView ivt = (PhotoView) itemView
					.findViewById(R.id.detail_image);
			Drawable drawable = ivt.getDrawable();
			if ((drawable instanceof BitmapDrawable)) {
				Bitmap bmTemp = ((BitmapDrawable) drawable).getBitmap();
				if (bmTemp == null) {
					return;
				}
				Bitmap bmDest = PictureUtil.rotaingImageView(90, bmTemp);
				ivt.setImageBitmap(bmDest);
				PictureUtil.saveBitmapFile(bmDest,
						mBigImageList.get(mCurrPageIndex).mImagePath);
				PictureUtil.galleryAddPic(getActivity(),
						mBigImageList.get(mCurrPageIndex).mImagePath);
				ThumbImageFetcher.getInstance(getActivity()).clearCache();
				bmTemp.recycle();
			}
		} else if (v == check_detail_selected) {
			if (((CheckBox) v).isChecked()) {
				if (!chooser.choosePhoto(mBigImageList.get(mCurrPageIndex))) {
					((CheckBox) v).setChecked(false);
					Toast.makeText(getActivity(),
							R.string.showimage_uploadCountFull,
							Toast.LENGTH_SHORT).show();
				}
			} else {
				chooser.unChoose(mBigImageList.get(mCurrPageIndex));
			}
			callback.onPhotoChoiceChange();
		}
	}
	public static GalleryBaseFragment newInstance(String tag) {
		GalleryBaseFragment f = new GalleryDetailFragment();
		Bundle args = new Bundle();
		args.putString("tag", tag);
		f.setArguments(args);
		return f;
	}

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
			List<ImageInfoHolder> mBigImageList = dataService
					.getmBigImageList();
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
			ImageLoader.getInstance().displayImage(
					Scheme.FILE.wrap(dataService.getmBigImageList().get(
							position).mImagePath), ivt);
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
			mCurrPageIndex = arg0;
			updateViewPager();
			//						updateImageHolderInfo();
		}
	}

	@Override
	protected void loadImageData() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void loadDataComplete() {
		// TODO Auto-generated method stub
		
	}
	@Override
	protected void notifyDataUpdate() {
		// TODO Auto-generated method stub
		
	}
}
