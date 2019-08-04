package com.oregonscientific.meep.safty;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.oregonscientific.meep.safty.R;

public class MyButton extends android.widget.Button
{

    public MyButton(Context context)
    {
        this(context, null);
    }

    public MyButton(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs);
        if (this.isInEditMode()) return ;

        final TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.TextViewPlus);
        final String customFont = a.getString(R.styleable.TextViewPlus_customFont);

        //Build a custom typeface-cache here!
        this.setTypeface(
            Typeface.createFromAsset(context.getAssets(), customFont)
        );  
    }
}
