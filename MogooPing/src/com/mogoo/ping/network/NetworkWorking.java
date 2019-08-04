/**
 * 
 */
package com.mogoo.ping.network;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.mogoo.ping.ctrl.RemoteApksManager;

import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author Aaron Lee
 * TODO
 * @Date 下午4:48:01  2012-10-16
 */
public class NetworkWorking {
	
	private static final String TAG = "NetworkWorking";
	
	/***/
	public static final int NETWORK_CONNECTION_TIMEOUT = 1000 * 10;

	public static int checkInternet(Context context) {
		int netType = -1;   
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        Log.i(TAG, "networkInfo "+networkInfo);
        if(networkInfo==null){  
            return netType;  
        }  
        int nType = networkInfo.getType();
        Log.d(TAG, "network type "+nType);
        if(nType==ConnectivityManager.TYPE_MOBILE){
			try {
				if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet")) {
					netType = 2;
				} else {
					netType = 3;
				}
			} catch (NullPointerException e) {
				netType = 4;
			}
        }  else if(nType==ConnectivityManager.TYPE_WIFI){  
            netType = nType;  
        }  
        return netType;  
        
	}
	
	/**
	 * Now the downloaded apk files is named with the MD5 replacing the software name plus the version string.
	 * @Author lizuokang
	 * @param context
	 * @param title
	 * @param localPath
	 * @param remotePath
	 * @return download id.if id == 0 means that the local file  exists,<br>
	 *  else if id == -1 means that download is fail;
	 * @Date 下午2:15:10  2012-10-13
	 */
	public static long downloadAndInstallApk(Context context,String title, String localPath, String remotePath) {
		long id = -1;
		Log.i(TAG, "local full path "+RemoteApksManager.APK_TEMP_URL+localPath);
		Log.i(TAG, "remote full path " + remotePath);
		if (localPath == null ||!new File(RemoteApksManager.APK_TEMP_URL+localPath).exists()) {
			if (!new File(RemoteApksManager.APK_TEMP_URL).exists()) {
				new File(RemoteApksManager.APK_TEMP_URL).mkdirs();
			}
			 //Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
			try {
				DownloadManager downloadManager = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);  
		          
				 Uri uri = Uri.parse(remotePath); 
				 Request request = new Request(uri);  
				 
				 request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE|DownloadManager.Request.NETWORK_WIFI);    
				 request.setTitle(title);
				 request.setVisibleInDownloadsUi(true);  
				 request.setAllowedOverRoaming(false);
				 request.setDestinationInExternalPublicDir("/MOGOO_PING/softwares", localPath);
				 id = downloadManager.enqueue(request);
				 Log.i(TAG, "IMPORTANT LOG begin download "+id);
				 return id;
			} catch (Exception e) {
				Log.e(TAG, "download fail ", e);
				return -1;
			}
		} else {
			return 0;
		}
	}
	
	public static boolean requestByGet(String url) {
		new RequestByGetAsyn().execute(url);
		return false;
	}
	
	public static boolean requestByGet(String url, Runnable onUpdateRunnable) {
		new RequestByGetAsyn().execute(url, onUpdateRunnable);
		return false;
	}
	
	private static class RequestByGetAsyn extends AsyncTask<Object, Void, Boolean> {
		
		private Runnable onUpdateRunnable;

		@Override
		protected Boolean doInBackground(Object... params) {
			HttpGet httpGet = new HttpGet((String)params[0]);
			if (params.length > 1) {
				onUpdateRunnable = (Runnable) params[1];
			}
			HttpResponse httpResponse;
			try {
				httpResponse = new DefaultHttpClient().execute(httpGet);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					Log.i(TAG, params[0]+" connect success");
					//String result = EntityUtils.toString(httpResponse.getEntity());
					//Log.d("aaron", "response result "+result);
					return true;
				} else {
					Log.i(TAG, params[0]+" connect false");
					
				}
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return false;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result && onUpdateRunnable != null) {
				onUpdateRunnable.run();
			}
			super.onPostExecute(result);
		}

		
	}
}
