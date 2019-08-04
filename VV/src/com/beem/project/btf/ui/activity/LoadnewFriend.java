package com.beem.project.btf.ui.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPException;

import android.app.Activity;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beem.project.btf.R;
import com.beem.project.btf.manager.LoginManager;
import com.beem.project.btf.ui.activity.base.VVBaseActivity;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg;
import com.beem.project.btf.ui.dialog.VVBaseLoadingDlg.VVBaseLoadingDlgCfg;
import com.beem.project.btf.ui.views.CustomTitleBtn;
import com.beem.project.btf.utils.BBSUtils;
import com.btf.push.Item;
import com.btf.push.Item.MsgType;
import com.butterfly.vv.adapter.CommonPhotoAdapter.DataStatus;
import com.butterfly.vv.adapter.localListViewAdapter;
import com.butterfly.vv.model.Start;
import com.butterfly.vv.service.ContactService;
import com.pullToRefresh.ui.PullToProcessStateListView;
import com.pullToRefresh.ui.PullToProcessStateListView.ProcessState;
import com.pullToRefresh.ui.PullToRefreshBase;
import com.pullToRefresh.ui.PullToRefreshBase.OnRefreshListener;
import com.pullToRefresh.ui.PullToRefreshBase.PullType;

public class LoadnewFriend extends VVBaseActivity {
	private ViewPager viewPager;// 页卡内容
	private ImageView imageView;// 动画图片
	private TextView city_txt, school_txt, phoneContact_txt,
			contacts_textView2;
	private List<View> views;
	private List<TextView> textViewlist;// Tab页面列表和标题
	private int offset = 0;// 动画图片偏移量
	private int currIndex = 0;// 当前页卡编号
	private Activity mActivity;
	private CustomTitleBtn back;
	private final MsgType[] msgTypes = new MsgType[] { MsgType.msg_phone,
			MsgType.msg_school };
	private Map<MsgType, localListViewAdapter> mapLv = new HashMap<MsgType, localListViewAdapter>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.load_new_friend);
		mActivity = this;
		InitImageView();
		InitViewPager();
		InitTextView();
		((PullToProcessStateListView) views.get(0)).doPullRefreshing(true, 500);
	}
	// 初始化viewpager
	private void InitViewPager() {
		viewPager = (ViewPager) findViewById(R.id.vPager);
		views = new ArrayList<View>();
		for (int i = 0; i < msgTypes.length; i++) {
			PullToProcessStateListView listView = (PullToProcessStateListView) getLayoutInflater()
					.inflate(R.layout.new_friend_lv, null);
			views.add(listView);
		}
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		// 设置每个页面listview的adapter
		setListviewAdapter(views);
	}
	/**
	 * 初始化头标
	 */
	private void InitTextView() {
		contacts_textView2 = (TextView) findViewById(R.id.topbar_title);
		contacts_textView2.setVisibility(View.GONE);
		back = (CustomTitleBtn) findViewById(R.id.leftbtn1);
		back.setTextAndImgRes("返回", R.drawable.bbs_back_selector)
				.setViewPaddingLeft().setVisibility(View.VISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View paramView) {
				finish();
			}
		});
		city_txt = (TextView) findViewById(R.id.local);
		school_txt = (TextView) findViewById(R.id.alumnus);
		phoneContact_txt = (TextView) findViewById(R.id.moblie_contacts);
		textViewlist = new ArrayList<TextView>();
		for (int i = 0; i < msgTypes.length; i++) {
			MsgType type = msgTypes[i];
			TextView view = null;
			if (type == MsgType.msg_city) {
				view = city_txt;
			} else if (type == MsgType.msg_phone) {
				view = phoneContact_txt;
			} else if (type == MsgType.msg_school) {
				view = school_txt;
			}
			if (view != null) {
				view.setVisibility(View.VISIBLE);
				view.setOnClickListener(new MyOnClickListener(i));
				textViewlist.add(view);
			}
		}
		setSelected(0);
	}
	/**
	 * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
	 */
	private void InitImageView() {
		imageView = (ImageView) findViewById(R.id.cursor);
		int screenW = BBSUtils.getScreenWH(mActivity)[0];
		offset = screenW / msgTypes.length;
		LayoutParams params = imageView.getLayoutParams();
		params.width = offset;
		imageView.setLayoutParams(params);
		Matrix matrix = new Matrix();
		matrix.postTranslate(0, 0);
		imageView.setImageMatrix(matrix);
	}

	/**
	 * 头标点击监听
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}
		@Override
		public void onClick(View v) {
			viewPager.setCurrentItem(index);
		}
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}
		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}
		@Override
		public int getCount() {
			return mListViews.size();
		}
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {
		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}
		@Override
		public void onPageSelected(final int arg0) {
			// 头标移动动画
			Animation animation = new TranslateAnimation(offset * currIndex,
					offset * arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			setSelected(arg0);
			localListViewAdapter adapter = mapLv.get(msgTypes[arg0]);
			PullToProcessStateListView pullListView = (PullToProcessStateListView) views
					.get(arg0);
			if (adapter.getStatus() == DataStatus.empty) {
				pullListView.doPullRefreshing(true, 500);
			}
		}
	}

	private void setSelected(int selectItems) {
		for (int i = 0; i < textViewlist.size(); i++) {
			if (i == selectItems) {
				textViewlist.get(i).setSelected(true);
				back.setText(textViewlist.get(i).getText());
			} else {
				textViewlist.get(i).setSelected(false);
			}
		}
	}
	private void setListviewAdapter(List<View> views) {
		for (int index = 0; index < msgTypes.length; index++) {
			PullToProcessStateListView listView = (PullToProcessStateListView) views
					.get(index);
			listView.setOnRefreshListener(refreshLis);
			listView.setPullRefreshEnabled(false);
			listView.setPullLoadEnabled(true);
			localListViewAdapter adapter = new localListViewAdapter(mActivity,
					listView.getRefreshableView());
			listView.getRefreshableView().setAdapter(adapter);
			listView.setListViewDivider(R.drawable.divider_line);
			listView.getRefreshableView().setFadingEdgeLength(0);
			mapLv.put(msgTypes[index], adapter);
			if (msgTypes[index] == MsgType.msg_phone) {
				listView.getEmptydataProcessView().setEmptydataImg(
						R.drawable.load_new_friend_phonecontact_empty);
				listView.getEmptydataProcessView().setloadEmptyBtn(null, null,
						false);
				listView.getEmptydataProcessView().setloadEmptyText(
						"无法获取到手机联系人");
			} else if (msgTypes[index] == MsgType.msg_school) {
				listView.getEmptydataProcessView().setEmptydataImg(
						R.drawable.load_new_friend_schoolmate_empty);
				listView.getEmptydataProcessView().setloadEmptyBtn("完善个人资料",
						new OnClickListener() {
							@Override
							public void onClick(View v) {
								ContactInfoActivity.launch(mContext,
										LoginManager.getInstance()
												.getMyContact());
							}
						});
				listView.getEmptydataProcessView().setloadEmptyText("学校资料未完善");
			}
		}
	}
	@Override
	public void registerVVBroadCastReceivers() {
	}

	/**
	 * @Fields refreshLis :刷新监听器
	 */
	private OnRefreshListener<ListView> refreshLis = new OnRefreshListener<ListView>() {
		@Override
		public void onPullDownToRefresh(
				final PullToRefreshBase<ListView> refreshView, PullType pullType) {
			int i = 0;
			for (View viewOne : views) {
				PullToProcessStateListView viewOne2 = (PullToProcessStateListView) viewOne;
				if (viewOne2.getPullToRefreshListView() == refreshView) {
					MsgType msgType = msgTypes[i];
					loadDataAsync(msgType, pullType);
					break;
				}
				i++;
			}
		}
		@Override
		public void onPullUpToRefresh(
				final PullToRefreshBase<ListView> refreshView, PullType pullType) {
			int i = 0;
			for (View viewOne : views) {
				PullToProcessStateListView viewOne2 = (PullToProcessStateListView) viewOne;
				if (viewOne2.getPullToRefreshListView() == refreshView) {
					MsgType msgType = msgTypes[i];
					loadDataAsync(msgType, pullType);
					break;
				}
				i++;
			}
		}
	};

	/**
	 * @Title: loadDataAsync
	 * @Description: 异步加载数据
	 * @param: @param msgType 消息类型
	 * @param: @param limit 数据数目
	 * @param: @param lis 结果监听器
	 * @return: void
	 * @throws:
	 */
	private void loadDataAsync(final MsgType msgType, final PullType pullType) {
		final localListViewAdapter adapter = mapLv.get(msgType);
		int index = 0;
		for (MsgType msgTypeOne : msgTypes) {
			if (msgTypeOne == msgType) {
				break;
			}
			index++;
		}
		final PullToProcessStateListView refreshView = (PullToProcessStateListView) views
				.get(index);
		new VVBaseLoadingDlg<List<Item>>(new VVBaseLoadingDlgCfg(this)) {
			@Override
			protected List<Item> doInBackground() {
				List<Item> retVal = null;
				try {
					Start startAdapter = adapter.getStart();
					Start start = new Start(startAdapter.getVal());
					if (msgType == MsgType.msg_school) {
						String schoolId = LoginManager.getInstance()
								.getMyContact().getSchoolId();
						retVal = ContactService.getInstance()
								.getSchoolMateList(schoolId, start, 10);
					} else if (msgType == MsgType.msg_phone) {
						String lastPhoneNum = null;
						if (adapter.getCount() > 0) {
							lastPhoneNum = adapter.getItem(
									adapter.getCount() - 1).getPhoneNum();
						}
						retVal = ContactService.getInstance()
								.getPhoneContactList(start, 100, lastPhoneNum);
					}
					//LogUtils.i("start.getVal:" + start.getVal() + " startAdapter.getVal:" + startAdapter.getVal()
					//							+ " pullType:" + pullType);
					if (pullType == PullType.PullUp) {
						startAdapter.setStart(start);
					} else if (pullType == PullType.PullDown) {
						// 下拉时第一次赋值start
						if (startAdapter.getVal() == null) {
							startAdapter.setStart(start);
						}
					}
				} catch (XMPPException e) {
					e.printStackTrace();
				}
				return retVal;
			}
			@Override
			protected void onTimeOut() {
				if (adapter.isEmpty()) {
					refreshView.setProcessState(ProcessState.TimeOut);
				} else {
					refreshView.setProcessState(ProcessState.Succeed);
				}
			}
			@Override
			protected void onPostExecute(List<Item> result) {
				super.onPostExecute(result);
				if (result == null || result.isEmpty()) {
					if (adapter.isEmpty()) {
						refreshView.setProcessState(ProcessState.Emptydata);
					} else {
						refreshView.setProcessState(ProcessState.Succeed, true);
					}
				} else {
					adapter.addItems(result);
					adapter.notifyDataSetChanged();
					adapter.setStatus(DataStatus.memory);
					refreshView.setProcessState(ProcessState.Succeed, adapter
							.getStart().isEnd());
				}
			}
		}.execute();
	}
}
