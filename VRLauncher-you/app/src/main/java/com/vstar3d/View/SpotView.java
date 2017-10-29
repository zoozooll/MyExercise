package com.vstar3d.View;

import com.vstar3d.Obj.Base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class SpotView  extends View {
	private int mWidth=0;
	private int mHeight=0;
	public View mOnView=null;
	public int mShowX=0;
	public int mShowY=0;
	public int mOffsetX=0;
	private Bitmap mBitmap=null;
	private Context mContext=null;
	private int mAniamationCount=8;
	private void init(Context context)
	{
		mContext=context;
	}
	

	public SpotView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		 init(context);
	}
	
	public SpotView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}
	
	public SpotView(Context context) {
		super(context);
		init(context);
	}
	int mAniamationId=0;
	@SuppressLint("HandlerLeak")
	int timesp=0;
	@SuppressLint("HandlerLeak")
	Handler mHandler = new Handler(){
    	public void handleMessage(android.os.Message msg) {
    		switch (msg.what) {
    		case 3333:
    			if(mBitmap!=null)
    			{
    				mBitmap.recycle();
					mBitmap=null;
    			}
    			if(msg.arg1<0)
    			{
    				if(mBitmap==null)
    					mBitmap=Base.loadImageAssets(mContext,"spot.png");
    			}else
    			{
    				mAniamationId=0;
    				if(mBitmap==null)
    					mBitmap=Base.loadImageAssets(mContext,"spot/spot0.png");
    				timesp=msg.arg2/mAniamationCount;
    				mAniamationId++;
    				Message message=new Message();  
    				message.what=8888; 
    				message.arg1=mId;
    				mHandler.sendMessageDelayed(message,timesp);
    			}
    			SpotView.this.invalidate();
    			break;
    		case 8888:
    			   			
    			if(mBitmap!=null)
    			{
    				mBitmap.recycle();
					mBitmap=null;
    			}
    	
    			if(mAniamationId<mAniamationCount)
    			{
    				mBitmap=Base.loadImageAssets(mContext,"spot/spot"+mAniamationId+".png");
					SpotView.this.invalidate();
					
    				mAniamationId++;
    				Message message=new Message();  
					message.what=8888; 
					message.arg1=mId;
					mHandler.sendMessageDelayed(message,timesp);
					
    			}else
    			{
    				mBitmap=Base.loadImageAssets(mContext,"spot.png");
					SpotView.this.invalidate();
    			}
    			break;
    		}
    	}
	};
	
	int mId=-1;
	public void SetAnimation(int id,int AnimationTime)
	{
		if(mId!=id)
		{
			mId=id;
			
				mHandler.removeMessages(8888);
				
				Message message=new Message();  
				message.what=3333; 
				message.arg1=mId;
				message.arg2=AnimationTime;
				
				mHandler.sendMessageDelayed(message,0);
		
			
		}
	}
	
	@SuppressLint("DrawAllocation")
	@Override
    protected void onDraw(Canvas canvas) {
       super.onDraw(canvas);
       if(mBitmap==null)
    	   mBitmap=Base.loadImageAssets(mContext,"spot.png");
       if(mBitmap!=null)
       {
    	   if(mWidth!=getWidth())
    		   mWidth=getWidth();
    	   if(mHeight!=getHeight())
    		   mHeight=getHeight();
		   //Log.e("3dvstar","3dvstar mWidth="+mWidth+","+mHeight);
    	   mShowX = (mWidth  -	mBitmap.getWidth())/2;
    	   mShowY = (mHeight -	mBitmap.getHeight())/2;
    	   canvas.drawBitmap(mBitmap, mShowX+mOffsetX, mShowY, null);
    	   
    	   
       }
	}

}
