package com.mogoo.market.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.Gallery.LayoutParams;
import android.widget.Toast;

import com.mogoo.components.download.MogooDownloadInfo;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.R;
import com.mogoo.market.MarketApplication.DownloadChangeCallback;
import com.mogoo.market.MarketApplication.PackagesChangeCallback;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.database.DownloadInfoDaoImpl;
import com.mogoo.market.model.AppUpdatesInfo;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.InstalledAppInfo;
import com.mogoo.market.ui.ManagerActivity;
import com.mogoo.market.ui.MarketGroupActivity;
import com.mogoo.market.ui.SdcardInstallDeleteActivity;
import com.mogoo.market.ui.SoftMoveActivity;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.ManagerClearAllView;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.uicomponent.PagerView;
import com.mogoo.market.utils.AppUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;

public class ManagerPagerAdapter extends PagerAdapter implements OnClickListener {

	static final String TAG = "ManagerPagerAdapter";
	
	public final static int HANDLER_DOWN_LIST_CHANGE = 1;
	public final static int HANDLER_INSTALL_LIST_CHANGE = 2;
	public final static int HANDLER_UPDATE_ALL = 3;
	public final static int HANDLER_DOWN_Failed = 4;
	
	private DownloadManagerAdapter<DownloadInfo> mDownAdapter;   //下载列表适配器
	private SoftManagerAdapter<InstalledAppInfo> mSoftAdapter;   //软件列表适配器
	private SimpleAdapter moreSimpleAdapter;

	private FixedListView mDownListView;    							 //下载列表
	private FixedListView mSoftListView;    							 //软件列表
	private ListView mMoreSettingsManagerListView;                       //更多列表
	
	private ManagerClearAllView mClearAllView = null;    //一键清除整体布局，用于列表addHeaderView
	private ManagerClearAllView mUpdateAllView = null;   //全部更新整体布局，用于列表addHeaderView
	private Button mClearAllButton = null;               //一键清除按钮
	private Button mUpdateAllBotton = null;				 //全部更新按钮
	
	private Resources mRes;
	private static MarketApplication application = null;
	private ImageDownloader mImageLoader;  				 //图片加载管理
	private MogooDownloadManager manager = null;         //下载管理器
	private List<Map<String,Object>> mMoreList;
	
	private Context mContext;
	private int mLength = 3;
	
	public ManagerPagerAdapter(Activity context) {
		mContext = context;
			
		application = MarketApplication.getInstance();
		
		mRes = mContext.getResources();
		mImageLoader = ImageDownloader.getInstance(mContext);
		
		manager = new MogooDownloadManager(mContext, mContext.getContentResolver(), mContext.getPackageName());
		
		init();
		
		application.addDownloadCallback(downloadChangeCallback);   //添加下载回调监听
		application.addInstalledAppCallback(packagesChangeCallback);  //添加软件包变更回调监听
	}
	
	@Override
	public int getCount() {
		return mLength;
	}

	
	@Override
	public Object instantiateItem(View container, int position) {

		PagerView pagerView = new PagerView(mContext);
		ListView listview = null;
		ViewGroup parent = null;
		Message msg = new Message();
		
		if (position == ManagerActivity.SOFT_MANAGER) {
			listview = mSoftListView;
			msg.what = HANDLER_INSTALL_LIST_CHANGE;
		} else if (position == ManagerActivity.DOWNLOAD_MANAGER) {
			listview = mDownListView;
			msg.what = HANDLER_DOWN_LIST_CHANGE;
		} else if (position == ManagerActivity.MORE_SETTINGS) {
			listview = mMoreSettingsManagerListView;
		}

		parent = (ViewGroup) listview.getParent();
		if (parent != null) {
			parent.removeView(listview);
		}
		pagerView.setBaseCenterLayout(listview);
		if(position == ManagerActivity.SOFT_MANAGER) {
			addEmptyView(mContext, mSoftListView ,R.string.no_install);
			mhandler.sendMessage(msg);
		}else if(position == ManagerActivity.DOWNLOAD_MANAGER) {
			addEmptyView(mContext, mDownListView ,R.string.no_down);
			mhandler.sendMessage(msg);
		}
		((ViewPager) container).addView(pagerView, 0);
		return pagerView;
	}
	
	@Override
	public void destroyItem(View container, int position, Object view) {
		((ViewPager) container).removeView((View) view);
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == ((View) object);
	}

	@Override
	public void finishUpdate(ViewGroup container) {
		super.finishUpdate(container);
	}

	@Override
	public void restoreState(Parcelable state, ClassLoader loader) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	
	
	@Override
	public void startUpdate(ViewGroup container) {
		super.startUpdate(container);
	}
	
	public void stop() {
		application.removeInstalledAppCallback(packagesChangeCallback);
		application.removeDownloadCallback(downloadChangeCallback);
	}
	
	public void refresh() {
		Message msg = new Message();
		// int position = ((ManagerActivity) mContext).getCurrentItem();
		// if(position == ManagerActivity.SOFT_MANAGER) {
		msg.what = HANDLER_INSTALL_LIST_CHANGE;
		mhandler.sendMessage(msg);
		// }else if(position == ManagerActivity.DOWNLOAD_MANAGER) {
		Message refreshDown = new Message();
		refreshDown.what = HANDLER_DOWN_LIST_CHANGE;
		mhandler.sendMessage(refreshDown);
		// }
	}
	
	public void refresh(int position) {
		Message msg = new Message();
		if(position == ManagerActivity.SOFT_MANAGER) {
			msg.what = HANDLER_INSTALL_LIST_CHANGE;
			mhandler.sendMessage(msg);
		}else if (position == ManagerActivity.DOWNLOAD_MANAGER) {
			msg.what = HANDLER_DOWN_LIST_CHANGE;
			mhandler.sendMessage(msg);
		}
	}
	
	/**
	 * 主要初始化视图
	 */
	private void init() 
	{
		mClearAllView = new ManagerClearAllView(mContext, mRes.getString(R.string.clear_all));
		mClearAllButton = (Button) mClearAllView.findViewById(R.id.btn_clear_all);
		mClearAllButton.setOnClickListener(this);
		mUpdateAllView = new ManagerClearAllView(mContext, mRes.getString(R.string.update_all));
		mUpdateAllBotton = (Button) mUpdateAllView.findViewById(R.id.btn_clear_all);
		mUpdateAllBotton.setOnClickListener(this);
		
		mDownAdapter = new DownloadManagerAdapter<DownloadInfo>(mContext, MarketApplication.myDownloadAppList, mImageLoader);
		mSoftAdapter = new SoftManagerAdapter<InstalledAppInfo>(mContext, -1, MarketApplication.installedAppList);
		
		mDownListView = new FixedListView(mContext);
		initListView(mDownListView);
		mSoftListView = new FixedListView(mContext);
		initListView(mSoftListView);
		mMoreSettingsManagerListView=new ListView(mContext);
		initListView(mMoreSettingsManagerListView);
	
		mDownListView.addHeaderView(mClearAllView);
		mDownListView.setDivider(mContext.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		mDownListView.setAdapter(mDownAdapter);
		mSoftListView.addHeaderView(mUpdateAllView);
		mSoftListView.setDivider(mContext.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		mSoftListView.setAdapter(mSoftAdapter);
		moreSimpleAdapter=new SimpleAdapterForBackground(mContext, getData(), R.layout.manager_more_layout, new String[]{"content","subtitle","image"},
				new int[]{R.id.item_content,R.id.item_subtitle,R.id.item_icon});
		mMoreSettingsManagerListView.setDivider(mContext.getResources().getDrawable(R.drawable.divider_horizontal_dark_opaque));
		mMoreSettingsManagerListView.setAdapter(moreSimpleAdapter);
		mMoreSettingsManagerListView.setOnItemClickListener(new MoreOnClickListener());
	}
	
	/**
	 * 初始化列表
	 * @param listview
	 */
	private void initListView(ListView listview) 
	{
		listview.setCacheColorHint(Color.WHITE);
		listview.setVerticalScrollBarEnabled(false);
	}
	
	private Handler mhandler = new Handler() 
	{
        @SuppressWarnings({ "unchecked", "rawtypes" })
		public void handleMessage(Message msg) 
        {
            switch (msg.what) {
                case HANDLER_DOWN_LIST_CHANGE:   //正在下载和已经完成的软件列表重新获取到了并更新数据
                	removeMessages(HANDLER_DOWN_LIST_CHANGE);
                	if(MarketApplication.myDownloadAppList.isEmpty())
                	{
                		HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mDownListView.getAdapter();
                		mDownAdapter=(DownloadManagerAdapter) listAdapter.getWrappedAdapter();
                		mDownAdapter.notifyDataSetInvalidated();
                	}
                	else
                	{
                		HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mDownListView.getAdapter();
                		mDownAdapter=(DownloadManagerAdapter) listAdapter.getWrappedAdapter();
                		mDownAdapter.notifyDataSetChanged();
                	}
                    break;
                    
                case HANDLER_DOWN_Failed:   //下载失败,得刷新界面
                	removeMessages(HANDLER_DOWN_LIST_CHANGE);
                	if(((ManagerActivity) mContext).getCurrentItem() == ManagerActivity.DOWNLOAD_MANAGER) 
            		{
                		if(MarketApplication.myDownloadAppList.isEmpty())
                    	{
                			HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mDownListView.getAdapter();
                    		mDownAdapter=(DownloadManagerAdapter) listAdapter.getWrappedAdapter();
                			mDownAdapter.notifyDataSetInvalidated();
                    	}
                    	else
                    	{
                    		HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mDownListView.getAdapter();
                    		mDownAdapter=(DownloadManagerAdapter) listAdapter.getWrappedAdapter();
                    		mDownAdapter.notifyDataSetChanged();
                    	}
            		}
            		else if(((ManagerActivity) mContext).getCurrentItem() == ManagerActivity.SOFT_MANAGER) 
            		{
            			HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSoftListView.getAdapter();
            			mSoftAdapter=(SoftManagerAdapter) listAdapter.getWrappedAdapter();
            			mSoftAdapter.notifyDataSetChanged();
            		}
                    break;
                
                case HANDLER_INSTALL_LIST_CHANGE:   //获取到了已安装软件列表并更新数据
                	removeMessages(HANDLER_INSTALL_LIST_CHANGE);
                	
                	if(MarketApplication.installedAppList.isEmpty())
                	{
                		HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSoftListView.getAdapter();
            			mSoftAdapter=(SoftManagerAdapter) listAdapter.getWrappedAdapter();
                		mSoftAdapter.notifyDataSetInvalidated();
                	}
                	else
                	{
                		HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSoftListView.getAdapter();
            			mSoftAdapter=(SoftManagerAdapter) listAdapter.getWrappedAdapter();
                		mSoftAdapter.notifyDataSetChanged();
                	}
                    break;
                    
                case HANDLER_UPDATE_ALL:   //有更新信息
                	removeMessages(HANDLER_UPDATE_ALL);
                	HeaderViewListAdapter listAdapter= (HeaderViewListAdapter)mSoftListView.getAdapter();
        			mSoftAdapter=(SoftManagerAdapter) listAdapter.getWrappedAdapter();
                	mSoftAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    
    /**
     * 添加列表数据为空时的界面
     */
    private void addEmptyView(Context context, ListView listview, int emptyTextId) 
    {
		CommonEmptyView emptyView = new CommonEmptyView(mContext, listview);
		emptyView.setEmptyText(context.getResources().getString(emptyTextId));

		ViewGroup layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
    
	@Override
	public void onClick(View v) 
	{
		int id = v.getId();
		if(!AppUtils.isSdcardExist()){
			MyToast.makeText(mContext,
					mContext.getResources().getText(R.string.no_sdcard_tip),
					Toast.LENGTH_SHORT).show();
			return;
		}
		switch (id) 
		{
		case R.id.btn_clear_all:
			if(((ManagerActivity) mContext).getCurrentItem()==0)  //全部更新
			{
				updatesAll();
			}
			else if(((ManagerActivity) mContext).getCurrentItem()==1)  //一键清除
			{
				showClearAllView();
			}

		default:
			break;
		}
		
	}
	
	/**
     * 显示一键清除提示框
     */
    private PopupWindow pwClearAll = null;
    private void showClearAllView() 
    {
    	final MyBoolean myChecked = new MyBoolean(false);
    	
    	LayoutInflater inf = LayoutInflater.from(mContext);
    	View view = inf.inflate(R.layout.clear_all_downloadlist_all_layout, null);
    	final ImageView iv_delete_apk = (ImageView) view.findViewById(R.id.iv_delete_apk);
    	Button btn_ok = (Button) view.findViewById(R.id.btn_ok_delete_all_downloadlist);
    	Button btn_cancel = (Button) view.findViewById(R.id.btn_cancel_delete_all_downloadlist);
    	
    	pwClearAll = new PopupWindow(view, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
    	pwClearAll.showAtLocation(view, Gravity.CENTER, 0, 0);
    	
    	iv_delete_apk.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				if(myChecked.isTrue())
				{
					iv_delete_apk.setImageResource(R.drawable.checkbox_uncheck);
				}
				else
				{
					iv_delete_apk.setImageResource(R.drawable.checkbox_check);
				}
				myChecked.changeValue();
			}
		});
    	
    	btn_ok.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				if(myChecked.isTrue())
				{
					cancelAllDownloads(true);
				}
				else
				{
					cancelAllDownloads(false);
				}
				pwClearAll.dismiss();
			}
		});
    	
    	btn_cancel.setOnClickListener(new OnClickListener() 
    	{
			@Override
			public void onClick(View v) 
			{
				pwClearAll.dismiss();
			}
		});
	}
    
    public boolean isClearAllShowing() {
    	return pwClearAll != null && pwClearAll.isShowing();
    }
    
    public void dismissClearAll() {
    	if(pwClearAll == null || !pwClearAll.isShowing()) {
    		return;
    	}
    	pwClearAll.dismiss();
    }
    
    class MyBoolean
    {
    	boolean myBoolean = false;

		public MyBoolean(boolean myBoolean) 
		{
			super();
			this.myBoolean = myBoolean;
		}

		public boolean isTrue() 
		{
			return myBoolean;
		}

		public void setMyBoolean(boolean myBoolean) 
		{
			this.myBoolean = myBoolean;
		}
		
		public void changeValue() 
		{
			myBoolean = !myBoolean;
		}
    }
	
	/**
     * 一键清除
     * @param isDeleteFile：是否同时删除已下载的文件
     */
    private void cancelAllDownloads(final boolean isDeleteFile) 
    {
    	new Thread(new Runnable() 
    	{
			@Override
			public void run() 
			{
				//ArrayList<DownloadInfo> appList = new ArrayList<DownloadInfo>();
				ArrayList<String> deletes = new ArrayList<String>();
		    	
				DownloadInfoDaoImpl did = DaoFactory.getDownloadInfoDao(mContext);
		        Cursor c = did.getAllBean();
		        try 
		        {
		        	if(c!=null && c.getCount()>0)
		            {
		            	for(c.moveToFirst();!c.isAfterLast();c.moveToNext())
		                {
		            		String download_id = c.getString(1);
		            		/*String app_id = c.getString(2);
		            		String download_url = c.getString(3);
		            		String save_path = c.getString(4);
		            		String name = c.getString(5);
		            		String size = c.getString(6);
		            		String icon_url = c.getString(7);
		            		String score = c.getString(8);
		            		String versionName = c.getString(9);
		            		int versionCode = c.getInt(10);
		            		String packageName = c.getString(11);*/
		            		
		            		MogooDownloadInfo info = manager.getDownloadInfo(download_id);
		            		if(info!=null)
		            		{
		            			String filepath = info.getDownloadPosition();
			            		int status = info.getDownloadStatus();
			            		if(status==200)  //下载完
			            		{
			            			if(isDeleteFile)
			            			{
			            				ToolsUtils.deleteFile(mContext, filepath);
			            			}
			            		
			            		}
			            			manager.cancelDownload(Long.parseLong(download_id));
			            			DownPrefsUtil.getInstance(mContext).removePresValue(String.valueOf(download_id));   //SharePreference是否下载，删除记录
			            			deletes.add(download_id);	
		            		}
		            		else
		            		{
		            			deletes.add(download_id);
		            		}
		                }
		            }
				} catch (Exception e) {
					Log.e(TAG, e.toString());
				}
		        finally
		        {
		        	if(c!=null)
		        	{
		        		c.close();
		        	}
		        }
		        
		        if(!deletes.isEmpty())
		        {
		        	// lxr add 一键清除所有下载同时清除正在更新列表.
		        	UpdatesUtils.deleteAll(mContext);
		        	did.delete(deletes);
		        }
			}
		}).start();
	}
    
    /**
     * 一键更新
     */
    private boolean isUpdated = false;   //判断是否一键更新过
    private void updatesAll()
    {
    	//没有更新，就不需再响应了
    	if(MarketApplication.installedAppList.isEmpty() || MarketApplication.getAppUpdatesInfoMap().isEmpty()/* || isUpdated*/)
    	{
    		return;
    	}
    	
    	HashMap<String, String> allUpdates = UpdatesUtils.getAll(mContext);   //正在更新列表
    	
    	int updateNum = 0;
    	for(int i = 0 ,sum = MarketApplication.installedAppList.size() ; i<sum ; ++i)
		{
    		InstalledAppInfo localApp = MarketApplication.installedAppList.get(i);
			String packageName = localApp.getPackageInfo().packageName;
			AppUpdatesInfo updateInfo = localApp.getUpdateInfo();
			String apkName = (updateInfo != null) ? updateInfo.getAppName()
					+ "_" + updateInfo.getAppVersionCode() + ".apk" : "";
			//此项没有正在更新，此时可能更新文件已经存在，就不要下载了
			if(updateInfo!=null && !allUpdates.containsKey(packageName) && MarketApplication.isHaveUpdate(packageName)
					&& !ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, apkName.replace(" ", "")))
			{
				
				if(ToolsUtils.checkBeforeDownload(application,Long.valueOf(localApp.getSizebytes())))  //更新文件不存在才下载
				{
					long mDownLoadId = ToolsUtils.downloadApk(manager, MarketApplication.SavaPath, apkName,updateInfo.getUpdateUrl());
					if(mDownLoadId>0)
					{
						boolean insertFlat=true;
						for(DownloadInfo downloadInfo : MarketApplication.myDownloadAppList){
							if(downloadInfo.getApp_id().equals(updateInfo.getApkId())){
								insertFlat=false;
								break;
							}else{
								insertFlat=true;
							}
								
						}
						//插入下载数据信息到数据库
						if(insertFlat){
							DaoFactory.getDownloadInfoDao(application).addBean(new DownloadInfo(String.valueOf(mDownLoadId), updateInfo.getApkId(), 
		            				updateInfo.getUpdateUrl(), MarketApplication.SavaPath, apkName, localApp.getSizebytes(), updateInfo.getIconUrl(), "",
		            				updateInfo.getAppVersionName(), Integer.valueOf(updateInfo.getAppVersionCode()), updateInfo.getPackageame()));
		            		DownPrefsUtil.getInstance(application).savePresValue(String.valueOf(mDownLoadId), String.valueOf(updateInfo.getApkId()), String.valueOf(true));
						}
	            		//保存更新下载id
		        		UpdatesUtils.save(mContext, updateInfo.getPackageame(), ""+mDownLoadId);
		        		updateNum++;
					}
				}
			}
		}
    	
    	//有更新，需要更新视图显示更新进度
    	if(updateNum>0)
    	{
    		isUpdated = true;
    		
    		Message msg = new Message();
			msg.what = HANDLER_UPDATE_ALL;
			mhandler.sendMessage(msg);
    	}
    }
    
    /**
     * 添加到MarketApplication中的软件变更回调
     */
    private PackagesChangeCallback packagesChangeCallback = new PackagesChangeCallback() 
    {
		@Override
		public void packageChanged() 
		{
			Message msg = new Message();
			msg.what = HANDLER_INSTALL_LIST_CHANGE;
			mhandler.sendMessage(msg);
		}

		@Override
		public void updateListDateChanged() 
		{
			Message msg = new Message();
			msg.what = HANDLER_INSTALL_LIST_CHANGE;
			mhandler.sendMessage(msg);
		}
	};
	
	/**
	 * 有下载任务完成回调接口
	 */
	DownloadChangeCallback downloadChangeCallback = new DownloadChangeCallback() 
    {
		@Override
		public void downloadChanged(long id,int downloadStatus) 
		{
			isUpdated = false;  //可能是更新失败，重新激活“全部更新”
			
			if(downloadStatus==200)
			{
				Message msg = new Message();
	    		msg.what = HANDLER_DOWN_LIST_CHANGE;
	    		mhandler.sendMessage(msg);
			}
			else
			{
				Message msg = new Message();
	    		msg.what = HANDLER_DOWN_Failed;
	    		mhandler.sendMessage(msg);
			}
    		
			HashMap<String, String> allUpdates = UpdatesUtils.getAll(mContext);
			if(!allUpdates.isEmpty() && allUpdates.containsValue(""+id))
			{
				Message msg1 = new Message();
	    		msg1.what = HANDLER_INSTALL_LIST_CHANGE;
	    		mhandler.sendMessage(msg1);
			}
		}

		@Override
		public void downloadListDataChanged() 
		{
			Message msg = new Message();
    		msg.what = HANDLER_DOWN_LIST_CHANGE;
    		mhandler.sendMessage(msg);
		}
	};
	
	//lzg
	private List<Map<String,Object>> getData(){
		mMoreList=new ArrayList<Map<String,Object>>();
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("content",mRes.getString(R.string.soft_move));
		map.put("subtitle",mRes.getString(R.string.soft_subtitle_move));
	    map.put("image",R.drawable.soft_move_image);
	    mMoreList.add(map);
		
		map = new HashMap<String,Object>();
		map.put("content",mRes.getString(R.string.delete_install_package));
		map.put("subtitle",mRes.getString(R.string.delete_install_subtitle_package));
	    map.put("image",R.drawable.delete_install_package_image);
	    mMoreList.add(map);
		
		return mMoreList;
	}
	
	class MoreOnClickListener implements AdapterView.OnItemClickListener{
		
		@Override
		public void onItemClick(AdapterView<?>arg0,View arg1,int arg2,long arg3){
			Intent childintent = new Intent();
			if(mMoreList.get(arg2).get("content").equals(mRes.getString(R.string.soft_move))){
				childintent.setClass(mContext, SoftMoveActivity.class);
				MarketGroupActivity mga = MarketGroupActivity.getInstance();
				childintent.putExtra(MarketGroupActivity.EXTRA_BACK_INTENT, mga.getNowTabIntent());
				mga.setupTabChildView(childintent);
				mga.setupTabIntent(MarketGroupActivity.getCurrentTab(),childintent);
			   
			}else if (mMoreList.get(arg2).get("content").equals(mRes.getString(R.string.delete_install_package))){
				if(!AppUtils.isSDCardReady()){
					String text = mContext.getResources().getString(R.string.no_sdcard_tip);
					MyToast.makeText(mContext, text, Toast.LENGTH_SHORT).show();
				}
				else{
				childintent.setClass(mContext, SdcardInstallDeleteActivity.class);
				MarketGroupActivity mga = MarketGroupActivity.getInstance();
				childintent.putExtra(MarketGroupActivity.EXTRA_BACK_INTENT, mga.getNowTabIntent());
				mga.setupTabChildView(childintent);
				mga.setupTabIntent(MarketGroupActivity.getCurrentTab(),childintent);
				}
			}
		}
	}

	/**
	 * Cannot fix the IndexOutOfBoundsException ,just have to ignore the error.
	 * 
	 */
	private class FixedListView extends ListView {

		public FixedListView(Context context) {
			super(context);
		}

		@Override
		protected void layoutChildren() {
			try {
				super.layoutChildren();
			} catch (IndexOutOfBoundsException e) {
				// ignore.
				Log.d(TAG, "current listview throw IndexOutOfBoundsException, cannot fix the issur.");
			}
		}
	}
	
	/**
	 * 重新扫描SD卡
	*/
//	Thread scanSdcardThread=new Thread(){
//		@Override
//		public void run(){
//			   Log.d("#####","+++++scanSdcardThread run");
//			   ssu.myFiles.clear();
//			   ssu.FindAllAPKFile(new File("/mnt/sdcard"));
//			   mSdcardInstallAdapter.notifyDataSetChanged();
//		       }
//		};
}
