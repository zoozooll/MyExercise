package com.oregonscientific.meep.sound;

import android.content.Context;
import android.media.MediaPlayer;

import com.osgd.meep.library.R;

public class SoundEffect {

	// private variables
	private static MediaPlayer mediaplayer;
	//private static HashSet<MediaPlayer> mpSet = new HashSet<MediaPlayer>();

	
	///
	//	click icon static function
	//
	public static void ClickIcon(Context context){		
		play(context,R.raw.click);
	}
	
	
	///
	//	play function
	//
	public static void play(Context context,int resid){
		
		//initial
		mediaplayer = MediaPlayer.create(context,resid);
			
		//set Listener
		mediaplayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //mpSet.remove(mp);
                mp.stop();
                mp.release();
            }
        });
		
		//start
		mediaplayer.start();
	}
	
	
//	///
//	//	stop function
//	//
//	public static void stop() {
//		if(mediaplayer!=null){
//			mediaplayer.stop();
//			mediaplayer.release();
//		}
//	}

}
