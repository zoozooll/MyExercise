package com.iskyinfor.duoduo.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;

import com.iskinfor.servicedata.bookshopdataservice.IShowProduct0200010001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.ShowProduct0200010001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.book.BookShelfActivity;
import com.iskyinfor.duoduo.ui.lesson.LessonActivity;
import com.iskyinfor.duoduo.ui.shop.BookstoreActivity;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;

public class IndexActivity extends Activity {
	
	private GridView gridView/*, popupGridView*/;
	private ImageView /*list_menu,*/ back_page;
//	private LinearLayout layout = null;
//	private PopupWindow popup = null;
	private ArrayList<Product> productes;
	private ArrayList<HashMap<String, Object>> gridData;
	private IShowProduct0200010001 bookShopData = new ShowProduct0200010001Impl();
	
	private int[] gridImage = 
	{ 
			R.drawable.index_bookstore_img,
			R.drawable.index_bookshelf_img,
			R.drawable.index_teaching_img,
			/*R.drawable.index_discuss_img,
			R.drawable.index_practice_img,
			R.drawable.index_homework_img,
			R.drawable.index_exam_selector*/
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.index_activity);
		Log.i("liu", "onCreate");
		initData();
		initView();
	}

	private void initView() {
		gridView = (GridView) findViewById(R.id.GridView);
		gridView.setAdapter(new IndexGridAdapter(this, getLayoutInflater(),
		gridData, 15, R.layout.index_item));
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long arg3) {
				if (position == 0)
				{
					Intent intent = new Intent(IndexActivity.this, BookstoreActivity.class);
					startActivity(intent);
				}
				
				if (position == 1)
				{
					Intent intent = new Intent(IndexActivity.this,
							BookShelfActivity.class);
					startActivity(intent);
				}
				if (position == 2)
				{
					Intent intent = new Intent(IndexActivity.this,
							LessonActivity.class);
					startActivity(intent);
				}
				
		/*		if (position == 3)
				{
					Intent intent = new Intent(IndexActivity.this,
							TalkGardenActivity.class);
					startActivity(intent);
				}
		*/
				/*//仅供测试 到达用户中心
				if (position == 4)
				{
					Intent intent = new Intent(IndexActivity.this,
							MyselfInfoActivity.class);
					startActivity(intent);
				}*/
			}
		});

//		list_menu = (ImageView) findViewById(R.id.duoduo_lesson_list_img);
//		list_menu.setOnClickListener(new OnClickListener()
//		{
//			public void onClick(View v)
//			{
//				showPopupMenu(); 
//			}
//		});

		back_page = (ImageView) findViewById(R.id.duoduo_index_back_img);
		back_page.setOnClickListener(new OnClickListener() {
			public void onClick(View v) 
			{
				AlertDialog dialog = new AlertDialog.Builder(IndexActivity.this)
			    .setTitle("退出").setMessage("是否确定离开主页？")
			    .setPositiveButton("确定",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						IndexActivity.this.finish();
						System.exit(0);
					}
				})
				.setNegativeButton("取消",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which)
					{
						dialog.dismiss();
					}
				}).create();
				
				dialog.show();
			}
		});
	}

	private void initData() {
		String[] gridStr = this.getResources().getStringArray(R.array.grid);
		/*
		 * try { Map<String, Object> resultData = bookShopData.getAllProduct();
		 * } catch (Exception e) { e.printStackTrace(); }
		 */
		gridData = new ArrayList<HashMap<String, Object>>();
		HashMap<String, Object> item;
		for (int i = 0; i < gridStr.length; i++) 
		{
			item = new HashMap<String, Object>();
			item.put("image", gridImage[i]);
			// item.put("image", R.drawable.icon);
			item.put("text", gridStr[i]);
			gridData.add(item);
		}
	}

	//初始化Menu
//	private void initPopupMenu() 
//	{
//		popupGridView = new GridView(IndexActivity.this);
//		popupGridView.setLayoutParams(new LayoutParams(
//				LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
//		popupGridView.setNumColumns(3);
//		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
//		popupGridView.setVerticalSpacing(1);
//		popupGridView.setHorizontalSpacing(1);
//		popupGridView.setGravity(Gravity.CENTER);
//		PopupIndexAdapter adapter = new PopupIndexAdapter(this, new String[] {
//		"个人设置", "推荐分享", "检测更新", "投诉建议", "关于帮助", "退出" }, 16, 0xFFFFFFFF);
//		popupGridView.setAdapter(adapter);
//		
//		popupGridView.setOnItemClickListener(new OnItemClickListener()
//		{
//			public void onItemClick(AdapterView<?> arg0, View view, int position,long arg3)
//			{
//				onChangeItem(view,position);
//			}
//		});
//	}
//
//	protected void onChangeItem(View view, int position)
//	{
//		switch (position) 
//		{
//		case 0:
//			Intent intent = new Intent(IndexActivity.this,MyselfInfoActivity.class);
//			startActivity(intent);
//			break;
//
//		default:
//			break;
//		}
//	}
//
//	//显示popupMenu
//	protected void showPopupMenu() 
//	{
//		layout = new LinearLayout(IndexActivity.this);
//		layout.setOrientation(LinearLayout.HORIZONTAL);
//		initPopupMenu();
//		layout.addView(popupGridView);
//
//		popup = new PopupWindow(layout, LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
//		popup.setBackgroundDrawable(getResources().getDrawable(R.drawable.menu_bg)); 
//		popup.setFocusable(true);
//		popup.update();
//		popup.showAtLocation(list_menu, Gravity.BOTTOM, 0, 40);
//	}
//
//
//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		menu.add("menu");
//		return super.onCreateOptionsMenu(menu);
//	}
//
//
//	@Override
//	public boolean onMenuOpened(int featureId, Menu menu) {
//		if (popup != null) {
//			if (popup.isShowing())
//				popup.dismiss();
//			else {
//				popup.showAtLocation(list_menu,Gravity.BOTTOM, 0, 40);
//			}
//		}
//		return false;
//	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK:
			IndexActivity.this.finish();
			break;
		default:
			break;
		}
		return false;
	}
	
}
