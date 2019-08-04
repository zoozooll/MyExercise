package com.oregonscientific.meep.store2;
import java.net.MalformedURLException;
import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.store2.adapter.ImageThreadLoader.ImageLoadedListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.SearchItemListener;
import com.oregonscientific.meep.store2.custom.scrollview.FlowLayout;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.object.MeepStoreItem;

public class ActivityMedia extends GenericStoreActivity {

	private final int ITEM_PER_PAGE =8;
	MeepStoreApplication mApp;
	View shelflayout;
	ImageButton sortCategory;
	ImageButton sortPrice;
	FlowLayout flowlayout;
	
	Button left;
	Button right;
	Button meepstoreicon;
	private int currentPage;
	
	DialogFragment newFragment;
	DialogFragment detailFragment;
	DialogFragment paymentFragment;
	Handler handler;
	
//	private String prefix;
	
	public final static String TAG = "STORE_MEDIA";
	
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_media);
		currentPage = 0;
		handler = new Handler() {

			
			public void handleMessage(Message msg) {
				if (msg.what == 0) {
				}
				super.handleMessage(msg);
			}

		};
		mApp = (MeepStoreApplication) this.getApplicationContext();
//		imageLoader = mApp.getImageLoader();
		mContext = this.getApplicationContext();

		shelflayout = findViewById(R.id.shelflayout);

		sortCategory = (ImageButton) findViewById(R.id.sortCategory);
		sortPrice = (ImageButton) findViewById(R.id.sortPrice);

		left = (Button) findViewById(R.id.shelfLeft);
		right = (Button) findViewById(R.id.shelfRight);
		meepstoreicon = (Button) findViewById(R.id.meepstoreicon);

		flowlayout = (FlowLayout) findViewById(R.id.flowLayoutMedia);
		showItem(currentPage);
		initListeners();

	}
    
    
	public void showItem(int page) {
		Log.d(TAG, "loading");
		// show items
		mApp.getRestRequest().searchItem(MeepStoreItem.TYPE_MEDIA, page, mApp.getUserToken(), null, null, ITEM_PER_PAGE);
		mApp.getRestRequest().setSearchItemListener(new SearchItemListener() {

			
			public void onSearchItemReceived(int code, String msg, ArrayList<MeepStoreItem> itemList, int total) {

				Log.d(TAG, "size:" + itemList.size());
				for (MeepStoreItem item : itemList) {
					addFlowChildren(item);
				}
			}

		});
		// start animation
	}
    
    
	public void initListeners() {
		sortCategory.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newFragment = CategoryDialogFragment
						.newInstance(CategoryDialogFragment.CATEGORY_GAME_DIALOG_ID);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});
		sortPrice.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				// TODO Auto-generated method stub
				newFragment = CategoryDialogFragment.newInstance(CategoryDialogFragment.PRICE_DIALOG_ID);
				newFragment.show(getFragmentManager(), "dialog");
			}
		});

		left.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				v.setEnabled(false);
				if (currentPage > 0) {
					flowlayout.removeAllViews();
					currentPage--;
					showItem(currentPage);
				}
				v.setEnabled(true);
			}
		});
		right.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				v.setEnabled(false);
				flowlayout.removeAllViews();
				currentPage++;
				showItem(currentPage);
				v.setEnabled(true);
			}
		});

		meepstoreicon.setOnClickListener(new View.OnClickListener() {

			
			public void onClick(View v) {
				onBackPressed();
			}
		});

	}

	
	protected void onStart() {
		// TODO Auto-generated method stub
		Animation slidein = AnimationUtils.loadAnimation(this, R.anim.slide_in_right);
		shelflayout.startAnimation(slidein);
		super.onStart();
	}
    
	public void addFlowChildren(final MeepStoreItem item) {

//		LinearLayout view = new LinearLayout(this);
//		view.setOrientation(LinearLayout.VERTICAL);
		View view = ((LayoutInflater)this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_item, null);
		
		TextView textName;
		Button button;
		final ImageView image;
		final ImageView imageblock;
		
		try {
			textName = (TextView) view.findViewById(R.id.textName);
			image = (ImageView) view.findViewById(R.id.image);
			imageblock = (ImageView) view.findViewById(R.id.imageblock);
			if (item.getItemAction().equals(MeepStoreItem.ACTION_NORMAL)) {
				button = (Button) view.findViewById(R.id.BCoins);
				button.setVisibility(View.VISIBLE);
				button.setText(Integer.toString(item.getPrice()));

			} else if (item.getItemAction().equals(MeepStoreItem.ACTION_FREE)) {
				button = (Button) view.findViewById(R.id.BFree);
				button.setVisibility(View.VISIBLE);
			} else if (item.getItemAction().equals(MeepStoreItem.ACTION_GET_IT)) {
				button = (Button) view.findViewById(R.id.BGet);
				button.setVisibility(View.VISIBLE);
			} else {
				button = (Button) view.findViewById(R.id.BBlank);
				button.setVisibility(View.VISIBLE);
			}

			Log.d(TAG, item.getItemAction());

		} catch (ClassCastException e) {
			Log.e(TAG, "Wrong resourceId", e);
			throw e;
		}
		
		textName.setText(item.getName());
		mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + item.getIconUrl(), image);
		
//		Bitmap cachedImage = null;
//		try {
//			prefix = mApp.getLoginInfo().url_prefix;
//			cachedImage = imageLoader.loadImage(prefix + item.getIconUrl(), new ImageLoadedListener() {
//				public void imageLoaded(Bitmap imageBitmap) {
//					imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block);
//					image.setImageBitmap(imageBitmap);
//					Log.d(TAG, "imageloaded");
//					// notifyDataSetChanged();
//				}
//			});
//		} catch (MalformedURLException e) {
//			Log.e("test", "Bad remote image URL: " + e.toString());
//		}
//		
//		if (cachedImage != null) {
//			image.setImageBitmap(cachedImage);
//		} else {
//			imageblock.setBackgroundResource(R.drawable.meep_market_shelf_block_dummy);
//		}
		
		view.setOnClickListener((new View.OnClickListener() {
			
			
			public void onClick(View pV) {
				// show detail
				detailFragment = DetailFragment.newInstance(item);
				detailFragment.show(getFragmentManager(), "dialog");
			}
		}));
		//set Button Listener
		if(item.getItemAction().equals(MeepStoreItem.ACTION_INSTALLED))
		{
			button.setOnClickListener((new View.OnClickListener() {
    			
    			
    			public void onClick(View pV) {
    				//TODO:uninstall
    				detailFragment = PaymentFragment.newInstance(item);
					detailFragment.show(getFragmentManager(), "dialog");
    			}
    		}));
		}
		else if(item.getItemAction().equals(MeepStoreItem.ACTION_PURCHASED))
		{
			button.setOnClickListener((new View.OnClickListener() {
    			
    			
    			public void onClick(View pV) {
    				//TODO:install
    				detailFragment = PaymentFragment.newInstance(item);
					detailFragment.show(getFragmentManager(), "dialog");
    			}
    		}));
		}
		else
		{
			button.setOnClickListener((new View.OnClickListener() {
				
				
				public void onClick(View pV) {
					//show detail
					paymentFragment = PaymentFragment.newInstance(item);
					paymentFragment.show(getFragmentManager(), "dialog");
				}
			}));
		}
		
		
		int w = FlowLayout.LayoutParams.WRAP_CONTENT;
		int h = FlowLayout.LayoutParams.WRAP_CONTENT;
		//add view and set padding
        flowlayout.addView(view, new FlowLayout.LayoutParams(w, h));
		
	}
    
	
    
    
  
    
}