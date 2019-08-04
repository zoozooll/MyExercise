package com.aaron.filter;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MyDrawView extends View {
	
	private static int index;
	private static String STR = "TEXTVIEW";

	private float mDelayX;
	private Camera mCamera = new Camera();
	private Matrix mMatrix = new Matrix(); 
	private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG); 

	public MyDrawView(Context context) {
		super(context);
		index ++;
	}

	public MyDrawView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		index ++;
	}

	public MyDrawView(Context context, AttributeSet attrs) {
		super(context, attrs);
		index ++;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		long start_time = System.currentTimeMillis();
    	Log.d("aaron", "mDelayX "+ mDelayX);
    	//canvas.drawARGB(0, 255, 0, 0);
        startRotate(canvas, mDelayX, canvas.getWidth(), canvas.getHeight());  
        super.dispatchDraw(canvas);  
        long end_time = System.currentTimeMillis();  
        Log.d("aaron", (end_time - start_time) + " ms");
	}
	/**
     * 需要从{@link Workspace}引用过来的位置，设置在滑动时候本CellLayout
     * @param delayX 滑动时再X方向的偏移量。即滑动时刻的X点跟静止时候的点的距离，左为负，右为正；
     */
    public void setCellLayoutDelayX(float delayX) {
    	mDelayX = delayX;
    }
    
    private void startRotate(Canvas canvas, float xCor, int width, int height) {  
        boolean flag = true;  
        /*if (xCor < 0) {  
            xCor = width + xCor;  
            flag = false;  
        } else if (xCor >= 0) {  
            // xCor = width - xCor;  
        } else if (xCor < 0) {  
            xCor = width + xCor;  
        } else if (xCor >= 0) {  
            flag = false;  
        }  */
        /*if (xCor <= 0) {  
            xCor = 10;  
        }// the maximum left  
        if (xCor > width) {  
            xCor = width - 10;  
        }// the maximum right  
*/        
        float angle = 0;  
        //if (isBorder) {  
            //doDraw(canvas, width, height, angle, xCor);  
        //} else if (!flag) {  
            /*angle = 90.0f - (xCor / (float) width) * 90.0f;  
            doDraw(canvas, width, height, angle, xCor);  */
        //} else {  
            angle = -(xCor / (float) width) * 90.00f;  
            doDraw(canvas, width, height, angle, xCor);  
        //}  
    }  
      
    private void doDraw(Canvas canvas, float width, float height, float angle, float cor) {
        canvas.save();  
        mCamera .save();  
        mCamera.rotateY(angle);  
        mCamera.getMatrix(mMatrix);  
        mCamera.restore();  
        Log.i("aaron", this +" angle "+ angle + " matrix  " + mMatrix);  
        if (angle < 0) {  
            mMatrix.preTranslate(-width, -height * 0.5f);  
            mMatrix.postTranslate(width, height * 0.5f);  
        } else {  
            mMatrix.preTranslate(0f, -height * 0.5f);  
            mMatrix.postTranslate(0f, height * 0.5f);  
        }  
       canvas.concat(mMatrix);
       int currentScreen = index % 5;
       String str = STR + currentScreen;
       switch (currentScreen) {  
       case 0:  
           mPaint.setColor(Color.RED);
           break;  
       case 1:  
           mPaint.setColor(Color.BLUE);
           break;  
       case 2:  
           mPaint.setColor(Color.YELLOW);  
           break;  
       case 3:  
           mPaint.setColor(Color.CYAN); 
           break;  
       case 4:  
           mPaint.setColor(Color.GREEN);  
           break;  
       } 
       mPaint.setAlpha(100);
       mPaint.setStyle(Paint.Style.FILL_AND_STROKE); 
       canvas.drawText(str, 20, 20, mPaint);
       canvas.drawRect(0, 0, width, height, mPaint);  
    }  
}
