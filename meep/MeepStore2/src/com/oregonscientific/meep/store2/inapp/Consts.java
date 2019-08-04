package com.oregonscientific.meep.store2.inapp;
public class Consts {
    public static final int RESULT_OK = 0;
    public static final int RESULT_USER_CANCELED = 1;
    public static final int RESULT_SERVICE_UNAVAILABLE = 2;
    public static final int RESULT_BILLING_UNAVAILABLE = 3;
    public static final int RESULT_ITEM_UNAVAILABLE = 4;
    public static final int RESULT_DEVELOPER_ERROR = 5;
    public static final int RESULT_ERROR = 6;
    
    //key in Bundle response
    public static final String RESPONSE_CODE = "RESPONSE_CODE";
    public static final String RESPONSE_GET_SKU_DETAILS_LIST = "DETAILS_LIST";
    public static final String RESPONSE_BUY_INTENT = "BUY_INTENT";
    public static final String RESPONSE_INAPP_PURCHASE_DATA = "INAPP_PURCHASE_DATA";
    public static final String RESPONSE_INAPP_PURCHASE_ITEM_LIST = "INAPP_PURCHASE_ITEM_LIST";
    public static final String RESPONSE_INAPP_PURCHASE_DATA_LIST = "INAPP_PURCHASE_DATA_LIST";
    public static final String INAPP_CONTINUATION_TOKEN = "INAPP_CONTINUATION_TOKEN";
    public static final String GET_SKU_DETAILS_ITEM_LIST = "ITEM_ID_LIST";
    
    public static final int[] SUPPORT_VERSIONS = {1,2};
    
    public final static String URL_IAP_AVAILABLE_SKUS = "store/iap/list/";
    public final static String URL_IAP_PURCHASED_ITEM_PREFIX = "store/iap/list/";
    public final static String URL_IAP_PURCHASED_ITEM_SUFFIX = "/purchased";
    public final static String URL_IAP_PURCHASE = "store/iap/purchase";
    public final static String URL_IAP_VERIFY = "store/iap/verify";
    
    public final static String KEY_PURCHASE_SKU = "purchase_sku";
    public final static String KEY_PURCHASE_PACKAGE_NAME = "purchase_package_name";
    public final static String KEY_PURCHASE_AUTH = "purchase_auth";
    
}
