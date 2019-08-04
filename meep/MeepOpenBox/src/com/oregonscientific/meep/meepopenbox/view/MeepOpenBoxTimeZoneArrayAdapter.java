package com.oregonscientific.meep.meepopenbox.view;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.oregonscientific.meep.meepopenbox.R;

/**
 * Array Adapter for showing a list of time zone
 * @author Charles
 *
 */
public class MeepOpenBoxTimeZoneArrayAdapter extends ArrayAdapter<String> {
	
	private Context mContext;
	private LayoutInflater mInflater;

	/**
	 * Array Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 */
	public MeepOpenBoxTimeZoneArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.open_box_list_view, values);
		
		this.mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Sets the selected value
	 * @param value value selected
	 */
	public void setSelectedValue(String value) {
		MeepOpenBoxViewManager.setTimeZonePreference(mContext, value);
	}
	
	/**
	 * Gets the selected value
	 * @return value selected
	 */
	public String getSelectedValue() {
		return MeepOpenBoxViewManager.getTimeZonePreference(mContext);
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
		convertView.setBackgroundResource(R.drawable.openbox_popup02_hl);
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
	 * @param position position of convertView
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
			return convertView;
		}
		
		convertView = mInflater.inflate(R.layout.open_box_list_view, null);
		convertView.setTag(getItem(position));
		TextView textView = (TextView) convertView.findViewById(R.id.name);
		if (textView != null) {
			textView.setText(getItem(position));
		}
		
		setViewAutomatically(convertView);
		return convertView;
	}
}