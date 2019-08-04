package com.beem.project.btf.ui.guide;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.beem.project.btf.R;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.MainpagerActivity;
import com.beem.project.btf.utils.SharedPrefsUtil;

//第一次运行的引导页代码
public class WelcomeActivity extends Activity implements OnPageChangeListener,
		OnClickListener {
	private static final String TAG = "WelcomeActivity";
	private Context mContext;
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	private ImageButton startButton;
	private ImageButton playerButton;
	private Button cancelButton;
	private LinearLayout indicatorLayout;
	private ArrayList<View> views;
	private ImageView[] indicators = null;
	private int[] images;
	private int[] instruct_images;
	private int[] age_images;
	// 引导页图片
	private ImageView[] mInstruction_one;
	private ImageView[] mInstruction_two;
	private int currentItem = 0; // 当前图片的索引号
	private ScheduledExecutorService scheduledExecutorService;
	private ScrollTask scrollTask;
	// 音频播放
	private MediaPlayer mediaPlayer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_welcome);
		mContext = this;
		// 创建桌面快捷方式
		// new CreateShut(this);
		// 设置引导图片
		// ! 仅需在这设置图片 指示器和page自动添加
		images = new int[] { R.drawable.welcome_01, R.drawable.welcome_02,
				R.drawable.welcome_03, R.drawable.welcome_04,
				R.drawable.welcome_05, R.drawable.welcome_06 };
		instruct_images = new int[] { R.drawable.welcome_01_2,
				R.drawable.welcome_02_2, R.drawable.welcome_03_2,
				R.drawable.welcome_04_2, R.drawable.welcome_05_2,
				R.drawable.welcome_06_2 };
		age_images = new int[] { R.drawable.welcome_01_3,
				R.drawable.welcome_02_3, R.drawable.welcome_03_3,
				R.drawable.welcome_04_3, R.drawable.welcome_05_3, 0 };
		initView();
		playAnimation(0);
	}
	// 初始化视图
	private void initView() {
		// 实例化视图控件
		viewPager = (ViewPager) findViewById(R.id.viewpage);
		startButton = (ImageButton) findViewById(R.id.start_Button);
		// startButton.setBackgroundColor(Color.TRANSPARENT);
		startButton.setOnClickListener(this);
		playerButton = (ImageButton) findViewById(R.id.player_Button);
		//playerButton.setBackgroundColor(Color.TRANSPARENT);
		playerButton.setOnClickListener(this);
		cancelButton = (Button) findViewById(R.id.cancel_Button);
		cancelButton.setBackgroundColor(Color.TRANSPARENT);
		//cancelButton.setOnClickListener(this);
		indicatorLayout = (LinearLayout) findViewById(R.id.indicator);
		views = new ArrayList<View>();
		indicators = new ImageView[images.length]; // 定义指示器数组大小
		mInstruction_one = new ImageView[images.length];
		mInstruction_two = new ImageView[images.length];
		// 设置引导页的动画效果
		LayoutInflater inflater = LayoutInflater.from(this);
		for (int i = 0; i < images.length; i++) {
			// 循环加入图片
			// ImageView imageView = new ImageView(context);
			// imageView.setBackgroundResource(images[i]);
			View view = inflater.inflate(R.layout.guide_fragment, null, false);
			view.setBackgroundResource(images[i]);
			mInstruction_one[i] = (ImageView) view
					.findViewById(R.id.instruction_one);
			mInstruction_one[i].setImageResource(instruct_images[i]);
			mInstruction_two[i] = (ImageView) view
					.findViewById(R.id.instruction_two);
			mInstruction_two[i].setImageResource(age_images[i]);
			views.add(view);
			// 循环加入指示器
			indicators[i] = new ImageView(mContext);
			indicators[i].setBackgroundResource(R.drawable.indicators_default);
			if (i == 0) {
				indicators[i].setBackgroundResource(R.drawable.indicators_now);
			}
			indicatorLayout.addView(indicators[i]);
		}
		pagerAdapter = new BasePagerAdapter(views);
		viewPager.setAdapter(pagerAdapter); // 设置适配器
		viewPager.setOnPageChangeListener(this);
		viewPager.setClickable(false);
		viewPager.setEnabled(false);
		/*
		 * viewPager.setOnKeyListener(new OnKeyListener() {
		 * @Override public boolean onKey(View v, int keyCode, KeyEvent event) { // TODO
		 * Auto-generated method stub int action = event.getAction(); Log.d(TAG,
		 * "TOUCH IS START action=" +action); return true; } });
		 */
		/*
		 * viewPager.setOnTouchListener(new OnTouchListener() {
		 * @Override public boolean onTouch(View v, MotionEvent event) { // TODO Auto-generated
		 * method stub int action = event.getAction(); Log.d(TAG, "TOUCH IS START action=" +action);
		 * return false; } });
		 */
		// 创建MediaPlayer对象
		mediaPlayer = MediaPlayer.create(this, R.raw.music);
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每6秒钟切换一次图片显示
		scrollTask = new ScrollTask();
		scheduledExecutorService.scheduleAtFixedRate(scrollTask, 8, 8,
				TimeUnit.SECONDS);
	}
	// 按钮的点击事件
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.start_Button) {
			SharedPrefsUtil.putValue(mContext, SettingKey.first, false);
			Intent intent = new Intent(WelcomeActivity.this,
					MainpagerActivity.class);
			startActivity(intent);
			this.finish();
		} else if (v.getId() == R.id.player_Button) {
			try {
				if (mediaPlayer != null) {
					//					Log.d(TAG, "----mediaplayer != null");
					mediaPlayer.stop();
					mediaPlayer.prepare();
					mediaPlayer.start();
				}
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			currentItem = 0;
			viewPager.setCurrentItem(currentItem);
			scheduledExecutorService = Executors
					.newSingleThreadScheduledExecutor();
			// 当Activity显示出来后，每5秒钟切换一次图片显示
			scrollTask = new ScrollTask();
			scheduledExecutorService.scheduleAtFixedRate(scrollTask, 8, 8,
					TimeUnit.SECONDS);
		} else if (v.getId() == R.id.cancel_Button) {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			scheduledExecutorService.shutdown();
			currentItem = indicators.length - 1;
			viewPager.setCurrentItem(currentItem);
		}
	}
	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub
	}
	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub
		//		Log.d(TAG, " ------onPageScrolled arg0=" + arg0);
	}
	// 监听viewpage
	@Override
	public void onPageSelected(int arg0) {
		//		Log.d(TAG, " ------PageSelected=" + arg0);
		// 显示最后一个图片时显示按钮
		AnimationSet animset2 = new AnimationSet(true);
		/*if (arg0 == 0) {
			cancelButton.setVisibility(View.VISIBLE);
		} else {
			cancelButton.setVisibility(View.GONE);
		}*/
		if (arg0 == indicators.length - 1) {
			// 锁定线程
			/*
			 * synchronized (scheduledExecutorService) { try { scheduledExecutorService.wait(500); }
			 * catch (InterruptedException e) { // TODO Auto-generated catch block
			 * e.printStackTrace(); } }
			 */
			//			Log.d(TAG, " --shutdown----PageSelected=" + arg0);
			scheduledExecutorService.shutdown();
			scrollTask = null;
			final Animation animFadeIn1 = AnimationUtils.loadAnimation(this,
					R.anim.fade_in);
			animFadeIn1.setFillAfter(true);
			final Animation animFadeIn2 = AnimationUtils.loadAnimation(this,
					R.anim.fade_in);
			animFadeIn2.setFillAfter(true);
			AnimationSet animset1 = new AnimationSet(true);
			animset1.cancel();
			animset1.reset();
			animset2.reset();
			animset1.addAnimation(animFadeIn1);
			animset1.setFillAfter(true);
			mInstruction_one[arg0].clearAnimation();
			mInstruction_one[arg0].startAnimation(animset1);
			animset2.addAnimation(animFadeIn2);
			animset2.setStartOffset(1500);
			animset2.setFillAfter(true);
			startButton.clearAnimation();
			startButton.startAnimation(animset2);
			startButton.setVisibility(View.VISIBLE);
			playerButton.setVisibility(View.VISIBLE);
		} else {
			animset2.cancel();
			startButton.clearAnimation();
			startButton.setVisibility(View.GONE);
			playerButton.setVisibility(View.GONE);
		}
		// 更改指示器图片
		for (int i = 0; i < indicators.length; i++) {
			indicators[arg0].setBackgroundResource(R.drawable.indicators_now);
			if (arg0 != i) {
				indicators[i]
						.setBackgroundResource(R.drawable.indicators_default);
			}
		}
		playAnimation(arg0);
	}
	// 播放动画效果
	private void playAnimation(int pageIndex) {
		if (pageIndex == indicators.length - 1)
			return;
		final int Index = pageIndex;
		//		Log.d(TAG, "---jj playAnimation Index=" + Index);
		final Animation animFadeIn1 = AnimationUtils.loadAnimation(this,
				R.anim.fade_in);
		animFadeIn1.setFillAfter(true);
		final Animation animFadeIn2 = AnimationUtils.loadAnimation(this,
				R.anim.fade_in);
		animFadeIn2.setFillAfter(true);
		final Animation animOutTopRight = AnimationUtils.loadAnimation(this,
				R.anim.out_to_top_right);
		animOutTopRight.setFillAfter(true);
		final Animation animOutBottomRight = AnimationUtils.loadAnimation(this,
				R.anim.out_to_bottom_right);
		animOutBottomRight.setFillAfter(true);
		AnimationSet animset1 = new AnimationSet(true);
		AnimationSet animset2 = new AnimationSet(true);
		final AnimationSet animset3 = new AnimationSet(true);
		final AnimationSet animset4 = new AnimationSet(true);
		animset1.cancel();
		animset1.reset();
		animset2.cancel();
		animset2.reset();
		animset1.addAnimation(animFadeIn1);
		animset1.setFillAfter(true);
		mInstruction_one[Index].clearAnimation();
		mInstruction_one[Index].startAnimation(animset1);
		animset2.addAnimation(animFadeIn2);
		animset2.setStartOffset(1500);
		animset2.setFillAfter(true);
		// animset2.setStartTime(startTime + 1000); //两秒后再执行动画
		mInstruction_two[Index].clearAnimation();
		mInstruction_two[Index].startAnimation(animset2);
		animset2.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
				// TODO Auto-generated method stub
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				// TODO Auto-generated method stub
				animset3.setStartOffset(1000);
				animset3.addAnimation(animOutTopRight);
				animset3.setFillAfter(true);
				mInstruction_one[Index].startAnimation(animset3);
				animset4.setStartOffset(2500);
				animset4.addAnimation(animOutBottomRight);
				animset4.setFillAfter(true);
				mInstruction_two[Index].startAnimation(animset4);
				// 获取屏幕尺寸
				int pWidth = viewPager.getMeasuredWidth();
				//				Log.d(TAG, "------PX width=" + pWidth);
				// viewPager.scrollBy(pWidth/2, 0);
			}
		});
	}

	/**
	 * 换行切换任务
	 * @author Administrator
	 */
	private class ScrollTask implements Runnable {
		@Override
		public void run() {
			synchronized (viewPager) {
				currentItem = (currentItem + 1) % 6;
				imagehandler.obtainMessage().sendToTarget(); // 通过Handler切换图片
			}
		}
	}

	// 切换当前显示的图片
	private Handler imagehandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			//viewPager.arrowScroll(currentItem);
			//LogUtils.i("currentItem:" + currentItem);
			if (currentItem > 5) {
				currentItem = 5;
			}
			viewPager.setCurrentItem(currentItem);// 切换当前显示的图片
			/*
			 * View view = viewPager.getChildAt(currentItem-1); view.setAnimation(animout); View
			 * view2 = viewPager.getChildAt(currentItem); view2.setAnimation(animin);
			 */
			// 设置切换效果
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		};
	};

	@Override
	protected void onStart() {
		try {
			if (mediaPlayer != null) {
				mediaPlayer.stop();
				mediaPlayer.prepare();
				mediaPlayer.start();
				mediaPlayer.setLooping(true);
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		synchronized (scheduledExecutorService) {
			if (scheduledExecutorService != null) {
				scheduledExecutorService.notify();
			}
		}
		super.onStart();
	}
	@Override
	protected void onStop() {
		// 当Activity不可见的时候停止切换
		if (scheduledExecutorService != null) {
			synchronized (scheduledExecutorService) {
				try {
					scheduledExecutorService.wait(500);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		if (mediaPlayer != null) {
			mediaPlayer.stop();
		}
		super.onStop();
	}
	@Override
	protected void onDestroy() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
		if (scheduledExecutorService != null) {
			scheduledExecutorService.shutdown();
		}
		super.onDestroy();
	}
}
