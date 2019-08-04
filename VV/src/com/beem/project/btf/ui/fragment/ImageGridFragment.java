package com.beem.project.btf.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.adapter.TimeCameraImageAdapter;
import com.beem.project.btf.ui.adapter.TimeCameraImageManageAdapter;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.ui.entity.TimeCameraImageInfo;
import com.beem.project.btf.utils.AppProperty;
import com.beem.project.btf.utils.TimeCameraMaterialUtil;
import com.beem.project.btf.utils.UIHelper;
import com.teleca.jamendo.api.WSError;
import com.teleca.jamendo.api.util.Caller;

import de.greenrobot.event.EventBus;

/**
 * 实现fragment懒加载
 */
public class ImageGridFragment extends BaseFragment {
	private static final String TAG = "ImageGridFragment";
	private String DecadeType;
	private String activityType = ActivityType.Loadimg.toString();
	private GridView image_grid_view;
	private TextView empty_note;
	private List<TimeCameraImageInfo> imageurls;
	private TimeCameraImageAdapter timecameraimageadapter;
	private TimeCameraImageManageAdapter timecameraimagemanageadapter;
	private Context mContext;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private boolean mHasLoadedOnce;
	private View mFragmentView;

	public enum DecadeType {
		thirty("30"), sixty("60"), eighty("80"), eighty1("80"), eighty2("80");
		private String num;

		private DecadeType(String num) {
			this.num = num;
		}
		public String getNum() {
			return num;
		}
	}

	public enum ActivityType {
		Loadimg, Manage
	}

	public static ImageGridFragment newInstance(String str,
			String... activityType) {
		ImageGridFragment newFragment = new ImageGridFragment();
		Bundle bundle = new Bundle();
		bundle.putString("DecadeType", str);
		if (activityType.length > 0) {
			bundle.putString("ActivityType", activityType[0]);
		}
		newFragment.setArguments(bundle);
		return newFragment;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DecadeType = getArguments().getString("DecadeType");
		if (getArguments().containsKey("ActivityType")) {
			activityType = getArguments().getString("ActivityType");
		}
		EventBus.getDefault().register(this);
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mContext = getActivity();
		Log.i("ImageGridFragment", "onCreateView_DecadeType:" + DecadeType);
		if (mFragmentView == null) {
			mFragmentView = inflater.inflate(R.layout.image_grid_fragment,
					container, false);
			isPrepared = true;
			lazyLoad();
		}
		image_grid_view = (GridView) mFragmentView
				.findViewById(R.id.image_grid_view);
		empty_note = (TextView) mFragmentView.findViewById(R.id.empty_note);
		if (activityType.equals(ActivityType.Manage.toString())) {
			timecameraimagemanageadapter = new TimeCameraImageManageAdapter(
					imageurls, mContext);
			image_grid_view.setAdapter(timecameraimagemanageadapter);
		} else {
			timecameraimageadapter = new TimeCameraImageAdapter(imageurls,
					mContext);
			image_grid_view.setAdapter(timecameraimageadapter);
		}
		// 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
		ViewGroup parent = (ViewGroup) mFragmentView.getParent();
		if (parent != null) {
			parent.removeView(mFragmentView);
		}
		return mFragmentView;
	}
	public void onEventMainThread(final EventBusData data) {
		// 判断消息类型
		if (data.getAction() == EventAction.ImageDelete) {
			// fragment在多个activity复用所以需判断fragment在哪个acticity
			if (activityType.equals(ActivityType.Loadimg.toString())) {
				if (DecadeType.equals(data.getMsg())) {
					// 判断是哪个fragment
					new AsyncTask<Void, Integer, List<TimeCameraImageInfo>>() {
						@Override
						protected List<TimeCameraImageInfo> doInBackground(
								Void... params) {
							// TODO Auto-generated method stub
							imageurls = TimeCameraImageInfo
									.queryAll((String) data.getMsg());
							return imageurls;
						}
						@Override
						protected void onPostExecute(
								List<TimeCameraImageInfo> result) {
							timecameraimageadapter.setItem(result);
						};
					}.execute();
				}
			}
		}
	}
	// 数据加载
	@Override
	protected void lazyLoad() {
		// TODO Auto-generated method stub
		if (!isPrepared || !isVisible || mHasLoadedOnce) {
			return;
		}
		new AsyncTask<Void, Integer, List<TimeCameraImageInfo>>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 显示加载进度对话框
				UIHelper.showDialogForLoading(mContext, "任务进行中", false);
			}
			@Override
			protected List<TimeCameraImageInfo> doInBackground(Void... params) {
				try {
					// 在这里添加调用接口获取数据的代码
					imageurls = new ArrayList<TimeCameraImageInfo>();
					if (activityType.equals(ActivityType.Manage.toString())) {
						// 管理界面只查数据库
						imageurls = TimeCameraImageInfo
								.queryWebDownload(DecadeType);
					} else {
						// 下载界面需要请求服务端数据
						TimeCameraMaterialUtil.SaveToDB(
								getJsonData(DecadeType), false);
						imageurls = TimeCameraImageInfo.queryAll(DecadeType);
					}
					//Log.i(TAG, "~~DecadeType~~" + DecadeType + "~~imageurls~~" + imageurls);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return imageurls;
			}
			@Override
			protected void onPostExecute(List<TimeCameraImageInfo> imageurls) {
				if (imageurls != null && imageurls.size() > 0) {
					// 加载成功
					mHasLoadedOnce = true;
					empty_note.setVisibility(View.GONE);
					if (activityType.equals(ActivityType.Manage.toString())) {
						timecameraimagemanageadapter.setItem(imageurls);
					} else {
						timecameraimageadapter.setItem(imageurls);
					}
				} else {
					// 加载失败
					if (activityType.equals(ActivityType.Manage.toString())) {
						empty_note.setVisibility(View.VISIBLE);
						empty_note.setText("此年代还未下载素材，赶紧去下载哟");
					} else {
						empty_note.setVisibility(View.VISIBLE);
						empty_note.setText("暂无数据");
					}
				}
				UIHelper.hideDialogForLoading();
			}
		}.execute();
	}
	/**
	 * 网络请求
	 * @throws WSError
	 */
	private String getJsonData(String groupid) {
		String response = null;
		String url = AppProperty.getInstance().VVAPI + "/get_photo_materials";
		// Log.i(TAG, "~~请求地址url~~" + url);
		String[] names = new String[] { "tm_id", "group_id", "session_id" };
		Object[] values = new Object[] {
				LoginManager.getInstance().getJidParsed(), groupid,
				LoginManager.getInstance().getSessionId() };
		try {
			response = Caller.doGet(url, names, values);
			//Log.i(TAG, "~~json返回字符串response~~" + response);
		} catch (WSError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}
