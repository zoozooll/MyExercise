package com.oregonscientific.meep.meepmusic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.oregonscientific.meep.ServiceManager;
import com.oregonscientific.meep.database.table.TableBlacklist;
import com.oregonscientific.meep.global.BadWordChecker;
import com.oregonscientific.meep.meepmusic.GridViewFragment.LoadImagesThread;
import com.oregonscientific.meep.meepmusic.MessageReceiver.OnMessageReceivedListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

public class FullListViewActivity extends Activity{
	//Fragment gridViewFrag,snakeFrag;
	public static final String STATE_FRAG_GRID = "stackFragGird";
	public static final String STATE_FRAG_SNACK = "stackFragSnack";
	// Add by aaronli at May22,2013
	/**	The fragment's tag. used in saveinstance */
	private static final String STATE_TAG = "tag";
	private MessageReceiver mMsgReceiver = null;
	private String[] blackListWord;
	private LoadImagesThread musicLoadThread = null;
	private BadWordChecker badWordChecker = new BadWordChecker();
	private FragmentManager mFragmentManager;
	private String mCurrentTag;
	private Map<String, Fragment> mFragments;
	private SharedPreferences sp;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frags);
		
		// Modified by aaronli at May22,2013
		/*mFragmentManager = getFragmentManager();
		//gridViewFrag = mFragmentManager.findFragmentById(R.id.gridViewFrag);
		//snakeFrag = mFragmentManager.findFragmentById(R.id.snakeFrag);
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
        ΪFragment���õ��뵭��Ч��Android��������ʾ������}�����Դ��android�ڲ���Դ���������ֶ����塣
        //ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out); 

        if (gridViewFrag.isHidden()) {
            ft.show(snakeFrag);
            ft.hide(gridViewFrag);
        } else {
        	ft.show(gridViewFrag);
        	ft.hide(snakeFrag);
        }
        ft.commit();*/
	
		mFragments = new HashMap<String, Fragment>();
		mFragmentManager = getFragmentManager();
		/*if (savedInstanceState != null) {
			mCurrentTag = savedInstanceState.getString(STATE_TAG);
		}
		Log.d("MeepMusic", "STATE_FRAG "+mCurrentTag);*/
		sp = getSharedPreferences("MeepMusic", Context.MODE_PRIVATE);
		mCurrentTag = sp.getString(STATE_TAG, STATE_FRAG_GRID);
		/*if (mCurrentTag == null) {
			mCurrentTag = STATE_FRAG_GRID;
		}*/
		showDetails(mCurrentTag);
		// end
        
        mMsgReceiver = new MessageReceiver(this);
		mMsgReceiver.setOnMessageReceivedListener(new OnMessageReceivedListener() {

			@Override
			public void onQueryMusicBlackListReceived(List<TableBlacklist> musicBlacklist) {
				// TODO Auto-generated method stub
				if (musicBlacklist.size() != 0) {
					TableBlacklist blacklist = musicBlacklist.get(0);

					if (blacklist.getListType().toString().equals("bypass")) {
//						byPassWord = blacklist.getListEntry();
//						if (byPassWord != null){
//							for (int i = 0; i < byPassWord.length; i++) {
//	
//								Log.d("photo", "photo bypass received: " + byPassWord[i]);
//							}
//						}
					} else {
						blackListWord = blacklist.getListEntry();
						badWordChecker.setBlacklist(blackListWord);	
//						for (int i = 0; i < blackListWord.length; i++) {
//							Log.d("photo", "photo blacklist received: "	+ blackListWord[i]);
//						}
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
		Log.d("MeepMusic", "onStart");
		super.onStart();
	}

	@Override
	protected void onResume() {
		Log.d("MeepMusic", "onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		Log.d("MeepMusic", "onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		Log.d("MeepMusic", "onStop");
		super.onStop();
	}

	public void showDetails(String tag) {
		// Instantiate a new fragment.
		/*if (mCurrentTag.equals(tag)) {
			return;
		}*/
		mCurrentTag = tag;
/*		Bundle bundle = new Bundle();
		bundle.putString("tag", tag);*/
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
	    // Add the fragment to the activity, pushing this transaction
	    // on to the back stack.
	    FragmentTransaction ft = getFragmentManager().beginTransaction();
	   // ft.setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out); 
	    ft.replace(R.id.layout_frags, fragment);
	   // ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	    //ft.addToBackStack(null);
	    ft.commit();
	}
	
	@Override
	protected void onDestroy() {
		sp.edit().putString(STATE_TAG, mCurrentTag).commit();
		mMsgReceiver.close();
		super.onDestroy();
		ServiceManager.unbindServices(this);
	}
	
	// add onSaveInstanceState and saved the key STATE_TAG,so that next
	// enter the acitivity should know the state is in GridFragment or SnackFragment.
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		Log.d("MeepMusic", "onSaveInstanceState "+mCurrentTag);
		outState.putString(STATE_TAG, mCurrentTag);
	}

}
	
