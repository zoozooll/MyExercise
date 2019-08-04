package com.beem.project.btf.ui.fragment;

import com.beem.project.btf.ui.activity.MainpagerActivity;

import de.greenrobot.event.EventBus;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;

public abstract class MainpagerAbstractFragment extends Fragment {
	protected OnMainpagerFramentCallback callback;
	protected Activity mContext;
	private String tagValue;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity;
		if (activity instanceof MainpagerActivity) {
			callback = (OnMainpagerFramentCallback) activity;
		}
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		tagValue = getArguments().getString("tag");
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public void onResume() {
//		MobclickAgent.onPageStart(tagValue);
		super.onResume();
	}
	@Override
	public void onPause() {
//		MobclickAgent.onPageEnd(tagValue); 
		super.onPause();
	}
	public abstract void autoAuthentificateCompleted();
	public static MainpagerAbstractFragment newInstance(String tag) {
		MainpagerAbstractFragment f = null;
		if (TabName.FRIEND.toString().equals(tag)) {
			f = new MainpagerContactFragment();
		} else if (TabName.TIMEFLY.toString().equals(tag)) {
			f = new MainpagerFragment();
		} else if (TabName.SHARE.toString().equals(tag)) {
			f = new MainpagerBbsFragment();
		} else if (TabName.SESSION.toString().equals(tag)) {
			f = new MainpagerMessagesFragment();
		}
		Bundle args = new Bundle();
		args.putString("tag", tag);
		f.setArguments(args);
		return f;
	}

	public static interface OnMainpagerFramentCallback {
		public void onSwitchFragment(TabName tag);
		public boolean isAutoAuthentificateCompleted();
	}

	public enum TabName {
		FRIEND, TIMEFLY, SHARE, SESSION;
	}
}
