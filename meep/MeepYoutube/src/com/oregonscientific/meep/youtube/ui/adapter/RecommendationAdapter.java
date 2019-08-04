package com.oregonscientific.meep.youtube.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.util.ImageDownloader;
import com.oregonscientific.meep.youtube.YouTubeUtility;
import com.oregonscientific.meep.youtube.R;

public class RecommendationAdapter extends ArrayAdapter<YoutubeObject> {

	String PACKAGE_NAME;
	private int resourceId = 0;
	private LayoutInflater inflater;
	ImageDownloader imageDownloader;

	public RecommendationAdapter(Context context, int resourceId,
			ArrayList<YoutubeObject> Items) {
		super(context, resourceId, Items);
		// TODO Auto-generated constructor stub
		this.resourceId = resourceId;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		PACKAGE_NAME = context.getPackageName();
		imageDownloader = new ImageDownloader(context,PACKAGE_NAME);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View rowView = convertView;
		if(convertView == null)
		{
		      rowView = inflater.inflate(resourceId, null);
		      ViewHolder viewHolder = new ViewHolder();
		      viewHolder.text = (TextView) rowView.findViewById(R.id.webText);
		      viewHolder.image = (ImageView) rowView.findViewById(R.id.webImage);			  
		      rowView.setTag(viewHolder);
		}
		ViewHolder holder = (ViewHolder) rowView.getTag();
		holder.youtubeObject = getItem(position);
		String url = holder.youtubeObject.getUrl();
		holder.text.setText("");
	    ImageView imageview = holder.image;
	    imageDownloader.download(holder.youtubeObject.getThumbnail(), imageview);
	    		
	    //YouTubeUtility.printLogcatDebugMessage(holder.website_object.getThumbnail());
		return rowView;
	}
	
	

}
