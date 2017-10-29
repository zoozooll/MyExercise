package com.iskyinfor.duoduo.ui.lesson;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.downloadManage.utils.SdcardUtil;
import com.iskyinfor.duoduo.ui.custom.page.PageListAdapter;
import com.iskyinfor.duoduo.ui.shop.task.VideoTask;

public class LessonListViewAdapter<T> extends PageListAdapter<T> {
	private Context mContext = null;
	private LayoutInflater factory = null;
	private ArrayList<StepLesson> mStepList;
	private String projectId = null;
	
	@SuppressWarnings("unchecked")
	public LessonListViewAdapter(Context context, ArrayList<T> arrayList) {
		super(context, arrayList);
		mContext = context;
		mStepList = (ArrayList<StepLesson>) arrayList;
		factory = LayoutInflater.from(mContext);
	}

	@SuppressWarnings("unchecked")
	@Override
	public View initItemView(View view, Object object, int position) {
		StepLesson stepLesson = mStepList.get(position);
		ViewHolder holder = null;

		if (view == null) {
			view = factory.inflate(R.layout.lesson_listview_items, null);
			holder = new ViewHolder();
			holder.image = (ImageView) view
					.findViewById(R.id.lesson_list_imgview);
			
			holder.teacherName = (TextView) view
					.findViewById(R.id.lesson_teacher_name);
			
			holder.teacherSchool = (TextView)
			view.findViewById(R.id.lesson_teacher_shcoll);
			
			holder.course = (TextView) view
					.findViewById(R.id.lesson_object_info);
			
			holder.teacherHonour = (TextView) view
					.findViewById(R.id.lesson_teacher_honour);

//			holder.scan = (TextView) view
//					.findViewById(R.id.lesson_share_scan_text);
//			holder.comment = (TextView) view
//					.findViewById(R.id.lesson_share_comment_text);

			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		/**
		 *  课件的ID
		 */
		projectId = stepLesson.getProId();

		/**
		 * 获得图片URL ,本地加载图片
		 */
		String imageUrl = stepLesson.getBigImgPath();
		Bitmap bitmap = SdcardUtil.nativeLoad(imageUrl);
		Log.i("yyj", "图片加载的信息是......." + bitmap);

		if (bitmap != null) 
		{
			holder.image.setImageBitmap(bitmap);
		} 
		else 
		{
			holder.image.setImageResource(R.drawable.book_img_icon);
		}

		
//		if (imageUrl != null || !"".equals(imageUrl)) {
//			try {
//				ImageTask imageTask = new ImageTask(holder.image);
//				imageTask.execute(CommArgs.PATH + imageUrl);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		//学校
//		String teacherSchool = stepLesson.getSchool();
//		if(teacherSchool != null && !"".equals(teacherSchool))
//		{
//			Log.i("yyj", "School Name =====>>>>" + teacherSchool);
//			holder.teacherSchool.setText(teacherSchool);
//		}

		
		// 教师名称
		String teacherName = stepLesson.getAther();
		if (teacherName != null && !"".equals(teacherName)) {
			holder.teacherName.setText(teacherName);
		}

		// 教师荣誉称号
		String teacherJobTitle = stepLesson.getJobsTitele();
		if (teacherJobTitle != null || "".equals(teacherJobTitle)) {
			holder.teacherHonour.setText(teacherJobTitle);
		}

		
		// 课件
		String coourse = stepLesson.getCourseName();
		if (coourse != null || "".equals(coourse)) {
			holder.course.setText(coourse);
			holder.course.setTextColor(Color.BLACK);
		}

		// 浏览观看的次数

		// 点评次数

		// 点击观看影片
		holder.image.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					VideoTask task = new VideoTask(mContext, projectId);
					task.execute();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return view;
	}
	
	
	class ViewHolder 
	{
		ImageView image;
		TextView teacherName;
		TextView teacherSchool;
		TextView teacherHonour;
		TextView course;
//		TextView scan;
//		TextView comment;
	}
	
}
