package com.mogoo.components.download;
/*
 * 下载进度条视图类.
 * 用于显示下载任务的下载情况. 
 * 
 */
import java.util.HashMap;

import android.content.Context;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.imogoo.providers.downloads.Downloads;

public class MogooDownloadView extends RelativeLayout implements OnClickListener {
    
    /**
     * 下载完毕后发出的广播(不一定是下载成功)
     */
    public final static String ACTION_DOWNLOAD_COMPLETE = "android.intent.action.DOWNLOAD_COMPLETE";
    

	private Context mContext;
	private MogooDownloadManager mogooDownloadManager = null; 
	private ProgressBar progressBar;
	private TextView progressTextView;
	private int downloadStatus = 0; 
	private MogooDownImageView imageView;
	private final static int IMAGE_PADDING = 5;

	public static final int BUTTON_TYPE_STOP = 0;
	private long _id;

	private final static int IMAGE_ID = 0xff0001;
	private final static int BUTTON_ID = 0xff0002;
	private final static String tag = "MogooDownloadView";
	private final static int DEFUALT_BACKGROUP = 0xff000000;
	
	private long totalSize;
	private boolean isFinish = false;
	private boolean isObserver = false;
	private static HashMap<Long, MogooDownloadView> mDownloadViews = new HashMap<Long , MogooDownloadView>(); 

	/**
	 * 新建下载进度条. 未绑定任何下载任务.
	 * 
	 * @param context
	 */
	public MogooDownloadView(Context context) {
		super(context);
		mContext = context;
		mogooDownloadManager = new MogooDownloadManager(mContext);
		initView();
		this.setOnClickListener(this);
	}
	
	/**
	 *  新建下载进度条. 绑定下载任务 downloadId.
	 *  用于查看downloadId 下载情况.
	 * 
	 * @param context
	 * @param downloadId
	 */
	public MogooDownloadView(Context context, long downloadId){
	    this(context);
	    if (!isObserver){
            DownloadProgressManager.getInstance(mContext).registerObserver(downloadId, MogooDownloadView.this);
            isObserver = true;
        }
	    this._id = downloadId;
	    downloadStatus = 1;
	    
	  //更新进度
	  refreshProgressFromDb();
	}

	public MogooDownloadView(Context context, AttributeSet attr) {
		this(context);
		initAttribute(context, attr);
	}

	private void initAttribute(Context context, AttributeSet attr) {
	}
	
	/**
	 * add by lcq:2012-5-30 
	 * to cache the view
	 */
	public static MogooDownloadView getViewFromFactory(Context context, long downloadId) {
//System.out.println("lcq:MogooDownloadView:get view from cache:"+mDownloadViews.containsKey(downloadId));
		if(mDownloadViews.containsKey(downloadId))
			return mDownloadViews.get(downloadId);
		else {
			MogooDownloadView view = new MogooDownloadView(context, downloadId);
			mDownloadViews.put(downloadId, view);
			return view;
		}
	}
	
	/**
	 * 开始下载任务
	 * 下载路径 保存文件路径 包名，请在开始下载前初始化这些数据
	 * 
	 * @param url    远程文件地址
	 * @param savePath   保存文件路径 如SD卡地址.
	 * @param fileName   保存文件名.
	 */
	public long startDownload(String url, String savePath, String fileName) {
		// this.setVisibility(View.VISIBLE);
		_id = mogooDownload(url, savePath, fileName);
		if (!isObserver){
		    DownloadProgressManager.getInstance(mContext).registerObserver(_id, MogooDownloadView.this);
		    isObserver = true;
		}
		
		downloadStatus = 1;
		return _id;
	}
	
	/**
	 * 暂停下载
	 * @param downloadId   下载ID
	 */
	public void pauseDownload(long... downloadId){
	    if (downloadId == null || downloadId.length == 0) {
            return;
        }
	    mogooDownloadManager.pauseDownload(downloadId);
	    downloadStatus = 2;
	}
	
	/**
	 * 继续下载
	 * @param downloadId  下载ID
	 */
	public void restartDownload(long... downloadId){
	    if (downloadId == null || downloadId.length == 0) {
            return;
        }
	    mogooDownloadManager.restartDownload(downloadId);
	    downloadStatus = 1;
	}
	
	/**
	 * 取消下载
	 * @param downloadId  下载ID
	 * @return
	 */
	public long cancelDownload(long... downloadId) {
        if (downloadId == null || downloadId.length == 0) {
            return 0;
        }
        downloadStatus = 3;
        DownloadProgressManager.getInstance(mContext).cancelDownload(downloadId); 
        
        return mContext.getContentResolver().delete(Downloads.Impl.CONTENT_URI,
                getWhereClauseForIds(downloadId),
                getWhereArgsForIds(downloadId));
    }

	
	/**
	 * 是否正在下载
	 * @return
	 */
	public boolean isDownloading(){
	    return downloadStatus == 1? true : false;
	}
	
	/**
	 * 返回文件总大小
	 * @return
	 */
	public long getTotalSize(){
	    return totalSize;
	}
	
	/**
	 * 文件是否下载完成
	 * @return
	 */
	public boolean isDownloadFinish(){
	    return isFinish;
	}
	
	public void refreshProgress(int progress){
	    MogooDownloadView.this.setProgressBar(progress);
        MogooDownloadView.this.progressTextView.setText(progress + "%");
	}
	
	/*
	 * 从数据库更新进度
	 */
	public void refreshProgressFromDb()
	{
		MogooDownloadInfo info = getDownloadInfo(this._id+"");
		if(info!=null && info.getDownloadTotolBytes()>0)
		{
			int progress = (int)(info.getDownloadCurrentBytes()*100/info.getDownloadTotolBytes());
		    refreshProgress(progress);
		}
	}

	private long mogooDownload(String url, String savePath, String fileName) {
		Log.i("mogooDownload", "mogooDownload()...");
		Log.i("mogooDownload", "url:" + url);
		long downloadId = -1;

		// 创建下载目录(在SDCARD中)
		Environment.getExternalStoragePublicDirectory(savePath).mkdir();
		// 保存到SDCARD的name目录中，并把保存的文件名设为rrr.mp3
		mogooDownloadManager.setDestinationInExternalPublicDir(savePath,fileName);
		mogooDownloadManager.setTitle(fileName);
		mogooDownloadManager.setmPackageName(fileName);
		// mogooDownloadManager.setDescription("一些描述信息");
		// mogooDownloadManager.setAllowedOverRoaming(false);
		// mogooDownloadManager.setVisibleInDownloadsUi(false);
		// mogooDownloadManager.setShowRunningNotification(false);
		// 开始下载
		downloadId = mogooDownloadManager.startDownload(url);
		return downloadId;
	}

	private void initView() {

		this.setBackgroundColor(DEFUALT_BACKGROUP);
		imageView = new MogooDownImageView(mContext);
		imageView.setScaleType(ScaleType.FIT_CENTER);
		imageView.setPadding(IMAGE_PADDING, IMAGE_PADDING, IMAGE_PADDING,
				IMAGE_PADDING);
		imageView.setVisibility(View.INVISIBLE);
		imageView.setId(IMAGE_ID);

		LinearLayout rcontentLayout = new LinearLayout(mContext);
		rcontentLayout.setId(BUTTON_ID);
		ViewGroup.LayoutParams rcontentlayoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		rcontentLayout.setLayoutParams(rcontentlayoutParams);

//		controlBtn = new Button(mContext);
//		controlBtn.setText("cancle");
//		initControlBtn(BUTTON_TYPE_STOP);
//		rcontentLayout.addView(controlBtn);

		ViewGroup.LayoutParams parentlayoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		this.setLayoutParams(parentlayoutParams);

		LinearLayout lcontentLayout = new LinearLayout(mContext);

		ViewGroup.LayoutParams lcontentlayoutParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		lcontentLayout.setLayoutParams(lcontentlayoutParams);
		lcontentLayout.setOrientation(LinearLayout.VERTICAL);

		progressBar = new ProgressBar(mContext, null,
				android.R.attr.progressBarStyleHorizontal);
		progressTextView = new TextView(mContext);

		RelativeLayout relayout = new RelativeLayout(mContext);

		RelativeLayout.LayoutParams lRelParams2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lRelParams2.addRule(RelativeLayout.CENTER_IN_PARENT);
		relayout.addView(progressTextView, lRelParams2);

		ViewGroup.LayoutParams progressParams = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.FILL_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		lRelParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		lcontentLayout.addView(progressBar, progressParams);
		lcontentLayout.addView(relayout);

		RelativeLayout.LayoutParams lrParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);

		lrParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		this.addView(imageView, lrParams);

		RelativeLayout.LayoutParams lrParams2 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lrParams2.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

		this.addView(rcontentLayout, lrParams2);

		RelativeLayout.LayoutParams lRelParams3 = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.FILL_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		lRelParams3.addRule(RelativeLayout.RIGHT_OF, IMAGE_ID);
		lRelParams3.addRule(RelativeLayout.LEFT_OF, BUTTON_ID);

		this.addView(lcontentLayout, lRelParams3);

	}


	private void setProgressBar(int progress) {
		progressBar.setProgress(progress);
	}

	// 设置组件背景颜色
	public void setBackgroupColor(int color) {
		this.setBackgroundColor(color);
	}


	/**
	 * 设置下载路径
	 * 
	 * @param url
	 */
	public void setImageUrl(String url) {
		imageView.setVisibility(View.VISIBLE);
		imageView.setImageUrl(url);
	}

	private static String getWhereClauseForIds(long[] ids) {
		StringBuilder whereClause = new StringBuilder();
		whereClause.append("(");
		for (int i = 0; i < ids.length; i++) {
			if (i > 0) {
				whereClause.append("OR ");
			}
			whereClause.append(Downloads.Impl._ID);
			whereClause.append(" = ? ");
		}
		whereClause.append(")");
		return whereClause.toString();
	}

	private static String[] getWhereArgsForIds(long[] ids) {
		String[] whereArgs = new String[ids.length];
		for (int i = 0; i < ids.length; i++) {
			whereArgs[i] = Long.toString(ids[i]);
		}
		return whereArgs;
	}

    @Override
    public void onClick(View v) {
        if (downloadStatus == 1){
            pauseDownload(_id);
        }else if (downloadStatus == 2){
            restartDownload(_id);
        }
    }
    
    /**
	 * 获取downloadId的下载地址
	 */
	public MogooDownloadInfo getDownloadInfo(String downloadId)
	{
		return mogooDownloadManager.getDownloadInfo(downloadId);
	}

}