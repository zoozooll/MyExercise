/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.oregonscientific.meep.home.HomeApplication;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.util.ImageDownloader;
import com.oregonscientific.meep.util.UriUtils;

/**
 * Handle to create a ImageView with fade in and fade out Animation
 * @author joyaether
 *
 */
public class AnimatedImageView extends RelativeLayout{
	
	private static final int FADE_IN_DURATION = 1000;
	private static final int START_OFFSET = 2000;
	private static final int FADE_OUT_DURATION = 3000;
	private int animatingIndex = 0;
	private final int MAX_WIDTH = 118;
	private final int MAX_HEIGHT = 76;
	private AlphaAnimation fadeIn;
	private AlphaAnimation fadeOut;
	private List<AnimatedImageViewItem> itemList;
	private final String TAG = getClass().getSimpleName();
	public static final String VIEW_TAG = "SLIDESHOW";

	public AnimatedImageView(Context context) {
		this(context, null);

	}
	
	public AnimatedImageView(Context context, AttributeSet attributeSet) {
		this(context, attributeSet, 0);	
	}
	
	public AnimatedImageView(Context context, AttributeSet attributeSet, int defaultStyle) {
		super(context, attributeSet, defaultStyle);
		this.setTag(VIEW_TAG);
		itemList = new ArrayList<AnimatedImageViewItem>();
		initAnimation();
	}
	
	@Override
	public void onFinishInflate() {
		super.onFinishInflate();
	}
	
	public void startAnimation() {
		if(fadeIn.hasStarted()) {
			fadeIn.cancel();
		}
		if (fadeOut.hasStarted()) {
			fadeOut.cancel();
		}
		if (getItems().size() > 0) {
			setAnimationListener();
		}
	}
	
	private void setAnimationListener() {
		fadeIn.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				AnimatedImageViewItem item = getItem(getCurrentAnimateIndex());
				if (item == null) {
					return;
				}
				
				ImageView view = (ImageView) findViewWithTag(item);
				if (view == null) {
					return;
				}
				view.startAnimation(fadeOut);
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				AnimatedImageViewItem item = getItem(getCurrentAnimateIndex());
				if (item == null) {
					return;
				}
				
				ImageView view = (ImageView) findViewWithTag(item);
				if (view == null) {
					return;
				}
				view.setVisibility(View.VISIBLE);
			}
			
		});
		
		fadeOut.setAnimationListener(new AnimationListener() {

			@Override
			public void onAnimationEnd(Animation arg0) {
				AnimatedImageViewItem item = getItem(getCurrentAnimateIndex());
				if (item == null) {
					return;
				}
				
				ImageView view = (ImageView) findViewWithTag(item);
				if (view == null) {
					return;
				}
				view.setVisibility(View.GONE);
				//reset to first image of index 0
				if(getCurrentAnimateIndex() == getItems().size() - 1) {
					setCurrentAnimateIndex(0);
				}else {
					setCurrentAnimateIndex(getCurrentAnimateIndex() + 1);
				}
				
				AnimatedImageViewItem nextItem = getItem(getCurrentAnimateIndex());
				if (nextItem == null) {
					return;
				}
				
				ImageView nextView = (ImageView) findViewWithTag(nextItem);
				if (nextView == null) {
					return;
				}
				nextView.startAnimation(fadeIn);
				
			}

			@Override
			public void onAnimationRepeat(Animation arg0) {
				
			}

			@Override
			public void onAnimationStart(Animation arg0) {
				
			}
			
		});
		AnimatedImageViewItem item = getItem(getCurrentAnimateIndex());
		if (item == null) {
			return;
		}
		
		ImageView view = (ImageView) findViewWithTag(item);
		if (view == null) {
			return;
		}
		view.startAnimation(fadeIn);
	}
	
	/**
	 * Initialized the animation, set it with non stop
	 */
	public void initAnimation() {
		fadeIn = new AlphaAnimation(0f, 1f);
		fadeIn.setDuration(FADE_IN_DURATION);
		
		fadeOut = new AlphaAnimation(1f, 0f);
		fadeOut.setDuration(FADE_OUT_DURATION);
		fadeOut.setStartOffset(START_OFFSET);
	}

	/**
	 * @return the animatingIndex
	 */
	public int getAnimatingIndex() {
		return animatingIndex;
	}

	/**
	 * @param animatingIndex the animatingIndex to set
	 */
	public void setAnimatingIndex(int animatingIndex) {
		this.animatingIndex = animatingIndex;
	}

	
	public synchronized void addItem(AnimatedImageViewItem item) {
		if (getItems() != null) {
			getItems().add(item);
			ImageView imageView = new ImageView(getContext());
			imageView.setTag(item);
			final Intent intent = item.getIntent();
			imageView.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					try{
						getContext().startActivity(intent);
					} catch (Exception e) {
						//Ingore
					}
				}
				
			});
			
			addView(imageView);
			try {
				if (UriUtils.isFileURI(item.getPath())) {
					Bitmap b = BitmapUtils.decodeSampledBitmapFromFile(new File(item.getPath()), MAX_WIDTH, MAX_HEIGHT);
				} else {
					// Downloads the image from the given URL
					HomeApplication app = (HomeApplication) getContext().getApplicationContext();
					ImageDownloader downloader = app == null ? null : app.getDownloader(HomeApplication.CACHE_STORE);
					if (downloader != null) {
						downloader.download(item.getPath(), MAX_WIDTH, MAX_HEIGHT, imageView);
					}
				}
			} catch (Exception ex) {
				Log.e(TAG, "Cannot set content of the view because " + ex);
			}
			imageView.setVisibility(View.GONE);
		}
	}
	
	/**
	 * @return the itemList
	 */
	public List<AnimatedImageViewItem> getItems() {
		return itemList;
	}

	public synchronized void clearItems() {
		removeAllViews();
		getItems().clear();
	}
	
	public synchronized AnimatedImageViewItem getItem(int position) {
		return getItems().get(position);
	}
	
	private int getCurrentAnimateIndex() {
		return animatingIndex;
	}
	
	private void setCurrentAnimateIndex(int index) {
		this.animatingIndex = index;
	}

	public class AnimatedImageViewItem {
		private Intent intent;
		private String path;
		
		public AnimatedImageViewItem() {
		
		}
		
		public AnimatedImageViewItem(Intent intent, String path) {
			this.intent = intent;
			this.path = path;
		}
		
		/**
		 * @return the intent
		 */
		public Intent getIntent() {
			return intent;
		}
		/**
		 * @param intent the intent to set
		 */
		public void setIntent(Intent intent) {
			this.intent = intent;
			
		}
		/**
		 * @return the path
		 */
		public String getPath() {
			return path;
		}
		/**
		 * @param path the path to set
		 */
		public void setPath(String path) {
			this.path = path;
		}
		
		
		
	}
	
}
