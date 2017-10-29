package com.iskyinfor.duoduo.ui.lesson;

import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.DownloadServiceStub;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.book.RequestBookUrlTask;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;

public class LessonGridViewAdapter extends PageListAdapter<StepLesson> {
	private LayoutInflater inflater;
	private Activity context;
	public List<StepLesson> booksList;
	public static final int columnsNubmber = 5;
	@SuppressWarnings("unused")
	private DownloadServiceStub downloadServiceStub;
	private int itemWidth = 80;
	private int itemHeight = 110;
	public static final int landspace = 10;

	public LessonGridViewAdapter(Activity context,ArrayList<StepLesson> arrayList) 
	{
		super(context, arrayList);
		this.context = context;
		this.booksList = arrayList;
		inflater = LayoutInflater.from(context);
		computeItemWidth();
		downloadServiceStub = new DownloadServiceStub(context);
	}

	@Override
	public int getCount() {
		int count = 0;
		if (booksList.size() % columnsNubmber == 0) {
			count = booksList.size() / columnsNubmber;
		} else {
			count = booksList.size() / columnsNubmber + 1;
		}

		if (count < 7) {
			count = 7;
		}

		return count;
	}

	@Override
	public Object getItem(int position) {
		Object o = null;
		try {
			o = booksList.get(position);
		} catch (Exception e) {
			// TODO: handle exception
			o = new Object();

		}

		return o;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {

		convertView = (LinearLayout) inflater.inflate(R.layout.bookshelf_jgt_child, null);
		ViewHolder holder = new ViewHolder();
		LayoutParams lp = new LayoutParams(itemWidth, itemHeight);

		holder.btnBooks = new ArrayList<Button>();
		holder.ivdels = new ArrayList<ImageView>();
		holder.forceGroups = new ArrayList<ImageView>();
		
		for (int i = 0; i < columnsNubmber; i++) 
		{
			final int ii = i;
			ImageView imageView = new ImageView(context);
			imageView.setLayoutParams(lp);
			LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.linear_layout);
			linearLayout.addView(imageView);

			Log.i("liu", "VideoPictureCount===>>>>>>>" + booksList.size());

			if (position * 5 + i <= (booksList.size() - 1)) {
				
				Bitmap bitmap = SdcardUtil.nativeLoad(booksList.get(position * 5 + i).getBigImgPath());
				Log.i("liu", "step class bitmap==>>>>>>>>" + bitmap);
				
				// true 本地加载图片
				if (bitmap != null) 
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
						// TODO 启动下载
						RequestBookUrlTask requestBookUrlTask = new RequestBookUrlTask(
								context, booksList.get(position * 5 + ii)
										.getProId(),booksList.get(position * 5 + ii)
										.getAther(), 0);
						requestBookUrlTask.execute();
					}
				});
				holder.forceGroups.add(imageView);
			}
		}

		return convertView;

	}

	@Override
	public View initItemView(View convertView, Object object, int position)
	{
		return null;
	}

	/**
	 * 计算item的宽度 和高度
	 */
	private void computeItemWidth() 
	{
		Display display = context.getWindowManager().getDefaultDisplay();
		int screenWidth = display.getWidth();
		@SuppressWarnings("unused")
		int screenHeight = display.getHeight();
		itemWidth = screenWidth / columnsNubmber - landspace;
	}

	class ViewHolder {
		List<Button> btnBooks;
		List<ImageView> ivdels;
		List<ImageView> forceGroups;
	}
}