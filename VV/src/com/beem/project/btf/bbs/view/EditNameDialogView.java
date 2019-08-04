package com.beem.project.btf.bbs.view;

import com.beem.project.btf.R;
import com.butterfly.vv.db.SQLdm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EditNameDialogView {
	private Context mContext;
	private View mView;
	private EditText nickNameEdit;
	private ListView showResultList;
	private TextView nicnameText;
	private SQLiteDatabase db;
	private Cursor cursor;
	private SimpleCursorAdapter adapter;
	private String Itemid;

	public enum EditNameDialogType {
		normal, search, addfriend
	}

	private EditNameDialogType type = EditNameDialogType.normal;

	public EditNameDialogView(Context context) {
		this(context, EditNameDialogType.normal);
	}
	public EditNameDialogView(Context context, EditNameDialogType type) {
		this.mContext = context;
		this.type = type;
		LayoutInflater inflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mView = inflater.inflate(R.layout.edit_searchdialogview_layout, null);
		nickNameEdit = (EditText) mView.findViewById(R.id.nickNameEdit);
		showResultList = (ListView) mView.findViewById(R.id.showResultList);
		nicnameText = (TextView) mView.findViewById(R.id.nicnameText);
		switch (this.type) {
			case normal: {
				showResultList.setVisibility(View.GONE);
				nicnameText.setVisibility(View.GONE);
				break;
			}
			case addfriend: {
				showResultList.setVisibility(View.GONE);
				nicnameText.setVisibility(View.VISIBLE);
				break;
			}
			case search: {
				showResultList.setVisibility(View.VISIBLE);
				nicnameText.setVisibility(View.GONE);
				initDb();
				nickNameEdit.addTextChangedListener(new TextWatcher() {
					@Override
					public void onTextChanged(CharSequence paramCharSequence,
							int paramInt1, int paramInt2, int paramInt3) {
						// TODO Auto-generated method stub
					}
					@Override
					public void beforeTextChanged(
							CharSequence paramCharSequence, int paramInt1,
							int paramInt2, int paramInt3) {
						// TODO Auto-generated method stub
					}
					@Override
					public void afterTextChanged(Editable paramEditable) {
						// TODO Auto-generated method stub
						// Toast.makeText(mContext, getText(),
						// Toast.LENGTH_SHORT).show();
						if (!getText().isEmpty()) {
							queryDb(getText());
						} else {
							if (cursor != null) {
								cursor.close();
							}
						}
					}
				});
				showResultList
						.setOnItemClickListener(new OnItemClickListener() {
							@Override
							public void onItemClick(
									AdapterView<?> paramAdapterView,
									View paramView, int paramInt, long paramLong) {
								// TODO Auto-generated method stub
								String temp = ((TextView) paramView
										.findViewById(R.id.colleage_name))
										.getText().toString();
								Itemid = ((TextView) paramView
										.findViewById(R.id.colleage_id))
										.getText().toString();
								nickNameEdit.setText(temp);
								if (cursor != null) {
									cursor.close();
								}
							}
						});
				break;
			}
		}
	}
	public void initDb() {
		db = SQLdm.openDatabase(mContext);
	}
	public void queryDb(String str) {
		str = "'%" + str + "%'";
		// 查询含有字符串的列表数据
		// 其中ucode映射为_id以满足SimpleCursorAdapter的需求
		cursor = db.rawQuery(
				"select ucode as _id,uname from colleage where uname like "
						+ str, null);
		if (adapter == null) {
			adapter = new SimpleCursorAdapter(mContext,
					R.layout.colleage_listitem, cursor, new String[] { "_id",
							"uname" }, new int[] { R.id.colleage_id,
							R.id.colleage_name });
			showResultList.setAdapter(adapter);
		} else {
			adapter.changeCursor(cursor);
			adapter.notifyDataSetChanged();
		}
	}
	public View getView() {
		return mView;
	}
	public void setText(String nickName) {
		nickNameEdit.setText(nickName);
		nickNameEdit.setSelection(nickName.length());
	}
	public void setForText(String forName) {
		nicnameText.setText(forName);
	}
	public void setHint(String hint) {
		nickNameEdit.setHint(hint);
	}
	// 设置字数限制
	public void setMaxlenth(int maxlenth) {
		nickNameEdit
				.setFilters(new InputFilter[] { new InputFilter.LengthFilter(
						maxlenth) });
	}
	public String getid() {
		return Itemid;
	}
	public void setInputType(int inputType) {
		nickNameEdit.setInputType(inputType);
	}
	public String getText() {
		return nickNameEdit.getText().toString();
	}
}
