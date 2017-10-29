package com.dvr.android.dvr.msetting;

import java.util.ArrayList;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class UniversityAdapter extends BaseAdapter
{

    private Context context;
    private ArrayList<University> universityList;

    public UniversityAdapter(Context context, ArrayList<University> universityList)
    {
        this.context = context;
        this.universityList = universityList;
    }

    public int getCount()
    {
        return universityList.size();
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    private int select = 0;

    public void notifyDataSetChanged(int albumId)
    {
        select = albumId;
        super.notifyDataSetChanged();
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView imageView = new ImageView(context);
        /*
         * if (select == position) { University pro = universityList.get(position % universityList.size()); University
         * pro= universityList.get(position); imageView.setImageBitmap(BitmapUtil.createReflectedImage(BitmapUtil
         * .createTxtImage(pro.getName(), 28))); } else {
         */

        /* University pro = universityList.get(position % universityList.size()); */

        University pro = universityList.get(position);
        imageView.setImageBitmap(BitmapUtil.createTxtImage(pro.getName(),25));
       
        /* } */

        return imageView;
    }
}
