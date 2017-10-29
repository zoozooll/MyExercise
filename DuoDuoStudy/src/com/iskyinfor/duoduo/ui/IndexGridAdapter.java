package com.iskyinfor.duoduo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;

public class IndexGridAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> productes;
	private int textSize;
	private int resId;

	public IndexGridAdapter(Context context, LayoutInflater inflater,
			ArrayList<HashMap<String, Object>> productes, int textSize,
			int resId) {
		super();
		this.inflater = inflater;
		this.productes = productes;
		this.textSize = textSize;
		this.resId = resId;
	}

	public int getCount()
	{
		return productes.size();
	}

	public Object getItem(int position) 
	{
		return productes.get(position);
	}

	public long getItemId(int position)
	{
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		if (convertView != null) 
		{
			view = convertView;
		}
		else 
		{
			view = inflater.inflate(resId, null, false);
		}

		FolderView holder = (FolderView) view.getTag();

		if (holder == null)
		{
			holder = new FolderView();
			holder.imageView = (ImageView) view.findViewById(R.id.gridImage);
			holder.imageView.setBackgroundResource((Integer) productes.get(
					position).get("image"));
			holder.textView = (TextView) view.findViewById(R.id.gridText);
			holder.textView.setText((CharSequence) productes.get(position).get(
					"text"));
			holder.textView.setTextSize(textSize);
			view.setTag(holder);
		}

		return view;
	}

	final class FolderView {
		ImageView imageView;
		TextView textView;
		TextView textPrice;
	}
}