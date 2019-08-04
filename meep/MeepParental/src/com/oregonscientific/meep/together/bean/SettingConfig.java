package com.oregonscientific.meep.together.bean;

import com.oregonscientific.meep.message.common.ApplicationAccess.Access;
import com.oregonscientific.meep.permission.Component;
import com.oregonscientific.meep.permission.Permission;
import com.oregonscientific.meep.permission.Permission.AccessLevels;
import com.oregonscientific.meep.together.activity.Consts;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.activity.Utils;

public class SettingConfig {
	public static String ACCESS_ALLOW = "allow";
	public static String ACCESS_DENY = "deny";
	public static String ACCESS_APPROVAL = "approval";
	public static String ACCESS_MEDIUM = "medium";
	public static String ACCESS_HIGH = "high";
	private String access;
	private int timelimit;

	public SettingConfig() {
		super();
	}

	public SettingConfig(String access, int timelimit) {
		this.access = access;
		this.timelimit = timelimit;
	}

	public String getAccess() {
		return access;
	}

	public void setAccess(String access) {
		this.access = access;
	}

	public int getTimelimit() {
		return timelimit;
	}

	public void setTimelimit(int timelimit) {
		this.timelimit = timelimit;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof SettingConfig) {
			SettingConfig setting = (SettingConfig)o;
			if (setting.getAccess().equals(this.getAccess())
					&& setting.getTimelimit() == this.getTimelimit()) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	/**
	 * get a SettingConfig object according to permission data from MeepHome
	 * 
	 * @param permission
	 * @return
	 */
	public static SettingConfig getSettingConfigByPermission(Permission permission) {
		String canAccess = null;
		if (permission.getComponent().getDisplayName().equals(Consts.SECURITYLEVEL_DISPLAY_NAME)) {
			canAccess = permission.getAccessLevel().equals(AccessLevels.HIGH) ? SettingConfig.ACCESS_HIGH
					: SettingConfig.ACCESS_MEDIUM;
		} else if (permission.getComponent().getDisplayName().equals(Consts.PURCHASE_DISPLAY_NAME)) {
			if (permission.getAccessLevel().equals(AccessLevels.ALLOW)) {
				canAccess = SettingConfig.ACCESS_ALLOW;
			} else if (permission.getAccessLevel().equals(AccessLevels.APPROVAL)) {
				canAccess = SettingConfig.ACCESS_APPROVAL;
			} else if (permission.getAccessLevel().equals(AccessLevels.DENY)) {
				canAccess = SettingConfig.ACCESS_DENY;
			}

		} else {
			canAccess = permission.getCanAccess() ? SettingConfig.ACCESS_ALLOW
					: SettingConfig.ACCESS_DENY;
		}
		long timelimit = permission.getTimeLimit() / Consts.ONE_MINUTE;
		Utils.printLogcatDebugMessage("timelimit" + timelimit);
		return new SettingConfig(canAccess, (int) timelimit);
	}
	
	public Permission generateOfflinePermissionObject(String appid)
	{
		AccessLevels canAccess = null;
		if (appid.equals(Consts.SECURITYLEVEL_DISPLAY_NAME)) {
			canAccess = getAccess().equals(SettingConfig.ACCESS_HIGH)? AccessLevels.HIGH
					: AccessLevels.MEDIUM;
		} else if (appid.equals(Consts.PURCHASE_DISPLAY_NAME)) {
			if (getAccess().equals(SettingConfig.ACCESS_ALLOW)) {
				canAccess = AccessLevels.ALLOW;
			} else if (getAccess().equals(SettingConfig.ACCESS_APPROVAL)) {
				canAccess = AccessLevels.APPROVAL;
			} else if (getAccess().equals(SettingConfig.ACCESS_DENY)) {
				canAccess = AccessLevels.DENY;
			}

		} else {
			canAccess = getAccess().equals(SettingConfig.ACCESS_ALLOW)? AccessLevels.ALLOW
					: AccessLevels.DENY;
		}
		long timelimit = getTimelimit() * Consts.ONE_MINUTE;
		Utils.printLogcatDebugMessage("timelimit" + timelimit);
		Permission permission = new Permission(canAccess, timelimit);
		permission.setId(UserFunction.getAppId(appid));
		permission.setComponent(UserFunction.getAppComponent(appid));
		return permission;
	}
//	public Permission generateOfflinePermissionObject(Permission permission)
//	{
//		String appid = permission.getComponent().getDisplayName();
//		AccessLevels canAccess = null;
//		if (appid.equals(Consts.SECURITYLEVEL_DISPLAY_NAME)) {
//			canAccess = getAccess().equals(SettingConfig.ACCESS_HIGH)? AccessLevels.HIGH
//					: AccessLevels.MEDIUM;
//		} else if (appid.equals(Consts.PURCHASE_DISPLAY_NAME)) {
//			if (getAccess().equals(SettingConfig.ACCESS_ALLOW)) {
//				canAccess = AccessLevels.ALLOW;
//			} else if (getAccess().equals(SettingConfig.ACCESS_APPROVAL)) {
//				canAccess = AccessLevels.APPROVAL;
//			} else if (getAccess().equals(SettingConfig.ACCESS_DENY)) {
//				canAccess = AccessLevels.DENY;
//			}
//			
//		} else {
//			canAccess = getAccess().equals(SettingConfig.ACCESS_ALLOW)? AccessLevels.ALLOW
//					: AccessLevels.DENY;
//		}
//		long timelimit = getTimelimit() * Consts.ONE_MINUTE;
//		Utils.printLogcatDebugMessage("timelimit" + timelimit);
//		permission.setAccessLevel(canAccess);
//		permission.setTimeLimit(timelimit);
//		permission.setId(UserFunction.getAppId(appid));
//		permission.setComponent(UserFunction.getAppComponent(appid));
//		return permission;
//	}
}
