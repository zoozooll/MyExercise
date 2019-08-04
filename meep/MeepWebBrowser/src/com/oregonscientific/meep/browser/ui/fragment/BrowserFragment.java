package com.oregonscientific.meep.browser.ui.fragment;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.oregonscientific.meep.browser.BrowserUtility;
import com.oregonscientific.meep.browser.Consts;
import com.oregonscientific.meep.browser.R;
import com.oregonscientific.meep.browser.WebBrowserActivity;
import com.oregonscientific.meep.browser.database.Bookmark;
import com.oregonscientific.meep.browser.database.History;
import com.oregonscientific.meep.customdialog.CommonPopup;

public class BrowserFragment extends Fragment {

	WebView webview;

	private String currentURL;

	public static final String TAG = "BrowserFragment";

	public void init(String url) {
		currentURL = url;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreateView");

		View view = inflater.inflate(R.layout.browser_fragment, container, false);
		webview = (WebView) view.findViewById(R.id.webview);

		webview.setWebViewClient(new MyWebViewClient());
		webview.setWebChromeClient(new MyWebChromeClient());
		webview.getSettings().setPluginsEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setSupportZoom(true);
		webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webview.getSettings().setAllowFileAccess(true);

		//for html5
		webview.getSettings().setDomStorageEnabled(true);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
		webview.getSettings().setUseWideViewPort(true);
		webview.getSettings().setLoadWithOverviewMode(true);
		webview.getSettings().setSavePassword(true);
		webview.getSettings().setSaveFormData(true);
		webview.getSettings().setGeolocationEnabled(true);
		webview.getSettings().setGeolocationDatabasePath("/data/data/com.oregonscientific.meep.browser/databases/");
		    
		if (currentURL != null) {
			webview.loadUrl(currentURL);
		}
		return view;
	}

	public static BrowserFragment newInstance() {

		Log.d(TAG, "new Instance");
		// create a new content fragment
		BrowserFragment f = new BrowserFragment();
		return f;
	}

	public void updateUrl(String url) {
		Log.d(TAG, "Update URL [" + url + "] - View [" + getView() + "]");
		currentURL = url;

		webview = (WebView) getView().findViewById(R.id.webview);
		webview.loadUrl(url);
	}

	private class MyWebChromeClient extends WebChromeClient {
		@Override
		public void onReceivedIcon(WebView view, Bitmap icon) {
			super.onReceivedIcon(view, icon);
		}
	}

	public class MyWebViewClient extends WebViewClient {
		private int webViewPreviousState;
		private final int PAGE_STARTED = 0x1;
		private final int PAGE_REDIRECTED = 0x2;

		@Override
		public boolean shouldOverrideUrlLoading(WebView view,
				String urlNewString) {
			webViewPreviousState = PAGE_REDIRECTED;
			
			if(((WebBrowserActivity)getActivity()).isAccessiableUrl(urlNewString) || isRecommended)
			{
				return super.shouldOverrideUrlLoading(view, urlNewString);
			}
			else
			{
				view.stopLoading();
				BrowserUtility.alertMessage(getActivity(), R.string.browser_title_blocked, R.string.cannot_access_website);
				((WebBrowserActivity) getActivity()).resetSearchBox();
				return false;
			}
		}

		@Override
		public void onPageStarted(final WebView view, String url, Bitmap favicon) {
			// start
			super.onPageStarted(view, url, favicon);

//			// TODO:(blacklist) pre-check whether url is safe
//			if (IsIgnoreWebsite(url)) {
//				view.stopLoading();
//				CommonPopup popup = new CommonPopup(getActivity(), R.string.browser_title_blocked, R.string.browser_msg_web_blocked);
//				popup.show();
//				// reset data
//				((WebBrowserActivity) getActivity()).updateSearchBox(null, null);
//				return;
//			}
			if (url.contains("http://get.adobe.com/flashplayer/mobile")) {
				url = "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.115.20/install_flash_player_ics.apk";
			}

			webViewPreviousState = PAGE_STARTED;
			BrowserUtility.printLogcatMessageWithTimeStamp("onPageStart");
			showLoadingDialog();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			super.onReceivedError(view, errorCode, description, failingUrl);
//			// TODO: - do something else in here if the site is down
//			String url = "https://www.google.com/search?safe=active&q="
//					+ failingUrl;
//			view.loadUrl(url);
			
			dismissLoadingDialog();
			BrowserUtility.printLogcatMessageWithTimeStamp("onReceivedError");
			CommonPopup popup = new CommonPopup(getActivity(), R.string.browser_title_blocked, getActivity().getResources().getString(R.string.load_website_fail)+" "+description);
			popup.show();
		}
		
		@Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            super.onReceivedSslError(view, handler, error);
 
            // this will ignore the Ssl error and go forward to the site
            handler.proceed();
        }
		
		@Override
		public void onPageFinished(WebView view, String url) {
			Log.v(TAG, "load:" + url);
			try {
				if (webViewPreviousState == PAGE_STARTED) {
					dismissLoadingDialog();
					if (getActivity() instanceof WebBrowserActivity) {
						((WebBrowserActivity) getActivity()).updateSearchBox(url, webview.getFavicon());
						((WebBrowserActivity) getActivity()).recordHistoryItem(getHistoryObject());
						Log.d(TAG, "really load:" + url);
						getScreenshot(view, url);
					}
					BrowserUtility.printLogcatMessageWithTimeStamp("onPageFinished -- stop loading");
					BrowserUtility.printLogcatMessageWithTimeStamp("EOF");
				}
			} catch (Exception e) {
				// Log.d(TAG, e.getMessage());
			}
		}

	}

	private void animateInRight(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_right);
		view.startAnimation(anim);
	}

//	public boolean IsIgnoreWebsite(String url) {
//		// ExecutorService service = Executors.newSingleThreadExecutor();
//		// service.execute(new Runnable() {
//		//
//		// @Override
//		// public void run() {
//		PermissionManager permissionManager = (PermissionManager) ServiceManager.getService(getActivity().getApplicationContext(), ServiceManager.PERMISSION_SERVICE);
//		String id = BrowserUtility.getAccountID(getActivity());
//		if (id == null || id.equals("")) {
//			BrowserUtility.printLogcatDebugMessage("ID is Empty");
//		} else {
//			boolean isBlocked = permissionManager.isItemInBlacklist(id, "", url);
//			BrowserUtility.printLogcatDebugMessage(url + " isBlocked : "+isBlocked);
//			return isBlocked;
//		}
//		// }
//		// });
//		return false;
//	}

	private void animateOutRight(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_right);
		view.startAnimation(anim);
	}

	private void animateInLeft(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_in_left);
		view.startAnimation(anim);
	}

	private void animateOutLeft(final WebView view) {
		Animation anim = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_out_left);
		view.startAnimation(anim);
	}

	public boolean canGoBack() {
		if (webview != null && webview.canGoBack()) {
			return true;
		} else {
			return false;
		}
	}

	public void goBack() {
		if (webview != null && webview.canGoBack()) {
			webview.goBack();
		}
	}

	public void goForwards() {
		if (webview != null && webview.canGoForward()) {
			webview.goForward();
		}
	}

	public void reload() {
		// webview = (WebView) getView().findViewById(R.id.webview);
		if (webview != null) {
			webview.reload();
		}
	}

	public String getCurrentUrl() {
		if (webview != null) {
			return webview.getUrl();
		}

		return null;
	}

	public Bookmark getBookmarkObject() {

		String name = webview.getTitle();
		String url = webview.getUrl();
		Bitmap favicon = webview.getFavicon();
		Bookmark bookmark = new Bookmark(name, url);
		if (favicon != null)
			bookmark.setFavicon(Bookmark.getByteBitmap(favicon));
		return bookmark;
	}

	public History getHistoryObject() {

		String name = webview.getTitle();
		String url = webview.getUrl();
		Bitmap favicon = webview.getFavicon();
		History item = new History(name, url);
		if (favicon != null)
			item.setFavicon(Bookmark.getByteBitmap(favicon));
		return item;
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (getActivity() instanceof WebBrowserActivity) {
			((WebBrowserActivity) getActivity()).resetSearchBox();
		}
	}

	public void getScreenshot(WebView view, String url) {
		// Picture picture = view.capturePicture();
		// Bitmap b = Bitmap.createBitmap(800, 444, Bitmap.Config.ARGB_8888);
		// Canvas c = new Canvas(b);
		// picture.draw(c);

		// image
		view.setDrawingCacheEnabled(true);
		Bitmap b = Bitmap.createBitmap(webview.getDrawingCache());
		view.setDrawingCacheEnabled(false);
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(getActivity().getApplicationContext().getFilesDir()
					+ Consts.PATH_RECENTLY_PICTURE);
			if (fos != null) {
				b.compress(Bitmap.CompressFormat.JPEG, 100, fos);
				fos.close();
			}
		} catch (Exception e) {
		}

		// url
		try {
			FileWriter fw = new FileWriter(getActivity().getApplicationContext().getFilesDir()
					+ Consts.PATH_RECENTLY_TEXT, false);
			fw.write(url);
			fw.close();
		} catch (Exception e) {
		}
	}
	private boolean isRecommended = false;

	public void init(String url, boolean isRecommended) {
		this.isRecommended = isRecommended;
		init(url);
	}

	public void updateUrl(String url, boolean isRecommended) {
		this.isRecommended = isRecommended;
		updateUrl(url);
		
	}
	
	private void showLoadingDialog()
	{
		if(getActivity() instanceof WebBrowserActivity)
		{
			((WebBrowserActivity)getActivity()).showLoadingDialog();
		}
	}
	private void dismissLoadingDialog()
	{
		if(getActivity() instanceof WebBrowserActivity)
		{
			((WebBrowserActivity)getActivity()).stopLoadingDialog();
		}
	}

	public void stopLoading() {
		if(webview!=null)
		{
			webview.stopLoading();
		}
	}

}
