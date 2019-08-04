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
public class InstallousAppInfo 
{
	/** 包信息 */
    private PackageInfo packageInfo;
	/** 上下文 */
    private Context context;
    
    private PackageManager pm;
    private ApplicationInfo appInfo;
    private Drawable appIcon;
    private String appName;
    private String appSize;
    private String appVersionName;
    private String appVersionCode;
    private String appSavePath;
    private String packageName;
    private int appType;
    
	public InstallousAppInfo(Context context, PackageInfo packageInfo) 
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
       return this.appIcon;
    }
    
    /**
     * 获取名称
     */
    public String getAppName() 
    {
        return this.appName;
    }
    
    /**
     * 获取总大小
     */
    public String getAppSize() 
    {
    	String appSize = ToolsUtils.getSizeStr(""+new File(packageInfo.applicationInfo.publicSourceDir).length());
    	return appSize+"";
	}
    
    /**
     * 获取总字节大小bytes
     */
    public String getSizebytes() 
    {
    	return new File(packageInfo.applicationInfo.publicSourceDir).length()+"";
	}

    public String getPackageName() 
    {
    	return packageName;
	}
    
	public PackageInfo getPackageInfo() {
		return this.packageInfo;
	}

	public void setPackageInfo(PackageInfo packageInfo) {
		this.packageInfo = packageInfo;
	}

	public ApplicationInfo getAppInfo() {
		return appInfo;
	}
	
	public String getAppSavePath(){
		return appSavePath;
	}

	public void setAppInfo(ApplicationInfo appInfo) {
		this.appInfo = appInfo;
	}
    
	/**
     * 设置图标
    */
    public void setAppIcon(Drawable Icon){
    	this.appIcon=Icon;
    }
    
    /**
     * 设置应用名称
    */
    public void setAppName(String appName){
    	this.appName=appName;
    }
    
    /**
     * 设置应用大小
    */
    public void setAppSize(Long appSize){
    	this.appSize=String.valueOf(appSize);
    }
    
    /**
     * 设置应用版本名
    */
    public void setAppVersionName(String appVersionName){
    	this.appVersionName=appVersionName;
    }
    
    /**
     * 设置应用版本码
    */
    public void setAppVersionCode(int appVersionCode){
    	this.appVersionCode=String.valueOf(appVersionCode);
    }
    
    /**
     * 设置应用路径
    */
    public void setAppSavePath(String appSavePath){
    	this.appSavePath=appSavePath;
    }
    
    /**
     * 设置应用包名
    */
    public void setAppPackageName(String packageName){
    	this.packageName=packageName;
    }
    
    /**
     * 设置应用类型
    */
    public void setAppType(int appType){
    	this.appType=appType;
    }
}
