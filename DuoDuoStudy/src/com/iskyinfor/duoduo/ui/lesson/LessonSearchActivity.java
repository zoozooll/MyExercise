package com.iskyinfor.duoduo.ui.lesson;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

public class LessonSearchActivity extends Activity {

	private Button backBtn;
	private ImageView searchBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lesson_sysnclass_search);

		initWidget();
	}

	private void initWidget() {
		backBtn = (Button) findViewById(R.id.lesson_sysnclass_btn);
		searchBtn = (ImageView) findViewById(R.id.lesson_aritics_search);
		backBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				LessonSearchActivity.this.finish();
			}
		});

		searchBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LessonSearchActivity.this,
						LessonActivity.class);
				startActivity(intent);
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}
}
