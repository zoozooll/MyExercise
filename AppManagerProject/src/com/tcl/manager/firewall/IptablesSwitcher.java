/**
 * 
 */
package com.tcl.manager.firewall;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.util.Log;

import com.tcl.framework.log.NLog;
import com.tcl.manager.util.AdbCmdTool;
import com.tcl.mie.manager.R;

/**
 * @author zuokang.li
 *
 */
public class IptablesSwitcher {

	private static final String[] INIT_COMMANDS = {
			"dmesg -c >/dev/null",
			"iptables -L droidwall >/dev/null 2>/dev/null || iptables --new droidwall",
			"iptables -L droidwall-3g >/dev/null 2>/dev/null || iptables --new droidwall-3g",
			"iptables -L droidwall-wifi >/dev/null 2>/dev/null || iptables --new droidwall-wifi",
			"iptables -L droidwall-reject >/dev/null 2>/dev/null || iptables --new droidwall-reject",
			"iptables -L droidwall-accept >/dev/null 2>/dev/null || iptables --new droidwall-accept",
			"iptables -L droidwall-input-accept >/dev/null 2>/dev/null || iptables --new droidwall-input-accept",
			"iptables -L droidwall-input-drop >/dev/null 2>/dev/null || iptables --new droidwall-input-drop",
			"iptables -L droidwall-vpn >/dev/null 2>/dev/null || iptables --new droidwall-vpn",
			"iptables -L droidwall-lan >/dev/null 2>/dev/null || iptables --new droidwall-lan",
			"iptables -L droidwall-input >/dev/null 2>/dev/null || iptables --new droidwall-input",
			"iptables -F droidwall",
			"iptables -F droidwall-3g",
			"iptables -F droidwall-wifi",
			"iptables -F droidwall-reject",
			"iptables -F droidwall-vpn",
			"iptables -F droidwall-lan",
			"iptables -F droidwall-input",
			"iptables -F droidwall-input-drop",
			"iptables -F droidwall-accept",
			"iptables -F droidwall-input-accept",
			"iptables -A droidwall -m owner --uid-owner 0 -p udp --dport 53 -j RETURN",
			"iptables -A droidwall -m owner --uid-owner 1000 -p udp --dport 123 -j RETURN",
			"iptables -D INPUT -j droidwall-input",
			"iptables -I INPUT 1 -j droidwall-input",
			"iptables -D OUTPUT -j droidwall",
			"iptables -I OUTPUT 1 -j droidwall",
			"iptables -A droidwall-reject -j REJECT",
			"iptables -A droidwall -o rmnet+ -j droidwall-3g",
			"iptables -A droidwall -o pdp+ -j droidwall-3g",
			"iptables -A droidwall -o ppp+ -j droidwall-3g",
			"iptables -A droidwall -o uwbr+ -j droidwall-3g",
			"iptables -A droidwall -o wimax+ -j droidwall-3g",
			"iptables -A droidwall -o vsnet+ -j droidwall-3g",
			"iptables -A droidwall -o ccmni+ -j droidwall-3g",
			"iptables -A droidwall -o usb+ -j droidwall-3g",
			"iptables -A droidwall -o rmnet_sdio+ -j droidwall-3g",
			"iptables -A droidwall -o qmi+ -j droidwall-3g",
			"iptables -A droidwall -o wwan+ -j droidwall-3g",
			"iptables -A droidwall -o svnet+ -j droidwall-3g",
			"iptables -A droidwall -o cdma_rmnet+ -j droidwall-3g",
			"iptables -A droidwall -o rmnet_usb+ -j droidwall-3g ",
			"iptables -A droidwall -o bond+ -j droidwall-3g",
			"iptables -A droidwall -o clat+ -j droidwall-3g",
			"iptables -A droidwall -o cc2mni+ -j droidwall-3g",
			"iptables -A droidwall -o tiwlan+ -j droidwall-wifi",
			"iptables -A droidwall -o wlan+ -j droidwall-wifi",
			"iptables -A droidwall -o eth+ -j droidwall-wifi",
			"iptables -A droidwall -o ra+ -j droidwall-wifi" 
			};
	
	private static final String INIT_COMMAND_STR = "  dmesg -c >/dev/null    ||   iptables -L droidwall >/dev/null 2>/dev/null || iptables --new droidwall    ||   iptables -L droidwall-3g >/dev/null 2>/dev/null || iptables --new droidwall-3g    ||   iptables -L droidwall-wifi >/dev/null 2>/dev/null || iptables --new droidwall-wifi    ||   iptables -L droidwall-reject >/dev/null 2>/dev/null || iptables --new droidwall-reject    ||   iptables -L droidwall-accept >/dev/null 2>/dev/null || iptables --new droidwall-accept    ||   iptables -L droidwall-input-accept >/dev/null 2>/dev/null || iptables --new droidwall-input-accept    ||   iptables -L droidwall-input-drop >/dev/null 2>/dev/null || iptables --new droidwall-input-drop    ||   iptables -L droidwall-vpn >/dev/null 2>/dev/null || iptables --new droidwall-vpn    ||   iptables -L droidwall-lan >/dev/null 2>/dev/null || iptables --new droidwall-lan    ||   iptables -L droidwall-input >/dev/null 2>/dev/null || iptables --new droidwall-input    ||   iptables -F droidwall    ||   iptables -F droidwall-3g    ||   iptables -F droidwall-wifi    ||   iptables -F droidwall-reject    ||   iptables -F droidwall-vpn    ||   iptables -F droidwall-lan    ||   iptables -F droidwall-input    ||   iptables -F droidwall-input-drop    ||   iptables -F droidwall-accept    ||   iptables -F droidwall-input-accept    ||   iptables -A droidwall -m owner --uid-owner 0 -p udp --dport 53 -j RETURN    ||   iptables -A droidwall -m owner --uid-owner 1000 -p udp --dport 123 -j RETURN    ||   iptables -D INPUT -j droidwall-input    ||   iptables -I INPUT 1 -j droidwall-input    ||   iptables -D OUTPUT -j droidwall    ||   iptables -I OUTPUT 1 -j droidwall    ||   iptables -A droidwall-reject -j REJECT    ||   iptables -A droidwall -o rmnet+ -j droidwall-3g    ||   iptables -A droidwall -o pdp+ -j droidwall-3g    ||   iptables -A droidwall -o ppp+ -j droidwall-3g    ||   iptables -A droidwall -o uwbr+ -j droidwall-3g    ||   iptables -A droidwall -o wimax+ -j droidwall-3g    ||   iptables -A droidwall -o vsnet+ -j droidwall-3g    ||   iptables -A droidwall -o ccmni+ -j droidwall-3g    ||   iptables -A droidwall -o usb+ -j droidwall-3g    ||   iptables -A droidwall -o rmnet_sdio+ -j droidwall-3g    ||   iptables -A droidwall -o qmi+ -j droidwall-3g    ||   iptables -A droidwall -o wwan+ -j droidwall-3g    ||   iptables -A droidwall -o svnet+ -j droidwall-3g    ||   iptables -A droidwall -o cdma_rmnet+ -j droidwall-3g    ||   iptables -A droidwall -o rmnet_usb+ -j droidwall-3g     ||   iptables -A droidwall -o bond+ -j droidwall-3g    ||   iptables -A droidwall -o clat+ -j droidwall-3g    ||   iptables -A droidwall -o cc2mni+ -j droidwall-3g    ||   iptables -A droidwall -o tiwlan+ -j droidwall-wifi    ||   iptables -A droidwall -o wlan+ -j droidwall-wifi    ||   iptables -A droidwall -o eth+ -j droidwall-wifi    ||   iptables -A droidwall -o ra+ -j droidwall-wifi ";
	
	@Deprecated
	private static final String[] INIT_COMMANDS2 = {
		"dmesg -c > /dev/null",
		"iptables -L droidwall > /dev/null 2> /dev/null || iptables --new droidwall",
		"iptables -L droidwall-3g > /dev/null 2> /dev/null || iptables --new droidwall-3g",
		"iptables -L droidwall-wifi > /dev/null 2> /dev/null || iptables --new droidwall-wifi",
		"iptables -L droidwall-reject > /dev/null 2> /dev/null || iptables --new droidwall-reject",
		"iptables -L droidwall-accept > /dev/null 2> /dev/null || iptables --new droidwall-accept",
		"iptables -L droidwall-input-accept > /dev/null 2> /dev/null || iptables --new droidwall-input-accept",
		"iptables -L droidwall-input-drop > /dev/null 2> /dev/null || iptables --new droidwall-input-drop",
		"iptables -L droidwall-vpn > /dev/null 2> /dev/null || iptables --new droidwall-vpn",
		"iptables -L droidwall-lan > /dev/null 2> /dev/null || iptables --new droidwall-lan",
		"iptables -L droidwall-input > /dev/null 2> /dev/null || iptables --new droidwall-input",
		"iptables -F droidwall",
		"iptables -F droidwall-3g",
		"iptables -F droidwall-wifi",
		"iptables -F droidwall-reject",
		"iptables -F droidwall-vpn",
		"iptables -F droidwall-lan",
		"iptables -F droidwall-input",
		"iptables -F droidwall-input-drop",
		"iptables -F droidwall-accept",
		"iptables -F droidwall-input-accept",
		"iptables -A droidwall -m owner --uid-owner 0 -p udp --dport 53 -j RETURN",
		"iptables -A droidwall -m owner --uid-owner 1000 -p udp --dport 123 -j RETURN",
		"iptables -D INPUT -j droidwall-input",
		"iptables -I INPUT 1 -j droidwall-input",
		"iptables -D OUTPUT -j droidwall",
		"iptables -I OUTPUT 1 -j droidwall",
		"iptables -A droidwall-reject -j REJECT",
		"iptables -A droidwall -o rmnet+ -j droidwall-3g",
		"iptables -A droidwall -o pdp+ -j droidwall-3g",
		"iptables -A droidwall -o ppp+ -j droidwall-3g",
		"iptables -A droidwall -o uwbr+ -j droidwall-3g",
		"iptables -A droidwall -o wimax+ -j droidwall-3g",
		"iptables -A droidwall -o vsnet+ -j droidwall-3g",
		"iptables -A droidwall -o ccmni+ -j droidwall-3g",
		"iptables -A droidwall -o usb+ -j droidwall-3g",
		"iptables -A droidwall -o rmnet_sdio+ -j droidwall-3g",
		"iptables -A droidwall -o qmi+ -j droidwall-3g",
		"iptables -A droidwall -o wwan+ -j droidwall-3g",
		"iptables -A droidwall -o svnet+ -j droidwall-3g",
		"iptables -A droidwall -o cdma_rmnet+ -j droidwall-3g",
		"iptables -A droidwall -o rmnet_usb+ -j droidwall-3g ",
		"iptables -A droidwall -o bond+ -j droidwall-3g",
		"iptables -A droidwall -o clat+ -j droidwall-3g",
		"iptables -A droidwall -o cc2mni+ -j droidwall-3g",
		"iptables -A droidwall -o tiwlan+ -j droidwall-wifi",
		"iptables -A droidwall -o wlan+ -j droidwall-wifi",
		"iptables -A droidwall -o eth+ -j droidwall-wifi",
		"iptables -A droidwall -o ra+ -j droidwall-wifi" 
		};
	
	private static final String COMMAND_FORMAT = "iptables -I %s -m owner --uid-owner %d -j %s";
	private static final String COMMAND_CATEGORY_WIFI = "droidwall-wifi";
	private static final String COMMAND_CATEGORY_MOBILEDATA = "droidwall-3g";
	private static final String COMMAND_CONTROL_FORBID = "droidwall-reject";
	private static final String COMMAND_CONTROL_ACCEPT = "RETURN";
	
	public static final int FIREWALL_NETWORK_CATEGORY_MOBILEDATA = 1;
	public static final int FIREWALL_NETWORK_CATEGORY_WIFI = 2;
	public static final int FIREWALL_NETWORK_CATEGORY_ALL = -1;
	
	private static class ClassHolder {
		private static final IptablesSwitcher INSTANCE = new IptablesSwitcher();
	}
	
	public static IptablesSwitcher getSingleInstance() {
		return ClassHolder.INSTANCE;
	}
	
	private IptablesSwitcher() {
		
	}
	
	/**Add firewall chains to iptables lib */
	public void enableFirewall(Context ctx) {
		assertBinaries(ctx, false);
		
/*		for (String cmd : INIT_COMMANDS) {
//			Log.i("CMD", cmd);
		}*/
		AdbCmdTool.adbCmd(INIT_COMMAND_STR);
	}
	
	@Deprecated
	public void enableFirewall2(Context ctx) {
		assertBinaries(ctx, false);
		
		for (String cmd : INIT_COMMANDS2) {
			AdbCmdTool.adbCmd(cmd);
			NLog.i("CMD", cmd);
		}
	}
	
	/**
	 * Previous some applications to connect to mobile data network
	 * @param uids
	 * @param category
	 */
	public boolean forbidAppsMobileData(int... uids) {
		if (uids == null) {
			return true;
		}
		boolean success = true;
		for (int uid : uids) {
			if (uid == -1) {
				continue;
			}
			String cmd = String.format(COMMAND_FORMAT,
					COMMAND_CATEGORY_MOBILEDATA, uid,
					COMMAND_CONTROL_FORBID);
			NLog.i("CMD", cmd);
			String result = AdbCmdTool.adbCmd(cmd);
			if (!com.tcl.manager.util.AdbCmdTool.SUCCESS.equals(result)) {
				success = false;
			}
		}
		return success;
	}
	
	/**
	 * Accept or reopen some applications to connect to mobile data network
	 * @param uids
	 * @param category
	 */
	public boolean acceptAppsMobileData(int... uids) {
		if (uids == null) {
			return true;
		}
		boolean success = true;
		for (int uid : uids) {
			if (uid == -1) {
				continue;
			}
			String cmd = String.format(COMMAND_FORMAT,
					COMMAND_CATEGORY_MOBILEDATA, uid,
					COMMAND_CONTROL_ACCEPT);
			String result = AdbCmdTool.adbCmd(cmd);
			NLog.i("CMD", cmd);
			if (!com.tcl.manager.util.AdbCmdTool.SUCCESS.equals(result)) {
				success = false;
			}
		}
		return success;
	}
	
	/**
	 * Previous some applications to connect to wifi network
	 * @param uids
	 * @param category
	 */
	public boolean forbidAppsWifi(int... uids) {
		if (uids == null) {
			return true;
		}
		boolean success = true;
		for (int uid : uids) {
			if (uid == -1) {
				continue;
			}
			String cmd = String.format(COMMAND_FORMAT,
					COMMAND_CATEGORY_WIFI, uid,
					COMMAND_CONTROL_FORBID);
			NLog.i("CMD", cmd);
			String result = AdbCmdTool.adbCmd(cmd);
			if (!com.tcl.manager.util.AdbCmdTool.SUCCESS.equals(result)) {
				success = false;
			}
		}
		return success;
	}
	
	/**
	 * Accept or reopen some applications to connect to wifi network
	 * @param uids
	 * @param category
	 */
	public boolean acceptAppsWifi(int... uids) {
		if (uids == null) {
			return true;
		}
		
		boolean success = true;
		for (int uid : uids) {
			if (uid == -1) {
				continue;
			}
			String cmd = String.format(COMMAND_FORMAT,
					COMMAND_CATEGORY_WIFI, uid,
					COMMAND_CONTROL_ACCEPT);
			String result = AdbCmdTool.adbCmd(cmd);
			if (!com.tcl.manager.util.AdbCmdTool.SUCCESS.equals(result)) {
				success = false;
			}
		}
		return success;
	}
	
	/**
	 * Asserts that the binary files are installed in the cache directory.
	 * 
	 * @param ctx
	 *            context
	 * @param showErrors
	 *            indicates if errors should be alerted
	 * @return false if the binary files could not be installed
	 */
	private boolean assertBinaries(Context ctx, boolean showErrors) {
		boolean changed = false;
		String arch = System.getProperty("os.arch");
		try {
			// Check iptables_armv5
			File file = new File(ctx.getDir("bin", 0), "iptables_armv5");
			if (!file.exists() || file.length() != 198652) {
				copyRawFile(ctx, R.raw.iptables_armv5, file, "755");
				changed = true;
			}
			if (arch.equals("i686")) {
				// Check busybox for x86
				file = new File(ctx.getDir("bin", 0), "busybox_x86v2");
				if (!file.exists()) {
					copyRawFile(ctx, R.raw.busybox_x86v2, file, "755");
					changed = true;
				}
			} else {
				// Check busybox for ARM
				file = new File(ctx.getDir("bin", 0), "busybox_g1");
				if (!file.exists()) {
					copyRawFile(ctx, R.raw.busybox_g1, file, "755");
					changed = true;
				}
			}
			// check nflog
			if (arch.equals("i686")) {
				file = new File(ctx.getDir("bin", 0), "nflog_x86");
				if (!file.exists()) {
					copyRawFile(ctx, R.raw.nflog_x86, file, "755");
					changed = true;
				}
			} else {
				file = new File(ctx.getDir("bin", 0), "nflogv2");
				if (!file.exists()) {
					copyRawFile(ctx, R.raw.nflogv2, file, "755");
					changed = true;
				}
			}
		} catch (Exception e) {
			NLog.e("IPTABLES", e);
			return false;
		}
		return true;
	}
	
	/**
	 * Copies a raw resource file, given its ID to the given location
	 * 
	 * @param ctx
	 *            context
	 * @param resid
	 *            resource id
	 * @param file
	 *            destination file
	 * @param mode
	 *            file permissions (E.g.: "755")
	 * @throws IOException
	 *             on error
	 * @throws InterruptedException
	 *             when interrupted
	 */
	private void copyRawFile(Context ctx, int resid, File file,
			String mode) throws IOException, InterruptedException {
		final String abspath = file.getAbsolutePath();
		// Write the iptables binary
		final FileOutputStream out = new FileOutputStream(file);
		final InputStream is = ctx.getResources().openRawResource(resid);
		byte buf[] = new byte[1024];
		int len;
		while ((len = is.read(buf)) > 0) {
			out.write(buf, 0, len);
		}
		out.close();
		is.close();
		// Change the permissions
		Runtime.getRuntime().exec("chmod " + mode + " " + abspath).waitFor();
	}
}
