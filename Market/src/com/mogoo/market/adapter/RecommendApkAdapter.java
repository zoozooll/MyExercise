package com.mogoo.market.adapter;

import java.util.HashMap;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ResourceCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.mogoo.components.download.DownloadProgressListener;
import com.mogoo.components.download.DownloadProgressManager;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.MarketApplication.DownloadChangeCallback;
import com.mogoo.market.MarketApplication.PackagesChangeCallback;
import com.mogoo.market.R;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.manager.MogooLocalSoftManager;
import com.mogoo.market.model.AppInfo;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.HotApp;
import com.mogoo.market.ui.AppDetailActivity;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;

public class RecommendApkAdapter<T> extends ResourceCursorAdapter  {

	private static final String TAG = "HotAdapter";
	private static final int NOTIFY_REFRESH = 1;
	private int mLayoutID = R.layout.app_item;
	private final int MAX_PROGRESS = 100;

	private LayoutInflater mInflater;
	private Context mContext;

	private String apkid;
	private String InstallPath = null;
	private String SavaPath = "MogooLoad/";
	private String fileName = null;
	ImageDownloader mImageManager;
	private MogooLocalSoftManager mSoftMgr;
	private HashMap<String,Integer> mLocalSoftVersion = new HashMap<String, Integer>();
	private MogooDownloadManager mogooDownloadManager;
	private DownloadCompleteListener mReceiverDownLoadComplete;
	/** 所有下载管理中所有已下载的任务，key是apkid，value是downloadid*/
	private HashMap<String, String> mAllDownloadIds;
	private HashMap<String, Long> mURI2DownIDMapping = new HashMap<String, Long>();
	/** 下载应用的downloadId和应用名字 */
	private HashMap<Long, String> downloadMap = new HashMap<Long, String>();

    private DownPrefsUtil mPrefsUtil = null;
    private PackagesChangeCallback mPackChangeListener;
    
	public RecommendApkAdapter(Context context, Cursor cursor) {
		super(context, R.layout.install_app_item, cursor);

		InstallPath = AppUtils.getSDKpath() + "/" + SavaPath;

		mSoftMgr = MarketApplication.getmManager();
		mLocalSoftVersion = MarketApplication.getInstance()
				.getInstalledAppPackages();
		this.mContext = context;
		mInflater = LayoutInflater.from(mContext);
		mImageManager = ImageDownloader.getInstance(mContext);
		mogooDownloadManager = new MogooDownloadManager(mContext,
				mContext.getContentResolver(), mContext.getPackageName());

		mAllDownloadIds = getAllDownloadId();
		registerDownloadCompleteListener();
		registerPackageChangeListener();
		mPrefsUtil = DownPrefsUtil.getInstance(mContext);
	}

	@Override
	public void bindView(View convertView, Context context, Cursor cursor) {
		ViewHolder viewHolder;
		final HotApp appInfo = HotApp.getHotApp(cursor);
		viewHolder = (ViewHolder) convertView.getTag();
		if (viewHolder == null) {
			viewHolder = new ViewHolder(convertView);
			convertView.setTag(viewHolder);
		}
		
		final ViewHolder tempViewHolder = viewHolder;
		mHandler.post(new Runnable() {
			
			@Override
			public void run() {
				mImageManager.download(appInfo.getIconUrl(), tempViewHolder.app_iv,
						BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defautl_list_itme_pic_loading));
			}
		});

		//viewHolder.app_progress_bar_parent.setVisibility(View.GONE);
		viewHolder.app_down_btn.setVisibility(View.VISIBLE);
		
		if (mAllDownloadIds != null
				&& mAllDownloadIds.containsKey(appInfo.getApkId())) {// 有下载
			final String downloadId = mAllDownloadIds.get(appInfo.getApkId());
			String value = mPrefsUtil.getPrefsValue(downloadId, "");
			String[] result = mPrefsUtil.parseResponse(value);
			if (!TextUtils.isEmpty(value)) {// 如果正在下载，下载成功为200
				//viewHolder.app_progress_bar_parent.setVisibility(View.VISIBLE);
				//viewHolder.app_down_btn.setVisibility(View.GONE);
				//viewHolder.app_progress_bg
				//	.setImageResource(R.drawable.app_item_progress_left_blue);
				setDownButton(false, R.string.install_app_downing, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_green_btn_xml);
			} else if (isUpdatable(appInfo.getPackageName())) {// 如果有更新
				setDownButton(true, R.string.app_update, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			} else if (mLocalSoftVersion.containsKey(appInfo.getPackageName())) {// 是不是已安装
				setDownButton(false, R.string.app_item_installed, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_green_btn_xml);
			} else if (ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))
					   &&ToolsUtils.checkApk(mContext,AppUtils.getSDKpath()
					   + "/MogooLoad/"+getApkName(appInfo), appInfo.getApkId())) {//如果已下载
				 setDownButton(true, R.string.install, viewHolder);
				 viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			} else{//直接显示免费或者价格
				setDownButton(true, R.string.app_item_down, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			}
		} else{
//			viewHolder.tag = null;
			if (isUpdatable(appInfo.getPackageName())) {// 如果有更新
				setDownButton(true, R.string.app_update, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			} else if (mLocalSoftVersion.containsKey(appInfo.getPackageName())) {// 如果已安装
				setDownButton(false, R.string.app_item_installed, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_green_btn_xml);
			} else if (ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))
					  &&ToolsUtils.checkApk(mContext,AppUtils.getSDKpath()
					  + "/MogooLoad/"+getApkName(appInfo), appInfo.getApkId())) {//如果已下载
				setDownButton(true, R.string.install, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			} else {
				setDownButton(true, R.string.app_item_down, viewHolder);
				viewHolder.app_down_btn.setBackgroundResource(R.drawable.install_white_btn_xml);
			}
		}
		//viewHolder.app_rating.setRating(Float.parseFloat(appInfo.getrScore()));
		viewHolder.app_name.setText(appInfo.getName());
		viewHolder.app_name.setSelected(true);
		viewHolder.app_version.setText(mContext.getString(
				R.string.app_item_version, appInfo.getVersionStr()));
		String sizeStr = getResources().getString(R.string.app_size) + ToolsUtils.getSizeStr(appInfo.getApkSize());
		viewHolder.app_size.setText(sizeStr);

		viewHolder.app_item_lay.setOnClickListener(new DownClickListener(
				viewHolder, appInfo));
		viewHolder.app_down_btn.setOnClickListener(new DownClickListener(
				viewHolder, appInfo));
//		viewHolder.app_downloading.setOnClickListener(new DownClickListener(
//				viewHolder, appInfo));
	}

	/**
	 * 设置下载监听器，监听下载进度
	 * 
	 * @param hotHolder
	 *            单个item
	 * @param downloadId
	 *            下载id
	 */
	private void registerDownloadListener(final ViewHolder hotHolder,
			final long downloadId) {
//		hotHolder.app_progress_bg
//			.setImageResource(R.drawable.app_item_progress_left_gray);
		DownloadProgressListener downListener = new DownloadProgressListener() {
			@Override
			public void onDownloadProgressChanged(int progress) {
				
				if (hotHolder.tag == this) {
					if (progress < 0 || progress >= MAX_PROGRESS) {//下载失败或者下载成功
						if (progress < 0) {
//							hotHolder.app_progress_bar_parent.setVisibility(View.GONE);
//							hotHolder.app_down_btn.setVisibility(View.VISIBLE);
//							hotHolder.app_down_btn.setText(R.string.app_item_down);
						} else {
//							hotHolder.app_progress_bar_parent.setVisibility(View.GONE);
//							hotHolder.app_down_btn.setVisibility(View.VISIBLE);
//							hotHolder.app_down_btn.setText(R.string.install);
						}
					} else if (progress > 0) {
//							hotHolder.app_progress_bar_parent.setVisibility(View.VISIBLE);
//							hotHolder.app_down_btn.setVisibility(View.GONE);
//							hotHolder.app_progress_bg
//							.setImageResource(R.drawable.app_item_progress_left_blue);
					}
				}
			}

			@Override
			public long getDownloadId() {
				return downloadId;
			}
		};

		DownloadProgressManager.getInstance(mContext).registerProgressListener(
				downListener);
		hotHolder.tag = downListener;
	}

	/**
	 * 设置下载按钮的状态
	 */
	private void setDownButton(boolean isBtnVisible, boolean enable,
			int textId, ViewHolder hotHolder ) {
		if(isBtnVisible){
			hotHolder.app_down_btn.setVisibility(View.VISIBLE);
		} else {
			hotHolder.app_down_btn.setVisibility(View.GONE);
		}
		hotHolder.app_down_btn.setEnabled(enable);
		hotHolder.app_down_btn.setText(textId);
	}
	
	private void setDownButton(boolean enable, int textId, ViewHolder hotHolder ) {
		hotHolder.app_down_btn.setEnabled(enable);
		hotHolder.app_down_btn.setText(textId);
	}

	/**
	 * 下载按钮和整个item的点击事件
	 * 
	 * @author lcq-motone
	 * 
	 */
	class DownClickListener implements View.OnClickListener {
		ViewHolder holder = null;
		HotApp hotApp;

		public DownClickListener(ViewHolder holder, HotApp hotApp) {
			super();
			this.holder = holder;
			this.hotApp = hotApp;
		}

		public void onClick(View v) {
			if (v.getId() == R.id.app_down_btn) {
				final long downId = startDownApp(hotApp);
				if (downId != -1) {
					mAllDownloadIds.put(hotApp.getApkId(), "" + downId); // 点击下载，更新下载集合
					setDownButton(false, R.string.install_app_downing, holder);
					holder.app_down_btn.setBackgroundResource(R.drawable.install_green_btn_xml);
					//holder.app_progress_bar_parent.setVisibility(View.VISIBLE);
					//本地保存downloadId，apkId，和状态（是否正在下载）
					mPrefsUtil.savePresValue(String.valueOf(downId), String.valueOf(hotApp.getApkId()), String.valueOf(true));
					//holder.app_progress_bar_parent.setVisibility(View.VISIBLE);
					//holder.app_down_btn.setVisibility(View.GONE);
					//holder.app_progress_bg
					//	.setImageResource(R.drawable.app_item_progress_left_blue);
				}

			} else if (v.getId() == R.id.app_item_lay) {
				
				//进入应用简介界面
				int downloadId = 0;
				if (mAllDownloadIds.containsKey(hotApp.getApkId())) 
				{
					downloadId = Integer.valueOf(mAllDownloadIds.get(hotApp.getApkId()));
				}
//				AppDetailActivity.launch(mContext, hotApp.getApkId(), hotApp.getPackageName(), hotApp.getVersionCode(), downloadId);
				startAppDetailActivity(downloadId);
			}
		}

		/**
		 * @param downloadId 下载id
		 */
		private void startAppDetailActivity(int downloadId) {
			Intent intent = new Intent();
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.setClass(mContext, AppDetailActivity.class);
			Bundle bundle = new Bundle();
			bundle.putString(AppDetailActivity.EXTRA_APP_ID,hotApp.getApkId()); 
			bundle.putString(AppDetailActivity.EXTRA_APP_PACKAGENAME,hotApp.getPackageName());
			bundle.putInt(AppDetailActivity.EXTRA_APP_VERSIONCODE_INT,hotApp.getVersionCode());
			bundle.putInt(AppDetailActivity.EXTRA_APP_DOWNLOADID_INT,downloadId);
			bundle.putString(AppDetailActivity.EXTRA_SOURCE_COMPONENT_NAME, mContext.getClass().getName());
			intent.putExtras(bundle);
			mContext.startActivity(intent);
		}

	}

	private boolean isUpdatable(String pkgName, int versionCode) {
		boolean updatable = false;
		if(mLocalSoftVersion.containsKey(pkgName)) {
			int localVersion = mLocalSoftVersion.get(pkgName);
			if(versionCode > localVersion)
				updatable = true;
		}
		return updatable;
	}

	/**
	 * 从服务端更新接口获取到是否有更新
	 * @param pkgName
	 * @return 是否有更新
	 */
	private boolean isUpdatable(String pkgName) {
		return MarketApplication.isHaveUpdate(pkgName);
	}
	
	public long startDownApp(HotApp appInfo) {
		fileName = appInfo.getName() + "_" + appInfo.getVersionCode() + ".apk";
		fileName.replace(" ", "");
		return onDownApp(appInfo);
	}

	private String getApkName(HotApp appInfo) {
		String fileName = appInfo.getName() + "_" + appInfo.getVersionCode()
				+ ".apk";
		fileName.replace(" ", "");
		return fileName;
	}

	/**
	 * 
	 * @param appInfo
	 * @return -1 if an error occurs,otherwise the downloadId
	 */
	private long onDownApp(HotApp appInfo) {
		long result = -1L;// 已下载返回－1
		if (AppUtils.getSDKpath() != null && !AppUtils.getSDKpath().equals("")) {
			// 判断是否下载
			if (isDownApk(InstallPath, fileName, appInfo.getApkId())) {
				InstallApk(InstallPath + fileName);// 如果已经下载，就安装
			} else {
				long fileSize = (long) appInfo.getApkSize();
				if (fileSize > ToolsUtils.getSdcardAvailableSize()) {
					MyToast.makeText(mContext,
							getResources().getText(R.string.sdcard_full),
							Toast.LENGTH_SHORT).show();
					return result;
				}
				// 网络是否可用
				if (ToolsUtils.isAvailableNetwork(mContext)) {
					result = down(appInfo);
				} else {
					MyToast.makeText(
							mContext,
							getResources().getText(
									R.string.tip_network_inactive),
							Toast.LENGTH_SHORT).show();
				}
			}
		} else {
			MyToast.makeText(mContext,getResources()
					.getText(R.string.install_no_sdcard),Toast.LENGTH_SHORT).show();
		}
		return result;
	}

	private long down(HotApp appInfo) 
	{
		String apkUrl = appInfo.getApkAddress();
		long tempDownLoadId = ToolsUtils.downloadApk(mogooDownloadManager, SavaPath, getApkName(appInfo),apkUrl);
		boolean insertFlat=true;
		downloadMap.put(tempDownLoadId, getApkName(appInfo));
		mURI2DownIDMapping.put(appInfo.getApkAddress(), tempDownLoadId);
                
		for(DownloadInfo downloadInfo : MarketApplication.myDownloadAppList){
			if(downloadInfo.getApp_id().equals(appInfo.getApkId())){
				insertFlat=false;
				break;
			}else{
				insertFlat=true;
			}
				
		}
		//插入下载数据信息到数据库
		if(insertFlat){
		      DaoFactory.getDownloadInfoDao(mContext).addBean(new DownloadInfo(String.valueOf(tempDownLoadId), appInfo.getApkId(), 
				 apkUrl, SavaPath, appInfo.getName(), ""+appInfo.getApkSize(), appInfo.getIconUrl(), appInfo.getvScore(),
				 appInfo.getVersionStr(), Integer.valueOf(appInfo.getVersionCode()), appInfo.getPackageName()));
		}		
				
		//数据库变化，重新查询所有已下载的id集合
		mAllDownloadIds = getAllDownloadId();
		
		//如果是更新，则保存包名和下载id到UpdatesUtils
		if(MarketApplication.installedAppPackages.containsKey(appInfo.getPackageName())) 
		{
			UpdatesUtils.save(mContext, appInfo.getPackageName(), tempDownLoadId + "");
		}
		return tempDownLoadId;
	}

	/**
	 * 下载
	 * */
	public void downLoad(AppInfo appInfo) {
		MogooDownloadManager downLoad = new MogooDownloadManager(mContext,
				mContext.getContentResolver(), null);
		// String url = DownLoad.getUrl(appInfo.getApkId(), null);
		// Log.e("test down url:", url);
		long downloadId = -1;
		// 创建下载目录(在SDCARD中)
		Environment.getExternalStoragePublicDirectory(SavaPath).mkdirs();
		// 保存到SDCARD的name目录中，并把保存的文件名设为rrr.mp3
		downLoad.setDestinationInExternalPublicDir(SavaPath, fileName);
		// mogooDownloadManager.setmPackageName(appDetails.getPackageName());
		downLoad.setTitle(fileName);
		// 开始下载
		// downloadId = downLoad.startDownload(url);
		downloadMap.put(downloadId, fileName);
		
		//如果是更新，则保存包名和下载id到UpdatesUtils
		if(mLocalSoftVersion.containsKey(appInfo.getPackageName())) 
		{
			UpdatesUtils.save(mContext, appInfo.getPackageName(), downloadId + "");
		}
	}

	/**
	 * 是否已经下载
	 * */
	public boolean isDownApk(String path, String apkName, String apkId) {
		if (ToolsUtils.isDownAPk(mContext, path, apkName)) {
			return ToolsUtils.checkApk(mContext, path + apkName, apkId);
		} else {
			return false;
		}
	}

	/**
	 * 安转APK
	 * */
	public void InstallApk(String path) {
		ToolsUtils.onInstallApk(mContext, path);
	}

	private class DownloadCompleteListener implements DownloadChangeCallback {

		@Override
		public void downloadChanged(long id, int downloadStatus) {
			if (downloadMap.containsKey(id) && downloadStatus != 200) {//如果反馈的id包含在此map中，且未安装成功，提示用户
				if(downloadStatus>=400)   //只是下载失败才弹出吧，因为如果是-1就可能是取消下载就不能弹出了
				{
					MyToast.makeText(mContext,
							R.string.app_item_download_failed,
							Toast.LENGTH_SHORT).show();
				}
				
			}
			
			//mPrefsUtil.removePresValue(String.valueOf(id));  //放Application的DownloadBroadcastReceiver里面
			
			Message msg = Message.obtain();
			msg.what = NOTIFY_REFRESH;
			mHandler.sendMessage(msg);
		}

		@Override
		public void downloadListDataChanged() {
			Message msg = Message.obtain();
			msg.what = NOTIFY_REFRESH;
			mHandler.sendMessage(msg);
		}

	}
	// 注册下载完成监听
	private void registerDownloadCompleteListener() {
		mReceiverDownLoadComplete = new DownloadCompleteListener();
		MarketApplication.getInstance().addDownloadCallback(mReceiverDownLoadComplete);
	}

	public void unRegisterDownloadCompleteListener() {
		if (mReceiverDownLoadComplete != null) {
			MarketApplication.getInstance().removeDownloadCallback(mReceiverDownLoadComplete);
		}
	}

	/**
	 * 注册应用程序安装的监听器，有程序安装或者卸载的时候，重新获取数据
	 */
	public void registerPackageChangeListener() {
		mPackChangeListener = new PackagesChangeCallback(){
			@Override
			public void packageChanged() {
				mLocalSoftVersion = MarketApplication.getInstance()
						.getInstalledAppPackages();
				Message msg = Message.obtain();
				msg.what = NOTIFY_REFRESH;
				mHandler.sendMessage(msg);
			}

			@Override
			public void updateListDateChanged() {//获取到更新数据后，刷新adapter，显示更新
				Message msg = Message.obtain();
				msg.what = NOTIFY_REFRESH;
				mHandler.sendMessage(msg);
			}
		};
		MarketApplication.getInstance().addInstalledAppCallback(mPackChangeListener);
	}
	
	public void removePackageChangeListener() {
		if (mPackChangeListener != null) {
			MarketApplication.getInstance().removeInstalledAppCallback(mPackChangeListener);
		}
	}
	
	static class ViewHolder {
		public ImageView app_iv; // 软件图标
		public TextView app_name; // 软件名
		public TextView app_version;// 版本号
		public TextView app_size;// 版本号
		public Button app_down_btn; // 免费 或 费用
		//public RatingBar app_rating; // 软件星评

		public View app_item_lay;
		// public ProgressBar app_progress_bar;// 下载进度条
		//public TextView app_downloading;
		//public RelativeLayout app_progress_bar_parent;
		//public ImageView app_progress_bg;// 进度条背景

		public Object tag;
		
		public ViewHolder(View convertView) {
			this.app_iv = (ImageView) convertView.findViewById(R.id.app_iv);
			this.app_name = (TextView) convertView.findViewById(R.id.app_name);
			this.app_version = (TextView) convertView
					.findViewById(R.id.app_version);
			this.app_size = (TextView) convertView.findViewById(R.id.app_size);
			this.app_down_btn = (Button) convertView
					.findViewById(R.id.app_down_btn);
//			this.app_rating = (RatingBar) convertView
//					.findViewById(R.id.app_rating);
			this.app_item_lay = (View) convertView
					.findViewById(R.id.app_item_lay);
			// this.app_progress_bar = (ProgressBar) convertView
			//		.findViewById(R.id.app_progress_bar);
//			this.app_downloading = (TextView) convertView
//					.findViewById(R.id.app_downloading);
//			this.app_progress_bar_parent = (RelativeLayout) convertView
//					.findViewById(R.id.app_progress_bar_parent);
//			this.app_progress_bg = (ImageView) convertView
//					.findViewById(R.id.app_progress_bg);
		}
		
	}

	private Resources getResources() {
		return mContext.getResources();
	}

	public Context getmContext() {
		return mContext;
	}

	public void setmContext(Context mContext) {
		this.mContext = mContext;
	}

	/**
	 * @return 所有的下载id的集合
	 */
	public HashMap<String, String> getAllDownloadId() {
		return DaoFactory.getDownloadInfoDao(mContext).getAllDownloadId(mContext);
		//return new HashMap<String, String>();
	}

	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		mAllDownloadIds = getAllDownloadId();
		super.notifyDataSetChanged();
	}
	
	Handler mHandler = new Handler()
	{
		@Override
		public void handleMessage(Message msg) 
		{
			if(msg.what == NOTIFY_REFRESH){
				removeMessages(NOTIFY_REFRESH);
				notifyDataSetChanged();
			}
			super.handleMessage(msg);
		}
	};
}
