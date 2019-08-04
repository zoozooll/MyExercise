/*
    BEEM is a videoconference application on the Android Platform.

    Copyright (C) 2009 by Frederic-Charles Barthelery,
                          Jean-Manuel Da Silva,
                          Nikita Kozlov,
                          Philippe Lago,
                          Jean Baptiste Vergely,
                          Vincent Veronis.

    This file is part of BEEM.

    BEEM is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    BEEM is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with BEEM.  If not, see <http://www.gnu.org/licenses/>.

    Please send bug reports with examples or suggestions to
    contact@beem-project.com or http://dev.beem-project.com/

    Epitech, hereby disclaims all copyright interest in the program "Beem"
    written by Frederic-Charles Barthelery,
               Jean-Manuel Da Silva,
               Nikita Kozlov,
               Philippe Lago,
               Jean Baptiste Vergely,
               Vincent Veronis.

    Nicolas Sadirac, November 26, 2009
    President of Epitech.

    Flavien Astraud, November 26, 2009
    Head of the EIP Laboratory.

 */
package com.beem.project.btf.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beem.project.btf.R;
import com.beem.project.btf.bbs.view.ClearEditText3;
import com.beem.project.btf.bbs.view.ClearEditText3.IsemptyListener;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.service.Contact;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.btf.push.Item.MsgTypeSub;
import com.butterfly.vv.adapter.localListViewAdapter;
import com.butterfly.vv.service.ContactService;
import com.butterfly.vv.vv.utils.CToast;

/**
 * 添加好友(This activity is used to add a contact.)
 * @author nikita
 */
public class AddVVContact extends VVBaseActivity implements OnClickListener {
	private ClearEditText3 search_id_Edit;
	private boolean isenabled = false;
	private Button search_btn;
	private ListView search_result_list;
	private localListViewAdapter searchRstAdpater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.vv_addcontact);
		TextView contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setText("添加好友");
		final CustomTitleBtn back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(back.getWindowToken(), 0);
				AddVVContact.this.finish();
			}
		});
		search_btn = (Button) findViewById(R.id.search_btn);
		search_btn.setOnClickListener(this);
		search_id_Edit = (ClearEditText3) findViewById(R.id.search_id_Edit);
		search_id_Edit.setIsemptyListener(new IsemptyListener() {
			@Override
			public void isempty(boolean staus) {
				// TODO Auto-generated method stub
				// 输入框为空时不可点
				isenabled = staus;
				search_btn.setEnabled(isenabled);
			}
		});
		search_result_list = (ListView) findViewById(R.id.search_result_list);
		searchRstAdpater = new localListViewAdapter(this, search_result_list);
		search_result_list.setAdapter(searchRstAdpater);
	}
	@Override
	public void onClick(View v) {
		if (v == search_btn) {
			final String searchTimeId = search_id_Edit.getText().toString();
			if (searchTimeId.matches("\\d+")) {
				new VVBaseLoadingDlg<Contact>(new VVBaseLoadingDlgCfg(
						AddVVContact.this).setShowWaitingView(true)) {
					@Override
					protected Contact doInBackground() {
						return ContactService.getInstance().getContact(
								searchTimeId);
					}
					@Override
					protected void onPostExecute(Contact result) {
						searchRstAdpater.clearItems();
						if (result != null) {
							Item item = new Item(result.getJIDCompleted(),
									result.getNickName());
							item.setPhoto(result.getPhoto());
							item.setMsgtype(MsgType.search_rst);
							item.setSex(result.getSex());
							item.setLat(result.getLat());
							item.setLon(result.getLon());
							item.setLogintime(result.getLogintime());
							item.setBday(result.getBday());
							item.setSignature(result.getSignature());
							if (LoginManager.getInstance().isMyJid(
									item.getJidParsed())
									|| ContactService.getInstance().friendYet(
											item.getJidParsed())) {
								item.setMsgTypeSub(MsgTypeSub.added);
							} else {
								item.setMsgTypeSub(MsgTypeSub.unadded);
							}
							searchRstAdpater.addItem(item);
						} else {
							CToast.showToast(AddVVContact.this, "查无此人!",
									Toast.LENGTH_SHORT);
						}
						searchRstAdpater.notifyDataSetChanged();
					}
				}.execute();
			} else {
				CToast.showToast(AddVVContact.this, "时光号必须全为数字",
						Toast.LENGTH_SHORT);
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
		// TODO Auto-generated method stub
	}
}
