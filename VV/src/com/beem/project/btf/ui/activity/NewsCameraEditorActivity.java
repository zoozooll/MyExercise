package com.beem.project.btf.ui.activity;

import org.apache.commons.lang.StringUtils;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.BeemApplication;
import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.BBSCustomerDialog;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.base.VVBaseFragmentActivity;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.fragment.NewsTVEditorFragment;
import com.beem.project.btf.ui.fragment.NewsTopTitleEditorFragment;
import com.beem.project.btf.ui.views.CartoonShareActivity;
import com.beem.project.btf.ui.views.CartoonShareActivity.RestartActivityType;
import com.beem.project.btf.ui.views.SimpleEditDilaogView;
import com.beem.project.btf.ui.views.SimpleEditDilaogView.BtnListener;
import com.beem.project.btf.utils.BBSUtils;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.PictureUtil;
import com.beem.project.btf.utils.SharedPrefsUtil;
import com.butterfly.vv.GalleryNavigation;

import de.greenrobot.event.EventBus;

public class NewsCameraEditorActivity extends VVBaseFragmentActivity {
	private Context mContext;
	private boolean isSaved = false;
	private LinearLayout news_type_wraper;
	private TextView news_tv, news_toptitle;
	private NewsTVEditorFragment newstveditorfragment;
	private NewsTopTitleEditorFragment toptitleeditorfragment;
	private GalleryNavigation mMyNavigationView;
	private NewsCameraFlagmentType flagmenttype = NewsCameraFlagmentType.NewsTv;
	private String mBmPath;
	private int screenWidth;
	private int screenHeight;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.news_camera_editor);
		mContext = this;
		news_type_wraper = (LinearLayout) findViewById(R.id.news_type_wraper);
		DisplayMetrics dm = BeemApplication.getContext().getResources()
				.getDisplayMetrics();
		screenWidth = dm.widthPixels;
		screenHeight = dm.heightPixels;
		// 导航条设置
		initNavigateView();
		setDefaultFragment();
		initBottomBar();
		EventBus.getDefault().register(this);
	}
	public static void launch(Context ctx) {
		Intent intent = new Intent(ctx, NewsCameraEditorActivity.class);
		ctx.startActivity(intent);
	}

	public enum NewsCameraFlagmentType {
		NewsTv, TopTitle;
	}

	/** 初始化导航条 */
	private void initNavigateView() {
		mMyNavigationView = (GalleryNavigation) findViewById(R.id.main_navigation_view);
		mMyNavigationView.setBtnLeftListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				checkIsSaved();
			}
		});
		mMyNavigationView.setBtnUploadListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isSaved = true;
				/*保存图片*/
				switch (flagmenttype) {
					case NewsTv: {
						if (newstveditorfragment != null) {
							mBmPath = newstveditorfragment.saveBitmap();
						}
						break;
					}
					case TopTitle: {
						if (toptitleeditorfragment != null) {
							mBmPath = toptitleeditorfragment.saveBitmap();
						}
						break;
					}
				}
				if (StringUtils.isEmpty(mBmPath)) {
					Toast.makeText(mContext, "请添加图片", Toast.LENGTH_SHORT)
							.show();
				} else {
					Toast.makeText(mContext, "图片保存路径为:" + mBmPath,
							Toast.LENGTH_SHORT).show();
					CartoonShareActivity.launch(NewsCameraEditorActivity.this,
							mBmPath, RestartActivityType.NewsCamera.toString());
					//记录关闭前模块标识
					SharedPrefsUtil.putValue(mContext, SettingKey.flagmenttype,
							flagmenttype.toString());
					finish();
				}
			}
		});
		mMyNavigationView.setChoiseMode("完成");
		mMyNavigationView.setStringTitle("娱乐相机", null);
	}
	/** 初始化底部导航条 */
	private void initBottomBar() {
		news_tv = (TextView) findViewById(R.id.news_tv);
		news_toptitle = (TextView) findViewById(R.id.news_toptitle);
		news_tv.setOnClickListener(mainlistener);
		news_toptitle.setOnClickListener(mainlistener);
		if (flagmenttype == NewsCameraFlagmentType.NewsTv) {
			news_tv.setSelected(true);
			news_toptitle.setSelected(false);
		} else {
			news_tv.setSelected(false);
			news_toptitle.setSelected(true);
		}
	}

	/** fragment切换监听器 */
	OnClickListener mainlistener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switchFragment(v);
		}
	};

	/** fragment切换 */
	private void switchFragment(View v) {
		FragmentManager fm = getSupportFragmentManager();
		// 开启Fragment事务  
		FragmentTransaction transaction = fm.beginTransaction();
		switch (v.getId()) {
			case R.id.news_tv: {
				flagmenttype = NewsCameraFlagmentType.NewsTv;
				news_tv.setSelected(true);
				news_toptitle.setSelected(false);
				if (newstveditorfragment == null) {
					newstveditorfragment = new NewsTVEditorFragment();
				}
				if (toptitleeditorfragment != null) {
					toptitleeditorfragment.dismissPopupWindow();
				}
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				transaction.replace(R.id.content_wraper, newstveditorfragment);
				break;
			}
			case R.id.news_toptitle: {
				flagmenttype = NewsCameraFlagmentType.TopTitle;
				news_tv.setSelected(false);
				news_toptitle.setSelected(true);
				if (toptitleeditorfragment == null) {
					toptitleeditorfragment = new NewsTopTitleEditorFragment();
				}
				transaction
						.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
				transaction
						.replace(R.id.content_wraper, toptitleeditorfragment);
				break;
			}
		}
		// 事务提交  
		transaction.commit();
	}
	/** 设置默认的fragment */
	private void setDefaultFragment() {
		FragmentManager fm = getSupportFragmentManager();
		FragmentTransaction transaction = fm.beginTransaction();
		String temp = SharedPrefsUtil.getValue(mContext,
				SettingKey.flagmenttype,
				NewsCameraFlagmentType.TopTitle.toString());
		if (temp.equals(NewsCameraFlagmentType.NewsTv.toString())) {
			flagmenttype = NewsCameraFlagmentType.NewsTv;
			newstveditorfragment = new NewsTVEditorFragment();
			transaction.replace(R.id.content_wraper, newstveditorfragment);
		} else {
			flagmenttype = NewsCameraFlagmentType.TopTitle;
			toptitleeditorfragment = new NewsTopTitleEditorFragment();
			transaction.replace(R.id.content_wraper, toptitleeditorfragment);
		}
		transaction.commit();
	}
	/** 检测未保存时按返回键 */
	private void checkIsSaved() {
		if (!isSaved) {
			final BBSCustomerDialog blurDlg = BBSCustomerDialog.newInstance(
					mContext, R.style.blurdialog);
			SimpleEditDilaogView dilaogView = new SimpleEditDilaogView(mContext);
			dilaogView.setTitle("提示:");
			dilaogView.setTextContent("当前图片还未保存，确认退出吗?");
			dilaogView.setPositiveButton("确定", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
					finish();
				}
			});
			dilaogView.setNegativeButton("取消", new BtnListener() {
				@Override
				public void ensure(View contentView) {
					blurDlg.dismiss();
				}
			});
			blurDlg.setContentView(dilaogView.getmView());
			blurDlg.show();
		}
	}
	@Override
	protected void onResume() {
		super.onResume();
		isSaved = false;
	}
	@Override
	public void onBackPressed() {
		boolean isHandle = false;
		if (flagmenttype == NewsCameraFlagmentType.NewsTv) {
			isHandle = newstveditorfragment.onBackPressed();
		} else if (flagmenttype == NewsCameraFlagmentType.TopTitle) {
			isHandle = toptitleeditorfragment.onBackPressed();
		}
		if (!isHandle) {
			checkIsSaved();
		}
	}
	public void onEventMainThread(final EventBusData data) {
		// 判断消息类型
		if (data.getAction() == EventAction.FinishActivity) {
			finish();
		}
	}
	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		//判断点击区域
		if (flagmenttype == NewsCameraFlagmentType.TopTitle) {
			if (ev.getAction() == MotionEvent.ACTION_DOWN) {
				float y = ev.getY();
				float miny = DimenUtils.dip2px(mContext, 42)
						+ BBSUtils.getStatusHeight(mContext);
				float maxy = screenHeight - DimenUtils.dip2px(mContext, 90);
				Log.i("NewsCameraEditorActivity", "~y~" + y + "~miny~" + miny
						+ "~maxy~" + maxy);
				if (toptitleeditorfragment != null) {
					if (y > miny && y < maxy) {
						//Toast.makeText(mContext, "图片编辑区域", Toast.LENGTH_SHORT).show();
						toptitleeditorfragment.dismissPopupWindow();
					} else {
						//Toast.makeText(mContext, "非图片编辑区域", Toast.LENGTH_SHORT).show();
					}
				}
			}
		}
		return super.dispatchTouchEvent(ev);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
		//删除临时图片
		PictureUtil.delTempImage();
	}
}
