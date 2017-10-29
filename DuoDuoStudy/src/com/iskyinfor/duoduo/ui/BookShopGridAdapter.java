package com.iskyinfor.duoduo.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.pojo.Product;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;
import com.iskyinfor.duoduo.ui.shop.ImageTask;

public class BookShopGridAdapter extends PageListAdapter<Product> {

	private LayoutInflater inflater;

	public BookShopGridAdapter(Context context,ArrayList<Product> productes ) {
		super(context , productes);
		inflater = LayoutInflater.from(context);
	}

	static class FolderView {
		ImageView imageView;
		TextView textView;
		TextView textPrice;
	}

	

	@Override
	public View initItemView(View convertView, Object object , int position) {
		FolderView holder = null;
		Product product = (Product) object;
		if (convertView == null) {
			holder = new FolderView();
			convertView = inflater.inflate(R.layout.bookshop_listview_item, null);
			holder.imageView = (ImageView) convertView.findViewById(R.id.bookImage);
			holder.textView = (TextView) convertView.findViewById(R.id.bookName);
			holder.textPrice = (TextView) convertView.findViewById(R.id.bookPrice);
			convertView.setTag(holder);
		} else {
			holder = (FolderView) convertView.getTag();
		}
		
		File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
		//true  本地加载图片
		if (SdcardUtil.isNativeLoad(file , product.getSmallImgPath()))
		{
			Bitmap bitmap = SdcardUtil.nativeLoad(file , product.getSmallImgPath());
			holder.imageView.setImageBitmap(bitmap);
		}
		else 
		{
			new ImageTask(holder.imageView).execute(CommArgs.PATH + product.getSmallImgPath());
		}	
		holder.textView.setText(product.getProName());
		holder.textPrice.setText(context.getResources().getString(R.string.storeListPrice) + product.getProPrice());
		return convertView;
	}
	
	public void setArrayList(ArrayList<Product> arrayList) {
		this.arrayList = arrayList;
	}

}
