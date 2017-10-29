package com.iskyinfor.duoduo.ui.book;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;
import com.iskyinfor.duoduo.ui.book.bookshelfReadNotesActivity.MyAdapter;
import com.iskyinfor.duoduo.ui.book.bookshelfReadNotesActivity.ViewHolder;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

public class bookshelfReadLabelActivity extends Activity {

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
		
       setContentView(R.layout.bookshelf_read_label);
		
		list=(ListView)findViewById(R.id.bookshelfread_label_lvsq);
		
		mData = getData();
		MyAdapter adapter = new MyAdapter(this,R.layout.bookshelf_read_label_child);
        list.setAdapter(adapter);  
        
        listMenu=(ImageView)findViewById(R.id.bookshelfreadlabel_bottomcd);
		listMenu.setOnClickListener(new BookshelfReadNotebomcd());
        img_results=(ImageView)findViewById(R.id.bookshelfreadlabel_bottomfh);
		img_results.setOnClickListener(new ImgResult());
		
	}
	
	boolean notebool=true;
	private final class BookshelfReadNotebomcd implements View.OnClickListener{
		public void onClick(View v)
		{
			if(notebool==true)
			{
				MyAdapter adapter = new MyAdapter(bookshelfReadLabelActivity.this,R.layout.bookshelf_read_label_child1);
		        list.setAdapter(adapter);notebool=false;
		        }
			else if(notebool==false)
			{
				
				MyAdapter adapter = new MyAdapter(bookshelfReadLabelActivity.this,R.layout.bookshelf_read_label_child);
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
		
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_label_child_txtsq", "书签书签书签书签");
		map.put("bookshelf_read_label_child_txtym", "第12页");
		list.add(map);
		
		return list;
	}

	public final class ViewHolder{
		public TextView bookshelf_read_label_child_txtsq;
		public TextView bookshelf_read_label_child_txtym;
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
				holder.bookshelf_read_label_child_txtsq = (TextView)convertView.findViewById(R.id.bookshelf_read_label_child_txtsq);
				holder.bookshelf_read_label_child_txtym = (TextView)convertView.findViewById(R.id.bookshelf_read_label_child_txtym);
				
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.bookshelf_read_label_child_txtsq.setText((String)mData.get(position).get("bookshelf_read_label_child_txtsq"));
			holder.bookshelf_read_label_child_txtym.setText((String)mData.get(position).get("bookshelf_read_label_child_txtym"));
			
			holder.bookshelf_read_label_child_txtsq.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
//					showInfo();	
					Toast.makeText(bookshelfReadLabelActivity.this, "书签", 1).show();
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
