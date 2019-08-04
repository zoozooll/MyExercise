package com.mogoo.market.ui;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mogoo.market.R;
import com.mogoo.market.adapter.LocalApkAdapter;
import com.mogoo.market.adapter.RecommendApkAdapter;
import com.mogoo.market.database.ApkListDaoImpl;
import com.mogoo.market.database.ApkListSQLTable;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.dao.IBeanDao;
import com.mogoo.market.manager.InstallousScanApk;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.model.InstallousAppInfo;
import com.mogoo.market.model.SdcardInstalledAppInfo;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.InstallPaginateListView;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.parser.XmlResultCallback;

public class InstallousActivity extends BaseListActivity {
	public final static int HANDLER_SCAN_SDCARD = 0x000100;
	private final static String TAG_MORE_APP_TEXT = "MORE_APP_TEXT_TAG";
	private final static String TAG_EMPTY_LOCAL_APK = "MORE_EMPTY_LOCAL_APK_TAG";
	
	public static final int SORT_METHOD_LOCAL = 0;
	public static final int SORT_METHOD_RECOMMEND = 1;
	
	private ListView mLocalInstallList;
	private InstallPaginateListView<HotApp> mRecommendListView;
	private LocalApkAdapter<InstallousAppInfo> mLocalInstallAdapter; 
	private RecommendApkAdapter<HotApp> mRecommendAdapter;
	private InstallousScanApk mSanSdCard;
	private String sdcardPath;
	private Button mReScanBtn;
	private Button mLocalApkBtn;
	private Button mRecommendApkBtn;
	private Context mContext;
	private TextView mMoreAppText;
	private CommonEmptyView mRecommendEmptyView;
	private CommonEmptyView mLocalEmptyView;
	private View mMoreAppFootView;
	private List<InstallousAppInfo> mLocalApkData = new ArrayList<InstallousAppInfo>();
	
	private String mRecommandUrl = HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_NECESSARY_APP_LIST);
	private MyOnclickListener mClickListener = new MyOnclickListener();

	private boolean isScanSdcard = false;
	private int mSortMethod = SORT_METHOD_LOCAL;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		setBaseTopLayout(R.layout.install_local_top);
		setBaseHeaderLayout(R.layout.install_local_header);
		getBaseHeaderLayout().setVisibility(View.VISIBLE);
		
		ApkListDaoImpl.getInstance(mContext, ApkListSQLTable.TABLE_INSTALLOUS).clearAllBean();
		
		findViewsAndSetListener();

		initSdCard();
		
		initLocalAPkListView();
		
		initRecommendApkListView();
	}
	
	@Override
	protected void onResume() {
		if(mLocalInstallAdapter != null) {
			mLocalInstallAdapter.notifyDataSetChanged();
		}
		if(mRecommendAdapter != null) {
			mRecommendAdapter.notifyDataSetChanged();
		}
		super.onResume();
	}
	
	@Override
	protected void onStart(){
		super.onStart();
	}

	@Override
	protected void onStop(){
		super.onStop();
		mSanSdCard.setStartScan(false);
	}
	
	@Override
	protected void onRestart(){
		super.onRestart();
		if(!AppUtils.isSdcardExist()){	
			sendMessageScanSdcard();
		}else{
//			MyToast.makeText(mContext,
//    				getResources().getText(R.string.scan_sdcard_tip),
//    				2).show();
			//mLocalInstallAdapter.clear();
			mSanSdCard.setStartScan(true);
			//scanSdcard();
		}

	}
	
	/**
	 * init the thread of scan apk
	 */
	private void startScanAPK() {
		mLocalApkData.clear();
		mSanSdCard.setStartScan(true);
		scanSdcard();
	}

	/**
	 * 
	 */
	private void initSdCard() {
		sdcardPath = AppUtils.getSDKpath();
		mSanSdCard = new InstallousScanApk(this);
		if (AppUtils.isSdcardExist()) {
			MyToast.makeText(InstallousActivity.this,
					getResources().getText(R.string.scan_sdcard_tip), 2).show();
		}
		startScanAPK();
	}

	private void findViewsAndSetListener() {
		mReScanBtn = (Button)findViewById(R.id.install_scan_sdcard);
		mLocalApkBtn = (Button)findViewById(R.id.install_local_btn);
		mRecommendApkBtn = (Button)findViewById(R.id.install_recommend_btn);
		mReScanBtn.setOnClickListener(mClickListener);
		mLocalApkBtn.setOnClickListener(mClickListener);
		mRecommendApkBtn.setOnClickListener(mClickListener);
	}
	
	/**
	 * int the lcoal apk listview and adapter
	 */
	private void initLocalAPkListView() 
	{
		mLocalInstallList = new ListView(this);
		mLocalInstallList.setDivider(this.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		mLocalInstallList.setCacheColorHint(Color.TRANSPARENT);
		mLocalInstallList.setVerticalScrollBarEnabled(false);
		mLocalInstallAdapter = new LocalApkAdapter<InstallousAppInfo>(this, -1, mLocalApkData);
		mLocalInstallList.setAdapter(mLocalInstallAdapter);
		setBaseCenterLayout(mLocalInstallList);
		getBaseCenterLayout().setBackgroundResource(R.color.install_bg);
		mLocalEmptyView = new CommonEmptyView(this, mLocalInstallList);
		mLocalEmptyView.setEmptyText(getResources().getString(R.string.no_sd_apk));
		addEmptyView(mLocalEmptyView, mLocalInstallList);
	}

	/**
	 * init the recommend apk and listview
	 */
	private void initRecommendApkListView() {
		IBeanDao<HotApp> necessaryDao = DaoFactory.getInstallousDao(this);
		mRecommendAdapter = new RecommendApkAdapter<HotApp>(this,
				necessaryDao.getAllBean());
		mRecommendListView = new InstallPaginateListView<HotApp>(
				this,
				mRecommendAdapter,
				necessaryDao,
				HttpUrls.createBaseUrlWithPairs(HttpUrls.URL_NECESSARY_APP_LIST));
		initPaginateListView(mRecommendListView, mRecommendAdapter,
				new HotApp.HotAppListCallback());
		mRecommendEmptyView =  new CommonEmptyView(mContext, mRecommendListView);
		//addEmptyView(mRecommendEmptyView, mRecommendListView);
	}
	
	/**
	 * init the recommend apk request
	 * @param listview the listview to be init
	 * @param adapter the listview's adapter
	 * @param xmlResultCallback when the net request is over, call this callback method
	 */
	private void initPaginateListView(InstallPaginateListView listview,
			BaseAdapter adapter, XmlResultCallback xmlResultCallback) {
		listview.setCacheColorHint(Color.TRANSPARENT);
		listview.setVerticalScrollBarEnabled(false);
		listview.setXmlResultCallback(xmlResultCallback);
		ListFooterView footerView = new ListFooterView(this, adapter);
		footerView.setShowNodataText(false);
		footerView.setFocusable(false);
		footerView.setFocusableInTouchMode(false);
		listview.addFooterView(footerView);
		
		adapter.registerDataSetObserver(observer);
		mMoreAppFootView = getLayoutInflater().inflate(R.layout.install_recommend_bottom_layout, null);
		mMoreAppFootView.setTag(TAG_MORE_APP_TEXT);
		mMoreAppFootView.setOnClickListener(mClickListener);
		mMoreAppFootView.setVisibility(View.GONE);
		listview.addFooterView(mMoreAppFootView);
		
		
		listview.setAdapter(adapter);
		if (adapter.getCount() == 0) {
			listview.doFirsetQuery(mRecommandUrl);
		}
	}
	
	private Handler mhandler = new Handler() 
	{
		public void handleMessage(Message msg) 
        {
			switch (msg.what) {
			case HANDLER_SCAN_SDCARD:
				removeMessages(HANDLER_SCAN_SDCARD);
				mLocalApkData = mSanSdCard.getMyFiles();
				for(InstallousAppInfo apkInfo : mLocalApkData){
					mLocalInstallAdapter.add(apkInfo);
				}
				if (mSanSdCard.getMyFiles().isEmpty()) {
					mLocalInstallAdapter.notifyDataSetInvalidated();
				} else {
					mLocalInstallAdapter.notifyDataSetChanged();
				}
				break;
			}
        }
    };
    
    private DataSetObserver observer = new DataSetObserver() {
		@Override
		public void onChanged() {
			// mMoreAppFootView.setVisibility(View.GONE);
		}

		@Override
		public void onInvalidated() {
			mMoreAppFootView.setVisibility(View.VISIBLE);
		}
	};
    
    private void addEmptyView(CommonEmptyView emptyView, ListView listview) 
    {
		View empty_text = emptyView.findViewById(R.id.empty_text);
		empty_text.setTag(TAG_EMPTY_LOCAL_APK);
		empty_text.setOnClickListener(mClickListener);

		emptyView.setBackgroundDrawable(null);
		ViewGroup layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
    
    private void addEmptyView(CommonEmptyView emptyView, InstallPaginateListView listview) {
		ViewGroup layout = (ViewGroup) emptyView.getParent();
		if(layout != null) {
			layout.removeView(emptyView);
		}
		emptyView.setBackgroundDrawable(null);
		layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
    private void sendMessageScanSdcard(){
    	isScanSdcard = false;
    	Message msg = Message.obtain();
		msg.what = HANDLER_SCAN_SDCARD;
		mhandler.sendMessage(msg);
    }
	
	private void scanSdcard(){
		isScanSdcard = true;
		new Thread(){
			@Override
			public void run(){
				mSanSdCard.getMyFiles().clear();
				mSanSdCard.FindAllAPKFile(new File(sdcardPath));
//				try {
//					sleep(5 * 1000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				sendMessageScanSdcard();
			}}.start();
	}	
	
	public class MyOnclickListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			if (v.getId() == R.id.install_scan_sdcard) {
				if(isScanSdcard)
					return;
				if (!AppUtils.isSDCardReady()) {
					String text = getResources().getString(
							R.string.no_sdcard_tip);
					MyToast.makeText(InstallousActivity.this, text,
							Toast.LENGTH_SHORT).show();
				} else {
					MyToast.makeText(InstallousActivity.this,
							getResources().getText(R.string.scan_sdcard_tip), 2)
							.show();
				}
				mLocalInstallAdapter.clear();
				scanSdcard();
			} else if (v.getId() == R.id.install_local_btn) {
				if(mSortMethod == SORT_METHOD_LOCAL) {
					return;
				}
				mSortMethod = SORT_METHOD_LOCAL;
				setHeader(0);
				setBaseCenterLayout(mLocalInstallList);
				addEmptyView(mLocalEmptyView, mLocalInstallList);
				mLocalInstallAdapter.notifyDataSetChanged();
			} else if (v.getId() == R.id.install_recommend_btn) {
				if(mSortMethod == SORT_METHOD_RECOMMEND) {
					return;
				}
				mSortMethod = SORT_METHOD_RECOMMEND;
				setHeader(1);
				setBaseCenterLayout(mRecommendListView);
				addEmptyView(mRecommendEmptyView, mRecommendListView);
				if(mRecommendAdapter != null){
					mRecommendAdapter.notifyDataSetChanged();
				}
			} else if(v.getTag().equals(TAG_MORE_APP_TEXT) || v.getTag().equals(TAG_EMPTY_LOCAL_APK)) {
				Intent intent = new Intent();
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
				intent.setComponent(new ComponentName("com.mogoo.market", "com.mogoo.market.ui.MarketGroupActivity"));
				intent.setAction("com.mogoo.market.action.RECOMMEND");
				mContext.startActivity(intent);
			}
		}
	}
	
	/**
	 * 设置选项卡背景颜色
	 * @param number
	 */
	private void setHeader(int number){
		if(number == 0){
			mReScanBtn.setVisibility(View.VISIBLE);
			mLocalApkBtn.setBackgroundResource(R.drawable.install_local_left_btn_check);
			mRecommendApkBtn.setBackgroundResource(R.drawable.install_local_right_btn_uncheck);
		}else if(number == 1){
			mReScanBtn.setVisibility(View.GONE);
			mLocalApkBtn.setBackgroundResource(R.drawable.install_local_left_btn_uncheck);
			mRecommendApkBtn.setBackgroundResource(R.drawable.install_local_right_btn_check);
		}
	}
	
}
