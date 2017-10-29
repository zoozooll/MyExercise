package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;

import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.User;
import com.iskyinfor.duoduo.R;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;

public class BookShelfGiftqqAdapter extends BaseAdapter {

	static class GridHolder {
		CheckBox chk;
	}

	private LayoutInflater mInflater;
	private ArrayList<User> mData;
	public Activity Acontent;
	public String[] reqUserID;
	private ArrayList userIdList;

	public BookShelfGiftqqAdapter(Activity context, ArrayList<User> data,
			String[] UserID) {
		this.mInflater = LayoutInflater.from(context);
		mData = data;
		Acontent = context;
		reqUserID = UserID;
		userIdList = new ArrayList();
		if (reqUserID != null) {
			for (int i = 0; i < reqUserID.length; i++) {
				userIdList.add(reqUserID[i]);
			}
		}
	}

	@Override
	public int getCount() {
		if (mData != null && mData.size() != 0) {
			return mData.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		return mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		GridHolder holder = null;
		final User itemUser = mData.get(position);
		
		if (convertView == null) {
			holder = new GridHolder();
			convertView = mInflater.inflate(
					R.layout.bookshelf_gift_editlist_child, null);
			holder.chk = (CheckBox) convertView
					.findViewById(R.id.bookshelfgift_listName);

			convertView.setTag(holder);
		} else {
			holder = (GridHolder) convertView.getTag();
		}

		holder.chk.setText(itemUser.getUserName());

		if (userIdList.size() > 0) {
			for (int i = 0; i < userIdList.size(); i++) {
				if (itemUser.getUserId().equals(userIdList.get(i).toString())) {
					itemUser.setUserChecked(true);
					holder.chk.setChecked(true);
					userIdList.remove(userIdList.get(i));
					i--;
				}
			}
		}
		holder.chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				Log.i("zg", "onCheckedChanged=onCheckedChanged==>");
				if (isChecked) {
					Log.i("zg", "isChecked===>" + isChecked);
					itemUser.setUserChecked(true);
				} else {
					itemUser.setUserChecked(false);
				}

			}
		});

		holder.chk.setTag(itemUser);

		return convertView;
	}

}
