package com.tcl.manager.miniapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.tcl.framework.log.NLog;
import com.tcl.mie.manager.R;

/**
 * 
 * @author difei.zou
 * @date 2014-12-13 下午2:43:34
 * @copyright
 */

public class MiniAppWidgetProvider extends AppWidgetProvider {
	/**
	 * 每删除一次窗口小部件就调用一次
	 */
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		// TODO Auto-generated method stub
		super.onDeleted(context, appWidgetIds);
		
		NLog.e("wenchao", "onDeleted");
	}

	/**
	 * 当最后一个该窗口小部件删除时调用该方法，注意是最后一个
	 */
	@Override
	public void onDisabled(Context context) {
		super.onDisabled(context);
		Intent intent = new Intent(context, MiniAppService.class);
		intent.setAction(MiniAppService.EVENT_DISABLE);
		context.startService(intent);

	}

	/**
	 * 接收窗口小部件点击时发送的广播
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		super.onReceive(context, intent);
	}

	/**
	 * 每次窗口小部件被点击更新都调用一次该方法
	 */
	@Override
	public void onUpdate(Context context, AppWidgetManager appWidgetManager,
			int[] appWidgetIds) {
		super.onUpdate(context, appWidgetManager, appWidgetIds);
		final int N = appWidgetIds.length;
		for (int i = 0; i < N; i++) {
			int appWidgetId = appWidgetIds[i];

			Intent intent = new Intent(context, MiniAppService.class);
			intent.setAction(MiniAppService.SCORE_ICON_CLICK);
			PendingIntent pendingIntent = PendingIntent.getService(context, 0,
					intent, PendingIntent.FLAG_UPDATE_CURRENT);

			
			Intent intent2 = new Intent(context, MiniAppService.class);
			intent2.setAction(MiniAppService.MORE_DETAIL_CLICK);
			PendingIntent pendingIntent2 = PendingIntent.getService(context, 0,
					intent2,
					PendingIntent.FLAG_UPDATE_CURRENT);
			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(),
					R.layout.mini_widget_layout);
			views.setOnClickPendingIntent(R.id.miniapp_window_score_layout,
					pendingIntent);
			views.setOnClickPendingIntent(R.id.miniapp_window_manager,
					pendingIntent2);

			// Tell the AppWidgetManager to perform an update on the current app
			// widget
			appWidgetManager.updateAppWidget(appWidgetId, views);

		}
		
		NLog.e("wenchao", "onUpgrade");
		MiniAppWindowManager.getInstance(context).checkScore();
	}

	/**
	 * 当该窗口小部件第一次添加到桌面时调用该方法，可添加多次但只第一次调用
	 */
	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		Intent intent = new Intent(context, MiniAppService.class);
		intent.setAction(MiniAppService.EVENT_ENABLE);
		context.startService(intent);
		
		NLog.e("wenchao", "onEnabled--");
	}

}
