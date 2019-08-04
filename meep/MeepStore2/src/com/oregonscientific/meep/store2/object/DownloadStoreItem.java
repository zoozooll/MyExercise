package com.oregonscientific.meep.store2.object;

public class DownloadStoreItem {
	private String imageUrl;
	private String itemUrl;
	private String id;
	private String name;
	private String type;
	private String checksum;
	private String packageName;
	
	private String localImagePath;
	private String localFilePath;
	
	public static final String TYPE_EBOOK = "ebook";
	public static final String TYPE_APP = "app";
	public static final String TYPE_GAME = "game";
	public static final String TYPE_MUSIC = "music";
	public static final String TYPE_MOVIE = "movie";
	public static final String TYPE_OTA = "OTA";
	
	
	public DownloadStoreItem(String id, String name, String type, String imageUrl, String itemUrl, String checksum, String packageName){
		setId(id);
		setName(name);
		setType(type);
		setImageUrl(imageUrl);
		setItemUrl(itemUrl);
		setChecksum(checksum);
		setPackageName(packageName);
	}
	
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getItemUrl() {
		return itemUrl;
	}
	public void setItemUrl(String itemUrl) {
		this.itemUrl = itemUrl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}

	public String getLocalImagePath() {
		return localImagePath;
	}

	public void setLocalImagePath(String localImagePath) {
		this.localImagePath = localImagePath;
	}

	public String getLocalFilePath() {
		return localFilePath;
	}

	public void setLocalFilePath(String localFilePath) {
		this.localFilePath = localFilePath;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getChecksum() {
		return checksum;
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
	
}
