package com.oregonscientific.meep.meepopenbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.DetailedState;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.WifiManager.WifiLock;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.oregonscientific.meep.global.Global;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxDialogFragment;
import com.oregonscientific.meep.meepopenbox.view.MeepOpenBoxViewManager;
import com.oregonscientific.meep.meepopenbox.view.WifiNetwork;
import com.oregonscientific.meep.meepopenbox.view.WifiNetworkAdapter;
import com.oregonscientific.meep.widget.StrokedTextView;

/**
 * Activity for WiFi Setup page
 * @author Charles
 */
public class MeepOpenBoxWiFiSetup extends MeepOpenBoxBaseActivity {
	
	private final String TAG = MeepOpenBoxWiFiSetup.class.getSimpleName();
	
	private final String WPA_WPA2_PSK = "WPA/WPA2 PSK";
	private final String WIFI_LOCK_TAG = "wifiLock";
	
	private WifiNetworkAdapter mAdapter = null;
	private String mConnectedWifiSSID = null;
	private String mConnectingWifiSSID = null;
	
	private BroadcastReceiver mConnectBroadcastReceiver = null;
	private BroadcastReceiver mScanBroadcastReceiver = null;
	private BroadcastReceiver mSupplicantReceiver = null;
	
	private WifiLock mWifiLock = null;
	private Timer mTimer = null;
	private boolean mIsWifiDialogDisplayed = false;
	
	/**
	 * An enumeration of WifiSecurity
	 */
	public enum WifiSecurity {
		EAP("EAP"), 
		WPA2_PSK("WPA2-PSK"), 
		WPA_WPA2_PSK("WPA/WPA2-PSK"),
		WPA_PSK("WPA-PSK"), 
		WEP("WEP"), 
		ESS("ESS");
		
		private String name;
		
		WifiSecurity(String name) {
			this.name = name;
		}
		
		@Override
		public String toString() {
			return name;
		}
		
		public static WifiSecurity fromCapabilities(String capabilities) {
			for (WifiSecurity wifiSecurity : WifiSecurity.values()) {
				if (capabilities.contains(wifiSecurity.toString())) {
					switch (WifiSecurity.valueOf(wifiSecurity.name())) {
						case EAP:
							return EAP;
						case WPA2_PSK:
							if (capabilities.contains(WifiSecurity.WPA_PSK.toString())) {
								return WPA_WPA2_PSK;
							}
							return WPA2_PSK;
						case WPA_PSK:
							return WPA_PSK;
						case WEP:
							return WEP;
						case ESS:
							return ESS;
						default:
							return null;
					}
				}
			}
			return null;
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.open_box_wifi_setup_layout);
		
		Button backButton = (Button) findViewById(R.id.wifiSetupBackBtn);
		backButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				onBackPressed();
			}
		});
		
		Button nextButton = (Button) findViewById(R.id.wifiSetupNextBtn);
		nextButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				StrokedTextView nextButtonText = (StrokedTextView) findViewById(R.id.wifiSetupNextText);
				String nextButtonString = nextButtonText != null && nextButtonText.getText() != null ? nextButtonText.getText().toString() : "";
				if (nextButtonString.compareToIgnoreCase(getResources().getString(R.string.skip_btn_text)) == 0) {
					if (!mIsWifiDialogDisplayed) {
						DialogFragment newFragment = MeepOpenBoxDialogFragment.newInstance(MeepOpenBoxDialogFragment.SKIP_WIFI_SETUP_DIALOG_ID);		
						newFragment.show(getFragmentManager(), "dialog");
						getFragmentManager().executePendingTransactions();
					}
				} else {
					MeepOpenBoxViewManager.goToNextPage(MeepOpenBoxWiFiSetup.this);
				}
			}
		});
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		final ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);
		progressBar.setVisibility(View.VISIBLE);
		final ListView listView = (ListView) findViewById(R.id.wifiSetupListView);
		listView.setVisibility(View.INVISIBLE);
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		// register receiver on finish connecting to WiFi
		IntentFilter connectFilter = new IntentFilter();
		connectFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		connectFilter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
		connectFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
		mConnectBroadcastReceiver = new BroadcastReceiver() {
			
			@Override
			public void onReceive(Context context, Intent intent) {
				DetailedState detailedState = null;
				NetworkInfo networkInfo = null;
				String ssid = null;
				String from = "";
				
				// Retrieve message information depending on the action that
				// triggered the message
				if (WifiManager.NETWORK_STATE_CHANGED_ACTION.equals(intent.getAction())) {
					from = WifiManager.NETWORK_STATE_CHANGED_ACTION;
					networkInfo = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
					
					if (networkInfo != null) {
						detailedState = networkInfo.getDetailedState();
						if (detailedState.equals(NetworkInfo.DetailedState.DISCONNECTED)) {
							ssid = mConnectedWifiSSID;
							mConnectedWifiSSID = null;
						} else if (detailedState.equals(NetworkInfo.DetailedState.CONNECTED)) {
							if (wifiManager != null) {
								WifiInfo wifiInfo = wifiManager.getConnectionInfo();
								ssid = wifiInfo.getSSID();
								mConnectedWifiSSID = ssid;
							}
						}
					}
				} else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION.equals(intent.getAction())) {
					if (wifiManager != null) {
						WifiInfo wifiInfo = wifiManager.getConnectionInfo();
						if (wifiInfo != null) {
							ssid = wifiInfo.getSSID();
						}
						ssid = ssid == null ? mConnectingWifiSSID : ssid;
					}
					
					from = WifiManager.SUPPLICANT_STATE_CHANGED_ACTION;
					SupplicantState supplicantState = intent.getParcelableExtra(WifiManager.EXTRA_NEW_STATE);
					if (ssid != null) {
						detailedState = WifiInfo.getDetailedStateOf(supplicantState);
					}
					
					int errorAuthenticating = intent.getIntExtra(WifiManager.EXTRA_SUPPLICANT_ERROR, 0);
					if (errorAuthenticating == WifiManager.ERROR_AUTHENTICATING) {
						detailedState = NetworkInfo.DetailedState.FAILED;
					}
				} 
				
				if (detailedState != null) {
					Log.d(TAG, "Detailed state of Wifi: " + detailedState + " from: " + from);
					
					// Connection to the network failed if is was disconnected but is
					// one of the configured networks
					WifiNetwork.State state = WifiNetwork.State.fromNetworkDetailedState(detailedState);
					if (detailedState.equals(NetworkInfo.DetailedState.DISCONNECTED)) {
						if (ssid != null && isWifiConfigured(ssid)) {
							state = mConnectingWifiSSID != null && ssid.equals(mConnectingWifiSSID) ? WifiNetwork.State.FAILED : WifiNetwork.State.SAVED;
						}
					}
					Log.d(TAG, "Connecting to: " + ssid);
					setWifiNetworkState(ssid, state);
					
					if (ssid != null 
							&& mConnectingWifiSSID != null 
							&& ssid.equals(mConnectingWifiSSID) 
							&& (detailedState.equals(NetworkInfo.DetailedState.CONNECTED) 
									|| detailedState.equals(NetworkInfo.DetailedState.BLOCKED)
									|| detailedState.equals(NetworkInfo.DetailedState.DISCONNECTED)
									|| detailedState.equals(NetworkInfo.DetailedState.FAILED)
									|| detailedState.equals(NetworkInfo.DetailedState.SUSPENDED)
									|| detailedState.equals(NetworkInfo.DetailedState.IDLE))) {
						mConnectingWifiSSID = null;
					}
				}
				
				String buttonText = getResources().getString(R.string.skip_btn_text);
				if (isWifiConnected() && progressBar != null && progressBar.getVisibility() == View.INVISIBLE) {
					// Scroll the list of available to the top ONLY when a connection
					// state changed
					if (listView != null) {
						listView.setSelection(0);
					}
					buttonText = getResources().getString(R.string.next_btn_text);
				} 
				setNextButtonText(buttonText);
				Log.d(TAG, "Setting button text to: " + buttonText + " from network state change");
			}
			
		};
		registerReceiver(mConnectBroadcastReceiver, connectFilter);
		
		// register receiver on finish scanning for WiFi
		IntentFilter scanFilter = new IntentFilter();
		scanFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		mScanBroadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				boolean shouldNotify = false;
				
				if (mAdapter == null) {
					setAdapter(listView, wifiManager);
				} else {
					// Remove items in the adapter that does not exist in the scan result
					for (int i = 0; i < mAdapter.getCount(); i++) {
						WifiNetwork wifi = mAdapter.getItem(i);
						if (wifi != null) {
							boolean existInScanResult = false;
							for (ScanResult result : wifiManager.getScanResults()) {
								if (removeQuotes(result.SSID).equals(removeQuotes(wifi.SSID))) {
									existInScanResult = true;
									break;
								}
							}
							
							if (!existInScanResult) {
								mAdapter.remove(wifi);
								shouldNotify = true;
							}
						}
					}
					
					// Add scan results that are not already exist in the adapter
					for (ScanResult result : wifiManager.getScanResults()) {
						WifiNetwork wifi = getWifiNetwork(result);
						if (wifi != null && !mAdapter.contains(wifi)) {
							mAdapter.add(wifi);
							shouldNotify = true;
						}
					}
					
					if (shouldNotify) {
						mAdapter.sort();
						mAdapter.notifyDataSetChanged();
					}
				}
				
				// Dismiss spinner and display the list of Wi-Fi networks
				listView.setVisibility(View.VISIBLE);
				if (mAdapter != null && progressBar != null) {
					progressBar.setVisibility(View.INVISIBLE);
				}
				
				String buttonText = getResources().getString(R.string.skip_btn_text);
				if (isWifiConnected()) {
					buttonText = getResources().getString(R.string.next_btn_text);
				}
				setNextButtonText(buttonText);
				Log.d(TAG, "Setting button text to: " + buttonText + " from ScanResult");
			}
		};
		registerReceiver(mScanBroadcastReceiver, scanFilter);
		
		if (!wifiManager.isWifiEnabled()) {
			// WiFi not enabled
			wifiManager.setWifiEnabled(true);
		} else {
			if (wifiManager.pingSupplicant()) {
				// Start scanning for WiFi networks
				mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_LOCK_TAG);
				mWifiLock.acquire();
				scheduleScan(wifiManager, 0, 10000);
			}
		}
		
		mSupplicantReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				boolean connected = intent.getBooleanExtra(WifiManager.EXTRA_SUPPLICANT_CONNECTED, false);
				if (connected) {
					// Start scanning for WiFi networks only if a connection to supplicant
					// has been established
					mWifiLock = wifiManager.createWifiLock(WifiManager.WIFI_MODE_FULL, WIFI_LOCK_TAG);
					mWifiLock.acquire();
					scheduleScan(wifiManager, 0, 10000);
				} else {
					if (mTimer != null) {
						mTimer.cancel();
						mTimer = null;
					}
				}
			}
			
		};
		IntentFilter supplicantFilter = new IntentFilter();
		supplicantFilter.addAction(WifiManager.SUPPLICANT_CONNECTION_CHANGE_ACTION);
		registerReceiver(mSupplicantReceiver, supplicantFilter);
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			setResult(Activity.RESULT_OK);
			finish();
		}
	}
	
	@Override
	public void hideBackButton() {
		Button backButton = (Button) findViewById(R.id.wifiSetupBackBtn);
		backButton.setVisibility(View.INVISIBLE);
		View backButtonText = findViewById(R.id.wifiSetupBackText);
		backButtonText.setVisibility(View.INVISIBLE);
	}
	
	@Override
	public void onPause() {
		if (mConnectBroadcastReceiver != null) {
			unregisterReceiver(mConnectBroadcastReceiver);
			mConnectBroadcastReceiver = null;
		}
		if (mScanBroadcastReceiver != null) {
			unregisterReceiver(mScanBroadcastReceiver);
			mScanBroadcastReceiver = null;
		}
		if (mSupplicantReceiver != null) {
			unregisterReceiver(mSupplicantReceiver);
			mSupplicantReceiver = null;
		}
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		if (mWifiLock != null) {
			mWifiLock.release();
			mWifiLock = null;
		}
		
		super.onPause();
	}
	
	/**
	 * Setup the adapter for ListView showing available WiFi
	 * @param listView ListView
	 * @param wifiManager WiFi Manager
	 * @param wifiTable HashMap of WiFi
	 */
	private void setAdapter(
			final ListView listView,
			final WifiManager wifiManager) {
		
		if (mAdapter != null || listView == null || wifiManager == null) {
			return;
		}
		
		// Creates WifiNetwork objects from the scan results
		HashMap<String, WifiNetwork> networks = new HashMap<String, WifiNetwork>();
		for (ScanResult result : wifiManager.getScanResults()) {
			WifiNetwork wifi = getWifiNetwork(result);
			if (wifi != null) {
				networks.put(result.SSID, wifi);
			}
		}
		
		mAdapter = new WifiNetworkAdapter(this, R.layout.open_box_wifi_list_view, new ArrayList<WifiNetwork>(networks.values()));
		listView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				onWifiNetworkClicked(parent, view, position);
			}
		});
		
		mAdapter.sort();
		listView.setAdapter(mAdapter);
	}
	
	/**
	 * Repeatedly scans for latest access points and updates the adapter of
	 * ListView for a fixed-delay
	 * @param wifiManager WiFi Manager
	 * @param delay amount of time in milliseconds before first update
	 * @param period amount of time in milliseconds between updates
	 */
	private void scheduleScan(
			final WifiManager wifiManager,
			long delay,
			long period) {

		TimerTask timerTask = new TimerTask() {
			@Override
			public void run() {
				wifiManager.startScan();
			}
		};
		
		// Cancels a previously scheduled timer task
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}
		
		mTimer = new Timer();
		mTimer.schedule(timerTask, delay, period);
	}
	
	/**
	 * Sets the text of next button
	 * @param text the text of next button
	 */
	private void setNextButtonText(String text) {
		StrokedTextView nextButtonText = (StrokedTextView) findViewById(R.id.wifiSetupNextText);
		if (nextButtonText != null) {
			nextButtonText.setText(text);
		}
	}
	
	@Override
	public void setNextButtonEnabled(boolean enabled) {
		Button nextButton = (Button) findViewById(R.id.wifiSetupNextBtn);
		nextButton.setEnabled(enabled);
	}
	
	private void onWifiNetworkClicked(AdapterView<?> parent, View view, int position) {
		WifiNetwork wifi = null;
		if (mAdapter != null) {
			wifi = mAdapter.getItem(position);
		}
		
		if (wifi != null) {
			if (wifi.state != null
					&& (wifi.state.equals(WifiNetwork.State.AUTHENTICATING) 
							|| wifi.state.equals(WifiNetwork.State.CONNECTING)
							|| wifi.state.equals(WifiNetwork.State.DISCONNECTING))) {
				return;
			}
			
			switch (wifi.state) {
			case OBTAINING_IPADDR:
			case CONNECTED:
				showWifiInformationDialog(wifi);
				break;
			case BLOCKED:
			case FAILED:
			case SAVED:
			case DISABLED:
				showReconnectWifiDialog(wifi);
				break;
			case AUTHENTICATING:
				break;
			default:
				switch (getSecurityType(wifi.capabilities)) {
				case ESS:
					WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
					WifiConfiguration wifiConfig = setupWifi(wifiManager, wifi, "");
					if (wifiConfig == null) {
						showSetupWifiDialog(wifi);
					}
					break;
				default:
					showSetupWifiDialog(wifi);
					break;
				}
				break;
			}
		}
	}
	
	/**
	 * Display a Wi-Fi setup dialog 
	 * 
	 * @param wifi The Wi-Fi network to setup
	 */
	private void showSetupWifiDialog(final WifiNetwork wifi) {
		if(Global.DISABLE_WIFI_SETUP){
			Log.e("MeepOpenBox-showWifiSetup","DISABLE_WIFI_SETUP");
			return;
		}

		// Quick return if no Wi-Fi network information to display
		if (wifi == null) {
			return;
		}
		
		DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag("dialog");
		if (dialogFragment != null && dialogFragment.isAdded()) {
			return;
		}
		
		final Dialog dialog = new Dialog(this, R.style.OpenBoxWifiDialog);
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
					mIsWifiDialogDisplayed = false;
			}
		});
		
		dialog.setContentView(R.layout.open_box_wifi_setup_dialog_layout);
		
		TextView title = (TextView) dialog.findViewById(R.id.title);
		if (title != null) {
			title.setText(wifi.SSID);
		}
		
		TextView signalStrength = (TextView) dialog.findViewById(R.id.signalStrength);
		if (signalStrength != null) {
			signalStrength.setText(getSignalStrengthText(wifi.level));
		}
		
		TextView security = (TextView) dialog.findViewById(R.id.security);
		if (security != null) {
			security.setText(getSecurityDescription(wifi.capabilities));
		}
		
		CheckBox showPassword = (CheckBox) dialog.findViewById(R.id.showPassword);
		if (showPassword != null) {
			showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					EditText password = (EditText) dialog.findViewById(R.id.password);
					if (isChecked) {
						password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
					} else {
						password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
					}
				}
			});
		}
		
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
		if (cancelButton != null) {
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
			});
		}
		
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final Button connectButton = (Button) dialog.findViewById(R.id.connect);
		connectButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				
				EditText password = (EditText) dialog.findViewById(R.id.password);
				WifiConfiguration wifiConfig = setupWifi(wifiManager, wifi, password.getEditableText().toString());
				
				if (wifi != null) {
					wifi.state = WifiNetwork.State.AUTHENTICATING;
				}
				
				if (wifiConfig != null) {
					if (wifiManager != null) {
						mConnectingWifiSSID = wifi.SSID;
						wifiManager.reconnect();
					}
					
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
				
				// TODO: Automatically retry to connect to the Wi-fi network
			}
		});
		
		final EditText password = (EditText) dialog.findViewById(R.id.password);
		if (password != null) {
			password.addTextChangedListener(new TextWatcher() {
				@Override
				public void afterTextChanged(Editable editable) {
					if (wifi != null) {
						WifiSecurity security = WifiSecurity.fromCapabilities(wifi.capabilities);
						if (security != null) {
							switch (security) {
							case WEP:
								switch (password.length()) {
								case 5:
								case 10:
								case 13:
								case 26:
									connectButton.setEnabled(true);
									break;
								default:
									connectButton.setEnabled(false);
								}
								break;
							case WPA_WPA2_PSK:
							case WPA2_PSK:
							case WPA_PSK:
								if (password.length() >= 8 && password.length() <= 64) {
									connectButton.setEnabled(true);
								} else { 
									connectButton.setEnabled(false);
								}
								break;
							default:
								connectButton.setEnabled(true);
								break;
							}
						}
					}
				}
				
				@Override
				public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {
				}
				
				@Override
				public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
				}
				
			});
		}
		
		dialog.show();
		mIsWifiDialogDisplayed = true;
	}
	
	/**
	 * Display the Wi-Fi network information dialog fragment
	 * 
	 * @param wifi The Wi-Fi network whose information is to be displayed
	 */
	private void showWifiInformationDialog(WifiNetwork wifi) {
		if(Global.DISABLE_WIFI_SETUP){
			Log.e("MeepOpenBox-showWifiSetup","DISABLE_WIFI_SETUP");
			return;
		}
		
		// Quick return if no Wi-Fi network information to display
		if (wifi == null) {
			return;
		}
			
		DialogFragment dialogFragment = (DialogFragment) getFragmentManager().findFragmentByTag("dialog");
		if (dialogFragment != null && dialogFragment.isAdded()) {
			return;
		}
		
		final Dialog dialog = new Dialog(this, R.style.OpenBoxWifiDialog);
		dialog.setContentView(R.layout.open_box_wifi_info_display_dialog_layout);
		
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
					mIsWifiDialogDisplayed = false;
			}
		});
		
		TextView title = (TextView) dialog.findViewById(R.id.title);
		title.setText(wifi.SSID);
		
		TextView status = (TextView) dialog.findViewById(R.id.status);
		if (status != null) {
			status.setText(getResources().getString(R.string.connected));
		}
		
		TextView signalStrength = (TextView) dialog.findViewById(R.id.signalStrength);
		if (signalStrength != null) {
			signalStrength.setText(getSignalStrengthText(wifi.level));
		}
		
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final WifiInfo wifiInfo = wifiManager.getConnectionInfo();
		
		int linkSpeedValue = wifiInfo.getLinkSpeed();
		TextView linkSpeed = (TextView) dialog.findViewById(R.id.linkSpeed);
		if (linkSpeed != null) {
			if (linkSpeedValue == -1) {
				TextView linkSpeedText = (TextView) dialog.findViewById(R.id.linkSpeedText);
				linkSpeedText.setVisibility(View.GONE);
				linkSpeed.setVisibility(View.GONE);
			} else {
				linkSpeed.setText(linkSpeedValue + WifiInfo.LINK_SPEED_UNITS);
			}
		}
		
		TextView security = (TextView) dialog.findViewById(R.id.security);
		security.setText(getSecurityDescription(wifi.capabilities));
		
		TextView ipAddress = (TextView) dialog.findViewById(R.id.ipAddress);
		if (ipAddress != null) {
			String ip = wifiInfo == null ? null : getIpAddress(wifiInfo.getIpAddress());
			ipAddress.setText(ip);
		}
		
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
		if (cancelButton != null) {
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
			});
		}
		
		Button forgetButton = (Button) dialog.findViewById(R.id.forget);
		if (forgetButton != null) {
			forgetButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					setNextButtonText(getResources().getString(R.string.skip_btn_text));
					
					if (wifiInfo != null) {
						WifiConfiguration wifiConfig = getConfiguredNetwork(wifiManager, wifiInfo.getSSID());
						if (wifiConfig != null) {
							mConnectingWifiSSID = wifiConfig.SSID;
							wifiManager.removeNetwork(wifiConfig.networkId);
							wifiManager.saveConfiguration();
						}
					}
					
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
			});
		}
		
		dialog.show();
		
		mIsWifiDialogDisplayed = true;
	}
	
	/**
	 * Loads a dialog for forgetting or reconnecting to WiFi
	 * @param context current context
	 * @param wifiManager WiFi Manager
	 * @param scanResult the WiFi selected
	 */
	private void showReconnectWifiDialog(final WifiNetwork wifi) {
		if(Global.DISABLE_WIFI_SETUP){
			Log.e("MeepOpenBox-showWifiSetup","DISABLE_WIFI_SETUP");
			return;
		}
		
		// Quick return if no Wi-Fi network information to display
		if (wifi == null) {
			return;
		}
		
		final Dialog dialog = new Dialog(this, R.style.OpenBoxWifiDialog);
		dialog.setContentView(R.layout.open_box_wifi_reconnect_dialog_layout);
		
		TextView title = (TextView) dialog.findViewById(R.id.title);
		if (title != null) {
			title.setText(wifi.SSID);
		}
		
		TextView signalStrength = (TextView) dialog.findViewById(R.id.signalStrength);
		if (signalStrength != null) {
			signalStrength.setText(getSignalStrengthText(wifi.level));
		}
		
		TextView security = (TextView) dialog.findViewById(R.id.security);
		if (security != null) {
			security.setText(getSecurityDescription(wifi.capabilities));
		}
		
		Button cancelButton = (Button) dialog.findViewById(R.id.cancel);
		if (cancelButton != null) {
			cancelButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
			});
		}
		
		final WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		
		Button forgetButton = (Button) dialog.findViewById(R.id.forget);
		if (forgetButton != null) {
			forgetButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (wifiManager != null) {
						WifiConfiguration wifiConfig = getConfiguredNetwork(wifiManager, wifi.SSID);
						if (wifiConfig != null) {
							mConnectingWifiSSID = wifiConfig.SSID;
							wifiManager.removeNetwork(wifiConfig.networkId);
							wifiManager.saveConfiguration();
						}
					}
					
					mIsWifiDialogDisplayed = false;
					
					setNextButtonText(getResources().getString(R.string.skip_btn_text));
					dialog.dismiss();
				}
			});
		}
		
		
		Button connectButton = (Button) dialog.findViewById(R.id.connect);
		if (connectButton != null) {
			connectButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View view) {
					if (wifi != null && wifiManager != null) {
						wifi.state = WifiNetwork.State.AUTHENTICATING;
						if (mAdapter != null) {
							mAdapter.notifyDataSetChanged();
						}
						
						List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
						for (WifiConfiguration config : configs) {
							if (removeQuotes(config.SSID).equals(removeQuotes(wifi.SSID))) {
								mConnectingWifiSSID = config.SSID;
								wifiManager.enableNetwork(config.networkId, true);
								wifiManager.reassociate();
								break;
							}
						}
					}
					
					mIsWifiDialogDisplayed = false;
					dialog.dismiss();
				}
			});
		}
		
		dialog.show();
		mIsWifiDialogDisplayed = true;
	}
	
	/**
	 * Gets the string expression of a WiFi signal
	 * @param level strength of WiFi
	 * @return string expression of the WiFi signal
	 */
	private String getSignalStrengthText(int level) {
		int numLevels = 4;
		switch (WifiManager.calculateSignalLevel(level, numLevels)) {
			case 0:
				return getResources().getString(R.string.signal_strength_poor);
			case 1:
				return getResources().getString(R.string.signal_strength_fair);
			case 2:
				return getResources().getString(R.string.signal_strength_good);
			case 3:
				return getResources().getString(R.string.signal_strength_excellent);
			default:
				return getResources().getString(R.string.signal_strength_poor);
		}
	}
	
	/**
	 * Gets the string expression of WiFi security
	 * @param capabilities security of WiFi
	 * @return string expression of the WiFi security
	 */
	private String getSecurityDescription(String capabilities) {
		for (WifiSecurity wifiSecurity : WifiSecurity.values()) {
			if (capabilities.contains(wifiSecurity.toString())) {
				switch (WifiSecurity.valueOf(wifiSecurity.name())) {
					case EAP:
						return wifiSecurity.name();
					case WPA2_PSK:
						if (capabilities.contains(WifiSecurity.WPA_PSK.toString())) {
							return WPA_WPA2_PSK;
						}
						return wifiSecurity.name().replace("_", " ");
					case WPA_PSK:
						return wifiSecurity.name().replace("_", " ");
					case WEP:
						return wifiSecurity.name();
					case ESS:
						return getResources().getString(R.string.wifi_security_none);
					default:
						return wifiSecurity.name();
				}
			}
		}
		return capabilities;
	}
	
	/**
	 * Gets the security type of WiFi
	 * @param capabilities security of WiFi
	 * @return the WiFi security type
	 */
	private WifiSecurity getSecurityType(String capabilities) {
		for (WifiSecurity wifiSecurity : WifiSecurity.values()) {
			if (capabilities.contains(wifiSecurity.toString())) {
				return wifiSecurity;
			}
		}
		return null;
	}
	
	/**
	 * Gets the IP Address in string format
	 * @return IP Address in string
	 */
	private String getIpAddress(int ip) {
		return String.format(Locale.ENGLISH, "%d.%d.%d.%d", (ip & 0xff), (ip >> 8 & 0xff), (ip >> 16 & 0xff), (ip >> 24 & 0xff));
	}
	
	/**
	 * Removes starting and ending quotes of a string if any
	 * @param string string to be quotes removed
	 * @return string without starting and ending quotes
	 */
	private String removeQuotes(String string) {
		if (string == null) {
			return null;
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
	 * Checks whether a string is blank
	 * @param string string to be checked
	 * @return true if the string is null or empty, false otherwise
	 */
	private boolean isBlank(String string) {
		int length;
		
		if ((string == null) || ((length = string.length()) == 0)) {
			return true;
		}
		
		for (int i = 0; i < length; i++) {
			if (!Character.isWhitespace(string.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Determine whether or not the given string is hexadecimal
	 * 
	 * @param in The input string
	 * @return true if the input string is hexadecimal, false otherwise
	 */
	public boolean isHex(String in) {
		return in == null ? false : in.matches("[0-9A-Fa-f]+");
	}
	
	/**
	 * Connects to an available WiFi
	 * 
	 * @param wifiManager The Wi-Fi manager that manages Wi-Fi connectivity
	 * @param wifi The Wi-Fi network to connect to
	 * @param password The password to authenticate the connection
	 * @return the WifiConfiguration if connected successfully, null otherwise
	 */
	private WifiConfiguration setupWifi(WifiManager wifiManager, WifiNetwork wifi, String password) {
		// Quick return if there is nothing to process
		if (wifiManager == null || wifi == null) {
			return null;
		}
		
		WifiConfiguration wifiConfig = new WifiConfiguration();
		wifiConfig.SSID = '\"' + wifi.SSID + '\"';
		
		wifiConfig.hiddenSSID = true;
		wifiConfig.status = WifiConfiguration.Status.ENABLED;
		
		wifiConfig.allowedProtocols.clear();
		wifiConfig.allowedKeyManagement.clear();
		wifiConfig.allowedAuthAlgorithms.clear();
		wifiConfig.allowedPairwiseCiphers.clear();
		wifiConfig.allowedGroupCiphers.clear();
		
		wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP40);
		wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.WEP104);
		
		for (WifiSecurity security : WifiSecurity.values()) {
			if (wifi.capabilities.contains(security.toString())) {
				switch (WifiSecurity.valueOf(security.name())) {
					case EAP:
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
				    
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_EAP);
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.IEEE8021X);
						
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
						
						wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
						wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
						
						if (password != null && password.length() > 0 && password.compareToIgnoreCase("") != 0) {
							wifiConfig.preSharedKey = '\"' + password + '\"';
						}
						break;
					case WPA2_PSK:
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
						
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
						
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
						
						if (password != null && password.length() > 0 && password.compareToIgnoreCase("") != 0) {
							if (password.length() == 64 && isHex(password)) {
								wifiConfig.preSharedKey = password;
							} else {
								wifiConfig.preSharedKey = '\"' + password + '\"';
							}
						}
						break;
					case WPA_PSK:
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
						
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
						
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
						wifiConfig.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
						
						if (password != null && password.length() > 0 && password.compareToIgnoreCase("") != 0) {
							if (password.length() == 64 && isHex(password)) {
								wifiConfig.preSharedKey = password;
							} else {
								wifiConfig.preSharedKey = '\"' + password + '\"';
							}
						}
						break;
					case WEP:
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.RSN); 
						wifiConfig.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
						
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
						
						wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.SHARED);
						wifiConfig.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);
						
						wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
						wifiConfig.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
						
						String passwd = password;
						if (password != null && password.length() > 0 && password.compareToIgnoreCase("") != 0) {
							if (password.length() == 10 && isHex(password)) {
								passwd = password;
							} else if (password.length() == 26 && isHex(password)) {
								passwd = password;
							} else {
								passwd = '\"' + password + '\"';
							}
							
							wifiConfig.wepKeys[0] = passwd;
							wifiConfig.wepTxKeyIndex = 0;
						}
						break;
					case ESS:
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
						break;
					default:
						wifiConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
						break;
				}
				break;
			}
		}
		
		int wifiId = wifiManager.addNetwork(wifiConfig);
		boolean success = wifiManager.enableNetwork(wifiId, true);
		wifiManager.saveConfiguration();
		
		return (success ? wifiConfig : null);
	}
	
	private void setWifiNetworkState(String SSID, WifiNetwork.State state) {
		if (SSID != null && mAdapter != null) {
			int position = mAdapter.getPosition(SSID);
			WifiNetwork wifi = mAdapter.getItem(position);
			if (wifi != null) {
				Log.d(TAG, SSID + " is " + state);
				wifi.state = state;
				mAdapter.sort();
				mAdapter.notifyDataSetChanged();
			}
		}
	}
	
	/**
	 * Determines whether WiFi connectivity exists and it is possible to establish connections
	 * 
	 * @return true if Wifi connectivity exists and is possible to establish connection, false otherwise
	 */
	private boolean isWifiConnected() {
		ConnectivityManager manager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		return networkInfo.isConnected();
	}
	
	/**
	 * Determines whether or not the given SSID is one of the configured networks 
	 * 
	 * @param SSID The SSID of the network
	 * @return true if the SSID represents one of the configured networks, false otherwise
	 */
	private boolean isWifiConfigured(String SSID) {
		WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		
		for (WifiConfiguration config : configs) {
			if (removeQuotes(config.SSID).equals(removeQuotes(SSID))) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets the network information currently connected to
	 * @param wifiManager WiFi Manager
	 * @return current network information
	 */
	private WifiInfo getCurrentConnectionInfo(WifiManager wifiManager) {
		if (isWifiConnected()) {
			final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
			if (connectionInfo != null && !isBlank(connectionInfo.getSSID())) {
				return connectionInfo;
			}
			// setNextButtonText(getResources().getString(R.string.next_btn_text));
		}
		return null;
	}
	
	/**
	 * Gets the configured settings of selected WiFi if any
	 * @param wifiManager WiFi Manager
	 * @param SSID Name of WiFi
	 * @return the configured WiFi matching the chosen one, null otherwise
	 */
	private WifiConfiguration getConfiguredNetwork(
			WifiManager wifiManager,
			String SSID) {
		
		WifiConfiguration wifiConfiguration;
		List<WifiConfiguration> wifiConfigurationList = wifiManager.getConfiguredNetworks();
		for (int i = 0; i < wifiConfigurationList.size(); i++) {
			wifiConfiguration = wifiConfigurationList.get(i);
			if (SSID != null 
					&& wifiConfiguration.SSID != null 
					&& removeQuotes(wifiConfiguration.SSID).equalsIgnoreCase(removeQuotes(SSID))) {
				if (wifiManager.enableNetwork(wifiConfiguration.networkId, true)) {
					return wifiConfiguration;
				}
			}
		}
		return null;
	}
	
	private WifiNetwork getWifiNetwork(ScanResult scan) {
		// Quick return if there is nothing to process
		if (scan == null) {
			return null;
		}
		
		int position = mAdapter == null ? AdapterView.INVALID_POSITION : mAdapter.getPosition(scan.SSID);
		WifiNetwork result = null;
		boolean isNew = false;
		boolean shouldNotify = false;
		
		if (position == AdapterView.INVALID_POSITION) {
			result = new WifiNetwork(scan);
			isNew = true;
		} else {
			result = mAdapter.getItem(position);
			result.capabilities = scan.capabilities;
		}
		
		WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		WifiInfo wifiInfo = getCurrentConnectionInfo(wifiManager);
		
		// Sets the state of the scan result
		boolean isWifiConfigured = false;
		List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
		for (WifiConfiguration config : configs) {
			if (!removeQuotes(result.SSID).equals(removeQuotes(config.SSID))) {
				continue;
			} else {
				isWifiConfigured = true;
			}
			
			if (wifiInfo != null && removeQuotes(wifiInfo.getSSID()).equals(removeQuotes(result.SSID))) {
				shouldNotify = (result.state != null && !result.state.equals(WifiNetwork.State.CONNECTED));
				result.state = WifiNetwork.State.CONNECTED;
				mConnectedWifiSSID = result.SSID;
				break;
			}
			
			Log.d(TAG, config.SSID + " status: " + config.status);
			
			WifiNetwork.State state = WifiNetwork.State.fromWifiConfigurationStatus(config.status);
			switch (state) {
			case DISABLED:
				if (!result.state.equals(WifiNetwork.State.FAILED) && !result.state.equals(WifiNetwork.State.DISABLED)) {
					shouldNotify = true;
					result.state = result.state.equals(WifiNetwork.State.FAILED) ? state : WifiNetwork.State.DISABLED;
				}
				break;
			case CONNECTED:
				shouldNotify = (result.state != null && !result.state.equals(state));
				result.state = WifiNetwork.State.CONNECTED;
				break;
			case IDLE:
				if (!result.state.equals(WifiNetwork.State.FAILED) && !result.state.equals(WifiNetwork.State.SAVED)) {
					shouldNotify = true;
					result.state = result.state.equals(WifiNetwork.State.FAILED) ? state : WifiNetwork.State.SAVED;
				}
				break;
			}
			
			break;
		}
		
		if (!isWifiConfigured) {
			switch (result.state) {
			case DISABLED:
			case CONNECTED:
			case FAILED:
			case SAVED:
				result.state = WifiNetwork.State.IDLE;
			}
		}
		
		if (!isNew && shouldNotify && mAdapter != null) {
			mAdapter.notifyDataSetChanged();
		}
		
		return result;
	}
	
}