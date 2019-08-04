package com.oregonscientific.meep.store2;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oregonscientific.meep.store2.PaymentFragment.UpdateStatusListener;
import com.oregonscientific.meep.store2.adapter.ListAdapterShelf;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.EbookCtrl;
import com.oregonscientific.meep.store2.ctrl.RestRequest.PurchaseItemListener;
import com.oregonscientific.meep.store2.ctrl.StoreItemDownloadCtrl;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.PurchaseFeedback;

public class DetailFragment extends DialogFragment {

	public static MeepStoreItem getStoreItem() {
		return storeItem;
	}

	public static void setStoreItem(MeepStoreItem item) {
		storeItem = item;
	}

	public static String getPackageNameOfItem()
	{
		String pkgName = null;
		if(storeItem!=null)
		{
			pkgName = storeItem.getPackageName();
		}
		return pkgName;
	}

	private static MeepStoreItem storeItem;
	private String prefix;
	// ListAdapterScreenshot adapter;
	// HorizontalListView listview;
	HorizontalScrollView hs;
	LinearLayout imageList;
	Drawable drawable;
	PaymentFragment paymentFragment;
	PopUpDialogFragment popupFragment;
	ScreenShotFragment shotFragment;
	// ImageThreadLoader imageLoader;
	// ImageThreadLoader imageShotLoader;
	MeepStoreApplication mApp;
	ProgressBar progressBar;
	Button button;
	ArrayList<String> screenShots = new ArrayList<String>();
	AppInstallationCtrl.PakageListener mPackageListener = null;

	private final int NO_NETWORK = 3;
	private final int LOADING = 4;
	private final int TIMEOUT = 5;
	private final int SUCCESS_MESSAGE = 6;
	private final int INSTALLING = 7;
	private final int WAITING = 8;
	private final int DOWNLOADING = 9;
	private final int NOT_ENOUGH_SPACE = 10;
	private final int EBOOK_DOWNLOAD_SUCCESS = 11;
	private final int AWAITING_APPROVAL = 12;
	private final int INSTALL = 14;

	public interface ReloadItem {
		public void reloadAllItems();

		public void updateItemStatus(String action, String packetname, String id);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
		// prefix = this.getResources().getString(R.string.prefix);
		mApp = (MeepStoreApplication) this.getActivity().getApplicationContext();
		// imageLoader = mApp.getImageLoader();
		// imageShotLoader = mApp.getImageShotLoader();

	}

	@Override
	public void onStart() {
		super.onStart();

		initStoreItemDownloadListener();
		initPackageListener();
	}

	// @Override
	// public void onStop() {
	// super.onStop();
	//
	// if (mApp.getAppCtrl() != null) {
	// mApp.getAppCtrl().removePackageListener(mPackageListener);
	// }
	// if (mApp.getStoreDownloadCtrl() != null) {
	// mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);
	// }
	// }

	public static DetailFragment newInstance(MeepStoreItem storeItem) {
		DetailFragment.storeItem = storeItem;
		DetailFragment myDialogFragment = new DetailFragment();
		return myDialogFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.layout_details, container, false);
		hs = (HorizontalScrollView) v.findViewById(R.id.horizontalScrollView);
		imageList = (LinearLayout) v.findViewById(R.id.imagesLinear);

		TextView name = (TextView) v.findViewById(R.id.textName);
		TextView info = (TextView) v.findViewById(R.id.textInfo);
		TextView size = (TextView) v.findViewById(R.id.textSize);
		TextView description = (TextView) v.findViewById(R.id.description);
		button = (Button) v.findViewById(R.id.button);
		progressBar = (ProgressBar) v.findViewById(R.id.progressBarDownload);
		ImageView recommend = (ImageView) v.findViewById(R.id.recommend);
		final ImageView image;
		final ImageView imageblock;
		View icon;
		boolean isEbook = false;
		if (storeItem.getItemType().equals(MeepStoreItem.TYPE_EBOOK)) {
			isEbook = true;
		}
		// content isnot ebook
		if (!isEbook) {
			icon = v.findViewById(R.id.icon);
			image = (ImageView) v.findViewById(R.id.image);
			imageblock = (ImageView) v.findViewById(R.id.imageblock);

			imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix
					+ storeItem.getIconUrl(), 75, 75, image);
			// Bitmap cachedImage = null;
			// try {
			// prefix = mApp.getLoginInfo().url_prefix;
			// cachedImage =
			// imageLoader.loadImage(prefix+storeItem.getIconUrl(), new
			// ImageLoadedListener() {
			// public void imageLoaded(Bitmap imageBitmap) {
			// imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
			// image.setImageBitmap(imageBitmap);
			// //notifyDataSetChanged();
			// }
			// });
			// } catch (MalformedURLException e) {
			// Log.e("APPSTATUS", "Bad remote image URL: " + e.toString());
			// }
			//
			// if( cachedImage != null ) {
			// imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
			// image.setImageBitmap(cachedImage);
			// }else{
			// imageblock.setBackgroundResource(R.drawable.meep_market_description_block_dummy);
			// }
		} else {
			// content is ebook
			icon = v.findViewById(R.id.icon2);
			image = (ImageView) v.findViewById(R.id.imagebook);
			imageblock = (ImageView) v.findViewById(R.id.imageblockbook);

			imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix
					+ storeItem.getIconUrl(), 75, 75, image);
			// Bitmap cachedImage = null;
			// try {
			// prefix = mApp.getLoginInfo().url_prefix;
			// cachedImage = imageLoader.loadImage(prefix +
			// storeItem.getIconUrl(),
			// new ImageLoadedListener() {
			// public void imageLoaded(Bitmap imageBitmap) {
			// imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
			// image.setImageBitmap(imageBitmap);
			// // notifyDataSetChanged();
			// }
			// });
			// } catch (MalformedURLException e) {
			// Log.e("APPSTATUS", "Bad remote image URL: " + e.toString());
			// }
			//
			// if (cachedImage != null) {
			// imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
			// image.setImageBitmap(cachedImage);
			// } else {
			// imageblock.setBackgroundResource(R.drawable.meep_market_book_block_dummy);
			// }
		}
		icon.setVisibility(View.VISIBLE);

		// BADGE
		setBadge(storeItem, v, isEbook);
		// common
		name.setText(storeItem.getName());
		info.setText(storeItem.getDeveloper());
		size.setText(storeItem.getSize() + "MB");
		description.setText(storeItem.getDescription());

		// Action
		button.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View pV) {
				// show detail
				paymentFragment = PaymentFragment.newInstance(storeItem);
				paymentFragment.show(getFragmentManager(), "dialog");
				paymentFragment.setUpdateStatusListener(new UpdateStatusListener() {

					@Override
					public void awaiting() {
						handler.sendEmptyMessage(AWAITING_APPROVAL);
					}

					@Override
					public void waiting() {
						handler.sendEmptyMessage(WAITING);
					}
				});
			}
		}));

		if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_NORMAL)) {
			button.setBackgroundResource(R.drawable.btn_item_description_coins);
			button.setText(Integer.toString(storeItem.getPrice()));
			button.setPadding(100, 0, 0, 0);

		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_FREE)) {
			button.setBackgroundResource(R.drawable.btn_item_description_free);
			button.setText(R.string.free);
			button.setPadding(30, 0, 0, 0);
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_GET_IT)) {
			button.setBackgroundResource(R.drawable.btn_item_description_get);
			button.setText(R.string.getit);
			button.setPadding(30, 0, 0, 0);

		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_INSTALLED)) {
			showButtonUninstall();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_PURCHASED)) {
			if (storeItem.isRecovery()) {
				button.setText(R.string.recover);
			}
			showButtonInstall();

		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_DOWNLOADING)) {
			showProgressBar();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD)) {
			showButtonQueuing();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_PENDING)) {
			showButtonAwaitingApproval();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_INSTALLING)) {
			showButtonInstalling();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_EBOOK_DOWNLOADED)) {
			showButtonSuccess();
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_BLOCKED)) {
			showTextBlocked();
		} else {
			button.setVisibility(View.GONE);
		}

		// Recommend
		if (storeItem.getRecommends() != null) {
			if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_RECOMMENDS)) {
				recommend.setImageResource(R.drawable.meep_store_gold);
			} else if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_FRIENDLY)) {
				recommend.setImageResource(R.drawable.meep_store_silver);
			} else if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_LIKES)) {
				recommend.setImageResource(R.drawable.meep_store_broze);
			} else {
				recommend.setVisibility(View.INVISIBLE);
			}
		} else {
			recommend.setVisibility(View.INVISIBLE);
		}

		// screenshot
		if (storeItem.getScreenShotUrls() == null
				&& storeItem.getScreenShotUrls().length == 0) {

		} else {
			screenShots.clear();
			for (String url : storeItem.getScreenShotUrls()) {
				screenShots.add(url);
				addFlowChildren(url);
			}
		}

		return v;
	}

	public void popupScreenshot(int index) {
		try {
			if (shotFragment == null) {
				shotFragment = ScreenShotFragment.newInstance(ScreenShotFragment.SCREENSHOT, index, screenShots);
				shotFragment.show(getFragmentManager(), "dialog");
			} else {
				ScreenShotFragment.setPictureIndex(index);
				shotFragment.loadImage();
				shotFragment.show(getFragmentManager(), "dialog");
			}
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
	}

	public void addFlowChildren(final String url) {

		View view = ((LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_screenshot, null);
		final ImageView image;
		image = (ImageView) view.findViewById(R.id.image);
		mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + url, 200, 200, image);
		// mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix +
		// url, image);

		// Bitmap cachedImage = null;
		// try {
		// prefix = mApp.getLoginInfo().url_prefix;
		// cachedImage = imageShotLoader.loadImage(prefix + url, new
		// ImageLoadedListener() {
		// public void imageLoaded(final Bitmap imageBitmap) {
		// image.setImageBitmap(imageBitmap);
		// MeepStoreLog.logcatMessage("APPSTATUS", "screenshot loaded" + url);
		// notifyDataSetChanged();
		// }
		// });
		// } catch (MalformedURLException e) {
		// Log.e("APPSTATUS", "Bad remote image URL: " + e.toString());
		// }
		// if (cachedImage != null) {
		// image.setImageBitmap(cachedImage);
		// } else {
		// }

		image.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View pV) {
				popupScreenshot(screenShots.lastIndexOf(url));
			}

		}));

		int w = 196;
		int h = 110;
		// add view and set padding
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(w, h);
		lp.setMargins(0, 0, 20, 0);
		imageList.addView(view, lp);

	}

	public void setBadge(MeepStoreItem item, View view, boolean isEbook) {
		ImageView badge;
		// BADGE
		if (!isEbook) {
			badge = (ImageView) view.findViewById(R.id.badge);
		} else {
			badge = (ImageView) view.findViewById(R.id.badgebook);
		}
		if (item.getBadge().equals(MeepStoreItem.BADGE_ACCESSORY)) {
			badge.setImageResource(R.drawable.meep_accessary);
		} else if (item.getBadge().equals(MeepStoreItem.BADGE_BESTSELLER)) {
			badge.setImageResource(R.drawable.meep_bestseller);

		} else if (item.getBadge().equals(MeepStoreItem.BADGE_HOTITEM)) {
			badge.setImageResource(R.drawable.meep_hotitem);

		} else if (item.getBadge().equals(MeepStoreItem.BADGE_SALE)) {
			badge.setImageResource(R.drawable.meep_sale);

		} else if (item.getBadge().equals(MeepStoreItem.BADGE_SDCARD)) {
			badge.setImageResource(R.drawable.meep_sdcard);

		} else {
			badge.setVisibility(View.GONE);
		}
	}

	public Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			try {
				switch (msg.what) {
				case EBOOK_DOWNLOAD_SUCCESS:
					showButtonSuccess();
					break;
				case NO_NETWORK:
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case LOADING:
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case TIMEOUT:
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case SUCCESS_MESSAGE:
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.COMMON_MESSAGE);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case INSTALLING:
					showButtonInstalling();
					break;
				case WAITING:
					showButtonQueuing();
					break;
				case DOWNLOADING:
					showProgressBar();
					break;
				case AWAITING_APPROVAL:
					showButtonAwaitingApproval();
					break;
				case NOT_ENOUGH_SPACE:
					popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NOT_ENOUGH_SPACE);
					if (getFragmentManager() != null)
						popupFragment.show(getFragmentManager(), "dialog");
					break;
				case INSTALL:
					showButtonInstall();
					break;
				default:
					break;
				}
			} catch (Exception e) {
				MeepStoreLog.logcatMessage("test", "parent/current fragment has been closed");
			}
		}

	};

	public Handler getHandler() {
		return handler;
	}

	public void showProgressBar() {
		progressBar.setVisibility(View.VISIBLE);
		button.setVisibility(View.GONE);
		updateShelf(MeepStoreItem.ACTION_DOWNLOADING, storeItem.getPackageName(), storeItem.getItemId());
	}

	public void showUnclickableButton() {
		progressBar.setVisibility(View.GONE);
		button.setVisibility(View.VISIBLE);
		button.setPadding(0, 0, 0, 0);
		button.setClickable(false);
		button.setBackgroundColor(Color.TRANSPARENT);
	}

	public void showCommonButton() {
		progressBar.setVisibility(View.GONE);
		button.setVisibility(View.VISIBLE);
		button.setPadding(0, 0, 0, 0);
		button.setBackgroundResource(R.drawable.btn_item_description_blank);
	}

	public void showButtonInstalling() {
		showUnclickableButton();
		button.setText(R.string.installing);
		updateShelf(MeepStoreItem.ACTION_INSTALLING, storeItem.getPackageName(), storeItem.getItemId());
	}

	public void showButtonQueuing() {
		showUnclickableButton();
		button.setText(R.string.queuing_for_downloading);
		updateShelf(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD, storeItem.getPackageName(), storeItem.getItemId());
	}

	public void showButtonAwaitingApproval() {
		showUnclickableButton();
		button.setText(R.string.awaiting);
		updateShelf(MeepStoreItem.ACTION_PENDING, storeItem.getPackageName(), storeItem.getItemId());
	}

	public void showButtonSuccess() {
		// showUnclickableButton();
		// button.setText(R.string.ebook_success);
		showCommonButton();
		button.setText(R.string.uninstall2);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				EbookCtrl.removeEbook(storeItem);
				storeItem.setItemAction(MeepStoreItem.ACTION_PURCHASED);
				showButtonInstall();
				updateShelf(MeepStoreItem.ACTION_PURCHASED, storeItem.getPackageName(), storeItem.getItemId());
			}
		});
		updateShelf(MeepStoreItem.ACTION_EBOOK_DOWNLOADED, storeItem.getPackageName(), storeItem.getItemId());
	}

	public void updateShelf(String action, String packageName, String id) {
		if (getActivity() instanceof GenericStoreActivity) {
			try {
				((GenericStoreActivity) getActivity()).updateItemStatus(action, packageName, id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void showButtonInstall() {
		if (button != null) {
			showCommonButton();
			button.setText(R.string.install);
			button.setOnClickListener((new View.OnClickListener() {

				@Override
				public void onClick(View pV) {
					if (mApp.isNetworkAvailable(getActivity())) {
						// loading
						handler.sendEmptyMessage(LOADING);
						pV.setEnabled(false);
						mApp.getRestRequest().purchaseStoreItem(storeItem.getItemId(), mApp.getUserToken());
						mApp.getRestRequest().setPurchaseItemListener(new PurchaseItemListener() {

							@Override
							public void onPurchaseReceived(
									PurchaseFeedback feedback) {
								if (popupFragment != null) {
									popupFragment.dismiss();
								}
								if (feedback != null) {
									switch (feedback.getCode()) {
									case 200:
										if (feedback.getUrl() != null) {
											DownloadStoreItem downloadItem = new DownloadStoreItem(feedback.getItem_id(), feedback.getName(), feedback.getType(), feedback.getImage(), feedback.getUrl(), "", feedback.getPackage_name());
											mApp.getStoreDownloadCtrl().addStoreDownloadItem(downloadItem);
										}
										if (mApp.isItemDownloading(storeItem.getItemId())) {
											storeItem.setItemAction(MeepStoreItem.ACTION_DOWNLOADING);
											showProgressBar();
										} else {
											storeItem.setItemAction(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD);
											showButtonQueuing();
										}
										break;
									case 999:
										handler.sendEmptyMessage(TIMEOUT);
										break;
									default:
										break;
									}
									button.setEnabled(true);
								} else {
									handler.sendEmptyMessage(TIMEOUT);
								}
								button.setEnabled(true);
							}
						});
					} else {
						// no network
						handler.sendEmptyMessage(NO_NETWORK);
					}
				}
			}));
		}

	}

	public void showButtonUninstall() {
		showCommonButton();
		button.setText(R.string.uninstall);
		button.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View pV) {
				AppInstallationCtrl.uninstallApp(getActivity(), storeItem.getPackageName());
			}
		}));
	}
	public void showTextBlocked() {
		showUnclickableButton();
		button.setTextSize(20);
		button.setGravity(Gravity.LEFT);
		button.setText(R.string.not_allow_to_downlaod);
	}

	StoreItemDownloadCtrl.DownloadListener mDownloadListener = null;

	private void initStoreItemDownloadListener() {
		mDownloadListener = new StoreItemDownloadCtrl.DownloadListener() {

			@Override
			public void onDownloadProgress(String id, int percentage) {
				updateDownloadStatus(id, percentage);
			}

			@Override
			public void onDownloadCompleted(boolean downloadAborted,
					DownloadStoreItem item) {
				if (!downloadAborted)
					updateDownloadCompleted(item);
				else {
					handler.sendEmptyMessage(INSTALL);
				}
			}

			@Override
			public void onNoSpace() {
				handler.sendEmptyMessage(NOT_ENOUGH_SPACE);

			}
		};
		mApp.getStoreDownloadCtrl().addDownloadListeners(mDownloadListener);
	}

	private void updateDownloadStatus(String id, int progress) {
		MeepStoreLog.logcatMessage("APPSTATUS", "id:" + id + ",progress:" + progress);
		if (storeItem.getItemId().equals(id)) {
			// show progressbar
			handler.sendEmptyMessage(DOWNLOADING);
			if (!storeItem.getItemAction().equals(MeepStoreItem.ACTION_DOWNLOADING)) {
				storeItem.setItemAction(MeepStoreItem.ACTION_DOWNLOADING);
				// updateShelf(MeepStoreItem.ACTION_DOWNLOADING, null, id
				// ,progress);
			}
			storeItem.setProgress(progress);
			if (progressBar != null) {
				progressBar.setProgress(progress);
			}
		}
		// else
		// {
		//
		// if(progress ==1)
		// {
		// updateShelf(MeepStoreItem.ACTION_DOWNLOADING, null, id,progress);
		// }
		// }
	}

	private void updateDownloadCompleted(DownloadStoreItem item) {
		String id = item.getId();
		String type = item.getType();
		MeepStoreLog.logcatMessage("APPSTATUS", "complete id:" + id);
		if (storeItem.getItemId().equals(id)) {
			if (progressBar != null) {
				progressBar.setProgress(100);
			}
			handler.sendEmptyMessage(INSTALLING);
			if (type != null) {
				if (type.equals(MeepStoreItem.TYPE_EBOOK)) {
					storeItem.setItemAction(MeepStoreItem.ACTION_EBOOK_DOWNLOADED);
					handler.sendEmptyMessage(EBOOK_DOWNLOAD_SUCCESS);
					// updateShelf(MeepStoreItem.ACTION_EBOOK_DOWNLOADED, null,
					// id,-1);
				} else {
					storeItem.setItemAction(MeepStoreItem.ACTION_INSTALLING);
					// updateShelf(MeepStoreItem.ACTION_INSTALLING, null,
					// id,100);
				}
			}
		}
	}

	public void initPackageListener() {
		mPackageListener = new AppInstallationCtrl.PakageListener() {

			@Override
			public void onpackageReplaced(String packageName) {
				// AppInstallationCtrl.removeApk(packageName);
				// TODO Auto-generated method stub
				MeepStoreLog.logcatMessage("storedownload", "package replace 2");
				MeepStoreLog.logcatMessage("APPSTATUS", "replace app " + packageName);
			}

			@Override
			public void onpackageRemoved(String packageName) {
				// TODO Auto-generated method stub
				MeepStoreLog.logcatMessage("storedownload", "package remove 2");
				MeepStoreLog.logcatMessage("APPSTATUS", "remove app " + packageName);
				if (storeItem != null && packageName != null) {
					if (packageName.equals(storeItem.getPackageName())) {
						storeItem.setItemAction(MeepStoreItem.ACTION_PURCHASED);
						if (storeItem.isRecovery() && button != null) {
							button.setText(R.string.recover);
						}
						showButtonInstall();
					}
				}
				// updateShelf(MeepStoreItem.ACTION_PURCHASED,packageName,null,-1);
			}

			@Override
			public void onpackageAdded(String packageName) {
				// AppInstallationCtrl.removeApk(packageName);
				MeepStoreLog.logcatMessage("storedownload", "package add 2");
				// TODO Auto-generated method stub
				MeepStoreLog.logcatMessage("APPSTATUS", "add app " + packageName);
				if (storeItem != null && packageName != null) {
					if (packageName.equals(storeItem.getPackageName())) {
						storeItem.setItemAction(MeepStoreItem.ACTION_INSTALLED);
						showButtonUninstall();
					}
				}
				// updateShelf(MeepStoreItem.ACTION_INSTALLED,packageName,null,-1);
			}
		};
		MeepStoreLog.logcatMessage("AppCtrl", "detail");
		mApp.getAppCtrl().addPackageListener(mPackageListener);
	}

	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		try {
			if (getActivity() instanceof GenericStoreActivity) {
				ListAdapterShelf listAdapterShelf = ((GenericStoreActivity) getActivity()).getShelfListAdapter();
				listAdapterShelf.enableItemButton();
			}
			super.onDismiss(dialog);
		} catch (Exception e) {
			MeepStoreLog.LogMsg("current activity has been closed");
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		MeepStoreLog.logcatMessage("AppCtrl", "remove detail");
		if (mApp.getAppCtrl() != null) {
			mApp.getAppCtrl().removePackageListener(mPackageListener);
		}
		if (mApp.getStoreDownloadCtrl() != null) {
			mApp.getStoreDownloadCtrl().removeDownloadListeners(mDownloadListener);
		}
	}

}