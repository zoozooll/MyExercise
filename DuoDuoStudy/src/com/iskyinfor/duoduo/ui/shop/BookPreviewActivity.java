package com.iskyinfor.duoduo.ui.shop;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;

/**
 * 预览/阅读
 * @author zhoushidong
 *
 */
public class BookPreviewActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookpreview_activity);
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
