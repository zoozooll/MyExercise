package com.mogoo.market.model;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.mogoo.market.utils.ToolsUtils;

/**
 * PackageInfo 和 AppUpdatesInfo（软件更新信息） 的封装类，以便直接通过此类判断软件是否有更新
 */
public class InstalledAppInfo 
{
	/** 包信息 */
    private PackageInfo packageInfo;
	/** 上下文 */
    private Context context;
    /** 更新信息 */
    private AppUpdatesInfo updateInfo = null;
    
    private PackageManager pm;
    private ApplicationInfo appInfo;
    
	public InstalledAppInfo(Context context, PackageInfo packageInfo) 
	{
        this.context = context;
        this.packageInfo = packageInfo;
        pm = context.getPackageManager();
        appInfo = packageInfo.applicationInfo;
    }
	
	/**
     * 获取图标
     */
    public Drawable getAppIcon() 
    {
        ApplicationInfo appInfo = packageInfo.applicationInfo;
        return pm.getApplicationIcon(appInfo);
    }
    
    /**
     * 获取名称
     */
    public String getAppName() 
    {
        CharSequence appLabel = null;
        appLabel = pm.getApplicationLabel(appInfo);
        return appLabel+"";
    }
    
    /**
     * 获取总大小
     */
    public String getSize() 
    {
    	String size = ToolsUtils.getSizeStr(""+new File(packageInfo.applicationInfo.publicSourceDir).length());
    	return size+"";
	}
    
    /**
     * 获取总字节大小bytes
     */
    public String getSizebytes() 
    {
    	return new File(packageInfo.applicationInfo.publicSourceDir).length()+"";
	}

	public PackageInfo getPackageInfo() {
		return packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	public AppUpdatesInfo getUpdateInfo() {
		return updateInfo;
	}

	public void setUpdateInfo(AppUpdatesInfo updateInfo) {
		this.updateInfo = updateInfo;
	}

	public ApplicationInfo getAppInfo() {
		return appInfo;
	}

	public void setAppInfo(ApplicationInfo appInfo) {
		this.appInfo = appInfo;
	}
    
    
}
