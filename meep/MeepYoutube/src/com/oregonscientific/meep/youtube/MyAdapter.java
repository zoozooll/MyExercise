package com.oregonscientific.meep.youtube;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.youtube.ImageThreadLoader.ImageLoadedListener;

public class MyAdapter extends ArrayAdapter<HashMap<String, Object>> {
	private final static String TAG = "MyAdapter";
	private int resourceId = 0;
	private LayoutInflater inflater;
	private Context context;

	private ImageThreadLoader imageLoader = new ImageThreadLoader();

	public MyAdapter(Context context, int resourceId, List<HashMap<String, Object>> Items) {
		super(context, 0, Items);
		this.resourceId = resourceId;
		inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.context = context;
	}
	
	public void refresh()
	{
		notifyDataSetChanged();  
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

	    View view;
	    TextView textId;
	    TextView textTitleShort;
	    TextView textTitleLong;
	    TextView textDuration;
	    TextView textViewCount;
	    final ImageView image;

	    Log.d("youtube testing", "youtube getview");
	    view = inflater.inflate(resourceId, parent, false);

	    try {
	    	textId = (TextView)view.findViewById(R.id.youtubeId);
	    	textTitleShort = (TextView)view.findViewById(R.id.youtubeTitleShort);
	    	textTitleLong = (TextView)view.findViewById(R.id.youtubeTitleLong);
	    	textDuration = (TextView)view.findViewById(R.id.youtubeDuration);
	    	textViewCount = (TextView)view.findViewById(R.id.youtubeViewCount);
	    	image = (ImageView)view.findViewById(R.id.youtubeImageView);
	    } catch( ClassCastException e ) {
	    	Log.e(TAG, "Your layout must provide an image and a text view with ID's icon and text.", e);
	    	throw e;
	    }

	    HashMap<String, Object> item = getItem(position);
	    
	    Bitmap cachedImage = null;
	    //2013-02-21 - Raymond - Catch all error
	    try {
	    	if(item!=null && item.size()>0){
		    	cachedImage = imageLoader.loadImage((String) item.get("youtubeImageView"), new ImageLoadedListener() {	
		    		public void imageLoaded(Bitmap imageBitmap) {
		    			image.setImageBitmap(imageBitmap);
		    			Log.d("youtube testing", "youtube network iamge loaded");
		    			//notifyDataSetChanged();                
		    			}
		    		});

			    textId.setText((String) item.get("youtubeId"));
			    textTitleShort.setText((String) item.get("youtubeTitleShort"));
			    textTitleLong.setText((String) item.get("youtubeTitleLong"));
			    textDuration.setText((String) item.get("youtubeDuration"));
			    textViewCount.setText((String) item.get("youtubeViewCount"));
	    	}


	    } catch (Exception e) {
	    	Log.e(TAG, "Bad remote image URL: " + e.toString());
	    }
	    
	    if( cachedImage != null ) {
	    	image.setImageBitmap(cachedImage);
	    }else{
	    	
	    }

	    return view;
	}
}