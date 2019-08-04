package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.fragment.ImageGridFragment;
import com.beem.project.btf.ui.fragment.ImageGridFragment.ActivityType;
import com.beem.project.btf.ui.fragment.ImageGridFragment.DecadeType;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.moonlight.smooth.ViewPagerTabIndicator;

public class TimeCameraManageActivity extends FragmentActivity {
	private static final String TAG = "TimeCameraManageActivity";
	private Context mContext;
	private ViewPager mPager;
	private int currIndex = 0;
	private ArrayList<Fragment> fragmentList;
	private ViewPagerTabIndicator decade_wraper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_camera_loadimg);
		mContext = this;
		currIndex = getIntent().getIntExtra("MangePageIndex", 0);
		decade_wraper = (ViewPagerTabIndicator) findViewById(R.id.linearLayout1);
		decade_wraper.setBottomLineColor(Color.parseColor("#17aff1"));
		decade_wraper.setBottomLineHeight((int) BBSUtils.toPixel(mContext,
				TypedValue.COMPLEX_UNIT_DIP, 3));
		decade_wraper.setColumn(3);
		inithead();
		initTextView();
		InitViewPager();
	}
	/**
	 * 导航条设置
	 */
	private void inithead() {
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setVisibility(View.GONE);
		btBack.setTextAndImgRes("素材管理", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
	}
	/**
	 * 初始化viewpager
	 */
	private void InitViewPager() {
		fragmentList = new ArrayList<Fragment>();
		mPager = (ViewPager) findViewById(R.id.vPager);
		// 关闭预加载，默认一次只加载一个Fragment
		mPager.setOffscreenPageLimit(1);
		for (DecadeType dt : DecadeType.values()) {
			Fragment tempFragment = ImageGridFragment.newInstance(dt.getNum(),
					ActivityType.Manage.toString());
			fragmentList.add(tempFragment);
		}
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		OnPageChangeListener pageChangeListener = new MyOnPageChangeListener();
		mPager.setOnPageChangeListener(pageChangeListener);
		decade_wraper.setViewPager(mPager, pageChangeListener);
		decade_wraper.setCurrentItem(currIndex);
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.i(TAG, "onDestroy");
		mPager.removeAllViews();
	}
	/**
	 * 初始化页签
	 */
	private void initTextView() {
		makeTextview();
	}
	private void makeTextview() {
		for (DecadeType dt : DecadeType.values()) {
			TextView temp = new TextView(this);
			// 设置样式
			temp.setText(dt.getNum() + "年代");
			temp.setClickable(true);
			try {
				XmlPullParser xrp = getResources().getXml(
						R.color.new_friend_tv_selector);
				ColorStateList csl = ColorStateList.createFromXml(
						getResources(), xrp);
				temp.setTextColor(csl);
			} catch (Exception e) {
			}
			temp.setGravity(Gravity.CENTER);
			android.widget.LinearLayout.LayoutParams lp = new android.widget.LinearLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
			lp.weight = 1.0f;
			temp.setLayoutParams(lp);
			decade_wraper.addTab(temp, -1, dt);
		}
	}

	/**
	 * 页面转换监听器
	 */
	private class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(final int arg0) {
			// 头标移动动画
			currIndex = arg0;
		}
	}

	/**
	 * viewpager页面fragment适配器
	 */
	private class MyFragmentPagerAdapter extends FragmentPagerAdapter {
		ArrayList<Fragment> list;

		public MyFragmentPagerAdapter(FragmentManager fm,
				ArrayList<Fragment> list) {
			super(fm);
			this.list = list;
		}
		@Override
		public int getCount() {
			return list.size();
		}
		@Override
		public Fragment getItem(int arg0) {
			return list.get(arg0);
		}
	}
}