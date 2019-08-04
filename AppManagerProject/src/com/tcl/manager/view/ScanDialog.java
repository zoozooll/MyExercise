package com.tcl.manager.view;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;
 

import com.tcl.manager.activity.adapter.ScanAdapter;
import com.tcl.manager.score.AppScoreProvider;
import com.tcl.manager.util.AndroidUtil;
import com.tcl.manager.util.ImageUtils;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.mie.manager.R;

/**
 * @Description: 扫描对话框
 * @author wenchao.zhang
 * @date 2015年1月10日 下午2:57:41
 * @copyright TCL-MIE
 */

public class ScanDialog extends Dialog {

	private AutoScrollViewPager mViewPager;

	public ScanDialog(Context context) {
		super(context, R.style.dialog_scan_dialog);

		setCancelable(false);
		setCanceledOnTouchOutside(false);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.main_scan_layout);
		setupWindow();

		setupViews();

	}

	private void setupWindow() {
		getWindow().setLayout(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT);
		// Window dialogWindow = this.getWindow();
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindow().getWindowManager().getDefaultDisplay().getMetrics(dm);
		// WindowManager.LayoutParams p = dialogWindow.getAttributes(); //
		// 获取对话框当前的参数值
		// p.width = dm.widthPixels;
		// p.height = dm.heightPixels;
		// dialogWindow.setAttributes(p);
	}

	private void setupViews() {
		Collection<ApplicationInfo> apps = PkgManagerTool
				.getInstalledAppFilter(getContext());
		List<Bitmap> iconList = new ArrayList<Bitmap>();
		for (ApplicationInfo applicationInfo : apps) {
			Drawable drawable = AppScoreProvider.getInstance()
					.getIconByPkgName(applicationInfo.packageName);
			Bitmap zoomBM = ImageUtils.drawableToBitmap(drawable,
					AndroidUtil.convertDIP2PX(getContext(), 100),
					AndroidUtil.convertDIP2PX(getContext(), 100));
			iconList.add(zoomBM);
		}

		mViewPager = (AutoScrollViewPager) findViewById(R.id.main_scan_viewpager);

		final List<View> listViews = new ArrayList<View>();

		for (int i = 0; i < iconList.size(); i++) {
			RelativeLayout parent = new RelativeLayout(getContext());

			ImageView iv = new ImageView(getContext());
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				AndroidUtil.convertDIP2PX(getContext(), 100), AndroidUtil.convertDIP2PX(
							getContext(), 100));
			params.addRule(RelativeLayout.CENTER_IN_PARENT);
			iv.setLayoutParams(params);
			iv.setScaleType(ScaleType.FIT_CENTER);
			iv.setImageBitmap(iconList.get(i));
			parent.addView(iv);
			listViews.add(parent);
		}

		ScanAdapter adapter = new ScanAdapter(listViews);
		mViewPager.setAdapter(adapter);

		mViewPager.setInterval(100);
		mViewPager.setCycle(true);
		mViewPager.setStopScrollWhenTouch(false);
		mViewPager.setAutoScrollDurationFactor(2.2);
		mViewPager.startAutoScroll();

	}

}
