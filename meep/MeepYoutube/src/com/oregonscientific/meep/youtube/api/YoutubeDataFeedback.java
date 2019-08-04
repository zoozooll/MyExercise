package com.oregonscientific.meep.youtube.api;

import java.util.ArrayList;

import com.google.api.client.util.Key;

public class YoutubeDataFeedback {

	public class VideosData{
		String small_thumbnail;
		String large_thumbnail;
		String title;
		String video_id;

		public void set_video_id(String video_id){
			this.video_id=video_id;
		}

		public void set_title(String title){
			this.title=title;
		}
		
		public void set_large_thumbnail(String large_thumbnail){
			this.large_thumbnail=large_thumbnail;
		}
		
		public void set_small_thumbnail(String small_thumbnail){
			this.small_thumbnail=small_thumbnail;
		}
		
		public String get_video_id(){
			return this.video_id;
		}

		public String get_title(){
			return this.title;
		}
		
		public String get_large_thumbnail(){
			return this.large_thumbnail;
		}
		
		public String get_small_thumbnail(){
			return this.small_thumbnail;
		}		
		
	}
	
    ArrayList<VideosData> videos;

    public ArrayList<VideosData> getVideos() {
        return videos;
    }
    
    public void setVideos(ArrayList<VideosData> videos) {
        this.videos = videos;
    }
	
	/*
	@Key Videos videos;
	@Key String status;
	
	public class Videos {
		@Key String small_thumbnail;
		@Key String large_thumbnail;
		@Key String title;
		@Key String video_id;
	}

	public String getStatus() {
    	return status;
	}
    
	public void setStatus(String status) {
    	this.status = status;
	}

	public Videos getVideos() {
    	return videos;
	}
    
	public void setVideos(Videos videos) {
    	this.videos = videos;
	}
	
	
	private String video_id;
	private String title;
	private String large_thumbnail;
	private String small_thumbnail;

	
	public YoutubeDataFeedback(){
		
	}
	
	public YoutubeDataFeedback(String video_id, String title, String large_thumbnail, String small_thumbnail)
	{
		this.video_id=video_id;
		this.title=title;
		this.large_thumbnail=large_thumbnail;
		this.small_thumbnail=small_thumbnail;
		
	}
	
	

	public void set_video_id(String video_id){
		this.video_id=video_id;
	}

	public void set_title(String title){
		this.title=title;
	}
	
	public void set_large_thumbnail(String large_thumbnail){
		this.large_thumbnail=large_thumbnail;
	}
	
	public void set_small_thumbnail(String small_thumbnail){
		this.small_thumbnail=small_thumbnail;
	}
	
	public String get_video_id(){
		return this.video_id;
	}

	public String get_title(){
		return this.title;
	}
	
	public String get_large_thumbnail(){
		return this.large_thumbnail;
	}
	
	public String get_small_thumbnail(){
		return this.small_thumbnail;
	}



*/	
	
}
