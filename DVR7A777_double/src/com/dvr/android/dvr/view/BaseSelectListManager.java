/**
 * 
 */
package com.dvr.android.dvr.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

/**
 * 管理选择性listview的父类，以衍生出各种样式的�?择listview<BR>
 * [功能详细描述]
 * 
 * @author sunshine
 * @version [yecon Android_Platform, 24 May 2012]
 */
public abstract class BaseSelectListManager implements ItemAction {

    private int mContentLayoutID;

    private int mListViewID;

    protected SelectAdapter mAdapter = null;

    protected String[] mItems = null;

    protected int resid;

    protected int selectImageId = 0;

    protected ListView mListView = null;

    protected int selectPosition = 0;

    protected Context mContext = null;

    protected View dialogView = null;

    protected OnSelectListListener mListener = null;

    private LayoutInflater mInflater;

    public BaseSelectListManager(Context context, int itemLayoutID, int imageViewId, int contentLayoutID, int listViewID) {
        mContext = context;
        selectImageId = imageViewId;
        mContentLayoutID = contentLayoutID;
        mListViewID = listViewID;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mAdapter = new SelectAdapter(context, this, itemLayoutID);
        init();
    }

    public void initView(OnItemClickListener listener) {
        dialogView = mInflater.inflate(mContentLayoutID, null);
        mListView = (ListView) dialogView.findViewById(mListViewID);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(listener);
    }

    public void onItemGen(View view, int position) {
        doOther(view, position);
        if (position == selectPosition) {
            view.findViewById(selectImageId).setVisibility(View.VISIBLE);
        } else {
            view.findViewById(selectImageId).setVisibility(View.INVISIBLE);
        }
    }

    public void setSelect(int pos) {
        selectPosition = pos;
        mAdapter.notifyDataSetChanged();
    }

    public ListView getListView() {
        return mListView;
    }

    public Dialog createDialog() {
        return new AlertDialog.Builder(mContext).setView(dialogView).setOnCancelListener(new OnCancelListener() {
            public void onCancel(DialogInterface dialog) {
                if (null != mListener) {
                    mListener.onDialogCancel();
                }
            }
        }).create();
    }

    public void setOnItemClickListener(OnSelectListListener listener) {
        mListener = listener;
    }

    public abstract void init();

    public abstract void doOther(View view, int position);
}
