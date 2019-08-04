package com.oregonscientific.meep.movie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.movie.MessageReceiver.OnMessageReceivedListener;

public class FullListViewActivity extends Activity{
	//Fragment gridViewFrag,snakeFrag;
	public static final String STATE_FRAG_GRID = "stackFragGird";
	public static final String STATE_FRAG_SNACK = "stackFragSnack";
	private static final String STATE_TAG = "tag";
	private MessageReceiver mMsgReceiver = null;
	private String[] blackListWord;
	//private LoadImagesThread moviesLoadThread = null;
	private FragmentManager mFragmentManager;
	private String mCurrentTag;
	private Map<String, Fragment> mFragments;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frags);
		
		mFragments = new HashMap<String, Fragment>();
		mFragmentManager = getFragmentManager();
		sp = getSharedPreferences("MeepMovie", Context.MODE_PRIVATE);
		mCurrentTag = sp.getString(STATE_TAG, STATE_FRAG_GRID);
		
		showDetails(mCurrentTag);

		mMsgReceiver = new MessageReceiver(this);
		mMsgReceiver.setOnMessageReceivedListener(new OnMessageReceivedListener() {
			@Override
			public void onQueryMovieBlackListReceived(List<TableBlacklist> movieBlacklist) {
				// TODO Auto-generated method stub
				if (movieBlacklist.size() != 0) {
					TableBlacklist blacklist = movieBlacklist.get(0);

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
	protected void onStart() {
		//initHandler();
		super.onStart();
	}

	@Override	
	protected void onDestroy() {
		sp.edit().putString(STATE_TAG, mCurrentTag).commit();
		mMsgReceiver.close();
		ServiceManager.unbindServices(this);
		super.onDestroy();
	}
	
	public void showDetails(String tag) {
		// Instantiate a new fragment.
		/*if (mCurrentTag.equals(tag)) {
			return;
		}*/
		mCurrentTag = tag;
		Fragment fragment = mFragments.get(tag);
	    if (fragment == null) {
	    	if (STATE_FRAG_SNACK.equals(tag)) {
	    		fragment = SnakeFragment.newInstance(tag);
	    	} else if (STATE_FRAG_GRID.equals(tag)) {
	    		fragment = GridViewFragment.newInstance(tag);
	    	}
	    	//mFragmentManager.putFragment(fragment.getArguments(), tag, fragment);
	    	mFragments.put(tag, fragment);
	    }
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	    ft.replace(R.id.layout_frags, fragment);
	    ft.commit();
	}
	
}


