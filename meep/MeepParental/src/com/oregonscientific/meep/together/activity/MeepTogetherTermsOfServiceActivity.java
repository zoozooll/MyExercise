package com.oregonscientific.meep.together.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;

import com.oregonscientific.meep.together.R;

public class MeepTogetherTermsOfServiceActivity extends Activity
{

	Button back;
	ImageButton barLeft;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        initUI();
        initListeners();
        getApplicationContext();
        
	}
	
	public void initUI()
	{
		setContentView(R.layout.layout_terms_of_service);
		back = (Button) findViewById(R.id.btnBack);
		barLeft = (ImageButton) findViewById(R.id.barImageButtonBack);
		
		//load privacy Policy
    	final WebView webview = (WebView)findViewById(R.id.webview);
    	String baseUrl = "https://meeptablet-static.commondatastorage.googleapis.com/terms.html";
    	webview.loadUrl(baseUrl);
    	webview.setBackgroundColor(0x00000000);
	}
	public void initListeners()
	{
		barLeft.setOnClickListener(new View.OnClickListener() {   
			@Override  
			public void onClick(View v) {   
				onBackPressed();
			}
		});
		back.setOnClickListener(new View.OnClickListener() {   
			@Override  
			public void onClick(View v) {   
				onBackPressed();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
		UserFunction.isTerms = true;
		super.onBackPressed();
	}
	
	
}
	