package com.oregonscientific.meep.communicator.view.conversation;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

/**
 * Adapter for showing a list of emoticons
 */
public class EmoticonAdapter extends ArrayAdapter<String> {
	
	private Context mContext;
	
	/**
	 * Adapter Constructor
	 * @param context the context to operate in
	 * @param values the values to be viewed
	 */
	public EmoticonAdapter(Context context, String[] values) {
		super(context, 0, values);
		
		mContext = context;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ImageView emoticon;
		String code = getItem(position);
		Integer emoticonId = Emoticon.getEmoticonId(code);
		
		if (convertView != null 
				&& convertView.getTag() != null 
				&& emoticonId != null 
				&& mContext.getResources().getDrawable(emoticonId) != null 
				&& convertView.getTag().equals(code)) {
			emoticon = (ImageView) convertView;
		} else {
			emoticon = new ImageView(mContext);
			if (emoticonId != null) {
				emoticon.setImageDrawable(mContext.getResources().getDrawable(emoticonId));
				emoticon.setTag(code);
			}
		}
		
		return emoticon;
		
	}
}