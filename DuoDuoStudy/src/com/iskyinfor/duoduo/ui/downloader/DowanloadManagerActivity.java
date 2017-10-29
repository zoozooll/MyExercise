package com.iskyinfor.duoduo.ui.downloader;

import java.util.ArrayList;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.DownloadTask;

public class DowanloadManagerActivity extends Activity implements
		OnClickListener {
	private TextView downloadingtab, downloadedTab;
	private DownloadServiceStub downloadServiceStub;
	private Button myDataBasse, allPause;
	private ImageButton imageMenuButton, imageBackButton;
	private DownloadedUi downloadedUi;
	private FrameLayout frameLayout;
	private int currentTab = 0;
	private DownloadingUi downloadingUi;
	DataBaseDialog dataBaseDialog;
	private PopupWindow popupWindow;
	Button downlioadingTabView;
	Button downloadedTabView ;
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 1001:
				downloadingUi.refreshDownloadView();
				break;
			default:
				break;
			}

		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.download_activity);
		downloadServiceStub = new DownloadServiceStub(
				DowanloadManagerActivity.this);
		initView();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		downloadingUi.stopRefresh();

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		downloadedUi.unregisteReceiver();
	}

	private void initView() {
		myDataBasse = (Button) findViewById(R.id.download_private_database);
		myDataBasse.setOnClickListener(this);
		allPause = (Button) findViewById(R.id.all_pause_download);
		allPause.setOnClickListener(this);
		imageMenuButton = (ImageButton) findViewById(R.id.duoduo_lesson_list_img);
		imageMenuButton.setOnClickListener(this);
		imageBackButton = (ImageButton) findViewById(R.id.duoduo_lesson_back_img);
		imageBackButton.setOnClickListener(this);
		// =====================================
		 downlioadingTabView = (Button) findViewById(R.id.tab_one);
//		downlioadingTabView.setBackgroundResource(R.drawable.download_tab_bg_focuse);
		downlioadingTabView.setOnClickListener(this);
//		downloadingtab = (TextView) downlioadingTabView
//				.findViewById(R.id.download_text_type);
//		downloadingtab.setText("正在下载");
		// ================================================
		 downloadedTabView = (Button) findViewById(R.id.tab_two);
		downloadedTabView.setOnClickListener(this);
//		downloadedTab = (TextView) downloadedTabView
//				.findViewById(R.id.download_text_type);
//		downloadedTab.setText("已下载");
		frameLayout = (FrameLayout) findViewById(R.id.tabcontent);
		downloadedUi = new DownloadedUi(DowanloadManagerActivity.this,
				frameLayout);
		downloadingUi = new DownloadingUi(DowanloadManagerActivity.this,
				frameLayout, handler);
		switch (currentTab) {
		case 0:

			downloadingUi.execute();

			break;
		case 1:
			downloadedUi = new DownloadedUi(DowanloadManagerActivity.this,
					frameLayout);
			downloadedUi.execute();
			break;
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.tab_one:
			// TODO 加载正在下载界面
			downloadingUi.execute();
			downloadingUi.refreshDownloadView();
//			downlioadingTabView.setBackgroundResource(R.drawable.download_tab_bg_focuse);
//			 downloadedTabView .setBackgroundResource(R.drawable.download_tab_bg);
			break;
		case R.id.tab_two:
			// TODO 加载已下载界面
			downloadedUi.execute();
			downloadingUi.stopRefresh();
//			downlioadingTabView.setBackgroundResource(R.drawable.download_tab_bg);
//			 downloadedTabView .setBackgroundResource(R.drawable.download_tab_bg_focuse);
			break;
		case R.id.download_private_database:
//			dataBaseDialog = new DataBaseDialog(DowanloadManagerActivity.this);
//			dataBaseDialog.setContentView();
			break;
		case R.id.all_pause_download:
			//暂停按钮
//			downloadServiceStub
//					.addDownloadTaskByUrl("http://192.168.1.16:8090/DuoWeb/upload/test.rar");
			 pauseAllDownload();
//			startService();
			break;
		case R.id.duoduo_lesson_list_img:
//			//弹出menu对话框
//			if (popupWindow != null) {
//				if (popupWindow.isShowing()) {
//					popupWindow.dismiss();
//				} else {
//					createSettingMenu();
//				}
//
//			} else {
//				createSettingMenu();
//			}

			break;
		case R.id.duoduo_lesson_back_img:
			//返回按钮
			finish();
			break;

		}

	}
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		if (popupWindow != null&&popupWindow.isShowing()) {
				popupWindow.dismiss();

		} 
	}

	/**
	 * 暂停所有下载
	 */
	private void pauseAllDownload() {
		ArrayList<DownloadTask> rundList = (ArrayList<DownloadTask>) downloadServiceStub
				.queryDownloadRunningTaskIfUnfinish();
		if (rundList != null) {
			for (int i = 0; i < rundList.size(); i++) {
				downloadServiceStub
						.pauseDownloadTask(rundList.get(i).resourceId);
			}
		}
	}

	/**
	 * 启动下载服务
	 */
	private void startService() {
		Intent intent = new Intent();
		intent.setAction(Constants.ACTION_START_SERVICE);
		startService(intent);
	}

	/**
	 * 创建下载设置menu
	 */

	private void createSettingMenu() {
		LayoutInflater inflater = LayoutInflater
				.from(DowanloadManagerActivity.this);
		View setingView = inflater.inflate(R.layout.download_setting_layout,
				null);
		setingView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(DowanloadManagerActivity.this,
						SettingDownloadedActivity.class);
				startActivity(intent);

			}
		});
		
		popupWindow = new PopupWindow(setingView, LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		popupWindow.update();
		popupWindow.showAtLocation(imageMenuButton, Gravity.BOTTOM, 0, 60);
	

	}
}
