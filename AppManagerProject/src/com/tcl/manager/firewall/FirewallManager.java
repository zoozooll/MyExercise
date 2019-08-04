/**
 * 
 */
package com.tcl.manager.firewall;

import java.util.Map;
import java.util.Set;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.tcl.framework.log.NLog;
import com.tcl.manager.application.ManagerApplication;
import com.tcl.manager.util.PkgManagerTool;

/**
 * @author zuokang.li
 *
 */
public class FirewallManager {
	
	/*Fields link to business */
	private IptablesSwitcher mFirewall;
	private SharedPreferences sp;
	
	private PackageManager mpm;
	
	private static final String FIREWALL_BLOCK = "firewall_block";
	
	private static boolean isInitialized = false;
	
	private static class ClassHolder {
		private static final FirewallManager INSTANCE = new FirewallManager();
	}
	
	public static FirewallManager getSingleInstance() {
		return ClassHolder.INSTANCE;
	}
	
	private FirewallManager(){
//		sp = PreferenceManager.getDefaultSharedPreferences(ManagerApplication.sApplication);
		sp = ManagerApplication.sApplication.getSharedPreferences(FIREWALL_BLOCK, Context.MODE_PRIVATE);
		mpm = ManagerApplication.sApplication.getPackageManager();
	}
	
	public void initFirewall() {
		if (!isInitialized) {
			setInitializedState();
			
			new Thread("init firewall"){
				public void run() {
					NLog.d(FIREWALL_BLOCK, "initFirewall");
					mFirewall = IptablesSwitcher.getSingleInstance();
					mFirewall.enableFirewall(ManagerApplication.sApplication);
					Map<String, ?> map = getAllBlockPackages();
					if (map != null) {
						for (Map.Entry<String, ?> entry : map.entrySet()) {
							if (Boolean.TRUE.equals(entry.getValue())) {
								setBlockPackages(entry.getKey());
							}
						}
					}
				}
				
			}.start();
		}
	}
	
	@Deprecated
	public void initFirewall2() {
		new Thread("init firewall"){
			
			public void run() {
				NLog.d(FIREWALL_BLOCK, "initFirewall");
				mFirewall = IptablesSwitcher.getSingleInstance();
				mFirewall.enableFirewall2(ManagerApplication.sApplication);
				Map<String, ?> map = getAllBlockPackages();
				if (map != null) {
					for (Map.Entry<String, ?> entry : map.entrySet()) {
						if (Boolean.TRUE.equals(entry.getValue())) {
							setBlockPackages(entry.getKey());
						}
					}
				}
			}
			
		}.start();
	}
	
	/**
	*
	*/
	public Map<String, ?> getAllBlockPackages() {
		Map<String, ?> map = sp.getAll();
		return map;
	}
	
	public Set<String> getBlockPkgSet() {
		Map<String, ?> map = sp.getAll();
		if (map != null) {
			return map.keySet();
		}
		return null;
	}
	
	public synchronized void setBlockPackages(String... pkgNames) {
		if (pkgNames == null) {
			return;
		}
		
		Editor edit = sp.edit();
		for (String string : pkgNames) {
			// Part time. Whether it is run success or not ,it will change the status.
			mFirewall.forbidAppsMobileData(PkgManagerTool.getUid(ManagerApplication.sApplication ,string));
				edit.putBoolean(string, true);
			
		}
		edit.apply();
		
	}
	
	/**
	*/
	public synchronized void unBlockPackages(String... pkgNames) {
		if (pkgNames == null) {
			return;
		}
		Editor edit = sp.edit();
		for (String string : pkgNames) {
			// Part time. Whether it is run success or not ,it will change the status.
			mFirewall.acceptAppsMobileData(PkgManagerTool.getUid(ManagerApplication.sApplication ,string));
			edit.remove(string);
			
		}
		edit.commit();
	}
	
	public synchronized void remove(String... pkgNames) {
		if (pkgNames == null) {
			return;
		}
		Editor edit = sp.edit();
		for (String string : pkgNames) {
			// Part time. Whether it is run success or not ,it will change the status.
			mFirewall.acceptAppsMobileData(PkgManagerTool.getUid(ManagerApplication.sApplication ,string));
			edit.remove(string);
	
		}
		edit.commit();
	}
	
	public synchronized void clearAll() {
		Map<String, ?> map = getAllBlockPackages();
		if (map != null) {
			for (Map.Entry<String, ?> entry : map.entrySet()) {
				if (Boolean.TRUE.equals(entry.getValue())) {
					unBlockPackages(entry.getKey());
				}
			}
		}
	}
	
	
	public boolean checkBlock(String pkgName) {
		return sp.getBoolean(pkgName, false);
	}
	
	private static void setInitializedState() {
		isInitialized = true;
	}
	
	
}
