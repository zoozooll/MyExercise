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

import com.iskinfor.servicedata.CommArgs;
import com.iskinfor.servicedata.pojo.BookShelf;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.StaticData;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

@SuppressWarnings("hiding")
public class BookShelfAdapter extends PageListAdapter<BookShelf> {
	private LayoutInflater inflater;
	public  Context context;
	private DownloadServiceStub downloadServiceStub;
	private TextView gift;
	private TextView recommend;
	private TextView note;
	private TextView label;
	private TextView commentcount;

	public BookShelfAdapter(Context context, ArrayList<BookShelf> arrayList) {
		super(context, arrayList);
		this.context = context;
		inflater = LayoutInflater.from(context);
		downloadServiceStub = new DownloadServiceStub(context);
	}

	@Override
	public View initItemView(View convertView, Object item, int position) {
		final BookShelf book = (BookShelf) item;
	    
		// ViewHolder holder = null;
		// if (convertView == null) {

		ViewHolder holder = new ViewHolder();
		convertView = inflater.inflate(R.layout.bookshelf_activity_item, null);
//		commentcount=(TextView)convertView.findViewById(R.id.comment_count);
//		commentcount.setText(Html.fromHtml("<u><a>"+commentcount.getText()+"</u></a>"));
		
		holder.img = (ImageView) convertView.findViewById(R.id.book_logo);
		holder.proName = (TextView) convertView.findViewById(R.id.bookname);
		holder.ather = (TextView) convertView.findViewById(R.id.author);
		holder.gift = (TextView) convertView.findViewById(R.id.gift);
		holder.gift.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( ((BookShelfActivity)context),BookShelfGiftEditActivity.class);
				intent.putExtra("BookNum", String.valueOf(book.getBookNum()));
				intent.putExtra("BookID", book.getProId());
				intent.putExtra("BookName", book.getProduct().getProName());
				
				context.startActivity(intent);
			}
		});
		holder.recommend=(TextView)convertView.findViewById(R.id.recommend);
		holder.recommend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent intent = new Intent( ((BookShelfActivity)context),BookShelfRecommendEditActivity.class);
				intent.putExtra("BookNum", String.valueOf(book.getBookNum()));
				intent.putExtra("BookID", book.getProId());
				intent.putExtra("BookName", book.getProduct().getProName());
				context.startActivity(intent);
			}
		});
		holder.scoreRatingBar = (RatingBar) convertView.findViewById(R.id.score);
		holder.commentCount = (TextView) convertView.findViewById(R.id.comment_count);
		holder.lasttime = (TextView) convertView.findViewById(R.id.lasttime);
		holder.bookCount = (TextView) convertView.findViewById(R.id.book_count);
		if (book != null) {
			File file = new File(StaticData.IMAGE_DOWNLOAD_ADDR);
			Bitmap bitmap = SdcardUtil.nativeLoad(book.getProduct().getSmallImgPath());
			//true  本地加载图片
			if (bitmap!=null)
			{
				holder.img.setImageBitmap(bitmap);
			}
			else 
			{
				holder.img.setBackgroundResource(R.drawable.book_img_icon);
			}
			ItemViewOclickEvent itemViewOclickEvent = new ItemViewOclickEvent(book, this.context);
			holder.img.setOnClickListener(itemViewOclickEvent);
			holder.proName.setText(book.getProduct().getProName()+"");
			holder.ather.setText(book.getProduct().getAther()+"");
			if (book.getProduct().getProRating() != null) {
				float score = Float.parseFloat(book.getProduct().getProRating());
				holder.scoreRatingBar.setRating(score);
			}
			try{
			int count = book.getProduct().getRatingNum();
			holder.commentCount.setText("("+count + "人评价)");}
			catch(Exception ex){Log.i("PLJ", "PLJ==>book.getProduct().getRatingNum()"+book.getProduct().getRatingNum());}
			
			holder.lasttime.setText(book.getProduct().getIntimeDate());
			holder.bookCount.setText(String.valueOf(book.getBookNum()));
//			holder.gift.setOnClickListener(itemViewOclickEvent);
 	 }
		return convertView;
	}

	private class ViewHolder {
		public ImageView img;
		public TextView proName;
		public TextView ather;
		public TextView gift;
		public TextView recommend;
		public RatingBar scoreRatingBar;
		public TextView commentCount;
		public TextView lasttime;
		public TextView bookCount;
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
