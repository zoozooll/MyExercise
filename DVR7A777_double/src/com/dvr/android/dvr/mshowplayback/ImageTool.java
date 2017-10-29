package com.dvr.android.dvr.mshowplayback;

import java.util.ArrayList;
import android.content.Context;
import android.graphics.Bitmap;
import android.provider.MediaStore.Images;
import android.util.Log;

public class ImageTool extends Thread {
	private static final String TAG = "ImageTool";
    private static final int VIDEO_TYPE = 0;
    private static final int IMAGE_TYPE = 2;

    private OnImageLoadListener mListener = null;
    private ArrayList<Long> tasks = new ArrayList<Long>();
    private boolean isRunning = true;
    private Bitmap defaultImage = null;
    private Context mContext = null;
    private boolean mIsVideo = true;

    public ImageTool(OnImageLoadListener listener, Bitmap bitmap, Context context , boolean isVideo){
        mListener = listener;
        defaultImage = bitmap;
        mContext = context;
        mIsVideo = isVideo;
    }

    public void add(long id){
        for (int i = 0; i < tasks.size(); i++){
            if (id == (tasks.get(i))){
                Log.d(TAG, "is repeat return");
                return;
            }
        }
        tasks.add(id);
        if (this.getState() == Thread.State.WAITING){
            synchronized (this){
                this.notifyAll();
            }
        }
    }

    public void cancelTask(){
        mListener = null;
        isRunning = false;
    }

    @Override
    public void run(){
        while (isRunning){
            if (0 != tasks.size()){
                long myId = tasks.get(0);
                if (myId < 0){
                	return;
                }
                tasks.remove(0);
                Log.d(TAG, "myId: " + myId);
                Bitmap bitmap =
                    BitmapManager.instance().getThumbnail(mContext.getContentResolver(),
                        myId,
                        Images.Thumbnails.MICRO_KIND,
                        null,
                        mIsVideo);
                if (null == bitmap){
                    bitmap = defaultImage;
                }
                else {
                    Log.d(TAG,
                        "get the bitmap success w = " + bitmap.getWidth() + " height= " + bitmap.getHeight());
                }
                
                if (null != mListener){
                    mListener.onImageGetSuccess(bitmap, myId , mIsVideo ? VIDEO_TYPE : IMAGE_TYPE);
                }
            }
            else {
                synchronized (this){
                    try {
                        this.wait();
                    }
                    catch (InterruptedException e){
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
