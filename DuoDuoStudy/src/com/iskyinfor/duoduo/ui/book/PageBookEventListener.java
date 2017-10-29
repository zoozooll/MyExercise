package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskyinfor.duoduo.ui.custom.page.DiscardOldestThreadPool;
import com.iskyinfor.duoduo.ui.custom.page.ImageLoaderThread;
import com.iskyinfor.duoduo.ui.custom.page.PageEventListener;

public class PageBookEventListener<T> implements PageEventListener<T>{
	ProgressDialog progressDialog;
	public PageBookEventListener(ProgressDialog progressDialog){
		this.progressDialog=progressDialog;
	}
	public PageBookEventListener(){
		
	}
	@Override
	public void endEveryPageListener(Context context,Handler handler, boolean flag,ArrayList<T> dataList) {
		// TODO Auto-generated method stub
	if(progressDialog!=null&&progressDialog.isShowing()){
		progressDialog.dismiss();
	}
	ArrayList<BookShelf> arrayList=(ArrayList<BookShelf>)dataList;
	ImageLoaderThread imageLoaderThread=new ImageLoaderThread(context, handler, arrayList);
	DiscardOldestThreadPool.getInstance().execute(imageLoaderThread);

	
	
	}

	@Override
	public void startEveryPageListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void loadingEveryPageListener() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionPageListener() {
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
