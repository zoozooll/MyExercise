package com.iskyinfor.duoduo.ui.lesson;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.TimeoutException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.iskinfor.servicedata.DataConstant;
import com.iskinfor.servicedata.bookshopdataservice.IQuerryStudyInfor0200020003;
import com.iskinfor.servicedata.bookshopdataserviceimpl.QuerryStudy0200020003Impl;
import com.iskinfor.servicedata.pojo.StepLesson;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.custom.page.PageListView;
import com.iskyinfor.duoduo.ui.custom.page.PageinateContainer;

public class TeacherUi implements PageListView.NetetworkDataInterface {
	/**
	 * 同步教学的布局
	 */
	private View lessonView;
	private Activity context;
	private FrameLayout frameLayout;
	private LayoutInflater inflater;
	private PageListView listView, gridView;
	/**
	 * 控制列表和书架效果的标识
	 */
	private boolean flag = false;
	/**
	 * // 保存取到的同步教学数据
	 */
	private ArrayList<StepLesson> mTeacherList = new ArrayList<StepLesson>();
	ProgressDialog progressDialog;
	/**
	 * 分页事件监听
	 */
	@SuppressWarnings("rawtypes")
	PageLessonEventListener pageLessonEventListener;
	/**
	 * 列表适配器
	 */
	@SuppressWarnings("rawtypes")
	LessonListViewAdapter lladapter;
	/**
	 * 书架效果的适配器
	 */
	LessonGridViewAdapter lessonGridAdapter;
	private boolean isInit = false;

	public boolean isInit() {
		return isInit;
	}

	public void setInit(boolean isInit) {
		this.isInit = isInit;
	}

	/**
	 * 加载数据的接口
	 */
	private IQuerryStudyInfor0200020003 iQueryStudyInfo = null;
	LinearLayout showLayout;
	private LinearLayout changeBG, gridChangeBG;
	private Handler handler = null;

	public TeacherUi(Activity context, Handler handler,
			FrameLayout frameLayout, LinearLayout changeBG,
			LinearLayout gridChangeBG) {
		this.context = context;
		this.frameLayout = frameLayout;
		this.changeBG = changeBG;
		this.gridChangeBG = gridChangeBG;
		this.handler = handler;
		inflater = LayoutInflater.from(context);
	}

	@SuppressWarnings("rawtypes")
	public void init() {
		if (!isInit) {
			isInit = true;
			progressDialog = new ProgressDialog(this.context);
			progressDialog.setMessage("加载数据中...");
			progressDialog.show();
			lessonView = inflater
					.inflate(R.layout.teacher_content_layout, null); // 注册同步教学的布局
			this.frameLayout.removeAllViews();
			this.frameLayout.addView(lessonView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			showLayout = (LinearLayout) lessonView
					.findViewById(R.id.teacher_show_data_layout);
			listView = (PageListView) lessonView
					.findViewById(R.id.duoduo_teacher_listview_fourth);
			gridView = (PageListView) lessonView
					.findViewById(R.id.duoduo_teacher_gridview_fourth);
			pageLessonEventListener = new PageLessonEventListener(
					progressDialog);
			iQueryStudyInfo = new QuerryStudy0200020003Impl();
		}
	}

	public void restView() {
		if (isInit()) {
			this.frameLayout.removeAllViews();
			this.frameLayout.addView(lessonView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		}
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public void loadData() {
		// Log.i("liu", "loadData===:"+flag);

		if (!flag) {
			this.changeBG.setBackgroundResource(R.drawable.page_top_bg);
			this.gridChangeBG.setBackgroundDrawable(null);
			gridView.setVisibility(View.GONE);
			showLayout.setVisibility(View.VISIBLE);

			if (lladapter == null) {
				lladapter = new LessonListViewAdapter(this.context,
						mTeacherList);
				listView.setListAdapter(lladapter);
				listView.setPageEvenListener(pageLessonEventListener);
				listView.setNetetworkDataInterface(this);
				listView.loadPage(1);
			} else {
				lladapter.notifyDataSetChanged();
				listView.setmCurrentPage(gridView.getmCurrentPage());
				listView.setTotalPage(gridView.getTotalPage());
			}

			flag = true;
		} else {
			// TODO 加载书架效果

			this.changeBG.setBackgroundDrawable(null);
			this.gridChangeBG.setBackgroundResource(R.drawable.tit);
			showLayout.setVisibility(View.GONE);
			gridView.setVisibility(View.VISIBLE);

			if (lessonGridAdapter == null) {
				Log.i("liu", "loadGridViewData===:");

				lessonGridAdapter = new LessonGridViewAdapter(this.context,
						mTeacherList);
				gridView.setListAdapter(lessonGridAdapter);
				gridView.setPageEvenListener(pageLessonEventListener);
				gridView.setNetetworkDataInterface(this);
			} else {
				lessonGridAdapter.notifyDataSetChanged();
			}
			gridView.setmCurrentPage(listView.getmCurrentPage());
			gridView.setTotalPage(listView.getTotalPage());
			this.flag = false;
		}

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public PageinateContainer condition(int reqPage) {
		Log.i("yyj", "  arrayList  == >>>>>" + reqPage);

		PageinateContainer pageinateContainer = new PageinateContainer();
		iQueryStudyInfo = new QuerryStudy0200020003Impl();
		Map<String, Object> mTeacherMap;

		try {
			mTeacherMap = iQueryStudyInfo.getFamilesLession(reqPage + "", "");

			ArrayList<StepLesson> arrayList = (ArrayList<StepLesson>) mTeacherMap
					.get(DataConstant.LIST);
			Log.i("yyj", "  总列表  == >>>>>" + arrayList.size());

			pageinateContainer.totalPageCount = (Integer) mTeacherMap
					.get(DataConstant.TOTAL_PAGS);
			pageinateContainer.responseData.addAll(arrayList);

			Log.i("yyj",
					"  总条目  == >>>>>"
							+ (Integer) mTeacherMap.get(DataConstant.TOTAL_NUM));

			Message msg = new Message();
			msg.what = 1;
			msg.obj = (Integer) mTeacherMap.get(DataConstant.TOTAL_NUM);
			handler.sendMessage(msg);

		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return pageinateContainer;
	}

	@Override
	public PageinateContainer condition(int reqPage, String strSearch,
			int intGiftType) {
		// TODO Auto-generated method stub
		return null;
	}

	public void canleDialog() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
	}

}
