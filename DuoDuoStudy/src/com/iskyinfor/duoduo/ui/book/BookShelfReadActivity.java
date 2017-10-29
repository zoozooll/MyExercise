package com.iskyinfor.duoduo.ui.book;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.iskinfor.servicedata.study.service.IManagerStudyOperater0100030001;
import com.iskinfor.servicedata.study.serviceimpl.ManagerStudyOperater010003000Impl;
import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;

public class BookShelfReadActivity extends Activity {

	private ImageView listMenu,img_results;
	private GridView popupGridView = null;
    private LinearLayout layout = null;
    private PopupWindow popupWindow = null;
    private TextView selectTilte/*,deleteTitle*/;
    private IManagerStudyOperater0100030001 bookshelfSubData = new ManagerStudyOperater010003000Impl();
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_read);
		
		SetText();
		TextView bookshelfread_topml=(TextView)findViewById(R.id.bookshelfread_topml);
		TextView bookshelfread_topss=(TextView)findViewById(R.id.bookshelfread_topss);
		bookshelfread_topss.setOnClickListener(new bookshelfread_topssListeners());
		bookshelfread_topml.setOnClickListener(new BookShelfReadDirListeners());

		TextView bookshelfread_topbj=(TextView)findViewById(R.id.bookshelfread_topbj);
		bookshelfread_topbj.setOnClickListener(new bookshelfread_topbjListeners());
		TextView bookshelfread_topsq=(TextView)findViewById(R.id.bookshelfread_topsq);
		bookshelfread_topsq.setOnClickListener(new bookshelfread_topsqListeners());
		listMenu = (ImageView) findViewById(R.id.bookshelfread_bottomcd);
		listMenu.setOnClickListener(new OnClickListener()
		{   public void onClick(View v) 
			{showPopupMenu(); }
		});
		img_results=(ImageView)findViewById(R.id.bookshelfread_bottomfh);
		img_results.setOnClickListener(new ImgResult());   
	}
	
	
	public void SetText()
	{
		TextView txt_Readzw=(TextView)findViewById(R.id.bookshelf_Readzw);
		txt_Readzw.setText("" +
				"A: My teacher's so cruel... I can't believe how much homework\n" +
				"I have!\n" +
				"B: Tell me about it! I barely even have time to sleep anymore.\n" +
				"我的老师太过分了……难以相信居然布置了这么多作业!\n" +
				"就是说嘛!我都快没时间休息了.\n" +
				"\n" +
				"A:I get so annoyed with Steve!\n" +
				"B:Tell me about it. He drives me crazy.\n" +
				"史蒂夫把我烦透了!\n" +
				"你算说对了.他快把我逼疯了.\n" +
				"\n" +
				"You’re telling me!: 说的就是,太对了.\n" +
				"\n" +
				"A: Jessica is really moody recently.\n" +
				"Jessica最近很喜怒无常..\n" +
				"B: You��re telling me! Yesterday she started shouting at me for\n" +
				" no reason!\n" +
				"说得就是!昨天她无缘无故对我大喊大叫!\n" +
				"\n" +
				"Amen to that!: ͬ��!��˵�Ķ�!\n" +
				"A: Jessica is really moody recently.\n" +
				"Jessica����ϲŭ�޳�.\n" +
				"B: You��re telling me! Yesterday she started shouting at me for\n" +
				" no reason!\n" +
				"\n");
	}
    private final class ImgResult implements View.OnClickListener{
    	public void onClick(View v)
    	{
    		finish();
    	}
    }
    private final class BookShelfReadDirListeners implements View.OnClickListener{
    	public void onClick(View v)
    	{
    		Intent noteintent=new Intent(BookShelfReadActivity.this,BookShelfReadDirActivity.class);
	    	 startActivity(noteintent);
    	}
    }
    private final class bookshelfread_topssListeners implements View.OnClickListener{
    	public void onClick(View v)
    	{
    		final EditText view = new EditText(BookShelfReadActivity.this);
			new AlertDialog.Builder(BookShelfReadActivity.this).setTitle("请输入搜索内容").setIcon(
					android.R.drawable.ic_dialog_info).setView(
							view).setPositiveButton("确定",new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// TODO Auto-generated method stub
							String s=  view.getText().toString();
							Toast.makeText(BookShelfReadActivity.this,s ,
									Toast.LENGTH_LONG).show();
						}
					}
					).setNegativeButton("取消", null).show();

    	}
    }
    private final class bookshelfread_topbjListeners implements View.OnClickListener{
    	public void onClick(View v)
    	{
          final EditText view=new EditText(BookShelfReadActivity.this);
          new AlertDialog.Builder(BookShelfReadActivity.this).setTitle("请输入笔记").setIcon(android.R.drawable.ic_dialog_alert)
          .setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				String s=  view.getText().toString();
				try {
					boolean notebool = bookshelfSubData.addNotes("0002", s);
					if(notebool==true)
					{Toast.makeText(BookShelfReadActivity.this,"添加成功",Toast.LENGTH_LONG).show();}
					else
					{Toast.makeText(BookShelfReadActivity.this,"添加失败",Toast.LENGTH_LONG).show();}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).setNegativeButton("取消", null).show();
    	}
     }
    private final class bookshelfread_topsqListeners implements View.OnClickListener{
    	public void onClick(View v)
    	{
    		final EditText view =new EditText(BookShelfReadActivity.this);
    		new AlertDialog.Builder(BookShelfReadActivity.this).setTitle("请输入书签").setIcon(android.R.drawable.ic_dialog_alert)
    		.setView(view).setPositiveButton("确定", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					String s=  view.getText().toString();
					boolean markbool;
					try {
						markbool = bookshelfSubData.addBookMark("0002", s);
						if(markbool==true)
						{Toast.makeText(BookShelfReadActivity.this,"添加成功",Toast.LENGTH_LONG).show();}
						else
						{Toast.makeText(BookShelfReadActivity.this,"添加失败",Toast.LENGTH_LONG).show();}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}).setNegativeButton("取消", null).show();    		
    	}
    } 
	// ��ʼ��Popup Menu�˵�
	protected void initPopupMenu()
	{
		popupGridView = new GridView(BookShelfReadActivity.this);
		popupGridView.setLayoutParams(new LayoutParams(
		LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT));
		popupGridView.setNumColumns(3);
		popupGridView.setStretchMode(GridView.STRETCH_COLUMN_WIDTH);
		popupGridView.setVerticalSpacing(1);
		popupGridView.setHorizontalSpacing(1);
		popupGridView.setGravity(Gravity.CENTER);
		BookShelfMenuTitleAdapter adapter = new BookShelfMenuTitleAdapter(this, 
		new String[] { "编辑书评","查看书签","查看笔记" }, 15, 0xFF000000);
		popupGridView.setAdapter(adapter);
	}
	
	//��ʾPopup Menu�˵�
	protected void showPopupMenu() 
	{
		layout = new LinearLayout(BookShelfReadActivity.this);
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
		
		popupWindow.showAtLocation(findViewById(R.id.bookshelfread_bottomcd),Gravity.BOTTOM, 0, 40);
	}
	
	private final class BookShelfMenu implements View.OnClickListener{
	    	public void onClick(View v){
	    
	    	if(v.getId()==0)//编辑书评
	    	{
	    		Intent noteintent=new Intent(BookShelfReadActivity.this,BookshelfReadCommentActivity.class);
		    	 startActivity(noteintent);
	    	}
	    	if(v.getId()==1)//查看书签
	    	{
	    		Intent noteintent=new Intent(BookShelfReadActivity.this,bookshelfReadLabelActivity.class);
		    	 startActivity(noteintent);
	    	}
	    	if(v.getId()==2)//查看笔记
	    	{Intent noteintent=new Intent(BookShelfReadActivity.this,bookshelfReadNotesActivity.class);
	    	 startActivity(noteintent); }
	    }
	}
	
	public class BookShelfMenuTitleAdapter extends BaseAdapter {

		private Context mContext;
		private int fontColor;
		private TextView[] title;
		
		public BookShelfMenuTitleAdapter(Context context, String[] titles, int fontSize,int color) {
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


	/**
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
				popupWindow.showAtLocation(findViewById(R.id.bookshelfread_bottomcd),Gravity.BOTTOM, 0, 40);
			}
		}
		return false;// ����Ϊtrue ����ʾϵͳmenu
	}
	
	public void onOptionsMenuClosed(Menu menu) {
		Toast.makeText(this, "ѡ��˵��ر���", Toast.LENGTH_LONG).show();
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
