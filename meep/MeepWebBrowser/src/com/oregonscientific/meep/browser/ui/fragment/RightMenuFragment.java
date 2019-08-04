package com.oregonscientific.meep.browser.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
import com.oregonscientific.meep.browser.BrowserUtility;
import com.oregonscientific.meep.browser.Consts;
import com.oregonscientific.meep.browser.R;
import com.oregonscientific.meep.browser.WebBrowserActivity;
import com.oregonscientific.meep.browser.database.Bookmark;
import com.oregonscientific.meep.browser.ui.adapter.BookmarkAdapter;
import com.oregonscientific.meep.browser.ui.adapter.RecommendationAdapter;
import com.oregonscientific.meep.browser.ui.adapter.ViewHolder;
import com.oregonscientific.meep.browser.ui.adapter.WebsiteObject;
import com.oregonscientific.meep.browser.ui.dialog.BookmarkDialog;
import com.oregonscientific.meep.customdialog.CommonPopup;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.PermissionManager;
import com.oregonscientific.meep.recommendation.IRecommendationServiceCallback;
import com.oregonscientific.meep.recommendation.Recommendation;
import com.oregonscientific.meep.recommendation.RecommendationManager;

public class RightMenuFragment extends Fragment {
	public static final String TAG = "RightMenuFragment";

	private static final int MODE_OS = 0;
	private static final int MODE_PARENTS = 1;
	private static final int MODE_BOOKMARK = 2;
	private int mode;
	private String isRecommended = Boolean.TRUE.toString();

	MyTextView menuTitle;
	MyTextView menuTitleShadow;
	View menuBackground;
	ListView listview;
	ArrayList<WebsiteObject> osList;
	ArrayList<WebsiteObject> parentsList;
	ArrayList<String> bookmarkList;

	RecommendationAdapter osAdapter;
	RecommendationAdapter parentAdapter;
	BookmarkAdapter bookmarkAdapter;

	PopupWindow popWindow = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		View view = inflater.inflate(R.layout.right_menu, container, false);

		menuTitle = (MyTextView) view.findViewById(R.id.menu_title);
		menuTitleShadow = (MyTextView) view.findViewById(R.id.menu_title_shadow);
		menuBackground = view.findViewById(R.id.menu_background);

		listview = (ListView) view.findViewById(R.id.listview);

		osList = new ArrayList<WebsiteObject>();
		parentsList = new ArrayList<WebsiteObject>();
		bookmarkList = new ArrayList<String>();

		osAdapter = new RecommendationAdapter(getActivity(), R.layout.item_recommend, osList);
		parentAdapter = new RecommendationAdapter(getActivity(), R.layout.item_recommend, parentsList);
		Cursor cursor = ((WebBrowserActivity) getActivity()).getBookmarkCursor();
		bookmarkAdapter = new BookmarkAdapter(getActivity(), R.layout.item_recommend, cursor);

		view.findViewById(R.id.tab_os).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayOsRecommendations();
			}
		});
		view.findViewById(R.id.tab_parents).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayParentsRecommendations();
			}
		});
		view.findViewById(R.id.tab_bookmark).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayBookmark();
			}
		});

		initListview();

		return view;
	}

	public void initListview() {
		listview.setAdapter(osAdapter);
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Log.d("checkurl",((ViewHolder) arg1.getTag()).website_object.getUrl()+","+isRecommended);
				((WebBrowserActivity)getActivity()).startBrowserWebsiteTask(((ViewHolder) arg1.getTag()).website_object.getUrl(),isRecommended);
				marqueeView(arg1);
			}
		});

	}

	public int[] generateRingMenuMargins(View view) {
		final int[] location = new int[2];
		final int[] margins = new int[2];
		view.getLocationOnScreen(location);
		float x = location[Consts.MARGIN_LEFT];
		float y = location[Consts.MARGIN_TOP] - 60;
		// update coordination
		if (y < 0)
			y = 0;
		if (x < 0)
			x = 0;

		int marginLeft = (int) x;
		if (marginLeft > 500) {
			marginLeft = 500;
		}
		int marginTop = (int) y;
		if (marginTop > 230) {
			marginTop = 230;
		}
		// return results
		margins[Consts.MARGIN_LEFT] = marginLeft;
		margins[Consts.MARGIN_TOP] = marginTop;
		return margins;
	}

	/**
	 * show ring menu
	 * 
	 * @param view
	 *            original view
	 */
	private void showRingMenu(View view) {
		int[] margins = generateRingMenuMargins(view);
		// get initial layout object
		LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View ringMenuShow = layoutInflater.inflate(R.layout.ring_menu_rename_delete, null);
		popWindow = new PopupWindow(ringMenuShow, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		RelativeLayout menuBackground = (RelativeLayout) ringMenuShow.findViewById(R.id.contentLayoutleft);
		// update margins
		android.widget.FrameLayout.LayoutParams layoutParams = (android.widget.FrameLayout.LayoutParams) menuBackground.getLayoutParams();
		layoutParams.leftMargin = margins[Consts.MARGIN_LEFT];
		layoutParams.topMargin = margins[Consts.MARGIN_TOP];
		menuBackground.setLayoutParams(layoutParams);
		// update layout
		popWindow.setContentView(ringMenuShow);
		popWindow.update();
		// add onClick event
		addRingMenuOnClickListeners(ringMenuShow, ((ViewHolder) view.getTag()).website_object);
		// popWindow.showAtLocation(view, Gravity.START, (int)x+70, (int)y-100);
		popWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
	}

	private void addRingMenuOnClickListeners(View rootView,
			final WebsiteObject object) {
		// add onClick event
		FrameLayout windowBackground = (FrameLayout) rootView.findViewById(R.id.two_button_left);
		windowBackground.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				hideRingMenu();
			}
		});
		ImageButton btnRename = (ImageButton) rootView.findViewById(R.id.btnRename);
		btnRename.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideRingMenu();
				Bookmark bookmark = new Bookmark(object.getId(), object.getName(), object.getUrl());
				BookmarkDialog dialog = new BookmarkDialog(getActivity(), BookmarkDialog.MODE_RENAME, bookmark);
				dialog.setDialogTitle(R.string.rename_bookmark);
				dialog.show();

			}
		});
		ImageButton btnDelete = (ImageButton) rootView.findViewById(R.id.btnDelete);
		btnDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				hideRingMenu();
				CommonPopup dialog = new CommonPopup(getActivity(), R.string.delete_bookmark, R.string.delete_bookmark_message);
				dialog.setTitleSize(35);
				dialog.setOnClickOkButtonListener(new OnClickOkButtonListener() {

					@Override
					public void onClickOk() {
						Bookmark deleteBookmark = new Bookmark();
						deleteBookmark.setId(object.getId());
						((WebBrowserActivity) getActivity()).deleteBookmark(deleteBookmark);

					}
				});
				dialog.setEnableCloseButton(true);
				dialog.show();
			}
		});

	}

	/**
	 * hide ring menu
	 */
	public void hideRingMenu() {
		if (popWindow != null && popWindow.isShowing()) {
			popWindow.dismiss();
		}
	}

	// ******************

	public void displayOsRecommendations() {
		Log.d(TAG, "tab:os-blue");
		mode = MODE_OS;
		isRecommended = Boolean.TRUE.toString();
		menuTitle.setText(R.string.menu_os_recommendations);
		menuTitleShadow.setText(R.string.menu_os_recommendations);
		menuTitleShadow.setStroke(5, getResources().getColor(R.color.shadowblue));
		menuBackground.setBackgroundResource(R.drawable.b_left_bg_os);
		// TODO:update list
		listview.setAdapter(osAdapter);
		osAdapter.notifyDataSetChanged();
		listview.setOnItemLongClickListener(null);
	}

	public void displayParentsRecommendations() {
		Log.d(TAG, "tab:parents-yellow");
		mode = MODE_PARENTS;
		isRecommended = Boolean.TRUE.toString();
		menuTitle.setText(R.string.menu_parents_recommendations);
		menuTitleShadow.setText(R.string.menu_parents_recommendations);
		menuTitleShadow.setStroke(5, getResources().getColor(R.color.shadowyellow));
		menuBackground.setBackgroundResource(R.drawable.b_left_bg_parents);
		// TODO:update list
		listview.setAdapter(parentAdapter);
		parentAdapter.notifyDataSetChanged();
		listview.setOnItemLongClickListener(null);
	}

	public void displayBookmark() {
		Log.d(TAG, "tab:bookmark-red");
		mode = MODE_BOOKMARK;
		isRecommended = Boolean.FALSE.toString();
		menuTitle.setText(R.string.menu_bookmark);
		menuTitleShadow.setText(R.string.menu_bookmark);
		menuTitleShadow.setStroke(5, getResources().getColor(R.color.shadowred));
		menuBackground.setBackgroundResource(R.drawable.b_left_bg_bookmark);
		// TODO:update list
		listview.setAdapter(bookmarkAdapter);
		bookmarkAdapter.notifyDataSetChanged();
		// ???
		listview.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				showRingMenu(arg1);
				return true;
			}
		});
		
	}

	public void initRecommendationList() {
		getCurrentAllRecommendations();
		setListenerRecommendationsFromServer();
	}

	public void refresh() {
		osAdapter.notifyDataSetChanged();
		parentAdapter.notifyDataSetChanged();
		bookmarkAdapter.getCursor().requery();
	}

	public void marqueeView(View view) {
		view.setSelected(true);
		view.requestFocus();
	}

	public void setListenerRecommendationsFromServer() {
		RecommendationManager recommendationManager = (RecommendationManager) ServiceManager.getService(getActivity().getApplicationContext(), ServiceManager.RECOMMENDATION_SERVICE);
		registerCallbackForRecommendation(RecommendationManager.TYPE_WEB_FROM_ADMIN, Consts.TYPE_OS_RECOMMENDATION, recommendationManager);
		registerCallbackForRecommendation(RecommendationManager.TYPE_WEB_FROM_PARENT, Consts.TYPE_PARENTS_RECOMMENDATION, recommendationManager);
	}

	private void registerCallbackForRecommendation(String serviceType,
			final int localType, RecommendationManager recommendationManager) {
		recommendationManager.registerCallback(serviceType, new IRecommendationServiceCallback() {

			@Override
			public IBinder asBinder() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public void onReceiveRecommendation(List<Recommendation> arg0)
					throws RemoteException {
				List<Recommendation> list = arg0;
				updateRecommendationList(list, localType);

			}
		});
	}

	public void getCurrentAllRecommendations() {
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				RecommendationManager recommendationManager = (RecommendationManager) ServiceManager.getService(getActivity().getApplicationContext(), ServiceManager.RECOMMENDATION_SERVICE);
				String id = BrowserUtility.getAccountID(getActivity());
				if (id == null || id.equals("")) {
					BrowserUtility.printLogcatDebugMessage("ID is Empty");
				} else {
					BrowserUtility.printLogcatDebugMessage("ID is " + id);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<Recommendation> list = recommendationManager.getAllRecommendations(id);
					updateRecommendationList(list);
				}
			}
		});
	}
	
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			refresh();
			}
		};

	/**
	 * Update mixed type list
	 * 
	 * @param list
	 */
	public void updateRecommendationList(List<Recommendation> list) {
		if (list != null) {
			osList.clear();
			parentsList.clear();
			for (Recommendation r : list) {
				WebsiteObject object = WebsiteObject.generateFromRecommendation(r);
				String type = r.getType();
				if (type.equals(RecommendationManager.TYPE_WEB_FROM_ADMIN)) {
					BrowserUtility.printLogcatDebugMessage("Recommendation List(os) item : "+object.getUrl());
					osList.add(object);
				} else if (type.equals(RecommendationManager.TYPE_WEB_FROM_PARENT)) {
					BrowserUtility.printLogcatDebugMessage("Recommendation List(parent) item : "+object.getUrl());
					parentsList.add(object);
				}
			}
			handler.sendEmptyMessage(0);
		} else {
			BrowserUtility.printLogcatDebugMessage("Recommendation List(0&1) is NULL");
		}
	}

	/**
	 * Update Specific type list
	 * 
	 * @param list
	 * @param type
	 */
	public void updateRecommendationList(List<Recommendation> list, int type) {
		if (list != null) {
			if (type == Consts.TYPE_OS_RECOMMENDATION) {
				osList.clear();
				for (Recommendation r : list) {
					WebsiteObject object = WebsiteObject.generateFromRecommendation(r);
					osList.add(object);
				}
				osAdapter.notifyDataSetChanged();
			} else if (type == Consts.TYPE_PARENTS_RECOMMENDATION) {
				parentsList.clear();
				for (Recommendation r : list) {
					WebsiteObject object = WebsiteObject.generateFromRecommendation(r);
					parentsList.add(object);
				}
				parentAdapter.notifyDataSetChanged();
			}
		} else {
			BrowserUtility.printLogcatDebugMessage("Recommendation List("
					+ type + ") is NULL");
		}
	}

}
