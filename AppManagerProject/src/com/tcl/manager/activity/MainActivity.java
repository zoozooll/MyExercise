package com.tcl.manager.activity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewAnimator;

import com.haarman.listviewanimations.SwingBottomInAnimationAdapter;
import com.tcl.framework.log.NLog;
import com.tcl.framework.util.DeviceManager;
import com.tcl.manager.activity.adapter.MainAdapter;
import com.tcl.manager.activity.adapter.MainScanningAdapter;
import com.tcl.manager.activity.entity.ExpandableListItem;
import com.tcl.manager.activity.entity.OptimizeChildItem;
import com.tcl.manager.activity.entity.ScanningItem;
import com.tcl.manager.analyst.Analyst;
import com.tcl.manager.analyst.IAnalystListener;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.arithmetic.entity.AppScoreInfo;
import com.tcl.manager.blackwhitelist.WhitelistSync;
import com.tcl.manager.miniapp.MiniAppWindowManager;
import com.tcl.manager.optimize.AutoStartBlackList;
import com.tcl.manager.score.AppScoreProvider;
import com.tcl.manager.score.InstalledAppProvider;
import com.tcl.manager.score.PageDataProvider;
import com.tcl.manager.score.PageFunctionProvider;
import com.tcl.manager.score.PageInfo;
import com.tcl.manager.score.ScoreLevel;
import com.tcl.manager.update.UpdateChecker;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.MemoryInfoProvider;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.manager.view.RadarViewLayout;
import com.tcl.manager.view.RiseNumberTextView;
import com.tcl.manager.view.RiseNumberTextView.EndListener;
import com.tcl.mie.manager.R;

public class MainActivity extends BaseActivity implements OnClickListener, IAnalystListener {

	// All Views
	// i.Root view
	private RelativeLayout mRootLayout;
	// ii.Views in main_top_layout
	private View topLayout;
	private RadarViewLayout radarView;
	private ImageButton btnMenu;
	private Button btnAppStatistic;
	private View     scanningTitleLayout;
    private TextView textScanningTitle;
    
    
	// ii.Views in main_foot_layout
    private View main_foot_layout;
    
	// iii. Views main_grid_layout
    private View gridLayout;
    private View btnMemery;// 内存按钮
    private View btnBattery;// 电池按钮
    private View btnData;// data按钮
    private View btnStorage;
    
    // iii. Views in main_bottom_layout
    private View bottomLayout;
    private View buttonLayout;// 按钮布局
    private Button btnOK;// ok按钮
    private ListView mListView;
    
    // iii. View in main_scanninglist_layout
    private ListView scanningListView;
    
	// View in layout_main_score
	private View layoutMainScore;
    private View mScoreCircleView;// 分数按钮
    private View view_color_round;
    private View mScoreCircleBorder;// 分数圈的边缘视图,通过它来定位
    private View mWhiteShadeView;// 分数圈边缘的转圈效果
    private TextView mStatusView;
    private RiseNumberTextView mScoreView;


    private PopupWindow menuPop;

    private MainAdapter mAdapter;
    private MainScanningAdapter mScanningAdapter;

    /** true:circle is up ;false:circle is down */
    private boolean mIsCircleUp = false;
    
    /** 当前杀死的进程数目 */
    private int mCurrentKillProcess = 0;// 单位apps
    /** 当前释放的内存数目 */
    private long mCurrentReleaseMemory = 0;// 单位mb

//    private View expandLayout2;// 圆圈以下描述布局,展开or收缩
//    private View expandLayout1;// 顶部空白，展开or收缩
    private Analyst mAppScoreAnaly;
    private int topHeight;
    private int scoredStep;
	private Collection<ApplicationInfo> animationList;

    public static MainActivity sMainActivity;
    
    

	@Override
    public void onCreate(Bundle savedInstanceState) {
		setActivity(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        initViews();
       
        startCheckScore();
        mUIHandler.sendEmptyMessage(MSG_START_SHOWING);
        checkInstalling();
        initializeWhiteList();
        // new ScanDialog(this).show();
        
    }

	

    @Override
    protected void onResume() {
        super.onResume();
//        restartScoreBorderAnim();
    }

    private void initViews() {
        mRootLayout = (RelativeLayout) findViewById(R.id.main_root_layout);
        layoutMainScore = findViewById(R.id.layout_main_score);
        mScoreCircleView = findViewById(R.id.main_score_circle_view);
        view_color_round = findViewById(R.id.view_color_round);
        		
        btnMemery = findViewById(R.id.main_memery);
        btnBattery = findViewById(R.id.main_battery);
        btnData = findViewById(R.id.main_data);
        btnStorage = findViewById(R.id.main_storage);

        topLayout = findViewById(R.id.main_top_layout);
        mListView = (ListView) findViewById(R.id.main_expandalelistview);
        bottomLayout = findViewById(R.id.main_bottom_layout);
        main_foot_layout = findViewById(R.id.main_foot_layout);
        scanningListView = (ListView) findViewById(R.id.main_scanninglist_layout);
        btnOK = (Button) findViewById(R.id.main_bottom_button);
        btnAppStatistic = (Button) findViewById(R.id.main_app_statistic);
        btnMenu = (ImageButton) findViewById(R.id.main_menu);

//        expandLayout2 = findViewById(R.id.main_expand_layout);
//        expandLayout1 = findViewById(R.id.main_expand_layout_01);
        gridLayout = findViewById(R.id.main_grid_layout);
        mScoreCircleBorder = findViewById(R.id.main_score_circle_outside);
        mWhiteShadeView = findViewById(R.id.main_whiteshareannulus);
        buttonLayout = findViewById(R.id.main_bottom_button_layout);

        mScoreView = (RiseNumberTextView) findViewById(R.id.main_score);
        mStatusView = (TextView) findViewById(R.id.main_status);
        
        scanningTitleLayout = findViewById(R.id.main_scanning_title_layout);
        textScanningTitle  = (TextView) findViewById(R.id.textScanningTitle);

        Typeface scoreTypeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Light.ttf");
        mScoreView.setTypeface(scoreTypeface);
        // setupListener();

        // init data
        mAdapter = new MainAdapter(this, mUIHandler);

        radarView = (RadarViewLayout) findViewById(R.id.radarView);

        radarView = (RadarViewLayout) findViewById(R.id.radarView);
        scanningListView.setEnabled(false);
    }

    private void setupListener() {
        mScoreCircleView.setOnClickListener(this);
        btnMemery.setOnClickListener(this);
        btnBattery.setOnClickListener(this);
        btnData.setOnClickListener(this);
        btnOK.setOnClickListener(this);
        btnAppStatistic.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
        btnStorage.setOnClickListener(this);

    }
    
    /** 检查分数完成 */
    public static final int MSG_CHECK_SCORE_FINISH = 0;

    /** 优化进程完成消息 */
    public static final int MSG_OPTIMIZE_PROCESS_FINISH = 1;
    /** 优化单个进程完成 */
    public static final int MSG_OPTIMIZE_SINGLE_PROCESS_FINISH = 2;
    /** 关闭自启 */
    public static final int MSG_SHUTDOWN_AUTOSTART = 4;
    /** 延迟执行圆圈动画 */
    private static final int MSG_UI_STARTSCOREANIM = 5;
    
    /** 检查内存分数完成 */
    public static final int MSG_MEMORY_SCORE_FINISH = 6;
    
    /** 检查风险项完成 */
    public static final int MSG_RISK_SCORE_FINISH = 11;
    
    /** Start running MEMORY list animation */
    public static final int MSG_MEMORY_ANIMATION_START = 7;
    
    /** Start running CACHE list animation */
    public static final int MSG_CACHE_ANIMATION_START = 8;
    
    /** Start running RISK list animation */
    public static final int MSG_RISK_ANIMATION_START = 9;
    
    /** Start running RISK list animation */
    public static final int MSG_SCANNING_ANIMATION = 10;
    
    public static final int MSG_START_SHOWING = 12;

    /** handle main ui changed */
    private Handler mUIHandler = new Handler(Looper.getMainLooper()) {
       
		public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
            case MSG_START_SHOWING: {
            	 jumpOutAllViewAnimation();
            }
            	break;
            case MSG_SCANNING_ANIMATION: {
            	updateScanningAdapter((ScanningItem) msg.obj);
            }
            	break;
            case MSG_MEMORY_ANIMATION_START: {
            	textScanningTitle.setText(R.string.main_scenning_memory);
            }
            	break;
            case MSG_CACHE_ANIMATION_START: {
            	textScanningTitle.setText(R.string.main_scenning_cache);
            }
            	break;
            case MSG_RISK_ANIMATION_START: {
            	textScanningTitle.setText(R.string.main_scenning_risk);
            }
            	break;
            
            case MSG_MEMORY_SCORE_FINISH: {
            	scoredStep = 1;
            	int score = msg.arg1;
            	runScoreAnimation(score);
            }
            	break;
            case MSG_RISK_SCORE_FINISH: {
            	scoredStep = 2;
            	
            }
            	break;
            
            case MSG_CHECK_SCORE_FINISH: {
            	// part time code
            	int score = PageDataProvider.getInstance().score;
//            	int score = 23;
            	finishCheckScore((int) score);
            	scanningTitleLayout.setVisibility(View.GONE);
            	scanningListView.setVisibility(View.GONE);
            	showGridLayout();
//            	gridLayout.setVisibility(View.VISIBLE);
                bottomLayout.setVisibility(View.GONE);
            	// 更新miniapp
            	MiniAppWindowManager.getInstance(MainActivity.this).checkScore();
            }
                break;
            case MSG_OPTIMIZE_SINGLE_PROCESS_FINISH:
                PageInfo runningAppInfo = (PageInfo) msg.obj;
                optimizeSingleProcessCompleted(runningAppInfo);

                break;
            case MSG_OPTIMIZE_PROCESS_FINISH:
                List<ExpandableListItem> list = (List<ExpandableListItem>) msg.obj;
                int newScore = msg.arg1;
                int needOptimizeItemCount = msg.arg2;
                optimizeProcessCompeleted(list, newScore, needOptimizeItemCount);

                // 更新miniapp
                MiniAppWindowManager.getInstance(MainActivity.this).checkScore();
                break;
            case MSG_SHUTDOWN_AUTOSTART:
                OptimizeChildItem item = (OptimizeChildItem) msg.obj;
                optimizeAutoStart(item.getType(), item.getPgkName(), item.getNeedAddScore());

                // 更新miniapp
                MiniAppWindowManager.getInstance(MainActivity.this).checkScore();
                break;
            case MSG_UI_STARTSCOREANIM:
                startScoreBorderAnim();
//                mScoreCircleView.setEnabled(true);
                break;
            default:
                break;
            }
        }

    };

    //TODO ************** Logics ************************************************
    
    /**
     * 检测分值
     */
    private void startCheckScore() {
        // 分数设置100分
        // mScoreCircleView.setScore(99);
        // setScore(99, null);
        // 转圈
        turnCircle();
        // 不可点击
        mScoreCircleView.setEnabled(false);

        // 开始检测分数
        if (mAppScoreAnaly == null) {
            mAppScoreAnaly = new Analyst(this, this);
        }
        mAppScoreAnaly.startAnalysisComplete();
    }
    
    private void checkInstalling() {
		UpdateChecker uc = new UpdateChecker(this);
        uc.check();
	}
    
    private void initializeWhiteList() {
        WhitelistSync.checkWhiteList();
    }
    
    /**
     * start optimize process
     */
	private void startOptimizeProcess() {
		// circle spin

		mScoreCircleView.setEnabled(false);
/*		if (!mIsCircleUp) {
			//startCircleMoveUp(40);
			
			// collapse(expandLayout2); collapse(expandLayout1);
			 

		}*/
		btnAppStatistic.setVisibility(View.GONE);
		// start the round's animation;
		scoreRoundZoomoutAnimation();
		// start top layout's animations
		topLayoutUpAnimation();

	}
	
	 /**
     * optimize process compeleted
     */
    private void optimizeProcessCompeleted(List<ExpandableListItem> list, final int newScore, final int needOptimizeItemCount) {
        // change score
        stopTurnCircle();
        mScoreCircleView.setEnabled(false);
        setScore(newScore, new EndListener() {

            @Override
            public void onEndFinish() {
                if (needOptimizeItemCount > 0) {
                    mStatusView.setText(R.string.main_optimize_needed2);
                } else {
                    // 显示状态文本
                    mStatusView.setText(ScoreLevel.resolveToString(newScore));
                }
            }
        });

        // add list data
        addOptimizeList(list);
        // loading finish
        mAdapter.setLoadingFinish();

        mCurrentKillProcess = 0;
        mCurrentReleaseMemory = 0;
        // start anim
        mUIHandler.sendEmptyMessageDelayed(MSG_UI_STARTSCOREANIM, 500);

        // 显示ok按钮
        showOKButton();

    }

	private void runScanningShowingThread() {
		new MemoryAppListGetter().start();
	}
	
    private void ok() {
        if (mIsCircleUp) {
            // startCircleMoveDown(40);
          /*  expand(expandLayout2, null);
            expand(expandLayout1, null);*/
            // wait expand finished
            mUIHandler.removeMessages(MSG_UI_STARTSCOREANIM);
            mUIHandler.sendEmptyMessageDelayed(MSG_UI_STARTSCOREANIM, 2000);
        }
        stopScoreBorderAnim();
     // start top layout's animations
        topLayoutDownAnimation();
     // showing the score round 
        /*Animation anim = AnimationUtils.loadAnimation(MainActivity.this, R.anim.main_optimized_round);
        layoutMainScore.startAnimation(anim);*/
        scoreRoundZoominAnimation();
        hideOKButton();
        showGridLayout();
        
    }
	
    /**
     * 优化单个进程完成?kill
     * 
     * @param runningAppInfo
     */
    private void optimizeSingleProcessCompleted(PageInfo runningAppInfo) {
        if (mAdapter.getCount() == 2) {
            mCurrentKillProcess++;
            mCurrentReleaseMemory += runningAppInfo.memorySize;
            mAdapter.setAppStopedLabel(mCurrentKillProcess, mCurrentReleaseMemory);
            setScore(runningAppInfo.indexScore, null);
        }
    }
    
	private void clearOptimizeList() {
        mAdapter.clear();
    }
	
    private void addOptimizeList(List<ExpandableListItem> list) {
        mAdapter.addList(list);
    }

    private void addOptimizeItem(ExpandableListItem item) {
        mAdapter.addItem(item);
    }
	
    /**
     * 初始化ListView中两项
     */
    private void setProcessItemToListView() {
        Resources res = getResources();
        List<ExpandableListItem> list = new ArrayList<ExpandableListItem>();
        list.add(new ExpandableListItem(R.drawable.ic_check, res.getString(R.string.main_list_item_label_01), 0 + res.getString(R.string.main_list_item_label_03)));
        String memoryStr = MemoryInfoProvider.byteToMB(this, 0);
        list.add(new ExpandableListItem(R.drawable.ic_check, res.getString(R.string.main_list_item_label_02), String.valueOf(memoryStr)));
        setOptimizeList(list);
    }

    private void setOptimizeList(List<ExpandableListItem> list) {
        SwingBottomInAnimationAdapter animationAdapter = new SwingBottomInAnimationAdapter(mAdapter);
        animationAdapter.setAbsListView(mListView);
        mListView.setAdapter(animationAdapter);
        mAdapter.setList(list);
    }
    
    @Override
	public void memoryScoreBack(int memoryScore) {
//		NLog.d("aaron", "memoryScoreBack " + memoryScore);
		Message msg = mUIHandler.obtainMessage(MSG_MEMORY_SCORE_FINISH);
		if (msg == null) {
			msg = new Message();
			msg.what = MSG_MEMORY_SCORE_FINISH;
		}
		msg.arg1 = memoryScore;
		mUIHandler.sendMessage(msg);
		
	}

	@Override
	public void totalScoreBack(List<AppScoreInfo> apps, int totalScore,
			int appsScore, int memoryScore) {
		
		PageDataProvider.getInstance().init(ManagerApplication.sApplication.getApplicationContext(), apps);
        PageDataProvider.getInstance().score = appsScore + memoryScore;
        PageDataProvider.getInstance().memoryScore = memoryScore;
        PageDataProvider.getInstance().appScore = appsScore;
		
//		NLog.d("aaron", "totalScoreBack " + totalScore);
		Message msg = mUIHandler.obtainMessage(MSG_RISK_SCORE_FINISH);
		if (msg == null) {
			msg = new Message();
			msg.what = MSG_RISK_SCORE_FINISH;
		}
		msg.arg1 = totalScore;
		mUIHandler.sendMessage(msg);
		
	}
	
	 /**
     * 优化自启
     * 
     * @param pkgName
     * @param addScore
     */
    private void optimizeAutoStart(int type, final String pkgName, final int addScore) {
        // 启动优化任务
        // new OptimizeTask(type, pkgName).start();
        switch (type) {
        case OptimizeChildItem.TYPE_BATTERY:// 电量
            PageFunctionProvider.setAutoStart(pkgName, false);
            break;
        case OptimizeChildItem.TYPE_DATA:// 流量
            PageFunctionProvider.setDataAccess(pkgName, false);
            break;
        default:
            break;
        }
        // mScoreCircleView.addScore(addScore);
        addScore(addScore, null);

    }
    
    /**
     * 结束分数检测，显示分数
     * 
     * @param score
     */
    private void finishCheckScore(int score) {
//        Log.i("wenchao", "检查后分数" + score);
        stopTurnCircle();
        mScoreCircleView.setEnabled(true);
        setScore(score, new EndListener() {

            @Override
            public void onEndFinish() {
                mStatusView.setText(R.string.main_optimize_needed);
            }
        });

        // 结束分数检测，可以初始化监听器
        setupListener();

        // mScoreCircleBorder.startAnimation(AnimationUtils.loadAnimation(this,
        // R.anim.anim_score_circle_spread));
        startScoreBorderAnim();
        btnAppStatistic.setVisibility(View.VISIBLE);

    }
    private void addScore(float score, EndListener callback) {
    	if (mScoreView.getTag() != null) {
    		float currScore = (Float) mScoreView.getTag();
    		currScore += score;
    		setScore(currScore, callback);
    		
    		PageFunctionProvider.setAppScore((int) currScore);
    		// mStatusView.setText(ScoreLevel.resolveToString((int) score));
    		// mStatusView.setTextColor(ScoreLevel.resolveToColor((int)score));
    	}
    }

    private void setScore(float score, EndListener callback) {
        // 设置分数动画
        // mScoreView.setText(String.valueOf((int) score));
        mScoreView.setOnEnd(callback);
        runScoreAnimation(score);

        NLog.i("wenchao", "设置分数" + score);

    }
    
    private void jumpToOptimizedActivity() {
        // jump to manager list. and set tag parameter which category list
        // to jump to.Default is OVERALL.
        Intent intent = new Intent(this, OptimizedActivity.class);
        startActivity(intent);
    }

    private void jumpToStatistic() {
        // jump to manager list. and set tag parameter which category list
        // to jump to.Default is OVERALL.
        Intent intent = new Intent(this, AppStatisticActivity.class);
        startActivity(intent);
    }
    
	// ******* Animations *****************************************************
	/**
	 * The animation running at beginning of the activity before views showing.
	 * After this animation, it will show the scanning applications at the foot view, and the score round at the top view
	 * It will not run at SDK less than 21.If the SDK is less than 21, it will showing all views with animation.
	 */
	@TargetApi(21)
    private void jumpOutAllViewAnimation() {
    	if (Build.VERSION.SDK_INT >= 21) {
    		Animator animator = ViewAnimationUtils.createCircularReveal(mRootLayout, 0, mRootLayout.getHeight(), 0, (float) Math.hypot(mRootLayout.getWidth(), mRootLayout.getHeight()));
    		animator.setInterpolator(new AccelerateInterpolator());
    		animator.setDuration(500);
    		animator.addListener(new Animator.AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					jumpOutScoreRound();
					runScanningShowingThread();
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					
				}
			});
    		animator.start();
    	} else {
    		jumpOutScoreRound();
    		runScanningShowingThread();
    	}
    };
    
    private void jumpOutScoreRound() {
    	Animation anim = AnimationUtils.loadAnimation(this, R.anim.main_jumpout_scoreround_anim);
    	layoutMainScore.setVisibility(View.VISIBLE);
    	layoutMainScore.startAnimation(anim);
    }
	
    private void turnCircle() {
        mWhiteShadeView.setVisibility(View.VISIBLE);
//        mWhiteShadeView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.anim_circle_turn));
        // 转圈的时候滞空
        mStatusView.setText("");
    }

    private void stopTurnCircle() {
        mWhiteShadeView.setVisibility(View.GONE);
//        mWhiteShadeView.clearAnimation();
    }
	
	private void topLayoutUpAnimation() {
		final ViewGroup.LayoutParams lps = topLayout.getLayoutParams();
		topHeight = lps.height;
		ValueAnimator layoutAnimator = ValueAnimator.ofInt(topHeight,
				(int) (topHeight * 0.25f));
		layoutAnimator.setDuration(300);
		layoutAnimator.setRepeatCount(0);
		layoutAnimator.setInterpolator(new LinearInterpolator());
//		NLog.d("aaron", "topview's bottom:%d,footview's top: %d",
//				topLayout.getBottom(), main_foot_layout.getTop());
		layoutAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					private int prevValue;
					private long prevTime;

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int value = (Integer) animation.getAnimatedValue();
						long time = animation.getCurrentPlayTime();
						if (time - prevTime > 10) {
							// if (Math.abs(prevValue - value) > 1) {
							topLayout.setBottom(value);
							main_foot_layout.setTop(value);
							prevValue = value;
							prevTime = time;
//							NLog.d("aaron", "main_foot_layout's bottom: %d",
//									main_foot_layout.getBottom());
						}
					}
				});
		layoutAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				int value = (Integer) ((ValueAnimator) animation)
						.getAnimatedValue();
				lps.height = value;
				clearOptimizeList();
				// bottom layout show, example listview
				// bottomLayout.setVisibility(View.VISIBLE);

				hideGridLayout();

				// 初始化2项
				setProcessItemToListView();
				// start optimize
				AppScoreProvider.getInstance().startKillProcess(
						getApplicationContext(), mUIHandler);
			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		layoutAnimator.start();
	}

	private void topLayoutDownAnimation() {
		final ViewGroup.LayoutParams lps = topLayout.getLayoutParams();
		ValueAnimator layoutAnimator = ValueAnimator.ofInt(
				(int) (topHeight * 0.25f), topHeight);
		layoutAnimator.setDuration(300);
		layoutAnimator.setRepeatCount(0);
		layoutAnimator.setInterpolator(new LinearInterpolator());
		// NLog.d("aaron",
		// "topview's bottom:%d,footview's top: %d",topLayout.getBottom(),
		// main_foot_layout.getTop());
		layoutAnimator
				.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
					private int prevValue;
					private long prevTime;

					@Override
					public void onAnimationUpdate(ValueAnimator animation) {
						int value = (Integer) animation.getAnimatedValue();
						long time = animation.getCurrentPlayTime();
						if (time - prevTime > 10) {
							// if (Math.abs(prevValue - value) > 1) {
							topLayout.setBottom(value);
							main_foot_layout.setTop(value);
							prevValue = value;
							prevTime = time;
						}
					}
				});
		layoutAnimator.addListener(new Animator.AnimatorListener() {

			@Override
			public void onAnimationStart(Animator animation) {

			}

			@Override
			public void onAnimationRepeat(Animator animation) {

			}

			@Override
			public void onAnimationEnd(Animator animation) {
				int value = (Integer) ((ValueAnimator) animation)
						.getAnimatedValue();
				lps.height = value;
				// bottom layout hide, example listview
				//

			}

			@Override
			public void onAnimationCancel(Animator animation) {

			}
		});
		layoutAnimator.start();
	}
	
	private void updateScanningAdapter(ScanningItem item){
		 if (scanningListView.getAdapter() == null) {
			 mScanningAdapter = new MainScanningAdapter(this);
			 scanningListView.setAdapter(mScanningAdapter);
//			 scanningListView.smoothScrollToPositionFromTop(scanningListView.getCount(), 0, 5000);
		 }
		 mScanningAdapter.addItem(item);
	 }
	 
	 private void loadAppIcon(ScanningItem app) {
			Bitmap icon = InstalledAppProvider.getInstance().getIcon(app.pkgName);
			if (icon == null) {
				Drawable d = PkgManagerTool.getIcon(getApplicationContext(), app.pkgName);
				if (d instanceof BitmapDrawable) {
					Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
					if (bitmap.getHeight() > 60 && bitmap.getHeight() > 0 && bitmap.getWidth() > 0) {
						float rate = 60.f / bitmap.getHeight();
						try {
							icon = com.tcl.manager.util.BitmapUtil.zoomBitmap(getApplicationContext(), bitmap, rate);
						} catch (Exception e) {
							e.printStackTrace();
							icon = bitmap;
						}
					} else {
						icon = bitmap;
					}
				}
			}
			app.icon = icon;
	 }
	 
	private void scoreRoundZoomoutAnimation() {
	    	AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.main_score_zoomout);
	    	ObjectAnimator tranAnim = ObjectAnimator.ofObject(layoutMainScore, "TranslationY", new IntEvaluator(), 0, - layoutMainScore.getHeight() >> 1);
	    	tranAnim.setInterpolator(new LinearInterpolator());
	    	animation.playTogether(tranAnim);
	    	animation.setTarget(layoutMainScore);
	    	animation.start();
	    	animation.addListener(new Animator.AnimatorListener() {
				
				@Override
				public void onAnimationStart(Animator animation) {
					
				}
				
				@Override
				public void onAnimationRepeat(Animator animation) {
					
				}
				
				@Override
				public void onAnimationEnd(Animator animation) {
					
					turnCircle();
				}
				
				@Override
				public void onAnimationCancel(Animator animation) {
					
				}
			});
	    }
	    
    private void scoreRoundZoominAnimation() {
    	AnimatorSet animation = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.main_score_zoomin);
    	ObjectAnimator tranAnim = ObjectAnimator.ofObject(layoutMainScore, "TranslationY", new IntEvaluator(),  -layoutMainScore.getTop() >> 1, 0);
    	tranAnim.setInterpolator(new LinearInterpolator());
    	animation.playTogether(tranAnim);
    	animation.setTarget(layoutMainScore);
    	animation.start();
    	animation.addListener(new Animator.AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator animation) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator animation) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator animation) {
				
				startScoreBorderAnim();
			}
			
			@Override
			public void onAnimationCancel(Animator animation) {
				
			}
		});
    	
    	
    }
	
    private void showGridLayout() {
    	gridLayout.setVisibility(View.VISIBLE);
        Animation showAnim = AnimationUtils.loadAnimation(this, R.anim.anim_main_ok_up);
        showAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	bottomLayout.setVisibility(View.GONE);
            	mScoreCircleView.setEnabled(true);
            	btnAppStatistic.setVisibility(View.VISIBLE);
            }
        });
        gridLayout.startAnimation(showAnim);

    }
    
    private void hideGridLayout() {
        if (gridLayout.getVisibility() == View.GONE) {
            return;
        }
        gridLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.VISIBLE);
        Animation hideAnim = AnimationUtils.loadAnimation(this, R.anim.anim_main_ok_down);
        hideAnim.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            	btnAppStatistic.setVisibility(View.GONE);
            }
        });
        hideAnim.setDuration(300);
        gridLayout.startAnimation(hideAnim);

    }
    
    
    
    /**
     * 开始圆圈边缘动画
     */
    private void startScoreBorderAnim() {
        /*mUIHandler.sendEmptyMessageDelayed(MSG_UI_STARTSCOREANIM, 2000);
        if (animView1 == null) {
            animView1 = createAnimView();
        }
        if (animView2 == null) {
            animView2 = createAnimView();
        }

        if (animView3 == null) {
            animView3 = createAnimView();
        }
*/
        /*animView1.startAnimation(createSpreadAnim(0));
        animView2.startAnimation(createSpreadAnim(300));
        animView3.startAnimation(createSpreadAnim(600));*/
//    	NLog.d("aaron", "animation startScoreBorderAnim %d, %d, %d, %d " ,layoutMainScore.getLeft(), layoutMainScore.getTop() , layoutMainScore.getRight(), layoutMainScore.getBottom());
//    	NLog.d("aaron", "animation Translation %s : %s", layoutMainScore.getTranslationX(), layoutMainScore.getTranslationY());
//    	NLog.d("aaron", "animation Scales %s : %s", layoutMainScore.getScaleX(), layoutMainScore.getScaleY()); 
		int width = (int) ((layoutMainScore.getRight() - layoutMainScore
				.getLeft()) * layoutMainScore.getScaleX());
		int height = (int) ((layoutMainScore.getBottom() - layoutMainScore
				.getTop()) * layoutMainScore.getScaleY());
		
		int midPointX = (layoutMainScore.getLeft() + layoutMainScore.getRight())/2 + (int)layoutMainScore.getTranslationX();
		int midPointY = (layoutMainScore.getTop() + layoutMainScore.getBottom())/2 + (int)layoutMainScore.getTranslationY();

    	radarView.setLocation(
    			midPointX - width / 2,
				midPointY - height/ 2,
				midPointX + width / 2,
				midPointY + height/ 2,
				DeviceManager.dip2px(this, 100));

    	radarView.start();
    }
	
    private void stopScoreBorderAnim() {
        /* mUIHandler.removeMessages(MSG_UI_STARTSCOREANIM);
         if (animView1 != null) {
             animView1.clearAnimation();
             mRootLayout.removeView(animView1);
             animView1 = null;
         }
         if (animView2 != null) {
             animView2.clearAnimation();
             mRootLayout.removeView(animView2);
             animView2 = null;
         }
         if (animView3 != null) {
             animView3.clearAnimation();
             mRootLayout.removeView(animView3);
             animView3 = null;
         }*/
     	radarView.end();
     }
    
    private void showOKButton() {

        if (buttonLayout.getVisibility() == View.GONE) {
            Animation okAnim = AnimationUtils.loadAnimation(this, R.anim.anim_main_ok_up);
            buttonLayout.setEnabled(false);
            okAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    bottomLayout.setEnabled(true);
                    startScoreBorderAnim();
                }
            });
            okAnim.setDuration(300);
            buttonLayout.setVisibility(View.VISIBLE);
            buttonLayout.startAnimation(okAnim);
        }

    }
    
    private void hideOKButton() {
        if (buttonLayout.getVisibility() == View.VISIBLE) {
            Animation okAnim = AnimationUtils.loadAnimation(this, R.anim.anim_main_ok_down);
            okAnim.setAnimationListener(new AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    buttonLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                    showGridLayout();
                }
            });
            okAnim.setDuration(300);
            buttonLayout.startAnimation(okAnim);
        }
    }
    
    /** show menu */
    @SuppressWarnings("deprecation")
    private void showMenuPopupWindow() {

        String[] items = new String[] { getString(R.string.main_optimized), getString(R.string.main_popwindow_about_us) };
        AlertDialog menuDialog = new AlertDialog.Builder(this, R.style.main_overflow_menus).setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case 0:// 已优化
                    dissmissMenuPop();
                    jumpToOptimizedActivity();
                    break;
                case 1:// 关于
                    startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                    break;
                }
            }
        }).create();
        menuDialog.show();

        if (Build.VERSION.SDK_INT < 21)
            menuDialog.getListView().setSelector(R.drawable.selector_list_holo);

        Rect rect = new Rect();
        findViewById(R.id.main_menu).getGlobalVisibleRect(rect);

        WindowManager.LayoutParams params = menuDialog.getWindow().getAttributes();
        params.x = rect.left;
        params.y = rect.top;
        params.width = getResources().getDimensionPixelSize(R.dimen.main_overflow_widht);
        menuDialog.setCanceledOnTouchOutside(true);
        menuDialog.getWindow().setGravity(Gravity.LEFT | Gravity.TOP);
        menuDialog.getWindow().setLayout(params.width, WindowManager.LayoutParams.WRAP_CONTENT);
        menuDialog.getWindow().setAttributes(params);
    }

    /** hide menu */
    private void dissmissMenuPop() {
        if (menuPop != null && menuPop.isShowing()) {
            menuPop.dismiss();
        }
    }
    
    /**
     * 改变背景色，渐变
     * 
     * @param toColor
     */
    private void changeBgColor(int toColor) {
        int bgColor = ((ColorDrawable) topLayout.getBackground()).getColor();
        if (bgColor != toColor) {
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(topLayout, "backgroundColor", new ArgbEvaluator(), bgColor, toColor);
            backgroundColorAnimator.setDuration(1000);
            backgroundColorAnimator.start();
            // view_color_round needs to change color,too.
            final ObjectAnimator backgroundColorAnimator2 = ObjectAnimator.ofObject(view_color_round, "backgroundColor", new ArgbEvaluator(), bgColor, toColor);
            backgroundColorAnimator2.setDuration(1000);
            backgroundColorAnimator2.start();
        }
    }
    
    private void runScoreAnimation(float score) {
		mScoreView.withNumber((int) score).start();
        mScoreView.setTag(score);
        changeBgColor(ScoreLevel.resolveToColor((int) score));
        // mStatusView.setText(ScoreLevel.resolveToString((int) score));
        mStatusView.setTextColor(ScoreLevel.resolveToColor((int) score));
        mScoreView.setTextColor(ScoreLevel.resolveToColor((int) score));
	}
    
    // **************Animations end*************************************************

    private static final long clickDividerMillions = 1 * 1000;
    private long lastClickTime = 0;

    @Override
    public void onClick(View v) {
        long currClickTime = System.currentTimeMillis();
        if (v.getId() != R.id.main_menu) {
            if (currClickTime - lastClickTime < clickDividerMillions) {
                return;
            }
            lastClickTime = currClickTime;
        }

        switch (v.getId()) {
        case R.id.main_score_circle_view:
            stopScoreBorderAnim();
            startOptimizeProcess();
            break;
        case R.id.main_memery:
            startActivity(new Intent(MainActivity.this, MemoryActivity.class));
            break;
        case R.id.main_battery:
            startActivity(new Intent(MainActivity.this, BatteryActivity.class));
            break;
        case R.id.main_data:
            startActivity(new Intent(MainActivity.this, DataActivity.class));
            break;
        case R.id.main_storage:
            startActivity(new Intent(MainActivity.this, StorageActivity.class));
            break;
		case R.id.main_bottom_button:// ok
            ok();
            break;
        case R.id.main_menu:// 右上角菜单
            showMenuPopupWindow();
            break;
        case R.id.main_menu_optimizalist:// 菜单Optimized
            dissmissMenuPop();
            jumpToOptimizedActivity();
            break;
        case R.id.main_menu_aboutus:// 菜单About us
            dissmissMenuPop();
            startActivity(new Intent(this, AboutUsActivity.class));
            break;
        case R.id.main_app_statistic:
            jumpToStatistic();
            break;
        default:
            break;
        }
    }
    
    // /退出相关
    private static boolean isExit = false;
    private static Timer tExit = null;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (!isExit) {
                isExit = true;
/*                if (tExit != null) {
                    tExit.cancel();
                }*/
                tExit = new Timer();
                TimerTask task = new TimerTask() {

                    @Override
                    public void run() {
                        isExit = false;
                    }
                };
                Toast.makeText(this, R.string.main_exit_tips_msg, Toast.LENGTH_SHORT).show();
                tExit.schedule(task, 2000);
            } else {
                finish();
            }
            return true;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        AutoStartBlackList.getInstance().closeAutoStart();
        super.onDestroy();
        sMainActivity = null;
    }
    
    private static  void setActivity(MainActivity a) {
    	sMainActivity = a;
    }
    
    private class MemoryAppListGetter extends Thread {

		@Override
		public void run() {
			animationList = com.tcl.manager.score.InstalledAppProvider.getInstance().getInstallUserApp();
			// running scanning memory
			mUIHandler.sendEmptyMessage(MSG_MEMORY_ANIMATION_START);
			runAddingList();
			runRemovingList();
			
			mUIHandler.sendEmptyMessage(MSG_CACHE_ANIMATION_START);
			do {
				runAddingList();
			}
			while (scoredStep < 1) ;
			runRemovingList();
			
			mUIHandler.sendEmptyMessage(MSG_RISK_ANIMATION_START);
			do {
				runAddingList();
			} while (scoredStep < 2) ;
			runRemovingList();
			
			mUIHandler.sendEmptyMessage(MSG_CHECK_SCORE_FINISH);
		}

		private void runAddingList() {
			long begin = SystemClock.elapsedRealtime();
			for (ApplicationInfo info : animationList) {
				ScanningItem item = new ScanningItem();
				
				item.pkgName = info.packageName;
				item.label = PkgManagerTool.getAppName(getApplicationContext(), item.pkgName);
				Message msg = mUIHandler.obtainMessage(MSG_SCANNING_ANIMATION);
				loadAppIcon(item);
				if (msg == null) {
					msg = new Message();
					msg.what = MSG_MEMORY_ANIMATION_START;
				}
				msg.obj = item;
				try {
					Thread.sleep(80L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				mUIHandler.sendMessage(msg);
				long now = SystemClock.elapsedRealtime();
				if (now - begin > 1000) {
					break;
				}
			}
		}

		private void runRemovingList() {
			mUIHandler.post(new Runnable() {
				
				@Override
				public void run() {
					if (mScanningAdapter != null && mScanningAdapter.removingItems()) {
						mUIHandler.postDelayed(this, 80L);
					}
					
				}
			});
		}
		 
		 
 }

   /* private void startCircleMoveUp(int offsetYDP) {

        final int movePx = AndroidUtil.convertDIP2PX(this, offsetYDP);
        TranslateAnimation circleAnimation = new TranslateAnimation(0, 0, 0, 0 - movePx);
        circleAnimation.setDuration(500);
        circleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mScoreCircleView.clearAnimation();
                LinearLayout.LayoutParams circleLayoutParams = (LinearLayout.LayoutParams) mScoreCircleView.getLayoutParams();
                int bottomMargin = circleLayoutParams.bottomMargin;
                int topMargin = circleLayoutParams.topMargin;
                circleLayoutParams.setMargins(0, topMargin - movePx, 0, bottomMargin);
                mScoreCircleView.setLayoutParams(circleLayoutParams);

                mIsCircleUp = true;
            }
        });
        mScoreCircleView.startAnimation(circleAnimation);

    }*/

   /* private void startCircleMoveDown(int offsetYDP) {
        final int movePx = AndroidUtil.convertDIP2PX(this, offsetYDP);

        TranslateAnimation circleAnimation = new TranslateAnimation(0, 0, 0, movePx);
        circleAnimation.setDuration(500);
        circleAnimation.setAnimationListener(new AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mScoreCircleView.clearAnimation();
                LinearLayout.LayoutParams circleLayoutParams = (LinearLayout.LayoutParams) mScoreCircleView.getLayoutParams();
                int bottomMargin = circleLayoutParams.bottomMargin;
                int topMargin = circleLayoutParams.topMargin;
                circleLayoutParams.setMargins(0, topMargin + movePx, 0, bottomMargin);
                mScoreCircleView.setLayoutParams(circleLayoutParams);
                mIsCircleUp = false;
            }
        });
        mScoreCircleView.startAnimation(circleAnimation);
    }
*/
    
//    private static final long SCORE_CHANGE_MILLIN = 1000;

/*    private void expand(final View v, AnimationListener listener) {
        int width = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);

        int height = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(width, height);
        // v.measure(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(this, 70));
        final int targetHeight = v.getMeasuredHeight();
        v.getLayoutParams().height = 0;
        v.setVisibility(View.VISIBLE);

        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    // isOpened = true;
                    mIsCircleUp = false;
                }

                v.getLayoutParams().height = (interpolatedTime == 1) ? LayoutParams.WRAP_CONTENT : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        animation.setDuration(300);
        animation.setAnimationListener(listener);
        v.startAnimation(animation);
    }*/

/*    private void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation animation = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    // isOpened = false;
                    mIsCircleUp = true;
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        animation.setDuration(300);
        v.startAnimation(animation);
    }
*/
    
//    private View animView1;
//    private View animView2;
//    private View animView3;

/*    private Animation createSpreadAnim(int startOffset) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.anim_score_circle_spread);
        anim.setStartOffset(startOffset);
        return anim;
    }*/

/*    private View createAnimView() {
        View animView = new View(this);
        int width = AndroidUtil.convertDIP2PX(this, 180);
        RelativeLayout.LayoutParams animViewParams = new RelativeLayout.LayoutParams(width, width);
        int location[] = new int[2];
        mScoreCircleBorder.getLocationInWindow(location);
        NLog.d("aaron", "getLocationInWindow  %s", Arrays.toString(location));
        Rect rect = new Rect();
        mScoreCircleBorder.getWindowVisibleDisplayFrame(rect);
        NLog.d("aaron", "getWindowVisibleDisplayFrame  %s", rect.toShortString());
        int rootlocation[] = new int[2];
        mRootLayout.getLocationInWindow(rootlocation);
        animViewParams.leftMargin = location[0] - rootlocation[0];
        animViewParams.topMargin = location[1] - rootlocation[1];
        animView.setBackgroundResource(R.drawable.shape_score_circle_outside);
        mRootLayout.addView(animView, animViewParams);
        return animView;
    }*/

   

    /**
     * 重启动画，为了解决黑屏安装，动画显示位置不对的bug
     */
/*    private void restartScoreBorderAnim() {
        if (animView1 != null) {
            // 表示有转圈,然后重启动画
            stopScoreBorderAnim();
            startScoreBorderAnim();
        }
    }*/



/*    @Override
    public void totalScoreBack(List<AppScoreInfo> list, int totalScore, int appsScore, int memoryScore) {
        *//** 初始化数据 **//*
        PageDataProvider.getInstance().init(ManagerApplication.sApplication.getApplicationContext(), list);
        PageDataProvider.getInstance().score = appsScore + memoryScore;
        PageDataProvider.getInstance().memoryScore = memoryScore;
        PageDataProvider.getInstance().appScore = appsScore;
        *//** debug **//*
        PageDataProvider.getInstance().debug = list;

//        mUIHandler.sendMessage(mUIHandler.obtainMessage(MSG_CHECK_SCORE_FINISH));
    }*/

    

   /* @Override
    public void memoryScoreBack(int memoryScore) {

    }*/
	 
	 //*********************Part time code for debugging animations*******************************
	/* private class ParttimeThread extends Thread {

		@Override
		public void run() {
			try {
				Thread.sleep(1000);
				Message msg = mUIHandler.obtainMessage(MSG_MEMORY_SCORE_FINISH);
				if (msg == null) {
					msg = new Message();
					msg.what = MSG_MEMORY_SCORE_FINISH;
				}
				msg.arg1 = 68;
				mUIHandler.sendMessage(msg);
				
				Thread.sleep(4000);
				msg = mUIHandler.obtainMessage(MSG_RISK_SCORE_FINISH);
				if (msg == null) {
					msg = new Message();
					msg.what = MSG_RISK_SCORE_FINISH;
				}
				msg.arg1 = 23;
				mUIHandler.sendMessage(msg);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		 
		 
	 }*/
    //*********************End part time code****************************************************
	 
	 
	 
	 
	 
	

	
}
