package com.oregonscientific.meep.together.comparision;

import java.util.ArrayList;
import java.util.List;

import com.oregonscientific.meep.together.activity.Utils;
import com.oregonscientific.meep.together.bean.PermissionNameIndex;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;

public class PermissionComparision extends PermissionData{
	
	//high security level
	private List<SettingConfig> mPermissionHighSecurityLevel;	
	//low security level
	private List<SettingConfig> mPermissionLowSecurityLevel;
	
	
	
	public List<SettingConfig> getPermissionHighSecurityLevel() {
		if(mPermissionHighSecurityLevel == null)
		{
			mPermissionHighSecurityLevel = getHighSecurityLevelList();
		}
		return mPermissionHighSecurityLevel;
	}
	public void setPermissionHighSecurityLevel(
			List<SettingConfig> permissionHighSecurityLevel) {
		mPermissionHighSecurityLevel = permissionHighSecurityLevel;
	}
	public List<SettingConfig> getPermissionLowSecurityLevel() {
		if(mPermissionLowSecurityLevel == null)
		{
			mPermissionLowSecurityLevel = getLowSecurityLevelList();
		}
		return mPermissionLowSecurityLevel;
	}

	private List<SettingConfig> getSecurityLevelList(int type) {
		SettingConfig[] set = getSecuritySet(type);
		SettingConfig applicationSecurity = set[SET_INDEX_APPLICATION];
		SettingConfig onlineApplicationSceurity = set[SET_INDEX_ONLINE_APPLICATION];
		SettingConfig communicatorSecurity = set[SET_INDEX_COMMUNICATOR];
		SettingConfig purchaseSecurity = set[SET_INDEX_PURCHASE];
		SettingConfig internetSecurity = set[SET_INDEX_INTERNET];
		SettingConfig othersSecurity = set[SET_INDEX_OTHERS];
		SettingConfig badwordSecurity = set[SET_INDEX_BADWORD];
		
		List<SettingConfig> permissionsList = new ArrayList<SettingConfig>();
		//app -> youtube
		for(int i = PermissionNameIndex.APP; i<= PermissionNameIndex.PICTURE;i++)
		{
			permissionsList.add(i,applicationSecurity);
		}
		
		permissionsList.add(PermissionNameIndex.BROWSER,onlineApplicationSceurity);
		permissionsList.add(PermissionNameIndex.YOUTUBE,onlineApplicationSceurity);
		//communicator
		permissionsList.add(PermissionNameIndex.COMMUNICATOR,communicatorSecurity);
		//purchase
		permissionsList.add(PermissionNameIndex.PURCHASE,purchaseSecurity);
		//inapp
		permissionsList.add(PermissionNameIndex.INAPP,othersSecurity);
		//googleplay
		permissionsList.add(PermissionNameIndex.GOOGLEPALY,othersSecurity);
		//internet security
		permissionsList.add(PermissionNameIndex.INTERNET_SECURITY_LEVEL,internetSecurity);
		//osgd badword
		permissionsList.add(PermissionNameIndex.OSGD_BADWORD,badwordSecurity);
		
		return permissionsList;
	}
	private List<SettingConfig> getLowSecurityLevelList() {
		return getSecurityLevelList(TYPE_LOW);
	}
	private List<SettingConfig> getHighSecurityLevelList() {
		return getSecurityLevelList(TYPE_HIGH);
	}
	public void setPermissionLowSecurityLevel(
			List<SettingConfig> permissionLowSecurityLevel) {
		mPermissionLowSecurityLevel = permissionLowSecurityLevel;
	}	
	
	public boolean equalsToHighLevel(List<SettingConfig> list)
	{
		List<SettingConfig> definedList = getHighSecurityLevelList();
		return compareLevelList(list, definedList);
	}
	public boolean equalsToLowLevel(List<SettingConfig> list)
	{
		List<SettingConfig> definedList = getLowSecurityLevelList();
		return compareLevelList(list, definedList);
	}
	
	public boolean compareLevelList(List<SettingConfig> list,List<SettingConfig> definedList)
	{
		for(int i = 0;i<definedList.size();i++)
		{
			Utils.printLogcatDebugMessage(list.get(i).getAccess() +" - "+list.get(i).getTimelimit());
			Utils.printLogcatDebugMessage(definedList.get(i).getAccess() +" - "+definedList.get(i).getTimelimit());
			if(!list.get(i).equals(definedList.get(i)))
			{
				return false;
			}
		}
		return true;
	}
	
	public boolean isInternetHighSecurityLevel(List<SettingConfig> list)
	{
		return list.get(PermissionNameIndex.INTERNET_SECURITY_LEVEL).equals(internetHighSecurity);
	}
	
}
