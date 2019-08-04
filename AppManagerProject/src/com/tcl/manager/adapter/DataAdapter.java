package com.tcl.manager.adapter;

import com.tcl.manager.activity.AppDetailActivity; 
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.AndroidUtil; 
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.view.CornerImageView;
import com.tcl.manager.view.CustomDialog;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;
 

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;  
import android.view.View; 
import android.view.View.OnClickListener; 
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView; 

/**
 * @Description: ManageApps流量查看适配器
 * 
 * @author pengcheng.zhang
 * @date 2014-12-24 下午9:33:21
 * @copyright TCL-MIE
 */
public class DataAdapter extends BaseListAdapter<PageInfo>
{
	
	public DataAdapter(Context context)
	{
		super(context);
	}
	
	@Override
	protected int getResource()
	{
		return R.layout.layout_list_item_data;
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
		holder.ivSwitch = (ImageView) convertView.findViewById(R.id.iv_switch);
		
		return holder;
	}
	
	@Override
	protected void setView(final int position, final View convertView, final Object viewHolder)
	{
		final PageInfo info = mList.get(position);
		final ViewHolder holder = (ViewHolder) viewHolder;
		
		holder.tvTitle.setText(info.appName);
//		Drawable icon = PkgManagerTool.getIcon(mContext, info.pkgName);
//		if (icon != null)
//		{
//			holder.ivIcon.setImageDrawable(icon);
//		}
		loadIcon(holder, info.pkgName);
		StringBuffer buffer = new StringBuffer();
		buffer.append(mContext.getString(R.string.app_list_today_text));
		buffer.append(":");
		buffer.append(AndroidUtil.formatSize(info.todayMobileSize)); 
		buffer.append("   ");
		buffer.append(AndroidUtil.formatSize(info.averageMobileSize)); 
		buffer.append(mContext.getString(R.string.app_list_per_day_text));
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
		
		holder.ivSwitch.setImageResource(info.isOpenDataAccess ? R.drawable.ic_open : R.drawable.ic_closed);
		holder.ivSwitch.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{ 
				
				String msgText = info.isOpenDataAccess ? mContext.getString(R.string.app_data_access_turn_off_guide_text)
						: mContext.getString(R.string.app_data_access_turn_on_guide_text);
				
				String tipsText = info.isOpenDataAccess ? mContext.getString(R.string.app_data_battery_btn_turn_off_tips_text)
						: mContext.getString(R.string.app_data_battery_btn_turn_on_tips_text);
				
				/*new CustomDialog(mContext, null, msgText, tipsText, null, null, null, new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{ 
						PageInfo pageInfo = PageFunctionProvider.setDataAccess(info.pkgName, !info.isOpenDataAccess);
						
						if(pageInfo == null || position < 0 || position >= mList.size())
						{
							return;
						}
						
						mList.set(position, pageInfo); 
						notifyDataSetChanged(); 
					}
				}).show();*/
				UIHelper.showCustomDialog(mContext, null, msgText, tipsText, null, null, null, new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{ 
						PageInfo pageInfo = PageFunctionProvider.setDataAccess(info.pkgName, !info.isOpenDataAccess);
						
						if(pageInfo == null || position < 0 || position >= mList.size())
						{
							return;
						}
						
						mList.set(position, pageInfo); 
						notifyDataSetChanged(); 
					}
				});
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
	
	private static class ViewHolder extends ImageHolderView
	{
		LinearLayout	llContainer;
		TextView		tvTitle;
		TextView		tvSize;
		TextView		tvDesc;
		ImageView		ivSwitch;
	}
}
