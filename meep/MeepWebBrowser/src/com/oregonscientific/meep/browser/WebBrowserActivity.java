package com.oregonscientific.meep.browser;

import java.sql.SQLException;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.InputMethodManager;
import android.webkit.URLUtil;
import android.webkit.WebIconDatabase;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;
import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.browser.database.Bookmark;
import com.oregonscientific.meep.browser.database.DatabaseHelper;
import com.oregonscientific.meep.browser.database.History;
import com.oregonscientific.meep.browser.ui.dialog.BookmarkDialog;
import com.oregonscientific.meep.browser.ui.fragment.BrowserFragment;
import com.oregonscientific.meep.browser.ui.fragment.MenuRecentlyFragment;
import com.oregonscientific.meep.browser.ui.fragment.RightMenuFragment;
import com.oregonscientific.meep.customdialog.CommonPopup;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickCloseButtonListener;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.permission.PermissionManager;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class WebBrowserActivity extends SlidingFragmentActivity{

	private static final String TAG = "WebBrowserActivity";
	FrameLayout container;
	private BrowserFragment browserFragment = null;
	private MenuRecentlyFragment recentlyFragment = null;
	private BookmarkDialog addBookmarkDialog = null;
	private RightMenuFragment rightMenuFragment = null;
	EditText searchBox;
	ImageView imgFavicon;
	private SlidingMenu menu;
	private DatabaseHelper databaseHelper = null;
	public static final String PATH_ICON = "/icons/";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_menu);

		initSearchBox();

		initContainer(savedInstanceState);

		initRightMenu();

		WebIconDatabase.getInstance().open(BrowserUtility.getDirs(getCacheDir().getAbsolutePath()
				+ PATH_ICON));

		if (!BrowserUtility.isNetworkAvailable(this)) {
			alertAccessDenied(R.string.nonetwork);
		} else {
			retriveAccountInformation();
		}

		getmPermissionManager();

	}

	@Override
	protected void onStart() {
		super.onStart();
		retriveSecurityLevel();

		if (getIntent().getAction().equals(Intent.ACTION_VIEW)) {
			final String url = getIntent().getDataString();
			Log.d("test", "from other application url:" + url);
			CommonPopup popup = new CommonPopup(WebBrowserActivity.this, R.string.notice_title_wait, R.string.notice_message_redirected_to_third_party);
			popup.setEnableTwoButtonPanel(true);
			popup.setEnableCloseButton(true);
			popup.setOnClickOkButtonListener(new OnClickOkButtonListener() {
				@Override
				public void onClickOk() {
					// TODO Auto-generated method stub
					new BrowserWebsiteAsyncTask().execute(url,Boolean.FALSE.toString());
				}
			});
			popup.show();
		}
	}

	public void initSearchBox() {
		searchBox = (EditText) findViewById(R.id.searchBox);
		imgFavicon = (ImageView) findViewById(R.id.imgFavicon);
		searchBox.setOnKeyListener(new OnKeyListener() {

			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if ((event.getAction() == KeyEvent.ACTION_DOWN)
						&& (keyCode == KeyEvent.KEYCODE_ENTER)) {
					new BrowserWebsiteAsyncTask().execute(searchBox.getText().toString());
					return true;
				}
				return false;
			}
		});
		searchBox.setOnEditorActionListener(new OnEditorActionListener() {

			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				String url = searchBox.getText().toString();
				url = url.trim();
				url = url.replace(" ", "+");
				searchBox.clearFocus();
				new BrowserWebsiteAsyncTask().execute(url,Boolean.FALSE.toString());
				BrowserUtility.hideKeyboard(WebBrowserActivity.this, v);
				return false;
			}
		});

		searchBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (!allow_search) {
					BrowserUtility.hideKeyboard(WebBrowserActivity.this, searchBox);
					BrowserUtility.alertMessage(WebBrowserActivity.this, R.string.browser_title_blocked, R.string.disable_search_function);
				}

			}
		});
	}

	public void initContainer(Bundle savedInstanceState) {
		container = (FrameLayout) findViewById(R.id.fragment_container);
		// Check that the activity is using the layout version with the
		// fragment_container FrameLayout
		if (container != null) {
			if (savedInstanceState != null)
				return;
			// Create an instance of MenuNavigationFragment
			recentlyFragment = MenuRecentlyFragment.newInstance();

			// add fragment to the fragment container layout
			addFragment(recentlyFragment);
		}
	}

	public void onClickMenuButton(View view) {
		hideKeyboard(view);
		switch (view.getId()) {
		case R.id.btnPrevious:
			toBackPage();
			break;
		case R.id.btnNext:
			toForwardsPage();
			break;
		case R.id.btnRefresh:
			refreshWebview();
			break;
		case R.id.btnSearch:
			new BrowserWebsiteAsyncTask().execute(searchBox.getText().toString().trim(),Boolean.FALSE.toString());
			break;
		case R.id.btnAddBookmark:
			if (browserFragment != null)
				popupAddBookmark(browserFragment.getCurrentUrl());
			break;
		case R.id.btnMainPage:
			toMainPage();
			break;
		case R.id.btnMenu:
			displayRightMenu();
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 */
	public int browserWebsite(String url, boolean isRecommended) {
		if (url == null || url.trim().isEmpty()) {
			return Consts.RESULT_EMPTY_SEARCH;
		}
		BrowserUtility.printLogcatMessageWithTimeStamp("start checking url");
		boolean isUrl = BrowserUtility.isValidDomain(url);
		if (!isUrl) {
			BrowserUtility.printLogcatMessageWithTimeStamp(url
					+ " is not a valid url");
			if (containsBadWord(url)) {
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " is badwords");
				// TODO:popup contain bad words
				return Consts.RESULT_BLOCKED_BADWORD;
			} else {
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " is safty word");
				url = Consts.GOOGLE_SEARCH_PREFIX + url;
			}
		} else {
			if (isRecommended) {
				char lastChar = url.charAt(url.length() - 1);
				if (lastChar == '/') {
					url = url.substring(0, url.length() - 1);
				}
			}
			BrowserUtility.printLogcatMessageWithTimeStamp(url
					+ " is valid url");
			if (!URLUtil.isHttpUrl(url) && !URLUtil.isHttpsUrl(url)) {
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " missing http prfix");
				url = "http://" + url;
			}
			// check URL
			if (!isAccessiableUrl(url)) {
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " is in blacklist");
				// TODO:popup url is in black list
				return Consts.RESULT_BLOCKED_BLACKLIST;
			} else if (url.startsWith("meepstore://")) {
				// TODO:goToStore
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " is meepstore url");
			} else if (url.toLowerCase().contains("youtube.com")) {
				// TODO:goToYoutube
				BrowserUtility.printLogcatMessageWithTimeStamp(url
						+ " is a youtube url");
			}
			BrowserUtility.printLogcatMessageWithTimeStamp(url
					+ " is safty url");
		}
		BrowserUtility.printLogcatMessageWithTimeStamp("start show content page");
		uiHandler.sendEmptyMessage(MESSAGE_SHOW_MENU);
		if (browserFragment == null) {
			BrowserUtility.printLogcatMessageWithTimeStamp("browser is null");
			browserFragment = BrowserFragment.newInstance();
			browserFragment.init(url, isRecommended);
			replaceFragment(browserFragment, false);
		} else {
			BrowserUtility.printLogcatMessageWithTimeStamp("browser is not null");
			browserFragment.updateUrl(url, isRecommended);
		}
		return Consts.RESULT_SUCCESS;
	}

	private PermissionManager mPermissionManager;

	public PermissionManager getmPermissionManager() {
		if (mPermissionManager == null)
			mPermissionManager = (PermissionManager) ServiceManager.getService(this, ServiceManager.PERMISSION_SERVICE);
		return mPermissionManager;
	}

	public void setmPermissionManager(PermissionManager mPermissionManager) {
		this.mPermissionManager = mPermissionManager;
	}

	public boolean containsBadWord(String word) {

		String id = BrowserUtility.getAccountID(this);
		PermissionManager pm = getmPermissionManager();
		if (pm.containsBadwordBlocking(id, word)) {
			Log.d("test", "badword:" + word);
			// try {
			// BrowserUtility.alertMessage(this, R.string.notice_title_opps,
			// R.string.browser_msg_web_blocked);
			// resetSearchBox();
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			return true;
		}
		return false;
	}

	public boolean isAccessiableUrl(String url) {
		String id = BrowserUtility.getAccountID(WebBrowserActivity.this);
		PermissionManager pm = getmPermissionManager();
		if (!pm.isUrlAccessibleBlocking(id, url)) {
			Log.d("test", "blacklist:" + url + "   " + id);
			// try {
			// BrowserUtility.alertMessage(this, R.string.notice_title_opps,
			// R.string.cannot_access_website);
			// resetSearchBox();
			// } catch (Exception e) {
			// // TODO: handle exception
			// }
			return false;
		}
		return true;
	}

	private void toBackPage() {
		if (browserFragment != null) {
			if (browserFragment.canGoBack()) {
				browserFragment.goBack();
			} else {
				replaceFragment(recentlyFragment, false);
				browserFragment = null;
			}
		}
	}

	private void toForwardsPage() {
		if (browserFragment != null) {
			browserFragment.goForwards();
		}
	}

	private void refreshWebview() {
		if (browserFragment != null) {
			browserFragment.reload();
		}
	}

	private void toMainPage() {
		if (browserFragment != null) {
			// update recently
			replaceFragment(recentlyFragment, false);
			browserFragment = null;
		}
		// clear search box
		resetSearchBox();
	}

	public void initRightMenu() {
		// set right menu
		setSlidingActionBarEnabled(false);
		menu = getSlidingMenu();
		menu.setMode(SlidingMenu.RIGHT);
		menu.setShadowDrawable(R.drawable.shadow);
		menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		menu.setBehindScrollScale(1.0f);
		menu.setShadowWidthRes(R.dimen.shadow_width);
		menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		menu.setFadeDegree(0.5f);
		// menu.setSecondaryMenu(R.layout.properties);
		// menu.setSecondaryShadowDrawable(R.drawable.shadow);
		setTitle("Sliding Bar");

		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
		rightMenuFragment = new RightMenuFragment();
		t.replace(R.id.menu_frame, rightMenuFragment);
		t.commit();
	}

	public void displayRightMenu() {
		menu.showMenu();
		if (rightMenuFragment != null) {
			rightMenuFragment.getCurrentAllRecommendations();
		}
	}

	public void dismissRightMenu() {
		menu.showContent();
	}

	public void popupAddBookmark(String url) {
		if (url != null && !url.trim().equals("")) {
			Bookmark newBookmark = browserFragment.getBookmarkObject();
			if (addBookmarkDialog != null)
				addBookmarkDialog = null;
			addBookmarkDialog = new BookmarkDialog(WebBrowserActivity.this, BookmarkDialog.MODE_ADD, newBookmark);
			addBookmarkDialog.show();
		}
	}

	public void popup(int title, int message) {
		CommonPopup popup = new CommonPopup(WebBrowserActivity.this, title, message);
		// popup.show();
		popup.setTextGravity(Gravity.CENTER_VERTICAL);
		popup.show();
	}

	public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	protected void replaceFragment(Fragment fragment, boolean init) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		if (init) {
			fragmentTransaction.add(R.id.fragment_container, fragment);
		} else {
			// fragmentTransaction.setCustomAnimations(R.anim.right_in,
			// R.anim.left_out, R.anim.left_in, R.anim.right_out);
			fragmentTransaction.replace(R.id.fragment_container, fragment);
			fragmentTransaction.addToBackStack(null);
		}
		fragmentTransaction.commit();
	}

	private void addFragment(Fragment fragment) {
		FragmentManager fragmentManager = this.getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.add(R.id.fragment_container, fragment);
		fragmentTransaction.setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right, R.anim.slide_in_right, R.anim.slide_out_left);
		fragmentTransaction.commit();
	}

	private void popFragment(Fragment fragment) {
		FragmentManager fragmentManager = getSupportFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		fragmentTransaction.remove(fragment);
		fragmentTransaction.commit();
	}

	@Override
	public void onBackPressed() {
		if (browserFragment != null) {
			if (menu.isMenuShowing()) {
				rightMenuFragment.hideRingMenu();
			} else {
				toBackPage();
			}
		} else {
			finish();
		}

	}

	public void updateSearchBox(String url, Bitmap favicon) {
		searchBox.setText(url);
		if (favicon != null)
			imgFavicon.setImageBitmap(favicon);
		else
			imgFavicon.setImageResource(R.drawable.meep_browser_favicon);
	}

	public void resetSearchBox() {
		searchBox.setText("");
		imgFavicon.setImageResource(R.drawable.meep_browser_favicon);
	}

	private DatabaseHelper getHelper() {
		if (databaseHelper == null) {
			databaseHelper = OpenHelperManager.getHelper(this, DatabaseHelper.class);
		}
		return databaseHelper;
	}

	public Cursor getBookmarkCursor() {
		try {
			Dao<Bookmark, Integer> bookmarkDao = getHelper().getBookmarkDao();
			// build your query
			QueryBuilder<Bookmark, Integer> qb = bookmarkDao.queryBuilder();
			// when you are done, prepare your query and build an iterator
			CloseableIterator<Bookmark> iterator = bookmarkDao.iterator(qb.prepare());
			try {
				// get the raw results which can be cast under Android
				AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
				Cursor cursor = results.getRawCursor();
				return cursor;
			} finally {
				// iterator.closeQuietly();
			}
		} catch (java.sql.SQLException e) {

		}
		return null;

	}

	public Cursor getHistoryCursor() {
		try {
			Dao<History, Integer> historyDao = getHelper().getHistoryDao();
			// build your query
			QueryBuilder<History, Integer> qb = historyDao.queryBuilder();
			// when you are done, prepare your query and build an iterator
			CloseableIterator<History> iterator = historyDao.iterator(qb.prepare());
			try {
				// get the raw results which can be cast under Android
				AndroidDatabaseResults results = (AndroidDatabaseResults) iterator.getRawResults();
				Cursor cursor = results.getRawCursor();
				return cursor;
			} finally {
				// iterator.closeQuietly();
			}
		} catch (java.sql.SQLException e) {

		}
		return null;

	}

	public void addBookmark(Bookmark bookmark) {
		try {
			getHelper().getBookmarkDao().create(bookmark);
		} catch (java.sql.SQLException e) {
			Log.d("test", e.getMessage());
		} catch (NullPointerException e) {
			Log.d("test", e.getMessage());
		}
		rightMenuFragment.refresh();
	}

	public void deleteBookmark(Bookmark bookmark) {
		try {
			DeleteBuilder db = getHelper().getBookmarkDao().deleteBuilder();
			db.where().eq(Bookmark.ID_FIELD_NAME, bookmark.getId());
			getHelper().getBookmarkDao().delete(db.prepare());
		} catch (java.sql.SQLException e) {
			Log.d("test", e.getMessage());
		} catch (NullPointerException e) {
			Log.d("test", e.getMessage());
		}
		rightMenuFragment.refresh();
	}

	public void updateBookmarkName(int id, String name) {
		try {
			Dao<Bookmark, Integer> bookmarkDao = getHelper().getBookmarkDao();
			QueryBuilder<Bookmark, Integer> queryBuilder = bookmarkDao.queryBuilder();
			queryBuilder.where().eq(Bookmark.ID_FIELD_NAME, id);
			PreparedQuery<Bookmark> pq = queryBuilder.prepare();
			Bookmark b = bookmarkDao.queryForFirst(pq);
			b.setName(name);
			bookmarkDao.createOrUpdate(b);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		rightMenuFragment.refresh();
	}

	public void recordHistoryItem(History item) {
		try {
			getHelper().getHistoryDao().create(item);
		} catch (java.sql.SQLException e) {
			Log.d("test", e.getMessage());
		} catch (NullPointerException e) {
			Log.d("test", e.getMessage());
		}
	}

	/**
	 * 
	 * @return whether store account information successful or not
	 */
	public void retriveAccountInformation() {

		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				AccountManager accountManager = (AccountManager) ServiceManager.getService(WebBrowserActivity.this, ServiceManager.ACCOUNT_SERVICE);
				if (accountManager == null) {
					BrowserUtility.printLogcatDebugMessage("AccountManager is NULL");
					handler.sendMessage(BrowserUtility.generateAlertMessageObject());
					return;
				}

				Account account = accountManager.getLastLoggedInAccountBlocking();
				if (account == null) {
					BrowserUtility.printLogcatDebugMessage("Account is NULL");
					handler.sendMessage(BrowserUtility.generateAlertMessageObject());
					return;
				}
				// store account information to preference
				BrowserUtility.setAccountInformation(getApplicationContext(), account);
				BrowserUtility.printLogcatDebugMessage(account.toJson());
				// init recommendation list in right menu
				handler.sendEmptyMessage(Consts.MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS);
			}

		});
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case Consts.MESSAGE_WHAT_ALERT_MESSAGE_CONTENT:
				alertAccessDenied(msg.getData().getInt(Consts.BUNDLE_KEY_ALERT_MESSAGE_CONTENT));
				break;
			case Consts.MESSAGE_WHAT_GET_ALL_RECOMMENDATIONS:
				rightMenuFragment.initRecommendationList();
				// startGetRecommendationService();
				break;

			default:
				break;
			}
		}
	};

	/**
	 * Alert user cannot access browser
	 * 
	 * @param resId
	 *            string recource to display
	 */
	public void alertAccessDenied(final int resId) {
		CommonPopup popup = new CommonPopup(WebBrowserActivity.this, R.string.notice, resId);
		popup.blockBackButton();
		popup.setOnClickOkButtonListener(new OnClickOkButtonListener() {
			@Override
			public void onClickOk() {
				if (resId == R.string.nonetwork) {
					BrowserUtility.openWifiSettings(WebBrowserActivity.this);
				}
				finish();
			}
		});
		if (resId == R.string.nonetwork) {
			popup.setOnClickCloseButtonListener(new OnClickCloseButtonListener() {
				@Override
				public void onClickClose() {
					WebBrowserActivity.this.finish();
				}
			});
			popup.setEnableCloseButton(true);
		}
		popup.show();
	}

	private static final int URL = 0;
	private static final int IS_RECOMMENDED = 1;
	class BrowserWebsiteAsyncTask extends AsyncTask<String, Void, Integer> {
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showLoadingDialog();
		}

		@Override
		protected Integer doInBackground(String... params) {
			try {
				String isRecommended = params[IS_RECOMMENDED];
				boolean r = false;
				try{
					Boolean.getBoolean(isRecommended);
				}catch(Exception e){
					e.printStackTrace();
				}
				return browserWebsite(params[URL], r);
			} catch (Exception e) {
				return Consts.RESULT_CANCELLED;
			}
		}

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
			case Consts.RESULT_SUCCESS:

				break;
			case Consts.RESULT_EMPTY_SEARCH:
				BrowserUtility.alertMessage(WebBrowserActivity.this, R.string.browser_title_blocked, R.string.empty_search);
				resetSearchBox();
				break;
			case Consts.RESULT_BLOCKED_BADWORD:
				BrowserUtility.alertMessage(WebBrowserActivity.this, R.string.browser_title_blocked, R.string.browser_msg_web_blocked);
				resetSearchBox();
				break;
			case Consts.RESULT_BLOCKED_BLACKLIST:
				if (allow_search) {
					BrowserUtility.alertMessage(WebBrowserActivity.this, R.string.browser_title_blocked, R.string.cannot_access_website);
				} else {
					BrowserUtility.alertMessage(WebBrowserActivity.this, R.string.browser_title_blocked, R.string.browser_suggestion_list);
				}
				resetSearchBox();
				break;
			default:
				break;
			}
		}

	}

	// waiting implement retrieve security level
	public static final String SECURITYLEVEL_DISPLAY_NAME = "securitylevel";
	private boolean allow_search = true;

	public void retriveSecurityLevel() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				int i = 10;
				while (i > 0) {
					i--;
					if (getmPermissionManager() == null) {
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					} else {
						break;
					}
				}
				try {
					List<Permission> permissions = getmPermissionManager().getAccessScheduleBlocking(null, BrowserUtility.getAccountID(WebBrowserActivity.this));
					for (Permission permission : permissions) {
						Component component = permission.getComponent();
						if (component != null
								&& component.getDisplayName().equals(SECURITYLEVEL_DISPLAY_NAME)) {
							Log.d("test", permission.getAccessLevel().toString());
							if (permission.getAccessLevel().toString().equals(AccessLevels.HIGH.toString())) {
								allow_search = false;
							} else {
								allow_search = true;
							}
							return;
						}
					}
				} catch (NullPointerException e) {
					allow_search = true;
				}

			}

		});
	}

	private ProgressDialog dialog;
	public void showLoadingDialog()
	{
		if (dialog == null || !dialog.isShowing()) {
			dialog = ProgressDialog.show(this, "", "", true, true);
			dialog.setCanceledOnTouchOutside(false);
			dialog.setOnCancelListener(new OnCancelListener() {
				
				@Override
				public void onCancel(DialogInterface dialog) {
					if(browserFragment!=null)
					{
						browserFragment.stopLoading();
					}
				}
			});
			dialog.setContentView(R.layout.progress);
			dialog.show();
		}
	}
	
	public void stopLoadingDialog()
	{
		dialog.dismiss();
		dialog = null;
	}
	
	public void startBrowserWebsiteTask(String url,String isRecommended)
	{
		new BrowserWebsiteAsyncTask().execute(url,isRecommended);
	}
	
	public static final int MESSAGE_SHOW_MENU = 0;
	private Handler uiHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case MESSAGE_SHOW_MENU:
				menu.showContent();
				break;

			default:
				break;
			}
		}
		
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// release datahelper
		if (databaseHelper != null) {
			OpenHelperManager.releaseHelper();
			databaseHelper = null;
		}
		// unbind services
		ServiceManager.unbindServices(this);
	}
	
}
