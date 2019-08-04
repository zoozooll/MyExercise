package com.oregonscientific.meep.together.library.rest.listener;

import com.oregonscientific.meep.together.bean.ResponseBasic;
import com.oregonscientific.meep.together.bean.ResponseLoadCoin;


public interface OnAllocateCoinsListener {
	public void onAllocateCoinsSuccess(ResponseLoadCoin infoCoin);
	public void onAllocateCoinsFailure(ResponseBasic r);
	public void onAllocateCoinsTimeout();
	
}
