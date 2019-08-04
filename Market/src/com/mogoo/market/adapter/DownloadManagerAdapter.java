package com.mogoo.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.mogoo.components.download.DownloadProgressListener;
import com.mogoo.components.download.DownloadProgressManager;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;
import com.mogoo.market.widget.TextProgressBar;

public class DownloadManagerAdapter<T> extends ArrayAdapter<T> 
{
	private Context mContext;
	private LayoutInflater mInflater;
	private ImageDownloader mImageLoader;
	
	//字符串资源
    private Resources rs;
    private String sDownlading = "";
    private String sPaused = "";
    private String sHaveDone = "";
    private String sVersion = "";
	
	private MogooDownloadManager manager = null;
	private DownPrefsUtil mPrefsUtil = null;
	
	public DownloadManagerAdapter(Context context, List<T> list, ImageDownloader imageLoader) 
	{
		super(context, -1, list);
		
		mContext = context;
		mImageLoader = imageLoader;
		mInflater = LayoutInflater.from(mContext);
		
		rs = mContext.getResources();
        sDownlading = rs.getString(R.string.downloading);
        sPaused = rs.getString(R.string.paused);
        sHaveDone = rs.getString(R.string.downloadone);
        sVersion = rs.getString(R.string.version_download_manager);
        
        manager = new MogooDownloadManager(mContext, mContext.getContentResolver(), context.getPackageName());
        mPrefsUtil = DownPrefsUtil.getInstance(mContext);
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
			case 1:
				removeMessages(1);
				if(DownloadManagerAdapter.this.isEmpty())
	        	{
					DownloadManagerAdapter.this.notifyDataSetInvalidated();
	        	}
	        	else
	        	{
	        		DownloadManagerAdapter.this.notifyDataSetChanged();
	        	}
				break;
			default:
				break;
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
        //左三按钮
        public Button btn3;
        
        public Object tag;
    }
	
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		if(position >= getCount()) {
			return convertView;
		}
		Log.d("#####","+++++position==,getCount()=="+position+"$"+getCount());
		final DownloadInfo info = (DownloadInfo)getItem(position);
		long downloadId = Long.valueOf(info.getDownload_id());
		
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
            convertView.setTag(holder);
        }
        else 
        {
            holder = (ViewHolder) convertView.getTag();
        }
        
        String appName = info.getName();
        if(appName.endsWith(".apk"))
        {
        	appName = appName.replace(".apk", "");
        }
        String sRating = info.getRating();
        float rating = 0;
        if(sRating!=null && !"".equals(sRating))
        {
        	rating = Float.valueOf(sRating);
        }
        
        String size = ToolsUtils.getSizeStr(info.getSize());
        
        holder.appIcon.setImageResource(R.drawable.defautl_list_itme_pic_loading);
        //加载图片
		downloadImage(info.getIcon_url(), holder);
        
        holder.appName.setText(appName);
        holder.appName.setSelected(true);
        
        holder.appRating.setRating(rating);
        
    	holder.progressbar.setVisibility(View.GONE);
    	
    	holder.app_version.setVisibility(View.VISIBLE);
        holder.app_version.setText(sVersion+info.getVersionName());
    	
    	holder.btn1.setVisibility(View.VISIBLE);
    	holder.btn1.setText(R.string.install);
    	holder.btn1.setBackgroundResource(R.drawable.manager_down_pause);
    	
    	holder.btn2.setVisibility(View.VISIBLE);
    	holder.btn2.setText(R.string.cancel);
    	holder.btn2.setTextColor(R.color.cancel_text_color);
    	holder.btn2.setBackgroundResource(R.drawable.manager_down_cancel);
    	
    	holder.appSize.setTextColor(R.color.app_size_color);
    	
    	holder.btn3.setVisibility(View.GONE);
    	
    	holder.tag = null;
    	
    	//***************************** 不查询数据库 *****************************
    	String value = mPrefsUtil.getPrefsValue(""+downloadId, "");
    	String[] result = mPrefsUtil.parseResponse(value);
    	if(result.length>1)  //有下载
    	{
    		holder.app_version.setVisibility(View.GONE);
			holder.progressbar.setVisibility(View.VISIBLE);
			registerDownloadListener(holder, downloadId);
			
    		if (TextUtils.equals("true", result[DownPrefsUtil.INFO_DOWN_LOAD_STATUS])) //下载中
    		{
    			holder.btn1.setText(R.string.pause);
    			holder.appSize.setText(sDownlading+size);
			}
    		else                                                                       //暂停
    		{
    			holder.btn1.setText(R.string.restart);
    			holder.appSize.setText(sPaused+size);
    		}
    	}
    	else                //没有下载
    	{
    		String infoName = info.getName();
			String apkName = "";
			if(infoName.contains("apk"))
			{
				apkName = infoName;
			}
			else
			{
				apkName = ToolsUtils.getFullApkName(info.getName(), info.getVersionCode()+"");
			}
    		if(ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, apkName)
    				&&ToolsUtils.checkApk(mContext,AppUtils.getSDKpath()+"/MogooLoad/"+apkName, info.getApp_id())) //下载成功
    		{
    			holder.btn2.setText(R.string.delete);
                holder.btn2.setTextColor(Color.WHITE);
                holder.btn2.setBackgroundResource(R.drawable.manager_down_delete);
                
                holder.appSize.setText(sHaveDone+size);
    		}
    		else                                                                          //下载失败
    		{
    			if(!AppUtils.isSdcardExist()){
    				holder.btn1.setText(R.string.download);
                    holder.appSize.setTextColor(Color.RED);
                    holder.appSize.setText(R.string.uninstall_sdcard);
                    holder.progressbar.setProgress(0);
    			}
    			else {
    				//获取不到下载信息，也当下载失败处理
            		holder.btn1.setText(R.string.download);
        			holder.appSize.setTextColor(Color.RED);
                    holder.appSize.setText(R.string.download_failed);
                    holder.progressbar.setProgress(0);
    			}
    			
    		}
    	}
    	
    	holder.btn1.setOnClickListener(new MyButtonListener(position,1,holder,size,info));
    	holder.btn2.setOnClickListener(new MyButtonListener(position,2,holder,size,info));
    	
		return convertView;
	}
	
	/**
	 * mImageLoader下载图片
	 */
	private void downloadImage(final String icoUrl,final ViewHolder holder) 
	{
		mHandler.post(new Runnable() 
		{
			@Override
			public void run() 
			{
				mImageLoader.download(icoUrl, holder.appIcon,
						BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defautl_list_itme_pic_loading));
			}
		});
	}
	
	class MyButtonListener implements View.OnClickListener
    {
		private int position = 0;
    	private int buttonIndex = 0;
    	private DownloadInfo info;
    	private String size;
    	private ViewHolder holder;
    	
		public MyButtonListener(int position,int buttonIndex,ViewHolder holder,String size,DownloadInfo info) 
		{
			super();
			this.position = position;
			this.buttonIndex = buttonIndex;
			this.info = info;
			this.size = size;
			this.holder = holder;
		}

		@SuppressWarnings("unchecked")
		@Override
		public void onClick(View v) 
		{
			String downloadId = info.getDownload_id();
			/*MogooDownloadInfo downloadInfo= manager.getDownloadInfo(""+downloadId);
			if(downloadInfo==null)
			{
				if(buttonIndex==2)
				{
					DownloadManagerAdapter.this.remove((T) info);
					DaoFactory.getDownloadInfoDao(mContext).delete(downloadId);
				}
				return;
			}*/
			String infoName = info.getName();
			String apkName = "";
			if(infoName.contains("apk"))
			{
				apkName = infoName;
			}
			else
			{
				apkName = ToolsUtils.getFullApkName(info.getName(), info.getVersionCode()+"");
			}
			String apkFullPath = Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+MarketApplication.SavaPath+apkName;
			long downloadSize = Long.valueOf(info.getSize());
			
			String text = ((Button)v).getText().toString();
			switch (buttonIndex) 
			{
			case 1:  //1--暂停/继续/安装按钮
				/*if(downloadInfo.getDownloadStatus()==200)  //下载完
            	{
					installApk(downloadInfo.getDownloadPosition());
            	}
            	else
            	{
            		if(downloadInfo.getDownloadStatus()<200)   //下载正常且未完成
            		{
            			if(downloadInfo.getControl()==0)  //下载中
                		{
                			manager.pauseDownload(Long.parseLong(downloadId));
                			mPrefsUtil.savePresValue(downloadId, info.getApp_id(), String.valueOf(false));
                			
                			holder.btn1.setText(R.string.restart);
                			holder.appSize.setText(sPaused+size);
                		}
                		else   //暂停
                		{
                			manager.restartDownload(Long.parseLong(downloadId));
                			mPrefsUtil.savePresValue(downloadId, info.getApp_id(), String.valueOf(true));
                			
                			holder.btn1.setText(R.string.pause);
                			holder.appSize.setText(sDownlading+size);
                		}
            		}
            		else   //下载失败,重新开始下载
            		{
            			String apkName = downloadInfo.getDownloadTitle();
            			String apkUrl = downloadInfo.getDownloadUrl();
            			long reDownloadId = restartDownload(apkName,apkUrl);
            			if(reDownloadId>0)
            			{
            				DownloadInfo info = (DownloadInfo) DownloadManagerAdapter.this.getItem(position);
            				info.setDownload_id(reDownloadId+"");
            			}
            		}
            		
            		Message msg = new Message();
            		msg.what = 1;
            		mHandler.sendMessage(msg);
            	}*/
				
				if(text.equals(rs.getString(R.string.pause)))
				{
					ToolsUtils.pauseDownload(mContext, manager, downloadId, info.getApp_id());
        			
        			holder.btn1.setText(R.string.restart);
        			holder.appSize.setText(sPaused+size);
				}
				else if(text.equals(rs.getString(R.string.restart)))  //暂停重新下载
				{
					ToolsUtils.restartDownload(mContext, manager, downloadId, info.getApp_id());
					
        			holder.btn1.setText(R.string.pause);
        			holder.appSize.setText(sDownlading+size);
				}
				else if(text.equals(rs.getString(R.string.install)))
				{
					installApk(apkFullPath);
				}
				else if(text.equals(rs.getString(R.string.download)))  //下载失败重新下载
				{
        			String apkUrl = info.getDownload_url();
        			restartDownload(apkName,apkUrl,downloadSize,position);
				}
				
				Message msg = new Message();
        		msg.what = 1;
        		mHandler.sendMessage(msg);
				break;
			case 2:  //2--取消/删除按钮
				v.setOnClickListener(null); //去掉点击事件，防止多次点击
				
				/*if(downloadInfo.getDownloadStatus()==200)  //下载完
				{
					DownloadManagerAdapter.this.remove((T) info);
					deleteFile(downloadInfo.getDownloadPosition(),downloadId);
					manager.cancelDownload(Long.parseLong(downloadId));
				}
				else
				{
					DownloadManagerAdapter.this.remove((T) info);
					cancelDownload(downloadId);
				}*/
				
				if(text.equals(rs.getString(R.string.cancel)))
				{
					DownloadManagerAdapter.this.remove((T) info);
					cancelDownload(downloadId);
				}
				else if(text.equals(rs.getString(R.string.delete)))
				{
					DownloadManagerAdapter.this.remove((T) info);
					deleteFile(apkFullPath,downloadId);
					cancelDownload(downloadId);
				}
				// lxr add 取消或下载同时删除正在更新列表中的项.
				String packageName = (info.getPackageName() != null) ? info.getPackageName() : "";
				UpdatesUtils.delete(mContext, packageName);
				break;
			default:
				break;
			}
		}
    	
    }
	
	
	/**
     * 安装软件，软件安装后也从列表删除
     * @param apkPath
     * @param downloadId
     */
    private void installApk(String apkPath) 
    {
    	ToolsUtils.onInstallApk(mContext, apkPath);
    	
    	Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}
    
    /**
     * 删除文件
     * @param fileName
     */
    private boolean deleteFile(String fileName,String downloadId) 
    {
    	boolean b = false;
    	if(fileName!=null)
    	{
    		ToolsUtils.deleteFile(mContext, fileName);
    	}
		
		DaoFactory.getDownloadInfoDao(mContext).delete(downloadId);
		
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
		
		return b;
	}
    
    /**
     * 取消下载，软件列表删除
     * @param downloadId
     */
    private void cancelDownload(String downloadId) 
    {
    	ToolsUtils.cancelDownload(mContext, manager, Long.valueOf(downloadId));
    	//lzg,modify 2012.7.18
    	DaoFactory.getDownloadInfoDao(mContext).delete(downloadId);
    	/*Intent i = new Intent(ManagerActivity.BROADCAST_ACTION_FILE_DELETE);
		mContext.sendBroadcast(i);*/
		
		Message msg = new Message();
		msg.what = 1;
		mHandler.sendMessage(msg);
	}
    
    /**
     * 重新开始下载
     */
    private long restartDownload(String apkName,String apkUrl,long size,int position) 
    {
    	long mDownLoadId = -1;
    	if(ToolsUtils.checkBeforeDownload(mContext, size))
    	{
    		mDownLoadId = ToolsUtils.downloadApk(manager, MarketApplication.SavaPath, apkName,apkUrl);
    	}
    	
    	if(mDownLoadId>0)
		{
			DownloadInfo info = (DownloadInfo) DownloadManagerAdapter.this.getItem(position);
			info.setDownload_id(mDownLoadId+"");
			mPrefsUtil.savePresValue(""+mDownLoadId, info.getApp_id(), String.valueOf(true));
		}
		return mDownLoadId;
	}
    
    /**
	 * 设置下载监听器，监听下载进度
	 */
	private void registerDownloadListener(final ViewHolder holder,final long downloadId) 
	{
		DownloadProgressListener downListener = new DownloadProgressListener() 
		{
			@Override
			public void onDownloadProgressChanged(int progress) 
			{
				if (holder.tag == this) 
				{
					if (progress>=0 && progress<=100) 
					{
						holder.progressbar.setProgress(progress);
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
	}
}
