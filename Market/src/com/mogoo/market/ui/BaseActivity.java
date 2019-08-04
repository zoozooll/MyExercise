package com.mogoo.market.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.mogoo.market.R;
import com.mogoo.market.widget.TitleBar;

/**
 * 所有界面的基类
 */
public class BaseActivity extends Activity {
	protected Intent backIntent = null;

	protected TitleBar titlebar = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		backIntent = getIntent().getParcelableExtra(
				MarketGroupActivity.EXTRA_BACK_INTENT);

	}

	/**
	 * 获得上一级界面的Intent
	 */
	public Intent getBackIntent() {
		return backIntent;
	}

	/**
	 * 获得标题栏布局
	 */
	public TitleBar getTitlebar() {
		return titlebar;
	}

	/**
	 * 初始化标题栏
	 */
	protected void initTitleBarBase() {
		titlebar = (TitleBar) findViewById(R.id.idTitlebar);
		if (titlebar != null) {
			titlebar.setLeftBtnText(R.string.titlebar_back);

			titlebar.leftBtn.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					MarketGroupActivity.getInstance().onBackPressed(backIntent);
				}
			});
		}
	}

	/**
	 * 返回按键调用Activityroup的
	 */
	@Override
	public void onBackPressed() {
		MarketGroupActivity.getInstance().onBackPressed(backIntent);
	}
}
