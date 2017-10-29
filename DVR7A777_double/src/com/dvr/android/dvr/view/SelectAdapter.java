/**
 * 
 */
package com.dvr.android.dvr.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Adapter<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 23 May 2012]
 */
public class SelectAdapter extends BaseAdapter
{
    private LayoutInflater mInflater;

    private Context mContext = null;

    private String[] mItems = null;

    private ItemAction mItemAction = null;

    private int mItemLayoutID = 0;

    public SelectAdapter(Context context, ItemAction action, int resid)
    {
        mContext = context;
        mItemAction = action;
        mItemLayoutID = resid;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void setItems(String[] items)
    {
        mItems = items;
    }

    public int getCount()
    {
        return mItems.length;
    }

    public Object getItem(int position)
    {
        return mItems[position];
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (null == convertView)
        {
            convertView = mInflater.inflate(mItemLayoutID, null);
            mItemAction.onItemGen(convertView, position);
        }
        else
        {
            mItemAction.onItemGen(convertView, position);
        }
        return convertView;
    }
}
