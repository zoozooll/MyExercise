package com.iskyinfor.duoduo.ui;

import java.util.ArrayList;
import java.util.HashMap;
import com.iskyinfor.duoduo.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ImageAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private ArrayList<HashMap<String, Object>> gridData;

	public ImageAdapter(Context context, LayoutInflater inflater,
			ArrayList<HashMap<String, Object>> gridData) {
		super();
		this.inflater = inflater;
		this.gridData = gridData;
	}

	public int getCount() {
		return gridData.size();
	}

	public Object getItem(int position) {
		return gridData.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		View view;
		
		if (convertView != null) 
		{
			view = convertView;
		} else {
			view = inflater.inflate(R.layout.index_item, null, false);
		}

		FolderView holder = (FolderView) view.getTag();
		
		if (holder == null) 
		{
			holder = new FolderView();
			holder.imageView = (ImageView) view.findViewById(R.id.gridImage);
			holder.imageView.setBackgroundResource((Integer)gridData.get(position).get("image"));
			holder.textView = (TextView) view.findViewById(R.id.gridText);
			holder.textView.setText((CharSequence)gridData.get(position).get("text"));
			view.setTag(holder);
		}

		
		return view;
	}

	static class FolderView {
		ImageView imageView;
		TextView textView;
	}

}
