package com.iskyinfor.duoduo.ui.shop;

import java.io.File;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataservice.IOperaterRecordQuerry0200020001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterRecordQuerry0200020001Impl;
import com.iskinfor.servicedata.pojo.Order;
import com.iskinfor.servicedata.pojo.Product;
import com.iskinfor.servicedata.usercenter.service.IManagerUserInfor0300030001;
import com.iskinfor.servicedata.usercenter.serviceimpl.ManagerUserInfor0300030001Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.downloadManage.utils.DuoduoUtils;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;
import com.iskyinfor.duoduo.ui.shop.task.AddToShellCtrler;
import com.iskyinfor.duoduo.ui.shop.task.BookFavoriteTask;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;
import com.iskyinfor.duoduo.ui.shop.task.OrderListTask;
import com.iskyinfor.duoduo.ui.shop.task.PayTask;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;
import com.iskyinfor.duoduo.ui.usercenter.RushMoneyActivity;

/**
 * 购物车
 * 
 * @author zhoushidong
 * 
 */
public class ShoppingCartActivity extends Activity implements OnClickListener {
	private ListView listView;
	public ArrayList<Product> datas;
	// 提示
	private TextView textToast;
	// 购物车
	private Button btnShoppingCar;
	// 收藏夹
	private Button btnFavorite;
	// 订单
	private Button btnOrder;
	// 充值
	private Button btnAccount;
	// 书店
	private Button btnBookstore;
	// 全选
	//private Button btnCheckAll;
	// 删除
	//private Button btnDelete;
	// 继续购买
	private Button btnContinue;
	// 结算
	private Button btnSettle;
	// 总金额
	private TextView allPrice;

	private Order o;
	private IOperaterRecordQuerry0200020001 record = new OperaterRecordQuerry0200020001Impl();
	private IOperaterProduct0200030001 opreater = new OperaterProduct020003001Impl();
	private IManagerUserInfor0300030001 userInfo = new ManagerUserInfor0300030001Impl();
	/**
	 * 选择项；
	 */
	boolean[] isCheck;
	/**
	 * 是否全选
	 */
	private boolean isCheckAll = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		new ShoppingCartTask(this, false).execute();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshopping_activity);
		findView();
		//getData();
		//setValue();
		setEvent();
	}

	private void setEvent() {
		btnShoppingCar.setOnClickListener(this);
		btnFavorite.setOnClickListener(this);
		btnOrder.setOnClickListener(this);
		btnAccount.setOnClickListener(this);
		btnBookstore.setOnClickListener(this);
		//btnCheckAll.setOnClickListener(this);
		//btnDelete.setOnClickListener(this);
		btnSettle.setOnClickListener(this);
		btnContinue.setOnClickListener(this);
	}

	private void findView() {
		textToast = (TextView) findViewById(android.R.id.empty);
		listView = (ListView) findViewById(R.id.shoppingList);

		btnShoppingCar = (Button) findViewById(R.id.btn_shopping_cart);
		btnFavorite = (Button) findViewById(R.id.btn_favorite);
		btnOrder = (Button) findViewById(R.id.btn_order);
		btnAccount = (Button) findViewById(R.id.btn_account);
		btnBookstore = (Button) findViewById(R.id.btn_shop_index);
		//btnCheckAll = (Button) findViewById(R.id.checkAll);
		//btnDelete = (Button) findViewById(R.id.delete);
		allPrice = (TextView) findViewById(R.id.allPrice);
		btnContinue = (Button) findViewById(R.id.continueShopping);
		btnSettle = (Button) findViewById(R.id.settleAccount);
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(this);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}

	private void setValue() {
		btnShoppingCar.setBackgroundResource(R.drawable.btn_blue_selector);
		listView.setCacheColorHint(0);
		allPrice.setText("￥"+new DecimalFormat("#0.00").format(computeTotalPrice()));
		View bookshopCarFoot = LayoutInflater.from(this).inflate(R.layout.bookshop_car_foot, null);
		listView.addFooterView(bookshopCarFoot);
	}

	public void getData(ArrayList<Product> datas) {
		// 取得Task发送过来的数据
		this.datas = datas;
		listView.setEmptyView(textToast);
		if (datas == null || datas.size() == 0) {
		} else {
			listView.setAdapter(new ListViewAdapter(this, datas));
		}
		setValue();
	}

	/**
	 * s刷新ListView
	 */
	void notifyDataSetChanged() {
		((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();
	}

	public void onClick(View v) {
		Intent intent = null;
		switch (v.getId()) {
		case R.id.btn_shopping_cart:
			// new ShoppingCartTask(this).execute();
			break;
		// 收藏夹
		case R.id.btn_favorite:
			intent = new Intent();
			intent.setClass(ShoppingCartActivity.this, BookFavoriteActivity.class);
			startActivity(intent);
			finish();

			break;
		// 订单列表
		case R.id.btn_order:
			intent = new Intent();
			intent.setClass(ShoppingCartActivity.this, OrderListActivity.class);
			startActivity(intent);
			finish();
			break;
		// 进入充值页面
		case R.id.btn_account:
			intent = new Intent();
			intent.setClass(this, RushMoneyActivity.class);
			startActivity(intent);
			break;
		// 书店首页
		case R.id.btn_shop_index:
			intent = new Intent();
			intent.setClass(ShoppingCartActivity.this, BookstoreActivity.class);
			startActivity(intent);
			finish();
			break;
		// 全选
		/*case R.id.checkAll:
			isCheckAll = !isCheckAll;
			if (isCheckAll) {
				for (int i = 0; i < isCheck.length; i++) {
					isCheck[i] = true;
				}
			} else {
				for (int i = 0; i < isCheck.length; i++) {
					isCheck[i] = false;
				}
			}
			notifyDataSetChanged();
			break;
		// 删除
		case R.id.delete:
			deleteProduct();
			notifyDataSetChanged();
			break;*/
		// 结算
		case R.id.settleAccount:
			Toast.makeText(this, "正在结算", Toast.LENGTH_LONG).show();
			if (settleAccount()) {
				new PayTask(this, true).execute(datas);
			}
			break;
		// 继续购物
		case R.id.continueShopping:
			intent = new Intent();
			intent.setClass(ShoppingCartActivity.this, BookstoreActivity.class);
			startActivity(intent);
			finish();
			break;
			//回车
		case R.id.duoduo_lesson_back_img:
			finish();
			break;
		}
		
	}

	/**
	 * 结算；
	 * @return 是否结算成功
	 * @Author Aaron Lee
	 * @date 2011-7-13 下午05:13:22
	 */
	private boolean settleAccount() {
		
		ArrayList<Product> products = datas;
		if (products==null || products.size()==0){
			Toast.makeText(this, R.string.shoppingCarMsgNoSelected,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		
		if (createOrder(products)) {
			Toast.makeText(this, R.string.shoppingCarMsgCreateOrderSuccess,
					Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, R.string.shoppingCarMsgCreateOrderFail,
					Toast.LENGTH_SHORT).show();
			return false;
		}
		try {
			boolean flag = userInfo.business(UiHelp.getUserShareID(this),
					String.valueOf(getPrice()), null);
			if (flag) {
				Toast.makeText(this, R.string.shoppingCarMsgBusinessSuccess,
						Toast.LENGTH_SHORT).show();
				if (!updateState()){
					Toast.makeText(this, R.string.shoppingCarMsgCreateOrderFail, Toast.LENGTH_SHORT);
				}
				boolean f = opreater.putBuyedProducetToShelf(
						UiHelp.getUserShareID(this),products);
				if (f) {
					Toast.makeText(this,
							R.string.shoppingCarMsgAddShellSuccess,
							Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(this, R.string.shoppingCarMsgAddShellFail,
							Toast.LENGTH_SHORT).show();
					AddToShellCtrler.addTOShell(opreater, UiHelp.getUserShareID(this), products);
				}
			} else {
				Toast.makeText(this, R.string.shoppingCarMsgMoneyLess,
						Toast.LENGTH_SHORT).show();
				Intent intent = new Intent();
				intent.setClass(this, RushMoneyActivity.class);
				startActivity(intent);
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	/**
	 * 删除
	 * 
	 * @return
	 *//*
	private boolean deleteProduct() {
		ArrayList<Product> checkItems = getCheckItem();
		boolean flag = false;
		try {
			flag = opreater.deleteGoodsById(UiHelp.getUserShareID(this),
					getCheckItemId());
			if (flag) {
				datas.removeAll(checkItems);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;
	}*/

	/**
	 * 得到选中商品 的Id
	 * 
	 * @return
	 */
/*	private String[] getCheckItemId() {
		String[] s = new String[getCheckItem().size()];
		for (int i = 0; i < getCheckItem().size(); i++) {
			s[i] = getCheckItem().get(i).getProId();
		}
		return s;
	}*/

	/**
	 * 得到所有选中的选项
	 * 
	 * @return
	 */
	/*private ArrayList<Product> getCheckItem() {
		ArrayList<Product> proes = new ArrayList<Product>();
		for (int i = 0; i < datas.size(); i++) {
			if (isCheck[i]) {
				proes.add(datas.get(i));
			}
		}

		return proes;
	}*/

	/**
	 * 生成订单
	 * 
	 * @return
	 */
	private boolean createOrder(ArrayList<Product> products ) {
		try {
			String orderId = opreater.addOrders(UiHelp.getUserShareID(this),
					products);
			o = new Order();
			o.setOrderId(orderId);
			if (o.getOrderId() != null) {
				return true;
			}
			return false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 得到选择的商品的价格
	 * 
	 * @return
	 */
	private double getPrice() {
		double price = 0.0;
		ArrayList<Product> checkItems = datas;
		for (int i = 0; i < checkItems.size(); i++) {
			Product product = checkItems.get(i);
			double itemPrice = Double.parseDouble(product.getProPrice())
					* product.getProNum();
			price += itemPrice;

		}
		checkItems = null;
		return price;
	}

	/**
	 * 更新订单状态
	 */
	private boolean updateState() {
		/*
		 * String [] ids = new String[getCheckItem().size()]; for (int i = 0; i
		 * < getCheckItem().size() ; i++) { ids[i] =
		 * getCheckItem().get(i).getProId(); }
		 */
		try {
			String str = opreater
					.updateorderType(new String[] { o.getOrderId() });
			if (DataConstant.UPDATE_ORDERTYPE_SUCCESS.equals(str)) {
				return true;
			}else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 计算总价
	 * 
	 * @return 总金额；
	 * @Author Aaron Lee
	 * @date 2011-7-8 下午06:47:35
	 */
	private double computeTotalPrice() {
		double totalPrice = 0;
		for (Product p : datas) {
			totalPrice += p.getProNum() * Double.parseDouble(p.getProPrice());
		}
		return totalPrice;
	}

	class ListViewAdapter extends PageListAdapter<Product> {
		private LayoutInflater inflater;

		public ListViewAdapter(Context context, ArrayList<Product> datas) {
			super(context, datas);

			inflater = LayoutInflater.from(ShoppingCartActivity.this);
			isCheck = new boolean[datas.size()];
			for (int i = 0; i < isCheck.length; i++) {
				isCheck[i] = true;
			}
		}

		@Override
		public View initItemView(View convertView, Object object, int position) {
			FolderView holder;
			Product product = (Product) object;
			if (convertView == null) {
				holder = new FolderView();
				convertView = inflater.inflate(
						R.layout.bookshopping_list_items, null);

				holder.imageView = (ImageView) convertView
						.findViewById(R.id.bookImage);
				/*holder.bookCheck = (ImageView) convertView
						.findViewById(R.id.bookCheck);*/
				holder.textBookName = (TextView) convertView
						.findViewById(R.id.bookName);
				holder.textBookPrice = (TextView) convertView
						.findViewById(R.id.bookPrice);
				holder.btnCollect = (Button) convertView
						.findViewById(R.id.collect);
				holder.btnDelete = (Button) convertView
						.findViewById(R.id.delete);
				convertView.setTag(holder);
			} else {
				holder = (FolderView) convertView.getTag();
			}

		/*	if (isCheck[position]) {
				holder.bookCheck.setBackgroundResource(R.drawable.checked);
			} else {
				holder.bookCheck.setBackgroundResource(R.drawable.unchecked);
			}*/
			File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
			// true 本地加载图片
			if (SdcardUtil.isNativeLoad(file, product.getSmallImgPath())) {
				Bitmap bitmap = SdcardUtil.nativeLoad(file,
						product.getSmallImgPath());
				holder.imageView.setImageBitmap(bitmap);
			} else {
				new ImageTask(holder.imageView).execute(CommArgs.PATH
						+ product.getSmallImgPath());
			}
			// 小结：
			double itemMoney = (double) (product.getProNum() * Double
					.parseDouble(product.getProPrice()));
			holder.textBookName.setText(DuoduoUtils.showNewText(
					product.getProName(), 10));

			holder.textBookPrice.setText("￥"+(new DecimalFormat("#0.00").format(itemMoney)));

			holder.btnCollect.setText(R.string.collect);

			holder.btnDelete.setText(R.string.delete);
			
			OnClickListener listener = new CheckOnClickListener(position); 
			
			holder.btnCollect.setOnClickListener(listener);

			holder.btnDelete.setOnClickListener(listener);
			//holder.bookCheck.setOnClickListener(listener);

			return convertView;
		}
		/**
		 * 书店列表中某个选项的点击事件类；
		 * @author Aaron Lee
		 * @date 2011-8-7 下午03:28:32
		 */
		class CheckOnClickListener implements OnClickListener {
			private int position;

			public CheckOnClickListener(int position) {
				this.position = position;
			}

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				/*case R.id.bookCheck:
					ImageView imageCheck = (ImageView) v;
					isCheck[position] = !isCheck[position];
					if (isCheck[position]) {
						imageCheck.setBackgroundResource(R.drawable.checked);
					} else {
						imageCheck.setBackgroundResource(R.drawable.unchecked);
					}
					// 判断是否全选，然后改变全选的状态
					boolean temp = true;
					for (boolean b : isCheck) {
						if (!b) {
							temp = false;
							break;
						}
					}
					isCheckAll = temp;
					break;*/
				/*
				 * 某一项选择删除
				 */
				case R.id.delete: {

					Product product = datas.get(position);
					boolean isDeleteSuccess = false;
					v.setClickable(false);
					try {
						isDeleteSuccess = opreater.deleteGoodsById(
								UiHelp.getUserShareID(ShoppingCartActivity.this),
								new String[] { product.getProId() });
					} catch (Exception e) {

						e.printStackTrace();
					}
					if (isDeleteSuccess) {
						Toast.makeText(ShoppingCartActivity.this, "删除成功",
								Toast.LENGTH_SHORT);
					} else {
						Toast.makeText(ShoppingCartActivity.this, "删除失败",
								Toast.LENGTH_SHORT);
						v.setClickable(true);
					}
					datas.remove(position);
					notifyDataSetChanged();
					allPrice.setText(new DecimalFormat("#0.00").format(computeTotalPrice()));
					break;
				}

				case R.id.collect: {
					Product product = datas.get(position);
					boolean flag = false;
					IOperaterProduct0200030001 operaterProduct = new OperaterProduct020003001Impl();
					try {
						flag = operaterProduct.saveGoods(
								UiHelp.getUserShareID(ShoppingCartActivity.this),
								new String[] { product.getProId() });
					} catch (Exception e) {
						e.printStackTrace();
						flag = false;
					}
					if(flag){
						Toast.makeText(context, "收藏成功", Toast.LENGTH_SHORT).show();
					}
					break;
				}

				}
			}
		};

		class FolderView {
			ImageView imageView;
			//ImageView bookCheck;
			TextView textBookName;
			TextView textBookPrice;
			TextView tvProNum;
			TextView tvItemMoney;
			Button btnCollect;
			Button btnDelete;
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
