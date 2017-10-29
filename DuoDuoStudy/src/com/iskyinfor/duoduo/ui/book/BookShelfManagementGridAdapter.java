package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;

import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.book.BookShelfGridAdapter.ViewHolder;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

public class BookShelfManagementGridAdapter extends PageListAdapter<BookShelf> {
	private LayoutInflater inflater;
	private Activity context;
	public List<BookShelf> booksList;
	public static final int columnsNubmber = 5;
	private DownloadServiceStub downloadServiceStub;
	private int itemWidth = 80;
	private int itemHeight = 90;
	public static final int landspace = 10;
	
	public BookShelfManagementGridAdapter(Activity context,
			ArrayList<BookShelf> arrayList) {
		super(context, arrayList);
		this.context = context;
		this.booksList = arrayList;
		inflater = LayoutInflater.from(context);
		computeItemWidth();
	}

	@Override
	public int getCount() {
		int count;
		if (booksList.size() % columnsNubmber == 0) {
			count=booksList.size() / columnsNubmber;
		} else {
			count=booksList.size() / columnsNubmber + 1;
		}
		if(count<7){
			count=7;
		}
		return count;
	}

	@Override
	public Object getItem(int position) {
		Object o = null;
		try {
			o=booksList.get(position);
		} catch (Exception e) {
			o=new Object();}
		return o;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		convertView = (LinearLayout) inflater.inflate(R.layout.bookshelfmanage_grid_child,null);
		ViewHolder holder = new ViewHolder();
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);
		Log.i("PLJ", "itemWidth==>"+itemWidth);
		holder.imageFrameLayout = new ArrayList<FrameLayout>();
		for (int i = 0; i < columnsNubmber; i++) {
			final int ii=i;
			FrameLayout framelayout=(FrameLayout)this.context.getLayoutInflater().inflate(R.layout.bookshelfmanage_imgchk_child, null);
			ImageView imgchk_img =(ImageView)framelayout.findViewById(R.id.bookshelfmanage_imgchk_img);
			CheckBox imgchk_chk =(CheckBox)framelayout.findViewById(R.id.bookshelfmanage_imgchk_chk);
			framelayout.setLayoutParams(lp);
			LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.bokshelfmanage_linear);
			linearLayout.addView(framelayout);
			
			if(position*5+i<=(booksList.size()-1))
			{
				imgchk_img.setBackgroundResource(R.drawable.book_img_icon);
				imgchk_chk.setVisibility(View.VISIBLE);
			}
			holder.imageFrameLayout.add(framelayout);
		}
		return convertView;
	}

	@Override
	public View initItemView(View v, Object object, int position) {
		return null;
	}

	/**
	 * 计算item的宽度 和高度
	 */
	private void computeItemWidth() {
		Display display = context.getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		int screenHeight = display.getHeight();
		itemWidth = (screenWidth / columnsNubmber)- landspace;
	}

	class ViewHolder {
		List<ImageView> img;
		List<CheckBox> chk;
		
		List<ImageView> forceGroups;
		List<FrameLayout> imageFrameLayout;
	}
	
}
