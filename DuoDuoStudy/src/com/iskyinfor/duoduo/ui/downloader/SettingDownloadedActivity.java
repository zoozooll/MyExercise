package com.iskyinfor.duoduo.ui.downloader;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

import com.iskyinfor.duoduo.R;

public class SettingDownloadedActivity extends Activity implements OnClickListener {

	private SeekBar seekBar;
	private Button imageButton;
//	private int restarProgress = 0;

	/**
	 * 当前指示器索引
	 */
	private int currentIndex = 0;

	/**
	 * seekbar的总进度
	 */
	private static int totalProgress = 1000;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_setting_activity);
		initView();
	}
	
	private void initView() {
		seekBar = (SeekBar) findViewById(R.id.setting_seekbar);
		seekBar.setMax(totalProgress);

		imageButton = (Button) findViewById(R.id.save_setting_btn);
		imageButton.setOnClickListener(this);

		// restarProgress = setDownloadCount();
		// seekBar.setProgress(restarProgress);

		seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStartTrackingTouch(SeekBar seekBar)
			{
				// TODO Auto-generated method stub
//				Log.i("liu", "start Tracking");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,boolean fromUser) 
			{
				// TODO Auto-generated method stub
				// restarProgress = computeBarProgress(progress);
//				Log.i("liu", "progress===:" + progress);

				currentIndex = progress / (totalProgress / 4);

				if (progress > (totalProgress - 10)) {
					currentIndex = 4;
				}

//				Log.i("liu", "currentIndex===:" + currentIndex);

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar)
			{
//				Log.i("liu", "stop Tracking");
				// seekBar.setProgress(restarProgress);
				seekBar.setProgress((currentIndex) * totalProgress / 4);
			}

		});
	}

	/**
	 * 计算seekbar进度条的算法
	 */
	// private int computeBarProgress(int progress) {
	// int tempProgress = 0;
	// if (progress < 125) {
	// tempProgress = 0;
	// } else if (progress >= 125 && progress < 375) {
	// tempProgress = 250;
	// } else if (progress >= 375 && progress < 625) {
	// tempProgress = 500;
	// } else if (progress >= 625 && progress < 875) {
	// tempProgress = 750;
	// } else if (progress >= 875) {
	// tempProgress = 1000;
	// }
	//
	// return tempProgress;
	//
	// }

	// private int setDownloadCount() {
	// SharedPreferences sharedPreferences = getSharedPreferences(
	// Constants.SHARE_DOWNLOAD_COUNT, 0);
	// int count = sharedPreferences.getInt(Constants.SHARE_DOWNLOAD_NAME, 2);
	//
	// return count * 250;
	// }

	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.duoduo_lesson_back_img:
			finish();
			break;
		default:
			break;
		}
	}
}