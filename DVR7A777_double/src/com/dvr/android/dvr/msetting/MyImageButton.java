package com.dvr.android.dvr.msetting;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.ImageButton;

public class MyImageButton extends ImageButton { 
    private String text = null;  
    private int color;   
    //private int offsetY = 50;  //800 参数
    //private int offsetX = 80;
    private float offsetY = 60;
    private float offsetX = 95;
    private float size = 20;
    
    public MyImageButton(Context context, AttributeSet attrs) { 
        super(context,attrs); 
    } 
      
    public void setText(String text){ 
        this.text = text;       
    } 
    
    public void setTextSize(float size){ 
        this.size = size;       
    } 
      
    public void setColor(int color){ 
        this.color = color;    
    } 
    
    public void setOffsetY(float offsetY){ 
        this.offsetY = offsetY;    
    } 
    
    public void setOffsetX(float offsetX){ 
        this.offsetX = offsetX;    
    } 
      
    @Override
    protected void onDraw(Canvas canvas) { 
        super.onDraw(canvas); 
        Paint paint=new Paint(); 
        //FontMetrics fontMetrics = paint.getFontMetrics();  
        //float fontTotalHeight = fontMetrics.bottom - fontMetrics.top;  
        //float offY = fontTotalHeight / 2 - fontMetrics.bottom;  
        //float fontTotalWidth = fontMetrics.descent - fontMetrics.ascent;
        //float offX = fontTotalWidth / 2 - fontMetrics.descent;  
        paint.setTextSize(size);
        paint.setTextAlign(Paint.Align.CENTER); 
        paint.setColor(color);
        
        if(text !=  null)
        {
        	canvas.drawText(text, offsetX, offsetY, paint);  
        }
        
    } 
  
}