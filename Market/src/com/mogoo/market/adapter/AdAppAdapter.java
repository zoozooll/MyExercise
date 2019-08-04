package com.mogoo.market.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
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
import com.mogoo.market.model.AdApp;
import com.mogoo.market.model.AppInfo;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.ui.AppDetailActivity;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;
import com.mogoo.market.widget.IrregularButton;

/**
 *@author dengliren
 * @param <T>
 * @param <T>
 *@date:2011-12-27
 *@description 畅销的数据适配器
 */
public class AdAppAdapter<T> extends ArrayAdapter<T> {

	private static final String TAG = "HotAdapter";
	private int mLayoutID = R.layout.app_item;
	private final int MAX_PROGRESS = 100;

	private LayoutInflater inflater;
	private Context mContext;
//	private DownloadManager mDownMgr;

	private String apkid;
	private List<String> apkidList = new ArrayList();
	private String InstallPath = null;
	private String SavaPath = "MogooLoad/";
	private String fileName = null;
	ImageDownloader mImageManager;
	private HashMap<String,Integer> mLocalSoftVersion = new HashMap<String, Integer>();
	private MogooDownloadManager mogooDownloadManager;
	private DownloadCompleteListener mReceiverDownLoadComplete;
	/** 所有下载管理中所有已下载的任务*/
	private HashMap<String, String> mAllDownloadIds;
	private HashMap<String, Long> mURI2DownIDMapping = new HashMap<String, Long>();
	/** 下载应用的downloadId和应用名字 */
	private HashMap<Long, String> downloadMap = new HashMap<Long, String>();
	private DownPrefsUtil mPrefsUtil = null;
	private PackagesChangeCallback mPackChangeListener;
	
	public AdAppAdapter(Context context, List<T> list) {
		super(context, -1, list);

		InstallPath = AppUtils.getSDKpath() + "/" + SavaPath;

		mLocalSoftVersion = MarketApplication.getInstance().getInstalledAppPackages();
		this.mContext = context;
		inflater = LayoutInflater.from(mContext);
		mImageManager = ImageDownloader.getInstance(mContext);
		mogooDownloadManager = new MogooDownloadManager(mContext,
				mContext.getContentResolver(), mContext.getPackageName());
//		mDownMgr = (DownloadManager) mContext
//				.getSystemService(Context.DOWNLOAD_SERVICE);
		mAllDownloadIds = getAllDownloadId();
		registerDownloadCompleteListener();
		registerPackageChangeListener();
		mPrefsUtil = DownPrefsUtil.getInstance(mContext);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder hotHolder;
		T item = getItem(position);
		final AdApp appInfo = (AdApp) item;

		if (convertView == null) {
			convertView = inflater.inflate(mLayoutID, parent, false);
			hotHolder = new ViewHolder(convertView);
			convertView.setTag(hotHolder);
		} else {
			hotHolder = (ViewHolder) convertView.getTag();
		}

		mImageManager.download(appInfo.getIconUrl(), hotHolder.app_iv,
				BitmapFactory.decodeResource(mContext.getResources(), R.drawable.defautl_list_itme_pic_loading));

		apkidList.add(appInfo.getApkId());

		hotHolder.app_progress_bar_parent.setVisibility(View.GONE);
		hotHolder.app_down_btn.setVisibility(View.VISIBLE);

		if (mAllDownloadIds != null
				&& mAllDownloadIds.containsKey(appInfo.getApkId())) {// 有下载
			MogooDownloadManager mdm = new MogooDownloadManager(mContext);
			final String downloadId = mAllDownloadIds.get(appInfo.getApkId());
			String value = mPrefsUtil.getPrefsValue(downloadId, "");
			String[] result = mPrefsUtil.parseResponse(value);
			//MogooDownloadInfo info = mdm.getDownloadInfo(downloadId);
			//if (info != null && (info.getDownloadStatus() < 200)) {// 如果正在下载，下载成功为200
			if (!TextUtils.isEmpty(value)) {// 如果正在下载，下载成功为200
				// registerDownloadListener(hotHolder, Long.parseLong(downloadId));
				hotHolder.app_progress_bar_parent.setVisibility(View.VISIBLE);
				hotHolder.app_down_btn.setVisibility(View.GONE);
				hotHolder.app_progress_bg
					.setImageResource(R.drawable.app_item_progress_left_blue);
			} else if (isUpdatable(appInfo.getPackageName())) {// 如果有更新
				if(ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))){//如果有更新且已下载
					showDownButton(true, true, R.string.install, hotHolder);
				} else {
					showDownButton(true, true, R.string.app_update, hotHolder);
				}
			} else if (mLocalSoftVersion.containsKey(appInfo.getPackageName())) {// 是不是已安装
				showDownButton(true, false, R.string.app_item_installed, hotHolder);
			} else if (ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))) {//如果已下载
				 showDownButton(true, true, R.string.install, hotHolder);
			}else{//直接显示免费或者价格
				showDownButton(true, true, R.string.app_item_down, hotHolder);
			}
		} else if (isUpdatable(appInfo.getPackageName())) {// 如果有更新
			if(ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))){//如果有更新且已下载
				showDownButton(true, true, R.string.install, hotHolder);
			} else {
				showDownButton(true, true, R.string.app_update, hotHolder);
			}
		} else if (mLocalSoftVersion.containsKey(appInfo.getPackageName())) {// 是不是已安装
			showDownButton(true, false, R.string.app_item_installed, hotHolder);
		} else if (ToolsUtils.isDownloadedAPk(mContext, SavaPath, getApkName(appInfo))) {//如果已下载
			 showDownButton(true, true, R.string.install, hotHolder);
		}else{//直接显示免费或者价格
			showDownButton(true, true, R.string.app_item_down, hotHolder);
		}
		
		hotHolder.app_rating.setRating(Float.parseFloat(appInfo.getScore()));
		hotHolder.app_name.setText(appInfo.getName());
		hotHolder.app_name.setSelected(true);
		hotHolder.app_version.setText(mContext.getString(
				R.string.app_item_version, appInfo.getVersionStr()));
		String sizeStr = ToolsUtils.getSizeStr(appInfo.getApkSize());
		hotHolder.app_size.setText(sizeStr);

		hotHolder.app_item_lay.setOnClickListener(new DownClickListener(
				hotHolder, appInfo));
		hotHolder.app_down_btn.setOnClickListener(new DownClickListener(
				hotHolder, appInfo));
		hotHolder.app_downloading.setOnClickListener(new DownClickListener(
				hotHolder, appInfo));
//		final Long downloadID = mURI2DownIDMapping.get(appInfo.getApkAddress());
//		if (downloadID != null) {
//		}

		return convertView;
	}

	private boolean isUpdatable(String pkgName) {
		return MarketApplication.isHaveUpdate(pkgName);
	}
	/**
	 * 设置viewholder
	 * 
	 * @param hotHolder
	 *            缓存
	 * @return appItem的视图
	 */
	// private View setAppItemViewTag(final ViewHolder hotHolder) {
	// View convertView;
	//
	// return convertView;
	// }

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
		DownloadProgressListener downListener = new DownloadProgressListener() {
			@Override
			public void onDownloadProgressChanged(int progress) {
//System.out.println(progress);
				if (hotHolder.tag == this) {
					if (progress < 0 || progress >= MAX_PROGRESS) {//下载失败或者下载成功
						if(progress == 100){
							// hotHolder.app_progress_bar.setProgress(progress);
						} else if (progress < 0) {
							MyToast.makeText(mContext,
									R.string.app_item_download_failed,
									Toast.LENGTH_SHORT).show();
						}
						hotHolder.app_progress_bar_parent.setVisibility(View.GONE);
						hotHolder.app_down_btn.setVisibility(View.VISIBLE);
					} else if (progress > 0) {
						hotHolder.app_progress_bar_parent.setVisibility(View.VISIBLE);
						hotHolder.app_down_btn.setVisibility(View.GONE);
						hotHolder.app_progress_bg
								.setImageResource(R.drawable.app_item_progress_left_blue);
						// hotHolder.app_progress_bar.setProgress(progress);
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
	private void showDownButton(boolean isBtnVisible, boolean enable,
			int textId, ViewHolder hotHolder) {
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
		AdApp AdApp;

		public DownClickListener(ViewHolder holder, AdApp AdApp) {
			super();
			this.holder = holder;
			this.AdApp = AdApp;
		}

		public void onClick(View v) {
			if (v.getId() == R.id.app_down_btn) {
				final long downId = startDownApp(AdApp);
				if (downId != -1) {
					mAllDownloadIds.put(AdApp.getApkId(), "" + downId); // 点击下载，更新下载集合
					holder.app_down_btn.setVisibility(View.GONE);
					holder.app_progress_bar_parent.setVisibility(View.VISIBLE);
					mPrefsUtil.savePresValue(String.valueOf(downId), String.valueOf(AdApp.getApkId()), String.valueOf(true));
					// registerDownloadListener(holder, downId);
					holder.app_progress_bar_parent.setVisibility(View.VISIBLE);
					holder.app_down_btn.setVisibility(View.GONE);
					holder.app_progress_bg
						.setImageResource(R.drawable.app_item_progress_left_blue);
				}

			} else if (v.getId() == R.id.app_item_lay) {

				// TODO Auto-generated method stub
				Intent intent = new Intent();
				intent.setClass(mContext, AppDetailActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString(AppDetailActivity.EXTRA_APP_ID,
						AdApp.getApkId());
				bundle.putString(AppDetailActivity.EXTRA_APP_PACKAGENAME,
						AdApp.getPackageName());

				if (mAllDownloadIds.containsKey(AdApp.getApkId())) {
					bundle.putInt(AppDetailActivity.EXTRA_APP_DOWNLOADID_INT,
							Integer.valueOf(mAllDownloadIds.get(AdApp
									.getApkId())));
				}

				intent.putExtras(bundle);
				mContext.startActivity(intent);
			}
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

	public long startDownApp(AdApp appInfo) {
		fileName = appInfo.getName() + "_" + appInfo.getVersionCode() + ".apk";
		fileName.replace(" ", "");
		return onDownApp(appInfo);
	}

	private String getApkName(AdApp appInfo) {
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
	private long onDownApp(AdApp appInfo) {
		long result = -1L;// 已下载返回－1
		// 判断sdcard
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
			MyToast.makeText(mContext,
					getResources().getText(R.string.install_no_sdcard),
					Toast.LENGTH_SHORT).show();

		}
		return result;
	}

	private long down(AdApp appInfo) 
	{
		String apkUrl = appInfo.getApkAddress();
		long tempDownLoadId = ToolsUtils.downloadApk(mogooDownloadManager, SavaPath, getApkName(appInfo),apkUrl);
		
		downloadMap.put(tempDownLoadId, getApkName(appInfo));
		mURI2DownIDMapping.put(appInfo.getApkAddress(), tempDownLoadId);

		//插入下载数据信息到数据库
		DaoFactory.getDownloadInfoDao(mContext).addBean(new DownloadInfo(String.valueOf(tempDownLoadId), appInfo.getApkId(), 
				apkUrl, SavaPath, appInfo.getName(), ""+appInfo.getApkSize(), appInfo.getIconUrl(), appInfo.getScore(),
				appInfo.getVersionStr(), Integer.valueOf(appInfo.getVersionCode()), appInfo.getPackageName()));
				
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
		if(MarketApplication.installedAppPackages.containsKey(appInfo.getPackageName())) 
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
			notifyDataSetChanged();
		}

		@Override
		public void downloadListDataChanged() {
			notifyDataSetChanged();
		}

	}
	// 注册广播监听器
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
				notifyDataSetChanged();
			}

			@Override
			public void updateListDateChanged() {//获取到更新数据后，刷新adapter，显示更新
				notifyDataSetChanged();
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
		public IrregularButton app_down_btn; // 免费 或 费用
		public RatingBar app_rating; // 软件星评

		public View app_item_lay;
		// public ProgressBar app_progress_bar;// 下载进度条
		public TextView app_downloading;
		public RelativeLayout app_progress_bar_parent;
		public ImageView app_progress_bg;// 进度条背景

		public Object tag;

		public ViewHolder(View view) {
			this.app_iv = (ImageView) view.findViewById(R.id.app_iv);
			this.app_name = (TextView) view.findViewById(R.id.app_name);
			this.app_version = (TextView) view.findViewById(R.id.app_version);
			this.app_size = (TextView) view.findViewById(R.id.app_size);
			this.app_down_btn = (IrregularButton) view
					.findViewById(R.id.app_down_btn);
			this.app_rating = (RatingBar) view.findViewById(R.id.app_rating);
			this.app_item_lay = (View) view.findViewById(R.id.app_item_lay);
			// this.app_progress_bar = (ProgressBar) view
			// 		.findViewById(R.id.app_progress_bar);
			this.app_downloading = (TextView) view
					.findViewById(R.id.app_downloading);
			this.app_progress_bar_parent = (RelativeLayout) view
					.findViewById(R.id.app_progress_bar_parent);
			this.app_progress_bg = (ImageView) view
					.findViewById(R.id.app_progress_bg);
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

	public String getApkid(int id) {
		return apkidList.get(id);
	}

	public List<String> getApkidList() {
		return apkidList;
	}

	public void setApkidList(List<String> apkidList) {
		this.apkidList = apkidList;
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


}
