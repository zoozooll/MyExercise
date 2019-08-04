package com.mogoo.market.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mogoo.market.R;

import com.mogoo.market.model.InstalledAppInfo;
import com.mogoo.market.utils.ToolsUtils;

public class SoftMoveManagerAdapter <T> extends ArrayAdapter<T> 
{
	private static final String SCHEME = "package";
	// 调用系统InstalledAppDetails界面所需Extra名称（用于android 2.1及之前版本）
	private static final String APP_PKG_NAME_21 = "com.android.settings.ApplicationPkgName";
	// 调用系统InstalledAppDetails界面所需Extra名称（用于android 2.2版本）
	private static final String APP_PKG_NAME_22 = "pkg";
	// InstalledAppDetails所在包名
	private static final String APP_DETAILS_PACKAGE_NAME = "com.android.settings";
	// InstalledAppDetails所在类名
	private static final String APP_DETAILS_CLASS_NAME = "com.android.settings.InstalledAppDetails";
	
	
	private Context mContext;
	private LayoutInflater mInflater;
	
	//字符串资源
    private Resources rs;
    private String sVersion = "";
    private String sAppSize = "";
    
    
	public SoftMoveManagerAdapter(Context context, int textViewResourceId, List<T> list) 
	{
		super(context, textViewResourceId, list);
		mContext = context;
		
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
			removeMessages(1);
			if(SoftMoveManagerAdapter.this.isEmpty())
			{
				SoftMoveManagerAdapter.this.notifyDataSetInvalidated();
			}
			else
			{
				SoftMoveManagerAdapter.this.notifyDataSetChanged();
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
        //public TextProgressBar progressbar;
        //public TextView tv_progress;
        public TextView app_version;
        //点击项
        public View setting_app_item_lay;
        
        public Object tag;
    }
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		final InstalledAppInfo installedInfo = (InstalledAppInfo)getItem(position);
		ViewHolder holder = null;
        if(convertView == null)
        {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.more_settings_manager_item, null);
            holder.appIcon = (ImageView) convertView.findViewById(R.id.setting_app_icon);
            holder.appName = (TextView) convertView.findViewById(R.id.setting_app_name);
            holder.appSize = (TextView) convertView.findViewById(R.id.setting_app_size);
            holder.setting_app_item_lay=(View)convertView.findViewById(R.id.seting_app_item_lay);

            holder.app_version = (TextView) convertView.findViewById(R.id.setting_app_version);

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
    
    	holder.app_version.setVisibility(View.VISIBLE);
    	holder.app_version.setText(sVersion+installedInfo.getPackageInfo().versionName);
    	holder.tag = null;
    	holder.setting_app_item_lay.setOnClickListener(new ItemListener(installedInfo));
		return convertView;
	}
	
	/**
	 * 调用系统InstalledAppDetails界面显示已安装应用的详细信息，对于android2.3（Api level 9）
	 * 以上，使用SDK接口；2.3 以下，使用非公开的接口（查看InstalledAppDetails源码）
	 * @param context
	 * @param packageName 应用程序包名
	 */
	private void showInstalledAppDetails(Context context,  String packageName) {
		Intent intent = new Intent();
		final int apiLevel = Build.VERSION.SDK_INT;
		if(apiLevel >= 9) {
			intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
			Uri uri = Uri.fromParts(SCHEME, packageName, null);
			intent.setData(uri);
		}else {
			final String appPkgName = (apiLevel == 8 ? APP_PKG_NAME_22 : APP_PKG_NAME_21);
			intent.setAction(Intent.ACTION_VIEW);
			intent.setClassName(APP_DETAILS_PACKAGE_NAME, APP_DETAILS_CLASS_NAME);
			intent.putExtra(appPkgName, packageName);
		}
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);//如果手机没有返回键，通过home键操作，推出该界面
		context.startActivity(intent);
	}
	
	class ItemListener implements View.OnClickListener{
		private InstalledAppInfo installedInfo;
		public ItemListener(InstalledAppInfo installedInfo){
			this.installedInfo = installedInfo;
		}
		
		@Override
		public void onClick(View v){
			// Log.d("####","ACTION_APPLICATION_DETAILS_SETTINGS");
			// Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS,
			// Uri.fromParts("package",
			// installedInfo.getPackageInfo().packageName, null));
			// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			// // start new activity to display extended information
			// mContext.startActivity(intent);
			showInstalledAppDetails(mContext, installedInfo.getPackageInfo().packageName);
		}
	}
}
