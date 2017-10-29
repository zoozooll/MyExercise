package com.vstar3d.Obj;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

public class VRBluetoothFace  extends VRFace  {
	private boolean blueadapteren=false;
	Context mContext=null;
	private boolean ischanged=true;
	public VRBluetoothFace(int l, int t, int w, int h, int xParallels,
			int yParallels, float z, Context context, String ImgName) {
		super(l, t, w, h, xParallels, yParallels, z, context, ImgName);
		this.mContext=context;
		BluetoothAdapter blueadapter=BluetoothAdapter.getDefaultAdapter();
		if(blueadapter!=null)
		{
			if(blueadapter.isEnabled())
			{
				blueadapteren=true;
			}else
			{
				blueadapteren=false;
			}
		}else
		{
			blueadapteren=false;
		}
		setBluetoothEnable(blueadapteren);
	}

	public void setBluetoothEnable(boolean enable){
		ischanged=true;
		if(mBitmap!=null)
		{
			mBitmap.recycle();
			mBitmap=null;
		}
		if(enable){
			blueadapteren=true;
			mBitmap=Base.loadImageAssets(mContext, "blue.png");
		}else {
			blueadapteren=false;
			mBitmap=Base.loadImageAssets(mContext, "blue_off.png");
		}
//		updateimg();


	}

	protected void updatecheng(float [] EulerAngles)
	{
		if(ischanged)
		{
			updateimg();
		}
		ischanged=false;
	}
	
//	protected void Draw(int attribPosition,int attribTexCoord,float [] EulerAngles,float []matrix,int mMVPMatrixHandle,boolean left)
//	{
////		if(blueadapteren)
//			super.Draw(attribPosition, attribTexCoord, EulerAngles, matrix,mMVPMatrixHandle,left);
//	}

}
