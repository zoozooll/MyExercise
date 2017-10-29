package com.dvr.android.dvr.mshowplayback;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import com.dvr.android.dvr.Config;
import com.dvr.android.dvr.msetting.MyImageButton;
import com.dvr.android.dvr.playback.PlayBackActivity;
import com.dvr.android.dvr.playback.PlayBacker;
import com.dvr.android.dvr.util.ImageManager;
import com.dvr.android.dvr.util.SDcardManager;
import com.dvr.android.dvr.util.VideoManager;
import com.dvr.android.dvr.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;//liujie add 1009
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ListView;
//import android.widget.Toast;
import android.widget.TextView;

/**
 * 这是�?�� �?视频�?�?“图片�? �?activity
 * 操作�?单击事件  �?  长按事件 
 * 
 * */
public class ShowPActivity extends Activity implements OnImageLoadListener {
    
    private MyImageButton b_v =null;
    private MyImageButton b_p =null;
    private MyImageButton b_v_lock =null;
    ProgressDialog pd =null;
    private int curMediaType = 1;
	private ListView listView = null;
	
	private LayoutInflater inflater = null;
    private View layout = null;

	public DetectSDCardMount     mSDCardMountReceiver;
	
    public static final int GET_DIR_SUCCESS = 0;
    public static final int GET_IMAGE_SECCESS = 1;
    public static final int DELETE_FILE_SECCESS = 3;
    public static final int GET_PLAY_LIST = 4;
    public static final int GET_PLAY_LIST_WAITING = 5;
    public static final int GET_PLAY_LIST_FINISH = 6;
    enum GL_STATUS{
    	GL_STATUS_IDLE, GL_STATUS_START, GL_STATUS_DEALING
    };
    
    private GL_STATUS mGetListStatus = GL_STATUS.GL_STATUS_IDLE;

    public static boolean isDetailFace = false;
    /**
     * 默认视频图片
     */
    private Bitmap defaultVideoImage = null;
    /**
     * 默认图库图片
     */
    private Bitmap defaultImageImage = null;
    private boolean mIsRoot = true;
    private boolean mIsVideoCameraRoot = false; //liujie add 1009
    
    private boolean mUpdateSaveDataSheet = false;
    
    private ContentResolver mContentResolver = null;

    TextView mEmptyText = null;
    /**
     * window �?gridview视图 �?grid
     */
    private ArrayList<FileDes> superFileDess = new ArrayList<FileDes>();
    private ArrayList<FileDes> mDirDess = new ArrayList<FileDes>();
    private ArrayList<FileDes> mCurrentDess = new ArrayList<FileDes>();
    private FileDes mCurrentFile;
    public FileDes fileInfo;
    
    Hashtable<String, FileDes> table = new Hashtable<String, FileDes>();
    ArrayList<String> keys = new ArrayList<String>();
    /**
     * 图片获取工具
     */
    private ImageTool imageTool = null;
    
    private int mnOpenType = 1;
    
    public boolean mbHaveGetlist = false;
    
  //the waiting deleting progress dialog
    ProgressDialog waitingProg = null;

    /**
     * grid的数据�?配器
     */
    private FileListAdapter adapter = null;
    //private int mType = MediaTool.MEDIA_VIDEO_TYPE;
    private final DecimalFormat sizeFormat = new DecimalFormat("0.#####");
//    private String currenDir = null;

    public final static String KEY_MEDIA_TYPE = "media_type"; 
    private final String TAG = "ShowPActivity";
    public static int mType = MediaTool.MEDIA_VIDEO_TYPE;
    public static boolean mbVideoLock = false;
    private byte[] lock = new byte[0]; //特殊的instance变量
    
    private long mFirstEnterAppTime = 0; // 进入app的时�?
    
    private void showSetTypeButton()
    {
    	if(mType == MediaTool.MEDIA_VIDEO_TYPE)
	    {
    		if(mbVideoLock)
    		{
    			b_v.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_normal));
    			b_v_lock.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_pressed)); 
    	    	b_p.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_pic_normal)); 
    		}
    		else
    		{
    			b_v.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_pressed)); 
    			b_v_lock.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_normal)); 
    	    	b_p.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_pic_normal)); 
    		}
	    }
	    else if(mType == MediaTool.MEDIA_IMAGE_TYPE)
	    {
	    	b_v.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_normal)); 
			b_v_lock.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_video_normal)); 
	    	b_p.setImageDrawable(getResources().getDrawable(R.drawable.btn_showplayback_pic_pressed)); 
	    }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       
       Log.e(TAG, "***onCreate***");
       
       Config.gbEnteredPlayList = true;
       
       mFirstEnterAppTime = System.currentTimeMillis();
       
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       setContentView(R.layout.selectpictureorvideo);
       
       //mType = MediaTool.MEDIA_VIDEO_TYPE;
       //mbVideoLock = false;
       
       b_v=(MyImageButton) findViewById(R.id.video_button_id);
       b_v.setOnClickListener(clickListener);
       /*b_v.setOnLongClickListener(new OnLongClickListener(){
	        public boolean onLongClick(View arg0){
	        	if(mIsRoot == true)
	        	{
	        		curMediaType =MediaTool.MEDIA_VIDEO_TYPE;
		            showTipDialog();
	        	}
	            return true;
	        }
	   });*/
       
       b_v_lock=(MyImageButton) findViewById(R.id.video_lock_button_id);
       b_v_lock.setOnClickListener(clickListener);
	       
	    b_p=(MyImageButton) findViewById(R.id.picture_button_id);
	    b_p.setOnClickListener(clickListener);
	    /*b_p.setOnLongClickListener(new OnLongClickListener(){
	        
	        public boolean onLongClick(View arg0)
	        {
	        	if(mIsRoot == true)
	        	{
	        		curMediaType =MediaTool.MEDIA_IMAGE_TYPE;
		            showTipDialog();
	        	}
	            return true;
	        }
	    });*/
	                     
	    
	    b_v.setText(getResources().getString(R.string.playback_video_unlock));
	    b_v.setTextSize(getResources().getDimension(R.dimen.playlist_btn_text_size));
	    b_v.setColor(Color.WHITE);
	    b_v.setOffsetX(getResources().getDimension(R.dimen.playlist_video_x));
	    b_v.setOffsetY(getResources().getDimension(R.dimen.playlist_video_y));
	    
	    b_v_lock.setText(getResources().getString(R.string.playback_video_lock));
	    b_v_lock.setTextSize(getResources().getDimension(R.dimen.playlist_btn_text_size));
	    b_v_lock.setColor(Color.WHITE);
	    b_v_lock.setOffsetX(getResources().getDimension(R.dimen.playlist_video_x));
	    b_v_lock.setOffsetY(getResources().getDimension(R.dimen.playlist_video_y));
	    
	    b_p.setText(getResources().getString(R.string.playback_pic));
	    b_p.setTextSize(getResources().getDimension(R.dimen.playlist_btn_text_size));
	    b_p.setColor(Color.WHITE);
	    b_p.setOffsetX(getResources().getDimension(R.dimen.playlist_pic_x));
	    b_p.setOffsetY(getResources().getDimension(R.dimen.playlist_pic_y));
	    showSetTypeButton();
	    startGetListThread();
        mHandler.sendEmptyMessage(GET_PLAY_LIST);
	    //initListView();
        
      //监听SD卡插�?
		mSDCardMountReceiver = new DetectSDCardMount();
        IntentFilter filterSDCardMount = new IntentFilter();
        filterSDCardMount.addAction(Intent.ACTION_MEDIA_MOUNTED);  
        filterSDCardMount.addAction(Intent.ACTION_MEDIA_UNMOUNTED);           
        filterSDCardMount.addDataScheme("file"); 
        this.registerReceiver(mSDCardMountReceiver, filterSDCardMount);
    }
    
    public class DetectSDCardMount extends BroadcastReceiver{
    	public String TAG = "DetectSDCardMount";
    	@Override
    	public void onReceive(Context context, Intent intent) {
    		// TODO Auto-generated method stub
    		String action = intent.getAction(); 
            if((Intent.ACTION_MEDIA_MOUNTED).equals(action))
            { 
            	SwitchViewTypePrev();
			    mEmptyText.setVisibility(View.INVISIBLE);
                mHandler.sendEmptyMessage(GET_PLAY_LIST);           	
            }
            else if((Intent.ACTION_MEDIA_UNMOUNTED).equals(action))
            {
            	SwitchViewTypePrev();
			    superFileDess.clear();
	        	mEmptyText.setVisibility(View.VISIBLE);
	            mHandler.sendEmptyMessage(GET_PLAY_LIST_FINISH);
            }
    	}
    }

	private void initListView()
    {
        listView = (ListView) this.findViewById(R.id.list_by_date);
        mEmptyText = (TextView) findViewById(R.id.filesempty);
        listView.setVisibility(View.INVISIBLE);
     // 读取默认图片
        if(defaultVideoImage == null)
        {
        	defaultVideoImage = BitmapFactory.decodeResource(getResources(), R.drawable.video_icon);
        }
        if(defaultImageImage == null)
        {
            defaultImageImage = BitmapFactory.decodeResource(getResources(), R.drawable.image_icon);
        }
        
        if(adapter == null)
        {
        	adapter = new FileListAdapter(this);
            //adapter.setFiles(superFileDess);
            listView.setAdapter(adapter);
        }
        
        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(); 

        listMedia(mType);		      
    }
	
	private HandlerThread mGetListThread;
    private GetListHandler mGetListHandler;

    private static final int GL_START = 34;
    private static final int GL_DEALING = 35;
    private static final int GL_FINISH = 36;
    class GetListHandler extends Handler
    {

        public GetListHandler(Looper looper)
        {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg)
        {
        	if (!mCanStartGetList) {
        		return;
        	}
            switch (msg.what)
            {
                case GL_START:
                	//hanJ add
                    break;
                case GL_DEALING:
                    dealItems();
                    mHandler.sendEmptyMessage(GET_PLAY_LIST_FINISH);
                	break;
            }
        }
    }
	
	private boolean mCanStartGetList;
    
	private void startGetListThread()
	{
	        //hanJ notify
		 	if (mGetListThread == null)
	        {
		 		mGetListThread = new HandlerThread("get_playlist");
		 		mGetListThread.start();
		    }
	        if (mGetListHandler == null)
	        {
	        	mGetListHandler = new GetListHandler(mGetListThread.getLooper());
	        }
	        //hanJ add
	        mCanStartGetList = true;
	}

	 private void stopCheckThread()
	 {
   	//hanJ notify
		 mCanStartGetList = false;
		 if(mGetListThread != null){
	   		if (mGetListHandler.hasMessages(GL_START)) {
	   			mGetListHandler.removeMessages(GL_START);
	       	}
	   		else if (mGetListHandler.hasMessages(GL_DEALING)) {
	   			mGetListHandler.removeMessages(GL_DEALING);
	       	}
	   		else if (mGetListHandler.hasMessages(GL_FINISH)) {
	   			mGetListHandler.removeMessages(GL_FINISH);
	       	}
		 }
	 }
    
    /**
     * 文件删除对话�?
     */
    private static final int FILEs_DELETE_DIALOG = 1;
    private static final int PROGRESS_DIALOG = 2;
    /**
     * 删除文件对话�?
     */
    private void showTipDialog(){
        showDialog(FILEs_DELETE_DIALOG);
    }
    
    @Override
    protected void onPrepareDialog(int id, Dialog dialog, Bundle args){
        switch (id){
            case FILEs_DELETE_DIALOG:
                 if(curMediaType==MediaTool.MEDIA_VIDEO_TYPE){
                     ((AlertDialog) dialog).setMessage(getResources().getString(R.string.myshowplayback_deletev));
                   
                 }else if(curMediaType==MediaTool.MEDIA_IMAGE_TYPE){
                     ((AlertDialog) dialog).setMessage(getResources().getString(R.string.myshowplayback_deletep));
                 }else {
                 }
                break;
        }
    }

    
    @Override
    protected Dialog onCreateDialog(int id, Bundle args){
        switch (id){
            case FILEs_DELETE_DIALOG:  
            return new AlertDialog.Builder(this).setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.myshowplayback_warning)
                .setMessage("")
                .setPositiveButton(R.string.myshowplayback_yes, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                        // 删除媒体文件
                        showDialog(PROGRESS_DIALOG);
                        new Thread(){
                            @Override
                            public void run(){
                            	//delFolder(file.getAbsolutePath());
                                MediaTool.deleteAllMedia(curMediaType, ShowPActivity.this);
                                if (null != mmHandler){
                                    mmHandler.obtainMessage(DELETE_FILEs_SECCESS).sendToTarget();
                                }
                            }
                        }.start();
                    }
                })
                .setNegativeButton(R.string.myshowplayback_cancel, new DialogInterface.OnClickListener(){
                    public void onClick(DialogInterface dialog, int whichButton){
                    }
                })
                .create();
            case PROGRESS_DIALOG:
                pd = new ProgressDialog(this);
                pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                pd.setTitle(R.string.myshowplayback_dele);
                pd.setMessage(getResources().getString(R.string.myshowplayback_deleing));
                pd.setIndeterminate(true);
                pd.setCancelable(true);
                return pd;
        }
        return null;
    }
    
    public static final int DELETE_FILEs_SECCESS = 3;
    /**
     * handler
     */
    private Handler mmHandler = new Handler(){
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case DELETE_FILEs_SECCESS:
                    /*Toast.makeText(ShowPActivity.this, 
                        "删除成功", Toast.LENGTH_SHORT).show();*/
                    dismissDialog(PROGRESS_DIALOG);
                     break;
            }
        }
    };
    
    private void SwitchViewTypePrev()
    {
    	isDetailFace =false;
       	if (!mIsRoot)
        {
            mIsRoot = true;
            showDess(mDirDess);
        }
    }
    /**
    * 
    */
    private OnClickListener clickListener = new OnClickListener(){
    
       public void onClick(View v)
       {
    	   //Intent typeInfo  = new Intent(ShowPActivity.this, ShowPActivity.class);
    	   switch (v.getId())
           {
               case R.id.video_button_id:
            	   if(mGetListStatus == GL_STATUS.GL_STATUS_IDLE)
            	   {
            		   if((mType == MediaTool.MEDIA_IMAGE_TYPE) || mbVideoLock)
            		   {
            			   mbVideoLock = false;
            			   SwitchViewTypePrev();
            			   mType = MediaTool.MEDIA_VIDEO_TYPE;
            			   mEmptyText.setVisibility(View.INVISIBLE);
                    	   showSetTypeButton();

                           mHandler.sendEmptyMessage(GET_PLAY_LIST);
            		   }
            	   }
                   break;
               case R.id.video_lock_button_id:
            	   if(mGetListStatus == GL_STATUS.GL_STATUS_IDLE)
            	   {
            		   if((mType == MediaTool.MEDIA_IMAGE_TYPE ) || !mbVideoLock)
            		   {
                		   mbVideoLock = true;
            			   SwitchViewTypePrev();
            			   mType = MediaTool.MEDIA_VIDEO_TYPE;
            			   mEmptyText.setVisibility(View.INVISIBLE);
                    	   showSetTypeButton();

                           mHandler.sendEmptyMessage(GET_PLAY_LIST);
            		   }
            	   }
            	   break;
               case R.id.picture_button_id:
            	   if(mGetListStatus == GL_STATUS.GL_STATUS_IDLE)
            	   {
            		   if(mType != MediaTool.MEDIA_IMAGE_TYPE)
            		   {
            			   SwitchViewTypePrev();
            			   mType = MediaTool.MEDIA_IMAGE_TYPE;
            			   mEmptyText.setVisibility(View.INVISIBLE);
                    	   showSetTypeButton();
                    	   initListView();
            		   }
            	   }
                   break;
           }   	 
           
       }};
       

       private void check(FileDes fd)
       {
           switch(mType) {
          	case MediaTool.MEDIA_VIDEO_TYPE:
          		Intent intent1 = new Intent(Intent.ACTION_VIEW);
          		//liujie add begin 1008
          		ComponentName componetName = new ComponentName("com.android.gallery3d","com.android.gallery3d.app.MovieActivity");
          		intent1.setComponent(componetName);
          		//liujie add end
                intent1.setDataAndType(Uri.parse(fd.filePath), "video/mp4");
                startActivity(intent1);
          		break;
          	case MediaTool.MEDIA_IMAGE_TYPE:
          		Intent intent = new Intent();
                intent.putExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILEPATH, fd.filePath);
                intent.putExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILETYPE, PlayBacker.TYPE_IMAGE);
                intent.setClass(this, PlayBackActivity.class);
                startActivity(intent);
          		break;
          }
    	   
           /*Intent intent = new Intent();
           intent.putExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILEPATH, fd.filePath);
           switch(mType) {
           	case MediaTool.MEDIA_VIDEO_TYPE:
           		intent.putExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILETYPE, PlayBacker.TYPE_VIDEO);
           		break;
           	case MediaTool.MEDIA_IMAGE_TYPE:
           		intent.putExtra(PlayBacker.PLAYBACK_KEY_VALUE_FILETYPE, PlayBacker.TYPE_IMAGE);
           		break;
           }
           
           intent.setClass(this, PlayBackActivity.class);
           startActivity(intent);*/
       }

       public void deleteCurrentDes(FileDes fd) {
       	int type = mType;
       	if (fd.isDir) {
       		ArrayList<FileDes> list = fd.mediaFileDess;
       		int size = list.size();
       		for (int i = 0; i < size; i++) {
       			FileDes deleteFileDes = list.get(i);
       			deleteMediaFile(deleteFileDes, type, this);
       		}
       	} else {
       		deleteMediaFile(fd, type, this);
       	}
       }
       

       /**
        * handler
        */
       public Handler mHandler = new Handler()
       {
           @Override
           public void handleMessage(Message msg)
           {
               switch (msg.what)
               {
                   case GET_DIR_SUCCESS:
                	   listView.setVisibility(View.VISIBLE);
                       setFileGridData();
                       break;
                   // 获取图片成功
                   case GET_IMAGE_SECCESS:
                       refreshImage();
                       break;
                   case DELETE_FILE_SECCESS:
                   	
                       reListCurrent(fileInfo);
                   	//showDess(mDirDess);
                   	//refreshImage();
                       break;
                   case GET_PLAY_LIST:
                	   mGetListStatus = GL_STATUS.GL_STATUS_START;
               	       initListView();
                	   break;
                   case GET_PLAY_LIST_WAITING:
                	   mGetListStatus = GL_STATUS.GL_STATUS_DEALING;
                       showWaitingDialog();
                       break;
                   case GET_PLAY_LIST_FINISH:
                	   closeWaitingDialog();
                	   finishListMedia();
                	   mGetListStatus = GL_STATUS.GL_STATUS_IDLE;
                	   break;
               }
           }
       };

       public void deleteMediaFile(final FileDes fd, final int type, final Context context)
       {
            File deleteFile = new File(fd.filePath);
            
	       	MediaTool.deleteMedia(fd.id, type, context);
	
	       	//add for can not delete file in external SD card
	    	if(deleteFile.exists())
	    	{
	    		deleteFile.delete();
	    	}
	       	deletePhysicalFile(fd);         
       }

       @Override
    protected void onResume()
       {
    	   if(mbHaveGetlist)
    	   {
    		   refreshImage();
               mHandler.sendEmptyMessage(GET_IMAGE_SECCESS);
    	   }
           super.onResume();
       }

       

       @Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
    	stopCheckThread();
    	Config.gbEnteredPlayList = false;
   		if(mSDCardMountReceiver != null)
   		{
   			unregisterReceiver(mSDCardMountReceiver);
   		}
		super.onDestroy();
	}

	@Override
       public boolean onKeyDown(int keyCode, KeyEvent event)
       {
       	
    	   Log.e(TAG, "***onKeyDown***");
           if (keyCode == KeyEvent.KEYCODE_BACK && Config.bSecondActivityCanBack)
           {        	
           	   onBack();
               return true;
           }
           if(Config.bSecondActivityCanBack){
        	   Log.e(TAG, "***onKeyDown bSecondActivityCanBack = " + Config.bSecondActivityCanBack + "***");
        	   Config.gbEnteredPlayList = false;
        	   return super.onKeyDown(keyCode, event);
           }
           else{
        	   if((System.currentTimeMillis() - mFirstEnterAppTime) > 3000)
        	   {
        		   Config.bSecondActivityCanBack = true;
        	   }
        	   return true;
           }
       }

	/*Handler m_handler;
	static final int UPDATE_TIME = 100000;
    class MyTimerTask extends TimerTask {

		@Override
		public void run() {
			// TODO Auto-generated method stub

			Message m = new Message();
			m.what = UPDATE_TIME;
			m_handler.sendMessage(m);
		}

    }
	
	private void  DelayToExit()
    {
    	m_handler=new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub

				switch (msg.what) {
				case UPDATE_TIME:
					Log.e(TAG, "DelayToExit time out");
					Config.bSecondActivityCanBack = true;
					break;
				}
			}
		};
		Log.e(TAG, "DelayToExit start");
		Timer timer=new Timer();
		timer.schedule(new MyTimerTask(), 1000);
    }*/
       
       private void onBack(){
       	isDetailFace =false;
       	if (!mIsRoot)
           {
       			//liujie add 1009
       			if(!mIsVideoCameraRoot && mType==MediaTool.MEDIA_VIDEO_TYPE){
       				mIsVideoCameraRoot = true;
       				if(fileInfo != null && fileInfo.mParent != null && fileInfo.mParent.mParent != null ){
       					showDess(fileInfo.mParent.mParent.mediaFileDess);
       				}
       			}else{
                    mIsRoot = true;
                    mIsVideoCameraRoot = false;
                    showDess(mDirDess);
       			}
       			//liujie add end
           }else{
        	   Config.gbEnteredPlayList = false;
               finish();
           }
       }

       
       public void handleDo(FileDes fd) {
       	if (fd == null) {
       		return;
       	}
       	//防止多次点击 休息�?��
       	//if(!isClickEnable)
       	//	return;
       	//isClickEnable = false;
       	synchronized (lock)
       	{
   	    	if (fd.isDir) {
   	    	    mIsRoot = false;
   	    	    //liujie add begin 1009
   	    	    if(mIsVideoCameraRoot/*fd.isLastDir*/){
   	    	    	mIsVideoCameraRoot = false;
   	    	    	showMedias(fd);
   	    	    }else{
   	    	    	mIsVideoCameraRoot = true;
   	    	    	showDess(fd.mediaFileDess);
   	    	    }
   	    	    //liujie add end
   	        } else {
   	        	check(fd);
   	        }
   	    	//isClickEnable = true;
       	}

       }
       
       
       public void handlerSend(FileDes fd) {
       	Intent intent = new Intent(Intent.ACTION_SEND);
       	intent.setData(Uri.fromFile(new File(fd.fileName)));
       	intent.setType("*/*");
       	startActivity(intent);
       }
       
       /**
        * 查找数据返回操作
        */
       private void setFileGridData()
       {
           adapter.setFiles(mCurrentDess);
           adapter.notifyDataSetChanged();
       }

       /**
        * 调用刷新
        */
       private void refreshImage()
       {
           adapter.notifyDataSetChanged();
       }
       
       private void showWaitingDialog(){
      	 waitingProg= new ProgressDialog(this);
      	 waitingProg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
      	 waitingProg.setTitle(R.string.myshowplayback_getlist);
      	 waitingProg.setMessage(this.getResources().getString(R.string.myshowplayback_getlist_wait));
      	 waitingProg.setIndeterminate(true);
      	 waitingProg.setCancelable(true);
      	 waitingProg.show();
       }
       private void closeWaitingDialog(){
    	   if(waitingProg != null)
    	   {
    		   waitingProg.dismiss();
    	       waitingProg=null;
    	   }
       }

       private void listMedia(int type)
       {
       	// 处理 条目
    	   if (!SDcardManager.checkSDCardMount())
           {
        	   mEmptyText.setVisibility(View.VISIBLE);
               mHandler.sendEmptyMessage(GET_PLAY_LIST_FINISH);
               return;
   		   }
           mHandler.sendEmptyMessage(GET_PLAY_LIST_WAITING);  
	       mGetListHandler.sendEmptyMessage(GL_DEALING);
       }
       
       private void finishListMedia()
       {
    	   ClearAllResource();
    	   getDirDess();
           int size = keys.size();
           for (int i = 0; i < size; i++)
           {
               String key = keys.get(i);
               mDirDess.add(table.get(key));
           }
           
           showDess(mDirDess);
           mbHaveGetlist = true;
       }
       
       public String getFileName(String pathandname){  
           
           int start=pathandname.lastIndexOf("/");  
           int end=pathandname.lastIndexOf(".");  
           if(start!=-1 && end!=-1){  
               return pathandname.substring(start+1,end);    
           }else{  
               return null;  
           }  
             
       }  

       public int GetdurationFromXMLFile(String filePath)
       {
    	   FileInputStream fileIn = null;//liujie modify 1020
           int length = 0;    
           int nReadLen = 26;
           byte [] buffer = new byte[nReadLen];  //offsettime="100" /></path>
           int byteOffset;    //只读文件末尾27个字符，获取视频的长�?
           int nStartOffset = 10;
           int nFirstQuotOffset;
           int nSecondQuotOffset;
           int nDuration;
           long nSkipByte;
           File file = new File(filePath);
           
           if(file.exists() == false)
           {
               Log.d("GetdurationFromXMLFile", "  not exit!" + filePath);  
    		   return 0;
    	   }
           
			try { //liujie modify 1020
				fileIn = new FileInputStream(filePath);

					   length = fileIn.available();
					   if(length <= 0)
					   {
						   //空文�?
						   return 0;
					   }
					   byteOffset = length - nReadLen;
			           nSkipByte = fileIn.skip(byteOffset);
			           if(nSkipByte == byteOffset)
			           {
			        	   fileIn.read(buffer);
			           }
			           else
			           {
			               Log.d("ShowPActivity", "GetdurationFromXMLFile skip fail: ");  
			           }
  
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  finally {
				try {
					if(fileIn!=null){
						fileIn.close();
					}
				} catch (IOException e) {
				}
			}     	   
           
           
           String strBuffer = new String(buffer);
           nFirstQuotOffset = strBuffer.indexOf('\"', nStartOffset);
           nSecondQuotOffset = strBuffer.indexOf('\"', nFirstQuotOffset+1);
           String strDuration = strBuffer.substring(nFirstQuotOffset + 1, nSecondQuotOffset);
           nDuration = Integer.parseInt(strDuration);
           nDuration = nDuration*1000;
           
           return nDuration;
       }
       
       public static String getExtensionName(String filename) {    
           if ((filename != null) && (filename.length() > 0)) {    
               int dot = filename.lastIndexOf('.');    
               if ((dot >-1) && (dot < (filename.length() - 1))) {    
                   return filename.substring(dot + 1);    
               }    
           }    
           return filename;    
       }  
       
       public void RecursionUpdateFileToDatasheet(String filePath)
       {
    	   File file = new File(filePath);
    	   int index = -1;
    	   
    	   /*if(file.exists() == false)
    	   {
               Log.d("ShowPActivity", "---data--- : " + filePath);  
    		   return;
    	   }*/    	       	   
    	   
           if(file.isFile())
           {
        	   String strExt = getExtensionName(filePath);
               String fileAbsolutePath = file.getAbsolutePath();
        	   String title = getFileName(fileAbsolutePath);
        	   if(MediaTool.MEDIA_VIDEO_TYPE == mType)
        	   {
        		   if(strExt.equalsIgnoreCase("mp4") == false) 
        		   {
        	       	   return;
        	       }
        		   /*
        		   if(mbVideoLock)
        		   {
        			   if(title.length() <= 15)   //加锁的文件名长度大于15 
        			   {
            	       	   return;
        			   }
        		   }*/
        		   
					//liujie modify 1020
        		    if(title.length() < 16)   //视频文件名长度大于等于16 
        			   {
            	       	   return;
        			   }

        	   }
        	   else if(MediaTool.MEDIA_IMAGE_TYPE == mType)
        	   {
        		   if(strExt.equalsIgnoreCase("jpg") == false) { 
        	       		return;
        	       }
        	   }
    	       
        	   long dateTaken = 0;   //这个已经找不回了
        	   boolean canDelete = true;
        	   long duration = 0;
        	   int indexExt;
        	  // Log.i("liujie","title = " + title + " title.length() = " + title.length());
        	   if(title.length() > 16)  //不可删除文件名如f20130110112033_l  len=17 //liujie modify 1020 title.length() > 15
        	   {
        		   canDelete = false;   
        	   }
        	   
        	   switch(mType) 
        	   {
              	// 视频 类型
              	case MediaTool.MEDIA_VIDEO_TYPE:
              		
              		indexExt = filePath.indexOf(".mp4");
              		String strXmlPath;
              		if(!canDelete)
              		{
              			strXmlPath = filePath.substring(0, indexExt - 4) +"-b" +".xml";   //去掉后缀“_l�? //liujie modify 1020
              		}
              		else
              		{
              			strXmlPath = filePath.substring(0, indexExt-2) +"-b"+ ".xml";//liujie modify 1020
              		}
              		duration = GetdurationFromXMLFile(strXmlPath);
              		VideoManager.addVideo(mContentResolver, title, dateTaken, duration, file, canDelete);
              		break;
              	// 图片 类型
              	case MediaTool.MEDIA_IMAGE_TYPE:
              		ImageManager.addImage(mContentResolver, title, dateTaken, null, file);
              		break;
              }
        	   
        	   mUpdateSaveDataSheet = true;
        	   return;
           }
           if(file.isDirectory())
           {
               File[] childFile = file.listFiles();
               String dirPath = file.getAbsolutePath();
               if(childFile == null || childFile.length == 0)
               {
                   return;
               }
               
	   			ArrayList<String> items=new ArrayList<String>();
	   			int i;
	   			for(i=0;i<childFile.length;i++)      
	   			{        
	   				File file1=childFile[i];        
	   				items.add(file1.getName());           
	   		    } 
	   			Collections.sort(items,String.CASE_INSENSITIVE_ORDER);  //按首字母排序 
	   			for (i = 0; i < childFile.length; i++)
	   			{
	   				String fileAllPath = dirPath + "/" + items.get(i);
	   				RecursionUpdateFileToDatasheet(fileAllPath);
	   			}
	               
	           /*File[] childFile1 = file.listFiles();
	           if(childFile1 == null || childFile1.length == 0)
	           {
	               return;
	           }*/
           }
       }
       
       private boolean RefreshSaveDatasheet()
       {
    	   boolean bRet = false;
    	   
    	   switch(mType) {
          	// 视频 类型
          	case MediaTool.MEDIA_VIDEO_TYPE:
          		RecursionUpdateFileToDatasheet(Config.SAVE_RECORD_PATH);
          		break;
          	// 图片 类型
          	case MediaTool.MEDIA_IMAGE_TYPE:
          		RecursionUpdateFileToDatasheet(Config.SAVE_CAPTURE_PATH);
          		break;
          }

    	   return bRet;
       }
       
       private void PAGetAllFiles()
       {
    	   //
           switch(mType) {
           	// 视频 类型
           	case MediaTool.MEDIA_VIDEO_TYPE:
           		superFileDess = MediaTool.getAllVideos(superFileDess, ShowPActivity.this, mbVideoLock);
           		break;
           	// 图片 类型
           	case MediaTool.MEDIA_IMAGE_TYPE:
           		superFileDess = MediaTool.getAllImage(superFileDess, ShowPActivity.this);
           		break;
           }
       }
       
       private void ClearAllResource()
       {
           mCurrentDess.clear();
           mDirDess.clear();
           table.clear();
           keys.clear();
       }

       private void dealItems()
       {        
           PAGetAllFiles();
           
           //如果父文件夹中没有文件，显示无内�?
           if (superFileDess == null || superFileDess.size() <= 0)
           {
        	   mUpdateSaveDataSheet = false;

        	   mContentResolver = ShowPActivity.this.getContentResolver();
        	   
        	   RefreshSaveDatasheet();
        	   if(mUpdateSaveDataSheet == false)
        	   {
        		   //mEmptyText.setVisibility(View.VISIBLE);
                   return;
        	   }
        	   else
        	   {
        		   PAGetAllFiles();
        		   if (superFileDess == null || superFileDess.size() <= 0)
        		   {
        			   //mEmptyText.setVisibility(View.VISIBLE);
                       return;
        		   }
        	   }
           }
           
           Collections.sort(superFileDess, new Comparator<FileDes>()
           { 
               public int compare(FileDes object1, FileDes object2)
               { 
               	 //根据文本排序 
                    return object2.fileName.compareTo(object1.fileName); 
               }     

              }
           );   
       }
       
       // 扫描文件内媒体文件，获得单个媒体文件交给handleDirDes处理
       private void getDirDess()
       {
           int size = superFileDess.size();
           for (int i = 0; i < size; i++)
           {
               FileDes fd = superFileDess.get(i);Log.i("PLJ", "ShowPActivity---->getDirDess:" + fd.fileName);
               handleDirDes(fd);
           }
       }
       
       //get all the individual media files and put them to a subFiledess ---得到 当个�?媒体 文件，并且放他们�? 父文件中
       private void handleDirDes(FileDes fd)
       {
           FileDes dateDirDes;
           FileDes videoCameraDirDes; //liujie add 1009
           // 获得文件的前�?位字符，并作�?
           String data = fd.fileName.substring(0, 9); //disname.substring(0, 8);
           Log.i("PLJ", "ShowPActivity---->handleDirDes:" + data);
           char first = data.charAt(0); //liujie add 1009
           if( Character.isDigit(first)){ 
        	   data = fd.fileName.substring(0, 8);//liujie add
           }
           // 如果目录文件内不存在含有data文件名的文件夹，就创�?
           if (!table.containsKey(data))
           {
               dateDirDes = new FileDes();

               if( Character.isDigit(first)){          	   
                   dateDirDes.fileName = data.substring(0, 4) + "-" + data.substring(4, 6) + "-" + data.substring(6, 8) ;
               }else {
            	   if(data.substring(0, 1).equals("f")){
                       String strFront = getResources().getString(R.string.playback_front);
            		   dateDirDes.fileName = strFront + "-" + data.substring(1, 5) + "-" + data.substring(5, 7) + "-" + data.substring(7, 9);
            	   }else {
            		   String strBack  = getResources().getString(R.string.playback_back);
            		   dateDirDes.fileName = strBack + "-" + data.substring(1, 5) + "-" + data.substring(5, 7) + "-" + data.substring(7, 9);
				   }
               }
               //liujie add end
               dateDirDes.disname = dateDirDes.fileName;
               dateDirDes.id = -1;
               dateDirDes.isDir = true;
               dateDirDes.isLastDir = true; //liujie add 1009
               // 将data键与对应的目标文件夹关联
               table.put(data, dateDirDes);
               // 创建了一个以data命名的文件夹后，将data加入ArrayList
               keys.add(data);
           }
           else
           {
               dateDirDes = table.get(data);
           }
            
           
           if( Character.isDigit(first)){//liujie add 1009
        	  
               videoCameraDirDes = new FileDes();
               //Log.i("liujie2", "videoCameraDirDes1 " + videoCameraDirDes);
               String strFrontCamera  = getResources().getString(R.string.playback_front_camera);
               String strBackCamera  = getResources().getString(R.string.playback_back_camera);
               if(fd.fileName.endsWith("f") || fd.fileName.endsWith("f_l")){
            	   videoCameraDirDes.fileName = strFrontCamera;
            	   videoCameraDirDes.disname = strFrontCamera;
               }else{
            	   videoCameraDirDes.fileName = strBackCamera;
            	   videoCameraDirDes.disname = strBackCamera;
               }
               
               if(!dateDirDes.videoCameraTable.containsKey(videoCameraDirDes.disname)){
                   videoCameraDirDes.id = -1;
                   videoCameraDirDes.isDir = true;
                   videoCameraDirDes.isLastDir = true; 

                   dateDirDes.videoCameraTable.put(videoCameraDirDes.disname, videoCameraDirDes);
                   dateDirDes.mediaFileDess.add(videoCameraDirDes);
               }else{
            	   videoCameraDirDes = dateDirDes.videoCameraTable.get(videoCameraDirDes.disname);
               }
        	   dateDirDes.isLastDir = false; 
        	   //Log.i("liujie2", "videoCameraDirDes2 " + videoCameraDirDes);
        	  // Log.i("liujie", "videoCameraDirDes.mediaFileDess " + videoCameraDirDes.mediaFileDess);
        	   //Log.i("liujie", "handleDirDes filename " + fd.fileName);
        	   videoCameraDirDes.mediaFileDess.add(fd); 
        	   videoCameraDirDes.mParent = dateDirDes;
        	   fd.mParent = videoCameraDirDes;
           }else{
        	   dateDirDes.mediaFileDess.add(fd); 
        	   fd.mParent = dateDirDes;
           }
          
       }

       private void deletePhysicalFile(FileDes fd)
       {
       	String filePath = fd.filePath;
       	int dotIndex = filePath.indexOf(".");
       	if (dotIndex <= 0) {
       		return;
       	}
       	String fileName = filePath.substring(0, dotIndex) + Config.INFO_SUFFIX;
       	File xmlFile = new File(fileName);
           if (xmlFile.exists() )
           {
           	xmlFile.delete();
           }
       }

       public void reListCurrent(FileDes fd)
       {
       	if (mIsRoot) {
       		mDirDess.remove(fd);
       		if (mDirDess.size() <= 0) {
       			mDirDess.clear();
       			mEmptyText.setVisibility(View.VISIBLE);
       		}
       		showDess(mDirDess);
           	
       	} else {
       		ArrayList<FileDes> list = fd.mParent.mediaFileDess;
       		list.remove(fd);
       		
       		//liujie add begin 1009
       		if(!mIsVideoCameraRoot && mType == MediaTool.MEDIA_VIDEO_TYPE){
       			if (list.size() <= 0) {
       				list.clear();
       				
       				ArrayList<FileDes> listParents = fd.mParent.mParent.mediaFileDess;
       				listParents.remove(fd.mParent);
       				if(listParents.size() <= 0){
       					listParents.clear();
       					mDirDess.remove(fd.mParent.mParent);
       	       			if (mDirDess.size() <= 0) {
       	           			mDirDess.clear();
       	           			mEmptyText.setVisibility(View.VISIBLE);
       	           		}
       	       			showDess(mDirDess);
       	       			mIsRoot = true;
       					
       				}else{  // 返回前后摄像头目录
               			fd.mParent.mParent.mediaFileDess = listParents;
               			mIsVideoCameraRoot = true;
               			showDess(listParents);
       				}
       			}else{ //依然在文件列表目录
           			fd.mParent.mediaFileDess = list;
           			showDess(list);
       			}
       		}else{//liujie add end

       		if (list.size() <= 0) {
       			list.clear();
       			mDirDess.remove(fd.mParent);
       			if (mDirDess.size() <= 0) {
           			mDirDess.clear();
           			mEmptyText.setVisibility(View.VISIBLE);
           		}
       			showDess(mDirDess);
       			mIsRoot = true;
       		} else {
       			fd.mParent.mediaFileDess = list;
       			showDess(list);
       		}
       	  }
       	}
       }

       private void showDess(ArrayList<FileDes> list)
       {
       	   mCurrentDess = list;
           Message msg = mHandler.obtainMessage(GET_DIR_SUCCESS);
           msg.sendToTarget();
       }

       // 显示 媒体
       private void showMedias(final FileDes dirDes)
       {
           if (null != imageTool)
           {
               imageTool.cancelTask();
               imageTool = null;
               adapter.setImageTool(null);
           }
           
           Thread thread = new Thread()
           {
               @Override
               public void run()
               {
               	switch(mType) {
               	  	// 视频 类型
               		case MediaTool.MEDIA_VIDEO_TYPE:
               			imageTool =
                               new ImageTool(ShowPActivity.this, 
                               		defaultVideoImage, ShowPActivity.this, true);
               			break;
               		// 图片 类型
               		case MediaTool.MEDIA_IMAGE_TYPE:
               			imageTool =
                               new ImageTool(ShowPActivity.this, defaultVideoImage, ShowPActivity.this,false);
               			break;
               	}
                   adapter.setImageTool(imageTool);
                   showDess(dirDes.mediaFileDess);
                   imageTool.start();
               }
           };
           thread.start();
       }

       public void onImageGetSuccess(Bitmap bitmap, long id, int type)
       {

           if (null != adapter)
           {
               // 如果返回的图片为空则以后使用默认图片
               if (null == bitmap)
               {
                   switch (type)
                   {
                       case MediaTool.MEDIA_VIDEO_TYPE:
                           bitmap = defaultVideoImage;
                           break;
                       case MediaTool.MEDIA_IMAGE_TYPE:
                           bitmap = defaultImageImage;
                           break;
                   }
               }
               adapter.addImage(id, bitmap);
           }
           Message msg = mHandler.obtainMessage(GET_IMAGE_SECCESS);
           msg.sendToTarget();
       }
}
