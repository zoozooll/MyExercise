package com.oregonscientific.meep.youtube.ui.adapter;

import com.oregonscientific.meep.global.MeepEncryption;
import com.oregonscientific.meep.recommendation.Recommendation;

public class YoutubeObject {
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
		return "http://i.ytimg.com/vi/"+url+"/0.jpg";
		//return thumbnail;
	}
	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}
	
	public static YoutubeObject generateFromRecommendation(Recommendation recommendation)
	{
		YoutubeObject object = new YoutubeObject();
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
