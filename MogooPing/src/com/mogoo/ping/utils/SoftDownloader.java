package com.mogoo.ping.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import com.mogoo.ping.vo.ApkItem;
import static com.mogoo.ping.network.NetworkWorking.*;

import android.content.Context;
import android.net.Uri;
import android.nfc.Tag;
import android.util.Log;
import android.util.Xml;

/**
 * @author Aaron Lee
 * ����Ģ������Ӧ�ó��򻺴�����
 * @Date ����10:11:44  2012-9-17
 */
public class SoftDownloader {
	
	
	public static final String XMLTAP_APKLIST_RESULT = "result";
	public static final String XMLTAP_APKLIST_SESSION= "session";
	public static final String XMLTAP_APKLIST_ERRORCODE = "errorcode";
	public static final String XMLTAP_APKLIST_RC = "rc";
	public static final String XMLTAP_APKLIST_APKID= "id";
	public static final String XMLTAP_APKLIST_APKNAME = "n";
	public static final String XMLTAP_APKLIST_VERSONNAME = "vn";
	public static final String XMLTAP_APKLIST_VERSIONCODE = "v";
	public static final String XMLTAP_APKLIST_PACKAGENAME = "pn";
	public static final String XMLTAP_APKLIST_REMOTE_URL = "ap";
	public static final String XMLTAP_APKLIST_REMOTE_IMG = "icp";
	
	/** 
     * ��ȡͼƬ�����л��湦�� 
     * @param remoteImagePath �����ͼƬ·��
     * @param localSaveDir �����ڱ��ص�ͼƬ·��
     * @return ͼƬURI
     * @throws Exception 
     */    
    public static Uri getImage(String remoteImagePath, String localSaveDir) throws Exception{  
        
        if (!(new File(localSaveDir).exists())) {
        	new File(localSaveDir).mkdirs();
        }
        File imageFile = new File(getLocalFileCache(localSaveDir, remoteImagePath));
        Log.d("aaron", "imageFile "+imageFile);
        if(imageFile.exists()){  
            return Uri.fromFile(imageFile);  
        }
        //����������ȡͼƬ  
        HttpURLConnection conn = (HttpURLConnection) new URL(remoteImagePath).openConnection();  
        conn.setConnectTimeout(NETWORK_CONNECTION_TIMEOUT);  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == 200){
        	if (!Utilities.isCanUseSdCard()) {
        		return null;
        	}
            copyStream(conn.getInputStream(), new FileOutputStream(imageFile));  
            return Uri.fromFile(imageFile);  
        }  
        return null;  
    }
    
    public static String getLocalFileCache(String localSaveDir, String remoteImagePath) {
    	return localSaveDir + MD5.getMD5(remoteImagePath) + remoteImagePath.substring(remoteImagePath.lastIndexOf("."));  
    }
  
    private static void copyStream(InputStream inputStream, OutputStream outStream) throws Exception{  
        byte[] buffer = new byte[1024];  
        int len = 0;  
        while((len = inputStream.read(buffer)) != -1){  
            outStream.write(buffer, 0, len);  
        } 
        inputStream.close();  
        outStream.close();  
    }  
  
    public static List<ApkItem> getImageInfos(String path, int... flag) throws Exception{  
    	Log.d("aaron", "getImageInfos "+path);
    	List<ApkItem> data = null;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();  
        conn.setConnectTimeout(NETWORK_CONNECTION_TIMEOUT);  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == 200){
        	data = parseXML(conn.getInputStream(), flag);  
        }
        conn.disconnect();
        return data;  
    }  
  
    private static List<ApkItem> parseXML(InputStream xml, int... flag) throws Exception{  
    	List<ApkItem> paths = null;  
    	ApkItem item = null;  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(xml, "UTF-8");  
          
        int event = parser.getEventType();
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT: 
            	paths = new ArrayList<ApkItem>();
                break;  
            case XmlPullParser.START_TAG: 
                if(XMLTAP_APKLIST_RC.equals(parser.getName())){
                	item = new ApkItem();
                	if (flag != null && flag.length >0) {
                		item.setType(Utilities.getSoftwareTypeStr(flag[0]));
                	}
                    item.setApkId(parser.getAttributeValue(0));  
                }  
                if(item!=null){  
                    if(XMLTAP_APKLIST_APKNAME.equals(parser.getName())){ 
                    	item.setApkName(parser.nextText());  
                    }else if(XMLTAP_APKLIST_VERSONNAME.equals(parser.getName())){ 
                    	item.setVersionStr(parser.nextText());  
                    }else if(XMLTAP_APKLIST_VERSIONCODE.equals(parser.getName())){
                    	try {
                    		item.setVersionCode(Integer.parseInt(parser.nextText()));  
						} catch (NumberFormatException e) {
							item.setVersionCode(0);
						}
                    }else if(XMLTAP_APKLIST_PACKAGENAME.equals(parser.getName())){ 
                    	item.setPackageName(parser.nextText());  
                    }else if(XMLTAP_APKLIST_REMOTE_URL.equals(parser.getName())){ 
                    	item.setApkAddressRemote(parser.nextText());  
                    }else if(XMLTAP_APKLIST_REMOTE_IMG.equals(parser.getName())){ 
                    	item.setIconRemote(parser.nextText());  
                    }
                }  
                break;  
            case XmlPullParser.END_TAG:
                if(XMLTAP_APKLIST_RC.equals(parser.getName())){ 
                	paths.add(item);
                }  
                break;  
            }  
            event = parser.next();
        }//end while  
        return paths;  
    }

}
