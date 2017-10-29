/**
 * 
 */
package com.dvr.android.dvr.util;

import java.text.NumberFormat;

/**
 * 字符工具<BR>
 * [功能详细描述]
 * @author sunshine
 * @version [yecon Android_Platform, 27 Mar 2012] 
 */
public class StringUtil
{
    private static final String E = " E";
    private static final String W = " W";
    private static final String N = " N";
    private static final String S = " S";
    
    /**
     * 转换经纬�?     * @param laction 数�?
     * @param latitude 是否是经�?true经度 false纬度
     * @return
     */
   public static String loactionToString(double laction , boolean latitude)
   {
       NumberFormat bnf = NumberFormat.getInstance();
       bnf.setMinimumFractionDigits(2);
       bnf.setMaximumFractionDigits(2);
       if(latitude)
       {
           if(laction >= 0)
           {
              return  bnf.format(laction) + E;
           }
           else
           {
               return bnf.format(Math.abs(laction)) + W;
           }
       }
       else
       {
           if(laction >= 0)
           {
              return  bnf.format(laction) + N;
           }
           else
           {
               return bnf.format(Math.abs(laction)) + S;
           }
       }
   }
}
