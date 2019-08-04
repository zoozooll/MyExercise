package com.oregonscientific.meep.store2.ctrl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.widget.Switch;

import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationCoinsAllocatedItem;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationContentApprovedItem;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationContentRejectItem;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationMessageItem;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;

public class MeepDownlaodMessageConverter {
	//download
	private static final String MESSAGE_FIELD_URL = "url";
	private static final String MESSAGE_FIELD_NAME = "name";
	private static final String MESSAGE_FIELD_CHECKSUM = "checksum";
	private static final String MESSAGE_FIELD_IMAGE = "image";
	private static final String MESSAGE_FIELD_ITEM_ID = "item_id";
	private static final String MESSAGE_FIELD_PACKAGE_NAME = "package_name";
	
	//approve,reject,coin allocate
	private static final String MESSAGE_FIELD_LOC_KEY = "loc_key";
	private static final String MESSAGE_FIELD_LOC_ARGS = "loc_args";
	
	private static final int ARGS_INDEX_PARENT_NAME = 0;
	private static final int ARGS_INDEX_APPLICATION_NAME = 1;
	private static final int ARGS_INDEX_COINS = 1;
	
	
	//all types
	private static final String TYPE_COINS_ALLOCATED = "COINS_ALLOCATED";
	private static final String TYPE_CONTENT_APPROVED = "CONTENT_APPROVED";
	private static final String TYPE_CONTENT_REJECTED = "CONTENT_REJECTED";
	private static final String TYPE_DOWNLOAD_ITEM = "???";
	
	
	//download or not
	public static final int MESSAGE_TYPE_DOWNLOAD =0;
	public static final int MESSAGE_TYPE_OTHERS =1;
	
	
	private DownloadStoreItem convertedDownloadItem = null;
	private NotificationMessageItem convertedNotificationItem = null;
	
	private Message message;
	
	public MeepDownlaodMessageConverter(Message message) {
		this.message = message;
	}
	
	public int getTypeOfMessage()
	{
		if(message.getProperty(MESSAGE_FIELD_LOC_KEY)!=null)
		{
			return MESSAGE_TYPE_OTHERS;
		}
		else
		{
			return MESSAGE_TYPE_DOWNLOAD;
		}
	}
	
	DownloadStoreItem generateDownloadItem(Message message)
	{
		String url = (String) message.getProperty(MESSAGE_FIELD_URL);
		String name = (String) message.getProperty(MESSAGE_FIELD_NAME);
		String checksum = (String) message.getProperty(MESSAGE_FIELD_CHECKSUM);
		String imageUrl = (String) message.getProperty(MESSAGE_FIELD_IMAGE);
		String itemId = (String) message.getProperty(MESSAGE_FIELD_ITEM_ID);
		String packageName = (String) message.getProperty(MESSAGE_FIELD_PACKAGE_NAME);
		String type = getTypeOfDownloadItem(message.getOperation());
		
		if(type == null) {return null;}
		
		DownloadStoreItem newDownloadItem = new DownloadStoreItem(itemId, name, type, imageUrl, url, checksum, packageName);
		return newDownloadItem;
	}
	
	@SuppressWarnings("unchecked")
	NotificationMessageItem generateNotificationMessageItem(Context context,Message message)
	{
		String locKey = (String) message.getProperty(MESSAGE_FIELD_LOC_KEY);
		List<Object> locArgs = (ArrayList<Object>) message.getProperty(MESSAGE_FIELD_LOC_ARGS);
		if(locArgs == null || locKey == null)
		{
			return null;
		}
		if(locKey.endsWith(TYPE_COINS_ALLOCATED))
		{
			return new NotificationCoinsAllocatedItem(context,NotificationCoinsAllocatedItem.COINS_ALLOCATED, (String)locArgs.get(ARGS_INDEX_PARENT_NAME),(Double)locArgs.get(ARGS_INDEX_COINS));
		}
		else if(locKey.endsWith(TYPE_CONTENT_APPROVED))
		{
			return new NotificationContentApprovedItem(context,NotificationContentApprovedItem.CONTENT_APPROVED, (String)locArgs.get(ARGS_INDEX_PARENT_NAME),(String)locArgs.get(ARGS_INDEX_APPLICATION_NAME));
		}
		else if(locKey.endsWith(TYPE_CONTENT_REJECTED))
		{
			return new NotificationContentRejectItem(context,NotificationContentRejectItem.COINS_REJECTED, (String)locArgs.get(ARGS_INDEX_PARENT_NAME),(String)locArgs.get(ARGS_INDEX_APPLICATION_NAME));
		}
		return null;
	}

	private static String getTypeOfDownloadItem(String opcode) {
		if (Message.OPERATION_CODE_DOWNLOAD_EBOOK.equals(opcode)) {
			return DownloadStoreItem.TYPE_EBOOK;
		} else if (Message.OPERATION_CODE_DOWNLOAD_APP.equals(opcode)) {
			return DownloadStoreItem.TYPE_APP;
		} else if (Message.OPERATION_CODE_DOWNLOAD_GAME.equals(opcode)) {
			return DownloadStoreItem.TYPE_GAME;
		}
		//filter other type
		return null;
		// TODO:add typemusic/movie
		// else if(Message.OPERATION_CODE_DOWNLOAD_MUSIC.equals(opcode))
		// {
		// return DownloadStoreItem.TYPE_MUSIC;
		// }
		// else if(Message.OPERATION_CODE_DOWNLOAD_MOVIE.equals(opcode))
		// {
		// return DownloadStoreItem.TYPE_MOVIE;
		// }

	}

	public DownloadStoreItem getConvertedDownloadItem() {
		if(convertedDownloadItem == null) convertedDownloadItem = generateDownloadItem(message);
		return convertedDownloadItem;
	}

	public void setConvertedDownloadItem(DownloadStoreItem convertedDownloadItem) {
		this.convertedDownloadItem = convertedDownloadItem;
	}

	public NotificationMessageItem getConvertedNotificationItem(Context context) {
		if(convertedNotificationItem == null) convertedNotificationItem = generateNotificationMessageItem(context, message);
		return convertedNotificationItem;
	}

	public void setConvertedNotificationItem(NotificationMessageItem convertedNotificationItem) {
		this.convertedNotificationItem = convertedNotificationItem;
	}
	

}
