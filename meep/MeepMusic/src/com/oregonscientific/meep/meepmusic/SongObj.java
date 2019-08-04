package com.oregonscientific.meep.meepmusic;

import android.graphics.Bitmap;

public class SongObj {

	private String mTitle;
	private String mAuthor;
	private String mAlbum;
	private String mPath;
	private Bitmap mConverImage = null;
	
	public SongObj()
	{
		setTitle("");
		setAlbum("");
		setAuthor("");
	}
	
	public SongObj(String title, String album, String authur)
	{
		setTitle(title);
		setAuthor(authur);
		setAlbum(album);
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		this.mTitle = title;
	}

	public String getAuthor() {
		return mAuthor;
	}

	public void setAuthor(String authur) {
		this.mAuthor = authur;
	}

	public String getAlbum() {
		return mAlbum;
	}

	public void setAlbum(String album) {
		this.mAlbum = album;
	}

	public String getPath() {
		return mPath;
	}

	public void setPath(String path) {
		this.mPath = path;
	}

	public Bitmap getConverImage() {
		return mConverImage;
	}

	public void setConverImage(Bitmap converImage) {
		this.mConverImage = converImage;
	}
	
	
}
