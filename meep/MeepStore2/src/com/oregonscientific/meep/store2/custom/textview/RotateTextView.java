package com.oregonscientific.meep.store2.custom.textview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class RotateTextView extends TextView{
	
	

	public RotateTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
    protected void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    this.setVisibility(View.GONE);
	    canvas.skew(1.0f, 0.3f);  //you need to change values over here
	    Rotate3dAnimation skew = new Rotate3dAnimation(
	              -25, 25,200, 200, -70, false);   //here too
	    startAnimation(skew);
	    this.setVisibility(View.VISIBLE);

    }
}
