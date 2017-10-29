package com.iskyinfor.duoduo.ui.lesson;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.SpinnerView;
import com.iskyinfor.duoduo.ui.SpinnerView.SpinnserPopuViewListener;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.BookShelfMenuView;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.downloader.DowanloadManagerActivity;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

public class LessonActivity extends Activity implements OnClickListener,
		SpinnserPopuViewListener {
	/**
	 * 同步教学标题
	 */

	private TextView stepText;

	/**
	 * 选择课程
	 */

	private SpinnerView spinnerView;

	/**
	 * 宫格和列表
	 */

	@SuppressWarnings("unused")
	private PageListView gridView = null;
	@SuppressWarnings("unused")
	private PageListView listView = null;

	/**
	 * 去书店
	 */

	private ImageView goBookStore/* , hignSsearch */;

	/**
	 * 开关按钮和底层菜单
	 */

	private ImageView toggleBtn, listMenu, backKey;

	/**
	 * 底层菜单
	 */

	private PopupWindow menuWindow = null;
	/**
	 * 课程选择
	 */
	private PopupWindow hiphop = null;

	/**
	 * 同步教学和名师指导单击切换按钮
	 */
	private Button lessonBtn, teacherBtn;

	/**
	 * 适配器
	 */
	LessonListViewAdapter<StepLesson> lladapter = null;
	LessonGridViewAdapter lgAdapter = null;

	/**
	 * 加载数据的对话框
	 */
	@SuppressWarnings("unused")
	private ProgressDialog progressDialog = null;

	private LinearLayout changeBG = null;
	private LinearLayout gridChangeBG = null;
	// private LinearLayout serachBG = null;

	/**
	 * 九宫格和列表切换标示 当changeFlag=true时表示列表状态 changeFlag=false时表示九宫格状态
	 */
	private boolean changeFlag = false;
	private int totalpage1 = 0; // 同步教学总条数
	private int totalpage2 = 0; // 名师指导总条数

	private ImageView imageView;
	/**
	 * 用于切换展示的布局
	 */
	private FrameLayout contentFrame;
	/**
	 * 布局加载器
	 */
	private LayoutInflater inflater;

	/**
	 * 同步教学的布局,名师指导的布局
	 */
	@SuppressWarnings("unused")
	private View lessonView, teacherView;

	/**
	 * 同步教学和名师指导页面加载
	 */
	private LessonUi lessonUi;
	private TeacherUi teacherUi;

	/**
	 * 同步教学和名师指导的切换状态
	 */
	private boolean flag = false;

	/**
	 * 分别取得同步教学和名师指导的总条目数
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				totalpage1 = (Integer) msg.obj;
				lessonBtn.setText("同步教学" + "(" + totalpage1 + ")");
				break;
			case 1:
				totalpage2 = (Integer) msg.obj;
				teacherBtn.setText("名师指导" + "(" + totalpage2 + ")");
				break;
			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.lesson_activity);
		initWidget();
	}

	private void initWidget() {
		inflater = LayoutInflater.from(this);

		stepText = (TextView) findViewById(R.id.lesson_text_name);
		lessonBtn = (Button) findViewById(R.id.duoduo_lesson_sysn_class);
		lessonBtn.setOnClickListener(this);
		teacherBtn = (Button) findViewById(R.id.duoduo_lesson_teacher_class);
		teacherBtn.setOnClickListener(this);

		toggleBtn = (ImageView) findViewById(R.id.lesson_img_switch);
		toggleBtn.setOnClickListener(this);
		goBookStore = (ImageView) findViewById(R.id.lesson_bookshelf_shop);
		goBookStore.setOnClickListener(this);

		// hignSsearch = (ImageView) findViewById(R.id.lesson_seach_text);
		// hignSsearch.setOnClickListener(this);
		imageView = (ImageView) findViewById(R.id.lessonimg);
		/**
		 * 选择课程
		 */
		spinnerView = (SpinnerView) findViewById(R.id.lesson_alldata);
		spinnerView.setPopuView(this);

		/**
		 * 底层菜单 和 返回按钮
		 */
		listMenu = (ImageView) findViewById(R.id.duoduo_lesson_list_img);
		listMenu.setOnClickListener(this);
		backKey = (ImageView) findViewById(R.id.duoduo_lesson_back_img);
		backKey.setOnClickListener(this);

		listView = (PageListView) findViewById(R.id.duoduo_lesson_listview_fourth);
		gridView = (PageListView) findViewById(R.id.duoduo_lesson_gridview_fourth);

		changeBG = (LinearLayout) findViewById(R.id.lesson_btn_change);
		// serachBG = (LinearLayout) findViewById(R.id.lesson_linear2);
		gridChangeBG = (LinearLayout) findViewById(R.id.grid_lesson_btn_change);

		// ====================================================================
		contentFrame = (FrameLayout) findViewById(R.id.content_layout);
		lessonView = inflater.inflate(R.layout.lesson_content_layout, null); // 注册同步教学的布局
		teacherView = inflater.inflate(R.layout.teacher_content_layout, null); // 注册名师指导的布局

		lessonUi = new LessonUi(this, handler, contentFrame, changeBG,
				gridChangeBG);
		lessonUi.init();
		lessonUi.loadData(); // 加载列表数据
		teacherUi = new TeacherUi(this, handler, contentFrame, changeBG,
				gridChangeBG);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.duoduo_lesson_sysn_class:
			// TODO 同步教学
			stepText.setText(LessonActivity.this.getResources().getString(
					R.string.sysnlesson));
			isButtoBackground(true);

			if (lessonUi.isInit()) {
				teacherUi.restView();
			} else {
				lessonUi.loadData();
			}
			flag = false;
			break;
		case R.id.duoduo_lesson_teacher_class:
			// TODO 名师指导
			stepText.setText(LessonActivity.this.getResources().getString(
					R.string.teacher));

			isButtoBackground(false);

			if (!teacherUi.isInit()) {
				teacherUi.init();
				teacherUi.loadData();
			} else {
				teacherUi.restView();
			}
			flag = true;
			break;
		case R.id.lesson_img_switch:
			// TODO 开关 -- 空格与列表的切换
			if (changeFlag == true) {
				toggleBtn
						.setBackgroundResource(R.drawable.bookshelf_list_switch_selector);
				imageView.setVisibility(View.VISIBLE);
				changeFlag = false;
			} else {
				toggleBtn
						.setBackgroundResource(R.drawable.bookshelf_switch_selector);
				imageView.setVisibility(View.GONE);
				changeFlag = true;
			}

			loadViewData();
			break;
		case R.id.lesson_bookshelf_shop:

			// TODO 去书店购物
			// new BookstoreTask(LessonActivity.this).execute(StaticData.ALL,
			// 1);
			Intent intent = new Intent(LessonActivity.this,
					BookstoreActivity.class);
			startActivity(intent);
			LessonActivity.this.finish();
			break;
		// case R.id.lesson_seach_text:
		// TODO 高级搜索
		// Intent intent = new Intent(LessonActivity.this,
		// LessonSearchActivity.class);
		// startActivity(intent);
		// break;
		case R.id.duoduo_lesson_list_img:
			// TODO 底层菜单
			if (menuWindow != null && menuWindow.isShowing()) {
				menuWindow.dismiss();
			} else {
				BookShelfMenuView bookShelfMenuView = new BookShelfMenuView(
						LessonActivity.this, 2);
				View menuView = bookShelfMenuView.createView();
				menuWindow = new PopupWindow(menuView,
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				menuWindow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.menu_bg)); // 设置menu菜单背景
				menuWindow.update();
				menuWindow.setFocusable(true);
				menuWindow.showAtLocation(listMenu, Gravity.BOTTOM, 0, 80);
			}
			break;
		case R.id.duoduo_lesson_back_img:
			// TODO 返回
			LessonActivity.this.finish();
			break;
		default:
			break;
		}
	}

	public void popDismiss() {
		if (menuWindow != null && menuWindow.isShowing()) {
			menuWindow.dismiss();
		}
	}

	/**
	 * 为同步教学和名师指导Button设置背景
	 * 
	 * @param btnFlag
	 * @return
	 */
	private boolean isButtoBackground(boolean btnFlag) {
		if (btnFlag) {
			lessonBtn.setBackgroundResource(R.drawable.public_down01_hover);
			teacherBtn.setBackgroundResource(R.drawable.public_down02);
			btnFlag = false;
		} else {
			teacherBtn.setBackgroundResource(R.drawable.public_down02_hover);
			lessonBtn.setBackgroundResource(R.drawable.public_down01);
			btnFlag = true;
		}
		return btnFlag;
	}

	/**
	 * 加载数据
	 */
	private void loadViewData() {
		if (!flag) {
			lessonUi.loadData();
		} else {
			teacherUi.loadData();
		}
	}

	/**
	 * 显示PopupWondow，弹出下拉列表
	 */
	@Override
	public void show(ViewFlipper v, boolean isShow) {
		if (isShow) {
			hiphop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			hiphop.setFocusable(false);
			hiphop.showAsDropDown(spinnerView, 0, 0);
		} else {
			if (hiphop != null || hiphop.isShowing()) {
				hiphop.dismiss();
			}
		}
	}

	/**
	 * 点击搜索键返回主页
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	/**
	 * 页面退出，销毁对话框
	 */
	@Override
	public void finish() {
		if (lessonUi != null) {
			lessonUi.canleDialog();
		}
		if (teacherUi != null) {
			lessonUi.canleDialog();
		}
		
		if (hiphop != null && hiphop.isShowing()) {
			hiphop.dismiss();
		}
		super.finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bookshelf_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.down_manager:
			intent = new Intent();
			intent.setClass(this, DowanloadManagerActivity.class);
			startActivity(intent);
			finish();
			break;
		

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(ListView listView, int position) {
		// TODO Auto-generated method stub
		
	}

	
	
}