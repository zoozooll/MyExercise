package com.mogoo.market.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mogoo.components.download.MogooDownloadInfo;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.AppUpdatesInfo;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.InstalledAppInfo;
import com.mogoo.market.ui.ManagerActivity;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;
import com.mogoo.market.widget.TextProgressBar;

/**
 * 软件管理列表适配器
 */
public class SoftManagerAdapter<T> extends ArrayAdapter<T> 
{
	private Context mContext;
	private LayoutInflater mInflater;
	
	//字符串资源
    private Resources rs;
    private String sVersion = "";
    private String sAppSize = "";
    
    private MogooDownloadManager manager = null;
    
	public SoftManagerAdapter(Context context, int textViewResourceId, List<T> list) 
	{
		super(context, textViewResourceId, list);
		mContext = context;
		
		mInflater = LayoutInflater.from(mContext);
		
		rs = mContext.getResources();
        sVersion = rs.getString(R.string.version_download_manager);
        sAppSize = rs.getString(R.string.app_size);
        
        manager = new MogooDownloadManager(mContext, mContext.getContentResolver(), mContext.getPackageName());
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			removeMessages(1);
			if(SoftManagerAdapter.this.isEmpty())
			{
				SoftManagerAdapter.this.notifyDataSetInvalidated();
			}
			else
			{
				SoftManagerAdapter.this.notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
	
	static class ViewHolder 
    {
    	//图片
        public ImageView appIcon;
        //名称
        public TextView appName;
        //软件大小
        public TextView appSize;
        //下载进度条
        public TextProgressBar progressbar;
        public TextView tv_progress;
        public TextView app_version;
        //评级
        public RatingBar appRating;
        //左一按钮
        public Button btn1;
        //左二按钮
        public Button btn2;
        //中间按钮
        public Button btn3;
        //更新中 进度条
        public TextProgressBar updatingProgressBar;
        
        public Object tag;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(position >= getCount()) {
			return convertView;
		}
		Log.d("#####","+++++position=,getCount()="+position+"$"+getCount());
		final InstalledAppInfo installedInfo = (InstalledAppInfo)getItem(position);
		ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.down_manager_item, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.app_icon);
            holder.appName = (TextView) convertView.findViewById(R.id.app_name);
            holder.appSize = (TextView) convertView.findViewById(R.id.app_size);
            holder.progressbar = (TextProgressBar) convertView.findViewById(R.id.idDownload_progressbar);
            holder.tv_progress = (TextView) convertView.findViewById(R.id.tv_download_progress);
            holder.app_version = (TextView) convertView.findViewById(R.id.app_version);
            holder.appRating = (RatingBar) convertView.findViewById(R.id.app_rating);
            holder.btn1 = (Button) convertView.findViewById(R.id.down_install_item_btn2);
            holder.btn2 = (Button) convertView.findViewById(R.id.down_install_item_btn1);
            holder.btn3 = (Button) convertView.findViewById(R.id.down_install_item_btn3);
            holder.updatingProgressBar = (TextProgressBar) convertView.findViewById(R.id.update_progress_bar);
            convertView.setTag(holder);
        }
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        String appName = installedInfo.getAppName();
        if(appName.endsWith(".apk"))
        {
        	appName = appName.replace(".apk", "");
        }
        holder.appName.setText(appName);
        holder.appName.setSelected(true);
        
        Drawable appIcon = installedInfo.getAppIcon();
        holder.appIcon.setImageDrawable(appIcon);
        
        String size = installedInfo.getSize();
        holder.appSize.setText(sAppSize+size);
    	
    	holder.updatingProgressBar.setVisibility(View.GONE);
    	holder.tv_progress.setVisibility(View.GONE);
    	holder.progressbar.setVisibility(View.GONE);
    	
    	holder.app_version.setVisibility(View.VISIBLE);
    	holder.app_version.setText(sVersion+installedInfo.getPackageInfo().versionName);
    	
    	holder.btn1.setVisibility(View.VISIBLE);
		holder.btn1.setText(R.string.open_application);
		holder.btn1.setBackgroundResource(R.drawable.manager_down_pause);
		
    	holder.btn2.setVisibility(View.VISIBLE);
    	holder.btn2.setText(R.string.uninstall);
    	holder.btn2.setTextColor(Color.WHITE);
    	holder.btn2.setBackgroundResource(R.drawable.manager_down_delete);
    	
    	holder.btn3.setVisibility(View.GONE);
    	
    	holder.tag = null;
    	
    	if(installedInfo.getUpdateInfo()!=null)  //有更新
    	{
    		String packageName = installedInfo.getUpdateInfo().getPackageame();
    		
    		holder.btn1.setVisibility(View.VISIBLE);
    		if(MarketApplication.isHaveUpdate(packageName))
    		{
    			String apkName = installedInfo.getUpdateInfo().getAppName() + "_" + installedInfo.getUpdateInfo().getAppVersionCode() + ".apk";
				if(ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, apkName.replace(" ", "")))  //已经更新下载了，直接安装文件
				{
					holder.btn1.setText(R.string.install);
				}else {
					holder.btn1.setText(R.string.app_update);
				}
    		}
    		else
    		{
    			holder.btn1.setText(R.string.open_application);
    		}
			
    		HashMap<String, String> allUpdates = UpdatesUtils.getAll(mContext);
    		
    		if(allUpdates.containsKey(packageName))  //正在更新
    		{
    			MogooDownloadInfo di = manager.getDownloadInfo(allUpdates.get(packageName));
    			if(di!=null && di.getDownloadStatus()<200)  //正在下载,显示进度
            	{
    				holder.btn1.setVisibility(View.GONE);
            		holder.btn2.setVisibility(View.GONE);
            		
            		holder.btn3.setVisibility(View.VISIBLE);
            		holder.btn3.setText(R.string.zhzaidownloading);
            		
            		//添加进度条
            		//long updateDownloadId = Long.valueOf(allUpdates.get(packageName));
                	//........................
                	//holder.updatingProgressBar.setVisibility(View.VISIBLE);
                	//registerDownloadListener(holder,updateDownloadId);
            	}
    			else  //没有下载，显示更新
    			{
					UpdatesUtils.delete(mContext, packageName);
    			}
    		}
    	}
        
        holder.btn1.setOnClickListener(new MyButtonListener(1,holder,installedInfo));
    	holder.btn2.setOnClickListener(new MyButtonListener(2,holder,installedInfo));
		
		return convertView;
	}
	
	
	class MyButtonListener implements View.OnClickListener
    {
    	private int buttonIndex = 0;
    	private InstalledAppInfo installedInfo;
    	private ViewHolder holder;
    	
		public MyButtonListener(int buttonIndex,ViewHolder holder,InstalledAppInfo installedInfo) 
		{
			super();
			this.buttonIndex = buttonIndex;
			this.installedInfo = installedInfo;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) 
		{
			PackageInfo pinfo = installedInfo.getPackageInfo();
			switch (buttonIndex) 
			{
			case 1:  //1--打开/更新
				AppUpdatesInfo updateInfo = installedInfo.getUpdateInfo();
				if(updateInfo!=null && MarketApplication.isHaveUpdate(pinfo.packageName))  //更新
            	{
					String apkName = updateInfo.getAppName() + "_" + updateInfo.getAppVersionCode() + ".apk";
					if(ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, apkName))  //已经更新下载了，直接安装文件
					{
						String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
						installApk(rootPath+"/"+MarketApplication.SavaPath+apkName);
					}
					else    //没更新下载，下载更新
					{
						if(!ToolsUtils.checkBeforeDownload(MarketApplication.getInstance(),Long.valueOf(installedInfo.getSizebytes())))
						{
							break;
						}
						
	            		long mDownLoadId = ToolsUtils.downloadApk(manager, MarketApplication.SavaPath, apkName,updateInfo.getUpdateUrl());
	            		if(mDownLoadId>0)
						{
	            			holder.btn1.setVisibility(View.GONE);
		            		holder.btn2.setVisibility(View.GONE);
		            		holder.btn3.setVisibility(View.VISIBLE);
		            		holder.btn3.setText(R.string.zhzaidownloading);
		            		boolean insertFlat=true;
		            		
		            		for(DownloadInfo downloadInfo : MarketApplication.myDownloadAppList){
		            			if(downloadInfo.getApp_id().equals(updateInfo.getApkId())){
		            				insertFlat=false;
		            				break;
		            			}else{
		            				insertFlat=true;
		            			}
		            				
		            		}
	            			//插入下载数据信息到数据库
		            		if(insertFlat){
		            		DaoFactory.getDownloadInfoDao(mContext).addBean(new DownloadInfo(String.valueOf(mDownLoadId), updateInfo.getApkId(), 
		            				updateInfo.getUpdateUrl(), MarketApplication.SavaPath, apkName, installedInfo.getSizebytes(), updateInfo.getIconUrl(), "",
		            				updateInfo.getAppVersionName(), Integer.valueOf(updateInfo.getAppVersionCode()), updateInfo.getPackageame()));
		            		}
		            		DownPrefsUtil.getInstance(mContext).savePresValue(String.valueOf(mDownLoadId), String.valueOf(updateInfo.getApkId()), String.valueOf(true));
							
		            		//保存更新下载id
		            		UpdatesUtils.save(mContext, updateInfo.getPackageame(), ""+mDownLoadId);
		            		
		                	//holder.updatingProgressBar.setVisibility(View.VISIBLE);
		                	//registerDownloadListener(holder,mDownLoadId);
						}
					}
            	}
            	else   //打开
            	{
            		PackageManager pm = mContext.getPackageManager();
            		if(pinfo != null && pinfo.packageName != null && !TextUtils.isEmpty(pinfo.packageName)) {
            			mContext.startActivity(pm.getLaunchIntentForPackage(pinfo.packageName));
            		}
            	}
				break;
			case 2:  //2--卸载
				unInstallApp(mContext, pinfo.packageName, ManagerActivity.RESULT_DELETE_OK);
				break;
			default:
				break;
			}
		}
    	
    }
	
	/**
     * 安装软件，软件安装后也从列表删除
     */
    private void installApk(String apkPath) 
    {
    	ToolsUtils.onInstallApk(mContext, apkPath);
    	
    	Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}
    
	/**
     * 卸载程序
     */
    private void unInstallApp(Context context, String packageName, int requestCode) 
    {
    	ToolsUtils.onUninsatllApk(mContext, packageName,requestCode );
	}
    
    /**
	 * 设置下载监听器，监听下载进度
	 */
	/*private void registerDownloadListener(final ViewHolder holder,final long downloadId) 
	{
		holder.updatingProgressBar.setProgress(0);
		
		DownloadProgressListener downListener = new DownloadProgressListener() 
		{
			@Override
			public void onDownloadProgressChanged(int progress) 
			{
				if (holder.tag == this) 
				{
					if (progress >= 0 && progress<=100) 
					{
						holder.updatingProgressBar.setProgress(progress);
					}
				}
			}

			@Override
			public long getDownloadId() 
			{
				return downloadId;
			}
		};

		DownloadProgressManager.getInstance(mContext).registerProgressListener(downListener);
		holder.tag = downListener;
	}*/
	
}
