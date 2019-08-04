package com.oregonscientific.meep.communicator.view.conversation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.User;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;
import com.oregonscientific.meep.util.BitmapUtils;
import com.oregonscientific.meep.util.ImageDownloader;

/**
 * Base class of a single conversation message view
 */
public class BaseConversationMessage extends LinearLayout {
	
	private String IMAGE_CACHE_DIR = "message_icons";
	private final String myName = "Me";
	private final String friendName = "Jane";
	
	/**
	 * Constructor for conversation message
	 * @param context current context
	 * @param message message to be constructed
	 * @param user current user
	 */
	public BaseConversationMessage(
			Context context,
			ConversationMessage message,
			User user) {
		super(context);
		init(context, message, user);
	}
	
	/**
	 * Initialize the layout of conversation message
	 * @param context current context
	 * @param message message to be initialized
	 * @param user current user
	 */
	public void init(Context context, ConversationMessage message, User user) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		
		if (message.getIsIncomingMessage()) {
			view = inflater.inflate(R.layout.conversation_message_style1, null);
			Friend friend = message.getConversation().getFriend();
			if (friend != null) {
				TextView name = (TextView) view.findViewById(R.id.name);
				name.setText(friend.getName()); // friendName

				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				CommunicatorActivity activity = (CommunicatorActivity) context;

				String iconAddress = friend.getIconAddress();
				if (iconAddress == null || iconAddress.length() == 0) {
					Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.default_avatar, icon.getLayoutParams().width, icon.getLayoutParams().height);
					icon.setImageBitmap(bitmap);
				} else {
					ImageDownloader imageDownloader = activity.getImageDownloader();
					if (imageDownloader != null) {
						imageDownloader.download(iconAddress,R.drawable.avatar, icon.getLayoutParams().width, icon.getLayoutParams().height, icon);
					}
				}
			}
		} else {
			view = inflater.inflate(R.layout.conversation_message_style2, null);
			TextView name = (TextView) view.findViewById(R.id.name);
			name.setText(myName);
			
			if (user != null) {
				ImageView icon = (ImageView) view.findViewById(R.id.icon);
				CommunicatorActivity activity = (CommunicatorActivity) context;

				String iconAddress = user.getIconAddress();
				if (iconAddress == null || iconAddress.length() == 0) {
					Bitmap bitmap = BitmapUtils.decodeSampledBitmapFromResource(getResources(), R.drawable.default_avatar, icon.getLayoutParams().width, icon.getLayoutParams().height);
					icon.setImageBitmap(bitmap);
				} else {
					ImageDownloader imageDownloader = activity.getImageDownloader();
					if (imageDownloader != null) {
						imageDownloader.download(iconAddress, R.drawable.avatar, icon.getLayoutParams().width, icon.getLayoutParams().height, icon);
					}
				}
			}
		}
		
		ConversationTextView content = (ConversationTextView) view.findViewById(R.id.message);
		content.setText(message.getContent());
		
		addView(view);
	}
}