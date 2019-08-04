package com.oregonscientific.meep.communicator.view.conversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.util.ImageDownloader;

/**
 * Base class of a single conversation
 */
public class BaseConversation extends LinearLayout {
	
	private final String TAG = getClass().getSimpleName();
	
	private Conversation conversation;
	
	/**
	 * Constructor for conversation
	 * @param context current context
	 * @param conversation conversation to be constructed
	 * @param big true if the conversation is current, false otherwise
	 */
	public BaseConversation(
			Context context,
			Conversation conversation,
			boolean big) {
		
		super(context);
		init(context, conversation, big);
		this.conversation = conversation;
	}
	
	/**
	 * Get the conversation of this view
	 * @return conversation of this view
	 */
	public Conversation getConversation() {
		return conversation;
	}
	
	/**
	 * Get the conversation view
	 * @return conversation view
	 */
	public View getView() {
		return findViewById(R.id.frame);
	}
	
	/**
	 * Get the close button of the conversation view
	 * @return close button of the conversation view
	 */
	public View getCloseButtonView() {
		return findViewById(R.id.cross);
	}
	
	/**
	 * Initialize the layout of conversation
	 * @param context current context
	 * @param conversation conversation to be initialized
	 * @param big true if the conversation is current, false otherwise
	 */
	public void init(Context context, final Conversation conversation, boolean big) {
		final LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (big) {
			view = inflater.inflate(R.layout.conversation_icon_big, null);
		} else {
			view = inflater.inflate(R.layout.conversation_icon_small, null);
		}
		
		final Friend friend = conversation.getFriend();
		
		if (friend != null) {
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(friend.getName());

			ImageView icon = (ImageView) view.findViewById(R.id.icon);
			if (context != null) {
				CommunicatorActivity activity = (CommunicatorActivity) context;

				String iconAddress = friend.getIconAddress();
				if (iconAddress == null || iconAddress.length() == 0) {
					Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.default_avatar, icon.getWidth(), icon.getHeight());
					icon.setImageBitmap(bitmap);
				} else {
					ImageDownloader imageDownloader = activity.getImageDownloader();
					if (imageDownloader != null) {
						imageDownloader.download(iconAddress, R.drawable.avatar, icon.getLayoutParams().width, icon.getLayoutParams().height, icon);
					}
				}
			}
		}

		addView(view);
	}

}