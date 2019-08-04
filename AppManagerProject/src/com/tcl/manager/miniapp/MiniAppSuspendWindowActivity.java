package com.tcl.manager.miniapp;

import java.util.List;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.tcl.manager.activity.MainActivity;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.score.AppScoreProvider;
import com.tcl.manager.score.ScoreLevel;
import com.tcl.manager.view.MiniAppScoreCircleView;
import com.tcl.mie.manager.R;

/**
 * @Description: miniApp 悬浮窗
 * @author wenchao.zhang
 * @date 2014年12月18日 下午3:51:04
 * @copyright TCL-MIE
 */

public class MiniAppSuspendWindowActivity extends Activity {

	private Rect mRect;
	
	private RelativeLayout mRootLayout;
	private LinearLayout mContainLayout;
	
	private View mAnimView;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		setContentView(R.layout.miniapp_popwindow);
		setupView();
		setupBounds();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		finish();
	}


	private void setupView() {
		mRootLayout = (RelativeLayout)findViewById(R.id.rootLayout);
		mContainLayout = (LinearLayout)findViewById(R.id.container);
		
		mAnimView = findViewById(R.id.miniapp_window_circle);
		
		RotateAnimation ra = new RotateAnimation(359, 0, RotateAnimation.RELATIVE_TO_SELF, 0.5f, RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		ra.setDuration(1000);
		ra.setInterpolator(new LinearInterpolator());
		ra.setRepeatCount(-1);
		mAnimView.startAnimation(ra);
	}

	private void setupBounds() {
		Intent intent = getIntent();
		if (intent == null) {
			finish();
			return;
		}
		mRect = intent.getParcelableExtra("rect");
		if (mRect == null) {
			finish();
			return;
		}

//		LinearLayout.LayoutParams indicatorParams = (LinearLayout.LayoutParams) mIndicatorView
//				.getLayoutParams();
//		int widths = indicatorParams.width;
//		int marginLeft = mRect.left + (mRect.right - mRect.left - widths) / 2
//				- mRootLayout.getPaddingLeft();
//		indicatorParams.leftMargin = marginLeft;
//		mIndicatorView.setLayoutParams(indicatorParams);
//
		RelativeLayout.LayoutParams rootParams = (RelativeLayout.LayoutParams) mContainLayout
				.getLayoutParams();
//		rootParams.topMargin = mRect.bottom - (mRect.bottom - mRect.top) / 3;
		rootParams.topMargin = mRect.top - (mRect.bottom - mRect.top) / 4;
//		rootParams.topMargin = mRect.top;
		rootParams.leftMargin = mRect.left;
//		rootParams.rightMargin = mRect.right;
//		rootParams.bottomMargin = mRect.bottom;
		mRootLayout.updateViewLayout(mContainLayout, rootParams);

	}



	private void optimize() {
		AppScoreProvider.getInstance().startKillProcess(this, handler);
	}
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case MainActivity.MSG_OPTIMIZE_PROCESS_FINISH:
				List<ExpandableListItem> list = (List<ExpandableListItem>) msg.obj;
				int newScore = msg.arg1;
				break;

			default:
				break;
			}
		};
	};
}
