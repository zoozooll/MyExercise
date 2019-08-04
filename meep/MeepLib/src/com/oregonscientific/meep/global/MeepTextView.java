package com.oregonscientific.meep.global;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


public class MeepTextView extends TextView {

    public MeepTextView(Context context) {
    	super(context);
    }
    
    public MeepTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Typeface font = Typeface.createFromAsset(context.getAssets(), "ARLRDBD.TTF");
        setTypeface(font);
    }
    
    public MeepTextView(Context context, AttributeSet attrs, int i) {
    	super(context, attrs, i);
    }
    

}