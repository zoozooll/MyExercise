package com.oregonscientific.meep.store2;

import java.util.ArrayList;

import android.app.DialogFragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oregonscientific.meep.store2.ctrl.RestRequest.RedeemListener;
import com.oregonscientific.meep.store2.global.MeepStoreApplication;
import com.oregonscientific.meep.store2.global.MeepStoreLog;
import com.oregonscientific.meep.store2.object.DownloadStoreItem;
import com.oregonscientific.meep.store2.object.RedeemConfirmFeedback;
import com.oregonscientific.meep.store2.object.RedeemConfirmItem;
import com.oregonscientific.meep.store2.object.RedeemFeedback;
import com.oregonscientific.meep.store2.object.RedeemItem;

public class PopUpDialogFragment extends DialogFragment{
	public static final int COUPON_CODE_EDIT = 1;  
	public static final int COUPON_COIN_CONFIRM = 2;  
    public static final int COUPON_COIN_FINISH = 3;  
    public static final int COUPON_ITEM_COMFIRM = 4;  
    public static final int COUPON_ITEM_FINISH = 5;  
    public static final int LOGIN_FAILED = 6;
    public static final int NO_NETWORK = 7;
    public static final int LOADING = 8;
    public static final int TIMEOUT = 9;
    public static final int COMMON_MESSAGE = 10;
    public static final int NOT_ENOUGH_SPACE = 11;
    public static final int OTA_UPGRADE = 12;
    public static final int EMPTY_SEARCH = 13;
    public static final int WAITING_APPROVAL = 14;
    public static final int REQUEST_COINS = 15;
    public static final int START_DOWNLOAD = 16;
    public static final int MEDIA_NOT_AVAILABLE = 17;
    
    public static final String FB_OP_CLOSE_APP = "close";
    
    private MeepStoreApplication mApp;
    private static String couponCode;
    private static int gotCoins;
    private static int remain;
    private static ArrayList<RedeemItem> redeemItems;
    private String prefix;
//    private ImageThreadLoader imageLoader;
    
    private UpdateUserCoinsListener updateCoins;
    static DialogFragment parent = null;
    
    
    public interface FeedBackListener
    {
    	public abstract void OnPopupFeedBack(String option, Object variable);
    }
    
  
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
        mApp = (MeepStoreApplication) this.getActivity().getApplicationContext();
//        imageLoader = mApp.getImageLoader();
    }
    
    public static PopUpDialogFragment newInstance(int title) {  
    	PopUpDialogFragment myDialogFragment = new PopUpDialogFragment();  
        Bundle bundle = new Bundle();  
        bundle.putInt("title", title);  
        myDialogFragment.setArguments(bundle);  
        return myDialogFragment;  
    }  
    public static PopUpDialogFragment newInstance(int title,String url,String message,String version) {  
    	PopUpDialogFragment myDialogFragment = new PopUpDialogFragment();  
    	Bundle bundle = new Bundle();  
    	bundle.putInt("title", title);  
    	bundle.putString("url", url);  
    	bundle.putString("message", message);  
    	bundle.putString("version", version);  
    	myDialogFragment.setArguments(bundle);  
    	return myDialogFragment;  
    }  
    public static PopUpDialogFragment newInstance(int title,PaymentFragment p) {  
    	PopUpDialogFragment myDialogFragment = new PopUpDialogFragment();  
    	Bundle bundle = new Bundle();  
    	bundle.putInt("title", title);  
    	myDialogFragment.setArguments(bundle); 
    	parent = p;
    	return myDialogFragment;  
    }  
  
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	int args = getArguments().getInt("title");  
    	View v = null;
    	switch (args) {  
			case COUPON_CODE_EDIT: 
				v = inflater.inflate(R.layout.dialog_exchange, container, false);
				createCouponCodeEdit(v);
				break;
			case COUPON_COIN_CONFIRM: 
				v = inflater.inflate(R.layout.dialog_big_exchange, container, false);
				createCouponCoinConfirm(v);
				break;
			case COUPON_COIN_FINISH:  
				v = inflater.inflate(R.layout.dialog_big_congratulation_exchange, container, false);
				createCouponCoinFinish(v);
				break;
			case COUPON_ITEM_COMFIRM: 
				v = inflater.inflate(R.layout.dialog_big_redeem, container, false);
				createCouponItemConfirm(v);
				break;
			case COUPON_ITEM_FINISH:  
				v = inflater.inflate(R.layout.dialog_big_congratulation_redeem, container, false);
				createCouponItemFinish(v);
				break;
			case LOGIN_FAILED:
				v = inflater.inflate(R.layout.dialog_login_fail, container, false);
				createLoginFailPopup(v);
				break;
			case NO_NETWORK:
				v = inflater.inflate(R.layout.dialog_no_network, container, false);
				createNoNetworkPopup(v);
				break;
			case LOADING:
				v = inflater.inflate(R.layout.loading, container, false);
				createLoading(v);
				break;
			case TIMEOUT:
				v = inflater.inflate(R.layout.timeout, container, false);
				createTimeout(v);
				break;
			case COMMON_MESSAGE:
				v = inflater.inflate(R.layout.common_message, container, false);
				createCommonMessage(v);
				break;
			case NOT_ENOUGH_SPACE:
				v = inflater.inflate(R.layout.dialog_not_enough_space, container, false);
				createNotEnoughSpace(v);
				break;
			case OTA_UPGRADE:
				String otaUrl = getArguments().getString("url");
				String message = getArguments().getString("message");
				String version = getArguments().getString("version");
				v = inflater.inflate(R.layout.dialog_ota_update, container, false);
				createOtaUpdatePopup(v,otaUrl,message,version);
				break;
			case EMPTY_SEARCH:
				v = inflater.inflate(R.layout.common_message, container, false);
				createEmptySearch(v);
				break;
			case WAITING_APPROVAL:
				v = inflater.inflate(R.layout.common_message, container, false);
				createCommonMessage(v,R.string.success_waiting_approval);
				break;
			case REQUEST_COINS:
				v = inflater.inflate(R.layout.common_message, container, false);
				createCommonMessage(v,R.string.success_request_coins);
				break;
			case START_DOWNLOAD:
				v = inflater.inflate(R.layout.common_message, container, false);
				createCommonMessage(v,R.string.success_start_download);
				break;
			case MEDIA_NOT_AVAILABLE:
				v = inflater.inflate(R.layout.dialog_media_not_available, container, false);
				createMediaNotAvailablePopup(v);
				break;
		}  
        return v;
    }
    
	public void createCouponCodeEdit(View v)
    {
		Button back = (Button) v.findViewById(R.id.back);
		final Button confirm = (Button) v.findViewById(R.id.confirm);
		final TextView coupon = (TextView) v.findViewById(R.id.exchangeCode);
		final TextView errorValid = (TextView) v.findViewById(R.id.errorValid);
		final View errorUsed = v.findViewById(R.id.errorUsed);
		
		back.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				coupon.setText("");
				PopUpDialogFragment.this.dismiss();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				couponCode = coupon.getText().toString().trim();
				if(couponCode.length()!=19)
				{
					if(couponCode.length() == 0)
					{
						errorValid.setText(R.string.empty_coupon_code);
					}
					else
					{
						errorValid.setText(R.string.exchange_error_valid);
					}
					errorUsed.setVisibility(View.GONE);
					errorValid.setVisibility(View.VISIBLE);
				}
				else
				{
					if(mApp.isNetworkAvailable(getActivity()))
					{
						handler.sendEmptyMessage(4);
						errorValid.setVisibility(View.INVISIBLE);
						errorUsed.setVisibility(View.INVISIBLE);
						couponCode = couponCode.toUpperCase();
						arg0.setEnabled(false);
						mApp.getRestRequest().getRedeemStoreItem(couponCode, mApp.getUserToken());
						mApp.getRestRequest().setRedeemListener(new RedeemListener() {
							
							@Override
							public void onRedeemReceived(RedeemFeedback feedback) {
								if (loadPopupFragment != null)
									loadPopupFragment.dismiss();
								
								confirm.setEnabled(true);
								
								if (feedback != null) {
									coupon.setText("");
									int code = feedback.getCode();
									switch (code) {
									case 200:
										String type = feedback.getType();
										PopUpDialogFragment.this.dismiss();
										if (type.equals(RedeemFeedback.TYPE_CONTENTS)) {
											PopUpDialogFragment.newInstance(COUPON_ITEM_COMFIRM).show(getFragmentManager(), "dialog");
											remain = feedback.getRemaining();
											redeemItems = feedback.getResults();
										} else if (type.equals(RedeemFeedback.TYPE_COINS)) {
											PopUpDialogFragment.newInstance(COUPON_COIN_CONFIRM).show(getFragmentManager(), "dialog");
											gotCoins = feedback.getCoins();
										}
										break;
									case 404:
										errorValid.setVisibility(View.VISIBLE);
										break;
									case 410:
										errorUsed.setVisibility(View.VISIBLE);
										break;
									case 999:
										handler.sendEmptyMessage(5);
										break;
									default:
										errorValid.setVisibility(View.VISIBLE);
										break;
									}
								} else {
									handler.sendEmptyMessage(5);
								}
										
							}
							
							@Override
							public void onConfirmRedeemReceived(
									RedeemConfirmFeedback redeemConfirmFeedbacks) {
								//nothing
								
							}
						});
					}
					else
					{
						handler.sendEmptyMessage(3);
					}
				}
				
			}
		});
    }
    public void createCouponCoinConfirm(View v)
    {
    	Button cancel = (Button) v.findViewById(R.id.cancel);
    	final Button confirm = (Button) v.findViewById(R.id.confirm);
    	TextView textCoins = (TextView) v.findViewById(R.id.textCoins);
    	textCoins.setText(String.format(textCoins.getText().toString(), gotCoins));
    	mApp.getRestRequest().setRedeemListener(new RedeemListener() {
    		
    		@Override
    		public void onRedeemReceived(RedeemFeedback feedback) {
    			//nothing
    		}
    		
    		@Override
    		public void onConfirmRedeemReceived(
    				RedeemConfirmFeedback redeemConfirmFeedbacks) {
    			if(loadPopupFragment!=null)
					loadPopupFragment.dismiss();
    			confirm.setEnabled(true);
				if (redeemConfirmFeedbacks != null) {
					int code = redeemConfirmFeedbacks.getCode();
					Log.d("test", "code:" + code);
					if (code == 200) {
						PopUpDialogFragment.this.dismiss();
						PopUpDialogFragment.newInstance(PopUpDialogFragment.COUPON_COIN_FINISH).show(getFragmentManager(), "dialog");
						mApp.getLoginInfo().coins = Integer.toString(redeemConfirmFeedbacks.getCoins());
						// Log.d("test","now coins are" +
						// mApp.getLoginInfo().coins);
						updateCoins = ((MainActivity) getActivity());
						updateCoins.updateUserCoinsListener();
					} else if (code == 999) {
						handler.sendEmptyMessage(5);
					}
				} else {
					handler.sendEmptyMessage(5);
				}
    		}
    	});
    	cancel.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			if(mApp.isNetworkAvailable(getActivity()))
				{
    				handler.sendEmptyMessage(4);
	    			arg0.setEnabled(false);
	    			mApp.getRestRequest().confirmRedeemStoreItem(couponCode, mApp.getUserToken());
				}
    			else
    			{
    				handler.sendEmptyMessage(3);
    			}
    		}
    	});
    }

	public void createCouponItemConfirm(View v) {
		Button cancel = (Button) v.findViewById(R.id.cancel);

		LinearLayout flowlayout = (LinearLayout) v.findViewById(R.id.flowlayout);
		if (redeemItems != null) {
			for (int i=0;i<redeemItems.size();i++) {
				RedeemItem item = redeemItems.get(i);
				View itemlayout = ((LayoutInflater) this.getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.item_item_redeem, null, false);
				if(i==0)
				{
					View plus = itemlayout.findViewById(R.id.plus);
					plus.setVisibility(View.GONE);
				}
				final ImageView image = (ImageView) itemlayout.findViewById(R.id.image);
				TextView name = (TextView) itemlayout.findViewById(R.id.textName);
				mApp.getImageDownloader().download(mApp.getLoginInfo().url_prefix + item.getIcon(), 75, 75, image);
//				Bitmap cachedImage = null;
//				try {
//					prefix = mApp.getLoginInfo().url_prefix;
//					cachedImage = imageLoader.loadImage(prefix + item.getIcon(), new ImageLoadedListener() {
//						public void imageLoaded(Bitmap imageBitmap) {
//							image.setImageBitmap(imageBitmap);
//						}
//					});
//				} catch (MalformedURLException e) {
//					Log.e("test", "Bad remote image URL: " + e.toString());
//				}
//
//				if (cachedImage != null) {
//					image.setImageBitmap(cachedImage);
//				}
				name.setText(item.getName());
				flowlayout.addView(itemlayout);
			}
		}
    	
		final Button confirm = (Button) v.findViewById(R.id.confirm);
		mApp.getRestRequest().setRedeemListener(new RedeemListener() {

			@Override
			public void onRedeemReceived(RedeemFeedback feedback) {
				// nothing
			}

			@Override
			public void onConfirmRedeemReceived(RedeemConfirmFeedback redeemConfirmFeedbacks) {
				if (loadPopupFragment != null)
					loadPopupFragment.dismiss();
				if (redeemConfirmFeedbacks != null) {
					confirm.setEnabled(true);
					int code = redeemConfirmFeedbacks.getCode();
					remain = redeemConfirmFeedbacks.getRemaining();
					if (code == 200) {
						PopUpDialogFragment.this.dismiss();
						PopUpDialogFragment.newInstance(PopUpDialogFragment.COUPON_ITEM_FINISH).show(getFragmentManager(), "dialog");
						
						if(redeemConfirmFeedbacks.getContents()!=null)
						{
							for(RedeemConfirmItem redeemItem:redeemConfirmFeedbacks.getContents())
							{
								DownloadStoreItem item = new DownloadStoreItem(redeemItem.getItem_id(), 
										redeemItem.getName(), 
										redeemItem.getType(), 
										redeemItem.getImage(), 
										redeemItem.getUrl(), "", 
										redeemItem.getPackage_name());
								mApp.getStoreDownloadCtrl().addStoreDownloadItem(item);
							}
						}
						
					} else if (code == 999) {
						handler.sendEmptyMessage(5);
					}
				} else {
					handler.sendEmptyMessage(5);
				}

			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				PopUpDialogFragment.this.dismiss();
			}
		});
		confirm.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO:send exchange code
				if (mApp.isNetworkAvailable(getActivity())) {
					handler.sendEmptyMessage(4);
					arg0.setEnabled(false);
					mApp.getRestRequest().confirmRedeemStoreItem(couponCode, mApp.getUserToken());
				} else {
					handler.sendEmptyMessage(3);
				}
			}
		});
	}
    
    public void createLoginFailPopup(View v)
    {	
    	final Button confirm = (Button) v.findViewById(R.id.confirm);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    			getActivity().finish();
    		}
    	});
    	v.setFocusable(true);
    	v.setFocusableInTouchMode(true);
    	v.requestFocus();
    	v.setOnKeyListener(new OnKeyListener() {
			
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if ((arg1 == KeyEvent.KEYCODE_BACK)){  
                    getActivity().finish();
                    return true;  
                } 
				return false;
			}
		});
    }
    
    public void createCouponItemFinish(View v)
    {
    	Button ok = (Button) v.findViewById(R.id.ok);
    	TextView textRemain = (TextView) v.findViewById(R.id.textRemain);
    	textRemain.setText(String.format(textRemain.getText().toString(),remain));
    	
    	ok.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    }
    public void createCouponCoinFinish(View v)
    {
    	Button ok = (Button) v.findViewById(R.id.ok);
    	TextView textCoins = (TextView) v.findViewById(R.id.textCoins);
    	textCoins.setText(String.format(textCoins.getText().toString(), gotCoins));
    	ok.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    }
    
    public void createNoNetworkPopup(View v)
    {	
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
//    			PopUpDialogFragment.this.dismiss();
				final Intent intent = new Intent(Intent.ACTION_MAIN, null);
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				final ComponentName cn = new ComponentName("com.android.settings", "com.android.settings.wifi.WifiSettings");
				intent.setComponent(cn);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				getActivity().startActivity(intent);
				getActivity().finish();

    		}
    	});
    	final Button cancel = (Button) v.findViewById(R.id.cancel);
    	cancel.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    			getActivity().finish();
    		}
    	});
    }
    public void createTimeout(View v)
    {	
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	if(getActivity() instanceof MainActivity)
    	{
    		confirm.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				getActivity().finish();
    				PopUpDialogFragment.this.dismiss();
    			}
    		});
    		v.setFocusable(true);
        	v.setFocusableInTouchMode(true);
        	v.requestFocus();
        	v.setOnKeyListener(new OnKeyListener() {
    			
    			@Override
    			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
    				if ((arg1 == KeyEvent.KEYCODE_BACK)){  
                        getActivity().finish();
                        return true;  
                    } 
    				return false;
    			}
    		});
    	}
    	else
    	{
    		confirm.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View arg0) {
    				PopUpDialogFragment.this.dismiss();
    			}
    		});
    	}
    }
    public void createCommonMessage(View v)
    {	
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    			if(parent!=null)
    			{
    				parent.dismiss();
    				parent = null;
    			}
    			
    		}
    	});
    }
    public void createCommonMessage(View v,int resId)
    {	
    	TextView text = (TextView) v.findViewById(R.id.exchangeCode);
    	text.setText(resId);
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    			if(parent!=null)
    			{
    				parent.dismiss();
    				parent = null;
    			}
    			
    		}
    	});
    }
    public void createNotEnoughSpace(View v)
    {	
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    }
    public void createEmptySearch(View v)
    {	
    	TextView text = (TextView) v.findViewById(R.id.exchangeCode);
    	text.setText(R.string.empty_search);
    	final Button confirm = (Button) v.findViewById(R.id.ok);
    	confirm.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    }
    public void createLoading(View v)
    {	
    	v.setFocusableInTouchMode(true);
    	v.setFocusable(true);
    	v.requestFocus();
    	v.setOnKeyListener( new OnKeyListener()
    	{
			@Override
			public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
				if( arg1 == KeyEvent.KEYCODE_BACK )
    	        {
					if(getActivity()!=null)
						getActivity().finish();
    	            return true;
    	        }
    	        return false;
			}
    	} );
    }
    
    DialogFragment loadPopupFragment;
    public Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 3:
				loadPopupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.NO_NETWORK);
				if(getFragmentManager()!=null)
					loadPopupFragment.show(getFragmentManager(), "dialog");
				break;
			case 4:
				loadPopupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
				if(getFragmentManager()!=null)
					loadPopupFragment.show(getFragmentManager(), "dialog");
				break;
			case 5:
				loadPopupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.TIMEOUT);
				if(getFragmentManager()!=null)
					loadPopupFragment.show(getFragmentManager(), "dialog");
				break;
			default:
				break;
			}
		}

	};
	
	public void createOtaUpdatePopup(View v,final String otaUrl,String message,String version)
    {	
		TextView textView = (TextView) v.findViewById(R.id.message);
		TextView textVersion = (TextView) v.findViewById(R.id.version);
		textView.setText(message);
		textVersion.setText(version);
    	Button later = (Button) v.findViewById(R.id.later);
    	Button update = (Button) v.findViewById(R.id.update);
    	later.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    	update.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			DownloadStoreItem item = new DownloadStoreItem( DownloadStoreItem.TYPE_OTA,  DownloadStoreItem.TYPE_OTA, DownloadStoreItem.TYPE_OTA, DownloadStoreItem.TYPE_OTA, otaUrl,"", "");
    			mApp.getStoreDownloadCtrl().addStoreDownloadItem(item);
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
    }
	
	private void createMediaNotAvailablePopup(View v) {
		final Button back = (Button) v.findViewById(R.id.back);
    	back.setOnClickListener(new View.OnClickListener() {
    		
    		@Override
    		public void onClick(View arg0) {
    			PopUpDialogFragment.this.dismiss();
    		}
    	});
	}

	
	public interface UpdateUserCoinsListener
	{
		public void updateUserCoinsListener();
	}
	
	@Override
	public void dismiss() {
		// TODO Auto-generated method stub
		try
		{
			super.dismiss();
		}
		catch(Exception e)
		{
			MeepStoreLog.LogMsg("parent activity has been closed");
		}
	}
	
  
}  