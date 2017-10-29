package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.study.serviceimpl.QuerryStudyOperater0100020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

public class BookShelfRecommendActivity extends Activity  implements
        PageListView.NetetworkDataInterface, OnClickListener,OnItemClickListener{
	public PageListView listviewrecommend,listviewbyrecommend;
	public ImageView bookshelfgift_bottomfh,bookshelfrecommendshop;
	public TextView bookshelfrecommendtoptype,bookshelfrecommendcurrpag,bookshelfrecommendcountpag;
	public PopupWindow hiphop, menuWindow;
	public ProgressDialog progressDialog;
	public BookShelfRecommendListAdapter bookshelfgiftlistadapter;
	public BookShelfRecommendListByAdapter bookshelfgiftlistbyadapter;
	public ArrayList<RecommentInfor> recommentdata = new ArrayList<RecommentInfor>();
	public ArrayList<RecommentInfor> recommentdataby = new ArrayList<RecommentInfor>();
	//赠送
	public TextView bookshelfgift_mygift,bookshelfgift_mygiftby,bookshelfgift_contentall;
	public boolean flag = true;
	public boolean showbool = true;
	public int helpId;
	public IQuerryStudyOperater0100020001 querrystudy = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_recommend);
		initView();
		loadData();
	}
	public void initView() {
		listviewrecommend = (PageListView) findViewById(R.id.listview_recommend);
		listviewrecommend.setOnItemClickListener(this);
		listviewbyrecommend = (PageListView) findViewById(R.id.listview_byrecommend);
		listviewbyrecommend.setOnItemClickListener(this);
		bookshelfgift_bottomfh = (ImageView) findViewById(R.id.bookshelfrecommend_rest);
		bookshelfgift_bottomfh.setOnClickListener(this);
		bookshelfrecommendshop = (ImageView) findViewById(R.id.bookshelfrecommend_shop);
		bookshelfrecommendshop.setOnClickListener(this);
		bookshelfrecommendtoptype = (TextView) findViewById(R.id.bookshelfrecommend_toptype);
		bookshelfrecommendtoptype.setOnClickListener(this);
		bookshelfrecommendcurrpag = (TextView) findViewById(R.id.bookshelfrecommend_currpag);
		bookshelfrecommendcountpag = (TextView) findViewById(R.id.bookshelfrecommend_countpag);
		querrystudy = new QuerryStudyOperater0100020001Impl();
	}

	public  void listviewGiftType() {
		if ( hiphop.isShowing()) {
			hiphop.dismiss();} 
		if(listviewrecommend.getVisibility()==View.VISIBLE)
		{return;}
		bookshelfrecommendtoptype.setText("我的推荐");
		if(bookshelfgiftlistadapter==null)
		{loadData();}
		listviewrecommend.setVisibility(View.VISIBLE);
		listviewbyrecommend.setVisibility(View.GONE);
	}
	
	public void listviewGiftByType()
	{
		if ( hiphop.isShowing()) {
			hiphop.dismiss();} 
		if(listviewbyrecommend.getVisibility()==View.VISIBLE)
		{return;}
		bookshelfrecommendtoptype.setText("我获荐的");
		if(bookshelfgiftlistbyadapter==null)
		{loadDataBy();}
		listviewrecommend.setVisibility(View.GONE);
		listviewbyrecommend.setVisibility(View.VISIBLE);
	}
	
	public void loadDataBy() {
		progressDialog = new ProgressDialog(BookShelfRecommendActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();
		
		PageGiftEventListener pageBookEventListener = new PageGiftEventListener(progressDialog);
		bookshelfgiftlistbyadapter = new BookShelfRecommendListByAdapter(
				BookShelfRecommendActivity.this, recommentdataby);
		listviewbyrecommend.setListAdapter(bookshelfgiftlistbyadapter);
		listviewbyrecommend.setPageEvenListener(pageBookEventListener);
		listviewbyrecommend.setNetetworkDataInterface(this);
		listviewbyrecommend.loadPage(1, "",7);
	}

	
	public void loadData() {
		progressDialog = new ProgressDialog(BookShelfRecommendActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();

		PageGiftEventListener pageBookEventListener = new PageGiftEventListener(progressDialog);
		bookshelfgiftlistadapter = new BookShelfRecommendListAdapter(
				BookShelfRecommendActivity.this, recommentdata);
		listviewrecommend.setListAdapter(bookshelfgiftlistadapter);
		listviewrecommend.setPageEvenListener(pageBookEventListener);
		listviewrecommend.setNetetworkDataInterface(this);
		listviewrecommend.loadPage(1, "",6);
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bookshelfrecommend_rest:
			finish();
			break;
		case R.id.bookshelfrecommend_shop:
			Intent intentshop = new Intent(BookShelfRecommendActivity.this,
					BookstoreActivity.class);
			startActivity(intentshop);
			break;
		case R.id.bookshelfrecommend_toptype:
			if (hiphop != null && hiphop.isShowing()) {
				hiphop.dismiss();
			} else {
				BookShelfRecommendMyrecommendChild bookShelfSortView = new BookShelfRecommendMyrecommendChild(BookShelfRecommendActivity.this); 
				View sortView = bookShelfSortView.onCreateView();
//				View sortView =BookShelfGiftActivity.this.getLayoutInflater().inflate(R.layout.bookshelf_giftchild, null); 
				hiphop = new PopupWindow(sortView, LayoutParams.WRAP_CONTENT,
						LayoutParams.WRAP_CONTENT);
				hiphop.update();
				hiphop.showAsDropDown(bookshelfrecommendtoptype, 0, 0);
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
				bookshelfrecommendcurrpag.setText(bundle.getString("reqPage"));
				bookshelfrecommendcountpag.setText(bundle.getString("PageCount"));
			}
		}
	};

	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		PageinateContainer pageinateContainer = new PageinateContainer();
		try {
			Log.i("liu", "star request====>>" + System.currentTimeMillis());
			Map<String, Object> map=null;
			if(intGiftType==6)
            { map = querrystudy.getRecommentInfor("06",
					"0002", "12", "", "", 0, "01", "", "", "", "", "", "");}
            else if(intGiftType==7)
            { map = querrystudy.getRecommentInfor("07",
					"0002", "12", "", "", 0, "01", "", "", "", "", "", "");}
			
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
			Log.i("liu", "net is excepation");
		}
		return pageinateContainer;
	}



	@Override
	public PageinateContainer condition(int reqPage) {
		// TODO Auto-generated method stub
		return null;
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
}
