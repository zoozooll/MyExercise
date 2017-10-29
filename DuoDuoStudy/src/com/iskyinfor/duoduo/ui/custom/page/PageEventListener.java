package com.iskyinfor.duoduo.ui.custom.page;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;

import com.iskinfor.servicedata.pojo.BookShelf;

public interface PageEventListener<T> {
	/**
	 * 每页加载完成后的监听事件
	 */
    public void endEveryPageListener(Context context,Handler handler,boolean flag,ArrayList<T> appList);
    
    /**
	 * 每页开始加载的监听事件
	 */
    public void startEveryPageListener();
    /**
	 * 每页加载过程中的监听事件
	 */
    public void loadingEveryPageListener();
    
	/**
	 * 异常监听处理事件监听事件
	 */
    public void exceptionPageListener();
    
    
    public void onScrollEvent(Context context, AbsListView view, int firstVisibleItem,
            int visibleItemCount, int totalItemCount);

    public void onScrollIdleStateEvent(Context context, AbsListView view, int scrollState,
            int firstVisibleItem, int visibleItemCount, int totalItemCount);


    public void onScrollTouchStateEvent(Context context, AbsListView view, int scrollState,
            int firstVisibleItem, int visibleItemCount, int totalItemCount);

  
    public void onScrollFlingStateEvent(Context context, AbsListView view, int scrollState,
            int firstVisibleItem, int visibleItemCount, int totalItemCount);

    
    public void onItemClickEvent(Context context, AdapterView<?> parent, View view, int position,
            long id);
    
    
    
    
}
