/**
 * 
 */
package com.oregonscientific.meep.meepmusic.media;

/**
 * @author aaronli
 *
 */
public interface IPlayerController {

	/**
	 *  start ot play
	 */
	public void play();
	
	/**
	 * stop playing
	 */
	public void stop();
	
	/** 
	 * open to play MeepMusic album,
	 * @param path the album path.
	 * 
	 */
	public void openFile(String path);
	
	/** 
	 * control to play position of the current album.
	 * 
	 */
	public void openQueuePositio(int index);
	
	/**
	 * get the position of the current album.
	 */
	public int getQueuePosition();
	
	/**
	 * check if it is playing now.
	 */
	public boolean isPlaying();
	
	/**
	 * pause playing
	 */
	public void pause();
	
	/**
	 * control to play the previous song 
	 */
	public void prev();
	/**
	 * control to play the next song 
	 */
	public void next();
	/**
	 * control to positon of current music
	 */
	public void seek(int pos);
	
	/**
	 * get positon of current music
	 */
	public int getSeek();
	
	public long duration();
	
	/**
	 * get music name of current music
	 */
	public String getName();
	
	/**
	 * get file path of current music
	 */
	public String getPath();
	
	public String[] getSongFileList();
	
	/**
	 * get name of current album
	 */
	public String getCDName();
	
	/**
	 * get file path of current album
	 */
	public String getAlbumPath();
	
	public void setOnMediaStatusChanged(OnMediaStatusChanged listener);
	
	public static interface OnMediaStatusChanged {
		
		public void onPrepared(String albumPath);
		
		public void onInitialized(String songPath, int songIndex);
		
		public void albumChanged(String albumPath);
		
		public void onSeek(int whereTo);
		
		public void onStarted();
		
		public void onPaused();
		
		public void allListEnd();
		
		public void onInitializedFailed();
		
		public void onMediaDied();
	}

	void release();
}
