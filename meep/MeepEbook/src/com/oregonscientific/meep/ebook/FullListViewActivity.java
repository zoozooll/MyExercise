package com.oregonscientific.meep.ebook;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.ebook.GridViewFragment.LoadImagesThread;
import com.oregonscientific.meep.ebook.MessageReceiver.OnMessageReceivedListener;
import com.oregonscientific.meep.global.BadWordChecker;

public class FullListViewActivity extends Activity{	
	public static final String STATE_FRAG_GRID = "stackFragGird";
	public static final String STATE_FRAG_SNACK = "stackFragSnack";
	private static final String STATE_TAG = "tag";
	private LoadImagesThread ebooksLoadThread = null;
	//broadcast message
	private MessageReceiver mMsgReceiver = null;
	private String[] blackListWord;
//	private BadWordChecker badWordChecker = new BadWordChecker();	
	private FragmentManager mFragmentManager;
	private String mCurrentTag;
	private Map<String,Fragment> mFragments;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frags);
		
		mFragments = new HashMap<String,Fragment>();
		mFragmentManager = getFragmentManager();
		sp =getSharedPreferences("MeepEbook",Context.MODE_PRIVATE);
		mCurrentTag = sp.getString(STATE_TAG,STATE_FRAG_GRID);
		showDetails(mCurrentTag);
		
		mMsgReceiver = new MessageReceiver(this);
		mMsgReceiver.setOnMessageReceivedListener(new OnMessageReceivedListener() {

			@Override
			public void onQueryEbookBlackListReceived(List<TableBlacklist> ebookBlacklist) {
				// TODO Auto-generated method stub
				if (ebookBlacklist.size() != 0) {
					TableBlacklist blacklist = ebookBlacklist.get(0);

					if (blacklist.getListType().toString().equals("bypass")) {
					} else {
						blackListWord = blacklist.getListEntry();
					}
				}
			}
		});
		if (ServiceController.getUser() == null) {
			ServiceController.getAccount(this);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}
	public void showDetails(String tag){
		mCurrentTag = tag;
		Fragment fragment = mFragments.get(tag);
		if (fragment == null) {
			if (STATE_FRAG_SNACK.equals(tag)) {
				fragment = SnakeFragment.newInstance(tag);
			} else if (STATE_FRAG_GRID.equals(tag)) {
				fragment = GridViewFragment.newInstance(tag);
			}
			mFragments.put(tag, fragment);
		}
		FragmentTransaction ft = getFragmentManager().beginTransaction();
		ft.replace(R.id.layout_frags, fragment);
		ft.commit();
	}
	
	@Override
	protected void onDestroy() {
		sp.edit().putString(STATE_TAG, mCurrentTag).commit();
		mMsgReceiver.close();
		ServiceManager.unbindServices(this);
		super.onDestroy();
	}
	
	// add onSaveInstanceState and saved the key STATE_TAG,so that next
	// enter the acitivity should know the state is in GridFragment or SnackFragment.
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("MeepEbook", "onSaveInstanceState "+mCurrentTag);
		outState.putString(STATE_TAG, mCurrentTag);
	}
	
}
