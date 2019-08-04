/**
 * Copyright (C) 2013 IDT International Ltd.
 */
package com.oregonscientific.meep.meepopenbox.view;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.MeepOpenBoxWiFiSetup.WifiSecurity;
import com.oregonscientific.meep.meepopenbox.R;

/**
 * The adapter that maps Wifi networks in a list
 * 
 * @author Stanley Lam
 */
public class WifiNetworkAdapter extends ArrayAdapter<WifiNetwork> {
	
	private final String TAG = WifiNetworkAdapter.class.getSimpleName();
	
	private LayoutInflater mInflater;
	
	/**
   * Constructor
   *
   * @param context The current context.
   * @param listViewResourceId The resource ID for a layout file containing a ListView to use when
   *                 instantiating views.
   * @param objects The objects to represent in the ListView.
   */
	public WifiNetworkAdapter(Context context, int listViewResourceId, List<WifiNetwork> objects) {
		super(context, listViewResourceId, objects);
		init(context, listViewResourceId, objects);
	}

	private void init(Context context, int listViewResourceId, List<WifiNetwork> objects) {
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		setNotifyOnChange(false);
	}
	
	/**
   * Adds the scan result at the end of the array if the given result
   * does not already exist.
   *
   * @param result The scan result to add at the end of the array
   */
	public void add(ScanResult result) {
		WifiNetwork wifi = new WifiNetwork(result);
		add(wifi);
	}
	
	/**
   * Adds the Wi-Fi network at the end of the array if the given network
   * does not already exist.
   *
   * @param result The Wi-Fi network to add at the end of the array
   */
	public void add(WifiNetwork wifi) {
		if (!contains(wifi)) {
			super.add(wifi);
		} else {
			int position = getPosition(wifi.SSID);
			WifiNetwork object = getItem(position);
			if (object != null) {
				object.state = wifi.state;
			}
		}
	}
	
	/**
   * Adds the specified Collection at the end of the array.
   *
   * @param collection The Collection to add at the end of the array.
   */
	@Override
	public void addAll(Collection<? extends WifiNetwork> collection) {
		if (collection != null) {
			for (WifiNetwork wifi : collection) {
				add(wifi);
			}
		}
	}
	
	 /**
   * Adds the specified items at the end of the array.
   *
   * @param items The items to add at the end of the array.
   */
  public void addAll(WifiNetwork ... items) {
  	if (items != null) {
  		for (WifiNetwork wifi : items) {
  			add(wifi);
  		}
  	}
  }
	
	/**
	 * Test whether the adapter contains the specified scan result
	 * 
	 * @param result The scan result to search for
	 * @return true if the scan result an a wifi network in this adapter, false otherwise
	 */
	public boolean contains(ScanResult result) {
		WifiNetwork wifi = new WifiNetwork(result);
		return contains(wifi);
	}
	
	/**
	 * Test whether the adapter contains the specified Wi-Fi network
	 * 
	 * @param wifi The Wi-Fi network to search for
	 * @return true if the Wi-Fi network an a wifi network in this adapter, false otherwise
	 */
	public boolean contains(WifiNetwork wifi) {
		int position = getPosition(wifi);
		return (position != AdapterView.INVALID_POSITION);
	}
	
	/**
   * {@inheritDoc}
   */
  public WifiNetwork getItem(int position) {
  	WifiNetwork result = null;
  	if (position != AdapterView.INVALID_POSITION) {
  		result = super.getItem(position);
  	}
  	return result;
  }
	
	/**
	 * Returns the position of the item with the given SSID
	 * 
	 * @param SSID The SSID of the Wifi network
	 * @return The position of the item with the given SSID
	 */
	public int getPosition(String SSID) {
		if (SSID != null) {
			for (int i = 0; i < getCount(); i++) {
				WifiNetwork wifi = getItem(i);
				if (removeQuotes(wifi.SSID).equals(removeQuotes(SSID))) {
					return i;
				}
			}
		}
		
		return AdapterView.INVALID_POSITION;
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
	
	/**
   * Sorts the content of this adapter.
   */
	public void sort() {
		super.sort(new Comparator<WifiNetwork>() {

			@Override
			public int compare(WifiNetwork lhs, WifiNetwork rhs) {
				
				if (isNetworkConfigured(rhs) && !isNetworkConfigured(lhs)) {
					return 1;
				} else if (isNetworkConfigured(lhs) && !isNetworkConfigured(rhs)) {
					return -1;
				} else if (isNetworkConfigured(lhs) && isNetworkConfigured(rhs)) {
					if (isNetworkConnected(lhs)) {
						return -1;
					} else if (isNetworkConnected(rhs)) {
						return 1;
					} else {
						Integer rhsLevel = rhs.level;
						return rhsLevel.compareTo(lhs.level);
					}
				} else {
					Integer rhsLevel = rhs.level;
					return rhsLevel.compareTo(lhs.level);
				}
			}
			
		});
	}
	
	private boolean isNetworkConfigured(WifiNetwork wifi) {
		WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		
		boolean result = false;
		for (WifiConfiguration wifiConfig : configs) {
			if (wifi != null && removeQuotes(wifiConfig.SSID).equals(removeQuotes(wifi.SSID))) {
				result = true;
				break;
			}
		}
		return result;
	}
	
	private boolean isNetworkConnected(WifiNetwork wifi) {
		WifiManager wifiManager = (WifiManager) getContext().getSystemService(Context.WIFI_SERVICE);
		boolean result = false;
		
		WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		if (wifiInfo != null && wifi != null) {
			if (removeQuotes(wifiInfo.getSSID()).equals(removeQuotes(wifi.SSID))) {
				return true;
			}
		}
		
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration wifiConfig : configs) {
			if (wifi != null && removeQuotes(wifiConfig.SSID).equals(removeQuotes(wifi.SSID))) {
				result = wifiConfig.status == WifiConfiguration.Status.CURRENT;
				break;
			}
		}
		return result;
	}
	
	/**
   * {@inheritDoc}
   */
  public View getView(int position, View convertView, ViewGroup parent) {
  	int resource = R.layout.open_box_wifi_list_view;
  	return createViewFromResource(position, convertView, parent, resource);
  }
  
  private View createViewFromResource(int position, View convertView, ViewGroup parent, int resource) {
  	View view;
    
		if (convertView == null) {
			view = mInflater.inflate(resource, parent, false);
		} else {
			view = convertView;
		}
		
		WifiNetwork item = getItem(position);
		
		TextView text = (TextView) view.findViewById(R.id.name);
		if (text != null) {
			if (item != null) {
				text.setText(item.SSID);
			} else {
				text.setText(null);
			}
		}
		
		int selectedItemPosition = AdapterView.INVALID_POSITION;
		if (parent instanceof ListView) {
			ListView listView = (ListView) parent;
			selectedItemPosition = listView.getSelectedItemPosition();
		}
		setSelected(position, view, selectedItemPosition == position);
		setStatus(position, view);
    
    return view;
  }
  
  private void setSelected(int position, View view, boolean selected) {
		// Quick return if there is nothing to process
  	if (view == null) {
  		return;
  	}
  	
  	TextView text = (TextView) view.findViewById(R.id.name);
  	if (selected) {
  		view.setBackgroundResource(R.drawable.openbox_popupwifi_hl);
  		if (text != null) {
  			text.setTextColor(getContext().getResources().getColor(R.color.brown));
  		}
  	} else {
  		view.setBackgroundColor(Color.TRANSPARENT);
  		if (text != null) {
  			text.setTextColor(getContext().getResources().getColor(R.color.white));
  		}
  	}
  }
  
  private void setStatus(int position, View view) {
		// Quick return if there is nothing to process
  	if (view == null) {
  		return;
  	}
  	
  	TextView text = (TextView) view.findViewById(R.id.status);
  	if (text == null) {
  		return;
  	}	
  	
  	WifiNetwork wifi = getItem(position);
  	if (wifi.state != null) {
  		switch (wifi.state) {
  		case CONNECTED:
  			text.setText(getContext().getResources().getString(R.string.connected));
  			break;
  		case SCANNING:
    	case CONNECTING:
    		text.setText(getContext().getResources().getString(R.string.connecting));
    		break;
    	case AUTHENTICATING:
    		text.setText(getContext().getResources().getString(R.string.authenticating));
    		break;
    	case OBTAINING_IPADDR:
    		text.setText(getContext().getResources().getString(R.string.obtaining_ipaddr));
    		break;
    	case BLOCKED:
    		text.setText(getContext().getResources().getString(R.string.blocked));
    		break;
    	case FAILED:
    		text.setText(getContext().getResources().getString(R.string.authentication_problem));
    		break;
    	case SAVED:
    		text.setText(getContext().getResources().getString(R.string.saved));
    		break;
    	case DISABLED:
    		text.setText(getContext().getResources().getString(R.string.disabled));
    		break;
  		default:
  			text.setText(null);
  			break;
    	}
  		
  		ImageView statusIcon = (ImageView) view.findViewById(R.id.status_icon);
  	  	if (statusIcon != null) {
  	  		if (wifi.capabilities.contains(WifiSecurity.EAP.toString()) || 
  	  				wifi.capabilities.contains(WifiSecurity.WEP.toString()) || 
  	  				wifi.capabilities.contains(WifiSecurity.WPA2_PSK.toString()) ||
  	  				wifi.capabilities.contains(WifiSecurity.WPA_PSK.toString()) || 
  	  				wifi.capabilities.contains(WifiSecurity.WPA_WPA2_PSK.toString())) {
  	  			//set the knock icon refer this access point need authentication
  	  			statusIcon.setImageResource(R.drawable.openbox_wifi_knock);
  	  		} else {
  	  			//set the open wifi icon refer this access point is open
  	  			statusIcon.setImageResource(R.drawable.openbox_wifi_icon);
  	  		}
  	  	}
  	} else {
  		text.setText(null);
  	}
  }
  
}
