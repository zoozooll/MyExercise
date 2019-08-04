package com.oregonscientific.meep.together.adapter;

import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.oregonscientific.meep.together.R;
import com.oregonscientific.meep.together.activity.UserFunction;
import com.oregonscientific.meep.together.bean.Kid;

public class ListAdapterSecurityLevel extends ArrayAdapter<Boolean> {
	private final static String TAG = "ListAdapterSecurityLevel";
	private int resourceId = 0;
	private LayoutInflater inflater;

	public ListAdapterSecurityLevel(Context context, int resourceId,
			List<Boolean> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void refresh() {
		notifyDataSetChanged();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		View view;
		TextView textName;
		TextView textOption;
		RadioButton radioButton;

		boolean isSelect = getItem(position);

		view = inflater.inflate(resourceId, null);

		try {
			textName = (TextView) view.findViewById(R.id.textName);
			textOption = (TextView) view.findViewById(R.id.textOption);
			radioButton = (RadioButton) view.findViewById(R.id.radioButton);
			radioButton.setChecked(isSelect);
		} catch (ClassCastException e) {
			Log.e(TAG, "Wrong resourceId", e);
			throw e;
		}
		switch (position) {
		case 0:
			textName.setText(R.string.high_security_level_title);
			break;
		case 1:
			textName.setText(R.string.low_security_level_title);
			break;
		case 2:
			textName.setText(R.string.custom_security_level_title);
			textOption.setText(R.string.edit);
			break;
		default:
			break;
		}

		if (position % 2 == 0) {
			view.setBackgroundResource(R.color.item_bkg_single);
		} else {
			view.setBackgroundResource(R.color.item_bkg_double);
		}

		return view;
	}
}
