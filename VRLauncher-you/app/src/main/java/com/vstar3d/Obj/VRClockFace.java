package com.vstar3d.Obj;


import android.content.Context;
import android.util.Log;

/**
 * Created by wangjx on 2016/6/20.
 */
public class VRClockFace extends VRFace{
    private static final String TAG = "VRClockFace";
    private Context mContext;
    private boolean isUpdate=false;

    public VRClockFace(int l, int t, int w, int h, int xParallels,
                      int yParallels, float z, Context context, String time) {
        super(l, t, w, h, xParallels, yParallels, z, context, time);
        mContext=context;
        updateTime();
    }

    public void updateTime(){
        isUpdate=true;
        if(mBitmap!=null)
        {
            mBitmap.recycle();
            mBitmap=null;
        }
        mBitmap=Base.initBitmap();
    }

    @Override
    protected void updatecheng(float[] EulerAngles) {
        if(isUpdate)
            updateimg();
        isUpdate=false;
    }
}
