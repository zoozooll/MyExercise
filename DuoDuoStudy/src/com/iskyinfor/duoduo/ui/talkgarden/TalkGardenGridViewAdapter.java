package com.iskyinfor.duoduo.ui.talkgarden;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import com.iskyinfor.duoduo.R;

public class TalkGardenGridViewAdapter extends BaseAdapter 
{
	private Context mContext = null;
	private int [] imgData = 
	{
		R.drawable.talkface_laugh,R.drawable.talkface_dizzy,
		R.drawable.talkface_cachexia,R.drawable.talkface_cachinnate,
		R.drawable.talkface_cow,R.drawable.talkface_glasses,
		R.drawable.talkface_kiss,R.drawable.talkface_leftclown,
		R.drawable.talkface_monkey,R.drawable.talkface_rightclown,
		R.drawable.talkface_sad,R.drawable.talkface_sadness,
		R.drawable.talkface_smile,R.drawable.talkface_tear,
		R.drawable.talkface_monkey,R.drawable.talkface_rightclown,
		R.drawable.talkface_sad,R.drawable.talkface_sadness,
		R.drawable.talkface_smile,R.drawable.talkface_tear
	};	
	
	public TalkGardenGridViewAdapter(Context context)
	{
		mContext = context;
	}
	
	@Override
	public int getCount()
	{
		return imgData.length;
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
		img.setImageResource(imgData[position]);
		img.setAdjustViewBounds(true);
		img.setScaleType(ImageView.ScaleType.FIT_XY);
		img.setLayoutParams(new GridView.LayoutParams(40, 40));
		img.setPadding(2,5,5,2);
		return img;
	}

}
