package com.mogoo.ping.ctrl;

import java.util.List;

import com.mogoo.ping.R;
import com.mogoo.ping.ctrl.RemoteApksManager.OnRemoteDataSyncListener;
import com.mogoo.ping.model.ApksDao;
import com.mogoo.ping.model.DataBaseConfig;
import com.mogoo.ping.utils.UsedDataManager;
import com.mogoo.ping.view.ScrollLayout;
import com.mogoo.ping.view.ScrollLayout.SnapToPageListener;
import com.mogoo.ping.vo.UsedActivityItem;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewDebug.FlagToString;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

@Deprecated
public class TabContentController implements SnapToPageListener ,OnClickListener{
	
	private static final String TAG = "TabContentController";
	
	public static final int CONTROLLER_TAB_APPLICATIONS = 1;
	public static final int CONTROLLER_TAB_GAMES = 2;
	
	private Context mContext;
	private TabHost mTabHost;
	private ViewGroup mTabContentView;
	private LinearLayout layout_secondtitles;
	private ScrollLayout scrolllayout_tab_content;
	private TextView tvw_secondtitle_lasted;
	private TextView tvw_secondtitle_recommend;
	private TextView tvw_secondtitle_used;
	private GridView grid_content_softwares_lasted;
	private GridView grid_content_softwares_recommend;
	private GridView grid_content_softwares_used;
	
	private int mTabContentIndex;
	private GridSoftwaresController mLastedGridSoftwaresController;
	private GridSoftwaresController mRecomendGridSoftwaresController;
	private GridSoftwaresController mUsedGridSoftwaresController;
	private Cursor mLastedCursor;
	private Cursor mRecommendCursor;
	private List<UsedActivityItem> mUsedData;
	private int[] secondtitleDiveders;
	private int[] contentThreadIndexes;
	
	private SnapToContentController mSnapToContentController;
	private RemoteApksManager mRemoteApksManager;
	
	public TabContentController(Context context,TabHost tabHost, ViewGroup tabContentView) {
		mContext = context;
		mTabHost = tabHost;
		mTabContentView = tabContentView;
		mRemoteApksManager = RemoteApksManager.getInstance(context);
		init();
	}
	
	public void onStart() {
		startTabContentDataThread(mTabContentIndex);
	}
	
	public void onStop() {
		
	}
	
	public void onDestroy() {
		
	}
	
	private void init() {
		
		tvw_secondtitle_lasted = (TextView) mTabContentView
				.findViewById(R.id.tvw_secondtitle_lasted);
		tvw_secondtitle_recommend = (TextView) mTabContentView
				.findViewById(R.id.tvw_secondtitle_recommend);
		tvw_secondtitle_used = (TextView) mTabContentView
				.findViewById(R.id.tvw_secondtitle_used);
		scrolllayout_tab_content = (ScrollLayout) mTabContentView
				.findViewById(R.id.scrolllayout_tab_content);
		grid_content_softwares_lasted = (GridView) mTabContentView
				.findViewById(R.id.grid_content_softwares_lasted);
		grid_content_softwares_recommend = (GridView) mTabContentView
				.findViewById(R.id.grid_content_softwares_recommend);
		grid_content_softwares_used = (GridView) mTabContentView
				.findViewById(R.id.grid_content_softwares_used);
		scrolllayout_tab_content.setmSnapToPageListener(this);
		
		mTabContentIndex = scrolllayout_tab_content.getCurScreen();
		secondtitleDiveders = new int[] { R.id.tvw_secondtitle_lasted,
				R.id.tvw_secondtitle_recommend, R.id.tvw_secondtitle_used };
		
		swithDividerSecondtitle(mTabContentIndex);
		
		tvw_secondtitle_lasted.setOnClickListener(this);
		tvw_secondtitle_recommend.setOnClickListener(this);
		tvw_secondtitle_used.setOnClickListener(this);
	}
	
	

	
	public void setGridViewsAdapter(BaseAdapter lastedAdapter,
			BaseAdapter recommendAdapter,
			BaseAdapter usedAdapter) {
		if (lastedAdapter != null) {
			mLastedGridSoftwaresController = new GridSoftwaresController(
					(Activity) mContext, grid_content_softwares_lasted,
					lastedAdapter, 0);
		}
		if (recommendAdapter != null) {
			mRecomendGridSoftwaresController = new GridSoftwaresController(
					(Activity) mContext, grid_content_softwares_recommend,
					recommendAdapter, 0);
		}
		if (usedAdapter != null) {
			mUsedGridSoftwaresController = new GridSoftwaresController(
					(Activity) mContext, grid_content_softwares_used,
					usedAdapter, 0);
		}
	}
	

	
	public void beginShowingInfront(String tag) {
		startTabContentDataThread(mTabContentIndex);
	}

	public void setUsedData() {
		mUsedData = UsedDataManager.getFullyUsedApks(mContext);
	}

	@Override
	public void EndsnapToScreen(ScrollLayout scrollLayer,
			int indexCurrentScreen, int indexToScreen) {
		swithDividerSecondtitle(indexToScreen);
		mTabContentIndex = indexToScreen;
		if (mSnapToContentController != null) {
			mSnapToContentController.snapToPage(indexToScreen);
		}
		startTabContentDataThread(indexToScreen);
		
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tvw_secondtitle_lasted:{
			scrolllayout_tab_content.snapToScreen(0);
			break;
		}
		case R.id.tvw_secondtitle_recommend:{
			scrolllayout_tab_content.snapToScreen(1);
			break;
		}
		case R.id.tvw_secondtitle_used:{
			scrolllayout_tab_content.snapToScreen(2);
			break;
		}

		}
	}
	
	private void swithDividerSecondtitle(int currentScreen) {
		for (int i = 0; i < secondtitleDiveders.length; i++) {
			if (i == currentScreen) {
				mTabContentView.findViewById(secondtitleDiveders[i])
						.setBackgroundResource(R.drawable.tab_secondtitle_h1);
			} else {
				mTabContentView.findViewById(secondtitleDiveders[i])
						.setBackgroundResource(
								R.drawable.tab_secondtitle_normal);
			}
		}
	}
	
	private void startTabContentDataThread(int flag) {
		switch (flag) {
		case 0:
			mLastedGridSoftwaresController.setAdapter(false);
			break;
		case 1:
			mRecomendGridSoftwaresController.setAdapter(false);
			break;
		case 2:
			UsedDataManager.getFullyUsedApks(mContext);
			mUsedGridSoftwaresController.setAdapter(false);
			break;
			
		default:
			break;
		}
		if (flag < 2) {
			mRemoteApksManager.startSync(contentThreadIndexes[flag]);
		} else if (flag == 2) {
			
		}
	}
	
	public void setContentThreadIndexes(int... contentThreadIndexes) {
		this.contentThreadIndexes = contentThreadIndexes;
	}

	public SnapToContentController getmSnapToContentController() {
		return mSnapToContentController;
	}

	public void setmSnapToContentController(
			SnapToContentController mSnapToContentController) {
		this.mSnapToContentController = mSnapToContentController;
	}
	public int getTabContentIndex() {
		return mTabContentIndex;
	}
	
	public interface SnapToContentController{
		public void snapToPage(int indexToScreen);
	}
	
	
}
