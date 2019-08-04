package com.tcl.manager.adapter;

import com.tcl.manager.activity.AppDetailActivity;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.AndroidUtil; 
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.view.CornerImageView;
import com.tcl.mie.manager.R;
 
import android.content.Context;
import android.content.Intent; 
import android.graphics.drawable.Drawable; 
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * @Description: ManageApps存储查看适配器
 * 
 * @author pengcheng.zhang
 * @date 2014-12-24 下午9:31:19
 * @copyright TCL-MIE
 */
public class StorageAdapter extends BaseListAdapter<PageInfo>
{
	
	public StorageAdapter(Context context)
	{
		super(context);
	}
	
	@Override
	protected int getResource()
	{
		return R.layout.layout_list_item_storage;
	}
	
	@Override
	protected Object setHolder(View convertView)
	{
		ViewHolder holder = new ViewHolder();
		holder.llContainer = (LinearLayout) convertView.findViewById(R.id.ll_container);
		holder.ivIcon = (CornerImageView) convertView.findViewById(R.id.iv_icon);
		holder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
		holder.tvSize = (TextView) convertView.findViewById(R.id.tv_desc1);
		holder.tvDesc = (TextView) convertView.findViewById(R.id.tv_desc2);
		holder.btnSwitch = (Button) convertView.findViewById(R.id.btn_switch);
		
		return holder;
	}
	
	@Override
	protected void setView(final int position, final View convertView, final Object viewHolder)
	{
		final ViewHolder holder = (ViewHolder) viewHolder;
		final PageInfo info = mList.get(position);
		
		holder.tvTitle.setText(info.appName);
		
//		Drawable icon = PkgManagerTool.getIcon(mContext, info.pkgName);
//		if (icon != null)
//		{
//			holder.ivIcon.setImageDrawable(icon);
//		}
		loadIcon(holder, info.pkgName);
		StringBuffer buffer = new StringBuffer();
		buffer.append(mContext.getString(R.string.app_list_app_size_text));
		buffer.append(":");
		buffer.append(AndroidUtil.formatSize(info.appSize + info.appDataSize)); 
//		buffer.append(" ");
//		buffer.append(mContext.getString(R.string.app_list_app_data_text));
//		buffer.append(":");
//		buffer.append(AndroidUtil.formatSize(info.appDataSize));  
		
		holder.tvSize.setText(buffer.toString());
		switch (info.frequencyLevel)
		{
			case OFTEN: // 常用
				holder.tvDesc.setText(R.string.app_list_fre_used_text);
				break;
			
			case GENERAL: // 一般
				holder.tvDesc.setText(R.string.app_list_nor_used_text);
				break;
			
			case NOT_OFTEN: // 不常用
				holder.tvDesc.setText(R.string.app_list_bar_used_text);
				break;
			
			case NEVER_UESE:// 从未使用
				holder.tvDesc.setText(R.string.app_list_nev_used_text);
				break;
				
			default:
				holder.tvDesc.setText(R.string.app_list_unrecord_text);
				break;
		}
		holder.btnSwitch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				PageFunctionProvider.unistallApp(mContext, info.pkgName);
			}
		});
//		holder.ivIcon.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				startAppDetail(info.pkgName);
//			}
//		});
		holder.llContainer.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				startAppDetail(info.pkgName);
			}
		});
	}
	
	private void startAppDetail(String packageName)
	{
		try
		{
			Intent it = new Intent(mContext, AppDetailActivity.class);
			it.putExtra(AppDetailActivity.EXTRA_PACKGENAME, packageName);
			mContext.startActivity(it);
		}
		catch (Exception e)
		{ 
			e.printStackTrace();
		}
	}
	
	static class ViewHolder extends ImageHolderView
	{
		LinearLayout	llContainer;
		TextView		tvTitle;
		TextView		tvSize;
		TextView		tvDesc;
		Button			btnSwitch;
	}
}
