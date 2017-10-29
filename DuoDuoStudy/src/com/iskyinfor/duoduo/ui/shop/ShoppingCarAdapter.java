package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;

import com.iskinfor.servicedata.bookshopdataservice.IOperaterProduct0200030001;
import com.iskinfor.servicedata.bookshopdataserviceimpl.OperaterProduct020003001Impl;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.shop.BookFavoriteActivity.FolderView;
import com.iskyinfor.duoduo.ui.shop.task.ShoppingCartTask;

import android.content.Intent;
import android.graphics.Bitmap;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ShoppingCarAdapter extends BaseAdapter {
	
		private BookFavoriteActivity activity;
		private ArrayList<Product> datas;
	
		private LayoutInflater inflater;
		private boolean isDel = false;	//
		FolderView holder;
		public ShoppingCarAdapter(BookFavoriteActivity activity, ArrayList<Product> datas) {
			this.activity = activity;
			this.datas = datas;
			inflater = LayoutInflater.from(activity);
		}

		public int getCount() {
			return datas.size();
		}

		public Object getItem(int position) {
			return datas.get(position);
		}

		public long getItemId(int position) {
			try {
				return Long.parseLong(datas.get(position).getProId());
				
			} catch (Exception e) {
				return -1;
			}
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			if (convertView == null) {
				holder = new FolderView();
				convertView = inflater.inflate(R.layout.bookfavorite_list_items, null , false);
				holder.textBookName = (TextView) convertView.findViewById(R.id.bookName);
				holder.textBookPrice = (TextView) convertView.findViewById(R.id.bookPrice);
				holder.bookImage = (ImageView) convertView.findViewById(R.id.bookImage);
				holder.imageShoppingCar = (Button) convertView.findViewById(R.id.bookAdd);
				holder.imageDelete = (Button) convertView.findViewById(R.id.bookDelete);
				convertView.setTag(holder);

			} else {
				holder = (FolderView) convertView.getTag();
			}
			
			Product product = datas.get(position);
			Bitmap bitmap = SdcardUtil.nativeLoad(product.getSmallImgPath());
			//true  本地加载图片
			if (bitmap!=null)
			{
				holder.bookImage.setImageBitmap(bitmap);
			}
			else 
			{
				holder.bookImage.setBackgroundResource(R.drawable.nopic_02);
			}
			holder.textBookName.setText(product.getProName());
			holder.textBookPrice.setText("￥"+product.getProPrice() );
			OnClickListener listener = new BookFavoriteListener(product);
			holder.imageDelete.setOnClickListener(listener);
			holder.imageShoppingCar.setOnClickListener(listener);
			return convertView;
		}
		
		class BookFavoriteListener implements OnClickListener{
			private Product product;
			

			public BookFavoriteListener(Product product) {
				super();
				this.product = product;
			}

			@Override
			public void onClick(View v) {
				IOperaterProduct0200030001 operaterProduct = new OperaterProduct020003001Impl();
				switch (v.getId()) {
				case R.id.bookAdd:
					boolean flag = false;
					product.setProNum(1);
					try {
						flag = operaterProduct.addShopCart(UiHelp.getUserShareID(activity), product.getProId(),product.getProPrice() , String.valueOf(product.getProNum()));
					} catch (Exception e) {
						e.printStackTrace();
					}
					if (flag) {
						Toast.makeText(activity, "添加购物车成功", Toast.LENGTH_SHORT).show();
						/*ArrayList<Product> productes = new ArrayList<Product>();
						productes.add(product);*/
						Intent intent = new Intent(activity, ShoppingCartActivity.class);
						activity.startActivity(intent);
						activity.finish();
					} else {
						Toast.makeText(activity, "添加购物车失败", Toast.LENGTH_SHORT).show();
					}
					break;
				case R.id.bookDelete:
					v.setClickable(false);
					try {
						flag = operaterProduct.delFavorate(UiHelp.getUserShareID(activity),new String[] {String.valueOf(product.getProId())});
					} catch (Exception e) {
						flag = false;
						e.printStackTrace();
					}
					if (flag){
						Toast.makeText(activity, "删除收藏成功", Toast.LENGTH_SHORT).show();
					} else {
						Toast.makeText(activity, "删除收藏失败", Toast.LENGTH_SHORT).show();
					}
					v.setClickable(true);
					break;
				}
				
			}
			
		}
	}

