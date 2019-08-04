package com.oregonscientific.meep.demo;

import java.io.File;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.VideoView;

public class MeepDemoActivity extends Activity {
	protected static final String TAG = "MeepDemoActivity";
	private boolean shouldInitVideo = true;
	//String videoPath = "/storage/external_storage/sdcard1/demo.mp4";
	private String videoPath = "/sdcard/demo.mp4";
	private VideoView videoView;

	public void onCreate(Bundle paramBundle) {
		super.onCreate(paramBundle);
		setContentView(R.layout.main);
		this.videoView = ((VideoView) findViewById(R.id.videoView1));
		//Judging whether a file in sdcard, does not exist to check sdcard1.
		if (!new File(videoPath).exists()) {
			videoPath = "/storage/external_storage/sdcard1/demo.mp4";
			if (!new File(videoPath).exists()) {
				popupDialog();
				return;
			}
		}
		//Get file thumbnails, Judging whether the file is a video file.
		if(ThumbnailUtils.createVideoThumbnail(videoPath,Thumbnails.MINI_KIND) != null){
			playVideo();
		}else{
			popupDialog();
		}						

	}

	protected void onDestroy() {
		super.onDestroy();
		//Process.killProcess(Process.myPid());
		Log.e("MeepDemoActivity", "Enter onDestroy");
	}

	public boolean onKeyDown(int paramInt, KeyEvent paramKeyEvent) {
		if ((paramInt == 4) || (paramInt == 3))
			finish();
		return super.onKeyDown(paramInt, paramKeyEvent);
	}

	protected void onResume() {
		super.onResume();
	}
	
    public void popupDialog(){
    	int messageId = android.R.string.VideoView_error_text_unknown;
		new AlertDialog.Builder(MeepDemoActivity.this)
				.setTitle(android.R.string.VideoView_error_title)
				.setMessage(messageId)
				.setPositiveButton(
						android.R.string.VideoView_error_button,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								finish();
							}
						}).setCancelable(false).show();
    }
	public void playVideo() {
		if (this.shouldInitVideo) {
			this.videoView
					.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(
								MediaPlayer paramAnonymousMediaPlayer) {
							Log.e("MeepDemoActivity", "onCompletion "+videoPath);
							videoView.setVideoPath(videoPath);
							videoView.requestFocus();
							videoView.start();
						}
					});
			this.videoView
					.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(
								MediaPlayer paramAnonymousMediaPlayer) {
							/*Log.e("MeepDemoActivity", "OnPrepared");
							paramAnonymousMediaPlayer.setLooping(true);
							paramAnonymousMediaPlayer.setScreenOnWhilePlaying(true);
							paramAnonymousMediaPlayer.start();*/
						}
					});
			this.videoView.setMediaController(null);
		}
		Log.e("MeepDemoActivity", "Enter PlayVideo "+videoPath);
		this.videoView.setVideoPath(this.videoPath);
		this.videoView.requestFocus();
		this.videoView.start();
	}
}