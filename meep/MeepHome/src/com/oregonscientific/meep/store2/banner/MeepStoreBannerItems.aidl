/*
 * Copyright (c) 2013 Oregon Scientific Global Distribution Limited
 *
 * All rights reserved.
 *
 */
package com.oregonscientific.meep.store2.banner;

/**
 * Response code from API calls
 *
 * RESULT_OK = 0 - success
 * RESULT_CANCELED = 1 - user canceled
 * RESULT_ERROR = 2 - invalid arguments provided to the API
 * RESULT_ITEM_UNAVAILABLE = 3 - requested banner is not available
 */
import com.oregonscientific.meep.store2.banner.MeepStoreBannerItemsCallback;

interface MeepStoreBannerItems {
    Bundle getLatestThreeBannerItems();
    
    Bundle getLatestBannerItems();
    
	void registerCallback(MeepStoreBannerItemsCallback callback);
	
	void unregisterCallback(MeepStoreBannerItemsCallback callback);
    
}