package com.mogoo.components.download;
/**
 * DownloadProgressManager 类主要用于监听正在下载任务的进度
 * 统一更新进度条视图.
 */


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import cn.imogoo.providers.downloads.Downloads;

public class DownloadProgressManager
{
    /**
     * 下载成功后发出的广播
     */
    public final static String ACTION_DOWNLOAD_SUCCESS = "android.intent.action.DOWNLOAD_SUCCESS";
    

    private String tag = "DownloadProgressManager";
    private Context mContext;
    private Map<Long, DownloadViewPair> viewMap = new HashMap<Long, DownloadViewPair>();
    
    private DownloadChangeObserver downloadChangeObserver = null;
    
    private boolean isObserver = false;
    
    private static DownloadProgressManager instance = null;
    
    private Handler handler = null;
    
    private DownloadProgressManager(Context context){
        this.mContext = context;
        handler = new Handler(context.getMainLooper()){
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == 100001){
                    downloadError(msg.arg1);
                }else if (msg.what == 100000){
                    finishView(msg.arg1);
                }else if (msg.what == 100002){
                    updateView(msg.arg1, msg.arg2);
                }
            }
            
        };
    }
    
    public static DownloadProgressManager getInstance(Context context){
        if (instance == null){
            instance = new DownloadProgressManager(context);
        }
        
        return instance;
    }
    
    /**
     * 注册下载进度条
     * @param downloadId  下载ID
     * @param view        进度条视图类
     */
    public void registerObserver(long downloadId, MogooDownloadView view ){
        android.util.Log.d(tag, "download id: " + downloadId);
        DownloadViewPair viewPair = viewMap.get(downloadId);
        if (viewPair == null){
            viewPair = new DownloadViewPair();
            viewMap.put(downloadId, viewPair);
        }
        
        viewPair.addMogooDownloadView(view);
        
        if (!isObserver){
            downloadChangeObserver = new DownloadChangeObserver();
            mContext.getContentResolver().registerContentObserver(
                    Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI, true,downloadChangeObserver);
            isObserver = true;
        }
        
        downloadChangeObserver.onChange(true);
    }
    
    
    private Map<Long,DownloadProgressListener> listeners = new HashMap<Long,DownloadProgressListener>();
    
    public synchronized void registerProgressListener(DownloadProgressListener listener){
        listeners.put(listener.getDownloadId(), listener);
        if (!isObserver){
            downloadChangeObserver = new DownloadChangeObserver();
            mContext.getContentResolver().registerContentObserver(
                    Downloads.Impl.ALL_DOWNLOADS_CONTENT_URI, true,downloadChangeObserver);
            isObserver = true;
        }
        
        downloadChangeObserver.onChange(true);
    }
    
    /**
     * 取消下载进度条的更新
     * 
     *  @param downloadId   下载ID
     */
    public synchronized void cancelDownload(long... downloadId){
        for (long did : downloadId){
            viewMap.remove(did);
            listeners.remove(downloadId);
        }
    }
    
    /**
     * 更新 downloadId 对应的下载进度
     * 
     * @param downloadId   下载ID
     * @param progress     进度值
     */
    private synchronized void updateView(long downloadId, int progress){
        DownloadProgressListener listener = listeners.get(downloadId);
        if (listener != null){
            listener.onDownloadProgressChanged(progress);
        }
        DownloadViewPair viewPair = viewMap.get(downloadId);
        if (viewPair != null){
            viewPair.refreshProgress(progress);
        }
    }
    
    /**
     * 下载完成后进度条视图停止更新进度
     * 移除进度条视图类引用.
     * 
     * @param downloadId   下载ID
     */
    private synchronized void finishView(long downloadId){
        DownloadViewPair viewPair = viewMap.remove(downloadId);
        DownloadProgressListener listener = listeners.remove(downloadId);
        if (listener != null){
            listener.onDownloadProgressChanged(100);
        }
        if (viewPair != null){
            viewPair.refreshProgress(100);
        }
    }
    
    /**
     * 下载失败
     * @param downloadId
     */
    private synchronized void downloadError(long downloadId){
        DownloadProgressListener listener = listeners.remove(downloadId);
        if (listener != null){
            listener.onDownloadProgressChanged(-1);
        }
        
        viewMap.remove(downloadId);
    }
    
    /**
     * 一个下载任务对应多个下载进度条视图类.
     * 所以建立一对多的关系类.
     * 
     * @author glg
     *
     */
    private class DownloadViewPair{
        private List<MogooDownloadView> viewList = new ArrayList<MogooDownloadView>();
        
        public void addMogooDownloadView(MogooDownloadView view){
            viewList.add(view);
        }
        
        public void refreshProgress(int progress){
            for (MogooDownloadView view : viewList){
                view.refreshProgress(progress);
            }
        }
        
    }
    
    
    /**
     * 监听下载进度改变事件.
     * 每次读取所有正在下载的任务， 检查是否下载完成， 如果完成则发出下载完成  ACTION_DOWNLOAD_SUCCESS 事件.
     * 更新正在下载任务的进度.
     * 
     */
    private class DownloadChangeObserver extends ContentObserver {

        public DownloadChangeObserver() {
            super(new Handler());
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onChange(boolean selfChage) {

            // 取消下载不再做监听
            if (viewMap.isEmpty() && listeners.isEmpty()) {
               	this.releaseContentObserver();
                isObserver = false;
                android.util.Log.d(tag, "cancel download observer ");
                return;
            }
            
            updateFromProvider();
        }
    }

    private void updateFromProvider(){
    	synchronized (this) {
    		mPendingUpdate = true;
    		if (mUpdatethread == null) {
    			mUpdatethread = new UpdateThread();
    			mUpdatethread.start();
    		}
    	}
    }
    
    private boolean mPendingUpdate;
    UpdateThread mUpdatethread;
    private class UpdateThread extends Thread {
    	@Override
    	public void run() {
    		Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
    		
    		for(;;){
    			StringBuilder wherequery = null;
    			String[] array = null;
    			//////////////////////////////////////////////////
    			synchronized (DownloadProgressManager.this) {
    				if(!mPendingUpdate) {
    					mUpdatethread = null;
    					return ;
    				}
    				mPendingUpdate = false;
    			
    			
    			wherequery = new StringBuilder();
    			
    			array = new String[listeners.size() + viewMap.size()+1];
    			Iterator<Long> itor = viewMap.keySet().iterator();
    			int i = 0;
    			while(itor.hasNext()){
    			    long downloadId = itor.next();
    			    wherequery.append(Downloads.Impl._ID).append("=?").append(" OR ");
    			    array[i++] = String.valueOf(downloadId);
    			}
    			
    			itor = listeners.keySet().iterator();
    			while(itor.hasNext()){
    			    long downloadId = itor.next();
    			    wherequery.append(Downloads.Impl._ID).append("=?").append(" OR ");
                    array[i++] = String.valueOf(downloadId);
    			}
    			 wherequery.append(Downloads.Impl._ID).append("=?");
    			 //array[i++] = "0";这样会导致数组越界
                           array[i] = "0";
    			}
                Cursor cursor = mContext.getContentResolver().query(
                        Downloads.Impl.CONTENT_URI,
                        new String[] {
                                Downloads.Impl._ID,
                                Downloads.Impl.COLUMN_TITLE,
                                Downloads.Impl.COLUMN_STATUS,
                                Downloads.Impl.COLUMN_TOTAL_BYTES,
                                Downloads.Impl.COLUMN_CURRENT_BYTES,},
                        wherequery.toString(),
                        array, null);  
                ///////////////////////////////////////////////////
                boolean isFinish = false;
                try {
                    if (cursor != null && cursor.getCount()>0) {
                        cursor.moveToFirst();
                        do{
                            int downloadId = cursor.getInt(0);
                            String title = cursor.getString(1);
                            int nStatus = cursor.getInt(2);
                            long totalSize = cursor.getLong(3);
                            long currentSize = cursor.getLong(4);
                            if (Downloads.Impl.isStatusError(nStatus) 
                                    || Downloads.Impl.isStatusServerError(nStatus)
                                    || Downloads.Impl.isStatusClientError(nStatus)){
                                android.util.Log.d(tag,"download error");
                                Message msg = new Message();
                                msg.what = 100001;
                                msg.arg1 = downloadId;
                                handler.sendMessage(msg);
//                                downloadError(downloadId);
                            }else{
                                isFinish = Downloads.Impl.isStatusSuccess(nStatus) || Downloads.Impl.isStatusCompleted(nStatus);
    //                            isFinish = totalSize == currentSize && totalSize != 0 ;    // 下载完成检查.
                                int progress = (int) ((currentSize * 100) / totalSize);
                                if (progress < 0){
                                    android.util.Log.d(tag,"download error");
                                    Message msg = new Message();
                                    msg.what = 100001;
                                    msg.arg1 = downloadId;
                                    handler.sendMessage(msg);
//                                    downloadError(downloadId);
                                }else  if (isFinish) {
                                    
                                    android.util.Log.d(tag,
                                            "Download finish");
                                    
                                    //广播下载完成  ACTION_DOWNLOAD_SUCCESS 事件
                                    Intent myIntent = new Intent();
                                    myIntent.putExtra("download_id", downloadId);
                                    myIntent.setAction(ACTION_DOWNLOAD_SUCCESS);
                                    // mogoo lxr add 2012.08.15
                                    // 系统已经发送广播，此处不冲突系统广播.
                                    //mContext.sendBroadcast(myIntent);
                                    // mogoo lxr add end 2012.08.15
                                    Message msg = new Message();
                                    msg.what = 100000;
                                    msg.arg1 = downloadId;
                                    handler.sendMessage(msg);
            
                                } else {
                                // 更新正在下载任务的进度
                                    android.util.Log.d(tag, " Downloading: " + progress);
                                    Message msg = new Message();
                                    msg.what = 100002;
                                    msg.arg1 = downloadId;
                                    msg.arg2 = progress;
                                    handler.sendMessage(msg);
//                                    updateView(downloadId, progress);
                                }
                            }
                        }while(cursor.moveToNext());
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                finally
                {
                    if(cursor!=null)
                    {
                        cursor.close();
                    }
                }
    			
    			
    		}
    		
    	}
    }
    
}
