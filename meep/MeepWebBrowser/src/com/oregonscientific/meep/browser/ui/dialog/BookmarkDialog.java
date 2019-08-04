package com.oregonscientific.meep.browser.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.view.Window;
import android.widget.ImageButton;

import com.oregonscientific.meep.browser.BrowserUtility;
import com.oregonscientific.meep.browser.R;
import com.oregonscientific.meep.browser.WebBrowserActivity;
import com.oregonscientific.meep.browser.R.id;
import com.oregonscientific.meep.browser.R.layout;
import com.oregonscientific.meep.browser.database.Bookmark;
import com.oregonscientific.meep.customdialog.CommonPopup.OnClickOkButtonListener;
import com.oregonscientific.meep.customfont.MyButton;
import com.oregonscientific.meep.customfont.MyEditText;
import com.oregonscientific.meep.customfont.MyTextView;

public class BookmarkDialog extends Dialog implements
		android.view.View.OnClickListener {
	private Bookmark mBookmark;
	public static final int MODE_ADD = 0;
	public static final int MODE_RENAME = 1;
	MyTextView bookmarkUrl;
	MyEditText bookmarkName;
	MyButton ok;
	ImageButton close;
	private int mode;
	private Context mContext;

	public BookmarkDialog(Context context, int mode, Bookmark bookmark) {
		super(context);
		this.mode = mode;
		this.mBookmark = bookmark;
		this.mContext = context;
		initDialog();
	}

	public void initDialog() {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.popup_addbookmark);
		getWindow().setLayout(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));

		bookmarkUrl = (MyTextView) findViewById(R.id.bookmarkUrl);
		bookmarkName = (MyEditText) findViewById(R.id.bookmarkName);
		bookmarkUrl.setText(mBookmark.getUrl());
		bookmarkName.setText(mBookmark.getName());
		close = (ImageButton) findViewById(R.id.btnClose);
		ok = (MyButton) findViewById(R.id.btnOk);
		close.setOnClickListener(this);
		ok.setOnClickListener(this);
	}

	public void setDialogTitle(int t) {
		MyTextView title = ((MyTextView) findViewById(R.id.title));
		title.setText(t);
		title.setTextSize(35);
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnOk:
			if (mode == MODE_ADD) {
				addBookmark();
			} else if (mode == MODE_RENAME) {
				updateBookmark();
			}
			BrowserUtility.hideKeyboard(getContext(), v);
			dismiss();
			break;
		case R.id.btnClose:
			BrowserUtility.hideKeyboard(getContext(), v);
			dismiss();
			break;
		}
	}

	private void addBookmark() {
		if ((Activity) mContext instanceof WebBrowserActivity) {
			// TODO:check name
			String name = bookmarkName.getText().toString().trim();
			
			if(!isValidBookmarkName(name))
			{
				return;
			}
			mBookmark.setName(name);
			((WebBrowserActivity) mContext).addBookmark(mBookmark);
		}
	}

	private void updateBookmark() {
		if ((Activity) mContext instanceof WebBrowserActivity) {
			String name = bookmarkName.getText().toString();
			if(!isValidBookmarkName(name))
			{
				return;
			}
			((WebBrowserActivity) mContext).updateBookmarkName(mBookmark.getId(), name);
		}

	}
	
	private boolean isValidBookmarkName(String name)
	{
		if (name == null || name.trim().isEmpty()) {
			BrowserUtility.alertMessage(mContext, R.string.browser_title_blocked, com.osgd.meep.library.R.string.msg_null);
			return false;
		}
		if (((WebBrowserActivity) mContext).containsBadWord(name)) {
			BrowserUtility.alertMessage(mContext, R.string.browser_title_blocked, R.string.this_word_has_been_blocked);
			return false;
		}
		return true;
	}

}
