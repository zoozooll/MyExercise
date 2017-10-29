/**
 * 
 */
package com.dvr.android.dvr.file;

import java.util.ArrayList;

import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;

import com.dvr.android.dvr.bean.City;
import com.dvr.android.dvr.bean.Province;

/**
 * XML解析�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 28 Nov 2011]
 */
public class XMLHelper
{
    /**
     * 解析资源文件
     * 
     * @param inStream
     * @return
     */
    public static ArrayList<Province> readXML(Context context, int resId)
    {
        XmlResourceParser parser = context.getResources().getXml(resId);
        ArrayList<Province> citys = new ArrayList<Province>();
        try
        {
            int eventType = parser.getEventType();


            Province province = null;

            while (eventType != XmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    case XmlPullParser.START_DOCUMENT:// 文档�?��事件,可以进行数据初始化处�?
                        break;

                    case XmlPullParser.START_TAG:// �?��元素事件

                        String name = parser.getName();

                        if (name.equalsIgnoreCase(Province.PROVINCE_TAG))
                        {
                            province = new Province();
                            province.name = parser.getAttributeValue(null, "name");
                            citys.add(province);
                        }
                        else if(name.equalsIgnoreCase(Province.CITY_TAG))
                        {
                            City city = new City();
                            city.name = parser.getAttributeValue(null, "name");
                            province.citys.add(city);
                        }
                        break;
                    case XmlPullParser.END_TAG:// 结束元素事件
                          
                        break;
                }
                eventType = parser.next();
            }

            return citys;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
