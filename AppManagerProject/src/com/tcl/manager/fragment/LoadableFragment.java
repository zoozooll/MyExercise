/**
 * 
 */
package com.tcl.manager.fragment;

import com.tcl.framework.log.NLog;
import com.tcl.mie.manager.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListView;

/**
 * @author zuokang.li
 *
 */
public abstract class LoadableFragment extends Fragment {
	
	private static final String TAG = "LoadableFragment";
	
	protected ViewGroup rootView;
	protected ListView listView;
	protected View loadingView;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		NLog.d(TAG, "onCreateView");
		rootView = (ViewGroup) inflater
				.inflate(R.layout.managerlist_content, container, false);
		listView = (ListView) rootView.findViewById(R.id.list_content);
		loadingView = inflater.inflate(
				R.layout.loading_view, null);
		return rootView;
	}

	@Override
	public void onResume() {
		startLoaing();
		super.onResume();
	}

	@Override
	public void onPause() {
		stopLoaing();
		super.onPause();
	}

	protected void startLoaing() {
		rootView.addView(loadingView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
	}
	
	protected void stopLoaing(){
		if (rootView != null) {
			rootView.removeView(loadingView);
		}
	}
	
	protected abstract void initializedView(View rootView);
}
