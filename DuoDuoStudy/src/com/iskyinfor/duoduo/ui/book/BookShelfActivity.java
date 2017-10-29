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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.study.service.IQuerryStudyOperater0100020001;
import com.iskinfor.servicedata.study.serviceimpl.QuerryStudyOperater0100020001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.SpinnerView;
import com.iskyinfor.duoduo.ui.SpinnerView.SpinnserPopuViewListener;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;
import com.iskyinfor.duoduo.ui.downloader.DowanloadManagerActivity;
import com.iskyinfor.duoduo.ui.lesson.LessonSearchActivity;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

public class BookShelfActivity extends Activity implements
		PageListView.NetetworkDataInterface, OnClickListener,SpinnserPopuViewListener{

	private IQuerryStudyOperater0100020001 bookshelfData = null;
	/**
	 * 书架数据源
	 */
	private ArrayList<BookShelf> bookListData = new ArrayList<BookShelf>();
	private ProgressDialog progressDialog;
	private ImageButton backImageBtn, menuImageButton;
	private ImageView imgSwitch;
	private GridView popupGridView;
	public  PopupWindow hiphop, menuWindow;
	private LinearLayout buttomLayot,bookshelf_main_pag1;
	private PageListView listView;
	private PageListView listviewShelf;
	private boolean flag = true;
	private ImageView bookshelfShop;
	private ImageView bookshelfarrowleft,bookshelfarrowright; 
	private TextView bookshelfcurrpag,bookshelfcountpag;
	private ImageView bookshelfsearchbtn;
	private ImageView bookshelf_maincontent_listtop,bookshelf_maincontent_listbuttom;
	/**
	 * 加载书架列表的适配器
	 */
	private BookShelfAdapter bookShelfFloorAdapter = null;
	/**
	 * 加载书架效果的适配器
	 */
	private BookShelfGridAdapter bookshelfgridviewadapter = null;
	private ImageView intentseach;
	private TextView txtinputpag;
	private boolean dataloadbool = true;
	private Button btnpag;
	private String grid_currage = "", grid_countage = "";
	private String list_currage = "", list_countage = "";
	private InputMethodManager mInputMethodManager;
	private EditText searchEdit, pageEdit;
	/**
	 * 类别
	 */
	private SpinnerView textSortBtn;
    private TextView Note,Label;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookshelf_main);
		initView(); // 初始化控件
//		loadData(); // 加载列表数据
		loadGridData("");
	}

	/**
	 * 初始化控件
	 */
	private void initView() {
		listView = (PageListView) findViewById(R.id.listview_bookshelf);
		listviewShelf = (PageListView) findViewById(R.id.listview_shelf);
		backImageBtn = (ImageButton) findViewById(R.id.duoduo_lesson_back_img);
		backImageBtn.setOnClickListener(this);
		menuImageButton = (ImageButton) findViewById(R.id.duoduo_lesson_list_img);
		menuImageButton.setOnClickListener(this);
		buttomLayot = (LinearLayout) findViewById(R.id.bookshelf_main_buttom);
		
		/**
		 * 下拉列表
		 */
		textSortBtn = (SpinnerView) findViewById(R.id.bookshelf_all);
		textSortBtn.setPopuView(this);
//		textSortBtn.setOnClickListener(this);
		
		imgSwitch = (ImageView) findViewById(R.id.img_switch);
		imgSwitch.setOnClickListener(this);
		bookshelfShop = (ImageView) findViewById(R.id.bookshelf_shop);
		bookshelfShop.setOnClickListener(this);
		intentseach = (ImageView) findViewById(R.id.seach_text);
		intentseach.setOnClickListener(this);
		bookshelfarrowleft=(ImageView)findViewById(R.id.bookshelf_arrowleft);
		bookshelfarrowleft.setOnClickListener(this);
		bookshelfarrowright=(ImageView)findViewById(R.id.bookshelf_arrowleft);
		bookshelfcurrpag=(TextView) findViewById(R.id.bookshelf_currpag);
		bookshelfcountpag=(TextView) findViewById(R.id.bookshelf_countpag);
		searchEdit = (EditText) findViewById(R.id.edits_serach);
		txtinputpag = (TextView) findViewById(R.id.input_pag);
		bookshelfsearchbtn=(ImageView)findViewById(R.id.bookshelf_searchbtn);
		bookshelfsearchbtn.setOnClickListener(this);
		bookshelf_maincontent_listtop=(ImageView)findViewById(R.id.bookshelf_maincontent_listtop);
		bookshelf_maincontent_listbuttom=(ImageView)findViewById(R.id.bookshelf_maincontent_listbuttom);
		bookshelf_maincontent_listtop.setVisibility(View.GONE);
		bookshelf_maincontent_listbuttom.setVisibility(View.GONE);
		bookshelf_main_pag1=(LinearLayout)findViewById(R.id.bookshelf_main_pag1);
		bookshelfData = new QuerryStudyOperater0100020001Impl();
		
	}

	/**
	 * 加载书架列表数据
	 */
	@SuppressWarnings("rawtypes")
	private void loadData() {
		PageBookEventListener pageBookEventListener = new PageBookEventListener(progressDialog);
		bookShelfFloorAdapter = new BookShelfAdapter(BookShelfActivity.this,bookListData);
		listView.setListAdapter(bookShelfFloorAdapter);
		listView.setPageEvenListener(pageBookEventListener);
		listView.setNetetworkDataInterface(this);
		listView.setmCurrentPage(listviewShelf.getmCurrentPage());
		listView.setTotalPage(listviewShelf.getTotalPage());
//		listView.loadPage(1);
	}

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if(bookListData.size()>0)
		{
			if(StaticData.boolSended==true)
			{
				StaticData.boolSended=!StaticData.boolSended;
				loadDataProgress();
				
				bookListData.clear();
				if(bookShelfFloorAdapter!=null)
				{bookShelfFloorAdapter.notifyDataSetChanged();}
				if(bookshelfgridviewadapter!=null)
				{bookshelfgridviewadapter.notifyDataSetChanged();}
				
				PageBookEventListener pageBookEventListener = new PageBookEventListener(progressDialog);
				bookShelfFloorAdapter = new BookShelfAdapter(BookShelfActivity.this,bookListData);
				listView.setListAdapter(bookShelfFloorAdapter);
				listView.setPageEvenListener(pageBookEventListener);
				listView.setNetetworkDataInterface(this);
				listView.setmCurrentPage(listviewShelf.getmCurrentPage());
				listView.setTotalPage(listviewShelf.getTotalPage());
				listView.loadPage(1);
			}
		}
		
	}

	/**
	 * 加载书架效果数据
	 */
	@SuppressWarnings("rawtypes")
	private void loadGridData(String strSearch) {
		loadDataProgress();
		
		PageBookEventListener pageBookEventListener = new PageBookEventListener(progressDialog);
		bookshelfgridviewadapter = new BookShelfGridAdapter(
				BookShelfActivity.this, bookListData);
		listviewShelf.setListAdapter(bookshelfgridviewadapter);
		listviewShelf.setPageEvenListener(pageBookEventListener);
		listviewShelf.setNetetworkDataInterface(this);   
		listviewShelf.loadPage(1);   
 
	}
  /**
   * 刷新页数的handler
   */
	Handler updatePagHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			Bundle bundle = msg.getData();
			if(bundle!=null){
			bookshelfcurrpag.setText(bundle.getString("reqPage"));
			bookshelfcountpag.setText(bundle.getString("PageCount"));
			}
		}

	};

	
	/**
	 * 加载数据的接口
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageinateContainer condition(int reqPage) {
		// TODO Auto-generated method stub
		Log.i("lzp", "==reqPage===:" + reqPage);
		// Txtcurrpage.setText(String.valueOf(reqPage));
		PageinateContainer pageinateContainer = new PageinateContainer();
		// pageinateContainer.totalCount=36;

		try {
			Log.i("liu", "star request====>>"+System.currentTimeMillis());
			
			Map<String, Object> map = bookshelfData.getBookShelf(
					UiHelp.getUserShareID(BookShelfActivity.this), "12", "",
					"", reqPage, "", "", "", "","", "","");
			Log.i("liu", "end request====>>"+System.currentTimeMillis());
			ArrayList<BookShelf> shelfData = (ArrayList<BookShelf>) map
					.get(DataConstant.SHELF_LIST_KEY);
			pageinateContainer.totalPageCount = (Integer) map
					.get(DataConstant.TOTAL_PAGS);
			pageinateContainer.responseData.addAll(shelfData);

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
	@SuppressWarnings("rawtypes")
	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.duoduo_lesson_back_img:
			finish();  //返回事件
			break;
		case R.id.duoduo_lesson_list_img:
			if (menuWindow != null && menuWindow.isShowing()) {
				menuWindow.dismiss();
			} else {
				BookShelfMenuView bookShelfMenuView = new BookShelfMenuView(BookShelfActivity.this,1);
				View menuView = bookShelfMenuView.createView();
				menuWindow = new PopupWindow(menuView,
						LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				menuWindow.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.menu_bg)); // 设置menu菜单背景
				// menu菜单获得焦点 如果没有获得焦点menu菜单中的控件事件无法响应
				menuWindow.update();
				menuWindow.setFocusable(true);
				menuWindow.showAtLocation(buttomLayot, Gravity.BOTTOM, 0, 100);
			}
			break;
//		case R.id.bookshelf_all:
//			if (hiphop != null && hiphop.isShowing()) {
//				hiphop.dismiss();
//			} else {
//				BookShelfSortView bookShelfSortView = new BookShelfSortView(
//						BookShelfActivity.this);
//				View sortView = bookShelfSortView.onCreateView();
//				hiphop = new PopupWindow(sortView, LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT);
//				hiphop.update();
////				hiphop.showAtLocation(textSortBtn, Gravity.TOP, 135, 65);
//				hiphop.showAsDropDown(textSortBtn, 0, 0);
//			}
//			break;
		case R.id.img_switch:
			Log.i("liu", "flag====:"+flag);
			if (flag) {
				if(bookShelfFloorAdapter!=null){
					Log.i("lzp", "bookListData.size()====:" + bookListData.size());
					Log.i("lzp", "bookListData.size()11111====:"
							+ bookShelfFloorAdapter.getArrayList().size());
					bookShelfFloorAdapter.notifyDataSetChanged();
					listView.setmCurrentPage(listviewShelf.getmCurrentPage());
					listView.setTotalPage(listviewShelf.getTotalPage());
				}else{
					loadData();
				}
				bookshelf_main_pag1.setVisibility(View.VISIBLE);
				listviewShelf.setVisibility(View.GONE);
				listView.setVisibility(View.VISIBLE);
				bookshelf_maincontent_listtop.setVisibility(View.VISIBLE);
				bookshelf_maincontent_listbuttom.setVisibility(View.VISIBLE);
				imgSwitch.setBackgroundResource(R.drawable.bookshelf_switch_selector);
				flag = false;
			} else {
				if (bookshelfgridviewadapter != null) {
					bookshelfgridviewadapter.notifyDataSetChanged();
					listviewShelf.setmCurrentPage(listView.getmCurrentPage());
					listviewShelf.setTotalPage(listView.getTotalPage());
				} else {
					loadGridData("");
				}
				bookshelf_main_pag1.setVisibility(View.GONE);
				listView.setVisibility(View.GONE);
				listviewShelf.setVisibility(View.VISIBLE);
				bookshelf_maincontent_listtop.setVisibility(View.GONE);
				bookshelf_maincontent_listbuttom.setVisibility(View.GONE);
				imgSwitch.setBackgroundResource(R.drawable.bookshelf_list_switch_selector);
				flag = true;
			}
			break;
		case R.id.bookshelf_shop:
			Intent intentshop = new Intent(BookShelfActivity.this,
					BookstoreActivity.class);
			startActivity(intentshop);
			break;
		case R.id.seach_text:
			Intent intentseach = new Intent(BookShelfActivity.this,
					LessonSearchActivity.class);
			startActivity(intentseach);
			break;
		case R.id.bookshelf_searchbtn:
			if(!searchEdit.getText().toString().equals(""))
			{
				 bookListData.clear();
				loadGridData(searchEdit.getText().toString());
			}
			else{Toast.makeText(BookShelfActivity.this, "搜索内容不为空", 1).show();}
			break;
		default:
			break;
		}
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}

	public void popDismiss() {
		if (menuWindow != null && menuWindow.isShowing()) {
			menuWindow.dismiss();
		} 
	}
	
//	public void popDismiss(String strname) {
//		textSortBtn.setText(strname); 
//		if (hiphop != null && hiphop.isShowing()) {
//			hiphop.dismiss();
//		}
//	}

	private void loadDataProgress(){
		progressDialog = new ProgressDialog(BookShelfActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();
	}
	private void cancelDialog(){
		if(progressDialog!=null&& progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		cancelDialog();
		if (hiphop != null && hiphop.isShowing()) {
			hiphop.dismiss();
		}
		super.finish();
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		cancelDialog();
		if (hiphop != null && hiphop.isShowing()) {
			hiphop.dismiss();
		}
		super.onDestroy();
	}

	/**
	 * 弹出下拉列表
	 */
	@Override
	public void show(ViewFlipper v, boolean isShow) 
	{
		if (isShow) {
			hiphop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			hiphop.setFocusable(false);
			hiphop.showAsDropDown(textSortBtn, 0, 0);
		} else {
			if (hiphop != null && hiphop.isShowing()) {
				hiphop.dismiss();
			}
		}
	}
	
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bookshelf_menu, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.down_manager:
			intent = new Intent();
			intent.setClass(this, DowanloadManagerActivity.class);
			startActivity(intent);
			finish();
			break;
		

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(ListView listView, int position) {
		// TODO Auto-generated method stub
		
	}

	
	
	
	
	
}