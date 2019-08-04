package com.idt.bw.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;

public class StartUpActivity extends Activity implements Runnable{
	
	private SharedPreferences preferences;
	private Editor editor;  
	private boolean bool;
	 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.startuppage);
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		preferences = getSharedPreferences("isFirstUse", StartUpActivity.MODE_PRIVATE);  
		bool = preferences.getBoolean("isFirstUse", true);
		new Handler().postDelayed(StartUpActivity.this, 1500);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//判断是不是首次登录，  
        if (bool) {  
        	editor = preferences.edit();  
            //将登录标志位设置为false，下次登录时不在显示首次登录界面  
            editor.putBoolean("isFirstUse", false);  
            editor.commit(); 
            Intent intent = new Intent(StartUpActivity.this,GuideActivity.class);  
            startActivity(intent);  
            //finish();
        }else{
        	Intent intent = new Intent(StartUpActivity.this,ChooseUserActivity.class);  
            startActivity(intent);  
            finish();
        }
	}
	
	
}
