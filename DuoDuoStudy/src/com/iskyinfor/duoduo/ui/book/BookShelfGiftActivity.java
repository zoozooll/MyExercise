package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.study.serviceimpl.QuerryStudyOperater0100020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class BookShelfGiftActivity extends Activity implements
		PageListView.NetetworkDataInterface, OnClickListener,
		OnItemClickListener {
	public List<Map<String, Object>> mData;
	public PageListView listviewgift;
	public PageListView listviewbygift;
	public ImageView bookshelfgift_bottomfh;
	public ImageView bookshelfgiftshop;
	public TextView bookshelfgifttoptype, bookshelfgiftcurrpag,
			bookshelfgiftcountpag;
	public PopupWindow hiphop, menuWindow;
	public ProgressDialog progressDialog;
	public BookShelfGiftListAdapter bookshelfgiftlistadapter;
	public BookShelfGiftListByAdapter bookshelfgiftlistbyadapter;
	public ArrayList<RecommentInfor> recommentdata = new ArrayList<RecommentInfor>();
	public ArrayList<RecommentInfor> recommentdataby = new ArrayList<RecommentInfor>();
	public TextView bookshelfgift_mygift, bookshelfgift_mygiftby;
	public TextView bookshelfgift_contentall;
	public boolean flag = true;
	public boolean showbool = true;
	public int helpId;
	public IQuerryStudyOperater0100020001 querrystudy = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_gift);
		initView();
		loadData();
	}

	public void initView() {
		listviewgift = (PageListView) findViewById(R.id.listview_gift);
		listviewgift.setOnItemClickListener(this);
		listviewbygift = (PageListView) findViewById(R.id.listview_bygift);
		listviewbygift.setOnItemClickListener(this);
		bookshelfgift_bottomfh = (ImageView) findViewById(R.id.bookshelfgift_rest);
		bookshelfgift_bottomfh.setOnClickListener(this);
		bookshelfgiftshop = (ImageView) findViewById(R.id.bookshelfgift_shop);
		bookshelfgiftshop.setOnClickListener(this);
		bookshelfgifttoptype = (TextView) findViewById(R.id.bookshelfgift_toptype);
		bookshelfgifttoptype.setOnClickListener(this);
		bookshelfgiftcurrpag = (TextView) findViewById(R.id.bookshelfgift_currpag);
		bookshelfgiftcountpag = (TextView) findViewById(R.id.bookshelfgift_countpag);
		querrystudy = new QuerryStudyOperater0100020001Impl();
	}

	public void listviewGiftType() {
		if (hiphop.isShowing()) {
			hiphop.dismiss();
		}
		if (listviewgift.getVisibility() == View.VISIBLE) {
			return; }
		bookshelfgifttoptype.setText("我的赠送");
		if(bookshelfgiftlistadapter==null)
		{loadData();}
		listviewgift.setVisibility(View.VISIBLE);
		listviewbygift.setVisibility(View.GONE);
	}

	public void listviewGiftByType() {
		if (hiphop.isShowing()) {
			hiphop.dismiss();
		}
		if (listviewbygift.getVisibility() == View.VISIBLE) {
			return;
		}
		bookshelfgifttoptype.setText("我获赠的");
		if(bookshelfgiftlistbyadapter==null)
		{loadDataBy();}
		listviewgift.setVisibility(View.GONE);
		listviewbygift.setVisibility(View.VISIBLE);
	}

	public void loadDataBy() {
		progressDialog = new ProgressDialog(BookShelfGiftActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();

		PageGiftEventListener pageBookEventListener = new PageGiftEventListener(
				progressDialog);
		bookshelfgiftlistbyadapter = new BookShelfGiftListByAdapter(
				BookShelfGiftActivity.this, recommentdataby);
		listviewbygift.setListAdapter(bookshelfgiftlistbyadapter);
		listviewbygift.setPageEvenListener(pageBookEventListener);
		listviewbygift.setNetetworkDataInterface(this);
		listviewbygift.loadPage(1, "", 7);
	}

	public void loadData() {
		progressDialog = new ProgressDialog(BookShelfGiftActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();

		PageGiftEventListener pageBookEventListener = new PageGiftEventListener(progressDialog);
		bookshelfgiftlistadapter = new BookShelfGiftListAdapter(
				BookShelfGiftActivity.this, recommentdata);
		listviewgift.setListAdapter(bookshelfgiftlistadapter);
		listviewgift.setPageEvenListener(pageBookEventListener);
		listviewgift.setNetetworkDataInterface(this);
		listviewgift.loadPage(1, "", 6);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bookshelfgift_rest:
			finish();
			break;
		case R.id.bookshelfgift_shop:
			Intent intentshop = new Intent(BookShelfGiftActivity.this,
					BookstoreActivity.class);
			startActivity(intentshop);
			break;
		case R.id.bookshelfgift_toptype:
			if (hiphop != null && hiphop.isShowing()) {
				hiphop.dismiss();
			} else {
				BookShelfGiftMygiftChild bookShelfSortView = new BookShelfGiftMygiftChild(
						BookShelfGiftActivity.this);
				View sortView = bookShelfSortView.onCreateView();
				// View sortView
				// =BookShelfGiftActivity.this.getLayoutInflater().inflate(R.layout.bookshelf_giftchild,
				// null);
				hiphop = new PopupWindow(sortView, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				hiphop.update();
				hiphop.showAsDropDown(bookshelfgifttoptype, 0, 0);
			}
			break;
		default:
			break;
		}
	}

	Handler updatePagHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if (bundle != null) {
				bookshelfgiftcurrpag.setText(bundle.getString("reqPage"));
				bookshelfgiftcountpag.setText(bundle.getString("PageCount"));
			}

		}

	};

	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		PageinateContainer pageinateContainer = new PageinateContainer();
		try {
			Map<String, Object> map = null;
			if (intGiftType == 6) {
				map = querrystudy.getRecommentInfor("06", "0002", "12", "", "",
						reqPage, "00", "", "", "", "", "", "");
			} else if (intGiftType == 7) {
				map = querrystudy.getRecommentInfor("07", "0002", "12", "", "",
						0, "00", "", "", "", "", "", "");
			}

			ArrayList<RecommentInfor> recomData = (ArrayList<RecommentInfor>) map
					.get(DataConstant.RECOMMENT_INFOR);
			pageinateContainer.totalPageCount = (Integer) map
					.get(DataConstant.TOTAL_PAGS);
			pageinateContainer.responseData.addAll(recomData);

			Message msg = updatePagHandler.obtainMessage();
			Bundle bundle = new Bundle();
			bundle.putString("reqPage", String.valueOf(reqPage));
			bundle.putString("PageCount",
					String.valueOf(pageinateContainer.totalPageCount));
			msg.setData(bundle);
			updatePagHandler.sendMessage(msg);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return pageinateContainer;
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

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO //AdapterView<?> arg0, View arg1, int arg2, long arg3
		switch (view.getId()) {
		case R.id.listview_gift:
			if (showbool == true) {
				// textView.setVisibility(View.VISIBLE);
				if (bookshelfgift_contentall == null) {
					bookshelfgift_contentall = (TextView) view
							.findViewById(R.id.bookshelfgift_contentall);
					helpId = position;
				} else if (bookshelfgift_contentall != view) {
					
					bookshelfgift_contentall = (TextView) view
							.findViewById(R.id.bookshelfgift_contentall);
					helpId = position;
				}

				TextView textView = (TextView) view
						.findViewById(R.id.bookshelfcommentlist_all);
				textView.setText("对于小学升学复习备考具有极强的针对性和指导性" + ""
						+ "对我的学习很有帮助，值得购买");
				showbool = false;
			} else if (showbool == false) {
				TextView textView = (TextView) view
						.findViewById(R.id.bookshelfcommentlist_all);
				textView.setText("查看全部");
				// textView.setVisibility(View.GONE);
				showbool = true;
			}
			break;
		case R.id.listview_bygift:
			break;
		default:
			break;
		}
	}

//	private String searchKey = "";
	private int intGiftType = 0;

	@Override
	public PageinateContainer condition(int reqPage) {
		// TODO Auto-generated method stub
		try {
			switch (intGiftType) {
			case 6:
				querrystudy.getRecommentInfor("06", "0002", "12", "", "",
						reqPage, "01", "", "", "", "", "", "");
				break;
			case 7:

				querrystudy.getRecommentInfor("07", "0002", "12", "", "", 0,
						"01", "", "", "", "", "", "");
				break;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}

}
