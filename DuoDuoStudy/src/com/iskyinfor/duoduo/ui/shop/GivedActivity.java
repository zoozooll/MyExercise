package com.iskyinfor.duoduo.ui.shop;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 
 * 赠送
 */
public class GivedActivity extends Activity implements OnClickListener {
	
	
	private Button btnGived;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.gived_activity);
		findView();
		setEvent();
	}
	
	private void findView() {
		
		btnGived = (Button) findViewById(R.id.btn_gived);

	}
	
	private void setEvent() {
		btnGived.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_gived:
			Intent intent = new Intent();
			intent.setClass(this, GivedListActivity.class);
			startActivity(intent);
			break;
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
