package com.iskyinfor.duoduo.ui.lesson;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.ui.custom.page.DiscardOldestThreadPool;
import com.iskyinfor.duoduo.ui.custom.page.PageEventListener;
import com.iskyinfor.duoduo.ui.custom.page.PictureLoaderThread;

public class PageLessonEventListener<T> implements PageEventListener<T> {
	ProgressDialog progressDialog;

	public PageLessonEventListener(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public PageLessonEventListener() {}

	@Override
	public void endEveryPageListener(Context context, Handler handler,
			boolean flag, ArrayList<T> dataList) {
		// TODO Auto-generated method stub
		Log.i("liu", "dataList===:"+dataList.size());
		if (progressDialog != null && progressDialog.isShowing()) 
		{
			progressDialog.dismiss();
		}
		
		ArrayList<StepLesson> arrayList = (ArrayList<StepLesson>) dataList;
		PictureLoaderThread imageLoaderThread = new PictureLoaderThread(
				context, handler, arrayList);
		DiscardOldestThreadPool.getInstance().execute(imageLoaderThread);

	}

	@Override
	public void startEveryPageListener() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void loadingEveryPageListener()
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void exceptionPageListener() 
	{
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollEvent(Context context, AbsListView view,
			int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollIdleStateEvent(Context context, AbsListView view,
			int scrollState, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollTouchStateEvent(Context context, AbsListView view,
			int scrollState, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onScrollFlingStateEvent(Context context, AbsListView view,
			int scrollState, int firstVisibleItem, int visibleItemCount,
			int totalItemCount) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onItemClickEvent(Context context, AdapterView<?> parent,
			View view, int position, long id) {
		// TODO Auto-generated method stub
	}
}