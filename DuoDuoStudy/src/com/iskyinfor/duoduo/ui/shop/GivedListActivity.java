package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 赠送列表
 * @author zhoushidong
 *
 */
public class GivedListActivity extends Activity {
	
	private ArrayList<String> datas = null;
	private ListView givedList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.givedlist_activity);
		findView();
		getData();
		setValue();
	}
	
	private void findView() {
		givedList = (ListView) findViewById(R.id.givedList);
		
	}
	
	private void setValue() {
		givedList.setCacheColorHint(0);
		givedList.setAdapter(new GivedAdapter(this));
	}
	
	private void getData() {
		datas = new ArrayList<String>();
		for (int i = 0; i < 20 ; i++) {
			datas.add("=--------");
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	public class GivedAdapter extends BaseAdapter {
		
		private LayoutInflater inflater;
		
		
		public GivedAdapter(Context context) {
			inflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FolderView holder = null;

			if (convertView == null) {
				holder = new FolderView();
				convertView = inflater.inflate(R.layout.gived_list_items, null);
				convertView.setTag(holder);

			} else {
				holder = (FolderView) convertView.getTag();
			}
			
			return convertView;
		}
		
		final class FolderView {
			ImageView givedBookImage;
			TextView givedBookName;
			TextView givedType;
			TextView givedPerson;
			TextView givedTalk;
		}
		
	} 
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}
}
