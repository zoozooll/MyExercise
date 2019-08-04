package com.mogoo.ping.ctrl;

import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.RemoteApksManager.OnRemoteDataSyncListener;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.utils.UsedDataManager;
import com.mogoo.ping.vo.UsedActivityItem;

import android.app.Activity;
import android.database.Cursor;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.BaseAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;

@Deprecated
public class TabWidgetController implements OnTabChangeListener, OnRemoteDataSyncListener {
	
	private final static String TAG = "TabWidgetController";
	private final static String TAB_SPEC_APPLICATIONS = "Applications";
	private final static String TAB_SPEC_GAMES = "Games";
	private final static String TAB_SPEC_WEB = "Web";

	private Activity mActivity;
	private TabHost mTabHost;
	private TabWidget mTabWidget;
	private ViewGroup layout_tab_content_applications;
	private ViewGroup layout_tab_content_game;
	private WebView webview_tab_content_favorite;
	private LayoutInflater inflater;
	
	private TabContentController mTabContentApplicationsController;
	private TabContentController mTabContentGamesController;
	private WebViewController mWebViewController;
	
	private SoftwaresAdapter applicationsLastedAdapter;
	private SoftwaresAdapter applicationsRecomendAdapter;
	private BaseAdapter applicationsUsedAdapter;
	private SoftwaresAdapter gamesLastedAdapter;
	private SoftwaresAdapter gamesRecomendAdapter;
	private BaseAdapter gamesUsedAdapter;

	public TabWidgetController(Activity activity,TabHost tabHost, TabWidget tabWidget) {
		super();
		mActivity = activity;
		mTabHost = tabHost;
		mTabWidget = tabWidget;
		inflater = LayoutInflater.from(activity);
		init();
	}
	
	private void init() {
		TextView tView = new TextView(mActivity);
		tView.setBackgroundResource(R.drawable.tab_title_applications_selector);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_SPEC_APPLICATIONS).setIndicator(tView)
				.setContent(R.id.layout_tab_content_applications));
		
		tView = new TextView(mActivity);
		tView.setBackgroundResource(R.drawable.tab_title_games_selector);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_SPEC_GAMES).setIndicator(tView)
				.setContent(R.id.layout_tab_content_game));
		
		tView = new TextView(mActivity);
		tView.setBackgroundResource(R.drawable.tab_title_web_selector);
		mTabHost.addTab(mTabHost.newTabSpec(TAB_SPEC_WEB).setIndicator(tView)
				.setContent(R.id.webview_tab_content_favorite));
		
		layout_tab_content_applications = (ViewGroup) mTabHost.findViewById(R.id.layout_tab_content_applications);
		layout_tab_content_game = (ViewGroup) mTabHost.findViewById(R.id.layout_tab_content_game);
		layout_tab_content_game.setVisibility(View.GONE);
		webview_tab_content_favorite = (WebView) mTabHost.findViewById(R.id.webview_tab_content_favorite);
		webview_tab_content_favorite.setVisibility(View.GONE);
		
		Cursor cursor = ApksDao.getInstance(mActivity).queryAllCursorTable(
				DataBaseConfig.ApplicationsLastedTable.TABLE_NAME);
		applicationsLastedAdapter = new SoftwaresAdapter(mActivity, cursor);

		Cursor cursor1 = ApksDao.getInstance(mActivity).queryAllCursorTable(
				DataBaseConfig.ApplicationsRecomendTable.TABLE_NAME);
		applicationsRecomendAdapter = new SoftwaresAdapter(mActivity, cursor1);
		
		applicationsUsedAdapter = new SoftwaresUsedAdapter<UsedActivityItem>(
				mActivity, UsedDataManager.getFullyUsedApks(mActivity));
		
		mTabContentApplicationsController = new TabContentController(mActivity,
				mTabHost, layout_tab_content_applications);
		mTabContentApplicationsController.setGridViewsAdapter(
				applicationsLastedAdapter, applicationsRecomendAdapter,
				applicationsUsedAdapter);
		mTabContentApplicationsController.setContentThreadIndexes(
				RemoteApksManager.TAG_APPLICATIONS_LASTED,
				RemoteApksManager.TAG_APPLICATIONS_RECOMEND);

		Cursor cursor2 = ApksDao.getInstance(mActivity).queryAllCursorTable(
				DataBaseConfig.GamesLastedTable.TABLE_NAME);
		gamesLastedAdapter = new SoftwaresAdapter(mActivity, cursor2);

		Cursor cursor3 = ApksDao.getInstance(mActivity).queryAllCursorTable(
				DataBaseConfig.GamesRecomendTable.TABLE_NAME);
		gamesRecomendAdapter = new SoftwaresAdapter(mActivity, cursor3);

		mTabContentGamesController = new TabContentController(mActivity,
				mTabHost, layout_tab_content_game);
		mTabContentGamesController.setGridViewsAdapter(gamesLastedAdapter,
				gamesRecomendAdapter, applicationsUsedAdapter);
		mTabContentGamesController.setContentThreadIndexes(
				RemoteApksManager.TAG_GAME_LASTED,
				RemoteApksManager.TAG_GAME_RECOMEND);
		
		
		mWebViewController = new WebViewController(webview_tab_content_favorite);
		mTabHost.setCurrentTab(0);
		//onTabChanged(TAB_SPEC_APPLICATIONS);
		mTabHost.setOnTabChangedListener(this);
	}
	
	@Override
	public void onTabChanged(String tabId) {
		if (TAB_SPEC_APPLICATIONS.endsWith(tabId)) {
			mTabContentApplicationsController.beginShowingInfront(TAB_SPEC_APPLICATIONS);
		} else if (TAB_SPEC_GAMES.endsWith(tabId)) {
			mTabContentGamesController.beginShowingInfront(TAB_SPEC_GAMES);
		} else if (TAB_SPEC_WEB.endsWith(tabId)) {
			mWebViewController.beginShowingInfront();
		}
	}

	@Override
	public void onSyncSuccess(Cursor cursor, int tag) {
		switch (tag) {
		case RemoteApksManager.TAG_APPLICATIONS_LASTED:
			applicationsLastedAdapter.changeCursor(cursor);
			break;
		case RemoteApksManager.TAG_APPLICATIONS_RECOMEND:
			applicationsRecomendAdapter.changeCursor(cursor);
			break;
		case RemoteApksManager.TAG_GAME_LASTED:
			gamesLastedAdapter.changeCursor(cursor);
			break;
		case RemoteApksManager.TAG_GAME_RECOMEND:
			gamesRecomendAdapter.changeCursor(cursor);
			break;
		default:
			throw new RuntimeException();
		}
		String format = mActivity.getResources().getString(R.string.toast_update_success);
		CharSequence dateStr = format + DateFormat.format(mActivity.getResources().getString(R.string.toast_update_data_format), System.currentTimeMillis());
		/*Toast toast = Toast.makeText(mActivity, dateStr, Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();*/
		ToastController.makeToast(mActivity, dateStr);
		
	}

	@Override
	public void onSyncFail() {
		/*Toast toast = Toast.makeText(mActivity, mActivity.getResources().getString(R.string.toast_update_fail), Toast.LENGTH_SHORT);
		toast.setGravity(Gravity.BOTTOM, 0, 0);
		toast.show();*/
		ToastController.makeToast(mActivity, R.string.toast_update_fail);
	}
	
	public void onStart() {
		if (TAB_SPEC_APPLICATIONS.equals(mTabHost.getCurrentTabTag())) {
			mTabContentApplicationsController.onStart();
		}  else if (TAB_SPEC_GAMES.equals(mTabHost.getCurrentTabTag())) {
			mTabContentGamesController.onStart();
		} else if (TAB_SPEC_WEB.equals(mTabHost.getCurrentTabTag())) {
			mWebViewController.beginShowingInfront();
		}
	}
	
	public void onStop() {
		if (TAB_SPEC_APPLICATIONS.equals(mTabHost.getCurrentTabTag())) {
			mTabContentApplicationsController.onStop();
		}  else if (TAB_SPEC_GAMES.equals(mTabHost.getCurrentTabTag())) {
			mTabContentGamesController.onStop();
		} else if (TAB_SPEC_WEB.equals(mTabHost.getCurrentTabTag())) {
			//mWebViewController.beginShowingInfront();
		}
		
		UsedDataManager.saveCurrentUsedApkitems(mActivity);
	}
	
	public void onDestroy() {
		
	}

}
