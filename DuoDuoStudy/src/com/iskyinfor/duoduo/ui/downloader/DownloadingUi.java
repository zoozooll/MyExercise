package com.iskyinfor.duoduo.ui.downloader;

import java.util.ArrayList;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.Constants;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.DownloadTask;

public class DownloadingUi implements OnItemLongClickListener{
	private Activity activity;
	private LayoutInflater inflater;
	private FrameLayout frameLayout;
	private View downloadRunView;
	private ListView downRunListView;
	private DownLoadingListViewAdapter loadingAdapter;
	private ArrayList<DownloadTask> downloadingData = new ArrayList<DownloadTask>();
	private DownloadServiceStub downloadServiceStub;
	private Handler handler;
	private boolean isload = false;
	private View cacheView;
	private boolean isUpdate = false;
	private int temp = 1;

	public DownloadingUi(Activity activity, FrameLayout frameLayout,
			Handler handler) {
		this.activity = activity;
		this.frameLayout = frameLayout;
		inflater = LayoutInflater.from(activity);
		this.downloadServiceStub = new DownloadServiceStub(activity);
		this.handler = handler;
	}

	/**
	 * 执行当前下载界面和下载刷新的处理
	 */
	public void execute() {

		if (cacheView != null && isload) { // 如果布局以加载过，就加载缓存
			frameLayout.removeAllViews();
			frameLayout.addView(cacheView);
		} else {
			loadDowmnloadingUi();
			loadDownloadingData();
		}
	}

	/**
	 * 加载布局
	 */
	private void loadDowmnloadingUi() {
		downloadRunView = inflater.inflate(R.layout.downloading_layout, null);
		cacheView = downloadRunView; // 将注册过的布局缓存起来
		frameLayout.addView(downloadRunView, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		downRunListView = (ListView) downloadRunView
				.findViewById(R.id.download_run_listView);
		downRunListView.setOnItemLongClickListener(this);
	}

	/**
	 * 加载下载数据
	 */
	public void loadDownloadingData() {
		downloadingData = (ArrayList<DownloadTask>) downloadServiceStub
				.queryUnSuccessTask();

		loadingAdapter = new DownLoadingListViewAdapter(activity, downloadingData);
		downRunListView.setAdapter(loadingAdapter);
		if (downloadingData != null && downloadingData.size() > 0) {
			Log.i("liu", "frameLayout.size()===:" + frameLayout.getChildCount());
			Message msg = new Message();
			msg.what = 1001;
			handler.sendMessage(msg);
			Log.i("liu", "sendMessage");
		}
		isload = true;
	}

	/**
	 * 刷新界面
	 */
	public void refreshDownloadView() {
		updateDownloadData();

		loadingAdapter.notifyDataSetChanged();
		Log.i("liu", "isUpdate===:"+isUpdate);
		if (isUpdate) {
			Message msg = new Message();
			msg.what = 1001;
			handler.sendMessageDelayed(msg, 1000);
		}
	}

	/**
	 * 更新下载数据的方法
	 */
	private void updateDownloadData() {
		ArrayList<DownloadTask> temp = (ArrayList<DownloadTask>) downloadServiceStub
				.queryTaskByUnFinishState();
		Log.i("liu", "temp===:"+temp.size());
		downloadingData.clear();
		if (temp != null&&temp.size()>0) {
			for (int i = 0; i < temp.size(); i++) {
				downloadingData.add(temp.get(i));
			}
			isUpdate = true;
		} else {
			isUpdate = false;
		}

	}
	/**
	 * 停止刷新
	 */
	public void stopRefresh() {
		handler.removeMessages(1001);

	}

	@Override
	public boolean onItemLongClick(AdapterView<?> parent, View view,
			int position, long id) {
		
		Log.i("peng", "position===:"+position);
	DownloadTask downloadTask=downloadingData.get(position);
	if(downloadTask!=null){
		createDiload(downloadTask);
		
	}else{
		Log.i("peng", "onclick downloadtask=====:"+downloadTask);
	}
		return false;
	}
	
	
	private void createDiload(final DownloadTask downloadTask){
		int arrayId=-1;
		switch (downloadTask.taskState) {
		case DownloadTask.State.RUNNING:
			arrayId=R.array.operate_menu_pause;
			Log.i("peng", "1111111111111=====================");
			break;
		case DownloadTask.State.PAUSE:
			Log.i("peng", "2222222222222222=====================");
			arrayId=R.array.operate_menu_continue;
			break;
		default:
			arrayId=R.array.operate_menu_delete;
			break;
		}
		 new AlertDialog.Builder(activity)
        .setTitle(R.string.operate)
        .setItems(arrayId, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
               switch(which){
               case 0:
            	   downloadServiceStub.cancelDownloadTask(downloadTask.resourceId);
            	   break;
               case 1:
            	   if(DownloadTask.State.RUNNING==downloadTask.taskState){
            		   downloadServiceStub.pauseDownloadTask(downloadTask.resourceId);
            	   }else if(DownloadTask.State.PAUSE==downloadTask.taskState){
            		   downloadServiceStub.revertDownloadTask(downloadTask.resourceId);
            		   startService();
            	   }
            	   break;
               }
            }
        })
        .show();
		
		
		
	}
	
	/**
     * 启动下载服务
     */
	private void startService(){
		Intent intent=new Intent();
		intent.setAction(Constants.ACTION_START_SERVICE);
		activity.startService(intent);
	}
	
}
