/**
 * 
 */
package com.dvr.android.dvr.view;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.dvr.android.dvr.msetting.SettingBean;
import com.dvr.android.dvr.R;

/**
 * 省电模式的对话框<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012]
 */
public class PowerSavingDialog extends BaseSelectListManager implements OnItemClickListener
{

    private static final int IMAGEVIEW_ID = R.id.power_saving_item_icon;

    private static final int ITEM_LAYOUTID = R.layout.settings_powersaving_listitem;

    private static final int CONTENT_LAYOUTID = R.layout.settings_selectdialog_listview;

    private static final int LISTVIEW_LAYOUTID = R.id.setting_powersaving_listview;

    private static int[] values = null;

    public PowerSavingDialog(Context context)
    {
        super(context,ITEM_LAYOUTID,IMAGEVIEW_ID,CONTENT_LAYOUTID,LISTVIEW_LAYOUTID);
    }

    @Override
    public void init()
    {
        mItems = mContext.getResources().getStringArray(R.array.setting_power_saving_str);
        values = mContext.getResources().getIntArray(R.array.setting_power_saving_value);
        mAdapter.setItems(mItems);
        initView(this);
        for (int i = 0; i < values.length; i++)
        {
              if(SettingBean.powersaving_time == values[i])
              {
                  setSelect(i);
              }
        }
        
    }

    @Override
    public void doOther(View view, int position)
    {
        TextView textView = (TextView) view.findViewById(R.id.power_saving_item_text);
        textView.setText(mItems[position]);
    }

    public void onItemClick(AdapterView<?> adapter, View view, int pos, long id)
    {
        this.setSelect(pos);
        if (null != mListener)
        {
            mListener.onSelectDialogItemClick(String.valueOf(values[pos]), pos);
        }
    }
}
