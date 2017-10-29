package com.iskyinfor.duoduo.ui.book;

import java.io.File;
import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskinfor.servicedata.pojo.RecommentInfor;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.book.BookShelfAdapter.ItemViewOclickEvent;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

public class BookShelfGiftListAdapter extends PageListAdapter<RecommentInfor> {
	public LayoutInflater inflater;
	public  Context context;
	public TextView gift;
	public TextView userId;//用户ID	
	public TextView username;//用户名
	public TextView proId;//书ID
	public TextView proName;//书名
	public ImageView proSmallPic;//书大图
	public ImageView proBigPic;//书小图
	public TextView recommendDate;//时间	
	public TextView reason;//原因
	public boolean boolAll=true;
	
	
	public BookShelfGiftListAdapter(Context context,
			ArrayList<RecommentInfor> arrayList) {
		super(context, arrayList);
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public View initItemView(View convertView, Object item, int position) {
        final RecommentInfor recomment = (RecommentInfor) item;
	   
		//ViewHolder holder = null;
		//if (convertView == null) {

		final ViewHolder holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.bookshelf_gift_child, null);
		
		holder.proBigPic = (ImageView) convertView.findViewById(R.id.bookshelfgift_img);
		holder.proName = (TextView) convertView.findViewById(R.id.bookshelfgift_bookname);
		//holder.ather = (TextView) convertView.findViewById(R.id.bookshelfgift_gifttype);
		holder.username = (TextView) convertView.findViewById(R.id.bookshelfgift_username);
		holder.reason = (TextView) convertView.findViewById(R.id.bookshelfgift_content);
		holder.recommendDate = (TextView) convertView.findViewById(R.id.bookshelfgift_gifttime);
		holder.bookshelfgift_contentall=(TextView)convertView.findViewById(R.id.bookshelfgift_contentall);
		holder.bookshelfgift_content=(TextView)convertView.findViewById(R.id.bookshelfgift_content);
		
		if (recomment != null) {
			File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
			Bitmap bitmap = SdcardUtil.nativeLoad(recomment.getProSmallPic());
			//true  本地加载图片
			if (bitmap!=null)
			{
				holder.proBigPic.setImageBitmap(bitmap);
			}
			else 
			{
				holder.proBigPic.setBackgroundResource(R.drawable.bookshelfgift_img);
			}

			holder.proName.setText(recomment.getProName());
			holder.username.setText(recomment.getUserName());
			holder.reason.setText(recomment.getReason());
			holder.recommendDate.setText(recomment.getRecommendDate());
			
			if(recomment.getReason().length()>20){
				((TextView)convertView.findViewById(R.id.bookshelfgift_content)).setHeight(35);
				((TextView)convertView.findViewById(R.id.bookshelfgift_contentall)).setVisibility(View.VISIBLE);
			}
			else{
				((TextView)convertView.findViewById(R.id.bookshelfgift_contentall)).setVisibility(View.GONE);
			}
//		    
			holder.bookshelfgift_contentall.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(boolAll==true)
					{
					holder.bookshelfgift_content.setHeight(100);
					holder.bookshelfgift_contentall.setText("收起");
					boolAll=false;}
					else{
						holder.bookshelfgift_content.setHeight(35);
						holder.bookshelfgift_contentall.setText("查看全部");
						boolAll=true;	
					}
				}
			});
//			holder.gift.setOnClickListener(itemViewOclickEvent);
 	     }
		return convertView;
	}
	
	public class ViewHolder {
		public TextView userId;//用户ID	
		public TextView username;//用户名
		public TextView proId;//书ID
		public TextView proName;//书名
		public ImageView proSmallPic;//书大图
		public ImageView proBigPic;//书小图
		public TextView recommendDate;//时间	
		public TextView reason;//原因
		public TextView bookshelfgift_contentall,bookshelfgift_content;

	}
	
	class ItemViewOclickEvent implements OnClickListener {
		BookShelf bookShelf;
		Context context;

		public ItemViewOclickEvent(BookShelf bookShelf, Context context) {
			this.bookShelf = bookShelf;
			this.context = context;
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.book_logo:
				// TODO 启动下载
//					RequestBookUrlTask requestBookUrlTask=new RequestBookUrlTask(this.context, bookShelf.getProId(),0);
//					requestBookUrlTask.execute();	
					RequestBookUrlTask requestBookUrlTask=new RequestBookUrlTask(this.context, bookShelf.getProId(),bookShelf.getProduct().getProName(),0);
					requestBookUrlTask.execute();	
			
				break;
//			case R.id.gift:
//				
//				
//				break;
			default:
				break;
			}

		}
	
	}

}
