package com.tcl.manager.activity;

import java.util.ArrayList;
import java.util.List;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.adapter.BaseListAdapter;
import com.tcl.manager.score.PackageChangeEvent;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.mie.manager.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

/**
 * @Description:
 * @author pengcheng.zhang
 * @date 2015-1-5 下午3:04:25
 * @copyright TCL-MIE
 */
public abstract class BaseAppListActivity extends BaseActivity implements OnClickListener, Subscriber<PackageChangeEvent>
{
	private ArrayList<PageInfo>			mInfos	= new ArrayList<PageInfo>();
	private LinearLayout				mLlDesc;
	private TextView					mTvDesc;
	private FrameLayout					mFlProgress;
	private ListView					mLvContent;
	private BaseListAdapter<PageInfo>	mAdapter;
	private boolean						mIsFinished;
	private Animation					mAnimIn;
	private Animation					mAnimOut;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_base_manager_fragment);
		register();
		init();
		
		showProgress(R.color.white);
	}
	
	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
			case R.id.header_action_bar_rl_left:
				this.finish();
				break;
				
			default:
				break;
		}
	}
	
	@Override
	protected void onStart()
	{
		super.onStart();
		updateList();
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		unRegister();
	}
	
	public void showProgress(int id)
	{
		mFlProgress.setOnClickListener(this);
		mFlProgress.setBackgroundColor(getResources().getColor(id));
		mFlProgress.setVisibility(View.VISIBLE);
	}
	
	protected void dimissProgress()
	{
		mFlProgress.setVisibility(View.GONE);
	}
	
	protected void updateList()
	{
		new Thread()
		{
			public void run()
			{
				Message msg = mHandler.obtainMessage();
				try
				{
					msg.obj = getPageInfoList();
				}
				catch (Exception e)
				{
					e.printStackTrace();
					msg.obj = null;
				}
				
				msg.what = MSG_REFRESH_LISTVIEW;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
	
	protected void updateView()
	{
		mAdapter.notifyDataSetChanged();
		if (mInfos == null || mInfos.isEmpty())
		{
			mLlDesc.setVisibility(View.GONE);
		}
		else
		{
			showDescription();
		}
		mFlProgress.setVisibility(View.GONE);
	}
	
	private void init()
	{
		((TextView) findViewById(R.id.header_action_bar_tv_left)).setText(getHeaderTitle());
		findViewById(R.id.header_action_bar_rl_left).setOnClickListener(this);
		
		mLlDesc = (LinearLayout) findViewById(R.id.ll_desc);
		mTvDesc = (TextView) findViewById(R.id.tv_desc);
		mFlProgress = (FrameLayout) findViewById(R.id.vs_progress);
		mLvContent = (ListView) findViewById(R.id.lv_content);
		
		mInfos = new ArrayList<PageInfo>();
		mAdapter = getAdapter();
		mAdapter.setList(mInfos);
		mLvContent.setAdapter(mAdapter);
		
		mAnimOut = AnimationUtils.loadAnimation(this, R.anim.anim_out_from_top);
		mAnimOut.setAnimationListener(mAniListener);
		
		mAnimIn = AnimationUtils.loadAnimation(this, R.anim.anim_in_from_top);
		mAnimIn.setAnimationListener(mAniListener);
		
		mIsFinished = false;
	}
	
	private void register()
	{
		// try
		// {
		// // IntentFilter filter = new
		// IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
		// // filter.addDataScheme("package");
		// // registerReceiver(mReceiver, filter);
		//
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		
		NotificationCenter.defaultCenter().subscriber(PackageChangeEvent.class, this);
	}
	
	private void unRegister()
	{
		// try
		// {
		// unregisterReceiver(mReceiver);
		// }
		// catch (Exception e)
		// {
		// e.printStackTrace();
		// }
		NotificationCenter.defaultCenter().unsubscribe(PackageChangeEvent.class, this);
	}
	
	@Override
	public void onEvent(PackageChangeEvent arg0)
	{
		updateList();
	}
	
	private void showDescription()
	{
		if (mIsFinished)
		{
			return;
		}
		mIsFinished = true;
		
		mTvDesc.setText(getDescription(mInfos));
		
		mLlDesc.setVisibility(View.VISIBLE);
		mLlDesc.startAnimation(mAnimIn);
	}
	
	protected abstract String getHeaderTitle();
	
	protected abstract List<PageInfo> getPageInfoList();
	
	protected abstract BaseListAdapter<PageInfo> getAdapter();
	
	protected abstract String getDescription(List<PageInfo> pageInfos);
	
	private static final int	MSG_ANIM_OUT			= 100;
	private static final int	MSG_REFRESH_LISTVIEW	= 200;
	
	private Handler				mHandler				= new Handler()
	{
		@SuppressWarnings("unchecked")
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case MSG_ANIM_OUT:
					mLlDesc.startAnimation(mAnimOut);
					break;
				
				case MSG_REFRESH_LISTVIEW:
					Object obj = msg.obj;
					if (obj == null)
					{
						updateView();
						return;
					}
					try
					{
						List<PageInfo> infos = (List<PageInfo>) obj; 
						
						if (!mInfos.isEmpty())
						{
							mInfos.clear();
						}
						
						mInfos.addAll(infos);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
					
					updateView();
					break;
					
				default:
					break;
			}
		};
	};													
	
	private AnimationListener	mAniListener			= new AnimationListener()
	{
		
		@Override
		public void onAnimationStart(Animation animation)
		{
			;
		}
		
		@Override
		public void onAnimationRepeat(Animation animation)
		{
			;
		}
		
		@Override
		public void onAnimationEnd(Animation animation)
		{
			if (animation == mAnimOut)
			{
				mLlDesc.setVisibility(View.GONE);
			}
			else if (animation == mAnimIn)
			{
				mHandler.sendEmptyMessageDelayed(MSG_ANIM_OUT, 2000);
			}
		}
	};													
	
	// private BroadcastReceiver mReceiver = new BroadcastReceiver()
	// {
	// @Override
	// public void onReceive(Context context, final Intent intent)
	// {
	// if (!Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction()))
	// {
	// return;
	// }
	//
	// new Thread()
	// {
	// public void run()
	// {
	// try
	// {
	// PageFunctionProvider.uninstalledSuccess(intent.getDataString().substring("package:".length()));
	// }
	// catch (Exception e)
	// {
	// e.printStackTrace();
	// }
	// updateList();
	// };
	// }.start();
	//
	// }
	// };
}
