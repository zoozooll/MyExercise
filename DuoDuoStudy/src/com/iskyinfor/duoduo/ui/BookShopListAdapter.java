package com.iskyinfor.duoduo.ui;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.text.Html;
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
import com.iskyinfor.duoduo.ui.shop.util.BookStoreUtil;

public class BookShopListAdapter extends PageListAdapter<Product> {

	private LayoutInflater inflater;
	private Resources res;

	public BookShopListAdapter(Context context, ArrayList<Product> arrayList) {
		super(context, arrayList);
		this.arrayList = arrayList;
		inflater = LayoutInflater.from(context);
		res = context.getResources();
	}

	int pos;

	class FolderView {
		ImageView imageView;
		TextView textBookName;
		TextView textBookAuthor;
		TextView textBookPrice;
		TextView textNum;
	}

	@Override
	public View initItemView(View convertView, Object object, int position) {
		FolderView holder = null;
		Product product = (Product) object;
		if (convertView == null) {
			holder = new FolderView();
			convertView = inflater.inflate(R.layout.bookshop_listview_item,
					null);
			holder.imageView = (ImageView) convertView
					.findViewById(R.id.bookImage);
			holder.textBookName = (TextView) convertView
					.findViewById(R.id.bookName);
			holder.textBookAuthor = (TextView) convertView
					.findViewById(R.id.bookAuthor);
			holder.textBookPrice = (TextView) convertView
					.findViewById(R.id.bookPrice);
			holder.textNum = (TextView) convertView.findViewById(R.id.bookNum);

			convertView.setTag(holder);
		} else {
			holder = (FolderView) convertView.getTag();
		}
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
		holder.textBookName.setText(product.getProName());
		holder.textBookAuthor.setText(res.getString(R.string.storeListAuthor)
				+ BookStoreUtil.getRealAuthor(product.getAther()));
		holder.textBookPrice
				.setText(Html.fromHtml(res.getString(R.string.storeListPrice)
						+ "<font color='red'>￥"
						+ BookStoreUtil.getRealMoney(product.getProPrice())
						+ "</font>"));
		holder.textNum.setText(Html.fromHtml("<font color='green' size='18px'>"
				+ String.valueOf(product.getRatingNum())
				+ "</font><font color='black' size='16px'>"
				+ res.getString(R.string.storeBookNumStr) + "</font>"));
		return convertView;
	}

	public void setArrayList(ArrayList<Product> arrayList) {
		this.arrayList = arrayList;
	}

}
