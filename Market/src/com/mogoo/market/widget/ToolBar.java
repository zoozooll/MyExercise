package com.mogoo.market.widget;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.mogoo.market.R;
//import android.graphics.Shader;
public class ToolBar extends View 
{
	 private Paint                   mPaint; 
     private Paint                   mActiveTextPaint;
     private Paint                   mInactiveTextPaint; 
     private ArrayList<TabMember>    mTabMembers;
     private int                     mActiveTab;   
     private OnTabClickListener      mOnTabClickListener = null;   
     
     private int textSize;
     private int imagePaddingTop,textPaddingButtom;
     private int mBackGroundResId;
     private int mSelectorResId;
     private int mSelectorPadding;
     
     private Bitmap mBgBitmap;
     private Bitmap mSelectorBitmap;
     private Resources r;
     public static HashMap<Integer, Bitmap> mtabImages = new HashMap<Integer, Bitmap>();
     public static HashMap<Integer, Bitmap> mtabSelectedImages = new HashMap<Integer, Bitmap>();
     
     public ToolBar(Context context)
     {
    	 this(context,null);
     }
     
     public ToolBar( Context context, AttributeSet attrs ) 
     {  
         super(context, attrs);   
         mTabMembers = new ArrayList<TabMember>( );
         r = getResources();
         
         mPaint = new Paint( );   
         mActiveTextPaint = new Paint( );   
         mInactiveTextPaint = new Paint( );   
         
      // 字体大小、字体距底端的距离在xml里面获得
 		TypedArray ta = context.obtainStyledAttributes(attrs,
 				R.styleable.ToolBar);
 		
         textSize = ta.getDimensionPixelSize(R.styleable.ToolBar_toolbarTextSize, 0);
         imagePaddingTop = ta.getDimensionPixelSize(R.styleable.ToolBar_imagePaddingTop, 0);
         textPaddingButtom = ta.getDimensionPixelSize(R.styleable.ToolBar_textPaddingButtom, 0);
         mSelectorPadding = ta.getDimensionPixelSize(R.styleable.ToolBar_selectorPadding, 0);
         mBackGroundResId = ta.getResourceId(R.styleable.ToolBar_backgroundDrawable, -1);
         mSelectorResId = ta.getResourceId(R.styleable.ToolBar_selectorDrawable, -1);
         if(mBackGroundResId != -1) {
        	 mBgBitmap = BitmapFactory.decodeResource(getResources(), mBackGroundResId);
         }
         
         if(mSelectorResId != -1) {
        	 mSelectorBitmap = BitmapFactory.decodeResource(getResources(), mSelectorResId);
         }
         mPaint.setStyle( Paint.Style.FILL );   
         mPaint.setColor( 0xFFFFFF00 );   
         mPaint.setAntiAlias(true);   
            
         mActiveTextPaint.setTextAlign( Align.CENTER );   
         mActiveTextPaint.setTextSize(textSize);   
         mActiveTextPaint.setColor( 0xFFFFFFFF );   
         mActiveTextPaint.setAntiAlias(true);   
            
         mInactiveTextPaint.setTextAlign( Align.CENTER );   
         mInactiveTextPaint.setTextSize(textSize);   
         mInactiveTextPaint.setColor( 0xFFFFFFFF );   
         mInactiveTextPaint.setAntiAlias(true);   
         mActiveTab = 0;  
         ta.recycle();
     }   

     @Override   
     public void onDraw( Canvas canvas )   
     {   
         super.onDraw( canvas );   
            
         Rect r = new Rect( );   
         this.getDrawingRect( r );   
            
         int singleTabWidth = r.right / ( mTabMembers.size( ) != 0 ? mTabMembers.size( ) : 1 );   
         if(mBgBitmap != null && !mBgBitmap.isRecycled()) {
        	 Paint paint = new Paint();
        	 Rect srcRect = new Rect(0, 0, mBgBitmap.getWidth(), mBgBitmap.getHeight());
        	 canvas.drawBitmap(mBgBitmap, srcRect, r, paint);
         }
           
         mPaint.setColor( 0xFF434343 );   
//         canvas.drawLine( r.left+2, r.top + 2, r.right-2, r.top + 2, mPaint );   
            
         for( int i = 0; i < mTabMembers.size( ); i++ )   
         {   
             TabMember tabMember = mTabMembers.get( i );
             if(!mtabImages.containsKey(i))
             {
            	 Bitmap icon = BitmapFactory.decodeResource( getResources( ), tabMember.getIconResourceId( ) );  
                 Bitmap iconSelected = BitmapFactory.decodeResource( getResources( ), tabMember.getIconSelectedId( ) );
                 
                 mtabImages.put(i, icon);
                 mtabSelectedImages.put(i, iconSelected);
             }
                
             int tabImgX = singleTabWidth * i + ( singleTabWidth / 2 - mtabImages.get(i).getWidth( ) / 2 );   
                
             if( mActiveTab == i )   
             {          
                 if(mSelectorBitmap != null && !mSelectorBitmap.isRecycled()) {
                	 Paint paint = new Paint();
                	 canvas.drawBitmap(mSelectorBitmap, r.left + singleTabWidth * i	+ ((singleTabWidth - mSelectorBitmap.getWidth()) / 2) , r.top + mSelectorPadding, paint);
                 }
                 canvas.drawBitmap( mtabSelectedImages.get(i), tabImgX , r.top + imagePaddingTop, null );
                 canvas.drawText( tabMember.getText( ),    
                         singleTabWidth * i + ( singleTabWidth / 2), r.bottom - textPaddingButtom, mActiveTextPaint );   
             } else   
             {   
                 canvas.drawBitmap( mtabImages.get(i), tabImgX , r.top + imagePaddingTop, null );
                 canvas.drawText( tabMember.getText( ),   
                         singleTabWidth * i + ( singleTabWidth / 2), r.bottom - textPaddingButtom, mInactiveTextPaint );   
             }   
         }   
     }   
    
     @Override   
     public boolean onTouchEvent( MotionEvent motionEvent )   
     {   
         Rect r = new Rect( );   
         this.getDrawingRect( r );              
         float singleTabWidth = r.right / ( mTabMembers.size( ) != 0 ? mTabMembers.size( ) : 1 );   
            
         int pressedTab = (int) ( ( motionEvent.getX( ) / singleTabWidth ) - ( motionEvent.getX( ) / singleTabWidth ) % 1 );   
            
         // lxr add 2012.07.23
         // 取消多次点击.
         if(pressedTab == mActiveTab)
        	 return super.onTouchEvent(motionEvent);
         
         mActiveTab = pressedTab;   
            
         if( this.mOnTabClickListener != null)   
         {   
             this.mOnTabClickListener.onTabClick( mTabMembers.get( pressedTab ).getId( ) );             
         }   
            
         this.invalidate();   
            
         return super.onTouchEvent( motionEvent );   
     }
     public void setActiveTabId(int tabId) 
      {
        mActiveTab = tabId;
       }
 
     public void setTabMember( int id, String text, int iconResourceId ,int iconSelectedId)
      {
    	 TabMember tabMember = new TabMember(id,text,iconResourceId,iconSelectedId);
    	 mTabMembers.add( tabMember );
     }
     public void addTabMember( int id, String text, int iconResourceId ,int iconSelectedId)
      {
    	 TabMember tabMember = new TabMember(id,text,iconResourceId,iconSelectedId);
    	 mTabMembers.add( tabMember );
     }
    public void addTabMember( TabMember tabMember )   
     {   
         mTabMembers.add( tabMember );   
     }   
        
     public void setOnTabClickListener( OnTabClickListener onTabClickListener )   
     {   
         mOnTabClickListener = onTabClickListener;   
     }   
        
     public static class TabMember  
     {   
         protected int       mId;   
         protected String    mText;   
         protected int       mIconResourceId; 
         protected int       mIconSelectedId;
            
         TabMember( int Id, String Text, int iconResourceId ,int iconSelectedId)   
         {   
             mId = Id;   
             mIconResourceId = iconResourceId;   
             mText = Text;   
             mIconSelectedId = iconSelectedId;
         }   
            
         public int getId( )   
         {   
             return mId;   
         }   
            
         public String getText( )   
         {   
             return mText;   
         }   
            
         public int getIconResourceId( )   
         {   
             return mIconResourceId;   
         }   
         public int getIconSelectedId( )   
         {   
             return mIconSelectedId;   
         }        
         public void setText( String Text )   
         {   
             mText = Text;   
         }   
            
         public void setIconResourceId( int iconResourceId )   
         {   
             mIconResourceId = iconResourceId;   
         }  
         public void setIconSelectedId( int iconSelectedId )   
         {   
             mIconSelectedId = iconSelectedId;   
         }  
     }   
        
     public static interface OnTabClickListener   
     {   
         public abstract void onTabClick( int tabId );   
     }   
}
