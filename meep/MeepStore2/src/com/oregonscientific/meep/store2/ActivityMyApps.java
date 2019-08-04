package com.oregonscientific.meep.store2;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ListView;

import com.oregonscientific.meep.store2.adapter.ListAdapterMyApp;
import com.oregonscientific.meep.store2.adapter.ListAdapterMyApp.ShowDetail;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.EbookCtrl;
import com.oregonscientific.meep.store2.ctrl.RestRequest.GetPurchasedItemListener;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.MyAppItems;

public class ActivityMyApps extends Activity implements ShowDetail{

	private MeepStoreApplication mApp;
	private MeepStoreService mService;
	Timer myTimer = new Timer();
	private ListView mListView = null;
	private ArrayList<MeepStoreItem> mStoreItemList;
	private ListAdapterMyApp mListAdaptor;
	private Context mContext;
	private Button meepstoreicon;
	
	private static final int UPDATE_LIST = 0;
	private static final int NO_NETWORK = 3;
	private static final int LOADING = 4;
	private static final int TIMEOUT = 5;
	private static final int NOT_ENOUGH_SPACE = 10;

	DialogFragment detailFragment;
	DialogFragment popupFragment;
	
	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_myapps);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mContext = this.getApplicationContext();
		mApp = (MeepStoreApplication) this.getApplicationContext();
		mStoreItemList = new ArrayList<MeepStoreItem>();
		meepstoreicon = (Button) findViewById(R.id.meepstoreicon);

		mListView = (ListView) findViewById(R.id.listViewMyApp);
		mListAdaptor = new ListAdapterMyApp(this, R.layout.item_myapps, mStoreItemList);
		View header = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.section, null, false);
		mListView.addHeaderView(header);
		mListView.setAdapter(mListAdaptor);
//		getPurchaseItems();
		initListeners();
		handler = new ActivityHandler(this);
//		initStoreItemDownloadListener();
//		initPackageListener();
	}

	public void initListeners() {
		meepstoreicon.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				onBackPressed();
			}
		});
	}
	
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		getPurchaseItems();
		initStoreItemDownloadListener();
		initPackageListener();
	}
	
//	@Override
//	protected void onStop() {
//		super.onStop();
//		
//		if (mApp.getAppCtrl() != null) {
//			mApp.getAppCtrl().removePackageListener(mPackageListener);
//		}
//		if (mApp.getStoreDownloadCtrl() != null) {
//			mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);
//		}
//	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
//		mApp.getAppCtrl().removePackageListener(mPackageListener);
//		mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);

		if (mApp.getAppCtrl() != null) {
			mApp.getAppCtrl().removePackageListener(mPackageListener);
		}
		if (mApp.getStoreDownloadCtrl() != null) {
			mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);
		}
	}
	
	private static class ActivityHandler extends Handler {
		private final WeakReference<ActivityMyApps> mActivity;
		
		ActivityHandler(ActivityMyApps activity) {
			mActivity = new WeakReference<ActivityMyApps>(activity);
		}
		
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			
			ActivityMyApps activity = mActivity.get();
			switch (msg.what) {
			case ActivityMyApps.UPDATE_LIST:
				activity.mListAdaptor.notifyDataSetChanged();
				break;
			case ActivityMyApps.NO_NETWORK:
				activity.showNoNetwork();
				break;
			case ActivityMyApps.LOADING:
				activity.loading();
				break;
			case ActivityMyApps.TIMEOUT:
				activity.timeout();
				break;
			case ActivityMyApps.NOT_ENOUGH_SPACE:
				activity.notEnoughSpace();
				break;

			}
		}
	}

//	private final Handler handler = new Handler() {
//		public void handleMessage(Message msg) {
//			super.handleMessage(msg);
//			switch (msg.what) {
//			case UPDATE_LIST:
//				mListAdaptor.notifyDataSetChanged();
//				break;
//			case NO_NETWORK:
//				showNoNetwork();
//				break;
//			case LOADING:
//				loading();
//				break;
//			case TIMEOUT:
//				timeout();
//				break;
//			case NOT_ENOUGH_SPACE:
//				notEnoughSpace();
//				break;
//
//			}
//		}
//	};
	
	private void showNoNetwork() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void loading() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void timeout() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
		popupFragment.show(getFragmentManager(), "dialog");
	}

	private void notEnoughSpace() {
		popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NOT_ENOUGH_SPACE);
		popupFragment.show(getFragmentManager(), "dialog");
	}
	
	AppInstallationCtrl.PakageListener mPackageListener = null;

	public void initPackageListener() {
		mPackageListener = new AppInstallationCtrl.PakageListener() {

			@Override
			public void onpackageReplaced(String packageName) {
				// TODO Auto-generated method stub
				MeepStoreLog.logcatMessage("APPSTATUS", "replace app " + packageName);
			}

			@Override
			public void onpackageRemoved(String packageName) {
				// TODO Auto-generated method stub
				try {
					MeepStoreLog.logcatMessage("APPSTATUS", "remove app " + packageName);
					updateItemStatus(MeepStoreItem.ACTION_PURCHASED, packageName, null, -1);
				} catch (Exception e) {
					// TODO: handle exception
				}

			}

			@Override
			public void onpackageAdded(String packageName) {
				// TODO Auto-generated method stub
				try {
					MeepStoreLog.logcatMessage("APPSTATUS", "add app " + packageName);
					updateItemStatus(MeepStoreItem.ACTION_INSTALLED, packageName, null, -1);
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		};
		MeepStoreLog.logcatMessage("AppCtrl", "myapps");
		mApp.getAppCtrl().addPackageListener(mPackageListener);
	}

	protected StoreItemDownloadCtrl.DownloadListener mDownloadListener = null;

	public void initStoreItemDownloadListener() {
		mDownloadListener = new StoreItemDownloadCtrl.DownloadListener() {

			@Override
			public void onDownloadProgress(String name, int percentage) {
				updateDownloadStatus(name, percentage);
			}

			@Override
			public void onDownloadCompleted(boolean downloadAborted, DownloadStoreItem item) {
				if (!downloadAborted)
					updateDownloadCompleted(item);
				else
					updateItemStatus(MeepStoreItem.ACTION_PURCHASED, null, item.getId(), -1);
			}

			@Override
			public void onNoSpace() {
				// TODO:not enough space
				handler.sendEmptyMessage(NOT_ENOUGH_SPACE);

			}

		};
		mApp.getStoreDownloadCtrl().addDownloadListeners(mDownloadListener);
	}

	private void updateDownloadStatus(String id, int progress) {
		updateItemStatus(MeepStoreItem.ACTION_DOWNLOADING, null, id, progress);
	}
    
	private void updateDownloadCompleted(DownloadStoreItem i) {
		String id = i.getId();
		if (i.getType().equals(MeepStoreItem.TYPE_EBOOK)) {
			updateItemStatus(MeepStoreItem.ACTION_EBOOK_DOWNLOADED, null, id, 100);
		} else {
			updateItemStatus(MeepStoreItem.ACTION_INSTALLING, null, id, 100);
		}
	}
    
	private void getPurchaseItems() {
		if (mApp.isNetworkAvailable(this.getApplicationContext())) {
			handler.sendEmptyMessage(LOADING);
			mApp.getRestRequest().getPurchaseItem(mApp.getUserToken());
			mApp.getRestRequest().setGetPurchasedItemListener(new GetPurchasedItemListener() {

				@Override
				public void onGetPurchasedItemListener(MyAppItems myAppItem) {
					try {
						if (popupFragment != null) {
							popupFragment.dismiss();
						}
						ArrayList<MeepStoreItem> purchasedItems = myAppItem.getPurchasedItems();
						ArrayList<MeepStoreItem> preloadedItems = myAppItem.getPreloadedItems();
						if (purchasedItems != null) {
							mListAdaptor.clear();
//							mStoreItemList.addAll(purchasedItems);
							mListAdaptor.addAll(purchasedItems);

							MeepStoreItem section = new MeepStoreItem();
							section.setName("this_is_section");
//							mStoreItemList.add(section);
							mListAdaptor.add(section);
							MeepStoreItem all = new MeepStoreItem();
							all.setName("this_is_all");
							all.setRecovery(true);
//							mStoreItemList.add(all);
							mListAdaptor.add(all);

//							mStoreItemList.addAll(preloadedItems);
							mListAdaptor.addAll(preloadedItems);
							mListAdaptor.notifyDataSetChanged();
						} else {
							handler.sendEmptyMessage(TIMEOUT);
						}
					} catch (NullPointerException e) {

					} catch (Exception e) {
						// TODO: handle exception
					}

				}
			
			});
		} else {
			handler.sendEmptyMessage(NO_NETWORK);
		}
	}

	@Override
	public void showDetail(MeepStoreItem item) {
		try {
			if (detailFragment == null) {
				detailFragment = DetailFragment.newInstance(item);
				detailFragment.show(getFragmentManager(), "dialog");
			} else {
				if (!detailFragment.isVisible()) {
					DetailFragment.setStoreItem(item);
					detailFragment.show(getFragmentManager(), "dialog");
				}
			}
		} catch (Exception e) {

		}
	}

	@Override
	public void loadingScreen() {
		try {
			popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
			popupFragment.show(getFragmentManager(), "dialog");
		} catch (Exception e) {

		}

	}

	@Override
	public void noNetworkScreen() {
		try {
			popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
			popupFragment.show(getFragmentManager(), "dialog");
		} catch (Exception e) {

		}

	}

	@Override
	public void timeOutScreen() {
		try {
			popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
			popupFragment.show(getFragmentManager(), "dialog");
		} catch (Exception e) {

		}

	}

	@Override
	public void dismissScreen() {
		if (popupFragment != null)
			popupFragment.dismiss();

	}
	
	
	public void fixItemActions(ArrayList<MeepStoreItem> items)
	{
		for(MeepStoreItem item:items)
		{
			if(item.getItemType().equals(MeepStoreItem.TYPE_EBOOK))
			{
//				if(mApp.isEbookInstalled(item.getItemId()))
				if(EbookCtrl.isEbookInstalled(item.getName()))
				{
					item.setItemAction(MeepStoreItem.ACTION_EBOOK_DOWNLOADED);
				}
				else
				{
					item.setItemAction(MeepStoreItem.ACTION_PURCHASED);
				}
			}
			else
			{
				if(mApp.isPackageInstalled(mContext, item.getPackageName()))
				{
					item.setItemAction(MeepStoreItem.ACTION_INSTALLED);
				}
				else
				{
					item.setItemAction(MeepStoreItem.ACTION_PURCHASED);
					
				}
			}
		}
	}
	
	public void updateItemStatus(String action, String packageName, String id, int progress) {
		if (mListAdaptor != null) {
			if (packageName != null) {
				for (int i = 0; i < mListAdaptor.getCount(); i++) {
					MeepStoreItem item = mListAdaptor.getItem(i);
					if (item != null && item.getPackageName() != null && packageName != null) {
						if (item.getPackageName().equals(packageName)) {
							item.setItemAction(action);
							handler.sendEmptyMessage(UPDATE_LIST);
							break;
						}
					}
				}
			} else if (id != null) {
				for (int i = 0; i < mListAdaptor.getCount(); i++) {
					MeepStoreItem item = mListAdaptor.getItem(i);
					if (item != null && item.getItemId() != null && id != null) {
						if (item.getItemId().equals(id)) {
							item.setItemAction(action);
							if (progress != -1)
								item.setProgress(progress);
							handler.sendEmptyMessage(UPDATE_LIST);
							break;
						}
					}

				}
			}
		}
	}
	
}
