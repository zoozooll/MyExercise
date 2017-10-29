/**
 * 
 */
package com.dvr.android.dvr.view;

import android.content.Context;
import android.os.StatFs;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.R;

/**
 * 省电模式的对话框<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012]
 */
public class SavePathDialog extends BaseSelectListManager implements OnItemClickListener
{

    private static final int IMAGEVIEW_ID = R.id.power_saving_item_icon;

    private static final int ITEM_LAYOUTID = R.layout.settings_savepath_listitem;

    private static final int CONTENT_LAYOUTID = R.layout.settings_selectdialog_listview;

    private static final int LISTVIEW_LAYOUTID = R.id.setting_powersaving_listview;

    private static String[] paths = {Config.SDCARD_PATH, Config.EXCARD_PATH };
    private static int cardNum = 0;
    private static int cardFlag = 0;

    public SavePathDialog(Context context)
    {
        super(context,ITEM_LAYOUTID,IMAGEVIEW_ID,CONTENT_LAYOUTID,LISTVIEW_LAYOUTID);
    }

    @Override
    public void init()
    {
        findSavePath();
        mAdapter.setItems(mItems);
        initView(this);
    }

    
    public static boolean checkSDCardStatus() {
    	try {
    		cardNum = 0;
			for(int i = 0; i < paths.length; i++)
	    	{
				StatFs stat = new StatFs(paths[i]);
				long blockCount = stat.getBlockCount();

				long availableBlocks = stat.getAvailableBlocks();
				
				if (blockCount > 0) {
					cardNum ++;
					cardFlag = i;
				} 
	    	}
			return true;
		}catch (IllegalArgumentException e) {
			return false;
		}
    } 
    
    
    /**
     * 找到可用路径
     */
    private void findSavePath()
    {
    	checkSDCardStatus();
    	if(cardNum == 2)
        mItems =
            new String[] {mContext.getResources().getString(R.string.msetting_save_path_fromSD),
                mContext.getResources().getString(R.string.msetting_save_path_fromextSD) };
    	else
    	{
    		if(cardFlag == 0)
    			  mItems =
    	            new String[] {mContext.getResources().getString(R.string.msetting_save_path_fromSD)};
    		else
    			  mItems =
    	            new String[] {mContext.getResources().getString(R.string.msetting_save_path_fromextSD)};
    	}
        for(int i=0 ; i < paths.length ; i++)
        {
            if(cardNum ==2)
            {
                this.setSelect(SettingBean.mPathItem);
            }
            else
            {
            	this.setSelect(0);
            	SettingBean.mPathItem = 0;
            }
        }
    }

    @Override
    public void doOther(View view, int position)
    {
        TextView titleText = (TextView) view.findViewById(R.id.savepath_path_text);
        TextView totalText = (TextView) view.findViewById(R.id.savepath_total_text);
        TextView remindText = (TextView) view.findViewById(R.id.savepath_remainder_text);
        titleText.setText(mItems[position]);
        StatFs sf = new StatFs(paths[position]);
        long total = ((long)sf.getBlockSize()) * sf.getBlockCount() / (1024l * 1024l);
        totalText.setText(mContext.getString(R.string.msetting_savepath_total, total));
        
        long remind = ((long)sf.getBlockSize()) * sf.getAvailableBlocks() / (1024l * 1024l);
        remindText.setText(mContext.getString(R.string.msetting_savepath_remind, remind));

    }

    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
    {
        this.setSelect(pos);
        SettingBean.mPathItem = pos;
        if (null != mListener)
        {
            mListener.onSelectDialogItemClick(paths[pos], pos);
        }
    }
}
