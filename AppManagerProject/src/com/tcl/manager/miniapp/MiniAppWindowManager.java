package com.tcl.manager.miniapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.View;
import android.widget.ImageView;
import android.widget.RemoteViews;

import com.tcl.manager.score.ScoreLevel;
import com.tcl.manager.util.LogUtil;
import com.tcl.manager.util.PkgManagerTool;
import com.tcl.mie.manager.R;

/**
 * @Description:
 * @author wenchao.zhang
 * @date 2014年12月19日 下午4:29:34
 * @copyright TCL-MIE
 */

public class MiniAppWindowManager {

	private Context context;

	private MiniAppWindowManager(Context context) {
		this.context = context;
	}

	private static MiniAppWindowManager instance;

	public static MiniAppWindowManager getInstance(Context context) {
		if (instance == null) {
			instance = new MiniAppWindowManager(context);
		}
		return instance;
	}

	// /**
	// * 开始动画
	// *
	// * @param currScore
	// */
	// public void startCircleAnim(int currScore) {
	// RemoteViews views = new RemoteViews(context.getPackageName(),
	// R.layout.mini_widget_layout);
	// // 先清除
	// views.removeAllViews(R.id.miniapp_anim_layout);
	// // 加入动画
	// RemoteViews nestedViews = new RemoteViews(context.getPackageName(),
	// R.layout.miniapp_anim_layout);
	// views.addView(R.id.miniapp_anim_layout, nestedViews);
	// nestedViews.setInt(R.id.miniapp_outside_circle,
	// "setBackgroundResource",
	// ScoreLevel.resolveToMiniappCircleResId(currScore));
	//
	// setupCircleClickable(views, false);
	// setupMoreDetailClickable(views, false);
	// setupScore(views, currScore);
	//
	// AppWidgetManager manager = AppWidgetManager.getInstance(context);
	// int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
	// MiniAppWidgetProvider.class));
	// manager.updateAppWidget(appIds, views);
	// }
	/**
	 * 分数检测完成，更新分数界面
	 * 
	 * @param score
	 * @param installedApps
	 * @param runningApps
	 */
	public void stopCircleAnim(int score, int installedApps, int runningApps,
			int mOptimizableItemCount) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mini_widget_layout);
		// 停止动画
		// views.removeAllViews(R.id.miniapp_anim_layout);

		// 显示optimize
		views.setViewVisibility(R.id.miniapp_window_score_desc, View.VISIBLE);

		views.setTextViewText(
				R.id.miniapp_window_detail_desc,
				getCheckCompleteDescString(installedApps, runningApps,
						mOptimizableItemCount));
		if (mOptimizableItemCount + runningApps > 0) {
			views.setTextViewText(R.id.miniapp_window_status_desc, context
					.getResources().getString(R.string.main_optimize_needed2));
		} else {
			views.setTextViewText(R.id.miniapp_window_status_desc, context
					.getResources().getString(R.string.main_healthy));
		}

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
				MiniAppWidgetProvider.class));
		manager.updateAppWidget(appIds, views);
	}

	/**
	 * 检查完成描述字符串
	 * 
	 * @param installedApps
	 * @param runningApps
	 * @param needOptimizeApps
	 * @return
	 */
	private String getCheckCompleteDescString(int installedApps,
			int runningApps, int needOptimizeApps) {
		StringBuilder sb = new StringBuilder();
		Resources res = context.getResources();
		sb.append(res.getString(R.string.miniapp_label_02))
				.append(installedApps)
				.append(res.getString(R.string.main_list_item_label_03))
				.append('\n');
		int totalOptimizeItems = runningApps + needOptimizeApps;

		if (totalOptimizeItems > 0) {
			sb.append(res.getString(R.string.miniapp_label_04))
					.append(totalOptimizeItems).append('\n')
					.append(res.getString(R.string.miniapp_label_06));
		} else {
			sb.append(res.getString(R.string.miniapp_label_03));
		}

		return sb.toString();
	}

	/**
	 * 开始优化ui
	 */
	public void startOptimizeAnim() {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mini_widget_layout);
		// 先清除
		views.removeAllViews(R.id.miniapp_anim_layout_optimize);
		// 加入动画
		RemoteViews nestedViews = new RemoteViews(context.getPackageName(),
				R.layout.miniapp_optimize_anim_layout);
		views.addView(R.id.miniapp_anim_layout_optimize, nestedViews);

		// 隐藏外圈动画
		views.setViewVisibility(R.id.miniapp_anim_layout, View.GONE);

		// 隐藏optimize
		views.setViewVisibility(R.id.miniapp_window_score_desc, View.GONE);
		// 分数描述文本显示为Score
		// views.setTextViewText(R.id.miniapp_window_score_desc,
		// context.getResources().getString(R.string.miniapp_label_01));

		// 显示进行中的文本
		views.setViewVisibility(R.id.miniapp_window_optimizing, View.VISIBLE);

		setupCircleClickable(views, false);
		setupMoreDetailClickable(views, false);

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
				MiniAppWidgetProvider.class));
		manager.updateAppWidget(appIds, views);
	}

	public void stopOptimizeAnim(int score, int installedApps, int runningApps,
			int needOptimizeApps) {
		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mini_widget_layout);
		// 先清除
		views.removeAllViews(R.id.miniapp_anim_layout_optimize);

		// 更新分数相关
		setupScore(views, score);

		views.setViewVisibility(R.id.miniapp_anim_layout, View.VISIBLE);
		views.setInt(R.id.miniapp_outside_circle, "setImageResource",
				ScoreLevel.resolveToMiniappCircleResId(score));

		// 隐藏此view
		views.setViewVisibility(R.id.miniapp_window_optimizing, View.GONE);

		// 显示optimize
		views.setViewVisibility(R.id.miniapp_window_score_desc, View.VISIBLE);

		// 标题文字
		if (needOptimizeApps > 0) {
			views.setTextViewText(R.id.miniapp_window_status_desc, context
					.getResources().getString(R.string.miniapp_label_10));
		} else {
			views.setTextViewText(R.id.miniapp_window_status_desc, context
					.getResources().getString(R.string.main_healthy));
		}

		// 描述文字
		StringBuilder sb = new StringBuilder();
		Resources res = context.getResources();
		sb.append(res.getString(R.string.miniapp_label_02))
				.append(installedApps)
				.append(res.getString(R.string.main_list_item_label_03))
				.append('\n').append(res.getString(R.string.miniapp_label_05))
				.append(runningApps);
		if (needOptimizeApps > 0) {
			// 需要深度优化
			sb.append('\n').append(needOptimizeApps + " ")
					.append(res.getString(R.string.miniapp_label_09));
		} else {
			sb.append('\n').append(res.getString(R.string.main_healthy));
		}
		views.setTextViewText(R.id.miniapp_window_detail_desc, sb.toString());

		setupCircleClickable(views, true);
		setupMoreDetailClickable(views, true);

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
				MiniAppWidgetProvider.class));
		manager.updateAppWidget(appIds, views);
	}

	/**
	 * 更新检查分数圆圈动画，
	 * 
	 * @param score
	 */
	public void updateCircleAnim(int score) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mini_widget_layout);
		setupScore(views, score);
		setupCircleClickable(views, false);
		setupMoreDetailClickable(views, false);

		views.setInt(R.id.miniapp_outside_circle, "setImageResource",
				ScoreLevel.resolveToMiniappCircleResId(score));

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
				MiniAppWidgetProvider.class));
		manager.updateAppWidget(appIds, views);
	}

	// Get the layout for the App Widget and attach an on-click listener
	// to the button
	/**
	 * 更新分数组件
	 * 
	 * @param scoreClickable
	 * @param moreDetailClickable
	 * @param score
	 */
	public void updateAppWidget2(boolean scoreClickable,
			boolean moreDetailClickable, int score) {

		RemoteViews views = new RemoteViews(context.getPackageName(),
				R.layout.mini_widget_layout);
		setupScore(views, score);
		setupCircleClickable(views, scoreClickable);
		setupMoreDetailClickable(views, moreDetailClickable);
		views.setInt(R.id.miniapp_outside_circle, "setImageResource",
				ScoreLevel.resolveToMiniappCircleResId(score));

		AppWidgetManager manager = AppWidgetManager.getInstance(context);
		int[] appIds = manager.getAppWidgetIds(new ComponentName(context,
				MiniAppWidgetProvider.class));
		manager.updateAppWidget(appIds, views);
	}

	private void setupCircleClickable(RemoteViews views, boolean clickable) {
		Intent intent = new Intent(context, MiniAppService.class);
		if (clickable) {
			intent.setAction(MiniAppService.SCORE_ICON_CLICK);
		} else {
			intent.setAction("unknow");
		}
		PendingIntent pendingIntent = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.miniapp_window_score_layout,
				pendingIntent);
	}

	private void setupScore(RemoteViews views, int score) {
		if (score < 0) {
			views.setTextViewText(R.id.miniapp_window_score, "---");
			views.setInt(R.id.miniapp_window_bg, "setBackgroundResource",
					ScoreLevel.resolveToMiniCircle(0));
			return;
		}
		views.setTextViewText(R.id.miniapp_window_score, String.valueOf(score));
		// views.setTextViewText(R.id.miniapp_window_score_desc,
		// ScoreLevel.resolveToString(score));
		views.setInt(R.id.miniapp_window_bg, "setBackgroundResource",
				ScoreLevel.resolveToMiniCircle(score));
	}

	private void setupMoreDetailClickable(RemoteViews views, boolean clickable) {
		Intent intent = new Intent(context, MiniAppService.class);
		// 不限制点击事件
		// if (clickable) {
		intent.setAction(MiniAppService.MORE_DETAIL_CLICK);
		// } else {
		// intent.setAction("unknow");
		// }
		PendingIntent pendingIntent2 = PendingIntent.getService(context, 0,
				intent, PendingIntent.FLAG_UPDATE_CURRENT);
		views.setOnClickPendingIntent(R.id.miniapp_window_manager,
				pendingIntent2);
	}

	public void showPopupWindow(Rect rect) {
		Intent intent = new Intent(context, MiniAppSuspendWindowActivity.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
				| Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.putExtra("rect", rect);
		context.startActivity(intent);
	}

	public void checkScore() {
		Intent intent = new Intent(context, MiniAppService.class);
		intent.setAction(MiniAppService.EVENT_UPDATE);
		context.startService(intent);
	}

}
