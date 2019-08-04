package com.tcl.manager.activity;

import java.util.ArrayList;

import com.tcl.base.http.IProviderCallback;
import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.protocol.RecommAppProtocol;
import com.tcl.manager.protocol.data.BaseAppInfo;
import com.tcl.manager.protocol.data.RecommAppPojo;
import com.tcl.manager.score.PackageChangeEvent;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.score.ScoreLevel;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.ImageLoader;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.view.CornerImageView;
import com.tcl.manager.view.CustomDialog;
import com.tcl.manager.view.UIHelper;
import com.tcl.mie.manager.R;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;  
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils; 
import android.view.View;
import android.view.View.OnClickListener; 
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button; 
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;  

/**
 * @Description: 查看应用详情界面
 * 
 * @author pengcheng.zhang
 * @date 2014-12-24 下午4:07:52
 * @copyright TCL-MIE
 */
public class AppDetailActivity extends BaseActivity implements OnClickListener, Subscriber<PackageChangeEvent>
{
	public static final String	EXTRA_PACKGENAME	= "packageName";
	private FrameLayout			mFlProgress;
	/* 标题部分 */
	private RelativeLayout		mRlBack;
	/* 简介部分 */
	private CornerImageView		mIvIcon;
	private TextView			mTvTitle;
	private TextView			mTvDesc1;
	private TextView			mTvDesc2;
	private TextView			mTvDesc3;
	private TextView			mTvDesc4;
	/* 描述部分 */
	private TextView			mTvMemoryDesc;
	private TextView			mTvBatteryDesc;
	private TextView			mTvNetworkDesc1;
	private TextView			mTvNetworkDesc2;
	private TextView			mTvStorageDesc1;
	private TextView			mTvStorageDesc2;
	/* 说明部分 */
	private TextView			mTvResTitle;
	private TextView			mTvResDesc;
	
	private LinearLayout		mLlRun;
	private Button				mBtnRun;
	private View				mVLineRun;
	private TextView			mTvStartDesc;
	private ImageView			mIvStart;
	private TextView			mTvDataDesc;
	private ImageView			mIvData;
	private LinearLayout		mLlBtom;
	private Button				mBtnUnistall;
	/* 推荐部分 */
	private LinearLayout		mLlRecomm;
	private PageInfo			mPageInfo;
	private String				mPackageName;
	private Animation			mAnimOutRight;
	private Animation			mAnimInBotm;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_app_detail);

		register();
		
		Intent it = this.getIntent();
		if (!it.hasExtra(EXTRA_PACKGENAME))
		{
			this.finish();
			return;
		} 

		init();  
		mPackageName = it.getStringExtra(EXTRA_PACKGENAME);
		
		showProgress(R.color.white);
		new Thread()
		{
			public void run() 
			{
				mPageInfo = PageFunctionProvider.getPageInfo(mPackageName);
				mHandler.sendEmptyMessage(MSG_INIT_FINISHED);
			};
		}.start();
	}
	
	@Override
	protected void onDestroy()
	{ 
		super.onDestroy();
		unRegister();
	}
	
	@Override
	public void onClick(View v)
	{
		try
		{
			if (v == mFlProgress)
			{
				return;
			}
			
			if (mPageInfo == null)
			{
				this.finish();
				return;
			}
			switch (v.getId())
			{
				 
				case R.id.header_action_bar_rl_left:
					AppDetailActivity.this.finish();
					break;
					
				case R.id.btn_run:       
					showProgress(R.color.dark);
					new Thread()
					{
						public void run() 
						{
							boolean flag = PageFunctionProvider.stop(mPageInfo.pkgName);  
							mPageInfo = PageFunctionProvider.getPageInfo(mPackageName);
							Message msg = mHandler.obtainMessage();
							msg.what = MSG_STOP_OK;
							msg.obj = flag; 
							mHandler.sendMessage(msg);
						};
					}.start(); 
					break;
					
				case R.id.iv_data: 
					
					String dataMsgText = mPageInfo.isOpenDataAccess ? getString(R.string.app_data_access_turn_off_guide_text)
							: getString(R.string.app_data_access_turn_on_guide_text);
					
					String dataBtnText = mPageInfo.isOpenDataAccess ? getString(R.string.app_data_battery_btn_turn_off_tips_text)
							: getString(R.string.app_data_battery_btn_turn_on_tips_text);
					
					/*new CustomDialog(this, null, dataMsgText, dataBtnText, null, null, null, new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{  
							mPageInfo = PageFunctionProvider.setDataAccess(mPageInfo.pkgName, !mPageInfo.isOpenDataAccess);
							if (mPageInfo == null)
							{
								AppDetailActivity.this.finish();
								return;
							}
							
							setHeader();
							setResume(); 
						}
					}).show(); */
					
					UIHelper.showCustomDialog(this, null, dataMsgText, dataBtnText, null, null, null, new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{  
							mPageInfo = PageFunctionProvider.setDataAccess(mPageInfo.pkgName, !mPageInfo.isOpenDataAccess);
							if (mPageInfo == null)
							{
								AppDetailActivity.this.finish();
								return;
							}
							
							setHeader();
							setResume(); 
						}
					});
					
					break;
					
				case R.id.iv_start:
					String startMsgText = mPageInfo.isOpenAutoStart ? getString(R.string.app_auto_start_turn_off_guide_text)
							: getString(R.string.app_auto_start_turn_on_guide_text);
					
					String startBtnText = mPageInfo.isOpenAutoStart ? getString(R.string.app_data_battery_btn_turn_off_tips_text)
							: getString(R.string.app_data_battery_btn_turn_on_tips_text);
					
					new CustomDialog(this, null, startMsgText, startBtnText, null, null, null, new OnClickListener()
					{
						@Override
						public void onClick(View v)
						{    
							mPageInfo = PageFunctionProvider.setAutoStart(mPageInfo.pkgName, !mPageInfo.isOpenAutoStart);
							if (mPageInfo == null)
							{
								AppDetailActivity.this.finish();
								return;
							}
							
							setHeader();
							setResume(); 
						}
					}).show();
					
					
					break;
					
				case R.id.btn_unins:
					PageFunctionProvider.unistallApp(this, mPageInfo.pkgName);
					break; 
					
				default:
					break;
			
			}
		}
		catch (Exception exp)
		{
			exp.printStackTrace();
			finish();
		}
	}

	@Override
	public void onEvent(PackageChangeEvent arg0)
	{ 
		AppDetailActivity.this.finish(); 
	}	
	
	
	private void init()
	{ 
		mRlBack = (RelativeLayout) this.findViewById(R.id.header_action_bar_rl_left); 
		mFlProgress = (FrameLayout) findViewById(R.id.vs_progress);
		mIvIcon = (CornerImageView) this.findViewById(R.id.iv_icon);
		mTvTitle = (TextView) this.findViewById(R.id.tv_title);
		mTvDesc1 = (TextView) this.findViewById(R.id.tv_desc1);
		mTvDesc2 = (TextView) this.findViewById(R.id.tv_desc2);
		mTvDesc3 = (TextView) this.findViewById(R.id.tv_desc3);
		mTvDesc4 = (TextView) this.findViewById(R.id.tv_desc4);
		mTvMemoryDesc = (TextView) this.findViewById(R.id.tv_memory_desc); 
		mTvBatteryDesc = (TextView) this.findViewById(R.id.tv_battery_desc); 
		mTvNetworkDesc1 = (TextView) this.findViewById(R.id.tv_network_desc1);
		mTvNetworkDesc2 = (TextView) this.findViewById(R.id.tv_network_desc2); 
		mTvStorageDesc1 = (TextView) this.findViewById(R.id.tv_storage_desc1);
		mTvStorageDesc2 = (TextView) this.findViewById(R.id.tv_storage_desc2);
		
		mTvResTitle = (TextView) this.findViewById(R.id.tv_resume_title);
		mTvResDesc = (TextView) this.findViewById(R.id.tv_resume_desc); 
		mTvStartDesc = (TextView) this.findViewById(R.id.tv_start_desc); 
		mLlRun = (LinearLayout) this.findViewById(R.id.ll_run);
		mVLineRun = (View) this.findViewById(R.id.v_run_line);
		mTvDataDesc = (TextView) this.findViewById(R.id.tv_data_desc); 
		mBtnRun = (Button) this.findViewById(R.id.btn_run); 
		mIvStart = (ImageView) this.findViewById(R.id.iv_start); 
		mIvData = (ImageView) this.findViewById(R.id.iv_data); 
		mLlBtom = (LinearLayout) this.findViewById(R.id.ll_bottom);
		mBtnUnistall = (Button) this.findViewById(R.id.btn_unins);
		mLlRecomm = (LinearLayout) this.findViewById(R.id.ll_recomm_app);
		
		((TextView) mRlBack.findViewById(R.id.header_action_bar_tv_left)).setText(R.string.app_detail_title_text);
		
		mRlBack.setOnClickListener(this);
		mBtnRun.setOnClickListener(this);
		mIvStart.setOnClickListener(this);
		mIvData.setOnClickListener(this);
		mBtnUnistall.setOnClickListener(this);
		
		mLlBtom.setVisibility(View.GONE);
		mLlRecomm.setVisibility(View.GONE); 
	}
	
	private void showProgress(int id)
	{  
		mFlProgress.setOnClickListener(this); 
		mFlProgress.setBackgroundColor(getResources().getColor(id));   
		mFlProgress.setVisibility(View.VISIBLE);  
	} 
	
	private void setHeader()
	{
		if (mPageInfo == null)
		{ 
			return;
		}
		try
		{
			mTvTitle.setText(mPageInfo.appName);
			Drawable icon = PkgManagerTool.getIcon(this, mPageInfo.pkgName);
			if (icon != null)
			{
				mIvIcon.setImageDrawable(icon);
			}
			switch (mPageInfo.frequencyLevel)
			{
				case OFTEN: // 常用
					mTvDesc1.setText(R.string.app_detail_fre_used_text);
					break;
				case GENERAL: // 一般
					mTvDesc1.setText(R.string.app_detail_nor_used_text);
					break;
				case NOT_OFTEN: // 不常用
					mTvDesc1.setText(R.string.app_detail_bar_used_text);
					break;
				case NEVER_UESE:// 从未使用
					mTvDesc1.setText(R.string.app_detail_nev_used_text);
					break;
				default:
					mTvDesc1.setText(R.string.app_detail_unrecord_text);
					break;
			}
			StringBuffer buffer = new StringBuffer();
//			buffer.append(mPageInfo.averageUseCount);
//			buffer.append(" ");
//			buffer.append(this.getString(R.string.app_detail_times_per_day_text));
//			buffer.append("   ");
			buffer.append(AndroidUtil.formatTime(mPageInfo.averageUseTime, 
				" " + this.getString(R.string.app_detail_hours_per_day_text), 
				" " + this.getString(R.string.app_detail_mins_per_day_text)));
			
			mTvDesc2.setText(buffer.toString());
			mTvDesc3.setText(String.valueOf(PageFunctionProvider.getPageInfoScore(mPageInfo)));
	        mTvDesc3.setTextColor(ScoreLevel.resolveToColor(PageFunctionProvider.getPageInfoScore(mPageInfo)));
			mTvDesc4.setText(this.getString(R.string.app_detail_score_text));
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setResume()
	{
		if (mPageInfo == null)
		{
			return;
		}
		try
		{
			mTvMemoryDesc.setText(mPageInfo.isRunning ? AndroidUtil.formatSize(mPageInfo.memorySize) : this.getString(R.string.app_detail_unrecord_text));
			mTvBatteryDesc.setText(String.valueOf(mPageInfo.batteryPercent) + "%");
			mTvNetworkDesc1.setText(this.getString(R.string.app_detail_today_text) + ": " + AndroidUtil.formatSize(mPageInfo.todayMobileSize));
			mTvNetworkDesc2.setText(AndroidUtil.formatSize(mPageInfo.averageMobileSize) + this.getString(R.string.app_detail_per_day_text));
			mTvStorageDesc1.setText(this.getString(R.string.app_detail_app_text) + ": " + AndroidUtil.formatSize(mPageInfo.appSize));
			mTvStorageDesc2.setText(this.getString(R.string.app_detail_app_data_text) + ": " + AndroidUtil.formatSize(mPageInfo.appDataSize));
			mTvResTitle.setText(this.getString(R.string.app_detail_diagnosis_text) + ": ");
			
			if (mPageInfo.needOptimizedBattery)
			{
				if (mPageInfo.needOptimizedNet)
				{
					mTvResDesc.setText(R.string.app_detail_diagnosis_bd_text);
					mTvDataDesc.setVisibility(View.VISIBLE);
				}
				else
				{
					mTvResDesc.setText(R.string.app_detail_diagnosis_battery_text);
					mTvDataDesc.setVisibility(View.INVISIBLE);
				}
				mTvStartDesc.setVisibility(View.VISIBLE);
			}
			else
			{
				if (mPageInfo.needOptimizedNet)
				{
					
					mTvResDesc.setText(R.string.app_detail_diagnosis_data_text);
					mTvDataDesc.setVisibility(View.VISIBLE);
				}
				else
				{
					mTvResDesc.setText(R.string.app_detail_diagnosis_healthy_text);
					mTvDataDesc.setVisibility(View.INVISIBLE);
				}
				mTvStartDesc.setVisibility(View.INVISIBLE);
			}
			
			mLlRun.setVisibility(mPageInfo.isRunning ? View.VISIBLE : View.GONE);
			mVLineRun.setVisibility(mPageInfo.isRunning ? View.VISIBLE : View.GONE);
			mIvStart.setImageResource(mPageInfo.isOpenAutoStart ? R.drawable.ic_open : R.drawable.ic_closed);
			mIvData.setImageResource(mPageInfo.isOpenDataAccess ? R.drawable.ic_open : R.drawable.ic_closed);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void setRecommList(RecommAppPojo obj)
	{
		if (obj == null)
		{
			return;
		}
		try
		{
			ArrayList<BaseAppInfo> appList = obj.data.apps;
			if (appList == null || appList.isEmpty())
			{
				return;
			}
			
			int len = appList.size();
			for (int i = 0; i < len; i++)
			{
				setRecommItemView(i, appList.get(i));
			}
			
			mLlRecomm.setVisibility(View.VISIBLE);
			mAnimInBotm = AnimationUtils.loadAnimation(this, R.anim.anim_app_recomm);
			mAnimInBotm.setAnimationListener(mAniListener);
			mLlRecomm.startAnimation(mAnimInBotm);
		
		}
		catch(Exception exp)
		{
			exp.printStackTrace();
		}
	}
	
	private void setBtomViewVisible(boolean canVisible)
	{
		if (canVisible)
		{
			mLlBtom.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, mLlRecomm.getHeight()));
			mLlBtom.setVisibility(View.VISIBLE);
		}
		else
		{
			mLlBtom.setVisibility(View.GONE);
		}
	}
	
	private void setRecommItemView(final int index, final BaseAppInfo info)
	{
		LinearLayout lyItem = null;
		CornerImageView imvItemIcon = null;
		TextView tvItemTitle = null;
		switch (index)
		{
			case 0:
				lyItem = (LinearLayout) mLlRecomm.findViewById(R.id.recomm_ll_list_item1);
				imvItemIcon = (CornerImageView) mLlRecomm.findViewById(R.id.recomm_civ_item1_icon);
				tvItemTitle = (TextView) mLlRecomm.findViewById(R.id.recomm_tv_item1_title);
				break;
			
			case 1:
				lyItem = (LinearLayout) mLlRecomm.findViewById(R.id.recomm_ll_list_item2);
				imvItemIcon = (CornerImageView) mLlRecomm.findViewById(R.id.recomm_civ_item2_icon);
				tvItemTitle = (TextView) mLlRecomm.findViewById(R.id.recomm_tv_item2_title);
				break;
			
			case 2:
				lyItem = (LinearLayout) mLlRecomm.findViewById(R.id.recomm_ll_list_item3);
				imvItemIcon = (CornerImageView) mLlRecomm.findViewById(R.id.recomm_civ_item3_icon);
				tvItemTitle = (TextView) mLlRecomm.findViewById(R.id.recomm_tv_item3_title);
				break;
			
			case 3:
				lyItem = (LinearLayout) mLlRecomm.findViewById(R.id.recomm_ll_list_item4);
				imvItemIcon = (CornerImageView) mLlRecomm.findViewById(R.id.recomm_civ_item4_icon);
				tvItemTitle = (TextView) mLlRecomm.findViewById(R.id.recomm_tv_item4_title);
				
			default:
				break;
		}
		
		try
		{
			if (lyItem != null && imvItemIcon != null && tvItemTitle != null)
			{
				ImageLoader.getInstance().bitmapUtils.display(imvItemIcon, info.iconUrl, ImageLoader.getInstance().iconConfig);
				tvItemTitle.setText(info.name);
				lyItem.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View v)
					{
						startAppCenterDetail(info.appPkg);
					}
				});
			}
		}
		catch (Exception e)
		{ 
			e.printStackTrace();
		}
	}
	
	private void startAppCenterDetail(String appPackageName)
	{
		try
		{
			Intent intent = new Intent(Intent.ACTION_MAIN);
			intent.addCategory(Intent.CATEGORY_LAUNCHER);
			intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); 
			intent.putExtra("appPkg", appPackageName);
			intent.setComponent(new ComponentName("com.tcl.live", "com.tcl.live.activity.AppInfoActivity"));
			AppDetailActivity.this.startActivity(intent);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void register()
	{
		NotificationCenter.defaultCenter().subscriber(PackageChangeEvent.class, this);
//		try
//		{
//			//监听卸载广播
//			IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_REMOVED);
//			filter.addDataScheme("package");
//			this.registerReceiver(mReceiver, filter);
//		}
//		catch (Exception e)
//		{ 
//			e.printStackTrace();
//		}
	}
	
	private void unRegister()
	{
		
		NotificationCenter.defaultCenter().unsubscribe(PackageChangeEvent.class, this);
//		try
//		{
//			this.unregisterReceiver(mReceiver);
//		}
//		catch (Exception e)
//		{ 
//			e.printStackTrace();
//		}
	}
	
	private static final int					MSG_STOP_OK			= 100; 
	private static final int					MSG_INIT_FINISHED	= 101;
	
	@SuppressLint("HandlerLeak")
	private Handler								mHandler			= new Handler()
	{
		
		public void handleMessage(Message msg)
		{
			super.handleMessage(msg);
			
			switch (msg.what)
			{
				case MSG_STOP_OK:
					if (mPageInfo == null)
					{
						AppDetailActivity.this.finish();
						return;
					}
					
					mFlProgress.setVisibility(View.GONE);
					try
					{
						Boolean flag = (Boolean) msg.obj;
						if (flag)
						{
							mAnimOutRight = AnimationUtils.loadAnimation(AppDetailActivity.this, R.anim.anim_progress_gone);
							mAnimOutRight.setAnimationListener(mAniListener);
							mLlRun.startAnimation(mAnimOutRight); 
						} 
					}
					catch (Exception e)
					{ 
						e.printStackTrace();
					} 
					break;
					
				case MSG_INIT_FINISHED:
					if (mPageInfo == null)
					{
						AppDetailActivity.this.finish();
						return;
					}
					
					mFlProgress.setVisibility(View.GONE);
					setHeader();
					setResume();
					
					new RecommAppProtocol(mPackageName, mRecommCallback).send(); 
					break;
					
				default:
					break;
			}
		};
	};																
	
//	private BroadcastReceiver mReceiver = new BroadcastReceiver()
//	{
//		
//		@Override
//		public void onReceive(Context context, Intent intent) 
//		{ 
//			if (!Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())) 
//			{
//				return;
//			}
//			  
//			final String packageName = mPageInfo.pkgName;
//			if (!TextUtils.isEmpty(packageName) && ("package:" + packageName).equals(intent.getDataString())) 
//			{  
//				try
//				{
//					new Thread()
//					{
//						public void run() 
//						{ 
//							PageFunctionProvider.uninstalledSuccess(packageName); 
//							
//							AppDetailActivity.this.finish(); 
//							 
//						};
//					}.start();
//				}
//				catch (Exception e)
//				{ 
//					e.printStackTrace();
//				} 
//			} 
//		}
//	}; 
	
	private AnimationListener	mAniListener	= new AnimationListener()
	{
		
		@Override
		public void onAnimationStart(Animation animation)
		{
			
		}
		
		@Override
		public void onAnimationRepeat(Animation animation)
		{
			
		}
		
		@Override
		public void onAnimationEnd(Animation animation)
		{
			if (animation == mAnimOutRight)
			{ 
				setHeader();
				setResume(); 
			}
			else if (animation == mAnimInBotm)
			{
				setBtomViewVisible(true);
			}
		}
	};															
	
	private IProviderCallback<RecommAppPojo>	mRecommCallback	= new IProviderCallback<RecommAppPojo>()
	{
		
		@Override
		public void onSuccess(RecommAppPojo obj)
		{
			setRecommList(obj);
		}
		
		@Override
		public void onFailed(int code, String msg, Object obj)
		{
			
		}
		
		@Override
		public void onCancel()
		{
			
		}
	};													
}
