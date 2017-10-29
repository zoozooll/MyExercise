package com.vstar3d.Obj;

import android.content.Context;
import android.net.wifi.WifiManager;

public class VRBatteryFace extends VRFace {

	Context mContext=null;
	boolean isreg=false;
	int mBv=-1;
	int SV=00;
	WifiManager mBatteryManager=null;
	public VRBatteryFace(int l, int t, int w, int h, int xParallels,
			int yParallels, float z, Context context, String ImgName, int bv) {
		super(l, t, w, h, xParallels, yParallels, z, context, ImgName);
		mContext=context;
		SetBv(bv);
	}
	
	
	protected void updatecheng(float [] EulerAngles)
	{
		if(mBv!=SV)
		{
			SV=mBv;
			
			updateimg();
		}
	}


	public void SetBv(int lv) {
		if(lv!=mBv)
        {
        	
        	mBv=lv;
        	if(mBitmap!=null)
			{
				mBitmap.recycle();
				mBitmap=null;
			}
			if(mBv==0)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_low.png");
			}else if(mBv==1)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_low.png");
			}else if(mBv==2)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_20.png");
			}else if(mBv==3)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_30.png");
			}else if(mBv==4)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_40.png");
			}else if(mBv==5)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_50.png");
			}else if(mBv==6)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_60.png");
			}else if(mBv==7)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_70.png");
			}else if(mBv==8)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_80.png");
			}else if(mBv==9)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_90.png");
			}else if(mBv==10)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_normal_100.png");
			}else if(mBv==20)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_low.png");
			}else if(mBv==21)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_low.png");
			}else if(mBv==30)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_100.png");
			}else if(mBv==22)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_20.png");
			}else if(mBv==23)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_30.png");
			}else if(mBv==24)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_40.png");
			}else if(mBv==25)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_50.png");
			}else if(mBv==26)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_60.png");
			}else if(mBv==27)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_70.png");
			}else if(mBv==28)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_80.png");
			}else if(mBv==29)
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/battery_charging_90.png");
			}else
			{
				mBitmap=Base.loadImageAssets(mContext, "battery/mac_battery_in.png");
			}
        }
        
    
		
	}



}
