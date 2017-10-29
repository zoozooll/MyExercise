package com.iskyinfor.duoduo.ui.lesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.ReadBookResource;
import com.iskyinfor.duoduo.ui.ReadSdcardResource;
import com.iskyinfor.duoduo.ui.UiHelp;

public class LessonPlayVideoActivity extends Activity {
	private WebView mWebView = null;
	private static String  fileName = ""; // 播放视频的文件名
	private String  bookId;
	private String bookName;
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.lesson_playvideo_activity);
		
		Intent intent = this.getIntent();
		fileName = intent.getStringExtra("file_name");
		
		Log.i("andy", "视频绝对路径>>>>>>=====" + fileName);
		
		/**
		 * 判断读取出的文件时PDF格式还是音视频格式文件
		 */
		if (ReadSdcardResource.isVideoFlashFile(fileName)) 
		{
			Log.i("yyj", "获取PDF的路径是:=====>>>>>>>" + fileName);
			ReadBookResource.getPDFFileIntent(LessonPlayVideoActivity.this, fileName,bookId,bookName);
		}
		else
		{
			Log.i("yyj", "获取视频的路径是:=====>>>>>>>" + fileName);
			showSWFVideoView();
		}	
	}

	// 调用flash播放器
	private void showSWFVideoView()
	{
		mWebView = (WebView) findViewById(R.id.webView);
		mWebView.getSettings().setJavaScriptEnabled(true);  
		mWebView.getSettings().setPluginsEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.setWebChromeClient(new WebChromeClient()); 
		mWebView.addJavascriptInterface(new CallJava(), "CallJava");
		mWebView.loadUrl("file:///android_asset/sample/flvflash3.html"); 
	}

	static class CallJava
	{
			public String  getFilePath()
			{
				return fileName;
			}
	}

	/***
	 * 截取search键返回主页
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{

		if (keyCode == KeyEvent.KEYCODE_SEARCH)
		{
			UiHelp.turnHome(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
	
}