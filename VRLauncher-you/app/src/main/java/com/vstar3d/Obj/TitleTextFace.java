package com.vstar3d.Obj;

import android.content.Context;

/**
 * Created by software department on 2016/7/2.
 */
public class TitleTextFace extends VRFace{
    private boolean isUpdate=false;
    public TitleTextFace(int l, int t, int w, int h, int xParallels,
                         int yParallels, float z, Context context, String title) {
        super(l, t, w, h, xParallels, yParallels, z, context, title);
        updateTitle(title);
    }

    public void updateTitle(String title){
        isUpdate=true;
        if(mBitmap!=null)
        {
            mBitmap.recycle();
            mBitmap=null;
        }
        mBitmap=Base.initBitmap(title);
    }

    @Override
    protected void updatecheng(float[] EulerAngles) {
        if(isUpdate)
            updateimg();
        isUpdate=false;
    }
}
