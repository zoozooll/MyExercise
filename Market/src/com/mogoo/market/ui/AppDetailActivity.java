package com.mogoo.market.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mogoo.components.download.DownloadProgressListener;
import com.mogoo.components.download.DownloadProgressManager;
import com.mogoo.components.download.MogooDownloadInfo;
import com.mogoo.components.download.MogooDownloadManager;
import com.mogoo.market.MarketApplication;
import com.mogoo.market.MarketApplication.DownloadChangeCallback;
import com.mogoo.market.R;
import com.mogoo.market.adapter.CommentAdapter;
import com.mogoo.market.adapter.ImageShotAdapter;
import com.mogoo.market.database.DaoFactory;
import com.mogoo.market.model.AdAppDetails;
import com.mogoo.market.model.AppDetailLikeItemFactory;
import com.mogoo.market.model.AppDetails;
import com.mogoo.market.model.AppDetailsReq;
import com.mogoo.market.model.Comments;
import com.mogoo.market.model.DownloadInfo;
import com.mogoo.market.model.PostCommentsReq;
import com.mogoo.market.model.RecommentAppInfo;
import com.mogoo.market.model.onRequestCallBack;
import com.mogoo.market.network.http.HttpUrls;
import com.mogoo.market.paginate.PaginateListView_1;
import com.mogoo.market.uicomponent.CommonEmptyView;
import com.mogoo.market.uicomponent.ImageDownloader;
import com.mogoo.market.uicomponent.ListFooterView;
import com.mogoo.market.uicomponent.MyToast;
import com.mogoo.market.utils.DateUtils;
import com.mogoo.market.utils.DownPrefsUtil;
import com.mogoo.market.utils.ToolsUtils;
import com.mogoo.market.utils.UpdatesUtils;
import com.mogoo.market.widget.AppDetailTabbar;
import com.mogoo.market.widget.AppDetailTabbar.DetailTabMember;
import com.mogoo.market.widget.TextProgressBar;
import com.mogoo.market.widget.TitleBar;
import com.mogoo.parser.Result;

/**
 * @author csq 软件详情界面
 */
public class AppDetailActivity extends Activity implements OnClickListener
{
	private static final String TAG = "AppDetailActivity";
	public static final String EXTRA_SOURCE_COMPONENT_NAME = "source_component_name";
	
	//Intent EXTRAs
	public static final String EXTRA_APP_ID = "appid";
	public static final String EXTRA_AD_ID = "adid";
	public static final String EXTRA_AD_POSITION_ID = "ad_position_id";
	public static final String EXTRA_APP_DOWNLOADID_INT = "download_id";  //应用状态，下载中。需传下载id
	public static final String EXTRA_APP_PACKAGENAME = "package_name";  //包名，判断是否安装
	public static final String EXTRA_APP_VERSIONCODE_INT = "versioncode";  //应用版本信息
	public static final String CT = "ct";
	
	public static final int HANDLER_GETTED_DETAIL = 1;           //获取到了单个应用的详细数据
	public static final int HANDLER_SHOW_LOADING_DATA = 2; 		 //显示加载数据界面
	public static final int HANDLER_HIDDEN_LOADING_DATA = 3;	 //隐藏加载数据界面
	public static final int HANDLER_POST_COMMENT_SUCCESS = 4;	 //提交评论成功，重新加载评论
	public static final int HANDLER_DATA_FAILURE = 5;			 //加载失败
	
	private String packageName = null;
	//private int mVersioncode;
	private boolean bHaveUpdate = false;
	private boolean bHaveInstalled = false;
	
	protected TitleBar titlebar = null;
	private AppDetailTabbar tabBar;
	private LinearLayout container;
	private LinearLayout detailLayout;
	private LinearLayout commentLayout;
	
	private LinearLayout ll_loading_data_view;
	private RelativeLayout ll_activity_content;
	private RelativeLayout rl_no_records_view;
	//软件简介
	private ImageView baseinfo_icon;
	private TextView version;
	private TextView mark_times;
	private TextView time;
	private TextView size;
	private TextView downloadNum;
	private RatingBar marks;
	private Gallery  gallery_shot;
	private LinearLayout gallery_like;
	private TextView description;
	private TextView seeMore;
	private int pictureCount;
	//用户评论
	private RatingBar commentRating;
	private EditText nikeName;
	private EditText commentEt;
	private Button postComment;
	private LinearLayout commentList;
	
	//底端下载管理
	private Button btnDownload;
	private ImageView btnPause;
	private TextProgressBar textProgressBar;
	private ImageView btnCancel;
	
	
	private Resources rs;
	private Context mContext;
	
	private String appid;
	private String ct;
	
	private boolean mSeeMoreExpandFlag = false;  //软件描述是否展开的标志
	
	private AppDetails appDetails;
	
	private String fileName = "";
	
	private MogooDownloadManager mogooDownloadManager;
	private MarketApplication application = null;
	
	private ImageDownloader imageDownloader;
	private DownPrefsUtil mPrefsUtil = null;
	// 能否提交评论标志
	private boolean mIsAblePostComment = true;
	
	/** 所有下载管理中所有已下载的任务，key是apkid，value是downloadid*/
	private HashMap<String, String> mAllDownloadIds;
	
	/**
	 * 启动应用详情界面
	 */
	public static void launch(Context context,String appId,String packageName,int versionCode,int downloadId) 
	{
		Intent intent = new Intent();
		intent.setClass(context, AppDetailActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString(AppDetailActivity.EXTRA_APP_ID,appId); 
		bundle.putString(AppDetailActivity.EXTRA_APP_PACKAGENAME,packageName);
		bundle.putInt(AppDetailActivity.EXTRA_APP_VERSIONCODE_INT,versionCode);
		bundle.putInt(AppDetailActivity.EXTRA_APP_DOWNLOADID_INT,downloadId);
		intent.putExtras(bundle);
		context.startActivity(intent);
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_detail_activity);
        
        mContext = this;
        application = MarketApplication.getInstance();
        mogooDownloadManager = new MogooDownloadManager(mContext,mContext.getContentResolver(), mContext.getPackageName());
        imageDownloader = ImageDownloader.getInstance(mContext);
        //add by lcq
        mPrefsUtil = DownPrefsUtil.getInstance(application);
        
        rs = getResources();
        mIsAblePostComment = true;
        initViews();
        getIntents();
        
        //监听下载完成
        addDownloadCallBack();
    }
    
    @Override
    protected void onRestart(){
    	super.onRestart();
    	if(pictureCount>1){
    	  gallery_shot.setSelection(1);
    	}
    }
    
    @Override
    protected void onResume() 
    {
    	//安装界面返回
    	if(appDetails!=null && application.getInstalledAppPackages().containsKey(appDetails.getPackageame()))
    	{
    		if(application.getInstalledAppPackages().get(appDetails.getPackageame()).toString().equals(appDetails.getVersionCode()))
    		{
    			btnDownload.setText(R.string.open);
    		}
    	}
    	if(appDetails!=null){
    		setRecommentListData();
    	}
    	
		if(mDownLoadId>0)
		{
			String value = mPrefsUtil.getPrefsValue(""+mDownLoadId, "");
	    	String[] result = mPrefsUtil.parseResponse(value);
	    	if(result.length>1)  //有下载
	    	{
	    		showProgressView();
	    		if (TextUtils.equals("true", result[DownPrefsUtil.INFO_DOWN_LOAD_STATUS])) //下载中
	    		{
	    			btnPause.setImageResource(R.drawable.appdetail_pause);
				}
	    		else                                                                       //暂停
	    		{
	    			btnPause.setImageResource(R.drawable.appdetail_restart);
	    		}
	    		
				registerDownloadListener(mDownLoadId);
	    	}
		}
    	super.onResume();
    }
    
    @Override
	protected void onDestroy() 
	{
    	removeDownloadCallBack();
		super.onDestroy();
	}

    @Override
	public void finish() {
		Bundle bundle = this.getIntent().getExtras();
		String sourceName = "";
		if(bundle != null) {
			sourceName = bundle.getString(EXTRA_SOURCE_COMPONENT_NAME);
		}
		if(!TextUtils.isEmpty(sourceName)) {
			Intent intent = new Intent();
			intent.setClassName(this, sourceName);
			startActivity(intent);
		}
		super.finish();
		
	}
    
    /**
     * 下载完成监听接口
     */
    DownloadChangeCallback downloadChangeCallback = new DownloadChangeCallback() 
    {
		@Override
		public void downloadChanged(long id,int downloadStatus) 
		{
			if(mDownLoadId==id)
			{
				hiddenProgressView();
				textProgressBar.setProgress(0);
				
				if(downloadStatus==200) //下载完
            	{
					if(bHaveUpdate)
					{
						btnDownload.setText(R.string.app_update);
					}
					else
					{
						btnDownload.setText(R.string.install);
					}
            	}
				else
				{
					if(bHaveUpdate)
					{
						btnDownload.setText(R.string.app_update);
					}
					else
					{
						btnDownload.setText(R.string.download);
					}
					
					Log.e(TAG, "MogooMarket  download_failed or download_canceled  : "+downloadStatus);
				}
			}
		}

		@Override
		public void downloadListDataChanged() {
			// TODO Auto-generated method stub
			
		}
	};
    
	/**
     * 添加下载完成监听接口
     */
    private void addDownloadCallBack() 
    {
    	application.addDownloadCallback(downloadChangeCallback);
	}
    /**
     * 移除下载完成监听接口
     */
    private void removeDownloadCallBack() 
    {
    	application.removeDownloadCallback(downloadChangeCallback);
	}
    
    
    //*********************************** 初始化视图 ***********************************
    private void initViews() 
    {
    	ll_loading_data_view = (LinearLayout) findViewById(R.id.loading_data);
    	rl_no_records_view = (RelativeLayout) findViewById(R.id.rl_no_records);
    	rl_no_records_view.findViewById(R.id.tv_empty_text).setOnClickListener(this);
    	ll_activity_content = (RelativeLayout) findViewById(R.id.app_detail_activity_content);
    	ll_activity_content.setVisibility(View.INVISIBLE);
    	
    	tabBar = (AppDetailTabbar) findViewById(R.id.idAppDetailTabBar);
    	container = (LinearLayout) findViewById(R.id.idAppDetailContainer);
    	
    	LayoutInflater inflater = LayoutInflater.from(this);
    	detailLayout = (LinearLayout) inflater.inflate(R.layout.app_detail_layout, null);
    	commentLayout = (LinearLayout) inflater.inflate(R.layout.app_detail_comment_layout, null);
    	
    	initTitleBar();
    	
    	findViews();
	}
    
    private void findViews() 
    {
    	baseinfo_icon = (ImageView) detailLayout.findViewById(R.id.baseinfo_icon);
    	version = (TextView) detailLayout.findViewById(R.id.baseinfo_version);
    	mark_times = (TextView) detailLayout.findViewById(R.id.baseinfo_mark_times);
    	time = (TextView) detailLayout.findViewById(R.id.baseinfo_time);
    	size = (TextView) detailLayout.findViewById(R.id.baseinfo_size);
    	downloadNum = (TextView) detailLayout.findViewById(R.id.baseinfo_download_num);
    	marks = (RatingBar) detailLayout.findViewById(R.id.baseinfo_rating);
    	gallery_shot = (Gallery) detailLayout.findViewById(R.id.gallery_shot);
    	gallery_like = (LinearLayout) detailLayout.findViewById(R.id.gallery_like);
    	description = (TextView) detailLayout.findViewById(R.id.app_detail_description);
    	seeMore = (TextView) detailLayout.findViewById(R.id.app_detail_see_more_tv);
    	seeMore.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    	seeMore.getPaint().setAntiAlias(true);
    	
    	commentRating = (RatingBar) commentLayout.findViewById(R.id.app_detail_comment_rating);
    	commentRating.setIsIndicator(false);
    	nikeName = (EditText) commentLayout.findViewById(R.id.app_detail_nickname_et);
    	commentEt = (EditText) commentLayout.findViewById(R.id.app_detail_comment_et);
    	postComment = (Button) commentLayout.findViewById(R.id.app_detail_post);
    	commentList = (LinearLayout) commentLayout.findViewById(R.id.app_detail_comment_list);
    	
    	btnDownload = (Button) findViewById(R.id.idAppDetailDownloadBtn);
    	btnPause = (ImageView) findViewById(R.id.idAppDetailDownloadPause);
    	textProgressBar = (TextProgressBar) findViewById(R.id.idAppDetail_progress_bar);
    	btnCancel = (ImageView) findViewById(R.id.idAppDetailDownloadCancel);
    	
    	addTabs();
	}
    
    
    
    /**
	 * 初始化标题栏
	 */
	private void initTitleBar() 
	{
		titlebar = (TitleBar) findViewById(R.id.idTitlebar);
		if (titlebar != null) 
		{
			titlebar.setLeftBtnText(R.string.titlebar_back);

			titlebar.leftBtn.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					finish();
				}
			});
		}
	}
	
	/**
	 * 返回键，结束Activity
	 */
	@Override
	public void onBackPressed() 
	{
		super.onBackPressed();
		finish();
	}
	
	/**
	 * 添加切换卡
	 */
	private void addTabs() 
	{
		DetailTabMember detailMember = new DetailTabMember(rs.getString(R.string.app_detail_detail), detailLayout);
    	DetailTabMember commentMember = new DetailTabMember(rs.getString(R.string.app_detail_comment), commentLayout);
    	tabBar.setContainer(container);
    	tabBar.addTab(detailMember);
    	tabBar.addTab(commentMember);
	}
	
	/**
	 * 接收Intent信息，判断应用状态，及请求、获取应用详情
	 */
	private void getIntents() 
	{
		Bundle bundle = this.getIntent().getExtras();
		
		if(bundle!=null)
		{
			appid = bundle.getString(EXTRA_APP_ID);
			packageName = bundle.getString(EXTRA_APP_PACKAGENAME);
			mDownLoadId = bundle.getInt(EXTRA_APP_DOWNLOADID_INT);
			//mVersioncode = bundle.getInt(EXTRA_APP_VERSIONCODE_INT);
			ct= bundle.getString(CT);
			
			showDataLoadingView();  //显示加载进度条，圆圈
			
			final HashMap<String,Integer> installedPackages = MarketApplication.installedAppPackages;
			
			if(appid!=null && isNumeric(appid))   //正确数字appid
			{
				//请求并获取应用详情
				getApkInfo(appid);
				//加载评论列表
				loadCommentListData();
				
				hiddenProgressView();
				
				if(MarketApplication.isPackageInstalled(packageName))   //已安装
				{
					bHaveInstalled = true;
					if(MarketApplication.isHaveUpdate(packageName))  //有更新
					{
						bHaveUpdate = true;
						btnDownload.setText(R.string.app_update);
					}
					else   //没更新，打开
					{
						btnDownload.setText(R.string.open);
					}
				}
				else   //下载
				{
					btnDownload.setText(R.string.download);
				}
				
				if(mDownLoadId>0)
				{
					String value = mPrefsUtil.getPrefsValue(""+mDownLoadId, "");
			    	String[] result = mPrefsUtil.parseResponse(value);
			    	if(result.length>1)  //有下载
			    	{
			    		showProgressView();
			    		if (TextUtils.equals("true", result[DownPrefsUtil.INFO_DOWN_LOAD_STATUS])) //下载中
			    		{
			    			btnPause.setImageResource(R.drawable.appdetail_pause);
						}
			    		else                                                                       //暂停
			    		{
			    			btnPause.setImageResource(R.drawable.appdetail_restart);
			    		}
			    		
						registerDownloadListener(mDownLoadId);
			    	}
				}
			}
			else if(appid!=null && appid.contains("getAppAdvertise"))  //广告
			{
				String adid = bundle.getString(EXTRA_AD_ID);
				String adPositionId = bundle.getString(EXTRA_AD_POSITION_ID);
				
				
				//广告需要先请求获取广告的appid和packageName
				AppDetailsReq.onRequest(mContext, appid, adPositionId, adid, new onRequestCallBack() 
				{
					@Override
					public void onSuccess(Result result) 
					{
						AdAppDetails adAppDetail = (AdAppDetails) result.getData();
						appid = adAppDetail.getApkid();
						packageName = adAppDetail.getPackageName();
						
						Log.e(TAG, "##### AdAppDetailsReq.onRequest  onSuccess apkid = "+appid+"  packageName = "+packageName);
						if(appid!=null && appid.length()>0)
						{
							//请求并获取应用详情
							getApkInfo(appid);
							//加载评论列表
							loadCommentListData();
							
							if(packageName!=null && installedPackages!=null && installedPackages.containsKey(packageName))   //已安装
							{
								hiddenProgressView();
								btnDownload.setText(R.string.open);
							}
							else   //未安装
							{
								hiddenProgressView();
								btnDownload.setText(R.string.download);
							}
						}
						else
						{
							Message msg1 = new Message();
							msg1.what = HANDLER_HIDDEN_LOADING_DATA;
							mhandler.sendMessage(msg1);
							
							MyToast.makeText(mContext, rs.getString(R.string.app_detail_getdetail_fail), 1).show();
						}
					}
					
					@Override
					public void onFail(Result result) 
					{
						// TODO Auto-generated method stub
						Log.e(TAG, "##### AdAppDetailsReq.onRequest  onFail ");
						
						Message msg1 = new Message();
						msg1.what = HANDLER_HIDDEN_LOADING_DATA;
						mhandler.sendMessage(msg1);
						
						Message msg2 = new Message();
						msg2.what = HANDLER_DATA_FAILURE;
						mhandler.sendMessage(msg2);
						MyToast.makeText(mContext, rs.getString(R.string.app_detail_getdetail_fail), 1).show();
					}
				});
			}
			else  //其他错误的appid
			{
				MyToast.makeText(mContext, rs.getString(R.string.app_detail_error_apkid)+appid, 1).show();
			}
			
		}
	}
	
	/**
	 * 判断str是否是纯数字
	 */
	public static boolean isNumeric(String str)
	{
		Pattern p = Pattern.compile("[0-9]*");
		return p.matcher(str).matches();
	}
	
	/**
	 * 刷新界面的handler
	 * */
	Handler mhandler = new Handler() 
	{
		public void handleMessage(Message msg) 
		{
			switch (msg.what) 
			{
				case HANDLER_GETTED_DETAIL:   
				{
					rl_no_records_view.setVisibility(View.GONE);
					setData();
					
					//下载软件图片
					String iconUrl = appDetails.getIconUrl();
					if(iconUrl!=null)
					{
						imageDownloader.download(iconUrl, baseinfo_icon,
								BitmapFactory.decodeResource(getResources(), R.drawable.defautl_list_itme_pic_loading));
					}
					break;
				}
				case HANDLER_SHOW_LOADING_DATA:  
				{
					ll_loading_data_view.setVisibility(View.VISIBLE);
					break;
				}
				
				case HANDLER_HIDDEN_LOADING_DATA:  
				{
					ll_loading_data_view.setVisibility(View.GONE);
					break;
				}
				
				case HANDLER_POST_COMMENT_SUCCESS:  
				{
					//nikeName.setText("");
					commentEt.setText("");
					reGetCommentListData();
					break;
				}
				case HANDLER_DATA_FAILURE:
				{
					rl_no_records_view.setVisibility(View.VISIBLE);
					break;
				}
			}
		}
	};
	
	//*********************************** 联网获取数据 ***********************************
	/**
	 * 获取单个应用的详细数据
	 * */
	public void getApkInfo(String appid) 
	{
		HashMap<String, String> params = new HashMap<String, String>();
		params.put("apkid", appid);
		params.put("ct", ct);
		
		AppDetailsReq.onRequest(this, params, new onRequestCallBack() 
		{
			public void onSuccess(Result result) 
			{
				appDetails = (AppDetails) result.getData();

				Message msg = new Message();
				msg.what = HANDLER_GETTED_DETAIL;
				mhandler.sendMessage(msg);
			}

			public void onFail(Result result) 
			{
				Log.e(TAG, "##### AppDetailsReq.onRequest  onFail ");
				Message msg = new Message();
				msg.what = HANDLER_HIDDEN_LOADING_DATA;
				mhandler.sendMessage(msg);
				Message msg2 = new Message();
				msg2.what = HANDLER_DATA_FAILURE;
				mhandler.sendMessage(msg2);
			}
		});

	}
	
	/**
	 * 显示数据加载界面
	 */
	private void showDataLoadingView() 
	{
		if(appDetails!=null)
		{
			return;
		}
		
		//防止网络连接超时一直显示加载视图，10秒钟后都隐藏加载数据界面
		new Thread(new Runnable() 
		{
			@Override
			public void run() 
			{
				//显示加载数据界面
				Message msg = new Message();
				msg.what = HANDLER_SHOW_LOADING_DATA;
				mhandler.sendMessage(msg);
				
				try 
				{
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				//隐藏加载数据界面
				Message msg1 = new Message();
				msg1.what = HANDLER_HIDDEN_LOADING_DATA;
				mhandler.sendMessage(msg1);
				if (appDetails == null) {
					Message msg2 = new Message();
					msg2.what = HANDLER_DATA_FAILURE;
					mhandler.sendMessage(msg2);
				}
			}
		}).start();
	}
	
	private PaginateListView_1<Comments> commList;
	private ListFooterView mFooterView;
	private CommentAdapter<Comments> commAdapter;
	/**
	 * 获取评论列表数据
	 */
	private void loadCommentListData() 
	{
		commAdapter = new CommentAdapter<Comments>(this,new ArrayList<Comments>());
		
		commList = new PaginateListView_1<Comments>(this,commAdapter);
		commList.setXmlResultCallback(new Comments.CommentsListCallback());
		commList.setSelector(R.drawable.item_bgcolor);
		commList.setCacheColorHint(0);
		
		mFooterView = new ListFooterView(this, commAdapter);
		commList.addFooterView(mFooterView);
		commList.setAdapter(commAdapter);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.WRAP_CONTENT);
		commentList.addView(commList, params);
		addEmptyView(this, commList);
		
		if(commAdapter.getCount() == 0) 
		{
			Map<String, String> baseParamPair = HttpUrls.getBaseParamPair();
			baseParamPair.put("apkid", appid);
			commList.doFirsetQuery(HttpUrls.createBaseUrlWithExtendPairs(HttpUrls.URL_COMMENTS_LIST, baseParamPair));
		}
	}
	
	/**
	 * 重新加载评论列表
	 */
	private void reGetCommentListData() 
	{
		commAdapter.clear();
		Map<String, String> baseParamPair = HttpUrls.getBaseParamPair();
		baseParamPair.put("apkid", appid);
		commList.doFirsetQuery(HttpUrls.createBaseUrlWithExtendPairs(HttpUrls.URL_COMMENTS_LIST, baseParamPair));
	}
	
	/**
	 * 设置评论列表无数据时视图
	 */
	private void addEmptyView(Context context, PaginateListView_1<Comments> listview) 
	{
		CommonEmptyView emptyView = new CommonEmptyView(this, listview);
		emptyView.setEmptyText(rs.getString(R.string.no_comment));

		ViewGroup layout = (ViewGroup) listview.getParent();
		layout.addView(emptyView, new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		// 注意ListView要先设置了Adapter之后才能使用此方法设置CommonEmptyView
		listview.setEmptyView(emptyView);
	}
	
	/**
	 * 应用图片快照 数据
	 */
	private ArrayList<String> shotListData;
	private int shotPosition;
	/**
	 * 应用图片快照
	 */
	private void setShotData() 
	{
		shotListData = appDetails.getImageList();
		if(shotListData==null)
		{
			return;
		}
		
		pictureCount = shotListData.size();
		
		if(pictureCount>0)
		{
			Log.d("####","++++++size==="+size);
			detailLayout.findViewById(R.id.gallery_shot_parent).setVisibility(View.VISIBLE);
			Display display=this.getWindowManager().getDefaultDisplay();
			int width=display.getWidth();
			int height=display.getHeight();

			gallery_shot.setAdapter(new ImageShotAdapter(this,shotListData,width,height));
			if(pictureCount>1){
			gallery_shot.setSelection(1);
			}
			gallery_shot.setOnItemClickListener(new OnItemClickListener() {
				public void onItemClick(AdapterView<?>parent,View v,int position,long id){
					AppDetailActivity.this.shotPosition=position;
					Intent shotIntent=new Intent(mContext,GalleryShotActivity.class);
					shotIntent.putStringArrayListExtra("shoturls", shotListData);
					shotIntent.putExtra("shotpositon", AppDetailActivity.this.shotPosition);
					startActivity(shotIntent);
				}
			});
		}
	}
	
	/**
	 * 推荐应用列表 数据
	 */
	private ArrayList<RecommentAppInfo> recommentAppInfoList;
	/**
	 * 设置获取到的推荐列表数据
	 */
	private void setRecommentListData() 
	{
		recommentAppInfoList = appDetails.getRecommentAppList();
		if(recommentAppInfoList==null)
		{
			return;
		}
		mAllDownloadIds=getAllDownloadId();
		int size = recommentAppInfoList.size();
		if(size>0)
		{
			detailLayout.findViewById(R.id.gallery_like_parent).setVisibility(View.VISIBLE);
			gallery_like.removeAllViews();
			for(int i = 0 ; i < size ; ++i)
			{
				RecommentAppInfo info = recommentAppInfoList.get(i);
				int downloadId = 0;
				if (mAllDownloadIds.containsKey(info.getId())) 
				{
					downloadId = Integer.valueOf(mAllDownloadIds.get(info.getId()));
				}
				gallery_like.addView(AppDetailLikeItemFactory.create(this,info.getIconUrl(),info.getName(),info.getId(),
						info.getPackageName(),downloadId));
			}
		}
	}
	
	/**
	 * 给相对应的组件添加数据
	 * */
	public void setData() 
	{
		fileName = appDetails.getName() + "_" + appDetails.getVersionCode()	+ ".apk";
		fileName.replace(" ", "");
		
		ll_activity_content.setVisibility(View.VISIBLE);
		Message msg1 = new Message();
		msg1.what = HANDLER_HIDDEN_LOADING_DATA;
		mhandler.sendMessage(msg1);
		
		titlebar.midTextView.setText(appDetails.getName());
    	version.setText(rs.getString(R.string.version)+" "+appDetails.getVersionStr()+rs.getString(R.string.version1));
    	mark_times.setText(rs.getString(R.string.mark)+" "+appDetails.getScoreTimes()+rs.getString(R.string.st_ci));//???
    	time.setText(rs.getString(R.string.time)+" "+appDetails.getTime());
    	size.setText(rs.getString(R.string.size)+" "+ToolsUtils.getSizeStr(appDetails.getSize()));
    	downloadNum.setText(appDetails.getDownloadTime()+rs.getString(R.string.download_num));//???
    	String rating = appDetails.getRatingScore();
    	if(rating!=null)
    	{
    		marks.setRating(Float.valueOf(rating));
    	}
    	String des = appDetails.getDescription();
    	if(des==null || des.equals("null"))
    	{
    		description.setText(R.string.nothing);
    	}
    	else
    	{
    		description.setText(des);
    	}
    	
    	//如果Intent没传下载Id,就是没有正在下载，可能是下载过应该显示安装，所以获取到软件信息后得检查一下
    	if( !bHaveInstalled && ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, fileName))
    	{
    			 btnDownload.setText(R.string.install);
    	}
    	
    	// 软件快照
    	setShotData();
		
		//推荐应用
		setRecommentListData();
    	
    	//添加点击事件
    	setListener();
	}
	
	/**
	 * 获取apk完整路径
	 * @param fileName
	 * @return
	 */
	private String getFullApkPath(String fileName) 
	{
		String rootPath = Environment.getExternalStorageDirectory().getAbsolutePath();
		return rootPath+"/"+MarketApplication.SavaPath+fileName;
	}
	
	/**
	 * 发表评论
	 */
	private void postComment() 
	{
		if(!mIsAblePostComment) {
			MyToast.makeText(mContext, getResources().getText(R.string.app_detail_post_comment_redo), 
					Toast.LENGTH_SHORT).show();
			return;
		}
		
		mIsAblePostComment = false;
		String name = nikeName.getText().toString();
		if(name==null)
		{
			name = "";
		}
		String content = commentEt.getText().toString();
		float rating = commentRating.getRating();
		
		if(content==null || content.length() < 1)
		{
			MyToast.makeText(mContext, R.string.app_detail_input_content, 0).show();
		}
		else
		{
			hiddenInput(commentEt);
			
			if (content != null && content.length() > 0) 
			{
				int len = 8;
				if (content.length() > 8) 
				{
					len = 8;
				}
				else 
				{
					len = content.length();
				}
				String title = content.substring(0, len);
				
				
				Comments comments = new Comments();
				comments.setApkId(appid);
				comments.setTitle(title);
				comments.setVersion(appDetails.getVersionStr());
				comments.setName(name);
				comments.setRating(rating + "");
				comments.setDate(DateUtils.getDateStrYYYY_MM_DD());
				comments.setComment(content);
				
				PostCommentsReq.onSubmitComment(mContext, comments,new onRequestCallBack() 
				{
					@Override
					public void onSuccess(Result result) 
					{
						mIsAblePostComment = true;
						mhandler.sendEmptyMessage(HANDLER_POST_COMMENT_SUCCESS);
						MyToast.makeText(mContext, getResources().getText(R.string.app_detail_post_comment_success), 
								Toast.LENGTH_SHORT).show();
					}

					@Override
					public void onFail(Result result) 
					{
						mIsAblePostComment = true;
						MyToast.makeText(mContext, getResources().getText(R.string.app_detail_post_comment_fail),
								Toast.LENGTH_SHORT).show();
					}
				});
			}
		}
	}
	
	/**
	 * 隐藏软键盘
	 */
	public void hiddenInput(EditText t)
	{
		InputMethodManager imm=(InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(t.getWindowToken(),0);
	}
	
	//*********************************** 添加点击事件 ***********************************
	/**
	 * 添加点击事件
	 * */
	public void setListener() 
	{
		seeMore.setOnClickListener(this);
		btnDownload.setOnClickListener(this);
		btnPause.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		postComment.setOnClickListener(this);
	}
	
	public void onClick(View v) 
	{
		int id = v.getId();
		switch (id) 
		{
		case R.id.app_detail_see_more_tv:  //查看更多详情
			int lines = mSeeMoreExpandFlag ? 5 : 50;
			description.setMaxLines(lines);
			int text = mSeeMoreExpandFlag ? R.string.app_detail_see_more
					: R.string.app_detail_see_more_col;
			seeMore.setText(text);
			if(!mSeeMoreExpandFlag)
			{
				seeMore.setGravity(Gravity.RIGHT);
			}
			mSeeMoreExpandFlag = !mSeeMoreExpandFlag;
			break;
			
		case R.id.idAppDetailDownloadBtn:  //下载按钮
			if(btnDownload.getText().equals(rs.getString(R.string.download)))   //下载
			{
				if(checkAndDownload())
				{
					setRecommentListData();
					break;
				}
			}
			else if(btnDownload.getText().equals(rs.getString(R.string.open)))  //打开
			{
				if(appDetails.getPackageame().length()>0)
				{
					try 
					{
						PackageManager pm = mContext.getPackageManager();
						mContext.startActivity(pm.getLaunchIntentForPackage(appDetails.getPackageame()));
					} 
					catch (Exception e) 
					{
						MyToast.makeText(mContext, R.string.open_failed, 0).show();
						if(appDetails.getPackageame()!=null)
						{
							Log.e(TAG, "##### open "+appDetails.getPackageame()+" failed");
						}
					}
				}
			}
			else if(btnDownload.getText().equals(rs.getString(R.string.app_update)))   //更新
			{
				//已经下载更新文件
				if(fileName!=null && fileName.length()>0 && ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, fileName))
				{
					ToolsUtils.onInstallApk(mContext, getFullApkPath(fileName));
				}
				else
				{
					if(checkAndDownload())
					{
						break;
					}
				}
			}
			else   //安装
			{
				if(fileName!=null && fileName.length()>0 && ToolsUtils.isDownloadedAPk(mContext, MarketApplication.SavaPath, fileName))
				{
					ToolsUtils.onInstallApk(mContext, getFullApkPath(fileName));
				}
			}
			
			hiddenProgressView();
			break;
			
		case R.id.idAppDetailDownloadPause:  //暂停下载按钮
			MogooDownloadInfo info = mogooDownloadManager.getDownloadInfo(""+mDownLoadId);
			if(info!=null)
			{
				if(info.getControl()==0)
				{
					btnPause.setImageResource(R.drawable.appdetail_restart);
					ToolsUtils.pauseDownload(mContext, mogooDownloadManager, String.valueOf(mDownLoadId), appid);
				}
				else
				{
					btnPause.setImageResource(R.drawable.appdetail_pause);
					ToolsUtils.restartDownload(mContext, mogooDownloadManager, String.valueOf(mDownLoadId), appid);
				}
			}
			break;
			
		case R.id.idAppDetailDownloadCancel:  //取消下载按钮
			ToolsUtils.cancelDownload(mContext, mogooDownloadManager, Long.valueOf(mDownLoadId));
			// lxr add 取消或下载同时删除正在更新列表中的项.
			String pkgName = (packageName != null) ? packageName : "";
			UpdatesUtils.delete(mContext, pkgName);
			btnPause.setImageResource(R.drawable.appdetail_pause);
			if(MarketApplication.isHaveUpdate(packageName))  //有更新
			{
				bHaveUpdate = true;
				btnDownload.setText(R.string.app_update);
			}
			else{
				btnDownload.setText(R.string.download);
			}
			
			hiddenProgressView();
			break;
			
		case R.id.app_detail_post:  //发表评论按钮
			postComment();
			break;
		case R.id.tv_empty_text:
			rl_no_records_view.setVisibility(View.GONE);
			getIntents();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 显示下载进度
	 */
	private void showProgressView() 
	{
		btnDownload.setVisibility(View.INVISIBLE);
		
		btnPause.setVisibility(View.VISIBLE);
		btnCancel.setVisibility(View.VISIBLE);
		textProgressBar.setVisibility(View.VISIBLE);
	}
	
	/**
	 * 隐藏下载进度
	 */
	private void hiddenProgressView() 
	{
		btnDownload.setVisibility(View.VISIBLE);
		
		btnPause.setVisibility(View.INVISIBLE);
		btnCancel.setVisibility(View.INVISIBLE);
		textProgressBar.setVisibility(View.INVISIBLE);
	}
	
	
	//***************************************** 下载 **********************************************
	private long mDownLoadId;
	//private MogooDownloadInfo downloadInfo;
	
	/**
	 * 检查网络、SD卡，再下载
	 */
	private boolean checkAndDownload() 
	{
		String size=appDetails.getSize();
		if(ToolsUtils.checkBeforeDownload(mContext, Long.valueOf((size!=null&&!size.equals(""))?size:"0")))
		{
			if (downLoad() > 0) 
			{
				showProgressView();
				registerDownloadListener(mDownLoadId);
				//add by lcq:2012-6-19
				mPrefsUtil.savePresValue(String.valueOf(mDownLoadId), String.valueOf(appDetails.getAppId()), String.valueOf(true));
				return true;
			}
		}
		return false;
	}
	/**
	 * 下载
	 * */
	public long downLoad() 
	{
		mDownLoadId = 0;
		mDownLoadId = mogooDownload(appDetails.getAppId(), MarketApplication.SavaPath, fileName);
		return mDownLoadId;
	}
	
	/**
	 * 调用mogoo下载组件进行下载
	 */
	public long mogooDownload(String apkId, String savePath, String apkName) 
	{
		String url = appDetails.getApkDownUrl();
		boolean insertFlat=true;
		mDownLoadId = ToolsUtils.downloadApk(mogooDownloadManager, MarketApplication.SavaPath, apkName,url);
		for(DownloadInfo downloadInfo : MarketApplication.myDownloadAppList){
			if(downloadInfo.getApp_id().equals(appDetails.getAppId())){
				insertFlat=false;
				break;
			}else{
				insertFlat=true;
			}
				
		}
		//插入下载数据信息到数据库
		if(insertFlat){
		DaoFactory.getDownloadInfoDao(mContext).addBean(new DownloadInfo(String.valueOf(mDownLoadId), apkId, 
				url, savePath, apkName, appDetails.getSize(), appDetails.getIconUrl(), appDetails.getRatingScore(),
				appDetails.getVersionStr(), Integer.valueOf(appDetails.getVersionCode()), appDetails.getPackageame()));
		}
		//如果是更新，则保存包名和下载id到UpdatesUtils
		String pn = appDetails.getPackageame();
		if(MarketApplication.installedAppPackages.containsKey(pn)) 
		{
			UpdatesUtils.save(mContext, pn, mDownLoadId + "");
		}
		
		return mDownLoadId;
	}
	
	 
	/**
	 * 设置下载监听器，监听下载进度
	 */
	private void registerDownloadListener(final long downloadId) 
	{
		DownloadProgressListener downListener = new DownloadProgressListener() 
		{
			@Override
			public void onDownloadProgressChanged(int progress) 
			{
				if(progress<100 && textProgressBar.getVisibility()!=View.VISIBLE)
				{
					showProgressView();
				}
				
				if (progress >= 0 && progress <= 100) 
				{
					textProgressBar.setProgress(progress);
				} 
			}

			@Override
			public long getDownloadId() 
			{
				return downloadId;
			}
		};

		DownloadProgressManager.getInstance(mContext).registerProgressListener(downListener);
	}
	
	/**
	 * @return 所有的下载id的集合
	 */
	public HashMap<String, String> getAllDownloadId() {
		return DaoFactory.getDownloadInfoDao(mContext).getAllDownloadId(mContext);
		//return new HashMap<String, String>();
	}
}
