package com.oregonscientific.meep.communicator.view.friend;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;
import com.oregonscientific.meep.util.ImageDownloader;
import com.oregonscientific.meep.widget.StrokedTextView;

/**
 * Base class of a single friend view
 */
public abstract class BaseFriend extends LinearLayout {
	
	private int mUnreadCount = 0;
	private final int MAX_UNREAD_COUNT = 999;
	
	/**
	 * Constructor for a friend view
	 * @param context current context
	 * @param friend friend to be constructed
	 */
	public BaseFriend(Context context, Friend friend) {
		super(context);
		init(context, friend);
	}
	
	/**
	 * Update the unread message count for the friend
	 * @param unreadCount updated unread message count of the friend
	 */
	
	public void setUnreadCount(int unreadCount) {
		
		mUnreadCount = unreadCount;
		
		if (mUnreadCount > MAX_UNREAD_COUNT)
			mUnreadCount = MAX_UNREAD_COUNT;
		
		TextView unreadCountView = (TextView) findViewById(R.id.unread_count);
		
		if (mUnreadCount == 0) {
			
			unreadCountView.setVisibility(View.INVISIBLE);
			unreadCountView.setVisibility(View.INVISIBLE);
		} else {

			unreadCountView.setVisibility(View.VISIBLE);
			unreadCountView.setVisibility(View.VISIBLE);
		}
		
		unreadCountView.setText(String.valueOf(unreadCount));
	}
	
	public int getUnreadCount() {
		return mUnreadCount;
	}
	
	/**
	 * Initialize the layout of a friend
	 * @param context current context
	 * @param friend friend to be initialized
	 */
	public void init(Context context, Friend friend) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		
		FriendViewProperty property = getProperty();
		// use style 1 if Friend View Property is not found
		if (property == null) {
			property = FriendViewProperty.getDefaultFriendViewProperty(getContext());
		}
		
		View view = inflater.inflate(property.getResourceId(), null);
		// use style 1 if resource id set is not found
		if (view == null) {
			view = inflater.inflate(R.layout.friend_style1, null);
		}
		
		// set whether badge icon is mirrored
		if (property.getIsBadgeMirror()) {
			mirrorBadge(view);
		}
		
		// set icon border
		ImageView border = (ImageView) view.findViewById(R.id.icon_border);
		rotateView(border, property.getIconRotateDegree());
		
		// set icon image
		ImageView icon = (ImageView) view.findViewById(R.id.icon);
		rotateView(icon, property.getIconRotateDegree());
		
		if (context != null) {
			CommunicatorActivity activity = (CommunicatorActivity) context;
			ImageDownloader imageDownloader = activity.getImageDownloader();
			String iconAddress = friend.getIconAddress();
			if (iconAddress != null && imageDownloader != null) {
				imageDownloader.download(iconAddress, R.drawable.avatar, icon.getLayoutParams().width, icon.getLayoutParams().height, icon);
			}
		}
		
		// set background image together with friend's name
		ImageView background = (ImageView) view.findViewById(R.id.background);
		rotateView(background, property.getBackgroundRotateDegree());
		StrokedTextView name = (StrokedTextView) view.findViewById(R.id.name);
		rotateView(name, property.getBackgroundRotateDegree());
		name.setText(friend.getName());
		
		addView(view);
		
		setUnreadCount(mUnreadCount);

	}
	
	/**
	 * Mirror the badge image
	 * @param parent the parent view where the badge belongs to
	 */
	@SuppressWarnings("deprecation")
	private void mirrorBadge(View parent) {
		
		TextView unreadCountView = (TextView) parent.findViewById(R.id.unread_count);
		if (unreadCountView == null) {
			return;
		}
		
		float[] mirrorY = { -1, 0, 0, 0, 1, 0, 0, 0, 1 };
		Matrix matrixMirrorY = new Matrix();
		matrixMirrorY.setValues(mirrorY);
		Matrix matrix = new Matrix();
		matrix.postConcat(matrixMirrorY);
		
		Bitmap bitmap = ((BitmapDrawable) unreadCountView.getBackground()).getBitmap();
		if (bitmap != null) {
			Bitmap mirrorBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
			if (mirrorBitmap != null) {
				unreadCountView.setBackgroundDrawable(new BitmapDrawable(mirrorBitmap));
			}
		}
		
	}
	
	/**
	 * Rotate a view by a degree
	 * @param view view to be rotated
	 * @param degrees degree of rotation
	 */
	private void rotateView(View view, float degrees) {
		
		if (view == null || degrees == 0f) {
			return;
		}
		
		view.setRotation(degrees);
//		RotateAnimation rotateAnimation = new RotateAnimation(0f, degrees, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//		rotateAnimation.setFillAfter(true);
//		rotateAnimation.setInterpolator(new LinearInterpolator());
//		rotateAnimation.setDuration(0);
//		view.startAnimation(rotateAnimation);
		
	}
	
	/**
	 * Get the Properties of the Friend's View
	 * @return a FriendViewProperty that contains properties of Friend's View
	 */
	public abstract FriendViewProperty getProperty();
	
}