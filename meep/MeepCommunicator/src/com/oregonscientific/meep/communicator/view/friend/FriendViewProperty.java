package com.oregonscientific.meep.communicator.view.friend;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.oregonscientific.meep.communicator.R;

/**
 * A class of Friend View's Properties
 */
public class FriendViewProperty {
	
	private int resourceId = 0;
	private Drawable backgroundImage = null;
	private Drawable border = null;
	private float iconRotateDegree = 0f;
	private float backgroundRotateDegree = 0f;
	private boolean mirrorBadge = false;
	
	/**
	 * Constructor for FriendViewProperty
	 * @param resourceId resource id of layout xml file
	 * @param backgroundImage drawable for background image
	 * @param border drawable for boarder of user icon
	 * @param iconRotateDegree rotation degree of user icon
	 * @param backgroundRotateDegree rotation degree of background image
	 * @param mirrorBadge true if badge image is mirrored, false otherwise
	 */
	public FriendViewProperty(
			int resourceId,
			Drawable backgroundImage,
			Drawable border,
			float iconRotateDegree,
			float backgroundRotateDegree,
			boolean mirrorBadge) {
		
		this.resourceId = resourceId;
		this.backgroundImage = backgroundImage;
		this.border = border;
		this.iconRotateDegree = iconRotateDegree;
		this.backgroundRotateDegree = backgroundRotateDegree;
		this.mirrorBadge = mirrorBadge;
	}
	
	/**
	 * Get the resource id of layout
	 * @return resource id of layout
	 */
	public int getResourceId() {
		return resourceId;
	}
	
	/**
	 * Set the resource id of layout
	 * @param resourceId resource id of layout
	 */
	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}
	
	/**
	 * Get the drawable of background image
	 * @return drawable of background image
	 */
	public Drawable getBackgroundImage() {
		return backgroundImage;
	}
	
	/**
	 * Set the drawable of background image
	 * @param backgroundImage drawable of background image
	 */
	public void setBackgroundImage(Drawable backgroundImage) {
		this.backgroundImage = backgroundImage;
	}
	
	/**
	 * Get the drawable of border of background image
	 * @return drawable of border of background image
	 */
	public Drawable getBorder() {
		return border;
	}
	
	/**
	 * Set the drawable of border of background image
	 * @param border drawable of border of background image
	 */
	public void setBorder(Drawable border) {
		this.border = border;
	}
	
	/**
	 * Get the rotation degree of user icon
	 * @return rotation degree of user icon
	 */
	public float getIconRotateDegree() {
		return iconRotateDegree;
	}
	
	/**
	 * Set the rotation degree of user icon
	 * @param iconRotateDegree rotation degree of user icon
	 */
	public void setIconRotateDegree(float iconRotateDegree) {
		this.iconRotateDegree = iconRotateDegree;
	}
	
	/**
	 * Get the rotation degree of background image
	 * @return rotation degree of background image
	 */
	public float getBackgroundRotateDegree() {
		return backgroundRotateDegree;
	}
	
	/**
	 * Set the rotation degree of background image
	 * @param backgroundRotateDegree rotation degree of background image
	 */
	public void setBackgroundRotateDegree(float backgroundRotateDegree) {
		this.backgroundRotateDegree = backgroundRotateDegree;
	}
	
	/**
	 * Get whether badge image is mirrored
	 * @return true if badge image is mirrored, false otherwise
	 */
	public boolean getIsBadgeMirror() {
		return mirrorBadge;
	}
	
	/**
	 * Set mirror state of badge image
	 * @param mirror true if badge image is mirrored, false otherwise
	 */
	public void mirrorBadge(boolean mirror) {
		this.mirrorBadge = mirror;
	}
	
	/**
	 * Get the default Friend View Property
	 * @param context current context
	 * @return the default Friend View Property
	 */
	public static FriendViewProperty getDefaultFriendViewProperty(Context context) {
		return new FriendViewProperty(R.layout.friend_style1, context.getResources().getDrawable(R.drawable.custom_style1), null, 15f, 0f, true);
	}
	
}