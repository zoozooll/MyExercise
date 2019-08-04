package com.oregonscientific.meep.store2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.store2.PopUpDialogFragment.UpdateUserCoinsListener;
import com.oregonscientific.meep.store2.adapter.ListAdapterShelf;
import com.oregonscientific.meep.store2.adapter.ListAdapterShelf.ShowDetailOrPayment;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.RestRequest.SearchItemListener;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public abstract class GenericStoreActivity extends BaseActivity implements UpdateUserCoinsListener,ShowDetailOrPayment{
	public static final int APP_MODE = 0;
	public static final int GAME_MODE = 1;
	public static final int EBOOK_MODE = 2;
	public static final int SEARCH_MODE = 3;
	
	public static final int NO_NETWORK = 3;
	public static final int LOADING = 4;
	public static final int TIMEOUT = 5;
	public static final int UPDATE_SHELF = 6;
	
	public final int ITEM_PER_PAGE = 8;
	
	protected int totalnumber;
	protected String pagenumber;
	protected int mMode;
	protected String mCateogry = "all";
	protected String mPrice = "all";
	protected String type;
	
	ImageButton sortCategory;
	ImageButton sortPrice;
	ImageButton btnSearch;
	Button left;
	Button right;
	Button meepstoreicon;
	
	ImageView sortCategoryImage;
	ImageView sortPriceImage;
	
	TextView pageLabel;
	TextView mUsercoins;
	TextView shelfLabel;
	
	GridView gridView;
	ArrayList<MeepStoreItem> listItem;
	ListAdapterShelf shelfAdapter;
	View shelflayout;

	MeepStoreApplication mApp;
	
	DetailFragment detailFragment;
	PaymentFragment paymentFragment;
	Context mContext;
//	ImageThreadLoader imageLoader;
	String prefix;
	
	@Override
	protected void onStart() {
		super.onStart();
		
		initStoreItemDownloadListener();
		initPackageListener();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		
		if (mApp.getAppCtrl() != null) {
			mApp.getAppCtrl().removePackageListener(mPackageListener);
		}
		if (mApp.getStoreDownloadCtrl() != null) {
			mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);
		}
	}
	
	private static class ActivityHandler extends Handler {
		private final WeakReference<GenericStoreActivity> mActivity;
		
		ActivityHandler(GenericStoreActivity activity) {
			mActivity = new WeakReference<GenericStoreActivity>(activity);
		}
		
		public void handleMessage(Message msg) {
			try {
				GenericStoreActivity activity = mActivity.get();
				switch (msg.what) {
				case 1:
					activity.mUsercoins.setText(activity.mApp.getLoginInfo().coins);
					break;
				case NO_NETWORK:
					activity.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
					if (activity.getFragmentManager() != null)
						activity.popupFragment.show(activity.getFragmentManager(), "dialog");
					break;
				case LOADING:
					activity.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
					if (activity.getFragmentManager() != null)
						activity.popupFragment.show(activity.getFragmentManager(), "dialog");
						//disable left and right button when showing loading screen
					activity.enableIndicatorButtons(false);
					break;
				case TIMEOUT:
					activity.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
					if(activity.getFragmentManager()!=null)
						activity.popupFragment.show(activity.getFragmentManager(), "dialog");
					break;
				case UPDATE_SHELF:
					activity.shelfAdapter.notifyDataSetChanged();
					break;
				default:
					break;
				}
			}
			catch (Exception e) {
				MeepStoreLog.LogMsg("current activity has been closed");
			}
		}
	}

	public Handler handler = new ActivityHandler(this);
	
//	TextView sortCategoryText;
//	TextView sortPriceText;
	
	protected int currentPage;
	
	private int mPriceImageIds [] = {R.drawable.meep_market_sort_priceall_normal, R.drawable.meep_market_sort_free_normal, R.drawable.meep_market_sort_paid_normal, R.drawable.meep_market_sort_others_normal};
	private int mAppCategoryImageIds [] = {R.drawable.meep_market_sort_all_normal, R.drawable.meep_market_sort_book_normal, R.drawable.meep_market_sort_education_normal, R.drawable.meep_market_sort_entertainment_normal, R.drawable.meep_market_sort_music_normal, R.drawable.meep_market_sort_photo_normal, R.drawable.meep_market_sort_tools_normal, R.drawable.meep_market_sort_video_normal};
	private int mGameCategoryImageIds [] = {R.drawable.meep_market_sort_all_normal, R.drawable.meep_market_sort_action_normal, R.drawable.meep_market_sort_brain_normal, R.drawable.meep_market_sort_cards_normal, R.drawable.meep_market_sort_casual_normal, R.drawable.meep_market_sort_education_normal, R.drawable.meep_market_sort_entertainment_normal, R.drawable.meep_market_sort_music_normal, R.drawable.meep_market_sort_racing_normal, R.drawable.meep_market_sort_sports_normal};
	
	private String mPriceTexts [] = {"all", "free", "paid", "googleplay"};
	private String mAppCategoryTexts [] = {"all", "Books", "Education", "Entertainment", "Music%20%26%20audio", "Photography", "Tools", "Video"};
	private String mGameCategoryTexts [] = {"all", "Arcade%20%26%20action", "Brain%20%26%20Puzzle", "Cards", "Casual", "Education", "Entertainment", "Music%20%26%20audio", "Racing", "Sports"};
	
	private boolean isPreviousPage = false;
	public void initUiComponent(int mode) {
		mMode = mode;
		//sort
		sortCategory = (ImageButton) findViewById(R.id.sortCategory);
		sortPrice = (ImageButton) findViewById(R.id.sortPrice);
		sortCategoryImage = (ImageView) findViewById(R.id.sortCategoryImage);
		sortPriceImage = (ImageView) findViewById(R.id.sortPriceImage);
		// common message
		meepstoreicon = (Button) findViewById(R.id.meepstoreicon);
		mUsercoins = (TextView) findViewById(R.id.usercoins);
		mContext = this.getApplicationContext();
		mApp = (MeepStoreApplication) this.getApplicationContext();
//		imageLoader = mApp.getImageLoader();
		if (mApp != null && mApp.getLoginInfo() != null && mApp.getLoginInfo().coins != null) {
			mUsercoins.setText(mApp.getLoginInfo().coins);
		}
		// search
		btnSearch = (ImageButton) findViewById(R.id.searchbtn);
		searchText = (EditText) findViewById(R.id.searchtext);
		// shelf
		gridView = (GridView) findViewById(R.id.gridview);
		listItem = new ArrayList<MeepStoreItem>();
		shelfAdapter = new ListAdapterShelf(this, R.layout.item_item, listItem);
		gridView.setAdapter(shelfAdapter);
		// shelf others
		shelflayout = findViewById(R.id.shelflayout);
		shelfLabel = (TextView) findViewById(R.id.shelfLabel);
		Typeface face = Typeface.createFromAsset(getAssets(), "fonts/BRLNSDB.TTF");
		shelfLabel.setTypeface(face);
		pageLabel = (TextView) findViewById(R.id.pageLabel);
		left = (Button) findViewById(R.id.shelfLeft);
		right = (Button) findViewById(R.id.shelfRight);
		currentPage = 0;

		// listeners
		btnSearch.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				v.setEnabled(false);
				performSearch();
				v.setEnabled(true);

			}
		});
		searchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				if (arg1 == EditorInfo.IME_ACTION_SEARCH) {
					performSearch();
					return true;
				}
				return false;
			}
		});
		left.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				if (currentPage > 0) {
					v.setEnabled(false);
//					currentPage--;
//					isPreviousPage = true;
					int page = currentPage - 1;
					showItem(page);
				}
			}
		});
		right.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				if (totalnumber > currentPage + 1) {
					v.setEnabled(false);
//					currentPage++;
					int page = currentPage + 1;
					showItem(page);
				}
			}
		});
		meepstoreicon.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				onBackPressed();
			}
		});
//        sortCategoryText = (TextView) findViewById(R.id.sortCategoryText);
//        sortPriceText = (TextView) findViewById(R.id.sortPriceText);
	}
	
	
	public void setSelectedCategoryId(int cateogryId) {
		switch(mMode) {
		case APP_MODE:
			mCateogry = mAppCategoryTexts[cateogryId];
			sortCategoryImage.setImageDrawable(getResources().getDrawable(mAppCategoryImageIds[cateogryId]));
			break;
		case GAME_MODE:
			mCateogry = mGameCategoryTexts[cateogryId];
			sortCategoryImage.setImageDrawable(getResources().getDrawable(mGameCategoryImageIds[cateogryId]));
			break;
		}
		
		currentPage = 0;
		showItem(currentPage);
	}
	
	public void setSelectedPriceId(int priceId) {
		mPrice = mPriceTexts[priceId];
		sortPriceImage.setImageDrawable(getResources().getDrawable(mPriceImageIds[priceId]));
		
		currentPage = 0;
		showItem(currentPage);
	}
	
	
	public void reloadAllItems() {
		showItem(currentPage);
	}
	
	public void updateItemStatus(String action, String packagename, String id) {
//		for (MeepStoreItem item : listItem) {
		for (int i = 0; i < shelfAdapter.getCount(); i++) {
			MeepStoreItem item = shelfAdapter.getItem(i);
			if (packagename != null && packagename.equals(item.getPackageName())) {
				MeepStoreLog.LogMsg( "package:" + action);
				MeepStoreLog.LogMsg( "package:" + item.getItemAction());
				item.setItemAction(action);
				handler.sendEmptyMessage(UPDATE_SHELF);
				break;
			}

			if (id != null && id.equals(item.getItemId())) {
				MeepStoreLog.LogMsg( "id:" + action);
				MeepStoreLog.LogMsg( "id:" + item.getItemAction());
				item.setItemAction(action);
				handler.sendEmptyMessage(UPDATE_SHELF);
				break;
			}
		}
	}
	
	
	public void updateUserCoinsListener() {
		handler.sendEmptyMessage(1);
	}
	
	
	public void showItem(final int page) {
		if (type != null && mApp.getUserToken() != null)
			if (mApp.isNetworkAvailable(getApplicationContext())) {
				handler.sendEmptyMessage(LOADING);
				// show items
				mApp.getRestRequest().searchItem(type, page, mApp.getUserToken(), mCateogry, mPrice, ITEM_PER_PAGE);
				mApp.getRestRequest().setSearchItemListener(new SearchItemListener() {

					public void onSearchItemReceived(int code, String msg, ArrayList<MeepStoreItem> itemList, int total) {
						switch (code) {
						case 200:
							isPreviousPage = currentPage > page;
							currentPage = page;
							
							updateShelfUIByResults(total);
							addItemsToShelf(itemList);
							break;
						case 999:
							handler.sendEmptyMessage(TIMEOUT);
							break;
						default:
							break;
						}
						//left.setEnabled(true);
						//right.setEnabled(true);
						
						//enable the left and right button when loading screen is dismissed
						enableIndicatorButtons(true);
						if (popupFragment != null) {
							popupFragment.dismiss();
						}
					}

				});
			} else {
				handler.sendEmptyMessage(NO_NETWORK);
			}

	}
	
	protected void updateShelfUIByResults(int total) {
		totalnumber = total / ITEM_PER_PAGE;
		if (total % ITEM_PER_PAGE != 0) {
			totalnumber++;
		}
		// last page
		if ((currentPage + 1) == totalnumber) {
			right.setVisibility(View.GONE);
			if (totalnumber == 1) {
				left.setVisibility(View.GONE);
			} else {
				left.setVisibility(View.VISIBLE);
			}
		}
		// first page
		else if (currentPage == 0) {
			left.setVisibility(View.GONE);
			if (totalnumber == 0) {
				right.setVisibility(View.GONE);
			} else {
				right.setVisibility(View.VISIBLE);
			}
		}
		// others
		else {
			left.setVisibility(View.VISIBLE);
			right.setVisibility(View.VISIBLE);
		}
		// set PageNumber
		if (totalnumber == 0)
			pagenumber = String.format(getResources().getString(R.string.pagenumber), currentPage, totalnumber);
		else
			pagenumber = String.format(getResources().getString(R.string.pagenumber), currentPage + 1, totalnumber);
		pageLabel.setText(pagenumber);
	}
	
	public void addItemsToShelf(ArrayList<MeepStoreItem> itemList) {
		//update Shelf
//		listItem.clear();
//		listItem.addAll(itemList);
		shelfAdapter.clear();
		shelfAdapter.addAll(itemList);
		shelfAdapter.notifyDataSetChanged();
		
		if(isPreviousPage)
			gridView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_left_alpha_item));
		else
			gridView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right_alpha_item));
		isPreviousPage = false;
	}
	
	public void showDetail(MeepStoreItem item) {
		if (detailFragment == null) {
			detailFragment = DetailFragment.newInstance(item);
			detailFragment.show(getFragmentManager(), "dialog");
		} else {
			if (!detailFragment.isVisible()) {
				DetailFragment.setStoreItem(item);
				detailFragment.show(getFragmentManager(), "dialog");
			}
		}
	}
	
	public void showPayment(MeepStoreItem item) {
		paymentFragment = PaymentFragment.newInstance(item);
		paymentFragment.show(getFragmentManager(), "dialog");
	}

	protected AppInstallationCtrl.PakageListener mPackageListener = null;

	protected void initPackageListener() {
		mPackageListener = new AppInstallationCtrl.PakageListener() {

			public void onpackageAdded(String packageName) {
				// TODO Auto-generated method stub
				updateItemStatus(MeepStoreItem.ACTION_INSTALLED, packageName, null);
			}

			public void onpackageReplaced(String packageName) {
				// TODO Auto-generated method stub
			}

			public void onpackageRemoved(String packageName) {
				// TODO Auto-generated method stub
				updateItemStatus(MeepStoreItem.ACTION_PURCHASED, packageName, null);
			}
		};
		Log.d("AppCtrl", "generic");
		mApp.getAppCtrl().addPackageListener(mPackageListener);
	}
	
	protected StoreItemDownloadCtrl.DownloadListener mDownloadListener = null;

	public void initStoreItemDownloadListener() {
		mDownloadListener = new StoreItemDownloadCtrl.DownloadListener() {

			public void onDownloadProgress(String id, int percentage) {
				if (percentage == 1 || percentage == 11 || percentage == 50)
					updateItemStatus(MeepStoreItem.ACTION_DOWNLOADING, null, id);
			}
			
			
			public void onDownloadCompleted(boolean downloadAborted, DownloadStoreItem item) {
				if (!downloadAborted) {
					if (item.getType().equals(MeepStoreItem.TYPE_EBOOK)) {
						updateItemStatus(MeepStoreItem.ACTION_EBOOK_DOWNLOADED, null, item.getId());
					} else {
						updateItemStatus(MeepStoreItem.ACTION_INSTALLING, null, item.getId());
					}
				} else {
					updateItemStatus(MeepStoreItem.ACTION_PURCHASED, null, item.getId());
				}
			}

			
			public void onNoSpace() {
				//TODO:
			}
			
		};
		mApp.getStoreDownloadCtrl().addDownloadListeners(mDownloadListener);
		
	}
	
	/**
	 * set the category and price button state
	 * @param enable
	 */
	public void enableSortingButtons(boolean enable){
		sortCategory.setEnabled(enable);
		sortPrice.setEnabled(enable);
	}
	
	/**
	 * set the indicator button state
	 * @param enable
	 */
	public void enableIndicatorButtons(boolean enable){
		left.setEnabled(enable);
		right.setEnabled(enable);
	}
	
	/**
	 * set the button state from the list adapter
	 * @param view
	 * @param enable
	 */
	public void enableListItemButton(View view, boolean enable){
		view.setEnabled(enable);
	}
	
	public ListAdapterShelf getShelfListAdapter(){
		return shelfAdapter;
	}

}
