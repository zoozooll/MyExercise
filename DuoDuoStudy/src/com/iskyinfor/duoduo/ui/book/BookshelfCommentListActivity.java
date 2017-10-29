package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.BookShelfReadActivity.BookShelfMenuTitleAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

public class BookshelfCommentListActivity extends Activity {
	private ImageView listMenu,img_results;
	private GridView popupGridView = null;
    private LinearLayout layout = null;
    private PopupWindow popupWindow = null;
    private TextView selectTilte/*,deleteTitle*/;
	private TextView textView;
	private List<Map<String, Object>> mData;
	private int helpId;
    boolean showbool=true;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_commentlist);
		
		final String[] data = {"",""}; 
		final String[] data2 = {"",""}; 
		mData = getData();
		final ListView l = (ListView)findViewById(R.id.bookshelfcommentlist_lv);
//		ListAdapter list = new ListAdapter(this,R.layout.bookshelf_commentlist_listviewchild, data);
//		l.setAdapter(list); 
		MyAdapter adapter = new MyAdapter(this);
        l.setAdapter(adapter);  
		
		l.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
				if(showbool==true)
				{
//					textView.setVisibility(View.VISIBLE);
					if(textView==null){
						textView = (TextView)arg1.findViewById(R.id.bookshelfcommentlist_all);
						helpId = arg2;
					}
					else if(textView!=arg1){
						textView.setText("");
						textView = (TextView)arg1.findViewById(R.id.bookshelfcommentlist_all);
						helpId = arg2;
					}
					
					TextView textView = (TextView) arg1.findViewById(R.id.bookshelfcommentlist_all);
					textView.setText("对于小学升学复习备考具有极强的针对性和指导性"
							+""+"对我的学习很有帮助，值得购买");
					showbool=false;
				}
				else if(showbool==false)
				{
					TextView textView = (TextView)arg1.findViewById(R.id.bookshelfcommentlist_all);
					textView.setText("查看全部");
//					textView.setVisibility(View.GONE);
					showbool=true;
				}
				
			}
		});
		
		listMenu = (ImageView) findViewById(R.id.bookshelfcommentlist_bomcd);
		listMenu.setOnClickListener(new OnClickListener()
		{
			public void onClick(View v) 
			{
//				showPopupMenu(); 
			}
		});
		img_results=(ImageView)findViewById(R.id.bookshelfcommentlist_bomfh);
		img_results.setOnClickListener(new ImgResult());
	}
	 private final class ImgResult implements View.OnClickListener{
	    	public void onClick(View v)
	    	{finish();}
	    }
	
		protected void initPopupMenu()
		{
			popupGridView = new GridView(BookshelfCommentListActivity.this);
			popupGridView.setLayoutParams(new LayoutParams(
			LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
			popupGridView.setNumColumns(2);
			popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
			popupGridView.setVerticalSpacing(1);
			popupGridView.setHorizontalSpacing(1);
			popupGridView.setGravity(Gravity.CENTER);
//			BookShelfMenuTitleAdapter adapter = new BookShelfMenuTitleAdapter(BookshelfCommentListActivity.this, 
//			new String[] { "�༭����","�鿴��ǩ","�鿴�ʼ�" }, 15, 0xFF000000);
//			popupGridView.setAdapter(adapter);
		}
		
		protected void showPopupMenu() 
		{
			layout = new LinearLayout(BookshelfCommentListActivity.this);
			layout.setOrientation(LinearLayout.HORIZONTAL);
			initPopupMenu();
			layout.addView(popupGridView);
			
			popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
			popupWindow.setBackgroundDrawable(getResources().getDrawable(
			R.drawable.bookshelf_indextopback));	       
			popupWindow.setFocusable(true);
			popupWindow.update();
			
			selectTilte = (TextView) popupGridView.getItemAtPosition(0);
			selectTilte.setBackgroundColor(0x00);
			
			popupWindow.showAtLocation(findViewById(R.id.bookshelfread_bottomcd),Gravity.BOTTOM, 0, 40);
		}
	
	public class ListAdapter extends ArrayAdapter {

		private int[] colors = new int[] { 0x30FF0000, 0x300000FF };   

		
		public ListAdapter(Context context, int textViewResourceId,
				Object[] objects) {
			super(context, textViewResourceId, objects);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TextView view = (TextView)super.getView(position, convertView, parent);    
			int colorPos = position % colors.length;   
			view.setMinimumHeight(30);
			view.setMinimumWidth(320);
			view.setTextColor(Color.BLACK);
			view.setTextSize(12);
			if(colorPos==1)   
				view.setBackgroundColor(Color.WHITE);    
			else  
				view.setBackgroundColor(Color.LTGRAY);    
			return view;     
		}
	}
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bookcommentlistname", "评 《小学六年级语文阅读》");
		map.put("img", R.drawable.bookshelflommentlist_img);
		list.add(map);

		map = new HashMap<String, Object>();
		map.put("bookcommentlistname", "评 《小学六年级语文阅读》");
		map.put("img", R.drawable.bookshelflommentlist_img);
		list.add(map);
		
		map=new HashMap<String,Object>();
		map.put("bookcommentlistname", "评 《小学六年级语文阅读》");
		map.put("img", R.drawable.bookshelflommentlist_img);
		list.add(map);
		
		return list;
	}

	public final class ViewHolder{
		public ImageView img;
		public TextView bookcomment;
		public TextView bookshelfcommentlist_all;
	}
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		
		public MyAdapter(Context context){
			this.mInflater = LayoutInflater.from(context);
		}
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			
			ViewHolder holder = null;
			if (convertView == null) {
				
				holder=new ViewHolder();  
				
				convertView = mInflater.inflate(R.layout.bookshelf_commentlist_listviewchild, null);
				holder.img = (ImageView)convertView.findViewById(R.id.bookshelfcommentlist_img);
				holder.bookcomment = (TextView)convertView.findViewById(R.id.bookshelfcommentlist_name);
				holder.bookshelfcommentlist_all=(TextView)convertView.findViewById(R.id.bookshelfcommentlist_name);
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.img.setBackgroundResource((Integer)mData.get(position).get("img"));
			holder.bookcomment.setText((String)mData.get(position).get("bookcommentlistname"));
            holder.bookshelfcommentlist_all.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
									
				}
			});
			return convertView;
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
