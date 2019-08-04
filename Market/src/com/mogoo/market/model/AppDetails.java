package com.mogoo.market.model;

import java.util.ArrayList;

import org.xml.sax.Attributes;

import android.text.TextUtils;

import com.mogoo.parser.XmlResultCallback;

public class AppDetails
{
    /** 软件图片地址列表 */
    private ArrayList<String> imageList = new ArrayList<String>();
    /** 下载了此应用的用户还下载了  列表*/
    private ArrayList<RecommentAppInfo> recommentAppList = new ArrayList<RecommentAppInfo>();
    
    /** appd */
    private String appId;
    /** 名称 */
    private String name;
    /** 版本号 */
    private String versionCode;
    /** 版本字符串 */
    private String versionStr;
    /** 包名 */
    private String packageame;
    /** 软件大小 */
    private String size;
    /** 评分次数 */
    private String scoreTimes;
    /** 虚拟评分 */
    private String vitualScore;
    /** 真实评分 */
    private String realScore;
    /** 设置星级的评分 */
    private String ratingScore;
    /** 下载次数 */
    private String downloadTime;
    /** 价格 */
    private String price;
    /** 作者 */
    private String author;
    /** apk下载地址 */
    private String apkDownUrl;
    /** 图标 url*/
    private String iconUrl;
    /** 描述 */
    private String description;
    /** 创建时间 */
    private String time;
    
    static class AppDetailsTag
    {
        /** 节点 */
        public static final String OPEN_TYPE = "openType";
        public static final String INFO_NODE = "rc";
        
        /** 名称 */
        public static final String INFO_APK_ID = "id";
        public static final String INFO_NAME = "n";
        public static final String INFO_SCORE_TIMES = "ct"; 
        public static final String INFO_PACKAGE_NAME = "pn";
        public static final String INFO_VC = "v"; 
        public static final String INFO_VS = "vn"; 
        public static final String INFO_SIZE = "s"; 
        public static final String INFO_VITUAL_SCORE = "xc"; 
        public static final String INFO_REAL_SCORE = "zc";
        public static final String INFO_RATING_SCORE = "se"; 
        public static final String INFO_DOWNLOAD_TIME = "dn"; 
        public static final String INFO_PRICE = "p"; 
        public static final String INFO_AUTHOR = "a"; 
        public static final String INFO_APP_DOWNLOAD_URL = "ap"; 
        public static final String INFO_ICON_DOWNLOAD_URL = "icp";
        public static final String INFO_DESC = "de"; 
        public static final String INFO_TIME = "t"; 
        
        /** 软件图片列表 */
        public static final String IMAGE_LIST = "is";
        /** 软件图片列表 */
        public static final String IMAGE_LIST_ICON = "isp";
        
        /** 推荐应用 */
        public static final String RECOMMENT_NODE = "app";
        public static final String RECOMMENT_APP_NODE = "l";
        public static final String RECOMMENT_ID = "lid";
        public static final String RECOMMENT_NAME = "ln";
        public static final String RECOMMENT_ICON_URL = "licp";
        public static final String RECOMMENT_PG_NAME = "lpn";
    }
    
    
    //解析类
    public static class AppDetailsCallback extends XmlResultCallback
    {
        private AppDetails appDetails = new AppDetails();
        //private String openType = ""; 
        private StringBuilder mStringBuilder = new StringBuilder();
        
        private RecommentAppInfo recommentAppInfo = null;
        
        @Override
        public Object getResult() 
        {
            return appDetails;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,Attributes attributes) 
        {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(AppDetailsTag.INFO_NODE)) 
            { 
                for(int i=0; i<attributes.getLength(); i++)
                {
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(AppDetailsTag.INFO_APK_ID))
                    {
                        String iQNameValue = attributes.getValue(iQName);
                        appDetails = new AppDetails();
                        appDetails.setAppId(iQNameValue);
                        //appDetails.setOpenType(openType);
                    }
                }
            }
//            else if(localName.equalsIgnoreCase(AppDetailsTag.IMAGE_LIST)) 
//            { 
//                appDetails.setImageList(new ArrayList<String>());
//            }
//            else if(localName.equalsIgnoreCase(AppDetailsTag.RECOMMENT_NODE)) 
//            { 
//                appDetails.setRecommentAppList(new ArrayList<RecommentAppInfo>());
//            }
            else if(localName.equalsIgnoreCase(AppDetailsTag.RECOMMENT_APP_NODE)) 
            { 
            	recommentAppInfo = new RecommentAppInfo();
            }
            
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) 
        {
            String data = mStringBuilder.toString();
            
            if(localName.equals(AppDetailsTag.INFO_APK_ID)) 
            {
                appDetails.setAppId(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_NAME)) 
            {
                appDetails.setName(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_SCORE_TIMES)) 
            {
                appDetails.setScoreTimes(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_PACKAGE_NAME)) 
            {
                appDetails.setPackageame(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_VC)) 
            {
                appDetails.setVersionCode(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_VS)) 
            {
                appDetails.setVersionStr(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_SIZE)) 
            {
                appDetails.setSize(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_VITUAL_SCORE))
            {
                appDetails.setVitualScore(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_REAL_SCORE)) 
            {
                appDetails.setRealScore(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_RATING_SCORE)) 
            {
                appDetails.setRatingScore(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_DOWNLOAD_TIME)) 
            {
                appDetails.setDownloadTime(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_PRICE)) 
            {
                appDetails.setPrice(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_AUTHOR)) 
            {
                appDetails.setAuthor(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_APP_DOWNLOAD_URL)) 
            {
                appDetails.setApkDownUrl(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_ICON_DOWNLOAD_URL)) 
            {
                appDetails.setIconUrl(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_DESC)) 
            {
                appDetails.setDescription(data);
            }
            else if(localName.equals(AppDetailsTag.INFO_TIME)) 
            {
                appDetails.setTime(data.substring(0, data.indexOf(" ")));
            }
            
            else if(localName.equals(AppDetailsTag.IMAGE_LIST_ICON)) 
            {
                appDetails.getImageList().add(data);
            }
            
            else if(localName.equals(AppDetailsTag.RECOMMENT_ID)) 
            {
                recommentAppInfo.setId(data);
            }
            else if(localName.equals(AppDetailsTag.RECOMMENT_NAME)) 
            {
            	recommentAppInfo.setName(data);
            }
            else if(localName.equals(AppDetailsTag.RECOMMENT_ICON_URL)) 
            {
            	recommentAppInfo.setIconUrl(data);
            }
            else if(localName.equals(AppDetailsTag.RECOMMENT_PG_NAME)){
            	recommentAppInfo.setPackageName(data);
            }
            if(localName.equalsIgnoreCase(AppDetailsTag.RECOMMENT_APP_NODE))
            {
            	appDetails.getRecommentAppList().add(recommentAppInfo);
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


	public ArrayList<String> getImageList() {
		return imageList;
	}


	public void setImageList(ArrayList<String> imageList) {
		this.imageList = imageList;
	}


	public ArrayList<RecommentAppInfo> getRecommentAppList() {
		return recommentAppList;
	}


	public void setRecommentAppList(ArrayList<RecommentAppInfo> recommentAppList) {
		this.recommentAppList = recommentAppList;
	}


	public String getAppId() {
		return appId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}
	
	public String getPackageame() {
		return packageame;
	}


	public void setPackageame(String packageame) {
		this.packageame = packageame;
	}


	public String getVersionCode() {
		if (versionCode == null || TextUtils.isEmpty(versionCode)
				|| versionCode.equals("null")) {
			return "0";
		}
		return versionCode;
	}


	public void setVersionCode(String versionCode) {
		this.versionCode = versionCode;
	}


	public String getVersionStr() {
		return versionStr;
	}


	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}


	public String getSize() {
		if (size == null || TextUtils.isEmpty(size) || size.equals("null")) {
			return "0";
		}
		return size;
	}


	public void setSize(String size) {
		this.size = size;
	}


	public String getScoreTimes() {
		// 返回vitualScore(虚拟评分) + realScore(真实评分);
		float virtualScoreInt = (vitualScore == null || TextUtils.isEmpty(vitualScore)) ? 0.0f : Float.parseFloat(vitualScore);
		float realScoreInt = (realScore == null || TextUtils.isEmpty(realScore)) ? 0.0f : Float.parseFloat(realScore);
		
		float result = virtualScoreInt + realScoreInt;
		return String.valueOf((int) result);
		// return scoreTimes;
	}


	public void setScoreTimes(String scoreTimes) {
		this.scoreTimes = scoreTimes;
	}


	public String getVitualScore() {
		return vitualScore;
	}


	public void setVitualScore(String vitualScore) {
		this.vitualScore = vitualScore;
	}


	public String getRealScore() {
		return realScore;
	}


	public void setRealScore(String realScore) {
		this.realScore = realScore;
	}


	public String getDownloadTime() {
		return downloadTime;
	}


	public void setDownloadTime(String downloadTime) {
		this.downloadTime = downloadTime;
	}


	public String getPrice() {
		return price;
	}


	public void setPrice(String price) {
		this.price = price;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getApkDownUrl() {
		return apkDownUrl;
	}


	public void setApkDownUrl(String apkDownUrl) {
		this.apkDownUrl = apkDownUrl;
	}


	public String getIconUrl() {
		return iconUrl;
	}


	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}


	public String getDescription() {
		return description;
	}


	public void setDescription(String description) {
		this.description = description;
	}


	public String getTime() {
		return time;
	}


	public void setTime(String time) {
		this.time = time;
	}


	public String getRatingScore() {
		// vitualScore(虚拟评分) + realScore(真实评分) <= 100,返回四颗星;
		// 其他情况返回五颗星.
		float virtualScoreInt = (vitualScore == null || TextUtils.isEmpty(vitualScore)) ? 0.0f : Float.parseFloat(vitualScore);
		float realScoreInt = (realScore == null || TextUtils.isEmpty(realScore)) ? 0.0f : Float.parseFloat(realScore);
		if(virtualScoreInt + realScoreInt <= 100.0F) {
			return "4.0";
		}else {
			return "5.0";
		}
		// return ratingScore;
	}


	public void setRatingScore(String ratingScore) {
		this.ratingScore = ratingScore;
	}
    
    

}
