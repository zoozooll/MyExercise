package com.iskyinfor.duoduo.ui.talkgarden;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

public class TalkLearncircleAdapter extends PageListAdapter<TalkGarden> {
	private LayoutInflater inflater;
	private Context context;
	
	public TalkLearncircleAdapter(Context context,
			ArrayList<TalkGarden> arrayListe) {
		super(context, arrayListe);
		// TODO Auto-generated constructor stub
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View initItemView(View convertView, Object object, int position) {
		// TODO Auto-generated method stub
		TalkGarden talkgarden = (TalkGarden) object;
		
		ViewHolder holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.talk_learncircles_child, null);		
		holder.talkName = (TextView) convertView.findViewById(R.id.talklearncircle_name);
		holder.talkName.setText(talkgarden.getTalkName());
		
		
		return convertView;
	}
	private class ViewHolder {
		public TextView talkName;
		
	}
}
