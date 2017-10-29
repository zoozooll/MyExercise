package com.dvr.android.dvr.mshowplayback;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dvr.android.dvr.R;

public class FileListAdapter extends BaseAdapter
{
	private final String TAG = "FileListAdapter";
    private Context mContext = null;
    private ArrayList<FileDes> mFileDess = null;
    private LayoutInflater inflater = null;
    //private boolean mIsList = false;
    private ImageTool imageTool = null;
    private boolean isClickEnable = true;
    private AlertDialog m_alertdig;
    //private boolean mHasImage = false;
    private HashMap<Long, WeakReference<Bitmap>> images = new HashMap<Long, WeakReference<Bitmap>>();
    
    //the waiting deleting progress dialog
    ProgressDialog waitingProg = null;
    Resources rc;
    String[] mWeekDays=null;//save the weekdays
    
    public FileListAdapter(Context c){
        mContext = c;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        rc = mContext.getResources();
    	String[] weekDays =  {rc.getString(R.string.Sunday), 
    			rc.getString(R.string.Monday), 
    			rc.getString(R.string.Tuesday), 
    			rc.getString(R.string.Wedenesday), 
    			rc.getString(R.string.Thursday), 
    			rc.getString(R.string.Friday), 
    			rc.getString(R.string.Saturday)};
    	mWeekDays = weekDays;
    }
    
    public void addImage(long id, Bitmap bmp){
        images.put(id, new WeakReference<Bitmap>(bmp));
    }

    /**
     * @param isList
     */
    /** public void setIsList(boolean isList)
    {
        mIsList = isList;
    }*/

    /**
     * @param displayType 
     */
    /** public void setHasImage(boolean hasImage )
    {
        mHasImage = hasImage;
    }*/

    /**
     * @param tool
     */
    public void setImageTool(ImageTool tool){
        images.clear();
        imageTool = tool;
    }

    /**
     * @param fileDess
     */
    public void setFiles(ArrayList<FileDes> fileDess){
    	mFileDess = fileDess;
    }

    public int getCount(){
    	if(mFileDess != null)
    	{
    		return mFileDess.size();
    	}
    	
    	return 0;
    }

    public Object getItem(int position){
    	if(mFileDess != null)
    	{
    		return mFileDess.get(position);
    	}
    	return null;
    }

    public long getItemId(int position){
        return position;
    }

    
    public View getView(int position, View convertView, ViewGroup parent){
    	//debugLog("fileInfo disname: "+ mFileDess.get(position).disname);
    	isClickEnable = true;
    	m_alertdig = null;
        if (null == convertView){
        	// 相等于layout
            convertView = inflater.inflate(R.layout.list_by_date_item, null);
        }
        
    	if (mFileDess == null || mFileDess.size() <= 0)
    	{
    		Log.e(TAG, "***mFileDess NULL or nFileDess no content");
    		return convertView;
    	}
    	
    	((ShowPActivity)mContext).fileInfo = mFileDess.get(position);
    	
        // 前面的图�?
        ImageView imageView = (ImageView) convertView.findViewById(R.id.list_by_date_flag_image_id);  
        // 日期与时�?
        TextView dateView = (TextView) convertView.findViewById(R.id.list_by_date_date_tv_id);  
        // 星期�?
        TextView weekdayView = (TextView) convertView.findViewById(R.id.list_by_date_weekday_tv_id);
        // 间隔时间
        TextView durationView = (TextView) convertView.findViewById(R.id.list_detail_duration_tv_id);
        // 发�?按钮
        ImageButton sendButton = (ImageButton)convertView.findViewById(R.id.list_detail_send_button_id);
        // 播放 按钮
        ImageButton playButton = (ImageButton)convertView.findViewById(R.id.list_detail_play_button_id);
        // 删除 按钮
        ImageButton deleteButton = (ImageButton)convertView.findViewById(R.id.list_by_date_delete_button_id);
        // 详情 按钮
        ImageButton detailButton = (ImageButton)convertView.findViewById(R.id.list_by_date_dvrlist_button_id);
       
        /*sendButton.setTag(((ShowPActivity)mContext).fileInfo);
        playButton.setTag(((ShowPActivity)mContext).fileInfo);
        deleteButton.setTag(((ShowPActivity)mContext).fileInfo);
        detailButton.setTag(((ShowPActivity)mContext).fileInfo);
        
        deleteButton.setOnClickListener(new OnClickListener(){  
            
            public void onClick(View v) {
            	
            	//showDeleteAlert(((ShowPActivity)mContext).fileInfo);
            	
            	//debugLog((String)v.getTag());
            	//debugLog("v.getId(): " + v.getId()+"");
            	//debugLog(((ShowPActivity)mContext).fileInfo.disname);
            	if(null != m_alertdig)
            		return;
            	((ShowPActivity)mContext).fileInfo = (FileDes) v.getTag();
            	//debugLog("v.getId(): " + v.getId()+"");
            	
            	showDeleteAlert(((ShowPActivity)mContext).fileInfo);
            	
            
            }  
              
        });  
        
        sendButton.setOnClickListener(new OnClickListener(){  
            
            public void onClick(View v) {
            	((ShowPActivity)mContext).fileInfo = (FileDes)v.getTag();
            	((ShowPActivity)mContext).handlerSend(((ShowPActivity)mContext).fileInfo);
            	//debugLog("v.getId(): " + v.getId()+"");
            }  
              
        });
        
        // 单击 事件
        detailButton.setOnClickListener(new OnClickListener()
        {  
            public void onClick(View v) {
            	if(!isClickEnable)
            		return;
            	isClickEnable = false;
            	ShowPActivity.isDetailFace = true;
            	((ShowPActivity)mContext).fileInfo = (FileDes)v.getTag();
            	((ShowPActivity)mContext). handleDo(((ShowPActivity)mContext).fileInfo); 
            	
            }  
              
        }); 
        
        // 播放 按钮的设�?
        playButton.setOnClickListener(new OnClickListener(){  
            
            public void onClick(View v) {
            	if(!isClickEnable)
            		return;
            	isClickEnable = false;
            	((ShowPActivity)mContext).fileInfo = (FileDes)v.getTag();
            	((ShowPActivity) mContext).handleDo(((ShowPActivity)mContext).fileInfo);            	
            }  
              
        });*/
        
        convertView.setTag(((ShowPActivity)mContext).fileInfo);      
        convertView.setOnClickListener(new OnClickListener()
        {  
            public void onClick(View v) 
            {
            	if(!isClickEnable)
            		return;
            	isClickEnable = false;
            	
            	if(!ShowPActivity.isDetailFace)
            	{
            		if( ((FileDes)v.getTag()).isLastDir ){ //liujie add 0901
            			ShowPActivity.isDetailFace = true;
            		}
                	((ShowPActivity)mContext).fileInfo = (FileDes)v.getTag();
                	((ShowPActivity)mContext). handleDo(((ShowPActivity)mContext).fileInfo);
            	}
            	else
            	{
                	((ShowPActivity)mContext).fileInfo = (FileDes)v.getTag();
                	((ShowPActivity) mContext).handleDo(((ShowPActivity)mContext).fileInfo);  
            	}          	
            }  
              
        }); 
        convertView.setOnLongClickListener(new OnLongClickListener(){
	        public boolean onLongClick(View arg0){
	        	if(ShowPActivity.isDetailFace)
	        	{
	        		((ShowPActivity)mContext).fileInfo = (FileDes) arg0.getTag();	            	
	            	showDeleteAlert(((ShowPActivity)mContext).fileInfo);
	        	}
	            return true;
	        }
	   });
        
        
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.list_by_date_item_id);
        /* if (mIsList)
        {
            layout.setGravity(Gravity.LEFT);
        }
        else
        {*/
            //layout.setGravity(Gravity.CENTER);
        /*}*/

        if(!ShowPActivity.isDetailFace)
        {
        	imageView.setImageResource(((ShowPActivity)mContext).fileInfo.fileIcon);
        	convertView.setBackgroundDrawable(((ShowPActivity)mContext).getResources().getDrawable(R.drawable.item_bg));
        }
        else
        {
        	convertView.setBackgroundDrawable(((ShowPActivity)mContext).getResources().getDrawable(R.drawable.item_bg_file));
        	if(((ShowPActivity)mContext).fileInfo.fileType == FileDes.FILE_TYPE_VIDEO)
            {
            	imageView.setImageResource(R.drawable.video_icon);
            }
            else if(((ShowPActivity)mContext).fileInfo.fileType == FileDes.FILE_TYPE_IMAGE)
            {
            	imageView.setImageResource(R.drawable.image_icon);
            }
        }
        
        if (true) {
            WeakReference<Bitmap> bmpRef = images.get(((ShowPActivity)mContext).fileInfo.id);
            // center_inside
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.getLayoutParams().width = 100;
            //imageView.getLayoutParams().height = 70;
            imageView.getLayoutParams().height = (int)((ShowPActivity)mContext).getResources().getDimension(R.dimen.playlist_image_height);
            if (null == bmpRef){
                if (null != imageTool){
                    imageTool.add(((ShowPActivity)mContext).fileInfo.id);
                }
            }
            else {
                Bitmap bmp = bmpRef.get();
                // 
                if (null == bmp){
                    if (null != imageTool){
                        imageTool.add(((ShowPActivity)mContext).fileInfo.id);
                    }
                }
                else {
                    imageView.setImageBitmap(bmp);
                }
            }
        }
        if(!ShowPActivity.isDetailFace){
	         detailButton.setVisibility(View.GONE);
	         sendButton.setVisibility(View.GONE);
	         playButton.setVisibility(View.GONE);
	         durationView.setVisibility(View.GONE);
	        	
	        dateView .setText(((ShowPActivity)mContext).fileInfo.fileName);
	         
	         /*
	         debugLog("isDetailFace:0 " + ShowPActivity.isDetailFace + "; dateView text: " +
	        			((ShowPActivity)mContext).fileInfo.fileName.substring(0,8));
	         */
	         //liujie modify begin 0911
	         /*
	         char first = ((ShowPActivity)mContext).fileInfo.fileName.charAt(0);
	         if(Character.isDigit(first)){
		         weekdayView.setText(getWeekDayByDate(((ShowPActivity)mContext).fileInfo.fileName.substring(0,4)
		        		 + ((ShowPActivity)mContext).fileInfo.fileName.substring(5,7)
		        		 +((ShowPActivity)mContext).fileInfo.fileName.substring(8))); 	        	 
	         }else{
		         weekdayView.setText(getWeekDayByDate(((ShowPActivity)mContext).fileInfo.fileName.substring(2,6)
		        		 + ((ShowPActivity)mContext).fileInfo.fileName.substring(7,9)
		        		 +((ShowPActivity)mContext).fileInfo.fileName.substring(10)));        	 
	         }*/
	         //liujie modify end 0911
        }else{
        	int nFileNameLen;
        	detailButton.setVisibility(View.GONE);
        	//sendButton.setVisibility(View.VISIBLE);
            playButton.setVisibility(View.GONE);
            
            nFileNameLen = ((ShowPActivity)mContext).fileInfo.fileName.length();
	         //liujie modify begin 0911
	         char first = ((ShowPActivity)mContext).fileInfo.fileName.charAt(0);
	         if(Character.isDigit(first)){//liujie add 1009
	        	 dateView.setText(((ShowPActivity)mContext).fileInfo.fileName.substring(0,4) + "-"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(4,6) + "-"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(6,8) + " "
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(8,10) + ":"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(10,12) + ":"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(12,14));
	         }else{
	        	 dateView.setText(((ShowPActivity)mContext).fileInfo.fileName.substring(1));
	        	 dateView.setText(((ShowPActivity)mContext).fileInfo.fileName.substring(1,5) + "-"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(5,7) + "-"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(7,9) + " "
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(9,11) + ":"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(11,13) + ":"
	        			 + ((ShowPActivity)mContext).fileInfo.fileName.substring(13));
	         }
	       //liujie modify end 0911
	       
        	// debugging
        	/*
        	debugLog("mFileDess size: " + getCount());
        	debugLog("isDetailFace:1 " + ShowPActivity.isDetailFace + "; dateView text: " +
        			((ShowPActivity)mContext).fileInfo.fileName.substring(0,8));
        	*/
	       //liujie modify begin 0911
	         /*
	         if(Character.isDigit(first)){
	             weekdayView.setText(((ShowPActivity)mContext).fileInfo.fileName.substring(4,6) 
	            		 + "-" 
	            		 +((ShowPActivity)mContext).fileInfo.fileName.substring(6,8)
	            		 +"    " 
	            		 // 
	            		 +getWeekDayByDate(((ShowPActivity)mContext).fileInfo.fileName.substring(0,8))); 
	         }else {
	             weekdayView.setText(((ShowPActivity)mContext).fileInfo.fileName.substring(5,7) 
	            		 + "-" 
	            		 +((ShowPActivity)mContext).fileInfo.fileName.substring(7,9)
	            		 +"    " 
	            		 // 
	            		 +getWeekDayByDate(((ShowPActivity)mContext).fileInfo.fileName.substring(1,9))); 
			}*/
	       //liujie modify end 0911

            if(ShowPActivity.mType == MediaTool.MEDIA_VIDEO_TYPE)
            {
	            durationView.setVisibility(View.VISIBLE);
	            int a = (int) ((ShowPActivity)mContext).fileInfo.duration;
	            String b = toTime(a);
	            durationView.setText(b);
            }
            else
            {
            	durationView.setVisibility(View.GONE);
            }
        }
        return convertView;
    }
    /**
     * 时间格式转换函数
     * 
     * @param time
     * @return
     */
    public String toTime(int time)
    {

        time /= 1000;
        int minute = time / 60;
        int hour = minute / 60;
        int second = time % 60;
        minute %= 60;
        return String.format("%02d:%02d:%02d", hour, minute, second);
    }
    
    /** 
     *当用户点击按钮时触发的事件，会弹出一个确认对话框 
     */  
     public void showDeleteAlert(final FileDes fd){ 
    	
     	String msg = mContext.getResources().getString(
     			fd.isDir ? R.string.myshowplayback_delete_dir : R.string.myshowplayback_delete_file)
     			+ " " + fd.disname + " ?";
  
        m_alertdig = new AlertDialog.Builder(mContext)    
  
                 .setIcon(R.drawable.alert_dialog_icon)
                 .setTitle(R.string.myshowplayback_warning)    
  
                .setMessage(msg)    
  
                .setPositiveButton(R.string.myshowplayback_yes, new DialogInterface.OnClickListener() {    
  
                public void onClick(DialogInterface dialog, int which) {
                	showWaitingDialog();
                	if (getCount() == 1) {
                		ShowPActivity.isDetailFace = false;
                	}
                	Thread thread = new Thread() {
        				@Override
                        public void run() {        					
        					((ShowPActivity)mContext).deleteCurrentDes(fd);
        					//((ShowPActivity)mContext).reListCurrent(fd);
        					closeWaitingDialog();
        					((ShowPActivity)mContext).mHandler.sendEmptyMessage(ShowPActivity.DELETE_FILE_SECCESS);
        					
        					//closeWaitingDialog();
        				}
        			};
        			thread.start();
                     }    
  
                 }).setNegativeButton(R.string.myshowplayback_cancel, new DialogInterface.OnClickListener() {    
  
                public void onClick(DialogInterface dialog, int which) { 
                		m_alertdig = null;
                     }    
  
                 }).setOnCancelListener(new OnCancelListener()
                 {
                     public void onCancel(DialogInterface dialog)
                     {
                    	 m_alertdig = null;
                     }
                 }).show();    
               
            }
     
     
     private void showWaitingDialog(){
    	 waitingProg= new ProgressDialog(mContext);
    	 waitingProg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    	 waitingProg.setTitle(R.string.myshowplayback_dele);
    	 waitingProg.setMessage(mContext.getResources().getString(R.string.myshowplayback_deleing));
    	 waitingProg.setIndeterminate(true);
    	 waitingProg.setCancelable(true);
    	 waitingProg.show();
     }
     private void closeWaitingDialog(){
    	 waitingProg.dismiss();
    	 waitingProg=null;
     }
     
    //
    private String getWeekDayByDate(String dateStr){   	
    	Calendar cal = Calendar.getInstance();

    	cal.set(Integer.valueOf(dateStr.substring(0,4)), 
    			Integer.valueOf(dateStr.substring(4,6))-1, 
    					Integer.valueOf(dateStr.substring(6,8)));
    	int i =cal.get(Calendar.DAY_OF_WEEK)-1;
    	if(i<=0) i=0;
    	return mWeekDays[i];
    } 
}

