package com.iskyinfor.duoduo.ui.downloader;

import java.io.File;
import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.broadcast.DownloadBundle;

public class DownloadedUi {
	private Activity activity;
	private LayoutInflater inflater;
	private FrameLayout frameLayout;
	private View downloadRunView;
	private ListView finishListView;
	private ArrayList<File> downloadData = new ArrayList<File>();
	private DownLoadedListViewAdapter downLoadedListViewAdapter;
	private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action=intent.getAction();
			DownloadBundle downloadBundle;
			if(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FINISH.equals(action)){
				downloadBundle=(DownloadBundle) intent.getSerializableExtra(Constants.TAG_DOWNLOADBUNDLE);
				if(downloadBundle!=null){
					//获取下载文件的路径
					String filePath=downloadBundle.getFilePath()+downloadBundle.getName();
					File file=new File(filePath);
					if(file.exists()&&!file.isDirectory()){
						downloadData.add(file);
						downLoadedListViewAdapter.notifyDataSetChanged();
					}
				}
			}

		}
	};

	/**
	 * 注册接受下载完成的广播
	 */
	private void registeReceiver() {
		IntentFilter intentFilter = new IntentFilter();
		intentFilter
				.addAction(Constants.ACTION_DOWNLOAD_BROADCAST_STATE_FINISH);
		activity.registerReceiver(broadcastReceiver, intentFilter);
	}

	/**
	 * 注销广播接受器
	 */
	public void unregisteReceiver() {
		if(broadcastReceiver!=null){
		activity.unregisterReceiver(broadcastReceiver);
		}
	}

	public DownloadedUi(Activity activity, FrameLayout frameLayout) {
		this.activity = activity;
		this.frameLayout = frameLayout;
		inflater = LayoutInflater.from(activity);
		registeReceiver();
	}

	/**
	 * 执行当前下载界面和下载刷新的处理
	 */
	public void execute() {
		frameLayout.removeAllViews();
		loadDowmnloadedUi();
		loadDownloadingData();
	}

	/**
	 * 加载布局
	 */
	private void loadDowmnloadedUi() {
		downloadRunView = inflater.inflate(R.layout.downloading_layout, null);
		frameLayout.addView(downloadRunView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		finishListView = (ListView) downloadRunView
				.findViewById(R.id.download_run_listView);
	}

	/**
	 * 加载下载数据
	 */
	private void loadDownloadingData() {
		downloadData = scanDownloadFile();
		if (downloadData != null && downloadData.size() > 0) {
			downLoadedListViewAdapter = new DownLoadedListViewAdapter(activity,
					downloadData);
			finishListView.setAdapter(downLoadedListViewAdapter);
		}
	}

	/**
	 * 扫描下载过的文件
	 */
	@SuppressWarnings("null")
	private ArrayList<File> scanDownloadFile() {
		ArrayList<File> arrayList = null;
		String path = getDownloadFilePath("bookfile");
		File file = new File(path);
		Log.i("liu", "file.exists() ===:"+file.exists() );
		Log.i("liu", "file.isDirectory() ===:"+file.isDirectory() );
		if (file.exists() && file.isDirectory()) {
			File[] temp = file.listFiles();
			Log.i("liu", "temp length ===:"+temp.length );
				arrayList=new ArrayList<File>();
			for (int i = 0; i < temp.length; i++) {
				Log.i("peng", "====>"+temp[i].getName().endsWith(".dstmp"));
				Log.i("peng", "file name====>"+temp[i].getName());
				if(temp[i].exists()&&!temp[i].getName().endsWith(".dstmp")){
					arrayList.add(temp[i]);
				}
				
			}
		}
		return arrayList;
	}

	static String getDownloadFilePath(String type) {
		String path = Environment.getExternalStorageDirectory()
				+ File.separator + "duoduo_download" + File.separator + type+ File.separator;
		return path != null ? path : "";
	}

}
