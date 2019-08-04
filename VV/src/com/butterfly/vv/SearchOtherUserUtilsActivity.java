package com.butterfly.vv;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;

import com.beem.project.btf.R;
import com.butterfly.vv.db.ormhelper.bean.UserFriendDB;
import com.butterfly.vv.db.ormhelper.bean.BaseDB.DBKey;
import com.butterfly.vv.service.ContactService;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.ContactListAdapter;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.pullToRefresh.ui.PullToRefreshListView;

/**
 * @author tjerk
 * @date 6/13/12 7:33 AM
 */
public class SearchOtherUserUtilsActivity extends VVBaseActivity {
	private ImageButton searchback;
	private EditText search_friend_Edit;
	private ExecutorService executor;
	private PullToRefreshListView listView;
	private ContactListAdapter adapter;
	private Handler mHandler = new Handler();

	@Override
	public void onCreate(Bundle savedData) {
		super.onCreate(savedData);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_contacts);
		searchback = (ImageButton) findViewById(R.id.searchback);
		search_friend_Edit = (EditText) findViewById(R.id.search_friend_Edit);
		listView = (PullToRefreshListView) findViewById(R.id.search_result_list);
		listView.setListViewDivider();
		adapter = new ContactListAdapter(mContext);
		searchback.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(searchback.getWindowToken(), 0);
				finish();
			}
		});
		search_friend_Edit.addTextChangedListener(searchWatcher);
		listView.getRefreshableView().setAdapter(adapter);
		executor = Executors.newSingleThreadExecutor();
	}
	@Override
	protected void onResume() {
		super.onResume();
	}
	@Override
	protected void onPause() {
		super.onPause();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		executor.shutdownNow();
	}

	private TextWatcher searchWatcher = new TextWatcher() {
		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {
		}
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
		}
		@Override
		public void afterTextChanged(Editable s) {
			//LogUtils.i("search text:" + s.toString());
			executor.execute(new WorkTask(s.toString().trim()));
		}
	};

	@Override
	public void registerVVBroadCastReceivers() {
	}

	// 查询好友的数据库
	private class WorkTask implements Runnable {
		private String likeWhere;

		private WorkTask(String likeWhere) {
			super();
			this.likeWhere = likeWhere;
		}
		@Override
		public void run() {
			List<Contact> friendContacts = new ArrayList<Contact>();
			if (!TextUtils.isEmpty(likeWhere)) {
				List<UserFriendDB> friendDBs = UserFriendDB.queryAllLike(
						LoginManager.getInstance().getJidParsed(), likeWhere);
				for (UserFriendDB friendDB : friendDBs) {
					Contact c = ContactService.getInstance().getContact(
							friendDB.getFieldStr(DBKey.jid_friend), true, true);
					if (c != null) {
						friendContacts.add(c);
					}
				}
			}
			onPostExecute(friendContacts);
		}
		public void onPostExecute(final List<Contact> friendContacts) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					adapter.clear();
					adapter.put(friendContacts);
					adapter.notifyDataSetChanged();
				}
			});
		}
	}
}
