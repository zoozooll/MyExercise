package com.iskyinfor.duoduo.ui.book;

import com.iskyinfor.duoduo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BookShelfGiftMygiftChild implements OnClickListener{
	public View menuView;
    //赠送
	public TextView bookshelfgift_mygift;
	//被赠送
	TextView bookshelfgift_mygiftby;
	public Activity mcontext;
	LayoutInflater inflater;
	
	public BookShelfGiftMygiftChild(Activity context){
		inflater=LayoutInflater.from(context);
		mcontext=context;
	}
	
	public View onCreateView(){
		menuView=inflater.inflate(R.layout.bookshelf_giftchild, null);
		intitMenuView();
		return menuView;
	}
	public void intitMenuView(){
		//赠送 被赠送
		bookshelfgift_mygift=(TextView) menuView.findViewById(R.id.bookshelfgift_mygift);
		bookshelfgift_mygiftby=(TextView) menuView.findViewById(R.id.bookshelfgift_mygiftby);
		bookshelfgift_mygift.setOnClickListener(this);
		bookshelfgift_mygiftby.setOnClickListener(this);
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO 类别点击
		
		switch (v.getId()) {
		case R.id.bookshelfgift_mygift:  
//			new BookShelfGiftActivity().listviewGiftType();
			((BookShelfGiftActivity)mcontext).listviewGiftType();
			break;
		case R.id.bookshelfgift_mygiftby:
			
			((BookShelfGiftActivity)mcontext).listviewGiftByType();
			break;
		
		default:
			break;
		}
	}

}
