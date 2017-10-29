package com.aaron.loving;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;

public class LovingView extends SurfaceView implements Callback,Runnable {
	
	private Context context;
	boolean mbloop = false;  
    SurfaceHolder mSurfaceHolder = null;  
    private Canvas canvas;  
    int miCount = 0;  
    int y = 50; 
    
    /**
     * 屏幕宽度；
     */
    private int screenWidth;
    /**
     * 屏幕高度；
     */
    private int screenHeight;

    /*
     * 
     */
	public LovingView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public LovingView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init();
	}

	public LovingView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init();
	}
	
	private void init(){
		 mSurfaceHolder = this.getHolder();  
	     mSurfaceHolder.addCallback(this);  
	     this.setFocusable(true);  
	     this.setKeepScreenOn(true);  
	     mbloop = true; 
	     Display display = ((Activity)context).getWindowManager().getDefaultDisplay();
	     screenWidth = display.getWidth();
	     screenHeight = display.getHeight();
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceChanged(android.view.SurfaceHolder, int, int, int)
	 */
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	

	}

	/*
	 * (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceCreated(android.view.SurfaceHolder)
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		new Thread(this).start(); 

	}

	/*
	 * (non-Javadoc)
	 * @see android.view.SurfaceHolder.Callback#surfaceDestroyed(android.view.SurfaceHolder)
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		 mbloop = false;

	}

	public void run() {
		while (mbloop) {  
            try {  
                Thread.sleep(200);  
            } catch (Exception e) {  
            }  
            synchronized (mSurfaceHolder) {  
            	drawingLove();  
            }  
        }  
	}
	
	private void drawingLove() {  
        canvas = mSurfaceHolder.lockCanvas();  
        try {  
            if (mSurfaceHolder == null || canvas == null) {  
                return;  
            }  
            if (miCount < 100) {  
                miCount++;  
            } else {  
                miCount = 0;  
            }  
            Paint paint = new Paint();  
            paint.setAntiAlias(true);  
            paint.setColor(Color.BLACK);  
            canvas.drawRect(0, 0, screenWidth, screenHeight, paint);  
            switch (miCount % 6) {  
            case 0:  
                paint.setColor(Color.BLUE);  
                break;  
            case 1:  
                paint.setColor(Color.GREEN);  
                break;  
            case 2:  
                paint.setColor(Color.RED);  
                break;  
            case 3:  
                paint.setColor(Color.YELLOW);  
                break;  
            case 4:  
                paint.setColor(Color.argb(255, 255, 181, 216));  
                break;  
            case 5:  
                paint.setColor(Color.argb(255, 0, 255, 255));  
                break;  
            default:  
                paint.setColor(Color.WHITE);  
                break;  
            }  
            int i, j;  
            double x, y, r;  
  
            for (i = 0; i <= 90; i++) {  
                for (j = 0; j <= 90; j++) {  
                    r = Math.PI / 45 * i * (1 - Math.sin(Math.PI / 45 * j))  
                            * 20;  
                    x = r * Math.cos(Math.PI / 45 * j)  
                            * Math.sin(Math.PI / 45 * i) + screenWidth / 2;  
                    y = -r * Math.sin(Math.PI / 45 * j) + (screenHeight-80) / 4;  
                    canvas.drawPoint((float) x, (float) y, paint);  
                }  
            }  
  
            paint.setTextSize(32);  
            paint.setTypeface(Typeface.create(Typeface.SERIF, Typeface.ITALIC));  
  
            RectF rect = new RectF(screenWidth/5, screenHeight-80,screenWidth-screenWidth/5, screenHeight-75);  
           // RectF rect = new RectF(0, 0, screenWidth, screenHeight); 
            canvas.drawRoundRect(rect, (float) 1.0, (float) 1.0, paint);  
            canvas.drawText("Loving You", 75, screenHeight-80, paint);  
            mSurfaceHolder.unlockCanvasAndPost(canvas);  
        } catch (Exception e) {  
        }  
  
    }  

}
