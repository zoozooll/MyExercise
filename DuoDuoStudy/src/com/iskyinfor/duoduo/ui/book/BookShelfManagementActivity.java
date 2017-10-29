package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.lesson.LessonSearchActivity;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;

public class BookShelfManagementActivity extends Activity implements OnClickListener{
	
	private ArrayList<BookShelf> bookListData = new ArrayList<BookShelf>();
	private ImageView listMenu,img_results;
	private GridView popupGridView = null;
    private LinearLayout layout = null;
    private PopupWindow popupWindow = null;
    private TextView selectTilte;
    private ProgressDialog progressDialog;
    private BookShelfManagementGridAdapter bookShelfmanagementgridadapter;
    private PageListView bookshelfmanage_lv;
    private ImageView bookshelfmanagement_shop;
	
	GridView gridview;
	ImageView bookshelfmanagement_bottomfh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	     setContentView(R.layout.bookshelf_management);
	     InitView();
	     loadGridData("");
	}
	
	/**
	 * 加载书架效果数据
	 */
	private void loadGridData(String strSearch) {
		progressDialog = new ProgressDialog(BookShelfManagementActivity.this);
		progressDialog.setMessage("加载数据中...");
		progressDialog.show();
		
//		PageBookEventListener pageBookEventListener = new PageBookEventListener(
//				progressDialog);
		for(int i=0;i<26;i++)
		{
			BookShelf bookShelf=new BookShelf();
			bookShelf.setProId(String.valueOf(i));
			bookListData.add(bookShelf);
			}
		bookShelfmanagementgridadapter = new BookShelfManagementGridAdapter(
				BookShelfManagementActivity.this, bookListData);
		bookshelfmanage_lv.setListAdapter(bookShelfmanagementgridadapter);
//		bookshelfmanage_lv.setPageEvenListener(pageBookEventListener);
//		bookshelfmanage_lv.setNetetworkDataInterface(this);
//		bookshelfmanage_lv.loadPage(1,strSearch);
		progressDialog.dismiss();

	}
	private void InitView()
	{
		listMenu = (ImageView) findViewById(R.id.duoduo_lesson_list_img);
		listMenu.setOnClickListener(this);	
		bookshelfmanagement_bottomfh=(ImageView)findViewById(R.id.duoduo_lesson_back_img);
		bookshelfmanagement_bottomfh.setOnClickListener(this);	
		bookshelfmanagement_shop = (ImageView) findViewById(R.id.bookshelf_shop);
	   
		bookshelfmanagement_shop.setOnClickListener(this);
		
		bookshelfmanage_lv= (PageListView) findViewById(R.id.bookshelfmanage_lv); 
	}

	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		   case R.id.bookshelf_shop:
			   Intent intent = new Intent(BookShelfManagementActivity.this,BookstoreActivity.class);
				startActivity(intent);
				break;
		   case R.id.duoduo_lesson_back_img:
			   finish();
			   break;
		   case R.id.duoduo_lesson_list_img:
			   showPopupMenu(); 
			   break;
			default :
				break;
		}
		
	}
	
	protected void showPopupMenu() 
	{
		layout = new LinearLayout(BookShelfManagementActivity.this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		initPopupMenu();
		layout.addView(popupGridView);
		
		popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.bookshelf_indextopback));	       
		popupWindow.setFocusable(true);
		popupWindow.update();
		
		
		selectTilte = (TextView) popupGridView.getItemAtPosition(0);
		selectTilte.setBackgroundColor(0x00);
		popupWindow.showAtLocation(findViewById(R.id.duoduo_lesson_list_img),Gravity.BOTTOM, 0, 40);
	}
	protected void initPopupMenu()
	{
		popupGridView = new GridView(BookShelfManagementActivity.this);
		popupGridView.setLayoutParams(new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(2);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		BookShelfManagementMenuTitleAdapter adapter = new BookShelfManagementMenuTitleAdapter(this, 
		new String[] { "全选","删除"}, 15, 0xFF000000);
		popupGridView.setAdapter(adapter);
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

}
