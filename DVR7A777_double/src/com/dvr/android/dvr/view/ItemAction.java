/**
 * 
 */
package com.dvr.android.dvr.view;

import android.view.View;

/**
 * 处理listview项接�?BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012]
 */
public interface ItemAction
{
    /**
     * getView方法中处理每�?��item 
     * @param view
     * @param position
     * @return true被�?�?false没有被�?�?     */
    public void onItemGen(View view, int position);
}
