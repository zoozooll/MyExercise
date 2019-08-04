package com.tcl.manager.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;  
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tcl.manager.activity.AppDetailActivity;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo; 
import com.tcl.manager.util.AndroidUtil; 
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.view.CornerImageView; 
import com.tcl.mie.manager.R;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2014-12-30 下午2:52:24
 * @copyright TCL-MIE
 */
public class MemoryAdapter extends BaseListAdapter<PageInfo>
{
	
	public MemoryAdapter(Context context)
	{
		super(context);
	}
	
	@Override
	public Object setHolder(View convertView)
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
	public void setView(final int position, final View convertView, final Object viewHolder)
	{
		final PageInfo info = mList.get(position);
		final ViewHolder holder = (ViewHolder) viewHolder;
		
		convertView.setVisibility(View.VISIBLE);
		holder.tvTitle.setText(info.appName);
//		Drawable icon = PkgManagerTool.getIcon(mContext, info.pkgName);
//		if (icon != null)
//		{
//			holder.ivIcon.setImageDrawable(icon);
//		}
		loadIcon(holder, info.pkgName);
		holder.tvSize.setText(AndroidUtil.formatSize(info.memorySize)); 
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
			public void onClick(final View v)
			{    
				if (mOnItemListener != null)
				{
					mOnItemListener.onItemChange(false, 0);
				}
				
				new Thread()
				{
					@Override
					public void run()
					{ 
						Message msg = mHandler.obtainMessage();
						msg.what = MSG_RERRESH_VIEW;
						if(PageFunctionProvider.stop(info.pkgName))
						{
							msg.arg1 = position; 
						}
						else
						{
							msg.arg1 = -1;
						}
						msg.obj = convertView;
						mHandler.sendMessage(msg); 
					}
				}.start();
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
	
	private static final int MSG_RERRESH_VIEW = 100;
	
	private Handler mHandler = new Handler()
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what)
			{
				case MSG_RERRESH_VIEW:  
					try
					{
						View view = (View) msg.obj;
						
						if (msg.arg1 >= 0 && msg.arg1 < mList.size() && view != null)
						{ 
							setAnimaition((View) msg.obj, msg.arg1);
							return;
						} 
						
					}
					catch (Exception e)
					{ 
						e.printStackTrace();
					}
					
					if (mOnItemListener != null)
					{ 
						mOnItemListener.onItemChange(true, 0);
					}
					
					break;
					
				default:
					break;
			}
		};
	};
	
	private void setAnimaition(final View view, final int position)
	{
		Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.anim_progress_gone);
		animation.setAnimationListener(new AnimationListener()
		{
			@Override
			public void onAnimationStart(Animation animation)
			{ 
				if (mOnItemListener != null)
				{
					mOnItemListener.onItemChange(true, 1); 
				}
			}
			
			@Override
			public void onAnimationRepeat(Animation animation)
			{ 
				
			}
			
			@Override
			public void onAnimationEnd(Animation animation)
			{
				if (mOnItemListener != null)
				{ 
					view.setVisibility(View.GONE);
					mOnItemListener.onItemChange(true, 2);
				}
			}
		}); 
		view.startAnimation(animation);
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
	
	@Override
	protected int getResource()
	{
		return R.layout.layout_list_item_memory;
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
