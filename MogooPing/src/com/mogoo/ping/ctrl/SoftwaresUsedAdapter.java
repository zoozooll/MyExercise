/**
 * 
 */
package com.mogoo.ping.ctrl;

import java.util.List;

import com.mogoo.ping.R;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.vo.UsedActivityItem;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @author Aaron Lee
 * ���䡰���ʹ�á�ģ�������
 * @param <UsedActivityItem>
 * @Date ����5:11:57  2012-9-17
 */
public class SoftwaresUsedAdapter<T extends UsedActivityItem> extends BaseAdapter{
	
	private Context mContext;
	private List<T> mData;
	private LayoutInflater inflater;
	
	/**
	 * @Author Aaron Lee
	 * @param mContext
	 * @param mData
	 * @Date ����10:47:26  2012-9-18
	 */
	public SoftwaresUsedAdapter(Context mContext, List<UsedActivityItem> mData) {
		super();
		this.mContext = mContext;
		this.mData = (List<T>) mData;
		inflater = LayoutInflater.from(mContext);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (mData == null) {
			return 0;
		}
		return mData.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public T getItem(int position) {
		if (mData == null) {
			return null;
		}
		return mData.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout view;
		if (convertView == null) {
			view = (LinearLayout) inflater.inflate(R.layout.content_apk_item, null);
		} else {
			view = (LinearLayout) convertView;
		}
		final TextView tvw_griditem_title = (TextView) view.findViewById(R.id.tvw_griditem_title);
		final ImageView  ivw_griditem_icon = (ImageView) view.findViewById(R.id.ivw_griditem_icon);
		UsedActivityItem item = getItem(position);
		tvw_griditem_title.setText(item.getName());
		//textView.setBackgroundDrawable(item.getIcon());
		//textView.setCompoundDrawablesWithIntrinsicBounds(null, item.getIcon(), null, null);
		ivw_griditem_icon.setImageDrawable(item.getIcon());
		view.setTag(R.id.tab_launch_intent, item.getLaunchIntent());
		return view;
	}

	/**
	 * @param mData the mData to set
	 */
	public void setmData(List<T> mData) {
		this.mData = mData;
	}

}
