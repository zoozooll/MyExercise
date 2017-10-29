package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageListView.NetetworkDataInterface;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;
import com.iskyinfor.duoduo.ui.shop.task.BookFavoriteTask;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderDetailTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderListTask;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;
import com.iskyinfor.duoduo.ui.usercenter.RushMoneyActivity;

public class OrderListActivity extends Activity implements OnClickListener,NetetworkDataInterface {
	
	public static final int COUNT_PER_PAGE = 15;
	
	//数据
	 
	private ArrayList<Order> orderListData;
	//没有数据的时候显示提示
	private TextView textToast;
	//
	//listView表格
	//private TableLayout tableLayout;
	//一行数据
	//private android.widget.TableRow tableRow;
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
	
	private LinearLayout lvwOrderListHead;
	
	private PageListView orderList;
	
	private IOperaterRecordQuerry0200020001 record = new OperaterRecordQuerry0200020001Impl();
	
	private int total;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		new OrderListTask(this, true).execute();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.orderlist_activity);
		findView();
		//getData();
		//showingInListTop();
		//createView();
		setEvent();
	}
	
	private void findView() {
		btnShoppingCar = (Button) findViewById(R.id.btn_shopping_cart);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnAccount = (Button) findViewById(R.id.btn_account);
		btnIndex = (Button) findViewById(R.id.btn_shop_index);
		lvwOrderListHead = (LinearLayout) findViewById(R.id.lvwOrderListHead);
		orderList = (PageListView) findViewById(R.id.orderList);
		btnOrder.setBackgroundResource(R.drawable.btn_blue_selector);
		orderList.setEmptyView(findViewById(android.R.id.empty));
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(this);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}

	private void setEvent() {
		btnShoppingCar.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		//btnOrder.setOnClickListener(this);
		btnAccount.setOnClickListener(this);
		btnIndex.setOnClickListener(this);
	}

	public void onClick(View v) {
		Intent intent  = null;
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
		case R.id.btn_account:
			intent = new Intent();
			intent.setClass(this, RushMoneyActivity.class);
			startActivity(intent);
			break;
		case R.id.btn_shop_index:
			intent = new Intent();
			intent.setClass(this, BookstoreActivity.class);
			startActivity(intent);
			//new BookstoreTask(this).execute(StaticData.ALL ,1);
			break;
		case R.id.duoduo_lesson_back_img:
			finish();
			break;
		}
	}

	public  void getData(ArrayList<Order> orderListData, int total) {
		
		this.orderListData = orderListData;
		this.total = total;
		createView();
		/*Map<String, Object> resultData =  null;
		try {
			resultData =  (Map<String, Object>) record.querryOrderInfor(StaticData.userId, "", "","");
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (orderListData == null) {
			orderListData =  new ArrayList<Order>();
			for (int i = 0; i < 20 ; i++) {
				Order o = new Order();
				o.setOrderId("编号" + i);
				o.setProName("名称------" + i);
				o.setOrderAccount("Y++" + 50);
				o.setOrderDate("2011-12-12");
				o.setOrdeState(1);
				orderListData.add(o);
			}
			//orderList.setAdapter(new ListViewAdapter());
		}*//* else {
		if (resultData != null) {
			orderListData =  (ArrayList<Order>) resultData.get(DataConstant.ORDER_KEY);
			
			if (orderListData == null || orderListData.size() == 0) {
				orderList.setVisibility(View.GONE);
				textToast.setVisibility(View.VISIBLE);
			} else {
				//orderList.setAdapter(new ListViewAdapter());
			}
		}
		}*/
	}
	
	
	private void createView() {
		
		
		
		OrderListTableAdapter orderListTableAdapter = new OrderListTableAdapter(this,orderListData);
		orderList.setListAdapter(orderListTableAdapter);
		
	}
	
	
	/**
	 * 显示订单列表的头部信息；
	 * 
	 * @Author Aaron Lee
	 * @date 2011-7-11 下午08:22:59
	 */
	/*private void showingInListTop() {
		TextView textView1 = (TextView) lvwOrderListHead.findViewById(R.id.textView1);
		TextView textView2 = (TextView) lvwOrderListHead.findViewById(R.id.textView2);
		TextView textView3 = (TextView) lvwOrderListHead.findViewById(R.id.textView3);
		TextView textView4 = (TextView) lvwOrderListHead.findViewById(R.id.textView4);
		
		textView1.setText(R.string.shoppingOrderListOrderId);
		textView2.setText(R.string.shoppingOrderListOrderAccount);
		textView3.setText(R.string.shoppingOrderListOrderDate);
		textView4.setText(R.string.shoppingOrderListOrderState);
		
		ViewStub stub = (ViewStub) lvwOrderListHead.findViewById(R.id.layout);
		stub.setLayoutResource(R.layout.shop_orderlist_ctrltext_head);
		stub.inflate();
		
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if (keyCode == KeyEvent.KEYCODE_SEARCH) {
			UiHelp.turnHome(this);
			return true;
		}else{
			return super.onKeyDown(keyCode, event);
		}
	}

	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		return null;
	}

	@Override
	public PageinateContainer condition(int reqPage) {
		PageinateContainer<Order> pageinateContainer=new PageinateContainer<Order>();
		pageinateContainer.totalCount=total;
		pageinateContainer.totalPageCount = (total+COUNT_PER_PAGE-1)/COUNT_PER_PAGE;
		ArrayList<Order> orders =  null;
		IOperaterRecordQuerry0200020001 operaterRecord = new OperaterRecordQuerry0200020001Impl();
		try {
			orders = (ArrayList<Order>) ((Map<String, Object>) (operaterRecord
					.querryOrderInfor(UiHelp.getUserShareID(this), null, null,
							reqPage, String.valueOf(COUNT_PER_PAGE), null)))
					.get(DataConstant.ORDER_KEY);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (reqPage>1){
			orderListData.addAll(orders);
		}
		pageinateContainer.responseData = orders;
		return pageinateContainer;
	}

}
