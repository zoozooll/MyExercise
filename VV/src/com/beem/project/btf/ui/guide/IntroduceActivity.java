package com.beem.project.btf.ui.guide;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.beem.project.btf.R;
import com.nostra13.universalimageloader.core.assist.deque.LinkedBlockingDeque;

public class IntroduceActivity extends Activity {
	private ViewPager viewPager;
	private ArrayList<View> pageViews;
	private LayoutInflater inflater ;
	private List<Integer> images;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.introduce_main);
		inflater = getLayoutInflater();
		int flag = getIntent().getIntExtra("which", -1);
		images = new ArrayList<Integer>();
		if (flag == 0) {
			images.add(R.drawable.introduce_1);
			images.add(R.drawable.introduce_2);
			images.add(R.drawable.introduce_3);
		} else if (flag == 1) {
			images.add(R.drawable.innerguide_cartoon_camera1);
			images.add(R.drawable.innerguide_cartoon_camera2);
			images.add(R.drawable.innerguide_cartoon_camera3);
		}
		pageViews = new ArrayList<View>();
		pageViews.add(inflater.inflate(R.layout.introduce_item02, null));
		pageViews.add(inflater.inflate(R.layout.introduce_item03, null));
		View view = inflater.inflate(R.layout.introduce_item04, null);
		pageViews.add(view);
		view.findViewById(R.id.imageView1).setOnClickListener(
				new OnClickListener() {
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						finish();
					}
				});
		viewPager = (ViewPager) findViewById(R.id.guidePages);
		// 动态设置viewpager大小
		/*int width = DimenUtils.getScreenMetrics(IntroduceActivity.this).x * 8 / 10;
		int height = (int) (width * 1.6f);
		android.widget.RelativeLayout.LayoutParams laypar = new android.widget.RelativeLayout.LayoutParams(width,
				height);
		laypar.setMargins(0, 0, 0, DimenUtils.dip2px(IntroduceActivity.this, 20));
		laypar.addRule(RelativeLayout.CENTER_IN_PARENT);
		viewPager.setLayoutParams(laypar);*/
		viewPager.setAdapter(new GuidePageAdapter());
		viewPager.setOnPageChangeListener(new GuidePageChangeListener());
	}

	class GuidePageAdapter extends PagerAdapter {
		
		private Queue<View> recyclerViews = new LinkedBlockingDeque<View>();
		@Override
		public int getCount() {
			if (images != null)
				return images.size();
			return 0;
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
		@Override
		public int getItemPosition(Object object) {
			return super.getItemPosition(object);
		}
		@Override
		public void destroyItem(View arg0, int arg1, Object v) {
			((ViewPager) arg0).removeView((View) v);
			recyclerViews.offer((View) v);
		}
		@Override
		public Object instantiateItem(View arg0, int arg1) {
			View view = recyclerViews.poll();
			if (view == null) {
				view = inflater.inflate(R.layout.introduce_item02, null);
				
			}
			ImageView imv = (ImageView) view.findViewById(R.id.imageView1);
			imv.setImageResource(images.get(arg1));
			if (arg1 == getCount() - 1) {
				view.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						finish();
					}
				});
			} else {
				view.setOnClickListener(null);
			}
			((ViewPager) arg0).addView(view);
			return view;
		}
	}

	class GuidePageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(int arg0) {
		}
	}
}
