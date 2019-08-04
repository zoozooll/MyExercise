package com.oregonscientific.meep.photo;

import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

public class PhotoService extends Service {

	private final String TAG = "RecommendationService";

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return mBinder;
	}

	@Override
	public int onStartCommand (Intent intent, int flags, int startId) {
		// Server messages will be inside the intent's extras
		handleCommand(intent);
		
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.
		return START_STICKY;
	}
	
	/**
	 * Handles the command as specified in the {@code} intent 
	 * 
	 * @param intent The intent supplied to {@link #onStartCommand(Intent, int, int)}
	 */
	private void handleCommand(Intent intent) {
		// Quick return if there is nothing to process
		if (intent == null) {
			return;
		}
	}
	
	private IPhotoService.Stub mBinder = new IPhotoService.Stub() {
		
		 /**
		  * Return the total numbers of photo in the device
		  * 
		  * @return return the number of photos in the device
		  */
		@Override
		public List<String> getPhotos() throws RemoteException {
			return null;
		}
		
		 /**
		  * Returns a list of file {@link android.net.Uri} for the photos in the device
		  * 
		  * @return return a list of URIs for the Photos, null if there are no photos in the device
		  */
		@Override
		public int getPhotoCount() throws RemoteException {
			return 0;
		}
	};
	
}
