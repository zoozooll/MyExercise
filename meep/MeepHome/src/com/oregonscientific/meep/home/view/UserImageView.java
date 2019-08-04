/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.home.HomeApplication;
import com.oregonscientific.meep.home.ProfileActivity;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.util.ImageDownloader;
import com.oregonscientific.meep.util.UriUtils;

/**
 * This is a view for construct the ImageView with click event and show the MEEP User image 
 */
public class UserImageView extends RelativeLayout {
	
	private final String TAG = getClass().getSimpleName();
	
	/**
	 * The maximum width and height of the AvatarView
	 */
	private final static int WIDTH = 90;
	private final static int HEIGHT = 90;
	
	private ImageView userImage;
	private TextView editTextView;
	private ProgressBar progressBar;
	
	public UserImageView(Context context) {
		super(context);
		setLayout(context);
	}

	/**
	 * Initialized the layout 
	 * @param context
	 */
	private void setLayout(Context context) {
		setLayoutParams(new LayoutParams(WIDTH + 10, HEIGHT + 10));
		
		userImage = new ImageView(context);
		userImage.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		userImage.setImageDrawable(context.getResources().getDrawable(R.drawable.default_avatar));
		
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		editTextView = new TextView(context);
		editTextView.setGravity(Gravity.CENTER_HORIZONTAL);
		editTextView.setLayoutParams(layoutParams);
		editTextView.setText(R.string.menu_item_header_edit);
		editTextView.setTextColor(Color.WHITE);
		editTextView.setBackgroundColor(Color.GRAY);
		editTextView.setAlpha(0.90f);
		progressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleInverse);
		progressBar.setVisibility(View.INVISIBLE);
		addView(userImage);
		setEnabled(false);
		RelativeLayout.LayoutParams progressBarLayoutParams = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		progressBarLayoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		addView(progressBar, progressBarLayoutParams);
		addView(editTextView);
		
		setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Intent launchIntent = new Intent(getContext(), ProfileActivity.class);
				((Activity) getContext()).startActivityForResult(launchIntent, 0);
			}
			
		});
		
	}
	
	public UserImageView(Context context, AttributeSet attributeSet) {
		super(context, attributeSet);
		setLayout(context);
	}
	
	/**
	 * Set the User image with bitmap and show on the view
	 * @param b
	 */
	public void setUserImageBitmap(Bitmap b){
		setUserImageBitmap(b, false);
	}
	
	/**
	 * Sets the content of this view to the {@link Bitmap} specified
	 * 
	 * @param b The {@link Bitmap} to use as the content of this view
	 * @param hasShadow A boolean that indicates whether or not a shadow should be rendered
	 */
	public void setUserImageBitmap(Bitmap b, boolean hasShadow) {
		// Quick return if the request cannot be handled
		if (b == null || userImage == null) {
			return;
		}
		
		if (hasShadow) {
			Bitmap bitmap = Bitmap.createBitmap(WIDTH + 10, HEIGHT + 10, b.getConfig());
			Canvas canvas = new Canvas(bitmap);
			Paint paint = new Paint();
			paint.setShadowLayer(5.0f, 10f, 10f, Color.BLACK);
			canvas.drawBitmap(b, 0, 0, paint);
			userImage.setImageBitmap(bitmap);
		} else {
			userImage.setImageBitmap(b);
		}
	}
	
	/**
	 * Sets the content of this view to the {@like File} specified
	 * 
	 * @param file The image file
	 */
	public void setUserImageFile(File file) {
		setUserImageFile(file, false);
	}
	
	/**
	 * Sets the content of this view to the {@like File} specified
	 * 
	 * @param file The image file
	 * @param hasShadow A boolean that indicates whether or not a shadow should be rendered
	 */
	public void setUserImageFile(File file, boolean hasShadow) {
		Bitmap b = BitmapUtils.decodeSampledBitmapFromFile(file, WIDTH, HEIGHT);
		setUserImageBitmap(b, hasShadow);
	}
	
	
	/**
	 * Sets a drawable as the content of the {@link ImageView} used to display 
	 * the avatar for a given user 
	 * 
	 * @param resId the resource identifier of the drawable
	 */
	public void setUserImageResource(int resId) {
		if (userImage != null) {
			Bitmap b = BitmapUtils.decodeSampledBitmapFromResource(getResources(), resId, WIDTH, HEIGHT);
			setUserImageBitmap(b);
		}
	}
	
	/**
	 * Sets the content of this view to the resource identified by the given {@code url}
	 * 
	 * @param url A string representing the location of the resource.
	 */
	public void setUserImage(String url) {
		setUserImage(url, false);
	}
	
	/**
	 * Sets the content of this view to the resource identified by the given {@code url}
	 * 
	 * @param url A string representing the location of the resource.
	 * @param hasShadow A boolean that indicates whether or not a shadow should be rendered
	 */
	public void setUserImage(String url, boolean hasShadow) {
		try {
			if (UriUtils.isFileURI(url)) {
				setUserImageFile(new File(url));
			} else {
				// Downloads the image from the given URL
				HomeApplication app = (HomeApplication) getContext().getApplicationContext();
				ImageDownloader downloader = app == null ? null : app.getDownloader(HomeApplication.CACHE_PROFILE);
				if (downloader != null) {
					downloader.download(url, R.drawable.default_avatar, WIDTH, HEIGHT, userImage);
				}
			}
		} catch (Exception ex) {
			Log.e(TAG, "Cannot set content of the view because " + ex);
		}
	}
	
	/**
	 * Clears a previously set drawable 
	 */
	public void clearImage() {
		if (userImage != null) {
			userImage.setImageBitmap(null);
		}
	}
	
	public static int getMaximunImageWidth() {
		return WIDTH;
	}
	
	public static int getMaximunImageHeight() {
		return HEIGHT;
	}
	
	public void setEditLableBackgroundColor(int color) {
		editTextView.setBackgroundColor(color);
	}
	
	public void showProgressBar(boolean isShow) {
		if (progressBar != null) {
			progressBar.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
		}
	}
	
}
