package com.mogoo.market.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.adapter.StorageDeleteAdapter.MyButtonListener;
import com.mogoo.market.adapter.StorageDeleteAdapter.ViewHolder;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.SdcardInstalledAppInfo;
import com.mogoo.market.utils.ScanSdCardUtils;
import com.mogoo.market.utils.ToolsUtils;

public class StorageDeleteAdapter<T> extends ArrayAdapter<T>{
	private Context mContext;
	private LayoutInflater mInflater;
	
	//字符串资源
    private Resources rs;
    private String sVersion = "";
    private String sAppSize = "";
    private ScanSdCardUtils ssu;
	public StorageDeleteAdapter(Context context, int textViewResourceId, List<T> list) 
	{
		super(context, textViewResourceId, list);
		mContext = context;
		ssu=new ScanSdCardUtils(mContext);
		mInflater = LayoutInflater.from(mContext);
		
		rs = mContext.getResources();
        sVersion = rs.getString(R.string.version_download_manager);
        sAppSize = rs.getString(R.string.app_size);
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
				if(StorageDeleteAdapter.this.isEmpty())
	        	{
					StorageDeleteAdapter.this.notifyDataSetInvalidated();
	        	}
	        	else
	        	{
	        		StorageDeleteAdapter.this.notifyDataSetChanged();
	        	    
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
        //软件版本
        public TextView app_version;
        //点击安装按钮
        public Button install_button;
        //点击删除按钮
        public Button delete_button;
        //点击项
        public View setting_app_item_lay;
        
        public Object tag;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final SdcardInstalledAppInfo installedInfo = (SdcardInstalledAppInfo)getItem(position);
		ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.sdcard_delete_manager_item, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.sdcard_delete_app_icon);
            holder.appName = (TextView) convertView.findViewById(R.id.sdcard_delete_app_name);
            holder.appSize = (TextView) convertView.findViewById(R.id.sdcard_delete_app_size);
            holder.delete_button=(Button) convertView.findViewById(R.id.sdcard_delete_item_btn2);
            holder.install_button=(Button) convertView.findViewById(R.id.sdcard_install_item_btn1);
            holder.app_version = (TextView) convertView.findViewById(R.id.sdcard_delete_app_version);

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
        
        String size = installedInfo.getAppSize();
        holder.appSize.setText(sAppSize+size);
    
    	holder.app_version.setVisibility(View.VISIBLE);
    	holder.app_version.setText(sVersion+installedInfo.getPackageInfo().versionName);
    	holder.tag = null;
    	holder.delete_button.setText(R.string.delete);
    	holder.install_button.setText(R.string.install);
    	holder.delete_button.setOnClickListener(new MyButtonListener(holder,installedInfo));
    	holder.install_button.setOnClickListener(new MyButtonListener(holder,installedInfo));
		return convertView;
	}
	
	class MyButtonListener implements View.OnClickListener
    {
    	private SdcardInstalledAppInfo installedInfo;
    	private ViewHolder holder;
    	
		public MyButtonListener(ViewHolder holder,SdcardInstalledAppInfo installedInfo) 
		{
			super();
			this.installedInfo = installedInfo;
			this.holder = holder;
		}

		@Override
		public void onClick(View v) 
		{
//			SdcardInstalledAppInfo pinfo = installedInfo;
			String appSavePath=installedInfo.getAppSavePath();
			int id=v.getId();
			if(id==R.id.sdcard_delete_item_btn2){
			   deleteFile(appSavePath,installedInfo);
			}
			else if(id==R.id.sdcard_install_item_btn1){
			
				installApk(appSavePath);
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
     * 删除文件
     * @param fileName
     */
    @SuppressWarnings("unchecked")
	private void deleteFile(String filePath,SdcardInstalledAppInfo pinfo) 
    {
    	if(filePath!=null)
    	{
    		
    		ToolsUtils.deleteFile(mContext, filePath);
    		StorageDeleteAdapter.this.remove((T)pinfo);
    		ArrayList<SdcardInstalledAppInfo>  appArrayList=ssu.myFiles;
    		ArrayList<DownloadInfo> downloadlist=MarketApplication.myDownloadAppList;
    		//Log.d("######","++++lzg downloadlist="+downloadlist.size());
    		for(SdcardInstalledAppInfo sd : appArrayList){
    			if(sd.getAppName().equals(pinfo.getAppName())){
    				ssu.myFiles.remove(sd);
    				break;
    			}
    		}
    		for(DownloadInfo di:downloadlist){
    			//Log.d("#####","++++lzg di.getName()="+di.getName());
    			//Log.d("#####","++++lzg pinfo.getAppName()="+pinfo.getAppName());
    			if(di.getName().equals(pinfo.getAppName())){
    				//Log.d("#####","++++lzg di.getApp_id()="+di.getDownload_id());
    				DaoFactory.getDownloadInfoDao(mContext).delete(di.getDownload_id());
    				break;
    			}
    		}
    		Message msg = new Message();
    		msg.what = 1;
    		mHandler.sendMessage(msg);
    	}
	}
}
