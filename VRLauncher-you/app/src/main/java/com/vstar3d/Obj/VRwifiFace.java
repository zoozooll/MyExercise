package com.vstar3d.Obj;

import android.content.Context;
import android.content.IntentFilter;
public class VRwifiFace extends VRFace {
	IntentFilter wifiIntentFilter;  
	Context mContext=null;
	boolean isreg=false;
	int mWifivalue=-1;
	int Shwowifivalue=00;
	public VRwifiFace(int l, int t, int w, int h, int xParallels,
			int yParallels, float z, Context context, String ImgName,int wv) {
		super(l, t, w, h, xParallels, yParallels, z, context, ImgName);
		mContext=context;	
		
		Setwifi(wv);
	}
	
	public void Setwifi(int wifivalue)
	{
		 
		{
			
			if(wifivalue==mWifivalue)
			{
				;
			}else
			{
				mWifivalue=wifivalue;
				if(mBitmap!=null)
				{
					mBitmap.recycle();
					mBitmap=null;
				}
				if(mWifivalue==10)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wificloseed.png");
				}else if(mWifivalue==20)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wificloseed.png");
				}else if(mWifivalue==30)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wifiun.png");
				}else if(mWifivalue==31)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wifien01.png");
				}else if(mWifivalue==32)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wifien02.png");
				}else if(mWifivalue==33)
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wifien03.png");
				}else
				{
					mBitmap=Base.loadImageAssets(mContext, "wifi/wifiun.png");
				}
			}

		}
	}
	

	
	protected void updatecheng(float [] EulerAngles)
	{
		if(mWifivalue!=Shwowifivalue)
		{
			Shwowifivalue=mWifivalue;
			updateimg();
		}
	}



}
