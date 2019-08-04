package com.oregonscientific.meep.communicator.activity;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Vector;

import org.arabidopsis.ahocorasick.AhoCorasick;
import org.arabidopsis.ahocorasick.SearchResult;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ToggleButton;

import com.j256.ormlite.dao.ForeignCollection;
import com.oregonscientific.meep.communicator.Conversation;
import com.oregonscientific.meep.communicator.ConversationMessage;
import com.oregonscientific.meep.communicator.Friend;
import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.User;
import com.oregonscientific.meep.communicator.view.conversation.ConversationMessageAdapter;
import com.oregonscientific.meep.communicator.view.conversation.ConversationPagerAdapter;
import com.oregonscientific.meep.communicator.view.conversation.ConversationPagerFragment;
import com.oregonscientific.meep.communicator.view.conversation.ConversationViewPager;
import com.oregonscientific.meep.communicator.view.conversation.Emoticon;
import com.oregonscientific.meep.communicator.view.conversation.EmoticonAdapter;

/**
 * Conversation Fragment for MEEP Communicator
 */
public class ConversationFragment extends DialogFragment implements ConversationViewPager.OnClickListener {
	
	private final String TAG = getClass().getSimpleName();
	private static Vector<Conversation> conversationList = new Vector<Conversation>();
	private String meepTag = "me";
	private Vector<ConversationMessage> messageList = null;
	private ConversationMessageAdapter adapter = null;
	private IConversationInterface mListener;
	private int mConversationPosition = 0;
	private final int MAX_CONVERSATION_MESSAGE = 10;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}
	
	/**
	 * Creates a fragment
	 */
	public static ConversationFragment newInstance(Friend friend) {
		ConversationFragment myDialogFragment = new ConversationFragment();
		Bundle bundle = new Bundle();
		bundle.putParcelable("friend", friend);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}
	
	/**
	 * Creates a fragment
	 */
	public static ConversationFragment newInstance() {
		ConversationFragment myDialogFragment = new ConversationFragment();
		return myDialogFragment;
	}
	
	/**
	 * Insert a conversation as the first conversation in the conversation list.
	 * If the conversation already exists, move it to the first position
	 * @param friend the friend in conversation
	 * @param conversation conversation having with the friend
	 */
	public void addConversation(
			Context context,
			Friend friend,
			Conversation conversation) {
		for (Iterator<Conversation> iterator = conversationList.iterator(); iterator.hasNext();) {
			Conversation existingConversation = iterator.next();
			if (existingConversation.getFriend().getAccountId().equals(friend.getAccountId())) {
				iterator.remove();
			}
		}
		conversationList.insertElementAt(conversation, 0);
		savePreferences(context, conversation);
		fillMessagesToList(context);
	}
	
	/**
	 * Clear the conversation list
	 */
	public static void clearConversations() {
		conversationList.clear();
	}
	
	/**
	 * Add a list of messages to current message list
	 * @param messages messages to add into message list
	 */
	public void addMessageToMessageList(Vector<ConversationMessage> messages) {
		if (messageList != null && adapter != null) {
			for (ConversationMessage message : messages) {
				messageList.add(message);
			}
			adapter.notifyDataSetChanged();
			ListView conversationMessageList = (ListView) getDialog().findViewById(R.id.conversations);
			conversationMessageList.setSelection(adapter.getCount() - 1);
			
		}
	}
	
	/**
	 * Add a message to current message list
	 * @param messages messages to add into message list
	 */
	public void addMessageToMessageList(ConversationMessage message) {
		if (messageList != null && adapter != null) {
			messageList.add(message);
			adapter.notifyDataSetChanged();
			ListView conversationMessageList = (ListView) getDialog().findViewById(R.id.conversations);
			// scroll to bottom
			conversationMessageList.setSelection(adapter.getCount() - 1);
			
		}
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {
		
		// inflate view
		final View view = inflater.inflate(R.layout.conversation_view, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		// load conversations
		final ConversationViewPager conversationViewPager = (ConversationViewPager) view.findViewById(R.id.conversation_list);
		conversationViewPager.setScrollingEnabled(false);
		conversationViewPager.setOnItemClickListener(this);
		
		// set left and right arrows
		final ImageView leftArrow = (ImageView) view.findViewById(R.id.arrow_left);
		leftArrow.setVisibility(View.INVISIBLE);
		final ImageView rightArrow = (ImageView) view.findViewById(R.id.arrow_right);
		leftArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if (conversationViewPager.getCurrentItem() > 0) {
					conversationViewPager.setCurrentItem(conversationViewPager.getCurrentItem() - 1, false);
				}
			}
		});
		
		rightArrow.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if (conversationViewPager.getCurrentItem() < conversationViewPager.getAdapter().getCount()) {
					conversationViewPager.setCurrentItem(conversationViewPager.getCurrentItem() + 1, false);
				}
			}
		});
		
		final ConversationPagerAdapter conversationPagerAdapter = new ConversationPagerAdapter(getChildFragmentManager(), this, conversationList);
		conversationViewPager.setAdapter(conversationPagerAdapter);
		conversationViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageScrollStateChanged(int state) {
			}
			
			@Override
			public void onPageScrolled(
					int position,
					float positionOffset,
					int positionOffsetPixels) {
			}
			
			@Override
			public void onPageSelected(int position) {
				
				if (position <= 0) {
					leftArrow.setVisibility(View.INVISIBLE);
					if (conversationViewPager.getAdapter().getCount() > 1) {
						rightArrow.setVisibility(View.VISIBLE);
					}
				} else if (position >= conversationViewPager.getAdapter().getCount() - 1) {
					leftArrow.setVisibility(View.VISIBLE);
					rightArrow.setVisibility(View.INVISIBLE);
				} else {
					leftArrow.setVisibility(View.VISIBLE);
					rightArrow.setVisibility(View.VISIBLE);
				}
				
			}
		});
		
		// load chat bar
		final EditText text = (EditText) view.findViewById(R.id.text);
		final ImageView sendButton = (ImageView) view.findViewById(R.id.send_button);
		GridView emoticonsGridView = (GridView) view.findViewById(R.id.emoticons);
		final ToggleButton emoticonButton = (ToggleButton) view.findViewById(R.id.emoticon);
		
		sendButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					sendButton.setImageDrawable(getResources().getDrawable(R.drawable.message_bar_send_highlight));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					sendButton.setImageDrawable(getResources().getDrawable(R.drawable.message_bar_send_normal));
				}
				return false;
			}
		});
		
		sendButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				
				if (emoticonButton.isChecked()) {
					emoticonButton.setChecked(false);
				}
				
				String messageToSend = text.getEditableText().toString();
				text.setText(null);
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
				
				if (conversationList != null && conversationList.size() > mConversationPosition) {
					Conversation conversation = conversationList.get(mConversationPosition);
					if (conversation != null) {
						
						Friend friend = conversation.getFriend();
						sendMessage(friend, messageToSend);
						
					}
				}
			}
		});
		
		emoticonsGridView.setAdapter(new EmoticonAdapter(getActivity(), Emoticon.getEmoticonCodes(getActivity())));
		emoticonsGridView.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(
					AdapterView<?> parent,
					View view,
					int position,
					long id) {
				
				Editable chatContent = text.getText();
				int startIndex = text.getSelectionStart();
				int endIndex = text.getSelectionEnd();
				CharSequence textBeforeCursor = chatContent.subSequence(0, startIndex);
				CharSequence textAfterCursor = chatContent.subSequence(endIndex, text.getText().length());
				String imgId = (String) view.getTag();
				
				updateCanvas(getActivity(), text, textBeforeCursor + imgId + textAfterCursor);
			}
		});
		
		emoticonButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					view.findViewById(R.id.emoticon_section).setVisibility(View.VISIBLE);
				} else {
					view.findViewById(R.id.emoticon_section).setVisibility(View.INVISIBLE);
				}
			}
		});
		
		if (adapter != null) {
			ListView conversationMessageList = (ListView) view.findViewById(R.id.conversations);
			if (conversationMessageList != null) {
				conversationMessageList.setAdapter(adapter);
			}
		}
		
		return view;
	}
	
	/**
	 * Update the canvas of an EditText view by replacing emoticon codes with
	 * emoticons
	 * @param context current context
	 * @param editText the EditText view
	 * @param str the string in EditText
	 */
	@SuppressWarnings("unchecked")
	public void updateCanvas(Context context, EditText editText, CharSequence str) {
		int cursorPosition = editText.getSelectionStart();
		editText.setText("");
		int index = 0;
		
		AhoCorasick tree = Emoticon.getTree(context);
		if (str != null && tree != null) {
			Iterator<?> itr = tree.search(str.toString().toLowerCase().getBytes());
			while (itr.hasNext()) {
				// get the string content in front of emoticon found
				SearchResult rst = (SearchResult) itr.next();
				Set<String> outputs = rst.getOutputs();
				
				int startIndex = rst.getLastIndex() - Emoticon.EMOTICON_CODE_LENGTH;
				int endIndex = rst.getLastIndex();
				CharSequence textBeforeCursor = str.subSequence(index, startIndex);
				editText.append(textBeforeCursor);
				
				// draw emoticon
				for (String output : outputs) {
					Bitmap bitmap = Emoticon.getEmoticon(context, output);
					if (bitmap != null) {
						ImageSpan imageSpan = new ImageSpan(getActivity(), bitmap);
						SpannableStringBuilder builder = new SpannableStringBuilder(output);
						builder.setSpan(imageSpan, 0, Emoticon.EMOTICON_CODE_LENGTH, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
						editText.append(builder);
					}
				}
				index = endIndex;
			}
			if (index < str.length()) {
				editText.append(str.subSequence(index, str.length()));
			}
			editText.setSelection(cursorPosition + Emoticon.EMOTICON_CODE_LENGTH);
		}
	}
	
	@Override
	public void dismiss() {
		if (getActivity() instanceof CommunicatorActivity) {
			((CommunicatorActivity) getActivity()).setGridViewEnabled(true);
			((CommunicatorActivity) getActivity()).setAddFriendsButtonEnabled(true);
		}
		super.dismiss();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (getActivity() instanceof CommunicatorActivity) {
			((CommunicatorActivity) getActivity()).setGridViewEnabled(true);
			((CommunicatorActivity) getActivity()).setAddFriendsButtonEnabled(true);
		}
		super.onDismiss(dialog);
	}
	
	
	/**
	 * Notify the activity to send a message
	 * 
	 * @param friend The friend object in the current conversation
	 * @param message the message to be sent out
	 */
	private void sendMessage(Friend friend, String message) {
		
		if (friend == null) {
			Log.e(TAG, "missing friend object");
			return;
		}
		
		if (message.length() > 0) {
			
			if (conversationList != null && conversationList.size() > 0) {
				// search through the conversationList
				for (Conversation conversation : conversationList) {
					
					Friend listFriend = conversation.getFriend();
					try {
						listFriend.refresh();
						if (listFriend.getAccountId().equals(friend.getAccountId())) {
							
							ConversationMessage cm = new ConversationMessage(conversation, null, message, null, Boolean.valueOf(false), true, ConversationMessage.NOTIFICATION_EMPTY_ID);
							
							if (mListener != null) {
								mListener.onSendButtonPressed(thisFragment(), message, cm);
							}
							break;
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	/**
	 * Create a listener that listen to the different action in this conversation fragment
	 * 
	 * @param listener The listner who want to listen to the action in this conversation fragment, 
	 * 		currently only Communicator Activity registered	 
	 * */
	public void setListener(IConversationInterface listener) {
		mListener = listener;
	}
	
	/**
	 * Retrieve the listener 
	 * 
	 * @return the Listener which implements the IConversationInterface
	 */
	public IConversationInterface getListener() {
		return mListener;
	}
	
	
	/**
	 * A method to return this
	 * 
	 * @return this
	 */
	private ConversationFragment thisFragment() {
		return this;
	}
	
	
	@Override
	public void onClick(int position) {
		
		mConversationPosition = position;
		fillMessagesToList(getActivity());
		if (mListener != null) {
			mListener.onConversationTabChanged(this, conversationList.get(mConversationPosition));
		}
	}
	
	/**
	 * Create a conversation message adapter, and update message list UI with messages 
	 * 
	 * @param context a CommunicatorActivity instance
	 */
	private void fillMessagesToList(Context context) {
		
		messageList = new Vector<ConversationMessage>();
		
		Conversation conversation = conversationList.elementAt(mConversationPosition);

		if (conversation != null) {
			CommunicatorActivity activity = (CommunicatorActivity) context;
			
			List<ConversationMessage> messages = activity.getConversationMessages(conversation, MAX_CONVERSATION_MESSAGE);
			Collections.reverse(messages);
			try {
				if (messages != null) {
					for (ConversationMessage m : messages) {
						m.refresh();
						m.getConversation().refresh();
						m.getConversation().getFriend().refresh();
						messageList.add(m);
					}
				}
			} catch (SQLException ex) {
				Log.e(TAG, "fail to refresh conversation messages");
			}
			
			User user = activity.getUser();
			if (user != null) {
				adapter = new ConversationMessageAdapter(context, messageList, user);
				Dialog dialog = getDialog();
				if (dialog != null) {
					ListView conversationMessageList = (ListView) dialog.findViewById(R.id.conversations);
					if (conversationMessageList != null) {
						conversationMessageList.setAdapter(adapter);
					}
				}
			}
		}
	}

	/**
	 * Save the currently chating component
	 * 
	 * @param context 
	 * @param conversation The first/top conversation in the tab 
	 */
	
	private void savePreferences(Context context, Conversation conversation) {
		// set and load conversation messages with first friend
		SharedPreferences settings = context.getSharedPreferences("MEEP Communicator", 0);
		SharedPreferences.Editor preferencesEditor = settings.edit();
		preferencesEditor.putString("FriendCurrentChatting", conversation.getFriend().getAccountId());
		preferencesEditor.commit();
	}
	
	
	/**
	 * A method for activity to notify a message has been received during chatting
	 * 
	 * @param message The received conversation message
	 */
	public void onReceiveConversationMessage(ConversationMessage message) {
		
		if (message.getConversation().getId() == conversationList.get(mConversationPosition).getId()) {
			addMessageToMessageList(message);
		}
		
	}
	
}
