/**
 * 
 */
package com.tcl.manager.util;

import android.content.Context;

/**
 * @author zuokang.li
 *
 */
public class DisplayUtils {

	public static float px2sp(Context context, float size) {
	    final float scale = context.getResources().getDisplayMetrics().scaledDensity;  
	    if (size <= 0) {
            size = 15.f;
        }       
        float realSize = size / scale;
        return realSize;
	}
	
    public static int dip2px(Context context, float dpValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (dpValue * scale + 0.5f);  
    }  
  
    public static int px2dip(Context context, float pxValue) {  
        final float scale = context.getResources().getDisplayMetrics().density;  
        return (int) (pxValue / scale + 0.5f);  
    }
	
	public static float sp2px(Context context, float size) {
	    final float scale = context.getResources().getDisplayMetrics().scaledDensity;  
	    if (size <= 0) {
	        size = 15.f;
	    }	    
	    float realSize = size * scale;
	    return realSize;
	}	
}
