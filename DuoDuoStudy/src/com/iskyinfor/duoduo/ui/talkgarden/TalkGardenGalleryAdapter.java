package com.iskyinfor.duoduo.ui.talkgarden;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import com.iskyinfor.duoduo.R;

public class TalkGardenGalleryAdapter extends BaseAdapter {
	private Context mContext = null;
	private int[] imageData = { R.drawable.person_haomeili,
			R.drawable.person_haomeili, R.drawable.person_liweiwei,
			R.drawable.person_liweiwei, R.drawable.person_haomeili,
			R.drawable.person_liweiwei, R.drawable.person_haomeili,
			R.drawable.person_liweiwei };

	public TalkGardenGalleryAdapter(Context c) 
	{
		mContext = c;
	}

	@Override
	public int getCount()
	{
		return imageData.length;
	}

	@Override
	public Object getItem(int position) 
	{
		return position;
	}

	@Override
	public long getItemId(int position) 
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		ImageView img = new ImageView(mContext);
		img.setImageResource(imageData[position]);
		img.setAdjustViewBounds(true);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setLayoutParams(new Gallery.LayoutParams(100, 100));
		img.setPadding(2,2,2,2);
		
		return img;
	}

}
