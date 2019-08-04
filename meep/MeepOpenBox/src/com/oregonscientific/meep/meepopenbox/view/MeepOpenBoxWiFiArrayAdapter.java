package com.oregonscientific.meep.meepopenbox.view;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.net.wifi.ScanResult;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.R;

/**
 * Array Adapter for showing a list of available WiFi
 * @author Charles
 *
 */
public class MeepOpenBoxWiFiArrayAdapter extends ArrayAdapter<String> {
	
	public static final int STATE_CONNECTED = 0;
	public static final int STATE_AUTHENTICATING = 1;
	public static final int STATE_AUTHENTICATION_PROBLEM = 2;
	public static final int STATE_DISABLED = 3;
	public static final int STATE_UNKNOWN = 4;
	
	private Context mContext;
	private LayoutInflater mInflater;
	private int mConnectionStatus = -1;
	List<String> mValues;
	private String connectedWiFiSSID = null;
	
	/**
	 * WiFi Array Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 */
	public MeepOpenBoxWiFiArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.open_box_wifi_list_view, values);
		
		this.mContext = context;
		this.mValues = values;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	/**
	 * Updates the WiFi list
	 * 
	 * @param values HashMap of WiFi
	 */
	public void refresh(LinkedHashMap<String, ScanResult> values) {
		if (values != null && !values.isEmpty()) {
			this.mValues.clear();
			this.mValues.addAll(new LinkedList<String>(Arrays.asList(values.keySet().toArray(new String[values.size()]))));
			notifyDataSetChanged();
		}
	}
	
	/**
	 * Updates the adapter with the new values
	 * 
	 * @param values The new values to update to
	 */
	public void update(HashMap<String, ScanResult> values) {
		boolean dataSetChanged = false;
		if (values != null) {
			// Only adds update the list if the existing list is different from
			// the new values
			for (String key : values.keySet()) {
				if (!mValues.contains(key)) {
					mValues.add(key);
					dataSetChanged = true;
				}
			}
		}
		
		if (dataSetChanged) {
			notifyDataSetChanged();
		}
	}
	
	/**
	 * Sets the SSID of selected WiFi
	 * @param SSID of WiFi selected
	 */
	public void setSelectedValue(String value) {
		this.connectedWiFiSSID = value;
	}
	
	/**
	 * Gets the selected value
	 * @return value selected
	 */
	public String getSelectedValue() {
		return connectedWiFiSSID;
	}
	
	/**
	 * Sets WiFi connection status
	 * @param connectionStatus value of connection status
	 */
	public void setConnectionStatus(int connectionStatus) {
		this.mConnectionStatus = connectionStatus;
	}
	
	/**
	 * Returns the position of the currently selected item
	 * 
	 * @return index of the currently selected item, -1 if there is not currently selected item
	 */
	public int getSelectedIndex() {
		String selectedValue = getSelectedValue();
		return mValues == null ? -1 : mValues.indexOf(selectedValue);
	}
	
	/**
	 * Sets the status of view
	 * @param convertView convertView
	 */
	private void setStatus(View convertView) {
		if (convertView == null) {
			return;
		}
		
		TextView textView = (TextView) convertView.findViewById(R.id.status);
		if (textView == null) {
			return;
		}
		
		if (getSelectedValue() != null && convertView.getTag().equals(getSelectedValue())) {
			switch (mConnectionStatus) {
				case STATE_CONNECTED:
					textView.setText(mContext.getResources().getString(R.string.connected));
					textView.setVisibility(View.VISIBLE);
					break;
				case STATE_AUTHENTICATING:
					textView.setText(mContext.getResources().getString(R.string.authenticating));
					textView.setVisibility(View.VISIBLE);
					break;
				case STATE_AUTHENTICATION_PROBLEM:
					textView.setText(mContext.getResources().getString(R.string.authentication_problem));
					textView.setVisibility(View.VISIBLE);
					break;
				default:
					textView.setVisibility(View.INVISIBLE);
					break;
			}
		} else {
			textView.setVisibility(View.INVISIBLE);
		}
	}
	
	/**
	 * Sets text of view highlighted
	 * @param convertView convertView
	 */
	private void highlightText(View convertView) {
		if (convertView == null) {
			return;
		}
		TextView textView = (TextView) convertView.findViewById(R.id.name);
		if (textView != null) {
			textView.setTextColor(mContext.getResources().getColor(R.color.brown));
		}
	}
	
	/**
	 * Sets text of view to default
	 * @param convertView convertView
	 */
	private void showDefaultText(View convertView) {
		if (convertView == null) {
			return;
		}
		TextView textView = (TextView) convertView.findViewById(R.id.name);
		if (textView != null) {
			textView.setTextColor(mContext.getResources().getColor(R.color.white));
		}
	}
	
	/**
	 * Sets background of view highlighted
	 * @param convertView convertView
	 */
	private void highlightBackground(View convertView) {
		if (convertView == null) {
			return;
		}
		convertView.setBackgroundResource(R.drawable.openbox_popupwifi_hl);
	}
	
	/**
	 * Sets background of view to default
	 * @param convertView convertView
	 */
	private void showDefaultBackground(View convertView) {
		if (convertView == null) {
			return;
		}
		convertView.setBackgroundColor(Color.TRANSPARENT);
	}
	
	/**
	 * Sets view according to its property
	 * @param convertView convertView
	 */
	private void setViewAutomatically(View convertView) {
		if (convertView == null) {
			return;
		}
		
		if (getSelectedValue() != null && convertView.getTag().equals(getSelectedValue())) {
			highlightBackground(convertView);
			highlightText(convertView);
		} else {
			showDefaultBackground(convertView);
			showDefaultText(convertView);
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView != null && convertView.getTag().equals(getItem(position))) {
			setViewAutomatically(convertView);
			setStatus(convertView);
			return convertView;
		}
		
		convertView = mInflater.inflate(R.layout.open_box_wifi_list_view, null);
		convertView.setTag(getItem(position));
		TextView name = (TextView) convertView.findViewById(R.id.name);
		if (name != null) {
			name.setText(getItem(position));
		}
		
		setViewAutomatically(convertView);
		setStatus(convertView);
		return convertView;
	}
}