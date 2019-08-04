package com.oregonscientific.meep.global;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

public class MeepEditText extends EditText {

	public MeepEditText(Context context) {
		super(context);
		Typeface font = Typeface.createFromAsset(context.getAssets(), "ARLRDBD.TTF");
		setTypeface(font);
	}

	public MeepEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface font = Typeface.createFromAsset(context.getAssets(), "ARLRDBD.TTF");
		setTypeface(font);
	}

	public MeepEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface font = Typeface.createFromAsset(context.getAssets(), "ARLRDBD.TTF");
		setTypeface(font);
	}
	
	public String[] getBlackList() {
		return mBlackList;
	}

	public void setBlackList(String[] blackList) {
		this.mBlackList = blackList;
		this.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// do nth
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// do nth
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				String text = s.toString();
				if(isBlackListed(text))
				{
					setText(text.substring(0,text.length()-1));
					setSelection(text.length()-1);
				}
			}
		});
	}	

	private boolean isBlackListed(String text) {
		if (mBlackList == null) {
			return false;
		} else {
			for (int i = 0; i < mBlackList.length; i++) {
				if (text.contains(mBlackList[i])) {
					return true;
				}
			}
		}
		return false;
	}
	
	//custom
	private String[] mBlackList = null;

}
