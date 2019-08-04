package com.oregonscientific.meep.store2;


import java.lang.ref.WeakReference;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.store2.PopUpDialogFragment.UpdateUserCoinsListener;
import com.oregonscientific.meep.store2.adapter.ListAdapterShelf;
import com.oregonscientific.meep.store2.ctrl.RestRequest.PurchaseItemListener;
import com.oregonscientific.meep.store2.ctrl.RestRequest.RequestCoinsListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.MeepStoreItem;
import com.oregonscientific.meep.store2.object.PurchaseFeedback;
import com.oregonscientific.meep.store2.object.StoreFeedback;



public class PaymentFragment extends DialogFragment{
	private static final int REQUEST_COINS = 15;
	private static final int START_DOWNLOAD = 16;
	private static MeepStoreItem storeItem;

	public static MeepStoreItem getStoreItem() {
		return storeItem;
	}

	public static void setStoreItem(MeepStoreItem storeItem) {
		PaymentFragment.storeItem = storeItem;
	}

	private MeepStoreApplication mApp;
	private String prefix;
	static PaymentFragment myFragment = null;
	UpdateUserCoinsListener updateCoins;

	public interface UpdateStatusListener {
		public void awaiting();
		public void waiting();
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mApp = (MeepStoreApplication) this.getActivity().getApplicationContext();
//        imageLoader = mApp.getImageLoader();
        
    }
    
	public static PaymentFragment newInstance(MeepStoreItem storeItem) {
		PaymentFragment.storeItem = storeItem;
		PaymentFragment myDialogFragment = new PaymentFragment();
		myFragment = myDialogFragment;
		return myDialogFragment;
	}
  
//    private ImageThreadLoader imageLoader;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

		View v = inflater.inflate(R.layout.layout_payment, container, false);
		TextView name = (TextView) v.findViewById(R.id.textName);
		TextView info = (TextView) v.findViewById(R.id.textInfo);
		TextView size = (TextView) v.findViewById(R.id.textSize);
		TextView coins = (TextView) v.findViewById(R.id.textCoins);
		TextView hint = (TextView) v.findViewById(R.id.textHint);
		ImageView recommend = (ImageView) v.findViewById(R.id.recommend);
		View coinEq = v.findViewById(R.id.coinEq);

		final ImageView image;
		final ImageView imageblock;
		View icon;
		boolean isEbook = false;
		if (storeItem.getItemType().equals(MeepStoreItem.TYPE_EBOOK)) {
			isEbook = true;
		}
	    
	    //content isnot ebook
		if (!isEbook) {
			icon = v.findViewById(R.id.icon);
			image = (ImageView) v.findViewById(R.id.image);
			imageblock = (ImageView) v.findViewById(R.id.imageblock);

			imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + storeItem.getIconUrl(), 75, 75, image);
		    //image
//		    Bitmap cachedImage = null;
//			try {
//				prefix = mApp.getLoginInfo().url_prefix;
//		    	cachedImage = imageLoader.loadImage(prefix+storeItem.getIconUrl(), new ImageLoadedListener() {	
//		    		public void imageLoaded(Bitmap imageBitmap) {
//		    			imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
//		    			image.setImageBitmap(imageBitmap);
//		    			//notifyDataSetChanged();                
//		    			}
//		    		});
//		    } catch (MalformedURLException e) {
//		    	Log.e("test", "Bad remote image URL: " + e.toString());
//		    }
//			
//			if( cachedImage != null ) {
//				imageblock.setBackgroundResource(R.drawable.meep_market_description_block);
//		    	image.setImageBitmap(cachedImage);
//		    }else{
//		    	imageblock.setBackgroundResource(R.drawable.meep_market_description_block_dummy);
//		    }
	  }
	  //content is ebook
	  else
	  {
			icon = v.findViewById(R.id.icon2);
			image = (ImageView) v.findViewById(R.id.imagebook);
			imageblock = (ImageView) v.findViewById(R.id.imageblockbook);

			imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
			mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + storeItem.getIconUrl(), 75, 75, image);
		    //image
//		    Bitmap cachedImage = null;
//			try {
//				prefix = mApp.getLoginInfo().url_prefix;
//		    	cachedImage = imageLoader.loadImage(prefix+storeItem.getIconUrl(), new ImageLoadedListener() {	
//		    		public void imageLoaded(Bitmap imageBitmap) {
//		    			imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
//		    			image.setImageBitmap(imageBitmap);
//		    			//notifyDataSetChanged();                
//		    			}
//		    		});
//		    } catch (MalformedURLException e) {
//		    	Log.e("test", "Bad remote image URL: " + e.toString());
//		    }
//			
//			if( cachedImage != null ) {
//				imageblock.setBackgroundResource(R.drawable.meep_market_book_block);
//		    	image.setImageBitmap(cachedImage);
//		    }else{
//		    	imageblock.setBackgroundResource(R.drawable.meep_market_book_block_dummy);
//		    }
		}
		icon.setVisibility(View.VISIBLE);
	    
		// common
		name.setText(storeItem.getName());
		info.setText(storeItem.getDeveloper());
		size.setText(storeItem.getSize() + "MB");

		// Action
		if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_NORMAL)) {
			hint.setText(R.string.payment_confirm);
			coins.setText(Integer.toString(storeItem.getPrice()));
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_FREE)) {
			hint.setText(R.string.payment_confirm);
			coins.setText(Integer.toString(storeItem.getPrice()));
		} else if (storeItem.getItemAction().equals(MeepStoreItem.ACTION_GET_IT)) {
			hint.setText(R.string.payment_google);
			coins.setVisibility(View.INVISIBLE);
			coinEq.setVisibility(View.INVISIBLE);
		} else {
			coins.setVisibility(View.INVISIBLE);
		}
	    
		// Recommend
		if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_RECOMMENDS)) {
			recommend.setImageResource(R.drawable.meep_store_gold);
		} else if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_FRIENDLY)) {
			recommend.setImageResource(R.drawable.meep_store_silver);
		} else if (storeItem.getRecommends().equals(MeepStoreItem.RECOMMEND_LIKES)) {
			recommend.setImageResource(R.drawable.meep_store_broze);
		} else {
			recommend.setVisibility(View.INVISIBLE);
		}
	    
		//back button
		Button back = (Button) v.findViewById(R.id.back);
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				if (getActivity() instanceof GenericStoreActivity) {
					ListAdapterShelf listAdapterShelf = ((GenericStoreActivity) getActivity()).getShelfListAdapter();
					listAdapterShelf.enableItemButton();
				}
				PaymentFragment.this.dismiss();

			}
		});
		
		
		//NOT GOOGLEPLAY
		if(!storeItem.getItemAction().equals(MeepStoreItem.ACTION_GET_IT))
		{
			int mycoins = Integer.parseInt(mApp.getLoginInfo().coins);
			
			TextView now = (TextView) v.findViewById(R.id.now);
			TextView price = (TextView) v.findViewById(R.id.price);
			TextView remain = (TextView) v.findViewById(R.id.remain);
			now.setText(mApp.getLoginInfo().coins);
			price.setText(Integer.toString(-storeItem.getPrice()));
			remain.setText(Integer.toString(mycoins-storeItem.getPrice()));
			
			//NOT ENOUGH
			if(mycoins<storeItem.getPrice())
			{
				hint.setText(R.string.payment_notenough);
				final Button request = (Button) v.findViewById(R.id.sendrequest);
				request.setVisibility(View.VISIBLE);
				hint.setTextColor(Color.RED);
				price.setTextColor(Color.RED);
				remain.setTextColor(Color.RED);
				request.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(mApp.isNetworkAvailable(getActivity()))
						{
							handler.sendEmptyMessage(4);
							arg0.setEnabled(false);
							mApp.getRestRequest().requestCoins(mApp.getUserToken(),storeItem.getPrice());
							mApp.getRestRequest().setRequestCoinsListener(new RequestCoinsListener() {
								
								@Override
								public void onRequestCoinsReceived(StoreFeedback feedback) {
									if(popupFragment!=null)
									{
										popupFragment.dismiss();
									}
									request.setEnabled(true);
									if (feedback != null) {
										if (feedback.getCode() == 200) {
											handler.sendEmptyMessage(REQUEST_COINS);
										} else if (feedback.getCode() == 999) {
											handler.sendEmptyMessage(5);
										}
									}
									else
									{
										handler.sendEmptyMessage(5);
									}
								}
							});
						}
						else
						{
							handler.sendEmptyMessage(3);
						}
					}
				});
				
					
				
			}
			//ENOUGH
			else 
			{
				final Button confirm = (Button) v.findViewById(R.id.confirm);
				confirm.setVisibility(View.VISIBLE);
				confirm.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(mApp.isNetworkAvailable(getActivity()))
						{
							handler.sendEmptyMessage(4);
							arg0.setEnabled(false);
							mApp.getRestRequest().purchaseStoreItem(storeItem.getItemId(), mApp.getUserToken());
							mApp.getRestRequest().setPurchaseItemListener(new PurchaseItemListener() {
								
								@Override
								public void onPurchaseReceived(PurchaseFeedback feedback) {
									confirm.setEnabled(true);
									if(popupFragment!=null && !popupFragment.isRemoving())
										
											popupFragment.dismiss();
									if (feedback != null) {
										if (feedback.getCode() >= 200 && feedback.getCode() < 400) {
											if(feedback.getCode()==200)
											{
												DownloadStoreItem downloadItem = new DownloadStoreItem(
														feedback.getItem_id(),
														feedback.getName(),
														feedback.getType(), 
														feedback.getImage(),
														feedback.getUrl(), 
														"",
														feedback.getPackage_name());
												mApp.getStoreDownloadCtrl().addStoreDownloadItem(downloadItem);
												handler.sendEmptyMessage(START_DOWNLOAD);
											}
											else if(feedback.getCode()==202)
											{
												handler.sendEmptyMessage(7);
											}
											if(DetailFragment.getStoreItem()!=null&&updateStatusListener!=null)
											{
												if(feedback.getCode()==200)
													updateStatusListener.waiting();
												if(feedback.getCode()==202)
													updateStatusListener.awaiting();
											}
											
											mApp.getLoginInfo().coins = Integer.toString(feedback.getCoins());
											if(getActivity() instanceof GenericStoreActivity)
											{
												updateCoins = ((GenericStoreActivity)getActivity());
												updateCoins.updateUserCoinsListener();
//												reloadShelf();
												if(feedback.getCode()==200)
												{
													if(mApp.hasItemDownloading())
													{
														updateShelf(MeepStoreItem.ACTION_PENDING_TO_DOWNLOAD, null, storeItem.getItemId());
													}
													else
													{
														updateShelf(MeepStoreItem.ACTION_DOWNLOADING, null, storeItem.getItemId());
													}
												}
												else if(feedback.getCode()==202)
													updateShelf(MeepStoreItem.ACTION_PENDING, null, storeItem.getItemId());
											}
											else
											{
												updateCoins = ((MainActivity)getActivity());
												updateCoins.updateUserCoinsListener();
												//TODO:update shelf
											}
											
										} else if (feedback.getCode() == 999) {
											handler.sendEmptyMessage(5);
										}
									}
									else
									{
										handler.sendEmptyMessage(5);
									}
								}
							});
						}
						else
						{
							handler.sendEmptyMessage(3);
						}
					}
				});
				
			}
		}
		//GOOGLEPLAY
		else
		{
			final Button request = (Button) v.findViewById(R.id.sendrequest);
			request.setVisibility(View.VISIBLE);
			request.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(final View arg0) {
					if(mApp.isNetworkAvailable(getActivity()))
					{
						handler.sendEmptyMessage(4);
						arg0.setEnabled(false);
						mApp.getRestRequest().purchaseStoreItem(storeItem.getItemId(), mApp.getUserToken());
						mApp.getRestRequest().setPurchaseItemListener(new PurchaseItemListener() {
							
							
							public void onPurchaseReceived(PurchaseFeedback feedback) {
								arg0.setEnabled(true);
								if(popupFragment!=null && !popupFragment.isRemoving())
										popupFragment.dismiss();
								if (feedback != null) {
									if (feedback.getCode() >= 200 && feedback.getCode() < 400) {
										if(feedback.getUrl()!=null)
										{
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
										handler.sendEmptyMessage(6);
									} else if (feedback.getCode() == 999) {
										handler.sendEmptyMessage(5);
									}
								}
								else
								{
									handler.sendEmptyMessage(5);
								}
							}
						});
					}
					else
					{
						handler.sendEmptyMessage(3);
					}
				}
			});
			
		}
		
		setBadge(storeItem, v, isEbook);
        return v;
    }
    
    public void setBadge(MeepStoreItem item,View view,boolean isEbook)
	{
//		MeepStoreLog.LogMsg("badge:"+item.getBadge());
		ImageView badge;
		//BADGE
		if(!isEbook)
		{
			 badge = (ImageView) view.findViewById(R.id.badge);
		}
		else
		{
			badge = (ImageView) view.findViewById(R.id.badgebook);
		}
		if(item.getBadge().equals(MeepStoreItem.BADGE_ACCESSORY))
		{
			badge.setImageResource(R.drawable.meep_accessary);
		}
		else if (item.getBadge().equals(MeepStoreItem.BADGE_BESTSELLER))
		{
			badge.setImageResource(R.drawable.meep_bestseller);
			
		}
		else if (item.getBadge().equals(MeepStoreItem.BADGE_HOTITEM))
		{
			badge.setImageResource(R.drawable.meep_hotitem);
			
		}
		else if (item.getBadge().equals(MeepStoreItem.BADGE_SALE))
		{
			badge.setImageResource(R.drawable.meep_sale);
			
		}
		else if (item.getBadge().equals(MeepStoreItem.BADGE_SDCARD))
		{
			badge.setImageResource(R.drawable.meep_sdcard);
			
		}
		else
		{
			badge.setVisibility(View.GONE);
		}
	}
    
    
	DialogFragment popupFragment;
	
	private Handler handler = new FragmentHandler(this);
    
	private static class FragmentHandler extends Handler {
		private final WeakReference<PaymentFragment> mFragment;

		FragmentHandler(PaymentFragment fragment) {
			mFragment = new WeakReference<PaymentFragment>(fragment);
		}

		public void handleMessage(Message msg) {
			PaymentFragment fragment = mFragment.get();
			switch (msg.what) {
			case 3:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case 4:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case 5:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case 6:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.COMMON_MESSAGE, myFragment);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case 7:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.WAITING_APPROVAL, myFragment);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case REQUEST_COINS:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.REQUEST_COINS, myFragment);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			case START_DOWNLOAD:
				fragment.popupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.START_DOWNLOAD, myFragment);
				if (fragment.getFragmentManager() != null)
					fragment.popupFragment.show(fragment.getFragmentManager(), "dialog");
				break;
			default:
				break;
			}
		}
	}
   
//	public void reloadShelf() {
//		if(getActivity() instanceof GenericStoreActivity)
//		{
//			((GenericStoreActivity)getActivity()).reloadAllItems();
//		}
//			
//	}
	@Override
	public void onDismiss(DialogInterface dialog) {
		// TODO Auto-generated method stub
		try {
			if (getActivity() instanceof GenericStoreActivity) {
				ListAdapterShelf listAdapterShelf = ((GenericStoreActivity)getActivity()).getShelfListAdapter();
				listAdapterShelf.enableItemButton();
			}
			super.onDismiss(dialog);
		} catch (Exception e) {
			MeepStoreLog.LogMsg( "parent activity has been closed");
		}
	}
	
	UpdateStatusListener updateStatusListener;
	
	public void setUpdateStatusListener(UpdateStatusListener updateStatusListener) {
		this.updateStatusListener = updateStatusListener;
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
	
}