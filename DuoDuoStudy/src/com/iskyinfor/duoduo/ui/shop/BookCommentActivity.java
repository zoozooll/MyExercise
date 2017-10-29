package com.iskyinfor.duoduo.ui.shop;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

/**
 * 评论
 * @author zhoushidong
 *
 */
public class BookCommentActivity extends Activity {
	
	/**
	 * 
	 */
	private LinearLayout llCon;
	private LinearLayout content;
	private LinearLayout date;
	//书名
	private TextView whoName;
	//书评论
	private TextView commentContent;
	//评论时间
	private TextView datetime;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookcomment_activity);
		findView();
		
	}
	
	private void findView() {
		llCon = (LinearLayout) findViewById(R.id.container);
		LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT , LayoutParams.WRAP_CONTENT);
		lp.rightMargin = 10;
		lp.gravity = Gravity.CENTER_VERTICAL;
		for (int i = 10 ; i < 20 ; i ++ ) {
			content = new LinearLayout(this);
			date = new LinearLayout(this);
			
			whoName = new TextView(this);
			
			whoName.setLayoutParams(lp);
			commentContent = new TextView(this);
			datetime = new TextView(this);
			whoName.setText(Html.fromHtml("<u>鼠鼠鼠</u>"));
			
			commentContent.setText("哥妹俩(KOKKO﹠MAY）套装—风靡新加城、马来西亚超级畅销书");
			datetime.setText("2011-5-30 18:03:48");
			whoName.setTextColor(0xff000000);
			commentContent.setTextColor(0xff000000);
			datetime.setTextColor(0xff000000);
			content.addView(whoName);
			content.addView(commentContent);
			date.addView(datetime);
			
			llCon.addView(content);
			llCon.addView(date);
		}
		
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
