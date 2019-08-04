package com.oregonscientific.meep.store2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.control.MeepStorageCtrl;
import com.oregonscientific.meep.database.table.TableEbook;
import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.global.Global.AppType;
import com.oregonscientific.meep.global.object.EncodingBase64;
import com.oregonscientific.meep.message.common.MeepAppMessage;
import com.oregonscientific.meep.message.common.MeepAppMessage.Category;
import com.oregonscientific.meep.message.common.MsmStoreToMeepApp;
import com.oregonscientific.meep.message.common.MsmStoreToMeepEbook;
import com.oregonscientific.meep.message.common.MsmStoreToMeepGame;
import com.oregonscientific.meep.message.common.MsmStoreToMeepMovie;
import com.oregonscientific.meep.message.common.MsmStoreToMeepMusic;
import com.oregonscientific.meep.msm.Message;
import com.oregonscientific.meep.msm.MessageManager;
import com.oregonscientific.meep.notification.NotificationManager;
import com.oregonscientific.meep.opengl.MediaManager;
import com.oregonscientific.meep.store2.adapter.ImageThreadLoader;
import com.oregonscientific.meep.store2.banner.BannerItemsService;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.ImageDownloadCtrl;
import com.oregonscientific.meep.store2.ctrl.MeepDownlaodMessageConverter;
import com.oregonscientific.meep.store2.ctrl.MeepVersionReportCtrl;
import com.oregonscientific.meep.store2.ctrl.RestRequest;
import com.oregonscientific.meep.store2.ctrl.RestRequest.GetPurchasedItemListener;
import com.oregonscientific.meep.store2.ctrl.RestoreImageDownloadCtrl;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationGenerator;
import com.oregonscientific.meep.store2.ctrl.notification.NotificationMessageItem;
import com.oregonscientific.meep.store2.db.DbAdapter;
import com.oregonscientific.meep.store2.db.TableStorePurchasedItem;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.inapp.InAppPurchaseService;
import com.oregonscientific.meep.store2.object.DownloadImageItem;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.MyAppItems;
import com.oregonscientific.meep.store2.object.StoreImageItem;

public class MeepStoreService extends Service {
	
	StoreItemDownloadCtrl mdownDownloadCtrl;
	Context mContext;
	private final IBinder mBinder = new LocalBinder();
	private RestoreImageDownloadCtrl mRestoreImageDownloadCtrl;
	private MeepStoreApplication mApp = null;
	
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return mBinder;
	}
	
//	private Message testDataCoinsAllocated()
//	{
//		Message message = new Message("store", "???");
//
//		Object[] list = new Object[2];
//		list[0] = "Meng";
//		list[1] = 1;
//
//		message.addProperty("loc_key", "COINS_ALLOCATED");
//		message.addProperty("loc_args", list);
//		return message;
//	}
//	private Message testDataContentRejected()
//	{
//		Message message = new Message("store", "???");
//		
//		Object[] list = new Object[2];
//		list[0] = "Meng";
//		list[1] = "Angry Birds";
//		
//		message.addProperty("loc_key", "CONTENT_REJECTED");
//		message.addProperty("loc_args", list);
//		return message;
//	}
//	private Message testDataContentApproved()
//	{
//		Message message = new Message("store", "???");
//		
//		Object[] list = new Object[2];
//		list[0] = "Meng";
//		list[1] = "Angry Birds";
//		
//		message.addProperty("loc_key", "CONTENT_APPROVED");
//		message.addProperty("loc_args", list);
//		return message;
//	}

	public void handleMessage(Message message)
	{
		MeepDownlaodMessageConverter convertedMessage = new MeepDownlaodMessageConverter(message);
		switch (convertedMessage.getTypeOfMessage()) {
		case MeepDownlaodMessageConverter.MESSAGE_TYPE_DOWNLOAD:
			DownloadStoreItem downloadItem = convertedMessage.getConvertedDownloadItem();
			if(downloadItem!=null)
			{
				mApp.getStoreDownloadCtrl().addStoreDownloadItem(downloadItem);
			}
			break;
		case MeepDownlaodMessageConverter.MESSAGE_TYPE_OTHERS:
			NotificationMessageItem notficationItem = convertedMessage.getConvertedNotificationItem(mContext);
			if(notficationItem !=null)
			{
				MeepStoreLog.LogMsg(notficationItem.getMessage());
				NotificationManager notificationManager = (NotificationManager) ServiceManager.getService(mContext, ServiceManager.NOTIFICATION_SERVICE);
				notificationManager.notify(mApp.getAccountID(), NotificationGenerator.generateNormalNotification(notficationItem));
			}
			break;

		default:
			break;
		}
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent != null)
		{
			if (MessageManager.MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
				Message message = intent.getParcelableExtra(MessageManager.EXTRA_MESSAGE);
				MeepStoreLog.LogMsg(message.toString());
				
				handleMessage(message);
				
			}
		}
		return START_STICKY;
	}
	
	public void startInAppPurchaseService()
	{
		Intent intent = new Intent(this, InAppPurchaseService.class);
		startService(intent);
	}
	public void startBannerItemsService()
	{
		Intent intent = new Intent(this, BannerItemsService.class);
		startService(intent);
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		startInAppPurchaseService();
		startBannerItemsService();
		
		mContext = this;
		MeepStoreLog.LogMsg("store_service: created");
		MeepStoreLog.logcatMessage("meepStoreService", "service created");
		regBroadcastFilter();
//		mGetUserTokenTimer = new CountDownTimer(30000, 5000) {
//			
//			@Override
//			public void onTick(long millisUntilFinished) {
//				if(mApp.getUserToken() == null){
//					initComponents();
//				}
//			}
//			
//			@Override
//			public void onFinish() {
//				
//				
//			}
//		};
		initComponents();
	}
	
	
	
	@Override
	public void onDestroy() {
		unregBroadcastFilter();
		super.onDestroy();
	}
	
//	private CountDownTimer mGetUserTokenTimer; 
	
	private void initComponents() {
		mApp = (MeepStoreApplication) this.getApplicationContext();
//		String userToken = getOwnerToken();
//		mApp.setUserToken(userToken);
//		if(userToken == null){
//			getUserToken();
//			return;
//		}else{
//			mGetUserTokenTimer.cancel();
//			
			//init db
			DbAdapter dbApt = new DbAdapter(this);
			dbApt.open();
			mApp.setDatabaseAdapter(dbApt);
			SQLiteDatabase db = dbApt.getDatabase();
			
			//init download ctrl
			mdownDownloadCtrl = mApp.getStoreDownloadCtrl();
			if (mdownDownloadCtrl == null) {
				mdownDownloadCtrl = new StoreItemDownloadCtrl(mContext, mApp.getDatabaseAdapter().getDatabase());
				mApp.setStoreDownloadCtrl(mdownDownloadCtrl);
				mdownDownloadCtrl.startDownload();
			}
			
			//init image download control
			ImageDownloadCtrl imageDownloadCtrl = mApp.getImageDownloadCtrl();
			if(imageDownloadCtrl == null){
				imageDownloadCtrl = new ImageDownloadCtrl(mContext, dbApt.getDatabase(), "globalImageThread");
				mApp.setImageDownloadCtrl(imageDownloadCtrl);
			}
			
			//init REST request control
			
			RestRequest restRequest = mApp.getRestRequest();
			if(restRequest == null){
				restRequest = new RestRequest(mContext);
				mApp.setRestRequest(restRequest);
			}
			
//			mApp.setUserToken(userToken);
			getEbookListFromDb();
			
			startVersionReportThread();
			
			//test
			mRestoreImageDownloadCtrl = new RestoreImageDownloadCtrl(mContext, db, "checkImageThread");
			loadPurchasedItem(db);
			
			mApp.setImageLoader(new ImageThreadLoader(16, mApp.getDatabaseAdapter().getDatabase()));
			mApp.setImageShotLoader(new ImageThreadLoader(10, mApp.getDatabaseAdapter().getDatabase()));
			//decodeMsg("");
//		}
	}
//	private String getOwnerToken() {
//		String token = null;
//		Uri uri = Uri.parse("content://com.oregonscientific.meep.providerinfo/user");
//		Cursor cursor = null;
//		try {
//			cursor = getContentResolver().query(uri, null, null, null, null);
//			if (cursor != null) {
//				cursor.moveToFirst();
//				token = cursor.getString(cursor.getColumnIndexOrThrow(com.oregonscientific.meep.database.table.TableUser.S_TOKEN));
//			}
//			MeepStoreLog.logcatMessage("TOKEN", "token: " + token);
//		} catch (Exception e) {
//			// TODO: handle exception
//		} finally {
//			if(cursor!=null)
//			cursor.close();
//		}
//		return token;
//	}

	private void startVersionReportThread() {
		MeepStoreApplication app = (MeepStoreApplication) this.getApplicationContext();
		String userToken = app.getUserToken();
		if (userToken != null) {
			MeepVersionReportCtrl reportVersionCtrl = new MeepVersionReportCtrl(this, userToken);
			reportVersionCtrl.startReportVersion();
		}
	}
	
	private void regBroadcastFilter(){
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.INTENT_MSG_PREFIX + Global.AppType.Store);
		//filter.addAction("android.intent.action.PACKAGE_REMOVED");
		//filter.addAction("android.intent.action.PACKAGE_ADDED");
		registerReceiver(mMsgReceiver, filter);
		MeepStoreLog.logcatMessage("meepStoreService", "reg receiver");
	}
	
	private void unregBroadcastFilter(){
		unregisterReceiver(mMsgReceiver);
		MeepStoreLog.logcatMessage("meepStoreService", "unreg receiver");
	}
	
	private BroadcastReceiver mMsgReceiver = new BroadcastReceiver() {
		Gson gson = new Gson();
		
		@Override
		public void onReceive(Context context, Intent intent) {
			
            if (intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)) {
				return;
	        }

            String msg = intent.getStringExtra(Global.STRING_MESSAGE);
            if (msg != null && !msg.equalsIgnoreCase("")) {
                try {
        			MeepStoreLog.logcatMessage("meepStoreService", msg);
        			MeepAppMessage mam = gson.fromJson(msg, MeepAppMessage.class);
        			
        			//*****download ******
        			if(mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_EBOOK) && mam.getCatagory().equals(Category.SERVER)){
        				String ebookMsgStr = mam.getMessage();
        				MsmStoreToMeepEbook ebookMsg = gson.fromJson(ebookMsgStr, MsmStoreToMeepEbook.class);
        				DownloadStoreItem item = new DownloadStoreItem(ebookMsg.getItemId(), ebookMsg.getName(), DownloadStoreItem.TYPE_EBOOK, ebookMsg.getImage(), ebookMsg.getUrl(),ebookMsg.getChecksum(), ebookMsg.getPackageName());
        				MeepStoreLog.LogMsg("receive ebook downloading msg ->" + ebookMsg.getName());
        				mdownDownloadCtrl.addStoreDownloadItem(item);
        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_APP)) {
        				String msgStr = mam.getMessage();
        				MsmStoreToMeepApp msgItem = gson.fromJson(msgStr, MsmStoreToMeepApp.class);
        				DownloadStoreItem item = new DownloadStoreItem(msgItem.getItemId(), msgItem.getName(), DownloadStoreItem.TYPE_APP, msgItem.getImage(), msgItem.getUrl(), msgItem.getChecksum(), msgItem.getPackageName());
        				MeepStoreLog.LogMsg("receive app downloading msg ->" + msgItem.getName());
        				mdownDownloadCtrl.addStoreDownloadItem(item);
        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_GAME)) {
        				String msgStr = mam.getMessage();
        				MsmStoreToMeepGame msgItem = gson.fromJson(msgStr, MsmStoreToMeepGame.class);
        				DownloadStoreItem item = new DownloadStoreItem(msgItem.getItemId(), msgItem.getName(), DownloadStoreItem.TYPE_GAME, msgItem.getImage(), msgItem.getUrl(), msgItem.getChecksum(), msgItem.getPackageName());
        				MeepStoreLog.LogMsg("receive game downloading msg ->" + msgItem.getName());
        				MeepStoreLog.LogMsg("receive game downloading msg ->" + msgStr);
        				mdownDownloadCtrl.addStoreDownloadItem(item);
        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_MOVIE)) {
        				String msgStr = mam.getMessage();
        				MsmStoreToMeepMovie msgItem = gson.fromJson(msgStr, MsmStoreToMeepMovie.class);
        				DownloadStoreItem item = new DownloadStoreItem(msgItem.getItemId(), msgItem.getName(), DownloadStoreItem.TYPE_MOVIE, msgItem.getImage(), msgItem.getUrl(), "", msgItem.getPackageName());
        				MeepStoreLog.LogMsg("receive movie downloading msg ->" + msgItem.getName());
        				mdownDownloadCtrl.addStoreDownloadItem(item);
        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_MUSIC)) {
        				String msgStr = mam.getMessage();
        				MsmStoreToMeepMusic msgItem = gson.fromJson(msgStr, MsmStoreToMeepMusic.class);
        				DownloadStoreItem item = new DownloadStoreItem(msgItem.getItemId(), msgItem.getName(), DownloadStoreItem.TYPE_MUSIC, msgItem.getImage(), msgItem.getUrl(),"", msgItem.getPackageName());
        				MeepStoreLog.LogMsg("receive music downloading msg ->" + msgItem.getName());
        				mdownDownloadCtrl.addStoreDownloadItem(item);
//        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_GET_USER_TOKEN)) {
//        				String userToken = mam.getMessage();
//        				MeepStoreLog.logcatMessage("meepStoreService", "got user token:" + userToken);
//        				if (userToken != null) {
//        					MeepStoreApplication app = (MeepStoreApplication) mContext.getApplicationContext();
//        					app.setUserToken(userToken);
//        					initComponents();
//        					//userToken = "";
//        				}
        			} else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_DOWNLOAD_EBOOK) && mam.getCatagory().equals(Category.DATABASE_QUERY)) {
        				decodeDbQueryMsg(mam, intent);
        			}else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_UPGRADE_OTA)) {
        				String url = mam.getMessage();
        				DownloadStoreItem item = new DownloadStoreItem(DownloadStoreItem.TYPE_OTA, DownloadStoreItem.TYPE_OTA, DownloadStoreItem.TYPE_OTA, DownloadStoreItem.TYPE_OTA, url,"", "");
        				mdownDownloadCtrl.addStoreDownloadItem(item);
        			}else if (mam.getOpcode().equals(MeepAppMessage.OPCODE_OPEN_MEEP_STORE)) {
        				Intent i = new Intent();
        				i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        				i.setClass(context, MainActivity.class);
        				context.startActivity(i);
        			}
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
		}
	};
	
	
	private void getUserToken(){
		MeepStoreLog.logcatMessage("meepStoreService", "get user token");
		MeepAppMessage mam = new MeepAppMessage(Category.SYSTEM, MeepAppMessage.OPCODE_GET_USER_TOKEN, "", Global.INTENT_MSG_PREFIX + Global.AppType.Store);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(mam);
		Intent i = new Intent();
		i.setAction(Global.INTENT_MSG_PREFIX + AppType.MeepMessage);
		i.putExtra(Global.STRING_MESSAGE, jsonStr);
		sendBroadcast(i);
	}
	
	private void getEbookListFromDb(){
		MeepStoreLog.logcatMessage("meepStoreService", "get ubook list");
		String sql = TableEbook.getSelectAllEbookSql();
		MeepAppMessage mam = new MeepAppMessage(Category.DATABASE_QUERY, MeepAppMessage.OPCODE_DOWNLOAD_EBOOK, sql, Global.INTENT_MSG_PREFIX + Global.AppType.Store);
		Gson gson = new Gson();
		String jsonStr = gson.toJson(mam);
		Intent i = new Intent();
		i.setAction(Global.INTENT_MSG_PREFIX + AppType.MeepMessage);
		i.putExtra(Global.STRING_MESSAGE, jsonStr);
		sendBroadcast(i);
	}
	
	
	private void decodeDbQueryMsg(MeepAppMessage meepAppMsg, Intent intent) {
			Gson gson = new Gson();
			String tableName = meepAppMsg.getMessage();
			MeepStoreLog.logcatMessage("database", "received table:" + tableName);
			MeepStoreLog.logcatMessage("database", "received table msg:" + meepAppMsg.getMessage());
			if (tableName.equals(TableEbook.S_TABLE_NAME)) {
				ArrayList<TableEbook> ebookList = new ArrayList<TableEbook>();
				String[] array = intent.getStringArrayExtra(Global.STRING_LIST);
				for (int i = 0; i < array.length; i++) {
					MeepStoreLog.logcatMessage("database", "array:"+array[i]);
					TableEbook ebook = gson.fromJson(array[i], TableEbook.class);
					ebookList.add(ebook);
				}
				MeepStoreApplication app = (MeepStoreApplication) mContext.getApplicationContext();
				app.setDownloadedEbook(ebookList);
			}
	}
				
	//************handle blurry icon*************
	private void loadPurchasedItem(SQLiteDatabase db) {
		
		MeepStoreLog.logcatMessage("storeimagedownload", "start loading...");
		ArrayList<StoreImageItem> itemlist = getPurchasedItemFromDb(db);
		if (itemlist.size() > 0) {
			MeepStoreLog.logcatMessage("storeimagedownload", "get item from db:" + itemlist.size());
			checkStoreItemImage(itemlist);
		}else{
			MeepStoreLog.logcatMessage("storeimagedownload", "get my app items...");
			MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
			app.getRestRequest().getPurchaseItem(app.getUserToken());
			app.getRestRequest().setGetPurchasedItemListener(new GetPurchasedItemListener() {
				
				@Override
				public void onGetPurchasedItemListener(MyAppItems myAppItems) {
					MeepStoreLog.logcatMessage("storeimagedownload", "got my app items");
					if(myAppItems!= null){
						MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
						SQLiteDatabase db = app.getDatabaseAdapter().getDatabase();
						db.execSQL(TableStorePurchasedItem.getDeleteAllSql());
						
						ArrayList<MeepStoreItem> purchaseItems = myAppItems.getPurchasedItems();
						if (purchaseItems != null && purchaseItems.size() > 0) {
							MeepStoreLog.logcatMessage("storeimagedownload", "got purchase items:" + purchaseItems.size());
							
							for (MeepStoreItem item : purchaseItems) {
								StoreImageItem imgItem = null;
								if (item.getItemType().equals(MeepStoreItem.TYPE_EBOOK)) {
									MeepStoreLog.logcatMessage("storeimagedownload", "ebook name:" +item.getName() + "url: " + item.getUrl());
									imgItem = new StoreImageItem(item.getItemId(), EncodingBase64.encode(item.getName()), item.getItemType(), item.getIconUrl());
								} else if (item.getItemType().equals(MeepStoreItem.TYPE_APP) || item.getItemType().equals(MeepStoreItem.TYPE_GAME)) {
									MeepStoreLog.logcatMessage("storeimagedownload", "package name:" +item.getPackageName() + "url: " + item.getUrl());
									imgItem = new StoreImageItem(item.getItemId(),item.getPackageName(), item.getItemType(), item.getIconUrl());
								}
								
								db.execSQL(TableStorePurchasedItem.getInsertSql(imgItem.getId(),imgItem.getName(), imgItem.getType(), imgItem.getIconUrl()));
							}
						}
						
						ArrayList<MeepStoreItem> preloadedItems = myAppItems.getPreloadedItems();
						if (preloadedItems != null && preloadedItems.size() > 0) {
							MeepStoreLog.logcatMessage("storeimagedownload", "got preloaded items:" + preloadedItems.size());
							
							for (MeepStoreItem item : preloadedItems) {
								StoreImageItem imgItem = new StoreImageItem(item.getItemId(), item.getPackageName(), item.getItemType(), item.getIconUrl());
								db.execSQL(TableStorePurchasedItem.getInsertSql(imgItem.getId(), imgItem.getName(), imgItem.getType(), imgItem.getIconUrl()));
							}
						}
						
						loadPurchasedItem(db);
					}
				}
			});
			
		}
	}
	
	ArrayList<StoreImageItem> mStoreImageList = new ArrayList<StoreImageItem>();
	
	private void checkStoreItemImage(ArrayList<StoreImageItem> list){
		MeepStoreLog.logcatMessage("storeimagedownload", "start check item image...");
		MeepStoreApplication app = (MeepStoreApplication)mContext.getApplicationContext();
		
		mRestoreImageDownloadCtrl.setDownloadListener(new RestoreImageDownloadCtrl.DownloadListener() {
			
			@Override
			public void onDownloadCompleted(boolean downloadAborted, DownloadImageItem item) {
				MeepStoreLog.logcatMessage("storeimagedownload", "image download received 0:" + item.getId());
				if(!downloadAborted){
					MeepStoreLog.logcatMessage("storeimagedownload", "image download received 1:" + item.getId());
					for(StoreImageItem storeitem : mStoreImageList){
						if(storeitem.getName().equals(item.getId())){
							String path = Global.PATH_DATA_HOME + storeitem.getType() + "/icon/" + storeitem.getName() + Global.FILE_TYPE_PNG;
							MeepStoreLog.logcatMessage("storeimagedownload", "check icon: " + path);
							MeepStoreLog.logcatMessage("storeimagedownload", "image: "+ item.getImage());
							MediaManager.saveImageToExternal(item.getImage(), path);
	                        MeepStorageCtrl.changeFilePermission(new File(path));
							MeepStoreLog.logcatMessage("storeimagedownload", "image saved: " + path);
							if(storeitem.getType().equals(MeepStoreItem.TYPE_APP) || storeitem.getType().equals(MeepStoreItem.TYPE_GAME)) {
								String mirrorpath = Global.PATH_DATA_HOME + storeitem.getType() + "/icon_lm/" + storeitem.getName() + Global.FILE_TYPE_PNG;
								String dimpath = Global.PATH_DATA_HOME + storeitem.getType() + "/icon_ld/" + storeitem.getName() + Global.FILE_TYPE_PNG;
								String smallpath = Global.PATH_DATA_HOME + storeitem.getType() + "/icon_ls/" + storeitem.getName() + Global.FILE_TYPE_PNG;
								File file  = new File(mirrorpath);
								file.deleteOnExit();
								file = new File(dimpath);
								file.deleteOnExit();
								file = new File(smallpath);
								file.deleteOnExit();
							}
							
							mStoreImageList.remove(storeitem);
							break;
						}
					}
				}
			}
		});
		
		//ebook
		String ebookDataPath = Global.PATH_DATA_HOME + MeepStoreItem.TYPE_EBOOK + "/data/";
		File dataDir = new File(ebookDataPath);
		String ebookImagePath = Global.PATH_DATA_HOME + MeepStoreItem.TYPE_EBOOK + "/icon/";
		if(dataDir.exists() && dataDir.isDirectory()){
			String[] dataFileList = dataDir.list();
			
			for(String filename : dataFileList){
				String nameWoExt = filename.substring(0, filename.length()-5);
				String pathImage = ebookImagePath + nameWoExt + "/" + Global.FILE_TYPE_PNG;
				File imagefile = new File(pathImage);
				if (!imagefile.exists()) {
					StoreImageItem item = fineStoreImage(list, MeepStoreItem.TYPE_EBOOK, nameWoExt);
					if (item != null) {
						
						mStoreImageList.add(item);
						
						String prefix = "https://meeptablet-storedelivery.storage.googleapis.com/";
						if(mApp.getLoginInfo()!= null){
							prefix = mApp.getLoginInfo().url_prefix;
						}
						DownloadImageItem dlImage = new DownloadImageItem(nameWoExt, prefix + item.getIconUrl());
						MeepStoreLog.logcatMessage("storeimagedownload", "image need to download: " + item.getName() + "   url:" + prefix + item.getIconUrl());
						mRestoreImageDownloadCtrl.addToDownloadQ(dlImage);
					}
				}
			}
		}
		
		//package
		List<ApplicationInfo> appList =  AppInstallationCtrl.getAllIntalledPackageInfos(mContext);
		for (ApplicationInfo appItems : appList) {
			for (StoreImageItem imgItem : list) {
				if (appItems.packageName.equals(imgItem.getName())) {
					if (imgItem.getType().equals(MeepStoreItem.TYPE_APP)) {
						String filePath = Global.PATH_DATA_HOME + MeepStoreItem.TYPE_APP + "/icon/" + imgItem.getName() + Global.FILE_TYPE_PNG;
						File file = new File(filePath);
						if (!file.exists()) {
							MeepStoreLog.logcatMessage("storeimagedownload", "app need to download:" + imgItem.getName() + "   url:" +   imgItem.getIconUrl());
							mStoreImageList.add(imgItem);
							addToRestoreDownload(imgItem.getName(), imgItem.getIconUrl());
						}
					} else if (imgItem.getType().equals(MeepStoreItem.TYPE_GAME)) {
						String filePath = Global.PATH_DATA_HOME + MeepStoreItem.TYPE_GAME + "/icon/" + imgItem.getName() + Global.FILE_TYPE_PNG;
                        File file = new File(filePath);
						if (!file.exists()) {
							MeepStoreLog.logcatMessage("storeimagedownload", "game need to download:"  + imgItem.getName() + "   url:" +  imgItem.getIconUrl());
							mStoreImageList.add(imgItem);
							addToRestoreDownload(imgItem.getName(), imgItem.getIconUrl());
						}
					}
				}
			}
		}
		
		mRestoreImageDownloadCtrl.startDownload();
	}

	
	
	private void addToRestoreDownload(String name, String url){
		String prefix = "https://meeptablet-storedelivery.storage.googleapis.com/";
		if(mApp.getLoginInfo()!= null){
			prefix = mApp.getLoginInfo().url_prefix;
		}
		DownloadImageItem dlImage = new DownloadImageItem(name, prefix + url);
		MeepStoreLog.logcatMessage("storeimagedownload", "image need to download: " + name + "   url:" + url);
		mRestoreImageDownloadCtrl.addToDownloadQ(dlImage);
	}
	

	private StoreImageItem fineStoreImage(ArrayList<StoreImageItem> list, String type, String fileName) {
		for (StoreImageItem item : list) {
			if (item.getName().equals(fileName) && item.getType().equals(type)) {
				return item;
			}
		}
		return null;
	}
	
	private ArrayList<StoreImageItem> getPurchasedItemFromDb(SQLiteDatabase db){
		ArrayList<StoreImageItem> itemList = new ArrayList<StoreImageItem>();
		Cursor c = db.rawQuery(TableStorePurchasedItem.getSelectItemSql(), null);
		int rowNum = c.getCount();
		if(rowNum>0){
			c.moveToFirst();
			for(int i=0; i< rowNum; i++){
				String id = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_ID));
				String name = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_NAME));
				String type = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_TYPE));
				String iconUrl = c.getString(c.getColumnIndex(TableStorePurchasedItem.S_ICON_URL));
				StoreImageItem item = new StoreImageItem(id, name, type, iconUrl);
				itemList.add(item);
				c.moveToNext();
			}
		}
		return itemList;
	}
	//*********end handle blurry icon
	
	
	/**
     * Class used for the client Binder.  Because we know this service always
     * runs in the same process as its clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        MeepStoreService getService() {
            // Return this instance of LocalService so clients can call public methods
            return MeepStoreService.this;
        }
      
    }

	

}
