package com.oregonscientific.meep.communicator.view;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.oregonscientific.meep.communicator.R;
import com.oregonscientific.meep.communicator.activity.CommunicatorActivity;

/**
 * Pop up Fragment for MEEP Communicator
 */
public class PopUpFragment extends DialogFragment {
	
	private final String TAG = PopUpFragment.class.getSimpleName();
	
	public static final int LOADING_DIALOG_ID = 0;
	public static final int ADD_FRIENDS_DIALOG_ID = 1;
	public static final int SEARCH_FRIENDS_DIALOG_ID = 2;
	public static final int ACCEPT_FRIEND_DIALOG_ID = 3;
	public static final int ERROR_MESSAGE_DIALOG_ID = 4;
	public static final int NETWORK_DIALOG_ID = 5;
	public static final int DELETE_FRIEND_DIALOG_ID = 6;
	public static final int DELETE_FRIEND_SUCCESS_DIALOG_ID = 7;
	public static final int FRIEND_REQUEST_SUCCESS_DIALOG_ID = 8;
	public static final int NETWORK_DIALOG_2_ID = 9;
	public static final int FRIEND_REQUEST_STATUS_DIALOG_ID = 10;
	public static final int FRIEND_REQUEST_MESSAGE_DIALOG_ID = 11;

	public static final String ERROR_ACTION_SEARCH_FRIEND = "search-friend";
	public static final String ERROR_ACTION_ADD_FRIEND = "add-friend";
	
	private IPopupInterface mListener;
	private IDeleteFriendPopUpInterface mDeleteFriendPopUpInterface;
	private int type;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
	}
	
	/**
	 * Creates a pop up
	 * @param title id of pop up
	 */
	public static PopUpFragment newInstance(int type, String args1, String args2) {
		PopUpFragment myDialogFragment = new PopUpFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putString("args1", args1);
		bundle.putString("args2", args2);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}
	
	public static PopUpFragment newInstance(int type, String args1, String args2, String args3) {
		PopUpFragment myDialogFragment = new PopUpFragment();
		Bundle bundle = new Bundle();
		bundle.putInt("type", type);
		bundle.putString("args1", args1);
		bundle.putString("args2", args2);
		bundle.putString("args3", args3);
		myDialogFragment.setArguments(bundle);
		return myDialogFragment;
	}
	
	@Override
	public View onCreateView(
			LayoutInflater inflater,
			ViewGroup container,
			Bundle savedInstanceState) {

		final int type = getArguments().getInt("type");
		
		setType(type);
		
		View view = null;
		switch (type) {
		case ADD_FRIENDS_DIALOG_ID:
			view = initAddFriendFragment(inflater, container);
			break;
		case SEARCH_FRIENDS_DIALOG_ID: 
			view = initSearchFriendFragment(inflater, container);
			break;
		case ACCEPT_FRIEND_DIALOG_ID:
			view = initAcceptFriendFragment(inflater, container);
			break;
		case ERROR_MESSAGE_DIALOG_ID:
			view = initErrorMessageFragment(inflater, container);
			break;
		case NETWORK_DIALOG_ID:
			view = initNetworkFragment(inflater, container);
			break;

		case DELETE_FRIEND_DIALOG_ID:
			view = initDeleteFriendConfirmationFragment(inflater, container);
			break;

		case DELETE_FRIEND_SUCCESS_DIALOG_ID:
			view = initDeleteFriendSuccessFragment(inflater, container);
			break;

		case FRIEND_REQUEST_SUCCESS_DIALOG_ID:
				view = initFriendRequestSuccessFragment(inflater, container);
				break;
				
		case NETWORK_DIALOG_2_ID:
			view = initNetworkFragment2(inflater, container);
			break;
			
		case FRIEND_REQUEST_STATUS_DIALOG_ID:
			view =	initFriendRequestStatusFragment(inflater, container);
			break;

		case FRIEND_REQUEST_MESSAGE_DIALOG_ID:
			view = initFriendRequestMessageFragment(inflater, container);
			break;
		}

		return view;
	}


	private View initAddFriendFragment(LayoutInflater inflater, ViewGroup container) {


		View view = inflater.inflate(R.layout.add_friends_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});

		TextView title = (TextView) view.findViewById(R.id.title);

		String titleText = getResources().getString(R.string.add_friends);
		
		if (titleText.length() > 15) {
			title.setTextSize(title.getTextSize()-5);
		}
		
		title.setText(titleText);

		
		TextView add = (TextView) view.findViewById(R.id.add);
		add.setText(getResources().getString(R.string.add));
		add.setTextSize(45);
		add.setTypeface(Typeface.DEFAULT);
		
		final EditText text = (EditText) view.findViewById(R.id.text);
		String nickname = getArguments().getString("args1");
		text.setHint(getString(R.string.add_friend_hint, nickname));	

		final ImageView addButton = (ImageView) view.findViewById(R.id.add_button);
		
		addButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					addButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_bar_send_highlight));					
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					addButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_bar_send_normal));
				}
				return false;
			}
		});
		
		addButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				addButton.setEnabled(false);

				String messageToSend = text.getEditableText().toString();

				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text.getWindowToken(), 0);

				if (mListener != null) {
					mListener.onAddButtonPressed(thisFragment(), messageToSend);
				} else {
					addButton.setEnabled(true);
				}
				

			}
		});

		return view;
	}


	private View initSearchFriendFragment(LayoutInflater inflater, ViewGroup container) {


		View view = inflater.inflate(R.layout.add_friends_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});

		TextView title = (TextView) view.findViewById(R.id.title);
		String titleText = getResources().getString(R.string.search_friend);
		
		if (titleText.length() > 15) {
			title.setTextSize(title.getTextSize()-5);
		}
		
		title.setText(titleText);

		TextView add = (TextView) view.findViewById(R.id.add);
		add.setText(getResources().getString(R.string.search));		
		
		final TextView message = (TextView) view.findViewById(R.id.message);
		final EditText text = (EditText) view.findViewById(R.id.text);
		text.setHint(R.string.search_friend_hint);	
		
		final ImageView searchButton = (ImageView) view.findViewById(R.id.add_button);
		
		searchButton.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					searchButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_bar_send_highlight));
				} else if (event.getAction() == MotionEvent.ACTION_UP) {
					searchButton.setBackgroundDrawable(getResources().getDrawable(R.drawable.message_bar_send_normal));
				}
				return false;
			}
		});
		
		
		searchButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {

				searchButton.setEnabled(false);
				// dismiss keyboard 
				InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(text.getWindowToken(), 0);

				String friendToSearch = text.getEditableText().toString();
				if (mListener != null) {
					mListener.onSearchButtonPressed(thisFragment(), friendToSearch);
				}
				
				searchButton.setEnabled(true);

			}
		});

		return view;
	}



	private View initAcceptFriendFragment(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.accept_friend_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		TextView title = (TextView) view.findViewById(R.id.title);

		String titleText = getResources().getString(R.string.accept_friend);
		
		if (titleText.length() > 15) {
			title.setTextSize(title.getTextSize()-5);
		}
		
		title.setText(titleText);
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		final ImageView acceptFriendButton = (ImageView) view.findViewById(R.id.accept_button);
		acceptFriendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener != null) {
					acceptFriendButton.setEnabled(false);
					String meepTag = getArguments().getString("args1");
					mListener.onAcceptButtonPressed(thisFragment(), meepTag);
				}
				else {
					acceptFriendButton.setEnabled(true);
				}
			}
		});	

		final ImageView declineFriendButton = (ImageView) view.findViewById(R.id.decline_button);
		declineFriendButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				declineFriendButton.setEnabled(false);

				if (mListener != null) {
					String meepTag = getArguments().getString("args1");
					mListener.onDeclineButtonPressed(thisFragment(), meepTag);
				} else {
					declineFriendButton.setEnabled(true);
				}

			}
		});
		
		String nickname = getArguments().getString("args2");

		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(getString(R.string.accept_friend_format_string, nickname));
		
		
		String friendMessage = getArguments().getString("args3");

		TextView friendTextView = (TextView)view.findViewById(R.id.message_from_friend);
		friendTextView.setText(getString(R.string.accept_friend_friends_message_format_string, nickname, friendMessage));
		return view;
	}
	
	
	private View initErrorMessageFragment(LayoutInflater inflater, ViewGroup container) {
		
		View view = inflater.inflate(R.layout.error_message_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		TextView titleTV = (TextView) view.findViewById(R.id.title);
		
		String titleText = getResources().getString(R.string.error);
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
		
		final String action = getArguments().getString("args3");
		
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener != null) {
					mListener.onOkButtonPressed(thisFragment(), action);
				}
				PopUpFragment.this.dismiss();
			}
		});	


		String titleText2 = getArguments().getString("args2");
		if (titleText2 != null) {
			if (titleText2.length() > 15) {
				titleTV.setTextSize(titleTV.getTextSize()-5);
			}
			titleTV.setText(titleText2);
		}
		
		String message = getArguments().getString("args1");

		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(message);
		
		return view;
	}
	
	private View initNetworkFragment(LayoutInflater inflater, ViewGroup container) {
		
		View view = inflater.inflate(R.layout.network_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		TextView titleTV = (TextView) view.findViewById(R.id.title);

		String titleText = (String) titleTV.getText();
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView yesButton = (ImageView) view.findViewById(R.id.yes_button);
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mListener != null)
					mListener.onYesButtonPressed(thisFragment());
			}
		});	
		
		ImageView noButton = (ImageView) view.findViewById(R.id.no_button);
		noButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				if (mListener != null)
					mListener.onNoButtonPressed(thisFragment());
			}
		});	
		
		return view;
	}
	
	
	private View initDeleteFriendConfirmationFragment(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.delete_friend_confirm_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		
		TextView titleTV = (TextView) view.findViewById(R.id.title);

		String titleText = (String) titleTV.getText();
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		final ImageView yesButton = (ImageView) view.findViewById(R.id.yes_button);
		yesButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mDeleteFriendPopUpInterface != null) {
					yesButton.setEnabled(false);
					String accountId = getArguments().getString("args1");
					mDeleteFriendPopUpInterface.onYesButtonPressed(thisFragment(), accountId);
				}
				else {
					yesButton.setEnabled(true);
				}
			}
		});	

		final ImageView noButton = (ImageView) view.findViewById(R.id.no_button);
		noButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				
				noButton.setEnabled(false);

				if (mDeleteFriendPopUpInterface != null) {
					mDeleteFriendPopUpInterface.onNoButtonPressed(thisFragment());
				} else {
					noButton.setEnabled(true);
				}

			}
		});
		
		String nickname = getArguments().getString("args2");
		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(getString(R.string.delete_friend_confirm_format, nickname));
		
		return view;
	}

	private View initDeleteFriendSuccessFragment(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.delete_friend_success_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		TextView titleTV = (TextView) view.findViewById(R.id.title);

		String titleText = (String) titleTV.getText();
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	
		
		String nickname = getArguments().getString("args1");
		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(getString(R.string.friend_deleted_message_format, nickname));
		
		return view;
	}	
	
	
	private View initFriendRequestSuccessFragment(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.friend_request_success_pop_up, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		TextView titleTV = (TextView) view.findViewById(R.id.title);

		String titleText = (String) titleTV.getText();
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		return view;
	}	
	
	private View initNetworkFragment2(LayoutInflater inflater, ViewGroup container) {

		View view = inflater.inflate(R.layout.network_pop_up_2, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	
		
		
		return view;
	}	
	
	private View initFriendRequestStatusFragment(LayoutInflater inflater, ViewGroup container) {
		
		View view = inflater.inflate(R.layout.friend_request_result, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		TextView titleTV = (TextView) view.findViewById(R.id.title);

		String titleText = (String) titleTV.getText();
		
		if (titleText.length() > 15) {
			titleTV.setTextSize(titleTV.getTextSize()-5);
		}
		
		titleTV.setText(titleText);
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
				
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
			
		});	

	
		String message = getArguments().getString("args1");

		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(message);
		
		String title =  getArguments().getString("args2");
		if (title != null) {
			TextView textView2 = (TextView)view.findViewById(R.id.title);
			textView2.setText(title);
		}
		
		return view;
		
	}

	private View initFriendRequestMessageFragment(LayoutInflater inflater, ViewGroup container) {
	
		View view = inflater.inflate(R.layout.friend_request_message, container, false);
		view.setBackgroundColor(getResources().getColor(R.color.dim_alpha));

		
		TextView title = (TextView) view.findViewById(R.id.title);

		String titleText = (String) getResources().getString(R.string.accept_friend_title);
		
		if (titleText.length() > 15) {
			title.setTextSize(title.getTextSize()-5);
		}
		
		title.setText(titleText);
		
		ImageView closeButton = (ImageView) view.findViewById(R.id.close_button);
		closeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				PopUpFragment.this.dismiss();
			}
		});	

		ImageView okButton = (ImageView) view.findViewById(R.id.ok_button);
		
		
		okButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				PopUpFragment.this.dismiss();
			}
		});	

	
		String message = getArguments().getString("args1");

		TextView textView = (TextView)view.findViewById(R.id.message);
		textView.setText(message);
		
		return view;
		
	}

	
	private PopUpFragment thisFragment() {
		return this;
	}
	
	

	@Override
	public void dismiss() {
		if (getActivity() instanceof CommunicatorActivity) {
			((CommunicatorActivity) getActivity()).setAddFriendsButtonEnabled(true);
		}
		super.dismiss();
	}
	
	@Override
	public void onDismiss(DialogInterface dialog) {
		if (getActivity() instanceof CommunicatorActivity) {
			((CommunicatorActivity) getActivity()).setAddFriendsButtonEnabled(true);
		}
		super.onDismiss(dialog);
	}
	
	public void setListener(IPopupInterface listener) {
		mListener = listener;
	}
	
	public void setDeleteFriendPopupListener(IDeleteFriendPopUpInterface listener) {
		mDeleteFriendPopUpInterface = listener;
	}
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
	
	

}

