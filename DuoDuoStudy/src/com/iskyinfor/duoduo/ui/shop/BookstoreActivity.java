package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IQurryProductInfor0200020002;
import com.iskinfor.servicedata.bookshopdataservice.IShowProduct0200010001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.QurryProductInfor0200020002Impl;
import com.iskinfor.servicedata.bookshopdataserviceimpl.ShowProduct0200010001Impl;
import com.iskinfor.servicedata.datahelp.PriceComparator;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.OkeyComparator;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.ui.BookShopListAdapter;
import com.iskyinfor.duoduo.ui.SpinnerView;
import com.iskyinfor.duoduo.ui.SpinnerView.SpinnserPopuViewListener;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageListView.NetetworkDataInterface;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;
import com.iskyinfor.duoduo.ui.lesson.LessonSearchActivity;
import com.iskyinfor.duoduo.ui.shop.task.BookstoreTask;

/**
 * 书店首页
 * 
 * @author zhoushidong
 */
public class BookstoreActivity extends Activity implements OnClickListener,
		OnItemClickListener, NetetworkDataInterface,SpinnserPopuViewListener {
	

	//常量;
	/**
	 * 关于搜索的请求号
	 */
	public static final int REQUEST_CODE_RESEARCH = 2;
	/**
	 * 每页显示的个数；
	 */
	public static final int COUNT_PER_PAGE = 12;
	
	public  PopupWindow hiphop;
	/** 
	 * 所有书籍的书架listview
	 */
	private View layoutShowShelf;
	private View layoutShowList;
	private PageListView bookShelfView;
	// 所有书籍的数据
	private ArrayList<Product> productes;
	// 所有书籍的列表
	private PageListView listView;
	// 网格列表切换按钮
	private ImageButton imageBtnMenu;
	// 刷新按钮
	private ImageButton btnRefresh;
	// 收藏按钮
	private ImageButton btnFavorite;
	// 购物车按钮
	private ImageButton btnShoppingCart;
	// 所有
	public SpinnerView btnAll;
	// 书籍
	/*private Button btnBook;
	// 课件
	private Button btnCourseware;
	// 习题
	private Button btnExam;
	// 考卷
	private Button btnExampaper;
	// 团购
	private Button btnGroupshop;*/
	// 价格排序按钮
	private Button btnPrice;
	// 成交排序按钮
	private Button btnOkey;
	// 书架按钮
	//private Button btnBookrack;
	// 查找按钮
	private ImageView search;
	// 高级查找按钮
	private ImageButton advancedSearch;
	// 提示
	private TextView textToast;
	
	
	//总条数
	int total;
	// 价格排序；
	boolean sort = false;

	private IShowProduct0200010001 bookShopData = new ShowProduct0200010001Impl();
	private IQurryProductInfor0200020002 queryInfo = new QurryProductInfor0200020002Impl();

	//适配器
	private BookShopShellAdapter bsShellAdapter;
	private BookShopListAdapter bsListAdapter;
	
	/**
	 * 查看分类方式
	 */
	int showCatalog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		new BookstoreTask(this).execute(StaticData.ALL, 1);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshop_activity);
		findView();
		//getData(StaticData.ALL);

		setEvent();
	}

	private void setEvent() {
		imageBtnMenu.setOnClickListener(this);
		//btnRefresh.setOnClickListener(this);
		//btnFavorite.setOnClickListener(this);
		//btnShoppingCart.setOnClickListener(this);
		listView.setOnItemClickListener(this);
		bookShelfView.setOnItemClickListener(this);

//		btnAll.setOnClickListener(this);
		/*btnBook.setOnClickListener(this);
		btnCourseware.setOnClickListener(this);
		btnExam.setOnClickListener(this);
		btnExampaper.setOnClickListener(this);
		btnGroupshop.setOnClickListener(this);*/

		btnPrice.setOnClickListener(this);
		btnOkey.setOnClickListener(this);
		//search.setOnClickListener(this);
		//btnBookrack.setOnClickListener(this);
		//advancedSearch.setOnClickListener(this);
	}

	private void findView() {
		layoutShowShelf = findViewById(R.id.layoutShowShelf) ;
		layoutShowList = findViewById(R.id.layoutShowList );
		bookShelfView = (PageListView) findViewById(R.id.bookShelfView);
		listView = (PageListView) findViewById(R.id.bookListView);
		imageBtnMenu = (ImageButton) findViewById(R.id.imageBtnMenu);
		/*btnRefresh = (ImageButton) findViewById(R.id.refresh);
		btnFavorite = (ImageButton) findViewById(R.id.favorite);
		btnShoppingCart = (ImageButton) findViewById(R.id.shoppingCart);*/

		/**
		 * 这是我修改的下拉列表
		 */
		btnAll = (SpinnerView) findViewById(R.id.all);
		btnAll.setPopuView(this);
		
		/*btnBook = (Button) findViewById(R.id.books);
		btnCourseware = (Button) findViewById(R.id.courseware);
		btnExam = (Button) findViewById(R.id.example);
		btnExampaper = (Button) findViewById(R.id.exampaper);
		btnGroupshop = (Button) findViewById(R.id.groupshop);*/

		btnPrice = (Button) findViewById(R.id.btnPrice);
		btnOkey = (Button) findViewById(R.id.btnOkey);
		//btnBookrack = (Button) findViewById(R.id.btnBookrack);
		search = (ImageView) findViewById(R.id.search);
		//advancedSearch = (ImageButton) findViewById(R.id.advancedSearch);
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(this);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}


	public void getData(int total, ArrayList<Product> productes) {
		this.total = total;
		this.productes = productes;

		layoutShowList.setVisibility(View.GONE);
		layoutShowShelf.setVisibility(View.VISIBLE);
		bsShellAdapter = new BookShopShellAdapter(this, productes);
		bsListAdapter = new BookShopListAdapter(this, productes);
		bookShelfView.setListAdapter(bsShellAdapter);
		listView.setListAdapter(bsListAdapter);
		
		bookShelfView.setEmptyView(findViewById(R.id.tvwEmptyShell));
		listView.setEmptyView(findViewById(R.id.tvwEmptyList));
		
		listView.setNetetworkDataInterface(this);
		listView.loadPage(1);
	}

	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.bookshop_index, menu);
	    return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
		case R.id.storeShowBookshop:
			intent = new Intent();
			intent.setClass(this, ShoppingCartActivity.class);
			startActivity(intent);
			finish();
			break;
		case R.id.storeShowBookFavorite:
			intent = new Intent();
			intent.setClass(this, BookFavoriteActivity.class);
			startActivity(intent);
			finish();
			break;
			
		case R.id.storeShowBookRefresh:
			new BookstoreTask(this).execute(StaticData.ALL, 1, 1);
			break;
		case R.id.storeShowBookSearch:
			
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Map<String, Object> resultData = null;
		try {
			// 通过Id取得详情
			resultData = queryInfo.getProductById(new String[] { ((productes
					.get(position))).getProId() });
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (resultData != null) {
			Product product = ((ArrayList<Product>) resultData
					.get(DataConstant.LIST)).get(0);
			Intent intent = new Intent();
			intent.putExtra("product", product);
			intent.setClass(this, BookDetailActivity.class);
			startActivity(intent);
		}
	}

	public void onClick(View v) {

		Intent intent = null;
		Comparator<Product> comparator = null;
		switch (v.getId()) {

		case R.id.imageBtnMenu:
			if (layoutShowList.getVisibility() != View.VISIBLE) {
				layoutShowShelf.setVisibility(View.GONE);
				layoutShowList.setVisibility(View.VISIBLE);
				//bsListAdapter.notifyDataSetChanged();
				v.setBackgroundResource(R.drawable.bookshelf_switch_selector);
			} else {
				layoutShowShelf.setVisibility(View.VISIBLE);
				layoutShowList.setVisibility(View.GONE);
				//bsGridAdapter.notifyDataSetChanged();
				v.setBackgroundResource(R.drawable.bookshelf_list_switch_selector);
			}
			break;
		/*case R.id.refresh:
			new BookstoreTask(this).execute(StaticData.ALL, 1, 1);
			break;
		case R.id.favorite:{
			intent = new Intent();
			intent.setClass(this, BookFavoriteActivity.class);
			startActivity(intent);
			finish();
			break;
		}
		case R.id.shoppingCart:{
			intent = new Intent();
			intent.setClass(this, ShoppingCartActivity.class);
			startActivity(intent);
			finish();
			break;
		}*/

		// 按价格排序
		case R.id.btnPrice:
			// 按价格排序
			comparator = new PriceComparator(sort);
			Collections.sort(productes, comparator);
			bsListAdapter.setArrayList(productes);
			bsShellAdapter.setArrayList(productes);
			bsListAdapter.notifyDataSetChanged();
			bsShellAdapter.notifyDataSetChanged();
			sort = !sort;
			break;
		// 按成交数量排序

		case R.id.btnOkey:

			comparator = new OkeyComparator();
			Collections.sort(productes, comparator);
			if (listView.getVisibility() == View.VISIBLE) {
				bsListAdapter.notifyDataSetChanged();
			} else {
				bsShellAdapter.notifyDataSetChanged();
			}
			break;
		// 全部
//		case R.id.all:
//			if (hiphop != null && hiphop.isShowing()) {
//				hiphop.dismiss();
//			} else {
//				BookShopSortView bookshopsortview = new BookShopSortView(
//						BookstoreActivity.this);
//				View sortView = bookshopsortview.onCreateView();
//				hiphop = new PopupWindow(sortView, LayoutParams.WRAP_CONTENT,
//						LayoutParams.WRAP_CONTENT);
//				hiphop.update();
//				hiphop.showAsDropDown(btnAll, 0, 0);
//				
//			}
//			break;
		// 进入高级搜索
		case R.id.advancedSearch:
			intent = new Intent();
			intent.setClass(this, LessonSearchActivity.class);
			startActivityForResult(intent, REQUEST_CODE_RESEARCH);
			break;
		// 一般搜索
		case R.id.search:
			break;
		// 进入书架首页
		/*case R.id.btnBookrack:
			intent = new Intent();
			intent.setClass(this, BookShelfActivity.class);
			startActivity(intent);
			finish();
			break;
		*/
		case R.id.duoduo_lesson_back_img:
			finish();
			break;
		}
	}

	@Override
	public PageinateContainer condition(int reqPage) {
		PageinateContainer<Product> pageinateContainer=new PageinateContainer<Product>();
		pageinateContainer.totalCount=total;
		pageinateContainer.totalPageCount = (total+COUNT_PER_PAGE-1)/COUNT_PER_PAGE;
		ArrayList<Product> products =  (ArrayList<Product>) getDataFromQuery(showCatalog, reqPage).get(DataConstant.LIST);
		if (reqPage>1){
			productes.addAll(products);
		}
		pageinateContainer.responseData = productes;
		return pageinateContainer;
	}

	/**
	 * 刷新列表；
	 * 
	 * @Author Aaron Lee
	 * @date 2011-7-12 上午11:58:59
	 */
	public void notifyDataSetChanged(ArrayList<Product> productes) {
		this.productes = productes;
 		bsShellAdapter.setArrayList(productes);
		bsListAdapter.setArrayList(productes);
		bookShelfView.setListAdapter(bsShellAdapter);
		listView.setListAdapter(bsListAdapter);
	}
	
	/**
	 * 从数据中重新获得引擎的数据
	 * @param type 查看的内容的类型
	 * @param page 页码
	 * @return MAP数据值
	 * @Author Aaron Lee
	 * @date 2011-7-14 下午06:03:41
	 */
	public Map<String, Object> getDataFromQuery(int type, int page){
		Map<String, Object> resultData = null ;
		try {
			switch (type) {
			//查询所有
			case StaticData.ALL:
				resultData = bookShopData.getAllProduct(page);
				break;
				//查询书籍
			case StaticData.BOOKS:
				resultData = bookShopData.getAllBook(page);
				break;
				//查询练习
			case StaticData.COURSEWARE:
				resultData = bookShopData.getAllCourseware(page);
				break;
				//查询习题
			case StaticData.EXAM:
				resultData = bookShopData.getAllExam(page);
				break;
				//查询考卷
			case StaticData.EXAMPAPER:
				resultData = bookShopData.getAllExercise(page);
				break;
				//团购
			case StaticData.GROUPSHOP:
				resultData = bookShopData.getAllProduct(page);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultData;
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
	public PageinateContainer condition(int reqPage, String strSearch,int intGiftType) {
		return null;
	}
	
//	/**
//	 * 被全部分类的方法
//	 */
//     public void BookShopSortAll(String strName)
//     {
//    	 btnAll.setText(strName);
//    	 if(hiphop!=null&&hiphop.isShowing())
//    	 {hiphop.dismiss();}
//     }

     /**
      * 弹出下拉列表
      */
	@Override
	public void show(ViewFlipper v, boolean isShow) 
	{
		if (isShow) {
			hiphop = new PopupWindow(v, LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			hiphop.setFocusable(false);
			hiphop.showAsDropDown(btnAll, 0, 0);
		} else {
			if(hiphop !=null && hiphop.isShowing())
			{
				hiphop.dismiss();
			}
		}
	}
	
	@Override
	public void finish() {
		// TODO Auto-generated method stub
		if (hiphop != null && hiphop.isShowing()) {
			hiphop.dismiss();
		}
		super.finish();
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (hiphop != null && hiphop.isShowing()) {
			hiphop.dismiss();
		}
		super.onDestroy();
	}

	@Override
	public void onItemClick(ListView listView, int position) {
		switch (position) {
		case 0:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.ALL, 1);
			break;
		case 1:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.BOOKS, 1);
			break;
		case 2:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.EXAMPAPER, 1);
			break;
		case 3:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.COURSEWARE, 1);
			break;
		case 4:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.EXAM, 1);
			break;
		case 5:
			new BookstoreTask(BookstoreActivity. this).execute(
					StaticData.GROUPSHOP, 1);
			break;
		
		}
		
	}
	
}