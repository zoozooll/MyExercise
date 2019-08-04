/**
 * 
 */
package com.beem.project.btf.ui.activity;

import com.beem.project.btf.R;
import com.beem.project.btf.utils.SharedPrefsUtil;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;

/**
 * @author Aaron Lee Created at 上午11:36:57 2015-9-24
 */
public class InnerGuideHelper {
	private static int pictureIndex;

	public static void showAddfriendsGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showAddfriendsGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_addfriends);
			SharedPrefsUtil.putValue(act, "showAddfriendsGuide", true);
		}
	}
	public static void showBBSGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showBBSGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_bbs);
			SharedPrefsUtil.putValue(act, "showBBSGuide", true);
		}
	}
	public static void showTimeflyGuide(Activity act) {
  		if (!SharedPrefsUtil.getValue(act, "showTimeflyGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_timefly,
					R.drawable.innerguide_timefly_faceicon);
			SharedPrefsUtil.putValue(act, "showTimeflyGuide", true);
		}
	}
	public static void showTimeflycameraGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showTimeflycameraGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_timefly_camera);
			SharedPrefsUtil.putValue(act, "showTimeflycameraGuide", true);
		}
	}
	public static void showCartoonCameraGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showCartoonCameraGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_cartoon_camera1, R.drawable.innerguide_cartoon_camera2, R.drawable.innerguide_cartoon_camera3);
			SharedPrefsUtil.putValue(act, "showCartoonCameraGuide", true);
		}
	}
	private static void showUserGuide(Activity act, final int... imgRes) {
		FrameLayout contentView = new FrameLayout(act);
		/*View view1 = new View(this);
		view1.setBackgroundResource(R.drawable.ic_launcher);
		FrameLayout.LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.LEFT | Gravity.TOP;
		lp.topMargin = 50;
		contentView.addView(view1, lp);
		
		view1 = new View(this);
		view1.setBackgroundResource(R.drawable.ic_launcher);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.RIGHT | Gravity.TOP;
		lp.topMargin = 50;
		contentView.addView(view1, lp);
		  
		view1 = new View(this);
		view1.setBackgroundResource(R.drawable.ic_launcher);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
		lp.bottomMargin = 50;
		contentView.addView(view1, lp);
		
		view1 = new View(this); 
		view1.setBackgroundResource(R.drawable.ic_launcher);
		lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
		lp.gravity = Gravity.RIGHT | Gravity.BOTTOM;
		lp.bottomMargin = 50;
		contentView.addView(view1, lp);*/
		//		pw.setWindowLayoutMode(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		pictureIndex = 0;
		contentView.setBackgroundResource(imgRes[pictureIndex]);
		final WindowManager wm = act.getWindowManager();
		contentView.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				pictureIndex++;
				if (pictureIndex < imgRes.length) {
					v.setBackgroundResource(imgRes[pictureIndex]);
				} else {
					if (v.getParent() != null) {
						wm.removeView(v);
					}
				}
			}
		});
		
		WindowManager.LayoutParams wlp = new WindowManager.LayoutParams();
		wlp.flags = WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN;
		wlp.format = PixelFormat.RGBA_8888;
		wm.addView(contentView, wlp);
	}
	public static void showNewsTvGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showNewsTvGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_newstv,
					R.drawable.innerguide_newstv1);
			SharedPrefsUtil.putValue(act, "showNewsTvGuide", true);
		}
	}
	public static void showNewsTopGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showNewsTopGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_newstop);
			SharedPrefsUtil.putValue(act, "showNewsTopGuide", true);
		}
	}
	public static void showNewsTopChangePicGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showNewsTopChangePicGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_newstop_choosepic);
			SharedPrefsUtil.putValue(act, "showNewsTopChangePicGuide", true);
		}
	}
	public static void showSharedGuide(Activity act) {
		if (!SharedPrefsUtil.getValue(act, "showSharedGuide", false)) {
			showUserGuide(act, R.drawable.innerguide_shared);
			SharedPrefsUtil.putValue(act, "showSharedGuide", true);
		}
	}
}
