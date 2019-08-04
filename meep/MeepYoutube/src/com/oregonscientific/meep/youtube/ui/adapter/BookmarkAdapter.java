package com.oregonscientific.meep.youtube.ui.adapter;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.youtube.R;
import com.oregonscientific.meep.youtube.database.Bookmark;
import com.oregonscientific.meep.youtube.ui.fragment.RightMenuFragment;

public class BookmarkAdapter extends CursorAdapter {
	private LayoutInflater inflater;
	private Context mContext;
	private int resourceId;

	public BookmarkAdapter(Context context,int resourceId, Cursor c) {
		super(context, c);
		mContext = context;
		inflater = LayoutInflater.from(context);
		this.resourceId = resourceId;
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		View v = inflater.inflate(resourceId, parent, false);
		ViewHolder viewHolder = new ViewHolder();
		viewHolder.text = (TextView) v.findViewById(R.id.webText);
		viewHolder.image = (ImageView) v.findViewById(R.id.webImage);
		viewHolder.youtubeObject = new YoutubeObject();
		v.setTag(viewHolder);
		return v;
	}

	@Override
	public void bindView(View v, Context context, Cursor c) {
		if(c!=null)
		{
			ViewHolder holder = (ViewHolder) v.getTag();
			String url = c.getString(c.getColumnIndexOrThrow(Bookmark.URL_FIELD_NAME));
			String name = c.getString(c.getColumnIndexOrThrow(Bookmark.NAME_FIELD_NAME));
			int id = c.getInt(c.getColumnIndexOrThrow(Bookmark.ID_FIELD_NAME));
			holder.text.setText(name);
			holder.youtubeObject.setUrl(url);
			holder.youtubeObject.setName(name);
			holder.youtubeObject.setId(id);
		}
	}
	
}
