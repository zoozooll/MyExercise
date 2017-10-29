package com.iskyinfor.duoduo.ui.usercenter;

import com.iskyinfor.duoduo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class AccountAdapter extends BaseAdapter {
	private Context context = null;
	private LayoutInflater factory = null;
	
	public AccountAdapter(Context c) 
	{
		context =  c;
		factory = LayoutInflater.from(context);
	}

	@Override
	public int getCount()
	{
		return 6;
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
	public View getView(int position, View view, ViewGroup parent)
	{
		ViewHolder holder = null;
		if(view == null)
		{
			view = factory.inflate(R.layout.myself_account_items, null);
			holder = new ViewHolder();
			holder.sequence = (TextView) view.findViewById(R.id.user_seq_content);
			holder.username = (TextView) view.findViewById(R.id.user_name_content);
			holder.date = (TextView) view.findViewById(R.id.user_date_content);
			
			holder.type = (TextView) view.findViewById(R.id.user_type_content);
			holder.total = (TextView) view.findViewById(R.id.user_money_content);
			holder.balance = (TextView) view.findViewById(R.id.user_balance_content);
		    view.setTag(holder);
		}
		else
		{
			view.getTag();
		}
		
		return view;
	}

	static class ViewHolder
	{
		TextView sequence;
		TextView username;
		TextView date;
		TextView type;
		TextView total ;
		TextView balance; //余额 
	}
	
}
