package com.iskyinfor.duoduo.ui.book;

import com.iskyinfor.duoduo.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BookShelfRecommendMyrecommendChild implements OnClickListener{
	public View menuView;
    //赠送
	public TextView bookshelfrecommend_myrecommend;
	//被赠送
	TextView bookshelfrecommend_myrecommendby;
	public Activity mcontext;
	LayoutInflater inflater;
	
	public BookShelfRecommendMyrecommendChild(Activity context){
		inflater=LayoutInflater.from(context);
		mcontext=context;
	}
	
	public View onCreateView(){
		menuView=inflater.inflate(R.layout.bookshelf_recommendchild, null);
		intitMenuView();
		return menuView;
	}
	public void intitMenuView(){
		//赠送 被赠送
		bookshelfrecommend_myrecommend=(TextView) menuView.findViewById(R.id.bookshelfrecommend_myrecommend);
		bookshelfrecommend_myrecommendby=(TextView) menuView.findViewById(R.id.bookshelfrecommend_myrecommendby);
		bookshelfrecommend_myrecommend.setOnClickListener(this);
		bookshelfrecommend_myrecommendby.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 类别点击
		
		switch (v.getId()) {
		case R.id.bookshelfrecommend_myrecommend: 
			
			//((BookShelfGiftActivity)mcontext).listviewGiftType();
//			new BookShelfGiftActivity().listviewGiftType();
			((BookShelfRecommendActivity)mcontext).listviewGiftType();
			break;
		case R.id.bookshelfrecommend_myrecommendby:
			
			((BookShelfRecommendActivity)mcontext).listviewGiftByType();
			break;
		
		default:
			break;
		}
	}

}
