/**
 * 
 */
package com.tcl.manager.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.tcl.framework.notification.NotificationCenter;
import com.tcl.framework.notification.Subscriber;
import com.tcl.manager.adapter.OptimizedAdapter;
import com.tcl.manager.adapter.StatisticAdapter;
import com.tcl.manager.analyst.AppFilter;
import com.tcl.manager.base.OptimizedAppInfo;
import com.tcl.manager.base.StatisticInfo;
import com.tcl.manager.blackwhitelist.WhiteListSharedManager;
import com.tcl.manager.firewall.FirewallManager;
import com.tcl.manager.firewall.IptablesSwitcher;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.score.PackageChangeEvent;
import com.tcl.manager.util.TCLThreadPool;
import com.tcl.mie.manager.R;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v4.widget.SimpleCursorAdapter.ViewBinder;
import android.view.View;
import android.view.ViewStub;

/**
 * @author zuokang.li
 *
 */
public class OptimizedActivity extends BaseActivity implements View.OnClickListener, ExpandableListView.OnChildClickListener
	, Subscriber<PackageChangeEvent>{
	
	private ImageView imageBack;
	private TextView textTitle;
	private View header_action_bar_rl_left;
	private  ExpandableListView listContent;
	private ViewStub vs_progress;
	private OptimizedAdapter mAdapter;
	private Handler mHandler = new MyHandler();
	private List<List<OptimizedAppInfo>> data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();
		setActionBarTitle();
	}

	@Override
	protected void onStart() {
		setList();
		super.onStart();
		regrestReceive();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		unregrestReceive();
	}

	private void initview() {
		setContentView(R.layout.optimized_activity);
		imageBack = (ImageView) findViewById(R.id.header_action_bar_iv_left);
		header_action_bar_rl_left = findViewById(R.id.header_action_bar_rl_left);
		textTitle = (TextView) findViewById(R.id.header_action_bar_tv_left);
		listContent = (ExpandableListView) findViewById(R.id.listContent);
		vs_progress = (ViewStub) findViewById(R.id.vs_progress);
		vs_progress.inflate();
		listContent.setOnChildClickListener(this);
	}
	
	private void setActionBarTitle() {
		textTitle.setText(R.string.optimized_actionbar_title);
		header_action_bar_rl_left.setOnClickListener(this);
	}
	
	private List<List<OptimizedAppInfo>> getData() {
		// load auto start black list;
		List<OptimizedAppInfo> listBattery = new ArrayList<OptimizedAppInfo>();
		Map<String, ?> map = AutoStartBlackList.getInstance().get();
		Collection<String> whiteList = WhiteListSharedManager.getSingleInstance().getAllWhiteList();
		if (map != null) {
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				if (whiteList.contains(entry.getKey())) {
					continue;
				}
				OptimizedAppInfo item = loadAppInfo(this.getApplicationContext(), entry.getKey());
				item.onOff = (Boolean) entry.getValue();
				listBattery.add(item);
			}
		}
		// load firewall blocks
		List<OptimizedAppInfo> listNetBlocks = new ArrayList<OptimizedAppInfo>();
		map = FirewallManager.getSingleInstance().getAllBlockPackages();
		if (map != null) {
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				if (!Boolean.TRUE.equals(entry.getValue())) {
					continue;
				}
				if (whiteList.contains(entry.getKey())) {
					continue;
				}
				OptimizedAppInfo item = loadAppInfo(this.getApplicationContext(), entry.getKey());
				if (item == null) {
					continue;
				}
				item.onOff = true;
				listNetBlocks.add(item);
			}
		}
		
		List<List<OptimizedAppInfo>> data = new ArrayList<List<OptimizedAppInfo>>();
		data.add(listBattery);
		data.add(listNetBlocks);
		return data;
	}
	
	private void setList() {
		
		/*mAdapter = new OptimizedAdapter(this, getData());
		listContent.setAdapter(mAdapter);
		int groupCount = listContent.getCount();
		for (int i = 0; i < groupCount; i++) {
			listContent.expandGroup(i);
		}
		*/
		vs_progress.setVisibility(View.VISIBLE);
		TCLThreadPool.runOnNonUIThread(new Runnable() {
			@Override
			public void run() {
				data = getData();
				mHandler.sendEmptyMessage(0);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == header_action_bar_rl_left) {
			finish();
		} 
		
	}
	
	@Override
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		OptimizedAppInfo obj = mAdapter.getChild(groupPosition, childPosition);
		if(obj != null) {
			Intent intent = new Intent(this, AppDetailActivity.class);
			intent.putExtra(AppDetailActivity.EXTRA_PACKGENAME, obj.pkgName);
			startActivity(intent);
		}
		return false;
	}
	
	private void refreshListView() {
		if (mAdapter == null) {
			mAdapter = new OptimizedAdapter(OptimizedActivity.this, data);
		} else {
			mAdapter.refreshData(data);
		}
		
		if (listContent.getAdapter() == null) {
			listContent.setAdapter(mAdapter);
			int groupCount = listContent.getCount();
			for (int i = 0; i < groupCount; i++) {
				listContent.expandGroup(i);
			}
		} else {
			mAdapter.notifyDataSetChanged();
		}
		vs_progress.setVisibility(View.GONE);
	}
	
	private void regrestReceive() {
		NotificationCenter.defaultCenter().subscriber(PackageChangeEvent.class, this);
	}
	
	private void unregrestReceive() {
		NotificationCenter.defaultCenter().unsubscribe(PackageChangeEvent.class, this);
	}
	
	
	private static OptimizedAppInfo loadAppInfo(Context context, String pkgName) {
		PackageManager pm = context.getApplicationContext().getPackageManager();
		OptimizedAppInfo info = new OptimizedAppInfo();
		info.pkgName = pkgName;
		ApplicationInfo appInfo;
		try {
			appInfo = pm.getApplicationInfo(info.pkgName, 0);
			info.appName = pm.getApplicationLabel(appInfo).toString();
			//info.icon = pm.getActivityLogo(pm.getLaunchIntentForPackage(pkgName));
			info.icon = pm.getApplicationIcon(appInfo);
		} catch (Exception e1) {
			e1.printStackTrace();
			info = null;
		}
		return info;
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
	
	@Override
	public void onEvent(PackageChangeEvent arg0) {
		
		final String packageName = arg0.packageName;
		if (data == null) {
			return;
		}
		for (List<OptimizedAppInfo> list : data) {
			
			if (list == null) {
				continue;
			}
			for (OptimizedAppInfo info :list) {
				if (info.pkgName.equals(packageName)) {
					list.remove(info);
					break;
				}
			}
		}
		mAdapter.notifyDataSetChanged();
	}
	
}
