package com.iskyinfor.duoduo.ui.lesson;

import java.util.HashMap;
import java.util.Map;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import com.dcfs.esb.client.exception.TimeoutException;
import com.iskinfor.servicedata.bookshopdataservice.IQuerryStudyInfor0200020003;
import com.iskinfor.servicedata.bookshopdataserviceimpl.QuerryStudy0200020003Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UIPublicConstant;

public class LessonTask extends AsyncTask<Void, Void, Void> {

	private IQuerryStudyInfor0200020003 iQueryStudyInfo = null;
	private Map<String, Object> mStepMap = null;
	private Map<String, Object> mTeacherMap = null;
	@SuppressWarnings("rawtypes")
	private Map<String, Map> returnMap = null;
	private int maxPage = 0;
	private Handler mHandler;
	private Context mContext = null;
	private ProgressDialog myLoadingDialog;

	public LessonTask(Context con, int page, Handler handler) {
		mContext = con;
		maxPage = page;
		mHandler = handler;
	}

	// 预处理
	@SuppressWarnings("rawtypes")
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		loadDataProgress();
		iQueryStudyInfo = new QuerryStudy0200020003Impl();
		mStepMap = new HashMap<String, Object>();
		mTeacherMap = new HashMap<String, Object>();
		returnMap = new HashMap<String, Map>();
	}

	// 后台运行
	@Override
	protected Void doInBackground(Void... params) {
		try {
			mStepMap = iQueryStudyInfo.getStepLession(maxPage + "", "");
			mTeacherMap = iQueryStudyInfo.getFamilesLession(maxPage + "", "");
			returnMap.put("mStepMap", mStepMap);
			returnMap.put("mTeacherMap", mTeacherMap);
		} catch (TimeoutException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	protected void onCancelled() {
		super.onCancelled();
	}

	// 数据更新
	@Override
	protected void onProgressUpdate(Void... values) {
		super.onProgressUpdate(values);

		if (myLoadingDialog != null && myLoadingDialog.isShowing()) 
		{
			myLoadingDialog.dismiss();
		}
	}

	// 结果显示
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);

		if (myLoadingDialog != null && myLoadingDialog.isShowing())
		{
			myLoadingDialog.dismiss();
		}

		sendHandleMessage();
	}

	// 发送消息
	private void sendHandleMessage() {
		if (mStepMap != null && mStepMap.size() != 0) {
			Message stempMessage = new Message();
			stempMessage.obj = returnMap;
			stempMessage.what = UIPublicConstant.LESSON_DATA_VIDEO_MESSAGE_TAG;
			mHandler.sendMessage(stempMessage);
		}
	}

	private void loadDataProgress() {
		myLoadingDialog = new ProgressDialog(mContext);
		myLoadingDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		myLoadingDialog.setTitle("Lesson");
		myLoadingDialog.setMessage("正在加载数据,请稍候片刻。。。");
		myLoadingDialog.setIcon(R.drawable.video);
		myLoadingDialog.setIndeterminate(false);
		myLoadingDialog.setCancelable(true);
		myLoadingDialog.show();
	}

}