package com.oregonscientific.meep.store2.global;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.database.table.TableEbook;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.store2.Constants;
import com.oregonscientific.meep.store2.R;
import com.oregonscientific.meep.store2.adapter.ImageDownloader;
import com.oregonscientific.meep.store2.adapter.ImageThreadLoader;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.EbookCtrl;
import com.oregonscientific.meep.store2.ctrl.ImageDownloadCtrl;
import com.oregonscientific.meep.store2.ctrl.MyBroadcastReceiver;
import com.oregonscientific.meep.store2.ctrl.RestRequest;
import com.oregonscientific.meep.store2.ctrl.RestRequestForInAppPurchase;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.db.DbAdapter;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreLoginInfo;

public class MeepStoreApplication extends Application {

	private DbAdapter mDbApt;
	private ImageDownloadCtrl mImageDownloadCtrl;
	private StoreItemDownloadCtrl mStoreDownloadCtrl;
	private RestRequest mRestRequest = null;
	private RestRequestForInAppPurchase mRestRequestIAP = null;
	private MeepStoreLoginInfo mLoginInfo = null;
	private ArrayList<TableEbook> mDownloadedEbook = null;
	private String mInstallingApk = null;
	private MyBroadcastReceiver mPackageBroadcastReciver = new MyBroadcastReceiver();
	private AppInstallationCtrl mAppCtrl = new AppInstallationCtrl();
	private ImageThreadLoader imageLoader = null;
	private ImageThreadLoader imageShotLoader = null;
	private PermissionManager mPermissionManager = null;
	
	@Override
	public void onCreate() {
		super.onCreate();
		mPermissionManager = (PermissionManager) ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
	}
	@Override
	public void onTerminate() {
		super.onTerminate();
		ServiceManager.unbindServices(this);
	}
	private static final String IMAGE_CACHE_DIR = "images";
	private ImageDownloader imageDownloader = null; 
	
	public ImageDownloader getImageDownloader() {
		if (imageDownloader == null) {
			imageDownloader = new ImageDownloader(this, IMAGE_CACHE_DIR);
		}
		return imageDownloader;
	}

	public DbAdapter getDatabaseAdapter() {
		return mDbApt;
	}

	public void setDatabaseAdapter(DbAdapter dbApt) {
		this.mDbApt = dbApt;
	}

	public MeepStoreLoginInfo getLoginInfo() {
		return mLoginInfo;
	}

	public void setLoginInfo(MeepStoreLoginInfo loginInfo) {
		this.mLoginInfo = loginInfo;
	}

	public ImageDownloadCtrl getImageDownloadCtrl() {
		return mImageDownloadCtrl;
	}

	public void setImageDownloadCtrl(ImageDownloadCtrl downloadCtrl) {
		this.mImageDownloadCtrl = downloadCtrl;
	}

	public RestRequest getRestRequest() {
		return mRestRequest;
	}

	public void setRestRequest(RestRequest restRequest) {
		this.mRestRequest = restRequest;
	}

	public StoreItemDownloadCtrl getStoreDownloadCtrl() {
		return mStoreDownloadCtrl;
	}

	public void setStoreDownloadCtrl(StoreItemDownloadCtrl storeDownloadCtrl) {
		this.mStoreDownloadCtrl = storeDownloadCtrl;
	}

	public ArrayList<TableEbook> getDownloadedEbook() {
		return mDownloadedEbook;
	}

	public void setDownloadedEbook(ArrayList<TableEbook> downloadedEbook) {
		this.mDownloadedEbook = downloadedEbook;
	}
	
//	public boolean isEbookInstalled(String id){
//		return EbookCtrl.isEbookInstalled(id, mDbApt.getDatabase());
//	}

	public boolean isPackageInstalled(Context context, String packageName){
		PackageManager packageManager = context.getPackageManager();
		List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
		for (ApplicationInfo packageInfo : packages) {
			
//			Log.d("compare", "pack1:" + packageName + "  pack2:" + packageInfo.packageName);
			if(packageInfo.packageName.equals(packageName)){
				return true;
			}

		}
		return false;
	}
	
	public boolean isItemWaitingToDownload(String itemId){
		return mStoreDownloadCtrl.isInDownloadQ(itemId);
	}
	
	public boolean isItemDownloading(String itemId){
		DownloadStoreItem item = mStoreDownloadCtrl.getDownloadingItem();
		if(item!=null && item.getId().equals(itemId)){
			return true;
		}else{
			return false;
		}
	}
	public boolean hasItemDownloading()
	{
		DownloadStoreItem item = mStoreDownloadCtrl.getDownloadingItem();
		if(item == null)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	public boolean isNetworkAvailable(Context context) {
	    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = cm.getActiveNetworkInfo();
	    // networkInfo will be null  if no network is available, otherwise connected
	    if (networkInfo != null && networkInfo.isConnected()) {
	        return true;
	    }
	    return false;
	}
	

	public String getInstallingApk() {
		return mInstallingApk;
	}

	public void setInstallingApk(String installingApk) {
		this.mInstallingApk = installingApk;
	}

	public MyBroadcastReceiver getPackageBroadcastReciver() {
		return mPackageBroadcastReciver;
	}

	public void setPackageBroadcastReciver(MyBroadcastReceiver packageBroadcastReciver) {
		this.mPackageBroadcastReciver = packageBroadcastReciver;
	}

	public AppInstallationCtrl getAppCtrl() {
		return mAppCtrl;
	}

	public void setAppCtrl(AppInstallationCtrl mAppCtrl) {
		this.mAppCtrl = mAppCtrl;
	}

	public ImageThreadLoader getImageLoader() {
		return imageLoader;
	}

	public void setImageLoader(ImageThreadLoader imageLoader) {
		this.imageLoader = imageLoader;
	}

	public ImageThreadLoader getImageShotLoader() {
		return imageShotLoader;
	}

	public void setImageShotLoader(ImageThreadLoader imageShotLoader) {
		this.imageShotLoader = imageShotLoader;
	}
	
	public boolean removeWaitingItem(String id)
	{
		boolean removed = mStoreDownloadCtrl.removeItemInDownloadTable(id);
		Log.d("test","remove waiting - "+removed);
		return removed;
	}
	
	public String getAccountToken(Context context) {
		SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Constants.PREFERENCE_KEY_TOKEN, "");
	}
	public String getAccountToken() {
		SharedPreferences sp = this.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Constants.PREFERENCE_KEY_TOKEN, "");
	}
	public String getAccountID() {
		SharedPreferences sp = this.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		return sp.getString(Constants.PREFERENCE_KEY_ID, "");
	}
	public String getUserToken()
	{
		return getAccountToken();
	}
	public void setAccountInformation(Account account) {
		SharedPreferences sp = this.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
		Editor edit = sp.edit();
		edit.clear();
		Log.d("test","store:"+account.toJson());
		edit.putString(Constants.PREFERENCE_KEY_TOKEN ,account.getToken());
		edit.putString(Constants.PREFERENCE_KEY_ID ,account.getId());
		edit.commit();
	}
//	public void resetAccountInformation(Context context) {
//		SharedPreferences sp = context.getSharedPreferences(Constants.PREFERENCE_FILE_NAME, Context.MODE_PRIVATE);
//		Editor edit = sp.edit();
//		edit.clear();
//		edit.commit();
//	}
	
	public RestRequestForInAppPurchase getRestRequestIAP() {
		if (mRestRequestIAP == null) {
			mRestRequestIAP = new RestRequestForInAppPurchase(getApplicationContext());
		}
		return mRestRequestIAP;
	}

	public void setmRestRequestIAP(RestRequestForInAppPurchase mRestRequestIAP) {
		this.mRestRequestIAP = mRestRequestIAP;
	}
	
	public boolean containsBadwords(String word)
	{
		Log.d("test",getAccountID());
		return mPermissionManager.containsBadwordBlocking(getAccountID(), word);
	}
}
