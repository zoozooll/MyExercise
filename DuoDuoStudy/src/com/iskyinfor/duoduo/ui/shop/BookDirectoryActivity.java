package com.iskyinfor.duoduo.ui.shop;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.iskyinfor.duoduo.R;
import com.iskyinfor.duoduo.ui.UiHelp;


/**
 * 目录
 * @author MI_Viewer
 *
 */
public class BookDirectoryActivity extends Activity {
	
	private TableLayout table;
	private TableRow row;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookdirectory_activity);
		findView();
		createRow();
		//setValue();
		
	}

	private void setValue() {
		
	}
	private void createRow() {
		for (int i = 0; i < 30 ; i++) {
			TableRow row = new TableRow(this);
			TextView t1 = new TextView(this);
			TextView t2 = new TextView(this);
			TextView t3 = new TextView(this);
			t1.setText("第一课");
			t2.setText(Html.fromHtml("<u>春天来了    </u>"));
			t3.setText("第一页");
			t1.setWidth(60);
			t2.setWidth(200);
			t3.setWidth(60);
			row.addView(t1 );
			row.addView(t2 );
			row.addView(t3 );
			table.addView(row , new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
		}
		
		
		
		
		/*TableLayout tableLayout = (TableLayout) findViewById(R.id.directory_table);
		  // ȫ�����Զ����հ״�
		  tableLayout.setStretchAllColumns(true);
		  for (int i = 0; i < 10; i++) {
		   TableRow tablerow = new TableRow(this);
		   for (int col = 0; col < 8; col++) {
		    // tv������ʾ
		    TextView tv = new TextView(this);
		    tv.setText("(" + col + "," + row + ")");
		    tablerow.addView(tv);
		   }
		   // �½���TableRow��ӵ�TableLayout
		   tableLayout.addView(tablerow, new TableLayout.LayoutParams(FP, WC));
		  }*/
	}

	private void findView() {
		table = (TableLayout) findViewById(R.id.directory_table);
		//row = (TableRow) findViewById(R.id.directory_row);
		findViewById(R.id.duoduo_lesson_back_img).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
				
			}
		});
		findViewById(R.id.duoduo_lesson_list_img).setVisibility(View.GONE);
		
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
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
