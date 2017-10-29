package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.BookShelfReadActivity.BookShelfMenuTitleAdapter;

public class bookshelfReadNotesActivity extends Activity {
	private List<Map<String, Object>> mData;
	ListView list;
	
	private ImageView listMenu,img_results;
	private GridView popupGridView = null;
    private LinearLayout layout = null;
    private PopupWindow popupWindow = null;
    private TextView selectTilte/*,deleteTitle*/;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_read_notes);
		
		list=(ListView)findViewById(R.id.bookshelfreadreadnote_lvbi);
		
		mData = getData();
		MyAdapter adapter = new MyAdapter(this,R.layout.bookshelf_read_notes_child);
        list.setAdapter(adapter);  
        
        listMenu=(ImageView)findViewById(R.id.bookshelfreadnote_bottomcd);
		listMenu.setOnClickListener(new BookshelfReadNotebomcd());
        img_results=(ImageView)findViewById(R.id.bookshelfreadnote_bottomfh);
		img_results.setOnClickListener(new ImgResult());
		
	}
	
	
	boolean notebool=true;
	private final class BookshelfReadNotebomcd implements View.OnClickListener{
		public void onClick(View v)
		{
			showPopupMenu();
			if(notebool==true)
			{
				MyAdapter adapter = new MyAdapter(bookshelfReadNotesActivity.this,R.layout.bookshelf_read_notes_child1);
		        list.setAdapter(adapter);notebool=false;
		        }
			else if(notebool==false)
			{
				
				MyAdapter adapter = new MyAdapter(bookshelfReadNotesActivity.this,R.layout.bookshelf_read_notes_child);
		        list.setAdapter(adapter);notebool=true;}
		}
	}
	 private final class ImgResult implements View.OnClickListener{
	    	public void onClick(View v)
	    	{finish();}
	    }
	
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_notes_child_txtbj", "�持续使用设计   5.1 为何使用");
		map.put("bookshelf_read_notes_child_txtys", "第12页");
		map.put("bookshelf_read_notes_child_txtrq", "2010年12月22日");
		list.add(map);

		return list;
	}

	public final class ViewHolder{
		public TextView bookshelf_read_notes_child_txtbj;
		public TextView bookshelf_read_notes_child_txtys;
		public TextView bookshelf_read_notes_child_txtrq;
	}
	
	public class MyAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		private int Notetype;
		
		public MyAdapter(Context context,int notetype){
			this.mInflater = LayoutInflater.from(context);
			Notetype=notetype;
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
				int note= Notetype;
				convertView = mInflater.inflate(note, null);
				holder.bookshelf_read_notes_child_txtbj = (TextView)convertView.findViewById(R.id.bookshelf_read_notes_child_txtbj);
				holder.bookshelf_read_notes_child_txtys = (TextView)convertView.findViewById(R.id.bookshelf_read_notes_child_txtys);
				holder.bookshelf_read_notes_child_txtrq = (TextView)convertView.findViewById(R.id.bookshelf_read_notes_child_txtrq);
				
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.bookshelf_read_notes_child_txtbj.setText((String)mData.get(position).get("bookshelf_read_notes_child_txtbj"));
			holder.bookshelf_read_notes_child_txtys.setText((String)mData.get(position).get("bookshelf_read_notes_child_txtys"));
			holder.bookshelf_read_notes_child_txtrq.setText((String)mData.get(position).get("bookshelf_read_notes_child_txtrq"));
			
			holder.bookshelf_read_notes_child_txtbj.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
//					showInfo();	
					Toast.makeText(bookshelfReadNotesActivity.this, "�ʼ�", 1).show();
				}
			});
			
			return convertView;
		}
		
	}

	//////////////////  Menu�˵�
	// ��ʼ��Popup Menu�˵�
	protected void initPopupMenu()
	{
		popupGridView = new GridView(bookshelfReadNotesActivity.this);
		popupGridView.setLayoutParams(new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(2);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		BookShelfReadNoteMenuTitleAdapter adapter = new BookShelfReadNoteMenuTitleAdapter(this, 
		new String[] { "ȫѡ","ɾ��"}, 15, 0xFF000000);
		popupGridView.setAdapter(adapter);
	}
	
	//��ʾPopup Menu�˵�
	protected void showPopupMenu() 
	{
		layout = new LinearLayout(bookshelfReadNotesActivity.this);
		layout.setOrientation(LinearLayout.HORIZONTAL);
		initPopupMenu();
		layout.addView(popupGridView);
		
		popupWindow = new PopupWindow(layout,LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
		popupWindow.setBackgroundDrawable(getResources().getDrawable(
		R.drawable.bookshelf_indextopback));	        // ����menu�˵�����
		popupWindow.setFocusable(true);// menu�˵���ý��� ���û�л�ý���menu�˵��еĿؼ��¼��޷���Ӧ
		popupWindow.update();
		
		// ����Ĭ����
		selectTilte = (TextView) popupGridView.getItemAtPosition(0);
		selectTilte.setBackgroundColor(0x00);
		
		popupWindow.showAtLocation(findViewById(R.id.bookshelfreadnote_bottomcd),Gravity.BOTTOM, 0, 40);
	}
	private final class BookShelfMenu implements View.OnClickListener{
	    	public void onClick(View v){
	    
	    	if(v.getId()==0)//
	    	{
	    		
	    	}
	    	if(v.getId()==1)//
	    	{}
	    }
	}
	public class BookShelfReadNoteMenuTitleAdapter extends BaseAdapter {

		private Context mContext;
		private int fontColor;
		private TextView[] title;
		
		public BookShelfReadNoteMenuTitleAdapter(Context context, String[] titles, int fontSize,int color) {
			this.mContext = context;
			this.fontColor = color;
			this.title = new TextView[titles.length];
			
			for (int i = 0; i < titles.length; i++)
			{
				title[i] = new TextView(mContext);
				title[i].setText(titles[i]);
				title[i].setId(i);
				title[i].setTextSize(fontSize);
				title[i].setTextColor(fontColor);
				title[i].setGravity(Gravity.CENTER);
				title[i].setPadding(10, 10, 10, 10);
				title[i].setOnClickListener(new BookShelfMenu());
				//title[i].setBackgroundColor(R.drawable.bookshelf_readss);
//				title[i].setBackgroundResource(R.drawable.green_btn);
			}
		}
		
		public int getCount() {
			// TODO Auto-generated method stub
			return title.length;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return title[position];
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return title[position].getId();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			View view;
			if (convertView == null)
			{
				view = title[position];
			} else {
				view = convertView;
			}
			return view;
		}

	}
     /*
	 * ����MENU
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add("menu");// ���봴��һ��
		return super.onCreateOptionsMenu(menu);
	}

	/**
	 * ����MENU
	 */
	@Override
	public boolean onMenuOpened(int featureId, Menu menu) {
		if (popupWindow != null) 
		{
			if (popupWindow.isShowing())  
				popupWindow.dismiss();
			else 
			{
				popupWindow.showAtLocation(findViewById(R.id.bookshelfreadnote_bottomcd),Gravity.BOTTOM, 0, 40);
			}
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}
	
	public void onOptionsMenuClosed(Menu menu) {}
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
