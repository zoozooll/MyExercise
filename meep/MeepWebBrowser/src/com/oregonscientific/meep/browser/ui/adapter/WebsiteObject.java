package com.oregonscientific.meep.browser.ui.adapter;

import com.oregonscientific.meep.global.MeepEncryption;
import com.oregonscientific.meep.recommendation.Recommendation;

public class WebsiteObject {
	private int id;
	private String url;
	private String name;
	private String thumbnail;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getThumbnail() {
		return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public static WebsiteObject generateFromRecommendation(Recommendation recommendation)
	{
		WebsiteObject object = new WebsiteObject();
		object.setName(recommendation.getUrl());
		object.setUrl(recommendation.getUrl());
		object.setThumbnail(generateThumbnailUrl(recommendation.getThumbnail(),recommendation.getUrl()));
		return object;
	}
	
	public static String generateThumbnailUrl(String prefix,String url)
	{
		String thumbnailUrl = prefix + MeepEncryption.md5(url) + ".png";
		return thumbnailUrl;
	}
}
