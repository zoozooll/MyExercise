package com.oregonscientific.meep.meepmusic;

import android.app.DialogFragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;

public class PopUpDialogFragment extends DialogFragment{
    public static final int LOADING = 8;
    
    public interface FeedBackListener {
    	public abstract void OnPopupFeedBack(String option, Object variable);
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			setStyle(STYLE_NO_FRAME, android.R.style.Theme_Translucent_NoTitleBar_Fullscreen);
    }
    
    public static PopUpDialogFragment newInstance(int title) {  
			PopUpDialogFragment myDialogFragment = new PopUpDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("title", title);
			myDialogFragment.setArguments(bundle);
			return myDialogFragment;  
    }
    
		public static PopUpDialogFragment newInstance(int title, String url, String message, String version) {
			PopUpDialogFragment myDialogFragment = new PopUpDialogFragment();
			Bundle bundle = new Bundle();
			bundle.putInt("title", title);
			bundle.putString("url", url);
			bundle.putString("message", message);
			bundle.putString("version", version);
			myDialogFragment.setArguments(bundle);
			return myDialogFragment;
		}
    
    @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			int args = getArguments().getInt("title");
			View v = null;
			switch (args) {
			case LOADING:
				v = inflater.inflate(R.layout.loading, container, false);
				createLoading(v);
				break;
			}
			return v;
		}
    
		public void createLoading(View v) {
			v.setFocusableInTouchMode(true);
			v.setFocusable(true);
			v.requestFocus();
			v.setOnKeyListener(new OnKeyListener() {
				@Override
				public boolean onKey(View arg0, int arg1, KeyEvent arg2) {
					if (arg1 == KeyEvent.KEYCODE_BACK) {
						if (getActivity() != null) {
							getActivity().finish();
						}
						return true;
					}
					return false;
				}
			});
		}
    
		DialogFragment loadPopupFragment;
		public Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case 4:
					loadPopupFragment = PopUpDialogFragment.newInstance(PopUpDialogFragment.LOADING);
					if (getFragmentManager() != null) {
						loadPopupFragment.show(getFragmentManager(), "dialog");
					}
					break;
				default:
					break;
				}
			}
		};
	
		public interface UpdateUserCoinsListener {
			public void updateUserCoinsListener();
		}
		
		@Override
		public void dismiss() {
			// TODO Auto-generated method stub
			try {
				super.dismiss();
			} catch (Exception e) {
				Log.d("test", "parent activity has been closed");
			}
		}
	
}  