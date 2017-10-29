package com.iskyinfor.duoduo.ui.downloader;

import com.iskyinfor.duoduo.ui.IndexActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ListView;

public class MyselfResourceActivity extends ListActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState) 
    {
    	super.onCreate(savedInstanceState);
    	
    	initView();
    }

	private void initView()
	{
    	final ListView listView = getListView();
    	listView.setItemsCanFocus(false);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
    	MyselfResourceAdapter adapter = new MyselfResourceAdapter(this);
    	setListAdapter(adapter);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		Log.i("liu", "===:"+keyCode);
		Log.i("liu", "event==:"+event.getKeyCode());
		switch (keyCode) {
		case KeyEvent.KEYCODE_HOME:
			Intent intent=new Intent();
			intent.setClass(MyselfResourceActivity.this, IndexActivity.class);
			startActivity(intent);
			finish();
			break;
		default:
			break;
		}
		return false;
	}
}
