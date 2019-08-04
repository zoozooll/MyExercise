/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.home.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oregonscientific.meep.home.ProfileActivity;
import com.oregonscientific.meep.home.R;
import com.oregonscientific.meep.widget.StrokedTextView;

/**
 * Display user profile information 
 */
public class HeaderProfileView extends LinearLayout {
	
	private StrokedTextView mDisplayName;
	private TextView userTagTextView;
	private UserImageView userImageView;
	private Drawable signedIn, signedOut;
	
	public HeaderProfileView(Context context) {
		this(context, null);
	}

	public HeaderProfileView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}
	
	public HeaderProfileView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		
		View v = View.inflate(context, R.layout.header_profile, this);
		mDisplayName = (StrokedTextView) v.findViewById(R.id.header_profile_display_name);
		userTagTextView = (TextView) v.findViewById(R.id.header_profile_user_tag);
		
		userImageView = (UserImageView) v.findViewById(R.id.header_profile_user_image);
		if (userImageView != null) {
			userImageView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					if (view.isClickable()) {
						Intent launchIntent = new Intent(getContext(), ProfileActivity.class);
						((Activity) getContext()).startActivityForResult(launchIntent, 0);
					}
				}
				
			});
		}
		
		signedIn = context.getResources().getDrawable(R.drawable.green);
		signedOut = context.getResources().getDrawable(R.drawable.red);
		enableChangeProfileSetting(true);
	}
	
	/**
	 * Set a bitmap as profile picture 
	 * @param b 
	 */
	public void setProfilePicture(Bitmap b) {
		if (b != null && userImageView != null) {
			userImageView.setUserImageBitmap(b);
		}
	}
	
	/**
	 * Set a bitmap as profile picture 
	 * 
	 * @param url The URL to the profile picture 
	 */
	public void setProfilePicture(String url) {
		setProfilePicture(url, false);
	}
	
	/**
	 * Set a bitmap as profile picture 
	 * 
	 * @param url The URL to the profile picture
	 * @param hasShadown a boolean that indicates whether or not a shadow should be rendered
	 */
	public void setProfilePicture(String url, boolean hasShadow) {
		if (url != null && userImageView != null) {
			userImageView.setUserImage(url, hasShadow);
		}
	}
	
	/**
	 * Sets a drawable as the content of the {@link ImageView} used to display 
	 * the avatar for a given user 
	 * 
	 * @param resId the resource identifier of the drawable
	 */
	public void setProfilePicture(int resId) {
		if (userImageView != null) {
			userImageView.setUserImageResource(resId);
		}
	}
	
	
	/**
	 * Set the user name field then show in menu header view
	 * @param name
	 */
	public void setUserDisplayName(String name) {
		if (name != null && mDisplayName != null) {
			mDisplayName.setText(name);
		}
	}
	
	/**
	 * Set the user tag field then show in menu header view
	 * @param tag
	 */
	public void setUserTag(String tag) {
		if (tag != null && userTagTextView != null) {
			userTagTextView.setText(tag);
		}
	}
	
	/**
	 * Enables/disables setting user profile
	 * 
	 * @param enable a boolean that indicates whether or not the user profile can be edited
	 */
	public void enableChangeProfileSetting(boolean enable) {
		userImageView.setEnabled(enable);
	}
	
	/**
	 * Tells the view whether a user is signed in and update indicator appropriately
	 * 
	 * @param b {@code true} if a user is signed in, {@code false} otherwise
	 */
	@SuppressWarnings("deprecation")
	public void signedIn(boolean b) {
		View v = findViewById(R.id.signin_status);
		if (v == null) {
			return;
		}
		
		if (signedIn == null || signedOut == null) {
			return;
		}
		
		v.setBackgroundDrawable(b ? signedIn : signedOut);
	}
	
	/**
	 * Clears the image when sign out
	 */
	public void clearImage() {
		if (userImageView != null) {
			userImageView.clearImage();
		}
	}
	
}
