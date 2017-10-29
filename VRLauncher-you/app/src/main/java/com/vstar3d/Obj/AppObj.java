package com.vstar3d.Obj;

import java.util.ArrayList;
import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AppObj {
	private static final String TAG = "AppObj";
	private int mChid=-1;
	Handler mHandler=null;
	Context mContext;
	int AnimationTime=1000;
	int row=2;
	private List<VRFace> mFaceList=new ArrayList<VRFace>(); 
	private List<VRFace> mAppFaceList=new ArrayList<VRFace>(); 
	private int appicowidth=78;
	private int appicoheight=60;
	private int apptitleheight=27;
	VRFace returnIco=null;
	VRFace UpIco=null;
	VRFace DownIco=null;
	private List<ResolveInfo> mApps=null;
	public static int pagesize=8;
	private int page=0;
	int curpagesize=0;
	int pagecount=0;
	private Bitmap [] mAppBmp= new Bitmap[pagesize];
	private Bitmap [] mAppBmpOn= new Bitmap[pagesize];
	
	private void loadApps() {
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        mApps = mContext.getPackageManager().queryIntentActivities(mainIntent, 0);
    }
	
	public AppObj(Context context, int mBv,int wv, Handler mHandler)
	{
		mContext=context;
		loadApps();
		this.mHandler=mHandler;
//		mFaceList.add(new VRFace(1840,885,320,230 ,10,1,0.8f,128, 0, 0, 0));//bj
		mFaceList.add(new VRFace(1835,885,500,300 ,10,1,0.8f,128, 0, 0, 0));//bj

		returnIco=new VRFace(1895,905,38,38 ,10,1,0.70f,context,"return.png","returnOn.png",27);
		mFaceList.add(returnIco);
		
		UpIco=new VRFace(2060,905,38,38 ,10,1,0.70f,context,"up.png","upOn.png","upOff.png",28);
		mFaceList.add(UpIco);
		
		DownIco=new VRFace(2225,905,38,38 ,10,1,0.70f,context,"down.png","downOn.png","downOff.png",29);
		mFaceList.add(DownIco);
		DownIco.setOff(false);
		
		
		Bitmap mBitmap=Base.loadImageAssets(context, "videoOn.png");
		
		for(int i=0;i<pagesize;i++)
		{
			int t=i/(pagesize/row);
//			mAppFaceList.add(new VRFace(1886+(i%(pagesize/row))*62,930+t*72,48,appicoheight+apptitleheight ,10,1,0.70f,context,mBitmap,mBitmap,i+10));
			mAppFaceList.add(new VRFace(1886+(i%(pagesize/row))*110,950+t*110,58,appicoheight+apptitleheight ,10,1,0.70f,context,mBitmap,mBitmap,i+10));
//			mAppFaceList.add(new VRFace(1900+(i%(pagesize/row))*122,945+t*125,96,appicoheight+apptitleheight ,10,1,0.70f,context,mBitmap,mBitmap,i+10));//修改大小之后的
			//mFaceList.add(mAppFaceList.get(i));

		}
		GetAppList();
		int mv=mApps.size()%pagesize;
		int tcount=(mApps.size()-mv)/pagesize;
		if(mv>0)
			pagecount=tcount+1;
		else
			pagecount=tcount;
		
		
		
		
	}
	
	private void GetAppList()
	{
		int fistpos=page*pagesize;
		int appsize=mApps.size();
		int tsize=appsize-page*pagesize;
		if (tsize<=0)
			return;
		if(tsize>pagesize)
			tsize=pagesize;
		
		curpagesize=tsize;
		for(int i=0;i<curpagesize;i++)
		{
			 ResolveInfo info = mApps.get(fistpos+i);
			 if(mAppBmp[i]!=null)
			 {
				 mAppBmp[i].recycle();
				 mAppBmp[i]=null;			
			 }
			 
			 if(mAppBmpOn[i]!=null)
			 {
				 mAppBmpOn[i].recycle();
				 mAppBmpOn[i]=null;			
			 }
			 String AppName=(String) info.loadLabel(mContext.getPackageManager());
			 Drawable bd =  info.loadIcon(mContext.getPackageManager());
			 if("android.graphics.drawable.BitmapDrawable".equals(bd.getClass().getName()))
			 {

				 Bitmap bitmap = ((BitmapDrawable)bd).getBitmap();
	
			 	mAppBmp[i]=getAppBmp(bitmap,AppName);
			 	mAppBmpOn[i]=getAppBmpOn(bitmap,AppName);
			 	mAppFaceList.get(i).setBmp(mAppBmp[i],mAppBmpOn[i]);
			 }
		}
		
		if(page==0)
			UpIco.setOff(true);
		else
			UpIco.setOff(false);
		
		if(pagecount==(page+1))
			DownIco.setOff(true);
		else
			DownIco.setOff(false);
		
	}

	private static final int TEXT_SIZE = 16;
	private Bitmap getAppBmp(Bitmap bitmap, String appName)
	{
		Bitmap tBitmap=Bitmap.createBitmap(appicowidth,appicoheight+apptitleheight,Bitmap.Config.ARGB_8888);  
		Canvas canvas = new Canvas(tBitmap);
		canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()), new RectF(0,0,appicowidth,appicoheight), null);
		Paint paint=new Paint();
		paint.setColor(Color.WHITE);
		paint.setTextSize(TEXT_SIZE);
		paint.setAntiAlias(true);
		paint.setTypeface(Typeface.DEFAULT_BOLD);
		if(appName!=null)
		{
//		int tw=appName.length()*TEXT_SIZE;
 		int tw=(int)paint.measureText(appName);
		int l=0;
		if(tw>appicowidth)
			l=0;
		else
			l=(appicowidth-tw)/2;
		canvas.drawText(appName, l, appicoheight+TEXT_SIZE, paint);
		}
		return tBitmap;
	}
	
	private Bitmap getAppBmpOn(Bitmap bitmap, String appName)
	{
		Bitmap tBitmap=Bitmap.createBitmap(appicowidth,appicoheight+apptitleheight,Bitmap.Config.ARGB_8888);  
		Canvas canvas = new Canvas(tBitmap);

		canvas.drawBitmap(bitmap, new Rect(0,0,bitmap.getWidth(),bitmap.getHeight()), new RectF(0,0,appicowidth,appicoheight), null);
		Paint paint=new Paint();
		paint.setColor(Color.GRAY);
		paint.setTextSize(TEXT_SIZE);
		if(appName!=null)
		{
//		int tw=appName.length()*TEXT_SIZE;
			int tw=(int)paint.measureText(appName);
		int l=0;
		if(tw>appicowidth)
			l=0;
		else
			l=(appicowidth-tw)/2;
		canvas.drawText(appName, l, appicoheight+TEXT_SIZE, paint);
		}

		return tBitmap;
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
		pagesize=mAppFaceList.size();
		if(pagesize>curpagesize)
			pagesize=curpagesize;
		
		for(int i=0;i<pagesize;i++)
		{
			VRFace obj=mAppFaceList.get(i);
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
	
		
	}
	
	public void SetWv(int wv) {

	}

	public void SetHandler(Handler mHandler) {
		this.mHandler=mHandler;
	}

	public void LastApp() {
		
		if(page>0)
		{
			page=page-1;
			GetAppList();
			
		}
	}

	public void NextApp() {
		
		if((page+1)<pagecount)
		{
			page=page+1;
			GetAppList();
		}
	}

	public void startActivity(int id) {
		int position=page*pagesize+id;
		if(position<mApps.size())
		{
			ResolveInfo info = mApps.get(position);
            
            String pkg = info.activityInfo.packageName;
            String cls = info.activityInfo.name;
             
            ComponentName componet = new ComponentName(pkg, cls);
             
            Intent i = new Intent();
            i.setComponent(componet);
            mContext.startActivity(i);

		}
		
	}
}
