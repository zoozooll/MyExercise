package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadCoin;



public interface OnUpdateCoinListener {
	public void onUpdateCoinSuccess(ResponseLoadCoin infoCoin);
	public void onUpdateCoinFailure(ResponseBasic r);
	public void onUpdateCoinTimeout();
	
}
