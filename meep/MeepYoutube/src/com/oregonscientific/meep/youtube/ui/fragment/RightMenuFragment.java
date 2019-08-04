package com.oregonscientific.meep.youtube.ui.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.oregonscientific.meep.youtube.YouTubeUtility;
import com.oregonscientific.meep.youtube.Consts;
import com.oregonscientific.meep.youtube.ui.adapter.YoutubeObject;
import com.oregonscientific.meep.youtube.ui.adapter.ViewHolder;
import com.oregonscientific.meep.customfont.MyTextView;
import com.oregonscientific.meep.youtube.R;
import com.oregonscientific.meep.youtube.YouTubeUtility;
import com.oregonscientific.meep.youtube.ui.adapter.BookmarkAdapter;
import com.oregonscientific.meep.youtube.ui.adapter.ListAdapterWebsite;
import com.oregonscientific.meep.youtube.ui.adapter.RecommendationAdapter;
import com.oregonscientific.meep.youtube.ui.adapter.YoutubeObject;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.account.Account;
import com.oregonscientific.meep.account.AccountManager;
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
	
	
	MyTextView menuTitle;
	MyTextView menuTitleShadow;
	View menuBackground;
	ListView listview;
	ArrayList<YoutubeObject> osList;
	ArrayList<YoutubeObject> parentsList;
	ArrayList<String> bookmarkList;
	
	RecommendationAdapter osAdapter;
	RecommendationAdapter parentAdapter;
	BookmarkAdapter bookmarkAdapter;
	
	MainViewCallback mainViewCallback;

	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		View view = inflater.inflate(R.layout.right_menu, container, false);
		
		menuTitle = (MyTextView) view.findViewById(R.id.menu_title);
		menuTitleShadow = (MyTextView) view.findViewById(R.id.menu_title_shadow);
		menuBackground = view.findViewById(R.id.menu_background);
		
		listview = (ListView) view.findViewById(R.id.listview);
		//2013-4-22 -Amy- when click listview ,remove black background
		listview.setCacheColorHint(Color.TRANSPARENT);

		osList = new ArrayList<YoutubeObject>();
		parentsList = new ArrayList<YoutubeObject>();
		bookmarkList = new ArrayList<String>();
				
		osAdapter = new RecommendationAdapter(getActivity(), R.layout.item_recommend, osList);
		parentAdapter = new RecommendationAdapter(getActivity(), R.layout.item_recommend, parentsList);

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
		//2013-5-10 -Amy- remove bookmark button
		/*view.findViewById(R.id.tab_bookmark).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				displayBookmark();
			}
		});*/
		
		initListview();

		return view;
	}

	public void initListview() {
		listview.setAdapter(osAdapter);
		mainViewCallback = (MainViewCallback) getActivity();
		listview.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				mainViewCallback.actionSelectedItem(((ViewHolder) arg1.getTag()).youtubeObject.getUrl());
				marqueeView(arg1);
			}
		});

	}
	
	public void displayOsRecommendations() {
		Log.d(TAG, "tab:os-blue");
		mode = MODE_OS;
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
		menuTitle.setText(R.string.menu_bookmark);
		menuTitleShadow.setText(R.string.menu_bookmark);
		menuTitleShadow.setStroke(5, getResources().getColor(R.color.shadowred));
		menuBackground.setBackgroundResource(R.drawable.b_left_bg_bookmark);
		//TODO:update list
		listview.setAdapter(bookmarkAdapter);
		bookmarkAdapter.notifyDataSetChanged();
	}
	

	public void initRecommendationList() {
		getCurrentAllRecommendations();
		setListenerRecommendationsFromServer();
	}


	public void refresh() {
		osAdapter.notifyDataSetChanged();
		parentAdapter.notifyDataSetChanged();
	}

	public void marqueeView(View view) {
		view.setSelected(true);
		view.requestFocus();
	}

	public void setListenerRecommendationsFromServer() {
		Log.e("XXXXX","setListenerRecommendationsFromServer");
		RecommendationManager recommendationManager = (RecommendationManager) ServiceManager.getService(getActivity().getApplicationContext(), ServiceManager.RECOMMENDATION_SERVICE);
		registerCallbackForRecommendation(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN, Consts.TYPE_OS_RECOMMENDATION, recommendationManager);
		registerCallbackForRecommendation(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT, Consts.TYPE_PARENTS_RECOMMENDATION, recommendationManager);
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
		Log.e("XXXXX","getCurrentAllRecommendations");
		ExecutorService service = Executors.newSingleThreadExecutor();
		service.execute(new Runnable() {

			@Override
			public void run() {
				RecommendationManager recommendationManager = (RecommendationManager) ServiceManager.getService(getActivity().getApplicationContext(), ServiceManager.RECOMMENDATION_SERVICE);
				String userID = YouTubeUtility.getAccountID(getActivity());
				if (userID == null || userID.equals("")) {
					YouTubeUtility.printLogcatDebugMessage("Meeptag is Empty");
				} else {
					YouTubeUtility.printLogcatDebugMessage("Meeptag is " + userID);
					//2013-06-14 - raymond - enhance recommendation loading speed
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					List<Recommendation> list = recommendationManager.getAllRecommendations(userID);
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
				YoutubeObject object = YoutubeObject.generateFromRecommendation(r);
				String type = r.getType();
				if (type.equals(RecommendationManager.TYPE_YOUTUBE_FROM_ADMIN)) {
					YouTubeUtility.printLogcatDebugMessage("Recommendation List(os) item : "+object.getUrl());
					osList.add(object);
				} else if (type.equals(RecommendationManager.TYPE_YOUTUBE_FROM_PARENT)) {
					YouTubeUtility.printLogcatDebugMessage("Recommendation List(parent) item : "+object.getUrl());
					parentsList.add(object);
				}
			}
			handler.sendEmptyMessage(0);
		} else {
			YouTubeUtility.printLogcatDebugMessage("Recommendation List(0&1) is NULL");
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
					YoutubeObject object = YoutubeObject.generateFromRecommendation(r);
					osList.add(object);
				}
				osAdapter.notifyDataSetChanged();
			} else if (type == Consts.TYPE_PARENTS_RECOMMENDATION) {
				parentsList.clear();
				for (Recommendation r : list) {
					YoutubeObject object = YoutubeObject.generateFromRecommendation(r);
					parentsList.add(object);
				}
				parentAdapter.notifyDataSetChanged();
			}
		} else {
			YouTubeUtility.printLogcatDebugMessage("Recommendation List("
					+ type + ") is NULL");
		}
	}

	
	
	public void testdata()
	{
		/*
		osList.add("http://www.google.com");
		osList.add("http://www.dogonews.com");
		osList.add("http://pbskids.org");
		osList.add("http://eo.ucar.edu/webweather");
		parentsList.add("http://oregonscientific.com/products_learningAndFun.asp");
		parentsList.add("http://http://kickinkitchen.tv");
		parentsList.add("http://www.timeforkids.com/news");
		parentsList.add("http://cartoonnetwork.com");
		bookmarkList.add("http://nick.com");
		bookmarkList.add("http://www.factmonster.com");
		bookmarkList.add("http://www.sciencemadesimple.com");
		*/
	}
	
	public interface MainViewCallback {
		public void actionSelectedItem(String videoId);
	}

}
