/**
 * 
 */
package com.dvr.android.dvr.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlSerializer;
import android.util.Log;
import android.util.Xml;

import com.dvr.android.dvr.bean.DrivePath;
import com.dvr.android.dvr.bean.DrivePosition;

/**
 * 将路劲写成xml文件,也从XML中读取路�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 20 Feb 2012]
 */
public class PathHelper
{
    /**
     * 编码
     */
    private static final String ENCODING = "UTF-8";
    private static final String PATH_TAG = "path";
    private static final String INFO_TAG = "info";
    private static final String ATTRIBUTE_TIME = "time";
    private static final String ATTRIBUTE_SPEED = "speed";
    private static final String ATTRIBUTE_LONGITUDE = "longitude";
    private static final String ATTRIBUTE_LATITUDE = "latitude";
    //private static final String ATTRIBUTE_FILE = "file";
    private static final String ATTRIBUTE_OFFSETTIMT = "offsettime";
    private static final String ATTRIBUTE_VALID = "valid";
    private static final String VALUE_TRUE = "1";
    private static final String VALUE_FALSE = "0";
    public static final String XML_SUFFIX = ".xml";
    
    public static String getFilePath(String filename) {    
        if ((filename != null) && (filename.length() > 0)) {    
            int dot = filename.lastIndexOf('/');    
            if ((dot >-1) && (dot < (filename.length() - 1))) {    
                return filename.substring(0, dot);    
            }    
        }    
        return filename;    
    } 

    /**
     * 将记录文件写入xml
     * 
     * @param path
     * @param fileName
     */
    public static void writePath(DrivePath path, String fileName)
    {
        String xmlFileName = fileName + XML_SUFFIX;
        String filePath = getFilePath(fileName);
        
        File file = new File(filePath + "/");
		if(!file.exists()) 
		{
	    	Log.e("PathHelper", "***writePath the path not exist  "+filePath);
			return;
		}
		
//        path.setFilePath(fileName);
        Log.d("TAG", "wirte size " + path.getPathData().size());
        FileOutputStream fileOut = null;
        try
        {
            fileOut = new FileOutputStream(xmlFileName);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(fileOut, ENCODING);
            serializer.startDocument(ENCODING,false);
            serializer.startTag(null, PATH_TAG);
//            serializer.attribute(null, ATTRIBUTE_FILE, path.getFilePath());

            for (DrivePosition position : path.getPathData())
            {
                serializer.startTag(null, INFO_TAG);
                // <info>
                // 如果GPS还没有获取，这里的数据应该是无效的，无效情况下只记录时间与偏�?                
                if (position.mValid)
                {
                    serializer.attribute(null, ATTRIBUTE_SPEED, String.valueOf(position.mSpeed));
                    serializer.attribute(null, ATTRIBUTE_LONGITUDE, String.valueOf(position.mLongitude));
                    serializer.attribute(null, ATTRIBUTE_LATITUDE, String.valueOf(position.mLatitude));
                    serializer.attribute(null, ATTRIBUTE_VALID, VALUE_TRUE);
                }
                else
                {
                    serializer.attribute(null, ATTRIBUTE_VALID, VALUE_FALSE);
                }

                serializer.attribute(null, ATTRIBUTE_TIME, String.valueOf(position.mTime));
                serializer.attribute(null, ATTRIBUTE_OFFSETTIMT, String.valueOf(position.mDelta));
                serializer.endTag(null, INFO_TAG);
            }
            serializer.endTag(null, PATH_TAG);
            serializer.flush();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            // finally close stream
            if (null != fileOut)
            {
                try
                {
                    fileOut.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }

            fileOut = null;
        }
    }

    /**
     * 从xml文件中，读取路径信息
     * 
     * @param filePath
     * @return
     */
    public static DrivePath readPathFromXML(String filePath)
    {
        Log.d("TAG", "load file path = " + filePath);
        FileInputStream fis = null;
        DrivePath path = null;
        try
        {
            fis = new FileInputStream(filePath);
            XmlPullParser parser = Xml.newPullParser();
            parser.setInput(fis, ENCODING);
            int eventCode = parser.getEventType();
            DrivePosition position = null;
            while (XmlPullParser.END_DOCUMENT != eventCode)
            {
                switch (eventCode)
                {
                    case XmlPullParser.START_DOCUMENT:
                        path = new DrivePath();
//                        path.setFilePath(parser.getAttributeValue(null, ATTRIBUTE_FILE));
                        break;
                    case XmlPullParser.START_TAG:
                        if (INFO_TAG.equals(parser.getName()))
                        {
                            position = new DrivePosition();
                            boolean valid =
                                VALUE_TRUE.equals(parser.getAttributeValue(null, ATTRIBUTE_VALID)) ? true
                                    : false;
                            position.mValid = valid;
                            // 如果GPS数据有效才读取GPS数据
                            if (valid)
                            {
                                position.mSpeed =
                                    Float.parseFloat(parser.getAttributeValue(null, ATTRIBUTE_SPEED));
                                position.mLongitude =
                                    Double.parseDouble(parser.getAttributeValue(null, ATTRIBUTE_LONGITUDE));
                                position.mLatitude =
                                    Double.parseDouble(parser.getAttributeValue(null, ATTRIBUTE_LATITUDE));
                            }
                            position.mDelta =
                                Long.parseLong(parser.getAttributeValue(null, ATTRIBUTE_OFFSETTIMT));
                            position.mTime = Long.parseLong(parser.getAttributeValue(null, ATTRIBUTE_TIME));
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        if (INFO_TAG.equals(parser.getName()))
                        {
                            path.recordPosition(position);
                        }
                        break;
                }
                eventCode = parser.next();
            }
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
            return null;
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
        finally
        {
            if (null != fis)
            {
                try
                {
                    fis.close();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            fis = null;
        }
        return path;
    }
}
