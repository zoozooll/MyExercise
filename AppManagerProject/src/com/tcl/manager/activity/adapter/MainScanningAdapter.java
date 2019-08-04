/**
 * 
 */
package com.tcl.manager.activity.adapter;

import java.util.LinkedList;
import java.util.List;

import com.tcl.manager.activity.entity.ScanningItem;
import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.HandlerUtils;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.util.TCLThreadPool;
import com.tcl.mie.manager.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author zuokang.li
 *
 */
public class MainScanningAdapter extends BaseAdapter {
	
	private Context mContext;
	private LinkedList<ScanningItem> animationList;
	private LayoutInflater mInflater;

	
	
	public MainScanningAdapter(Context c) {
		super();
		this.mContext = c;
		mInflater = LayoutInflater.from(c);
		animationList = new LinkedList<ScanningItem>();
	}
	
	public void addItem(ScanningItem item) {
		synchronized (animationList) {
			animationList.addLast(item);
			if (animationList.size() > 5) {
				ScanningItem peekItem = animationList.pollFirst();
				/*if (peekItem.icon != null && !peekItem.icon.isRecycled()) {
					peekItem.icon.recycle();
				}*/
			}
		}
		notifyDataSetChanged();
	}
	
	public boolean removingItems() {
		if (animationList != null && animationList.size() > 0) {
			ScanningItem peekItem = animationList.pollFirst();
			/*if (peekItem.icon != null && !peekItem.icon.isRecycled()) {
				peekItem.icon.recycle();
			}*/
			notifyDataSetChanged();
			if (animationList.size() > 0) {
				return true;
			}
		} 
		return false;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		if (animationList != null) {
			return animationList.size();
		}
		return 0;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public ScanningItem getItem(int position) {
		if (animationList != null && position < animationList.size()) {
			return animationList.get(position);
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.main_scanninglist_item_view, null);
			holder.icon = (ImageView) convertView.findViewById(R.id.img_icon);
			holder.name = (TextView) convertView.findViewById(R.id.text_appname);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		ScanningItem item = getItem(position);
		holder.name.setText(item.label);
		if (item.icon != null) { 
			holder.icon.setImageBitmap(item.icon); 
		} else {
			holder.icon.setImageResource(R.drawable.ic_default);
		}
		return convertView;
	}
	
	private class ViewHolder {
		ImageView icon;
		TextView name;
	}
	
}
