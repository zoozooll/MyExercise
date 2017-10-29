package com.iskyinfor.duoduo.ui.book;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;
import com.iskinfor.servicedata.study.serviceimpl.ManagerStudyOperater010003000Impl;
import com.iskyinfor.duoduo.R;

import com.iskyinfor.duoduo.ui.UiHelp;

import com.iskyinfor.duoduo.ui.IndexActivity;


public class BookshelfReadCommentActivity extends Activity {
	private IManagerStudyOperater0100030001 bookshelfSubData = new ManagerStudyOperater010003000Impl();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_raed_comment);
		ImageView bookshelfcomment_result=(ImageView)findViewById(R.id.bookshelfreadcomment_bottomfh);
		bookshelfcomment_result.setOnClickListener(new closeActivity());
		
		TextView bookshelfcomment_add=(TextView)findViewById(R.id.bookshelfcomment_add);
		bookshelfcomment_add.setOnClickListener(new bookshelfcomment_addListener());
	}
	
	 private final class closeActivity implements View.OnClickListener{
		 public void onClick(View v)
		 {finish();}
	 }
    
	 private final class bookshelfcomment_addListener implements View.OnClickListener{
		 public void onClick(View v)
		 {
			 //bookshelfcomment_content
			 TextView bookshelfcomment_content=(TextView)findViewById(R.id.bookshelfcomment_content);
			 if(bookshelfcomment_content.getText().length()>1)
			 {
				 try {
					boolean productbool = bookshelfSubData.assProduct("0002", bookshelfcomment_content.getText().toString());
					
					if(productbool==true)
					{Toast.makeText(BookshelfReadCommentActivity.this, "添加成功", 1).show();}
					else if(productbool==false)
					{Toast.makeText(BookshelfReadCommentActivity.this, "添加失败", 1).show();}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 else{Toast.makeText(BookshelfReadCommentActivity.this, "内容不能为空！", 1);}
		 }
	 }

	 @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			
			if (keyCode == KeyEvent.KEYCODE_SEARCH) {
				UiHelp.turnHome(this);
				return true;
			}else{
				return super.onKeyDown(keyCode, event);
			}
		}

}
