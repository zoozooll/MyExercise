package com.oregonscientific.meep.youtube.ui.adapter;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import com.oregonscientific.meep.youtube.ImageThreadLoader;
import com.oregonscientific.meep.youtube.R;
import com.oregonscientific.meep.youtube.ImageThreadLoader.ImageLoadedListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ListAdapterWebsite extends ArrayAdapter<String> {

	private int resourceId = 0;
	private LayoutInflater inflater;
	ImageThreadLoader imageLoader = new ImageThreadLoader();
	
	public ListAdapterWebsite(Context context, int resourceId, ArrayList<String> Items) {
		super(context, resourceId, Items);
		// TODO Auto-generated constructor stub
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = inflater.inflate(resourceId,null);
		String url = getItem(position);
		view.setTag(url);
		final ImageView image = (ImageView) view.findViewById(R.id.webImage);
		TextView text = (TextView) view.findViewById(R.id.webText);
		text.setText(url);
		
		Bitmap cachedImage = null;
		try {
				cachedImage = imageLoader.loadImage(url, new ImageLoadedListener() {
					public void imageLoaded(Bitmap imageBitmap) {
						image.setImageBitmap(imageBitmap);
						// notifyDataSetChanged();
					}
				});
		} catch (MalformedURLException e) {
			Log.e("browser", "Bad remote image URL: " + url, e);
		}

		if (cachedImage != null) {
			image.setImageBitmap(cachedImage);
		}
		return view;
	}

}
