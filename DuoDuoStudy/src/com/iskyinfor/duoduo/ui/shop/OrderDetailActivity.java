package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.task.BookFavoriteTask;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderListTask;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;
import com.iskyinfor.duoduo.ui.usercenter.RushMoneyActivity;

public class OrderDetailActivity extends Activity implements OnClickListener {
	private final String  [] titleStr = {"编号" ,"商品名称" ,"价格" ,"数量"  ,"操作" }; 
	//List
	private ListView listView; 
	//private HorizontalScrollView horizontal;
	private TableLayout tableLayout;
	private android.widget.TableRow tableRow;
	private ArrayList<Product> orderListData;
	//购物车
	private Button btnShoppingCar;
	//收藏夹
	private Button btnFavorite;
	//订单
	private Button btnOrder;
	//充值
	private Button btnAccount;
	//首页
	private Button btnIndex;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderdetail_activity);
		getData();
		findView();
		setEvent();
		createView();
		
	}
	
	private void findView() {
		btnShoppingCar = (Button) findViewById(R.id.btn_shopping_cart);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnAccount = (Button) findViewById(R.id.btn_account);
		btnIndex = (Button) findViewById(R.id.btn_shop_index);
	}



	private void setEvent() {
		btnShoppingCar.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnOrder.setOnClickListener(this);
		btnAccount.setOnClickListener(this);
		btnIndex.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_shopping_cart:
			intent = new Intent();
			intent.setClass(this, ShoppingCartActivity.class);
			startActivity(intent);
			//new ShoppingCartTask(this).execute();
			break;

		case R.id.btn_favorite:
			intent = new Intent();
			intent.setClass(this, BookFavoriteActivity.class);
			startActivity(intent);
			//new BookFavoriteTask(this).execute();
			break;
		case R.id.btn_order:
			intent = new Intent();
			intent.setClass(this, OrderListActivity.class);
			startActivity(intent);
			//new OrderListTask(this).execute();
			break;
		case R.id.btn_account:
			intent = new Intent();
			intent.setClass(this, RushMoneyActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_shop_index:
			intent = new Intent();
			intent.setClass(this, BookstoreActivity.class);
			startActivity(intent);
			//new BookstoreTask(this).execute(StaticData.ALL , 1);
			break;
		}
	}
	
	private void getData() {
		Intent intent = getIntent();
		orderListData = (ArrayList<Product>) intent.getSerializableExtra(StaticData.STORE_KEY_PRODUCTES);
		/*for (int i = 0; i < 20 ; i++) {
			Order o = new Order();
			o.setOrderId("编号" + i);
			o.setProName("名称------" + i);
			o.setOrderAccount("Y++" + 50);
			o.setOrderDate("2011-12-12");
			o.setOrdeState();
			orderListData.add(o);
		}*/
	}

	private void createView() {
		//horizontal = (HorizontalScrollView) findViewById(R.id.HorizontalScrollView01);
		tableLayout = new TableLayout(this);
		tableLayout.setLayoutParams(new TableLayout.LayoutParams(android.widget.TableLayout.LayoutParams.FILL_PARENT , TableLayout.LayoutParams.FILL_PARENT));
		
		LayoutParams layoutParams2 = new LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
		int [] widths = new int[]{80 , 110 , 110 , 80 , 80};
		//添加表头
		tableRow = new android.widget.TableRow(this);
		for (int i = 0; i < cells.length ; i++) {
			TextView textCell = new TextView(this);
			textCell.setText(titleStr[i]);
			textCell.setTextColor(0xFF000000);	//字体设置为黑色
			textCell.setGravity(Gravity.CENTER);
			textCell.setWidth(widths[i]);
			tableRow.addView(textCell);
		}
		tableLayout.addView(tableRow , layoutParams2);
		android.view.ViewGroup.LayoutParams layoutParams =
			  new LayoutParams(ViewGroup.LayoutParams
			.FILL_PARENT , 1);
		//添加表格内容
		for (int row = 0 ; row < orderListData.size(); row++) {
			tableRow = new android.widget.TableRow(this);
			Product order = orderListData.get(row);
			ArrayList<Object> content = new ArrayList<Object>();
			content.add(order.getProId());
			content.add(order.getProName());
			content.add(order.getProPrice());
			content.add(order.getProNum());
			TableCell [] cells = createTableCell(content , widths);
			
			for (int i = 0; i < cells.length ; i++) {
				if (i == 1) {
					LinearLayout layout = new LinearLayout(this);
					ImageView bookImage = new ImageView(this);
					TextView bookName = new TextView(this);
					
					bookImage.setBackgroundResource(R.drawable.bookone);
					bookName.setText(order.getProName());
					layout.addView(bookImage);
					layout.addView(bookName);
					tableRow.addView(layout);
					continue;
					
				} else if (i == cells.length - 1) {
					LinearLayout layout = new LinearLayout(this);
					TextView textCell1 = new TextView(this);
					TextView textCell2 = new TextView(this);
					textCell1.setText(Html.fromHtml("<u>看书</u>"));
					textCell1.setOnClickListener(new TableOnClickListener(row));
					layout.addView(textCell1);
					textCell2.setText(Html.fromHtml("<u>赠送</u>"));
					layout.addView(textCell2);
					layout.setGravity(Gravity.CENTER);
					textCell1.setTextColor(Color.BLUE);
					textCell2.setTextColor(Color.BLUE);
					tableRow.addView(layout);
					continue;
				} else {
					TextView textCell = new TextView(this);
					
					textCell.setGravity(Gravity.CENTER);
					textCell.setTextColor(0xFF000000);
					textCell.setWidth(widths[i]);
					textCell.setText((CharSequence)cells[i].getContent());
					
					tableRow.addView(textCell);
				}
				
			}
			
			View view=new View(this);
			view.setBackgroundColor(Color.GRAY);
			tableLayout.addView(view , layoutParams);
			tableLayout.addView(tableRow , layoutParams2);
		}
		
		tableLayout.setStretchAllColumns(true);
		//horizontal.addView(tableLayout);
	}
	
	TableCell [] cells = new TableCell[5];
	private TableCell[] createTableCell(ArrayList<Object> content , int width[]) {
		
		for (int index = 0; index < content.size(); index++) {
			cells[index] = new TableCell(width[index], 0);
			if (cells[index].getType() == 0) {
				if (content.get(index) instanceof Integer) {
					switch ((Integer)content.get(index)) {
					case 0:
						cells[index].setContent("未支付");
						break;
					case 1:
						cells[index].setContent("已支付");
						break;
						
					case 2:
						cells[index].setContent("未支付");
						break;
					}
				} else { 
					cells[index].setContent((String)content.get(index));
				}
			} else {
				cells[index].setContent((Bitmap)content.get(index));
			}
		}
		return cells;
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	
	class TableOnClickListener implements OnClickListener {
		
		private int row;	//得到表格点击的是哪一行
		private int column;	//表格的列
		
		public TableOnClickListener(int row) {
			this.row = row;
		}
		public TableOnClickListener(int row, int column) {
			this.row = row;
			this.column = column;
		}
		@Override
		public void onClick(View v) {
			Intent intent = new Intent();
			intent.setClass(OrderDetailActivity.this, GivedActivity.class);
			startActivity(intent);
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
	
}	
