package com.mogoo.market.model;

import com.mogoo.market.network.IBEManager;
import com.mogoo.market.network.http.HttpUrls;

import org.xml.sax.Attributes;

import com.mogoo.parser.XmlResultCallback;

import java.util.ArrayList;
/**
 * @see 软件信息
 * @author LHW
 *
 */
public class AppInfo {
    
    
    /** 类型：应用 */
    public static String TYPE = "app"; 
    
    /** 打开类型, 0列表； 1详情 */
    private String openType = ""; 
    
    /** apk id*/
    private String apkId;     
    /** 名称 */
    private String name;
    /** 作者 */
    private String author;
    /** 图标 url*/
    private String iconUrl;
    /** 价格 */
    private String price;
    /** 评分 */
    private String score;
    /** 评论数 */
    private String comNum;
    /** 简介 */
    private String details;
    /** 包名 */
    private String packageName;
    /** 版本号 */
    private int versionCode;
    /** 版本字符串 */
    private String versionStr;
    /** 软件大小 */
    private int apkSize;
    /** 下载地址 */
    private String apkAddress;
 
    static class AppInfoTag {
        /** 节点 */
        public static final String NODE = "node";
        /** apk id*/
        public static final String APK_ID = "id";     
        /** 名称 */
        public static final String NAME = "n"; 
        /** 作者 */
        public static final String AUTHOR = "a"; 
        /** 图标 */
        public static final String ICON_URL = "i"; 
        /** 价格 */
        public static final String PRICE = "p"; 
        /** 评分 */
        public static final String SCORE = "r"; 
        /** 评论数 */
        public static final String COM_NUM = "c"; 
        /** 简介 */
        public static final String DETAILS = "l"; 
        /** 包名 */
        public static final String PACKAGE_NAME = "pn";
        /** 版本号 */
        public static final String VERSION_CODE = "vc";
        /** 版本字符串 */
        public static final String VERSION_STR = "vs";
        /** 软件大小 */
        public static final String APK_SIZE = "sz";
        /** 下载地址 */
        public static final String APK_ADDRESS = "apk";
    }
    

	/**
     * 解析类
     * @author luo
     */
    public static class AppInfoListCallback extends XmlResultCallback {
        private AppInfo appInfo ;
        public ArrayList<AppInfo> appInfoList = new ArrayList<AppInfo>();
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() {
            
            return appInfoList;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,
                Attributes attributes) {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(AppInfoTag.NODE)) { 
                for(int i=0; i<attributes.getLength(); i++){
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(AppInfoTag.APK_ID)){
                        String iQNameValue = attributes.getValue(iQName);
                        appInfo = new AppInfo();
                        appInfo.setApkId(iQNameValue);
                    }
                }
            }
            super.startElement(uri, localName, qName, attributes);
        }

        @Override
        public void endElement(String uri, String localName, String qName) {
            String data = mStringBuilder.toString();
            
            if(localName.equals(AppInfoTag.NAME)) {
                appInfo.name = data;
            }else if(localName.equals(AppInfoTag.AUTHOR)) {
                appInfo.author = data;
            }else if(localName.equals(AppInfoTag.ICON_URL)) {
            	if(data !=null && !data.startsWith("http://")){
            		data = IBEManager.getInstance().getMasServer() + "/OSS" + data;
            	}
                appInfo.iconUrl = (data);
            }else if(localName.equals(AppInfoTag.PRICE)) {
                appInfo.price = data;
            }else if(localName.equals(AppInfoTag.SCORE)) {
                appInfo.score = data;
            }else if(localName.equals(AppInfoTag.COM_NUM)) {
                appInfo.comNum = data;
            }else if(localName.equals(AppInfoTag.DETAILS)) {
                appInfo.details = data;
            }else if(localName.equals(AppInfoTag.PACKAGE_NAME)) {
                appInfo.packageName = data;
            }else if(localName.equals(AppInfoTag.VERSION_CODE)) {
                // 应用大小可能超出integer范围或者为null，默认为0.
                try {
                	appInfo.versionCode = Integer.parseInt(data);
				}catch (NumberFormatException e) {
					appInfo.versionCode = 0;
				}
            }else if(localName.equals(AppInfoTag.VERSION_STR)) {
                appInfo.versionStr = data;
            }else if(localName.equals(AppInfoTag.APK_SIZE)) {
            	// 应用大小可能超出integer范围或者为null，默认为0.
                try {
                	appInfo.apkSize = Integer.parseInt(data);
				}catch (NumberFormatException e) {
					appInfo.apkSize = 0;
				}
            }else if(localName.equals(AppInfoTag.APK_ADDRESS)) {
                appInfo.apkAddress = data;
            }
            if(localName.equalsIgnoreCase(AppInfoTag.NODE)){
                appInfoList.add(appInfo);
                appInfo = null;
            } 
            
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) { 
            super.characters(ch, start, length);
            mStringBuilder.append(ch, start, length);
        }     
    }
    
    //属性赋值
    public AppInfo setCharacters(AppInfo appInfo, String localName, String data) { 
        
        if(localName.equals(AppInfoTag.APK_ID)) {
            appInfo.apkId = data;
        }else if(localName.equals(AppInfoTag.NAME)) {
            appInfo.name = data;
        }else if(localName.equals(AppInfoTag.AUTHOR)) {
            appInfo.author = data;
        }else if(localName.equals(AppInfoTag.ICON_URL)) {
            appInfo.iconUrl = (data);
        }else if(localName.equals(AppInfoTag.PRICE)) {
            appInfo.price = data;
        }else if(localName.equals(AppInfoTag.SCORE)) {
            appInfo.score = data;
        }else if(localName.equals(AppInfoTag.COM_NUM)) {
            appInfo.comNum = data;
        }else if(localName.equals(AppInfoTag.DETAILS)) {
            appInfo.details = data;
        }
        
        return appInfo;
    }
    
    
    public String getOpenType() {
        return openType;
    }


    public void setOpenType(String openType) {
        this.openType = openType;
    }


    public static String getTYPE() {
        return TYPE;
    }

    public static void setTYPE(String tYPE) {
        TYPE = tYPE;
    }

    public String getApkId() {
        return apkId;
    }

    public void setApkId(String apkId) {
        this.apkId = apkId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getComNum() {
        return comNum;
    }

    public void setComNum(String comNum) {
        this.comNum = comNum;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}


	public int getVersionCode() {
		return versionCode;
	}


	public void setVersionCode(int versionCode) {
		this.versionCode = versionCode;
	}


	public int getApkSize() {
		return apkSize;
	}


	public void setApkSize(int apkSize) {
		this.apkSize = apkSize;
	}


	public String getApkAddress() {
		return apkAddress;
	}


	public void setApkAddress(String apkAddress) {
		this.apkAddress = apkAddress;
	}


	public String getVersionStr() {
		return versionStr;
	}


	public void setVersionStr(String versionStr) {
		this.versionStr = versionStr;
	}
    
}