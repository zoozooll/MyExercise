package com.iskyinfor.duoduo.ui.shop;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.log.DuoduoLog;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

public class BookShopShellAdapter extends PageListAdapter<Product> {
	
	private LayoutInflater inflater;
	public static final int columnsNubmber = 5;
	private int itemWidth = 80;
	private int itemHeight = 110;
	public static final int landspace = 10;

	public BookShopShellAdapter(Context context, ArrayList<Product> arrayList) {
		super(context, arrayList);
		inflater = LayoutInflater.from(context);
		computeItemWidth();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		int count;
		if (arrayList.size() % columnsNubmber == 0) {
			count=arrayList.size() / columnsNubmber;
//			return booksList.size() / columnsNubmber;
		} else {
			count=arrayList.size() / columnsNubmber + 1;
//			return booksList.size() / columnsNubmber + 1;
		}
		if(count<7){
			count=7;
		}
		return count;
	}

	@Override
	public Product getItem(int position) {
		// TODO Auto-generated method stub
		Product o = null;
		try {
			o=arrayList.get(position);
		} catch (Exception e) {
			DuoduoLog.w("aaron", "getItem", e);
		}
		
		return o;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		
		convertView = (LinearLayout) inflater.inflate(R.layout.bookshelf_jgt_child,null);
		ViewHolder holder = new ViewHolder();
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);

		holder.btnBooks = new ArrayList<Button>();
		holder.ivdels = new ArrayList<ImageView>();
		holder.forceGroups = new ArrayList<ImageView>();
		for (int i = 0; i < columnsNubmber; i++) {
			final int ii=i;
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(lp);
			LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
			linearLayout.addView(imageView);
			
			//imageView.setImageResource(R.drawable.book3);
			Log.i("liu", "bitmap===:"+(position*5+i<=(arrayList.size()-1)));
			
			Log.i("liu", "bookShelfCount===:"+arrayList.size());
			
			if(position*columnsNubmber+i<=(arrayList.size()-1))
			{
				final Product product = arrayList.get(position*columnsNubmber+i);
				Bitmap bitmap = SdcardUtil.nativeLoad(product.getSmallImgPath());
				Log.i("liu", "bitmap===:"+bitmap);
				//true  本地加载图片
				if (bitmap!=null)
				{
					imageView.setImageBitmap(bitmap);
				}
				else 
				{
					imageView.setImageResource(R.drawable.book_img_icon);
				}
				
				imageView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						
						Intent intent = new Intent();
						intent.putExtra("product", product);
						intent.setClass(context, BookDetailActivity.class);
						context.startActivity(intent);
					}
				});
				holder.forceGroups.add(imageView);
			}
		}
		
		
		return convertView;

	}

	@Override
	public View initItemView(View convertView, Object object, int position) {
		return null;
	}
	
	public void setArrayList(ArrayList<Product> arrayList) {
		this.arrayList=arrayList;
	}
	
	/**
	 * 计算item的宽度 和高度
	 */
	private void computeItemWidth() {
		Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		itemWidth = screenWidth / columnsNubmber - landspace;
	}

	private class ViewHolder {
		List<Button> btnBooks;
		List<ImageView> ivdels;
		List<ImageView> forceGroups;
	}
}
