/**
 * 
 */
package com.oregonscientific.meep.meepmusic.media;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;

import com.oregonscientific.meep.meepmusic.DataSourceManager;
import com.oregonscientific.meep.message.common.OsListViewItem;

/**
 * @author aaronli
 *
 */
public class MusicPlayerController implements IPlayerController {
	
	
	private static final String LOGTAG = "MusicPlayerController";
	private static final String PREFERENCE_NAME = "meepmusic";
	private static final String MSG_PLAY_PATH = "play_path";
	private static final String MSG_PLAY_POSITION = "play_position";
	private static final String MSG_START_PLAYING = "startPlaying";
	public static final String MSG_ALBUMCHANGED = "albumChanged";
	public static final String MSG_INITIALIZA_FAILED = "initializaFailed";
	public static final String MSG_ALLLIST_END = "allListEnd";
	public static final String MSG_CHANGED = "changed";
	
	// these const vars are loading state'
	private final static int LOAD_RESULT_SUCCESS = 1;
	private final static int LOAD_RESULT_FAIL = 2;
	private static final int LOAD_RESULT_FROMSAVED_FAIL = 3;
	
	// these const vars are initaliza state:
	private final static int INITIALIZA_PREVIOUS  = 1;
	private final static int INITIALIZA_LOADING  = 2;
	private final static int INITIALIZA_FAILED  = 3;
	private final static int INITIALIZA_SUCCESS  = 4;
	
	private static final int TRACK_ENDED = 1;
    private static final int RELEASE_WAKELOCK = 2;
    private static final int SERVER_DIED = 3;
    private static final int FOCUSCHANGE = 4;
    private static final int FADEDOWN = 5;
    private static final int FADEUP = 6;
    private static final int START_PLAY = 7;
    private static final int PREPARE_PLAY = 8;
	private SharedPreferences mPreferences;
	
	private Context mContext;
	private String currentAlbumPath = "";
	private int mPlayPos = -1;
    private int mPlayListLen;
    private int initializaState;
    private int mSeek;
    
    private MultiPlayer mPlayer;
    private String[] mFiles;
    private int mServiceStartId = -1;
    private IPlayerController.OnMediaStatusChanged mediaChangedListener;
    
    private Handler loadAlbumHandler = new Handler(){

		/* (non-Javadoc)
		 * @see android.os.Handler#handleMessage(android.os.Message)
		 */
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case LOAD_RESULT_SUCCESS:
				initializaState = INITIALIZA_SUCCESS;
				/*Bundle b = new Bundle();
				b.putString("albumPath", currentAlbumPath);
				sendPlayerServiceBroadcast(MSG_ALBUMCHANGED, b);*/
				if (mediaChangedListener != null) {
					mediaChangedListener.albumChanged(currentAlbumPath);
				}
				new Thread(mLoadMusicForAlbumRunner).start();
				break;
			case LOAD_RESULT_FAIL:
				initializaState = INITIALIZA_FAILED;
				//sendPlayerServiceBroadcast(MSG_INITIALIZA_FAILED);
				if (mediaChangedListener != null) {
					mediaChangedListener.onInitializedFailed();
				}
				break;
			case LOAD_RESULT_FROMSAVED_FAIL:
				new Thread(mLoadAlbumFromDataSourceRunner).start();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
    	
    };
    
    private Handler mMediaplayerHandler = new Handler() {
        float mCurrentVolume = 1.0f;
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case FADEDOWN:
                    mCurrentVolume -= .05f;
                    if (mCurrentVolume > .2f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEDOWN, 10);
                    } else {
                        mCurrentVolume = .2f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case FADEUP:
                    mCurrentVolume += .01f;
                    if (mCurrentVolume < 1.0f) {
                        mMediaplayerHandler.sendEmptyMessageDelayed(FADEUP, 10);
                    } else {
                        mCurrentVolume = 1.0f;
                    }
                    mPlayer.setVolume(mCurrentVolume);
                    break;
                case SERVER_DIED:
                	//sendPlayerServiceBroadcast("died");
                	if (mediaChangedListener != null) {
                		mediaChangedListener.onMediaDied();
                	}
                    break;
                case TRACK_ENDED:
                    next(false);
                    break;

                case FOCUSCHANGE:
                    break;
                case PREPARE_PLAY:
                	final String path = msg.getData().getString(MSG_PLAY_PATH);
                	final int position = msg.getData().getInt(MSG_PLAY_POSITION, 0);
                	final boolean startPlaying = msg.getData().getBoolean(MSG_START_PLAYING, false);
                	/*Bundle b = new Bundle();
                	b.putString("albumPath", currentAlbumPath);*/
        
            		if (!startPlaying) {
            			if (mediaChangedListener != null) {
                    		mediaChangedListener.onPrepared(currentAlbumPath);
                    	}
                	}
			
                	
                	mPlayer.setDataSource(path);
                	mPlayer.seek(position);
                	if (startPlaying) {
                		sendEmptyMessage(START_PLAY);
                	}
                	break;
                case START_PLAY:
                	startToPlay();
                	break;

                default:
                    break;
            }
        }
    };
    
    private Handler mDelayedStopHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // Check again to make sure nothing is playing right now
            if (isPlaying() ||  mMediaplayerHandler.hasMessages(TRACK_ENDED)) {
                return;
            }
            // save the queue again, because it might have changed
            // since the user exited the music app (because of
            // party-shuffle or because the play-position changed)
        }
    };
    
    
    private Runnable mReloadAlbumPathFromSavedRunner = new Runnable() {
		
		@Override
		public void run() {
			reloadAlbumPathFromSaved();
		}
	};
	
	private Runnable mLoadAlbumFromDataSourceRunner = new Runnable() {
		private boolean isRunning;
		
		@Override
		public void run() {
			if (!isRunning) {
				isRunning = true;
				loadAlbumFromDataSource();
				isRunning = false;
			}
		}

	};
	
	private Runnable mLoadMusicForAlbumRunner  = new Runnable() {
		
		@Override
		public void run() {
			//Log.d(LOGTAG, "mLoadMusicForAlbum currentAlbumPath "+currentAlbumPath);
			mFiles = DataSourceManager.getSongFileList(currentAlbumPath);
			if (mFiles == null || mFiles.length == 0) {
				return;
			}
			if (mPlayPos < 0 || mPlayPos > mFiles.length -1) {
				mPlayPos = 0;
			}
			/*Message msg = mMediaplayerHandler.obtainMessage(PREPARE_PLAY);
			if (msg == null) {
				msg = new Message();
				msg.what = PREPARE_PLAY;
				
			}
			Bundle data = new Bundle();
			data.putString(MSG_PLAY_PATH, mFiles[mPlayPos]);
			data.putInt(MSG_PLAY_POSITION, mSeek);
			msg.setData(data);
			mMediaplayerHandler.sendMessage(msg);*/
			
			sendHandlerInitialized(mFiles[mPlayPos], false);
		}
	};

	
	/**
	 * 
	 */
	public MusicPlayerController(Context context) {
		mContext = context;
		init();
	}


	private void init() {
		initializaState = INITIALIZA_PREVIOUS;
		mPreferences = mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
		//reloadQueue();
		mPlayer = new MultiPlayer();
        mPlayer.setHandler(mMediaplayerHandler);
        // If the service was idle, but got killed before it stopped itself, the
        // system will relaunch it. Make sure it gets stopped again in that case.
        Message msg = mDelayedStopHandler.obtainMessage();
        mDelayedStopHandler.sendMessageDelayed(msg, 60000);
	}

	@Override
	public void release() {
		mDelayedStopHandler.removeCallbacksAndMessages(null);
		mPlayer.release();
	}

	public void open(String path) {
		//if (!currentAlbumPath.equals(path)) {
		initializaState = INITIALIZA_PREVIOUS;
		//mPlayer.stop();
		currentAlbumPath = path;
		mPlayPos = 0;
		loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_SUCCESS);
		//}
	}

	public int getSeek() {
		return mPlayer.position();
	}

	public void openQueuePositio(int index) {
		//mPlayer.stop();
		Log.d(LOGTAG, "openQueuePositio");
		mPlayPos = index;
		if (initializaState == INITIALIZA_SUCCESS) {
			sendHandlerInitialized(mFiles[index], true);
		}
	}

	public String getAlbumPath() {
		return currentAlbumPath;
	}

	public long duration() {
		return mPlayer.duration();
	}

	public void seek(int pos) {
		mPlayer.seek(pos);
		
	}

	public String getCDName() {
		return DataSourceManager.getFileNameWithoutExten(currentAlbumPath);
	}

	public String getName() {
		return DataSourceManager.getFileNameWithoutExten(getPath());
	}

	public String getPath() {
		return mFiles[mPlayPos];
	}

	public void next(boolean b) {
		if (initializaState == INITIALIZA_SUCCESS) {
			Log.d(LOGTAG, "next");
			if (mFiles == null || mFiles.length == 0) {
				return;
			}
			if (mPlayPos < (mFiles.length - 1)) {
				mPlayPos++;
				//mPlayer.setDataSource(mFiles[mPlayPos]);
				//mMediaplayerHandler.sendEmptyMessage(START_PLAY);
				sendHandlerInitialized(mFiles[mPlayPos], true);
			} else if (b) {
				mPlayPos = mFiles.length - 1;
				//mPlayer.setDataSource(mFiles[mPlayPos]);
				//mMediaplayerHandler.sendEmptyMessage(START_PLAY);
				sendHandlerInitialized(mFiles[mPlayPos], true);
			} else {
				//sendPlayerServiceBroadcast(MSG_ALLLIST_END);
				//stop();
				//2013-10-23 - Zoya - solve#6640
				if (mediaChangedListener != null) {
					mediaChangedListener.allListEnd();
				}
				pause();
				seek(0);
			}
		}
		
	}

	public void prev() {
		if (initializaState == INITIALIZA_SUCCESS) {
			mPlayPos = mPlayPos > 0 ? mPlayPos-1 : 0;
			//mPlayer.setDataSource(mFiles[mPlayPos]);
			//mMediaplayerHandler.sendEmptyMessage(START_PLAY);
			sendHandlerInitialized(mFiles[mPlayPos], true);
		}
		
		
	}

	public void play() {
		if (mPlayer.isInitialized() && !mPlayer.isPlaying()) {
			mPlayer.start();
		}
	}

	public void pause() {
		if (mPlayer.isPlaying()) {
			mPlayer.pause();
		}
		loadAlbumHandler.removeCallbacksAndMessages(null);
		mMediaplayerHandler.removeCallbacksAndMessages(null);
		mDelayedStopHandler.removeCallbacksAndMessages(null);
	}

	public void stop() {
		mPlayer.stop();
	}

	public boolean isPlaying() {
		return mPlayer.isPlaying();
	}

	public int getQueuePosition() {
		return mPlayPos;
	}
	
	public String[] getSongFileList() {
		return mFiles;
	}
	
	
	private synchronized void reloadQueue() {
		if (TextUtils.isEmpty(currentAlbumPath)) {
			initializaState = INITIALIZA_LOADING;
			new Thread(mReloadAlbumPathFromSavedRunner).start();
			reloadAlbumPathFromSaved();
		} else {
			loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_SUCCESS);
		}
	}
	
	private synchronized void saveQueue() {
		mPreferences.edit().putString("saved_album", currentAlbumPath).commit();
	}
	
	private void reloadAlbumPathFromSaved() {
		synchronized (currentAlbumPath) {
			currentAlbumPath = mPreferences.getString("saved_album", "");
			if (TextUtils.isEmpty(currentAlbumPath)) {
				loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_FROMSAVED_FAIL);
			} else {
				loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_SUCCESS);
			}
		}
	}
	

	private void loadAlbumFromDataSource() {
		synchronized (currentAlbumPath){
			List<OsListViewItem> list = DataSourceManager.getAllItems(false);
			if (list != null && !list.isEmpty()) {
				currentAlbumPath = list.get(0).getPath();
			}
			if (TextUtils.isEmpty(currentAlbumPath)) {
				loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_FAIL);
			} else {
				loadAlbumHandler.sendEmptyMessage(LOAD_RESULT_SUCCESS);
			}
		}
	}
	
	
	private void startToPlay() {
		if (mPlayer.isInitialized() && !mPlayer.isPlaying()) {
    		mPlayer.start();
    	}
	}
	
	/*private void sendPlayerServiceBroadcast(String changed, Bundle extras) {
		Intent intent = new Intent(SERVICE_BROADCAST);
		Bundle data = new Bundle();
		data.putString(MSG_CHANGED, changed);
		if (extras != null) {
			data.putAll(extras);
			
		}
		intent.putExtras(data);
		sendBroadcast(intent);
	}*/
	
	private void sendHandlerInitialized(String songPath, boolean startPlaying) {
		Message msg = mMediaplayerHandler.obtainMessage(PREPARE_PLAY);
		if (msg == null) {
			msg = new Message();
			msg.what = PREPARE_PLAY;
			
		}
		Bundle data = new Bundle();
		data.putString(MSG_PLAY_PATH, songPath);
		data.putInt(MSG_PLAY_POSITION, mSeek);
		data.putBoolean(MSG_START_PLAYING, startPlaying);
		msg.setData(data);
		mMediaplayerHandler.removeMessages(PREPARE_PLAY);
		mMediaplayerHandler.sendMessageDelayed(msg, 500);
	}
	
    /**
     * Provides a unified interface for dealing with midi files and
     * other media files.
     */
    private class MultiPlayer {
        private MediaPlayer mMediaPlayer = new MediaPlayer();
        private Handler mHandler;
        private boolean mIsInitialized = false;

        public MultiPlayer() {
        }

        public synchronized void setDataSource(String path) {
        	mIsInitialized = false;
            try {
            	Log.d(LOGTAG, "mMediaPlayer reset");
            	try {
            		mMediaPlayer.reset();
				} catch (Exception e) {
					e.printStackTrace();
				}
                mMediaPlayer.setOnPreparedListener(null);
                if (path.startsWith("content://")) {
                	Log.d(LOGTAG, "mMediaPlayer setDataSource " + path);
                    mMediaPlayer.setDataSource(mContext, Uri.parse(path));
                } else {
                	Log.d(LOGTAG, "mMediaPlayer setDataSource " + path);
                    mMediaPlayer.setDataSource(path);
                }
                Log.d(LOGTAG, "mMediaPlayer setAudioStreamType ");
                mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                Log.d(LOGTAG, "mMediaPlayer prepare ");
                mMediaPlayer.prepare();
            } catch (IOException ex) {
            	ex.printStackTrace();
                mIsInitialized = false;
                return;
            } catch (IllegalArgumentException ex) {
            	ex.printStackTrace();
                mIsInitialized = false;
                return;
            } catch (Exception e) {
            	mIsInitialized = false;
                return;
			}
            mMediaPlayer.setOnCompletionListener(listener);
            mMediaPlayer.setOnErrorListener(errorListener);
            /*Bundle b = new Bundle();
            b.putString("songPath", path);
            b.putInt("songIndex", mPlayPos);
            sendPlayerServiceBroadcast("initialized", b);*/
            mIsInitialized = true;
            if (MusicPlayerController.this.mediaChangedListener != null) {
            	mediaChangedListener.onInitialized(path, mPlayPos);
            }
        }
        
        public boolean isInitialized() {
            return mIsInitialized;
        }

        public void start() {
//        	sendPlayerServiceBroadcast("started");
//        	Log.d(LOGTAG, "mMediaPlayer start ");
        	if (mediaChangedListener != null) {
        		mediaChangedListener.onStarted();
        	}
            mMediaPlayer.start();
        }

        public void stop() {
        	mIsInitialized = false;
        	//Log.d(LOGTAG, "mMediaPlayer pause ");
        	// 2013-10-24 - Zoya - Fixed #6649
        	try {
        		if(mMediaPlayer.isPlaying()){
            		mMediaPlayer.pause();
            	}
        		mMediaPlayer.reset();
			} catch (Exception e) {
				e.printStackTrace();
			}        	
        	Log.d(LOGTAG, "mMediaPlayer reset ");
            
        }

        /**
         * You CANNOT use this player anymore after calling release()
         */
        public void release() {
            stop();
            Log.d(LOGTAG, "mMediaPlayer release ");
            mMediaPlayer.release();
            
        }
        
        public void pause() {
//        	sendPlayerServiceBroadcast("paused");
//        	Log.d(LOGTAG, "mMediaPlayer pause ");
            mMediaPlayer.pause();
        	if (mediaChangedListener != null) {
        		mediaChangedListener.onPaused();
        	}
        }
        
        public void setHandler(Handler handler) {
            mHandler = handler;
        }

        MediaPlayer.OnCompletionListener listener = new MediaPlayer.OnCompletionListener() {
            public void onCompletion(MediaPlayer mp) {
                // Acquire a temporary wakelock, since when we return from
                // this callback the MediaPlayer will release its wakelock
                // and allow the device to go to sleep.
                // This temporary wakelock is released when the RELEASE_WAKELOCK
                // message is processed, but just in case, put a timeout on it.
                mHandler.sendEmptyMessage(TRACK_ENDED);
            }
        };

        MediaPlayer.OnErrorListener errorListener = new MediaPlayer.OnErrorListener() {
            public boolean onError(MediaPlayer mp, int what, int extra) {
                switch (what) {
                case MediaPlayer.MEDIA_ERROR_SERVER_DIED:
                    mIsInitialized = false;
                    Log.d(LOGTAG, "mMediaPlayer release ");
                    mMediaPlayer.release();
                    // Creating a new MediaPlayer and settings its wakemode does not
                    // require the media service, so it's OK to do this now, while the
                    // service is still being restarted
                    Log.d(LOGTAG, "mMediaPlayer initialized ");
                    mMediaPlayer = new MediaPlayer(); 
                    mMediaPlayer.setWakeMode(mContext, PowerManager.PARTIAL_WAKE_LOCK);
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(SERVER_DIED), 2000);
                    return true;
                default:
                    Log.e("MultiPlayer", "Error: "+ getName() + "," + what + "," + extra);
                    break;
                }
                return false;
           }
        };

        public long duration() {
        	if (mIsInitialized) {
        		Log.d(LOGTAG, "mMediaPlayer getDuration " + mMediaPlayer.getDuration());
        		return mMediaPlayer.getDuration();
        	}
        	return 0;
        }

        public int position() {
        	if (mIsInitialized) {
        		Log.d(LOGTAG, "mMediaPlayer getCurrentPosition "+mMediaPlayer.getCurrentPosition());
        		return mMediaPlayer.getCurrentPosition();
        	}
        	return 0;
        }

        public void seek(int whereto) {
//        	Log.d(LOGTAG, "seek "+whereto);
        	/*Bundle b = new Bundle();
        	b.putInt("seekTo", whereto);
        	sendPlayerServiceBroadcast("seek", b);*/
        	
        	try {
        		mMediaPlayer.seekTo(whereto);
			} catch (Exception e) {
				// TODO: handle exception
			}
        	
        	if (mediaChangedListener != null) {
        		mediaChangedListener.onSeek(whereto);
        	}
        }

        public void setVolume(float vol) {
            mMediaPlayer.setVolume(vol, vol);
        }
        
        public boolean isPlaying() {
        	if (mIsInitialized){
        		return mMediaPlayer.isPlaying();
        	}
        	return false;
        }

        public void setAudioSessionId(int sessionId) {
            mMediaPlayer.setAudioSessionId(sessionId);
        }

        public int getAudioSessionId() {
            return mMediaPlayer.getAudioSessionId();
        }
        
    }
	


	/* (non-Javadoc)
	 * @see com.oregonscientific.meep.meepmusic.media.IPlayerController#openFile(java.lang.String)
	 */
	@Override
	public void openFile(String path) {
		open(path);
	}


	@Override
	public void next() {
		next(true);
		
	}
	
	
	@Override
	public void setOnMediaStatusChanged(IPlayerController.OnMediaStatusChanged listener) {
		this.mediaChangedListener = listener;
	}


}
