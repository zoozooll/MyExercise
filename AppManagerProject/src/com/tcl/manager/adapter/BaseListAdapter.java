package com.tcl.manager.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.util.HandlerUtils;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.util.TCLThreadPool;
import com.tcl.manager.view.CornerImageView;
import com.tcl.mie.manager.R;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2014-12-30 下午2:15:11
 * @copyright TCL-MIE
 */
public abstract class BaseListAdapter<T> extends BaseAdapter
{
	protected static final int GET_ICON = 0;
	protected Context					mContext;
	protected OnAdapterItemListener<T>	mOnItemListener;
	protected ArrayList<T>				mList = new ArrayList<T>(); 
	
	public BaseListAdapter(Context context)
	{
		mContext = context; 
	}
	
	public void setList(ArrayList<T> list)
	{
		if (list != null)
		{
			mList = list;
		} 
	}
	
	public void setOnAdapterItemListener(OnAdapterItemListener<T> onItemListener)
	{
		mOnItemListener = onItemListener;
	}
	
	@Override
	public int getCount()
	{
		return mList.size();
	}
	
	@Override
	public Object getItem(int position)
	{
		return mList.get(position);
	}
	
	@Override
	public long getItemId(int position)
	{
		return position;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Object holder = null;
		if (convertView == null)
		{
			convertView = View.inflate(mContext, getResource(), null);
			holder = setHolder(convertView);
			convertView.setTag(holder);
		}
		else
		{
			holder = convertView.getTag();
		}
		
		try
		{
			setView(position, convertView, holder);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return convertView;
	}
	
	protected void loadIcon(final ImageHolderView holder2, final String packageName) {
		holder2.ivIcon.setTag(R.id.container, packageName);
		Bitmap icon = InstalledAppProvider.getInstance().getIcon(packageName);
		if( icon != null) {
			holder2.ivIcon.setImageBitmap(icon);
			return;
		}
		
		TCLThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				doLoad(holder2, packageName);
			}
		});
	}
	
	
	private void doLoad(final ImageHolderView holder2, final String pakcageName) {
		try {
			if (holder2 != null) {
				Drawable icon = PkgManagerTool.getIcon(mContext, pakcageName);
				if (icon instanceof BitmapDrawable) {
					Bitmap bitmap = ((BitmapDrawable) icon).getBitmap();
					Bitmap target = null;
					if (bitmap.getHeight() > holder2.ivIcon.getHeight() && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
						float rate = holder2.ivIcon.getHeight() * 1.0f / bitmap.getHeight();
						try {
							target = com.tcl.manager.util.BitmapUtil.zoomBitmap(mContext, bitmap, rate);
						} catch (Throwable e) {
							e.printStackTrace();
							target = bitmap;
						}
					} else {
						target = bitmap;
					}
					if (target != null) {
						InstalledAppProvider.getInstance().setIcon(pakcageName, target);
						setLoadedImage(holder2, target, pakcageName);
					}
				}

			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	
	private void setLoadedImage(final ImageHolderView holder2, final Bitmap bitmap, final String packageName) {
		HandlerUtils.runUITask(new Runnable() {
			@Override
			public void run() {
				String key = (String) holder2.ivIcon.getTag(R.id.container);
				if (!TextUtils.isEmpty(key) && key.equals(packageName)) {
					holder2.ivIcon.setImageBitmap(bitmap);
				}
			}
		});
	}
	
	protected abstract int getResource();
	
	protected abstract Object setHolder(View convertView);
	
	protected abstract void setView(final int position, final View convertView, final Object holder);
	
	static class ImageHolderView {
		public CornerImageView ivIcon;
	}
}
