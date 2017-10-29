/**
 * 
 */
package com.dvr.android.dvr.view;

/**
 * 选择列表某项选中弹出�?BR>
 * [功能详细描述]
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012] 
 */
public interface OnSelectListListener
{
   public void onSelectDialogItemClick(String value , int pos);
   
   public void onDialogCancel();
}
