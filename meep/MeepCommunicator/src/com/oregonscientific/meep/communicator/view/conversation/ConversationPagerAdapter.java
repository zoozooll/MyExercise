package com.oregonscientific.meep.communicator.view.conversation;

import java.util.Vector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.activity.ConversationFragment;

/**
 * Pager Adapter for showing list of conversations
 */
public class ConversationPagerAdapter extends FragmentStatePagerAdapter {
	
	private Vector<Conversation> conversationList;
	private ConversationFragment converationFragment;
	
	public ConversationPagerAdapter(FragmentManager fragmentManager, 
			ConversationFragment fragment, 
			Vector<Conversation> conversationList) {
		super(fragmentManager);
		this.conversationList = conversationList;
		this.converationFragment = fragment;
	}
	
	@Override
	public int getItemPosition(Object object) {
		return POSITION_NONE;
	}
	
	@Override
	public Fragment getItem(int position) {
		
		Fragment fragment = ConversationPagerFragment.newInstance(conversationList, position);
		return fragment;
		
	}
	
	@Override
	public void finishUpdate(ViewGroup container) {
		
		super.finishUpdate(container);
		
		ConversationViewPager pager = (ConversationViewPager) container;
		// set right arrow to invisible when there is no additional conversation
		// at the right side
		if (pager != null && pager.getCurrentItem() >= pager.getAdapter().getCount() - 1) {
			if (pager.getParent() != null) {
				ImageView rightArrow = (ImageView) ((View) pager.getParent()).findViewById(R.id.arrow_right);
				if (rightArrow != null) {
					rightArrow.setVisibility(View.INVISIBLE);
				}
			}
		}
		// set left arrow to invisible when there is no additional conversation
		// at the left side
		if (pager != null && pager.getCurrentItem() <= 0) {
			if (pager.getParent() != null) {
				ImageView leftArrow = (ImageView) ((View) pager.getParent()).findViewById(R.id.arrow_left);
				if (leftArrow != null) {
					leftArrow.setVisibility(View.INVISIBLE);
				}
			}
		}
		// dismiss fragment if there is no conversation
		if (pager != null && pager.getAdapter().getCount() == 0) {
			converationFragment.dismiss();
		}
		
	}
	
	@Override
	public int getCount() {
		int count = 0;
		switch (conversationList.size()) {
			case 0:
				break;
			case 1:
			case 2:
			case 3:
				count = 1;
				break;
			default:
				count = conversationList.size() - 2;
				break;
		}
		return count;
		// return (int) Math.ceil((double) conversationList.size() / 3);
	}
}
