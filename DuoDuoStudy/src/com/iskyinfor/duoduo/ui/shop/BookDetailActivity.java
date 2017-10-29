package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.util.BookStoreUtil;

/**
 * 书籍详情
 * @author zhoushidong
 *
 */
public class BookDetailActivity extends Activity {
	
	//各种组件
	private TextView tvwBookName;
	private ImageView ivwBookImage;
	private TextView tvwPrice;;
	private TextView tvwAuthor;;
	private TextView tvwPublish;
	private RatingBar rbrBookGrade;
	private TextView tvwBookGrade;
	private ImageView ibnReduceNum;
	private EditText etxBookNum;
	private ImageView ibnPlusNum;
	private TextView tvwBookFavion;
	private TextView tvwRecommend;
	private TextView tvwIntroduction;
	private Product product;
	private Button btnGotoStore;
	private Button btnGotoFavicion;
	
	int num = 1;
	
	private IOperaterProduct0200030001 operaterProduct = new OperaterProduct020003001Impl();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdetail_activity);
		findView();
		Intent intentData = getIntent();
		product = (Product) intentData.getSerializableExtra("product");
		setValue();
		setEvent();
	}

	

	private void setValue() {
		tvwBookName.setText(product.getProName());
		String value = "<font color= 'black'><b>价格：</b></font><font color= 'red'>￥";
		value+= BookStoreUtil.getRealMoney(product.getProPrice())+"</font>";
		tvwPrice.setText(Html.fromHtml(value));
		tvwAuthor.setText("作者："+BookStoreUtil.getRealAuthor(product.getAther()));
		tvwPublish.setText("出版："+BookStoreUtil.getRealAuthor(product.getPublisher()));
		rbrBookGrade.setProgress(product.getRatingNum());
		tvwBookGrade.setText(product.getProRating());
		value = "<font color= '#444444'><b>收藏：</b></font>";
		value+= "<font color= 'green'>"+BookStoreUtil.getRealMoney(product.getCollectionNum())+"</font>";
		tvwBookFavion.setText(Html.fromHtml(value));
		value = "<font color= '#444444'><b>推荐：</b></font>";
		value+= "<font color= 'green'>"+String.valueOf(product.getRatingNum())+"</font>";
		tvwRecommend.setText(Html.fromHtml(value));
		tvwIntroduction.setText(product.getIntroContent());
		Bitmap bitmap = SdcardUtil.nativeLoad(product.getBigImgPath());
		//true  本地加载图片
		if (bitmap!=null)
		{
			ivwBookImage .setImageBitmap(bitmap);
		}
		else 
		{
			ivwBookImage.setBackgroundResource(R.drawable.book_img_icon);
		}
	}

	private void findView() {
		tvwBookName = (TextView) findViewById(R.id.tvwBookName);
		ivwBookImage = (ImageView) findViewById(R.id.ivwBookImage);
		tvwPrice = (TextView) findViewById(R.id.tvwPrice);
		tvwAuthor = (TextView) findViewById(R.id.tvwAuthor);
		tvwPublish = (TextView) findViewById(R.id.tvwPublish);
		rbrBookGrade = (RatingBar) findViewById(R.id.rbrBookGrade);
		tvwBookGrade = (TextView) findViewById(R.id.tvwBookGrade);
		ibnReduceNum = (ImageButton) findViewById(R.id.ibnReduceNum);
		etxBookNum = (EditText) findViewById(R.id.etxBookNum);
		ibnPlusNum = (ImageButton) findViewById(R.id.ibnPlusNum);
		tvwBookFavion = (TextView) findViewById(R.id.tvwBookFavion);
		tvwRecommend = (TextView) findViewById(R.id.tvwRecommend);
		tvwIntroduction = (TextView) findViewById(R.id.tvwIntroduction);
		btnGotoStore = (Button) findViewById(R.id.btnGotoStore);
		btnGotoFavicion = (Button) findViewById(R.id.btnGotoFavicion);
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(clickListener);
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
	}                                       
	
	private void setEvent() {
		ibnReduceNum.setOnClickListener(clickListener);
		ibnPlusNum.setOnClickListener(clickListener);
		btnGotoStore.setOnClickListener(clickListener);
		btnGotoFavicion.setOnClickListener(clickListener);
	}
	
	
	/**
	 * 添加收藏
	 * @Author Aaron Lee
	 * @date 2011-7-12 下午05:30:07
	 */
	private void addCollect() {
		boolean flag = false;
		try {
			flag = operaterProduct.saveGoods(UiHelp.getUserShareID(this), new String[]{product.getProId()});
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag) {
			Toast.makeText(this, "添加收藏成功", Toast.LENGTH_SHORT).show();
			Intent intent = new Intent();
			intent.setClass(this, BookFavoriteActivity.class);
			startActivity(intent);
			finish();
		} else {
			Toast.makeText(this, "添加收藏失败", Toast.LENGTH_SHORT).show();
		}
	}
	
	/**
	 * 添加购物车
	 * @Author Aaron Lee
	 * @date 2011-7-12 下午05:30:07
	 */
	private void addShopping() {
		boolean flag = false;
		String textNum = etxBookNum.getText().toString();
		try {
			num = Integer.parseInt(textNum);
			product.setProNum(num);
			
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(this, "请输入正确数字", Toast.LENGTH_SHORT).show();
			etxBookNum.setText("1");
			return;
		}
		try {
			flag = operaterProduct.addShopCart(UiHelp.getUserShareID(this), product.getProId(),product.getProPrice() , String.valueOf(product.getProNum()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (flag) {
			Toast.makeText(this, "添加购物车成功", Toast.LENGTH_SHORT).show();
			/*ArrayList<Product> productes = new ArrayList<Product>();
			productes.add(product);*/
			//将购买的加到书架里面去
			/*try {
				operaterProduct.putBuyedProducetToShelf(StaticData.userId, productes);
			} catch (TimeoutException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}*/
			//new PutShelfTask(this).execute(productes);
			Intent intent = new Intent();
			intent.setClass(this, ShoppingCartActivity.class);
			startActivity(intent);
			finish();
			//new ShoppingCartTask(this, true).execute();
		} else {
			Toast.makeText(this, "添加购物车失败", Toast.LENGTH_SHORT).show();
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
	
	private OnClickListener clickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.btnGotoStore:
				addShopping();
				break;
				
			case R.id.btnGotoFavicion:
				addCollect();
				break;

			case R.id.ibnPlusNum:
				String textNum = etxBookNum.getText().toString();
				num = Integer.parseInt(textNum);
				num ++;
				etxBookNum.setText(String.valueOf(num));
				break;
			case R.id.ibnReduceNum:
				textNum =etxBookNum.getText().toString();
				num = Integer.parseInt(textNum);
				num --;
				if (num<=1){
					num =1;
				}
				etxBookNum.setText(String.valueOf(num));
				break;
			case R.id.duoduo_lesson_back_img:
				finish();
				break;
			}
		}
	};
}
