/**
 * 
 */
package com.tcl.manager.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.adapter.StatisticAdapter;
import com.tcl.manager.base.StatisticInfo;
import com.tcl.manager.score.PackageChangeEvent;
import com.tcl.manager.score.PageDataProvider;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.util.TCLThreadPool;
import com.tcl.mie.manager.R;

/**
 * @author zuokang.li
 *
 */
public class AppStatisticActivity extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener, Subscriber<PackageChangeEvent> {
	
	private ImageView imageBack;
	private View header_action_bar_rl_left;
	private TextView textTitle;
	private ListView listContent;
	private ViewStub vs_progress;
	
	private StatisticAdapter mAdapter;
	private Handler mHandler = new MyHandler();
	private List<StatisticInfo> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initViews();
		setActionBarTitle();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		setList();
		regrestReceive();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregrestReceive();
	}

	@Override
	public void onClick(View v) {
		if (v == header_action_bar_rl_left) {
			finish();
		} 
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		StatisticInfo obj = mAdapter.getItem(position);
		if (obj != null) {
			Intent intent = new Intent(this, AppDetailActivity.class);
			intent.putExtra(AppDetailActivity.EXTRA_PACKGENAME, obj.pkgName);
			startActivity(intent);
		}
		
	}
	
	private void initViews() {
		setContentView(R.layout.statistic_activity);
		imageBack = (ImageView) findViewById(R.id.header_action_bar_iv_left);
		header_action_bar_rl_left = findViewById(R.id.header_action_bar_rl_left);
		textTitle = (TextView) findViewById(R.id.header_action_bar_tv_left);
		listContent = (ListView) findViewById(R.id.listContent);
		vs_progress = (ViewStub) findViewById(R.id.vs_progress);
		vs_progress.inflate();
		listContent.setOnItemClickListener(this);
	}
	
	private void setActionBarTitle() {
		textTitle.setText(R.string.statistic_actionbar_title);
		header_action_bar_rl_left.setOnClickListener(this);
	}
	
	private void refreshListView() {
		if (mAdapter == null) {
			mAdapter = new StatisticAdapter(this, data);
		} else {
			mAdapter.setData(data);
		}
		
		if (listContent.getAdapter() == null) {
			listContent.setAdapter(mAdapter);
		} else {
			mAdapter.notifyDataSetChanged();
		}
		vs_progress.setVisibility(View.GONE);
	}
	
	private List<StatisticInfo> getData() {
		List<StatisticInfo> data = null;
		PageDataProvider provider = PageDataProvider.getInstance();
		if (provider.isInit()) {
			List<PageInfo> apps = provider.getAll();
			data = new ArrayList<StatisticInfo>();
			for (PageInfo info : apps) {
				StatisticInfo item = new StatisticInfo();
				item.pkgName = info.pkgName;
				item.appName = info.appName.trim();
				item.batteryPercent = info.batteryPercent;
				item.dataPerDay = info.averageMobileSize;
				item.dataToday = info.todayMobileSize;
				item.frequencySeconds = info.averageUseTime;
				item.frequencyTimes = (int) info.averageUseCount;
				item.isRunning = info.isRunning;
				item.memory = info.memorySize;
			    item.score = PageFunctionProvider.getPageInfoScore(info);
				item.level = info.frequencyLevel;
				data.add(item);
			}
			java.util.Collections.sort(data);
		}
		return data;
	}
	
	private void setList() {
		
		vs_progress.setVisibility(View.VISIBLE);
		TCLThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				data = getData();
				mHandler.sendEmptyMessage(0);
			}
		});
		
		
	}
	
	private void regrestReceive() {
//		IntentFilter filter = new IntentFilter();
//		filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
//		filter.addDataScheme("package");
//		registerReceiver(mRemoveReceiver, filter);
		NotificationCenter.defaultCenter().subscriber(PackageChangeEvent.class, this);
	}
	
	private void unregrestReceive() {
//		unregisterReceiver(mRemoveReceiver);
		NotificationCenter.defaultCenter().unsubscribe(PackageChangeEvent.class, this);
	}

	private class MyHandler extends Handler {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 0:
				
				refreshListView();
				break;

			default:
				break;
			}
			super.handleMessage(msg);
		}
		
	}
	
//	private class RemoveApplicationReceiver extends BroadcastReceiver{
//
//		@Override
//		public void onReceive(Context context, Intent intent) {
//			//判断它是否是为电量变化的Broadcast Action
//			if(Intent.ACTION_PACKAGE_REMOVED.equals(intent.getAction())){
//				Uri uri = intent.getData();
//				final String packageName = uri.getSchemeSpecificPart();
//				if (data == null) {
//					return;
//				}
//				for (StatisticInfo info : data) {
//					if (info.pkgName.equals(packageName)) {
//						data.remove(info);
//						break;
//					}
//				}
//				mAdapter.notifyDataSetChanged();
//			}
//		}
//		
//	}

	@Override
	public void onEvent(PackageChangeEvent arg0) {
		
		final String packageName = arg0.packageName;
		if (data == null) {
			return;
		}
		for (StatisticInfo info : data) {
			if (info.pkgName.equals(packageName)) {
				data.remove(info);
				break;
			}
		}
		mAdapter.notifyDataSetChanged();
	}

}
