package com.iskyinfor.duoduo.ui.shop;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

/**
 * 详情简介
 * @author zhoushidong
 *
 */
public class BookIntroActivity extends Activity {
	
	private TextView textIntro;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookintro_activity);
		findView();
		setValue();
	}

	private void setValue() {
	}

	private void findView() {
		textIntro = (TextView) findViewById(R.id.intro);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
