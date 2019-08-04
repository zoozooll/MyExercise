package com.mogoo.market.adapter;

import java.util.ArrayList;

import com.mogoo.market.model.ShotItemViewFctory;

import android.content.Context; 
import android.util.Log;
import android.view.View; 
import android.view.ViewGroup; 
import android.widget.BaseAdapter; 
import android.widget.Gallery;
import android.widget.ImageView; 
import android.widget.TableLayout.LayoutParams;

public class ImageShotAdapter extends BaseAdapter{ 


private Context mContext; 
private ArrayList<String> shotUrlList;
private int mWidth;
private int mHeight;

 public ImageShotAdapter(Context c,ArrayList<String> shotUrlList,int width,int height){ 
   mContext = c; 
   this.shotUrlList=shotUrlList;
   this.mWidth=width;
   this.mHeight=height;
 } 

 public int getCount() { 
   // TODO Auto-generated method stub 
	 Log.d("####","++++++shotUrlList.size==="+shotUrlList.size());
   return shotUrlList.size(); 
 } 
 
 public Object getItem(int position) { 
   // TODO Auto-generated method stub 
   return position; 
 } 
 
 public long getItemId(int position) { 
   // TODO Auto-generated method stub 
   return position; 
 } 

 public View getView(int position, View convertView, ViewGroup parent) { 

   ImageView imageView=null;
   Log.d("#####","+++++position=="+position);
   if(convertView==null){
	   imageView= (ImageView) ShotItemViewFctory.getImagesView(shotUrlList.get(position),mContext);
   }else{
	   imageView=(ImageView)convertView;
   }
   
   imageView.setLayoutParams(new Gallery.LayoutParams((mWidth/3-5),LayoutParams.FILL_PARENT));
   imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); 
   return imageView; 
 } 

} 

