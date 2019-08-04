package com.oregonscientific.meep.meepphoto;

import java.io.File;
import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.tool.ImageDownloader;
import com.oregonscientific.meep.tool.ImageLoadingListener;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

;

public class MeepPhotoViewPagerActivity extends Activity {
	private DeactivableViewPager myPager;
	private MyPagerAdapter pagerAdapter;

	List<String> mFileList;
	private Context mContext;
	private ImageDownloader mImageDownloader;
	// Bitmap[] bitmapCache = null;
	// private ImageCache cache;
	private static final String IMAGE_CACHE_DIR = "images";
	private final static int SHOW_WIDTH = 750;
	private final static int SHOW_HEIGHT = 440;
	private final static float SHOW_HEIGHT_IN_FLOAT = 450f;
	
	int fileIdx;
	private String[] fileArray;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.viewpager);

		myPager = (DeactivableViewPager) findViewById(R.id.myviewpager);
		mFileList = new ArrayList<String>();
		mImageDownloader = new ImageDownloader(this, IMAGE_CACHE_DIR);
		mImageDownloader.setmImageDownloadListener(mDownloadListener);
		mContext = getApplicationContext();
	}

	public String getRealPathFromURI(Uri contentUri) {
		String[] proj = { MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(contentUri, proj, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	protected void onStop() {
		finish();
		super.onStop();
	}
	
	private Bitmap openBitmap(String url, int maxWidth, int maxHeight) {
		try {
			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(url, options);
			
			// Calculate inSampleSize
			int inSampleSize = calculateInSampleSize(options, maxWidth, maxHeight);
			options.inSampleSize = inSampleSize;
	    
	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    final Bitmap bmp = BitmapFactory.decodeFile(url, options);
	    return bmp;
		} catch (OutOfMemoryError err) {

		} finally {
			System.gc();
		}
		return null;
	}
	
	/**
	 * Calculates the in sample size for the given width and height requirement
	 * 
	 * @param options
	 *          The options that control downsampling
	 * @param reqWidth
	 *          The required width of the bitmap
	 * @param reqHeight
	 *          The required height of the bitmap
	 * @return the in sample size of the bitmap given the width and height
	 *         requirement
	 */
	private int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			// Calculate ratios of height and width to requested height and width
			final int heightRatio = Math.round((float) height / (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);

			// Choose the smallest ratio as inSampleSize value, this will guarantee
			// a final image with both dimensions larger than or equal to the
			// requested height and width.
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}

		return inSampleSize;
	}

	private void scaleZoomView(Bitmap bmp, int maxWidth, int maxHeight, ZoomView convertView) {
		int bmpWidth = bmp == null ? maxWidth : bmp.getWidth();
		int bmpHeight = bmp == null ? maxHeight : bmp.getHeight();
		
		float photoScale = (float) bmpWidth / (float) bmpHeight;
		float widthScale = (float) bmpWidth / maxWidth;
		float heightScale = (float) bmpHeight / maxHeight;
		
		// Minimize photo using scaleFactor
		if (widthScale >= heightScale) {
			convertView.scaleFactor = widthScale;
		} else if (widthScale < heightScale) {
			convertView.scaleFactor = heightScale;
		}
		
		convertView.fitWidth = (float) bmpWidth / convertView.scaleFactor;
		convertView.fitHeight = (float) bmpHeight / convertView.scaleFactor;
		
		// Align photo to center
		if (photoScale >= maxWidth / (float) maxHeight) {
			convertView.translateStartY = (maxHeight - convertView.fitHeight) / 2;
		} else if (photoScale < maxWidth / (float) maxHeight) {
			convertView.translateStartX = (maxWidth - convertView.fitWidth) / 2;
		}
		
		// Initialize the default translates
		convertView.translateX = convertView.translateStartX;
		convertView.translateY = convertView.translateStartY;
	}

	private int getSelectedIndex(List<String> fileArray, String file) {
		for (int i = 0; i < fileArray.size(); i++) {
			// Log.e("getSelectedIndex", "get: " + file);
			if (mFileList.get(i).equals(file)) {
				Log.e("getSelectedIndex", "i: " + i);
				return i;
			}
		}
		return -1;
	}

	@Override
	protected void onStart() {
		mFileList.clear();
		String[] fileArray = getIntent()
				.getStringArrayExtra(Global.STRING_LIST);
		// Log.d("photo", "get photo:" + fileArray.length);

		if (fileArray == null || fileArray.length == 0) {
			List<File> imageList = MediaManager.getAllImagesFiles();
			for (int i = 0; i < imageList.size(); i++) {
				mFileList.add(imageList.get(i).getAbsolutePath());
			}
			// return;
		} else {
			for (int i = 0; i < fileArray.length; i++) {
				mFileList.add(fileArray[i]);
			}
		}
		//Log.d("photo", "get mFile:" + mFileList.size());

		// Collections.sort(mFileList);
		Collections.reverse(mFileList);

		String selectedFilePath = getIntent()
				.getStringExtra(Global.STRING_PATH);
		if (selectedFilePath == null) {
			Uri uri = getIntent().getData();
			selectedFilePath = getRealPathFromURI(uri);
		}

		fileIdx = getSelectedIndex(mFileList, selectedFilePath);
		// loadPhoto(selectedFilePath);

		//fileArray = getIntent().getStringArrayExtra(Global.STRING_LIST);
		pagerAdapter = new MyPagerAdapter(mFileList);
		myPager.setAdapter(pagerAdapter);
		myPager.setCurrentItem(fileIdx);
		/*
		 * try { gallery.setSelection(fileIdx, false); } catch (Exception e) {
		 * Log.e("photo", "get err:" + e.toString()); }
		 * gallery.setOnItemClickListener(new OnItemClickListener() { public
		 * void onItemClick(AdapterView parent, View v, int position, long id) {
		 * // Toast.makeText(MeepPhotoGalleryActivity.this, "" + position, //
		 * Toast.LENGTH_SHORT).show(); } });
		 */

		super.onStart();
	}

	/*private static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math
						.round(((float) height / (float) reqHeight) * 2);
			} else {
				inSampleSize = Math
						.round(((float) width / (float) reqWidth) * 2);
			}
		}
		return inSampleSize;
	}*/

	/*public static Bitmap decodeSampledBitmapFromResource(Resources res,
			int resId, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeResource(res, resId, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeResource(res, resId, options);
	}
*/
	class MyPagerAdapter extends PagerAdapter {
		private LayoutInflater inflater;
		private Map<Integer, SoftReference<View>> mViewReference = new HashMap<Integer, SoftReference<View>>();
		List<String> mFileList;
		
		MyPagerAdapter(List<String> mFileList) {
			this.mFileList = mFileList;
			inflater = getLayoutInflater();
		}

		@Override
		public int getCount() {
			int count = mFileList.size();
			if(mFileList != null){
				return count;
			}else{
				return 0;
			}
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			View view = null;
			SoftReference<View> reference = mViewReference.get(position);
			if (reference != null) {
				view = reference.get();
				if (view == null) {
					view = inflateView(container, position);
					reference = new SoftReference<View>(view);
				}
			} else {
				view = inflateView(container, position);
				reference = new SoftReference<View>(view);
				mViewReference.put(position, reference);
			}
		/*	if (reference == null || (reference.get()) == null) {
				view = inflateView(container, position);
				reference = new SoftReference<View>(view);
				if ()
			} else {
				view = reference.get();
			}*/
			
			((ViewPager) container).addView(view, 0);
			return view;
		}

		/**
		 * @param container
		 * @param position
		 * @return
		 */
		private View inflateView(ViewGroup container, int position) {
			View view;
			view  = inflater.inflate(R.layout.item_pager_image, container, false);
			final ProgressBar spinner = (ProgressBar) view.findViewById(R.id.loading);
			final ZoomView imageView = (ZoomView) view.findViewById(R.id.image);
			
			imageView.setOnPageScaleListener(mOnPageScaleListener);
			// position = fileIdx;
			String url = mFileList.get(position);
			MyImageLoadingListener loadingListener = new MyImageLoadingListener(spinner);
			mImageDownloader.download(url, imageView, loadingListener);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			((ViewPager) container).removeView((View) object);
		}

	}
	
	private class MyImageLoadingListener implements ImageLoadingListener {
		
		private ProgressBar spinner;
		
		/**
		 * @param spinner
		 */
		public MyImageLoadingListener(ProgressBar spinner) {
			super();
			this.spinner = spinner;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			//Log.d("MyImageLoadingListener", "onLoadingStarted "+spinner);
			spinner.setVisibility(View.VISIBLE);
			
		}
		
		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			spinner.setVisibility(View.GONE);
			//Log.d("MyImageLoadingListener", "onLoadingFailed "+spinner);
		}
		
		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
			spinner.setVisibility(View.GONE);
			//Log.d("MyImageLoadingListener", "onLoadingComplete "+spinner);
		}
		
		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			spinner.setVisibility(View.GONE);
			//Log.d("MyImageLoadingListener", "onLoadingCancelled "+spinner);
		}
	};
	
	private OnPageScaleListener mOnPageScaleListener = new OnPageScaleListener() {
		
		@Override
		public void onScaleEnd(float scale) {
			if (scale > 1.0) {
				myPager.deactivate();
			} else {
				myPager.activate();
			}
			
		}
		
		@Override
		public void onScaleBegin() {
			
			myPager.deactivate();
		}

		@Override
		public void onImageToLeft() {
			myPager.activate();
			
		}

		@Override
		public void onImageToRight() {
			myPager.activate();
			
		}
	};
	
	private ImageDownloader.ImageDownloadListener mDownloadListener = new ImageDownloader.ImageDownloadListener() {
		
		@Override
		public void onDownloadSuccessed(ImageView view, Bitmap bitmap) {
			if (view instanceof ZoomView) {
				scaleZoomView(bitmap, SHOW_WIDTH, SHOW_HEIGHT, (ZoomView) view);
			}
			view.setImageBitmap(bitmap);
		}
		
		@Override
		public void onDownloadFromCache(ImageView view, Bitmap bitmapFromCache) {
			if (view instanceof ZoomView) {
				scaleZoomView(bitmapFromCache, SHOW_WIDTH, SHOW_HEIGHT, (ZoomView) view);
			}
			view.setImageBitmap(bitmapFromCache);
		}
		
		@Override
		public void onDownloadFail(ImageView view) {
			view.setImageResource(R.drawable.ic_empty);
		}
		
		@Override
		public Bitmap loadImageFromUrl(String url) {
			return openBitmap(url, SHOW_WIDTH, SHOW_HEIGHT);
		}
	};
}
