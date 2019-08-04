package com.oregonscientific.meep.store2.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oregonscientific.meep.store2.ActivityMyApps;
import com.oregonscientific.meep.store2.R;
import com.oregonscientific.meep.store2.ctrl.AppInstallationCtrl;
import com.oregonscientific.meep.store2.ctrl.EbookCtrl;
import com.oregonscientific.meep.store2.ctrl.RestRequest.PurchaseItemListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.RecoveryContentListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.object.ContentRecoveryFeedback;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.PurchaseFeedback;


public class ListAdapterMyApp extends ArrayAdapter<MeepStoreItem> {
	private final static String TAG = "ListMyApp";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private Context mContext;
	MeepStoreApplication mApp;
	private ShowDetail showdetail;
	private String prefix;
//	private ImageThreadLoader imageLoader;
	private Button buttonApp;
	private ProgressBar progrss;
	private Animation fade_in;
	
//	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	public ListAdapterMyApp(Context context, int resourceId, ArrayList<MeepStoreItem> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		mContext = context;
		mApp = (MeepStoreApplication) mContext.getApplicationContext();
//		imageLoader = mApp.getImageLoader();
		showdetail =((ActivityMyApps)mContext);
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		fade_in = AnimationUtils.loadAnimation(mContext, R.anim.fade_in_item);
		
	}
	
	public void refresh() {
		notifyDataSetChanged();
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		final MeepStoreItem item = getItem(position);
		View view = convertView;
		String tag = view == null ? null : (String) view.getTag();
		
		// final String id = item.getItemId();
		if (item.getName().equals("this_is_section")) {
			if (tag != null && tag.equals(item.getName())) {
				
			} else {
				view = inflater.inflate(R.layout.section2, null);
				view.setTag(item.getName());
			}
		} else if (item.getName().equals("this_is_all")) {
			if (tag != null && tag.equals(item.getName())) {
				
			} else {
				view = inflater.inflate(R.layout.item_myapps_all, null);
				view.setTag(item.getName());
			}	
			buttonApp = (Button) view.findViewById(R.id.btnRecover);
			showButtonRecoverAll();
		} else {
			if (tag != null && tag.equals(Integer.toString(resourceId))) {
				
			} else {
				view = inflater.inflate(resourceId, null);
				view.setTag(Integer.toString(resourceId));
			}
				
//	    	Log.d(TAG, "getview - " + item.getName());
			TextView txtName = (TextView) view.findViewById(R.id.textMyAppItemName);
			TextView txtInfo = (TextView) view.findViewById(R.id.textMyAppItemInfo);
			TextView txtSize = (TextView) view.findViewById(R.id.textMyAppItemSize);
			progrss = (ProgressBar) view.findViewById(R.id.progressBarDownload);
			buttonApp = (Button) view.findViewById(R.id.btnRecover);
			buttonApp.setTag(item);

			ImageView image;
			View icon;
			if (!item.getItemType().equals(MeepStoreItem.TYPE_EBOOK)) {
				view.findViewById(R.id.icon2).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.imagebook).setVisibility(View.INVISIBLE);
				
				icon = view.findViewById(R.id.icon);
				image = (ImageView) view.findViewById(R.id.image);
			}
			// content is ebook
			else {
				view.findViewById(R.id.icon).setVisibility(View.INVISIBLE);
				view.findViewById(R.id.image).setVisibility(View.INVISIBLE);
				
				icon = view.findViewById(R.id.icon2);
				image = (ImageView) view.findViewById(R.id.imagebook);
			}
			
			loadItemImage(item, image, icon);
	    	
			// is dowloaded
			if (!item.isRecovery()) {
				if (item.getItemAction() != null
						&& item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLED)) {
					buttonApp.setText(R.string.uninstall);
					buttonApp.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// uninstall
							MeepStoreItem item = (MeepStoreItem) v.getTag();
							if (item.getItemType().equals(MeepStoreItem.TYPE_APP)
									|| item.getItemType().equals(MeepStoreItem.TYPE_GAME)) {
								AppInstallationCtrl.uninstallApp(v.getContext(), item.getPackageName());
							}
						}
					});
					progrss.setVisibility(View.GONE);
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_DOWNLOADING)) {
					// TODO:btnRecover.setText(R.string.downloading);
					showButtonDownloading();
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD)) {
					showButtonQueueing(item, R.string.install);
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_EBOOK_DOWNLOADED)) {
					buttonApp.setText(R.string.uninstall2);
					buttonApp.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							EbookCtrl.removeEbook(item);
							item.setItemAction(MeepStoreItem.ACTION_PURCHASED);
							notifyDataSetChanged();
						}
					});
					progrss.setVisibility(View.GONE);
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLING)) {
					showButtonUnclickable(R.string.installing);
					progrss.setVisibility(View.GONE);
				} else {
					showButtonInstallOrRecover(R.string.install);
					progrss.setVisibility(View.GONE);
				}
			}
			// is recovery
			else {
				if (item.getItemAction() != null
						&& item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLED)) {
					buttonApp.setText(R.string.uninstall);
					progrss.setVisibility(View.GONE);
					buttonApp.setOnClickListener(new View.OnClickListener() {

						@Override
						public void onClick(View v) {
							// uninstall
							MeepStoreItem item = (MeepStoreItem) v.getTag();
							if (item.getItemType().equals(MeepStoreItem.TYPE_APP)
									|| item.getItemType().equals(MeepStoreItem.TYPE_GAME)) {
								AppInstallationCtrl.uninstallApp(mContext, item.getPackageName());
							}
						}
					});
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_DOWNLOADING)) {
					showButtonDownloading();
				} else if (item.getItemAction().equals(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD)) {
					showButtonQueueing(item, R.string.recover);
				} else {
					showButtonInstallOrRecover(R.string.recover);
					progrss.setVisibility(View.GONE);
				}
			}

			txtName.setText(item.getName());
			txtInfo.setText(item.getDeveloper());
			txtSize.setText(item.getSize() + "MB");
			if (item.getIcon() != null) {
				image.setImageBitmap(item.getIcon());
				Log.d(TAG, "icon null");
			}

			image.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View arg0) {
					// TODO:
					showdetail.showDetail(item);
				}
			});
	    	
//	    if(isPackageInstalled(view.getContext(), item.getName())){
//	    	btnRecover.setText("Uninstall");
//	    }else{
//	    	btnRecover.setText("Install");
//	    }
	    	
		}
		return view;
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
	
	public interface ShowDetail {
		public void showDetail(MeepStoreItem item);

		public void loadingScreen();

		public void noNetworkScreen();

		public void timeOutScreen();

		public void dismissScreen();
	}
	
	public void showButtonInstallOrRecover(final int textResId) {
		buttonApp.setText(textResId);
		buttonApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				final MeepStoreItem item = (MeepStoreItem) v.getTag();
				if (mApp.isNetworkAvailable(mContext)) {
					// load
					showdetail.loadingScreen();
					final View view = v;
					view.setEnabled(false);
					mApp.getRestRequest().purchaseStoreItem(item.getItemId(), mApp.getUserToken());
					mApp.getRestRequest().setPurchaseItemListener(new PurchaseItemListener() {
						
						@Override
						public void onPurchaseReceived(PurchaseFeedback feedback) {
							showdetail.dismissScreen();
							if (feedback != null) {
								switch (feedback.getCode()) {
								case 200:
									if (feedback.getUrl() != null) {
										DownloadStoreItem downloadItem = new DownloadStoreItem(
												feedback.getItem_id(),
												feedback.getName(), 
												feedback.getType(), 
												feedback.getImage(), 
												feedback.getUrl(), 
												"", 
												feedback.getPackage_name());
										mApp.getStoreDownloadCtrl().addStoreDownloadItem(downloadItem);
									}
									if (mApp.hasItemDownloading()) {
										if (mApp.isItemDownloading(item.getItemId())) {
											item.setItemAction(MeepStoreItem.ACTION_DOWNLOADING);
											// //TODO:btnRecover.setText(R.string.downloading);
											showButtonDownloading();
										} else {
											item.setItemAction(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD);
											showButtonQueueing(item, textResId);
										}
									} else {
										item.setItemAction(MeepStoreItem.ACTION_DOWNLOADING);
										// //TODO:btnRecover.setText(R.string.downloading);
										showButtonDownloading();
									}
									notifyDataSetChanged();
									break;
								case 999:
									showdetail.timeOutScreen();
									break;
								default:
									break;
								}
							} else {
								showdetail.timeOutScreen();
							}
							view.setEnabled(true);
						}
					});
				} else {
					// no network
					showdetail.dismissScreen();
				}

			}
		});
	}
	
	public void showButtonQueueing(final MeepStoreItem item, final int installOrRecover) {
		buttonApp.setText(R.string.queuing);
		buttonApp.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// remove item from DB
				if (mApp.removeWaitingItem(item.getItemId())) {
					item.setItemAction(MeepStoreItem.ACTION_PURCHASED);
					notifyDataSetChanged();
				}
			}
		});
		buttonApp.setVisibility(View.VISIBLE);
		progrss.setVisibility(View.GONE);
	}
	public void showButtonWaiting(final MeepStoreItem item, final int installOrRecover) {
		buttonApp.setText(R.string.waiting);
		buttonApp.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// remove item from DB
				if (mApp.removeWaitingItem(item.getItemId())) {
					item.setItemAction(MeepStoreItem.ACTION_PURCHASED);
					notifyDataSetChanged();
				}
			}
		});
		buttonApp.setVisibility(View.VISIBLE);
		progrss.setVisibility(View.GONE);
	}
	
	public void showButtonRecoverAll() {
		buttonApp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mApp.isNetworkAvailable(mContext)) {
					// load
					showdetail.loadingScreen();
					final View view = v;
					view.setEnabled(false);
					mApp.getRestRequest().contentRecovery(mApp.getUserToken());
					mApp.getRestRequest().setRecoveryContentListener(new RecoveryContentListener() {
						
						@Override
						public void onContentRecovery(ContentRecoveryFeedback feedback) {
							showdetail.dismissScreen();
							if (feedback != null) {
								switch (feedback.getCode()) {
								case 200:

									break;
								case 999:
									showdetail.timeOutScreen();
									break;
								default:
									break;
								}
							}
							view.setEnabled(true);
						}
					});
				} else {
					showdetail.noNetworkScreen();
				}
			}
		});
	}
	
	public void loadItemImage(MeepStoreItem item, final ImageView image, View icon) {
		// image
//		Bitmap cachedImage = null;
//		try {
//			prefix = mApp.getLoginInfo().url_prefix;
//			cachedImage = imageLoader.loadImage(prefix + item.getIconUrl(), new ImageLoadedListener() {
//				public void imageLoaded(Bitmap imageBitmap) {
//					image.setImageBitmap(imageBitmap);
//					image.startAnimation(fade_in);
//				}
//			});
//		} catch (MalformedURLException e) {
//			Log.e("test", "Bad remote image URL: " + e.toString());
//		}
//		if (cachedImage != null) {
//			image.setImageBitmap(cachedImage);
//		}
		mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + item.getIconUrl(), 75, 75, image);
		image.setVisibility(View.VISIBLE);
		icon.setVisibility(View.VISIBLE);
	}

	public void showButtonUnclickable(int textResId) {
		buttonApp.setText(textResId);
		buttonApp.setOnClickListener(null);
		buttonApp.setVisibility(View.VISIBLE);
	}

	public void showButtonDownloading() {
		showButtonUnclickable(R.string.installing);
		progrss.setVisibility(View.VISIBLE);
		if (buttonApp != null) {
			MeepStoreItem meepStoreItem = (MeepStoreItem) buttonApp.getTag();
			if (meepStoreItem != null) {
				progrss.setProgress(meepStoreItem.getProgress());
			}
		}
	}
	
}
