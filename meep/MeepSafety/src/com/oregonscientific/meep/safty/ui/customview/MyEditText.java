package com.oregonscientific.meep.safty.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import com.oregonscientific.meep.safty.R;

public class MyEditText extends android.widget.EditText
{

    public MyEditText(Context context)
    {
        this(context, null);
    }

    public MyEditText(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyle)
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
