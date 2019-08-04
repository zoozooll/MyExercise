package com.mogoo.market.ui;

import java.util.ArrayList;

import com.mogoo.market.adapter.ImageBrowseAdapter;
import com.mogoo.market.widget.TitleBar;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View; 
import android.widget.Gallery; 

import com.mogoo.market.R;

public class GalleryShotActivity extends Activity {
	private ArrayList<String> shotListData=null;
	private int shotpositon;
	protected TitleBar titlebar = null;
    /** Called when the activity is first created. */ 
    @Override 
    public void onCreate(Bundle savedInstanceState) { 
        super.onCreate(savedInstanceState); 
        Intent shotIntent=this.getIntent();
        shotListData=shotIntent.getStringArrayListExtra("shoturls");
        shotpositon=shotIntent.getIntExtra("shotpositon", 0);
        setContentView(R.layout.gallery_shot); 
        initTitleBar();
        Gallery gallery = (Gallery) findViewById(R.id.view_shot); 
        gallery.setAdapter(new ImageBrowseAdapter(this,shotListData)); 
        gallery.setSelection(shotpositon);
     }
    
    /**
	 * 初始化标题栏
	 */
	private void initTitleBar() 
	{
		titlebar = (TitleBar) findViewById(R.id.idTitlebar);
		titlebar.midTextView.setText(getResources().getString(R.string.soft_shot));
		if (titlebar != null) 
		{
			titlebar.setLeftBtnText(R.string.titlebar_back);

			titlebar.leftBtn.setOnClickListener(new View.OnClickListener() 
			{
				public void onClick(View v) 
				{
					finish();
				}
			});
		}
	}
}

 
