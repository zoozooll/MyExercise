package com.mogoo.market;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.util.Log;
import cn.imogoo.providers.downloads.DownloadManager;

import com.mogoo.components.download.MogooDownloadInfo;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.manager.MogooLocalSoftManager;
import com.mogoo.market.model.AppUpdatesInfo;
import com.mogoo.market.model.AppUpdatesInfoReq;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.InstalledAppInfo;
import com.mogoo.market.model.UploadAppInfoReq;
import com.mogoo.market.model.onRequestCallBack;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;
import com.mogoo.parser.Result;
/**
 * @author 张永辉
 * @date 2011-12-24
 */
public class MarketApplication extends Application 
{
	public static final String SELF_VERSION = 2.0+"";
	public static boolean debug = false;
	public static String TAG = "MarketApplication";
    
	/**
	 * 是否为蘑菇系统；
	 * 当前商城将用于蘑菇系统和高仿系统中，如果为高仿系统则值为false
	 */
	public static boolean isMogooSystem = false;
	
	private static MogooLocalSoftManager mManager;
	
	public static final String SavaPath = "MogooLoad/";
	private String downloadPath = null;
	
	private MogooDownloadManager manager = null;
	
	private DownPrefsUtil mPrefsUtil = null;

	private BroadcastReceiver mUnmountReceiver = null;
	
    private static MarketApplication instance = null;
    public static MarketApplication getInstance()
    {
    	if(instance != null)
    	{
    		return instance;
    	}
    	return new MarketApplication();
    }
	
	@Override
	public void onCreate() 
	{
		super.onCreate();
		
		instance = this;
		
		mManager = MogooLocalSoftManager.init(this);
		
		downloadPath = AppUtils.getSDKpath() + "/" + SavaPath;
		
		registAppInstallReceiver();
		registerDownloadBroadcastReceiver();
		registerExternalStorageListener();
		manager = new MogooDownloadManager(this, this.getContentResolver(), null);
		mPrefsUtil = new DownPrefsUtil(this);
    }

    @Override
    public void onTerminate() 
    {
        super.onTerminate();
        mManager.destory();
        unRegistAppInstallReceiver();
        unRegisterDownloadBroadcastReceiver();
		if (mUnmountReceiver != null) {
			unregisterReceiver(mUnmountReceiver);
			mUnmountReceiver = null;
		}
    	// 清空所有数据库中列表的缓存.
    	DaoFactory.clearDataBase(this);
    }
	
    public static MogooLocalSoftManager getmManager() {
		return mManager;
	}
    
    //*******************************************************************************
  	/**
  	 * 应用缓存目录
  	 */
  	private static String cacheAppPath  = null;
  	/**
  	 * 返回应用程序缓存目录
  	 */
  	public static String getAppCachePath()
  	{
  		return cacheAppPath;
  	}
  	
    //****************************************** 软件更新 *********************************************
  	// 上传客户端安的apk 信息
 	public void onUploadApkInfo() 
 	{
		UploadAppInfoReq.onUploadAppInfo(this,
		new onRequestCallBack() 
		{
			@Override
			public void onSuccess(Result result) 
			{
				if(debug)
				{
					Log.d(TAG, "#####  onUploadApkInfo  onSuccess");
				}
				
				getAppUpdateInfo();  //上传成功了，就要再获取有更新的软件列表
			}

			@Override
			public void onFail(Result result) 
			{
				Log.e(TAG, "#####  onUploadApkInfo  onFail");
			}
		});
 	}
 	
 	/**
     * 请求并获取更新信息
     */
    public void getAppUpdateInfo() 
    {
		final HashMap<String, String> params = new HashMap<String, String>();
		
		AppUpdatesInfoReq.onRequest(this, params, new onRequestCallBack() 
		{
			@Override
			public void onSuccess(Result result) 
			{
				@SuppressWarnings("unchecked")
				ArrayList<AppUpdatesInfo> appUpdatesInfoList = (ArrayList<AppUpdatesInfo>) result.getData();
				
				//清空再重新设置更新数据
				appUpdatesInfoMap.clear();
				if(!appUpdatesInfoList.isEmpty())
				{
					for(int i = 0,sum = appUpdatesInfoList.size() ; i<sum ; ++i)
					{
						AppUpdatesInfo info = appUpdatesInfoList.get(i);
						appUpdatesInfoMap.put(info.getPackageame(), info);
						if(debug)
						{
							Log.d(TAG, "##### getAppUpdateInfo onSuccess  package"+i+" = "+info.getPackageame()+"  url = "+info.getUpdateUrl());
						}
					}
					
					//获取到了更新信息，将更新信息添加到已安装软件列表
					addUpdatesToInstallList();
				}
				else
				{
					if(debug)
					{
						Log.d(TAG, "##### getAppUpdateInfo onSuccess have no updates");
					}
				}
			}
			
			@Override
			public void onFail(Result result) 
			{
				Log.e(TAG, "##### getAppUpdateInfo onFail");
			}
		});
	}
    
    /**
     * 将更新信息添加到已安装软件列表
     */
    private synchronized void addUpdatesToInstallList() 
    {
    	if(!appUpdatesInfoMap.isEmpty() && !installedAppList.isEmpty())
    	{
    		for(InstalledAppInfo info : installedAppList)
    		{
    			String packageName = info.getPackageInfo().packageName;
				if(appUpdatesInfoMap.containsKey(packageName))
    			{
					if(!appUpdatesInfoMap.get(packageName).getAppVersionCode().equals(""+info.getPackageInfo().versionCode))
    				{
    					info.setUpdateInfo(appUpdatesInfoMap.get(packageName));
    				}
    				else
    				{
    					info.setUpdateInfo(null);
    					appUpdatesInfoMap.remove(packageName);
    				}
    			}
				else
				{
					info.setUpdateInfo(null);
				}
    		}
    		
    		//通知其他地方软件列表有变动
        	for(PackagesChangeCallback cb : packagesChangeCallbacks)
            {
            	cb.updateListDateChanged();
            }
    	}
	}
  	
  	/**
     * add by csq:获取软件更新信息
     */
    public static  HashMap<String, AppUpdatesInfo> appUpdatesInfoMap = new HashMap<String, AppUpdatesInfo>();  //string : packageName
    /**
     * 获取更新列表
     */
    public static HashMap<String, AppUpdatesInfo> getAppUpdatesInfoMap() 
    {
		return appUpdatesInfoMap;
	}
    /**
     * 根据包名判断是否有更新
     */
    public static boolean isHaveUpdate(String packageName)
    {
    	boolean b = false;
    	if(packageName!=null && appUpdatesInfoMap.containsKey(packageName))
    	{
    		AppUpdatesInfo updatesInfo = appUpdatesInfoMap.get(packageName);
    		if(updatesInfo!=null && !"".equals(updatesInfo.getAppVersionCode()))
    		{
    			int vc = Integer.valueOf(appUpdatesInfoMap.get(packageName).getAppVersionCode());
    			Integer installVc = installedAppPackages.get(packageName);
        		if(installVc!=null && vc>installVc)
        		{
        			b = true;
        		}
    		}
    	}
    	return b;
    }

	
	//****************************************** 软件安装监听 *********************************************
	/**
	 * 已安装软件数组，适用于adapter，软件管理列表直接引用的此处数据
	 */
	public static ArrayList<InstalledAppInfo> installedAppList = new ArrayList<InstalledAppInfo>();
	/**
	 * key:packageName;value:versionCode，用于通过包名获取版本号
	 */
	public static HashMap<String,Integer> installedAppPackages = new HashMap<String, Integer>();
	
	/**
     * 获取已经安的软件
     * @return
     */
    public synchronized void updateInstalledAppInfos() 
    {
    	PackageManager pm = getPackageManager();
        ArrayList<PackageInfo> packageInfoList = (ArrayList<PackageInfo>) pm.getInstalledPackages(0);
        
        //可能有其他地方直接引用了installedAppList，所以不能installedAppList=list
        installedAppList.clear();
    	installedAppPackages.clear();
        for(PackageInfo pkgInfo : packageInfoList)
        {
        	ApplicationInfo appInfo = pkgInfo.applicationInfo;
        	String packageName = pkgInfo.packageName;
            
            if(((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) ==0||(appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP)!=0)
            		&& (pm.getLaunchIntentForPackage(pkgInfo.packageName)!=null)
            		&& !pkgInfo.packageName.equals("com.mogoo.market"))  //去掉蘑菇应用商城
            {
            	InstalledAppInfo info = new InstalledAppInfo(this, pkgInfo);
            	//如果有更新，设置更新信息
        		if(!appUpdatesInfoMap.isEmpty() && appUpdatesInfoMap.containsKey(packageName))
    			{
        			info.setUpdateInfo(appUpdatesInfoMap.get(packageName));
    			}
            	installedAppList.add(info);
            	installedAppPackages.put(packageName,pkgInfo.versionCode);
            }
        }
        
        //通知其他地方软件列表有变动
    	for(PackagesChangeCallback cb : packagesChangeCallbacks)
        {
        	cb.packageChanged();
        }
    }
    
    /**
     * 获取已经安装的应用列表
     * @param isContainMarket 返回的列表是否包含"com.mogoo.market"
     */
    public ArrayList<InstalledAppInfo> getInstalledAppList(boolean isContainMarket) 
    {
		return installedAppList;
	}
    
    /**
     * 通过已安装的版本名获取版本号
     */
    public int getInstalledVersionCode(String versionName)
    {
    	return installedAppPackages.get(versionName);
    }
    
    /**
     * 获取所有已安装软件版本名和版本号
     */
    public HashMap<String,Integer> getInstalledAppPackages() 
    {
		return installedAppPackages;
	}
    
    /**
     * 根据包名判断应用是否安装
     */
    public static boolean isPackageInstalled(String packageName)
    {
    	boolean isInstalled = false;
    	if(packageName!=null && installedAppPackages.containsKey(packageName))
    	{
    		isInstalled = true;
    	}
    	return isInstalled;
    }
    
    /**
     * add by csq:注册软件安装广播
     */
    private BroadcastReceiver installReceiver = null;
    private void registAppInstallReceiver() 
    {
    	updateInstalledAppInfos();  //注册时获取已经安的软件
    	
		if(installReceiver==null)
		{
			installReceiver = new BroadcastReceiver() 
			{
				@Override
				public void onReceive(Context context, Intent intent) 
				{
					String packageName = intent.getDataString();
					packageName = packageName.replace("package:", "");
					
					//获取包信息再添加移除也麻烦，直接重新获取软件列表
					updateInstalledAppInfos();
					
					if(packageName!=null)
					{
						Log.d("", "===== package  changed  packageName = "+packageName);
						
						//如果软件包变更的是更新的软件，则从更新列表中删除记录
						HashMap<String, String> allUpdates = UpdatesUtils.getAll(context);
						if(allUpdates.containsKey(packageName))
						{
							UpdatesUtils.delete(context, packageName);
						}
					}
				}
			};
			
			IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
			filter.addAction(Intent.ACTION_PACKAGE_REPLACED);
			filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
			filter.addDataScheme("package");
			registerReceiver(installReceiver, filter);
		}
	}
    
	// 启动广播，可以不断监听SD卡状态
	public void registerExternalStorageListener() {
		if (mUnmountReceiver == null) {
			mUnmountReceiver = new BroadcastReceiver() {
				@Override
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					if (action.equals(Intent.ACTION_MEDIA_EJECT)
							|| action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
						updateInstalledAppInfos(); // 注册时获取已经安的软件
					}
				}
			};
			IntentFilter iFilter = new IntentFilter();
			iFilter.addAction(Intent.ACTION_MEDIA_EJECT);
			iFilter.addAction(Intent.ACTION_MEDIA_SCANNER_FINISHED);
			iFilter.addDataScheme("file");
			registerReceiver(mUnmountReceiver, iFilter);
		}
	}
	
    
    /**
     * 注销软件安装广播监听
     */
    private void unRegistAppInstallReceiver() 
    {
    	if(installReceiver!=null)
		{
    		unregisterReceiver(installReceiver);
		}
    }
    
    /**
     *  添加到MarketApplication中的软件变更回调接口
     */
    public interface PackagesChangeCallback
    {
    	/**
    	 * 安装的软件有变更
    	 */
    	public void packageChanged();
    	/**
    	 * 更新信息appUpdatesInfoMap有变更
    	 */
    	public void updateListDateChanged();
    }
    
    /**
     * 所有软件变更回调
     */
    private ArrayList<PackagesChangeCallback> packagesChangeCallbacks = new ArrayList<PackagesChangeCallback>();
    /**
     * 增加软件变更回调
     */
    public void addInstalledAppCallback(PackagesChangeCallback callback) 
    {
    	if(callback==null)
    	{
    		throw new IllegalArgumentException("The PackagesChangeCallback is Null");
    	}
    	else
    	{
    		packagesChangeCallbacks.add(callback);
    		Log.d("#####","++++addInstalledAppCallback");
    	}
	}
    /**
     * 移除软件变更回调
     */
    public void removeInstalledAppCallback(PackagesChangeCallback callback) 
    {
    	packagesChangeCallbacks.remove(callback);
	}
    
    
    //********************************************* 下载广播监听 *****************************************************
    /**
     * 注册广播监听器,是否有任务下载完成
     */
    private BroadcastReceiver mDownloadReceiver = null;
 	private void registerDownloadBroadcastReceiver() 
 	{
 		updateMyDownloadAppList();  //注册时获取所有下载中以及下载完的的软件列表
 		
 		if(mDownloadReceiver != null)
 		{
 			return;
 		}
 			
 		mDownloadReceiver = new BroadcastReceiver() 
 		{
 			@Override
 			public void onReceive(Context context, Intent intent) 
 			{
 				String action = intent.getAction();
 				// 接收下载操作完成的广播（并不意味着下载成功）
 				if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) 
 				{
 					long id = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, 0);
 					int downloadStatus = -1;
 					MogooDownloadInfo downloadInfo= manager.getDownloadInfo(""+id);
 					
 					if(downloadInfo!=null)
 					{
 						downloadStatus = downloadInfo.getDownloadStatus();
 						
 						if(downloadStatus==200 && downloadInfo.getDownloadPosition()!=null)  //下载完成,自动安装
 			        	{
 			        		ToolsUtils.onInstallApk(context, downloadInfo.getDownloadPosition());
 			        	}
 						//下载失败记录还是留着，下载416也应该不是数据库存在原因
 	 					/*else
 	 					{
 	 						manager.cancelDownload(id);   //下载失败，从数据库删除下载记录
 	 					}*/
 					}
 					
 					Log.e("", "===== DOWNLOAD_COMPLETE   id = "+id+"    downloadStatus = "+downloadStatus);
 					
 					mPrefsUtil.removePresValue(String.valueOf(id));
 					
 					for(DownloadChangeCallback cb : downloadChangeCallbacks)
 			        {
 			        	cb.downloadChanged(id,downloadStatus);
 			        }
 				}
 			}
 		};

 		IntentFilter filter = new IntentFilter();
 		filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
 		registerReceiver(mDownloadReceiver, filter);
 	}
 	/**
     * 注销广播监听器
     */
 	private void unRegisterDownloadBroadcastReceiver() 
 	{
		if(mDownloadReceiver != null)
		{
			this.unregisterReceiver(mDownloadReceiver);
			mDownloadReceiver = null;
		}
	}
 	
 	//下载任务变更回调
 	/**
     *  添加到MarketApplication中的下载任务变更回调接口
     */
    public interface DownloadChangeCallback
    {
    	/**
    	 * @param downloadId = id的下载任务完成了
    	 */
    	public void downloadChanged(long id,int downloadStatus);
    	/**
    	 * 下载列表数据myDownloadAppList有变更
    	 */
    	public void downloadListDataChanged();
    }
    /**
     * 所有下载监听回调
     */
    private ArrayList<DownloadChangeCallback> downloadChangeCallbacks = new ArrayList<DownloadChangeCallback>();
    /**
     * 添加下载监听回调
     */
    public void addDownloadCallback(DownloadChangeCallback callback) 
    {
    	if(callback==null)
    	{
    		throw new IllegalArgumentException("The DownloadChangeCallback is Null");
    	}
    	else
    	{
    		downloadChangeCallbacks.add(callback);
    	}
	}
    /**
     * 移除下载监听回调
     */
    public void removeDownloadCallback(DownloadChangeCallback callback) 
    {
    	downloadChangeCallbacks.remove(callback);
	}
    
    
    public String getDownloadPath() {
		return downloadPath;
	}

	public void setDownloadPath(String downloadPath) {
		this.downloadPath = downloadPath;
	}
	
	
	//************************************* 获得下载中以及下载完的的软件列表 ******************************************
	/**
	 * 所有下载中以及下载完的的软件列表,即所有下载管理的数据，下载管理列表直接引用的此处数据
	 */
	public static ArrayList<DownloadInfo> myDownloadAppList = new ArrayList<DownloadInfo>();
	/**
	 * 更新下载中以及下载完的的软件列表
	 */
	public synchronized void updateMyDownloadAppList() 
    {
		Cursor c = DaoFactory.getDownloadInfoDao(instance).getAllBean();
		myDownloadAppList.clear();
        try 
        {
        	if(c!=null && c.getCount()>0)
            {
            	for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
                {
            		String download_id = c.getString(1);
            		String app_id = c.getString(2);
            		String download_url = c.getString(3);
            		String save_path = c.getString(4);
            		String name = c.getString(5);
            		String size = c.getString(6);
            		String icon_url = c.getString(7);
            		String score = c.getString(8);
            		String versionName = c.getString(9);
            		int versionCode = c.getInt(10);
            		String packageName = c.getString(11);
            		
            		//只要数据库存在，不管下载是否成功，都获取
            		DownloadInfo di = new DownloadInfo(download_id,app_id, download_url, save_path,
        					name, size, icon_url, score, versionName,versionCode,packageName);
            		addToMyDownloadAppList(di);
                }
            }
		} 
        catch (Exception e) {
			// TODO: handle exception
		}
        finally
        {
        	if(c!=null)
        	{
        		c.close();
        	}
        }
        
        for(DownloadChangeCallback cb : downloadChangeCallbacks)
        {
        	cb.downloadListDataChanged();
        }
	}
	/**
	 * 增加下载任务
	 */
	public void addToMyDownloadAppList(DownloadInfo downloadInfo) 
    {
		myDownloadAppList.add(downloadInfo);
		for(DownloadChangeCallback cb : downloadChangeCallbacks)
        {
        	cb.downloadListDataChanged();
        }
    }
	/**
	 * 删除下载任务
	 */
	public void deleteMyDownloadAppList(String download_id) 
    {
		for(DownloadInfo downloadInfo : myDownloadAppList)
		{
			if(downloadInfo.getDownload_id().equals(download_id))
			{
				myDownloadAppList.remove(downloadInfo);
				break;
			}
		}
		for(DownloadChangeCallback cb : downloadChangeCallbacks)
        {
        	cb.downloadListDataChanged();
        }
    }
	/**
	 * 增加下载任务
	 */
	public void clearAllMyDownloadAppList() 
    {
		myDownloadAppList.clear();
		for(DownloadChangeCallback cb : downloadChangeCallbacks)
        {
        	cb.downloadListDataChanged();
        }
    }
    
}
