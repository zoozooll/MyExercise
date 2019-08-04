package com.mogoo.market.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import com.mogoo.market.network.IBEManager;
import com.mogoo.parser.XmlResultCallback;

/**
 * @author csq -- 软件更新信息
 */
public class AppUpdatesInfo 
{
    /** 应用id */
    private String apkId = "";
    
    /** 应用名 */
    private String appName = "";
    
    /** 包名 */
    private String packageame = "";
    
    /** 版本 */
    private String appVersionName = "";
    
    /** 版本 */
    private String appVersionCode = "";
    
    /** 更新下载地址 */
    private String updateUrl = "";
    
    /** 更新描述 */
    private String description = "";
    
    /** 图标 url*/
    private String iconUrl = "";
    
    static class AppUpdatesInfoTag 
    {
        /** 节点 */
        public static final String NODE = "node";
        /** 节点id */
        public static final String NODE_ID = "id";
        /** 应用名 */
        public static final String APPNAME = "an"; 
        /** 包名 */
        public static final String PACKAGE = "p"; 
        /** 版本 */
        public static final String VERSION_NAME = "vs";
        /** 版本-数字 */
        public static final String VERSION_CODE = "v"; 
        /** 更新下载地址 */
        public static final String URL = "l"; 
        /** 更新描述 */
        public static final String DESCRIPTION = "s"; 
        /** 图标 */
        public static final String ICON_URL = "i"; 
    }
    
    /**
     * 解析类
     */
    public static class AppUpdatesCallback extends XmlResultCallback 
    {
        private AppUpdatesInfo appUpdatesInfo ;
        public ArrayList<AppUpdatesInfo> appUpdatesInfoList = new ArrayList<AppUpdatesInfo>();
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() 
        {
            return appUpdatesInfoList;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) 
        {
            mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(AppUpdatesInfoTag.NODE)) 
            { 
                appUpdatesInfo = new AppUpdatesInfo();
                for(int i=0; i<attributes.getLength(); i++)
                {
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(AppUpdatesInfoTag.NODE_ID))
                    {
                        String iQNameValue = attributes.getValue(iQName);
                        appUpdatesInfo.setApkId(iQNameValue);
                    }
                }
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) 
        {
            String data = mStringBuilder.toString();
            
            if(localName.equals(AppUpdatesInfoTag.PACKAGE)) 
            {
                appUpdatesInfo.packageame = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.APPNAME)) 
            {
                appUpdatesInfo.appName = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.VERSION_NAME)) 
            {
                appUpdatesInfo.appVersionName = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.VERSION_CODE)) 
            {
                appUpdatesInfo.appVersionCode = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.URL)) 
            {
                if(data !=null && !data.startsWith("http://"))
                {
                    data = IBEManager.getInstance().getMasServer() + "/OSS" + data;
                }
                appUpdatesInfo.updateUrl = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.DESCRIPTION)) 
            {
                appUpdatesInfo.description = data;
            }
            else if(localName.equals(AppUpdatesInfoTag.ICON_URL)) 
            {
                appUpdatesInfo.iconUrl = data;
            }
            if(localName.equalsIgnoreCase(AppUpdatesInfoTag.NODE)){
                appUpdatesInfoList.add(appUpdatesInfo);
                appUpdatesInfo = null;
            } 
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) 
        { 
            super.characters(ch, start, length);
            mStringBuilder.append(ch, start, length);
        }     
    }
    
    /**
     * 属性赋值
     */
    public AppUpdatesInfo setCharacters(AppUpdatesInfo appUpdatesInfo, String localName, String data) 
    { 
        if(localName.equals(AppUpdatesInfoTag.PACKAGE)) {
            appUpdatesInfo.packageame = data;
        }else if(localName.equals(AppUpdatesInfoTag.APPNAME)) {
            appUpdatesInfo.appName = data;
        }else if(localName.equals(AppUpdatesInfoTag.VERSION_NAME)) {
            appUpdatesInfo.appVersionName = data;
        }else if(localName.equals(AppUpdatesInfoTag.VERSION_CODE)) {
            appUpdatesInfo.appVersionCode = data;
        }else if(localName.equals(AppUpdatesInfoTag.URL)) {
            appUpdatesInfo.updateUrl = data;
        }else if(localName.equals(AppUpdatesInfoTag.DESCRIPTION)) {
            appUpdatesInfo.description = data;
        } else if(localName.equals(AppUpdatesInfoTag.ICON_URL)) {
            appUpdatesInfo.iconUrl = data;
        }
        return appUpdatesInfo;
    }
    
    
    
    public String getApkId() {
        return apkId;
    }

    public void setApkId(String apkId) {
        this.apkId = apkId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getPackageame() {
        return packageame;
    }

    public void setPackageame(String packageame) {
        this.packageame = packageame;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersion(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public String getUpdateUrl() {
        return updateUrl;
    }
    
    public void setAppVersionCode(String appVersionCode) {
        this.appVersionCode = appVersionCode;
    }
    
    public String getAppVersionCode() {
        return appVersionCode;
    }

    public void setUpdateUrl(String updateUrl) {
        this.updateUrl = updateUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    
    public String getIconUrl() {
        return iconUrl;
    }
    
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }
    
    @Override
    public String toString() 
    {
        // TODO Auto-generated method stub
        return "AppUpdatesInfo = "+" (packageame)"+packageame+" (appVersionName)"+appVersionName
                +" (appVersionCode)"+appVersionCode+" (updateUrl)"+updateUrl;
    }
}
