package com.mogoo.market.adapter;

import java.util.ArrayList;

import com.mogoo.market.model.ShotItemViewFctory;

import android.content.Context; 
import android.view.View; 
import android.view.ViewGroup; 
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter; 
import android.widget.Gallery; 
import android.widget.ImageView; 

public class ImageBrowseAdapter extends BaseAdapter{ 


private Context mContext; 
private ArrayList<String> shotUrlList;
 
 public ImageBrowseAdapter(Context c,ArrayList<String> shotUrlList){ 
   mContext = c; 
   this.shotUrlList=shotUrlList;
 } 

 public int getCount() { 
   // TODO Auto-generated method stub 
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

    ImageView imageView;
    if(convertView==null){
    	 imageView= (ImageView) ShotItemViewFctory.getImagesView(shotUrlList.get(position),mContext);
    }
    else{
 	   imageView=(ImageView)convertView;
    }
    imageView.setLayoutParams(new Gallery.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT)); 
    imageView.setScaleType(ImageView.ScaleType.FIT_CENTER); 
   return imageView; 
 } 

} 

