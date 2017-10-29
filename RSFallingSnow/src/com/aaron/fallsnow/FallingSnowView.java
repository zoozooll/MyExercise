package com.aaron.fallsnow;

import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.renderscript.RSSurfaceView;
import android.renderscript.RenderScriptGL;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;

public class FallingSnowView extends RSSurfaceView {
	
	private final static String TAG = "FallingSnowView";
	private final static long DROP_MIN_INTERVAL = 200;
	private final static float DROP_MIN_DELAY = 5.0F;
	
	private Context mContext;
	private RenderScriptGL mRSGL;
    private SnowRS mRender;
    private long mLastDropTimeMillion;
    private float mLastDropX;
    private float mLastDropY;
	private Bitmap mBackBitmap;

     
    public FallingSnowView(Context context) {
        super(context);
    }
 
    public FallingSnowView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
	}

	@Override
    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        super.surfaceChanged(holder, format, w, h);
        Log.d(TAG, "surface "+w +","+h);
        if (mRSGL == null) {
            RenderScriptGL.SurfaceConfig sc = new RenderScriptGL.SurfaceConfig();
            mRSGL = createRenderScriptGL(sc);
            mRSGL.setSurface(holder, w, h);
            mRender = new SnowRS(w, h);
            if (mBackBitmap == null) {
            	WallpaperManager mana = WallpaperManager.getInstance(mContext);
                //mBackBitmap = ((BitmapDrawable)mana.getDrawable()).getBitmap();
                //Log.d(TAG, "BackBitmap "+mBackBitmap.getWidth() +","+mBackBitmap.getHeight());
            	Log.d(TAG, "view's w "+w +" h "+h);
            	DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            	int winWidht = displayMetrics.widthPixels;
            	int winHeight = displayMetrics.heightPixels;
            	Log.d(TAG, "window's w "+winWidht+" h "+winHeight );
                mBackBitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
                Canvas c = new Canvas();
                c.setBitmap(mBackBitmap);
                //c.drawBitmap(((BitmapDrawable)mana.getDrawable()).getBitmap(), 0, 0, null);
                Rect src = new Rect(0, winHeight - h, winWidht, winHeight);
                Rect dst = new Rect(0, 0, w, h);
                c.drawBitmap(((BitmapDrawable)mana.getDrawable()).getBitmap(), src, dst, null);
            }
            Log.d(TAG, "mBackBitmap "+mBackBitmap.getWidth()+","+mBackBitmap.getHeight());
            mRender.setmBackBitmap(mBackBitmap);
            mRender.init(mRSGL, getResources(), false);
            mRender.start();
        }
    }
    
	@Override
    protected void onDetachedFromWindow() {
        if (mRSGL != null) {
            mRSGL = null;
            destroyRenderScriptGL();
            mBackBitmap.recycle();
        }
    }
    
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	final float curDropX = event.getX();
    	final float curDropY = event.getY();
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
        case MotionEvent.ACTION_MOVE:
        	onDroping(curDropX, curDropY);
        	
            break;
        case MotionEvent.ACTION_UP:
        	long currentTimeMillion = System.currentTimeMillis();
    		// 时间间隔太短，不调用直接退出；
    		if (currentTimeMillion - mLastDropTimeMillion < DROP_MIN_INTERVAL) {
    			return false;
    		}
    		mRender.addDrop(curDropX, curDropY);
    		mLastDropTimeMillion = currentTimeMillion;
        	mLastDropX = -100.f;
            mLastDropY = -100.f;
        	break;
        }
        return true;
    }

	private void onDroping(final float curDropX, final float curDropY) {
		long currentTimeMillion = System.currentTimeMillis();
		// 时间间隔太短，不调用直接退出；
		if (currentTimeMillion - mLastDropTimeMillion < DROP_MIN_INTERVAL) {
			return;
		}
		// 位置距离太短，不调用直接退出；
		if ((Math.pow((curDropX - mLastDropX), 2) + Math.pow(
				curDropY - mLastDropY, 2)) < Math.pow(DROP_MIN_DELAY, 2)) {
			return;
		}
		mRender.addDrop(curDropX, curDropY);
		mLastDropTimeMillion = currentTimeMillion;
		mLastDropX = curDropX;
		mLastDropY = curDropY;
	}
}
