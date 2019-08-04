/**
 * 
 */
package com.mogoo.ping.ctrl;

import android.webkit.WebView;

/**
 * @author Aaron Lee
 * the Controller control the webview
 * @Date ����10:11:42  2012-9-19
 */
public class WebViewController {

	private WebView mWebView;
	private static final String HOME_FIRSTPAGE = "http://www.mogu123.cn/";

	public WebViewController(WebView webView) {
		super();
		this.mWebView = webView;
		mWebView.getSettings().setJavaScriptEnabled(true);
	}
	
	private void startConnect() {
		mWebView.loadUrl(HOME_FIRSTPAGE);
	}

	/**
	 * 
	 * @Title beginShowingInfront
	 * @Description when the tabWidget content show in front of screen.
	 * @Date 2012-10-16 上午10:00:15
	 * @Version 1.0
	 */
	public void beginShowingInfront() {
		startConnect();
	}
	
	
}
