package com.beem.project.btf.ui.activity.base;

import java.util.Stack;

import android.app.Activity;
import android.content.Intent;
import com.beem.project.btf.constant.SettingKey;
import com.beem.project.btf.ui.activity.AccountConfigureFragment;
import com.beem.project.btf.utils.SharedPrefsUtil;

/**
 * @ClassName: ActivityController
 * @Description: Acitivity控制器，提供结束所有Activity方法
 * @author: yuedong bao
 * @date: 2015-3-6 下午4:31:11
 */
public class ActivityController {
	private static Stack<Activity> activitys = new Stack<Activity>();
	private volatile static ActivityController instance;

	private ActivityController() {
	};
	public static ActivityController getInstance() {
		if (instance == null) {
			synchronized (ActivityController.class) {
				if (instance == null) {
					instance = new ActivityController();
				}
			}
		}
		return instance;
	}
	public synchronized void addActivity(Activity activity) {
		activitys.push(activity);
	}
	public synchronized void removeActivity(Activity activity) {
		activity.finish();
		activitys.removeElement(activity);
		//LogUtils.i("activitys:" + activity.getClass().getSimpleName() + " size:" + activitys.size());
		activity = null;
	}
	public synchronized void finishAllActivity() {
		while (getCurrentActivity() != null) {
			Activity activity = getCurrentActivity();
			removeActivity(activity);
		}
	}
	public void gotoLogin() {
		Activity activity = activitys.get(activitys.size() - 1);
		Intent intent = new Intent(activity, AccountConfigureFragment.class);
		activity.startActivity(intent);
	}
	// 重新登录
	public synchronized void relogin() {
		Activity activity = activitys.get(activitys.size() - 1);
		BeemServiceHelper.getInstance(activity.getApplicationContext())
				.xmppLogout();
		finishAllActivity();
		SharedPrefsUtil.putValue(activity, SettingKey.account_password, "");
		Intent intent = new Intent(activity, AccountConfigureFragment.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		activity.startActivity(intent);
	}
	//得到栈顶(当前)的Activity
	public synchronized Activity getCurrentActivity() {
		if (activitys.empty()) {
			return null;
		}
		return activitys.lastElement();
	}
	//得到栈底的Activity
	public synchronized Activity getBottomActivity() {
		if (activitys.empty()) {
			return null;
		}
		return activitys.firstElement();
	}
	/**
	 * @Title: isAcitivityAlive
	 * @Description: 是否指定的类型的Acitivity存活在活动栈中
	 * @param: @param activity
	 * @param: @return
	 * @return: boolean
	 * @throws:
	 */
	public boolean isAcitivityAlive(Class<?> cls) {
		boolean retVal = false;
		for (Activity act : activitys) {
			if (act.getClass() == cls) {
				retVal = true;
				break;
			}
		}
		return retVal;
	}
}
