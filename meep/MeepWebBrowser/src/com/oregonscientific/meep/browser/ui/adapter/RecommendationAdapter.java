package com.oregonscientific.meep.browser.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.browser.BrowserUtility;
import com.oregonscientific.meep.browser.WebBrowserApplication;
import com.oregonscientific.meep.browser.R;

public class RecommendationAdapter extends ArrayAdapter<WebsiteObject> {

	private int resourceId = 0;
	private LayoutInflater inflater;
	ImageDownloader imageDownloader;

	public RecommendationAdapter(Context context, int resourceId,
			ArrayList<WebsiteObject> Items) {
		super(context, resourceId, Items);
		// TODO Auto-generated constructor stub
		this.resourceId = resourceId;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		imageDownloader = ((WebBrowserApplication)context.getApplicationContext()).getImageDownloader();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View rowView = convertView;
		if(rowView == null)
		{
		      rowView = inflater.inflate(resourceId, null);
		      ViewHolder viewHolder = new ViewHolder();
		      viewHolder.text = (TextView) rowView.findViewById(R.id.webText);
		      viewHolder.image = (ImageView) rowView
		          .findViewById(R.id.webImage);
		      rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.website_object = getItem(position);
		String url = holder.website_object.getUrl();
	    holder.text.setText(url);
	    ImageView imageview = holder.image;
	    imageDownloader.download(holder.website_object.getThumbnail(), imageview);
	    BrowserUtility.printLogcatDebugMessage(holder.website_object.getThumbnail());
		return rowView;
	}
	
	

}
