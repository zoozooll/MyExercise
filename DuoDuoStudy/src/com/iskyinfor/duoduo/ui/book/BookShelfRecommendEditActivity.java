package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.pojo.User;
import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;
import com.iskinfor.servicedata.study.serviceimpl.ManagerStudyOperater010003000Impl;
import com.iskinfor.servicedata.usercenter.service.IQuerryUserInfor0300020001;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;

public class BookShelfRecommendEditActivity extends Activity implements
		OnClickListener {
	private ImageView img_results = null;
	private TextView bookshelfrecommendeditlist, bookshelfrecommendbooknum,
			bookshelfrecommendbookname;
	private IQuerryUserInfor0300020001 queryuserinfo;
	private IManagerStudyOperater0100030001 managerstudy;
	private BookShelfqq bookshelfqq;
	private ArrayList<User> classesList, teacherList, homeList, friendList;
	private Map<String, Object> CLASSNAMTESmap;
	private String[] userIDs;
	private ImageView bookshelfrecommendsend;
	private EditText bookshelfrecommentcontext;
	private String strBookID, strBookNum, strBookName;
	private String giftName;
	private TextView bookshelfrecommendrecommendname;
	private CheckBox bookshelfrecommendchk;
	private ProgressDialog myLoadingDialog;
	private GiftEditTask giftedittask;
	private GiftSendTask giftsendtask;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_recommend_edit);
		InitView();
	}

	public void InitView() {
		strBookID = getIntent().getStringExtra("BookID");
		strBookNum = getIntent().getStringExtra("BookNum");
		strBookName = getIntent().getStringExtra("BookName");

		bookshelfrecommendsend = (ImageView) findViewById(R.id.bookshelfrecommend_send);
		bookshelfrecommendsend.setOnClickListener(this);

		bookshelfrecommendbooknum = (TextView) findViewById(R.id.bookshelfrecommend_booknum);
		bookshelfrecommendbooknum.setText(strBookNum);

		bookshelfrecommendbookname = (TextView) findViewById(R.id.bookshelfrecommend_bookname);
		bookshelfrecommendbookname.setText(strBookName);
		bookshelfrecommendrecommendname = (TextView) findViewById(R.id.bookshelfrecommend_recommendname);
		bookshelfrecommendeditlist = (TextView) findViewById(R.id.bookshelfrecommend_editlist);
		bookshelfrecommendeditlist.setText(Html.fromHtml("<u><a>"
				+ bookshelfrecommendeditlist.getText() + "</u></a>"));
		bookshelfrecommendeditlist.setOnClickListener(this);
		img_results = (ImageView) findViewById(R.id.bookshelfrecommend_edit_bottomfh);
		img_results.setOnClickListener(this);
		bookshelfrecommendchk = (CheckBox) findViewById(R.id.bookshelfrecommend_chk);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bookshelfrecommend_edit_bottomfh:
			finish();
			break;
		case R.id.bookshelfrecommend_editlist:

			myLoadingDialog = new ProgressDialog(this);
			myLoadingDialog.setMessage("好友列表加载中...");
			myLoadingDialog.show();

			try {
				giftedittask = new GiftEditTask(
						BookShelfRecommendEditActivity.this, myLoadingDialog,
						"BookShelfRecommendEditActivity");
				giftedittask.execute();

			} catch (Exception e) {
				e.printStackTrace();
			}

			break;
		case R.id.bookshelfrecommend_send:
			bookshelfrecommentcontext = (EditText) findViewById(R.id.bookshelfrecomment_context);
			managerstudy = new ManagerStudyOperater010003000Impl();
			try {
				if (bookshelfrecommendrecommendname.getText().toString().equals("")) {
					Toast.makeText(BookShelfRecommendEditActivity.this,"推荐人不能为空！", Toast.LENGTH_LONG).show();
					return;}
				if (bookshelfrecommentcontext.getText().toString().equals("")) {
					Toast.makeText(BookShelfRecommendEditActivity.this,"推荐内容不能为空！", Toast.LENGTH_LONG).show();
					return;}
				
				
				myLoadingDialog = new ProgressDialog(this);
	    		myLoadingDialog.setMessage("推荐中,请稍候...");
	    		myLoadingDialog.show();
	    		giftsendtask=new GiftSendTask(BookShelfRecommendEditActivity.this, myLoadingDialog, "recommend",UiHelp.getUserShareID(BookShelfRecommendEditActivity.this),strBookID, bookshelfrecommendchk.isChecked() ? "01": "00", bookshelfrecommentcontext.getText().toString(), userIDs, "");
				giftsendtask.execute();
	    		
				//String reinfo = managerstudy.recommProduct(UiHelp.getUserShareID(BookShelfRecommendEditActivity.this),strBookID, bookshelfrecommendchk.isChecked() ? "01": "00", bookshelfrecommentcontext.getText().toString(), userIDs, "");
				
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;

		}
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		} else {
			return super.onKeyDown(keyCode, event);
		}
	}

	public void setUserId(String[] IDs, String giftname) {
		userIDs = IDs;
		giftName = giftname;
		bookshelfrecommendrecommendname.setText(giftname);

		String IDS = "";
		for (int i = 0; i < userIDs.length; i++) {
			IDS += userIDs[i] + " ";
		}
		Log.i("PLJ", "RecommendIDS==>" + IDS);

	}

	public void setRecommendFriendMap(Map<String, Object> nameMap) {
		CLASSNAMTESmap = nameMap;
		bookshelfqq = new BookShelfqq(BookShelfRecommendEditActivity.this,
				bookshelfrecommendeditlist, CLASSNAMTESmap,
				"BookShelfRecommendEditActivity", myLoadingDialog,userIDs);
		bookshelfqq.showQQScren();
	}
	
	public void setRecommendSendInfo(String reinfo)
	{
		if (reinfo == "") {
			Toast.makeText(BookShelfRecommendEditActivity.this,"推荐成功！", Toast.LENGTH_LONG).show(); StaticData.boolSended=true; finish();
		} else {
			Toast.makeText(BookShelfRecommendEditActivity.this,"推荐失败：" + reinfo, Toast.LENGTH_LONG).show();
		}
	}

	private void cancelDialog() {
		if (myLoadingDialog != null && myLoadingDialog.isShowing()) {
			myLoadingDialog.dismiss();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.finish();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cancelDialog();
		super.onDestroy();
	}
}
