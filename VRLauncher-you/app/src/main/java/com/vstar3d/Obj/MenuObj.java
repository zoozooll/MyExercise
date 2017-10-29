package com.vstar3d.Obj;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.vstar3d.VRLauncher.R;


public class MenuObj {
	private static final String TAG = "MenuObj";
	private int mChid=-1;
	Handler mHandler=null;
	int AnimationTime=1000;
	private List<VRFace> mFaceList=new ArrayList<VRFace>(); 
	private VRFace appico;
	private VRFace onlineico;
	private VRFace imagesico;
	private VRFace viedoico;
	private VRFace shopico;
	private VRFace setico;
	private VRwifiFace wifiico;
	private VRBluetoothFace blueico;
	private VRBatteryFace batterico;
	private VRClockFace clockico;

	private TitleTextFace appTitle;
	private TitleTextFace viewTitle;
	private TitleTextFace photoTitle;
	private TitleTextFace videoTitle;
	private TitleTextFace storeTitle;
	private TitleTextFace settingTitle;
	public MenuObj(Context context, int mBv,int wv, Handler mHandler)
	{
		this.mHandler=mHandler;
//		mFaceList.add(new VRFace(1700,972,600,120 ,10,1,0.8f,128, 0, 0, 0));//bj
//		mFaceList.add(new VRFace(1700,944,600,120 ,10,1,0.8f,128, 0, 0, 0));//bj
		String title = context.getResources().getString(R.string.launcher_icon_allapps);
//		appico=new VRFace(1728,990,64,64 ,10,1,0.70f,context,drawIconBitmap(context,"app.png",title,false),drawIconBitmap(context,"appOn.png",title,true),0);
//		appico=new VRFace(1728,990,64,64 ,10,1,0.70f,context,"app.png","appOn.png",0);
		appico=new VRFace(1728,962,64,64 ,10,1,0.70f,context,"app.png","appOn.png",0);
		mFaceList.add(appico);
//		appTitle=new TitleTextFace(1728,1060,70,30 ,10,1,0.70f,context,title);
		appTitle=new TitleTextFace(1728,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(appTitle);

		title = context.getResources().getString(R.string.launcher_icon_onlinevideo);
//		onlineico=new VRFace(1818,990,86,86 ,10,1,0.70f,context,drawIconBitmap(context,"online.png",title,false,true),drawIconBitmap(context,"onlineOn.png",title,true,true),1);
		onlineico=new VRFace(1818,962,64,64 ,10,1,0.70f,context,"online.png","onlineOn.png",1);
		mFaceList.add(onlineico);
		viewTitle=new TitleTextFace(1818,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(viewTitle);

		title = context.getResources().getString(R.string.launcher_icon_photo);
//		imagesico=new VRFace(1918,990,86,86 ,10,1,0.70f,context,drawIconBitmap(context,"images.png",title,false),drawIconBitmap(context,"imagesOn.png",title,true),2);
		imagesico=new VRFace(1918,962,64,64 ,10,1,0.70f,context,"images.png","imagesOn.png",2);
		mFaceList.add(imagesico);
		photoTitle=new TitleTextFace(1918,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(photoTitle);

		title = context.getResources().getString(R.string.launcher_icon_movie);
//		viedoico=new VRFace(2018,990,86,86 ,10,1,0.70f,context,drawIconBitmap(context,"video.png",title,false),drawIconBitmap(context,"videoOn.png",title,true),3);
		viedoico=new VRFace(2018,962,64,64 ,10,1,0.70f,context,"video.png","videoOn.png",3);
		mFaceList.add(viedoico);
		videoTitle=new TitleTextFace(2018,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(videoTitle);

		title = context.getResources().getString(R.string.launcher_icon_strore);
//		shopico=new VRFace(2118,990,86,86 ,10,1,0.70f,context,drawIconBitmap(context,"shop.png",title,false),drawIconBitmap(context,"shopOn.png",title,true),4);
		shopico=new VRFace(2118,962,64,64 ,10,1,0.70f,context,"shop.png","shopOn.png",4);
		mFaceList.add(shopico);
		storeTitle=new TitleTextFace(2118,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(storeTitle);

		title = context.getResources().getString(R.string.launcher_icon_settings);
//		setico=new VRFace(2218,990,86,86 ,10,1,0.70f,context,drawIconBitmap(context,"set.png",title,false),drawIconBitmap(context,"setOn.png",title,true),5);
		setico=new VRFace(2218,962,64,64 ,10,1,0.70f,context,"set.png","setOn.png",5);
		mFaceList.add(setico);
		settingTitle=new TitleTextFace(2218,1030,70,30 ,10,1,0.70f,context,title);
		mFaceList.add(settingTitle);


		
//		mFaceList.add(new VRFace(1900,1120,260,50 ,10,1,0.8f,128, 0, 0, 0));//bj
		
		wifiico=new VRwifiFace(1920,1134,28,22 ,10,1,0.70f,context,"wifiun.png",wv);//
		mFaceList.add(wifiico);
		
		blueico=new VRBluetoothFace(1970,1134,28,22 ,10,1,0.70f,context,"blue_off.png");//
		mFaceList.add(blueico);
		
		batterico=new VRBatteryFace(2030,1134,28,22 ,10,1,0.70f,context,"battery.png",mBv);//
		mFaceList.add(batterico);

		clockico= new VRClockFace(2085,1134,56,25,10,1,0.7f,context,"00:00");
		mFaceList.add(clockico);
	}

	private static final int textSize = 18;
	private static final int appIconWidth = 50;
	private static final int appIconHeight = 50;
	private static final int textHeight = 15;


	private Bitmap drawIconBitmap(Context context,String iconName,String appName,boolean on){
		Bitmap bitmap = Base.loadImageAssets(context, iconName);
		Bitmap bmp=Bitmap.createBitmap(appIconWidth+36,appIconHeight+textHeight+20,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()), new RectF(0,0,appIconWidth,appIconHeight), null);
		Paint paint=new Paint();
		if(!on){
			paint.setColor(Color.WHITE);
		}else{
			paint.setColor(Color.GRAY);
		}

		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		if(appName!=null)
		{
			int tw=(int)paint.measureText(appName);
			int l=0;
			if(tw>appIconWidth)
				l=0;
			else
				l=(appIconWidth-tw)/2;
			canvas.drawText(appName, l, appIconHeight+textHeight+18, paint);
		}
		return bmp;
	}

	private Bitmap drawIconBitmap(Context context,String iconName,String appName,boolean on,boolean video){
		Bitmap bitmap = Base.loadImageAssets(context, iconName);
		Bitmap bmp=Bitmap.createBitmap(appIconWidth+36,appIconHeight+textHeight+20,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bmp);
		canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()), new RectF(10,0,appIconWidth+10,appIconHeight), null);
		Paint paint=new Paint();
		if(!on){
			paint.setColor(Color.WHITE);
		}else{
			paint.setColor(Color.GRAY);
		}

		paint.setTextSize(textSize);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		if(appName!=null)
		{
			int tw=(int)paint.measureText(appName);
			int l=0;
			if(tw>appIconWidth)
				l=0;
			else
				l=(appIconWidth-tw)/2;
			canvas.drawText(appName, -2, appIconHeight+textHeight+18, paint);
		}
		return bmp;
	}
	

	
	public void Draw(int attribPosition,int attribTexCoord,float [] EulerAngles, float[] matrix, int mMVPMatrixHandle,boolean left)
	{
		int pagesize=mFaceList.size();
		int checkid=-1;
		for(int i=0;i<pagesize;i++)
		{
			VRFace obj=mFaceList.get(i);
			obj.Draw(attribPosition, attribTexCoord,EulerAngles,matrix,mMVPMatrixHandle,left);
			if(obj.Checked)
			{
				checkid=obj.id;
			}
		}
		if(mChid!=checkid)
		{
			mChid=checkid;
			mHandler.removeMessages(8888);

			Message message=new Message();
			message.what=3333; 
			message.arg1=mChid;
			message.arg2=AnimationTime;
			mHandler.sendMessageDelayed(message,0);
			
			if(mChid>-1)
			{
				message=new Message();  
				message.what=8888; 
				message.arg1=mChid;
				mHandler.sendMessageDelayed(message,AnimationTime);
			}
		}
	}


	public void SetBv(int mBv) {
		if(batterico!=null)
		{
			batterico.SetBv(mBv);
		}
		
	}
	
	public void SetWv(int wv) {
		if(batterico!=null)
		{
			wifiico.Setwifi(wv);
		}
		
	}

	public void setBluetoothStatus(boolean status){
		if(blueico!=null)
			blueico.setBluetoothEnable(status);
	}

	public void updateTime(){
		if(clockico!=null)
			clockico.updateTime();
	}




	public void SetHandler(Handler mHandler) {
		this.mHandler=mHandler;
	}

}
