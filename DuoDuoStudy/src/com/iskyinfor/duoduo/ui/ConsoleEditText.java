package com.iskyinfor.duoduo.ui;

import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

public class ConsoleEditText {
	
	private static CharSequence temp = null;
	
	/**
	 * 判断是否EditText是否可以编辑
	 */
	private static boolean isEdit = true;
	
	public static void ConsoleEditTextMethod(final EditText editText)
	{
		editText.addTextChangedListener(new  TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) 
			{
				if(isEdit == false)
				{
					Editable etext = editText.getText();
					
//					Log.i("yyj", "取到的密码长度是 =====>>>>>>" + etext.length());
					int pos = etext.length();
					Selection.setSelection(etext,pos);
				}
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,int after)
			{
				temp = s;
			}
			
			@Override
			public void afterTextChanged(Editable s)
			{
				if (temp.length() >24)
				{
					isEdit = false;
					s.delete(temp.length()-1,temp.length());
					editText.setText(s);
					Log.i("yyj", " length ======>>>>>>>" + temp.length());
				}
			}
		});
	}
}