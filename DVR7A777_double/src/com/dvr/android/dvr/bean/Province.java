/**
 * 
 */
package com.dvr.android.dvr.bean;

import java.util.ArrayList;

/**
 * 表示省份<BR>
 * [功能详细描述]
 * @author sunshine
 * @version [yecon Android_Platform, 7 Mar 2012] 
 */
public class Province extends City
{
   public static final String PROVINCE_TAG = "province";
   
   public static final String CITY_TAG = "city";
    
   public ArrayList<City> citys = new ArrayList<City>();
}
