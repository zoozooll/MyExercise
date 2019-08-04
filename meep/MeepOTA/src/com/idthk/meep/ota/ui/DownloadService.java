package com.idthk.meep.ota.ui;

import java.io.File;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

import com.idthk.meep.ota.utility.Constants;
import com.idthk.meep.ota.utility.DownloadUtility;
import com.idthk.meep.ota.utility.OtaUpgradeUtility;

public class DownloadService extends Service{
	
	public static boolean mDownloading = false;
	Thread mDownloadThread;
	private String url="";
    @Override
    public void onCreate() {
    	if(mDownloadThread==null || !mDownloadThread.isAlive())
    	{
    		mDownloading = true;
    		mDownloadThread = new Thread(downloadRun);
    		mDownloadThread.start();
    	}
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private final IBinder mBinder = new Binder() {
        @Override
        protected boolean onTransact(int code, Parcel data, Parcel reply,
                        int flags) throws RemoteException {
                return super.onTransact(code, data, reply, flags);
        }
    };

    @Override
    public void onDestroy() {
    }
    
    private Runnable downloadRun = new Runnable() {
		
		@Override
		public void run() {
			DownloadUtility downloadUtility = new DownloadUtility();
	    	downloadUtility.setmDownloadListener(new DownloadUtility.DownloadListener() {
				
				@Override
				public void onProgress(int progress) {
					Intent i = new Intent();
					i.setAction(Constants.ACTION_DOWNLOAD_PROGRESS);
					i.putExtra(Constants.VALUE_STRING_PROGRESS, progress);
					sendBroadcast(i);
				}
			});
			//download
	    	String localPath =  OtaUpgradeUtility.CACHE_PARTITION + OtaUpgradeUtility.DEFAULT_PACKAGE_NAME;
	    	File file = downloadUtility.downloadOtaFile(url, localPath);
	    	
	    	
	    	if(file == null)
	    	{
	    		//TODO: Pop up:there's not enough space or no network.
	    		Intent i = new Intent(Constants.ACTION_DOWNLOAD_ABORT);
	    		sendBroadcast(i);
	    	}
	    	else
	    	{
	    		Intent i = new Intent(Constants.ACTION_DOWNLOAD_COMPLETE);
	    		sendBroadcast(i);
	    	}
	    	downloadUtility.closeHttpClient();
	    	mDownloading = false;
		}
	};
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
	    // stopped, so return sticky.
		handlerStart(intent);
	    return START_STICKY;
	}
	
	public void handlerStart(Intent intent)
	{
		url = intent.getStringExtra(Constants.VALUE_STRING_URL);
		Utils.printLogcatDebugMessage(url);
		if(mDownloadThread==null || !mDownloadThread.isAlive())
    	{
			mDownloading = true;
    		mDownloadThread = new Thread(downloadRun);
    		mDownloadThread.start();
    	}
	}
}
