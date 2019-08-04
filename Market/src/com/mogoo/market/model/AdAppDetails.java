package com.mogoo.market.model;

import org.xml.sax.Attributes;

import com.mogoo.parser.XmlResultCallback;

/**
 * 广告点击，单个应用，仅为获取Apkid信息
 */
public class AdAppDetails
{
    private String apkid;
    private String packageName;
    
    static class AppDetailsTag
    {
        /** 节点 */
        public static final String INFO_NODE = "node";
        
        /** apkId */
        public static final String INFO_APK_ID = "id";
        
        /** packageName */
        public static final String INFO_PACKAGE_NAME = "pn";
    }
    
    
    //解析类
    public static class AdAppDetailsCallback extends XmlResultCallback
    {
        private AdAppDetails adAppInfo;
        private StringBuilder mStringBuilder = new StringBuilder();
        
        @Override
        public Object getResult() 
        {
            return adAppInfo;
        }
        
        @Override
        public void startElement(String uri, String localName, String qName,Attributes attributes) 
        {
        	mStringBuilder.setLength(0);
            if(localName.equalsIgnoreCase(AppDetailsTag.INFO_NODE)) 
            { 
            	adAppInfo = new AdAppDetails();
            	
                for(int i=0; i<attributes.getLength(); i++)
                {
                    String iQName = attributes.getQName(i);
                    if(iQName.equalsIgnoreCase(AppDetailsTag.INFO_APK_ID))
                    {
                        String iQNameValue = attributes.getValue(iQName);
                        adAppInfo.setApkid(iQNameValue);
                    }
                }
            }
            super.startElement(uri, localName, qName, attributes);
           
        }

        @Override
        public void endElement(String uri, String localName, String qName) 
        {
        	String data = mStringBuilder.toString();
            
            if(localName.equals(AppDetailsTag.INFO_PACKAGE_NAME)) 
            {
            	adAppInfo.setPackageName(data);
            }
            super.endElement(uri, localName, qName);
        }

        @Override
        public void characters(char[] ch, int start, int length) 
        { 
        	mStringBuilder.append(ch, start, length);

        } 
        
    }


	public String getApkid() {
		return apkid;
	}


	public void setApkid(String apkid) {
		this.apkid = apkid;
	}


	public String getPackageName() {
		return packageName;
	}


	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
    
    

}
