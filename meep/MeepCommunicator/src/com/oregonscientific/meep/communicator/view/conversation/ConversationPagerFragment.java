package com.oregonscientific.meep.communicator.view.conversation;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.R;

/**
 * Fragment of conversations
 */
public class ConversationPagerFragment extends Fragment {
	
	private String TAG = ConversationPagerFragment.class.getSimpleName();
	
	private Vector<Conversation> conversationList;
	
	private LinearLayout conversationOne;
	private LinearLayout conversationTwo;
	private LinearLayout conversationThree;
	
	public static ConversationPagerFragment newInstance(
			Vector<Conversation> conversations,
			int position) {
		
		ConversationPagerFragment pageFragment = new ConversationPagerFragment();
		pageFragment.setConversationList(conversations);
		
		Bundle bundle = new Bundle();
		bundle.putInt("position", position);
		pageFragment.setArguments(bundle);
		
		return pageFragment;
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.conversation_pager_view, container, false);
		conversationOne = (LinearLayout) view.findViewById(R.id.conversation_one);
		conversationTwo = (LinearLayout) view.findViewById(R.id.conversation_two);
		conversationThree = (LinearLayout) view.findViewById(R.id.conversation_three);
		
		int position = getArguments().getInt("position");
		setConversationTab(getActivity(), conversationList, position);
		
		return view;
	}
	
	/**
	 * Get the parent view pager
	 * @return parent view pager
	 */
	private ConversationViewPager getViewPager() {
		return (ConversationViewPager) getParentFragment().getView().findViewById(R.id.conversation_list);
	}
	
	/**
	 * Set the conversation list
	 * @param conversations list of conversation
	 */
	private void setConversationList(Vector<Conversation> conversations) {
		conversationList = conversations;
	}
	
	/**
	 * Set the view of conversations
	 * @param context current context
	 * @param conversationList list of active conversation
	 * @param position current index of conversation
	 */
	private void setConversationTab(
			final Context context,
			final Vector<Conversation> conversationList,
			final int position) {
		
		// get the name of friend currently chatting with
		SharedPreferences settings = context.getSharedPreferences("MEEP Communicator", 0);
		String friendCurrentChatting = settings.getString("FriendCurrentChatting", "");
		
		// set view for first conversation in view
		if (conversationList.size() > position) {
			BaseConversation firstConversationView;

			if (friendCurrentChatting.equals(conversationList.get(position).getFriend().getAccountId())) {
				firstConversationView = new BaseConversation(context, conversationList.get(position), true);
			} else {
				firstConversationView = new BaseConversation(context, conversationList.get(position), false);
			}
			conversationOne.removeAllViews();
			conversationOne.addView(firstConversationView);
			((BaseConversation) conversationOne.getChildAt(0)).getView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					ConversationViewPager pager = getViewPager();
					if (pager != null) {
						pager.onItemClick(position);
					}
					
					SharedPreferences settings = context.getSharedPreferences("MEEP Communicator", 0);
					SharedPreferences.Editor preferencesEditor = settings.edit();
					preferencesEditor.putString("FriendCurrentChatting", conversationList.get(position).getFriend().getAccountId());
					preferencesEditor.commit();
					
					getViewPager().getAdapter().notifyDataSetChanged();
					
				}
			});
			
			((BaseConversation) conversationOne.getChildAt(0)).getCloseButtonView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					conversationList.remove(position);
					getViewPager().getAdapter().notifyDataSetChanged();
				}
			});
		} else {
			conversationOne.removeAllViews();
		}
		
		// set view for second conversation in view
		if (conversationList.size() > position + 1) {
			BaseConversation secondConversationView;
			if (friendCurrentChatting.equals(conversationList.get(position + 1).getFriend().getAccountId())) {
				secondConversationView = new BaseConversation(context, conversationList.get(position + 1), true);
			} else {
				secondConversationView = new BaseConversation(context, conversationList.get(position + 1), false);
			}
			conversationTwo.removeAllViews();
			conversationTwo.addView(secondConversationView);
			((BaseConversation) conversationTwo.getChildAt(0)).getView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					ConversationViewPager pager = getViewPager();
					if (pager != null) {
						pager.onItemClick(position + 1);
					}
					
					SharedPreferences settings = context.getSharedPreferences("MEEP Communicator", 0);
					SharedPreferences.Editor preferencesEditor = settings.edit();
					preferencesEditor.putString("FriendCurrentChatting", conversationList.get(position + 1).getFriend().getAccountId());
					preferencesEditor.commit();
					
					getViewPager().getAdapter().notifyDataSetChanged();
					
				}
			});
			
			((BaseConversation) conversationTwo.getChildAt(0)).getCloseButtonView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					conversationList.remove(position + 1);
					getViewPager().getAdapter().notifyDataSetChanged();
				}
			});
		} else {
			conversationTwo.removeAllViews();
		}
		
		// set view for third conversation in view
		if (conversationList.size() > position + 2) {
			BaseConversation thirdConversationView;
			if (friendCurrentChatting.equals(conversationList.get(position + 2).getFriend().getAccountId())) {
				thirdConversationView = new BaseConversation(context, conversationList.get(position + 2), true);
			} else {
				thirdConversationView = new BaseConversation(context, conversationList.get(position + 2), false);
			}
			conversationThree.removeAllViews();
			conversationThree.addView(thirdConversationView);
			((BaseConversation) conversationThree.getChildAt(0)).getView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					
					ConversationViewPager pager = getViewPager();
					if (pager != null) {
						pager.onItemClick(position + 2);
					}
					
					SharedPreferences settings = context.getSharedPreferences("MEEP Communicator", 0);
					SharedPreferences.Editor preferencesEditor = settings.edit();
					preferencesEditor.putString("FriendCurrentChatting", conversationList.get(position + 2).getFriend().getAccountId());
					preferencesEditor.commit();
					
					getViewPager().getAdapter().notifyDataSetChanged();
					
				}
			});
			
			((BaseConversation) conversationThree.getChildAt(0)).getCloseButtonView().setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View view) {
					conversationList.remove(position + 2);
					getViewPager().getAdapter().notifyDataSetChanged();
				}
			});
		} else {
			conversationThree.removeAllViews();
		}
		
	}
	
}