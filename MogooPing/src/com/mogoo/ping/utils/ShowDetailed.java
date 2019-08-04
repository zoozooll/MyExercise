/**
 * 
 */
package com.mogoo.ping.utils;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;

import android.util.Log;
import android.util.Xml;

import com.mogoo.ping.vo.ApkItem;
import com.mogoo.ping.vo.MessageBody;
import static com.mogoo.ping.network.NetworkWorking.*;

/**
 * @author Aaron Lee
 * @Date 下午5:36:12  2012-10-10
 */
public class ShowDetailed {
	
	private static final String XMLTAP_ROOT = "result";
	private static final String XMLTAP_DETAILED_SESSION = "session";
	private static final String XMLTAP_DETAILED_ERRORCODE = "errorcode";
	private static final String XMLTAP_DETAILED_NODE = "node";
	private static final String XMLTAP_DETAILED_NODE_ID = "id";
	private static final String XMLTAP_DETAILED_NAME= "n";
	private static final String XMLTAP_DETAILED_A = "a";
	private static final String XMLTAP_DETAILED_ICON ="i";
	private static final String XMLTAP_DETAILED_P = "p";
	private static final String XMLTAP_DETAILED_R = "r";
	private static final String XMLTAP_DETAILED_C = "c";
	private static final String XMLTAP_DETAILED_L = "l";
	private static final String XMLTAP_DETAILED_D = "d";
	private static final String XMLTAP_DETAILED_IMAGES = "is";
	private static final String XMLTAP_DETAILED_IMAGES_SHOW = "si";
	private static final String XMLTAP_DETAILED_RD = "rd";
	private static final String XMLTAP_DETAILED_VERSIONCODE = "v";
	private static final String XMLTAP_DETAILED_SIZE = "s";
	private static final String XMLTAP_DETAILED_CL = "c_l";
	private static final String XMLTAP_DETAILED_RL = "r_l";
	private static final String XMLTAP_DETAILED_CMP = "cm_p";
	private static final String XMLTAP_DETAILED_BUGP = "bug_p";
	private static final String XMLTAP_DETAILED_APK = "apk";
	private static final String XMLTAP_DETAILED_PACKAGENAME = "pn";
	private static final String XMLTAP_DETAILED_VERSIONSTR = "vs";
	
	/**
	 * 
	 * @Author lizuokang
	 * @param path
	 * @return
	 * @throws Exception
	 * @Date 下午7:11:48  2012-10-10
	 */
	public static MessageBody getDetailedInfos(String path) throws Exception{  
    	Log.d("aaron", "getDetailedInfos "+path);
    	MessageBody data = null;
        HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();  
        conn.setConnectTimeout(NETWORK_CONNECTION_TIMEOUT);  
        conn.setRequestMethod("GET");  
        if(conn.getResponseCode() == 200){  
        	data = parseXML(conn.getInputStream());  
        }
        conn.disconnect();
        return data;  
    } 

	private static MessageBody parseXML(InputStream xml) throws Exception{  
        XmlPullParser parser = Xml.newPullParser();  
        parser.setInput(xml, "UTF-8");  
        MessageBody body = null;
        List<String> showImages = null;
        int event = parser.getEventType();
        while(event!=XmlPullParser.END_DOCUMENT){  
            switch(event){  
            case XmlPullParser.START_DOCUMENT:
            	
                break;  
            case XmlPullParser.START_TAG:
                if(XMLTAP_DETAILED_NODE.equals(parser.getName())){
                	body = new MessageBody();
                }  
                if(body!=null){  
                    if(XMLTAP_DETAILED_NAME.equals(parser.getName())){ 
                    	body.setmAppName(parser.nextText());
                    	//body.set(parser.getAttributeValue(0));
                    }else if(XMLTAP_DETAILED_A.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_ICON.equals(parser.getName())){ 
                    	body.setmAppImage(parser.nextText());
                    }else if(XMLTAP_DETAILED_P.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_R.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_C.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_L.equals(parser.getName())){ 
                    	body.setmAppDescribe(parser.nextText());
                    }else if(XMLTAP_DETAILED_D.equals(parser.getName())){ 
                    	if (body.getmAppDescribe() == null || "".equals(body.getmAppDescribe())) {
                    		body.setmAppDescribe(parser.nextText());
                    	}
                    }else if(XMLTAP_DETAILED_IMAGES.equals(parser.getName())){ 
                    	showImages = new ArrayList<String>();
                    }else if(XMLTAP_DETAILED_IMAGES_SHOW.equals(parser.getName())){ 
                    	showImages.add(parser.nextText());
                    }else if(XMLTAP_DETAILED_RD.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_VERSIONCODE.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_SIZE.equals(parser.getName())){ 
                    	body.setmSize(parser.nextText());
                    }else if(XMLTAP_DETAILED_CL.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_RL.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_CMP.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_BUGP.equals(parser.getName())){ 
                    	
                    }else if(XMLTAP_DETAILED_APK.equals(parser.getName())){ 
                    	body.setmDownUrl(parser.nextText());
                    }else if(XMLTAP_DETAILED_PACKAGENAME.equals(parser.getName())){ 
                    	body.setPackageName(parser.nextText());
                    }else if(XMLTAP_DETAILED_VERSIONSTR.equals(parser.getName())){ 
                    	body.setmVerName(parser.nextText());
                    }
                }  
                break;  
            case XmlPullParser.END_TAG:
                if(XMLTAP_DETAILED_NODE.equals(parser.getName())){
                	
                }else if (XMLTAP_DETAILED_IMAGES.equals(parser.getName())) {
                	if (body != null) {
                		body.setmPreviewLists(showImages);
                	}
                }
                break;  
            }  
            event = parser.next(); 
        }//end while  
        return body;  
    } 
}
