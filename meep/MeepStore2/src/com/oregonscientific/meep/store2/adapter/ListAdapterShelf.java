package com.oregonscientific.meep.store2.adapter;


import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.oregonscientific.meep.store2.GenericStoreActivity;
import com.oregonscientific.meep.store2.R;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.object.MeepStoreItem;


public class ListAdapterShelf extends ArrayAdapter<MeepStoreItem> {
	private final static String TAG = "ListMyApp";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private Context mContext;
	MeepStoreApplication mApp;
	private String prefix;
//	private ImageThreadLoader imageLoader;
	private ShowDetailOrPayment detailOrPayment;
	private View selectedItemButton;
	
//	private final ImageDownloader imageDownloader = new ImageDownloader();
	
	public ListAdapterShelf(Context context, int resourceId, ArrayList<MeepStoreItem> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		mContext = context;
		mApp = (MeepStoreApplication) mContext.getApplicationContext();
//		imageLoader = mApp.getImageLoader();
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		detailOrPayment = ((GenericStoreActivity)context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getItemsInView(getItem(position), convertView);
	}
	
	
	public View getItemsInView(MeepStoreItem item, View convertView) {
		View view = convertView;
		boolean isEbook = false;
		final MeepStoreItem meepStoreItem = item;
		
		if (view == null) {
			if (meepStoreItem.getItemType().equals(MeepStoreItem.TYPE_EBOOK)) {
				isEbook = true;
				view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_ebook, null);
			} else {
				view = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_item, null);
			}
		}
		TextView textName;
		final ImageView image;
		final ImageView imageblock;

		try {
			textName = (TextView) view.findViewById(R.id.textName);
			image = (ImageView) view.findViewById(R.id.image);
//			imageblock = (ImageView) view.findViewById(R.id.imageblock);
			
			// make the buttons invisible
			RelativeLayout buttons = (RelativeLayout) view.findViewById(R.id.buttons);
			if (buttons != null) {
				for (int i = 0; i < buttons.getChildCount(); i++) {
					buttons.getChildAt(i).setVisibility(View.INVISIBLE);
				}
			}
			setButtonByItemAction(meepStoreItem, view);
		} catch (ClassCastException e) {
			Log.e("STORE", "Wrong resourceId", e);
			throw e;
		}

		textName.setText(meepStoreItem.getName());

		if (!isEbook) {
			// image
//			Bitmap cachedImage = null;
//			try {
//				prefix = mApp.getLoginInfo().url_prefix;
//				cachedImage = imageLoader.loadImage(prefix + item.getIconUrl(), new ImageLoadedListener() {
//					public void imageLoaded(Bitmap imageBitmap) {
//						imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block);
//						image.setImageBitmap(imageBitmap);
//						// notifyDataSetChanged();
//						image.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.fade_in_item));
//					}
//				});
//			} catch (MalformedURLException e) {
//				Log.e("test", "Bad remote image URL: " + e.toString());
//			}
//
//			if (cachedImage != null) {
//				imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block);
//				image.setImageBitmap(cachedImage);
//			} else {
//				imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block_dummy);
//			}
//			imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + meepStoreItem.getIconUrl(), 75, 75, image);
		} else {
			// content is ebook
//			imageblock.setBackgroundResource(R.drawable.meep_market_shelf_book_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + meepStoreItem.getIconUrl(), 75, 75, image);
		}
		
		setBadge(meepStoreItem, view);

		view.setOnClickListener((new View.OnClickListener() {

			@Override
			public void onClick(View pV) {
				detailOrPayment.showDetail(meepStoreItem);
			}
		}));
		
		return view;
	}
	
	public void setButtonByItemAction(final MeepStoreItem item, View view) {
		final Button button;
		//NOMARL
		if (item.getItemAction().equals(MeepStoreItem.ACTION_NORMAL)) {
			button = (Button) view.findViewById(R.id.BCoins);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(Integer.toString(item.getPrice()));
			button.setOnClickListener((new View.OnClickListener() {
				
				@Override
				public void onClick(View pV) {
					selectedItemButton = button;
					button.setEnabled(false);
					detailOrPayment.showPayment(item);
				}
			}));
		}
		//FREE
		else if (item.getItemAction().equals(MeepStoreItem.ACTION_FREE)) {
			button = (Button) view.findViewById(R.id.BFree);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setOnClickListener((new View.OnClickListener() {
				
				@Override
				public void onClick(View pV) {
					selectedItemButton = button;
					button.setEnabled(false);
					detailOrPayment.showPayment(item);
				}
			}));
		} 
		//GETIT
		else if (item.getItemAction().equals(MeepStoreItem.ACTION_GET_IT)) {
			button = (Button) view.findViewById(R.id.BGet);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setOnClickListener((new View.OnClickListener() {
				
				@Override
				public void onClick(View pV) {
					selectedItemButton = button;
					button.setEnabled(false);
					detailOrPayment.showPayment(item);
				}
			}));
		}
		// INSTALLED
		else if (item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLED)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.uninstall);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					button.setEnabled(false);
					detailOrPayment.showDetail(item);
				}
			});
		}
		// PURCHASED
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_PURCHASED)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.install);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					button.setEnabled(false);
					selectedItemButton = button;
					detailOrPayment.showDetail(item);
				}
			});
		}
		// PENDING
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_PENDING)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.waiting);
			button.setOnClickListener(null);
		}
		// PENDING TO DOWNLOAD
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.queuing);
			button.setOnClickListener(null);
		}
		// DOWNLOADING
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_DOWNLOADING)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.downloading);
			button.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					button.setEnabled(false);
					selectedItemButton = button;
					detailOrPayment.showDetail(item);
				}
			});
		}
		// BLOCKED
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_BLOCKED)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.blocked);
			button.setOnClickListener((new View.OnClickListener() {

				@Override
				public void onClick(View pV) {
					detailOrPayment.showDetail(item);
				}
			}));
		}
		// COMING SOON
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_COMING_SOON)) {
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.comingsoon);
			button.setOnClickListener(null);
		}
		// INSTALLING
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLING)) {
			Log.d(TAG, "Installing Name: " + item.getName());
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.installing);
			button.setOnClickListener(null);
		}
		// EBOOK DOWNLOADED
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_EBOOK_DOWNLOADED)) {
			Log.d(TAG, "Downloaded Name: " + item.getName());
			button = (Button) view.findViewById(R.id.BBlank);
			button.setEnabled(true);
			button.setVisibility(View.VISIBLE);
			button.setText(R.string.uninstall2);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					button.setEnabled(false);
					selectedItemButton = button;
					detailOrPayment.showDetail(item);
				}
			});
		}
	}
	
	public void setBadge(MeepStoreItem item, View view) {
//		Log.d("test","badge:"+item.getBadge());
		//BADGE
		ImageView badge = (ImageView) view.findViewById(R.id.badge);
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
	
	/**
	 * enable button
	 */
	public void enableItemButton() {
		if (selectedItemButton != null) {
			selectedItemButton.setEnabled(true);
			selectedItemButton = null;
		}
	}

	public interface ShowDetailOrPayment {
		
		public void showDetail(MeepStoreItem item);

		public void showPayment(MeepStoreItem item);
	}
}
