/*
 * Copyright (c) 2013 Oregon Scientific Global Distribution Limited
 *
 * All rights reserved.
 *
 */

package com.oregonscientific.meep.store2.inapp;

/**
 * Response code from API calls
 *
 * RESULT_OK = 0 - success
 * RESULT_CANCELED = 1 - user canceled the purchase
 * RESULT_ERROR = 2 - invalid arguments provided to the API
 * RESULT_ITEM_UNAVAILABLE = 3 - requested SKU in not available for purchase
 * RESULT_ITEM_ALREADY_OWNED = 4 - user already owned this item
 * RESULT_ITEM_NOT_OWNED = 5 - user has not owned this item
 */

interface MeepStoreInAppPurchase {
    /**
     * Checks whether the requested API version supports in-app purchase
     * @param apiVersion the API version in use
     * @param packageName the package name of the request app
     * @return RESULT_OK(0) if success, other response codes on
     *             failure as listed above.
     */
    int isPurchaseSupported(int apiVersion, String packageName);
    
    /**
     * Retrieve details of SKUs
     * Given a list of SKUs in the skusBundle, this returns a bundle with a list of
     * JSON strings containing the productId, price, title, description, consumability.
     * @param apiVersion the API version in use
     * @param packageName the package name of the calling app
     * @param skusBundle bundle containing a StringArrayList of SKUs with key "ITEM_ID_LIST"
     * @return Bundle containing the following key-value pairs
     *         "RESPONSE_CODE" with int value, RESULT_OK(0) if success, other response codes on
     *              failure as listed above.
     *         "DETAILS_LIST" with a StringArrayList containing purchase information (JSON format)
     *              '{ "productId" : "exampleSku", "coins" : 199,
     *                 "title : "Example Title", "description" : "This is an example description" ,"consumable" : true}'
     */
    Bundle getSkuDetails(int apiVersion, String packageName, in Bundle skusBundle);
    
    /**
     * Returns a pending intent to launch the purchase flow for an in-app item by providing a SKU.
     * @param apiVersion the API version in use
     * @param packageName package name of the calling app
     * @param sku the SKU of the in-app item
     * @param authString the shared key to ensure that calling app is authorized
     * @return Bundle containing the following key-value pairs
     *         "RESPONSE_CODE" with int value, RESULT_OK(0) if success, other response codes on
     *              failure as listed above.
     *         "BUY_INTENT" - PendingIntent to start the purchase flow
     *
     * The Pending intent should be launched with startIntentSenderForResult. When purchase flow
     * has completed, the onActivityResult() will give a resultCode of OK or CANCELED.
     * If the purchase is successful, the result data will contain the following key-value pairs
     *         "RESPONSE_CODE" with int value, RESULT_OK(0) if success, other response codes on
     *              failure as listed above.
     *         "INAPP_PURCHASE_DATA" - order information in JSON format
     *              '{ "orderId" : "123456789987654321",
     *                 "packageName" : "com.example.app",
     *                 "productId" : "exampleSku",
     *                 "purchaseTime" : 1345678900000,
     *                 "purchaseToken" : "122333444455555" }'
     */
    Bundle getBuyIntent(int apiVersion, String packageName, String sku, String authString);
    
    
    /**
     * Returns the current SKUs owned by the user of package name specified along with purchase information.
     * @param apiVersion the API version in use
     * @param packageName package name of the calling app
     * @param continuationToken to be set as null for the first call, if the number of owned
     *        skus are too many (more than 25), a continuationToken is returned in the response bundle.
     *        This method can be called again with the continuation token to get the next set of
     *        owned skus.
     * @return Bundle containing the following key-value pairs
     *         "RESPONSE_CODE" with int value, RESULT_OK(0) if success, other response codes on
     *              failure as listed above.
     *         "INAPP_PURCHASE_ITEM_LIST" - StringArrayList containing the list of SKUs
     *         "INAPP_PURCHASE_DATA_LIST" - StringArrayList containing the purchase information
     *         "INAPP_CONTINUATION_TOKEN" - String containing a continuation token for the
     *                                      next set of in-app purchases.
     */
    Bundle getPurchases(int apiVersion, String packageName, String continuationToken);
}