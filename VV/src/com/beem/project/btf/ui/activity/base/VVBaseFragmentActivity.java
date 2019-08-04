package com.beem.project.btf.ui.activity.base;

import com.beem.project.btf.ui.receiver.base.VVBaseBroadCastReceiver;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

/**
 * @func VV项目中FragmentActivit的基类，存放公用信息
 * @author yuedong bao
 * @time 2014-12-17 上午10:33:35
 */
public abstract class VVBaseFragmentActivity extends FragmentActivity implements
		IVVActivityAction {
	private static final String TAG = "VVBaseFragmentActivity";
	protected SharedPreferences mSettings;
	protected Context mContext;
	protected float density;
	private BroadCastReceiverCollector collectors;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//LogUtils.i("onCreate:" + getClass().getSimpleName());
		ActivityController.getInstance().addActivity(this);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		mContext = this;
		mSettings = PreferenceManager.getDefaultSharedPreferences(this);
		collectors = new BroadCastReceiverCollector(this);
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		density = dm.density;
		registerVVBroadCastReceivers();
	};
	@Override
	public void setupNavigateView() {
	}
	@Override
	public final void unRegisterVVBroadCastReceivers() {
		collectors.unRegisterBroadCastReceivers();
	};
	@Override
	public void registerVVBroadCastReceiver(
			VVBaseBroadCastReceiver castReceiver, IntentFilter filter) {
		collectors.registerBroadCastReceiver(castReceiver, filter);
	};
	@Override
	public final void unRegisterVVBroadCastReceiver(
			VVBaseBroadCastReceiver castReceiver) {
		collectors.unRegisterBroadCastReceiver(castReceiver);
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		//LogUtils.i("onDestroy:" + getClass().getSimpleName());
		ActivityController.getInstance().removeActivity(this);
		unRegisterVVBroadCastReceivers();
	}
	@Override
	protected void onPause() {
		super.onPause();
//		MobclickAgent.onPause(this);
	}
	@Override
	protected void onResume() {
		super.onResume();
//		MobclickAgent.onResume(this);
	}
	@Override
	protected void onStart() {
		super.onStart();
		//LogUtils.i("onStart:" + getClass().getSimpleName());
	}
	@Override
	protected void onStop() {
		super.onStop();
		//LogUtils.i("onStop:" + getClass().getSimpleName());
	}
	@Override
	protected void onRestart() {
		super.onRestart();
		//LogUtils.i("onRestart:" + getClass().getSimpleName());
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
		super.onActivityResult(requestCode, resultCode, arg2);
		//LogUtils.i("onActivityResult:" + getClass().getSimpleName() + " requestCode:" + requestCode + " resultCode:"
		//				+ resultCode);
	}
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		//LogUtils.i("onSaveInstanceState:" + getClass().getSimpleName());
	}
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		//LogUtils.i("onRestoreInstanceState:" + getClass().getSimpleName());
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		//LogUtils.i("onNewIntent:" + getClass().getSimpleName());
	}
	// 获取根视图
	public View getRootView() {
		return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
	}
}
