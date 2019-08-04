package com.oregonscientific.meep.message.common;

public class MsmStoreToMeepMovie extends MeepServerMessage {
	
	public MsmStoreToMeepMovie(String proc, String opcode) {
		super(proc, opcode);
	}
	
	private String url = null;
	private String image = null;
	private String name = null;
	private String item_id = null;
	private String package_name = null;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getItemId() {
		return item_id;
	}

	public void setItemId(String itemId) {
		this.item_id = itemId;
	}

    public String getPackageName() {
        return package_name;
    }

    public void setPackageName(String packageName) {
        this.package_name = packageName;
    }

	
}
