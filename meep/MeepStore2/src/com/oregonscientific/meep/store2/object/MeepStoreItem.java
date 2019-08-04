package com.oregonscientific.meep.store2.object;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import android.graphics.Bitmap;

public class MeepStoreItem {
	private String _id = null;
	private String name = null;
	private Bitmap mIcon=null;
	private String icon  = null;
	private String type;
	private String mItemAction;
	private String url = null;
	private String mLocalPath = null;
	private int coins;
	private String[] screenshots = null;
	private String mPromotionStatus = null; 
	private String mRecommends =  null;
	private String category = null;
	private String developer = null;
	private String badge = null;
	private double size;
	private String description = null;
	private String package_name = null;
	private int mProgress = 0;
	private boolean isRecovery;
	
	public static final String TYPE_APP = "app";
	public static final String TYPE_GAME = "game";
	public static final String TYPE_MEDIA = "media";
	public static final String TYPE_EBOOK = "ebook";
	public static final String TYPE_MOVIE = "movie";
	public static final String TYPE_MUSIC = "music";

//	
	public static final String ACTION_NORMAL = "NORMAL"; //coins
	public static final String ACTION_PURCHASED = "PURCHASED"; //install
	public static final String ACTION_PENDING = "PENDING"; //waiting for approval
	public static final String ACTION_BLOCKED = "BLOCKED"; //not display
	public static final String ACTION_FREE = "FREE"; //free
	public static final String ACTION_GET_IT = "GET_IT"; //get it
	public static final String ACTION_DOWNLOADING = "DOWNLOADING"; //downloading
	public static final String ACTION_PENDING_TO_DOWNLOAD = "PENDING_TO_DOWNLOAD"; //pending to download
	public static final String ACTION_COMING_SOON = "COMING_SOON"; //coming soon
	public static final String ACTION_INSTALLED = "INSTALLED"; //UNINSTALL
	
	public static final String ACTION_INSTALLING = "INSTALLING"; //installing
	public static final String ACTION_EBOOK_DOWNLOADED = "ebook_downloaded";
	
	//Recommends
	public static final String RECOMMEND_RECOMMENDS = "recommends";
	public static final String RECOMMEND_FRIENDLY = "friendly";
	public static final String RECOMMEND_LIKES = "likes";
	
	//BADGE
	public static final String BADGE_BESTSELLER = "bestseller";
	public static final String BADGE_HOTITEM = "hotitem";
	public static final String BADGE_SALE = "sale";
	public static final String BADGE_ACCESSORY = "accessory";
	public static final String BADGE_SDCARD = "sdcard";
	
	
	public MeepStoreItem()
	{

	}
	
//	public MeepStoreItem(String itemId, String name, Bitmap icon, String itemType, 
//			String itemAction, String url, String localPath, DownloadState downloadState, 
//			int price, String promotionStatus){
//		setItemId(itemId);
//		setName(name);
//		setIcon(icon);
//		setItemType(itemType);
//		setItemAction(itemAction);
//		setUrl(url);
//		setLocalPath(localPath);
//		setDownloadState(downloadState);
//		setPrice(price);
//		setPromotionStatus(promotionStatus);
//	}
	
	
	public String getItemId() {
		return _id;
	}

	public void setItemId(String itemId) {
		this._id = itemId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Bitmap getIcon() {
		return mIcon;
	}

	public void setIcon(Bitmap icon) {
		this.mIcon = icon;
	}

	public String getItemType() {
		return type;
	}

	public void setItemType(String itemType) {
		this.type = itemType;
	}

	public String getItemAction() {
		return mItemAction;
	}

	public void setItemAction(String itemAction) {
		this.mItemAction = itemAction;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getLocalPath() {
		return mLocalPath;
	}

	public void setLocalPath(String localPath) {
		this.mLocalPath = localPath;
	}
	
	public int getPrice() {
		return coins;
	}

	public void setPrice(int price) {
		this.coins = price;
	}
	
	public String getPromotionStatus() {
		return mPromotionStatus;
	}

	public void setPromotionStatus(String promotionStatus) {
		this.mPromotionStatus = promotionStatus;
	}

	public String[] getScreenShotUrls() {
		return screenshots;
	}

	public void setScreenShotUrls(String[] screenShotUrls) {
		this.screenshots = screenShotUrls;
	}

	public String getRecommends() {
		return mRecommends;
	}

	public void setRecommends(String recommends) {
		this.mRecommends = recommends;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public String getBadge() {
		return badge;
	}

	public void setBadge(String badge) {
		this.badge = badge;
	}

	public String getSize() {
        NumberFormat formatter = new DecimalFormat("#.#");
        return formatter.format(size);
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getIconUrl() {
		return icon;
	}

	public void setIconUrl(String iconUrl) {
		this.icon = iconUrl;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPackageName() {
		return package_name;
	}

	public void setPackageName(String packageName) {
		this.package_name = packageName;
	}

	public int getProgress() {
		return mProgress;
	}

	public void setProgress(int progress) {
		this.mProgress = progress;
	}

	public boolean isRecovery() {
		return isRecovery;
	}

	public void setRecovery(boolean isRecovery) {
		this.isRecovery = isRecovery;
	}
	

	
	
}
