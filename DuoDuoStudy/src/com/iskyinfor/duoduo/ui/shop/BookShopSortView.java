package com.iskyinfor.duoduo.ui.shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import com.iskyinfor.duoduo.R;

public class BookShopSortView implements OnClickListener {

	private View menuView;
	// 全部类别按钮
	private TextView bookshopsort_all;
	private TextView bookshopsort_books;
	private TextView bookshopsort_courseware;
	private TextView bookshopsort_problem;
	private TextView bookshopsort_examination;
	private TextView bookshopsort_group;
	@SuppressWarnings("unused")
	private Context mcontext;
	LayoutInflater inflater;

	public BookShopSortView(Context context) {
		inflater = LayoutInflater.from(context);
		mcontext = context;
	}

	public View onCreateView() {
		menuView = inflater.inflate(R.layout.bookshop_allchild, null);
		intitMenuView();
		return menuView;
	}

	private void intitMenuView() {

		bookshopsort_all = (TextView) menuView
				.findViewById(R.id.bookshopsort_all);
		bookshopsort_all.setOnClickListener(this);
		bookshopsort_books = (TextView) menuView
				.findViewById(R.id.bookshopsort_books);
		bookshopsort_books.setOnClickListener(this);
		bookshopsort_courseware = (TextView) menuView
				.findViewById(R.id.bookshopsort_courseware);
		bookshopsort_courseware.setOnClickListener(this);
		bookshopsort_problem = (TextView) menuView
				.findViewById(R.id.bookshopsort_problem);
		bookshopsort_problem.setOnClickListener(this);
		bookshopsort_examination = (TextView) menuView
				.findViewById(R.id.bookshopsort_examination);
		bookshopsort_examination.setOnClickListener(this);
		bookshopsort_group = (TextView) menuView
				.findViewById(R.id.bookshopsort_group);
		bookshopsort_group.setOnClickListener(this);

	}

	@Override
		public void onClick(View v) {
				/*switch (v.getId()) {
				case R.id.bookshopsort_all:
					((BookstoreActivity) mcontext).BookShopSortAll("全部");
					((BookstoreActivity) mcontext).showCatalog = StaticData.ALL;
					new BookstoreTask((BookstoreActivity) mcontext).execute(
							StaticData.ALL, 1);
					break;
				case R.id.bookshopsort_books:
					((BookstoreActivity) mcontext).BookShopSortAll("书籍");
					((BookstoreActivity) mcontext).showCatalog = StaticData.BOOKS;
					new BookstoreTask(((BookstoreActivity) mcontext)).execute(
							StaticData.BOOKS, 1);
					break;
				case R.id.bookshopsort_courseware:
					((BookstoreActivity) mcontext).BookShopSortAll("课件");
					((BookstoreActivity) mcontext).showCatalog = StaticData.EXAMPAPER;
					new BookstoreTask(((BookstoreActivity) mcontext)).execute(
							StaticData.EXAMPAPER, 1);
					break;
				case R.id.bookshopsort_problem:
					((BookstoreActivity) mcontext).BookShopSortAll("习题");
					((BookstoreActivity) mcontext).showCatalog = StaticData.COURSEWARE;
					new BookstoreTask(((BookstoreActivity) mcontext)).execute(
							StaticData.COURSEWARE, 1);
					break;
				case R.id.bookshopsort_examination:
					((BookstoreActivity) mcontext).BookShopSortAll("考卷");
					((BookstoreActivity) mcontext).showCatalog = StaticData.EXAM;
					new BookstoreTask(((BookstoreActivity) mcontext)).execute(
							StaticData.EXAM, 1);
					break;
				case R.id.bookshopsort_group:
					((BookstoreActivity) mcontext).BookShopSortAll("团购");
					((BookstoreActivity) mcontext).showCatalog = StaticData.GROUPSHOP;
					new BookstoreTask(((BookstoreActivity) mcontext)).execute(
							StaticData.GROUPSHOP, 1);
					break;
				}*/
			}
		}