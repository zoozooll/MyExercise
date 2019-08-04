/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.meepopenbox.view;

import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;

/**
 * A class representing a Wi-Fi network
 */
public class WifiNetwork {
	
	/**
   * The network's SSID. Can either be an ASCII string,
   * which must be enclosed in double quotation marks
   * (e.g., {@code "MyNetwork"}, or a string of
   * hex digits,which are not enclosed in quotes
   * (e.g., {@code 01a243f405}).
   */
  public String SSID;
  
  /**
   * When set, this network configuration entry should only be used when
   * associating with the AP having the specified BSSID. The value is
   * a string in the format of an Ethernet MAC address, e.g.,
   * <code>XX:XX:XX:XX:XX:XX</code> where each <code>X</code> is a hex digit.
   */
  public String BSSID;
  
  /**
   * Describes the authentication, key management, and encryption schemes
   * supported by the access point.
   */
  public String capabilities;
  
  /**
   * The detected signal level in dBm. At least those are the units used by
   * the TI driver.
   */
  public int level;
  
  /**
   * The ID number that the supplicant uses to identify this
   * network configuration entry. This must be passed as an argument
   * to most calls into the supplicant.
   */
  public int networkId;
  
  /**
   * The detailed state of the Wi-Fi network
   */
  public State state = State.UNKNOWN;
  
  /** Possible status of a Wi-Fi network */
  public enum State {
  	/** Ready to start data connection setup. */
    IDLE,
    /** Searching for an available access point. */
    SCANNING,
    /** Currently setting up data connection. */
    CONNECTING,
    /** Network link established, performing authentication. */
    AUTHENTICATING,
    /** Awaiting response from DHCP server in order to assign IP address information. */
    OBTAINING_IPADDR,
    /** IP traffic should be available. */
    CONNECTED,
    /** IP traffic is suspended */
    SUSPENDED,
    /** Currently tearing down data connection. */
    DISCONNECTING,
    /** IP traffic not available. */
    DISCONNECTED,
    /** Attempt to connect failed. */
    FAILED,
    /** Access to this network is blocked. */
    BLOCKED,
    /** Link has poor connectivity. */
    VERIFYING_POOR_LINK,
    /** Checking if network is a captive portal */
    CAPTIVE_PORTAL_CHECK,
    /** Configured and is available for association */
    SAVED,
    /** Disabled and supplicant will not attempt to connect to this network */
    DISABLED,
    /** An unknown state */
    UNKNOWN;
    
    /**
     * Converts a detailed network state to the corresponding wi-fi network state
     * 
     * @param detailedState The detailed state of the network
     * @return The network status enum
     */
    public static WifiNetwork.State fromNetworkDetailedState(NetworkInfo.DetailedState detailedState) {
			switch (detailedState) {
			case IDLE:
				return IDLE;
			case SCANNING:
				return SCANNING;
			case CONNECTING:
				return CONNECTING;
			case AUTHENTICATING:
				return AUTHENTICATING;
			case OBTAINING_IPADDR:
				return OBTAINING_IPADDR;
			case CONNECTED:
				return CONNECTED;
			case SUSPENDED:
				return SUSPENDED;
			case DISCONNECTING:
				return DISCONNECTING;
			case DISCONNECTED:
				return DISCONNECTED;
			case FAILED:
				return FAILED;
			case BLOCKED:
				return BLOCKED;
//			case VERIFYING_POOR_LINK:
//				return VERIFYING_POOR_LINK;
//			case CAPTIVE_PORTAL_CHECK:
//				return CAPTIVE_PORTAL_CHECK;
			}
			return UNKNOWN;
    }
    
    /**
     * Converts a wi-fi configuration status to the corresponding wi-fi network status
     * 
     * @param status The WifiConfiguration.Status
     * @return The network status enum
     */
    public static State fromWifiConfigurationStatus(WifiConfiguration.Status status) {
    	if (status != null) {
    		if (status.equals(WifiConfiguration.Status.DISABLED)) {
      		return DISABLED;
      	} else if (status.equals(WifiConfiguration.Status.CURRENT)) {
      		return CONNECTED;
      	} else {
      		return IDLE;
      	}
    	}
    	return UNKNOWN;
    }
    
    /**
     * Converts a integer representation of wi-fi configuration status to the corresponding 
     * wi-fi network status
     * 
     * @param status Integer representation of the WifiConfiguration.Status
     * @return The network status enum
     */
    public static State fromWifiConfigurationStatus(int status) {
			switch (status) {
			case WifiConfiguration.Status.DISABLED:
				return DISABLED;
			case WifiConfiguration.Status.CURRENT:
				return CONNECTED;
			case WifiConfiguration.Status.ENABLED:
				return IDLE;
			default:
				return UNKNOWN;
			}
    }
  }
  
  /**
   * Construct a WifiNetwork from a scanned network
   * 
   * @param scanResult the scan result used to construct the config entry
   */
  public WifiNetwork(ScanResult network) {
  	networkId = -1;
    SSID = network.SSID;
    BSSID = network.BSSID;
    level = network.level;
    capabilities = network.capabilities;
  }
  
  /**
	 * Removes starting and ending quotes of a string if any
	 * @param string string to be quotes removed
	 * @return string without starting and ending quotes
	 */
	private String removeQuotes(String string) {
		if (string == null) {
			return "";
		}
		
		if (string.startsWith("\"")) {
			string = string.substring(1, string.length());
		}
		if (string.endsWith("\"")) {
			string = string.substring(0, string.length() - 1);
		}
		return string;
	}
  
  @Override
  public boolean equals(Object other) {
  	if (other == null || other.getClass() != getClass()) {
			return false;
		}
  	
  	WifiNetwork otherWifi = (WifiNetwork) other;
  	if (SSID == null) {
  		return (otherWifi.SSID == null);
  	} else {
  		return (removeQuotes(SSID).equals(removeQuotes(otherWifi.SSID)));
  	}
  }

}
