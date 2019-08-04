package com.beem.project.btf.ui.fragment;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.ui.adapter.BaseCameraImageAdapter;
import com.beem.project.btf.ui.entity.CommonCameraInfo;
import com.beem.project.btf.ui.entity.EventBusData;
import com.beem.project.btf.ui.entity.EventBusData.EventAction;
import com.beem.project.btf.utils.DimenUtils;
import com.beem.project.btf.utils.UIHelper;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;
import com.teleca.jamendo.api.WSError;

import de.greenrobot.event.EventBus;

/**
 * @ClassName: BaseImageGridFragment
 * @Description: 素材下载的基类
 * @author: yuedong bao
 * @date: 2015-11-13 下午5:49:53
 */
public abstract class BaseImageGridFragment<T extends CommonCameraInfo> extends
		BaseFragment {
	private ActivityType activityType = ActivityType.Loadimg;
	protected IMaterialType materialType;
	//private PullToRefreshGridView image_grid_view;
	private GridView image_grid_view;
	private TextView empty_note;
	protected List<T> imageurls;
	private BaseCameraImageAdapter<T> adpater;
	protected Context mContext;
	/** 标志位，标志已经初始化完成 */
	private boolean isPrepared;
	/** 是否已被加载过一次，第二次就不再去请求数据了 */
	private boolean mHasLoadedOnce;
	private View mFragmentView;

	public enum ActivityType {
		Loadimg, Manage
	}

	/**
	 * @ClassName: IMaterialType
	 * @Description: 时光素材类型DecadeType
	 * @author: yuedong bao
	 * @date: 2015-11-17 上午11:32:50
	 */
	public interface IMaterialType extends Serializable {
		//获取模块
		public String getGroupId();
		//获取标题
		public String getTitle();
		//获取Fragment
		public Fragment getFragment(ActivityType actType);
		//获取列数
		public int getColumn();
		//获取索引
		public int getIndex();
		//获取所有成员
		public IMaterialType[] getAllMembers();
		//索取素材下载失败时提示语
		public String getLoadFailedMessage(ActivityType actType);
		//根据groupid获得素材类型
		public IMaterialType getMaterialType(String groupId);
		//获取下载显示界面显示的列数
		public int getNumColumns();;
	}

	//查询素材管理界面数据
	public abstract List<T> queryManageMaterialDB(String model);
	//查询素材展示界面的数据
	public abstract List<T> queryShowMaterialDB(String model);
	//创建素材显示的adapter
	public abstract BaseCameraImageAdapter<T> createAdapter(ActivityType type);
	//请求服务器数据并保存到数据库
	public abstract void requestNetAndSaveDB(String groupid) throws WSError;
	//下拉刷新获取数据
	public abstract List<T> onPullUpToRefresh();
	//下拉刷新获取数据
	public abstract List<T> onPullDownToRefresh();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activityType = (ActivityType) getArguments().getSerializable(
				"ActivityType");
		materialType = (IMaterialType) getArguments().getSerializable(
				"MaterialType");
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
		if (mFragmentView == null) {
			mFragmentView = inflater.inflate(R.layout.image_grid_fragment2,
					container, false);
			isPrepared = true;
			lazyLoad();
		}
		/** 暂时使用原生gridview,使用PullToRefreshGridView会出现item显示不完全的bug */
		image_grid_view = (GridView) mFragmentView
				.findViewById(R.id.image_grid_view);
		//image_grid_view.getRefreshableView().setNumColumns(materialType.getNumColumns());
		//image_grid_view.getRefreshableView().setVerticalSpacing(DimenUtils.dip2px(mContext, 10));
		//image_grid_view.getRefreshableView().setHorizontalSpacing(DimenUtils.dip2px(mContext, 10));
		image_grid_view.setNumColumns(materialType.getNumColumns());
		image_grid_view.setVerticalSpacing(DimenUtils.dip2px(mContext, 10));
		image_grid_view.setHorizontalSpacing(DimenUtils.dip2px(mContext, 10));
		empty_note = (TextView) mFragmentView.findViewById(R.id.empty_note);
		adpater = createAdapter(activityType);
		image_grid_view.setAdapter(adpater);
		//image_grid_view.getRefreshableView().setAdapter(adpater);
		//image_grid_view.setOnRefreshListener(refreshListener);
		//image_grid_view.setPullRefreshEnabled(false);
		//image_grid_view.setPullLoadEnabled(false);
		// 因为共用一个Fragment视图，所以当前这个视图已被加载到Activity中，必须先清除后再加入Activity
		ViewGroup parent = (ViewGroup) mFragmentView.getParent();
		if (parent != null) {
			parent.removeView(mFragmentView);
		}
		return mFragmentView;
	}
	/*protected GridView getGridView() {
		return image_grid_view.getRefreshableView();
	}*/
	protected IMaterialType getMaterialType() {
		return materialType;
	}

	private OnRefreshListener<GridView> refreshListener = new OnRefreshListener<GridView>() {
		@Override
		public void onPullDownToRefresh(
				PullToRefreshBase<GridView> refreshView, PullType pullType) {
			refreshView.onPullRefreshComplete();
		}
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView,
				PullType pullType) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			List<T> rst = BaseImageGridFragment.this.onPullUpToRefresh();
			if (rst != null && rst.size() > 0) {
				imageurls.addAll(rst);
				adpater.notifyDataSetChanged();
			}
			refreshView.onPullRefreshComplete();
		}
	};

	public void onEventMainThread(final EventBusData data) {
		// 判断消息类型
		if (data.getAction() == EventAction.ImageDelete) {
			// fragment在多个activity复用所以需判断fragment在哪个acticity
			if (activityType == ActivityType.Loadimg) {
				if (materialType.getGroupId().equals(data.getMsg())) {
					// 判断是哪个fragment
					new AsyncTask<Void, Integer, List<T>>() {
						@Override
						protected List<T> doInBackground(Void... params) {
							// TODO Auto-generated method stub
							imageurls = queryShowMaterialDB((String) data
									.getMsg());
							return imageurls;
						}
						@Override
						protected void onPostExecute(List<T> result) {
							adpater.setItem(result);
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
		//LogUtils.i("call lazyLoad", 100);
		new AsyncTask<Void, Integer, List<T>>() {
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				// 显示加载进度对话框
				UIHelper.showDialogForLoading(mContext, "任务进行中", false);
			}
			@Override
			protected List<T> doInBackground(Void... params) {
				try {
					// 在这里添加调用接口获取数据的代码
					imageurls = new ArrayList<T>();
					if (activityType == ActivityType.Manage) {
						// 管理界面只查数据库
						imageurls = queryManageMaterialDB(materialType
								.getGroupId());
					} else {
						// 下载界面需要请求服务端数据
						requestNetAndSaveDB(materialType.getGroupId());
						imageurls = queryShowMaterialDB(materialType
								.getGroupId());
					}
				} catch (Exception e) {
					e.printStackTrace();
				} catch (WSError e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return imageurls;
			}
			@Override
			protected void onPostExecute(List<T> imageurls) {
				if (imageurls != null && imageurls.size() > 0) {
					// 加载成功
					mHasLoadedOnce = true;
					empty_note.setVisibility(View.GONE);
					adpater.setItem(imageurls);
				} else {
					// 加载失败
					empty_note.setVisibility(View.VISIBLE);
					empty_note.setText(materialType
							.getLoadFailedMessage(activityType));
				}
				UIHelper.hideDialogForLoading();
			}
		}.execute();
	}
}
