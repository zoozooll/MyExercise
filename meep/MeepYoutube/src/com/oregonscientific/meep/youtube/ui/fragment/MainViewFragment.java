package com.oregonscientific.meep.youtube.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oregonscientific.meep.youtube.R;

public class MainViewFragment extends Fragment {

	WebView webview;

	private String currentURL;

	public static final String TAG = "mainViewFragment";

	public void init(String url) {
		currentURL = url;
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");
		
		View view = inflater.inflate(R.layout.mainview_fragment, container, false);

		/*
			init Youtube UI
		*/

		return view;
	}

	public static MainViewFragment newInstance() {
		
		Log.d(TAG, "new Instance");
		// create a new content fragment
		MainViewFragment f = new MainViewFragment();
		return f;
	}

	
	public void updateUrl(String url) {
		Log.d(TAG, "Update URL [" + url + "] - View [" + getView() + "]");
		currentURL = url;

		webview = (WebView) getView().findViewById(R.id.webview);
		webview.loadUrl(url);
	}

	public class MyWebViewClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			Log.v(TAG, url);
			return super.shouldOverrideUrlLoading(view, url);
		}
		@Override
		public void onPageFinished(WebView view, String url) {
			// TODO Auto-generated method stub
			super.onPageFinished(view, url);
		}
	}
	
	private void animateInRight(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_in_right);
		view.startAnimation(anim);
	}
	private void animateOutRight(final WebView view) {
        Animation anim = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_right);
        view.startAnimation(anim);
    }
	private void animateInLeft(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_in_left);
		view.startAnimation(anim);
	}
	private void animateOutLeft(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(),
				R.anim.slide_out_left);
		view.startAnimation(anim);
	}
	
	public boolean goBack()
	{
		webview = (WebView) getView().findViewById(R.id.webview);
		if(webview!=null && webview.canGoBack())
		{
			webview.goBack();
			return true;
		}
		else
		{
			return false;
		}
	}
	public void goForwards()
	{
		webview = (WebView) getView().findViewById(R.id.webview);
		if(webview!=null && webview.canGoForward())
		{
			webview.goForward();
		}
	}
	public void reload()
	{
//		webview = (WebView) getView().findViewById(R.id.webview);
		if(webview!=null)
		{
			webview.reload();
		}
	}
	public String getCurrentUrl() {
		if(webview!=null)
		{
			return webview.getUrl();
		}
		return null;
	}
	
	

}
