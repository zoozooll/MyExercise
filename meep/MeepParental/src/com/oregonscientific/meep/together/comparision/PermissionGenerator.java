package com.oregonscientific.meep.together.comparision;

import java.util.ArrayList;
import java.util.List;

import android.widget.ArrayAdapter;

import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.together.activity.Consts;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.PermissionNameIndex;
import com.oregonscientific.meep.together.bean.Permissions;
import com.oregonscientific.meep.together.bean.SettingConfig;
import com.oregonscientific.meep.together.library.rest.RestClientUsage;

public class PermissionGenerator extends PermissionData {
	public Permissions generateHighSecurityLevel() {
		return generateSpecificSecurityLevelPermission(TYPE_HIGH);
	}

	public Permissions generateLowSecurityLevel() {
		return generateSpecificSecurityLevelPermission(TYPE_LOW);
	}

	private Permissions generateSpecificSecurityLevelPermission(int type) {
		SettingConfig[] set = getSecuritySet(type);
		SettingConfig applicationSecurity = set[SET_INDEX_APPLICATION];
		SettingConfig onlineApplicationSecurity = set[SET_INDEX_ONLINE_APPLICATION];
		SettingConfig communicatorSecurity = set[SET_INDEX_COMMUNICATOR];
		SettingConfig purchaseSecurity = set[SET_INDEX_PURCHASE];
		SettingConfig internetSecurity = set[SET_INDEX_INTERNET];
		SettingConfig othersSecurity = set[SET_INDEX_OTHERS];
		SettingConfig badwordSecurity = set[SET_INDEX_BADWORD];
		
		Permissions permissions = new Permissions();
		permissions.setApps(applicationSecurity);
		permissions.setGame(applicationSecurity);
		permissions.setMovie(applicationSecurity);
		permissions.setMusic(applicationSecurity);
		permissions.setEbook(applicationSecurity);
		permissions.setPicture(applicationSecurity);
		permissions.setBrowser(onlineApplicationSecurity);
		permissions.setYoutube(onlineApplicationSecurity);
		permissions.setCommunicator(communicatorSecurity);
		permissions.setPurchase(purchaseSecurity);
		permissions.setInapp(othersSecurity);
		permissions.setGoogleplay(othersSecurity);
		permissions.setSecuritylevel(internetSecurity);
		permissions.setOsgdbadword(badwordSecurity);

		return permissions;
	}

	public List<Permission> generateOfflinePermissionList(int type) {
		SettingConfig[] set = getSecuritySet(type);
		SettingConfig applicationSecurity = set[SET_INDEX_APPLICATION];
		SettingConfig onlineApplicationSecurity = set[SET_INDEX_ONLINE_APPLICATION];
		SettingConfig communicatorSecurity = set[SET_INDEX_COMMUNICATOR];
		SettingConfig purchaseSecurity = set[SET_INDEX_PURCHASE];
		SettingConfig internetSecurity = set[SET_INDEX_INTERNET];
		SettingConfig othersSecurity = set[SET_INDEX_OTHERS];
		SettingConfig badwordSecurity = set[SET_INDEX_BADWORD];

		List<Permission> list = new ArrayList<Permission>();
		for (int i = PermissionNameIndex.APP; i <= PermissionNameIndex.PICTURE; i++) {
			list.add(i, applicationSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[i]));
		}
		list.add(PermissionNameIndex.BROWSER, onlineApplicationSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.BROWSER]));
		list.add(PermissionNameIndex.YOUTUBE, onlineApplicationSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.YOUTUBE]));
		// communicator
		list.add(PermissionNameIndex.COMMUNICATOR, communicatorSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.COMMUNICATOR]));
		// purchase
		list.add(PermissionNameIndex.PURCHASE, purchaseSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.PURCHASE]));
		// inapp
		list.add(PermissionNameIndex.INAPP, othersSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.INAPP]));
		// googleplay
		list.add(PermissionNameIndex.GOOGLEPALY, othersSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.GOOGLEPALY]));
		// internet security
		list.add(PermissionNameIndex.INTERNET_SECURITY_LEVEL, internetSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.INTERNET_SECURITY_LEVEL]));
		// osgdbadword
		list.add(PermissionNameIndex.OSGD_BADWORD, badwordSecurity.generateOfflinePermissionObject(PermissionNameIndex.LIST_NAME[PermissionNameIndex.OSGD_BADWORD]));

		return list;

	}
}
