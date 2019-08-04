package com.oregonscientific.meep.store2.custom.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.TextView;

public class RotateTextViewGame extends TextView{
	
	

	public RotateTextViewGame(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    canvas.skew(1.0f, 0.3f);  //you need to change values over here
	    Rotate3dAnimation skew = new Rotate3dAnimation(
	              -15, 15,200, 200, -35, false);   //here too
	    startAnimation(skew);

    }
}
