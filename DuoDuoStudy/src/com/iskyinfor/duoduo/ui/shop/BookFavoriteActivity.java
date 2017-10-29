package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableRow;
import android.widget.TextView;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.task.BookFavoriteTask;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderListTask;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;
import com.iskyinfor.duoduo.ui.usercenter.RushMoneyActivity;

/**
 * 收藏夹活动项
 * @author zhoushidong
 *
 */
public class BookFavoriteActivity extends Activity {
	
	private IOperaterRecordQuerry0200020001 record = new OperaterRecordQuerry0200020001Impl();
	//收藏夹ListView
	private ListView listView;
	/**
	 * 获得要显示的所有收藏的产品的数据；
	 */
	private ArrayList<Product>  datas;
	
	private ShoppingCarAdapter adapter;
	//购物车
	private Button btnShoppingCar;
	//收藏夹
	private Button btnFavorite;
	//订单
	private Button btnOrder;
	//充值
	private Button btnAccount;
	//书店首页
	private Button btnBookstore;
	//全选按钮
	private Button btnAll;
	//书籍按钮
	private Button btnBooks;
	//课程按钮
	private Button btnCourseware;
	//练习按钮
	private Button btnExercise;
	//考卷按钮
	private Button btnExamPaper;
	
	private TextView textToast;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		new BookFavoriteTask(this, false).execute();
		setContentView(R.layout.bookfavorite_activity);
		findView();
		//getData();
		//setValue();
		setEvent();
		
	}
	
	TableRow tr = null;
	LinearLayout ll ;
	
	private void setEvent() {
		btnShoppingCar.setOnClickListener(onClickListener);
		btnFavorite.setOnClickListener(onClickListener);
		btnOrder.setOnClickListener(onClickListener);
		btnAccount.setOnClickListener(onClickListener);
		//btnBookstore.setOnClickListener(onClickListener);
		/*btnAll.setOnClickListener(onClickListener);
		btnBooks.setOnClickListener(onClickListener);
		btnCourseware.setOnClickListener(onClickListener);
		btnExercise.setOnClickListener(onClickListener);
		btnExamPaper.setOnClickListener(onClickListener);*/
		
		
	}
	
	private OnClickListener onClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btn_shopping_cart:{
				
				Intent intent = new Intent();
				intent.setClass(BookFavoriteActivity.this, ShoppingCartActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			case R.id.btn_favorite:
				break;
			case R.id.btn_order:{
				
				Intent intent = new Intent();
				intent.setClass(BookFavoriteActivity.this, OrderListActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			case R.id.btn_account:{
				
				Intent intent = new Intent();
				intent.setClass(BookFavoriteActivity.this, RushMoneyActivity.class);
				startActivity(intent);
				finish();
				break;
			}
			/*case R.id.btn_shop_index:
				finish();
				break;*/
			/*case R.id.btn_all:
				
				break;
			case R.id.btn_books:
				
				break;
			case R.id.btn_courseware:
				
				break;
			case R.id.btn_exercise:
				break;
			case R.id.btn_examPaper:
				break;*/
			case R.id.duoduo_lesson_back_img:
				finish();
				break;
			}
		}
	};
	
	public void getData(ArrayList<Product> datas) {
		
		this.datas = datas;
		if (null == datas || datas.size() == 0) {
			//textToast.setVisibility(View.VISIBLE);
		} else {
			listView.setAdapter(new ShoppingCarAdapter(this, datas));
		}
		setValue();
	}
	
	private void findView() {
		listView = (ListView) findViewById(R.id.listView);
		btnShoppingCar = (Button) findViewById(R.id.btn_shopping_cart);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnAccount = (Button) findViewById(R.id.btn_account);
		btnBookstore = (Button) findViewById(R.id.btn_shop_index);
		/*btnAll = (Button) findViewById(R.id.btn_all);
		btnBooks = (Button) findViewById(R.id.btn_books);
		btnCourseware = (Button) findViewById(R.id.btn_courseware);
		btnExercise = (Button) findViewById(R.id.btn_exercise);
		btnExamPaper = (Button) findViewById(R.id.btn_examPaper);*/
		textToast = (TextView) findViewById(R.id.data);
		btnFavorite.setBackgroundResource(R.drawable.btn_blue_selector);
		//将原本书店的按钮功能改为类别功能
		btnBookstore.setText("全部");
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(onClickListener);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}
	
	private void setValue() {
		listView.setEmptyView(findViewById(android.R.id.empty));
		
	}
	
	static class FolderView {
		ImageView bookImage;
		TextView textBookName;
		TextView textBookPrice;
		Button imageShoppingCar;
		Button imageDelete;
		Button imageDel;
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
