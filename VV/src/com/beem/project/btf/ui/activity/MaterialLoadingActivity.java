package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.Intent;
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
import android.view.Window;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.fragment.BaseImageGridFragment.ActivityType;
import com.beem.project.btf.ui.fragment.BaseImageGridFragment.IMaterialType;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.moonlight.smooth.ViewPagerTabIndicator;

/**
 * @ClassName: MaterialLoadingActivity
 * @Description: 素材显示界面
 * @author: yuedong bao
 * @date: 2015-11-17 下午2:23:10
 */
public class MaterialLoadingActivity extends FragmentActivity {
	private static final String TAG = "TimeCameraLoadimgActivity";
	private Context mContext;
	private ViewPager mPager;
	private IMaterialType materialType;
	private ArrayList<Fragment> fragmentList;
	private ViewPagerTabIndicator decade_wraper;

	/**
	 * @Title: launch
	 * @Description:
	 * @param: @param context
	 * @param: @param materialType 素材类型
	 * @return: void
	 * @throws:
	 */
	public static void launch(Context context, IMaterialType materialType) {
		Intent intent = new Intent(context, MaterialLoadingActivity.class);
		intent.putExtra("MaterialType", materialType);
		context.startActivity(intent);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.time_camera_loadimg);
		mContext = this;
		decade_wraper = (ViewPagerTabIndicator) findViewById(R.id.linearLayout1);
		decade_wraper.setBottomLineColor(Color.parseColor("#17aff1"));
		decade_wraper.setBottomLineHeight((int) BBSUtils.toPixel(mContext,
				TypedValue.COMPLEX_UNIT_DIP, 3));
		materialType = (IMaterialType) getIntent().getSerializableExtra(
				"MaterialType");
		decade_wraper.setColumn(materialType.getColumn());
		inithead();
		initTextView();
		InitViewPager();
	}
	/**
	 * 导航条设置
	 */
	private void inithead() {
		CustomTitleBtn btBack = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		CustomTitleBtn btright = (CustomTitleBtn) findViewById(R.id.rightbtn1);
		TextView headTitle = (TextView) findViewById(R.id.topbar_title);
		headTitle.setVisibility(View.GONE);
		btBack.setTextAndImgRes("素材下载", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		btBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		btright.setImgResource(R.drawable.manage_materials_icon)
				.setTextViewVisibility(View.GONE).setViewPaddingRight()
				.setVisibility(View.VISIBLE);
		btright.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 跳转到管理界面
				MaterialManageActivity.launch(MaterialLoadingActivity.this,
						materialType);
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
		for (IMaterialType type : materialType.getAllMembers()) {
			fragmentList.add(type.getFragment(ActivityType.Loadimg));
		}
		mPager.setAdapter(new MyFragmentPagerAdapter(
				getSupportFragmentManager(), fragmentList));
		OnPageChangeListener pageChangeListener = new MyOnPageChangeListener();
		mPager.setOnPageChangeListener(pageChangeListener);
		decade_wraper.setViewPager(mPager, pageChangeListener);
		decade_wraper.setCurrentItem(materialType.getIndex());
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
		for (IMaterialType dt : materialType.getAllMembers()) {
			TextView temp = new TextView(this);
			// 设置样式
			temp.setText(dt.getTitle());
			try {
				XmlPullParser xrp = getResources().getXml(
						R.color.new_friend_tv_selector);
				ColorStateList csl = ColorStateList.createFromXml(
						getResources(), xrp);
				temp.setTextColor(csl);
			} catch (Exception e) {
			}
			temp.setGravity(Gravity.CENTER);
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
			materialType = materialType.getAllMembers()[arg0];
		}
	}

	/**
	 * viewpager页面fragment适配器
	 */
	public static class MyFragmentPagerAdapter extends FragmentPagerAdapter {
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
