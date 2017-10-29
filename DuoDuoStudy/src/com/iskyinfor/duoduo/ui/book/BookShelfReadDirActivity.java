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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class BookShelfReadDirActivity extends Activity {
	ListView list;
	private List<Map<String, Object>> mData;
	ImageView bookshelfreaddir_bottomfh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_raed_directory);
		
        list=(ListView)findViewById(R.id.bookshelfread_dirlv);
        bookshelfreaddir_bottomfh=(ImageView)findViewById(R.id.bookshelfreaddir_bottomfh);
        bookshelfreaddir_bottomfh.setOnClickListener(new bookshelfreaddir_bottomfhListeners());
        
		mData = getData();
		MyAdapter adapter = new MyAdapter(this,R.layout.bookshelf_raed_director_child);
        list.setAdapter(adapter);  
        
        
	}
	private final class bookshelfreaddir_bottomfhListeners implements View.OnClickListener{
    	public void onClick(View v)
    	{finish();}
    }
	private List<Map<String, Object>> getData() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = new HashMap<String, Object>();
		
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		map = new HashMap<String, Object>();
		map.put("bookshelf_read_dir1", "第一课");
		map.put("bookshelf_read_dir2", "春天来了");
		map.put("bookshelf_read_dir3", "第1页");
		list.add(map);
		
		return list;
	}

	public final class ViewHolder{
		public TextView bookshelf_read_dir1;
		public TextView bookshelf_read_dir2;
		public TextView bookshelf_read_dir3;
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
				holder.bookshelf_read_dir1 = (TextView)convertView.findViewById(R.id.bookshelf_read_dir1);
				holder.bookshelf_read_dir2 = (TextView)convertView.findViewById(R.id.bookshelf_read_dir2);
				holder.bookshelf_read_dir3 = (TextView)convertView.findViewById(R.id.bookshelf_read_dir3);
				
				convertView.setTag(holder);
				
			}else {
				
				holder = (ViewHolder)convertView.getTag();
			}
			
			
			holder.bookshelf_read_dir1.setText((String)mData.get(position).get("bookshelf_read_dir1"));
			holder.bookshelf_read_dir2.setText((String)mData.get(position).get("bookshelf_read_dir2"));
			holder.bookshelf_read_dir3.setText((String)mData.get(position).get("bookshelf_read_dir3"));
			
			holder.bookshelf_read_dir3.setOnClickListener(new View.OnClickListener() {
				
				public void onClick(View v) {
//					showInfo();	
					Toast.makeText(BookShelfReadDirActivity.this, "目录", 1).show();
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
