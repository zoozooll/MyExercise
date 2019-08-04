package com.oregonscientific.meep.together.comparision;

import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;

public class PermissionData {
	public static final int TYPE_HIGH = 0;
	public static final int TYPE_LOW = 1;
	public static final int TYPE_CUSTOM = 2;
	
	public static final int SET_INDEX_APPLICATION = 0;
	public static final int SET_INDEX_ONLINE_APPLICATION = 1;
	public static final int SET_INDEX_COMMUNICATOR = 2;
	public static final int SET_INDEX_PURCHASE = 3;
	public static final int SET_INDEX_INTERNET = 4;
	public static final int SET_INDEX_OTHERS = 5;
	public static final int SET_INDEX_BADWORD = 6;
	// setting for
	// applications(app,game,movie,music,ebook,picture)
	protected static SettingConfig applicationHighSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 1440);
	protected static SettingConfig applicationLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 1440);

	// applications(browser,youtube)
	protected static SettingConfig onlineApplicationHighSecurity = new SettingConfig(SettingConfig.ACCESS_DENY, 0);
	protected static SettingConfig onlineApplicationLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 1440);

	// setting for communicator
	protected static SettingConfig communicatorHighSecurity = new SettingConfig(SettingConfig.ACCESS_DENY, 0);
	protected static SettingConfig communicatorLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 0);
	// setting for purchase
	protected static SettingConfig purchaseHighSecurity = new SettingConfig(SettingConfig.ACCESS_DENY, 0);
	protected static SettingConfig purchaseLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 0);

	// setting for internet security
	protected static SettingConfig internetHighSecurity = new SettingConfig(SettingConfig.ACCESS_HIGH, 0);
	protected static SettingConfig internetLowSecurity = new SettingConfig(SettingConfig.ACCESS_MEDIUM, 0);

	// setting for others (inapp,googleplay)
	protected static SettingConfig othersHighSecurity = new SettingConfig(SettingConfig.ACCESS_DENY, 0);
	protected static SettingConfig othersLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 0);
	
	// setting for others (osgdbadword)
	protected static SettingConfig badwordHighSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 0);
	protected static SettingConfig badwordLowSecurity = new SettingConfig(SettingConfig.ACCESS_ALLOW, 0);

	protected static SettingConfig[] getSecuritySet(int type)
	{
		SettingConfig applicationSecurity = applicationHighSecurity;
		SettingConfig onlineApplicationScurity = onlineApplicationHighSecurity;
		SettingConfig commnicatorSecurity = communicatorHighSecurity;
		SettingConfig purchaseSecurity = purchaseHighSecurity;
		SettingConfig internetSecurity = internetHighSecurity;
		SettingConfig othersSecurity = othersHighSecurity;
		SettingConfig badwordSecurity = badwordHighSecurity;
		switch (type) {
		case TYPE_HIGH:
			
			break;
		case TYPE_LOW:
			applicationSecurity = applicationLowSecurity;
			 onlineApplicationScurity = onlineApplicationLowSecurity;
			 if(!UserFunction.isCreditCardVerified())
			 {
				 commnicatorSecurity = communicatorHighSecurity;
			 }
			 else
			 {
				 commnicatorSecurity = communicatorLowSecurity;
			 }
			purchaseSecurity = purchaseLowSecurity;
			internetSecurity = internetLowSecurity;
			othersSecurity = othersLowSecurity;
			badwordSecurity = badwordLowSecurity;
			break;

		default:
			break;
		}
		
		return new SettingConfig[]{applicationSecurity,onlineApplicationScurity,commnicatorSecurity,purchaseSecurity,internetSecurity,othersSecurity,badwordSecurity};
		
	}
}
