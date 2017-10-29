package com.iskyinfor.duoduo.ui.book;

import com.iskyinfor.duoduo.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class BookShelfSortView implements OnClickListener{
	private View menuView;
    //全部类别按钮
	private TextView allSort;
	//书名类别
	TextView bookName;
    //作者类别
	TextView author;
	//分类
	TextView sortType;
	private Context mcontext;
	LayoutInflater inflater;
	public BookShelfSortView(Context context){
		inflater=LayoutInflater.from(context);
		mcontext=context;
	}
	
	public View onCreateView(){
		menuView=inflater.inflate(R.layout.bookshelf_allchild, null);
		intitMenuView();
		return menuView;
	}
	private void intitMenuView(){
		
	 	 allSort=(TextView) menuView.findViewById(R.id.bookshelfall_allProject);
		 /**
			 * 书名类别
			 */
			 bookName=(TextView) menuView.findViewById(R.id.bookshelfall_chinese);
			/**
			 * 作者类别
			 */
			 author=(TextView) menuView.findViewById(R.id.bookshelfall_math);
			/**
			 * 分类
			 */
			 sortType=(TextView) menuView.findViewById(R.id.bookshelfall_english);
			 allSort.setOnClickListener(this);
				bookName.setOnClickListener(this);
				author.setOnClickListener(this);
				sortType.setOnClickListener(this);
	}
	
	@Override
	public void onClick(View v) {
		// TODO 类别点击
		
//		switch (v.getId()) {
//		case R.id.bookshelfall_allProject:
//			  ((BookShelfActivity)mcontext).popDismiss(allSort.getText().toString());
//			break;
//		case R.id.bookshelfall_chinese:
//			  ((BookShelfActivity)mcontext).popDismiss(bookName.getText().toString());
//			break;
//		case R.id.bookshelfall_math:
//			  ((BookShelfActivity)mcontext).popDismiss(author.getText().toString());
//			break;
//		case R.id.bookshelfall_english:
//			  ((BookShelfActivity)mcontext).popDismiss(sortType.getText().toString());
//			break;
//		default:
//			break;
//		}
	}

}
