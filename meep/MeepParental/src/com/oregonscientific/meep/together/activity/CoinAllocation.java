package com.oregonscientific.meep.together.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadCoin;
import com.oregonscientific.meep.together.library.rest.listener.OnAllocateCoinsListener;
import com.oregonscientific.meep.together.library.rest.listener.OnUpdateCoinListener;

public class CoinAllocation extends AccountGeneral{
	
	//===Coin 2
	ViewFlipper coinflipper;
	View LayoutCoinAllocate;
	View LayoutCoinConfirm;
	View LayoutCoinSuccess;
	
	//button
	ImageButton barLeftCoin2Back;
	Button coinNext;
	Button coinConfirm;
	Button coinClose;
	EditText alloCoin;
	EditText parentCoin;
	EditText alloCoinConfirm;
	EditText resultCoin;
	
	private  int allocate;
	MyProgressDialog loading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main_coin2);
		loading = new MyProgressDialog(this);
		loading.setMessage(this.getResources().getString(R.string.loading_text));
		
    	coinflipper = (ViewFlipper) findViewById(R.id.coinflipper);
    	LayoutCoinAllocate = coinflipper.findViewById(R.id.LayoutCoinAllocate);
    	LayoutCoinConfirm = coinflipper.findViewById(R.id.LayoutCoinConfirm);
    	LayoutCoinSuccess = coinflipper.findViewById(R.id.LayoutCoinSuccess);
		
		//---coin allocate---
        barLeftCoin2Back = (ImageButton) findViewById(R.id.barImageButtonBack);
        coinNext = (Button) LayoutCoinAllocate.findViewById(R.id.btnNext);
        coinConfirm = (Button) LayoutCoinConfirm.findViewById(R.id.btnConfirm);
        coinClose = (Button) LayoutCoinSuccess.findViewById(R.id.btnClose);
        alloCoin = (EditText) LayoutCoinAllocate.findViewById(R.id.alloCoin);
        parentCoin = (EditText) LayoutCoinAllocate.findViewById(R.id.parentCoin);
        alloCoinConfirm = (EditText) LayoutCoinConfirm.findViewById(R.id.alloCoin);
        resultCoin = (EditText) LayoutCoinSuccess.findViewById(R.id.coin);
        
        String name = getCurrentKid().getName();
        TextView allocateName1 = (TextView) LayoutCoinAllocate.findViewById(R.id.allocateName1);
		TextView allocateName2 = (TextView) LayoutCoinConfirm.findViewById(R.id.allocateName2);
		TextView allocateName3 = (TextView) LayoutCoinSuccess.findViewById(R.id.allocateName3);
		allocateName1.setText(String.format(this.getResources().getString(R.string.main_page_coins_allocate_1), name));
		allocateName2.setText(name);
		allocateName3.setText(name);
		UserFunction.loadImage((ImageView) LayoutCoinConfirm.findViewById(R.id.coin_user_icon1), getCurrentKid().getAvatar());
		UserFunction.loadImage((ImageView) LayoutCoinSuccess.findViewById(R.id.coin_user_icon2), getCurrentKid().getAvatar());
        
		parentCoin.setText(getCurrentUser().getCoins());
        //listeners
        barLeftCoin2Back.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		switch(coinflipper.getDisplayedChild())
        		{
        		case 0:
        		case 2:
        			finish();
        			break;
        		case 1:
        			coinflipper.showPrevious();
        			break;
        		}
        	}
        });
        
        coinNext.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		hideKeyboard(arg0);
        		String s = alloCoin.getText().toString().trim();
        		s = removeZeroPrefix(s);
        		if(s==null||s.length()==0||s.equals("0"))
        		{
        			UserFunction.popupMessage(R.string.coin_null, CoinAllocation.this,loading);
        		}
        		//TODO: parent's coins are smaller than allocated
        		else
        		{
        			try {
						allocate = Integer.parseInt(s);
						alloCoinConfirm.setText(s);
						coinflipper.setDisplayedChild(1);
						coinConfirm.setEnabled(true);
					} catch (NumberFormatException e) {
					}
        		}
        		
        	}
        });
        coinConfirm.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		loading.show();
        		if(allocate>Integer.parseInt(getCurrentUser().getCoins()))
				{
        			UserFunction.popupMessage(R.string.coin_insuff, CoinAllocation.this,loading);
				}
        		else
        		{
        			arg0.setEnabled(false);
        			if(!UserFunction.isNetworkAvailable(getApplicationContext()))
    		    	{
    		    		UserFunction.popupMessage(R.string.no_network, getApplicationContext(),loading);
    		    	} else {
        				getCurrentCoins();
        			}
        		}
        	}
        });
        coinClose.setOnClickListener(new View.OnClickListener() {
        	
        	@Override
        	public void onClick(View arg0) {
        		finish();
        	}
        });
        
	}
	
	
	public void getCurrentCoins()
	{
		UserFunction.getRestHelp().refreshCoin(getCurrentKid().getUserId());
		UserFunction.getRestHelp().setOnUpdateCoinListener(new OnUpdateCoinListener() {
			
			@Override
			public void onUpdateCoinSuccess(ResponseLoadCoin infoCoin) {
				onUpdateCoin(infoCoin);
				if(!UserFunction.isNetworkAvailable(getApplicationContext()))
				{
					UserFunction.popupMessage(R.string.no_network, getApplicationContext(),loading);
				} else {
					sendUpdatedCoins(allocate);
				}
			}
			
			@Override
			public void onUpdateCoinFailure(ResponseBasic r) {
//		    	UserFunction.popupMessage(R.string.allocate_coins_failure, CoinAllocation.this,loading);
				UserFunction.popupResponse(r.getStatus(), CoinAllocation.this, loading);
			}

			@Override
			public void onUpdateCoinTimeout() {
				UserFunction.popupMessage(R.string.please_retry, CoinAllocation.this, loading);
			}
		});
	}
	
	public void sendUpdatedCoins(int allocate)
	{
		UserFunction.getRestHelp().saveAllocation(getCurrentKid().getUserId(), allocate);
    	UserFunction.getRestHelp().setOnAllocateCoinsListener(new OnAllocateCoinsListener() {
 			
 			@Override
 			public void onAllocateCoinsSuccess(ResponseLoadCoin infoCoin) {
 				onAllocateCoin(infoCoin);
 			}
 			
 			@Override
 			public void onAllocateCoinsFailure(ResponseBasic r) {
 				onAllocateCoinFailure(r);
 				
 			}
			@Override
			public void onAllocateCoinsTimeout() {
				UserFunction.popupMessage(R.string.please_retry, CoinAllocation.this, loading);
			}
 		});
	}
	/**
     * Coins
     * 
     */
    public void onUpdateCoin(ResponseLoadCoin childInfo)
    {
    	long childCoin = childInfo.getCoins()+allocate;
    	resultCoin.setText(Long.toString(childCoin));
    	
    }
    
    public void onAllocateCoin(ResponseLoadCoin parentInfo)
    {
    	long parentCoin = parentInfo.getCoins();
    	
    	//show result page
    	coinflipper.setDisplayedChild(2);
    	loading.dismiss();
//    	UserFunction.getRestHelp().addNewKidOrUpdate();
    	Intent intent = new Intent();
    	Bundle bundle = getIntentBundle();
    	bundle.putLong("update-coin", parentCoin);
    	intent.putExtra("result", bundle);
    	setResult(RESULT_OK,intent);
    }
    
    public void onAllocateCoinFailure(ResponseBasic r)
    {
    	coinConfirm.setEnabled(true);
//    	UserFunction.popupMessage(R.string.allocate_coins_failure, this,loading);
    	UserFunction.popupResponse(r.getStatus(), CoinAllocation.this, loading);
    }
	
    public static String removeZeroPrefix(String str) {
		if (str != null && str.trim().length() != 0) {
			while (true) {
				if (str.startsWith("0")) {
					str = str.substring(1, str.length());
				} else {
					break;
					
				}
			}
		}
		return str;
	}
    public void hideKeyboard(View v) {
		// hide keyboard
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

}
