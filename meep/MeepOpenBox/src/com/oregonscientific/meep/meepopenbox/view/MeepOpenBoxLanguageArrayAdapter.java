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
 * Array Adapter for showing a list of languages
 * @author Charles
 *
 */
public class MeepOpenBoxLanguageArrayAdapter extends ArrayAdapter<String> {
	
	private Context mContext;
	private LayoutInflater mInflater;

	/**
	 * Array Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 */
	public MeepOpenBoxLanguageArrayAdapter(Context context, List<String> values) {
		super(context, R.layout.open_box_list_view, values);
		
		this.mContext = context;
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	/**
	 * Sets the selected position
	 * @param position position selected
	 */
	public void setSelectedPosition(String position) {
		MeepOpenBoxViewManager.setLanguagePreference(mContext, position);
	}
	
	/**
	 * Gets the selected position
	 * @return position selected
	 */
	public String getSelectedPosition() {
		return MeepOpenBoxViewManager.getLanguagePreference(mContext);
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
		convertView.setBackgroundResource(R.drawable.openbox_popup01_hl);
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
	private void setViewAutomatically(View convertView, int position) {
		if (convertView == null) {
			return;
		}
		if (getSelectedPosition() != null && String.valueOf(position).compareTo(getSelectedPosition()) == 0) {
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
			setViewAutomatically(convertView, position);
			return convertView;
		}
		
		convertView = mInflater.inflate(R.layout.open_box_list_view, null);
		convertView.setTag(getItem(position));
		TextView textView = (TextView) convertView.findViewById(R.id.name);
		if (textView != null) {
			textView.setText(getItem(position));
		}
		
		setViewAutomatically(convertView, position);
		return convertView;
	}
}